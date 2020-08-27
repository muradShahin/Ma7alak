package com.muradit.projectx.View.InsideStore;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.Model.others.NotificationsSender;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterItemsFull;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterStoresOffersInside;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.View.popOutWindow.popWindow;
import com.muradit.projectx.View.reportView.reportScreen;
import com.muradit.projectx.ViewModel.InsideStore.InsideViewModel;

import java.util.List;
import java.util.Objects;


public class InsideStoreFragment extends Fragment {
    private static final int REQUEST_CALL =1 ;
    private RecyclerView recyclerView;
    private CardView chat,offers,location,phone,email,reviews,follow;
    private InsideViewModel viewModel;
    private TextView about;
    private ImageView storeImage,followImage,offersIcon;
    private FloatingActionButton recyclerStyleBtn;
    private FirebaseAuth firebaseAuth;


    //vars
    private String recyclerStyle="style1";
    private boolean offerMode=true;
    private boolean isFollowed=false;
    private String relKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_inside_store, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle(CurrentStore.name);
        setHasOptionsMenu(true);
       init(v);
        

      return v;
    }

    private void init(View v) {



        viewModel=new InsideViewModel(getActivity());
        firebaseAuth=FirebaseAuth.getInstance();



        recyclerView=v.findViewById(R.id.rec);
        recyclerStyleBtn=v.findViewById(R.id.floatingActionButton);
        chat=v.findViewById(R.id.itemsCard);
        offers=v.findViewById(R.id.offers);
        location=v.findViewById(R.id.locationCard);
        reviews=v.findViewById(R.id.reviewsCard);
        email=v.findViewById(R.id.emailCard);
        phone=v.findViewById(R.id.phoneCard);
        follow=v.findViewById(R.id.followCard);
        offersIcon=v.findViewById(R.id.imageView24);



        Flags.Flag_Room=false;
        Flags.Customer_flag=true;



        about=v.findViewById(R.id.about);
        storeImage=v.findViewById(R.id.storeImage);
        followImage=v.findViewById(R.id.followImage);

        recyclerStyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerStyle.equals("style1")) {
                    recyclerStyle="style2";
                    getAllProducts();
                    recyclerStyleBtn.setImageResource(R.drawable.ic_menu);
                }else{
                    recyclerStyle="style1";
                    getAllProducts();
                    recyclerStyleBtn.setImageResource(R.drawable.ic_grid);
                }

            }
        });

        if(CurrentStore.ownerEmail.equals(CurrentUserInfo.email)){
            follow.setVisibility(View.GONE);
        }


        //init components
        Glide.with(getActivity()).load(CurrentStore.image).into(storeImage);
        about.setText(CurrentStore.about);

        getAllProducts();
        Flags.wishItem="storeItem";

        checkIfStoreIsFollowedBefore();
        getRelationKey();

        if(firebaseAuth.getCurrentUser() ==null){
            chat.setEnabled(false);
        }

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_insideStoreFragment_to_chatRoom);

            }
        });

        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(offerMode) {
                  offersIcon.setImageResource(R.drawable.ic_inventory);
                  offerMode=false;
                  getAllOffers();

              }else{
                  offersIcon.setImageResource(R.drawable.ic_sale);
                  offerMode=true;
                  getAllProducts();
              }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),storeLocation.class);
                startActivity(i);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              callPhone();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"+CurrentStore.email));
                startActivity(Intent.createChooser(emailIntent, "Send email to the Store"));
            }
        });

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flags.CUST_STORE="customer";
                Navigation.findNavController(v).navigate(R.id.action_insideStoreFragment_to_reviewsFragment);
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFollowed) {
                    viewModel.followStore();
                    NotificationsSender ns=new NotificationsSender("spec");
                    ns.sendNotification(CurrentStore.ownerEmail,CurrentUserInfo.name+" just followed your store");
                }else{
                    viewModel.unFollowStore(relKey);
                    isFollowed=false;
                    followImage.setImageResource(R.drawable.ic_follow);

                }
            }
        });


    }



    private void checkIfStoreIsFollowedBefore(){
        viewModel.checkIfFollowed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    followImage.setImageResource(R.drawable.ic_followed);
                    isFollowed=true;
                }else{
                    followImage.setImageResource(R.drawable.ic_follow);
                    isFollowed=false;
                }


            }
        });
    }

    private void getRelationKey() {
        viewModel.getRelKey().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                relKey=s;
            }
        });
    }


    private void getAllProducts(){
        viewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<AddItemModel>>() {
            @Override
            public void onChanged(List<AddItemModel> addItemModels) {
                if(recyclerStyle.equals("style1")) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                }else if(recyclerStyle.equals("style2")){
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                }

                RecyclerAdapterItemsFull recAdapter = new RecyclerAdapterItemsFull(getActivity(), addItemModels, recyclerStyle);
                recyclerView.setAdapter(recAdapter);
            }
        });
    }

    private void getAllOffers(){
        viewModel.getStoreOffers().observe(getViewLifecycleOwner(), new Observer<List<Offers>>() {
            @Override
            public void onChanged(List<Offers> offers) {
                RecyclerAdapterStoresOffersInside recAdapter=new RecyclerAdapterStoresOffersInside(getActivity(),offers);
                recyclerView.setAdapter(recAdapter);
            }
        });
    }

    private void callPhone(){

            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

            }else{
                String dia="tel:"+CurrentStore.phone;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dia)));

            }

        }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
       if(firebaseAuth.getCurrentUser()!=null)
          inflater.inflate(R.menu.store_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()){
           case R.id.report:Intent i=new Intent(getContext(), reportScreen.class);
                         startActivity(i);
                         break;
       }


        return super.onOptionsItemSelected(item);
    }
}