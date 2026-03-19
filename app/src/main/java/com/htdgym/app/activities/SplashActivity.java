package com.htdgym.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.MainActivity;
import com.htdgym.app.R;
import com.htdgym.app.utils.DatabaseHelper;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            Log.d(TAG, "onCreate: Starting SplashActivity");
            setContentView(R.layout.activity_splash);
            Log.d(TAG, "onCreate: Layout set successfully");

            // Hide action bar
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            // Check database health first
            checkDatabaseHealth();

            // Delay and navigate
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                try {
                    navigateToNextScreen();
                } catch (Exception e) {
                    Log.e(TAG, "Error during navigation", e);
                    handleError(e);
                }
            }, SPLASH_DELAY);
            
            Log.d(TAG, "onCreate: SplashActivity initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Critical error in SplashActivity", e);
            handleError(e);
        }
    }

    private void checkDatabaseHealth() {
        new Thread(() -> {
            try {
                if (!DatabaseHelper.isDatabaseHealthy(this)) {
                    Log.w(TAG, "Database health check failed, clearing data");
                    DatabaseHelper.clearAppData(this);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking database health", e);
                DatabaseHelper.handleDatabaseError(this, e);
            }
        }).start();
    }

    private void navigateToNextScreen() {
        try {
            Log.d(TAG, "navigateToNextScreen: Checking login status");
            SharedPreferences prefs = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);
            
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
            Log.d(TAG, "navigateToNextScreen: User logged in: " + isLoggedIn);

            Intent intent;

            if (isLoggedIn) {
                // Already logged in - go to main app
                Log.d(TAG, "navigateToNextScreen: Navigating to MainActivity");
                intent = new Intent(this, MainActivity.class);
            } else {
                // Not logged in - go to login screen
                Log.d(TAG, "navigateToNextScreen: Navigating to LoginActivity");
                intent = new Intent(this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
            Log.d(TAG, "navigateToNextScreen: Navigation completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "navigateToNextScreen: Error during navigation", e);
            handleError(e);
        }
    }

    private void handleError(Exception e) {
        Toast.makeText(this, "Lỗi khởi động: " + e.getMessage(), Toast.LENGTH_LONG).show();
        
        // Try to go to login as fallback
        try {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } catch (Exception ex) {
            Log.e(TAG, "Failed to navigate to login", ex);
        }
        finish();
    }
}
