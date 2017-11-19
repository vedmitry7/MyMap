package com.vedmitryapps.mymap.view.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

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
        imageView.setImageBitmap(getBitmapFromView(markerSurfaceView));
        mRealm.beginTransaction();
        MarkerImage markerImage = mRealm.createObject(MarkerImage.class);
        markerImage.setImage(getBytes(getBitmapFromView(markerSurfaceView)));
        mRealm.commitTransaction();
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        byte[] bitmapdata = blob.toByteArray();

        return Bitmap.createScaledBitmap(returnedBitmap, 96,96, true);
    }

    byte[] getBytes(Bitmap returnedBitmap){
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        return blob.toByteArray();
    }

}
