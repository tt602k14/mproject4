package com.htdgym.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.htdgym.app.MainActivity;
import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.User;

import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.htdgym.app.activities.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";
    
    private TextInputEditText etFullName, etUsername, etEmail, etPhone, etPassword, etConfirmPassword;
    private CardView btnRegister;
    private TextView tvLogin;
    private SharedPreferences sharedPreferences;
    private GymDatabase database;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.d(TAG, "onCreate: Starting RegisterActivity");

        sharedPreferences = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);
        database = GymDatabase.getInstance(getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();

        initViews();
        setupClickListeners();
        
        Log.d(TAG, "onCreate: RegisterActivity initialized successfully");
    }

    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> register());
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void register() {
        Log.d(TAG, "register: Starting registration process");
        
        String fullName = etFullName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Validation
        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return;
        }

        if (username.length() < 4) {
            etUsername.setError("Tên đăng nhập phải có ít nhất 4 ký tự");
            etUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
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

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        // Disable button
        btnRegister.setEnabled(false);

        // Hash password
        final String hashedPassword = hashPassword(password);
        Log.d(TAG, "register: Password hashed");

        // Insert into database in background
        executorService.execute(() -> {
            try {
                Log.d(TAG, "register: Checking if email exists: " + email);
                
                // Check if email exists
                User existingUser = database.userDao().getUserByEmail(email);
                
                if (existingUser != null) {
                    Log.w(TAG, "register: Email already exists: " + email);
                    runOnUiThread(() -> {
                        btnRegister.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, 
                                "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                Log.d(TAG, "register: Creating new user");
                
                // Create new user
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setPassword(hashedPassword);
                newUser.setName(fullName);
                newUser.setPhone(phone);
                newUser.setLoginType("email");
                
                // Insert user
                long userId = database.userDao().insertUser(newUser);
                newUser.setId((int) userId);
                
                Log.d(TAG, "register: User created successfully with ID: " + userId);

                runOnUiThread(() -> {
                    btnRegister.setEnabled(true);
                    
                    // Save user session
                    sharedPreferences.edit()
                            .putInt("user_id", newUser.getId())
                            .putString("username", username)
                            .putString("email", email)
                            .putString("full_name", fullName)
                            .putString("phone", phone)
                            .putBoolean("is_logged_in", true)
                            .apply();
                    
                    Toast.makeText(RegisterActivity.this, 
                            "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // Navigate to MainActivity
                    Log.d(TAG, "register: Navigating to MainActivity");
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
                
            } catch (Exception e) {
                Log.e(TAG, "register: Error during registration", e);
                runOnUiThread(() -> {
                    btnRegister.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, 
                            "Lỗi đăng ký: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Cleaning up resources");
        
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
