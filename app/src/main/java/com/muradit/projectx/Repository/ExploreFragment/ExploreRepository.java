package com.muradit.projectx.Repository.ExploreFragment;

import android.content.Context;
import android.graphics.Color;

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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExploreRepository {

    private MutableLiveData<List<StoreModel>> stores;
    private MutableLiveData<List<AddItemModel>> Items;
    private SweetAlertDialog pd;

    private Context context;
    private DatabaseReference databaseReference,databaseReference2;

    public ExploreRepository(Context context) {
        this.context = context;
        stores=new MutableLiveData<>();
        Items=new MutableLiveData<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Stores");
        databaseReference2=FirebaseDatabase.getInstance().getReference("Items");
    }
    //    public AddItemModel(String item_name, String item_desc, String item_price, String item_category, String item_store, String store_email, String item_image

    private void getAllStores(){
        pd=new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#ffc107"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);
        pd.show();
        final List<StoreModel> storesList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
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
                            key.child("about").getValue().toString()

                    );
                    storeModel.setId(key.getKey());

                    storesList.add(storeModel);

                }
                stores.setValue(storesList);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void getAllItems(){

        final List<AddItemModel> items=new ArrayList<>();

        databaseReference2.addValueEventListener(new ValueEventListener() {
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
                    items.add(item);
                }
                Items.setValue(items);
                pd.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public LiveData<List<StoreModel>> getStores(){
        getAllStores();
        return stores;
    }
    public LiveData<List<AddItemModel>> getItems(){
        getAllItems();
        return Items;

    }




}
