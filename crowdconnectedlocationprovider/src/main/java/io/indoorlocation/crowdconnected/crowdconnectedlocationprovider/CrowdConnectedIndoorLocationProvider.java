package io.indoorlocation.crowdconnected.crowdconnectedlocationprovider;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import net.crowdconnected.android.core.Configuration;
import net.crowdconnected.android.core.ConfigurationBuilder;
import net.crowdconnected.android.core.CrowdConnected;
import net.crowdconnected.android.ips.IPSModule;

import io.indoorlocation.core.IndoorLocation;
import io.indoorlocation.core.IndoorLocationProvider;

public class CrowdConnectedIndoorLocationProvider extends IndoorLocationProvider {

    private static final String LOG_TAG = "CC_LOC_PROV";

    private static final String CROWDCONNECTED_APP_KEY = "YOUR_CROWDCONNECTED_APP_KEY";
    private static final String CROWDCONNECTED_TOKEN = "YOUR_CROWDCONNECTED_TOKEN";
    private static final String CROWDCONNECTED_SECRET = "YOUR_CROWDCONNECTED_SECRET";

    private final Application application;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isStarted;

    public CrowdConnectedIndoorLocationProvider(Application application) {
        this.application = application;
    }

    @Override
    public void start() {
        Log.i("LOC_PROV", "Start");
        Configuration configuration = new ConfigurationBuilder()
                .withAppKey(CROWDCONNECTED_APP_KEY)
                .withToken(CROWDCONNECTED_TOKEN)
                .withSecret(CROWDCONNECTED_SECRET)
                .withStatusCallback(reason -> Log.i(LOG_TAG, "Start up failure: " + reason))
                .addModule(new IPSModule())
                .build();
        CrowdConnected.start(application, configuration);
        CrowdConnected.getInstance().registerPositionCallback(position -> {
            if (position != null && position.getFloor() != null) {
                handler.post(() -> dispatchIndoorLocationChange(
                        new IndoorLocation("Colocator", position.getLatitude(), position.getLongitude(), (double) position.getFloor(), System.currentTimeMillis())));
            }
        });
        isStarted = true;
    }

    @Override
    public void stop() {
        Log.i("LOC_PROV", "Stop");
        CrowdConnected crowdConnected = CrowdConnected.getInstance();
        if (crowdConnected != null) {
            crowdConnected.deregisterPositionCallback();
            crowdConnected.stop();
        }
        isStarted = false;
    }

    @Override
    public boolean supportsFloor() {
        return true;
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }
}