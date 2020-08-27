package com.muradit.projectx.ViewModel.MyStoresItemsVM;

import androidx.lifecycle.LiveData;

import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Repository.MyStoresItems.MyStoresItemsRepoistory;

import java.util.List;

public class MyStoresItemsViewModel {
    private MyStoresItemsRepoistory repoistory;

    public MyStoresItemsViewModel() {
        repoistory=new MyStoresItemsRepoistory();
    }

    public LiveData<List<AddItemModel>> getStoresItems(){
       return repoistory.storesItemsList();
    }
}
