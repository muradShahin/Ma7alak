package com.muradit.projectx.Repository.EditInfoRepository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.R;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditRepository {
    private DatabaseReference databaseReference;
    private Context context;

    public EditRepository(Context context) {
        this.context = context;
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
    }
    public void updateUserInfo(String name,String email,String phone,String city,String image){
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("email",email);
        map.put("phone",phone);
        map.put("city",city);
        map.put("image",image);


         databaseReference.child(CurrentUserInfo.id).updateChildren(map)
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                 .setTitleText("Your information is updated SUCCESSFULLY!")
                                 .setContentText("Thank you for using our App !")
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
}
