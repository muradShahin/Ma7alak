package com.muradit.projectx.RecyclerAdapters;

/*
public class RecyclerAdapterChats {
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.Messages;
import com.muradit.projectx.Model.Reviews;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentChatRoom;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.Model.others.CurrentStoreChat;
import com.muradit.projectx.Model.others.CurrentUserChat;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.Model.others.Flags;
import com.muradit.projectx.R;

import java.util.ArrayList;
import java.util.List;

public class  RecyclerAdapterChats extends    RecyclerView.Adapter< RecyclerAdapterChats.viewitem> {



    List<Messages> items;
    Context context;



    public RecyclerAdapterChats(Context c, List<Messages> item)
    {
        items=item;
        context=c;

    }


    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView review,review2;
        private ImageView user_Image,user_image2;
        CardView senderCard,reciverCard;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
            user_Image=itemView.findViewById(R.id.user_image);
            review=itemView.findViewById(R.id.review);
            review2=itemView.findViewById(R.id.review2);
            user_image2=itemView.findViewById(R.id.user_image2);
            senderCard=itemView.findViewById(R.id.cv2);
            reciverCard=itemView.findViewById(R.id.cv1);






        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        String contentToSend=items.get(position).getContent();

        if(Flags.Flag_Room) {
            if (items.get(position).getSenderId().equals(CurrentUserInfo.id)
                    || items.get(position).getSenderId().equals(CurrentStore.id)) {
                holder.senderCard.setVisibility(View.VISIBLE);
                holder.reciverCard.setVisibility(View.GONE);
                holder.review2.setText(contentToSend);
            } else {
                holder.senderCard.setVisibility(View.GONE);
                holder.reciverCard.setVisibility(View.VISIBLE);
                holder.review.setText(contentToSend);
            }
        }else{
            if (items.get(position).getSenderId().equals(CurrentUserInfo.id)) {
                holder.senderCard.setVisibility(View.VISIBLE);
                holder.reciverCard.setVisibility(View.GONE);
                holder.review2.setText(contentToSend);
            } else {
                holder.senderCard.setVisibility(View.GONE);
                holder.reciverCard.setVisibility(View.VISIBLE);
                holder.review.setText(contentToSend);
            }
        }



        if(items.get(position).getSenderId().equals(CurrentChatRoom.userId))
            getUserInfo(holder,items.get(position).getSenderId(),position);
        else
            getStoreInfo(holder,items.get(position).getSenderId(),position);




    }
    private void getStoreInfo(final viewitem holder, final String id, final int position){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Stores");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(id)){
                        if(Flags.Flag_Room) {
                            if (items.get(position).getSenderId().equals(CurrentUserInfo.id)
                                    || items.get(position).getSenderId().equals(CurrentStore.id)) {
                                Glide.with(context)
                                        .load(key.child("image").getValue().toString())
                                        .into(holder.user_image2);
                            } else {
                                Glide.with(context)
                                        .load(key.child("image").getValue().toString())
                                        .into(holder.user_Image);
                            }
                        }else{
                            if (items.get(position).getSenderId().equals(CurrentUserInfo.id)) {
                                Glide.with(context)
                                        .load(key.child("image").getValue().toString())
                                        .into(holder.user_image2);
                            } else {
                                Glide.with(context)
                                        .load(key.child("image").getValue().toString())
                                        .into(holder.user_Image);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUserInfo(final viewitem holder, final String id, final int position) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(CurrentChatRoom.userId)){
                        if(key.getKey().equals(id)) {
                            if(Flags.Flag_Room) {
                                if (items.get(position).getSenderId().equals(CurrentUserInfo.id)
                                        || items.get(position).getSenderId().equals(CurrentStore.id)) {
                                    Glide.with(context)
                                            .load(key.child("image").getValue().toString())
                                            .into(holder.user_image2);
                                } else {
                                    Glide.with(context)
                                            .load(key.child("image").getValue().toString())
                                            .into(holder.user_Image);
                                }
                            }else{
                                if (items.get(position).getSenderId().equals(CurrentUserInfo.id)) {
                                    Glide.with(context)
                                            .load(key.child("image").getValue().toString())
                                            .into(holder.user_image2);
                                } else {
                                    Glide.with(context)
                                            .load(key.child("image").getValue().toString())
                                            .into(holder.user_Image);
                                }
                            }
                        }
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




    // private final View.OnClickListener mOnClickListener = new MyOnClickListener();



//    @Override
//    public void onClick(final View view) {
//        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//        String item = mList.get(itemPosition);
//        Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
//    }


}
