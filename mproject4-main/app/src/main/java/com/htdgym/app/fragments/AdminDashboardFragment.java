package com.htdgym.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.htdgym.app.R;
import com.htdgym.app.activities.AdminDashboardActivity;
// import com.htdgym.app.fragments.AdminPaymentManagementFragment;
import com.htdgym.app.fragments.AdminAnalyticsFragment;
import com.htdgym.app.models.Admin;
import com.htdgym.app.utils.AdminManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminDashboardFragment extends Fragment {
    
    private TextView tvWelcomeAdmin, tvLastLogin;
    private TextView tvTotalUsers, tvPremiumUsers, tvTotalRevenue, tvActiveToday;
    private CardView cardManageUsers, cardViewPayments, cardViewAnalytics;
    private LinearLayout layoutRecentActivities;
    private TextView tvViewAllActivities;
    
    private AdminManager adminManager;
    private Admin currentAdmin;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        
        adminManager = AdminManager.getInstance(requireContext());
        currentAdmin = adminManager.getCurrentAdmin();
        
        initViews(view);
        setupClickListeners();
        loadDashboardData();
        
        return view;
    }
    
    private void initViews(View view) {
        tvWelcomeAdmin = view.findViewById(R.id.tv_welcome_admin);
        tvLastLogin = view.findViewById(R.id.tv_last_login);
        tvTotalUsers = view.findViewById(R.id.tv_total_users);
        tvPremiumUsers = view.findViewById(R.id.tv_premium_users);
        tvTotalRevenue = view.findViewById(R.id.tv_total_revenue);
        tvActiveToday = view.findViewById(R.id.tv_active_today);
        cardManageUsers = view.findViewById(R.id.card_manage_users);
        cardViewPayments = view.findViewById(R.id.card_view_payments);
        cardViewAnalytics = view.findViewById(R.id.card_view_analytics);
        layoutRecentActivities = view.findViewById(R.id.layout_recent_activities);
        tvViewAllActivities = view.findViewById(R.id.tv_view_all_activities);
    }
    
    private void setupClickListeners() {
        cardManageUsers.setOnClickListener(v -> {
            if (adminManager.hasPermission("manage_users")) {
                // Navigate to user management
                ((AdminDashboardActivity) requireActivity())
                        .onNavigationItemSelected(((AdminDashboardActivity) requireActivity())
                                .findViewById(R.id.nav_user_management));
            } else {
                showPermissionDenied();
            }
        });
        
        cardViewPayments.setOnClickListener(v -> {
            if (adminManager.hasPermission("manage_payments")) {
                // Navigate to payment management
                // ((AdminDashboardActivity) requireActivity()).getSupportFragmentManager()
                //         .beginTransaction()
                //         .replace(R.id.content_frame, new AdminPaymentManagementFragment())
                //         .commit();
                // ((AdminDashboardActivity) requireActivity()).getSupportActionBar()
                //         .setTitle("💳 Quản lý thanh toán");
                android.widget.Toast.makeText(requireContext(), "💳 Tính năng đang phát triển", android.widget.Toast.LENGTH_SHORT).show();
            } else {
                showPermissionDenied();
            }
        });
        
        cardViewAnalytics.setOnClickListener(v -> {
            if (adminManager.hasPermission("view_analytics")) {
                // Navigate to analytics
                ((AdminDashboardActivity) requireActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new AdminAnalyticsFragment())
                        .commit();
                ((AdminDashboardActivity) requireActivity()).getSupportActionBar()
                        .setTitle("📈 Thống kê & Báo cáo");
            } else {
                showPermissionDenied();
            }
        });
        
        tvViewAllActivities.setOnClickListener(v -> {
            // TODO: Navigate to full activity log
        });
    }
    
    private void loadDashboardData() {
        // Update welcome message
        if (currentAdmin != null) {
            tvWelcomeAdmin.setText("Chào mừng " + currentAdmin.getFullName() + "! 👑");
            
            // Format last login
            if (currentAdmin.getLastLogin() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                tvLastLogin.setText("Lần đăng nhập cuối: " + sdf.format(currentAdmin.getLastLogin()));
            }
        }
        
        // Load user statistics
        adminManager.getUserStats(new AdminManager.UserStatsCallback() {
            @Override
            public void onSuccess(AdminManager.UserStats stats) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        tvTotalUsers.setText(String.valueOf(stats.totalUsers));
                        tvPremiumUsers.setText(String.valueOf(stats.premiumUsers));
                        tvActiveToday.setText(String.valueOf(stats.newUsersToday));
                        
                        // Calculate estimated revenue (example calculation)
                        long estimatedRevenue = stats.premiumUsers * 400000L; // Average premium price
                        tvTotalRevenue.setText(formatCurrency(estimatedRevenue));
                    });
                }
            }
            
            @Override
            public void onError(String error) {
                // Handle error - show default values
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        tvTotalUsers.setText("--");
                        tvPremiumUsers.setText("--");
                        tvActiveToday.setText("--");
                        tvTotalRevenue.setText("--");
                    });
                }
            }
        });
        
        // Load recent activities
        loadRecentActivities();
    }
    
    private void loadRecentActivities() {
        // Clear existing activities
        layoutRecentActivities.removeAllViews();
        
        // Add sample activities (in real app, load from database)
        addActivityItem("👤 Người dùng mới đăng ký", "Nguyễn Văn A vừa tạo tài khoản", "5 phút trước");
        addActivityItem("💎 Nâng cấp Premium", "Trần Thị B đã mua gói Premium 7 tháng", "15 phút trước");
        addActivityItem("💳 Thanh toán thành công", "Lê Văn C đã thanh toán 400,000 VND", "30 phút trước");
        addActivityItem("🏋️ Hoàn thành workout", "Phạm Thị D đã hoàn thành bài tập ngực", "1 giờ trước");
        addActivityItem("🎯 Tham gia thử thách", "Hoàng Văn E tham gia thử thách 30 ngày squat", "2 giờ trước");
    }
    
    private void addActivityItem(String title, String description, String time) {
        View activityView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_admin_activity, layoutRecentActivities, false);
        
        TextView tvTitle = activityView.findViewById(R.id.tv_activity_title);
        TextView tvDescription = activityView.findViewById(R.id.tv_activity_description);
        TextView tvTime = activityView.findViewById(R.id.tv_activity_time);
        
        tvTitle.setText(title);
        tvDescription.setText(description);
        tvTime.setText(time);
        
        layoutRecentActivities.addView(activityView);
    }
    
    private String formatCurrency(long amount) {
        if (amount >= 1000000) {
            return String.format("%.1fM VND", amount / 1000000.0);
        } else if (amount >= 1000) {
            return String.format("%.0fK VND", amount / 1000.0);
        } else {
            return amount + " VND";
        }
    }
    
    private void showPermissionDenied() {
        android.widget.Toast.makeText(requireContext(), 
                "❌ Bạn không có quyền truy cập tính năng này", 
                android.widget.Toast.LENGTH_SHORT).show();
    }
}