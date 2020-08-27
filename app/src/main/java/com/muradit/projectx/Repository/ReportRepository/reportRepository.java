package com.muradit.projectx.Repository.ReportRepository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.Report;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class reportRepository {
    private DatabaseReference databaseReference;
    private Context context;

    public reportRepository(Context context) {
        this.context = context;
        databaseReference= FirebaseDatabase.getInstance().getReference("Reports");
    }

    public void submitReport(String report){
        String key=databaseReference.push().getKey();

        Date date= Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate=formatter.format(date);

        Report reportInstance=new Report(CurrentUserInfo.id, CurrentStore.id,report,currentDate);
        databaseReference.child(key).setValue(reportInstance)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setTitleText(context.getString(R.string.reportStr))
                                .setContentText(context.getString(R.string.reportStr2))
                                .setCustomImage(R.drawable.ic_done)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        ((AppCompatActivity)context).finish();
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
