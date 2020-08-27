package com.muradit.projectx.View.popOutWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterCategory;
import com.muradit.projectx.RecyclerAdapters.RecyclerCategroisPopWindow;
import com.muradit.projectx.ViewModel.popViewModel.popViewModel;

import java.util.List;

public class popWindow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private popViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_window);
        getSupportActionBar().hide();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width=displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;

        getWindow().setLayout((int)(width *0.8),(int)(height*0.8));

        viewModel=new popViewModel(this);
        recyclerView=findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        prepreCategories();

    }

    private void prepreCategories(){
        viewModel.getCategoriesList().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                RecyclerCategroisPopWindow recyclerCategroisPopWindow=new RecyclerCategroisPopWindow(popWindow.this,categories);
                recyclerView.setAdapter(recyclerCategroisPopWindow);
            }
        });

    }


}
