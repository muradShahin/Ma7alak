package com.muradit.projectx.View.StoreOrders;

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

public class CustomerLocation extends AppCompatActivity implements OnMapReadyCallback {
    private static final float DEFAULT_ZOOM =16 ;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_location);
        getSupportActionBar().hide();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width=displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;

        getWindow().setLayout((int)(width *0.8),(int)(height*0.8));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        double currentLat=getIntent().getExtras().getDouble("lat");
        double currentLng=getIntent().getExtras().getDouble("lng");
        LatLng currentLocation = new LatLng(currentLat, currentLng);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Customer"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,DEFAULT_ZOOM));
    }
}