package com.vedmitryapps.mymap.view.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.vedmitryapps.mymap.BitmapUntils;
import com.vedmitryapps.mymap.R;
import com.vedmitryapps.mymap.model.MarkerImage;
import com.vedmitryapps.mymap.view.CreateMarkerSurfaceView;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class CreateMarkerActivity extends AppCompatActivity {


    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.createMarkerSurfaceView)
    CreateMarkerSurfaceView markerSurfaceView;

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_marker);
        ButterKnife.bind(this);


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();

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
        mRealm = Realm.getInstance(this);
    }


    public void saveImg(View view) {
        imageView.setImageBitmap(BitmapUntils.getBitmapFromView(markerSurfaceView, 96, 96));
        mRealm.beginTransaction();
        MarkerImage markerImage = mRealm.createObject(MarkerImage.class);
        markerImage.setImage(getBytes(BitmapUntils.getBitmapFromView(markerSurfaceView, 96,96)));
        mRealm.commitTransaction();
    }

    byte[] getBytes(Bitmap returnedBitmap){
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        return blob.toByteArray();
    }

}
