package com.muradit.projectx.View.MainActivity.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.muradit.projectx.Model.ContractModel;
import com.muradit.projectx.Model.Follow;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.Model.others.MessagesNumber;
import com.muradit.projectx.Model.others.Orders;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterFollowedStores;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterReviews;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterUserOrders;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.View.login.loginView;
import com.muradit.projectx.ViewModel.profileViewModel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProfileFragment extends Fragment implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM =16 ;
    private ProfileViewModel profileViewModel;
    private ImageView profieImage;
    private TextView userName,titleOfRec,followsText,numberOfMessages;
    private CardView wishList,missing,ordersCard,favorStoresCard;
    private LinearLayout profileLayout,noAccountLayout;
    private Button createAccount;
    private RelativeLayout editLayout,notifiCard;
    private GoogleMap mMap;
    private RecyclerView recyclerView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(CurrentUserInfo.name);
        init(root);





         return root;
    }

    private void init(View root) {
        profileViewModel =new ProfileViewModel(getActivity());
        profieImage=root.findViewById(R.id.profileImage);
        userName=root.findViewById(R.id.userName);
        wishList=root.findViewById(R.id.wishListCard);
        missing=root.findViewById(R.id.missing);
        ordersCard=root.findViewById(R.id.cardOrders);
        favorStoresCard=root.findViewById(R.id.cardFvorStores);
        titleOfRec=root.findViewById(R.id.titleRec);
        editLayout=root.findViewById(R.id.editLayout);
        followsText=root.findViewById(R.id.followTxt);
        notifiCard=root.findViewById(R.id.notifCard);
        numberOfMessages=root.findViewById(R.id.number_messages);

        createAccount=root.findViewById(R.id.errorCreateAcc);

        profileLayout=root.findViewById(R.id.profileMain);
        noAccountLayout=root.findViewById(R.id.noAccLayout);

        recyclerView=root.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        titleOfRec.setText(getString(R.string.myOrders));


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


        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.action_nav_gallery_to_wishListFragment);
            }
        });

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_gallery_to_editInfoFragment);
            }
        });

        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrders();
            }
        });

        favorStoresCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getFollowedStores();
            }
        });

        notifiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flags.Customer_flag=true;
                Navigation.findNavController(v).navigate(R.id.action_nav_gallery_to_chatHeaders);
            }
        });

    }

    private void configureProfileLayout() {
        if(profileViewModel.checkIfLoggedIn()) {
            noAccountLayout.setVisibility(View.GONE);
            profileLayout.setVisibility(View.VISIBLE);
            setProfieImage();
            setUserName();
            checkInfoMissing();
            initMap();
            getFollowedStores();
            getNumberOfMessages();

        }else{
            noAccountLayout.setVisibility(View.VISIBLE);
            profileLayout.setVisibility(View.GONE);

        }


    }



    private void setProfieImage() {
        String profileImage=profileViewModel.getImageUri();
        Glide.with(this).load(profileImage).into(profieImage);
    }
    private void setUserName(){
        String name=profileViewModel.getUserName();
        userName.setText(name);
    }
    /*private void setRate(){
        int rate=profileViewModel.getRate();

        switch (rate){
            case 1:
                star5.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                break;
            case 2:
                star5.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                break;
            case 3:
                star5.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                break;
            case 4:
                star5.setVisibility(View.GONE);
                break;

        }
    }
*/
    private void goToLoginView(){
        Intent i=new Intent(getActivity(), loginView.class);
        getActivity().startActivity(i);
        getActivity().finish();
    }

    private void checkInfoMissing(){
    profileViewModel.getMissingInfo().observe(getViewLifecycleOwner(), new Observer<Map<String, Object>>() {
        @Override
        public void onChanged(Map<String, Object> stringObjectMap) {
            if(stringObjectMap.get("email").toString().equals("no email")
              || stringObjectMap.get("phone").toString().equals("no phone")
                    || stringObjectMap.get("image").toString().equals("no image")
              ){
                missing.setVisibility(View.VISIBLE);
            }else{
                missing.setVisibility(View.GONE);
            }

        }
    });
    }
    private void initMap() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView2, fragment);
        transaction.commit();
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng currentLocation;
        // Add a marker in Sydney and move the camera
        if(FirebaseAuth.getInstance().getCurrentUser() !=null) {
            double currentLat = Double.parseDouble(CurrentUserInfo.lat);
            double currentLng = Double.parseDouble(CurrentUserInfo.lng);
            currentLocation = new LatLng(currentLat, currentLng);
        }else{
          currentLocation=new LatLng(  31.948735, 35.935405);
        }
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("My location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,DEFAULT_ZOOM));
    }

    public void getFollowedStores(){
        profileViewModel.getAllFollowers().observe(getViewLifecycleOwner(), new Observer<List<Follow>>() {
            @Override
            public void onChanged(List<Follow> follows) {
                followsText.setText(getString(R.string.following)+" "+follows.size());
                titleOfRec.setText(getString(R.string.myStores));

                RecyclerAdapterFollowedStores followAdapter=new RecyclerAdapterFollowedStores(getActivity(),follows);
                recyclerView.setAdapter(followAdapter);


            }
        });

    }

    public void getOrders(){
        titleOfRec.setText(getString(R.string.myOrders));
        profileViewModel.getOrders().observe(getViewLifecycleOwner(), new Observer<List<Orders>>() {
            @Override
            public void onChanged(List<Orders> orders) {
                RecyclerAdapterUserOrders recyclerAdapterUserOrders=new RecyclerAdapterUserOrders(getActivity(),orders);
                recyclerView.setAdapter(recyclerAdapterUserOrders);
            }
        });
    }
    private void getNumberOfMessages() {
        MessagesNumber mn=new MessagesNumber();
        mn.getMessages(CurrentUserInfo.id);
        mn.getNumberOfMessages().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                numberOfMessages.setText(integer+"");
            }
        });
    }

    @Override
    public void onResume() {
    getNumberOfMessages();
        super.onResume();
    }
}