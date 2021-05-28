package io.indoorlocation.crowdconnected.crowdconnectedlocationprovider;

import android.app.Activity;
import android.util.Log;

import net.crowdconnected.android.core.CrowdConnected;

import io.indoorlocation.core.IndoorLocation;
import io.indoorlocation.core.IndoorLocationProvider;

public class CrowdConnectedIndoorLocationProvider extends IndoorLocationProvider {

    private final Activity activity;
    private boolean isStarted;

    public CrowdConnectedIndoorLocationProvider(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void start() {
        Log.i("LOC_PROV", "Start");
        CrowdConnected.getInstance().registerPositionCallback(position -> activity.runOnUiThread(() -> dispatchIndoorLocationChange(
                new IndoorLocation("Colocator", position.getLat(), position.getLng(), (double) position.getFloor(), System.currentTimeMillis()))));
        isStarted = true;
    }

    @Override
    public void stop() {
        Log.i("LOC_PROV", "Stop");
        CrowdConnected.getInstance().deregisterPositionCallback();
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