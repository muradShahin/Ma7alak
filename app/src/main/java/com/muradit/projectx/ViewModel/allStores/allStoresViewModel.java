package com.muradit.projectx.ViewModel.allStores;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Repository.allStores.allStoresRepository;

import java.util.List;

public class allStoresViewModel extends ViewModel {
    private allStoresRepository repository;

    public allStoresViewModel(Context context) {
        repository=new allStoresRepository(context);
    }

    public LiveData<List<StoreModel>> getAllStores(){
        return repository.getStoresList();
    }

    public LiveData<List<StoreModel>> getStoreByCategory(String categ){

        return repository.getStoresCategory(categ);
    }

}
