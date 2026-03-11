package com.htdgym.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.MainActivity;
import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.User;

import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple Login Activity - Minimal version for testing
 * Chỉ có đăng ký và đăng nhập, không có tabs
 */
public class SimpleLoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etName;
    private Button btnLogin, btnRegister;
    private GymDatabase database;
    private ExecutorService executor;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_login);

        // Initialize
        database = GymDatabase.getInstance(this);
        executor = Executors.newSingleThreadExecutor();
        prefs = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);

        // Check if logged in
        if (prefs.getBoolean("is_logged_in", false)) {
            goToMain();
            return;
        }

        // Find views
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);

        // Register button
        btnRegister.setOnClickListener(v -> register());

        // Login button
        btnLogin.setOnClickListener(v -> login());
    }

    private void register() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Validation
        if (name.isEmpty()) {
            Toast.makeText(this, "Nhập họ tên", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "Mật khẩu tối thiểu 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);
        Toast.makeText(this, "Đang đăng ký...", Toast.LENGTH_SHORT).show();

        // Register in background
        executor.execute(() -> {
            try {
                // Check email exists
                User existing = database.userDao().getUserByEmail(email);
                if (existing != null) {
                    runOnUiThread(() -> {
                        btnRegister.setEnabled(true);
                        Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                // Create user
                User user = new User();
                user.setEmail(email);
                user.setPassword(hashPassword(password));
                user.setName(name);
                user.setLoginType("email");

                // Insert
                long id = database.userDao().insertUser(user);
                user.setId((int) id);

                // Save session
                prefs.edit()
                        .putInt("user_id", user.getId())
                        .putString("email", user.getEmail())
                        .putString("full_name", user.getName())
                        .putBoolean("is_logged_in", true)
                        .apply();

                // Success
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    goToMain();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    btnRegister.setEnabled(true);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);
        Toast.makeText(this, "Đang đăng nhập...", Toast.LENGTH_SHORT).show();

        executor.execute(() -> {
            try {
                User user = database.userDao().login(email, hashPassword(password));

                if (user != null) {
                    // Save session
                    prefs.edit()
                            .putInt("user_id", user.getId())
                            .putString("email", user.getEmail())
                            .putString("full_name", user.getName())
                            .putBoolean("is_logged_in", true)
                            .apply();

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        goToMain();
                    });
                } else {
                    runOnUiThread(() -> {
                        btnLogin.setEnabled(true);
                        Toast.makeText(this, "Email hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception e) {
            return password;
        }
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}
