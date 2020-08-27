package com.muradit.projectx.RecyclerAdapters;


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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.Invoice;
import com.muradit.projectx.Model.others.NotificationsSender;
import com.muradit.projectx.Model.others.Orders;
import com.muradit.projectx.R;
import com.muradit.projectx.View.StoreOrders.CustomerLocation;
import com.muradit.projectx.ViewModel.StoreOrders.StoreOrdersViewModel;

import java.util.List;

public class RecyclerAdapterStoreOrders extends    RecyclerView.Adapter<RecyclerAdapterStoreOrders.viewitem> {


    private static final int REQUEST_CALL =1 ;
    List<Orders> items;
    Context context;
    private StoreOrdersViewModel storeOrdersViewModel;

    public RecyclerAdapterStoreOrders(Context c, List<Orders> item)
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
        private CardView callCard,locationCard,emailCard;
        private String customerEmail;
        private Button delivered,decline;



        //initialize
        public viewitem(View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.username);
            itemName=itemView.findViewById(R.id.itemName);
            itemImage=itemView.findViewById(R.id.itemImg);
            date=itemView.findViewById(R.id.date);
            quantity=itemView.findViewById(R.id.quantity);

            callCard=itemView.findViewById(R.id.callCard);
            locationCard=itemView.findViewById(R.id.locationCard);
            emailCard=itemView.findViewById(R.id.mailCard);
            delivered=itemView.findViewById(R.id.deliverdBtn);
            decline=itemView.findViewById(R.id.declineBtn);
            storeOrdersViewModel=new StoreOrdersViewModel(context);






        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_store_order, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        holder.quantity.setText(context.getString(R.string.QuantityStr)+" : "+items.get(position).getQuantity());
        holder.date.setText(items.get(position).getDate());
        getUserInfo(holder,items.get(position).getUser_id());
        getItemInfo(items.get(position).getItem_id(),position,holder);

        holder.callCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(items.get(position).getCustomer_phone());
            }
        });

        holder.emailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"+holder.customerEmail));
                context.startActivity(Intent.createChooser(emailIntent, "Send email to the customer"));
            }
        });

        holder.locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, CustomerLocation.class);
                i.putExtra("lat",items.get(position).getLat());
                i.putExtra("lng",items.get(position).getLng());
                context.startActivity(i);

            }
        });

        holder.delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeOrdersViewModel.moveToArchive(items.get(position));
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrder(items.get(position).getRequest_ID(),holder.customerEmail,items.get(position).getStore_name());
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
                        holder.customerEmail=key.child("email").getValue().toString();
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
                        item.setStore_id(key.child("store_id").getValue().toString());
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


    private void callPhone(String phone){

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

        }else{
            String dia="tel:"+ phone;
            context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dia)));

        }

    }

    private void deleteOrder(String reqKey, final String customerEmail, final String storeName) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.child(reqKey).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sendNotification(customerEmail,storeName);
                        Toast.makeText(context, context.getString(R.string.deleteStr), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendNotification(String customerEmail,String storeName) {
        NotificationsSender ns=new NotificationsSender("spec");
                ns.sendNotification(customerEmail,storeName+" "+context.getString(R.string.declinedOrderStr));
    }

    // private final View.OnClickListener mOnClickListener = new MyOnClickListener();



//    @Override
//    public void onClick(final View view) {
//        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//        String item = mList.get(itemPosition);
//        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
//    }


}
