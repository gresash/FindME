package com.example.ahmet.findme;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Utility class for access to runtime permissions.
 */
abstract class PermissionUtils {


    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String permission=Manifest.permission.ACCESS_FINE_LOCATION;
    public static void checkLocationPermission(final AppCompatActivity activity, int requestId) {


        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
               

                Toast.makeText(activity,R.string.permission_rationale_location,Toast.LENGTH_SHORT).show();

            } else {
                // Location permission has not been granted yet, request it.
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);

            }

        }

    }

    public static boolean isGPSEnabled(LocationManager lm,Activity app) {
        boolean isLocationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isLocationEnabled) {
            Toast.makeText(app,R.string.request_enable_gps,Toast.LENGTH_SHORT).show();
        }
        return isLocationEnabled;

    }

    public static boolean connectivityCheck(ConnectivityManager cm, Activity app) {

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {

        Toast.makeText(app,R.string.permission_rationale_connectivity,Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;

    }

    /**
     * Checks if the result contains a {@link PackageManager#PERMISSION_GRANTED} result for a
     * permission from a runtime permissions request.
     *
     * @see android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
     */
    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }
}

