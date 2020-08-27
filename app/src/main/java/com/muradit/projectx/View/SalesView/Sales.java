package com.muradit.projectx.View.SalesView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterAllSales;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterSalesHome;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.SalesViewModel.SalesViewModel;

import java.util.List;

public class Sales extends Fragment {
    private SalesViewModel viewModel;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_sales, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.offersStr));

        init(root);

        return root;
    }

    private void init(View root) {
      viewModel=new SalesViewModel();
      recyclerView=root.findViewById(R.id.rec);
      recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));


      getAllOffers();
    }

    private void getAllOffers() {
    viewModel.getAllOffers().observe(getViewLifecycleOwner(), new Observer<List<Offers>>() {
        @Override
        public void onChanged(List<Offers> offers) {
            RecyclerAdapterAllSales rec=new RecyclerAdapterAllSales(getActivity(),offers);
            recyclerView.setAdapter(rec);

        }
    });

    }


}