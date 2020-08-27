package com.muradit.projectx.Repository.MapsActivity;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.others.CurrentStore;

import java.util.HashMap;
import java.util.Map;

public class MapsRepository  {
    private DatabaseReference databaseReference;
    private Context context;

    public MapsRepository(Context context) {
        this.context = context;
        databaseReference= FirebaseDatabase.getInstance().getReference("Stores");
    }

    public void updateLocation(String lat,String lng){
        Map<String ,Object> map=new HashMap<>();
        map.put("lat",lat);
        map.put("lng",lng);

        databaseReference.child(CurrentStore.id).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "updated !", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
}
