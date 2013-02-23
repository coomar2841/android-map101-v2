
package com.beanie.map101;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class HomeActivity extends Activity implements LocationListener {

    private final static int MIN_TIME = 2000;

    private final static float MIN_DISTANCE = 100;

    private LocationManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getCurrentLocation();
    }

    private void getCurrentLocation() {
        lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.NETWORK_PROVIDER;

        if (lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        }
        lManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(getClass().getName(), location.getLatitude() + "," + location.getLongitude());
        startLocationActivity(location);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        lManager.removeUpdates(this);
    }

    private void startLocationActivity(Location location) {
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra("lat", location.getLatitude());
        intent.putExtra("long", location.getLongitude());
        startActivity(intent);

        finish();
    }
}
