package io.indoorlocation.crowdconnected.demoapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.crowdconnected.android.core.Configuration;
import net.crowdconnected.android.core.ConfigurationBuilder;
import net.crowdconnected.android.core.CrowdConnected;
import net.crowdconnected.android.ips.IPSModule;

import java.util.Locale;

import io.indoorlocation.crowdconnected.crowdconnectedlocationprovider.CrowdConnectedIndoorLocationProvider;
import io.mapwize.mapwizesdk.api.MapwizeObject;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizeui.MapwizeFragment;

import static io.indoorlocation.crowdconnected.demoapp.CrowdConnectedIndoorLocationProviderDemoApplication.CROWDCONNECTED_APP_KEY;
import static io.indoorlocation.crowdconnected.demoapp.CrowdConnectedIndoorLocationProviderDemoApplication.CROWDCONNECTED_SECRET;
import static io.indoorlocation.crowdconnected.demoapp.CrowdConnectedIndoorLocationProviderDemoApplication.CROWDCONNECTED_TOKEN;

public class MapActivity extends AppCompatActivity implements MapwizeFragment.OnFragmentInteractionListener {

    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 0;
    private static final String LOG_TAG = "MAP_ACTIVITY";

    private MapwizeFragment mapwizeFragment;
    private CrowdConnectedIndoorLocationProvider crowdConnectedIndoorLocationProvider;
    private MapwizeMap mapwizeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        FrameLayout container = findViewById(R.id.container);
        MapOptions opts = new MapOptions.Builder()
                .language(Locale.getDefault().getLanguage())
                .build();
        mapwizeFragment = MapwizeFragment.newInstance(opts);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(container.getId(), mapwizeFragment);
        ft.commit();
    }

    @Override
    public void onStart() {
        Configuration configuration = new ConfigurationBuilder()
                .withAppKey(CROWDCONNECTED_APP_KEY)
                .withToken(CROWDCONNECTED_TOKEN)
                .withSecret(CROWDCONNECTED_SECRET)
                .withStatusCallback(reason -> Log.i(LOG_TAG, "Start up failure: " + reason))
                .addModule(new IPSModule())
                .build();
        CrowdConnected.start(getApplication(), configuration);
        setupLocationProvider();
        mapwizeFragment.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapwizeFragment.onResume();
    }

    @Override
    public void onPause() {
        mapwizeFragment.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mapwizeFragment.onStop();
        if (CrowdConnected.getInstance() != null) {
            CrowdConnected.getInstance().stop();
        }
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@androidx.annotation.NonNull Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        mapwizeFragment.onSaveInstanceState(saveInstanceState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapwizeFragment.onLowMemory();
    }

    @Override
    public void onDestroy() {
        mapwizeFragment.onDestroy();
        super.onDestroy();
    }

    private void startLocationService() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            setupLocationProvider();
        }
    }

    private void setupLocationProvider() {
        if (mapwizeMap != null) {
            crowdConnectedIndoorLocationProvider = new CrowdConnectedIndoorLocationProvider();
            crowdConnectedIndoorLocationProvider.start();
            mapwizeMap.setIndoorLocationProvider(crowdConnectedIndoorLocationProvider);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_ACCESS_FINE_LOCATION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupLocationProvider();
        }
    }

    @Override
    public void onMenuButtonClick() {
        Log.i(LOG_TAG, "onMenuButtonClick");
    }

    @Override
    public void onInformationButtonClick(MapwizeObject mapwizeObject) {
        Log.i(LOG_TAG, "onInformationButtonClick");
    }

    @Override
    public void onFragmentReady(MapwizeMap mapwizeMap) {
        this.mapwizeMap = mapwizeMap;
        startLocationService();
    }

    @Override
    public void onFollowUserButtonClickWithoutLocation() {
        Log.i(LOG_TAG, "onFollowUserButtonClickWithoutLocation");
    }
}