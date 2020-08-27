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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muradit.projectx.Model.AddItemModel;
import com.muradit.projectx.Model.StoreModel;
import com.muradit.projectx.Model.others.Category;
import com.muradit.projectx.Model.others.CurrentItem;
import com.muradit.projectx.R;
import com.muradit.projectx.View.allItems.AllItemsFragment;
import com.muradit.projectx.ViewModel.allItems.allItemsViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterCategory extends    RecyclerView.Adapter<RecyclerAdapterCategory.viewitem> {



    List<Category> items;
    Context context;
    RecyclerView recyclerViewAllItems;
    String styleType;

    public RecyclerAdapterCategory(Context c, List<Category> item ,RecyclerView recyclerView,String style)
    {
        items=item;
        context=c;
        recyclerViewAllItems=recyclerView;
        styleType=style;

    }

    //The View Item part responsible for connecting the row.xml with
    // each item in the RecyclerView
    //make declare and initalize
    class  viewitem extends RecyclerView.ViewHolder
    {


        private TextView name;
        private CardView itemCard;



        //initialize
        public viewitem(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.category_name);
            itemCard=itemView.findViewById(R.id.catItem_card);





        }
    }



    //onCreateViewHolder used to HAndle on Clicks
    @Override
    public viewitem onCreateViewHolder(final ViewGroup parent, int viewType) {



        //the itemView now is the row
        //We will add 2 onClicks


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);





        return new viewitem(itemView);
    }


    //to fill each item with data from the array depending on position
    @Override
    public void onBindViewHolder(final viewitem holder, final int position) {
        holder.name.setText(items.get(position).getName());


        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               allItemsViewModel viewModel=new allItemsViewModel(context);
                Category.Flag=true;
                viewModel.getCategoryItems(items.get(position).getName()).observe((LifecycleOwner) context, new Observer<List<AddItemModel>>() {
                    @Override
                    public void onChanged(List<AddItemModel> addItemModels) {
                        RecyclerAdapterItemsFull recyclerAdapterItemsFull = new RecyclerAdapterItemsFull(context, addItemModels,styleType);
                        recyclerAdapterItemsFull.notifyDataSetChanged();
                        recyclerViewAllItems.setAdapter(recyclerAdapterItemsFull);

                    }
                });

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