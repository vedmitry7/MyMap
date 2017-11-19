package com.vedmitryapps.mymap.view.activities;


import android.content.Context;

import com.vedmitryapps.mymap.model.Point;

import io.realm.RealmResults;

public interface ViewInterface {

    Context getContext();
    void showPoints(RealmResults<Point> points);
}
