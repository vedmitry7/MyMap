package com.vedmitryapps.mymap.view.activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.vedmitryapps.mymap.R;
import com.vedmitryapps.mymap.model.Point;
import com.vedmitryapps.mymap.view.fragments.AddPointFragment;
import com.vedmitryapps.mymap.view.fragments.PointFragment;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.vedmitryapps.mymap.R.id.location;
import static com.vedmitryapps.mymap.R.id.map;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener {

    private GoogleMap mMap;
    private TextView textView;
    private Marker mMarker;
    private FragmentManager fragmentManager;
    private Realm mRealm;
    private LocationManager locationManager;
    private String provider;
    private LocationRequest mLocationRequest;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        fragmentManager = getFragmentManager();

        textView = (TextView) findViewById(location);

        mRealm = Realm.getInstance(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
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
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.i("TAG21", "Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Log.i("TAG21", "Location not available");
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.i("TAG21", location.getLatitude() + ":" + location.getLongitude());
                        }
                    }
                });
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
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
                .draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        showPoints();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                createLocationRequest();
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
        });

    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void showPoints() {
        RealmResults<Point> points = mRealm.where(Point.class).findAll();
        Log.i("TAG21", String.valueOf(points.size()));
        for (Point p : points
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

    public void addPoint(View v) {
        mRealm.beginTransaction();
        Point point = mRealm.createObject(Point.class);
        point.setLatitude(mMarker.getPosition().latitude);
        point.setLongitude(mMarker.getPosition().longitude);
        mRealm.commitTransaction();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "lan - " + location.getLatitude() + "\r\n lon - " + location.getLongitude(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void copy(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("TAG21", "SHIT!");

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.i("TAG21", location.getLatitude() + ":" + location.getLongitude());
                            Toast.makeText(getApplication(), "lan - " + location.getLatitude() + "\r\n lon - " + location.getLongitude(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


