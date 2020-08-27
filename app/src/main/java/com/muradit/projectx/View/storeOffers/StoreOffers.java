package com.muradit.projectx.View.storeOffers;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.Model.others.UploadImageFirebase;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterStoresOffers;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.StoreOffers.storeOffersViewModel;
import com.muradit.projectx.ViewModel.StoreOrders.StoreOrdersViewModel;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class StoreOffers extends Fragment {
    private RecyclerView  previousOffersRecycler;
    private LinearLayout newOfferLayout;
    private CardView  switchBtn;
    private ImageView offersBtnImage,offerImage;
    private TextView sectionTitle;
    private EditText offerContent;
    private Button saveButton;
    private storeOffersViewModel viewModel;
    //vars
    private boolean FLAG_SECTION;
    private String IMAGE_URL="no image";
    private DatePickerTimeline datePickerTimeline;
    private String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_store_offers, container, false);

        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.OffersTitle));

        viewModel=new storeOffersViewModel(getActivity());
        previousOffersRecycler=v.findViewById(R.id.rec);
        newOfferLayout=v.findViewById(R.id.newOfferLay);
        switchBtn=v.findViewById(R.id.offersBtn);
        offersBtnImage=v.findViewById(R.id.offersImg);
        sectionTitle=v.findViewById(R.id.offersText);
        offerContent=v.findViewById(R.id.offerContent);
        saveButton=v.findViewById(R.id.saveBtn);
        offerImage=v.findViewById(R.id.offerImage);
        FLAG_SECTION=true;

        datePickerTimeline=v.findViewById(R.id.datePickerTimeline);
        datePickerTimeline.setInitialDate(2020, 7, 21);
        datePickerTimeline.setDayTextColor(Color.parseColor("#42a5f4"));
        datePickerTimeline.setMonthTextColor(Color.parseColor("#ffc107"));
        datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                date=year+"/"+month+"/"+day;
            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {
                // Do Something
            }
        });

        newOfferLayout.setVisibility(View.GONE);
        getAllOffers();

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FLAG_SECTION){
                offersBtnImage.setImageResource(R.drawable.ic_sale);
                previousOffersRecycler.setVisibility(View.GONE);
                newOfferLayout.setVisibility(View.VISIBLE);
                sectionTitle.setText(getString(R.string.myOffers));
                    FLAG_SECTION=false;
                }else{
                    offersBtnImage.setImageResource(R.drawable.ic_assignment);
                    previousOffersRecycler.setVisibility(View.VISIBLE);
                    newOfferLayout.setVisibility(View.GONE);
                    sectionTitle.setText(getString(R.string.newOfferStre));
                    FLAG_SECTION=true;
                    getAllOffers();
                }

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfFilled();


            }
        });

        offerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });


        return v;
    }

    private void checkIfFilled() {
        if(!TextUtils.isEmpty(offerContent.getText().toString()))
            if(!TextUtils.isEmpty(date))
                if(!TextUtils.isEmpty(IMAGE_URL))
                    viewModel.addOffer(offerContent.getText().toString(),date,IMAGE_URL);
                else
                    Toast.makeText(getActivity(), getString(R.string.mustUpPhotoStr), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), getString(R.string.pickExpDate), Toast.LENGTH_SHORT).show();
        else
            offerContent.setError("required");

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
            offerImage.setImageURI(imgUriProfile);
            int rand=(int)(Math.random()*10000);
            String path=CurrentStore.name+ CurrentStore.email+rand;
            UploadImageFirebase upf=new UploadImageFirebase(getActivity(),path,imgUriProfile,"items");
            upf.FileUpload();
            upf.getUriDownlaod().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    IMAGE_URL=s;
                }
            });

        }
    }

    private void getAllOffers() {
        viewModel.getAllOffers().observe(getViewLifecycleOwner(), new Observer<List<Offers>>() {
            @Override
            public void onChanged(List<Offers> offers) {
                previousOffersRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                RecyclerAdapterStoresOffers recAdapter=new RecyclerAdapterStoresOffers(getActivity(),offers);
                previousOffersRecycler.setAdapter(recAdapter);

            }
        });
    }

}