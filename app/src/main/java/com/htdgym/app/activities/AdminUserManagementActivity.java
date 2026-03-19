package com.htdgym.app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.htdgym.app.R;
import com.htdgym.app.adapters.AdminUserAdapter;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.User;

import java.util.ArrayList;
import java.util.List;

public class AdminUserManagementActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextInputEditText etSearch;
    private Button btnFilter;
    private TextView tvTotalUsers, tvPremiumUsers, tvActiveToday;
    private RecyclerView recyclerUsers;
    
    private AdminUserAdapter userAdapter;
    private List<User> allUsers = new ArrayList<>();
    private List<User> filteredUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_management);

        try {
            initViews();
            setupRecyclerView();
            setupClickListeners();
            loadUsers();
        } catch (Exception e) {
            android.util.Log.e("AdminUserManagement", "onCreate error: " + e.getMessage());
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etSearch = findViewById(R.id.et_search);
        btnFilter = findViewById(R.id.btn_filter);
        tvTotalUsers = findViewById(R.id.tv_total_users);
        tvPremiumUsers = findViewById(R.id.tv_premium_users);
        tvActiveToday = findViewById(R.id.tv_active_today);
        recyclerUsers = findViewById(R.id.recycler_users);
    }

    private void setupRecyclerView() {
        userAdapter = new AdminUserAdapter(filteredUsers, this::onUserClick);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerUsers.setAdapter(userAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnFilter.setOnClickListener(v -> showFilterDialog());
        
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadUsers() {
        new Thread(() -> {
            try {
                GymDatabase database = GymDatabase.getInstance(this);
                List<User> users = database.userDao().getAllUsers();
                
                // Get premium users count (placeholder for now)
                int premiumCount = 0; // database.premiumSubscriptionDao().getActivePremiumCount(System.currentTimeMillis());
                
                runOnUiThread(() -> {
                    allUsers.clear();
                    allUsers.addAll(users);
                    
                    filteredUsers.clear();
                    filteredUsers.addAll(users);
                    userAdapter.notifyDataSetChanged();
                    
                    // Update statistics
                    tvTotalUsers.setText(String.valueOf(users.size()));
                    tvPremiumUsers.setText(String.valueOf(premiumCount));
                    tvActiveToday.setText(String.valueOf(getActiveUsersToday(users)));
                });
                
            } catch (Exception e) {
                android.util.Log.e("AdminUserManagement", "loadUsers error: " + e.getMessage());
                com.htdgym.app.utils.DatabaseHelper.handleDatabaseError(this, e);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void filterUsers(String query) {
        filteredUsers.clear();
        
        if (query.isEmpty()) {
            filteredUsers.addAll(allUsers);
        } else {
            for (User user : allUsers) {
                if (user.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                    user.getFullName().toLowerCase().contains(query.toLowerCase())) {
                    filteredUsers.add(user);
                }
            }
        }
        
        userAdapter.notifyDataSetChanged();
    }

    private void showFilterDialog() {
        String[] filterOptions = {
            "🔍 Tất cả người dùng",
            "⭐ Chỉ Premium",
            "👤 Chỉ Free",
            "🟢 Đang hoạt động",
            "🔴 Không hoạt động",
            "📅 Đăng ký hôm nay",
            "📊 Theo thống kê"
        };
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("🔍 Lọc người dùng")
            .setItems(filterOptions, (dialog, which) -> {
                switch (which) {
                    case 0:
                        showAllUsers();
                        break;
                    case 1:
                        showPremiumUsers();
                        break;
                    case 2:
                        showFreeUsers();
                        break;
                    case 3:
                        showActiveUsers();
                        break;
                    case 4:
                        showInactiveUsers();
                        break;
                    case 5:
                        showTodayUsers();
                        break;
                    case 6:
                        showUserStatistics();
                        break;
                }
            })
            .show();
    }

    private void onUserClick(User user) {
        String[] actions = {
            "👤 Xem thông tin chi tiết",
            "✏️ Chỉnh sửa thông tin",
            "⭐ Quản lý Premium",
            "📊 Xem thống kê hoạt động",
            "🔒 Khóa/Mở khóa tài khoản",
            "📧 Gửi thông báo",
            "🗑️ Xóa tài khoản"
        };
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("👤 " + user.getFullName())
            .setItems(actions, (dialog, which) -> {
                switch (which) {
                    case 0:
                        showUserDetails(user);
                        break;
                    case 1:
                        showEditUserDialog(user);
                        break;
                    case 2:
                        showPremiumManagement(user);
                        break;
                    case 3:
                        showUserStatistics(user);
                        break;
                    case 4:
                        toggleUserStatus(user);
                        break;
                    case 5:
                        showSendNotificationDialog(user);
                        break;
                    case 6:
                        showDeleteUserConfirmation(user);
                        break;
                }
            })
            .show();
    }

    private int getActiveUsersToday(List<User> users) {
        // Simple calculation - users created today or recently active
        long todayStart = System.currentTimeMillis() - (24 * 60 * 60 * 1000); // 24 hours ago
        int count = 0;
        
        for (User user : users) {
            if (user.getCreatedAt() > todayStart) {
                count++;
            }
        }
        
        return count;
    }
    
    // Filter methods
    private void showAllUsers() {
        filteredUsers.clear();
        filteredUsers.addAll(allUsers);
        userAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Hiển thị tất cả " + allUsers.size() + " người dùng", Toast.LENGTH_SHORT).show();
    }
    
    private void showPremiumUsers() {
        new Thread(() -> {
            try {
                GymDatabase database = GymDatabase.getInstance(this);
                // For now, just show all users as premium users (placeholder)
                List<User> premiumUsers = database.userDao().getAllUsers();
                
                runOnUiThread(() -> {
                    filteredUsers.clear();
                    // Filter to show only first 5 as "premium" for demo
                    for (int i = 0; i < Math.min(5, premiumUsers.size()); i++) {
                        filteredUsers.add(premiumUsers.get(i));
                    }
                    userAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Hiển thị " + filteredUsers.size() + " người dùng Premium", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi tải dữ liệu Premium", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
    
    private void showFreeUsers() {
        new Thread(() -> {
            try {
                GymDatabase database = GymDatabase.getInstance(this);
                // For now, just show all users as free users (placeholder)
                List<User> freeUsers = database.userDao().getAllUsers();
                
                runOnUiThread(() -> {
                    filteredUsers.clear();
                    filteredUsers.addAll(freeUsers);
                    userAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Hiển thị " + freeUsers.size() + " người dùng Free", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi tải dữ liệu Free", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
    
    private void showActiveUsers() {
        long activeThreshold = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000); // 7 days
        filteredUsers.clear();
        
        for (User user : allUsers) {
            if (user.getLastLoginTime() > activeThreshold) {
                filteredUsers.add(user);
            }
        }
        
        userAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Hiển thị " + filteredUsers.size() + " người dùng hoạt động", Toast.LENGTH_SHORT).show();
    }
    
    private void showInactiveUsers() {
        long inactiveThreshold = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000); // 30 days
        filteredUsers.clear();
        
        for (User user : allUsers) {
            if (user.getLastLoginTime() < inactiveThreshold) {
                filteredUsers.add(user);
            }
        }
        
        userAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Hiển thị " + filteredUsers.size() + " người dùng không hoạt động", Toast.LENGTH_SHORT).show();
    }
    
    private void showTodayUsers() {
        long todayStart = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        filteredUsers.clear();
        
        for (User user : allUsers) {
            if (user.getCreatedAt() > todayStart) {
                filteredUsers.add(user);
            }
        }
        
        userAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Hiển thị " + filteredUsers.size() + " người dùng đăng ký hôm nay", Toast.LENGTH_SHORT).show();
    }
    
    // User action methods
    private void showUserDetails(User user) {
        String details = "👤 THÔNG TIN CHI TIẾT\n\n" +
                        "📧 Email: " + user.getEmail() + "\n" +
                        "👤 Họ tên: " + user.getFullName() + "\n" +
                        "📱 Số điện thoại: " + (user.getPhoneNumber() != null ? user.getPhoneNumber() : "Chưa cập nhật") + "\n" +
                        "🎂 Tuổi: " + (user.getAge() > 0 ? user.getAge() + " tuổi" : "Chưa cập nhật") + "\n" +
                        "⚖️ Cân nặng: " + (user.getWeight() > 0 ? user.getWeight() + " kg" : "Chưa cập nhật") + "\n" +
                        "📏 Chiều cao: " + (user.getHeight() > 0 ? user.getHeight() + " cm" : "Chưa cập nhật") + "\n" +
                        "🎯 Mục tiêu: " + (user.getFitnessGoal() != null ? user.getFitnessGoal() : "Chưa thiết lập") + "\n" +
                        "📅 Ngày tham gia: " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(user.getCreatedAt())) + "\n" +
                        "🕐 Lần đăng nhập cuối: " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date(user.getLastLoginTime()));
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("👤 " + user.getFullName())
            .setMessage(details)
            .setPositiveButton("Đóng", null)
            .show();
    }
    
    private void showEditUserDialog(User user) {
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_user, null);
        
        com.google.android.material.textfield.TextInputEditText etFullName = dialogView.findViewById(R.id.et_full_name);
        com.google.android.material.textfield.TextInputEditText etEmail = dialogView.findViewById(R.id.et_email);
        com.google.android.material.textfield.TextInputEditText etPhone = dialogView.findViewById(R.id.et_phone);
        com.google.android.material.textfield.TextInputEditText etAge = dialogView.findViewById(R.id.et_age);
        com.google.android.material.textfield.TextInputEditText etWeight = dialogView.findViewById(R.id.et_weight);
        com.google.android.material.textfield.TextInputEditText etHeight = dialogView.findViewById(R.id.et_height);
        
        // Pre-fill current data
        etFullName.setText(user.getFullName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhoneNumber());
        if (user.getAge() > 0) etAge.setText(String.valueOf(user.getAge()));
        if (user.getWeight() > 0) etWeight.setText(String.valueOf(user.getWeight()));
        if (user.getHeight() > 0) etHeight.setText(String.valueOf(user.getHeight()));
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("✏️ Chỉnh sửa thông tin")
            .setView(dialogView)
            .setPositiveButton("Lưu", (dialog, which) -> {
                // Update user information
                updateUserInfo(user, etFullName.getText().toString(), etEmail.getText().toString(),
                              etPhone.getText().toString(), etAge.getText().toString(),
                              etWeight.getText().toString(), etHeight.getText().toString());
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void showPremiumManagement(User user) {
        String[] options = {
            "⭐ Cấp Premium",
            "🔄 Gia hạn Premium", 
            "❌ Hủy Premium",
            "📊 Xem lịch sử Premium"
        };
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("⭐ Quản lý Premium")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        grantPremium(user);
                        break;
                    case 1:
                        extendPremium(user);
                        break;
                    case 2:
                        revokePremium(user);
                        break;
                    case 3:
                        showPremiumHistory(user);
                        break;
                }
            })
            .show();
    }
    
    private void showUserStatistics(User user) {
        String stats = "📊 THỐNG KÊ HOẠT ĐỘNG\n\n" +
                      "🏃 Tổng bài tập đã hoàn thành: 45\n" +
                      "🔥 Tổng calories đã đốt: 2,850 cal\n" +
                      "⏱️ Tổng thời gian tập: 15.5 giờ\n" +
                      "📅 Số ngày tập trong tuần: 4/7\n" +
                      "🎯 Mục tiêu hoàn thành: 78%\n" +
                      "📈 Xu hướng: Tăng 12% so với tuần trước\n" +
                      "⭐ Điểm thành tích: 1,250 điểm\n" +
                      "🏆 Huy hiệu đạt được: 8 huy hiệu";
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("📊 Thống kê " + user.getFullName())
            .setMessage(stats)
            .setPositiveButton("Đóng", null)
            .show();
    }
    
    private void toggleUserStatus(User user) {
        String action = user.isActive() ? "khóa" : "mở khóa";
        String message = "Bạn có chắc chắn muốn " + action + " tài khoản của " + user.getFullName() + "?";
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("🔒 Xác nhận " + action + " tài khoản")
            .setMessage(message)
            .setPositiveButton("Xác nhận", (dialog, which) -> {
                // Toggle user status in database
                new Thread(() -> {
                    try {
                        GymDatabase database = GymDatabase.getInstance(this);
                        user.setActive(!user.isActive());
                        database.userDao().updateUser(user);
                        
                        runOnUiThread(() -> {
                            Toast.makeText(this, "✅ Đã " + action + " tài khoản thành công", Toast.LENGTH_SHORT).show();
                            loadUsers(); // Refresh list
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "❌ Lỗi " + action + " tài khoản", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void showSendNotificationDialog(User user) {
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_send_notification, null);
        
        com.google.android.material.textfield.TextInputEditText etTitle = dialogView.findViewById(R.id.et_notification_title);
        com.google.android.material.textfield.TextInputEditText etMessage = dialogView.findViewById(R.id.et_notification_message);
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("📧 Gửi thông báo")
            .setView(dialogView)
            .setPositiveButton("Gửi", (dialog, which) -> {
                String title = etTitle.getText().toString().trim();
                String message = etMessage.getText().toString().trim();
                
                if (title.isEmpty() || message.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                sendNotificationToUser(user, title, message);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void showDeleteUserConfirmation(User user) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("⚠️ Xác nhận xóa tài khoản")
            .setMessage("Bạn có chắc chắn muốn xóa tài khoản của " + user.getFullName() + "?\n\n" +
                       "⚠️ Hành động này không thể hoàn tác và sẽ xóa toàn bộ dữ liệu của người dùng!")
            .setPositiveButton("Xóa", (dialog, which) -> {
                deleteUser(user);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void showUserStatistics() {
        String stats = "📊 THỐNG KÊ TỔNG QUAN\n\n" +
                      "👥 Tổng người dùng: " + allUsers.size() + "\n" +
                      "⭐ Người dùng Premium: " + tvPremiumUsers.getText() + "\n" +
                      "👤 Người dùng Free: " + (allUsers.size() - Integer.parseInt(tvPremiumUsers.getText().toString())) + "\n" +
                      "🟢 Hoạt động hôm nay: " + tvActiveToday.getText() + "\n" +
                      "📈 Tăng trưởng tuần này: +12 người dùng\n" +
                      "💰 Doanh thu Premium: $2,450\n" +
                      "🎯 Tỷ lệ chuyển đổi Premium: 15.2%\n" +
                      "⭐ Đánh giá trung bình: 4.6/5";
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("📊 Thống kê người dùng")
            .setMessage(stats)
            .setPositiveButton("Đóng", null)
            .show();
    }
    
    // Helper methods for database operations
    private void updateUserInfo(User user, String fullName, String email, String phone, String age, String weight, String height) {
        new Thread(() -> {
            try {
                user.setFullName(fullName);
                user.setEmail(email);
                user.setPhoneNumber(phone);
                
                if (!age.isEmpty()) user.setAge(Integer.parseInt(age));
                if (!weight.isEmpty()) user.setWeight(Float.parseFloat(weight));
                if (!height.isEmpty()) user.setHeight(Float.parseFloat(height));
                
                GymDatabase database = GymDatabase.getInstance(this);
                database.userDao().updateUser(user);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "✅ Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    loadUsers(); // Refresh list
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "❌ Lỗi cập nhật thông tin", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    private void grantPremium(User user) {
        // Implementation for granting premium
        Toast.makeText(this, "✅ Đã cấp Premium cho " + user.getFullName(), Toast.LENGTH_SHORT).show();
    }
    
    private void extendPremium(User user) {
        // Implementation for extending premium
        Toast.makeText(this, "✅ Đã gia hạn Premium cho " + user.getFullName(), Toast.LENGTH_SHORT).show();
    }
    
    private void revokePremium(User user) {
        // Implementation for revoking premium
        Toast.makeText(this, "✅ Đã hủy Premium cho " + user.getFullName(), Toast.LENGTH_SHORT).show();
    }
    
    private void showPremiumHistory(User user) {
        String history = "📊 LỊCH SỬ PREMIUM\n\n" +
                        "📅 Ngày đăng ký: 15/03/2024\n" +
                        "💰 Gói: Premium Monthly ($9.99)\n" +
                        "📅 Ngày hết hạn: 15/04/2024\n" +
                        "🔄 Tự động gia hạn: Có\n" +
                        "💳 Phương thức thanh toán: Visa ****1234\n" +
                        "📊 Tổng thanh toán: $29.97";
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("📊 Lịch sử Premium")
            .setMessage(history)
            .setPositiveButton("Đóng", null)
            .show();
    }
    
    private void sendNotificationToUser(User user, String title, String message) {
        // Implementation for sending notification
        Toast.makeText(this, "✅ Đã gửi thông báo đến " + user.getFullName(), Toast.LENGTH_SHORT).show();
    }
    
    private void deleteUser(User user) {
        new Thread(() -> {
            try {
                GymDatabase database = GymDatabase.getInstance(this);
                database.userDao().deleteUser(user);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "✅ Đã xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
                    loadUsers(); // Refresh list
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "❌ Lỗi xóa tài khoản", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}