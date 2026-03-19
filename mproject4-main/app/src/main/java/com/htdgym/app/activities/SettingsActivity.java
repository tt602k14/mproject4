package com.htdgym.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.activities.BaseActivity;
import com.htdgym.app.dialogs.ThemeDialog;
import com.htdgym.app.utils.ThemeHelper;

public class SettingsActivity extends BaseActivity {

    private CardView btnBack;
    private LinearLayout btnAge, btnHeight, btnWeight, btnLanguage, btnMusic, btnTheme;
    private LinearLayout btnAchievements, btnTrainingHistory, btnRestorePurchase;
    private LinearLayout btnShareApp, btnRateApp, btnContactSupport;
    private CardView btnLogout;
    
    private TextView tvAgeValue, tvHeightValue, tvWeightValue;
    private TextView tvLanguageValue, tvMusicValue, tvThemeValue;
    
    private SharedPreferences prefs;

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
        btnBack = findViewById(R.id.btn_back);
        
        // Tôi section
        btnAge = findViewById(R.id.btn_age);
        btnHeight = findViewById(R.id.btn_height);
        btnWeight = findViewById(R.id.btn_weight);
        tvAgeValue = findViewById(R.id.tv_age_value);
        tvHeightValue = findViewById(R.id.tv_height_value);
        tvWeightValue = findViewById(R.id.tv_weight_value);
        
        // Cài đặt section
        btnLanguage = findViewById(R.id.btn_language);
        btnMusic = findViewById(R.id.btn_music);
        btnTheme = findViewById(R.id.btn_theme);
        tvLanguageValue = findViewById(R.id.tv_language_value);
        tvMusicValue = findViewById(R.id.tv_music_value);
        tvThemeValue = findViewById(R.id.tv_theme_value);
        
        btnAchievements = findViewById(R.id.btn_achievements);
        btnTrainingHistory = findViewById(R.id.btn_training_history);
        btnRestorePurchase = findViewById(R.id.btn_restore_purchase);
        
        // Hỗ trợ section
        btnShareApp = findViewById(R.id.btn_share_app);
        btnRateApp = findViewById(R.id.btn_rate_app);
        btnContactSupport = findViewById(R.id.btn_contact_support);
        
        btnLogout = findViewById(R.id.btn_logout);
    }

    private void loadUserData() {
        // Load saved user data
        int age = prefs.getInt("user_age", 25);
        int height = prefs.getInt("user_height", 170);
        int weight = prefs.getInt("user_weight", 70);
        String language = prefs.getString("user_language", "Tiếng Việt");
        String music = prefs.getString("user_music", "My Life");
        int themeMode = ThemeHelper.getThemeMode(this);
        
        tvAgeValue.setText(String.valueOf(age));
        tvHeightValue.setText(height + " cm");
        tvWeightValue.setText(weight + " kg");
        tvLanguageValue.setText(language);
        tvMusicValue.setText(music);
        tvThemeValue.setText(ThemeHelper.getThemeModeName(this, themeMode));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        // Tôi section
        btnAge.setOnClickListener(v -> showAgeDialog());
        btnHeight.setOnClickListener(v -> showHeightDialog());
        btnWeight.setOnClickListener(v -> showWeightDialog());
        
        // Cài đặt section
        btnLanguage.setOnClickListener(v -> showLanguageDialog());
        btnMusic.setOnClickListener(v -> showMusicDialog());
        btnTheme.setOnClickListener(v -> showThemeDialog());
        
        btnAchievements.setOnClickListener(v -> showPremiumPlansDialog());
        
        btnTrainingHistory.setOnClickListener(v -> showClearHistoryDialog());
        
        btnRestorePurchase.setOnClickListener(v -> showRestorePurchaseDialog());
        
        // Hỗ trợ section
        btnShareApp.setOnClickListener(v -> shareApp());
        btnRateApp.setOnClickListener(v -> rateApp());
        btnContactSupport.setOnClickListener(v -> contactSupport());
        
        // Logout
        btnLogout.setOnClickListener(v -> showLogoutDialog());
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

    private void showThemeDialog() {
        ThemeDialog dialog = new ThemeDialog(this);
        dialog.show();
        
        // Update theme value after dialog closes
        dialog.setOnDismissListener(d -> {
            int themeMode = ThemeHelper.getThemeMode(this);
            tvThemeValue.setText(ThemeHelper.getThemeModeName(this, themeMode));
        });
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
    
    private void showRestorePurchaseDialog() {
        com.htdgym.app.utils.PremiumManager premiumManager = 
            com.htdgym.app.utils.PremiumManager.getInstance(this);
        
        boolean isPremium = premiumManager.isPremiumUser();
        
        if (isPremium) {
            // User has premium - show option to reset for testing
            new AlertDialog.Builder(this)
                .setTitle("🔄 Khôi phục mua hàng")
                .setMessage("Bạn đang có gói Premium đang hoạt động.\n\n" +
                    "Chọn hành động:")
                .setPositiveButton("🔓 Reset Premium (Test)", (dialog, which) -> {
                    resetPremiumStatus();
                })
                .setNeutralButton("📊 Xem thông tin Premium", (dialog, which) -> {
                    showPremiumInfo();
                })
                .setNegativeButton("Hủy", null)
                .show();
        } else {
            // User doesn't have premium - show restore options
            new AlertDialog.Builder(this)
                .setTitle("🔄 Khôi phục mua hàng")
                .setMessage("Bạn chưa có gói Premium nào.\n\n" +
                    "Chọn hành động:")
                .setPositiveButton("🎁 Kích hoạt Premium (Test)", (dialog, which) -> {
                    activateTestPremium();
                })
                .setNeutralButton("💳 Mua Premium", (dialog, which) -> {
                    Intent intent = new Intent(this, com.htdgym.app.activities.PremiumActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", null)
                .show();
        }
    }
    
    private void resetPremiumStatus() {
        new AlertDialog.Builder(this)
            .setTitle("⚠️ Xác nhận Reset")
            .setMessage("Bạn có chắc chắn muốn reset trạng thái Premium?\n\n" +
                "Điều này sẽ:\n" +
                "• Hủy gói Premium hiện tại\n" +
                "• Khóa lại tất cả nội dung Premium\n" +
                "• Xóa dữ liệu thanh toán\n\n" +
                "⚠️ Chỉ dùng để test!")
            .setPositiveButton("🔓 Reset ngay", (dialog, which) -> {
                com.htdgym.app.utils.PremiumManager premiumManager = 
                    com.htdgym.app.utils.PremiumManager.getInstance(this);
                
                // Deactivate premium
                premiumManager.deactivatePremium();
                
                // Show success message
                Toast.makeText(this, "✅ Đã reset Premium! Tất cả nội dung đã bị khóa lại.", 
                    Toast.LENGTH_LONG).show();
                
                // Optional: Restart app to refresh UI
                showRestartAppDialog();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void activateTestPremium() {
        new AlertDialog.Builder(this)
            .setTitle("🎁 Kích hoạt Premium Test")
            .setMessage("Chọn gói Premium để test:")
            .setPositiveButton("📅 Gói tháng (Test)", (dialog, which) -> {
                activatePremiumForTesting("monthly", 50000);
            })
            .setNeutralButton("📆 Gói năm (Test)", (dialog, which) -> {
                activatePremiumForTesting("yearly", 500000);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void activatePremiumForTesting(String planType, double price) {
        com.htdgym.app.utils.PremiumManager premiumManager = 
            com.htdgym.app.utils.PremiumManager.getInstance(this);
        
        // Activate premium for testing
        String testTransactionId = "TEST_" + System.currentTimeMillis();
        premiumManager.activatePremium(planType, "test_payment", testTransactionId, price);
        
        Toast.makeText(this, "🎉 Đã kích hoạt Premium " + planType + " để test!", 
            Toast.LENGTH_LONG).show();
        
        showRestartAppDialog();
    }
    
    private void showPremiumInfo() {
        com.htdgym.app.utils.PremiumManager premiumManager = 
            com.htdgym.app.utils.PremiumManager.getInstance(this);
        
        premiumManager.getPremiumUserInfo(premiumUser -> {
            runOnUiThread(() -> {
                if (premiumUser != null) {
                    String info = "📊 Thông tin Premium:\n\n" +
                        "🎯 Trạng thái: " + (premiumUser.isPremium() ? "Đang hoạt động" : "Không hoạt động") + "\n" +
                        "📦 Gói: " + premiumUser.getSubscriptionType() + "\n" +
                        "💳 Phương thức: " + premiumUser.getPaymentMethod() + "\n" +
                        "💰 Giá: " + String.format("%,.0f VND", premiumUser.getPrice()) + "\n" +
                        "📅 Bắt đầu: " + (premiumUser.getSubscriptionStartDate() != null ? 
                            premiumUser.getSubscriptionStartDate().toString() : "N/A") + "\n" +
                        "⏰ Kết thúc: " + (premiumUser.getSubscriptionEndDate() != null ? 
                            premiumUser.getSubscriptionEndDate().toString() : "Vĩnh viễn");
                    
                    new AlertDialog.Builder(this)
                        .setTitle("📊 Thông tin Premium")
                        .setMessage(info)
                        .setPositiveButton("OK", null)
                        .setNeutralButton("📋 Copy", (dialog, which) -> {
                            android.content.ClipboardManager clipboard = 
                                (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("Premium Info", info);
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(this, "Đã copy thông tin", Toast.LENGTH_SHORT).show();
                        })
                        .show();
                } else {
                    Toast.makeText(this, "Không tìm thấy thông tin Premium", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    private void showRestartAppDialog() {
        new AlertDialog.Builder(this)
            .setTitle("🔄 Khởi động lại")
            .setMessage("Để thay đổi có hiệu lực, bạn cần khởi động lại ứng dụng.")
            .setPositiveButton("🔄 Khởi động lại", (dialog, which) -> {
                // Restart app
                Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);
            })
            .setNegativeButton("Để sau", null)
            .show();
    }
    
    private void showPremiumPlansDialog() {
        com.htdgym.app.utils.PremiumManager premiumManager = 
            com.htdgym.app.utils.PremiumManager.getInstance(this);
        
        boolean isPremium = premiumManager.isPremiumUser();
        
        if (isPremium) {
            // User has premium - show current plan info and option to upgrade
            premiumManager.getPremiumUserInfo(premiumUser -> {
                runOnUiThread(() -> {
                    if (premiumUser != null) {
                        String currentPlan = premiumUser.getSubscriptionType();
                        String planName = getPlanDisplayName(currentPlan);
                        
                        new AlertDialog.Builder(this)
                            .setTitle("👑 Gói Premium Hiện Tại")
                            .setMessage("Bạn đang sử dụng: " + planName + "\n\n" +
                                "Bạn muốn làm gì?")
                            .setPositiveButton("📊 Xem tất cả gói", (dialog, which) -> {
                                showAllPremiumPlans();
                            })
                            .setNeutralButton("📋 Thông tin chi tiết", (dialog, which) -> {
                                showPremiumInfo();
                            })
                            .setNegativeButton("Đóng", null)
                            .show();
                    }
                });
            });
        } else {
            // User doesn't have premium - show all plans
            showAllPremiumPlans();
        }
    }
    
    private void showAllPremiumPlans() {
        // Create a custom dialog with beautiful premium plans
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        // Create custom view with enhanced styling
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(32, 32, 32, 32);
        mainLayout.setBackgroundResource(R.drawable.gradient_header);
        
        // Enhanced title with animation
        TextView title = new TextView(this);
        title.setText("💎 Chọn Gói Premium Phù Hợp");
        title.setTextSize(28f);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setTextColor(getResources().getColor(android.R.color.white));
        title.setGravity(android.view.Gravity.CENTER);
        title.setShadowLayer(4f, 2f, 2f, getResources().getColor(android.R.color.black));
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, 16);
        title.setLayoutParams(titleParams);
        mainLayout.addView(title);
        
        // Subtitle with benefits
        TextView subtitle = new TextView(this);
        subtitle.setText("🎯 Mở khóa tất cả tính năng Premium\n🚀 Nâng cao trải nghiệm tập luyện");
        subtitle.setTextSize(16f);
        subtitle.setTextColor(getResources().getColor(android.R.color.white));
        subtitle.setGravity(android.view.Gravity.CENTER);
        subtitle.setAlpha(0.9f);
        LinearLayout.LayoutParams subtitleParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        subtitleParams.setMargins(0, 0, 0, 32);
        subtitle.setLayoutParams(subtitleParams);
        mainLayout.addView(subtitle);
        
        // Container for plan cards with enhanced background
        LinearLayout plansContainer = new LinearLayout(this);
        plansContainer.setOrientation(LinearLayout.VERTICAL);
        plansContainer.setPadding(16, 24, 16, 24);
        plansContainer.setBackgroundColor(getResources().getColor(R.color.background_color));
        
        // Get premium plans
        com.htdgym.app.utils.PremiumManager premiumManager = 
            com.htdgym.app.utils.PremiumManager.getInstance(this);
        com.htdgym.app.utils.PremiumManager.PremiumPlan[] plans = premiumManager.getPremiumPlans();
        
        // Create plan cards with staggered animation
        for (int i = 0; i < plans.length; i++) {
            View planCard = createPremiumPlanCard(plans[i]);
            
            // Add staggered fade-in animation
            planCard.setAlpha(0f);
            planCard.setTranslationY(100f);
            planCard.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(i * 150)
                .start();
            
            plansContainer.addView(planCard);
        }
        
        mainLayout.addView(plansContainer);
        
        // Enhanced footer with guarantee
        LinearLayout footerLayout = new LinearLayout(this);
        footerLayout.setOrientation(LinearLayout.VERTICAL);
        footerLayout.setPadding(24, 24, 24, 24);
        footerLayout.setGravity(android.view.Gravity.CENTER);
        
        TextView guarantee = new TextView(this);
        guarantee.setText("🛡️ Đảm bảo hoàn tiền 100%\n🔒 Thanh toán an toàn & bảo mật\n📞 Hỗ trợ 24/7");
        guarantee.setTextSize(14f);
        guarantee.setTextColor(getResources().getColor(android.R.color.white));
        guarantee.setGravity(android.view.Gravity.CENTER);
        guarantee.setAlpha(0.8f);
        footerLayout.addView(guarantee);
        
        mainLayout.addView(footerLayout);
        
        scrollView.addView(mainLayout);
        
        // Create enhanced dialog
        AlertDialog dialog = builder
            .setView(scrollView)
            .setNegativeButton("❌ Đóng", (d, which) -> d.dismiss())
            .create();
        
        // Customize dialog appearance
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        
        // Add dialog entrance animation
        dialog.getWindow().getDecorView().setScaleX(0.8f);
        dialog.getWindow().getDecorView().setScaleY(0.8f);
        dialog.getWindow().getDecorView().setAlpha(0f);
        dialog.getWindow().getDecorView().animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(300)
            .start();
    }
    
    private View createPremiumPlanCard(com.htdgym.app.utils.PremiumManager.PremiumPlan plan) {
        // Create card container with enhanced styling
        androidx.cardview.widget.CardView cardView = new androidx.cardview.widget.CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 32);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(24f);
        cardView.setCardElevation(plan.isPopular ? 20f : 12f);
        
        // Enhanced background with gradient for popular plan
        if (plan.isPopular) {
            // Use custom drawable with border for popular plan
            cardView.setBackgroundResource(R.drawable.card_premium_border);
        } else {
            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        }
        
        // Main container with gradient background
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(40, 40, 40, 40);
        
        // Add subtle gradient background for popular plan
        if (plan.isPopular) {
            container.setBackgroundResource(R.drawable.gradient_card_3);
        }
        
        // Popular badge with enhanced styling
        if (plan.isPopular) {
            LinearLayout badgeContainer = new LinearLayout(this);
            badgeContainer.setOrientation(LinearLayout.HORIZONTAL);
            badgeContainer.setGravity(android.view.Gravity.CENTER);
            LinearLayout.LayoutParams badgeContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            badgeContainerParams.setMargins(0, 0, 0, 24);
            badgeContainer.setLayoutParams(badgeContainerParams);
            
            TextView popularBadge = new TextView(this);
            popularBadge.setText("🔥 PHỔ BIẾN NHẤT 🔥");
            popularBadge.setTextColor(getResources().getColor(android.R.color.white));
            popularBadge.setTextSize(14f);
            popularBadge.setTypeface(null, android.graphics.Typeface.BOLD);
            popularBadge.setPadding(32, 12, 32, 12);
            popularBadge.setBackgroundResource(R.drawable.badge_popular);
            popularBadge.setGravity(android.view.Gravity.CENTER);
            
            // Add pulsing animation effect
            android.view.animation.Animation pulse = new android.view.animation.ScaleAnimation(
                1.0f, 1.05f, 1.0f, 1.05f,
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f
            );
            pulse.setDuration(1000);
            pulse.setRepeatMode(android.view.animation.Animation.REVERSE);
            pulse.setRepeatCount(android.view.animation.Animation.INFINITE);
            popularBadge.startAnimation(pulse);
            
            badgeContainer.addView(popularBadge);
            container.addView(badgeContainer);
        }
        
        // Enhanced header with icon and name
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        headerLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        headerParams.setMargins(0, 0, 0, 24);
        headerLayout.setLayoutParams(headerParams);
        
        // Enhanced plan icon with circular background
        androidx.cardview.widget.CardView iconContainer = new androidx.cardview.widget.CardView(this);
        LinearLayout.LayoutParams iconContainerParams = new LinearLayout.LayoutParams(100, 100);
        iconContainerParams.setMargins(0, 0, 24, 0);
        iconContainer.setLayoutParams(iconContainerParams);
        iconContainer.setRadius(50f);
        iconContainer.setCardElevation(8f);
        iconContainer.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        
        TextView planIcon = new TextView(this);
        planIcon.setText(plan.icon);
        planIcon.setTextSize(36f);
        planIcon.setGravity(android.view.Gravity.CENTER);
        planIcon.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.MATCH_PARENT
        ));
        
        iconContainer.addView(planIcon);
        headerLayout.addView(iconContainer);
        
        // Name and savings container with enhanced styling
        LinearLayout nameContainer = new LinearLayout(this);
        nameContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams nameContainerParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        nameContainer.setLayoutParams(nameContainerParams);
        
        // Enhanced plan name
        TextView planName = new TextView(this);
        planName.setText(plan.name);
        planName.setTextColor(plan.isPopular ? 
            getResources().getColor(android.R.color.white) : 
            getResources().getColor(R.color.text_primary));
        planName.setTextSize(22f);
        planName.setTypeface(null, android.graphics.Typeface.BOLD);
        planName.setShadowLayer(2f, 1f, 1f, getResources().getColor(android.R.color.black));
        nameContainer.addView(planName);
        
        // Enhanced savings label
        String savings = plan.getSavingsText();
        if (!savings.isEmpty()) {
            androidx.cardview.widget.CardView savingsCard = new androidx.cardview.widget.CardView(this);
            LinearLayout.LayoutParams savingsCardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            savingsCardParams.setMargins(0, 12, 0, 0);
            savingsCard.setLayoutParams(savingsCardParams);
            savingsCard.setRadius(16f);
            savingsCard.setCardElevation(4f);
            savingsCard.setCardBackgroundColor(getResources().getColor(R.color.primary));
            
            TextView savingsLabel = new TextView(this);
            savingsLabel.setText("💰 " + savings);
            savingsLabel.setTextColor(getResources().getColor(android.R.color.white));
            savingsLabel.setTextSize(14f);
            savingsLabel.setTypeface(null, android.graphics.Typeface.BOLD);
            savingsLabel.setPadding(20, 8, 20, 8);
            savingsLabel.setGravity(android.view.Gravity.CENTER);
            
            savingsCard.addView(savingsLabel);
            nameContainer.addView(savingsCard);
        }
        
        headerLayout.addView(nameContainer);
        container.addView(headerLayout);
        
        // Enhanced price section with visual effects
        androidx.cardview.widget.CardView priceCard = new androidx.cardview.widget.CardView(this);
        LinearLayout.LayoutParams priceCardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        priceCardParams.setMargins(0, 0, 0, 20);
        priceCard.setLayoutParams(priceCardParams);
        priceCard.setRadius(16f);
        priceCard.setCardElevation(6f);
        priceCard.setCardBackgroundColor(plan.isPopular ? 
            getResources().getColor(R.color.premium_gold_light) : 
            getResources().getColor(android.R.color.white));
        
        LinearLayout priceLayout = new LinearLayout(this);
        priceLayout.setOrientation(LinearLayout.VERTICAL);
        priceLayout.setPadding(24, 20, 24, 20);
        priceLayout.setGravity(android.view.Gravity.CENTER);
        
        // Main price with enhanced styling
        TextView planPrice = new TextView(this);
        planPrice.setText(plan.getFormattedPrice());
        planPrice.setTextColor(getResources().getColor(R.color.secondary));
        planPrice.setTextSize(32f);
        planPrice.setTypeface(null, android.graphics.Typeface.BOLD);
        planPrice.setGravity(android.view.Gravity.CENTER);
        planPrice.setShadowLayer(3f, 2f, 2f, getResources().getColor(android.R.color.black));
        priceLayout.addView(planPrice);
        
        // Price per month with enhanced styling
        if ("7months".equals(plan.id)) {
            double monthlyPrice = plan.price / 7;
            TextView pricePerMonth = new TextView(this);
            pricePerMonth.setText(String.format("Chỉ %.0f VND/tháng", monthlyPrice));
            pricePerMonth.setTextColor(getResources().getColor(R.color.primary_darker));
            pricePerMonth.setTextSize(16f);
            pricePerMonth.setTypeface(null, android.graphics.Typeface.BOLD);
            pricePerMonth.setGravity(android.view.Gravity.CENTER);
            LinearLayout.LayoutParams monthlyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            monthlyParams.setMargins(0, 8, 0, 0);
            pricePerMonth.setLayoutParams(monthlyParams);
            priceLayout.addView(pricePerMonth);
        } else if ("yearly".equals(plan.id)) {
            double monthlyPrice = plan.price / 12;
            TextView pricePerMonth = new TextView(this);
            pricePerMonth.setText(String.format("Chỉ %.0f VND/tháng", monthlyPrice));
            pricePerMonth.setTextColor(getResources().getColor(R.color.primary_darker));
            pricePerMonth.setTextSize(16f);
            pricePerMonth.setTypeface(null, android.graphics.Typeface.BOLD);
            pricePerMonth.setGravity(android.view.Gravity.CENTER);
            LinearLayout.LayoutParams monthlyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            monthlyParams.setMargins(0, 8, 0, 0);
            pricePerMonth.setLayoutParams(monthlyParams);
            priceLayout.addView(pricePerMonth);
        } else if ("lifetime".equals(plan.id)) {
            TextView pricePerMonth = new TextView(this);
            pricePerMonth.setText("🎯 Trả một lần - Dùng mãi mãi");
            pricePerMonth.setTextColor(getResources().getColor(R.color.primary_darker));
            pricePerMonth.setTextSize(16f);
            pricePerMonth.setTypeface(null, android.graphics.Typeface.BOLD);
            pricePerMonth.setGravity(android.view.Gravity.CENTER);
            LinearLayout.LayoutParams monthlyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            monthlyParams.setMargins(0, 8, 0, 0);
            pricePerMonth.setLayoutParams(monthlyParams);
            priceLayout.addView(pricePerMonth);
        }
        
        priceCard.addView(priceLayout);
        container.addView(priceCard);
        
        // Enhanced description
        TextView planDescription = new TextView(this);
        planDescription.setText("📋 " + plan.description);
        planDescription.setTextColor(plan.isPopular ? 
            getResources().getColor(android.R.color.white) : 
            getResources().getColor(R.color.text_secondary));
        planDescription.setTextSize(16f);
        planDescription.setGravity(android.view.Gravity.CENTER);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        descParams.setMargins(0, 0, 0, 24);
        planDescription.setLayoutParams(descParams);
        container.addView(planDescription);
        
        // Enhanced features section
        androidx.cardview.widget.CardView featuresCard = new androidx.cardview.widget.CardView(this);
        LinearLayout.LayoutParams featuresCardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        featuresCardParams.setMargins(0, 0, 0, 24);
        featuresCard.setLayoutParams(featuresCardParams);
        featuresCard.setRadius(16f);
        featuresCard.setCardElevation(4f);
        featuresCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        
        LinearLayout featuresContainer = new LinearLayout(this);
        featuresContainer.setOrientation(LinearLayout.VERTICAL);
        featuresContainer.setPadding(20, 16, 20, 16);
        
        String[] features = {
            "🏋️ Chương trình tập luyện 60 & 90 ngày",
            "🥗 Kế hoạch dinh dưỡng nâng cao", 
            "👨‍⚕️ Tư vấn cá nhân hóa 24/7",
            "🚫 Không quảng cáo làm phiền",
            "📱 Tải video offline không giới hạn"
        };
        
        for (String feature : features) {
            TextView featureText = new TextView(this);
            featureText.setText(feature);
            featureText.setTextColor(getResources().getColor(R.color.text_primary));
            featureText.setTextSize(15f);
            featureText.setPadding(0, 6, 0, 6);
            LinearLayout.LayoutParams featureParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            featureText.setLayoutParams(featureParams);
            featuresContainer.addView(featureText);
        }
        
        featuresCard.addView(featuresContainer);
        container.addView(featuresCard);
        
        // Enhanced select button with gradient and animation
        Button selectButton = new Button(this);
        selectButton.setText(plan.isPopular ? "🔥 CHỌN GÓI PHỔ BIẾN 🔥" : "✨ Chọn gói này");
        selectButton.setTextColor(getResources().getColor(android.R.color.white));
        selectButton.setTextSize(18f);
        selectButton.setTypeface(null, android.graphics.Typeface.BOLD);
        selectButton.setBackgroundResource(R.drawable.button_premium);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            120
        );
        selectButton.setLayoutParams(buttonParams);
        
        // Add click animation
        selectButton.setOnClickListener(v -> {
            // Scale animation on click
            android.view.animation.Animation scaleDown = new android.view.animation.ScaleAnimation(
                1.0f, 0.95f, 1.0f, 0.95f,
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f
            );
            scaleDown.setDuration(100);
            scaleDown.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
                @Override
                public void onAnimationStart(android.view.animation.Animation animation) {}
                
                @Override
                public void onAnimationEnd(android.view.animation.Animation animation) {
                    android.view.animation.Animation scaleUp = new android.view.animation.ScaleAnimation(
                        0.95f, 1.0f, 0.95f, 1.0f,
                        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,
                        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f
                    );
                    scaleUp.setDuration(100);
                    selectButton.startAnimation(scaleUp);
                    
                    // Navigate to PremiumActivity for purchase
                    Intent intent = new Intent(SettingsActivity.this, com.htdgym.app.activities.PremiumActivity.class);
                    startActivity(intent);
                }
                
                @Override
                public void onAnimationRepeat(android.view.animation.Animation animation) {}
            });
            selectButton.startAnimation(scaleDown);
        });
        
        container.addView(selectButton);
        
        cardView.addView(container);
        return cardView;
    }
    
    private String getPlanDisplayName(String planType) {
        switch (planType) {
            case "monthly":
                return "Gói 1 Tháng 📅";
            case "7months":
                return "Gói 7 Tháng 🔥";
            case "yearly":
                return "Gói 1 Năm 📆";
            case "lifetime":
                return "Gói Cả Đời 👑";
            default:
                return "Gói Premium";
        }
    }
}
