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
import com.muradit.projectx.Model.Follow;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.Model.others.CurrentStore;
import com.muradit.projectx.R;
import com.muradit.projectx.ViewModel.profileViewModel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterFollowedStores extends    RecyclerView.Adapter<RecyclerAdapterFollowedStores.viewitem> {



    List<Follow> items;
    Context context;
    private List<StoreModel> storesInfo;
    private ProfileViewModel viewModel;

    public RecyclerAdapterFollowedStores(Context c, List<Follow> item)
    {
        items=item;
        context=c;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView name;
        private ImageView storeImage;
        private Button unFollow;
        private CardView cardImage;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.storeName);
            storeImage=itemView.findViewById(R.id.storeImage);
            unFollow=itemView.findViewById(R.id.unFollowBtn);
            cardImage=itemView.findViewById(R.id.cardImg);
            viewModel=new ProfileViewModel(context);
            storesInfo=new ArrayList<>();





        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_followed, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {

        getStoreInfo(holder,position,items.get(position).getStoreId());

        holder.unFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.unFollowStore(items.get(position).getRelKey());
            }
        });

        holder.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentStore.name=storesInfo.get(position).getName();
                CurrentStore.email=storesInfo.get(position).getEmail();
                CurrentStore.city=storesInfo.get(position).getCity();
                CurrentStore.id=storesInfo.get(position).getId();
                CurrentStore.lat=storesInfo.get(position).getLat();
                CurrentStore.lng=storesInfo.get(position).getLng();
                CurrentStore.image=storesInfo.get(position).getImage();
                CurrentStore.ownerEmail=storesInfo.get(position).getOwnerEmail();
                CurrentStore.ownerName=storesInfo.get(position).getOwnerName();
                CurrentStore.phone=storesInfo.get(position).getPhone();
                CurrentStore.about=storesInfo.get(position).getAbout();
                Navigation.findNavController(v).navigate(R.id.action_nav_gallery_to_insideStoreFragment);
            }
        });
    }

    private void getStoreInfo(final viewitem holder, final int position, final String Store_Id){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Stores");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot key:dataSnapshot.getChildren()){
                    if(Store_Id.equals(key.getKey())){

                        StoreModel storeModel=new StoreModel(
                                key.child("image").getValue().toString(),
                                key.child("name").getValue().toString(),
                                key.child("email").getValue().toString(),
                                key.child("phone").getValue().toString(),
                                key.child("city").getValue().toString(),
                                key.child("ownerName").getValue().toString(),
                                key.child("ownerEmail").getValue().toString(),
                                key.child("lat").getValue().toString(),
                                key.child("lng").getValue().toString(),
                                key.child("category").getValue().toString(),
                                key.child("about").getValue().toString());
                        storeModel.setId(key.getKey());

                        storesInfo.add(position,storeModel);


                        //Init component
                        Glide.with(context).load(key.child("image").getValue().toString()).into(holder.storeImage);
                        holder.name.setText(key.child("name").getValue().toString());



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
