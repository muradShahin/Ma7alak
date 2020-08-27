package com.muradit.projectx.Repository.MainActivityRepository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.LoginSignUpModel;
import com.muradit.projectx.Model.others.CurrentUserInfo;

import java.util.HashMap;
import java.util.Map;


public class MainRepository {
    private DatabaseReference databaseReference;
    private MutableLiveData<LoginSignUpModel> userInformation;
    private FirebaseAuth firebaseAuth;
    private Context context;

    public MainRepository(Context context) {
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        userInformation=new MutableLiveData<>();
        firebaseAuth=FirebaseAuth.getInstance();
        this.context=context;
    }

    private void getUserData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(firebaseAuth.getCurrentUser() !=null) {
                        if (key.getKey().equals(firebaseAuth.getCurrentUser().getUid())) {
                            LoginSignUpModel user = new LoginSignUpModel();
                            user.setName(key.child("name").getValue().toString());
                            user.setEmail(key.child("email").getValue().toString());
                            user.setPassword(key.child("password").getValue().toString());
                            user.setCity(key.child("city").getValue().toString());
                            user.setPhone(key.child("phone").getValue().toString());
                            user.setImage(key.child("image").getValue().toString());
                            if (key.child("lat").getValue() == null)
                                user.setLat("0000");
                            else
                                user.setLat(key.child("lat").getValue().toString());

                            if (key.child("lng").getValue() == null)
                                user.setLat("0000");
                            else
                                user.setLat(key.child("lng").getValue().toString());


                            user.setLang(key.child("lang").getValue().toString());
                            user.setId(key.getKey());

                            userInformation.setValue(user);
                            break;

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateLocation(){
        Map<String ,Object> map=new HashMap<>();
        map.put("lat", CurrentUserInfo.lat);
        map.put("lng",CurrentUserInfo.lng);

        databaseReference.child(CurrentUserInfo.id).updateChildren(map)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }
    public LiveData<LoginSignUpModel> getUserInfo(){
        getUserData();
        return userInformation;
    }

}
