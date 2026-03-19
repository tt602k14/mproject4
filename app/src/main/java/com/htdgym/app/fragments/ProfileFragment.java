package com.htdgym.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.htdgym.app.R;
import com.htdgym.app.activities.LoginActivity;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName, tvProfileEmail;
    private Button btnEditProfile;
    private LinearLayout layoutChangePassword, layoutPrivacy;
    private LinearLayout layoutNotifications, layoutLanguage, layoutUnits, layoutTheme;
    private LinearLayout layoutHelp, layoutFeedback, layoutAbout;
    private Button btnLogout;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        initViews(view);
        loadUserData();
        setupClickListeners();
        
        return view;
    }

    private void initViews(View view) {
        tvProfileName = view.findViewById(R.id.tv_profile_name);
        tvProfileEmail = view.findViewById(R.id.tv_profile_email);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        
        layoutChangePassword = view.findViewById(R.id.layout_change_password);
        layoutPrivacy = view.findViewById(R.id.layout_privacy);
        
        layoutNotifications = view.findViewById(R.id.layout_notifications);
        layoutLanguage = view.findViewById(R.id.layout_language);
        layoutUnits = view.findViewById(R.id.layout_units);
        layoutTheme = view.findViewById(R.id.layout_theme);
        
        layoutHelp = view.findViewById(R.id.layout_help);
        layoutFeedback = view.findViewById(R.id.layout_feedback);
        layoutAbout = view.findViewById(R.id.layout_about);
        
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    private void loadUserData() {
        // Sample data - replace with actual user data from SharedPreferences or database
        tvProfileName.setText("Hoàng Tuấn");
        tvProfileEmail.setText("hoangtuan@htdgym.com");
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Mở form chỉnh sửa hồ sơ", Toast.LENGTH_SHORT).show();
            // TODO: Open edit profile dialog or activity
        });
        
        layoutChangePassword.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Mở form đổi mật khẩu", Toast.LENGTH_SHORT).show();
            // TODO: Open change password dialog
        });
        
        layoutPrivacy.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Cài đặt quyền riêng tư", Toast.LENGTH_SHORT).show();
            // TODO: Open privacy settings
        });
        
        layoutNotifications.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Cài đặt thông báo", Toast.LENGTH_SHORT).show();
            // TODO: Open notification settings
        });
        
        layoutLanguage.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Chọn ngôn ngữ", Toast.LENGTH_SHORT).show();
            // TODO: Open language selection dialog
        });
        
        layoutUnits.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Chọn đơn vị đo lường", Toast.LENGTH_SHORT).show();
            // TODO: Open units selection dialog
        });
        
        layoutTheme.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Chọn giao diện", Toast.LENGTH_SHORT).show();
            // TODO: Open theme selection dialog
        });
        
        layoutHelp.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Trung tâm trợ giúp", Toast.LENGTH_SHORT).show();
            // TODO: Open help center
        });
        
        layoutFeedback.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Gửi phản hồi", Toast.LENGTH_SHORT).show();
            // TODO: Open feedback form
        });
        
        layoutAbout.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "HTD Gym App v1.0.0\n© 2026 HTD Gym", 
                    Toast.LENGTH_LONG).show();
            // TODO: Open about page
        });
        
        btnLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        // TODO: Clear user session from SharedPreferences
        Toast.makeText(requireContext(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
        
        // Navigate to login screen
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
