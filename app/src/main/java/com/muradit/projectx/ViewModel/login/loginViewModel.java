package com.muradit.projectx.ViewModel.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.muradit.projectx.Model.LoginSignUpModel;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.GetCurrentLocation;
import com.muradit.projectx.Repository.Login.loginRepository;
import com.muradit.projectx.View.MainActivity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class loginViewModel extends ViewModel {
    private loginRepository loginRepo;
    private FirebaseAuth firebaseAuth;
    private ArrayList userData;
    private Context context;
 //   private Object mBinding;

    public void init(Context context){
        this.context=context;
        firebaseAuth=FirebaseAuth.getInstance();
        loginRepo=loginRepository.getInstance(context);


    }
    private void openMain(){
        Intent i=new Intent(context, MainActivity.class);
        context.startActivity(i);
        ((AppCompatActivity)context).finish();

    }

    // this segment of code to handle regular firebase authentication (email ,password)
    public void verifiedUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        GetCurrentLocation getCurrentLocation=new GetCurrentLocation(context);
                        getCurrentLocation.getLocation();
                        getCurrentLocation.getLocationState().observe((LifecycleOwner) context, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                openMain();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void addUserToDatabase(String id,String email,String name,String password,String phone,String image) {
        LoginSignUpModel lsm = new LoginSignUpModel();
        lsm.setEmail(email);
        lsm.setName(name);
        lsm.setPassword(password);
        lsm.setPhone(phone);
        lsm.setLng(CurrentUserInfo.lng);
        lsm.setLat(CurrentUserInfo.lat);
        lsm.setImage(image);
        lsm.setCity("amman");
        lsm.setLang("en");
        loginRepo.addUserData(id, lsm);

    }




















}
