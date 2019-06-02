
package com.example.tuppermeals.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tuppermeals.R;
import com.example.tuppermeals.ui.MainActivity;
import com.example.tuppermeals.ui.RecipeActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TupperMealAdapter extends RecyclerView.Adapter<TupperMealAdapter.ViewHolder> {
    final private OnItemClickListener mItemClickListener;
    private Context mContext;
    public List<TupperMeal> mTupperMeals;

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



        String expiry = convertDate(tupperMeal.getDate(), tupperMeal.getCoolingType());
        viewHolder.dateView.setText("Expires on: " + expiry);

        String url = tupperMeal.getUrl();
        Glide.with(mContext).load(url).into(viewHolder.imageView);
    }

    private String convertDate(String date, String coolingType) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(mContext);
        String fridge = SP.getString("fridge", "3");
        String freezer = SP.getString("freezer", "30");
        String dt = date;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (coolingType.equals("Fridge")) {
            c.add(Calendar.DATE, Integer.valueOf(fridge));  // number of days to add
            dt = sdf.format(c.getTime());  // dt is now the new date
        } else {
            System.out.println("HIER IS OBJECT met " + coolingType);
            c.add(Calendar.DATE, Integer.valueOf(freezer));  // number of days to add
            dt = sdf.format(c.getTime());  // dt is now the new date
        }
//        Calendar b = Calendar.getInstance();

        return dt;
    }

//    public static long getDifferenceDays(Date d1, Date d2) {
//        long diff = d2.getTime() - d1.getTime();
//        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//    }

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
}
