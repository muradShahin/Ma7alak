package com.muradit.projectx.RecyclerAdapters;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.R;

import java.util.List;

public class RecyclerAdapterStoresItems extends    RecyclerView.Adapter<RecyclerAdapterStoresItems.viewitem> {



    List<AddItemModel> items;
    Context context;

    public RecyclerAdapterStoresItems(Context c, List<AddItemModel> item)
    {
        items=item;
        context=c;


    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView name,desc,status,price,storeName;
        private Button more_details;
        private ImageView itemImage,storesImage;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
                name = itemView.findViewById(R.id.name);
                price = itemView.findViewById(R.id.price);
                desc = itemView.findViewById(R.id.desc);
                status = itemView.findViewById(R.id.status);
                storesImage=itemView.findViewById(R.id.store_image);
                storeName=itemView.findViewById(R.id.storeName);

            itemImage=itemView.findViewById(R.id.itemImg);
            more_details=itemView.findViewById(R.id.button2);






        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {

        View  itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_stores_items, parent, false);



        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getItem_image()).into(holder.itemImage);
            holder.name.setText(items.get(position).getItem_name());
            holder.price.setText(items.get(position).getItem_price() + " JD");
            holder.desc.setText(items.get(position).getItem_desc());
            holder.status.setText(items.get(position).getItem_state());

            getStoreInfo(items.get(position).getStore_id(),holder);

        holder.more_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentItem.item_store = items.get(position).getItem_store();
                CurrentItem.item_state = items.get(position).getItem_state();
                CurrentItem.store_email = items.get(position).getStore_email();
                CurrentItem.item_price = items.get(position).getItem_price();
                CurrentItem.item_image = items.get(position).getItem_image();
                CurrentItem.item_id = items.get(position).getItem_id();
                CurrentItem.item_desc = items.get(position).getItem_desc();
                CurrentItem.item_category = items.get(position).getItem_category();
                CurrentItem.item_name = items.get(position).getItem_name();
                Navigation.findNavController(v).navigate(R.id.action_myStoresItems_to_fullDetailsItemFragment);

            }
        });





    }

    private void getStoreInfo(final String storeId, final viewitem holder) {
        FirebaseDatabase.getInstance().getReference("Stores")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot key:dataSnapshot.getChildren()){
                            if(key.getKey().equals(storeId)){
                                Glide.with(context).load(key.child("image").getValue().toString())
                                        .into(holder.storesImage);
                                holder.storeName.setText(key.child("name").getValue().toString());
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



    // private final View.OnClickListener mOnClickListener = new MyOnClickListener();



//    @Override
//    public void onClick(final View view) {
//        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//        String item = mList.get(itemPosition);
//        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
//    }


}
