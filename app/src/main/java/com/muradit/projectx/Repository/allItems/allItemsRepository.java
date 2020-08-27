package com.muradit.projectx.Repository.allItems;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.Category;

import java.util.ArrayList;
import java.util.List;

public class allItemsRepository {
    private MutableLiveData<List<AddItemModel>> itemsList;
    private MutableLiveData<List<AddItemModel>> searchItemsList;
    private MutableLiveData<List<AddItemModel>> categoryItemsList;



    private DatabaseReference databaseReference;
    private Context context;

    public allItemsRepository(Context context) {
        itemsList=new MutableLiveData<>();
        searchItemsList=new MutableLiveData<>();
        categoryItemsList=new MutableLiveData<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Items");
        this.context=context;
    }

    private void getAllItems(){
        final List<AddItemModel> list=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot key:dataSnapshot.getChildren()){
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
                    list.add(item);
                }
                itemsList.setValue(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void getSearchItems(final String searchKey){
        databaseReference=FirebaseDatabase.getInstance().getReference("Items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String searchInput=searchKey;
                List<AddItemModel> itemsList=new ArrayList<>();
                if(searchInput.length()==1){
                    searchInput=searchInput.toUpperCase();

                }
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("item_name").getValue().toString().equalsIgnoreCase(searchInput)
                       || key.child("item_name").getValue().toString().startsWith(searchInput)) {

                        AddItemModel item=new AddItemModel(key.child("item_name").getValue().toString(),
                                key.child("item_desc").getValue().toString(),
                                key.child("item_price").getValue().toString(),
                                key.child("item_category").getValue().toString(),
                                key.child("item_store").getValue().toString(),
                                key.child("store_email").getValue().toString(),
                                key.child("item_image").getValue().toString(),
                                key.child("item_state").getValue().toString());
                        item.setItem_id(key.getKey());
                        itemsList.add(item);
                    }

                }
                searchItemsList.setValue(itemsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getItemsOnCategory(final String category){
        databaseReference= FirebaseDatabase.getInstance().getReference("Items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AddItemModel> itemsList =new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(category.equals(key.child("item_category").getValue().toString())){
                        AddItemModel item=new AddItemModel(key.child("item_name").getValue().toString(),
                                key.child("item_desc").getValue().toString(),
                                key.child("item_price").getValue().toString(),
                                key.child("item_category").getValue().toString(),
                                key.child("item_store").getValue().toString(),
                                key.child("store_email").getValue().toString(),
                                key.child("item_image").getValue().toString(),
                                key.child("item_state").getValue().toString());
                        item.setItem_id(key.getKey());
                        itemsList.add(item);
                    }
                }
                categoryItemsList.setValue(itemsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public LiveData<List<AddItemModel>> getItemsList(){
        getAllItems();
        return itemsList;
    }

    public LiveData<List<AddItemModel>> getCategoryItems(String categ){
        getItemsOnCategory(categ);
        return categoryItemsList;
    }

    public LiveData<List<AddItemModel>> getSearchResult(String searchKey){
        getSearchItems(searchKey);
        return searchItemsList;
    }
}
