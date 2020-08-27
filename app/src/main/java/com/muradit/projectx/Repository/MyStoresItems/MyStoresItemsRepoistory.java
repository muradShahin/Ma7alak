package com.muradit.projectx.Repository.MyStoresItems;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.others.CurrentUserInfo;

import java.util.ArrayList;
import java.util.List;

public class MyStoresItemsRepoistory {
    private DatabaseReference databaseReference;
    private MutableLiveData<List<AddItemModel>> storeItems;
    List<AddItemModel> items=new ArrayList<>();

    public MyStoresItemsRepoistory() {
        storeItems=new MutableLiveData<>();
    }

    private void getAllStoreItems(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Followers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("userId").getValue().toString().equals(CurrentUserInfo.id)){

                        getItems(key.child("storeId").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getItems(final String storeId) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("store_id").getValue().toString().equals(storeId)){
                        AddItemModel item=new AddItemModel(key.child("item_name").getValue().toString(),
                                key.child("item_desc").getValue().toString(),
                                key.child("item_price").getValue().toString(),
                                key.child("item_category").getValue().toString(),
                                key.child("item_store").getValue().toString(),
                                key.child("store_email").getValue().toString(),
                                key.child("item_image").getValue().toString(),
                                key.child("item_state").getValue().toString());
                        item.setStore_id(key.child("store_id").getValue().toString());
                        item.setItem_id(key.getKey());
                        items.add(item);
                    }
                }
                storeItems.setValue(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public LiveData<List<AddItemModel>> storesItemsList(){
        getAllStoreItems();
        return storeItems;
    }
}
