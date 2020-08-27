package com.muradit.projectx.Repository.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class loginRepository {

    private static loginRepository instance;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Context context;
    public  boolean FLAG;


    public loginRepository(Context context) {
        firebaseDatabase =FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        firebaseAuth=FirebaseAuth.getInstance();
        this.context=context;


    }

    public static loginRepository getInstance(Context context){
        if(instance==null)
            instance=new loginRepository(context);
        return instance;
    }

    public void addUserData(final String id, final LoginSignUpModel loginSignUpModel){

        checkIfAddedBefore(id);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!FLAG)
                    addInfo(id,loginSignUpModel);
                else
                    updateLocation();
            }
        }, 3000);



    }
    private void checkIfAddedBefore(final String id){
        FLAG=false;
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(id))
                        FLAG=true;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void addInfo(String id,LoginSignUpModel loginSignUpModel){
          firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");

        databaseReference.child(id).setValue(loginSignUpModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

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








}
