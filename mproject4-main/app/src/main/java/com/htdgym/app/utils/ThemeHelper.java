package com.htdgym.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME_MODE = "theme_mode";
    
    // Theme modes
    public static final int MODE_LIGHT = 0;
    public static final int MODE_DARK = 1;
    public static final int MODE_AUTO = 2;
    
    /**
     * Apply saved theme
     */
    public static void applyTheme(Context context) {
        int mode = getThemeMode(context);
        applyTheme(mode);
    }
    
    /**
     * Apply theme by mode
     */
    public static void applyTheme(int mode) {
        switch (mode) {
            case MODE_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case MODE_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case MODE_AUTO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
    
    /**
     * Save theme mode
     */
    public static void saveThemeMode(Context context, int mode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_THEME_MODE, mode).apply();
        applyTheme(mode);
    }
    
    /**
     * Get saved theme mode
     */
    public static int getThemeMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME_MODE, MODE_AUTO);
    }
    
    /**
     * Check if dark mode is active
     */
    public static boolean isDarkMode(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode 
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES;
    }
    
    /**
     * Toggle between light and dark mode
     */
    public static void toggleTheme(Context context) {
        int currentMode = getThemeMode(context);
        int newMode = (currentMode == MODE_LIGHT) ? MODE_DARK : MODE_LIGHT;
        saveThemeMode(context, newMode);
    }
    
    /**
     * Get theme mode name
     */
    public static String getThemeModeName(Context context, int mode) {
        switch (mode) {
            case MODE_LIGHT:
                return "Sáng";
            case MODE_DARK:
                return "Tối";
            case MODE_AUTO:
                return "Tự động";
            default:
                return "Không xác định";
        }
    }
}
