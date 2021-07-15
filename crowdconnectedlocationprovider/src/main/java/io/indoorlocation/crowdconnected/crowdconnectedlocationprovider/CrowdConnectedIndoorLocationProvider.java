package io.indoorlocation.crowdconnected.crowdconnectedlocationprovider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import net.crowdconnected.android.core.CrowdConnected;

import io.indoorlocation.core.IndoorLocation;
import io.indoorlocation.core.IndoorLocationProvider;

public class CrowdConnectedIndoorLocationProvider extends IndoorLocationProvider {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isStarted;

    @Override
    public void start() {
        Log.i("LOC_PROV", "Start");
        CrowdConnected.getInstance().registerPositionCallback(position -> handler.post(() -> dispatchIndoorLocationChange(
                new IndoorLocation("Colocator", position.getLatitude(), position.getLongitude(), (double) position.getFloor(), System.currentTimeMillis()))));
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