package com.muradit.projectx.Model.others;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GetCurrentLocation {
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private LocationCallback locationCallback;
    private double lat,lng;
    private SweetAlertDialog pd;
    private MutableLiveData<Boolean> isLocationReady;

    public GetCurrentLocation(Context context) {
        this.context = context;
        isLocationReady=new MutableLiveData<>();
        pd=new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context);


    }

    public void getLocation(){

        // this code is responsible to check the GPS
        LocationRequest locationRequest=LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient= LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task=settingsClient.checkLocationSettings(builder.build());
        //if the gps is enabled
        task.addOnSuccessListener((AppCompatActivity)context, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();

            }
        });

        //if Gps is not enable , enable dialog will appear
        task.addOnFailureListener((AppCompatActivity)context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException resolvableApiException=(ResolvableApiException)e;
                    try {
                        resolvableApiException.startResolutionForResult((AppCompatActivity)context,51);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {

        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);
        pd.show();


        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    lastKnownLocation=task.getResult();
                    if(lastKnownLocation!=null){
                        lat=lastKnownLocation.getLatitude();
                        lng=lastKnownLocation.getLongitude();
                        CurrentUserInfo.lat=lat+"";
                        CurrentUserInfo.lng=lng+"";
                        isLocationReady.setValue(true);

                    }else{
                        final LocationRequest locationRequest=LocationRequest.create();
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
                        locationCallback =new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                if(locationResult==null) {
                                    return;
                                }
                                lastKnownLocation=locationResult.getLastLocation();
                                lat=lastKnownLocation.getLatitude();
                                lng=lastKnownLocation.getLongitude();
                                CurrentUserInfo.lat=lat+"";
                                CurrentUserInfo.lng=lng+"";
                                isLocationReady.setValue(true);
                                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);


                    }
                    pd.dismiss();
                } else{
                    isLocationReady.setValue(false);
                    Toast.makeText((AppCompatActivity)context, "unable to get last location", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public LiveData<Boolean> getLocationState(){
        return isLocationReady;
    }



}
