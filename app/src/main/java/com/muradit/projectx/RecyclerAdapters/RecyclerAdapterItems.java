package com.muradit.projectx.RecyclerAdapters;

/*
public class RecyclerAdapterItems {
}
*/

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
import com.muradit.projectx.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterItems extends    RecyclerView.Adapter<RecyclerAdapterItems.viewitem> {



    List<AddItemModel> items;
    Context context;

    public RecyclerAdapterItems(Context c, List<AddItemModel> item)
    {
        items=item;
        context=c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView name,price;
        private CardView itemCard;
        private ImageView itemImage;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.itemName);
            price=itemView.findViewById(R.id.itemPrice);
            itemImage=itemView.findViewById(R.id.itemImg);
            itemCard=itemView.findViewById(R.id.itemCard);





        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_items, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getItem_image()).into(holder.itemImage);
        holder.name.setText(items.get(position).getItem_name());
        holder.price.setText(items.get(position).getItem_price()+" JD");

        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentItem.store_email=items.get(position).getStore_email();
                CurrentItem.item_name=items.get(position).getItem_name();
                CurrentItem.item_category=items.get(position).getItem_category();
                CurrentItem.item_desc=items.get(position).getItem_desc();
                CurrentItem.item_category=items.get(position).getItem_category();
                CurrentItem.item_image=items.get(position).getItem_image();
                CurrentItem.item_price=items.get(position).getItem_price();
                CurrentItem.item_state=items.get(position).getItem_state();
                CurrentItem.item_id=items.get(position).getItem_id();

                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_fullDetailsItemFragment);


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
