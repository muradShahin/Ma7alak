package com.muradit.projectx.Repository.FullDetailsRepository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.ContractModel;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.WishListItems;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentUserInfo;

public class FullDetailsRepository {

    private DatabaseReference databaseReference;
    private MutableLiveData<StoreModel> storeInfo;
    private MutableLiveData<Boolean> isExist;
    private MutableLiveData<Boolean> boughtBefore;
    private FirebaseAuth firebaseAuth;
    private Context context;

    public FullDetailsRepository(Context context) {
        this.context=context;
        storeInfo=new MutableLiveData<>();
        isExist=new MutableLiveData<>();
        boughtBefore=new MutableLiveData<>();
        firebaseAuth=FirebaseAuth.getInstance();


    }

    private void getStoreData(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Stores");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("email").getValue().toString().equals(CurrentItem.store_email)){

                        //List<StoreModel> infoList=new ArrayList<>();
                        StoreModel storeModel=new StoreModel(
                                key.child("image").getValue().toString(),
                                key.child("name").getValue().toString(),
                                key.child("email").getValue().toString(),
                                key.child("phone").getValue().toString(),
                                key.child("city").getValue().toString(),
                                key.child("ownerName").getValue().toString(),
                                key.child("ownerEmail").getValue().toString(),
                                key.child("lat").getValue().toString(),
                                key.child("lng").getValue().toString(),
                                key.child("category").getValue().toString(),
                                key.child("about").getValue().toString()
                        );
                        storeModel.setId(key.getKey());
                        //infoList.add(storeModel);
                        storeInfo.setValue(storeModel);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addItemTowishList(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("WishList");
        String key=databaseReference.push().getKey();
        WishListItems wl=new WishListItems(CurrentItem.item_id,user.getEmail());
        databaseReference.child(key).setValue(wl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void checkifInList(){
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference("WishList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("item_id").getValue().toString().equals(CurrentItem.item_id)
                      && key.child("user_email").getValue().toString().equals(user.getEmail())
                     ){
                        isExist.setValue(true);
                        break;
                    }else{
                        isExist.setValue(false);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public LiveData<StoreModel> getStoreInfo(){
        getStoreData();
        return storeInfo;
    }
    public LiveData<Boolean> itemState(){

        checkifInList();
        return isExist;
    }

}
