package com.muradit.projectx.RecyclerAdapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muradit.projectx.Model.others.Invoice;
import com.muradit.projectx.Model.others.Orders;
import com.muradit.projectx.R;

import java.util.List;

public class RecyclerAdapterUserOrders extends    RecyclerView.Adapter<RecyclerAdapterUserOrders.viewitem> {



    List<Orders> items;
    Context context;

    public RecyclerAdapterUserOrders(Context c, List<Orders> item)
    {
        items=item;
        context=c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView nameItem,storeName,date,quantity,price;
        private ImageView itemImage;
        private Button moreBtn;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
            nameItem=itemView.findViewById(R.id.nameItem);
            price=itemView.findViewById(R.id.price);
            itemImage=itemView.findViewById(R.id.itemImg);
            moreBtn=itemView.findViewById(R.id.seeMoreBtn);
            storeName=itemView.findViewById(R.id.storeName);
            date=itemView.findViewById(R.id.date);
            quantity=itemView.findViewById(R.id.quantity);






        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_user_orders, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        Glide.with(context).load(items.get(position).getItemInfo().getItem_image()).into(holder.itemImage);
        holder.nameItem.setText(items.get(position).getItemInfo().getItem_name());
        holder.storeName.setText(items.get(position).getStore_name());
        holder.quantity.setText(context.getString(R.string.QuantityStr)+" : "+items.get(position).getQuantity());
        holder.date.setText(items.get(position).getDate());
        holder.price.setText(context.getString(R.string.priceStr)+" : "+items.get(position).getItemInfo().getItem_price());

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invoice.currentOrder=items.get(position);
                Navigation.findNavController(v).navigate(R.id.action_nav_gallery_to_invoiceFragment2);
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
