package com.vedmitryapps.mymap.presenter;

import android.util.Log;

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
    public void createPoint(Point p) {
        mRealm.beginTransaction();
        Point point = mRealm.createObject(Point.class);
        point.setLatitude(p.getLatitude());
        point.setLongitude(p.getLongitude());
        point.setDescription(p.getDescription());
        mRealm.commitTransaction();

        Log.i("TAG21", String.valueOf(p.getDescription()));

       updatePoints();
    }

    @Override
    public void updatePoint(Point point) {

    }

    @Override
    public void deletePoint(Marker marker) {
        mRealm.beginTransaction();
 /*       RealmResults<Point> points = mRealm.where(Point.class)
                .equalTo("latitude", marker.getPosition().latitude)
                .equalTo("longitude", marker.getPosition().longitude)
                .findAll();

        if(!points.isEmpty()){
            for (int i = points.size() - 1; i >= 0; i--) {
                points.get(i).removeFromRealm();
            }
        }*/

        Point point = mRealm.where(Point.class)
                .equalTo("latitude", marker.getPosition().latitude)
                .equalTo("longitude", marker.getPosition().longitude)
                .findFirst();

        if(point != null){
            point.removeFromRealm();
        }

        mRealm.commitTransaction();
        updatePoints();
    }

    @Override
    public void onDestroy() {
        mRealm.close();
    }

    @Override
    public void mapReady() {
        updatePoints();
    }

    private void updatePoints(){
        RealmResults<Point> points = mRealm.where(Point.class).findAll();
        view.showPoints(points);
    }
}
