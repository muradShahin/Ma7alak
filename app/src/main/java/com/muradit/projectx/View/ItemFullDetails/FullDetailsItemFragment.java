package com.muradit.projectx.View.ItemFullDetails;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.NotificationsSender;
import com.muradit.projectx.R;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.FullDtailsViewModel.FullDetailsViewModel;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullDetailsItemFragment extends Fragment {

    private TextView item_name,desc,price,store_name,store_address,store_phone;
    private ImageView storeImage;
    private FullDetailsViewModel viewModel;
    private CardView buyCard,wishCard,cardInHeart;
    private Button moreButton;
    private ImageView itemImage;
    private FirebaseAuth firebaseAuth;



    public FullDetailsItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_full_details_item, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.itemDetails));
        itemImage=root.findViewById(R.id.itemImage);




        init(root);





        return  root;
    }

    private void init(View root) {
        viewModel=new FullDetailsViewModel(getActivity());
        firebaseAuth=FirebaseAuth.getInstance();

        item_name=root.findViewById(R.id.itemName);
        desc=root.findViewById(R.id.desc);
        price=root.findViewById(R.id.price);
        store_name=root.findViewById(R.id.storeName);
        store_address=root.findViewById(R.id.address);
        store_phone=root.findViewById(R.id.phone);
        storeImage=root.findViewById(R.id.storeImage);
        buyCard=root.findViewById(R.id.card_buy);
        wishCard=root.findViewById(R.id.card_wish);
        moreButton=root.findViewById(R.id.moreBtn);
        cardInHeart=root.findViewById(R.id.card_heart);

        desc.setText(CurrentItem.item_desc);
        price.setText(CurrentItem.item_price+" JD");
        item_name.setText(CurrentItem.item_name);
        Glide.with(getActivity()).load(CurrentItem.item_image).into(itemImage);



        getStoreInfo(root);


        //Handling card buy , card wishlist

        buyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
               if(firebaseAuth.getCurrentUser()!=null)
                Navigation.findNavController(v).navigate(R.id.action_fullDetailsItemFragment_to_contractFragment);
               else {
                   new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                           .setTitleText(getString(R.string.mustLogin))
                           .setCustomImage(R.drawable.ic_error)
                           .show();

               }

            }
        });

        wishCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser()!=null)
                viewModel.addToWishList();
                else{
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setTitleText(getString(R.string.mustLogin))
                            .setCustomImage(R.drawable.ic_error)
                            .show();
                }
            }
        });

        if(firebaseAuth.getCurrentUser()!=null) {
            viewModel.isExist().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        wishCard.setEnabled(false);
                        wishCard.setVisibility(View.GONE);
                        cardInHeart.setVisibility(View.VISIBLE);

                    } else {
                        wishCard.setEnabled(true);
                        wishCard.setVisibility(View.VISIBLE);
                        cardInHeart.setVisibility(View.GONE);
                    }
                }
            });
        }

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_fullDetailsItemFragment_to_insideStoreFragment);

            }
        });



    }

    private void getStoreInfo(final View v) {
        viewModel.getStoreInfoFromRepository().observe(getViewLifecycleOwner(), new Observer<StoreModel>() {
            @Override
            public void onChanged(StoreModel storeModel) {
             Glide.with(getActivity()).load(storeModel.getImage()).into(storeImage);
             store_name.setText(storeModel.getName());
             store_address.setText(storeModel.getCity());
             store_phone.setText(storeModel.getPhone());

                CurrentStore.name=storeModel.getName();
                CurrentStore.email=storeModel.getEmail();
                CurrentStore.city=storeModel.getCity();
                CurrentStore.id=storeModel.getId();
                CurrentStore.lat=storeModel.getLat();
                CurrentStore.lng=storeModel.getLng();
                CurrentStore.image=storeModel.getImage();
                CurrentStore.ownerEmail=storeModel.getOwnerEmail();
                CurrentStore.ownerName=storeModel.getOwnerName();
                CurrentStore.phone=storeModel.getPhone();
                CurrentStore.about=storeModel.getAbout();
                CurrentStore.id=storeModel.getId();

            }
        });
    }



}
