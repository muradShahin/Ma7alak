package com.muradit.projectx.Repository.OrderDetailsRepository;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InvoiceRepsitory {
    private DatabaseReference databaseReference;
    private Context context;
    private View v;

    public InvoiceRepsitory(Context context,View v) {
        this.context = context;
        this.v=v;
        databaseReference= FirebaseDatabase.getInstance().getReference("Requests");
    }

    public void cancelOrder(String orderId){
        databaseReference.child(orderId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setTitleText(context.getString(R.string.orderCancelStr))
                                .setCustomImage(R.drawable.ic_done)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Navigation.findNavController(v).navigate(R.id.action_invoiceFragment2_to_nav_gallery);

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
