package com.vedmitryapps.mymap.presenter;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.vedmitryapps.mymap.model.Point;
import com.vedmitryapps.mymap.view.activities.ViewInterface;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

public class PresenterImpl implements Presenter {

    private ViewInterface view;
    private Realm mRealm;

    public PresenterImpl(ViewInterface view) {
        this.view = view;
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(view.getContext()).build();

        try {
            Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e){
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                Realm.getInstance(realmConfiguration);
            } catch (Exception ex){
                throw ex;
                //No Realm file to remove.
            }
        }
        mRealm = Realm.getInstance(view.getContext());
    }

    @Override
    public void createPoint(Point p) {
        mRealm.beginTransaction();
        Point point = mRealm.createObject(Point.class);
        point.setLatitude(p.getLatitude());
        point.setLongitude(p.getLongitude());
        point.setDescription(p.getDescription());
        point.setId(getNextKey());
        mRealm.commitTransaction();

        Log.i("TAG21", String.valueOf(p.getDescription()));

       updatePoints();
    }

    @Override
    public void updatePoint(Marker m1, Marker m2) {
        Point point = mRealm.where(Point.class)
                .equalTo("id", (Long) m1.getTag())
                .findFirst();

        if(point != null){
            Log.i("TAG21", "Point founded");
            mRealm.beginTransaction();
            point.setLatitude(m2.getPosition().latitude);
            point.setLongitude(m2.getPosition().longitude);
            mRealm.commitTransaction();
        }
    }

    @Override
    public void deletePoint(Marker marker) {
        mRealm.beginTransaction();

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
    public String getPointDescription(long id) {
        Point point = mRealm.where(Point.class)
                .equalTo("id", id)
                .findFirst();

        if(point != null){
            return point.getDescription();
        }
        return "";
    }

    @Override
    public void onMarkerClick(Marker marker) {

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

    public long getNextKey() {
        try {
            Number number = mRealm.where(Point.class).max("id");
            if (number != null) {
                return number.longValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }


    public void removeAllPoints(){
        RealmResults<Point> points = mRealm.where(Point.class)
                .findAll();
        mRealm.beginTransaction();

        for (Point p:points
             ) {
            p.removeFromRealm();
        }
        mRealm.commitTransaction();
    }


}
