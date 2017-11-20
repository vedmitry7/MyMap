package com.vedmitryapps.mymap.presenter;


import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.vedmitryapps.mymap.model.Point;

public interface Presenter {

    void createPoint(Point point);
    void updatePoint(Marker m1, Marker m2);
    void deletePoint(Marker marker);
    String getPointDescription(long id);
    void onMarkerClick(Marker marker);
    void onDestroy();
    void mapReady();

    BitmapDescriptor getBitmap(int markerImageId);
}
