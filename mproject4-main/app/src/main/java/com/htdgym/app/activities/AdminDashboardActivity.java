package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.htdgym.app.R;
import com.htdgym.app.fragments.AdminDashboardFragment;
import com.htdgym.app.fragments.AdminUserManagementFragment;
// import com.htdgym.app.fragments.AdminPaymentManagementFragment;
import com.htdgym.app.fragments.AdminAnalyticsFragment;
import com.htdgym.app.models.Admin;
import com.htdgym.app.utils.AdminManager;

public class AdminDashboardActivity extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener {
    
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private AdminManager adminManager;
    private Admin currentAdmin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        
        adminManager = AdminManager.getInstance(this);
        
        // Check admin authentication
        if (!adminManager.isAdminLoggedIn()) {
            redirectToLogin();
            return;
        }
        
        currentAdmin = adminManager.getCurrentAdmin();
        
        initViews();
        setupToolbar();
        setupNavigationDrawer();
        updateNavigationHeader();
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new AdminDashboardFragment());
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }
    }
    
    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("👑 HTD Gym Admin");
        }
    }
    
    private void setupNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, 
                R.string.navigation_drawer_open, 
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        
        navigationView.setNavigationItemSelectedListener(this);
    }
    
    private void updateNavigationHeader() {
        if (currentAdmin != null) {
            TextView tvAdminName = navigationView.getHeaderView(0).findViewById(R.id.tv_admin_name);
            TextView tvAdminRole = navigationView.getHeaderView(0).findViewById(R.id.tv_admin_role);
            
            tvAdminName.setText(currentAdmin.getFullName());
            tvAdminRole.setText(getRoleDisplayName(currentAdmin.getRole()));
        }
    }
    
    private String getRoleDisplayName(String role) {
        switch (role) {
            case "super_admin": return "Super Admin";
            case "admin": return "Administrator";
            case "moderator": return "Moderator";
            default: return "Admin";
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String title = "";
        
        int itemId = item.getItemId();
        
        if (itemId == R.id.nav_dashboard) {
            fragment = new AdminDashboardFragment();
            title = "👑 Tổng quan Admin";
        } else if (itemId == R.id.nav_user_management) {
            if (adminManager.hasPermission("manage_users")) {
                fragment = new AdminUserManagementFragment();
                title = "👥 Quản lý người dùng";
            } else {
                showPermissionDenied();
            }
        } else if (itemId == R.id.nav_payment_management) {
            if (adminManager.hasPermission("manage_payments")) {
                // fragment = new AdminPaymentManagementFragment();
                // title = "💳 Quản lý thanh toán";
                Toast.makeText(this, "💳 Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
            } else {
                showPermissionDenied();
            }
        } else if (itemId == R.id.nav_content_management) {
            if (adminManager.hasPermission("manage_content")) {
                // TODO: Create ContentManagementFragment
                Toast.makeText(this, "📝 Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
            } else {
                showPermissionDenied();
            }
        } else if (itemId == R.id.nav_analytics) {
            if (adminManager.hasPermission("view_analytics")) {
                fragment = new AdminAnalyticsFragment();
                title = "📈 Thống kê & Báo cáo";
            } else {
                showPermissionDenied();
            }
        } else if (itemId == R.id.nav_system_settings) {
            if (adminManager.hasPermission("system_settings")) {
                // TODO: Create SystemSettingsFragment
                Toast.makeText(this, "⚙️ Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
            } else {
                showPermissionDenied();
            }
        } else if (itemId == R.id.nav_admin_profile) {
            // TODO: Create AdminProfileFragment
            Toast.makeText(this, "👤 Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_change_password) {
            // TODO: Create ChangePasswordDialog for admin
            Toast.makeText(this, "🔐 Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_logout) {
            showLogoutConfirmation();
        }
        
        if (fragment != null) {
            loadFragment(fragment);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }
        }
        
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
    
    private void showPermissionDenied() {
        Toast.makeText(this, "❌ Bạn không có quyền truy cập tính năng này", Toast.LENGTH_SHORT).show();
    }
    
    private void showLogoutConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("🚪 Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản Admin?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    adminManager.logoutAdmin();
                    redirectToLogin();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void redirectToLogin() {
        Intent intent = new Intent(this, AdminLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Show confirmation before exiting
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Thoát ứng dụng")
                    .setMessage("Bạn có muốn thoát khỏi trang quản trị?")
                    .setPositiveButton("Thoát", (dialog, which) -> super.onBackPressed())
                    .setNegativeButton("Hủy", null)
                    .show();
        }
    }
}