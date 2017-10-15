package com.example.aitor.flickry;

import android.content.Context;
import com.example.aitor.flickry.api.ApiModule;
import com.example.aitor.flickry.api.PhotoService;
import com.example.aitor.flickry.photos.FlickrData;
import com.example.aitor.flickry.photos.PhotoRequest;
import com.example.aitor.flickry.photos.PhotoStore;
import com.example.aitor.flickry.photos.PhotosModule;
import com.nytimes.android.external.store3.base.impl.Store;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AndroidModule.class,
    PhotosModule.class,
    ApiModule.class,
})
public interface AppObjectGraph {

    PhotoService photoService();
    @PhotoStore Store<Resource<FlickrData>, PhotoRequest> photoStore();

    // Injected into the graph
    void inject(MainActivity target);

    final class Builder {
        private Builder() {
            throw new AssertionError("No instances.");
        }

        public static AppObjectGraph build(final Context context) {
            return DaggerAppObjectGraph.builder()
                .androidModule(new AndroidModule(context.getApplicationContext()))
                .build();
        }
    }

}
