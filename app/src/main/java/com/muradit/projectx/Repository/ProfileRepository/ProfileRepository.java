package com.muradit.projectx.Repository.ProfileRepository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.ContractModel;
import com.muradit.projectx.Model.Follow;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Orders;
import com.muradit.projectx.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileRepository {
    private DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    private Context context;
    private MutableLiveData<Map<String,Object>> missingInfo;
    private MutableLiveData<List<Orders>> ordersList;
    private MutableLiveData<List<Follow>>followers;

    public ProfileRepository(Context context) {
        this.context = context;
        missingInfo=new MutableLiveData<>();
        ordersList=new MutableLiveData<>();
        followers=new MutableLiveData<>();
    }
    private void checkWhatsMissing(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> map=new HashMap<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(CurrentUserInfo.id)){
                            map.put("email",key.child("email").getValue().toString());
                            map.put("phone",key.child("phone").getValue().toString());
                            map.put("image",key.child("image").getValue().toString());
                    }

                }
                missingInfo.setValue(map);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllOrders(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Requests");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Orders> list=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("user_id").getValue().toString().equals(CurrentUserInfo.id)){
                        Orders order=new Orders();
                        order.setDate(key.child("date").getValue().toString());
                        order.setCustomer_phone(key.child("customer_phone").getValue().toString());
                        order.setLat(Double.parseDouble(key.child("lat").getValue().toString()));
                        order.setLng(Double.parseDouble(key.child("lng").getValue().toString()));
                        order.setQuantity(Integer.parseInt(key.child("quantity").getValue().toString()));
                        order.setStore_name(key.child("store_name").getValue().toString());
                        order.setStore_email(key.child("store_email").getValue().toString());
                        order.setItem_id(key.child("item_id").getValue().toString());
                        String item_id=key.child("item_id").getValue().toString();
                        order.setRequest_ID(key.getKey());

                       getItemInfo(item_id,list,order);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getItemInfo(final String item_id, final List<Orders> orders, final Orders order){
        databaseReference2=FirebaseDatabase.getInstance().getReference("Items");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(item_id)){
                        AddItemModel item=new AddItemModel(key.child("item_name").getValue().toString(),
                                key.child("item_desc").getValue().toString(),
                                key.child("item_price").getValue().toString(),
                                key.child("item_category").getValue().toString(),
                                key.child("item_store").getValue().toString(),
                                key.child("store_email").getValue().toString(),
                                key.child("item_image").getValue().toString(),
                                key.child("item_state").getValue().toString());
                        item.setItem_id(key.getKey());
                        order.setItemInfo(item);
                        orders.add(order);

                    }
                }
                ordersList.setValue(orders);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllFollowers(){
        databaseReference=FirebaseDatabase.getInstance().getReference("Followers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Follow> followList=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("userId").getValue().toString().equals(CurrentUserInfo.id)){
                        Follow follower=new Follow(
                                key.child("userId").getValue().toString(),
                                key.child("storeId").getValue().toString());
                        follower.setRelKey(key.getKey());
                        followList.add(follower);


                    }
                }
                followers.setValue(followList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void unFollowStore(final String keyRel){
        databaseReference=FirebaseDatabase.getInstance().getReference("Followers");
        databaseReference.child(keyRel).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, context.getString(R.string.unFollowMss), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    public LiveData<List<Follow>> getFollowers(){
        getAllFollowers();
        return followers;
    }
    public LiveData<Map<String,Object>> getMissingInfo(){
        checkWhatsMissing();
        return missingInfo;
    }
    public LiveData<List<Orders>> getOrders(){
        getAllOrders();
        return ordersList;
    }

}
