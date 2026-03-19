package com.htdgym.app.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.utils.DefaultAccountManager;

import java.util.List;

public class DefaultAccountsActivity extends AppCompatActivity {

    private LinearLayout layoutAccounts;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_accounts);

        initViews();
        setupClickListeners();
        loadDefaultAccounts();
    }

    private void initViews() {
        layoutAccounts = findViewById(R.id.layout_accounts);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadDefaultAccounts() {
        List<DefaultAccountManager.AccountInfo> accounts = DefaultAccountManager.getAllDefaultAccounts();
        
        for (DefaultAccountManager.AccountInfo account : accounts) {
            addAccountCard(account);
        }
    }

    private void addAccountCard(DefaultAccountManager.AccountInfo account) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (16 * getResources().getDisplayMetrics().density));
        card.setLayoutParams(cardParams);
        card.setRadius(12 * getResources().getDisplayMetrics().density);
        card.setCardElevation(4 * getResources().getDisplayMetrics().density);
        card.setCardBackgroundColor(0xFFFFFFFF);

        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density));

        // Account type header
        TextView tvType = new TextView(this);
        tvType.setText(getAccountTypeDisplay(account.type));
        tvType.setTextSize(18);
        tvType.setTextColor(getAccountTypeColor(account.type));
        tvType.setTypeface(null, android.graphics.Typeface.BOLD);
        contentLayout.addView(tvType);

        // Name
        TextView tvName = new TextView(this);
        tvName.setText(account.name);
        tvName.setTextSize(16);
        tvName.setTextColor(0xFF1A1A1A);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setPadding(0, (int) (8 * getResources().getDisplayMetrics().density), 0, 0);
        contentLayout.addView(tvName);

        // Email
        TextView tvEmail = new TextView(this);
        tvEmail.setText("📧 " + account.email);
        tvEmail.setTextSize(14);
        tvEmail.setTextColor(0xFF666666);
        tvEmail.setPadding(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        contentLayout.addView(tvEmail);

        // Password
        TextView tvPassword = new TextView(this);
        tvPassword.setText("🔑 " + account.password);
        tvPassword.setTextSize(14);
        tvPassword.setTextColor(0xFF666666);
        tvPassword.setPadding(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        contentLayout.addView(tvPassword);

        // Description
        TextView tvDescription = new TextView(this);
        tvDescription.setText(account.description);
        tvDescription.setTextSize(12);
        tvDescription.setTextColor(0xFF999999);
        tvDescription.setPadding(0, (int) (8 * getResources().getDisplayMetrics().density), 0, 0);
        contentLayout.addView(tvDescription);

        // Copy button
        TextView btnCopy = new TextView(this);
        btnCopy.setText("📋 Copy thông tin");
        btnCopy.setTextSize(12);
        btnCopy.setTextColor(0xFF2196F3);
        btnCopy.setTypeface(null, android.graphics.Typeface.BOLD);
        btnCopy.setPadding(0, (int) (12 * getResources().getDisplayMetrics().density), 0, 0);
        btnCopy.setOnClickListener(v -> copyAccountInfo(account));
        contentLayout.addView(btnCopy);

        card.addView(contentLayout);
        layoutAccounts.addView(card);
    }

    private String getAccountTypeDisplay(String type) {
        switch (type) {
            case "admin": return "👨‍💼 ADMIN";
            case "premium": return "👑 USER PREMIUM";
            case "user": return "👤 USER THƯỜNG";
            case "demo": return "🎭 USER DEMO";
            default: return type.toUpperCase();
        }
    }

    private int getAccountTypeColor(String type) {
        switch (type) {
            case "admin": return 0xFFE91E63;
            case "premium": return 0xFFFFD700;
            case "user": return 0xFF2196F3;
            case "demo": return 0xFF4CAF50;
            default: return 0xFF666666;
        }
    }

    private void copyAccountInfo(DefaultAccountManager.AccountInfo account) {
        String info = String.format("Email: %s\nPassword: %s\nType: %s\nName: %s", 
                account.email, account.password, account.type, account.name);
        
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Account Info", info);
        clipboard.setPrimaryClip(clip);
        
        Toast.makeText(this, "Đã copy thông tin tài khoản!", Toast.LENGTH_SHORT).show();
    }
}