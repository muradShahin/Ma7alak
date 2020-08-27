package com.muradit.projectx.ViewModel.moreFragment;

import android.content.Context;

import androidx.lifecycle.ViewModel;


import com.muradit.projectx.Repository.moreFragment.moreFragmentRepository;

public class moreFragmentViewModel extends ViewModel {
    private moreFragmentRepository repository;
    private Context context;

    public moreFragmentViewModel(Context context) {
        repository=new moreFragmentRepository(context);
        this.context=context;
    }

    public void editInfo(String itemImageUri, String name, String desc, String price, String category){
        repository.UpdateFirebase(itemImageUri,name,desc,price,category);
    }
}
