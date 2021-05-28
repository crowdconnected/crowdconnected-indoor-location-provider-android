package io.indoorlocation.crowdconnected.demoapp;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import io.mapwize.mapwizesdk.core.MapwizeConfiguration;

public class CrowdConnectedIndoorLocationProviderDemoApplication extends Application {

    static final String MAPWIZE_API_KEY = "YOUR_MAPWIZE_APIKEY";
    static final String CROWDCONNECTED_APP_KEY = "YOUR_CROWDCONNECTED_APP_KEY";
    static final String CROWDCONNECTED_TOKEN = "YOUR_CROWDCONNECTED_TOKEN";
    static final String CROWDCONNECTED_SECRET = "YOUR_CROWDCONNECTED_SECRET";

    @Override
    public void onCreate() {
        super.onCreate();
        Mapbox.getInstance(this, "pk.mapwize");
        MapwizeConfiguration config = new MapwizeConfiguration.Builder(this, MAPWIZE_API_KEY).build();
        MapwizeConfiguration.start(config);
    }

}
