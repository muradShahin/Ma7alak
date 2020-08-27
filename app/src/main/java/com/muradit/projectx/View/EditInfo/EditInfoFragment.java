package com.muradit.projectx.View.EditInfo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.UploadImageFirebase;
import com.muradit.projectx.R;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.EditInfoViewModel.EditViewModel;
import com.muradit.projectx.ViewModel.profileViewModel.ProfileViewModel;

import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;


public class EditInfoFragment extends Fragment {
    private ProfileViewModel viewModel;
    private EditViewModel editViewModel;
    private ImageView userPic,notePic;
    private EditText name,email,phone,city;
    private CardView nameEdit,phoneEdit,emaiEdit,cityEdit,imageEdit,noteCard;
    private Button saveBtn;
    private TextView noteText;
    private String downloadUri;
    private Uri imgUriProfile;
    private RelativeLayout relativeLayoutNote;
    private LinearLayout notesLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_edit_info, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.editStr));
        init(v);




        return v;
    }
    private void init(View v){
        viewModel=new ProfileViewModel(getActivity());
        editViewModel=new EditViewModel(getActivity());
        name=v.findViewById(R.id.userName);
        email=v.findViewById(R.id.email);
        phone=v.findViewById(R.id.phone);
        city=v.findViewById(R.id.city);
        userPic=v.findViewById(R.id.userImage);
        nameEdit=v.findViewById(R.id.updateImgCardName);
        emaiEdit=v.findViewById(R.id.updateImgCardEmail);
        phoneEdit=v.findViewById(R.id.updateImgCardPhone);
        cityEdit=v.findViewById(R.id.updateImgCardCity);
        imageEdit=v.findViewById(R.id.updateImgCard);
        saveBtn=v.findViewById(R.id.saveBtn);
        notePic=v.findViewById(R.id.noteImg);
        noteText=v.findViewById(R.id.noteText);
        noteCard=v.findViewById(R.id.NoteCard);
        relativeLayoutNote=v.findViewById(R.id.relativeNote);
        notesLayout=v.findViewById(R.id.LinearNote);

        notesLayout.setVisibility(View.GONE);


        name.setText(CurrentUserInfo.name);
        city.setText(CurrentUserInfo.city);
        email.setText(CurrentUserInfo.email);
        phone.setText(CurrentUserInfo.phone);
        downloadUri=CurrentUserInfo.image;
        getMissingInfo();
        setUserCurrentImage();

        //Handling card edit
        nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.isEnabled())
                  name.setEnabled(false);
                else
                    name.setEnabled(true);

            }
        });

        emaiEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.isEnabled())
                email.setEnabled(false);
                else
                    email.setEnabled(true);
            }
        });

        phoneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.isEnabled())
                phone.setEnabled(false);
                else
                    phone.setEnabled(true);
            }
        });

        cityEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(city.isEnabled()){
                    city.setEnabled(false);
                }else
                  city.setEnabled(true);
            }
        });
        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(getString(R.string.info1))
                        .setContentText(getString(R.string.info2))
                        .setConfirmText(getString(R.string.imsure))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                String nameStr=name.getText().toString();
                                String emailStr=email.getText().toString();
                                String phoneStr=phone.getText().toString();
                                String cityStr=city.getText().toString();
                                editViewModel.updateUserInfo(nameStr,emailStr,phoneStr,cityStr,downloadUri);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton(getString(R.string.cancelStr), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        });



    }




    private void getMissingInfo(){
        viewModel.getMissingInfo().observe(getViewLifecycleOwner(), new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> stringObjectMap) {
               String note="";
                   if (stringObjectMap.get("email").toString().equals("no email")) {
                       note = getString(R.string.validEmail)+",";
                       email.setText(stringObjectMap.get("email").toString());
                       notesLayout.setVisibility(View.VISIBLE);
                   }
                   if (stringObjectMap.get("phone").toString().equals("no phone")) {
                       note += getString(R.string.validPhone)+",";
                       phone.setText(stringObjectMap.get("phone").toString());
                       notesLayout.setVisibility(View.VISIBLE);
                   }
                   if (stringObjectMap.get("image").toString().equals("no image")) {
                       note += getString(R.string.addPhoto)+",";
                       notesLayout.setVisibility(View.VISIBLE);
                   }

               noteText.setText(note+" "+getString(R.string.thankStr) );



            }
        });
    }

    private void setUserCurrentImage() {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if (CurrentUserInfo.image.equals("no image"))
            Glide.with(getActivity()).load(user.getPhotoUrl()).into(userPic);
        else
            Glide.with(getActivity()).load(CurrentUserInfo.image).into(userPic);



    }
    private void FileChooser(){
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,1);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 1 && resultCode==RESULT_OK && data!=null){
            imgUriProfile = data.getData();
            userPic.setImageURI(imgUriProfile);
            String path=CurrentUserInfo.name+CurrentUserInfo.email;
            UploadImageFirebase upf=new UploadImageFirebase(getActivity(),path,imgUriProfile,"usersImage");
            upf.FileUpload();
            upf.getUriDownlaod().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    downloadUri=s;
                }
            });

        }
    }
}
