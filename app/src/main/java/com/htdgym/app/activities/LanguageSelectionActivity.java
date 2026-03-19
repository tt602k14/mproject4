package com.htdgym.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.R;

public class LanguageSelectionActivity extends AppCompatActivity {

    private LinearLayout layoutInitial, layoutLanguageList;
    private RadioGroup radioGroupLanguages;
    private TextView btnDone;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        sharedPreferences = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        layoutInitial = findViewById(R.id.layout_initial);
        layoutLanguageList = findViewById(R.id.layout_language_list);
        radioGroupLanguages = findViewById(R.id.radio_group_languages);
        btnDone = findViewById(R.id.btn_done);

        // Load saved language
        String savedLanguage = sharedPreferences.getString("selected_language", "vi");
        setSelectedLanguage(savedLanguage);
    }

    private void setSelectedLanguage(String languageCode) {
        switch (languageCode) {
            case "fr":
                radioGroupLanguages.check(R.id.radio_french);
                break;
            case "de":
                radioGroupLanguages.check(R.id.radio_german);
                break;
            case "es":
                radioGroupLanguages.check(R.id.radio_spanish);
                break;
            case "pt":
                radioGroupLanguages.check(R.id.radio_portuguese);
                break;
            case "zh":
                radioGroupLanguages.check(R.id.radio_chinese);
                break;
            case "ru":
                radioGroupLanguages.check(R.id.radio_russian);
                break;
            case "vi":
            default:
                radioGroupLanguages.check(R.id.radio_vietnamese);
                break;
        }
    }

    private void setupClickListeners() {
        // Click on initial screen to show language list
        layoutInitial.setOnClickListener(v -> {
            layoutInitial.setVisibility(View.GONE);
            layoutLanguageList.setVisibility(View.VISIBLE);
        });

        // Done button
        btnDone.setOnClickListener(v -> {
            String selectedLanguage = getSelectedLanguage();
            
            // Save language preference
            sharedPreferences.edit()
                    .putString("selected_language", selectedLanguage)
                    .putBoolean("language_selected", true)
                    .apply();

            Toast.makeText(this, "Ngôn ngữ đã được chọn", Toast.LENGTH_SHORT).show();

            // Navigate to Welcome screen
            Intent intent = new Intent(LanguageSelectionActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private String getSelectedLanguage() {
        int selectedId = radioGroupLanguages.getCheckedRadioButtonId();
        
        if (selectedId == R.id.radio_french) {
            return "fr";
        } else if (selectedId == R.id.radio_german) {
            return "de";
        } else if (selectedId == R.id.radio_spanish) {
            return "es";
        } else if (selectedId == R.id.radio_portuguese) {
            return "pt";
        } else if (selectedId == R.id.radio_chinese) {
            return "zh";
        } else if (selectedId == R.id.radio_russian) {
            return "ru";
        } else {
            return "vi";
        }
    }

    @Override
    public void onBackPressed() {
        if (layoutLanguageList.getVisibility() == View.VISIBLE) {
            // Go back to initial screen
            layoutLanguageList.setVisibility(View.GONE);
            layoutInitial.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}
