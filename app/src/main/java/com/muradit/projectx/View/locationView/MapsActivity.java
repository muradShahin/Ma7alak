package com.muradit.projectx.View.locationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.R;
import com.muradit.projectx.ViewModel.MapsViewModel.MapsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 18;
    private GoogleMap mMap;
    private double latittude=31.8360368,longitude=36.2278347;
    private MaterialSearchBar searchBar;
    private TextView placeInfo;
    private MapsViewModel mapsViewModel;
    private Button update;

    //places objects
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    //vars
    private String lat,lng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
       // getPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        searchBar=findViewById(R.id.searchBar);
        placeInfo=findViewById(R.id.placeInfo);
        update=findViewById(R.id.updateBtn);
        mapsViewModel=new MapsViewModel(this);
        initSearchBar();



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsViewModel.updateStoreLocation(lat,lng);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        double currentLat=Double.parseDouble(CurrentStore.lat);
        double currentLng=Double.parseDouble(CurrentStore.lng);
        LatLng currentLocation = new LatLng(currentLat, currentLng);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Store"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,DEFAULT_ZOOM));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                geoLocate(latLng);
            }
        });
    }

    private void geoLocate(LatLng latLng){
        Geocoder geocoder=new Geocoder(this);

        List<Address> list=new ArrayList<>();

        try {
            list=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        }catch (Exception e){

        }
        if(list.size()>0){
            Address address=list.get(0);
            latittude=address.getLatitude();
            longitude=address.getLongitude();
            Log.d("locationSearch","geoLocate:found a location"+address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
            placeInfo.setText("New Location : "+address.getAddressLine(0));
            lat=address.getLatitude()+"";
            lng=address.getLongitude()+"";
        }



    }
    private void moveCamera(LatLng latLng,float zoom,String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions options=new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);

    }

    private void initSearchBar(){

        Places.initialize(this,"AIzaSyDUlj6qD7kNCBM5mAZbMFznS-6c7DlzHq8");
        placesClient=Places.createClient(this);
        final AutocompleteSessionToken token=AutocompleteSessionToken.newInstance();
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(),true,null,true);

            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if(buttonCode==searchBar.BUTTON_NAVIGATION){
                    //open or close

                }else if(buttonCode==searchBar.BUTTON_BACK){
                    searchBar.disableSearch();

                }

            }
        });


        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest=FindAutocompletePredictionsRequest.builder().setCountry("jo")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()){
                            FindAutocompletePredictionsResponse predictionsResponse=task.getResult();
                            if(predictionsResponse!=null){
                                predictionList=predictionsResponse.getAutocompletePredictions();
                                List<String> suggestions=new ArrayList<>();
                                for (int i=0;i<predictionList.size();i++){
                                    AutocompletePrediction prediction=predictionList.get(i);
                                    suggestions.add(prediction.getFullText(null).toString());
                                }
                                searchBar.updateLastSuggestions(suggestions);
                                if(!searchBar.isSuggestionsVisible()){
                                    searchBar.showSuggestionsList();
                                }
                            }

                        } else{
                            Log.i("mytag","prediction unsuccessful");
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
           @Override
           public void OnItemClickListener(int position, View v) {
               if(position>=predictionList.size()){
                   return;

               }
               AutocompletePrediction selectedPrediction=predictionList.get(position);
               String suggestion =searchBar.getLastSuggestions().get(position).toString();
               searchBar.setText(suggestion);

               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       searchBar.clearSuggestions();
                   }
               }, 1000);

               InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
               if(imm !=null)
                   imm.hideSoftInputFromWindow(searchBar.getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
               final String placeId=selectedPrediction.getPlaceId();
               List<Place.Field>placeFields= Arrays.asList(Place.Field.LAT_LNG);

               FetchPlaceRequest fetchPlaceRequest=FetchPlaceRequest.builder(placeId,placeFields).build();
               placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                   @Override
                   public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                       Place place=fetchPlaceResponse.getPlace();
                       Log.i("mytag","place found"+place.getName());
                       LatLng latLngOfPlace=place.getLatLng();
                       if(latLngOfPlace !=null){
                           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace,DEFAULT_ZOOM));

                       }

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       if(e instanceof ApiException){
                           ApiException apiException=(ApiException)e;
                           apiException.printStackTrace();
                           int statusCode=apiException.getStatusCode();
                           Log.i("mytag","place not found"+e.getMessage());
                           Log.i("mytag","status code"+statusCode);

                       }
                   }
               });
           }
           @Override
           public void OnItemDeleteListener(int position, View v) {

           }
       });


    }


   /* private void getPermission(){

        String permissions[]={FINE_LOCATION,COARSE_LOCATION};


        if(ContextCompat.checkSelfPermission(this,FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                locationPermissionGranted=true;
            else{
                ActivityCompat.requestPermissions(this,permissions,LOCATION_REQUEST_CODE);
            }


        } else{
            ActivityCompat.requestPermissions(this,permissions,LOCATION_REQUEST_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted=false;

        switch (requestCode){

            case LOCATION_REQUEST_CODE:{
                if(grantResults.length>0){
                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i] !=PackageManager.PERMISSION_GRANTED){
                            locationPermissionGranted=false;
                            return;
                        }

                    }
                    locationPermissionGranted=true;
                    //init the map

                }

            }

        }


    }*/
}
