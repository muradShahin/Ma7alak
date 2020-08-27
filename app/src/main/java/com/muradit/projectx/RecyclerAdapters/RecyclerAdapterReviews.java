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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.Reviews;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentChatRoom;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentUserChat;
import com.muradit.projectx.Model.others.CurrentUserInfo;
import com.muradit.projectx.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterReviews extends    RecyclerView.Adapter<RecyclerAdapterReviews.viewitem> {



    List<Reviews> items;
    Context context;

    public RecyclerAdapterReviews(Context c, List<Reviews> item)
    {
        items=item;
        context=c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView name,date,review;
        private ImageView user_Image;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            user_Image=itemView.findViewById(R.id.user_image);
            review=itemView.findViewById(R.id.review);





        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_row, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        holder.name.setText(items.get(position).getUserName());
        holder.date.setText(items.get(position).getDate());
        holder.review.setText(items.get(position).getReview());
        
        getUserImage(holder , position);
        
        

    }

    private void getUserImage(final viewitem holder, final int position) {

        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(key.getKey().equals(items.get(position).getUserId())){
                        Glide.with(context).load(key.child("image").getValue().toString())
                                .into(holder.user_Image);
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
