package com.muradit.projectx.Repository.popWindow;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.others.Category;

import java.util.ArrayList;
import java.util.List;

public class popWindowRepository {

    private DatabaseReference databaseReference;
    private MutableLiveData<List<Category>> categories;
    private Context context;

    public popWindowRepository(Context context) {
        this.context = context;
        categories=new MutableLiveData<>();
    }

    private void getAllCategories(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Category> categoryList=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    categoryList.add(new Category(key.child("type").getValue().toString()));

                }

                categories.setValue(categoryList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public LiveData<List<Category>> getCategoriesList(){
        getAllCategories();
        return categories;
    }



}
