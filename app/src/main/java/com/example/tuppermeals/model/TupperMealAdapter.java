

package com.example.tuppermeals.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tuppermeals.R;
import com.example.tuppermeals.ui.MainActivity;
import com.example.tuppermeals.ui.RecipeActivity;

import java.util.List;

//import com.bumptech.glide.Glide;


public class TupperMealAdapter extends RecyclerView.Adapter<TupperMealAdapter.ViewHolder> implements Filterable {
    final private OnItemClickListener mItemClickListener;
    private Context mContext;
    public List<TupperMeal> mTupperMeals, filterList;
    CustomFilter filter;

    public TupperMealAdapter(Context mContext, List<TupperMeal> mTupperMeals, OnItemClickListener mItemClickListener) {
        this.mContext = mContext;
        this.mTupperMeals = mTupperMeals;
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
        viewHolder.titleView.setText(tupperMeal.getTitle());
        viewHolder.imageRecipeLogoView.setImageResource(R.drawable.recipelogo);
        viewHolder.refrigeratorType.setText(tupperMeal.getCoolingType());
        viewHolder.dateView.setText("Expires on: "+tupperMeal.getDate());

        String url = tupperMeal.getUrl();
        Glide.with(mContext).load(url).into(viewHolder.imageView);
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
        ImageView imageView;
        ImageView imageRecipeLogoView;
        TextView refrigeratorType;
        TextView dateView;

        public ViewHolder(final View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.text_view_title);
            imageView = itemView.findViewById(R.id.imageView_image);
            imageRecipeLogoView = itemView.findViewById(R.id.imageView_image3);
            refrigeratorType = itemView.findViewById(R.id.refrigerator_type_spinner);
            dateView = itemView.findViewById(R.id.text_view_datum);

//            imageRecipeLogoView.setOnClickListener(this);
            imageRecipeLogoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    TupperMeal tupperMeal = mTupperMeals.get(position);
                    Intent intent = new Intent(v.getContext() , RecipeActivity.class);
                    intent.putExtra(MainActivity.EXTRA_TUPPERMEAL, tupperMeal);
                    mContext.startActivity(intent);
                }
            });
            itemView.setOnClickListener(this);
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
