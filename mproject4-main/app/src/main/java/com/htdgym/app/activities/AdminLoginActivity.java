package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.htdgym.app.R;
import com.htdgym.app.models.Admin;
import com.htdgym.app.utils.AdminManager;

public class AdminLoginActivity extends AppCompatActivity {
    
    private TextInputEditText etUsername, etPassword;
    private CheckBox cbRemember;
    private Button btnLogin;
    private TextView tvForgotPassword, tvBackToUserLogin;
    private ProgressBar progressBar;
    
    private AdminManager adminManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        
        adminManager = AdminManager.getInstance(this);
        
        // Check if admin is already logged in
        if (adminManager.isAdminLoggedIn()) {
            navigateToAdminDashboard();
            return;
        }
        
        initViews();
        setupClickListeners();
        
        // Debug: Pre-fill admin credentials for testing
        etUsername.setText("admin@gmail.com");
        etPassword.setText("admin123");
        
        // Debug: Check if admin account exists
        checkAdminAccountExists();
    }
    
    private void checkAdminAccountExists() {
        adminManager.authenticateAdminByEmail("admin@gmail.com", "admin123", new AdminManager.AdminAuthCallback() {
            @Override
            public void onSuccess(Admin admin) {
                runOnUiThread(() -> {
                    Toast.makeText(AdminLoginActivity.this, 
                            "✅ Tài khoản admin đã sẵn sàng: " + admin.getEmail(), 
                            Toast.LENGTH_LONG).show();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AdminLoginActivity.this, 
                            "❌ Lỗi tài khoản admin: " + error, 
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private void initViews() {
        etUsername = findViewById(R.id.et_admin_username);
        etPassword = findViewById(R.id.et_admin_password);
        cbRemember = findViewById(R.id.cb_remember_admin);
        btnLogin = findViewById(R.id.btn_admin_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvBackToUserLogin = findViewById(R.id.tv_back_to_user_login);
        progressBar = findViewById(R.id.progress_admin_login);
    }
    
    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
        
        tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
        
        tvBackToUserLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    
    private void attemptLogin() {
        String emailOrUsername = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // Validation
        if (emailOrUsername.isEmpty()) {
            etUsername.setError("Vui lòng nhập email hoặc tên đăng nhập");
            etUsername.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }
        
        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return;
        }
        
        // Show loading
        showLoading(true);
        
        // Check if input is email or username and authenticate accordingly
        if (emailOrUsername.contains("@")) {
            // Authenticate by email
            adminManager.authenticateAdminByEmail(emailOrUsername, password, new AdminManager.AdminAuthCallback() {
                @Override
                public void onSuccess(Admin admin) {
                    runOnUiThread(() -> {
                        showLoading(false);
                        Toast.makeText(AdminLoginActivity.this, 
                                "Chào mừng " + admin.getFullName() + "! 👑", 
                                Toast.LENGTH_SHORT).show();
                        navigateToAdminDashboard();
                    });
                }
                
                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        showLoading(false);
                        Toast.makeText(AdminLoginActivity.this, error, Toast.LENGTH_LONG).show();
                        
                        // Clear password field on error
                        etPassword.setText("");
                        etPassword.requestFocus();
                    });
                }
            });
        } else {
            // Authenticate by username
            adminManager.authenticateAdmin(emailOrUsername, password, new AdminManager.AdminAuthCallback() {
                @Override
                public void onSuccess(Admin admin) {
                    runOnUiThread(() -> {
                        showLoading(false);
                        Toast.makeText(AdminLoginActivity.this, 
                                "Chào mừng " + admin.getFullName() + "! 👑", 
                                Toast.LENGTH_SHORT).show();
                        navigateToAdminDashboard();
                    });
                }
                
                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        showLoading(false);
                        Toast.makeText(AdminLoginActivity.this, error, Toast.LENGTH_LONG).show();
                        
                        // Clear password field on error
                        etPassword.setText("");
                        etPassword.requestFocus();
                    });
                }
            });
        }
    }
    
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        etUsername.setEnabled(!show);
        etPassword.setEnabled(!show);
    }
    
    private void navigateToAdminDashboard() {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    private void showForgotPasswordDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = 
                new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("🔐 Quên mật khẩu Admin")
                .setMessage("Để khôi phục mật khẩu Admin, vui lòng liên hệ:\n\n" +
                           "📧 Email: admin@htdgym.com\n" +
                           "📞 Hotline: 1900-HTD-GYM\n" +
                           "🌐 Website: htdgym.com/admin-support\n\n" +
                           "Bạn sẽ cần cung cấp thông tin xác thực để được hỗ trợ.")
                .setPositiveButton("Đã hiểu", null)
                .setNegativeButton("Liên hệ ngay", (dialog, which) -> {
                    // Open email or phone app
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"admin@htdgym.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Yêu cầu khôi phục mật khẩu Admin");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Tôi cần hỗ trợ khôi phục mật khẩu Admin...");
                    
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Gửi email hỗ trợ"));
                    } catch (Exception e) {
                        Toast.makeText(this, "Không thể mở ứng dụng email", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
    
    @Override
    public void onBackPressed() {
        // Navigate back to user login instead of closing app
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}