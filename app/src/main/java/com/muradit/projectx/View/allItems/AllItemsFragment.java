package com.muradit.projectx.View.allItems;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.Model.others.CurrentLastPos;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterCategory;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterItemsFull;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.allItems.allItemsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllItemsFragment extends Fragment {

    private allItemsViewModel viewModel;
    private RecyclerView recyclerView,categoryRecycler;
    private CardView categBtn;
    private ImageView arrowImage;
    private List<AddItemModel> allItemsList;
    private FloatingActionButton styleBtn;
    private AdView adView;
    public String styleType="style1";

    public AllItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_all_items, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.itemsForSale));
         viewModel=new allItemsViewModel(getActivity());
         adView=root.findViewById(R.id.adView);
         allItemsList=new ArrayList<>();
         recyclerView=root.findViewById(R.id.recItems);
         categoryRecycler=root.findViewById(R.id.recCateg);
         categBtn=root.findViewById(R.id.categBtn);
         arrowImage=root.findViewById(R.id.arrowImage);
         styleBtn=root.findViewById(R.id.floatingActionButton2);
         Flags.wishItem="allItems";

         categoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

         getALLItems();



         //handling on cardBtn click
        categBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryRecycler.getVisibility() == View.VISIBLE) {
                    categoryRecycler.setVisibility(View.GONE);
                    arrowImage.setImageResource(R.drawable.ic_arrow_down);
                    Category.Flag=false;
                    getALLItems();
                }
                else {
                    getAllCategories();
                    arrowImage.setImageResource(R.drawable.ic_arrow_up);
                }
                }
        });

        styleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(styleType.equals("style1")){
                    styleType="style2";
                    getALLItems();
                }else if(styleType.equals("style2")){
                    styleType="style1";
                    getALLItems();
                }

            }
        });



          return root;
    }



    private void getALLItems(){

        viewModel.getListItems().observe(getViewLifecycleOwner(), new Observer<List<AddItemModel>>() {
                @Override
                public void onChanged(List<AddItemModel> addItemModels) {
                    //allItemsList=addItemModels;
                    if(styleType.equals("style2")){
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                        styleBtn.setImageResource(R.drawable.ic_menu);
                    }else if(styleType.equals("style1")){
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                        styleBtn.setImageResource(R.drawable.ic_grid);
                    }

                    RecyclerAdapterItemsFull recyclerAdapterItemsFull = new RecyclerAdapterItemsFull(getActivity(), addItemModels,styleType);
                    recyclerView.setAdapter(recyclerAdapterItemsFull);
                    recyclerView.smoothScrollToPosition(CurrentLastPos.allItems_last_pos);

                }
            });

    }


    private void getAllCategories() {
        viewModel.getAllCategroies().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                RecyclerAdapterCategory recyclerAdapterCategory=new RecyclerAdapterCategory(getActivity(),categories,recyclerView,styleType);
                categoryRecycler.setAdapter(recyclerAdapterCategory);
                categoryRecycler.setVisibility(View.VISIBLE);

            }
        });

    }


}
