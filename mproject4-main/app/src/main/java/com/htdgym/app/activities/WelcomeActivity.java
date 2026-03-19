package com.htdgym.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.MainActivity;
import com.htdgym.app.R;
import com.htdgym.app.activities.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    private CardView btnStart;
    private LinearLayout layoutLanguageSelector;
    private TextView tvSelectedLanguage;
    private SharedPreferences sharedPreferences;

    private String[] languages = {"Tiếng Việt", "English", "Français", "Deutsch", "Español", "Português", "中文", "Русский язык"};
    private String[] languageCodes = {"vi", "en", "fr", "de", "es", "pt", "zh", "ru"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedPreferences = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);

        initViews();
        loadSelectedLanguage();
        setupClickListeners();
    }

    private void initViews() {
        btnStart = findViewById(R.id.btn_start);
        layoutLanguageSelector = findViewById(R.id.layout_language_selector);
        tvSelectedLanguage = findViewById(R.id.tv_selected_language);
    }

    private void loadSelectedLanguage() {
        String savedLanguage = sharedPreferences.getString("selected_language", "vi");
        updateLanguageDisplay(savedLanguage);
    }

    private void updateLanguageDisplay(String languageCode) {
        for (int i = 0; i < languageCodes.length; i++) {
            if (languageCodes[i].equals(languageCode)) {
                tvSelectedLanguage.setText(languages[i]);
                break;
            }
        }
    }

    private void setupClickListeners() {
        // Language selector click
        layoutLanguageSelector.setOnClickListener(v -> showLanguageDialog());

        // Start button click
        btnStart.setOnClickListener(v -> {
            // Mark as not first time anymore
            sharedPreferences.edit()
                    .putBoolean("isFirstTime", false)
                    .apply();

            // Navigate to Login screen with tabs
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showLanguageDialog() {
        String currentLanguage = sharedPreferences.getString("selected_language", "vi");
        int selectedIndex = 0;
        
        // Find current language index
        for (int i = 0; i < languageCodes.length; i++) {
            if (languageCodes[i].equals(currentLanguage)) {
                selectedIndex = i;
                break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ngôn ngữ");
        builder.setSingleChoiceItems(languages, selectedIndex, (dialog, which) -> {
            // Save selected language
            sharedPreferences.edit()
                    .putString("selected_language", languageCodes[which])
                    .putBoolean("language_selected", true)
                    .apply();

            // Update display
            updateLanguageDisplay(languageCodes[which]);

            dialog.dismiss();
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
