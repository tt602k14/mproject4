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
import com.htdgym.app.models.Payment;
import com.htdgym.app.viewmodels.PaymentViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPaymentActivity extends AppCompatActivity {
    private EditText etAmount, etDescription, etPaymentDate;
    private Spinner spinnerPaymentMethod, spinnerPaymentType;
    private Button btnSave, btnCancel;
    private PaymentViewModel paymentViewModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Date paymentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        initViews();
        setupSpinners();
        setupViewModel();
        setupClickListeners();
    }

    private void initViews() {
        etAmount = findViewById(R.id.et_payment_amount);
        etDescription = findViewById(R.id.et_payment_description);
        etPaymentDate = findViewById(R.id.et_payment_date);
        spinnerPaymentMethod = findViewById(R.id.spinner_payment_method);
        spinnerPaymentType = findViewById(R.id.spinner_payment_type);
        btnSave = findViewById(R.id.btn_save_payment);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void setupSpinners() {
        // Payment Method Spinner
        String[] paymentMethods = {"Tiền mặt", "Thẻ", "Chuyển khoản", "Ví điện tử"};
        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, paymentMethods);
        methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentMethod.setAdapter(methodAdapter);

        // Payment Type Spinner
        String[] paymentTypes = {"Phí thành viên", "Personal Training", "Phụ phí", "Khác"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, paymentTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentType.setAdapter(typeAdapter);
    }

    private void setupViewModel() {
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
    }

    private void setupClickListeners() {
        etPaymentDate.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> savePayment());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                paymentDate = calendar.getTime();
                etPaymentDate.setText(dateFormat.format(paymentDate));
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void savePayment() {
        String amountStr = etAmount.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String paymentMethod = spinnerPaymentMethod.getSelectedItem().toString();
        String paymentType = spinnerPaymentType.getSelectedItem().toString();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mô tả", Toast.LENGTH_SHORT).show();
            return;
        }

        if (paymentDate == null) {
            Toast.makeText(this, "Vui lòng chọn ngày thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // For now, using memberId = 1 as default. In real app, you'd select member
        Payment payment = new Payment(1, amount, paymentDate, paymentMethod, description, paymentType);
        paymentViewModel.insert(payment);

        Toast.makeText(this, "Đã thêm thanh toán thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}