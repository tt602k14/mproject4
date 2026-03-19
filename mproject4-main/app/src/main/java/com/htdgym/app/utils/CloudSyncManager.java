package com.htdgym.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudSyncManager {
    private static final String TAG = "CloudSyncManager";
    private static final String PREF_NAME = "cloud_sync_prefs";
    private static final String KEY_LAST_SYNC_TIME = "last_sync_time";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_SYNC_ENABLED = "sync_enabled";
    
    private static CloudSyncManager instance;
    private Context context;
    private SharedPreferences prefs;
    private GymDatabase database;
    private ExecutorService executor;
    private Gson gson;
    
    private CloudSyncManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.database = GymDatabase.getInstance(context);
        this.executor = Executors.newSingleThreadExecutor();
        this.gson = new Gson();
        
        // Generate device ID if not exists
        if (getDeviceId().isEmpty()) {
            generateDeviceId();
        }
    }
    
    public static synchronized CloudSyncManager getInstance(Context context) {
        if (instance == null) {
            instance = new CloudSyncManager(context);
        }
        return instance;
    }
    
    // Sync user data to cloud (simulated)
    public void syncUserToCloud(User user, CloudSyncCallback callback) {
        executor.execute(() -> {
            try {
                Log.d(TAG, "Syncing user to cloud: " + user.getEmail());
                
                // Simulate cloud sync delay
                Thread.sleep(1000);
                
                // Update user's sync timestamp
                user.setCreatedAt(System.currentTimeMillis());
                database.userDao().updateUser(user);
                
                // Update last sync time
                prefs.edit().putLong(KEY_LAST_SYNC_TIME, System.currentTimeMillis()).apply();
                
                callback.onSuccess("Đồng bộ thành công với cloud");
                Log.d(TAG, "User synced to cloud successfully");
                
            } catch (Exception e) {
                Log.e(TAG, "Error syncing user to cloud", e);
                callback.onError("Lỗi đồng bộ: " + e.getMessage());
            }
        });
    }
    
    // Sync user from cloud (simulated)
    public void syncUserFromCloud(String email, CloudSyncCallback callback) {
        executor.execute(() -> {
            try {
                Log.d(TAG, "Syncing user from cloud: " + email);
                
                // Simulate cloud fetch delay
                Thread.sleep(1500);
                
                // Check if user exists in local database
                User localUser = database.userDao().getUserByEmail(email);
                if (localUser != null) {
                    // User exists locally, update sync time
                    localUser.setCreatedAt(System.currentTimeMillis());
                    database.userDao().updateUser(localUser);
                    
                    prefs.edit().putLong(KEY_LAST_SYNC_TIME, System.currentTimeMillis()).apply();
                    
                    callback.onSuccess("Tài khoản đã được đồng bộ từ cloud");
                    Log.d(TAG, "User synced from cloud successfully");
                } else {
                    callback.onError("Tài khoản không tồn tại trên cloud");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error syncing user from cloud", e);
                callback.onError("Lỗi đồng bộ từ cloud: " + e.getMessage());
            }
        });
    }
    
    // Check if account exists on other devices
    public void checkAccountOnOtherDevices(String email, DeviceCheckCallback callback) {
        executor.execute(() -> {
            try {
                Log.d(TAG, "Checking account on other devices: " + email);
                
                // Simulate checking other devices
                Thread.sleep(800);
                
                // Check in local database first
                User user = database.userDao().getUserByEmail(email);
                if (user != null) {
                    // Simulate that account exists on other devices
                    boolean existsOnOtherDevices = user.getCreatedAt() > 0;
                    
                    if (existsOnOtherDevices) {
                        callback.onFound(user, "Tài khoản được tìm thấy trên thiết bị khác");
                    } else {
                        callback.onNotFound("Tài khoản chỉ tồn tại trên thiết bị này");
                    }
                } else {
                    callback.onNotFound("Tài khoản không tồn tại");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error checking account on other devices", e);
                callback.onError("Lỗi kiểm tra thiết bị: " + e.getMessage());
            }
        });
    }
    
    // Auto sync when user logs in from new device
    public void autoSyncOnNewDevice(String email, String password, AutoSyncCallback callback) {
        executor.execute(() -> {
            try {
                Log.d(TAG, "Auto sync on new device for: " + email);
                
                // Check if this is a new device login
                String lastDeviceId = prefs.getString("last_device_" + email, "");
                String currentDeviceId = getDeviceId();
                
                if (!lastDeviceId.equals(currentDeviceId)) {
                    Log.d(TAG, "New device detected, syncing account");
                    
                    // Sync user from cloud
                    syncUserFromCloud(email, new CloudSyncCallback() {
                        @Override
                        public void onSuccess(String message) {
                            // Save device ID for this user
                            prefs.edit().putString("last_device_" + email, currentDeviceId).apply();
                            callback.onSyncComplete(message);
                        }
                        
                        @Override
                        public void onError(String error) {
                            callback.onSyncFailed(error);
                        }
                    });
                } else {
                    callback.onSyncComplete("Thiết bị đã được đồng bộ trước đó");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error in auto sync on new device", e);
                callback.onSyncFailed("Lỗi đồng bộ tự động: " + e.getMessage());
            }
        });
    }
    
    // Generate unique device ID
    private void generateDeviceId() {
        String deviceId = "HTDGym_" + System.currentTimeMillis() + "_" + 
                         android.provider.Settings.Secure.getString(
                             context.getContentResolver(),
                             android.provider.Settings.Secure.ANDROID_ID
                         );
        prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply();
        Log.d(TAG, "Generated device ID: " + deviceId);
    }
    
    // Get device ID
    public String getDeviceId() {
        return prefs.getString(KEY_DEVICE_ID, "");
    }
    
    // Get last sync time
    public long getLastSyncTime() {
        return prefs.getLong(KEY_LAST_SYNC_TIME, 0);
    }
    
    // Check if sync is enabled
    public boolean isSyncEnabled() {
        return prefs.getBoolean(KEY_SYNC_ENABLED, true);
    }
    
    // Enable/disable sync
    public void setSyncEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SYNC_ENABLED, enabled).apply();
    }
    
    // Get sync status info
    public String getSyncStatusInfo() {
        long lastSync = getLastSyncTime();
        if (lastSync == 0) {
            return "Chưa đồng bộ";
        } else {
            long timeDiff = System.currentTimeMillis() - lastSync;
            long hours = timeDiff / (1000 * 60 * 60);
            if (hours < 1) {
                return "Đồng bộ gần đây";
            } else if (hours < 24) {
                return "Đồng bộ " + hours + " giờ trước";
            } else {
                long days = hours / 24;
                return "Đồng bộ " + days + " ngày trước";
            }
        }
    }
    
    // Callback interfaces
    public interface CloudSyncCallback {
        void onSuccess(String message);
        void onError(String error);
    }
    
    public interface DeviceCheckCallback {
        void onFound(User user, String message);
        void onNotFound(String message);
        void onError(String error);
    }
    
    public interface AutoSyncCallback {
        void onSyncComplete(String message);
        void onSyncFailed(String error);
    }
}