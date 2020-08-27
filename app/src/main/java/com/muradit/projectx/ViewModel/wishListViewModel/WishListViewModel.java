package com.muradit.projectx.ViewModel.wishListViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Repository.WishListRepository.WishListRepository;

import java.util.List;

public class WishListViewModel extends ViewModel {
    private WishListRepository repository;
    private Context context;

    public WishListViewModel(Context context) {
        this.context = context;
        repository=new WishListRepository(context);
    }

    public LiveData<List<AddItemModel>> getWishLists(){
        return repository.getWishItems();
    }
}
