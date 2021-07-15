package io.indoorlocation.crowdconnected.demoapp;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import io.mapwize.mapwizesdk.core.MapwizeConfiguration;

public class CrowdConnectedIndoorLocationProviderDemoApplication extends Application {

    public static final String MAPWIZE_API_KEY = "YOU_MAPWIZE_APIKEY";

    @Override
    public void onCreate() {
        super.onCreate();
        Mapbox.getInstance(this, "pk.mapwize");
        MapwizeConfiguration config = new MapwizeConfiguration.Builder(this, MAPWIZE_API_KEY).build();
        MapwizeConfiguration.start(config);
    }

}
