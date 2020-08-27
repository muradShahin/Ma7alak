package com.muradit.projectx.ViewModel.toolsViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Repository.toolsRepository.ToolsRepository;

public class ToolsViewModel extends ViewModel {

    private ToolsRepository repository;
    public ToolsViewModel(Context context) {
        repository=new ToolsRepository(context);
    }

    public void updateLanguage(String lang){
        repository.updateLanguage(lang);
    }

}