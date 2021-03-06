package com.vedmitryapps.mymap.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Point extends RealmObject implements ClusterItem {

    @PrimaryKey
    private long id;
    private double latitude;
    private double longitude;
    private String description;
    private String shape;
    private int color;
    private int radius;
    private int markerImageId;

    @Ignore
    private LatLng position;

    public int getMarkerImageId() {
        return markerImageId;
    }

    public void setMarkerImageId(int markerImageId) {
        this.markerImageId = markerImageId;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setPosition(LatLng latLng){
        position = latLng;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }
}
