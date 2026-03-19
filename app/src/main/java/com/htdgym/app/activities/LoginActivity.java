package com.htdgym.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.MainActivity;
import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.Admin;
import com.htdgym.app.models.User;
import com.htdgym.app.utils.ValidationHelper;

import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    
    // Tabs
    private TextView tabLogin, tabRegister, tabForgot;
    private ImageView btnClose;
    
    // Forms
    private LinearLayout formLogin, formRegister, formForgot;
    
    // Login Form
    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogle, btnFacebook;
    
    // Register Form
    private EditText etRegisterName, etRegisterEmail, etRegisterPassword;
    private Button btnRegister;
    
    // Forgot Form
    private EditText etForgotEmail;
    private Button btnReset;
    
    private SharedPreferences sharedPreferences;
    private GymDatabase database;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            Log.d(TAG, "onCreate: Starting LoginActivity");
            setContentView(R.layout.activity_login);
            Log.d(TAG, "onCreate: Layout set successfully");

            // Initialize basic components first
            sharedPreferences = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);
            executorService = Executors.newSingleThreadExecutor();
            
            // Check if already logged in (simple check first)
            if (sharedPreferences.getBoolean("is_logged_in", false)) {
                Log.d(TAG, "onCreate: User already logged in, navigating to MainActivity");
                navigateToMain();
                return;
            }
            
            // Initialize database in background to avoid blocking UI
            executorService.execute(() -> {
                try {
                    Log.d(TAG, "onCreate: Initializing database...");
                    database = GymDatabase.getInstance(getApplicationContext());
                    Log.d(TAG, "onCreate: Database initialized successfully");
                } catch (Exception e) {
                    Log.e(TAG, "onCreate: Error initializing database", e);
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, 
                                "Lỗi khởi tạo database. Vui lòng thử lại.", 
                                Toast.LENGTH_LONG).show();
                    });
                }
            });

            // Initialize views
            initViews();
            setupClickListeners();
            showLoginForm(); // Default to login form
            
            Log.d(TAG, "onCreate: LoginActivity initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Critical error in LoginActivity", e);
            // Show a simple error message and close
            Toast.makeText(this, "Lỗi khởi tạo ứng dụng: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        try {
            // Tabs
            tabLogin = findViewById(R.id.tab_login);
            tabRegister = findViewById(R.id.tab_register);
            tabForgot = findViewById(R.id.tab_forgot);
            btnClose = findViewById(R.id.btn_close);
            
            // Forms
            formLogin = findViewById(R.id.form_login);
            formRegister = findViewById(R.id.form_register);
            formForgot = findViewById(R.id.form_forgot);
            
            // Login Form
            etEmail = findViewById(R.id.et_email);
            etPassword = findViewById(R.id.et_password);
            btnLogin = findViewById(R.id.btn_login);
            btnGoogle = findViewById(R.id.btn_google);
            btnFacebook = findViewById(R.id.btn_facebook);
            
            // Register Form
            etRegisterName = findViewById(R.id.et_register_name);
            etRegisterEmail = findViewById(R.id.et_register_email);
            etRegisterPassword = findViewById(R.id.et_register_password);
            btnRegister = findViewById(R.id.btn_register);
            
            // Forgot Form
            etForgotEmail = findViewById(R.id.et_forgot_email);
            btnReset = findViewById(R.id.btn_reset);
            
            Log.d(TAG, "initViews: All views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "initViews: Error initializing views", e);
            throw e; // Re-throw to be caught by onCreate
        }
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
        btnReset.setOnClickListener(v -> resetPassword());
        
        // Social buttons (UI only)
        btnGoogle.setOnClickListener(v -> 
            Toast.makeText(this, "Đăng nhập Google đang phát triển", Toast.LENGTH_SHORT).show());
        btnFacebook.setOnClickListener(v -> 
            Toast.makeText(this, "Đăng nhập Facebook đang phát triển", Toast.LENGTH_SHORT).show());
    }

    private void showLoginForm() {
        // Update tabs
        updateTabSelection(tabLogin, tabRegister, tabForgot);
        
        // Show/hide forms
        formLogin.setVisibility(View.VISIBLE);
        formRegister.setVisibility(View.GONE);
        formForgot.setVisibility(View.GONE);
    }

    private void showRegisterForm() {
        // Update tabs
        updateTabSelection(tabRegister, tabLogin, tabForgot);
        
        // Show/hide forms
        formLogin.setVisibility(View.GONE);
        formRegister.setVisibility(View.VISIBLE);
        formForgot.setVisibility(View.GONE);
    }

    private void showForgotForm() {
        // Update tabs
        updateTabSelection(tabForgot, tabLogin, tabRegister);
        
        // Show/hide forms
        formLogin.setVisibility(View.GONE);
        formRegister.setVisibility(View.GONE);
        formForgot.setVisibility(View.VISIBLE);
    }

    private void updateTabSelection(TextView selected, TextView... others) {
        selected.setBackground(getDrawable(R.drawable.tab_selected_bg));
        selected.setTextColor(0xFFFFFFFF);
        
        for (TextView tab : others) {
            tab.setBackground(getDrawable(R.drawable.tab_unselected_bg));
            tab.setTextColor(0xFF999999);
        }
    }

    private void loginUser() {
        Log.d(TAG, "loginUser: Starting login process");
        
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation using helper
        if (!ValidationHelper.validateEmail(etEmail) || 
            !ValidationHelper.validateRequired(etPassword, "mật khẩu")) {
            return;
        }

        // Show progress
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");

        // Check if database is ready
        if (database == null) {
            Log.w(TAG, "loginUser: Database not ready, initializing...");
            executorService.execute(() -> {
                try {
                    database = GymDatabase.getInstance(getApplicationContext());
                    runOnUiThread(() -> loginUser()); // Retry login
                } catch (Exception e) {
                    Log.e(TAG, "loginUser: Error initializing database", e);
                    runOnUiThread(() -> {
                        btnLogin.setEnabled(true);
                        btnLogin.setText("→ Đăng nhập");
                        Toast.makeText(LoginActivity.this, "Lỗi kết nối database", Toast.LENGTH_SHORT).show();
                    });
                }
            });
            return;
        }

        // Hash password
        final String hashedPassword = hashPassword(password);
        Log.d(TAG, "loginUser: Password hashed");

        // Query database in background
        executorService.execute(() -> {
            try {
                Log.d(TAG, "loginUser: Querying database for: " + email);
                
                // First check if it's an admin account
                Admin admin = database.adminDao().login(email, hashedPassword);
                
                if (admin != null) {
                    Log.d(TAG, "loginUser: Admin login successful for: " + email);
                    runOnUiThread(() -> {
                        btnLogin.setEnabled(true);
                        btnLogin.setText("→ Đăng nhập");
                        
                        // Save admin session using AdminManager
                        com.htdgym.app.utils.AdminManager.loginAdmin(LoginActivity.this, admin);
                        
                        Toast.makeText(LoginActivity.this, 
                                "Đăng nhập Admin thành công!", Toast.LENGTH_SHORT).show();
                        
                        // Navigate to Admin Dashboard
                        navigateToAdminDashboard();
                    });
                    return;
                }
                
                // If not admin, check regular user
                User user = database.userDao().login(email, hashedPassword);
                
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("→ Đăng nhập");
                    
                    if (user != null) {
                        Log.d(TAG, "loginUser: User login successful for: " + email);
                        saveUserSession(user);
                        Toast.makeText(LoginActivity.this, 
                                "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    } else {
                        Log.w(TAG, "loginUser: Login failed - invalid credentials");
                        Toast.makeText(LoginActivity.this, 
                                "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "loginUser: Error during login", e);
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("→ Đăng nhập");
                    Toast.makeText(LoginActivity.this, 
                            "Lỗi đăng nhập: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void registerUser() {
        Log.d(TAG, "registerUser: Starting registration process");
        
        String name = etRegisterName.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString();

        // Validation using helper
        if (!ValidationHelper.validateName(etRegisterName) ||
            !ValidationHelper.validateEmail(etRegisterEmail) ||
            !ValidationHelper.validatePassword(etRegisterPassword)) {
            return;
        }

        // Show progress
        btnRegister.setEnabled(false);
        btnRegister.setText("Đang đăng ký...");

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
                        btnRegister.setEnabled(true);
                        btnRegister.setText("→ Đăng ký");
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
                    btnRegister.setEnabled(true);
                    btnRegister.setText("→ Đăng ký");
                    
                    // Save user session
                    saveUserSession(newUser);
                    
                    Toast.makeText(LoginActivity.this, 
                            "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    
                    Log.d(TAG, "registerUser: Navigating to MainActivity");
                    navigateToMain();
                });
                
            } catch (Exception e) {
                Log.e(TAG, "registerUser: Error during registration", e);
                runOnUiThread(() -> {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("→ Đăng ký");
                    Toast.makeText(LoginActivity.this, 
                            "Lỗi đăng ký: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void resetPassword() {
        String email = etForgotEmail.getText().toString().trim();

        // Validation using helper
        if (!ValidationHelper.validateEmail(etForgotEmail)) {
            return;
        }

        btnReset.setEnabled(false);
        btnReset.setText("Đang gửi...");

        // Check if email exists
        executorService.execute(() -> {
            try {
                User user = database.userDao().getUserByEmail(email);
                
                runOnUiThread(() -> {
                    btnReset.setEnabled(true);
                    btnReset.setText("→ Gửi link đặt lại");
                    
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
                Log.e(TAG, "resetPassword: Error", e);
                runOnUiThread(() -> {
                    btnReset.setEnabled(true);
                    btnReset.setText("→ Gửi link đặt lại");
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
    
    private void navigateToAdminDashboard() {
        try {
            Log.d(TAG, "navigateToAdminDashboard: Starting AdminDashboardActivity");
            
            Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            
            Log.d(TAG, "navigateToAdminDashboard: Navigation successful");
        } catch (Exception e) {
            Log.e(TAG, "navigateToAdminDashboard: Error navigating to AdminDashboard", e);
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
}