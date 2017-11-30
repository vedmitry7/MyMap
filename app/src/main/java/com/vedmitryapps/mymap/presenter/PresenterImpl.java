package com.vedmitryapps.mymap.presenter;

import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.vedmitryapps.mymap.BitmapUntils;
import com.vedmitryapps.mymap.R;
import com.vedmitryapps.mymap.model.MarkerImage;
import com.vedmitryapps.mymap.model.Point;
import com.vedmitryapps.mymap.view.activities.ViewInterface;

import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterImpl implements Presenter {

    private ViewInterface view;
    private Realm mRealm;

    public PresenterImpl(ViewInterface view) {
        this.view = view;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void createPoint(Point p) {
        mRealm.beginTransaction();
        Point point = mRealm.createObject(Point.class);
        point.setLatitude(p.getLatitude());
        point.setLongitude(p.getLongitude());
        point.setDescription(p.getDescription());
        point.setId(getNextKey());
        point.setMarkerImageId(p.getMarkerImageId());
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

    @Override
    public BitmapDescriptor getBitmap(int markerImageId) {

        MarkerImage markerImage = mRealm.where(MarkerImage.class)
                .equalTo("id", markerImageId)
                .findFirst();

        if(markerImage != null){
            return BitmapUntils.getBitmapFromByteArray(markerImage.getImage());
        }
        return BitmapDescriptorFactory.fromResource(R.drawable.flag36);
    }

    @Override
    public MarkerImage getMarkerImage(int markerImageId) {
        MarkerImage markerImage = mRealm.where(MarkerImage.class)
                .equalTo("id", markerImageId)
                .findFirst();

        return markerImage;
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


    public void removeAll(){
        RealmResults<Point> points = mRealm.where(Point.class)
                .findAll();
        mRealm.beginTransaction();

        for (Point p:points
             ) {
            p.removeFromRealm();
        }
        mRealm.commitTransaction();


    RealmResults<MarkerImage> markerImages = mRealm.where(MarkerImage.class)
            .findAll();
        mRealm.beginTransaction();

        for (MarkerImage m:markerImages
             ) {
        m.removeFromRealm();
    }
        mRealm.commitTransaction();
    }
}
