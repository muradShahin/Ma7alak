package com.muradit.projectx.View.allStores;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.Model.others.CurrentLastPos;
import com.muradit.projectx.Model.others.GetCategories;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdaperStoreByCategory;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterCategory;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterStoresFull;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.allStores.allStoresViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class allStores extends Fragment {
    public allStores() {
        // Required empty public constructor
    }

    private allStoresViewModel viewModel;
    private RecyclerView recyclerView,categoryRecycler;
    private CardView categBtn;
    private ImageView arrowImage;
    private AdView adView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_all_stores, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.allStoresStr));
        viewModel=new allStoresViewModel(getActivity());
        recyclerView=root.findViewById(R.id.recStores);
        categoryRecycler=root.findViewById(R.id.recCateg);
        categBtn=root.findViewById(R.id.categBtn);
        arrowImage=root.findViewById(R.id.arrowImage);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        //ads init
        adView=root.findViewById(R.id.adView2);
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getAllStores();


        //handling on cardBtn click
        categBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryRecycler.getVisibility() == View.VISIBLE) {
                    categoryRecycler.setVisibility(View.GONE);
                    arrowImage.setImageResource(R.drawable.ic_arrow_down);
                    Category.Flag=false;
                    getAllStores();
                }
                else {
                    getAllCategories();
                    arrowImage.setImageResource(R.drawable.ic_arrow_up);
                }
            }
        });



        return root;
    }

    private void getAllStores() {
     viewModel.getAllStores().observe(getViewLifecycleOwner(), new Observer<List<StoreModel>>() {
         @Override
         public void onChanged(List<StoreModel> storeModels) {
             RecyclerAdapterStoresFull recStores=new RecyclerAdapterStoresFull(getActivity(),storeModels);
             recyclerView.setAdapter(recStores);
             recyclerView.scrollToPosition(CurrentLastPos.allStores_last_pos);

         }
     });
    }

    private void getAllCategories() {
        GetCategories getCategories=new GetCategories();
        getCategories.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
               RecyclerAdaperStoreByCategory recyclerAdapterCategory=new RecyclerAdaperStoreByCategory(getActivity(),categories,recyclerView);
                categoryRecycler.setAdapter(recyclerAdapterCategory);
                categoryRecycler.setVisibility(View.VISIBLE);
            }
        });

    }


}
