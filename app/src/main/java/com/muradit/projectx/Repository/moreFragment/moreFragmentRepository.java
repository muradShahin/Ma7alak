package com.muradit.projectx.Repository.moreFragment;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.others.CurrentItem;

import java.util.HashMap;
import java.util.Map;

public class moreFragmentRepository {
    private DatabaseReference databaseReference;
    private Context context;


    public moreFragmentRepository(Context context) {
        databaseReference= FirebaseDatabase.getInstance().getReference("Items");
        this.context=context;
    }

    public void UpdateFirebase(String itemImageUri, String name, String desc, String price, String category){

      /*  databaseReference.child(CurrentItem.item_id).child("item_name").setValue(name);
        databaseReference.child(CurrentItem.item_id).child("item_image").setValue(itemImageUri);
        databaseReference.child(CurrentItem.item_id).child("item_price").setValue(price);
        databaseReference.child(CurrentItem.item_id).child("item_desc").setValue(desc);
        databaseReference.child(CurrentItem.item_id).child("item_category").setValue(itemImageUri);
*/
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("item_name",name);
        map.put("item_image",itemImageUri);
        map.put("item_price",price);
        map.put("item_desc",desc);
        map.put("item_category",category);
        databaseReference.child(CurrentItem.item_id).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
