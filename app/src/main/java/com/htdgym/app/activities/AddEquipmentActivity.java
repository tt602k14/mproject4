package com.htdgym.app.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.htdgym.app.R;
import com.htdgym.app.models.Equipment;
import com.htdgym.app.viewmodels.EquipmentViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEquipmentActivity extends AppCompatActivity {
    private EditText etName, etBrand, etPurchasePrice, etLocation, etNotes, etPurchaseDate;
    private Spinner spinnerCategory, spinnerCondition;
    private Button btnSave, btnCancel;
    private EquipmentViewModel equipmentViewModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Date purchaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);

        initViews();
        setupSpinners();
        setupViewModel();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.et_equipment_name);
        etBrand = findViewById(R.id.et_equipment_brand);
        etPurchasePrice = findViewById(R.id.et_purchase_price);
        etLocation = findViewById(R.id.et_equipment_location);
        etNotes = findViewById(R.id.et_equipment_notes);
        etPurchaseDate = findViewById(R.id.et_purchase_date);
        spinnerCategory = findViewById(R.id.spinner_equipment_category);
        spinnerCondition = findViewById(R.id.spinner_equipment_condition);
        btnSave = findViewById(R.id.btn_save_equipment);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void setupSpinners() {
        // Category Spinner
        String[] categories = {"Cardio", "Sức mạnh", "Tự do", "Chức năng", "Khác"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Condition Spinner
        String[] conditions = {"Xuất sắc", "Tốt", "Khá", "Kém"};
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, conditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCondition.setAdapter(conditionAdapter);
    }

    private void setupViewModel() {
        equipmentViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);
    }

    private void setupClickListeners() {
        etPurchaseDate.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> saveEquipment());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                purchaseDate = calendar.getTime();
                etPurchaseDate.setText(dateFormat.format(purchaseDate));
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveEquipment() {
        String name = etName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String priceStr = etPurchasePrice.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String condition = spinnerCondition.getSelectedItem().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên thiết bị", Toast.LENGTH_SHORT).show();
            return;
        }

        if (location.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập vị trí thiết bị", Toast.LENGTH_SHORT).show();
            return;
        }

        if (purchaseDate == null) {
            Toast.makeText(this, "Vui lòng chọn ngày mua", Toast.LENGTH_SHORT).show();
            return;
        }

        double purchasePrice = priceStr.isEmpty() ? 0.0 : Double.parseDouble(priceStr);

        Equipment equipment = new Equipment(name, category, brand, purchaseDate, purchasePrice, condition, location);
        equipment.setNotes(notes);
        
        equipmentViewModel.insert(equipment);

        Toast.makeText(this, "Đã thêm thiết bị thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}