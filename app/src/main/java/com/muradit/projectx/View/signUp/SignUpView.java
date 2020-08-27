package com.muradit.projectx.View.signUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.LoginSignUpModel;
import com.muradit.projectx.R;
import com.muradit.projectx.ViewModel.signUp.signUpViewModel;

public class SignUpView extends AppCompatActivity {
    private EditText email,password,passwordConfirm,name,phone;
    private Button createBtn;
    private signUpViewModel signUpViewModel;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_view);
        getSupportActionBar().hide();

        email=findViewById(R.id.mail);
        password=findViewById(R.id.password);
        passwordConfirm=findViewById(R.id.passwordConfirm);
        name=findViewById(R.id.full_name);
        phone=findViewById(R.id.phone);
        createBtn=findViewById(R.id.create);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");

        signUpViewModel= ViewModelProviders.of(this).get(signUpViewModel.class);
        signUpViewModel.init(this);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(isFilled())
                   if(checkPhone())
                       if(checkPasswords())
                            createAccount();


            }
        });

    }

    private void createAccount() {
     //   signUpViewModel.init(this);

    signUpViewModel.createAccount(email.getText().toString(),name.getText().toString(),phone.getText().toString(),password.getText().toString());


    }
    private boolean isFilled(){
        boolean Flag=false;

        if(!TextUtils.isEmpty(email.getText().toString()))
            if(!TextUtils.isEmpty(password.getText().toString()))
                if(!TextUtils.isEmpty(passwordConfirm.getText().toString()))
                    if(!TextUtils.isEmpty(name.getText().toString()))
                        if(!TextUtils.isEmpty(phone.getText().toString()))
                            Flag=true;
                        else
                            phone.setError("Required");
                    else
                        name.setError("Required");
                else
                    passwordConfirm.setError("Required");
            else
                password.setError("Required");
        else
            email.setError("Required");

    return Flag;
    }
    private boolean checkPhone(){
        boolean Flag=false;

        String phoneStr=phone.getText().toString();
        if(phoneStr.length()==10){
            int sumOfDigits;
            int d1=Integer.parseInt(phoneStr.charAt(0)+"");
            int d2=Integer.parseInt(phoneStr.charAt(1)+"");
            int d3=Integer.parseInt(phoneStr.charAt(2)+"");
            sumOfDigits=d1+d2+d3;
            if(sumOfDigits>=14 && sumOfDigits<=16)
                Flag=true;

        }else{
            phone.setError("not valid");
        }


        return Flag;
    }
    private boolean checkPasswords(){
        boolean Flag=false;
        if(password.getText().toString().equals(passwordConfirm.getText().toString()))
            Flag=true;
        else
            passwordConfirm.setError("doesn't match");

        return Flag;
    }

}
