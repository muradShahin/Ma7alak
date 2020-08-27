package com.muradit.projectx.ViewModel.ReviewsViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.muradit.projectx.Model.Reviews;
import com.muradit.projectx.Repository.ReviewsRepository.ReviewsRepository;

import java.util.List;

public class ReviewsViewModel {
    private ReviewsRepository reviewsRepository;
    private Context context;

    public ReviewsViewModel(Context context) {
        this.context = context;
        reviewsRepository=new ReviewsRepository(context);
    }

    public void postReview(String review){
        reviewsRepository.postReview(review);
    }

    public LiveData<List<Reviews>> getAllReviews(){
       return reviewsRepository.getReviewsList();
    }
}
