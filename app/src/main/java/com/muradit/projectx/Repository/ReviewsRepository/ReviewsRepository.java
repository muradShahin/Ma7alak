package com.muradit.projectx.Repository.ReviewsRepository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.Reviews;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ReviewsRepository {
    private DatabaseReference databaseReference;
    private MutableLiveData<List<Reviews>> AllReviews;
    private Context context;

    public ReviewsRepository(Context context) {
        this.context = context;
        databaseReference= FirebaseDatabase.getInstance().getReference("Reviews");
        AllReviews=new MutableLiveData<>();
    }

    public void postReview(String review){
        String key=databaseReference.push().getKey();
        Date date= Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate=formatter.format(date);

        Reviews reviews=new Reviews(CurrentUserInfo.id,CurrentUserInfo.name,review,currentDate, CurrentStore.id);
        databaseReference.child(key).setValue(reviews)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, context.getString(R.string.thankStr), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("reviewError", Objects.requireNonNull(e.getMessage()));
            }
        });

    }

    private void getAllReviews(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Reviews> reviews=new ArrayList<>();
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.child("storeId").getValue().toString().equals(CurrentStore.id)){
                        Reviews rev=new Reviews(
                                key.child("userId").getValue().toString(),
                                key.child("userName").getValue().toString(),
                                key.child("review").getValue().toString(),
                                key.child("date").getValue().toString(),
                                key.child("storeId").getValue().toString() );
                        reviews.add(rev);
                    }
                }
                AllReviews.setValue(reviews);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Reviews>> getReviewsList(){
        getAllReviews();
        return AllReviews;
    }

}
