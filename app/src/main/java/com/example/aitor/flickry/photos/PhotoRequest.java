package com.example.aitor.flickry.photos;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PhotoRequest implements Parcelable {
    /**
     * Default initial search
     */
    public static final PhotoRequest DEFAULT = PhotoRequest.create("", 1);

    public abstract String search();
    public abstract Integer page();

    public static PhotoRequest create(String search, int page) {
        return new AutoValue_PhotoRequest.Builder().page(page).search(search).build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_PhotoRequest.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder search(String value);
        public abstract Builder page(Integer value);

        public abstract PhotoRequest build();
    }

}
