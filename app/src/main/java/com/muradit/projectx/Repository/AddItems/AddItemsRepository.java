package com.muradit.projectx.Repository.AddItems;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.View.addItemsFragment.AddItems;

public class AddItemsRepository {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Context context;
    private MutableLiveData<Boolean> uploaded;

    public AddItemsRepository(Context context) {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Items");
        uploaded=new MutableLiveData<>();
        this.context=context;
    }

    public void postItem(String itemN,String image,String desc,String price,String category,String state){
        String key=databaseReference.push().getKey();
        AddItemModel addItemModel=new AddItemModel(itemN,desc,price,category, CurrentStore.name,CurrentStore.email,image,state);
        addItemModel.setStore_id(CurrentStore.id);

        databaseReference.child(key).setValue(addItemModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "added successfully ", Toast.LENGTH_SHORT).show();
                        uploaded.setValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }

    public LiveData<Boolean> getStatus(){
        return uploaded;
    }
}
