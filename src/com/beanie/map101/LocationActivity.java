
package com.beanie.map101;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

public class LocationActivity extends Activity implements OnMapClickListener {
    private MapFragment mapFragment;

    private CameraPosition cameraPosition;

    private LatLng myLatLng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Bundle data = getIntent().getExtras();
        double lat = data.getDouble("lat");
        double lng = data.getDouble("long");
        myLatLng = new LatLng(lat, lng);
        cameraPosition = new CameraPosition(myLatLng, 14, 0, 0);

        initializeUI();
    }

    private void initializeUI() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragmentMap);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mapFragment.getMap().moveCamera(cameraUpdate);

        mapFragment.getMap().setOnMapClickListener(this);

        addMyLocationMarker();
        addSavedLocations();
    }

    private void addMyLocationMarker() {
        MarkerOptions myMarker = new MarkerOptions();
        myMarker.title("My Location");
        myMarker.snippet("You are currently here.");
        myMarker.position(myLatLng);
        mapFragment.getMap().addMarker(myMarker);
    }

    @Override
    public void onMapClick(LatLng latLong) {
        Log.i(getClass().getName(), "Tapped: " + latLong.latitude + "," + latLong.longitude);

        DialogAddPlace dialogAddPlace = new DialogAddPlace(this);
        dialogAddPlace.setLatLng(latLong);
        dialogAddPlace.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                DialogAddPlace d = (DialogAddPlace) dialog;
                addLocation(d.getLocation());
            }
        });
        dialogAddPlace.show();
    }

    private void addSavedLocations() {
        VisibleRegion visibleRegion = mapFragment.getMap().getProjection().getVisibleRegion();
        DBHelper helper = new DBHelper(getApplicationContext());
        helper.open();
        ArrayList<MapLocation> locations = helper.getSavedLocations(visibleRegion);
        helper.close();
        for (MapLocation location : locations) {
            addLocation(location);
        }
    }

    private void addLocation(MapLocation location) {
        if (location != null) {
            MarkerOptions placeMarker = new MarkerOptions();
            placeMarker.title(location.getName());
            placeMarker.snippet(location.getDescription());
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            placeMarker.position(latLng);
            mapFragment.getMap().addMarker(placeMarker);
        }
    }

}
