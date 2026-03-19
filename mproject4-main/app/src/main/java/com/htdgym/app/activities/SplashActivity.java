package com.htdgym.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.MainActivity;
import com.htdgym.app.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Delay and navigate
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            navigateToNextScreen();
        }, SPLASH_DELAY);
    }

    private void navigateToNextScreen() {
        SharedPreferences prefs = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);
        
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        Intent intent;

        if (isLoggedIn) {
            // Already logged in - go to main app
            intent = new Intent(this, MainActivity.class);
        } else {
            // Not logged in - go to login screen
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
