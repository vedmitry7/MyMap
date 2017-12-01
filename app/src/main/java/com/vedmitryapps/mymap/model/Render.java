package com.vedmitryapps.mymap.model;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.vedmitryapps.mymap.BitmapUntils;

import io.realm.Realm;

public class Render extends DefaultClusterRenderer<MyItem> {

    private Realm mRealm;

    public Render(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {

        MarkerImage markerImage = mRealm.where(MarkerImage.class)
                .equalTo("id", item.getIconId())
                .findFirst();

        markerOptions.icon(BitmapUntils.getBitmapFromByteArray(markerImage.getImage()));
        markerOptions.title(item.getTitle());
        markerOptions.draggable(true);
        super.onBeforeClusterItemRendered(item, markerOptions);
    }


}
