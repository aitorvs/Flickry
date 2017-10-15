package com.example.aitor.flickry.api;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;

@AutoValue
public abstract class JsonResponse implements Parcelable {
    public abstract JsonPhotos photos();
    public abstract String stat();

    public static JsonAdapter<JsonResponse> jsonAdapter(Moshi moshi) {
        return new AutoValue_JsonResponse.MoshiJsonAdapter(moshi);
    }
}
