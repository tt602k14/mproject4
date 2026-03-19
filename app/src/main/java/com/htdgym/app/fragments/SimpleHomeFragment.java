package com.htdgym.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SimpleHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        
        // Create a simple layout programmatically to avoid layout issues
        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);
        layout.setBackgroundColor(0xFF121212);
        
        TextView title = new TextView(requireContext());
        title.setText("HTD Gym - Trang chủ");
        title.setTextSize(24);
        title.setTextColor(0xFFFFFFFF);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
        
        TextView subtitle = new TextView(requireContext());
        subtitle.setText("Chào mừng bạn đến với HTD Gym!");
        subtitle.setTextSize(16);
        subtitle.setTextColor(0xFFCCCCCC);
        subtitle.setPadding(0, 0, 0, 20);
        layout.addView(subtitle);
        
        TextView status = new TextView(requireContext());
        status.setText("✅ Ứng dụng hoạt động bình thường");
        status.setTextSize(14);
        status.setTextColor(0xFF4CAF50);
        layout.addView(status);
        
        return layout;
    }
}