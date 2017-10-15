package com.example.aitor.flickry.api;

import android.support.v4.util.ArrayMap;
import com.example.aitor.flickry.BuildConfig;
import com.google.auto.value.AutoValue;
import java.util.Map;

@AutoValue
abstract class FlickrQuery {
    private static final String PARAM_METHOD = "method";
    private static final String PARAM_KEY = "api_key";
    private static final String PARAM_FORMAT = "format";
    private static final String NOJSONCALLBACK = "nojsoncallback";
    private static final String SAFE_SEARCH = "safe_search";
    private static final String PER_PAGE = "per_page";
    private static final String PAGE = "page";
    private static final String TEXT = "text";

    abstract String text();
    abstract Integer page();

    public static Builder builder() {
        return new AutoValue_FlickrQuery.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder text(String value);
        abstract Builder page(Integer value);

        abstract FlickrQuery build();
    }

    Map<String, String> toQueryMap() {
        Map<String, String> map = new ArrayMap<>();
        if (text().isEmpty()) {
            // empty search query, get recent
            map.put(PARAM_METHOD, "flickr.photos.getRecent");
        } else {
            // get search query
            map.put(PARAM_METHOD, "flickr.photos.search");
        }
        map.put(PARAM_KEY, BuildConfig.API_KEY);
        map.put(PARAM_FORMAT, "json");
        map.put(NOJSONCALLBACK, "1");
        map.put(SAFE_SEARCH, "1");
        map.put(PER_PAGE, "20");
        map.put(PAGE, page().toString());
        map.put(TEXT, text());

        return map;
    }
}
