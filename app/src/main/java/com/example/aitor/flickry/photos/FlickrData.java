package com.example.aitor.flickry.photos;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue
public abstract class FlickrData implements Parcelable {
    public abstract Integer page();
    public abstract List<Photo> photos();

    public static Builder builder() {
        return new AutoValue_FlickrData.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder photos(List<Photo> value);
        public abstract Builder page(Integer value);

        public abstract FlickrData build();
    }
}
