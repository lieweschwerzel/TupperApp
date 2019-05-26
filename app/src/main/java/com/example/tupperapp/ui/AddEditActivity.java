package com.example.tupperapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tupperapp.R;
import com.example.tupperapp.model.MainViewModel;
import com.example.tupperapp.model.Recipe;
import com.example.tupperapp.model.TupperMeal;
import com.example.tupperapp.model.TupperMealAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    String url;

    private TupperMealAdapter mAdapter;
    private MainViewModel mMainViewModel;

    private EditText mTupperMealTitle;
    private EditText mTupperMealPlatform;
    private ImageView mTupperMealImage;
    private Spinner mGameStatus;
    private Button cameraButton;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit);
        //Initialize the instance variables
        mTupperMealTitle = findViewById(R.id.editTitle_addedit);
        mTupperMealPlatform = findViewById(R.id.editTitle_addedit);
        mTupperMealImage = findViewById(R.id.imageTupperMeal_addedit);
        mGameStatus = findViewById(R.id.editStatus_addedit);
        cameraButton = findViewById(R.id.cameraButton);
        mTupperMeals = new ArrayList<>();

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mMainViewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                mRecipes = recipes;
                String url = mRecipes.get(0).getThumbnail().replace("\\", "");
                Toast.makeText(AddEditActivity.this, url, Toast.LENGTH_LONG).show();
                System.out.println(url);

                String poster =  "https://cdn0.wideopencountry.com/wp-content/uploads/2018/07/country-songs-about-rain-793x526.jpg";

                Glide.with(AddEditActivity.this)
                        .load(poster)
                        .into(mTupperMealImage);
            }
        });

        addItemsOnSpinner();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final TupperMeal tmpTupperMeal = intent.getParcelableExtra(MainActivity.EXTRA_TUPPERMEAL);
        if (tmpTupperMeal != null) {
            setTitle("Edit this Meal");
            mTupperMealTitle.setText(tmpTupperMeal.getTitle());
            mTupperMealImage.setImageResource(tmpTupperMeal.getImageId());
//            mTupperMealTitle.setText(mMainViewModel.getAllRecipes().toString());
//            mTupperMealImage.setImageDrawable(tmpTupperMeal.getImageId());
            //Spinner instellen, krijg positie van status in de lijst met spinner elementen
            String tmpstatus = tmpTupperMeal.getStatus();
            mGameStatus.setSelection(getmGameStatusPos(tmpstatus));
        } else
            setTitle("New Meal");

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        FloatingActionButton savebutton = findViewById(R.id.fabsave);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String platform = mTupperMealPlatform.getText().toString();
                String title = mTupperMealTitle.getText().toString();
                int imageid = R.drawable.person;
                String status = mGameStatus.getSelectedItem().toString();
                String date = getDate();

                //Check if everything has been added
//                if (!(TextUtils.isEmpty(title.trim())) && status != "Select a status...") {
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
//                } else
//                    Snackbar.make(view, "Please insert a title, platform and select a status", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        mTupperMealImage.setImageBitmap(bitmap);
        // Get the data from an ImageView as bytes
//        mTupperMealImage.setDrawingCacheEnabled(true);
//        mTupperMealImage.buildDrawingCache();
//        Bitmap bitmap = ((BitmapDrawable) mTupperMealImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data2 = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data2);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }



}
