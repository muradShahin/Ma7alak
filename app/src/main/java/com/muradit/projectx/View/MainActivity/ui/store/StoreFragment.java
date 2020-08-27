package com.muradit.projectx.View.MainActivity.ui.store;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.Model.others.GetCategories;
import com.muradit.projectx.Model.others.GetCurrentLocation;
import com.muradit.projectx.Model.others.MessagesNumber;
import com.muradit.projectx.Model.others.UploadImageFirebase;
import com.muradit.projectx.R;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.View.locationView.MapsActivity;
import com.muradit.projectx.View.login.loginView;
import com.muradit.projectx.ViewModel.storeViewModel.StoreViewModel;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;

public class StoreFragment extends Fragment  {

    private StoreViewModel storeViewModel;
    private Button createAccount,createStore;
    private  LinearLayout noAccountLayout;
    private LinearLayout createStoreLayout;
    private LinearLayout storeLayout;
    private ImageView storeImage,storeKarmaImage;
    private EditText storeName,emailStore,storePhone;
    private MaterialSpinner storeLocation,categoryEditText;
    private TextView storeN,numberOfNotification;
    private String downloadUri;
    private Uri imgUriProfile;
    private CardView inventoryCard,locationCard,ordersCard,reviewsCard,offersCard,messagesCard,notificationCard;
    private EditText bioText;
    private Button saveBtn;

    private String location,category;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.my_store));
        storeViewModel =new StoreViewModel(getActivity());

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_slideshow_to_addItems);
            }
        });
        createAccount=root.findViewById(R.id.errorCreateAcc);
        createStore=root.findViewById(R.id.createStoreButton);
        noAccountLayout=root.findViewById(R.id.noAccLayout);
        createStoreLayout=root.findViewById(R.id.createAstore);
        storeLayout=root.findViewById(R.id.storeLayout);
        storeImage=root.findViewById(R.id.storeImage);//this image is used in create store phase
        storeKarmaImage=root.findViewById(R.id.storeImg);//this image is used after the store is created
        storeName=root.findViewById(R.id.storeName);
        emailStore=root.findViewById(R.id.mail);
        storePhone=root.findViewById(R.id.phone);
        storeLocation=root.findViewById(R.id.location);
        storeN=root.findViewById(R.id.storeN);
        inventoryCard=root.findViewById(R.id.inventoryCard);
        locationCard=root.findViewById(R.id.locationCard);
        ordersCard=root.findViewById(R.id.ordersCard);
        reviewsCard=root.findViewById(R.id.reviewsCard);
        offersCard=root.findViewById(R.id.offersCard);
        categoryEditText=root.findViewById(R.id.categoryEditText);
        messagesCard=root.findViewById(R.id.messagesCard);
        notificationCard=root.findViewById(R.id.notifCard);
        numberOfNotification=root.findViewById(R.id.numberOfNotif);
        bioText=root.findViewById(R.id.bioTxt);
        saveBtn=root.findViewById(R.id.saveBtn);

        //init spinners with items
        storeLocation.setItems("Ajlun","Amman", "Aqaba", "Balqa", "Irbid","Jarash",
                "Karak","Ma`an","Madaba","Mafraq","Tafilah","Zarqa");

        //here im going to retrive the categoris from firebase
        setCategories();



        configureProfileLayout();


        //create account button handling
        //this button will be visible if the user is trying to enter
        // the profile section , nut he is not logged in
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginView();
            }
        });



        //handling the click on image view
        storeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });
        //handling create store button
        createStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=storeName.getText().toString();
                String email=emailStore.getText().toString();
                String phone=storePhone.getText().toString();
                String address=storeLocation.getText().toString();
                String imageUril=downloadUri;
                checkInputsForCreateAccount(name,email,phone,address,imageUril);
            }
        });



        //handling the click on inventory cardView
        inventoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_inventoryFragment);
            }
        });

        //handling click on location card
        locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), MapsActivity.class);
                startActivity(i);
            }
        });

        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_storeOrders);
            }
        });

        reviewsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flags.CUST_STORE="store";
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_reviewsFragment);
            }
        });

        offersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_storeOffers);
            }
        });

        storeLocation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                location=item.toString();
            }
        });

        categoryEditText.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                category=item.toString();
            }
        });

        messagesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flags.Customer_flag=false;
                Flags.Flag_Room=false;
                Navigation.findNavController(v).navigate(R.id.action_nav_slideshow_to_chatHeaders);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBio();
            }
        });

        return root;
    }

    private void editBio() {
        storeViewModel.editBio(CurrentStore.id,bioText.getText().toString());
    }


    private void createStore(final String name, final String email, final String phone, final String city, final String imageUril, final String lat, final String lng
    , final String category , final String about) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Are you ready to open your store?")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        storeViewModel.createStore(name,email,phone,city,imageUril,lat,lng,category,about);
                        storeViewModel.isCreated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if(aBoolean){
                                  configureProfileLayout();
                                }
                            }
                        });
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();

    }



    private void goToLoginView(){
        Intent i=new Intent(getActivity(), loginView.class);
        getActivity().startActivity(i);
        getActivity().finish();
    }

    private void FileChooser(){
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,1);


    }

    private void configureProfileLayout() {


        if(storeViewModel.checkIfLoggedIn()) {

            noAccountLayout.setVisibility(View.GONE);
            storeViewModel.createdAstoreBefore();
            storeViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean){
                        storeLayout.setVisibility(View.VISIBLE);
                        createStoreLayout.setVisibility(View.GONE);
                        storeViewModel.getStore();
                        storeViewModel.getStoreData().observe(getViewLifecycleOwner(), new Observer<List<StoreModel>>() {
                            @Override
                            public void onChanged(List<StoreModel> storeModels) {
                                    Glide.with(Objects.requireNonNull(getActivity())).load(storeModels.get(0).getImage()).into(storeKarmaImage);
                                    storeN.setText(storeModels.get(0).getName());
                                    bioText.setText(storeModels.get(0).getAbout());
                                    CurrentStore.name = storeModels.get(0).getName();
                                    CurrentStore.city = storeModels.get(0).getCity();
                                    CurrentStore.email = storeModels.get(0).getEmail();
                                    CurrentStore.image = storeModels.get(0).getImage();
                                    CurrentStore.ownerEmail = storeModels.get(0).getOwnerEmail();
                                    CurrentStore.ownerName = storeModels.get(0).getOwnerName();
                                    CurrentStore.id = storeModels.get(0).getId();
                                    CurrentStore.lat = storeModels.get(0).getLat();
                                    CurrentStore.lng = storeModels.get(0).getLng();
                                    CurrentStore.about=storeModels.get(0).getAbout();

                                 //   getNotificationsOnMessages();

                            }
                        });

                    }else{
                        storeLayout.setVisibility(View.GONE);
                        createStoreLayout.setVisibility(View.VISIBLE);
                        setDefaultValues();
                    }
                }
            });

        }else{
            noAccountLayout.setVisibility(View.VISIBLE);
            createStoreLayout.setVisibility(View.GONE);
            storeLayout.setVisibility(View.GONE);


        }

    }


    private void setLayoutAfterCreation(){

        storeLayout.setVisibility(View.VISIBLE);
        createStoreLayout.setVisibility(View.GONE);
        storeViewModel.getStore();
        storeViewModel.getStoreData().observe(getViewLifecycleOwner(), new Observer<List<StoreModel>>() {
            @Override
            public void onChanged(List<StoreModel> storeModels) {
                Glide.with(getActivity()).load(storeModels.get(0).getImage()).into(storeKarmaImage);
                storeN.setText(storeModels.get(0).getName());
                CurrentStore.name=storeModels.get(0).getName();
                CurrentStore.city=storeModels.get(0).getCity();
                CurrentStore.email=storeModels.get(0).getEmail();
                CurrentStore.image=storeModels.get(0).getImage();
                CurrentStore.ownerEmail=storeModels.get(0).getOwnerEmail();
                CurrentStore.ownerName=storeModels.get(0).getOwnerName();
                CurrentStore.id=storeModels.get(0).getId();
                CurrentStore.lat=storeModels.get(0).getLat();
                CurrentStore.lng=storeModels.get(0).getLng();

                getNotificationsOnMessages();


            }
        });
    }



    private void setDefaultValues(){
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        String email=currentUser.getEmail();
        String name=currentUser.getDisplayName();
        storeName.setText(name);
        emailStore.setText(email);
    }

    private void checkInputsForCreateAccount(final String name, final String email, final String phone, final String address, final String imageUril){
        if(!TextUtils.isEmpty(storeName.getText().toString()))
            if(!TextUtils.isEmpty(emailStore.getText().toString()))
                if(!TextUtils.isEmpty(storePhone.getText().toString()))
                    if(!TextUtils.isEmpty(downloadUri))
                        createStore(name,email,phone,location,imageUril,"31.9485946","35.9386983",category,"about");
                    else
                        Toast.makeText(getActivity(), "Please upload an image", Toast.LENGTH_SHORT).show();
                else
                    storePhone.setError("required");
            else
                emailStore.setError("required");
        else
            storeName.setError("required");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 1 && resultCode==RESULT_OK && data!=null){
            imgUriProfile = data.getData();
            storeImage.setImageURI(imgUriProfile);
            String path=storeName.getText().toString()+emailStore.getText().toString();
            UploadImageFirebase upf=new UploadImageFirebase(getActivity(),path,imgUriProfile,"storesImages");
            upf.FileUpload();
            upf.getUriDownlaod().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    downloadUri=s;
                }
            });

        }
    }

    private void setCategories() {
        GetCategories getCategories =new GetCategories();
        getCategories.getCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                List<String> categoryItems=new ArrayList<>();
                for (int i=0;i<categories.size();i++){
                    categoryItems.add(categories.get(i).getName());
                }
                categoryEditText.setItems(categoryItems);
            }
        });
    }

    private void getNotificationsOnMessages() {
        MessagesNumber mn=new MessagesNumber();
        mn.getMessages(CurrentStore.id);
        mn.getNumberOfMessages().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                numberOfNotification.setText(integer +"");
            }
        });
    }
    @Override
    public void onResume() {
        getNotificationsOnMessages();
        super.onResume();
    }



}