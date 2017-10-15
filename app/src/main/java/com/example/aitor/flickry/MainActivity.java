package com.example.aitor.flickry;

import android.os.Bundle;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.aitor.flickry.materialsearchview.MaterialSearchView;
import com.example.aitor.flickry.photos.FlickrData;
import com.example.aitor.flickry.photos.PhotoAdapter;
import com.example.aitor.flickry.photos.PhotoRequest;
import com.example.aitor.flickry.photos.PhotoStore;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.nytimes.android.external.store3.base.impl.Store;
import com.squareup.picasso.Picasso;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import javax.inject.Inject;
import timber.log.Timber;

import static com.example.aitor.flickry.ObjectGraphStore.onObjectGraph;

public class MainActivity extends AppCompatActivity {
    private static final String SEARCH_KEY = "search_key";

    // View DI
    @BindView(R.id.images) RecyclerView rvImages;
    @BindView(R.id.search_view) MaterialSearchView searchView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    // DI
    @Inject @PhotoStore Store<Resource<FlickrData>, PhotoRequest> photoStore;
    @Inject PhotoAdapter photoAdapter;
    @Inject Picasso picasso;

    private ArraySet<String> suggestions  = new ArraySet<>(10);
    private PhotoRequest currentSearch = PhotoRequest.DEFAULT;
    private BehaviorRelay<PhotoRequest> searchPhotos = BehaviorRelay.createDefault(PhotoRequest.DEFAULT);
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // view injection
        ButterKnife.bind(this);
        // dependency injection
        onObjectGraph(AppObjectGraph.class).inject(this);

        // check if we have previous search in the state
        if (savedInstanceState != null && savedInstanceState.containsKey(SEARCH_KEY)) {
            Timber.d("onCreate: we have saved search");
            PhotoRequest search = (PhotoRequest) savedInstanceState.get(SEARCH_KEY);
            searchPhotos.accept(search); // if it is null, we let it crash, we control this type
        }

        // init the recycler view
        rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        rvImages.setAdapter(photoAdapter);
        /*
            REQUIREMENT: App must allow endless scrolling.
         */
        rvImages.addOnScrollListener(new EndlessScrollListener(photoAdapter, this::loadNextPage));

        // setup the toolbar with the search view
        setupToolbar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SEARCH_KEY, currentSearch);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        disposable = new CompositeDisposable();
        disposable.add(searchPhotos
            .doOnNext(search -> currentSearch = search) // side effect, update current search only on success
            .flatMap(search -> photoStore.get(search).toObservable()
                .onErrorReturn(Resource::error)
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.loading())
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(r -> {
                if (r.isSuccess()) {
                    photoAdapter.accept(r.data);
                }
            }, OnErrorNotImplementedException::new)
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposable.dispose();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        /*
            REQUIREMENT: App must allow to see history of past searches.
         */
        searchView.setVoiceSearch(false);
        // ellipsize suggestions longer than line
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Timber.d("onQueryTextSubmit() called with: query = [" + query + "]");
                // update the suggestions
                suggestions.add(query);
                searchView.setSuggestions(suggestions.toArray(new String[0]));
                searchPhotos.accept(PhotoRequest.DEFAULT.toBuilder().page(1).search(query).build());
                // do not consume the event so that the view is also closed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /* no-op */
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        // consume event
        return true;
    }

    @Override
    public void onBackPressed() {
        // first check whether we should close the search
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            return;
        }
        // else, check if we should scroll up
        if (shouldScrollUp()) {
            rvImages.smoothScrollToPosition(0);
            return;
        }
        // else, call super
        super.onBackPressed();
    }

    private void loadNextPage() {
        searchPhotos.accept(currentSearch.toBuilder().page(currentSearch.page() + 1).build());
    }

    /**
     * @return Returns <code>true</code> if the first item is not visible in the RV.
     *
     * FIXME should also be done in a better way, smooth scroll when close enough, just scroll when far from first item
     */
    private boolean shouldScrollUp() {
        GridLayoutManager llm = (GridLayoutManager) rvImages.getLayoutManager();
        return (llm.findFirstVisibleItemPosition() > 1);
    }
}
