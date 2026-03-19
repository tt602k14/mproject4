package com.htdgym.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.MainActivity;
import com.htdgym.app.R;
import com.htdgym.app.adapters.SavedAccountAdapter;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.User;
import com.htdgym.app.models.Admin;
import com.htdgym.app.utils.AccountManager;
import com.htdgym.app.utils.AdminManager;
import com.htdgym.app.utils.CloudSyncManager;
import com.htdgym.app.activities.AdminDashboardActivity;

import java.security.MessageDigest;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.htdgym.app.activities.BaseActivity;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    
    // Tabs
    private TextView tabLogin, tabRegister, tabForgot, btnClose;
    
    // Login Form
    private LinearLayout formLogin;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private CheckBox cbRememberMe, cbAutoLogin;
    private TextView tvSavedAccounts;
    private RecyclerView rvSavedAccounts;
    
    // Register Form
    private LinearLayout formRegister;
    private EditText etName, etRegisterEmail, etRegisterPassword, etConfirmPassword;
    private Button btnRegister;
    
    // Forgot Password Form
    private LinearLayout formForgot;
    private EditText etForgotEmail;
    private Button btnSendReset;
    
    private SharedPreferences sharedPreferences;
    private GymDatabase database;
    private ExecutorService executorService;
    private AccountManager accountManager;
    private SavedAccountAdapter savedAccountAdapter;
    private CloudSyncManager cloudSyncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: Starting LoginActivity");

        try {
            sharedPreferences = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);
            database = GymDatabase.getInstance(getApplicationContext());
            executorService = Executors.newSingleThreadExecutor();
            accountManager = AccountManager.getInstance(this);
            cloudSyncManager = CloudSyncManager.getInstance(this);

            // Check if already logged in
            if (sharedPreferences.getBoolean("is_logged_in", false)) {
                Log.d(TAG, "onCreate: User already logged in, navigating to MainActivity");
                navigateToMain();
                return;
            }

            initViews();
            setupClickListeners();
            setupSavedAccounts();
            
            // Attempt auto login if enabled
            attemptAutoLogin();
            
            Log.d(TAG, "onCreate: LoginActivity initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error initializing LoginActivity", e);
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        // Tabs
        tabLogin = findViewById(R.id.tab_login);
        tabRegister = findViewById(R.id.tab_register);
        tabForgot = findViewById(R.id.tab_forgot);
        btnClose = findViewById(R.id.btn_close);
        
        // Login Form
        formLogin = findViewById(R.id.form_login);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        cbRememberMe = findViewById(R.id.cb_remember_me);
        cbAutoLogin = findViewById(R.id.cb_auto_login);
        tvSavedAccounts = findViewById(R.id.tv_saved_accounts);
        rvSavedAccounts = findViewById(R.id.rv_saved_accounts);
        
        // Register Form
        formRegister = findViewById(R.id.form_register);
        etName = findViewById(R.id.et_name);
        etRegisterEmail = findViewById(R.id.et_register_email);
        etRegisterPassword = findViewById(R.id.et_register_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        
        // Forgot Password Form
        formForgot = findViewById(R.id.form_forgot);
        etForgotEmail = findViewById(R.id.et_forgot_email);
        btnSendReset = findViewById(R.id.btn_send_reset);
    }

    private void setupClickListeners() {
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> finish());
        }
        
        // Tab clicks
        tabLogin.setOnClickListener(v -> showLoginForm());
        tabRegister.setOnClickListener(v -> showRegisterForm());
        tabForgot.setOnClickListener(v -> showForgotForm());
        
        // Button clicks
        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> registerUser());
        btnSendReset.setOnClickListener(v -> sendResetLink());
        
        // Checkbox listeners
        if (cbRememberMe != null) {
            cbRememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
                accountManager.setRememberAccountsEnabled(isChecked);
                if (!isChecked) {
                    cbAutoLogin.setChecked(false);
                    accountManager.setAutoLoginEnabled(false);
                }
                updateSavedAccountsVisibility();
            });
        }
        
        if (cbAutoLogin != null) {
            cbAutoLogin.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked && !cbRememberMe.isChecked()) {
                    cbRememberMe.setChecked(true);
                    accountManager.setRememberAccountsEnabled(true);
                }
                accountManager.setAutoLoginEnabled(isChecked);
            });
        }
        
        // Admin access (hidden button)
        TextView tvAdminAccess = findViewById(R.id.tv_admin_access);
        if (tvAdminAccess != null) {
            tvAdminAccess.setOnClickListener(v -> {
                Intent adminIntent = new Intent(this, AdminLoginActivity.class);
                startActivity(adminIntent);
            });
        }
        
        // Sync info button
        TextView tvSyncInfo = findViewById(R.id.tv_sync_info);
        if (tvSyncInfo != null) {
            tvSyncInfo.setOnClickListener(v -> showSyncInfoDialog());
        }
    }

    private void showLoginForm() {
        // Update tabs
        tabLogin.setBackgroundColor(0xFF4CAF50);
        tabLogin.setTextColor(0xFFFFFFFF);
        tabRegister.setBackgroundColor(0xFF3C3C3C);
        tabRegister.setTextColor(0xFFB0B0B0);
        tabForgot.setBackgroundColor(0xFF3C3C3C);
        tabForgot.setTextColor(0xFFB0B0B0);
        
        // Show/hide forms
        formLogin.setVisibility(View.VISIBLE);
        formRegister.setVisibility(View.GONE);
        formForgot.setVisibility(View.GONE);
    }

    private void showRegisterForm() {
        // Update tabs
        tabLogin.setBackgroundColor(0xFF3C3C3C);
        tabLogin.setTextColor(0xFFB0B0B0);
        tabRegister.setBackgroundColor(0xFF4CAF50);
        tabRegister.setTextColor(0xFFFFFFFF);
        tabForgot.setBackgroundColor(0xFF3C3C3C);
        tabForgot.setTextColor(0xFFB0B0B0);
        
        // Show/hide forms
        formLogin.setVisibility(View.GONE);
        formRegister.setVisibility(View.VISIBLE);
        formForgot.setVisibility(View.GONE);
    }

    private void showForgotForm() {
        // Update tabs
        tabLogin.setBackgroundColor(0xFF3C3C3C);
        tabLogin.setTextColor(0xFFB0B0B0);
        tabRegister.setBackgroundColor(0xFF3C3C3C);
        tabRegister.setTextColor(0xFFB0B0B0);
        tabForgot.setBackgroundColor(0xFF4CAF50);
        tabForgot.setTextColor(0xFFFFFFFF);
        
        // Show/hide forms
        formLogin.setVisibility(View.GONE);
        formRegister.setVisibility(View.GONE);
        formForgot.setVisibility(View.VISIBLE);
    }

    private void loginUser() {
        Log.d(TAG, "loginUser: Starting login process");
        
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation
        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        // Show progress
        showProgress(true);
        btnLogin.setEnabled(false);

        // Check if this is an admin login first
        if (AdminManager.isAdminEmail(email)) {
            Log.d(TAG, "loginUser: Admin email detected, attempting admin login");
            AdminManager adminManager = AdminManager.getInstance(this);
            
            // Debug admin status
            adminManager.debugAdminStatus(email);
            
            adminManager.authenticateAdminByEmail(email, password, new AdminManager.AdminAuthCallback() {
                @Override
                public void onSuccess(Admin admin) {
                    runOnUiThread(() -> {
                        showProgress(false);
                        btnLogin.setEnabled(true);
                        Log.d(TAG, "loginUser: Admin login successful");
                        Toast.makeText(LoginActivity.this, 
                                "Chào mừng Admin " + admin.getFullName() + "!", Toast.LENGTH_SHORT).show();
                        navigateToAdminDashboard();
                    });
                }

                @Override
                public void onError(String error) {
                    Log.w(TAG, "loginUser: Admin login failed: " + error);
                    runOnUiThread(() -> {
                        showProgress(false);
                        btnLogin.setEnabled(true);
                        
                        // Show specific error message for admin login
                        if (error.contains("Database") || error.contains("Room") || error.contains("không khả dụng")) {
                            Toast.makeText(LoginActivity.this, 
                                    "Lỗi hệ thống admin. Vui lòng thử lại sau.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, 
                                    "Đăng nhập admin thất bại: " + error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            return;
        }

        // Regular user login
        performRegularUserLogin(email, password);
    }
    
    private void performRegularUserLogin(String email, String password) {
        // Hash password
        final String hashedPassword = hashPassword(password);
        Log.d(TAG, "performRegularUserLogin: Password hashed");

        // Query database in background
        executorService.execute(() -> {
            try {
                Log.d(TAG, "performRegularUserLogin: Querying database for user: " + email);
                User user = database.userDao().login(email, hashedPassword);
                
                runOnUiThread(() -> {
                    showProgress(false);
                    btnLogin.setEnabled(true);
                    
                    if (user != null) {
                        Log.d(TAG, "performRegularUserLogin: Login successful for user: " + user.getEmail());
                        
                        // Auto sync on new device
                        cloudSyncManager.autoSyncOnNewDevice(email, password, new CloudSyncManager.AutoSyncCallback() {
                            @Override
                            public void onSyncComplete(String message) {
                                Log.d(TAG, "Auto sync completed: " + message);
                                runOnUiThread(() -> {
                                    saveUserSession(user);
                                    Toast.makeText(LoginActivity.this, 
                                            "Đăng nhập thành công! " + message, Toast.LENGTH_SHORT).show();
                                    navigateToMain();
                                });
                            }
                            
                            @Override
                            public void onSyncFailed(String error) {
                                Log.w(TAG, "Auto sync failed: " + error);
                                // Continue with normal login even if sync fails
                                runOnUiThread(() -> {
                                    saveUserSession(user);
                                    Toast.makeText(LoginActivity.this, 
                                            "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    navigateToMain();
                                });
                            }
                        });
                    } else {
                        Log.w(TAG, "performRegularUserLogin: Login failed - invalid credentials");
                        Toast.makeText(LoginActivity.this, 
                                "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "performRegularUserLogin: Error during login", e);
                runOnUiThread(() -> {
                    showProgress(false);
                    btnLogin.setEnabled(true);
                    Toast.makeText(LoginActivity.this, 
                            "Lỗi đăng nhập: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void registerUser() {
        Log.d(TAG, "registerUser: Starting registration process");
        
        String name = etName.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Validation
        if (name.isEmpty()) {
            etName.setError("Vui lòng nhập họ tên");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etRegisterEmail.setError("Vui lòng nhập email");
            etRegisterEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etRegisterEmail.setError("Email không hợp lệ");
            etRegisterEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etRegisterPassword.setError("Vui lòng nhập mật khẩu");
            etRegisterPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etRegisterPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etRegisterPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        // Show progress
        showProgress(true);
        btnRegister.setEnabled(false);

        // Hash password
        final String hashedPassword = hashPassword(password);
        Log.d(TAG, "registerUser: Password hashed");

        // Insert into database in background
        executorService.execute(() -> {
            try {
                Log.d(TAG, "registerUser: Checking if email exists: " + email);
                
                // Check if email exists
                User existingUser = database.userDao().getUserByEmail(email);
                
                if (existingUser != null) {
                    Log.w(TAG, "registerUser: Email already exists: " + email);
                    runOnUiThread(() -> {
                        showProgress(false);
                        btnRegister.setEnabled(true);
                        Toast.makeText(LoginActivity.this, 
                                "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                Log.d(TAG, "registerUser: Creating new user");
                
                // Create new user
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setPassword(hashedPassword);
                newUser.setName(name);
                newUser.setLoginType("email");
                
                // Insert user
                long userId = database.userDao().insertUser(newUser);
                newUser.setId((int) userId);
                
                Log.d(TAG, "registerUser: User created successfully with ID: " + userId);

                runOnUiThread(() -> {
                    showProgress(false);
                    btnRegister.setEnabled(true);
                    
                    // Save user session
                    saveUserSession(newUser);
                    
                    // Sync new user to cloud
                    cloudSyncManager.syncUserToCloud(newUser, new CloudSyncManager.CloudSyncCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Log.d(TAG, "New user synced to cloud: " + message);
                        }
                        
                        @Override
                        public void onError(String error) {
                            Log.w(TAG, "Failed to sync new user to cloud: " + error);
                        }
                    });
                    
                    Toast.makeText(LoginActivity.this, 
                            "Đăng ký thành công! Tài khoản đã được đồng bộ.", Toast.LENGTH_SHORT).show();
                    
                    Log.d(TAG, "registerUser: Navigating to MainActivity");
                    navigateToMain();
                });
                
            } catch (Exception e) {
                Log.e(TAG, "registerUser: Error during registration", e);
                runOnUiThread(() -> {
                    showProgress(false);
                    btnRegister.setEnabled(true);
                    Toast.makeText(LoginActivity.this, 
                            "Lỗi đăng ký: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void sendResetLink() {
        String email = etForgotEmail.getText().toString().trim();

        if (email.isEmpty()) {
            etForgotEmail.setError("Vui lòng nhập email");
            etForgotEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etForgotEmail.setError("Email không hợp lệ");
            etForgotEmail.requestFocus();
            return;
        }

        showProgress(true);
        btnSendReset.setEnabled(false);

        // Check if email exists
        executorService.execute(() -> {
            try {
                User user = database.userDao().getUserByEmail(email);
                
                runOnUiThread(() -> {
                    showProgress(false);
                    btnSendReset.setEnabled(true);
                    
                    if (user != null) {
                        Toast.makeText(this, 
                                "Link đặt lại mật khẩu đã được gửi đến " + email, 
                                Toast.LENGTH_LONG).show();
                        showLoginForm();
                    } else {
                        Toast.makeText(this, 
                                "Email không tồn tại trong hệ thống!", 
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "sendResetLink: Error", e);
                runOnUiThread(() -> {
                    showProgress(false);
                    btnSendReset.setEnabled(true);
                    Toast.makeText(this, 
                            "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void saveUserSession(User user) {
        try {
            Log.d(TAG, "saveUserSession: Saving session for user: " + user.getEmail());
            
            sharedPreferences.edit()
                    .putInt("user_id", user.getId())
                    .putString("username", user.getEmail())
                    .putString("email", user.getEmail())
                    .putString("full_name", user.getName() != null ? user.getName() : "")
                    .putBoolean("is_logged_in", true)
                    .apply();
            
            // Save account if remember me is enabled
            if (cbRememberMe != null && cbRememberMe.isChecked()) {
                accountManager.saveAccount(user);
            }
            
            Log.d(TAG, "saveUserSession: Session saved successfully");
        } catch (Exception e) {
            Log.e(TAG, "saveUserSession: Error saving session", e);
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            Log.e(TAG, "hashPassword: Error hashing password", e);
            return password; // Fallback (not recommended for production)
        }
    }

    private void showProgress(boolean show) {
        // Progress bar removed - using button enabled/disabled instead
        Log.d(TAG, "showProgress: " + show);
    }

    private void navigateToMain() {
        try {
            Log.d(TAG, "navigateToMain: Starting MainActivity");
            
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            
            Log.d(TAG, "navigateToMain: Navigation successful");
        } catch (Exception e) {
            Log.e(TAG, "navigateToMain: Error navigating to MainActivity", e);
            Toast.makeText(this, "Lỗi chuyển trang: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Cleaning up resources");
        
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
    
    private void navigateToAdminDashboard() {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    // Setup saved accounts functionality
    private void setupSavedAccounts() {
        // Initialize checkbox states
        if (cbRememberMe != null) {
            cbRememberMe.setChecked(accountManager.isRememberAccountsEnabled());
        }
        if (cbAutoLogin != null) {
            cbAutoLogin.setChecked(accountManager.isAutoLoginEnabled());
        }
        
        // Setup RecyclerView for saved accounts
        if (rvSavedAccounts != null) {
            rvSavedAccounts.setLayoutManager(new LinearLayoutManager(this));
            List<AccountManager.SavedAccount> savedAccounts = accountManager.getSavedAccounts();
            savedAccountAdapter = new SavedAccountAdapter(this, savedAccounts);
            rvSavedAccounts.setAdapter(savedAccountAdapter);
            
            savedAccountAdapter.setOnAccountClickListener(new SavedAccountAdapter.OnAccountClickListener() {
                @Override
                public void onAccountClick(AccountManager.SavedAccount account) {
                    // Fill email field and focus on password
                    etEmail.setText(account.email);
                    etPassword.requestFocus();
                }
                
                @Override
                public void onAccountRemove(AccountManager.SavedAccount account) {
                    showRemoveAccountDialog(account);
                }
            });
        }
        
        updateSavedAccountsVisibility();
        
        // Pre-fill last login email
        String lastEmail = accountManager.getLastLoginEmail();
        if (!lastEmail.isEmpty() && etEmail != null) {
            etEmail.setText(lastEmail);
        }
    }
    
    private void updateSavedAccountsVisibility() {
        List<AccountManager.SavedAccount> savedAccounts = accountManager.getSavedAccounts();
        boolean hasAccounts = !savedAccounts.isEmpty() && accountManager.isRememberAccountsEnabled();
        
        if (tvSavedAccounts != null) {
            tvSavedAccounts.setVisibility(hasAccounts ? View.VISIBLE : View.GONE);
        }
        if (rvSavedAccounts != null) {
            rvSavedAccounts.setVisibility(hasAccounts ? View.VISIBLE : View.GONE);
        }
        
        if (savedAccountAdapter != null) {
            savedAccountAdapter.updateAccounts(savedAccounts);
        }
    }
    
    private void showRemoveAccountDialog(AccountManager.SavedAccount account) {
        new AlertDialog.Builder(this)
            .setTitle("Xóa tài khoản đã lưu")
            .setMessage("Bạn có muốn xóa tài khoản " + account.email + " khỏi danh sách đã lưu?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                accountManager.removeSavedAccount(account.email);
                updateSavedAccountsVisibility();
                Toast.makeText(this, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void attemptAutoLogin() {
        accountManager.attemptAutoLogin(new AccountManager.AutoLoginCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Auto login successful for: " + user.getEmail());
                    saveUserSession(user);
                    Toast.makeText(LoginActivity.this, 
                            "Đăng nhập tự động thành công!", Toast.LENGTH_SHORT).show();
                    navigateToMain();
                });
            }
            
            @Override
            public void onError(String error) {
                Log.w(TAG, "Auto login failed: " + error);
                // Don't show error to user, just continue normally
            }
            
            @Override
            public void onSkip(String reason) {
                Log.d(TAG, "Auto login skipped: " + reason);
                // Continue normally
            }
        });
    }
    
    private void showSyncInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_sync_info, null);
        builder.setView(dialogView);
        
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        
        // Setup dialog views
        TextView tvSyncStatus = dialogView.findViewById(R.id.tv_sync_status);
        TextView tvDeviceId = dialogView.findViewById(R.id.tv_device_id);
        TextView tvLastSync = dialogView.findViewById(R.id.tv_last_sync);
        Button btnSyncNow = dialogView.findViewById(R.id.btn_sync_now);
        Button btnClose = dialogView.findViewById(R.id.btn_close_sync);
        
        // Set sync info
        tvSyncStatus.setText("Trạng thái: " + (cloudSyncManager.isSyncEnabled() ? "Đã bật" : "Đã tắt"));
        tvDeviceId.setText("Thiết bị: " + cloudSyncManager.getDeviceId().substring(0, Math.min(20, cloudSyncManager.getDeviceId().length())) + "...");
        tvLastSync.setText("Lần cuối: " + cloudSyncManager.getSyncStatusInfo());
        
        // Button listeners
        btnSyncNow.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                cloudSyncManager.syncUserFromCloud(email, new CloudSyncManager.CloudSyncCallback() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }
                    
                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            } else {
                Toast.makeText(this, "Vui lòng nhập email trước", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnClose.setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }
}