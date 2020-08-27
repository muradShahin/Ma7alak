package com.muradit.projectx.ViewModel.MainActivityViewModel;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Model.LoginSignUpModel;
import com.muradit.projectx.Repository.MainActivityRepository.MainRepository;

public class MainViewModel extends ViewModel {

     private MainRepository repository;
     private Context context;
    public MainViewModel(Context context) {
        repository=new MainRepository(context);
        this.context=context;
    }


    public void updateLocation(){
        repository.updateLocation();
    }
    public LiveData<LoginSignUpModel> getUserInfo(){
      return   repository.getUserInfo();
    }




}
