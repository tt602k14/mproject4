package com.htdgym.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.PremiumSubscription;

import java.util.Calendar;

public class PremiumActivity extends AppCompatActivity {

    // Thông tin tài khoản nhận tiền
    private static final String BANK_ID      = "MB";
    private static final String ACCOUNT_NO   = "7369786789";
    private static final String ACCOUNT_NAME = "LU DANG HUNG";

    private ImageView btnBack;
    private CardView btnUpgradePremium, btnMonthlyPlan, btn5MonthsPlan, btnYearlyPlan, btn5YearsPlan;
    private TextView tvMonthlyPrice, tvYearlyPrice, tvYearlyDiscount;
    private GymDatabase database;
    private SharedPreferences sharedPreferences;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        database = GymDatabase.getInstance(this);
        sharedPreferences = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", 0);

        initViews();
        setupClickListeners();
        setupPricing();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnUpgradePremium = findViewById(R.id.btn_upgrade_premium);
        btnMonthlyPlan = findViewById(R.id.btn_monthly_plan);
        btn5MonthsPlan = findViewById(R.id.btn_5months_plan);
        btnYearlyPlan = findViewById(R.id.btn_yearly_plan);
        btn5YearsPlan = findViewById(R.id.btn_5years_plan);
        tvMonthlyPrice = findViewById(R.id.tv_monthly_price);
        tvYearlyPrice = findViewById(R.id.tv_yearly_price);
        tvYearlyDiscount = findViewById(R.id.tv_yearly_discount);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnUpgradePremium.setOnClickListener(v ->
            findViewById(R.id.layout_premium_plans).setVisibility(android.view.View.VISIBLE));

        btnMonthlyPlan.setOnClickListener(v ->
            showQrDialog("Gói 1 Tháng", "monthly", 50000));
        btn5MonthsPlan.setOnClickListener(v ->
            showQrDialog("Gói 5 Tháng", "5months", 300000));
        btnYearlyPlan.setOnClickListener(v ->
            showQrDialog("Gói 1 Năm", "yearly", 500000));
        btn5YearsPlan.setOnClickListener(v ->
            showQrDialog("Gói 5 Năm", "5years", 1000000));
    }

    private void setupPricing() {
        tvMonthlyPrice.setText("50.000 VND");
        tvYearlyPrice.setText("500.000 VND");
        tvYearlyDiscount.setText("Tiết kiệm 58% so với gói tháng");
    }

    /** Hiển thị dialog QR VietQR với số tiền đúng theo gói */
    private void showQrDialog(String planName, String type, long amount) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        // Root layout
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundResource(R.drawable.rounded_white_bg);
        int pad = dp(24);
        root.setPadding(pad, pad, pad, pad);

        // Tiêu đề
        TextView tvTitle = new TextView(this);
        tvTitle.setText("💳 Thanh toán " + planName);
        tvTitle.setTextSize(18f);
        tvTitle.setTextColor(0xFF1A1A1A);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setGravity(Gravity.CENTER);
        root.addView(tvTitle);

        // Số tiền
        TextView tvAmount = new TextView(this);
        tvAmount.setText(String.format("%,.0fđ", (double) amount));
        tvAmount.setTextSize(26f);
        tvAmount.setTextColor(0xFFFF6B35);
        tvAmount.setTypeface(null, android.graphics.Typeface.BOLD);
        tvAmount.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams amountParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        amountParams.setMargins(0, dp(8), 0, dp(4));
        tvAmount.setLayoutParams(amountParams);
        root.addView(tvAmount);

        // Thông tin tài khoản
        TextView tvBank = new TextView(this);
        tvBank.setText("MBBank  •  " + ACCOUNT_NO + "  •  " + ACCOUNT_NAME);
        tvBank.setTextSize(13f);
        tvBank.setTextColor(0xFF666666);
        tvBank.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams bankParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bankParams.setMargins(0, 0, 0, dp(12));
        tvBank.setLayoutParams(bankParams);
        root.addView(tvBank);

        // Ảnh QR — load từ VietQR API
        ImageView ivQr = new ImageView(this);
        int qrSize = dp(260);
        LinearLayout.LayoutParams qrParams = new LinearLayout.LayoutParams(qrSize, qrSize);
        qrParams.gravity = Gravity.CENTER_HORIZONTAL;
        ivQr.setLayoutParams(qrParams);
        ivQr.setScaleType(ImageView.ScaleType.FIT_CENTER);

        String addInfo = "HTDGym " + planName;
        String qrUrl = "https://img.vietqr.io/image/"
            + BANK_ID + "-" + ACCOUNT_NO + "-compact2.png"
            + "?amount=" + amount
            + "&addInfo=" + android.net.Uri.encode(addInfo)
            + "&accountName=" + android.net.Uri.encode(ACCOUNT_NAME);

        Glide.with(this).load(qrUrl)
            .placeholder(R.drawable.ic_qr_code)
            .into(ivQr);
        root.addView(ivQr);

        // Hướng dẫn
        TextView tvNote = new TextView(this);
        tvNote.setText("Quét mã bằng app ngân hàng bất kỳ\nNội dung chuyển khoản: " + addInfo);
        tvNote.setTextSize(12f);
        tvNote.setTextColor(0xFF888888);
        tvNote.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams noteParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        noteParams.setMargins(0, dp(10), 0, dp(16));
        tvNote.setLayoutParams(noteParams);
        root.addView(tvNote);

        // Nút "Tôi đã chuyển khoản"
        Button btnDone = new Button(this);
        btnDone.setText("✅  Tôi đã chuyển khoản");
        btnDone.setTextColor(0xFF1A1A1A);
        btnDone.setTextSize(15f);
        btnDone.setTypeface(null, android.graphics.Typeface.BOLD);
        btnDone.setBackgroundResource(R.drawable.button_gradient);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(52));
        btnDone.setLayoutParams(btnParams);
        btnDone.setOnClickListener(v -> {
            dialog.dismiss();
            activatePremium(type, amount);
        });
        root.addView(btnDone);

        // Nút huỷ
        Button btnCancel = new Button(this);
        btnCancel.setText("Huỷ");
        btnCancel.setTextColor(0xFF888888);
        btnCancel.setTextSize(14f);
        btnCancel.setBackgroundColor(0x00000000);
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cancelParams.gravity = Gravity.CENTER_HORIZONTAL;
        cancelParams.setMargins(0, dp(4), 0, 0);
        btnCancel.setLayoutParams(cancelParams);
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        root.addView(btnCancel);

        dialog.setContentView(root);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                (int) (getResources().getDisplayMetrics().widthPixels * 0.9f),
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
    }

    private void activatePremium(String type, double price) {
        new Thread(() -> {
            try {
                database.premiumSubscriptionDao().deactivateUserSubscriptions(userId);

                Calendar calendar = Calendar.getInstance();
                long startDate = calendar.getTimeInMillis();

                if (type.equals("monthly"))      calendar.add(Calendar.MONTH, 1);
                else if (type.equals("5months")) calendar.add(Calendar.MONTH, 5);
                else if (type.equals("yearly"))  calendar.add(Calendar.YEAR, 1);
                else if (type.equals("5years"))  calendar.add(Calendar.YEAR, 5);
                else                             calendar.add(Calendar.MONTH, 1);

                long endDate = calendar.getTimeInMillis();

                PremiumSubscription subscription = new PremiumSubscription(
                    userId, type, startDate, endDate, price);
                subscription.setPaymentMethod("vietqr");
                database.premiumSubscriptionDao().insertSubscription(subscription);

                sharedPreferences.edit()
                    .putBoolean("isPremium", true)
                    .putLong("premiumEndDate", endDate)
                    .apply();

                runOnUiThread(() -> {
                    Toast.makeText(this, "🎉 Kích hoạt Premium thành công!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, com.htdgym.app.MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}