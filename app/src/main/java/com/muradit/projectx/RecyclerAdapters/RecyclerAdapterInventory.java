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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterInventory extends    RecyclerView.Adapter<RecyclerAdapterInventory.viewitem> {



    List<AddItemModel> items;
    Context context;

    public RecyclerAdapterInventory(Context c, List<AddItemModel> item)
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
        private Button sold,more;
        private ImageView itemImage;

        //initialize
        public viewitem(View itemView) {
            super(itemView);
            itemImage=itemView.findViewById(R.id.itemImg);
            sold=itemView.findViewById(R.id.soldBtn);
            more=itemView.findViewById(R.id.MoreBtn);
            name=itemView.findViewById(R.id.name);





        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        holder.name.setText(items.get(position).getItem_name());
        Glide.with(context).load(items.get(position).getItem_image()).into(holder.itemImage);

        if(items.get(position).getItem_state().equals("available"))
            holder.sold.setText("Sold");
        else
            holder.sold.setText("Available");



        holder.sold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_state=items.get(position).getItem_state();

                if(item_state.equals("available"))
                    item_state="sold";
                else
                    item_state="available";

                changeItemStatus(items.get(position).getItem_id(),item_state);
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentItem.item_name=items.get(position).getItem_name();
                CurrentItem.item_category=items.get(position).getItem_category();
                CurrentItem.item_desc=items.get(position).getItem_desc();
                CurrentItem.item_id=items.get(position).getItem_id();
                CurrentItem.item_image=items.get(position).getItem_image();
                CurrentItem.item_price=items.get(position).getItem_price();
                CurrentItem.item_state=items.get(position).getItem_state();
                CurrentItem.store_email=items.get(position).getStore_email();
                CurrentItem.item_store=items.get(position).getItem_store();

                Navigation.findNavController(v).navigate(R.id.action_inventoryFragment_to_moreFragment);
            }
        });




    }

    private void changeItemStatus(String item_id, String item_state) {
       items.clear();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Items");

        databaseReference.child(item_id).child("item_state").setValue(item_state)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

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
