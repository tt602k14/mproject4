package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.utils.AdminManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminDashboardActivity extends AppCompatActivity {

    private static final String TAG = "AdminDashboard";
    
    // UI Components
    private TextView tvAdminName, tvTotalUsers, tvPremiumUsers;
    private ImageView btnLogout;
    private CardView cardUserManagement, cardPremiumManagement, 
                     cardContentManagement, cardSystemSettings;
    
    // Background executor
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            Log.d(TAG, "Starting AdminDashboard initialization");
            
            if (!validateAdminAccess()) {
                return;
            }
            
            setContentView(R.layout.activity_admin_dashboard);
            
            initializeComponents();
            setupClickListeners();
            loadDashboardData();
            
            Log.d(TAG, "AdminDashboard initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing AdminDashboard", e);
            handleError("Lỗi khởi tạo Admin Dashboard", e);
        }
    }

    private boolean validateAdminAccess() {
        if (!AdminManager.isAdminLoggedIn(this)) {
            Log.w(TAG, "Admin not logged in, redirecting to login");
            navigateToLogin();
            return false;
        }
        return true;
    }

    private void initializeComponents() {
        executorService = Executors.newSingleThreadExecutor();
        
        // Initialize UI components
        tvAdminName = findViewById(R.id.tv_admin_name);
        tvTotalUsers = findViewById(R.id.tv_total_users);
        tvPremiumUsers = findViewById(R.id.tv_premium_users);
        btnLogout = findViewById(R.id.btn_logout);
        
        cardUserManagement = findViewById(R.id.card_user_management);
        cardPremiumManagement = findViewById(R.id.card_premium_management);
        cardContentManagement = findViewById(R.id.card_content_management);
        cardSystemSettings = findViewById(R.id.card_system_settings);
        
        // Set admin information
        displayAdminInfo();
        
        Log.d(TAG, "Components initialized successfully");
    }

    private void displayAdminInfo() {
        String adminEmail = AdminManager.getCurrentAdminEmail(this);
        String role = AdminManager.getCurrentAdminRole(this);
        String displayName = formatAdminDisplayName(adminEmail, role);
        
        tvAdminName.setText(displayName);
    }

    private String formatAdminDisplayName(String email, String role) {
        String roleDisplay = getRoleDisplayName(role);
        return String.format("Chào mừng %s (%s)", email, roleDisplay);
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> showLogoutDialog());
        
        cardUserManagement.setOnClickListener(v -> 
            navigateToActivity(AdminUserManagementActivity.class));
        
        cardPremiumManagement.setOnClickListener(v -> 
            navigateToActivity(AdminPremiumManagementActivity.class));
        
        cardContentManagement.setOnClickListener(v -> 
            navigateToActivity(AdminContentManagementActivity.class));
        
        cardSystemSettings.setOnClickListener(v -> handleSystemSettingsAccess());
        
        Log.d(TAG, "Click listeners setup completed");
    }

    private void handleSystemSettingsAccess() {
        if (AdminManager.isSuperAdmin(this)) {
            navigateToActivity(AdminSystemSettingsActivity.class);
        } else {
            showAccessDeniedMessage();
        }
    }

    private void showAccessDeniedMessage() {
        Toast.makeText(this, "Chỉ Super Admin mới có quyền truy cập", Toast.LENGTH_SHORT).show();
    }

    private void loadDashboardData() {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Loading dashboard statistics");
                
                GymDatabase database = GymDatabase.getInstance(this);
                
                // Get statistics
                int totalUsers = database.userDao().getUserCount();
                int premiumUsers = database.premiumSubscriptionDao()
                    .getActivePremiumCount(System.currentTimeMillis());
                
                // Update UI on main thread
                runOnUiThread(() -> updateStatistics(totalUsers, premiumUsers));
                
                Log.d(TAG, "Dashboard statistics loaded successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error loading dashboard data", e);
                runOnUiThread(() -> handleDataLoadError(e));
            }
        });
    }

    private void updateStatistics(int totalUsers, int premiumUsers) {
        tvTotalUsers.setText(String.valueOf(totalUsers));
        tvPremiumUsers.setText(String.valueOf(premiumUsers));
    }

    private void handleDataLoadError(Exception e) {
        String message = "Lỗi tải dữ liệu: " + e.getMessage();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi Admin Panel?")
            .setPositiveButton("Đăng xuất", (dialog, which) -> performLogout())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void performLogout() {
        try {
            AdminManager.logoutAdmin(this);
            Toast.makeText(this, "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        } catch (Exception e) {
            Log.e(TAG, "Error during logout", e);
            handleError("Lỗi đăng xuất", e);
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToActivity(Class<?> activityClass) {
        try {
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to activity: " + activityClass.getSimpleName(), e);
            handleError("Lỗi điều hướng", e);
        }
    }

    private String getRoleDisplayName(String role) {
        switch (role) {
            case "super_admin":
                return "Super Admin";
            case "admin":
                return "Admin";
            case "moderator":
                return "Moderator";
            default:
                return "Unknown";
        }
    }

    private void handleError(String message, Exception e) {
        String fullMessage = message + ": " + e.getMessage();
        Toast.makeText(this, fullMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        
        Log.d(TAG, "AdminDashboard destroyed and resources cleaned up");
    }
}