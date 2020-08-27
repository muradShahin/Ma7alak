package com.muradit.projectx.Repository.InsideStore;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.Follow;
import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InsideRepository {
    private DatabaseReference databaseReference;
    private MutableLiveData<List<AddItemModel>> products;
    private MutableLiveData<List<Offers>> StoreOffers;
    private MutableLiveData<Boolean> isFollwed;
    private MutableLiveData<String>keyRel;
    private Context context;

    public InsideRepository(Context context) {
        this.context=context;
        products=new MutableLiveData<>();
        isFollwed=new MutableLiveData<>();
        StoreOffers=new MutableLiveData<>();
        keyRel=new MutableLiveData<>();
    }

    private void getAllProducts(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AddItemModel> listOfItems=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(CurrentStore.email.equals(key.child("store_email").getValue().toString())
                      || CurrentStore.name.equals(key.child("item_store").getValue().toString())){
                        AddItemModel item=new AddItemModel(
                                key.child("item_name").getValue().toString(),
                                key.child("item_desc").getValue().toString(),
                                key.child("item_price").getValue().toString(),
                                key.child("item_category").getValue().toString(),
                                key.child("item_store").getValue().toString(),
                                key.child("store_email").getValue().toString(),
                                key.child("item_image").getValue().toString(),
                                key.child("item_state").getValue().toString()
                        );
                        item.setStore_id(key.child("store_id").getValue().toString());
                        item.setItem_id(key.getKey());
                        listOfItems.add(item);
                    }
                }
                products.setValue(listOfItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void followStore(){
        databaseReference=FirebaseDatabase.getInstance().getReference("Followers");
        String key=databaseReference.push().getKey();
        Follow follow=new Follow(CurrentUserInfo.id,CurrentStore.id);
        databaseReference.child(key).setValue(follow)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setTitleText(context.getString(R.string.followMessage))
                                .setContentText(context.getString(R.string.followMessage2))
                                .setCustomImage(R.drawable.ic_done)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void checkIfFollwed(){
        isFollwed.setValue(false);
        keyRel.setValue("notFollowed");
        databaseReference=FirebaseDatabase.getInstance().getReference("Followers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("userId").getValue().toString().equals(CurrentUserInfo.id)
                      && key.child("storeId").getValue().toString().equals(CurrentStore.id)){
                        isFollwed.setValue(true);
                        keyRel.setValue(key.getKey());
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void unFollowStore(String keyRel){
         databaseReference=FirebaseDatabase.getInstance().getReference("Followers");
        databaseReference.child(keyRel).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, context.getString(R.string.unFollowMss), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void getStoreOffers(){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Offers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Offers> offersList=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("storeId").getValue().toString().equals(CurrentStore.id)){
                        Offers offer=new Offers();
                        offer.setStoreId(key.child("storeId").getValue().toString());
                        offer.setOfferContent(key.child("offerContent").getValue().toString());
                        offer.setOfferImage(key.child("offerImage").getValue().toString());
                        offer.setExpireDate(key.child("expireDate").getValue().toString());
                        offersList.add(offer);
                    }
                }
                StoreOffers.setValue(offersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<AddItemModel>> getProducts(){
        getAllProducts();
        return products;
    }

    public LiveData<Boolean> getFollowResult(){
        checkIfFollwed();
        return isFollwed;
    }
    public LiveData<List<Offers>> getOffers(){
        getStoreOffers();
        return StoreOffers;
    }
    public LiveData<String> getRelKey(){
        return keyRel;
    }
}
