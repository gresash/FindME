package com.example.ahmet.findme;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Criteria;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class MapActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback,  ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private ConnectivityManager cm;
    private double locLong;
    private double locLat;
    private LocationManager mLocationManager;
    private Location mLocation;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onLocationChanged(Location loc) {
        locLat = loc.getLatitude();
        locLong = loc.getLongitude();
        updateData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        updateData();
    }


    private void updateData() {



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (mLocation == null) {
                Criteria c = new Criteria();
                c.setAccuracy(Criteria.ACCURACY_FINE);
                c.setPowerRequirement(Criteria.POWER_LOW);
                String bestProvider = mLocationManager.getBestProvider(c, true);
                mLocation = mLocationManager.getLastKnownLocation(bestProvider);
                mLocationManager.requestLocationUpdates(bestProvider, 5000, 100, this);
            } else {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, this);
            }
            if (mLocation != null) {
                locLat = mLocation.getLatitude();
                locLong = mLocation.getLongitude();
                if (mMap != null) {

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locLat, locLong), 15));

                }

            } else {
             Toast.makeText(MapActivity.this,R.string.no_provider_error,Toast.LENGTH_SHORT).show();

            }
        } else {

                    PermissionUtils.checkLocationPermission(MapActivity.this, PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE);



        }
    }


    public void onClickButtonDetails(View v) {
        boolean  hasConnectivity = PermissionUtils.connectivityCheck(cm, this);
        boolean locationEnabled= PermissionUtils.isGPSEnabled(mLocationManager,this);
        if (hasConnectivity&&locationEnabled) {
            if(mLocation!=null) {
                String elevation="";

                if(mLocation.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                    elevation=String.valueOf(Math.round(mLocation.getAltitude()));
                }
                startActivity(sendData(locLat,locLong,elevation));
            }
            else{
                Toast.makeText(this,R.string.location_not_determined_yet,Toast.LENGTH_SHORT).show();

            }
        }

    }

    private Intent sendData(double lat,double lon,String elevation) {

            Intent i = new Intent(getBaseContext(), Info.class);
            i.setAction(Intent.ACTION_SEND);
            i.putExtra("Latitude", lat);
            i.putExtra("Longitude", lon);
            i.putExtra("Elevation",elevation);

            return i;


    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        boolean locationEnabled= PermissionUtils.isGPSEnabled(mLocationManager,this);

        if(locationEnabled){
            updateData();
        }
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {


    }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults)) {
            updateData();

        } else {
            // Display the missing permission error dialog when the fragments resume.
            PermissionUtils.checkLocationPermission(this, requestCode);
        }
    }




}






