package com.vedmitryapps.mymap.presenter;


import com.google.android.gms.maps.model.Marker;

public interface Presenter {

    void addPoint(Marker marker);
    void deletePoint(Marker marker);
    void onDestroy();
    void mapReady();
}
