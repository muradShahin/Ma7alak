package com.muradit.projectx.Repository.SalesRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.Offers;

import java.util.ArrayList;
import java.util.List;

public class SalesRepository {
    private DatabaseReference databaseReference;
    private MutableLiveData<List<Offers>> Offers;

    public SalesRepository() {
        databaseReference= FirebaseDatabase.getInstance().getReference("Offers");
        Offers=new MutableLiveData<>();
    }

    private void getAllOffers(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Offers> offersList=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    Offers offer=new Offers();
                    offer.setStoreId(key.child("storeId").getValue().toString());
                    offer.setExpireDate(key.child("expireDate").getValue().toString());
                    offer.setOfferImage(key.child("offerImage").getValue().toString());
                    offer.setOfferContent(key.child("offerContent").getValue().toString());

                    offersList.add(offer);
                }
                Offers.setValue(offersList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Offers>> getOffers(){
        getAllOffers();
        return Offers;
    }
}
