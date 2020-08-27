package com.muradit.projectx.View.MyStoresItems;

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
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterStoresItems;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.MyStoresItemsVM.MyStoresItemsViewModel;

import java.util.List;


public class MyStoresItems extends Fragment {
    private MyStoresItemsViewModel viewModel;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_my_stores, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.myStores));
        init(v);
        getItems();
        return v;
    }

    private void init(View v){
        viewModel=new MyStoresItemsViewModel();
        recyclerView=v.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

    }


    private void getItems() {


    viewModel.getStoresItems().observe(getViewLifecycleOwner(), new Observer<List<AddItemModel>>() {
        @Override
        public void onChanged(List<AddItemModel> addItemModels) {
            RecyclerAdapterStoresItems recycler=new RecyclerAdapterStoresItems(getActivity(),addItemModels);
            recyclerView.setAdapter(recycler);
            //Toast.makeText(getActivity(), addItemModels.size()+"", Toast.LENGTH_SHORT).show();
        }
    });
    }
}