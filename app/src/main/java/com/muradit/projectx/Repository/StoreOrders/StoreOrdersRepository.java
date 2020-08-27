package com.muradit.projectx.Repository.StoreOrders;

import android.content.Context;
import android.util.Log;
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
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Orders;
import com.muradit.projectx.R;

import java.util.ArrayList;
import java.util.List;

public class StoreOrdersRepository {
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;

    private MutableLiveData<List<Orders>>orders;
    private MutableLiveData<List<Orders>>archived;

    private Context context;

    public StoreOrdersRepository(Context context) {
        this.context=context;
        databaseReference1= FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference2= FirebaseDatabase.getInstance().getReference("Archived");

        orders=new MutableLiveData<>();
        archived=new MutableLiveData<>();
    }

    private void getAllOrders(){
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Orders> ordersList=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("store_email").getValue().toString().equals(CurrentStore.email)
                     && key.child("store_name").getValue().toString().equals(CurrentStore.name)){

                        Orders order=new Orders();
                        order.setUser_id(key.child("user_id").getValue().toString());
                        order.setRequest_ID(key.getKey());
                        order.setItem_id(key.child("item_id").getValue().toString());
                        order.setDate(key.child("date").getValue().toString());
                        order.setQuantity(Integer.parseInt(key.child("quantity").getValue().toString()));
                        order.setCustomer_phone(key.child("customer_phone").getValue().toString());
                        order.setLat(Double.parseDouble(key.child("lat").getValue().toString()));
                        order.setLng(Double.parseDouble(key.child("lng").getValue().toString()));
                        order.setStore_email(key.child("store_email").getValue().toString());
                        order.setStore_name(key.child("store_name").getValue().toString());
                        ordersList.add(order);

                    }
                }
                orders.setValue(ordersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void moveToArchive(final Orders order){

        String key=databaseReference2.push().getKey();
        databaseReference2.child(key).setValue(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Requests");
                        databaseReference.child(order.getRequest_ID()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,context.getString(R.string.wellDone) , Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("delOrder",e.getMessage());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("moveToArchive",e.getMessage());

            }
        });

    }

    private void getArchived(){
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              List<Orders> archivedOrders=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("store_email").getValue().toString().equals(CurrentStore.email)
                    && key.child("store_name").getValue().toString().equals(CurrentStore.name)){

                        Orders order=new Orders();
                        order.setUser_id(key.child("user_id").getValue().toString());
                        order.setRequest_ID(key.getKey());
                        order.setItem_id(key.child("item_id").getValue().toString());
                        order.setDate(key.child("date").getValue().toString());
                        order.setQuantity(Integer.parseInt(key.child("quantity").getValue().toString()));
                        order.setCustomer_phone(key.child("customer_phone").getValue().toString());
                        order.setLat(Double.parseDouble(key.child("lat").getValue().toString()));
                        order.setLng(Double.parseDouble(key.child("lng").getValue().toString()));
                        order.setStore_email(key.child("store_email").getValue().toString());
                        order.setStore_name(key.child("store_name").getValue().toString());
                        archivedOrders.add(order);
                    }
                }
                archived.setValue(archivedOrders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void removeOrderFromArchive(String id){
        databaseReference2.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, context.getString(R.string.removed), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public LiveData<List<Orders>> getOrders(){
        getAllOrders();
        return orders;
    }
    public LiveData<List<Orders>> getArchivedList(){
        getArchived();
        return archived;
    }


}
