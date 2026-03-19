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
import com.htdgym.app.models.Member;
import com.htdgym.app.viewmodels.MemberViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMemberActivity extends AppCompatActivity {
    private EditText etName, etPhone, etEmail, etAddress, etJoinDate, etExpiryDate;
    private Spinner spinnerMembershipType;
    private Button btnSave, btnCancel;
    private MemberViewModel memberViewModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Date joinDate, expiryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        initViews();
        setupSpinner();
        setupViewModel();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.et_member_name);
        etPhone = findViewById(R.id.et_member_phone);
        etEmail = findViewById(R.id.et_member_email);
        etAddress = findViewById(R.id.et_member_address);
        etJoinDate = findViewById(R.id.et_join_date);
        etExpiryDate = findViewById(R.id.et_expiry_date);
        spinnerMembershipType = findViewById(R.id.spinner_membership_type);
        btnSave = findViewById(R.id.btn_save_member);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void setupSpinner() {
        String[] membershipTypes = {"Tháng", "3 Tháng", "6 Tháng", "1 Năm", "VIP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, membershipTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMembershipType.setAdapter(adapter);
    }

    private void setupViewModel() {
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
    }

    private void setupClickListeners() {
        etJoinDate.setOnClickListener(v -> showDatePicker(true));
        etExpiryDate.setOnClickListener(v -> showDatePicker(false));
        
        btnSave.setOnClickListener(v -> saveMember());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker(boolean isJoinDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                Date selectedDate = calendar.getTime();
                String formattedDate = dateFormat.format(selectedDate);
                
                if (isJoinDate) {
                    joinDate = selectedDate;
                    etJoinDate.setText(formattedDate);
                } else {
                    expiryDate = selectedDate;
                    etExpiryDate.setText(formattedDate);
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveMember() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String membershipType = spinnerMembershipType.getSelectedItem().toString();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (joinDate == null || expiryDate == null) {
            Toast.makeText(this, "Vui lòng chọn ngày tham gia và ngày hết hạn", Toast.LENGTH_SHORT).show();
            return;
        }

        Member member = new Member(name, phone, email, address, joinDate, expiryDate, membershipType);
        memberViewModel.insert(member);

        Toast.makeText(this, "Đã thêm thành viên thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}