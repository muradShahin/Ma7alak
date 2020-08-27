package com.muradit.projectx.ViewModel.EditInfoViewModel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Repository.EditInfoRepository.EditRepository;

public class EditViewModel extends ViewModel {
    private EditRepository editRepository;

    public EditViewModel(Context context) {
        editRepository=new EditRepository(context);
    }

    public void updateUserInfo(String name,String email,String phone,String city,String image){
        editRepository.updateUserInfo(name,email,phone,city,image);
    }
}
