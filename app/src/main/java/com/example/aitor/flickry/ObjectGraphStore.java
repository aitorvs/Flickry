package com.example.aitor.flickry;

import android.content.Context;
import android.support.annotation.NonNull;

import static com.example.aitor.flickry.util.Preconditions.checkNotNull;

/**
 * This class works together with the application class and requires it implements two methods,
 * <code>addObjectGraph()</code> and <code>getObjectGraph()</code>.
 * It also requires a <code>getInstance</code> method
 */
public final class ObjectGraphStore {
    private ObjectGraphStore() {
        throw new AssertionError("No instances.");
    }

    static void addGraph(@NonNull Context context, Class<?> type, @NonNull Object component) {
        checkNotNull(context, "context == null");
        checkNotNull(component, "component == null");
        FlickryApp app = (FlickryApp) context.getApplicationContext();
        app.addObjectGraph(type, component);
    }

    public static <T> T onObjectGraph(@NonNull Class<T> type) {
        return FlickryApp.instance().getObjectGraph(type);
    }
}
