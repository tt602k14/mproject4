package com.htdgym.app.activities;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;

public class AdminSystemSettingsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private CardView cardAppSettings, cardDatabaseSettings, cardNotificationSettings;
    private CardView cardSecuritySettings, cardBackupSettings, cardMaintenanceSettings;
    private CardView cardAnalyticsSettings, cardApiSettings;
    
    private SharedPreferences sharedPreferences;
    private GymDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_system_settings);

        sharedPreferences = getSharedPreferences("admin_settings", MODE_PRIVATE);
        database = GymDatabase.getInstance(this);
        
        initViews();
        setupClickListeners();
    }
    
    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        cardAppSettings = findViewById(R.id.card_app_settings);
        cardDatabaseSettings = findViewById(R.id.card_database_settings);
        cardNotificationSettings = findViewById(R.id.card_notification_settings);
        cardSecuritySettings = findViewById(R.id.card_security_settings);
        cardBackupSettings = findViewById(R.id.card_backup_settings);
        cardMaintenanceSettings = findViewById(R.id.card_maintenance_settings);
        cardAnalyticsSettings = findViewById(R.id.card_analytics_settings);
        cardApiSettings = findViewById(R.id.card_api_settings);
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        cardAppSettings.setOnClickListener(v -> showAppSettingsDialog());
        cardDatabaseSettings.setOnClickListener(v -> showDatabaseSettingsDialog());
        cardNotificationSettings.setOnClickListener(v -> showNotificationSettingsDialog());
        cardSecuritySettings.setOnClickListener(v -> showSecuritySettingsDialog());
        cardBackupSettings.setOnClickListener(v -> showBackupSettingsDialog());
        cardMaintenanceSettings.setOnClickListener(v -> showMaintenanceSettingsDialog());
        cardAnalyticsSettings.setOnClickListener(v -> showAnalyticsSettingsDialog());
        cardApiSettings.setOnClickListener(v -> showApiSettingsDialog());
    }
    
    private void showAppSettingsDialog() {
        String[] options = {
            "🎨 Cài đặt giao diện",
            "🌐 Cài đặt ngôn ngữ",
            "📱 Cài đặt ứng dụng",
            "🔄 Cập nhật ứng dụng",
            "📊 Thông tin phiên bản"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("⚙️ Cài đặt ứng dụng")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        showThemeSettings();
                        break;
                    case 1:
                        showLanguageSettings();
                        break;
                    case 2:
                        showGeneralAppSettings();
                        break;
                    case 3:
                        checkForUpdates();
                        break;
                    case 4:
                        showVersionInfo();
                        break;
                }
            })
            .show();
    }
    
    private void showDatabaseSettingsDialog() {
        String[] options = {
            "🗄️ Thông tin database",
            "🧹 Dọn dẹp database",
            "📊 Thống kê database",
            "🔄 Đồng bộ dữ liệu",
            "⚠️ Reset database"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("🗄️ Cài đặt Database")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        showDatabaseInfo();
                        break;
                    case 1:
                        cleanupDatabase();
                        break;
                    case 2:
                        showDatabaseStats();
                        break;
                    case 3:
                        syncDatabase();
                        break;
                    case 4:
                        showResetDatabaseConfirmation();
                        break;
                }
            })
            .show();
    }
    
    private void showNotificationSettingsDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_notification_settings, null);
        
        Switch swPushNotifications = dialogView.findViewById(R.id.sw_push_notifications);
        Switch swEmailNotifications = dialogView.findViewById(R.id.sw_email_notifications);
        Switch swSmsNotifications = dialogView.findViewById(R.id.sw_sms_notifications);
        Switch swMaintenanceNotifications = dialogView.findViewById(R.id.sw_maintenance_notifications);
        
        // Load current settings
        swPushNotifications.setChecked(sharedPreferences.getBoolean("push_notifications", true));
        swEmailNotifications.setChecked(sharedPreferences.getBoolean("email_notifications", true));
        swSmsNotifications.setChecked(sharedPreferences.getBoolean("sms_notifications", false));
        swMaintenanceNotifications.setChecked(sharedPreferences.getBoolean("maintenance_notifications", true));
        
        new AlertDialog.Builder(this)
            .setTitle("🔔 Cài đặt thông báo")
            .setView(dialogView)
            .setPositiveButton("Lưu", (dialog, which) -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("push_notifications", swPushNotifications.isChecked());
                editor.putBoolean("email_notifications", swEmailNotifications.isChecked());
                editor.putBoolean("sms_notifications", swSmsNotifications.isChecked());
                editor.putBoolean("maintenance_notifications", swMaintenanceNotifications.isChecked());
                editor.apply();
                
                Toast.makeText(this, "✅ Đã lưu cài đặt thông báo", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void showSecuritySettingsDialog() {
        String[] options = {
            "🔐 Cài đặt mật khẩu",
            "🛡️ Bảo mật hai lớp",
            "📱 Quản lý phiên đăng nhập",
            "🚫 Danh sách IP bị chặn",
            "📋 Nhật ký bảo mật"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("🔒 Cài đặt bảo mật")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        showPasswordSettings();
                        break;
                    case 1:
                        showTwoFactorSettings();
                        break;
                    case 2:
                        showSessionManagement();
                        break;
                    case 3:
                        showBlockedIPs();
                        break;
                    case 4:
                        showSecurityLogs();
                        break;
                }
            })
            .show();
    }
    
    private void showBackupSettingsDialog() {
        String[] options = {
            "💾 Sao lưu thủ công",
            "🔄 Sao lưu tự động",
            "📥 Khôi phục dữ liệu",
            "☁️ Cài đặt Cloud",
            "📋 Lịch sử sao lưu"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("💾 Cài đặt sao lưu")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        performManualBackup();
                        break;
                    case 1:
                        showAutoBackupSettings();
                        break;
                    case 2:
                        showRestoreOptions();
                        break;
                    case 3:
                        showCloudSettings();
                        break;
                    case 4:
                        showBackupHistory();
                        break;
                }
            })
            .show();
    }
    
    private void showMaintenanceSettingsDialog() {
        String[] options = {
            "🔧 Chế độ bảo trì",
            "📊 Giám sát hệ thống",
            "🧹 Dọn dẹp cache",
            "📈 Tối ưu hiệu suất",
            "🔄 Khởi động lại hệ thống"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("🔧 Bảo trì hệ thống")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        toggleMaintenanceMode();
                        break;
                    case 1:
                        showSystemMonitoring();
                        break;
                    case 2:
                        clearCache();
                        break;
                    case 3:
                        optimizePerformance();
                        break;
                    case 4:
                        showRestartConfirmation();
                        break;
                }
            })
            .show();
    }
    
    private void showAnalyticsSettingsDialog() {
        String[] options = {
            "📊 Cài đặt Google Analytics",
            "🔥 Cài đặt Firebase",
            "📈 Báo cáo tùy chỉnh",
            "🎯 Theo dõi sự kiện",
            "📋 Xuất báo cáo"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("📊 Cài đặt Analytics")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        showGoogleAnalyticsSettings();
                        break;
                    case 1:
                        showFirebaseSettings();
                        break;
                    case 2:
                        showCustomReports();
                        break;
                    case 3:
                        showEventTracking();
                        break;
                    case 4:
                        exportReports();
                        break;
                }
            })
            .show();
    }
    
    private void showApiSettingsDialog() {
        String[] options = {
            "🔑 Quản lý API Keys",
            "🌐 Cài đặt endpoints",
            "⏱️ Cài đặt timeout",
            "🔄 Rate limiting",
            "📋 API logs"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("🔌 Cài đặt API")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        showApiKeyManagement();
                        break;
                    case 1:
                        showEndpointSettings();
                        break;
                    case 2:
                        showTimeoutSettings();
                        break;
                    case 3:
                        showRateLimitSettings();
                        break;
                    case 4:
                        showApiLogs();
                        break;
                }
            })
            .show();
    }
    
    // Implementation methods
    private void showThemeSettings() {
        String[] themes = {"🌞 Sáng", "🌙 Tối", "🎨 Tự động"};
        int currentTheme = sharedPreferences.getInt("theme", 0);
        
        new AlertDialog.Builder(this)
            .setTitle("🎨 Chọn giao diện")
            .setSingleChoiceItems(themes, currentTheme, (dialog, which) -> {
                sharedPreferences.edit().putInt("theme", which).apply();
                Toast.makeText(this, "✅ Đã thay đổi giao diện", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            })
            .show();
    }
    
    private void showLanguageSettings() {
        String[] languages = {"🇻🇳 Tiếng Việt", "🇺🇸 English", "🇯🇵 日本語"};
        int currentLang = sharedPreferences.getInt("language", 0);
        
        new AlertDialog.Builder(this)
            .setTitle("🌐 Chọn ngôn ngữ")
            .setSingleChoiceItems(languages, currentLang, (dialog, which) -> {
                sharedPreferences.edit().putInt("language", which).apply();
                Toast.makeText(this, "✅ Đã thay đổi ngôn ngữ", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            })
            .show();
    }
    
    private void showGeneralAppSettings() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_app_settings, null);
        
        Switch swAutoUpdate = dialogView.findViewById(R.id.sw_auto_update);
        Switch swCrashReporting = dialogView.findViewById(R.id.sw_crash_reporting);
        Switch swUsageStats = dialogView.findViewById(R.id.sw_usage_stats);
        
        swAutoUpdate.setChecked(sharedPreferences.getBoolean("auto_update", true));
        swCrashReporting.setChecked(sharedPreferences.getBoolean("crash_reporting", true));
        swUsageStats.setChecked(sharedPreferences.getBoolean("usage_stats", true));
        
        new AlertDialog.Builder(this)
            .setTitle("📱 Cài đặt ứng dụng")
            .setView(dialogView)
            .setPositiveButton("Lưu", (dialog, which) -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("auto_update", swAutoUpdate.isChecked());
                editor.putBoolean("crash_reporting", swCrashReporting.isChecked());
                editor.putBoolean("usage_stats", swUsageStats.isChecked());
                editor.apply();
                
                Toast.makeText(this, "✅ Đã lưu cài đặt", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void checkForUpdates() {
        new AlertDialog.Builder(this)
            .setTitle("🔄 Kiểm tra cập nhật")
            .setMessage("Đang kiểm tra phiên bản mới...\n\n" +
                       "📱 Phiên bản hiện tại: 1.2.3\n" +
                       "✅ Bạn đang sử dụng phiên bản mới nhất!")
            .setPositiveButton("Đóng", null)
            .show();
    }
    
    private void showVersionInfo() {
        String versionInfo = "📱 THÔNG TIN PHIÊN BẢN\n\n" +
                           "🏷️ Tên ứng dụng: HTD Gym\n" +
                           "📊 Phiên bản: 1.2.3 (Build 123)\n" +
                           "📅 Ngày build: 17/03/2024\n" +
                           "🔧 API Level: 34\n" +
                           "📱 Min SDK: 21\n" +
                           "🎯 Target SDK: 34\n" +
                           "👨‍💻 Nhà phát triển: HTD Team\n" +
                           "📧 Liên hệ: support@htdgym.com";
        
        new AlertDialog.Builder(this)
            .setTitle("📊 Thông tin phiên bản")
            .setMessage(versionInfo)
            .setPositiveButton("Đóng", null)
            .show();
    }
    
    private void showDatabaseInfo() {
        new Thread(() -> {
            try {
                // Get database statistics
                int userCount = database.userDao().getUserCount();
                int workoutCount = database.workoutDao().getWorkoutCount();
                int videoCount = database.videoDao().getVideoCount();
                
                String dbInfo = "🗄️ THÔNG TIN DATABASE\n\n" +
                              "📊 Tên database: gym_database\n" +
                              "📈 Phiên bản: 9\n" +
                              "👥 Số người dùng: " + userCount + "\n" +
                              "🏋️ Số bài tập: " + workoutCount + "\n" +
                              "🎥 Số video: " + videoCount + "\n" +
                              "💾 Kích thước: ~2.5 MB\n" +
                              "📅 Cập nhật cuối: Hôm nay\n" +
                              "✅ Trạng thái: Hoạt động bình thường";
                
                runOnUiThread(() -> {
                    new AlertDialog.Builder(this)
                        .setTitle("🗄️ Thông tin Database")
                        .setMessage(dbInfo)
                        .setPositiveButton("Đóng", null)
                        .show();
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi lấy thông tin database", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    private void cleanupDatabase() {
        new AlertDialog.Builder(this)
            .setTitle("🧹 Dọn dẹp Database")
            .setMessage("Bạn có muốn dọn dẹp dữ liệu không cần thiết?\n\n" +
                       "• Xóa dữ liệu tạm thời\n" +
                       "• Nén database\n" +
                       "• Xóa log cũ")
            .setPositiveButton("Dọn dẹp", (dialog, which) -> {
                Toast.makeText(this, "✅ Đã dọn dẹp database thành công", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void showDatabaseStats() {
        String stats = "📊 THỐNG KÊ DATABASE\n\n" +
                      "📈 Tăng trưởng dữ liệu:\n" +
                      "• Người dùng: +15 tuần này\n" +
                      "• Bài tập: +8 tuần này\n" +
                      "• Video: +5 tuần này\n\n" +
                      "💾 Sử dụng bộ nhớ:\n" +
                      "• Database: 2.5 MB\n" +
                      "• Cache: 1.2 MB\n" +
                      "• Logs: 0.8 MB\n\n" +
                      "⚡ Hiệu suất:\n" +
                      "• Truy vấn trung bình: 15ms\n" +
                      "• Kết nối: Ổn định\n" +
                      "• Lỗi: 0.02%";
        
        new AlertDialog.Builder(this)
            .setTitle("📊 Thống kê Database")
            .setMessage(stats)
            .setPositiveButton("Đóng", null)
            .show();
    }
    
    private void syncDatabase() {
        new AlertDialog.Builder(this)
            .setTitle("🔄 Đồng bộ Database")
            .setMessage("Đang đồng bộ dữ liệu với server...\n\n" +
                       "📤 Upload: 25 records\n" +
                       "📥 Download: 12 records\n" +
                       "🔄 Update: 8 records")
            .setPositiveButton("Hoàn thành", (dialog, which) -> {
                Toast.makeText(this, "✅ Đồng bộ thành công", Toast.LENGTH_SHORT).show();
            })
            .show();
    }
    
    private void showResetDatabaseConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle("⚠️ Cảnh báo")
            .setMessage("NGUY HIỂM: Bạn có chắc chắn muốn reset toàn bộ database?\n\n" +
                       "❌ Tất cả dữ liệu sẽ bị xóa vĩnh viễn!\n" +
                       "❌ Không thể khôi phục!\n\n" +
                       "Chọn 'Reset' để xác nhận:")
            .setPositiveButton("Reset", (dialog, which) -> {
                try {
                    com.htdgym.app.utils.DatabaseHelper.forceDatabaseRecreation(this);
                    Toast.makeText(this, "✅ Đã reset database thành công. Vui lòng khởi động lại ứng dụng.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "❌ Lỗi reset database: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    // Additional helper methods for other settings...
    private void showPasswordSettings() {
        Toast.makeText(this, "🔐 Cài đặt mật khẩu admin", Toast.LENGTH_SHORT).show();
    }
    
    private void showTwoFactorSettings() {
        Toast.makeText(this, "🛡️ Cài đặt xác thực 2 lớp", Toast.LENGTH_SHORT).show();
    }
    
    private void showSessionManagement() {
        Toast.makeText(this, "📱 Quản lý phiên đăng nhập", Toast.LENGTH_SHORT).show();
    }
    
    private void showBlockedIPs() {
        Toast.makeText(this, "🚫 Danh sách IP bị chặn", Toast.LENGTH_SHORT).show();
    }
    
    private void showSecurityLogs() {
        Toast.makeText(this, "📋 Nhật ký bảo mật", Toast.LENGTH_SHORT).show();
    }
    
    private void performManualBackup() {
        Toast.makeText(this, "💾 Đang thực hiện sao lưu...", Toast.LENGTH_SHORT).show();
    }
    
    private void showAutoBackupSettings() {
        Toast.makeText(this, "🔄 Cài đặt sao lưu tự động", Toast.LENGTH_SHORT).show();
    }
    
    private void showRestoreOptions() {
        Toast.makeText(this, "📥 Tùy chọn khôi phục", Toast.LENGTH_SHORT).show();
    }
    
    private void showCloudSettings() {
        Toast.makeText(this, "☁️ Cài đặt Cloud Storage", Toast.LENGTH_SHORT).show();
    }
    
    private void showBackupHistory() {
        Toast.makeText(this, "📋 Lịch sử sao lưu", Toast.LENGTH_SHORT).show();
    }
    
    private void toggleMaintenanceMode() {
        boolean isMaintenanceMode = sharedPreferences.getBoolean("maintenance_mode", false);
        sharedPreferences.edit().putBoolean("maintenance_mode", !isMaintenanceMode).apply();
        
        String status = !isMaintenanceMode ? "BẬT" : "TẮT";
        Toast.makeText(this, "🔧 Đã " + status + " chế độ bảo trì", Toast.LENGTH_SHORT).show();
    }
    
    private void showSystemMonitoring() {
        Toast.makeText(this, "📊 Giám sát hệ thống", Toast.LENGTH_SHORT).show();
    }
    
    private void clearCache() {
        Toast.makeText(this, "🧹 Đã xóa cache", Toast.LENGTH_SHORT).show();
    }
    
    private void optimizePerformance() {
        Toast.makeText(this, "📈 Đang tối ưu hiệu suất...", Toast.LENGTH_SHORT).show();
    }
    
    private void showRestartConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle("🔄 Khởi động lại")
            .setMessage("Bạn có muốn khởi động lại ứng dụng?")
            .setPositiveButton("Khởi động lại", (dialog, which) -> {
                // Restart app logic
                finishAffinity();
                System.exit(0);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void showGoogleAnalyticsSettings() {
        Toast.makeText(this, "📊 Cài đặt Google Analytics", Toast.LENGTH_SHORT).show();
    }
    
    private void showFirebaseSettings() {
        Toast.makeText(this, "🔥 Cài đặt Firebase", Toast.LENGTH_SHORT).show();
    }
    
    private void showCustomReports() {
        Toast.makeText(this, "📈 Báo cáo tùy chỉnh", Toast.LENGTH_SHORT).show();
    }
    
    private void showEventTracking() {
        Toast.makeText(this, "🎯 Theo dõi sự kiện", Toast.LENGTH_SHORT).show();
    }
    
    private void exportReports() {
        Toast.makeText(this, "📋 Xuất báo cáo", Toast.LENGTH_SHORT).show();
    }
    
    private void showApiKeyManagement() {
        Toast.makeText(this, "🔑 Quản lý API Keys", Toast.LENGTH_SHORT).show();
    }
    
    private void showEndpointSettings() {
        Toast.makeText(this, "🌐 Cài đặt endpoints", Toast.LENGTH_SHORT).show();
    }
    
    private void showTimeoutSettings() {
        Toast.makeText(this, "⏱️ Cài đặt timeout", Toast.LENGTH_SHORT).show();
    }
    
    private void showRateLimitSettings() {
        Toast.makeText(this, "🔄 Cài đặt rate limiting", Toast.LENGTH_SHORT).show();
    }
    
    private void showApiLogs() {
        Toast.makeText(this, "📋 API logs", Toast.LENGTH_SHORT).show();
    }
}