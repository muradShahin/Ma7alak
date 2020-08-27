package com.muradit.projectx.Repository.signUp;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.LoginSignUpModel;
import com.muradit.projectx.View.login.loginView;
import com.muradit.projectx.View.signUp.SignUpView;

public class signUpRepository {

    private static signUpRepository instance;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    public signUpRepository() {



    }

    public static signUpRepository getInstance(){
        if(instance==null)
            instance=new signUpRepository();
        return instance;
    }

    public void addUserData(String email, String name, String phone, String password, final Context context){
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();


        LoginSignUpModel lsm=new LoginSignUpModel();
        lsm.setEmail(email); lsm.setName(name); lsm.setPassword(password); lsm.setPhone(phone);
        lsm.setLng("lng"); lsm.setLat("lat"); lsm.setImage("no image"); lsm.setCity("amman"); lsm.setLang("en");
        databaseReference.child(user.getUid()).setValue(lsm)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(context, "created successfully", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(context, loginView.class);
                            context.startActivity(i);
                           ((AppCompatActivity)context).finish();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

            }
        });
    }
}
