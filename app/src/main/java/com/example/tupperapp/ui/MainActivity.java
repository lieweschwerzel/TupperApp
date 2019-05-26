package com.example.tupperapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tupperapp.R;
import com.example.tupperapp.model.MainViewModel;
import com.example.tupperapp.model.TupperMeal;
import com.example.tupperapp.model.TupperMealAdapter;

import java.util.List;


public class MainActivity extends AppCompatActivity implements TupperMealAdapter.OnItemClickListener, SearchView.OnQueryTextListener {
    //instance variables
    private List<TupperMeal> mTupperMeals;
    //    private List<Recipe_old> mRecipes;
    private TupperMealAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MainViewModel mMainViewModel;

    public static final String EXTRA_TUPPERMEAL = "Tuppermeal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize the instance variables
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mMainViewModel.getmTupperMeals().observe(this, new Observer<List<TupperMeal>>() {
            @Override
            public void onChanged(@Nullable List<TupperMeal> tupperMeals) {
                mTupperMeals = tupperMeals;
                updateUI();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent,0);
//                }
//            });
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                startActivity(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                // hold position in view item
                final int pos = viewHolder.getAdapterPosition();
                View parentLayout = findViewById(android.R.id.content);
                final TupperMeal tmpDeletedTupperMeal = mTupperMeals.get(pos);

                mMainViewModel.delete(mAdapter.getMealAt(pos));
                Snackbar.make(parentLayout, "Deleted: " + tmpDeletedTupperMeal.getTitle(), Snackbar.LENGTH_LONG)
                        .setActionTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark))
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                            }
                        })
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMainViewModel.insert(tmpDeletedTupperMeal);
                            }
                        })
                        .show();
                mAdapter.notifyItemRangeChanged(pos, mAdapter.getItemCount());

            }
        }).attachToRecyclerView(mRecyclerView);
//        mMainViewModel.searchRecipes("pizza");
    }

    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new TupperMealAdapter(this, mTupperMeals, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mTupperMeals);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            //deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        TupperMeal tupperMeal = mTupperMeals.get(position);
//        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
//        startActivity(intent);
//        Toast.makeText(this, tupperMeal.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        intent.putExtra(MainActivity.EXTRA_TUPPERMEAL, tupperMeal);
        startActivity(intent);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//        mTupperMeals.get(0).setImageId(bitmap);
//        mAdapter.
//        imageView.setImageBitmap(bitmap);
    }
}
