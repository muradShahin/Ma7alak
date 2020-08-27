package com.muradit.projectx.ViewModel.profileViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muradit.projectx.Model.ContractModel;
import com.muradit.projectx.Model.Follow;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Orders;
import com.muradit.projectx.Repository.ProfileRepository.ProfileRepository;

import java.util.List;
import java.util.Map;

public class ProfileViewModel extends ViewModel {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProfileRepository repository;

   public ProfileViewModel(Context context){
       firebaseAuth=FirebaseAuth.getInstance();
       firebaseUser=firebaseAuth.getCurrentUser();
       repository=new ProfileRepository(context);
   }

   public String getImageUri(){
       String imageUri;
       if (CurrentUserInfo.image.equals("no image"))
           if (firebaseUser.getPhotoUrl()!=null)
                imageUri=firebaseUser.getPhotoUrl().toString();
           else
               imageUri= CurrentUserInfo.image;

       else
           imageUri= CurrentUserInfo.image;

        return imageUri;
   }

   public String getUserName(){
       return CurrentUserInfo.name;
   }

   public int getRate(){
       return 5;
   }

   public boolean checkIfLoggedIn(){
       if(firebaseAuth.getCurrentUser() !=null)
           return true;
       return false;
   }

   public void unFollowStore(String storeId){
       repository.unFollowStore(storeId);
   }

   public LiveData<List<Follow>> getAllFollowers(){
       return repository.getFollowers();
   }

   public LiveData<Map<String,Object>> getMissingInfo(){
       return repository.getMissingInfo() ;
   }

   public LiveData<List<Orders>> getOrders(){
       return repository.getOrders();
   }
}