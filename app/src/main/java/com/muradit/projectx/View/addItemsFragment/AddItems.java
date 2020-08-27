package com.muradit.projectx.View.addItemsFragment;


import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.GetCategories;
import com.muradit.projectx.Model.others.NotificationsSender;
import com.muradit.projectx.Model.others.UploadImageFirebase;
import com.muradit.projectx.R;
import com.muradit.projectx.ViewModel.addItemsViewModel.addItemsViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddItems extends Fragment {
    private ImageView itemImage;
    private EditText itemName,price,desc;
    private Button postItem;
    private MaterialSpinner category;
    private addItemsViewModel addItemsViewModel;
    private String itemImageUri;

    //VARS
    private String categorySelected;


    public AddItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_add_items, container, false);
        addItemsViewModel=new addItemsViewModel(getContext());
        itemImage=root.findViewById(R.id.itemImage);
        itemName=root.findViewById(R.id.itemName);
        desc=root.findViewById(R.id.desc);
        price=root.findViewById(R.id.price);
        category=root.findViewById(R.id.category);
        postItem=root.findViewById(R.id.publishBtn);

        //init categories to  spinner
        initCateogries();

        //handling click on image
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        category.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                categorySelected=item.toString();
            }
        });

        //handing post item button
        postItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfFilled();
                addItemsViewModel.getUploadedStatus().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean){
                            itemImage.setImageResource(R.drawable.ic_upload);
                            itemName.setText("");
                            itemName.setHint(R.string.itemsStr);
                            price.setText("");
                            price.setHint("price");
                            desc.setText("");
                            desc.setHint("description");
                            NotificationsSender ns=new NotificationsSender("All");
                            ns.sendNotification("",CurrentStore.name +" "+getString(R.string.postedNewItem));
                        }
                    }
                });
            }
        });






    return root;
    }

    private void initCateogries() {
        GetCategories gg=new GetCategories();
        gg.getCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                List<String> categoriesName=new ArrayList<>();
                for (int i=0;i<categories.size();i++)
                    categoriesName.add(categories.get(i).getName());

                category.setItems(categoriesName);
            }
        });
    }

    private void checkIfFilled() {
        if(!TextUtils.isEmpty(itemImageUri))
             if(!TextUtils.isEmpty(itemName.getText().toString()))
                if(!TextUtils.isEmpty(price.getText().toString()))
                   if(!TextUtils.isEmpty(desc.getText().toString()))
                       if(!TextUtils.isEmpty(categorySelected))
                         postItem(itemName.getText().toString(),desc.getText().toString(),
                             price.getText().toString(),categorySelected);
                       else
                           Toast.makeText(getActivity(),getString(R.string.pleaseSelectCategoryStr), Toast.LENGTH_SHORT).show();
                   else
                         desc.setError("required");
                else
                    price.setError("required");
             else
                 itemName.setError("required");
         else
            Toast.makeText(getActivity(), getString(R.string.mustUpPhotoStr), Toast.LENGTH_SHORT).show();

    }

    private void postItem(String itemName, String desc, String price, String category) {
    addItemsViewModel.postItemInit(itemName,desc,price,category,itemImageUri,"available");

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
            itemImage.setImageURI(imgUriProfile);
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

}
