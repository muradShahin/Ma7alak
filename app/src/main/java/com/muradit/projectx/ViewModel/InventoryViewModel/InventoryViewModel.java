package com.muradit.projectx.ViewModel.InventoryViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Repository.Inventory.InventoryReporistory;

import java.util.List;

public class InventoryViewModel extends ViewModel {
    private InventoryReporistory inventoryReporistory;
    private LiveData<List<AddItemModel>> itemsList;

    public InventoryViewModel() {
        inventoryReporistory=new InventoryReporistory();
    }
    public LiveData<List<AddItemModel>> getItems(){
      itemsList=inventoryReporistory.getItemsSet();
      return itemsList;
    }
}
