

package com.example.tupperapp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.tupperapp.R;

import java.util.ArrayList;
import java.util.List;


public class TupperMealAdapter extends RecyclerView.Adapter<TupperMealAdapter.ViewHolder> implements Filterable {
    final private OnItemClickListener mItemClickListener;
    private Context mContext;
    public List<TupperMeal> mTupperMeals, filterList;
    public List<Recipe> mRecipes;

    CustomFilter filter;


    public TupperMealAdapter(Context mContext, List<TupperMeal> mTupperMeals, List<Recipe> mRecipes,  OnItemClickListener mItemClickListener) {
        this.mContext = mContext;
        this.mTupperMeals = mTupperMeals;
        this.mRecipes = mRecipes;
        this.mItemClickListener = mItemClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cardview, parent, false);
        // Return a new holder instance
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TupperMeal tupperMeal = mTupperMeals.get(i);
//        Recipe recipe = mRecipes.get(i);
        viewHolder.titleView.setText(tupperMeal.getTitle());
//        viewHolder.platformView.setText(tupperMeal.getTitle());
        viewHolder.imageView.setImageResource(tupperMeal.getImageId());
        viewHolder.statusView.setText(tupperMeal.getStatus());
        viewHolder.dateView.setText(tupperMeal.getDate());

        String poster =  "https:\\/\\/www.themealdb.com\\/images\\/media\\/meals\\/ustsqw1468250014.jpg";
//        poster.replace("\\", "");
        System.out.println();

        Glide.with(mContext)
                .load(poster.replace("\\", ""))
//                .placeholder(R.drawable.loading)
                .into(viewHolder.imageView);
    }

    public TupperMeal getMealAt(int position) {
        return mTupperMeals.get(position);
    }

    @Override
    public int getItemCount() {
        return mTupperMeals.size();
    }

    public void swapList(List<TupperMeal> newList) {
        mTupperMeals = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView;
        TextView platformView;
        ImageView imageView;
        TextView statusView;
        TextView dateView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.text_view_title);
            platformView = itemView.findViewById(R.id.editPlatform_addedit);
//            final ImageView imageView = (ImageView)imageView.findViewById(R.id.imageView_image);
            imageView = itemView.findViewById(R.id.imageView_image);
            statusView = itemView.findViewById(R.id.text_view_status);
            dateView = itemView.findViewById(R.id.text_view_datum);
            imageView.setOnClickListener(this);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mItemClickListener.onItemClick(position);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }
        return filter;


    }
}
