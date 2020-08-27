package com.muradit.projectx.View.moreFragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.UploadImageFirebase;
import com.muradit.projectx.R;
import com.muradit.projectx.ViewModel.moreFragment.moreFragmentViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class moreFragment extends Fragment {
    private ImageView item_image;
    private CardView edit_icon;
    private EditText itemName,price,desc,category;
    private Button saveButton;
    private String itemImageUri;
    private  moreFragmentViewModel viewModel;


    public moreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_more, container, false);
        viewModel=new moreFragmentViewModel(getActivity());

        item_image=root.findViewById(R.id.itemImage);
        itemName=root.findViewById(R.id.itemName);
        desc=root.findViewById(R.id.desc);
        price=root.findViewById(R.id.price);
        category=root.findViewById(R.id.category);
        saveButton=root.findViewById(R.id.saveBtn);
        edit_icon=root.findViewById(R.id.editCard);


        initComponent();

        //handling edit card on click
        edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        //handling click on save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItemInfo();
            }
        });


           return root;
    }




    private void initComponent() {
        Glide.with(getActivity()).load(CurrentItem.item_image).into(item_image);
        itemName.setText(CurrentItem.item_name);
        desc.setText(CurrentItem.item_desc);
        price.setText(CurrentItem.item_price);
        category.setText(CurrentItem.item_category);


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
            Uri imgUriProfile = data.getData();
            item_image.setImageURI(imgUriProfile);
            String path=itemName.getText().toString()+ CurrentStore.email;
            UploadImageFirebase upf=new UploadImageFirebase(getActivity(),path,imgUriProfile,"items");
            upf.FileUpload();
            upf.getUriDownlaod().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    itemImageUri=s;
                }
            });

        }
    }
    private void editItemInfo() {
        checkValidationOfData();

    }

    private void checkValidationOfData() {
        if(TextUtils.isEmpty(itemImageUri))
            itemImageUri=CurrentItem.item_image;

        if(!TextUtils.isEmpty(itemName.getText().toString()))
              if(!TextUtils.isEmpty(desc.getText().toString()))
                  if(!TextUtils.isEmpty(price.getText().toString()))
                      if(!TextUtils.isEmpty(category.getText().toString()))
                         viewModel.editInfo(itemImageUri,itemName.getText().toString(),desc.getText().toString(),
                                    price.getText().toString(),category.getText().toString());
                      else
                          category.setError("required");
                  else
                      price.setError("required");
              else
                  desc.setError("required");
        else
            itemName.setText("required");
    }

}
