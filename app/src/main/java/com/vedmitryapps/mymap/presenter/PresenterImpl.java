package com.vedmitryapps.mymap.presenter;

import com.google.android.gms.maps.model.Marker;
import com.vedmitryapps.mymap.model.Point;
import com.vedmitryapps.mymap.view.activities.View;

import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterImpl implements Presenter {

    private View view;
    private Realm mRealm;

    public PresenterImpl(View view) {
        this.view = view;
        mRealm = Realm.getInstance(view.getContext());
    }



    @Override
    public void addPoint(Marker marker) {
        mRealm.beginTransaction();
        Point point = mRealm.createObject(Point.class);
        point.setLatitude(marker.getPosition().latitude);
        point.setLongitude(marker.getPosition().longitude);
        mRealm.commitTransaction();

        RealmResults<Point> points = mRealm.where(Point.class).findAll();
        view.showPoints(points);
    }

    @Override
    public void deletePoint(Point point) {

    }

    @Override
    public void onDestroy() {
        mRealm.close();
    }

    @Override
    public void mapReady() {
        RealmResults<Point> points = mRealm.where(Point.class).findAll();
        view.showPoints(points);
    }
}
