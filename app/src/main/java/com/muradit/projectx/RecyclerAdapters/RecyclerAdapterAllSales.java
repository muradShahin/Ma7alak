package com.muradit.projectx.RecyclerAdapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.R;
import com.muradit.projectx.View.allItems.AllItemsFragment;
import com.muradit.projectx.ViewModel.allItems.allItemsViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterAllSales  extends    RecyclerView.Adapter<RecyclerAdapterAllSales .viewitem> {


    List<Offers> items;
    List<StoreModel> stores;
    Context context;

    public RecyclerAdapterAllSales (Context c, List<Offers> item) {
        items = item;
        context = c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class viewitem extends RecyclerView.ViewHolder {


        private TextView content,storeName,date;
        private ImageView offerImage,seeMore,storeImage;
        private CardView offerCard;


        //initialize
        public viewitem(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.offerContent);
            storeName=itemView.findViewById(R.id.storeName);
            date=itemView.findViewById(R.id.date);
            storeImage=itemView.findViewById(R.id.storeImg);
            seeMore=itemView.findViewById(R.id.seeMoreBtn);
            offerImage = itemView.findViewById(R.id.offerImage);
            storeName=itemView.findViewById(R.id.storeName);
            offerCard=itemView.findViewById(R.id.cv);

            stores=new ArrayList<>();



        }
    }


    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {


        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_sales_row, parent, false);


        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getOfferImage())
                .into(holder.offerImage);


        holder.date.setText(items.get(position).getExpireDate());

        holder.content.setText(items.get(position).getOfferContent());
        getStoreInfo(position,items.get(position).getStoreId(),holder);

        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentStore.id=stores.get(position).getId();
                CurrentStore.name=stores.get(position).getName();
                CurrentStore.city=stores.get(position).getCity();
                CurrentStore.about=stores.get(position).getAbout();
                CurrentStore.phone=stores.get(position).getPhone();
                CurrentStore.ownerName=stores.get(position).getOwnerName();
                CurrentStore.ownerEmail=stores.get(position).getOwnerEmail();
                CurrentStore.image=stores.get(position).getImage();
                CurrentStore.lng=stores.get(position).getLng();
                CurrentStore.lat=stores.get(position).getLat();
                CurrentStore.email=stores.get(position).getEmail();
                Navigation.findNavController(v).navigate(R.id.action_sales_to_insideStoreFragment);
            }
        });


    }

    private void getStoreInfo(final int pos, final String id, final viewitem holder) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Stores");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(id)){
                        String image=key.child("image").getValue().toString();
                        String email=key.child("email").getValue().toString();
                        String name=key.child("name").getValue().toString();
                        String city=key.child("city").getValue().toString();
                        String phone=key.child("phone").getValue().toString();
                        String ownerEmail=key.child("ownerEmail").getValue().toString();
                        String ownerName=key.child("ownerName").getValue().toString();
                        String lat=key.child("lat").getValue().toString();
                        String lng=key.child("lng").getValue().toString();
                        String category=key.child("category").getValue().toString();
                        String about=key.child("about").getValue().toString();

                        Glide.with(context).load(key.child("image").getValue().toString())
                                .into(holder.storeImage);
                        holder.storeName.setText(key.child("name").getValue().toString());
                        StoreModel sm=new StoreModel( image,  name,  email,  phone,  city,  ownerName,  ownerEmail,lat,lng,category,about);
                        sm.setId(key.getKey());
                        stores.add(pos,sm);

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }
}



// private final View.OnClickListener mOnClickListener = new MyOnClickListener();



//    @Override
//    public void onClick(final View view) {
//        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//        String item = mList.get(itemPosition);
//        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
//    }


