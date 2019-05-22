package com.example.tupperapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tupperapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class Home_screen extends AppCompatActivity {
    private TextView mName;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);

        mName = findViewById(R.id.textView);


        Button button = (Button) findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();
        String text = mAuth.getCurrentUser().getDisplayName();
        String id = mAuth.getCurrentUser().getUid();

        mName.setText(text + " " + id);

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();



        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(Home_screen.this, Login_activity.class));
                }
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();


            }
        });


    }
}
