package com.htdgym.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.User;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountManager {
    private static final String TAG = "AccountManager";
    private static final String PREF_NAME = "saved_accounts";
    private static final String KEY_SAVED_ACCOUNTS = "saved_accounts_list";
    private static final String KEY_LAST_LOGIN_EMAIL = "last_login_email";
    private static final String KEY_AUTO_LOGIN_ENABLED = "auto_login_enabled";
    private static final String KEY_REMEMBER_ACCOUNTS = "remember_accounts";
    
    private static AccountManager instance;
    private Context context;
    private SharedPreferences prefs;
    private GymDatabase database;
    private ExecutorService executor;
    private Gson gson;
    
    private AccountManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.database = GymDatabase.getInstance(context);
        this.executor = Executors.newSingleThreadExecutor();
        this.gson = new Gson();
    }
    
    public static synchronized AccountManager getInstance(Context context) {
        if (instance == null) {
            instance = new AccountManager(context);
        }
        return instance;
    }
    
    // Model class for saved account
    public static class SavedAccount {
        public String email;
        public String name;
        public String profileImage;
        public long lastLoginTime;
        public boolean isRemembered;
        
        public SavedAccount() {}
        
        public SavedAccount(String email, String name, String profileImage) {
            this.email = email;
            this.name = name;
            this.profileImage = profileImage;
            this.lastLoginTime = System.currentTimeMillis();
            this.isRemembered = true;
        }
    }
    
    // Save account after successful login
    public void saveAccount(User user) {
        try {
            if (!isRememberAccountsEnabled()) {
                Log.d(TAG, "Remember accounts is disabled");
                return;
            }
            
            List<SavedAccount> savedAccounts = getSavedAccounts();
            
            // Remove existing account with same email
            savedAccounts.removeIf(account -> account.email.equals(user.getEmail()));
            
            // Add new account at the beginning
            SavedAccount newAccount = new SavedAccount(
                user.getEmail(),
                user.getName() != null ? user.getName() : "",
                user.getProfileImage()
            );
            savedAccounts.add(0, newAccount);
            
            // Keep only last 5 accounts
            if (savedAccounts.size() > 5) {
                savedAccounts = savedAccounts.subList(0, 5);
            }
            
            // Save to preferences
            String json = gson.toJson(savedAccounts);
            prefs.edit()
                .putString(KEY_SAVED_ACCOUNTS, json)
                .putString(KEY_LAST_LOGIN_EMAIL, user.getEmail())
                .apply();
                
            Log.d(TAG, "Account saved: " + user.getEmail());
            
        } catch (Exception e) {
            Log.e(TAG, "Error saving account", e);
        }
    }
    
    // Get list of saved accounts
    public List<SavedAccount> getSavedAccounts() {
        try {
            String json = prefs.getString(KEY_SAVED_ACCOUNTS, "[]");
            Type listType = new TypeToken<List<SavedAccount>>(){}.getType();
            List<SavedAccount> accounts = gson.fromJson(json, listType);
            return accounts != null ? accounts : new ArrayList<>();
        } catch (Exception e) {
            Log.e(TAG, "Error getting saved accounts", e);
            return new ArrayList<>();
        }
    }
    
    // Get last login email
    public String getLastLoginEmail() {
        return prefs.getString(KEY_LAST_LOGIN_EMAIL, "");
    }
    
    // Remove saved account
    public void removeSavedAccount(String email) {
        try {
            List<SavedAccount> savedAccounts = getSavedAccounts();
            savedAccounts.removeIf(account -> account.email.equals(email));
            
            String json = gson.toJson(savedAccounts);
            prefs.edit().putString(KEY_SAVED_ACCOUNTS, json).apply();
            
            Log.d(TAG, "Account removed: " + email);
        } catch (Exception e) {
            Log.e(TAG, "Error removing account", e);
        }
    }
    
    // Clear all saved accounts
    public void clearAllSavedAccounts() {
        prefs.edit()
            .remove(KEY_SAVED_ACCOUNTS)
            .remove(KEY_LAST_LOGIN_EMAIL)
            .apply();
        Log.d(TAG, "All saved accounts cleared");
    }
    
    // Auto login settings
    public boolean isAutoLoginEnabled() {
        return prefs.getBoolean(KEY_AUTO_LOGIN_ENABLED, false);
    }
    
    public void setAutoLoginEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_AUTO_LOGIN_ENABLED, enabled).apply();
    }
    
    public boolean isRememberAccountsEnabled() {
        return prefs.getBoolean(KEY_REMEMBER_ACCOUNTS, true);
    }
    
    public void setRememberAccountsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_REMEMBER_ACCOUNTS, enabled).apply();
        if (!enabled) {
            clearAllSavedAccounts();
        }
    }
    
    // Sync account with server (for multi-device support)
    public void syncAccountWithServer(String email, AccountSyncCallback callback) {
        executor.execute(() -> {
            try {
                // Get user from database
                User user = database.userDao().getUserByEmail(email);
                if (user != null) {
                    // Update user's last sync time
                    user.setCreatedAt(System.currentTimeMillis());
                    database.userDao().updateUser(user);
                    
                    // Save account locally
                    saveAccount(user);
                    
                    callback.onSuccess("Đồng bộ tài khoản thành công");
                } else {
                    callback.onError("Không tìm thấy tài khoản");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error syncing account", e);
                callback.onError("Lỗi đồng bộ: " + e.getMessage());
            }
        });
    }
    
    // Auto login with last account
    public void attemptAutoLogin(AutoLoginCallback callback) {
        if (!isAutoLoginEnabled()) {
            callback.onSkip("Auto login disabled");
            return;
        }
        
        String lastEmail = getLastLoginEmail();
        if (lastEmail.isEmpty()) {
            callback.onSkip("No last login email");
            return;
        }
        
        executor.execute(() -> {
            try {
                User user = database.userDao().getUserByEmail(lastEmail);
                if (user != null && user.isActive()) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Tài khoản không tồn tại hoặc đã bị khóa");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in auto login", e);
                callback.onError("Lỗi đăng nhập tự động: " + e.getMessage());
            }
        });
    }
    
    // Validate account exists on server (for multi-device)
    public void validateAccountOnServer(String email, String password, AccountValidationCallback callback) {
        executor.execute(() -> {
            try {
                // Hash password for comparison
                String hashedPassword = hashPassword(password);
                
                // Check in database
                User user = database.userDao().login(email, hashedPassword);
                if (user != null) {
                    // Account exists and valid
                    syncAccountWithServer(email, new AccountSyncCallback() {
                        @Override
                        public void onSuccess(String message) {
                            callback.onValid(user);
                        }
                        
                        @Override
                        public void onError(String error) {
                            // Even if sync fails, account is valid
                            callback.onValid(user);
                        }
                    });
                } else {
                    callback.onInvalid("Tài khoản hoặc mật khẩu không đúng");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error validating account", e);
                callback.onInvalid("Lỗi xác thực: " + e.getMessage());
            }
        });
    }
    
    // Helper method to hash password
    private String hashPassword(String password) {
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
            Log.e(TAG, "Error hashing password", e);
            return password;
        }
    }
    
    // Callback interfaces
    public interface AccountSyncCallback {
        void onSuccess(String message);
        void onError(String error);
    }
    
    public interface AutoLoginCallback {
        void onSuccess(User user);
        void onError(String error);
        void onSkip(String reason);
    }
    
    public interface AccountValidationCallback {
        void onValid(User user);
        void onInvalid(String error);
    }
}