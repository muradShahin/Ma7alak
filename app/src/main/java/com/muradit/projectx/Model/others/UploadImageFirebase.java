package com.muradit.projectx.Model.others;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class UploadImageFirebase {
    private String path;
    private Uri imgUri;
    private Context context;
    private SweetAlertDialog pd;
    private StorageReference mStorgeref;
    private String fileLocation;
    private String uriDownlaod;
    private MutableLiveData<String> uriMD=new MutableLiveData<>();

    public UploadImageFirebase(Context context, String path, Uri imgUri,String fileLocation) {
        this.path = path;
        this.imgUri = imgUri;
        this.context=context;
        this.fileLocation=fileLocation;
        mStorgeref= FirebaseStorage.getInstance().getReference(fileLocation);

    }
    public void FileUpload() {
        pd = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);
        pd.show();
        final int n=(int)(Math.random()*1000);
        mStorgeref.child(path+n+"."+getExtension(imgUri))
                .putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getUri(n);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
    private void getUri(int n) {
        mStorgeref.child(path+n+"."+getExtension(imgUri)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                uriDownlaod = uri.toString();
                Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                uriMD.setValue(uriDownlaod);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private String getExtension(Uri uri){
        ContentResolver contentResolver=context.getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    public LiveData<String> getUriDownlaod(){

        return uriMD;
    }
}
