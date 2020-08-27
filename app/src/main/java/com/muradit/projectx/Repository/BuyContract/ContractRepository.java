package com.muradit.projectx.Repository.BuyContract;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.ContractModel;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.NotificationsSender;
import com.muradit.projectx.R;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ContractRepository {
    private DatabaseReference databaseReference;
    private Context context;
    private SweetAlertDialog pd;

    public ContractRepository(Context context) {
        this.context = context;

    }


    public void buyItem(final ContractModel contractModel){
        pd=new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
        databaseReference=FirebaseDatabase.getInstance().getReference("Requests");
        int n=(int)(Math.random()*(10000));
        databaseReference.child(CurrentItem.item_store+n).setValue(contractModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setTitleText(context.getString(R.string.confirmMessage))
                                .setContentText(context.getString(R.string.arriveSoon))
                                .setCustomImage(R.drawable.ic_done)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        NotificationsSender ns=new NotificationsSender("spec");
                                                ns.sendNotification(CurrentStore.ownerEmail,CurrentUserInfo.name+" "+context.getString(R.string.orderReqStr));
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .show();



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void updatePhone(final String number){
        databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        Map<String,Object> map=new HashMap<>();
        map.put("phone",number);

        databaseReference.child(CurrentUserInfo.id).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "updated successfully ", Toast.LENGTH_SHORT).show();
                        CurrentUserInfo.phone=number;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
