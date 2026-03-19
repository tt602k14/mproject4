package com.htdgym.app.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Utility class for common activity operations
 */
public class ActivityHelper {
    
    private static final String TAG = "ActivityHelper";
    
    public static void showError(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
    public static void showLongError(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    
    public static void showSuccess(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
    public static void navigateToActivity(Context context, Class<?> activityClass) {
        try {
            Intent intent = new Intent(context, activityClass);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to activity: " + activityClass.getSimpleName(), e);
            showError(context, "Lỗi điều hướng");
        }
    }
    
    public static void navigateWithFlags(Context context, Class<?> activityClass, int flags) {
        try {
            Intent intent = new Intent(context, activityClass);
            intent.setFlags(flags);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error navigating with flags to: " + activityClass.getSimpleName(), e);
            showError(context, "Lỗi điều hướng");
        }
    }
    
    public static void executeInBackground(Runnable task) {
        new Thread(task).start();
    }
    
    public static void logError(String tag, String message, Exception e) {
        Log.e(tag, message, e);
    }
    
    public static void logDebug(String tag, String message) {
        Log.d(tag, message);
    }
}