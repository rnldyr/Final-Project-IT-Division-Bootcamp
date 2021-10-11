package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.kepo.R;

public class SplashScreenActivity extends AppCompatActivity {

    private int splashTime = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 1.5 seconds
                    sleep(splashTime);

                    // After 1.5 seconds redirect to another intent
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);

                    //Remove activity
                    finish();
                } catch (Exception e) {

                }
            }
        };
        // Start thread
        background.start();
    }
}