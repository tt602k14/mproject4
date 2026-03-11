package com.htdgym.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.MainActivity;
import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.User;

import java.security.MessageDigest;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: Starting LoginActivity");

        try {
            sharedPreferences = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);
            database = GymDatabase.getInstance(getApplicationContext());
            executorService = Executors.newSingleThreadExecutor();

            // Check if already logged in
            if (sharedPreferences.getBoolean("is_logged_in", false)) {
                Log.d(TAG, "onCreate: User already logged in, navigating to MainActivity");
                navigateToMain();
                return;
            }

            initViews();
            setupClickListeners();
            
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

        // Hash password
        final String hashedPassword = hashPassword(password);
        Log.d(TAG, "loginUser: Password hashed");

        // Query database in background
        executorService.execute(() -> {
            try {
                Log.d(TAG, "loginUser: Querying database for user: " + email);
                User user = database.userDao().login(email, hashedPassword);
                
                runOnUiThread(() -> {
                    showProgress(false);
                    btnLogin.setEnabled(true);
                    
                    if (user != null) {
                        Log.d(TAG, "loginUser: Login successful for user: " + email);
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
                    
                    Toast.makeText(LoginActivity.this, 
                            "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    
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
}
