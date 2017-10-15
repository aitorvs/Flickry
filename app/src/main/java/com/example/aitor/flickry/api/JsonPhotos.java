package com.example.aitor.flickry.api;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;

@AutoValue
public abstract class JsonPhotos implements Parcelable {
    public abstract Integer total();
    public abstract Integer pages();
    public abstract Integer page();
    @Json(name = "perpage") public abstract Integer perPage();
    @Json(name = "photo") public abstract List<JsonPhoto> photos();

    public static JsonAdapter<JsonPhotos> jsonAdapter(Moshi moshi) {
        return new AutoValue_JsonPhotos.MoshiJsonAdapter(moshi);
    }
}
