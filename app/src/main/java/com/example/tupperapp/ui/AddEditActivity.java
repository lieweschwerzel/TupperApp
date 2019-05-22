package com.example.tupperapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tupperapp.R;
import com.example.tupperapp.model.Recipe;
import com.example.tupperapp.model.TupperMeal;
import com.example.tupperapp.model.TupperMealAdapter;
import com.example.tupperapp.model.MainViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEditActivity extends AppCompatActivity {
    //instance variables
    private List<TupperMeal> mTupperMeals;
    private List<Recipe> mRecipes;

    private TupperMealAdapter mAdapter;
    private MainViewModel mMainViewModel;

    private EditText mTupperMealTitle;
    private ImageView mTupperMealImage;
    private Spinner mGameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit);
        //Initialize the instance variables
        mTupperMealTitle = findViewById(R.id.editTitle_addedit);
        mTupperMealImage = findViewById(R.id.imageView_image);
        mGameStatus = findViewById(R.id.editStatus_addedit);
        mTupperMeals = new ArrayList<>();

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getAllRecipes();


        addItemsOnSpinner();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final TupperMeal tmpTupperMeal = intent.getParcelableExtra(MainActivity.EXTRA_TUPPERMEAL);
        if (tmpTupperMeal != null) {
            setTitle("Edit this Meal");
            mTupperMealTitle.setText(tmpTupperMeal.getTitle());
//            mTupperMealTitle.setText(mMainViewModel.getAllRecipes().toString());
//            mTupperMealImage.setImageDrawable(tmpTupperMeal.getImageId());
            //Spinner instellen, krijg positie van status in de lijst met spinner elementen
            String tmpstatus = tmpTupperMeal.getStatus();
            mGameStatus.setSelection(getmGameStatusPos(tmpstatus));
        } else
            setTitle("New Meal");



        FloatingActionButton savebutton = findViewById(R.id.fabsave);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTupperMealTitle.getText().toString();
                int imageid = R.drawable.images;
                String status = mGameStatus.getSelectedItem().toString();
                String date = getDate();
                String url = mMainViewModel.searchRecipes(title);

                //Check if everything has been added
                if (!(TextUtils.isEmpty(title.trim())) && status != "Select a status...") {
                    if (tmpTupperMeal != null) {
                        TupperMeal editTupperMeal = new TupperMeal(title, imageid, status, date, url);
                        int id = tmpTupperMeal.getId();
                        editTupperMeal.setId(id);
                        mMainViewModel.update(editTupperMeal);
                        Intent intent = new Intent(AddEditActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        TupperMeal newTupperMeal = new TupperMeal(title, imageid, status, date, url);
                        mMainViewModel.insert(newTupperMeal);
                        Intent intent = new Intent(AddEditActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else
                    Snackbar.make(view, "Please insert a title, platform and select a status", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {
        mGameStatus = (Spinner) findViewById(R.id.editStatus_addedit);
        List<String> list = new ArrayList<String>();
        list.add("Select a status...");
        list.add("Want to Play");
        list.add("Playing");
        list.add("Stalled");
        list.add("Dropped");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list) {
            //grijs maken van de voorselectie op de spinner nadat erop is geklikt
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGameStatus.setAdapter(dataAdapter);

    }

    // get position of existing Status in spinner
    public int getmGameStatusPos(String status) {
        List<String> list = new ArrayList<>();
        list.add("Select a status...");
        list.add("Want to Play");
        list.add("Playing");
        list.add("Stalled");
        list.add("Dropped");
        return list.indexOf(status);
    }

    //get date in correct format
    public String getDate() {
        Calendar cal = Calendar.getInstance();
        Date tempdate = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(tempdate);
        return date;
    }

}
