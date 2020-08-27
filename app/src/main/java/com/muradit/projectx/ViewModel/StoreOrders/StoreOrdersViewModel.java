package com.muradit.projectx.ViewModel.StoreOrders;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.muradit.projectx.Model.others.Orders;
import com.muradit.projectx.Repository.StoreOrders.StoreOrdersRepository;

import java.util.List;

public class StoreOrdersViewModel {
    private StoreOrdersRepository repository;

    public StoreOrdersViewModel(Context context) {
        repository=new StoreOrdersRepository(context);
    }

    public void moveToArchive(Orders order){
        repository.moveToArchive(order);
    }
    public void removeFromArchived(String id){
        repository.removeOrderFromArchive(id);
    }
    public LiveData<List<Orders>> getOrders(){
        return repository.getOrders();
    }
    public LiveData<List<Orders>> getArchived(){
        return repository.getArchivedList();
    }
}
