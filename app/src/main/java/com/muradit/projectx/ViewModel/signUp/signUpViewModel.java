package com.muradit.projectx.ViewModel.signUp;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.LoginSignUpModel;
import com.muradit.projectx.Repository.signUp.signUpRepository;

public class signUpViewModel extends ViewModel {

   private signUpRepository signRepository;
   private FirebaseAuth firebaseAuth;

   private Context context;
    public void init(Context context){
        this.context=context;
        firebaseAuth=FirebaseAuth.getInstance();
        signRepository=signUpRepository.getInstance();

    }
   public void createAccount(final String email, final String name, final String phone , final String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        signRepository.addUserData(email,name,phone,password,context);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }

}
