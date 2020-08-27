package com.muradit.projectx.ViewModel.allItems;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.Model.others.GetCategories;
import com.muradit.projectx.Repository.allItems.allItemsRepository;

import java.util.List;

public class allItemsViewModel extends ViewModel {
    private allItemsRepository repository;


    public allItemsViewModel(Context context) {
        repository = new allItemsRepository(context);

    }

    public LiveData<List<Category>> getAllCategroies(){
        GetCategories getCategories =new GetCategories();
        return getCategories.getCategories();

    }




    public LiveData<List<AddItemModel>> storesSuggestion(String searchKey){
        return repository.getSearchResult(searchKey);
    }

    public LiveData<List<AddItemModel>> getCategoryItems(String categ){
        return repository.getCategoryItems(categ);
    }

    public LiveData<List<AddItemModel>> getListItems(){

        return repository.getItemsList();
    }
}
