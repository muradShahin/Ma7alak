package com.muradit.projectx.Repository.allStores;

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
import com.muradit.projectx.Model.others.CurrentUserInfo;

import java.util.ArrayList;
import java.util.List;

public class allStoresRepository {
    private MutableLiveData<List<StoreModel>> stores;
    private MutableLiveData<List<StoreModel>> categoryStores;

    private DatabaseReference databaseReference;
    private Context context;

    public allStoresRepository(Context context) {
        this.context = context;
        stores=new MutableLiveData<>();
        categoryStores=new MutableLiveData<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Stores");
    }

    private void getAllStores(){
        final List<StoreModel> storeModels=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key :dataSnapshot.getChildren()){

                    if(key.child("ownerEmail").getValue().toString().equals(CurrentUserInfo.email))
                        continue;
                    StoreModel storeModel=new StoreModel(
                            key.child("image").getValue().toString(),
                            key.child("name").getValue().toString(),
                            key.child("email").getValue().toString(),
                            key.child("phone").getValue().toString(),
                            key.child("city").getValue().toString(),
                            key.child("ownerName").getValue().toString(),
                            key.child("ownerEmail").getValue().toString(),
                            key.child("lat").getValue().toString(),
                            key.child("lng").getValue().toString(),
                            key.child("category").getValue().toString(),
                            key.child("about").getValue().toString());
                    storeModel.setId(key.getKey());

                    storeModels.add(storeModel);
                }
                stores.setValue(storeModels);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getStoresOnCategory(final String category){
        databaseReference= FirebaseDatabase.getInstance().getReference("Stores");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<StoreModel> storeList =new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(category.equals(key.child("category").getValue().toString())){
                            StoreModel store=new StoreModel();
                            store.setId(key.getKey());
                            store.setAbout(key.child("about").getValue().toString());
                            store.setCity(key.child("city").getValue().toString());
                            store.setCategory(key.child("category").getValue().toString());
                            store.setEmail(key.child("email").getValue().toString());
                            store.setOwnerEmail(key.child("ownerEmail").getValue().toString());
                            store.setOwnerName(key.child("ownerName").getValue().toString());
                            store.setPhone(key.child("phone").getValue().toString());
                            store.setLat(key.child("lat").getValue().toString());
                            store.setLng(key.child("lng").getValue().toString());
                            store.setImage(key.child("image").getValue().toString());
                            store.setName(key.child("name").getValue().toString());

                            storeList.add(store);
                    }
                }
                categoryStores.setValue(storeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public LiveData<List<StoreModel>> getStoresList(){
        getAllStores();
        return stores;
    }
    public LiveData<List<StoreModel>> getStoresCategory(String categ){
        getStoresOnCategory(categ);
        return categoryStores;
    }
}
