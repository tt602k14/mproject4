package com.htdgym.app.fragments;

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
import com.htdgym.app.utils.AdminManager;

public class AdminAnalyticsFragment extends Fragment {
    
    private TextView tvUserGrowth, tvRevenueGrowth;
    private TextView tvDailyActiveUsers, tvAvgSessionDuration, tvRetentionRate;
    private LinearLayout layoutFeatureUsage;
    private Button btnExportUserReport, btnExportRevenueReport;
    
    private AdminManager adminManager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_analytics, container, false);
        
        adminManager = AdminManager.getInstance(requireContext());
        
        initViews(view);
        setupClickListeners();
        loadAnalyticsData();
        
        return view;
    }
    
    private void initViews(View view) {
        tvUserGrowth = view.findViewById(R.id.tv_user_growth);
        tvRevenueGrowth = view.findViewById(R.id.tv_revenue_growth);
        tvDailyActiveUsers = view.findViewById(R.id.tv_daily_active_users);
        tvAvgSessionDuration = view.findViewById(R.id.tv_avg_session_duration);
        tvRetentionRate = view.findViewById(R.id.tv_retention_rate);
        layoutFeatureUsage = view.findViewById(R.id.layout_feature_usage);
        btnExportUserReport = view.findViewById(R.id.btn_export_user_report);
        btnExportRevenueReport = view.findViewById(R.id.btn_export_revenue_report);
    }
    
    private void setupClickListeners() {
        btnExportUserReport.setOnClickListener(v -> exportUserReport());
        btnExportRevenueReport.setOnClickListener(v -> exportRevenueReport());
    }
    
    private void loadAnalyticsData() {
        // Load sample analytics data
        loadGrowthMetrics();
        loadUsageStatistics();
        loadFeatureUsage();
    }
    
    private void loadGrowthMetrics() {
        // Sample growth data
        tvUserGrowth.setText("+12.5%");
        tvRevenueGrowth.setText("+18.3%");
    }
    
    private void loadUsageStatistics() {
        // Sample usage data
        tvDailyActiveUsers.setText("456");
        tvAvgSessionDuration.setText("12.5m");
        tvRetentionRate.setText("78%");
    }
    
    private void loadFeatureUsage() {
        layoutFeatureUsage.removeAllViews();
        
        // Sample feature usage data
        addFeatureUsageItem("🏋️ Workout Sessions", "2,340 lượt", "85%");
        addFeatureUsageItem("🍎 Nutrition Tracking", "1,890 lượt", "68%");
        addFeatureUsageItem("👥 Community", "1,456 lượt", "52%");
        addFeatureUsageItem("🤖 AI Coach", "987 lượt", "35%");
        addFeatureUsageItem("💎 Premium Features", "654 lượt", "23%");
    }
    
    private void addFeatureUsageItem(String featureName, String usage, String percentage) {
        View itemView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_feature_usage, layoutFeatureUsage, false);
        
        TextView tvFeatureName = itemView.findViewById(R.id.tv_feature_name);
        TextView tvUsage = itemView.findViewById(R.id.tv_usage);
        TextView tvPercentage = itemView.findViewById(R.id.tv_percentage);
        View progressBar = itemView.findViewById(R.id.progress_bar);
        
        tvFeatureName.setText(featureName);
        tvUsage.setText(usage);
        tvPercentage.setText(percentage);
        
        // Set progress bar width based on percentage
        int progressWidth = (int) (200 * (Float.parseFloat(percentage.replace("%", "")) / 100));
        ViewGroup.LayoutParams params = progressBar.getLayoutParams();
        params.width = progressWidth;
        progressBar.setLayoutParams(params);
        
        layoutFeatureUsage.addView(itemView);
    }
    
    private void exportUserReport() {
        Toast.makeText(requireContext(), "📈 Đang xuất báo cáo người dùng...", Toast.LENGTH_SHORT).show();
        // TODO: Implement user report export
    }
    
    private void exportRevenueReport() {
        Toast.makeText(requireContext(), "💰 Đang xuất báo cáo doanh thu...", Toast.LENGTH_SHORT).show();
        // TODO: Implement revenue report export
    }
}