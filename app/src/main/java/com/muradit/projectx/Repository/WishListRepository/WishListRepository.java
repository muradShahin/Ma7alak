package com.muradit.projectx.Repository.WishListRepository;

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
import com.muradit.projectx.Model.others.CurrentUserInfo;

import java.util.ArrayList;
import java.util.List;

public class WishListRepository {
    private DatabaseReference databaseReference,databaseReference2;
    private Context context;
    private MutableLiveData<List<AddItemModel>> wishList;


    public WishListRepository(Context context) {
        this.context = context;
        databaseReference= FirebaseDatabase.getInstance().getReference("WishList");
        databaseReference2=FirebaseDatabase.getInstance().getReference("Items");
        wishList=new MutableLiveData<>();
    }

    private void getUserWishListItems(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot key:dataSnapshot.getChildren()){
                    //stores the current itemID in Global var so i can use it in the second onDataChange method
                    //so we can get the item info
                    final String itemId=key.child("item_id").getValue().toString();

                    if(key.child("user_email").getValue().toString().equals(CurrentUserInfo.email)){
                       //getting the current item information
                        databaseReference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<AddItemModel> items=new ArrayList<>();
                                for (DataSnapshot key:dataSnapshot.getChildren()){
                                    if(itemId.equals(key.getKey())) {
                                       AddItemModel item = new AddItemModel(
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
                                       items.add(item);
                                   }

                                }
                                wishList.setValue(items);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<AddItemModel>> getWishItems(){
        getUserWishListItems();
        return wishList;
    }
}
