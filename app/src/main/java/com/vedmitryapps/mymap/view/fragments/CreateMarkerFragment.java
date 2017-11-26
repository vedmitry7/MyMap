package com.vedmitryapps.mymap.view.fragments;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vedmitryapps.mymap.R;
import com.vedmitryapps.mymap.model.MarkerImage;
import com.vedmitryapps.mymap.model.Point;
import com.vedmitryapps.mymap.view.CreateMarkerSurfaceView;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class CreateMarkerFragment extends Fragment {

    @Nullable
    @BindView(R.id.createMarkerSurfaceView)
    CreateMarkerSurfaceView markerSurfaceView;

    Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_marker, container, false);


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getActivity()).build();

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
        mRealm = Realm.getInstance(getActivity());

        ButterKnife.bind(this, view);
        return view;
    }

    public void saveImg() {
        mRealm.beginTransaction();
        MarkerImage markerImage = mRealm.createObject(MarkerImage.class);
        markerImage.setId(getNextKey());
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

    public int getNextKey() {
        try {
            Number number = mRealm.where(Point.class).max("id");
            if (number != null) {
                return (int) (number.longValue() + 1);
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public void nextStep() {
        markerSurfaceView.nextStep();
    }
}
