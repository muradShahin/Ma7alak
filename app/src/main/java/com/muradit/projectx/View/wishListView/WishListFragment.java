package com.muradit.projectx.View.wishListView;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterItemsFull;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.wishListViewModel.WishListViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishListFragment extends Fragment {

    private WishListViewModel viewModel;
    private RecyclerView recyclerView;

    public WishListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_wish_list, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.wishList));
        init(v);
        getWishListItems();





      return v;
    }

    private void init(View v) {
        viewModel=new WishListViewModel(getActivity());
        recyclerView=v.findViewById(R.id.rec);
        Flags.wishItem="wish";


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

    }

    public void getWishListItems(){
        viewModel.getWishLists().observe(getViewLifecycleOwner(), new Observer<List<AddItemModel>>() {
            @Override
            public void onChanged(List<AddItemModel> list) {
                RecyclerAdapterItemsFull recyclerAdapterItemsFull=new RecyclerAdapterItemsFull(getActivity(),list,"style1");
                recyclerView.setAdapter(recyclerAdapterItemsFull);
            }
        });



    }

}
