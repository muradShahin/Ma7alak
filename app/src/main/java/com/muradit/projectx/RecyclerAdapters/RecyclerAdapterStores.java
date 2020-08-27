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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterStores extends    RecyclerView.Adapter<RecyclerAdapterStores.viewitem> {



    List<StoreModel> items;
    Context context;

    public RecyclerAdapterStores(Context c, List<StoreModel> item)
    {
        items=item;
        context=c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView name;
        private CardView storeCard;
        private ImageView storeImage;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.storeNameR);
            storeImage=itemView.findViewById(R.id.storeImage);
            storeCard=itemView.findViewById(R.id.storeCard);





        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_stores, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getImage()).into(holder.storeImage);
        holder.name.setText(items.get(position).getName());

        holder.storeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentStore.name=items.get(position).getName();
                CurrentStore.email=items.get(position).getEmail();
                CurrentStore.city=items.get(position).getCity();
                CurrentStore.id=items.get(position).getId();
                CurrentStore.lat=items.get(position).getLat();
                CurrentStore.lng=items.get(position).getLng();
                CurrentStore.image=items.get(position).getImage();
                CurrentStore.ownerEmail=items.get(position).getOwnerEmail();
                CurrentStore.ownerName=items.get(position).getOwnerName();
                CurrentStore.phone=items.get(position).getPhone();
                CurrentStore.about=items.get(position).getAbout();

              Navigation.findNavController(v).navigate(R.id.action_nav_home_to_insideStoreFragment);
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
