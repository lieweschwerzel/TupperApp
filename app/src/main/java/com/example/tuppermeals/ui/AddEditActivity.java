package com.example.tuppermeals.ui;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.example.tuppermeals.R;
import com.example.tuppermeals.model.MainViewModel;
import com.example.tuppermeals.model.Recipe;
import com.example.tuppermeals.model.TupperMeal;
import com.example.tuppermeals.model.TupperMealAdapter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class AddEditActivity extends AppCompatActivity {
    //instance variables
    private List<TupperMeal> mTupperMeals;
//    private List<Recipe> mRecipes;

    private TupperMealAdapter mAdapter;
    private MainViewModel mMainViewModel;

    private EditText mTupperMealTitle;
    private EditText mTupperMealPlatform;
    private ImageView mTupperMealImage;
    private Spinner mCoolingType;
    private Button cameraButton, sensorButton;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;

    private String currentPhotoPath = null;
//    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit);
        //Initialize the instance variables
        mTupperMealTitle = findViewById(R.id.editTitle_addedit);
        mTupperMealPlatform = findViewById(R.id.editTitle_addedit);
        mTupperMealImage = findViewById(R.id.imageTupperMeal_addedit);
        mCoolingType = findViewById(R.id.editStatus_addedit);
        cameraButton = findViewById(R.id.cameraButton);
        sensorButton = findViewById(R.id.sensorButton);
        mTupperMeals = new ArrayList<>();

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
            Glide.with(this).load(tmpTupperMeal.getUrl()).into(mTupperMealImage);
            //Spinner instellen, krijg positie van coolingtype in de lijst met spinner elementen
            String tmpstatus = tmpTupperMeal.getCoolingType();
            mCoolingType.setSelection(getmGameStatusPos(tmpstatus));
        } else
            setTitle("Add your Meal");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        final MediaPlayer mp = MediaPlayer.create(AddEditActivity.this, R.raw.a);
        sensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Intent intent = new Intent(AddEditActivity.this, SensorActivity.class);
                startActivity(intent);
                CustomIntent.customType(AddEditActivity.this, "left-to-right");
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });

        FloatingActionButton savebutton = findViewById(R.id.fabsave);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String platform = mTupperMealPlatform.getText().toString();
                String title = mTupperMealTitle.getText().toString();
                int imageid = R.drawable.person;

                String coolingtype = mCoolingType.getSelectedItem().toString();
                String date = getDate();

                //Check if everything has been added
                if (!(TextUtils.isEmpty(title.trim())) && coolingtype != "Select a coolingtype...") {
                if (tmpTupperMeal != null) {
                    if (currentPhotoPath==null){
                        String url = tmpTupperMeal.getUrl();
                        TupperMeal editTupperMeal = new TupperMeal(title, imageid, coolingtype, date, url);
                        int id = tmpTupperMeal.getId();
                        editTupperMeal.setId(id);
                        mMainViewModel.update(editTupperMeal);
                    }else {
                        String url = currentPhotoPath;
                        TupperMeal editTupperMeal = new TupperMeal(title, imageid, coolingtype, date, url);
                        int id = tmpTupperMeal.getId();
                        editTupperMeal.setId(id);
                        mMainViewModel.update(editTupperMeal);
                    }
                    Intent intent = new Intent(AddEditActivity.this, MainActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(AddEditActivity.this, "fadein-to-fadeout");
                } else {
                    String url = currentPhotoPath;
                    TupperMeal newTupperMeal = new TupperMeal(title, imageid, coolingtype, date, url);
                    mMainViewModel.insert(newTupperMeal);
                    Intent intent = new Intent(AddEditActivity.this, MainActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(AddEditActivity.this, "fadein-to-fadeout");
                }
                } else
                    Snackbar.make(view, "Please type in a name for a meal and select a type of cooling", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void openCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "no file", Toast.LENGTH_SHORT).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.tuppermeals",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            //don't compare the data to null, it will always come as  null because we are providing a file URI, so load with the imageFilePath we obtained before opening the cameraIntent
            Glide.with(this).load(currentPhotoPath).into(mTupperMealImage);
        }
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {
        mCoolingType = (Spinner) findViewById(R.id.editStatus_addedit);
        List<String> list = new ArrayList<String>();
        list.add("Select a coolingtype...");
        list.add("Fridge");
        list.add("Freezer");
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
        mCoolingType.setAdapter(dataAdapter);

    }

    // get position of existing Status in spinner
    public int getmGameStatusPos(String status) {
        List<String> list = new ArrayList<>();
        list.add("Select a coolingtype...");
        list.add("Fridge");
        list.add("Freezer");
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
