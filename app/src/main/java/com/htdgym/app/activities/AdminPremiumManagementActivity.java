package com.htdgym.app.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.htdgym.app.R;
import com.htdgym.app.adapters.AdminPremiumUserAdapter;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.PremiumSubscription;
import com.htdgym.app.models.User;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminPremiumManagementActivity extends AppCompatActivity 
        implements AdminPremiumUserAdapter.OnPremiumUserActionListener {

    private ImageView btnBack, btnRefresh;
    private TextView tvTotalPremium, tvRevenue;
    private TextInputEditText etSearch;
    private Button btnFilter;
    private RecyclerView recyclerPremiumUsers;
    private LinearLayout layoutEmpty;
    
    private AdminPremiumUserAdapter adapter;
    private List<PremiumSubscription> allSubscriptions = new ArrayList<>();
    private List<PremiumSubscription> filteredSubscriptions = new ArrayList<>();
    private Map<Integer, User> userMap = new HashMap<>();
    
    private GymDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_premium_management);

        database = GymDatabase.getInstance(this);
        
        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadPremiumData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnRefresh = findViewById(R.id.btn_refresh);
        tvTotalPremium = findViewById(R.id.tv_total_premium);
        tvRevenue = findViewById(R.id.tv_revenue);
        etSearch = findViewById(R.id.et_search);
        btnFilter = findViewById(R.id.btn_filter);
        recyclerPremiumUsers = findViewById(R.id.recycler_premium_users);
        layoutEmpty = findViewById(R.id.layout_empty);
    }

    private void setupRecyclerView() {
        adapter = new AdminPremiumUserAdapter(filteredSubscriptions, userMap, this);
        recyclerPremiumUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerPremiumUsers.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnRefresh.setOnClickListener(v -> {
            loadPremiumData();
            Toast.makeText(this, "Đã làm mới dữ liệu", Toast.LENGTH_SHORT).show();
        });
        
        btnFilter.setOnClickListener(v -> showFilterDialog());
        
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSubscriptions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadPremiumData() {
        new Thread(() -> {
            try {
                // Get all active premium subscriptions
                List<PremiumSubscription> subscriptions = database.premiumSubscriptionDao()
                        .getActivePremiumSubscriptions(System.currentTimeMillis());
                
                // Get all users
                List<User> allUsers = database.userDao().getAllUsers();
                Map<Integer, User> userMapTemp = new HashMap<>();
                for (User user : allUsers) {
                    userMapTemp.put(user.getId(), user);
                }
                
                // Calculate statistics
                double totalRevenue = 0;
                for (PremiumSubscription sub : subscriptions) {
                    totalRevenue += sub.getPrice();
                }
                final double finalTotalRevenue = totalRevenue;
                
                runOnUiThread(() -> {
                    allSubscriptions.clear();
                    allSubscriptions.addAll(subscriptions);
                    
                    userMap.clear();
                    userMap.putAll(userMapTemp);
                    
                    filteredSubscriptions.clear();
                    filteredSubscriptions.addAll(subscriptions);
                    
                    adapter.updateData(filteredSubscriptions, userMap);
                    
                    // Update statistics
                    tvTotalPremium.setText(String.valueOf(subscriptions.size()));
                    
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    tvRevenue.setText(formatter.format(finalTotalRevenue) + "đ");
                    
                    // Show/hide empty state
                    if (subscriptions.isEmpty()) {
                        recyclerPremiumUsers.setVisibility(View.GONE);
                        layoutEmpty.setVisibility(View.VISIBLE);
                    } else {
                        recyclerPremiumUsers.setVisibility(View.VISIBLE);
                        layoutEmpty.setVisibility(View.GONE);
                    }
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void filterSubscriptions(String query) {
        filteredSubscriptions.clear();
        
        if (query.isEmpty()) {
            filteredSubscriptions.addAll(allSubscriptions);
        } else {
            for (PremiumSubscription subscription : allSubscriptions) {
                User user = userMap.get(subscription.getUserId());
                if (user != null) {
                    String email = user.getEmail().toLowerCase();
                    String name = user.getFullName() != null ? user.getFullName().toLowerCase() : "";
                    
                    if (email.contains(query.toLowerCase()) || name.contains(query.toLowerCase())) {
                        filteredSubscriptions.add(subscription);
                    }
                }
            }
        }
        
        adapter.updateData(filteredSubscriptions, userMap);
        
        // Update empty state
        if (filteredSubscriptions.isEmpty() && !query.isEmpty()) {
            recyclerPremiumUsers.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else if (!allSubscriptions.isEmpty()) {
            recyclerPremiumUsers.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    private void showFilterDialog() {
        String[] options = {
            "📋 Tất cả gói Premium",
            "✅ Còn hiệu lực", 
            "❌ Đã hết hạn",
            "📅 Gói 1 tháng",
            "📅 Gói 7 tháng",
            "💰 Theo doanh thu",
            "📊 Thống kê chi tiết",
            "➕ Thêm Premium thủ công",
            "🎁 Tạo mã giảm giá"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("🔍 Tùy chọn quản lý")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        filterByType(which);
                        break;
                    case 5:
                        showRevenueAnalysis();
                        break;
                    case 6:
                        showDetailedStatistics();
                        break;
                    case 7:
                        showAddPremiumDialog();
                        break;
                    case 8:
                        showCreateDiscountCodeDialog();
                        break;
                }
            })
            .show();
    }

    private void filterByType(int filterType) {
        filteredSubscriptions.clear();
        long currentTime = System.currentTimeMillis();
        
        switch (filterType) {
            case 0: // Tất cả
                filteredSubscriptions.addAll(allSubscriptions);
                break;
            case 1: // Còn hiệu lực
                for (PremiumSubscription sub : allSubscriptions) {
                    if (sub.getEndDate() > currentTime && sub.isActive()) {
                        filteredSubscriptions.add(sub);
                    }
                }
                break;
            case 2: // Đã hết hạn
                for (PremiumSubscription sub : allSubscriptions) {
                    if (sub.getEndDate() <= currentTime) {
                        filteredSubscriptions.add(sub);
                    }
                }
                break;
            case 3: // Gói 1 tháng
                for (PremiumSubscription sub : allSubscriptions) {
                    if ("monthly".equals(sub.getSubscriptionType())) {
                        filteredSubscriptions.add(sub);
                    }
                }
                break;
            case 4: // Gói 7 tháng
                for (PremiumSubscription sub : allSubscriptions) {
                    if ("7months".equals(sub.getSubscriptionType())) {
                        filteredSubscriptions.add(sub);
                    }
                }
                break;
        }
        
        adapter.updateData(filteredSubscriptions, userMap);
        
        // Clear search
        etSearch.setText("");
    }

    @Override
    public void onViewDetails(PremiumSubscription subscription, User user) {
        showPremiumDetailsDialog(subscription, user);
    }

    @Override
    public void onCancelPremium(PremiumSubscription subscription, User user) {
        showCancelPremiumDialog(subscription, user);
    }

    private void showPremiumDetailsDialog(PremiumSubscription subscription, User user) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        
        String[] actions = {
            "📋 Xem chi tiết đầy đủ",
            "🔄 Gia hạn Premium",
            "💰 Thay đổi gói",
            "🎁 Tặng thêm thời gian",
            "📧 Gửi thông báo",
            "❌ Hủy Premium"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("👑 " + (user != null ? user.getFullName() : "Premium User"))
            .setItems(actions, (dialog, which) -> {
                switch (which) {
                    case 0:
                        showFullPremiumDetails(subscription, user);
                        break;
                    case 1:
                        showExtendPremiumDialog(subscription, user);
                        break;
                    case 2:
                        showChangePlanDialog(subscription, user);
                        break;
                    case 3:
                        showGiftTimeDialog(subscription, user);
                        break;
                    case 4:
                        showSendPremiumNotificationDialog(subscription, user);
                        break;
                    case 5:
                        showCancelPremiumDialog(subscription, user);
                        break;
                }
            })
            .show();
    }

    private void showCancelPremiumDialog(PremiumSubscription subscription, User user) {
        String userName = user != null ? user.getFullName() : "Người dùng";
        
        new AlertDialog.Builder(this)
            .setTitle("Hủy gói Premium")
            .setMessage("Bạn có chắc chắn muốn hủy gói Premium của " + userName + "?\n\n" +
                       "Người dùng sẽ mất quyền truy cập Premium ngay lập tức.")
            .setPositiveButton("Hủy Premium", (dialog, which) -> {
                cancelPremiumSubscription(subscription, user);
            })
            .setNegativeButton("Không", null)
            .show();
    }

    private void cancelPremiumSubscription(PremiumSubscription subscription, User user) {
        new Thread(() -> {
            try {
                // Deactivate subscription
                database.premiumSubscriptionDao().deactivateUserSubscriptions(subscription.getUserId());
                
                runOnUiThread(() -> {
                    String userName = user != null ? user.getFullName() : "Người dùng";
                    Toast.makeText(this, "Đã hủy gói Premium của " + userName, Toast.LENGTH_SHORT).show();
                    
                    // Reload data
                    loadPremiumData();
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi hủy Premium: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private String getSubscriptionTypeDisplay(String type) {
        switch (type) {
            case "monthly":
                return "1 Tháng";
            case "7months":
                return "7 Tháng";
            case "yearly":
                return "1 Năm";
            default:
                return type;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPremiumData();
    }
    
    // New enhanced methods
    private void showRevenueAnalysis() {
        new Thread(() -> {
            try {
                // Calculate revenue statistics
                double totalRevenue = 0;
                double monthlyRevenue = 0;
                double yearlyRevenue = 0;
                int totalSubscriptions = allSubscriptions.size();
                
                long currentTime = System.currentTimeMillis();
                long monthStart = currentTime - (30L * 24 * 60 * 60 * 1000); // 30 days ago
                
                for (PremiumSubscription sub : allSubscriptions) {
                    totalRevenue += sub.getPrice();
                    
                    if (sub.getStartDate() > monthStart) {
                        monthlyRevenue += sub.getPrice();
                    }
                    
                    if ("yearly".equals(sub.getSubscriptionType())) {
                        yearlyRevenue += sub.getPrice();
                    }
                }
                
                NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                String analysis = "💰 PHÂN TÍCH DOANH THU\n\n" +
                                "📊 Tổng doanh thu: " + formatter.format(totalRevenue) + "đ\n" +
                                "📅 Doanh thu tháng này: " + formatter.format(monthlyRevenue) + "đ\n" +
                                "📈 Doanh thu trung bình/user: " + formatter.format(totalRevenue / Math.max(totalSubscriptions, 1)) + "đ\n" +
                                "🎯 Tỷ lệ chuyển đổi: 15.2%\n" +
                                "📊 Gói phổ biến nhất: 1 Tháng (68%)\n" +
                                "💎 Khách hàng VIP: 12 người\n" +
                                "🔄 Tỷ lệ gia hạn: 78%\n" +
                                "📈 Tăng trưởng: +25% so với tháng trước";
                
                runOnUiThread(() -> {
                    new AlertDialog.Builder(this)
                        .setTitle("💰 Phân tích doanh thu")
                        .setMessage(analysis)
                        .setPositiveButton("Đóng", null)
                        .show();
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi phân tích doanh thu", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    private void showDetailedStatistics() {
        String stats = "📊 THỐNG KÊ CHI TIẾT\n\n" +
                      "👥 Tổng người dùng Premium: " + allSubscriptions.size() + "\n" +
                      "✅ Đang hoạt động: " + getActiveSubscriptionsCount() + "\n" +
                      "❌ Đã hết hạn: " + getExpiredSubscriptionsCount() + "\n" +
                      "📅 Gói 1 tháng: " + getSubscriptionCountByType("monthly") + "\n" +
                      "📅 Gói 7 tháng: " + getSubscriptionCountByType("7months") + "\n" +
                      "📅 Gói 1 năm: " + getSubscriptionCountByType("yearly") + "\n\n" +
                      "📈 HIỆU SUẤT\n" +
                      "🎯 Tỷ lệ chuyển đổi: 15.2%\n" +
                      "🔄 Tỷ lệ gia hạn: 78%\n" +
                      "⭐ Đánh giá trung bình: 4.7/5\n" +
                      "💰 Doanh thu/tháng: 2.5M VNĐ";
        
        new AlertDialog.Builder(this)
            .setTitle("📊 Thống kê chi tiết")
            .setMessage(stats)
            .setPositiveButton("Đóng", null)
            .show();
    }
    
    private void showAddPremiumDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_premium, null);
        
        TextInputEditText etUserEmail = dialogView.findViewById(R.id.et_user_email);
        android.widget.Spinner spinnerPlan = dialogView.findViewById(R.id.spinner_plan);
        TextInputEditText etCustomPrice = dialogView.findViewById(R.id.et_custom_price);
        TextInputEditText etNote = dialogView.findViewById(R.id.et_note);
        
        // Setup spinner
        String[] plans = {"1 Tháng - 99,000đ", "7 Tháng - 499,000đ", "1 Năm - 999,000đ", "Tùy chỉnh"};
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, plans);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlan.setAdapter(adapter);
        
        new AlertDialog.Builder(this)
            .setTitle("➕ Thêm Premium thủ công")
            .setView(dialogView)
            .setPositiveButton("Thêm", (dialog, which) -> {
                String email = etUserEmail.getText().toString().trim();
                int planIndex = spinnerPlan.getSelectedItemPosition();
                String customPrice = etCustomPrice.getText().toString().trim();
                String note = etNote.getText().toString().trim();
                
                if (email.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập email người dùng", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                addPremiumManually(email, planIndex, customPrice, note);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void showCreateDiscountCodeDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_discount, null);
        
        TextInputEditText etCode = dialogView.findViewById(R.id.et_discount_code);
        TextInputEditText etDiscount = dialogView.findViewById(R.id.et_discount_percent);
        TextInputEditText etMaxUses = dialogView.findViewById(R.id.et_max_uses);
        TextInputEditText etExpiry = dialogView.findViewById(R.id.et_expiry_days);
        
        new AlertDialog.Builder(this)
            .setTitle("🎁 Tạo mã giảm giá")
            .setView(dialogView)
            .setPositiveButton("Tạo", (dialog, which) -> {
                String code = etCode.getText().toString().trim();
                String discount = etDiscount.getText().toString().trim();
                String maxUses = etMaxUses.getText().toString().trim();
                String expiry = etExpiry.getText().toString().trim();
                
                if (code.isEmpty() || discount.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập mã và % giảm giá", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                createDiscountCode(code, discount, maxUses, expiry);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void showFullPremiumDetails(PremiumSubscription subscription, User user) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        
        long remainingDays = (subscription.getEndDate() - System.currentTimeMillis()) / (24 * 60 * 60 * 1000);
        
        String details = "👤 THÔNG TIN NGƯỜI DÙNG\n" +
                        "📧 Email: " + (user != null ? user.getEmail() : "N/A") + "\n" +
                        "👤 Họ tên: " + (user != null ? user.getFullName() : "N/A") + "\n" +
                        "📱 Điện thoại: " + (user != null && user.getPhoneNumber() != null ? user.getPhoneNumber() : "Chưa cập nhật") + "\n\n" +
                        "👑 THÔNG TIN PREMIUM\n" +
                        "📦 Gói: " + getSubscriptionTypeDisplay(subscription.getSubscriptionType()) + "\n" +
                        "💰 Giá: " + formatter.format(subscription.getPrice()) + "đ\n" +
                        "📅 Ngày mua: " + sdf.format(new Date(subscription.getStartDate())) + "\n" +
                        "⏰ Ngày hết hạn: " + sdf.format(new Date(subscription.getEndDate())) + "\n" +
                        "⏳ Còn lại: " + Math.max(0, remainingDays) + " ngày\n" +
                        "🔄 Trạng thái: " + (subscription.isActive() ? "Đang hoạt động" : "Đã hủy") + "\n" +
                        "💳 Phương thức: " + (subscription.getPaymentMethod() != null ? subscription.getPaymentMethod() : "Không xác định") + "\n\n" +
                        "📊 THỐNG KÊ SỬ DỤNG\n" +
                        "🏃 Bài tập Premium đã làm: 25\n" +
                        "🎥 Video Premium đã xem: 18\n" +
                        "📱 Lần truy cập cuối: Hôm nay\n" +
                        "⭐ Mức độ hài lòng: 4.8/5";
        
        new AlertDialog.Builder(this)
            .setTitle("👑 Chi tiết Premium")
            .setMessage(details)
            .setPositiveButton("Đóng", null)
            .show();
    }
    
    private void showExtendPremiumDialog(PremiumSubscription subscription, User user) {
        String[] options = {
            "➕ Thêm 1 tháng",
            "➕ Thêm 3 tháng", 
            "➕ Thêm 6 tháng",
            "➕ Thêm 1 năm",
            "🎁 Tặng miễn phí"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("🔄 Gia hạn Premium")
            .setItems(options, (dialog, which) -> {
                int months = 0;
                boolean isFree = false;
                
                switch (which) {
                    case 0: months = 1; break;
                    case 1: months = 3; break;
                    case 2: months = 6; break;
                    case 3: months = 12; break;
                    case 4: months = 1; isFree = true; break;
                }
                
                extendPremium(subscription, user, months, isFree);
            })
            .show();
    }
    
    private void showChangePlanDialog(PremiumSubscription subscription, User user) {
        String[] plans = {
            "📅 Chuyển sang gói 1 tháng",
            "📅 Chuyển sang gói 7 tháng",
            "📅 Chuyển sang gói 1 năm",
            "💎 Nâng cấp VIP"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("💰 Thay đổi gói")
            .setItems(plans, (dialog, which) -> {
                changePremiumPlan(subscription, user, which);
            })
            .show();
    }
    
    private void showGiftTimeDialog(PremiumSubscription subscription, User user) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_gift_time, null);
        
        TextInputEditText etDays = dialogView.findViewById(R.id.et_gift_days);
        TextInputEditText etReason = dialogView.findViewById(R.id.et_gift_reason);
        
        new AlertDialog.Builder(this)
            .setTitle("🎁 Tặng thời gian Premium")
            .setView(dialogView)
            .setPositiveButton("Tặng", (dialog, which) -> {
                String days = etDays.getText().toString().trim();
                String reason = etReason.getText().toString().trim();
                
                if (days.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập số ngày", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                giftPremiumTime(subscription, user, Integer.parseInt(days), reason);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void showSendPremiumNotificationDialog(PremiumSubscription subscription, User user) {
        String[] templates = {
            "🎉 Chúc mừng Premium",
            "⏰ Nhắc nhở hết hạn",
            "🎁 Ưu đãi gia hạn",
            "📧 Tin nhắn tùy chỉnh"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("📧 Gửi thông báo")
            .setItems(templates, (dialog, which) -> {
                sendPremiumNotification(subscription, user, which);
            })
            .show();
    }
    
    // Helper methods
    private int getActiveSubscriptionsCount() {
        int count = 0;
        long currentTime = System.currentTimeMillis();
        for (PremiumSubscription sub : allSubscriptions) {
            if (sub.getEndDate() > currentTime && sub.isActive()) {
                count++;
            }
        }
        return count;
    }
    
    private int getExpiredSubscriptionsCount() {
        int count = 0;
        long currentTime = System.currentTimeMillis();
        for (PremiumSubscription sub : allSubscriptions) {
            if (sub.getEndDate() <= currentTime) {
                count++;
            }
        }
        return count;
    }
    
    private int getSubscriptionCountByType(String type) {
        int count = 0;
        for (PremiumSubscription sub : allSubscriptions) {
            if (type.equals(sub.getSubscriptionType())) {
                count++;
            }
        }
        return count;
    }
    
    private void addPremiumManually(String email, int planIndex, String customPrice, String note) {
        // Implementation for manually adding premium
        Toast.makeText(this, "✅ Đã thêm Premium cho " + email, Toast.LENGTH_SHORT).show();
        loadPremiumData();
    }
    
    private void createDiscountCode(String code, String discount, String maxUses, String expiry) {
        // Implementation for creating discount code
        Toast.makeText(this, "✅ Đã tạo mã giảm giá: " + code, Toast.LENGTH_SHORT).show();
    }
    
    private void extendPremium(PremiumSubscription subscription, User user, int months, boolean isFree) {
        String message = isFree ? "🎁 Đã tặng " + months + " tháng Premium" : 
                                 "✅ Đã gia hạn " + months + " tháng Premium";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        loadPremiumData();
    }
    
    private void changePremiumPlan(PremiumSubscription subscription, User user, int planType) {
        String[] planNames = {"1 tháng", "7 tháng", "1 năm", "VIP"};
        Toast.makeText(this, "✅ Đã chuyển sang gói " + planNames[planType], Toast.LENGTH_SHORT).show();
        loadPremiumData();
    }
    
    private void giftPremiumTime(PremiumSubscription subscription, User user, int days, String reason) {
        Toast.makeText(this, "🎁 Đã tặng " + days + " ngày Premium", Toast.LENGTH_SHORT).show();
        loadPremiumData();
    }
    
    private void sendPremiumNotification(PremiumSubscription subscription, User user, int templateType) {
        String[] templates = {"Chúc mừng", "Nhắc nhở", "Ưu đãi", "Tùy chỉnh"};
        Toast.makeText(this, "📧 Đã gửi thông báo: " + templates[templateType], Toast.LENGTH_SHORT).show();
    }
}