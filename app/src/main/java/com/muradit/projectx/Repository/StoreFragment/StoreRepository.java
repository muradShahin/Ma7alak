package com.muradit.projectx.Repository.StoreFragment;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreRepository {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private MutableLiveData<List<StoreModel>> storeInfo;
    private FirebaseAuth firebaseAuth;
    private Context context;
    public StoreRepository(Context context) {
        this.context=context;
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Stores");
        storeInfo=new MutableLiveData<>();

    }
    public void getStoreInfo(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("ownerEmail").getValue().toString().equals(CurrentUserInfo.email)){
                        String image=key.child("image").getValue().toString();
                        String email=key.child("email").getValue().toString();
                        String name=key.child("name").getValue().toString();
                        String city=key.child("city").getValue().toString();
                        String phone=key.child("phone").getValue().toString();
                        String ownerEmail=key.child("ownerEmail").getValue().toString();
                        String ownerName=key.child("ownerName").getValue().toString();
                        String lat=key.child("lat").getValue().toString();
                        String lng=key.child("lng").getValue().toString();
                        String category=key.child("category").getValue().toString();
                        String about=key.child("about").getValue().toString();

                        StoreModel sm=new StoreModel( image,  name,  email,  phone,  city,  ownerName,  ownerEmail,lat,lng,category,about);
                        sm.setId(key.getKey());
                        List<StoreModel> list=new ArrayList<>();
                        list.add(sm);
                        storeInfo.setValue(list);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void updateStoreBio(String storeKey,String bioTxt){
        Map<String,Object> map=new HashMap<>();
        map.put("about",bioTxt);
        databaseReference.child(storeKey).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, context.getString(R.string.updatededBio), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }






    public LiveData<List<StoreModel>> getInfo(){
        return storeInfo;
    }
}
