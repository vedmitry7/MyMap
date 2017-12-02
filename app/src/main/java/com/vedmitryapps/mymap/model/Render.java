package com.vedmitryapps.mymap.model;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.vedmitryapps.mymap.BitmapUntils;

import io.realm.Realm;

public class Render extends DefaultClusterRenderer<Point> {

    private Realm mRealm;

    public Render(Context context, GoogleMap map, ClusterManager<Point> clusterManager) {
        super(context, map, clusterManager);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void onBeforeClusterItemRendered(Point item, MarkerOptions markerOptions) {

        MarkerImage markerImage = mRealm.where(MarkerImage.class)
                .equalTo("id", item.getMarkerImageId())
                .findFirst();

        markerOptions.icon(BitmapUntils.getBitmapFromByteArray(markerImage.getImage()));
        markerOptions.anchor(markerImage.getCoordinateX(), markerImage.getCoordinateY());
        markerOptions.title(item.getDescription());
        markerOptions.draggable(true);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }


}
