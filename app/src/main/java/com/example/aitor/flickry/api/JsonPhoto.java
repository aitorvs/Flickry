package com.example.aitor.flickry.api;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/*
{    
    "id": "23451156376",    
    "owner": "28017113@N08",    
    "secret": "8983a8ebc7",    
    "server": "578",    
    "farm": 1,    
    "title": "Merry Christmas!",    
    "ispublic": 1,    
    "isfriend": 0,    
    "isfamily": 0 
}
 */
@AutoValue
public abstract class JsonPhoto implements Parcelable {
    public abstract String id();
    public abstract String owner();
    public abstract String secret();
    public abstract String server();
    public abstract Integer farm();
    public abstract String title();
    @Json(name = "ispublic") public abstract Integer isPublic();
    @Json(name = "isfriend") public abstract Integer isFriend();
    @Json(name = "isfamily") public abstract Integer isFamily();

    public static Builder builder() {
        return new AutoValue_JsonPhoto.Builder();
    }

    public static JsonAdapter<JsonPhoto> jsonAdapter(Moshi moshi) {
        return new AutoValue_JsonPhoto.MoshiJsonAdapter(moshi);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String value);
        public abstract Builder owner(String value);
        public abstract Builder secret(String value);
        public abstract Builder server(String value);
        public abstract Builder title(String value);
        public abstract Builder farm(Integer value);
        public abstract Builder isPublic(Integer value);
        public abstract Builder isFriend(Integer value);
        public abstract Builder isFamily(Integer value);

        public abstract JsonPhoto build();
    }
}
