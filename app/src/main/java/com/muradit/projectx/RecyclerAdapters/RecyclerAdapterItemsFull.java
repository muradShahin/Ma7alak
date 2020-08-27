package com.muradit.projectx.RecyclerAdapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.cardview.widget.CardView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentLastPos;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.R;

import java.util.List;

public class RecyclerAdapterItemsFull extends    RecyclerView.Adapter<RecyclerAdapterItemsFull.viewitem> {



    List<AddItemModel> items;
    Context context;
    String rowType;
    InterstitialAd interstitialAd;

    public RecyclerAdapterItemsFull(Context c, List<AddItemModel> item,String rowType)
    {
        items=item;
        context=c;
        this.rowType=rowType;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView name,desc,status,price;
        private Button more_details;
        private ImageView itemImage;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
           if(rowType.equals("style1")) {
               name = itemView.findViewById(R.id.name);
               price = itemView.findViewById(R.id.price);
               desc = itemView.findViewById(R.id.desc);
               status = itemView.findViewById(R.id.status);
           }
            itemImage=itemView.findViewById(R.id.itemImg);
            more_details=itemView.findViewById(R.id.button2);

            interstitialAd=new InterstitialAd(context);
            //ca-app-pub-5224320711263350/6407947988
            interstitialAd.setAdUnitId("ca-app-pub-5224320711263350/6407947988");
            interstitialAd.loadAd(new AdRequest.Builder().build());




        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {

        View  itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_full_item, parent, false);

        //the itemView now is the row
        //We will add 2 onClicks
        if(rowType.equals("style1")) {
            itemView= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_full_item, parent, false);
        }else if(rowType.equals("style2")){
            itemView= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_full_item_style2, parent, false);
        }



        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getItem_image()).into(holder.itemImage);
        if(rowType.equals("style1")) {
            holder.name.setText(items.get(position).getItem_name());
            holder.price.setText(items.get(position).getItem_price() + " JD");
            holder.status.setText(items.get(position).getItem_state());
            String desc=items.get(position).getItem_desc();
            if(desc.length()>40)
                desc=desc.substring(0,39)+"....";

            holder.desc.setText(desc);
        }

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

                CurrentLastPos.allItems_last_pos=position;


                if(interstitialAd.isLoaded())
                     interstitialAd.show();

                   if(Flags.wishItem.equals("allItems"))
                   Navigation.findNavController(v).navigate(R.id.action_allItemsFragment_to_fullDetailsItemFragment);
               else if(Flags.wishItem.equals("wish"))
                   Navigation.findNavController(v).navigate(R.id.action_wishListFragment_to_fullDetailsItemFragment);
               else if(Flags.wishItem.equals("storeItem"))
                  Navigation.findNavController(v).navigate(R.id.action_insideStoreFragment_to_fullDetailsItemFragment);
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
