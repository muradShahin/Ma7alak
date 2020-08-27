package com.muradit.projectx.ViewModel.addItemsViewModel;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Repository.AddItems.AddItemsRepository;

public class addItemsViewModel extends ViewModel {
    private AddItemsRepository repository;
    private LiveData<Boolean>uploaded;
    private Context context;

    public addItemsViewModel(Context context) {
        repository=new AddItemsRepository(context);
        this.context=context;
    }

    public void postItemInit(String itemN,String desc,String price,String category,String image,String state){
        repository.postItem(itemN,image,desc,price,category,state);
    }
    public LiveData<Boolean> getUploadedStatus(){
        uploaded=repository.getStatus();
        return uploaded;
    }

}
