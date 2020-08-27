package com.muradit.projectx.Model.others;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetCategories {
    private MutableLiveData<List<Category>> categories;
    private DatabaseReference databaseReference;

    public GetCategories() {
        categories=new MutableLiveData<>();
    }

    private void getAllCategories(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Category> categoryList=new ArrayList<>();
                for (DataSnapshot key :dataSnapshot.getChildren()){
                    categoryList.add(new Category(key.child("type").getValue().toString()));

                }
                categories.setValue(categoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Category>> getCategories(){
        getAllCategories();
        return categories;
    }

}
