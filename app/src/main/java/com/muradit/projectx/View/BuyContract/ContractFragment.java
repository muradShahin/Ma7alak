package com.muradit.projectx.View.BuyContract;


import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.GetCurrentLocation;
import com.muradit.projectx.R;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.buyContract.ContractViewModel;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContractFragment extends Fragment implements OnMapReadyCallback {

    //vars..
    private static final float DEFAULT_ZOOM =16 ;
    private double latittude;
    private double longitude;
    //new location update vars
    double lat ,lng;
    private int quantity;

    private MaterialSearchBar searchBar;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private LinearLayout location_layout;
    private TextView new_location,itemName,price;
    private ImageView itemImage;
    private CheckBox confirmNewLocation;
    private NumberPicker numberPicker;
    private CardView buyCard;
    private ContractViewModel viewModel;


    public ContractFragment() {
        // Required empty public constructor
    }
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_contract, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.contractSet));
        init(v);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();
        fragment.getMapAsync(this);



        initSearchBar();

        return v;

    }

    private void init(View v) {
        viewModel=new ContractViewModel(getActivity());
        searchBar=v.findViewById(R.id.searchBar);
        location_layout=v.findViewById(R.id.newLocationLayout);
        location_layout.setVisibility(View.GONE);
        new_location=v.findViewById(R.id.newLocation);
        itemImage=v.findViewById(R.id.itemImage);
        itemName=v.findViewById(R.id.item_name);
        confirmNewLocation=v.findViewById(R.id.checkBoxLocation);
        price=v.findViewById(R.id.price);
        numberPicker=v.findViewById(R.id.number_picker);
        buyCard=v.findViewById(R.id.buy_card);

        itemName.setText(CurrentItem.item_name);
        Glide.with(getActivity()).load(CurrentItem.item_image).into(itemImage);
        price.setText(CurrentItem.item_price+" JD");



        quantity=numberPicker.getValue();
        numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                quantity=value;
                Toast.makeText(getActivity(), quantity+" "+value, Toast.LENGTH_SHORT).show();
            }
        });

        buyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(confirmNewLocation.isChecked()){
                    lat=latittude;
                    lng=longitude;
                }else{
                    lat=Double.parseDouble(CurrentUserInfo.lat);
                    lng=Double.parseDouble(CurrentUserInfo.lng);
                }

                if(CurrentUserInfo.phone.equals("no phone")){
                    updatePhoneNumber();


                }else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("Confirm your request !")
                            .setContentText("by confirming this , your request will be sent to the shop ")
                            .setConfirmText("ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    viewModel.initBuyProcess(quantity, lat, lng,CurrentUserInfo.phone);
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                }
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        double currentLat=Double.parseDouble(CurrentUserInfo.lat);
        double currentLng=Double.parseDouble(CurrentUserInfo.lng);
        LatLng currentLocation = new LatLng(currentLat,currentLng );
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,DEFAULT_ZOOM));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                geoLocate(latLng);
                location_layout.setVisibility(View.VISIBLE);
                confirmNewLocation.setChecked(false);
            }
        });
    }

    //this method is used to get the details of the location that u clicked on
    private void geoLocate(LatLng latLng){
        Geocoder geocoder=new Geocoder(getActivity());

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
            new_location.setText("New Location is : "+address.getAddressLine(0));
        }

    }
    // this method will move the map to the new location
    private void moveCamera(LatLng latLng,float zoom,String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions options=new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);

    }

    private void initSearchBar(){

        Places.initialize(getActivity(),"AIzaSyDUlj6qD7kNCBM5mAZbMFznS-6c7DlzHq8");
        placesClient=Places.createClient(getActivity());
        final AutocompleteSessionToken token=AutocompleteSessionToken.newInstance();
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                getActivity().startSearch(text.toString(),true,null,true);

            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if(buttonCode==searchBar.BUTTON_NAVIGATION){
                    //open or close

                }else if(buttonCode==searchBar.BUTTON_BACK){
                    searchBar.disableSearch();
                    searchBar.hideSuggestionsList();
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

                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
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

    private void updatePhoneNumber() {
        final EditText editText = new EditText(getActivity());
        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Update your phone number")
                .setCustomView(editText)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if(!TextUtils.isEmpty(editText.getText().toString())) {
                             viewModel.updatePhoneNumber(editText.getText().toString());
                             sDialog.dismissWithAnimation();
                        }
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();

    }

}
