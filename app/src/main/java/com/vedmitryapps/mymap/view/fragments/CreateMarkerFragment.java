package com.vedmitryapps.mymap.view.fragments;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.vedmitryapps.mymap.BitmapUntils;
import com.vedmitryapps.mymap.R;
import com.vedmitryapps.mymap.model.MarkerImage;
import com.vedmitryapps.mymap.model.Point;
import com.vedmitryapps.mymap.view.CreateMarkerSurfaceView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class CreateMarkerFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    @Nullable
    @BindView(R.id.createMarkerSurfaceView)
    CreateMarkerSurfaceView markerSurfaceView;
    @BindView(R.id.colorBlack)
    Button buttonColorBlack;
    @BindView(R.id.colorGreen)
    Button buttonColorGreen;
    @BindView(R.id.colorBlue)
    Button buttonColorBlue;
    @BindView(R.id.colorRed)
    Button buttonColorRed;

    @BindView(R.id.seekBar)
    SeekBar seekBar;

    Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_marker, container, false);
        ButterKnife.bind(this, view);
        mRealm = Realm.getDefaultInstance();
        buttonColorBlack.setOnClickListener(this);
        buttonColorBlue.setOnClickListener(this);
        buttonColorGreen.setOnClickListener(this);
        buttonColorRed.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        return view;
    }

    public void saveImg() {
        mRealm.beginTransaction();
        MarkerImage markerImage = mRealm.createObject(MarkerImage.class);
        markerImage.setId(getNextKey());
        markerSurfaceView.prepareToScreen();
        markerImage.setImage(BitmapUntils.getBytesFromBitmap(BitmapUntils.getBitmapFromView(markerSurfaceView, 96, 96)));
        markerImage.setCoordinateX(markerSurfaceView.getPositionMarker()[0]);
        markerImage.setCoordinateY(markerSurfaceView.getPositionMarker()[1]);
        mRealm.commitTransaction();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.colorBlack:
                markerSurfaceView.setColor(Color.BLACK);
                break;
            case R.id.colorRed:
                markerSurfaceView.setColor(Color.RED);
                break;
            case R.id.colorGreen:
                markerSurfaceView.setColor(Color.GREEN);
                break;
            case R.id.colorBlue:
                markerSurfaceView.setColor(Color.BLUE);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        markerSurfaceView.setStrokeWidth(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
