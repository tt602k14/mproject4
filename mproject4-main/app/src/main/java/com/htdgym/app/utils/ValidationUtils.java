package com.htdgym.app.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidationUtils {

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validate password (minimum 6 characters)
     */
    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    /**
     * Validate name (minimum 2 characters)
     */
    public static boolean isValidName(String name) {
        return !TextUtils.isEmpty(name) && name.length() >= 2;
    }

    /**
     * Validate phone number (Vietnamese format)
     */
    public static boolean isValidPhone(String phone) {
        if (TextUtils.isEmpty(phone)) return false;
        // Vietnamese phone: 10-11 digits, starts with 0
        return phone.matches("^0[0-9]{9,10}$");
    }

    /**
     * Validate positive number
     */
    public static boolean isPositiveNumber(String value) {
        try {
            double number = Double.parseDouble(value);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate positive integer
     */
    public static boolean isPositiveInteger(String value) {
        try {
            int number = Integer.parseInt(value);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate number in range
     */
    public static boolean isInRange(String value, double min, double max) {
        try {
            double number = Double.parseDouble(value);
            return number >= min && number <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate equipment name
     */
    public static boolean isValidEquipmentName(String name) {
        return !TextUtils.isEmpty(name) && name.length() >= 3;
    }

    /**
     * Validate member name
     */
    public static boolean isValidMemberName(String name) {
        return !TextUtils.isEmpty(name) && name.length() >= 2;
    }

    /**
     * Validate workout name
     */
    public static boolean isValidWorkoutName(String name) {
        return !TextUtils.isEmpty(name) && name.length() >= 3;
    }
}
