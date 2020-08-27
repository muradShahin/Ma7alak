package com.muradit.projectx.View.OrderFullDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Invoice;
import com.muradit.projectx.Model.others.Orders;
import com.muradit.projectx.R;
import com.muradit.projectx.ViewModel.OrderDetailsViewModel.InvoiceViewModel;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class InvoiceFragment extends Fragment implements OnMapReadyCallback {
    private static final float DEFAULT_ZOOM =16 ;
    private Orders orderInfo;
    private TextView dateText,storeName,quantity,price,itemName;
    private Button moreBtn,cancelBtn,seeItemDetails;
    private ImageView item_image;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_invoice, container, false);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.map, fragment);
        transaction.commit();
        fragment.getMapAsync(this);
        init(v);

    return v;
    }
    private void init(View v) {
        final InvoiceViewModel viewModel=new InvoiceViewModel(getActivity(),v);
        dateText=v.findViewById(R.id.orderDate);
        storeName=v.findViewById(R.id.storeName);
        moreBtn=v.findViewById(R.id.moreBtn);
        cancelBtn=v.findViewById(R.id.cancelBtn);
        quantity=v.findViewById(R.id.quantity);
        price=v.findViewById(R.id.price);
        item_image=v.findViewById(R.id.item_image);
        itemName=v.findViewById(R.id.item_name);
        seeItemDetails=v.findViewById(R.id.seeItemBtn);

        orderInfo= Invoice.currentOrder;

        Glide.with(this).load(orderInfo.getItemInfo().getItem_image()).into(item_image);
        itemName.setText(orderInfo.getItemInfo().getItem_name());
        dateText.setText(getString(R.string.orderDateStr)+orderInfo.getDate());
        storeName.setText(orderInfo.getStore_name());
        quantity.setText(getString(R.string.QuantityStr)+" : "+orderInfo.getQuantity());
        price.setText(getString(R.string.priceStr)+" : "+orderInfo.getItemInfo().getItem_price());

        seeItemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentItem.item_id=orderInfo.getItem_id();
                CurrentItem.item_name=orderInfo.getItemInfo().getItem_name();
                CurrentItem.item_category=orderInfo.getItemInfo().getItem_category();
                CurrentItem.item_desc=orderInfo.getItemInfo().getItem_desc();
                CurrentItem.item_image=orderInfo.getItemInfo().getItem_image();
                CurrentItem.item_price=orderInfo.getItemInfo().getItem_price();
                CurrentItem.item_state=orderInfo.getItemInfo().getItem_state();
                CurrentItem.item_store=orderInfo.getStore_name();
                CurrentItem.store_email=orderInfo.getStore_email();

                Navigation.findNavController(v).navigate(R.id.action_invoiceFragment2_to_fullDetailsItemFragment);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(getString(R.string.cancelOrderQuest))
                        .setContentText(getString(R.string.orderCancelMessage))
                        .setConfirmText(getString(R.string.confirmStr))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                viewModel.cancelOreder(orderInfo.getRequest_ID());
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        double currentLat=orderInfo.getLat();
        double currentLng=orderInfo.getLng();
        LatLng currentLocation = new LatLng(currentLat, currentLng);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("deliver location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,DEFAULT_ZOOM));
    }
}