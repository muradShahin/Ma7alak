package com.muradit.projectx.ViewModel.popViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.Repository.popWindow.popWindowRepository;

import java.util.List;

public class popViewModel extends ViewModel {

    private popWindowRepository repository;

    public popViewModel(Context context) {
        repository=new popWindowRepository(context);
    }

    public LiveData<List<Category>> getCategoriesList(){
        return repository.getCategoriesList();
    }
}
