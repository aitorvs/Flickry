package com.example.aitor.flickry.photos;

import android.support.annotation.NonNull;
import com.example.aitor.flickry.Resource;
import com.example.aitor.flickry.api.JsonPhoto;
import com.example.aitor.flickry.api.JsonResponse;
import com.example.aitor.flickry.api.PhotoService;
import com.nytimes.android.external.store3.base.Fetcher;
import com.nytimes.android.external.store3.base.impl.MemoryPolicy;
import com.nytimes.android.external.store3.base.impl.Store;
import com.nytimes.android.external.store3.base.impl.StoreBuilder;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.inject.Singleton;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Dagger module that provides dependencies for the Flickr photo feature
 */
@Module
public final class PhotosModule {

    @Provides
    @Singleton
    @PhotoStore
    Store<Resource<FlickrData>, PhotoRequest> providePhotoStore(PhotoService photoService) {
        return StoreBuilder.<PhotoRequest, Resource<FlickrData>>key()
            .fetcher(fetchFlickrPhotos(photoService))
            .memoryPolicy(MemoryPolicy
                .builder()
                .setExpireAfterWrite(TimeUnit.MINUTES.toSeconds(1))
                .setExpireAfterTimeUnit(TimeUnit.SECONDS)
                .build())
            .open();
    }

    @NonNull
    private Fetcher<Resource<FlickrData>, PhotoRequest> fetchFlickrPhotos(PhotoService photoService) {
        return new Fetcher<Resource<FlickrData>, PhotoRequest>() {
            @Nonnull
            @Override
            public Single<Resource<FlickrData>> fetch(@Nonnull PhotoRequest request) {
                Timber.d("fetch() called with: request = [" + request + "]");
                return photoService.getPhotosOf(request)
                    .map(r -> {
                        if (r.isError()) {
                            // error, don't break the chain and issue error resource
                            return Resource.error(r.error());
                        }

                        ArrayList<Photo> photoList = new ArrayList<>();
                        Response<JsonResponse> response = r.response();
                        if (response != null && response.body() != null) {
                            for (JsonPhoto jsonPhoto : response.body().photos().photos()) {
                                Photo photo = Photo.builder()
                                    .farm(jsonPhoto.farm())
                                    .id(jsonPhoto.id())
                                    .secret(jsonPhoto.secret())
                                    .server(jsonPhoto.server())
                                    .build();
                                photoList.add(photo);
                            }
                        }
                        // Any HTTP error would result in empty list
                        return Resource.success(FlickrData.builder()
                            .page(response.body().photos().page())
                            .photos(photoList)
                            .build());
                    });
            }
        };
    }
}
