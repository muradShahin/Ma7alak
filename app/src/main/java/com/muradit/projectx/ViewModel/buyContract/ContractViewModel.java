package com.muradit.projectx.ViewModel.buyContract;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Model.ContractModel;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Repository.BuyContract.ContractRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ContractViewModel extends ViewModel {
    private ContractRepository contractRepository;
    private Context context;

    public ContractViewModel(Context context) {
        this.context = context;
        contractRepository=new ContractRepository(context);
    }

    public void initBuyProcess(int quantity,double lat,double lng,String phone){
        Date date= Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate=formatter.format(date);
        ContractModel ir=new ContractModel();
        ir.setItem_id(CurrentItem.item_id);
        ir.setStore_email(CurrentItem.store_email);
        ir.setStore_name(CurrentItem.item_store);
        ir.setUser_id(CurrentUserInfo.id);
        ir.setLat(lat);
        ir.setLng(lng);
        ir.setCustomer_phone(phone);
        ir.setQuantity(quantity);
        ir.setDate(currentDate);

        contractRepository.buyItem(ir);
    }
    public void updatePhoneNumber(String number){
        contractRepository.updatePhone(number);
    }
}
