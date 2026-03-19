package com.htdgym.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.htdgym.app.R;
import com.htdgym.app.utils.SharedPrefManager;

public class UnitsDialog extends Dialog {

    private RadioGroup radioGroupUnits;
    private Button btnSave, btnCancel;

    public UnitsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_units);

        initViews();
        loadCurrentUnits();
        setupClickListeners();
    }

    private void initViews() {
        radioGroupUnits = findViewById(R.id.radio_group_units);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void loadCurrentUnits() {
        String currentUnits = SharedPrefManager.getInstance(getContext())
                .getSharedPreferences().getString("units_system", "metric");

        if (currentUnits.equals("metric")) {
            radioGroupUnits.check(R.id.radio_metric);
        } else {
            radioGroupUnits.check(R.id.radio_imperial);
        }
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveUnits());
        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void saveUnits() {
        int selectedId = radioGroupUnits.getCheckedRadioButtonId();
        String unitsSystem = selectedId == R.id.radio_metric ? "metric" : "imperial";
        String unitsName = selectedId == R.id.radio_metric ? "Hệ mét (kg, cm)" : "Hệ Anh (lb, in)";

        SharedPrefManager.getInstance(getContext())
                .getSharedPreferences()
                .edit()
                .putString("units_system", unitsSystem)
                .apply();

        Toast.makeText(getContext(), "Đã chọn: " + unitsName, Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
