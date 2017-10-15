package com.example.aitor.flickry;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * This listener works with the {@link RecyclerView.Adapter}
 */
public final class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private final RecyclerView.Adapter photoAdapter;
    private final EndlessScrollInterface l;
    private boolean dataPaginationEnabled;

    public interface EndlessScrollInterface {
        void onLoadMore();
    }

    EndlessScrollListener(RecyclerView.Adapter adapter, @Nullable EndlessScrollInterface listener) {
        photoAdapter = adapter;
        l = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // only interested in load more items when scrolling up
        // we put dy >= 0 (with '=') because otherwise the tests fail when trying to scroll from tests
        if (dy >= 0 && photoAdapter.getItemCount() > 0) {
            GridLayoutManager llm = (GridLayoutManager) recyclerView.getLayoutManager();
            int lastPosition = llm.findLastVisibleItemPosition();

            if (dataPaginationEnabled && lastPosition == photoAdapter.getItemCount() - 1) {
                // disable the pagination so that it is not called multiple times. It will be re-enabled when
                // data is retrieved
                dataPaginationEnabled = false;
                // increment the page
                if (l != null) {
                    l.onLoadMore();
                }
            }
            dataPaginationEnabled = (photoAdapter.getItemCount() - 1) > lastPosition;
        }
    }
}
