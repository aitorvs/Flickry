package com.example.aitor.flickry;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import timber.log.Timber;

import static com.example.aitor.flickry.util.Preconditions.checkNotNull;

public class FlickryApp extends Application {
    private static FlickryApp instance;

    private ConcurrentMap<String, Object> objectGraphMap;

    @Override
    public void onCreate() {
        super.onCreate();

        // first DI
        objectGraphMap = new ConcurrentHashMap<>();
        AppObjectGraph appObjectGraph = AppObjectGraph.Builder.build(this);
        ObjectGraphStore.addGraph(this, AppObjectGraph.class, appObjectGraph);

        // init timber with debug tree
        // FIXME: in real app I would create release and debug apps with different plants
        Timber.plant(new Timber.DebugTree());

        // FIXME: in real app I would create release and debug apps
        if (BuildConfig.DEBUG) {
            initStetho(this);
        }

        instance = this;
    }

    public static FlickryApp instance() {
        return checkNotNull(instance, "instance == null");
    }

    public void addObjectGraph(@NonNull Class<?> type, @NonNull Object graph) {
        objectGraphMap.put(checkNotNull(type).getName(), checkNotNull(graph));
    }

    @NonNull
    public <T> T getObjectGraph(@NonNull Class<T> type) {
        //noinspection unchecked
        return (T) checkNotNull(objectGraphMap.get(checkNotNull(type).getName()), type.getName() + " == null");
    }

    private void initStetho(Context context) {
        Stetho.initialize(
            Stetho.newInitializerBuilder(context)
                // enable all default CLI plugins provider
                .enableDumpapp(new DumperPluginsProvider(context))
                // enable the inspector to be used with chrome
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                .build());
    }

    /**
     * Class to get the Stetho default plugins
     */
    private class DumperPluginsProvider implements com.facebook.stetho.DumperPluginsProvider {
        private final Context mContext;

        DumperPluginsProvider(Context context) {
            mContext = context.getApplicationContext();
        }

        @Override
        public Iterable<DumperPlugin> get() {
            ArrayList<DumperPlugin> plugins = new ArrayList<>();
            for (DumperPlugin dumperPlugin : Stetho.defaultDumperPluginsProvider(mContext).get()) {
                plugins.add(dumperPlugin);
            }

            return plugins;
        }
    }
}
