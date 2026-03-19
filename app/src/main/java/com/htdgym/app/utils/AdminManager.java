package com.htdgym.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.Admin;

public class AdminManager {
    
    private static final String PREFS_NAME = "HTDGymAdminPrefs";
    private static final String KEY_ADMIN_ID = "adminId";
    private static final String KEY_ADMIN_EMAIL = "adminEmail";
    private static final String KEY_ADMIN_ROLE = "adminRole";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    
    public static boolean isAdminLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public static void loginAdmin(Context context, Admin admin) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putInt(KEY_ADMIN_ID, admin.getId())
            .putString(KEY_ADMIN_EMAIL, admin.getEmail())
            .putString(KEY_ADMIN_ROLE, admin.getRole())
            .apply();
            
        // Update last login time
        new Thread(() -> {
            GymDatabase database = GymDatabase.getInstance(context);
            database.adminDao().updateLastLogin(admin.getId(), System.currentTimeMillis());
        }).start();
    }
    
    public static void logoutAdmin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
    
    public static int getCurrentAdminId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_ADMIN_ID, -1);
    }
    
    public static String getCurrentAdminEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_ADMIN_EMAIL, "");
    }
    
    public static String getCurrentAdminRole(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_ADMIN_ROLE, "");
    }
    
    public static boolean isSuperAdmin(Context context) {
        return "super_admin".equals(getCurrentAdminRole(context));
    }
    
    public static boolean isAdmin(Context context) {
        String role = getCurrentAdminRole(context);
        return "super_admin".equals(role) || "admin".equals(role);
    }
    
    public static void createDefaultAdmin(Context context) {
        new Thread(() -> {
            GymDatabase database = GymDatabase.getInstance(context);
            
            // Check if any admin exists
            if (database.adminDao().getActiveAdminCount() == 0) {
                // Hash the default password
                String hashedPassword = hashPassword("admin123");
                
                // Create default super admin
                Admin defaultAdmin = new Admin(
                    "admin@htdgym.com", 
                    hashedPassword, 
                    "Super Administrator", 
                    "super_admin"
                );
                database.adminDao().insertAdmin(defaultAdmin);
            }
        }).start();
    }
    
    public static void recreateDefaultAdmin(Context context) {
        new Thread(() -> {
            GymDatabase database = GymDatabase.getInstance(context);
            
            // Check if admin with email exists
            Admin existingAdmin = database.adminDao().getAdminByEmail("admin@htdgym.com");
            if (existingAdmin != null) {
                // Delete existing admin
                database.adminDao().deleteAdmin(existingAdmin);
            }
            
            // Hash the default password
            String hashedPassword = hashPassword("admin123");
            
            // Create default super admin with hashed password
            Admin defaultAdmin = new Admin(
                "admin@htdgym.com", 
                hashedPassword, 
                "Super Administrator", 
                "super_admin"
            );
            database.adminDao().insertAdmin(defaultAdmin);
        }).start();
    }
    
    private static String hashPassword(String password) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            return password; // Fallback (not recommended for production)
        }
    }
}