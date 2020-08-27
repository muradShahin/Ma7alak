package com.muradit.projectx.View.MainActivity.ui.explore;


import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.ads.mediation.AbstractAdViewAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.LoginSignUpModel;
import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentLangLocal;
import com.muradit.projectx.Model.others.CurrentLastPos;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterMyStoresItems_explore;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterSalesHome;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterStores;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.MainActivityViewModel.MainViewModel;
import com.muradit.projectx.ViewModel.MyStoresItemsVM.MyStoresItemsViewModel;
import com.muradit.projectx.ViewModel.SalesViewModel.SalesViewModel;
import com.muradit.projectx.ViewModel.exploreViewModel.ExploreViewModel;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ExploreFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private ExploreViewModel exploreViewModel;
    private LinearLayout allItems,allStors,allOffers,myStoresItems;
    private SliderLayout slider;
    private RecyclerView StoresRecycler,OffersRecycler,myStoresItemsRec;
    private TextView allItemsText,allStoresText,allOffersText,myStoreItemsText;

    private ArrayList<String> listUrl = new ArrayList<>();
    private ArrayList<String> listName = new ArrayList<>();
    private ArrayList<String> itemId = new ArrayList<>();
    private ArrayList<AddItemModel> itemDetails = new ArrayList<>();
    private AdView adView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_explore, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.app_name));
        exploreViewModel=new ExploreViewModel(getActivity());

        //recyclerViewStores=root.findViewById(R.id.recStores);
       // recyclerViewItems=root.findViewById(R.id.recItems);
       // recyclerViewStores.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
       // recyclerViewItems.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


    /*    sales=root.findViewById(R.id.salesLay);
        myStores=root.findViewById(R.id.myStoresLay);*/

        slider=root.findViewById(R.id.slider);
        allItems=root.findViewById(R.id.allItems);
        allStors=root.findViewById(R.id.allStores);
        allOffers=root.findViewById(R.id.allOffers);
        myStoresItems=root.findViewById(R.id.myStoresItems);

        allItemsText=root.findViewById(R.id.allItemsTxt);
        allStoresText=root.findViewById(R.id.allStoresTxt);
        allOffersText=root.findViewById(R.id.allOffersTxt);
        myStoreItemsText=root.findViewById(R.id.myStoresTxt);

        //reset last position of all items fragment and all stores fragment
        CurrentLastPos.allItems_last_pos=0;
        CurrentLastPos.allStores_last_pos=0;

        //recyclerView init
        StoresRecycler=root.findViewById(R.id.recStores);
        StoresRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        OffersRecycler=root.findViewById(R.id.recOffers);
        OffersRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        myStoresItemsRec=root.findViewById(R.id.recMyStoresItems);
        myStoresItemsRec.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

//ads init
        adView=root.findViewById(R.id.adView);
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);




        initSlider();
        getAllStores();
        getAllItems();
        getAllOffers();
        getMyStoresItems();
        changeLanguage();


        allItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_allItemsFragment);

            }
        });

        allStors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_allStores);


            }
        });



        allOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_sales);
            }
        });

        myStoresItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_myStoresItems);
            }
        });


        return root;
    }

    private void changeLanguage() {

        try{
            if(CurrentLangLocal.Local.equalsIgnoreCase("ar")){
                Typeface face = ResourcesCompat.getFont(getActivity(), R.font.ar_font);
                allItemsText.setTypeface(face);
                allStoresText.setTypeface(face);
                allOffersText.setTypeface(face);
                myStoreItemsText.setTypeface(face);
            }
        }catch (NullPointerException e){
            Log.d("EXP1",e.getMessage());
        }


        MainViewModel mainViewModel=new MainViewModel(getActivity());
        mainViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<LoginSignUpModel>() {
            @Override
            public void onChanged(LoginSignUpModel loginSignUpModel) {
                if(loginSignUpModel.getLang().equals("ar")){
                    Typeface face = ResourcesCompat.getFont(getActivity(), R.font.ar_font);
                    allItemsText.setTypeface(face);
                    allStoresText.setTypeface(face);
                    allOffersText.setTypeface(face);
                    myStoreItemsText.setTypeface(face);
                }
            }
        });

    }

    private void initSlider() {

        RequestOptions requestOptions = new RequestOptions();


        for (int i = 0; i < listUrl.size(); i++) {
            TextSliderView sliderView = new TextSliderView(getActivity());
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    .description(listName.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", listName.get(i));
            sliderView.getBundle().putString("itemId",itemId.get(i));
            slider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);

        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.addOnPageChangeListener(this);
        slider.stopCyclingWhenTouch(false);

    }


    private void getAllStores() {

        exploreViewModel.getStores().observe(getViewLifecycleOwner(), new Observer<List<StoreModel>>() {
        @Override
        public void onChanged(List<StoreModel> storeModels) {
            RecyclerAdapterStores recAdapter=new RecyclerAdapterStores(getActivity(),storeModels);
            StoresRecycler.setAdapter(recAdapter);

        }

    });

    }

    private void getAllItems(){
        exploreViewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<AddItemModel>>() {
            @Override
            public void onChanged(List<AddItemModel> addItemModels) {

                for (int i=0;i<4;i++){
                    int index_random=(int)(Math.random()*((addItemModels.size()-1)+1));
                    String desc=addItemModels.get(index_random).getItem_desc();
                    listUrl.add(addItemModels.get(index_random).getItem_image());
                    itemId.add(addItemModels.get(index_random).getItem_id());
                    itemDetails.add(addItemModels.get(index_random));
                    if(addItemModels.get(index_random).getItem_desc().length()>40){
                        desc=desc.substring(0,35)+" "+getString(R.string.seeMoreStr)+" ....";
                    }
                    listName.add(desc);
                }
                initSlider();

            }

        });


    }

    public void getAllOffers(){
        SalesViewModel viewModel=new SalesViewModel();
        viewModel.getAllOffers().observe(getViewLifecycleOwner(), new Observer<List<Offers>>() {
            @Override
            public void onChanged(List<Offers> offers) {
                RecyclerAdapterSalesHome rec=new RecyclerAdapterSalesHome(getActivity(),offers);
                OffersRecycler.setAdapter(rec);

            }
        });
    }

    public void getMyStoresItems(){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null) {
            MyStoresItemsViewModel viewModel = new MyStoresItemsViewModel();
            viewModel.getStoresItems().observe(getViewLifecycleOwner(), new Observer<List<AddItemModel>>() {
                @Override
                public void onChanged(List<AddItemModel> addItemModels) {
                    RecyclerAdapterMyStoresItems_explore adapter = new RecyclerAdapterMyStoresItems_explore(getActivity(), addItemModels);
                    myStoresItemsRec.setAdapter(adapter);
                }
            });
        }
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        for (int i=0;i<itemDetails.size();i++){
            if(itemDetails.get(i).getItem_id().equals(slider.getBundle().getString("itemId"))){
                CurrentItem.store_email=itemDetails.get(i).getStore_email();
                CurrentItem.item_store=itemDetails.get(i).getItem_store();
                CurrentItem.item_state=itemDetails.get(i).getItem_state();
                CurrentItem.item_price=itemDetails.get(i).getItem_price();
                CurrentItem.item_image=itemDetails.get(i).getItem_image();
                CurrentItem.item_desc=itemDetails.get(i).getItem_desc();
                CurrentItem.item_category=itemDetails.get(i).getItem_category();
                CurrentItem.item_name=itemDetails.get(i).getItem_name();
                CurrentItem.item_id=itemDetails.get(i).getItem_id();
                Navigation.findNavController(getView()).navigate(R.id.action_nav_home_to_fullDetailsItemFragment2);
                break;

            }
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        slider.stopAutoCycle();
        listName.clear();
        listUrl.clear();
        itemDetails.clear();
        itemId.clear();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}