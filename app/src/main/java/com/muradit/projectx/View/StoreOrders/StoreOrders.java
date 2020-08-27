package com.muradit.projectx.View.StoreOrders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.muradit.projectx.Model.others.Orders;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterStoreArchived;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterStoreOrders;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.StoreOrders.StoreOrdersViewModel;
import com.muradit.projectx.ViewModel.storeViewModel.StoreViewModel;

import java.util.List;


public class StoreOrders extends Fragment {

    private LinearLayout pending,archive;
    private RecyclerView recyclerView,recyclerArchived;
    private StoreOrdersViewModel storeOrdersViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_store_orders, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.storeOrderStr));
        init(v);


      return v;
    }

    private void init(View v) {
        storeOrdersViewModel=new StoreOrdersViewModel(getActivity());
        pending=v.findViewById(R.id.pending);
        archive=v.findViewById(R.id.archive);
        recyclerView=v.findViewById(R.id.rec);
        recyclerArchived=v.findViewById(R.id.recArcived);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerArchived.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerArchived.setVisibility(View.GONE);

        getAllOrders();
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerArchived.setVisibility(View.GONE);
                getAllOrders();

            }
        });

        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerArchived.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                getArchivedOrders();
            }
        });

    }


    private void getAllOrders(){
        storeOrdersViewModel.getOrders().observe(getViewLifecycleOwner(), new Observer<List<Orders>>() {
            @Override
            public void onChanged(List<Orders> orders) {
                RecyclerAdapterStoreOrders rec=new RecyclerAdapterStoreOrders(getActivity(),orders);

                recyclerView.setAdapter(rec);
            }
        });
    }

    private void getArchivedOrders() {
        storeOrdersViewModel.getArchived().observe(getViewLifecycleOwner(), new Observer<List<Orders>>() {
            @Override
            public void onChanged(List<Orders> orders) {
                RecyclerAdapterStoreArchived recArc=new RecyclerAdapterStoreArchived(getActivity(),orders);

                recyclerArchived.setAdapter(recArc);

            }
        });
    }

}