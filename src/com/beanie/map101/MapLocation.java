
package com.beanie.map101;

import com.google.android.gms.maps.model.LatLng;

public class MapLocation {
    private long _id;

    private double latitude;

    private double longitude;

    private String name;

    private String description;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public LatLng getLatLng(){
        LatLng latLng = new LatLng(latitude, longitude);
        return latLng;
    }
}
