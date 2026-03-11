package com.htdgym.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.User;
import com.htdgym.app.utils.SharedPrefManager;
import com.htdgym.app.utils.ValidationUtils;

public class EditProfileDialog extends Dialog {

    private EditText etName, etEmail, etPhone;
    private Button btnSave, btnCancel;
    private OnProfileUpdatedListener listener;
    private User currentUser;

    public interface OnProfileUpdatedListener {
        void onProfileUpdated(String name, String email);
    }

    public EditProfileDialog(@NonNull Context context, User user, OnProfileUpdatedListener listener) {
        super(context);
        this.currentUser = user;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_profile);

        initViews();
        loadUserData();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void loadUserData() {
        if (currentUser != null) {
            etName.setText(currentUser.getName());
            etEmail.setText(currentUser.getEmail());
            etPhone.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
        }
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveProfile());
        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(name)) {
            etName.setError("Vui lòng nhập tên");
            etName.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidName(name)) {
            etName.setError("Tên phải có ít nhất 2 ký tự");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(phone) && !ValidationUtils.isValidPhone(phone)) {
            etPhone.setError("Số điện thoại không hợp lệ");
            etPhone.requestFocus();
            return;
        }

        // Update user in database
        new Thread(() -> {
            try {
                currentUser.setName(name);
                currentUser.setEmail(email);
                currentUser.setPhone(phone);

                GymDatabase db = GymDatabase.getInstance(getContext());
                db.userDao().update(currentUser);

                // Update SharedPreferences
                SharedPrefManager.getInstance(getContext()).saveUserName(name);
                SharedPrefManager.getInstance(getContext()).saveUserEmail(email);

                ((android.app.Activity) getContext()).runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onProfileUpdated(name, email);
                    }
                    dismiss();
                });
            } catch (Exception e) {
                ((android.app.Activity) getContext()).runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
