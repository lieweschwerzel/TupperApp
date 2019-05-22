package com.example.tupperapp.model;

    import android.widget.Filter;
    import java.util.ArrayList;
    import java.util.List;

    public class CustomFilter extends Filter{
        TupperMealAdapter adapter;
        List<TupperMeal> filterList;
        public CustomFilter(List<TupperMeal> filterList, TupperMealAdapter adapter)
        {
            this.adapter=adapter;
            this.filterList=filterList;
        }
        //FILTERING OCURS
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            //CHECK CONSTRAINT VALIDITY
            if(constraint != null && constraint.length() > 0)
            {
                //CHANGE TO UPPER
                constraint=constraint.toString().toUpperCase();
                //STORE OUR FILTERED PLAYERS
                ArrayList<TupperMeal> filteredMeals=new ArrayList<>();
                for (int i=0;i<filterList.size();i++)
                {
                    //CHECK
                    if(filterList.get(i).getTitle().toUpperCase().contains(constraint))
                    {
                        //ADD PLAYER TO FILTERED PLAYERS
                        filteredMeals.add(filterList.get(i));
                    }
                }
                results.count=filteredMeals.size();
                results.values=filteredMeals;
            }else
            {
                results.count=filterList.size();
                results.values=filterList;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            .addAll((List)results.values);
            //REFRESH
            adapter.notifyDataSetChanged();
        }
    }

