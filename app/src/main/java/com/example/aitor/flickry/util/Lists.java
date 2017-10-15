package com.example.aitor.flickry.util;

import java.util.List;

public final class Lists {
    private Lists() {
        // No instances.
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }
}
