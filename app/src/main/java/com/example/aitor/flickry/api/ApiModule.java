package com.example.aitor.flickry.api;

import android.content.Context;
import com.example.aitor.flickry.AppContext;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * This Dagger module will provide dependencies specific to the Android framework
 */
@Module
public final class ApiModule {

    @Provides
    @Singleton
    Moshi provideMoshi() {
        return new Moshi.Builder()
            .add(MoshiJsonAdapterFactory.create())
            .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient().newBuilder()
            .addInterceptor(new StethoInterceptor())
            .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Moshi moshi, OkHttpClient client) {
        return new Retrofit.Builder() //
            .baseUrl("https://api.flickr.com/") // base URL
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi)) //
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())) // default in IO
            .build();
    }

    @Provides
    @Singleton
    FlickrService provideFlickrService(Retrofit retrofit) {
        return retrofit.create(FlickrService.class);
    }

    @Provides
    @Singleton
    Picasso providePicasso(@AppContext Context context) {
        return Picasso.with(context);
    }
}
