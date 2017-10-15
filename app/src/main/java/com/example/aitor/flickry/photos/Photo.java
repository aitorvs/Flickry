package com.example.aitor.flickry.photos;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.squareup.phrase.Phrase;

@AutoValue
public abstract class Photo implements Parcelable {
    private static final String URL_TEMPLATE = "http://farm{farm}.static.flickr.com/{server}/{id}_{secret}.jpg";
    abstract Integer farm();
    abstract String server();
    abstract String id();
    abstract String secret();

    @NonNull
    public String url() {
        return Phrase.from(URL_TEMPLATE)
            .put("farm", farm())
            .put("server", server())
            .put("id", id())
            .put("secret", secret())
            .format().toString();
    }

    public static Builder builder() {
        return new AutoValue_Photo.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder server(String value);
        public abstract Builder id(String value);
        public abstract Builder secret(String value);
        public abstract Builder farm(Integer value);

        public abstract Photo build();
    }
}
