package com.htdgym.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.htdgym.app.activities.PremiumActivity;
import com.htdgym.app.activities.SettingsActivity;
import com.htdgym.app.fragments.*;
import com.htdgym.app.utils.AdminManager;
import com.htdgym.app.utils.DefaultAccountManager;
import com.htdgym.app.utils.PremiumManager;
import com.htdgym.app.utils.ThumbnailUpdater;
import com.htdgym.app.utils.YouTubeHelper;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "HTDGymPrefs";
    
    private BottomNavigationView bottomNavigation;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            Log.d(TAG, "Starting MainActivity initialization");
            setContentView(R.layout.activity_main);
            
            initializeComponents();
            setupBottomNavigation();
            setDefaultFragment(savedInstanceState);
            initializeBackgroundTasks();
            
            Log.d(TAG, "MainActivity initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Critical error in MainActivity onCreate", e);
            handleCriticalError(e);
        }
    }

    private void initializeComponents() {
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        
        if (bottomNavigation == null) {
            throw new IllegalStateException("Bottom navigation not found in layout");
        }
        
        Log.d(TAG, "Components initialized successfully");
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            try {
                Fragment selectedFragment = createFragmentForMenuItem(item.getItemId());
                
                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                    Log.d(TAG, "Fragment replaced: " + selectedFragment.getClass().getSimpleName());
                    return true;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in navigation", e);
                showError("Lỗi điều hướng: " + e.getMessage());
            }
            return false;
        });
        
        Log.d(TAG, "Bottom navigation setup completed");
    }

    private Fragment createFragmentForMenuItem(int itemId) {
        if (itemId == R.id.nav_home) {
            return new HomeFragment();
        } else if (itemId == R.id.nav_workouts) {
            return new WorkoutsFragment();
        } else if (itemId == R.id.nav_nutrition) {
            return new NutritionFragment();
        } else if (itemId == R.id.nav_progress) {
            return new ProgressFragment();
        } else if (itemId == R.id.nav_community) {
            return new CommunityFragment();
        }
        return null;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit();
    }

    private void setDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            try {
                Log.d(TAG, "Setting default HomeFragment");
                replaceFragment(new HomeFragment());
                Log.d(TAG, "Default fragment set successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error setting default fragment", e);
                showError("Lỗi tải trang chủ: " + e.getMessage());
            }
        }
    }

    private void initializeBackgroundTasks() {
        // Print default accounts info to log
        DefaultAccountManager.printDefaultAccounts();
        
        // Check if default accounts exist
        executeInBackground(() -> {
            try {
                boolean accountsExist = DefaultAccountManager.checkDefaultAccountsExist(this);
                Log.d(TAG, "Default accounts exist: " + accountsExist);
                
                if (!accountsExist) {
                    Log.d(TAG, "Creating missing default accounts...");
                    DefaultAccountManager.forceCreateDefaultAccounts(this);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking default accounts", e);
            }
        });

        // Initialize admin account in background
        executeInBackground(() -> {
            try {
                AdminManager.recreateDefaultAdmin(this);
                Log.d(TAG, "Admin account initialized");
            } catch (Exception e) {
                Log.e(TAG, "Error creating default admin", e);
            }
        });

        // Check premium status in background
        executeInBackground(() -> {
            try {
                int userId = preferences.getInt("user_id", 0);
                if (userId > 0) {
                    PremiumManager.checkPremiumStatus(this, userId);
                    Log.d(TAG, "Premium status checked for user: " + userId);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error checking premium status", e);
            }
        });

        // Update missing thumbnails in background
        executeInBackground(() -> {
            try {
                // Debug log exercises first
                ThumbnailUpdater.debugLogExercises(this);
                
                // Auto-fix all missing thumbnails (NEW!)
                com.htdgym.app.utils.AutoThumbnailFixer.fixAllMissingThumbnails(this);
                
                // Download specific thumbnails for problematic exercises
                com.htdgym.app.utils.ThumbnailDownloader.downloadSpecificThumbnails(this);
                
                // Force update all thumbnails to ensure they are properly generated
                ThumbnailUpdater.forceUpdateAllThumbnails(this);
                Log.d(TAG, "Force thumbnail update process started");
                
                // Debug log again after update
                new Thread(() -> {
                    try {
                        Thread.sleep(3000); // Wait 3 seconds for updates to complete
                        ThumbnailUpdater.debugLogExercises(this);
                        
                        // Test thumbnail URL generation
                        testThumbnailGeneration();
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Debug thread interrupted", e);
                    }
                }).start();
                
            } catch (Exception e) {
                Log.e(TAG, "Error updating thumbnails", e);
            }
        });
    }

    private void executeInBackground(Runnable task) {
        new Thread(task).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        
        // Configure premium menu item visibility
        MenuItem premiumItem = menu.findItem(R.id.action_premium);
        if (premiumItem != null) {
            boolean isPremium = PremiumManager.isPremiumUser(this);
            premiumItem.setVisible(!isPremium);
        }
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        
        try {
            if (itemId == R.id.action_settings) {
                navigateToActivity(SettingsActivity.class);
                return true;
            } else if (itemId == R.id.action_premium) {
                navigateToActivity(PremiumActivity.class);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling menu item", e);
            showError("Lỗi điều hướng menu");
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    public void navigateToTab(int tabId) {
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(tabId);
        }
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void handleCriticalError(Exception e) {
        showError("Lỗi khởi tạo ứng dụng: " + e.getMessage());
        
        // Try to navigate back to login instead of crashing
        try {
            Intent intent = new Intent(this, com.htdgym.app.activities.LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            Log.e(TAG, "Failed to navigate to login", ex);
            finish();
        }
    }
    
    private void testThumbnailGeneration() {
        Log.d(TAG, "=== TESTING THUMBNAIL GENERATION ===");
        
        String[] testUrls = {
            "https://youtu.be/IODxDxX7oi4",
            "https://youtu.be/c4DAnQ6DtF8", 
            "https://youtu.be/1fbU_MkV7NE",
            "https://youtu.be/pSHjTRCQxIw",
            "https://youtu.be/JZQA08SlJnM",
            // Test the specific URLs user mentioned
            "https://youtu.be/20Khkl95_qA",  // Tabata
            "https://youtu.be/8opcQdC-V-U",  // Cardio
            "https://www.youtube.com/watch?v=20Khkl95_qA",  // Tabata (full format)
            "https://www.youtube.com/watch?v=8opcQdC-V-U"   // Cardio (full format)
        };
        
        for (String url : testUrls) {
            // Use the correct method name: extractVideoId instead of getVideoIdFromUrl
            String videoId = YouTubeHelper.extractVideoId(url);
            String thumbnailUrl = YouTubeHelper.getThumbnailUrl(url, YouTubeHelper.ThumbnailQuality.HIGH);
            
            Log.d(TAG, "URL: " + url);
            Log.d(TAG, "Video ID: " + videoId);
            Log.d(TAG, "Thumbnail: " + thumbnailUrl);
            Log.d(TAG, "---");
        }
        
        // Test direct thumbnail URLs
        Log.d(TAG, "=== TESTING DIRECT THUMBNAIL ACCESS ===");
        Log.d(TAG, "Cardio thumbnail: https://i.ytimg.com/vi/8opcQdC-V-U/hqdefault.jpg");
        Log.d(TAG, "Tabata thumbnail: https://i.ytimg.com/vi/20Khkl95_qA/hqdefault.jpg");
        
        Log.d(TAG, "=== END THUMBNAIL TEST ===");
    }
}