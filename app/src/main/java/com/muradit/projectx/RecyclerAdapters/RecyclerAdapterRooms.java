package com.muradit.projectx.RecyclerAdapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.muradit.projectx.Model.ChatRoom;
import com.muradit.projectx.Model.Offers;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.Model.others.CurrentChatRoom;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentStoreChat;
import com.muradit.projectx.Model.others.CurrentStoreChatStatic;
import com.muradit.projectx.Model.others.CurrentUserChat;
import com.muradit.projectx.Model.others.CurrentUserChatStatic;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.Model.others.MessagesNumber;
import com.muradit.projectx.R;
import com.muradit.projectx.View.allItems.AllItemsFragment;
import com.muradit.projectx.ViewModel.allItems.allItemsViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterRooms extends    RecyclerView.Adapter<RecyclerAdapterRooms.viewitem> {



    List<ChatRoom> items;
    Context context;

    public RecyclerAdapterRooms(Context c, List<ChatRoom> item )
    {
        items=item;
        context=c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {



        private RelativeLayout roomLayout;
        private ImageView image;
        private TextView name;
        private CardView notificationCard;
        private TextView notificationNumber;



        //initialize
        public viewitem(View itemView) {
            super(itemView);
            roomLayout=itemView.findViewById(R.id.roomLay);
            image=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            notificationCard=itemView.findViewById(R.id.cvNotif);
            notificationNumber=itemView.findViewById(R.id.notifNumb);







        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rooms, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {

        if(Flags.Customer_flag) {
            configRoomStore(holder, position);
            getNumberOfMessages(holder,items.get(position).getRoomId(),items.get(position).getCustomerId());
        }
        else {
            configRoomCust(holder, position);
            getNumberOfMessages(holder,items.get(position).getRoomId(), items.get(position).getStoreId());

        }
        holder.roomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentChatRoom.roomId=items.get(position).getRoomId();
                Log.d("roomId",CurrentChatRoom.roomId);

                Flags.Flag_Room=true;
                if(Flags.Customer_flag){
                    CurrentStoreChatStatic.name=items.get(position).getStoreChat().getName();
                    CurrentStoreChatStatic.email=items.get(position).getStoreChat().getEmail();
                    CurrentStoreChatStatic.about=items.get(position).getStoreChat().getAbout();
                    CurrentStoreChatStatic.city=items.get(position).getStoreChat().getCity();
                    CurrentStoreChatStatic.image=items.get(position).getStoreChat().getImage();
                    CurrentStoreChatStatic.id=items.get(position).getStoreChat().getId();
                    CurrentStoreChatStatic.lat=items.get(position).getStoreChat().getLat();
                    CurrentStoreChatStatic.lng=items.get(position).getStoreChat().getLng();
                    CurrentStoreChatStatic.ownerEmail=items.get(position).getStoreChat().getOwnerEmail();
                    CurrentStoreChatStatic.ownerName=items.get(position).getStoreChat().getOwnerName();
                    CurrentStoreChatStatic.phone=items.get(position).getStoreChat().getPhone();


                }else{
                    CurrentUserChatStatic.name=items.get(position).getUserChat().getName();
                    CurrentUserChatStatic.email=items.get(position).getUserChat().getEmail();
                    CurrentUserChatStatic.city=items.get(position).getUserChat().getCity();
                    CurrentUserChatStatic.image=items.get(position).getUserChat().getImage();
                    CurrentUserChatStatic.id=items.get(position).getUserChat().getId();
                    CurrentUserChatStatic.lat=items.get(position).getUserChat().getLat();
                    CurrentUserChatStatic.lng=items.get(position).getUserChat().getLng();
                    CurrentUserChatStatic.phone=items.get(position).getUserChat().getPhone();
                }
                Navigation.findNavController(v).navigate(R.id.action_chatHeaders_to_chatRoom);
            }
        });


    }

    private void getNumberOfMessages(final viewitem holder, String roomId, String id) {
        MessagesNumber mn=new MessagesNumber();
        mn.getMessagesForRoom(roomId,id);
        mn.getNumberOfMessagesForRoom().observe((LifecycleOwner) context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer>0) {
                    holder.notificationCard.setVisibility(View.VISIBLE);
                    holder.notificationNumber.setText(integer + "");
                }
            }
        });
    }

    private void configRoomStore(final viewitem holder, final int position) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Stores");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(items.get(position).getStoreId())){
                        holder.name.setText(key.child("name").getValue().toString());
                        Glide.with(context).load(key.child("image").getValue().toString())
                                .into(holder.image);
                        CurrentStoreChat storeChat=new CurrentStoreChat();
                        storeChat.setImage(key.child("image").getValue().toString());
                        storeChat.setName(key.child("name").getValue().toString());
                        storeChat.setEmail(key.child("email").getValue().toString());
                        storeChat.setPhone(key.child("phone").getValue().toString());
                        storeChat.setCity(key.child("city").getValue().toString());
                        storeChat.setOwnerName(key.child("ownerName").getValue().toString());
                        storeChat.setOwnerEmail(key.child("ownerEmail").getValue().toString());
                        storeChat.setLat(key.child("lat").getValue().toString());
                        storeChat.setLng(key.child("lng").getValue().toString());
                        storeChat.setAbout(key.child("about").getValue().toString());
                        storeChat.setId(key.getKey());
                        items.get(position).setStoreChat(storeChat);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void configRoomCust(final viewitem holder, final int pos) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(items.get(pos).getCustomerId())){
                        holder.name.setText(key.child("name").getValue().toString());
                        Glide.with(context).load(key.child("image").getValue().toString())
                                .into(holder.image);

                        CurrentUserChat userChat=new CurrentUserChat();
                        userChat.setImage(key.child("image").getValue().toString());
                        userChat.setName(key.child("name").getValue().toString());
                        userChat.setEmail(key.child("email").getValue().toString());
                        userChat.setPhone(key.child("phone").getValue().toString());
                        userChat.setCity(key.child("city").getValue().toString());
                        userChat.setLat(key.child("lat").getValue().toString());
                        userChat.setLng(key.child("lng").getValue().toString());
                        userChat.setId(key.getKey());
                        items.get(pos).setUserChat(userChat);
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