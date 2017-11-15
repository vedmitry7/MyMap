package com.vedmitryapps.mymap.presenter;


import com.google.android.gms.maps.model.Marker;
import com.vedmitryapps.mymap.model.Point;

public interface Presenter {

    void createPoint(Point point);
    void updatePoint(Point point);
    void deletePoint(Marker marker);
    void onDestroy();
    void mapReady();
}
