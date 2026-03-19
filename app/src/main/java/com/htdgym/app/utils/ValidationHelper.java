package com.htdgym.app.utils;

import android.util.Patterns;
import android.widget.EditText;

/**
 * Utility class for input validation
 */
public class ValidationHelper {
    
    public static boolean validateEmail(EditText editText) {
        String email = editText.getText().toString().trim();
        
        if (email.isEmpty()) {
            editText.setError("Vui lòng nhập email");
            editText.requestFocus();
            return false;
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText.setError("Email không hợp lệ");
            editText.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public static boolean validatePassword(EditText editText) {
        String password = editText.getText().toString();
        
        if (password.isEmpty()) {
            editText.setError("Vui lòng nhập mật khẩu");
            editText.requestFocus();
            return false;
        }
        
        if (password.length() < 6) {
            editText.setError("Mật khẩu phải có ít nhất 6 ký tự");
            editText.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public static boolean validateName(EditText editText) {
        String name = editText.getText().toString().trim();
        
        if (name.isEmpty()) {
            editText.setError("Vui lòng nhập họ tên");
            editText.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public static boolean validateRequired(EditText editText, String fieldName) {
        String value = editText.getText().toString().trim();
        
        if (value.isEmpty()) {
            editText.setError("Vui lòng nhập " + fieldName);
            editText.requestFocus();
            return false;
        }
        
        return true;
    }
}