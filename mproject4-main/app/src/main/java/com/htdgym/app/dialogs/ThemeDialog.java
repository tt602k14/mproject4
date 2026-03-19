package com.htdgym.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.utils.ThemeHelper;

public class ThemeDialog extends Dialog {

    private CardView cardLightMode, cardDarkMode, cardAutoMode;
    private ImageView checkLight, checkDark, checkAuto;
    private int currentMode;

    public ThemeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_theme);

        initViews();
        loadCurrentTheme();
        setupClickListeners();
    }

    private void initViews() {
        cardLightMode = findViewById(R.id.card_light_mode);
        cardDarkMode = findViewById(R.id.card_dark_mode);
        cardAutoMode = findViewById(R.id.card_auto_mode);
        
        checkLight = findViewById(R.id.check_light);
        checkDark = findViewById(R.id.check_dark);
        checkAuto = findViewById(R.id.check_auto);
    }

    private void loadCurrentTheme() {
        currentMode = ThemeHelper.getThemeMode(getContext());
        updateCheckmarks();
    }

    private void setupClickListeners() {
        cardLightMode.setOnClickListener(v -> selectTheme(ThemeHelper.MODE_LIGHT));
        cardDarkMode.setOnClickListener(v -> selectTheme(ThemeHelper.MODE_DARK));
        cardAutoMode.setOnClickListener(v -> selectTheme(ThemeHelper.MODE_AUTO));
    }

    private void selectTheme(int mode) {
        currentMode = mode;
        ThemeHelper.saveThemeMode(getContext(), mode);
        updateCheckmarks();
        
        // Close dialog after a short delay
        cardLightMode.postDelayed(() -> dismiss(), 300);
    }

    private void updateCheckmarks() {
        checkLight.setVisibility(currentMode == ThemeHelper.MODE_LIGHT ? View.VISIBLE : View.GONE);
        checkDark.setVisibility(currentMode == ThemeHelper.MODE_DARK ? View.VISIBLE : View.GONE);
        checkAuto.setVisibility(currentMode == ThemeHelper.MODE_AUTO ? View.VISIBLE : View.GONE);
    }
}
