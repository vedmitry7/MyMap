package com.vedmitryapps.mymap.view.activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vedmitryapps.mymap.R;
import com.vedmitryapps.mymap.model.Point;
import com.vedmitryapps.mymap.view.fragments.AddPointFragment;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.vedmitryapps.mymap.R.id.map;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private TextView textView;
    private Marker mMarker;
    private FragmentManager fragmentManager;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        fragmentManager = getFragmentManager();

        textView = (TextView) findViewById(R.id.location);

        mRealm = Realm.getInstance(this);

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a mMarker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            textView.setText("ggggggggggg");
            // Show rationale and request permission.
        }

        // Add a mMarker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
                .draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        RealmResults<Point> points = mRealm.where(Point.class).findAll();
        Log.i("TAG21", String.valueOf(points.size()));
        for (Point p:points
                ) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag36))
                    .draggable(true)
                    .anchor(0.0f, 1.0f) // Anchors the mMarker on the bottom left
                    .position(new LatLng(p.getLatitude(), p.getLongitude())));
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
/*        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag36))
                .draggable(true)
                .anchor(0.0f, 1.0f) // Anchors the mMarker on the bottom left
                .position(latLng));
        textView.setText(latLng.latitude + "\r\n" + latLng.longitude);*/

        animateMarker(mMarker, latLng);

        Bundle bundle = new Bundle();
        bundle.putDouble("lat", latLng.latitude);
        bundle.putDouble("lon", latLng.longitude);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AddPointFragment fragment = new AddPointFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addPoint(View v){
        mRealm.beginTransaction();
        Point point = mRealm.createObject(Point.class);
        point.setLatitude(mMarker.getPosition().latitude);
        point.setLongitude(mMarker.getPosition().longitude);
        mRealm.commitTransaction();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        textView.setText("Marker" + "\r\n" + mMarker.getPosition().latitude + "\r\n" + mMarker.getPosition().longitude);

        return false;
    }

    static void animateMarker(Marker marker, LatLng finalPosition) {
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startPosition, LatLng endPosition) {

                double lat = (endPosition.latitude - startPosition.latitude) * fraction + startPosition.latitude;
                double lng = (endPosition.longitude - startPosition.longitude) * fraction + startPosition.longitude;
                return new LatLng(lat, lng);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        animator.setDuration(300);
        animator.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}


