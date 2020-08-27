package com.muradit.projectx.RecyclerAdapters;

/*
public class RecyclerAdapterStoreArchived {
}
*/

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Invoice;
import com.muradit.projectx.Model.others.Orders;
import com.muradit.projectx.R;
import com.muradit.projectx.View.StoreOrders.CustomerLocation;
import com.muradit.projectx.ViewModel.StoreOrders.StoreOrdersViewModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RecyclerAdapterStoreArchived extends    RecyclerView.Adapter<RecyclerAdapterStoreArchived.viewitem> {


    private static final int REQUEST_CALL =1 ;
    List<Orders> items;
    Context context;
    private StoreOrdersViewModel storeOrdersViewModel;

    public RecyclerAdapterStoreArchived(Context c, List<Orders> item)
    {
        items=item;
        context=c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView userName,itemName,date,quantity;
        private ImageView itemImage;
        private Button remove;


        //initialize
        public viewitem(View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.username);
            itemName=itemView.findViewById(R.id.itemName);
            itemImage=itemView.findViewById(R.id.itemImg);
            date=itemView.findViewById(R.id.date);
            quantity=itemView.findViewById(R.id.quantity);
            remove=itemView.findViewById(R.id.declineBtn);


            storeOrdersViewModel=new StoreOrdersViewModel(context);






        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_archived, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        holder.quantity.setText(context.getString(R.string.QuantityStr)+" : "+items.get(position).getQuantity());
        holder.date.setText(items.get(position).getDate());
        getUserInfo(holder,items.get(position).getUser_id());
        getItemInfo(items.get(position).getItem_id(),position,holder);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(context.getString(R.string.deleteArch))
                        .setContentText(context.getString(R.string.deleteArch2))
                        .setConfirmText("ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                storeOrdersViewModel.removeFromArchived(items.get(position).getRequest_ID());
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        });






    }
    private void getUserInfo(final viewitem holder, final String userID){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(userID)){
                        holder.userName.setText(key.child("name").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getItemInfo(final String itemId, final int pos, final viewitem holder){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(itemId)){
                        AddItemModel item=new AddItemModel(key.child("item_name").getValue().toString(),
                                key.child("item_desc").getValue().toString(),
                                key.child("item_price").getValue().toString(),
                                key.child("item_category").getValue().toString(),
                                key.child("item_store").getValue().toString(),
                                key.child("store_email").getValue().toString(),
                                key.child("item_image").getValue().toString(),
                                key.child("item_state").getValue().toString());
                        item.setItem_id(key.getKey());
                        items.get(pos).setItemInfo(item);
                        Glide.with(context).load( key.child("item_image").getValue().toString())
                                .into(holder.itemImage);
                        holder.itemName.setText(key.child("item_name").getValue().toString());
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
