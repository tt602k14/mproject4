package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.MainActivity;
import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;

public class DiagnosticActivity extends AppCompatActivity {

    private static final String TAG = "DiagnosticActivity";
    private TextView tvStatus;
    private Button btnTestDatabase, btnTestMainActivity, btnTestLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            // Create a simple layout programmatically
            android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(50, 50, 50, 50);
            layout.setBackgroundColor(0xFF121212);
            
            TextView title = new TextView(this);
            title.setText("HTD Gym - Chẩn đoán");
            title.setTextSize(24);
            title.setTextColor(0xFFFFFFFF);
            title.setPadding(0, 0, 0, 30);
            layout.addView(title);
            
            tvStatus = new TextView(this);
            tvStatus.setText("Sẵn sàng kiểm tra...");
            tvStatus.setTextColor(0xFFFFFFFF);
            tvStatus.setPadding(0, 0, 0, 30);
            layout.addView(tvStatus);
            
            btnTestDatabase = new Button(this);
            btnTestDatabase.setText("Kiểm tra Database");
            btnTestDatabase.setOnClickListener(v -> testDatabase());
            layout.addView(btnTestDatabase);
            
            btnTestMainActivity = new Button(this);
            btnTestMainActivity.setText("Kiểm tra MainActivity");
            btnTestMainActivity.setOnClickListener(v -> testMainActivity());
            layout.addView(btnTestMainActivity);
            
            btnTestLogin = new Button(this);
            btnTestLogin.setText("Mở LoginActivity");
            btnTestLogin.setOnClickListener(v -> openLoginActivity());
            layout.addView(btnTestLogin);
            
            setContentView(layout);
            
            Log.d(TAG, "DiagnosticActivity created successfully");
            tvStatus.setText("DiagnosticActivity khởi tạo thành công!");
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating DiagnosticActivity", e);
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void testDatabase() {
        tvStatus.setText("Đang kiểm tra database...");
        
        new Thread(() -> {
            try {
                Log.d(TAG, "Testing database initialization");
                GymDatabase database = GymDatabase.getInstance(getApplicationContext());
                
                runOnUiThread(() -> {
                    tvStatus.setText("✅ Database khởi tạo thành công!");
                    Toast.makeText(this, "Database hoạt động bình thường", Toast.LENGTH_SHORT).show();
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Database test failed", e);
                runOnUiThread(() -> {
                    tvStatus.setText("❌ Lỗi database: " + e.getMessage());
                    Toast.makeText(this, "Lỗi database: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
    
    private void testMainActivity() {
        tvStatus.setText("Đang kiểm tra MainActivity...");
        
        try {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            tvStatus.setText("✅ MainActivity mở thành công!");
        } catch (Exception e) {
            Log.e(TAG, "MainActivity test failed", e);
            tvStatus.setText("❌ Lỗi MainActivity: " + e.getMessage());
            Toast.makeText(this, "Lỗi MainActivity: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void openLoginActivity() {
        tvStatus.setText("Đang mở LoginActivity...");
        
        try {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            tvStatus.setText("✅ LoginActivity mở thành công!");
        } catch (Exception e) {
            Log.e(TAG, "LoginActivity test failed", e);
            tvStatus.setText("❌ Lỗi LoginActivity: " + e.getMessage());
            Toast.makeText(this, "Lỗi LoginActivity: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}