package com.muradit.projectx.Repository.StoreOffers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StoreOffersRepository {
    private DatabaseReference databaseReference;
    private MutableLiveData<List<Offers>> allOffers;
    private Context context;

    public StoreOffersRepository(Context context) {
        this.context = context;
        databaseReference= FirebaseDatabase.getInstance().getReference("Offers");
        allOffers=new MutableLiveData<>();

    }
    public void addOffer(String offerContent, String expireDate ,String imageUrl){
        Offers offer=new Offers();
        offer.setStoreId(CurrentStore.id);
        offer.setExpireDate(expireDate);
        offer.setOfferContent(offerContent);
        offer.setOfferImage(imageUrl);

        String key=databaseReference.push().getKey();
        databaseReference.child(key).setValue(offer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setTitleText(context.getString(R.string.offerMessage))
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

    private void getAllOffers(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Offers> offers=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("storeId").getValue().toString().equals(CurrentStore.id)){
                        Offers offer=new Offers();
                        offer.setOfferContent(key.child("offerContent").getValue().toString());
                        offer.setExpireDate(key.child("expireDate").getValue().toString());
                        offer.setOfferImage(key.child("offerImage").getValue().toString());
                        offer.setStoreId(key.child("storeId").getValue().toString());

                        offers.add(offer);
                    }
                }
                allOffers.setValue(offers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Offers>> getOffersList(){
        getAllOffers();
        return allOffers;
    }
}
