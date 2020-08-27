package com.muradit.projectx.ViewModel.InsideStore;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Repository.InsideStore.InsideRepository;

import java.util.List;

public class InsideViewModel extends ViewModel {
    private InsideRepository repository;

    public InsideViewModel(Context context) {
        repository=new InsideRepository(context);
    }

    public void followStore(){
        repository.followStore();}

   public void unFollowStore(String keyRel){
        repository.unFollowStore(keyRel);
    }
    public LiveData<String> getRelKey(){
       return repository.getRelKey();
    }

    public LiveData<Boolean> checkIfFollowed(){
        return repository.getFollowResult();
    }

    public LiveData<List<AddItemModel>> getProducts(){
        return repository.getProducts();
    }

    public LiveData<List<Offers>>getStoreOffers(){
       return repository.getOffers();
    }
}
