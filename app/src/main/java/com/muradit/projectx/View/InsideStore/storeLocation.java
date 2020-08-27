package com.muradit.projectx.View.InsideStore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.R;

public class storeLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int DEFAULT_ZOOM=16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_location);
        getSupportActionBar().hide();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width=displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;

        getWindow().setLayout((int)(width *0.8),(int)(height*0.8));

        init();
    }

    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        double currentLat=Double.parseDouble(CurrentStore.lat);
        double currentLng=Double.parseDouble(CurrentStore.lng);
        LatLng currentLocation = new LatLng(currentLat, currentLng);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Store"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,DEFAULT_ZOOM));

    }

}