package com.muradit.projectx.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.R;
import com.muradit.projectx.View.allItems.AllItemsFragment;
import com.muradit.projectx.ViewModel.allItems.allItemsViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterStoresOffers extends    RecyclerView.Adapter<RecyclerAdapterStoresOffers.viewitem> {



    List<Offers> items;
    Context context;

    public RecyclerAdapterStoresOffers(Context c, List<Offers> item )
    {
        items=item;
        context=c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView content,date;
        private CardView deleteCard;
        private ImageView offerImage;



        //initialize
        public viewitem(View itemView) {
            super(itemView);
            content=itemView.findViewById(R.id.content);
            date=itemView.findViewById(R.id.expireDate);
            deleteCard=itemView.findViewById(R.id.deleteCard);
            offerImage=itemView.findViewById(R.id.offerImage);





        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_store_offers, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getOfferImage())
                .into(holder.offerImage);

        holder.content.setText(items.get(position).getOfferContent());
        holder.date.setText(items.get(position).getExpireDate());

        holder.deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });







    }



    @Override
    public int getItemCount() {
        return items.size();
    }



    // private final View.OnClickListener mOnClickListener = new MyOnClickListener();



//    @Override
//    public void onClick(final View view) {
//        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//        String item = mList.get(itemPosition);
//        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
//    }


}