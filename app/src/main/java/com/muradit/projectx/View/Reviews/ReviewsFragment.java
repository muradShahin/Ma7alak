package com.muradit.projectx.View.Reviews;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.muradit.projectx.Model.Reviews;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.R;
import com.muradit.projectx.RecyclerAdapters.RecyclerAdapterReviews;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.ViewModel.ReviewsViewModel.ReviewsViewModel;

import java.util.List;


public class ReviewsFragment extends Fragment {
    private ImageView send;
    private EditText reviewText;
    private RecyclerView recyclerViewReview;
    private ReviewsViewModel viewModel;
    private LinearLayout sendLayout;
    private FirebaseAuth firebaseAuth;
    //vars
    private int Last_Pos;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_reviews, container, false);


        init(v);

        getAllReviews();

        return v;
    }

    private void init(View v) {
        viewModel=new ReviewsViewModel(getActivity());
        firebaseAuth=FirebaseAuth.getInstance();

        send=v.findViewById(R.id.sendIcon);
        reviewText=v.findViewById(R.id.reviewEditText);
        sendLayout=v.findViewById(R.id.sendLay);
        recyclerViewReview=v.findViewById(R.id.recyclerView);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        if(Flags.CUST_STORE.equals("store") || firebaseAuth.getCurrentUser()==null)
             sendLayout.setVisibility(View.GONE);
        else
            sendLayout.setVisibility(View.VISIBLE);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.postReview(reviewText.getText().toString());
                reviewText.setText("");
                reviewText.setHint(R.string.reviewHint);
                hideKeyboardFrom(getActivity(),v);
                ((LinearLayoutManager)recyclerViewReview.getLayoutManager()).setStackFromEnd(false);
                recyclerViewReview.scrollToPosition(Last_Pos);
            }
        });

        reviewText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    ((LinearLayoutManager)recyclerViewReview.getLayoutManager()).setStackFromEnd(true);
                }
            }
        });


    }
    private void getAllReviews(){
        viewModel.getAllReviews().observe(getViewLifecycleOwner(), new Observer<List<Reviews>>() {
            @Override
            public void onChanged(List<Reviews> reviews) {
                Last_Pos=reviews.size()-1;
                RecyclerAdapterReviews recyclerAdapterReviews=new RecyclerAdapterReviews(getActivity(),reviews);
                recyclerViewReview.setAdapter(recyclerAdapterReviews);
            }
        });
    }



    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity())
                .setActionBarTitle(getString(R.string.reviewStr));
        super.onResume();
    }
}