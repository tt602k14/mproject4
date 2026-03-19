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
import com.htdgym.app.utils.ValidationUtils;

public class ChangePasswordDialog extends Dialog {

    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnSave, btnCancel;
    private User currentUser;

    public ChangePasswordDialog(@NonNull Context context, User user) {
        super(context);
        this.currentUser = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_change_password);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> changePassword());
        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(currentPassword)) {
            etCurrentPassword.setError("Vui lòng nhập mật khẩu hiện tại");
            etCurrentPassword.requestFocus();
            return;
        }

        if (!currentPassword.equals(currentUser.getPassword())) {
            etCurrentPassword.setError("Mật khẩu hiện tại không đúng");
            etCurrentPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError("Vui lòng nhập mật khẩu mới");
            etNewPassword.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidPassword(newPassword)) {
            etNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etNewPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        if (currentPassword.equals(newPassword)) {
            etNewPassword.setError("Mật khẩu mới phải khác mật khẩu hiện tại");
            etNewPassword.requestFocus();
            return;
        }

        // Update password in database
        new Thread(() -> {
            try {
                currentUser.setPassword(newPassword);
                GymDatabase db = GymDatabase.getInstance(getContext());
                db.userDao().update(currentUser);

                ((android.app.Activity) getContext()).runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
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
