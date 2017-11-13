package com.vedmitryapps.mymap.view.activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Property;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.vedmitryapps.mymap.presenter.PresenterImpl;
import com.vedmitryapps.mymap.view.fragments.AddPointFragment;
import com.vedmitryapps.mymap.view.fragments.PointFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

import static com.vedmitryapps.mymap.R.id.map;

public class MapsActivity extends AppCompatActivity implements
        View,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener
{

    private GoogleMap mMap;
    private Marker mMarker;
    private Marker mMyLocationMarker;
    private FragmentManager fragmentManager;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private int count;
    private PresenterImpl presenter;

    private FusedLocationProviderClient mFusedLocationClient;

    @BindView(R.id.coordinates)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        fragmentManager = getFragmentManager();

        presenter = new PresenterImpl(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    count++;
                    Log.i("TAG21", location.getLatitude() + ":" + location.getLongitude());
                    textView.setText(count +  "\r\n" + location.getLatitude() + "\r\n" + location.getLongitude());

                    if(!mMyLocationMarker.isVisible())
                        mMyLocationMarker.setVisible(true);

                    animateMarker(mMyLocationMarker, new LatLng(location.getLatitude(), location.getLongitude()));
                  //  mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                }
            }
        };

        createLocationRequest();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG21", "resume, start updates");
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
        Log.i("TAG21", "request");
    }

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
        }

        LatLng sydney = new LatLng(-34, 151);
     /*   mMarker = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
                .draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMyLocationMarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag24))
                .anchor(0.25f,1)
                .position(sydney)
                .visible(false)
                .title("I'm"));

        presenter.mapReady();

       /* LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                //  createLocationRequest();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(MapsActivity.this,
                                    25);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });*/
    }

    protected void createLocationRequest() {
        Log.i("TAG21", "createLocationRequest");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onMapClick(LatLng latLng) {

        if(mMarker == null){
            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .draggable(true));
        } else {
            animateMarker(mMarker, latLng);
        }

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

    public void addPoint(View v) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
//        textView.setText("Marker" + "\r\n" + mMarker.getPosition().latitude + "\r\n" + mMarker.getPosition().longitude);

        Bundle bundle = new Bundle();
        bundle.putDouble("lat", marker.getPosition().latitude);
        bundle.putDouble("lon", marker.getPosition().longitude);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        PointFragment fragment = new PointFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

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

    public void copy(View view) {
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showPoints(RealmResults<Point> points) {
        Log.i("TAG21", String.valueOf(points.size()));
        for (Point p : points
                ) {
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag36))
                    .draggable(true)
                    .anchor(0.0f, 1.0f) // Anchors the mMarker on the bottom left
                    .position(new LatLng(p.getLatitude(), p.getLongitude())));
        }
    }

    public void findMe(android.view.View view) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMyLocationMarker.getPosition(), 18.0f));

    }

    public void addPoint(android.view.View view) {
        presenter.addPoint(mMarker);
    }
}


/*        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        *//**
 * Work good!
 *//*
                        if (location != null) {
                            Log.i("TAG21", location.getLatitude() + ":" + location.getLongitude());
                        }
                    }
                });

*/