package com.htdgym.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.htdgym.app.database.GymDatabase;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String PREFS_NAME = "DatabaseHelper";
    private static final String KEY_DB_VERSION = "db_version";
    
    public static void handleDatabaseError(Context context, Exception e) {
        Log.e(TAG, "Database error occurred", e);
        
        // Clear database instance to force recreation
        GymDatabase.resetInstance();
        
        // Clear shared preferences related to database
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        
        // Clear user session if database is corrupted
        SharedPreferences userPrefs = context.getSharedPreferences("HTDGymPrefs", Context.MODE_PRIVATE);
        userPrefs.edit()
            .putBoolean("is_logged_in", false)
            .putInt("user_id", 0)
            .apply();
    }
    
    public static boolean isDatabaseHealthy(Context context) {
        try {
            GymDatabase db = GymDatabase.getInstance(context);
            // Simple health check
            db.userDao().getUserCount();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Database health check failed", e);
            return false;
        }
    }
    
    public static void clearAppData(Context context) {
        try {
            // Clear database
            GymDatabase.resetInstance();
            
            // Clear all shared preferences
            SharedPreferences prefs = context.getSharedPreferences("HTDGymPrefs", Context.MODE_PRIVATE);
            prefs.edit().clear().apply();
            
            SharedPreferences adminPrefs = context.getSharedPreferences("admin_settings", Context.MODE_PRIVATE);
            adminPrefs.edit().clear().apply();
            
            Log.d(TAG, "App data cleared successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing app data", e);
        }
    }
    
    public static void forceDatabaseRecreation(Context context) {
        try {
            // Close and reset database instance
            GymDatabase.resetInstance();
            
            // Delete all database files
            String[] dbFiles = {"gym_database", "gym_database-shm", "gym_database-wal"};
            for (String dbFile : dbFiles) {
                try {
                    context.deleteDatabase(dbFile);
                    Log.d(TAG, "Deleted database file: " + dbFile);
                } catch (Exception e) {
                    Log.w(TAG, "Could not delete database file: " + dbFile, e);
                }
            }
            
            // Clear related preferences
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().clear().apply();
            
            // Clear user session
            SharedPreferences userPrefs = context.getSharedPreferences("HTDGymPrefs", Context.MODE_PRIVATE);
            userPrefs.edit()
                .putBoolean("is_logged_in", false)
                .putInt("user_id", 0)
                .apply();
            
            Log.d(TAG, "Database recreation forced successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error forcing database recreation", e);
        }
    }
    
    public static void fixMigrationError(Context context) {
        try {
            Log.d(TAG, "Attempting to fix migration error...");
            
            // Force database recreation
            forceDatabaseRecreation(context);
            
            // Try to initialize database again
            GymDatabase.getInstance(context);
            
            Log.d(TAG, "Migration error fixed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error fixing migration", e);
        }
    }
}