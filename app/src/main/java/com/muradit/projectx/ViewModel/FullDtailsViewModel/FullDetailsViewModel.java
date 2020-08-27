package com.muradit.projectx.ViewModel.FullDtailsViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Repository.FullDetailsRepository.FullDetailsRepository;

import java.util.List;

public class FullDetailsViewModel extends ViewModel {

    private FullDetailsRepository repository;
    private Context context;

    public FullDetailsViewModel(Context context) {
        this.context=context;
        repository=new FullDetailsRepository(context);


    }

    public void addToWishList(){
        repository.addItemTowishList();
    }

    public LiveData<StoreModel> getStoreInfoFromRepository(){

        return repository.getStoreInfo();
    }

    public LiveData<Boolean> isExist(){

        return repository.itemState();
    }
}
