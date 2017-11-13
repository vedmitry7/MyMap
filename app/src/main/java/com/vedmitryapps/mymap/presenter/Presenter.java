package com.vedmitryapps.mymap.presenter;


import com.google.android.gms.maps.model.Marker;
import com.vedmitryapps.mymap.model.Point;

public interface Presenter {

    void addPoint(Marker marker);
    void deletePoint(Point point);
    void onDestroy();
    void mapReady();
}
