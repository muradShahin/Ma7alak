package com.muradit.projectx.ViewModel.MapsViewModel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.muradit.projectx.Repository.MapsActivity.MapsRepository;

public class MapsViewModel extends ViewModel {

    private Context context;
    private MapsRepository mapsRepository;

    public MapsViewModel(Context context) {
        this.context = context;
        mapsRepository=new MapsRepository(context);
    }

    public void updateStoreLocation(String lat,String lng){
        mapsRepository.updateLocation(lat,lng);
    }
}
