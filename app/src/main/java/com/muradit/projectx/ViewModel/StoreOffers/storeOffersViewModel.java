package com.muradit.projectx.ViewModel.StoreOffers;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Repository.StoreOffers.StoreOffersRepository;

import java.util.List;

public class storeOffersViewModel {
    private StoreOffersRepository repository;

    public storeOffersViewModel(Context context) {
        repository=new StoreOffersRepository(context);
    }

    public void addOffer(String offerContent,String expireDate,String imageUrl){
        repository.addOffer(offerContent,expireDate,imageUrl);
    }
    public LiveData<List<Offers>> getAllOffers(){
        return repository.getOffersList();
    }
}
