package com.htdgym.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.htdgym.app.R;
import com.htdgym.app.utils.SharedPrefManager;

public class LanguageDialog extends Dialog {

    private RadioGroup radioGroupLanguages;
    private Button btnSave, btnCancel;
    private OnLanguageSelectedListener listener;

    public interface OnLanguageSelectedListener {
        void onLanguageSelected(String language);
    }

    public LanguageDialog(@NonNull Context context, OnLanguageSelectedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_language);

        initViews();
        loadCurrentLanguage();
        setupClickListeners();
    }

    private void initViews() {
        radioGroupLanguages = findViewById(R.id.radio_group_languages);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void loadCurrentLanguage() {
        String currentLanguage = SharedPrefManager.getInstance(getContext())
                .getSharedPreferences().getString("selected_language", "vi");

        int selectedId = R.id.radio_vietnamese;
        switch (currentLanguage) {
            case "vi": selectedId = R.id.radio_vietnamese; break;
            case "en": selectedId = R.id.radio_english; break;
            case "fr": selectedId = R.id.radio_french; break;
            case "de": selectedId = R.id.radio_german; break;
        }
        radioGroupLanguages.check(selectedId);
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveLanguage());
        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void saveLanguage() {
        int selectedId = radioGroupLanguages.getCheckedRadioButtonId();
        String languageCode = "vi";
        String languageName = "Tiếng Việt";

        if (selectedId == R.id.radio_vietnamese) {
            languageCode = "vi";
            languageName = "Tiếng Việt";
        } else if (selectedId == R.id.radio_english) {
            languageCode = "en";
            languageName = "English";
        } else if (selectedId == R.id.radio_french) {
            languageCode = "fr";
            languageName = "Français";
        } else if (selectedId == R.id.radio_german) {
            languageCode = "de";
            languageName = "Deutsch";
        }

        // Save language and apply immediately
        com.htdgym.app.utils.LocaleHelper.setLocale(getContext(), languageCode);

        Toast.makeText(getContext(), "Language changed to: " + languageName, Toast.LENGTH_SHORT).show();
        
        if (listener != null) {
            listener.onLanguageSelected(languageName);
        }
        
        dismiss();
        
        // Restart activity to apply language
        if (getContext() instanceof android.app.Activity) {
            android.app.Activity activity = (android.app.Activity) getContext();
            activity.recreate();
        }
    }
}
