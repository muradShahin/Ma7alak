package com.muradit.projectx.ViewModel.SalesViewModel;

import androidx.lifecycle.LiveData;

import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Repository.SalesRepository.SalesRepository;

import java.util.List;

public class SalesViewModel {
    private SalesRepository repository;

    public SalesViewModel() {
        repository=new SalesRepository();
    }

    public LiveData<List<Offers>> getAllOffers(){
        return  repository.getOffers();
    }
}
