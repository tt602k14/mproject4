package com.htdgym.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.htdgym.app.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String KEY_AVATAR_URI = "avatar_uri";
    private static final String KEY_USER_NAME  = "user_display_name";

    private ImageView btnBack, ivAvatar;
    private TextView tvUserName, tvMemberSince;
    private LinearLayout btnAge, btnHeight, btnWeight, btnLanguage, btnMusic;
    private LinearLayout btnAchievements, btnTrainingHistory, btnRestorePurchase, btnDefaultAccounts;
    private LinearLayout btnShareApp, btnRateApp, btnContactSupport;
    private CardView btnLogout;

    private TextView tvAgeValue, tvHeightValue, tvWeightValue;
    private TextView tvLanguageValue, tvMusicValue;

    private SharedPreferences prefs;

    // Launcher chọn ảnh từ gallery
    private final ActivityResultLauncher<String> pickImageLauncher =
        registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                // Lưu URI và hiển thị
                prefs.edit().putString(KEY_AVATAR_URI, uri.toString()).apply();
                loadAvatar(uri.toString());
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);

        initViews();
        loadUserData();
        setupClickListeners();
    }

    private void initViews() {
        btnBack   = findViewById(R.id.btn_back);
        ivAvatar  = findViewById(R.id.iv_avatar);
        tvUserName    = findViewById(R.id.tv_user_name);
        tvMemberSince = findViewById(R.id.tv_member_since);

        btnAge    = findViewById(R.id.btn_age);
        btnHeight = findViewById(R.id.btn_height);
        btnWeight = findViewById(R.id.btn_weight);
        tvAgeValue    = findViewById(R.id.tv_age_value);
        tvHeightValue = findViewById(R.id.tv_height_value);
        tvWeightValue = findViewById(R.id.tv_weight_value);

        btnLanguage = findViewById(R.id.btn_language);
        btnMusic    = findViewById(R.id.btn_music);
        tvLanguageValue = findViewById(R.id.tv_language_value);
        tvMusicValue    = findViewById(R.id.tv_music_value);

        btnAchievements    = findViewById(R.id.btn_achievements);
        btnTrainingHistory = findViewById(R.id.btn_training_history);
        btnRestorePurchase = findViewById(R.id.btn_restore_purchase);
        btnDefaultAccounts = findViewById(R.id.btn_default_accounts);

        btnShareApp      = findViewById(R.id.btn_share_app);
        btnRateApp       = findViewById(R.id.btn_rate_app);
        btnContactSupport = findViewById(R.id.btn_contact_support);

        btnLogout = findViewById(R.id.btn_logout);
    }

    private void loadUserData() {
        int age      = prefs.getInt("user_age", 25);
        int height   = prefs.getInt("user_height", 170);
        int weight   = prefs.getInt("user_weight", 70);
        String language = prefs.getString("user_language", "Tiếng Việt");
        String music    = prefs.getString("user_music", "My Life");
        String name     = prefs.getString(KEY_USER_NAME, "Người dùng HTD Gym");
        String avatarUri = prefs.getString(KEY_AVATAR_URI, null);

        tvAgeValue.setText(String.valueOf(age));
        tvHeightValue.setText(height + " cm");
        tvWeightValue.setText(weight + " kg");
        tvLanguageValue.setText(language);
        tvMusicValue.setText(music);
        tvUserName.setText(name);

        // Load avatar nếu đã có
        loadAvatar(avatarUri);
    }

    /** Load avatar từ URI đã lưu, hoặc hiển thị icon mặc định */
    private void loadAvatar(String uriString) {
        if (uriString != null && !uriString.isEmpty()) {
            // Tắt tint khi có ảnh thật
            ivAvatar.setColorFilter(null);
            ivAvatar.setPadding(0, 0, 0, 0);
            Glide.with(this)
                .load(Uri.parse(uriString))
                .transform(new CircleCrop())
                .placeholder(R.drawable.ic_user_avatar)
                .error(R.drawable.ic_user_avatar)
                .into(ivAvatar);
        } else {
            // Icon mặc định với tint trắng
            ivAvatar.setImageResource(R.drawable.ic_user_avatar);
            ivAvatar.setColorFilter(android.graphics.Color.WHITE);
            int pad = (int)(14 * getResources().getDisplayMetrics().density);
            ivAvatar.setPadding(pad, pad, pad, pad);
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Nhấn avatar → chọn ảnh
        ivAvatar.setOnClickListener(v -> showAvatarOptions());

        btnAge.setOnClickListener(v -> showAgeDialog());
        btnHeight.setOnClickListener(v -> showHeightDialog());
        btnWeight.setOnClickListener(v -> showWeightDialog());

        btnLanguage.setOnClickListener(v -> showLanguageDialog());
        btnMusic.setOnClickListener(v -> showMusicDialog());

        btnAchievements.setOnClickListener(v ->
            Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show());

        btnTrainingHistory.setOnClickListener(v -> showClearHistoryDialog());

        btnRestorePurchase.setOnClickListener(v ->
            Toast.makeText(this, "Không có giao dịch nào để khôi phục", Toast.LENGTH_SHORT).show());

        btnDefaultAccounts.setOnClickListener(v -> {
            Intent intent = new Intent(this, DefaultAccountsActivity.class);
            startActivity(intent);
        });

        btnShareApp.setOnClickListener(v -> shareApp());
        btnRateApp.setOnClickListener(v -> rateApp());
        btnContactSupport.setOnClickListener(v -> contactSupport());
        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showAvatarOptions() {
        new AlertDialog.Builder(this)
            .setTitle("Ảnh đại diện")
            .setItems(new String[]{"Chọn từ thư viện", "Xóa ảnh hiện tại"}, (dialog, which) -> {
                if (which == 0) {
                    pickImageLauncher.launch("image/*");
                } else {
                    prefs.edit().remove(KEY_AVATAR_URI).apply();
                    loadAvatar(null);
                    Toast.makeText(this, "Đã xóa ảnh đại diện", Toast.LENGTH_SHORT).show();
                }
            })
            .show();
    }

    private void showAgeDialog() {
        String[] ages = new String[83]; // 18-100
        for (int i = 0; i < ages.length; i++) {
            ages[i] = String.valueOf(i + 18);
        }
        
        new AlertDialog.Builder(this)
            .setTitle("Chọn tuổi")
            .setItems(ages, (dialog, which) -> {
                int age = which + 18;
                prefs.edit().putInt("user_age", age).apply();
                tvAgeValue.setText(String.valueOf(age));
            })
            .show();
    }

    private void showHeightDialog() {
        String[] heights = new String[101]; // 140-240 cm
        for (int i = 0; i < heights.length; i++) {
            heights[i] = (i + 140) + " cm";
        }
        
        new AlertDialog.Builder(this)
            .setTitle("Chọn chiều cao")
            .setItems(heights, (dialog, which) -> {
                int height = which + 140;
                prefs.edit().putInt("user_height", height).apply();
                tvHeightValue.setText(height + " cm");
            })
            .show();
    }

    private void showWeightDialog() {
        String[] weights = new String[151]; // 40-190 kg
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (i + 40) + " kg";
        }
        
        new AlertDialog.Builder(this)
            .setTitle("Chọn trọng lượng")
            .setItems(weights, (dialog, which) -> {
                int weight = which + 40;
                prefs.edit().putInt("user_weight", weight).apply();
                tvWeightValue.setText(weight + " kg");
            })
            .show();
    }

    private void showLanguageDialog() {
        String[] languages = {"Tiếng Việt", "English", "中文", "日本語", "한국어", "Español", "Français", "Deutsch"};
        
        new AlertDialog.Builder(this)
            .setTitle("Chọn ngôn ngữ")
            .setItems(languages, (dialog, which) -> {
                String language = languages[which];
                prefs.edit().putString("user_language", language).apply();
                tvLanguageValue.setText(language);
                Toast.makeText(this, "Ngôn ngữ: " + language, Toast.LENGTH_SHORT).show();
            })
            .show();
    }

    private void showMusicDialog() {
        String[] musics = {"My Life", "Workout Mix", "Motivation", "Rock", "Pop", "EDM", "Hip Hop", "Không có"};
        
        new AlertDialog.Builder(this)
            .setTitle("Chọn âm nhạc")
            .setItems(musics, (dialog, which) -> {
                String music = musics[which];
                prefs.edit().putString("user_music", music).apply();
                tvMusicValue.setText(music);
                Toast.makeText(this, "Âm nhạc: " + music, Toast.LENGTH_SHORT).show();
            })
            .show();
    }

    private void showClearHistoryDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Xóa lịch sử tập luyện")
            .setMessage("Bạn có chắc chắn muốn xóa toàn bộ lịch sử tập luyện?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                // Clear training history
                prefs.edit()
                    .remove("training_history")
                    .remove("total_workouts")
                    .remove("total_calories")
                    .apply();
                Toast.makeText(this, "Đã xóa lịch sử tập luyện", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HTD GYM");
        shareIntent.putExtra(Intent.EXTRA_TEXT, 
            "Hãy thử ứng dụng HTD GYM - Tập luyện tại nhà hiệu quả!\n" +
            "Tải ngay: [Link ứng dụng]");
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
    }

    private void rateApp() {
        Toast.makeText(this, "Cảm ơn bạn đã đánh giá ứng dụng!", Toast.LENGTH_SHORT).show();
        // In production: Open Play Store
        // Intent intent = new Intent(Intent.ACTION_VIEW);
        // intent.setData(Uri.parse("market://details?id=" + getPackageName()));
        // startActivity(intent);
    }

    private void contactSupport() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@htdgym.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hỗ trợ HTD GYM");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Xin chào,\n\n");
        
        try {
            startActivity(Intent.createChooser(emailIntent, "Gửi email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Không có ứng dụng email", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất?")
            .setPositiveButton("Đăng xuất", (dialog, which) -> {
                // Clear login session
                prefs.edit()
                    .putBoolean("is_logged_in", false)
                    .remove("user_id")
                    .remove("username")
                    .remove("email")
                    .remove("full_name")
                    .apply();
                
                // Go to login screen
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}
