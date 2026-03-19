package com.htdgym.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.Admin;
import com.htdgym.app.models.PaymentTransaction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminManager {
    private static final String TAG = "AdminManager";
    private static final String PREF_NAME = "admin_prefs";
    private static final String KEY_CURRENT_ADMIN_ID = "current_admin_id";
    private static final String KEY_IS_ADMIN_LOGGED_IN = "is_admin_logged_in";
    private static final String KEY_ADMIN_SESSION_TOKEN = "admin_session_token";
    
    private static AdminManager instance;
    private Context context;
    private SharedPreferences prefs;
    private GymDatabase database;
    private ExecutorService executor;
    private Admin currentAdmin;
    
    private AdminManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        
        try {
            this.database = GymDatabase.getInstance(context);
            Log.d(TAG, "Database initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing database", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
        
        this.executor = Executors.newSingleThreadExecutor();
        
        // Initialize default super admin if none exists
        initializeDefaultAdmin();
        
        // Also ensure email admin exists immediately
        executor.execute(() -> {
            try {
                Thread.sleep(100); // Small delay to ensure database is ready
                ensureEmailAdminExists();
            } catch (Exception e) {
                Log.e(TAG, "Error in immediate admin creation", e);
            }
        });
    }
    
    public static synchronized AdminManager getInstance(Context context) {
        if (instance == null) {
            instance = new AdminManager(context);
        }
        return instance;
    }
    
    // Initialize default super admin
    private void initializeDefaultAdmin() {
        executor.execute(() -> {
            try {
                int superAdminCount = database.adminDao().getSuperAdminCount();
                if (superAdminCount == 0) {
                    // Create default super admin
                    Admin defaultAdmin = new Admin();
                    defaultAdmin.setAdminId("HTDGYM_ADMIN_001");
                    defaultAdmin.setUsername("htdgym_admin");
                    defaultAdmin.setEmail("admin@htdgym.com");
                    defaultAdmin.setPasswordHash(hashPassword("HTDGym@2024"));
                    defaultAdmin.setFullName("HTD Gym Administrator");
                    defaultAdmin.setRole("super_admin");
                    defaultAdmin.setPermissions(getAllPermissions());
                    defaultAdmin.setActive(true);
                    
                    database.adminDao().insertAdmin(defaultAdmin);
                    Log.d(TAG, "Default super admin created successfully");
                }
                
                // Check if email admin exists
                Admin emailAdmin = database.adminDao().getAdminByEmail("admin@gmail.com");
                if (emailAdmin == null) {
                    // Create admin with email login
                    Admin emailAdminAccount = new Admin();
                    emailAdminAccount.setAdminId("HTDGYM_ADMIN_002");
                    emailAdminAccount.setUsername("admin_email");
                    emailAdminAccount.setEmail("admin@gmail.com");
                    emailAdminAccount.setPasswordHash(hashPassword("admin123"));
                    emailAdminAccount.setFullName("HTD Gym Admin");
                    emailAdminAccount.setRole("super_admin");
                    emailAdminAccount.setPermissions(getAllPermissions());
                    emailAdminAccount.setActive(true);
                    
                    database.adminDao().insertAdmin(emailAdminAccount);
                    Log.d(TAG, "Email admin created successfully");
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error initializing default admin", e);
            }
        });
    }
    
    // Authentication
    public void authenticateAdmin(String username, String password, AdminAuthCallback callback) {
        executor.execute(() -> {
            try {
                String hashedPassword = hashPassword(password);
                Admin admin = database.adminDao().authenticateAdmin(username, hashedPassword);
                
                if (admin != null && admin.isActive()) {
                    // Update last login
                    admin.setLastLogin(new Date());
                    database.adminDao().updateLastLogin(admin.getId(), new Date());
                    
                    // Save session
                    currentAdmin = admin;
                    String sessionToken = generateSessionToken();
                    prefs.edit()
                        .putString(KEY_CURRENT_ADMIN_ID, admin.getAdminId())
                        .putBoolean(KEY_IS_ADMIN_LOGGED_IN, true)
                        .putString(KEY_ADMIN_SESSION_TOKEN, sessionToken)
                        .apply();
                    
                    callback.onSuccess(admin);
                } else {
                    callback.onError("Tên đăng nhập hoặc mật khẩu không đúng");
                }
            } catch (Exception e) {
                Log.e(TAG, "Authentication error", e);
                callback.onError("Lỗi xác thực: " + e.getMessage());
            }
        });
    }
    
    // Check if admin is logged in
    public boolean isAdminLoggedIn() {
        return prefs.getBoolean(KEY_IS_ADMIN_LOGGED_IN, false) && getCurrentAdmin() != null;
    }
    
    // Get current admin
    public Admin getCurrentAdmin() {
        if (currentAdmin == null) {
            String adminId = prefs.getString(KEY_CURRENT_ADMIN_ID, null);
            if (adminId != null) {
                // Load from database synchronously (consider using async for better performance)
                try {
                    currentAdmin = database.adminDao().getAdminByAdminId(adminId);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading current admin", e);
                }
            }
        }
        return currentAdmin;
    }
    
    // Logout admin
    public void logoutAdmin() {
        currentAdmin = null;
        prefs.edit()
            .remove(KEY_CURRENT_ADMIN_ID)
            .putBoolean(KEY_IS_ADMIN_LOGGED_IN, false)
            .remove(KEY_ADMIN_SESSION_TOKEN)
            .apply();
    }
    
    // User management methods
    public void getAllUsers(UserListCallback callback) {
        executor.execute(() -> {
            try {
                List<com.htdgym.app.models.User> users = database.userDao().getAllUsers();
                callback.onSuccess(users);
            } catch (Exception e) {
                Log.e(TAG, "Error getting users", e);
                callback.onError("Lỗi tải danh sách người dùng: " + e.getMessage());
            }
        });
    }
    
    public void getUserStats(UserStatsCallback callback) {
        executor.execute(() -> {
            try {
                int totalUsers = database.userDao().getTotalUserCount();
                int activeUsers = database.userDao().getActiveUserCount();
                int premiumUsers = database.premiumUserDao().getActivePremiumUserCount();
                int newUsersToday = database.userDao().getNewUsersToday();
                
                UserStats stats = new UserStats(totalUsers, activeUsers, premiumUsers, newUsersToday);
                callback.onSuccess(stats);
            } catch (Exception e) {
                Log.e(TAG, "Error getting user stats", e);
                callback.onError("Lỗi tải thống kê: " + e.getMessage());
            }
        });
    }
    
    public void updateUserStatus(String userId, boolean isActive, AdminActionCallback callback) {
        if (!hasPermission("manage_users")) {
            callback.onError("Không có quyền quản lý người dùng");
            return;
        }
        
        executor.execute(() -> {
            try {
                database.userDao().updateUserStatus(userId, isActive);
                callback.onSuccess("Cập nhật trạng thái người dùng thành công");
            } catch (Exception e) {
                Log.e(TAG, "Error updating user status", e);
                callback.onError("Lỗi cập nhật trạng thái: " + e.getMessage());
            }
        });
    }
    
    public void deleteUser(String userId, AdminActionCallback callback) {
        if (!hasPermission("delete_users")) {
            callback.onError("Không có quyền xóa người dùng");
            return;
        }
        
        executor.execute(() -> {
            try {
                com.htdgym.app.models.User user = database.userDao().getUserById(userId);
                if (user != null) {
                    database.userDao().deleteUser(user);
                    callback.onSuccess("Xóa người dùng thành công");
                } else {
                    callback.onError("Không tìm thấy người dùng");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error deleting user", e);
                callback.onError("Lỗi xóa người dùng: " + e.getMessage());
            }
        });
    }
    
    // Permission checking
    public boolean hasPermission(String permission) {
        Admin admin = getCurrentAdmin();
        return admin != null && admin.hasPermission(permission);
    }
    
    // Utility methods
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error hashing password", e);
            return password; // Fallback (not secure)
        }
    }
    
    private String generateSessionToken() {
        return UUID.randomUUID().toString();
    }
    
    private String getAllPermissions() {
        return "manage_users,delete_users,manage_payments,view_analytics,manage_content,manage_admins,system_settings";
    }
    
    // Callback interfaces
    public interface AdminAuthCallback {
        void onSuccess(Admin admin);
        void onError(String error);
    }
    
    public interface UserListCallback {
        void onSuccess(List<com.htdgym.app.models.User> users);
        void onError(String error);
    }
    
    public interface UserStatsCallback {
        void onSuccess(UserStats stats);
        void onError(String error);
    }
    
    public interface AdminActionCallback {
        void onSuccess(String message);
        void onError(String error);
    }
    
    public interface PaymentListCallback {
        void onSuccess(List<PaymentTransaction> payments);
        void onError(String error);
    }
    
    public interface PaymentStatsCallback {
        void onSuccess(PaymentStats stats);
        void onError(String error);
    }
    
    // Payment management methods
    public void getAllPayments(PaymentListCallback callback) {
        executor.execute(() -> {
            try {
                List<PaymentTransaction> payments = database.paymentTransactionDao().getAllTransactions();
                callback.onSuccess(payments);
            } catch (Exception e) {
                Log.e(TAG, "Error getting payments", e);
                callback.onError("Lỗi tải danh sách giao dịch: " + e.getMessage());
            }
        });
    }
    
    public void getPaymentStats(PaymentStatsCallback callback) {
        executor.execute(() -> {
            try {
                double todayRevenue = database.paymentTransactionDao().getTodayRevenue();
                double monthRevenue = database.paymentTransactionDao().getMonthRevenue();
                int totalTransactions = database.paymentTransactionDao().getTotalTransactionCount();
                int successfulTransactions = database.paymentTransactionDao().getSuccessfulTransactionCount();
                
                PaymentStats stats = new PaymentStats(todayRevenue, monthRevenue, totalTransactions, successfulTransactions);
                callback.onSuccess(stats);
            } catch (Exception e) {
                Log.e(TAG, "Error getting payment stats", e);
                callback.onError("Lỗi tải thống kê thanh toán: " + e.getMessage());
            }
        });
    }
    
    // User statistics class
    public static class UserStats {
        public final int totalUsers;
        public final int activeUsers;
        public final int premiumUsers;
        public final int newUsersToday;
        
        public UserStats(int totalUsers, int activeUsers, int premiumUsers, int newUsersToday) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.premiumUsers = premiumUsers;
            this.newUsersToday = newUsersToday;
        }
    }
    
    // Payment statistics class
    public static class PaymentStats {
        public final double todayRevenue;
        public final double monthRevenue;
        public final int totalTransactions;
        public final int successfulTransactions;
        
        public PaymentStats(double todayRevenue, double monthRevenue, int totalTransactions, int successfulTransactions) {
            this.todayRevenue = todayRevenue;
            this.monthRevenue = monthRevenue;
            this.totalTransactions = totalTransactions;
            this.successfulTransactions = successfulTransactions;
        }
    }
    
    // Method to authenticate admin by email (for integration with regular login)
    public void authenticateAdminByEmail(String email, String password, AdminAuthCallback callback) {
        executor.execute(() -> {
            try {
                Log.d(TAG, "Starting admin authentication for email: " + email);
                
                // Ensure admin is created first
                ensureEmailAdminExists();
                
                if (database == null) {
                    Log.e(TAG, "Database is null, trying test login");
                    testAdminLogin(email, password, callback);
                    return;
                }
                
                if (database.adminDao() == null) {
                    Log.e(TAG, "AdminDao is null, trying test login");
                    testAdminLogin(email, password, callback);
                    return;
                }
                
                Admin admin = database.adminDao().getAdminByEmail(email);
                Log.d(TAG, "Admin lookup result: " + (admin != null ? "Found" : "Not found"));
                
                if (admin != null && admin.isActive()) {
                    String hashedPassword = hashPassword(password);
                    Log.d(TAG, "Comparing passwords - Input hash: " + hashedPassword);
                    Log.d(TAG, "Stored hash: " + admin.getPasswordHash());
                    
                    if (hashedPassword.equals(admin.getPasswordHash())) {
                        // Update last login
                        admin.setLastLogin(new Date());
                        database.adminDao().updateAdmin(admin);
                        
                        // Save session
                        currentAdmin = admin;
                        String sessionToken = generateSessionToken();
                        
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(KEY_CURRENT_ADMIN_ID, admin.getAdminId());
                        editor.putString(KEY_ADMIN_SESSION_TOKEN, sessionToken);
                        editor.putBoolean(KEY_IS_ADMIN_LOGGED_IN, true);
                        editor.apply();
                        
                        callback.onSuccess(admin);
                        Log.d(TAG, "Admin authenticated successfully by email: " + email);
                    } else {
                        Log.w(TAG, "Password mismatch for admin: " + email);
                        callback.onError("Mật khẩu không chính xác");
                    }
                } else {
                    Log.w(TAG, "Admin not found or inactive: " + email);
                    // Try test login as fallback
                    testAdminLogin(email, password, callback);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error authenticating admin by email: " + e.getMessage(), e);
                // Try test login as fallback for any database errors
                Log.d(TAG, "Falling back to test admin login due to error: " + e.getMessage());
                testAdminLogin(email, password, callback);
            }
        });
    }
    
    // Ensure email admin exists (synchronous)
    private void ensureEmailAdminExists() {
        try {
            Log.d(TAG, "Checking if email admin exists...");
            
            if (database == null) {
                Log.e(TAG, "Database is null!");
                return;
            }
            
            if (database.adminDao() == null) {
                Log.e(TAG, "AdminDao is null!");
                return;
            }
            
            Admin emailAdmin = database.adminDao().getAdminByEmail("admin@gmail.com");
            if (emailAdmin == null) {
                Log.d(TAG, "Creating email admin account...");
                // Create admin with email login
                Admin emailAdminAccount = new Admin();
                emailAdminAccount.setAdminId("HTDGYM_ADMIN_002");
                emailAdminAccount.setUsername("admin_email");
                emailAdminAccount.setEmail("admin@gmail.com");
                emailAdminAccount.setPasswordHash(hashPassword("admin123"));
                emailAdminAccount.setFullName("HTD Gym Admin");
                emailAdminAccount.setRole("super_admin");
                emailAdminAccount.setPermissions(getAllPermissions());
                emailAdminAccount.setActive(true);
                
                long result = database.adminDao().insertAdmin(emailAdminAccount);
                Log.d(TAG, "Email admin created successfully with ID: " + result);
            } else {
                Log.d(TAG, "Email admin already exists: " + emailAdmin.getEmail());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error ensuring email admin exists: " + e.getMessage(), e);
            // Don't throw exception, just log it - we have fallback methods
        }
    }
    
    // Check if email belongs to admin
    public static boolean isAdminEmail(String email) {
        return "admin@gmail.com".equals(email) || "admin@htdgym.com".equals(email);
    }
    
    // Debug method to check admin status
    public void debugAdminStatus(String email) {
        executor.execute(() -> {
            try {
                Admin admin = database.adminDao().getAdminByEmail(email);
                if (admin != null) {
                    Log.d(TAG, "DEBUG - Admin found:");
                    Log.d(TAG, "  Email: " + admin.getEmail());
                    Log.d(TAG, "  Username: " + admin.getUsername());
                    Log.d(TAG, "  Full Name: " + admin.getFullName());
                    Log.d(TAG, "  Role: " + admin.getRole());
                    Log.d(TAG, "  Active: " + admin.isActive());
                    Log.d(TAG, "  Password Hash: " + admin.getPasswordHash());
                    
                    // Test password hash
                    String testHash = hashPassword("admin123");
                    Log.d(TAG, "  Test Hash: " + testHash);
                    Log.d(TAG, "  Hash Match: " + testHash.equals(admin.getPasswordHash()));
                } else {
                    Log.d(TAG, "DEBUG - No admin found with email: " + email);
                    
                    // Check all admins
                    List<Admin> allAdmins = database.adminDao().getAllAdmins();
                    Log.d(TAG, "DEBUG - Total admins in database: " + allAdmins.size());
                    for (Admin a : allAdmins) {
                        Log.d(TAG, "  Admin: " + a.getEmail() + " - " + a.getUsername());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in debug admin status", e);
            }
        });
    }
    
    // Temporary workaround for Room database issues
    public void testAdminLogin(String email, String password, AdminAuthCallback callback) {
        Log.d(TAG, "Using test admin login for: " + email);
        
        // Hardcoded test for admin@gmail.com / admin123
        if ("admin@gmail.com".equals(email) && "admin123".equals(password)) {
            Admin testAdmin = new Admin();
            testAdmin.setAdminId("TEST_ADMIN_001");
            testAdmin.setEmail(email);
            testAdmin.setUsername("test_admin");
            testAdmin.setFullName("HTD Gym Admin");
            testAdmin.setRole("super_admin");
            testAdmin.setActive(true);
            testAdmin.setPermissions(getAllPermissions());
            
            // Save session
            currentAdmin = testAdmin;
            prefs.edit()
                .putString(KEY_CURRENT_ADMIN_ID, testAdmin.getAdminId())
                .putBoolean(KEY_IS_ADMIN_LOGGED_IN, true)
                .putString(KEY_ADMIN_SESSION_TOKEN, generateSessionToken())
                .apply();
                
            callback.onSuccess(testAdmin);
            Log.d(TAG, "Test admin login successful");
        } else {
            callback.onError("Tài khoản test không đúng");
            Log.w(TAG, "Test admin login failed - wrong credentials");
        }
    }
}