package com.example.aitor.flickry.api;

import io.reactivex.Single;
import java.util.Map;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

interface FlickrService {
    @GET("services/rest")
    Single<Result<JsonResponse>> getPhotos(@QueryMap Map<String, String> params);
}
