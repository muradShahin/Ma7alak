package com.muradit.projectx.ViewModel.OrderDetailsViewModel;

import android.content.Context;
import android.view.View;

import com.muradit.projectx.Repository.OrderDetailsRepository.InvoiceRepsitory;

public class InvoiceViewModel {
    private InvoiceRepsitory repsitory;

    public InvoiceViewModel(Context context,View view) {
        repsitory=new InvoiceRepsitory(context,view);
    }

    public void cancelOreder(String orderId){
        repsitory.cancelOrder(orderId);
    }
}
