package com.muradit.projectx.Repository.toolsRepository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.others.CurrentUserInfo;

import java.util.HashMap;
import java.util.Map;

public class ToolsRepository {
    private DatabaseReference databaseReference;
    private Context context;

    public ToolsRepository(Context context) {
        this.context=context;
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
    }

    public void updateLanguage(String lang){
        Map<String,Object> map=new HashMap<>();
        map.put("lang",lang);

        databaseReference.child(CurrentUserInfo.id).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
