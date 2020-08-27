package com.muradit.projectx.Repository.Inventory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.View.addItemsFragment.AddItems;

import java.util.ArrayList;
import java.util.List;

public class InventoryReporistory {
    private DatabaseReference databaseReference;
    private MutableLiveData<List<AddItemModel>> itemInfo;
    private List<AddItemModel> itemsList;

    public InventoryReporistory() {
        databaseReference= FirebaseDatabase.getInstance().getReference("Items");
        itemInfo=new MutableLiveData<>();
        itemsList=new ArrayList<>();
    }
    public LiveData<List<AddItemModel>> getItemsSet(){
        getItemsFromFirebase();
        return itemInfo;

    }
    private void getItemsFromFirebase(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){

                    if(key.child("store_id").getValue().toString().equals(CurrentStore.id)){
                        AddItemModel item=new AddItemModel(
                                key.child("item_name").getValue().toString(),
                                key.child("item_desc").getValue().toString(),
                                key.child("item_price").getValue().toString(),
                                key.child("item_category").getValue().toString(),
                                key.child("item_store").getValue().toString(),
                                key.child("store_email").getValue().toString(),
                                key.child("item_image").getValue().toString(),
                                key.child("item_state").getValue().toString()
                        );
                        item.setStore_id(key.child("store_id").getValue().toString());
                        item.setItem_id(key.getKey());
                        itemsList.add(item);
                    }
                }
                itemInfo.setValue(itemsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
