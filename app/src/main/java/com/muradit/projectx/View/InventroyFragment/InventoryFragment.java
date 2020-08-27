package com.muradit.projectx.View.InventroyFragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterInventory;
import com.muradit.projectx.ViewModel.InventoryViewModel.InventoryViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment {


    public InventoryFragment() {
        // Required empty public constructor
    }
    private InventoryViewModel inventoryViewModel;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_inventory, container, false);
        inventoryViewModel=new InventoryViewModel();
        recyclerView=root.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));



        inventoryViewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<AddItemModel>>() {
            @Override
            public void onChanged(List<AddItemModel> addItemModels) {
                RecyclerAdapterInventory recyclerAdapterInventory=new RecyclerAdapterInventory(getActivity(),addItemModels);
                recyclerView.setAdapter(recyclerAdapterInventory);
            }
        });

         return root;
    }

}
