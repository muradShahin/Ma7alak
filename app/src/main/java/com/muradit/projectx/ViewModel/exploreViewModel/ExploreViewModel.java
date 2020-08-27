package com.muradit.projectx.ViewModel.exploreViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Repository.ExploreFragment.ExploreRepository;

import java.util.List;

public class ExploreViewModel extends ViewModel {

    private LiveData<List<StoreModel>> listOfStores;
    private Context context;
    private ExploreRepository exploreRepository;

    public ExploreViewModel(Context context) {
        listOfStores=new MutableLiveData<>();
        exploreRepository=new ExploreRepository(context);
        this.context=context;


    }

    public LiveData<List<StoreModel>> getStores() {
        listOfStores=exploreRepository.getStores();
        return listOfStores;
    }
    public LiveData<List<AddItemModel>> getItems(){
        return exploreRepository.getItems();
    }
}