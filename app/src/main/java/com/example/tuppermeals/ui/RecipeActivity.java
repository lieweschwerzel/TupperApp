package com.example.tuppermeals.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.tuppermeals.R;
import com.example.tuppermeals.model.MainViewModel;
import com.example.tuppermeals.model.Recipe;
import com.example.tuppermeals.model.TupperMeal;

import java.util.List;

public class RecipeActivity extends AppCompatActivity {
    private List<Recipe> mRecipes;
    private MainViewModel mMainViewModel;
    String url;
    private Context mContext;

//    private TupperMealAdapter mAdapter;
//    private MainViewModel mMainViewModel;

    private TextView mTupperMealTitle;
    private TextView mTupperMealWeb;
    private ImageView mTupperMealImage;
    private Button mButton;

    public static final String EXTRA_BOOKMARK = "Bookmark";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mTupperMealImage = findViewById(R.id.imageView2);
        mTupperMealWeb = findViewById(R.id.recipeWebTV);

        mButton = findViewById(R.id.button);
                mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String website = mRecipes.get(0).getSourceUrl();
                Intent intent = new Intent(RecipeActivity.this, WebActivity.class);
                intent.putExtra(EXTRA_BOOKMARK, website);
                startActivity(intent);
            }
        });



//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mMainViewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {

                mRecipes = recipes;
//                Toast.makeText(getApplication(), mRecipes.get(0).getTitle(), Toast.LENGTH_LONG).show();
//                http:\/\/img.recipepuppy.com\/267093.jpg
                String url = mRecipes.get(0).getImageUrl();//.replace("\\", "");
//                String website = ;
//                Toast.makeText(RecipeActivity.this, website, Toast.LENGTH_SHORT).show();
//                updateUI();
                setTitle(mRecipes.get(0).getTitle());
//                mTupperMealTitle.setText(mRecipes.get(0).getTitle());
                mTupperMealWeb.setText(mRecipes.get(0).getSourceUrl());

                        Glide.with(getApplicationContext())
                        .load(url)
//                .image(R.drawable.loading)
                        .into(mTupperMealImage);
            }
        });




        Intent intent = getIntent();
        final TupperMeal tupperMeal = intent.getParcelableExtra(MainActivity.EXTRA_TUPPERMEAL);
        mMainViewModel.searchRecipes(tupperMeal.getTitle());
        setTitle(tupperMeal.getTitle() + " recipe");

//            mTupperMealTitle.setText(mMainViewModel.getAllRecipes().toString());
//            mTupperMealImage.setImageDrawable(tmpTupperMeal.getImageId());
        //Spinner instellen, krijg positie van coolingtype in de lijst met spinner elementen
//            String tmpstatus = tmpTupperMeal.getCoolingType();
//            mGameStatus.setSelection(getmGameStatusPos(tmpstatus));


    }
}
