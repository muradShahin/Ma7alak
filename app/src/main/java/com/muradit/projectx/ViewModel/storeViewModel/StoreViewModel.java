package com.muradit.projectx.ViewModel.storeViewModel;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Repository.StoreFragment.StoreRepository;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;



public class StoreViewModel extends ViewModel {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Context context;
    private SweetAlertDialog pd;
    private MutableLiveData<Boolean> exist;
    private MutableLiveData<Boolean> created;
    private LiveData<List<StoreModel>> storeInformation;
    private StoreRepository storeRepository;


    public StoreViewModel(Context context) {
        storeRepository=new StoreRepository(context);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Stores");
        exist=new MutableLiveData<>();
        created=new MutableLiveData<>();
        this.context=context;

    }


    public boolean checkIfLoggedIn() {
        if (firebaseAuth.getCurrentUser() != null)
            return true;
        return false;
    }

    public void createStore(String name, String email, String phone, String city, String imageUri,String lat,String lng,String category,String about) {
        String ownerEmail=CurrentUserInfo.email;
        String ownerName= CurrentUserInfo.name;


           String key=databaseReference.push().getKey();
        StoreModel storeModel=new StoreModel(imageUri,name,email,phone,city,ownerName,ownerEmail,lat,lng,category,about);
        databaseReference.child(key).setValue(storeModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        created.setValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

   public LiveData<Boolean> getResult(){
       return exist;
   }
   public LiveData<Boolean> isCreated(){
        return created;
   }


    public void createdAstoreBefore(){
        pd=new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);
        pd.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("ownerEmail").getValue().toString().equals(CurrentUserInfo.email)){
                        exist.setValue(true);
                        pd.dismiss();
                        break;
                    }else{
                        exist.setValue(false);
                    }
                }
                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

  public void getStore(){
      storeRepository.getStoreInfo();
      storeInformation=storeRepository.getInfo();
  }

  public void editBio(String storeKey,String bioString){
        storeRepository.updateStoreBio(storeKey,bioString);
  }



    public LiveData<List<StoreModel>> getStoreData(){

        return storeInformation;
    }





}