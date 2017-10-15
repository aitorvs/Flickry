package com.example.aitor.flickry.api;

import com.example.aitor.flickry.photos.PhotoRequest;
import io.reactivex.Single;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.adapter.rxjava2.Result;

@Singleton
public final class PhotoService {
    private final FlickrService service;

    @Inject
    PhotoService(FlickrService service) {
        this.service = service;
    }

    /**
     * Search the photos given the search string
     * @param request {@link PhotoRequest} instance that represent the search request
     * @return Returns an observable of {@link Result} that wraps the JSON objects
     */
    public Single<Result<JsonResponse>> getPhotosOf(PhotoRequest request) {
        // wrap it to single so that we can use Store(s)
        return service.getPhotos(
            FlickrQuery.builder().page(request.page()).text(request.search()).build().toQueryMap());
    }
}
