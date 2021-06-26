package com.parovi.zadruga;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class GpsTracker implements LocationListener {
    private static Context mContext;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;
    private final Executor executor;

    public GpsTracker(Context c) {
        mContext = c;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        executor = MoreExecutors.directExecutor();
    }

    public void getLocation(MutableLiveData<CustomResponse<?>> location) {
        try {
            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                location.postValue(new CustomResponse<>(CustomResponse.Status.SERVICE_NOT_AVAILABLE, "Servis nije dostupan"));
            } else if (isGPSEnabled) {
                //check the network permission
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    location.postValue(new CustomResponse<>(CustomResponse.Status.PERMISSION_NOT_GRANTED, "ACCESS_COARSE_LOCATION is not  granted"));
                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, executor, loc -> {
                    if(loc != null){
                        location.postValue(new CustomResponse<>(CustomResponse.Status.OK, loc));
                    }
                });

            } else {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    location.postValue(new CustomResponse<>(CustomResponse.Status.PERMISSION_NOT_GRANTED, "ACCESS_FINE_LOCATION is not  granted"));
                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, executor, loc -> {
                    if(loc != null){
                        location.postValue(new CustomResponse<>(CustomResponse.Status.OK, loc));
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            location.postValue(new CustomResponse<>(CustomResponse.Status.EXCEPTION_ERROR, e.getMessage()));
        }
    }
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsTracker.this);
        }
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
