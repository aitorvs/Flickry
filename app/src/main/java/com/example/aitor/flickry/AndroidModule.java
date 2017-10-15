package com.example.aitor.flickry;

import android.content.Context;
import android.support.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * This Dagger module will provide dependencies specific to the Android framework
 */
@Module
final class AndroidModule {

    private final Context mContext;

    AndroidModule(@NonNull final Context context) {
        mContext = context.getApplicationContext();
    }

    @Provides
    @Singleton
    @AppContext
    Context provideContext() {
        return mContext;
    }
}
