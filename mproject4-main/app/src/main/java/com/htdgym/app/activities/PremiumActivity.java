package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.utils.PremiumManager;

public class PremiumActivity extends AppCompatActivity {
    
    private PremiumManager premiumManager;
    private LinearLayout featuresContainer;
    private LinearLayout plansContainer;
    private TextView statusText;
    private Button upgradeButton;
    private ImageView backButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        
        initViews();
        setupPremiumManager();
        loadPremiumContent();
        setupClickListeners();
    }
    
    private void initViews() {
        backButton = findViewById(R.id.btn_back);
        statusText = findViewById(R.id.tv_premium_status);
        featuresContainer = findViewById(R.id.container_features);
        plansContainer = findViewById(R.id.container_plans);
        upgradeButton = findViewById(R.id.btn_upgrade);
    }
    
    private void setupPremiumManager() {
        premiumManager = PremiumManager.getInstance(this);
        
        // Set a demo user ID for testing (replace with actual auth system)
        if (premiumManager.getCurrentUserId() == null) {
            premiumManager.setCurrentUserId("demo_user_" + System.currentTimeMillis());
        }
    }
    
    private void loadPremiumContent() {
        // Check current premium status
        boolean isPremium = premiumManager.isPremiumUser();
        
        if (isPremium) {
            statusText.setText("🎉 Bạn đang sử dụng Premium");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            upgradeButton.setText("Quản lý gói Premium");
            
            // Load premium user info
            premiumManager.getPremiumUserInfo(premiumUser -> {
                runOnUiThread(() -> {
                    if (premiumUser != null && premiumUser.isSubscriptionValid()) {
                        long daysRemaining = premiumUser.getDaysRemaining();
                        if (daysRemaining == Long.MAX_VALUE) {
                            statusText.setText("🎉 Premium trọn đời");
                        } else {
                            statusText.setText(String.format("🎉 Premium còn %d ngày", daysRemaining));
                        }
                    }
                });
            });
        } else {
            statusText.setText("Nâng cấp lên Premium để mở khóa tất cả tính năng");
            statusText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            upgradeButton.setText("Nâng cấp Premium");
        }
        
        loadPremiumFeatures();
        loadPremiumPlans();
    }
    
    private void loadPremiumFeatures() {
        featuresContainer.removeAllViews();
        
        String[] features = premiumManager.getPremiumFeatures();
        
        for (String feature : features) {
            View featureView = getLayoutInflater().inflate(R.layout.item_premium_feature, featuresContainer, false);
            
            TextView featureText = featureView.findViewById(R.id.tv_feature);
            ImageView featureIcon = featureView.findViewById(R.id.iv_feature_icon);
            
            featureText.setText(feature);
            featureIcon.setImageResource(R.drawable.ic_check_circle);
            
            featuresContainer.addView(featureView);
        }
    }
    
    private void loadPremiumPlans() {
        plansContainer.removeAllViews();
        
        PremiumManager.PremiumPlan[] plans = premiumManager.getPremiumPlans();
        
        for (PremiumManager.PremiumPlan plan : plans) {
            // Create plan view programmatically
            View planView = createPremiumPlanView(plan);
            plansContainer.addView(planView);
        }
    }
    
    private View createPremiumPlanView(PremiumManager.PremiumPlan plan) {
        // Create CardView
        androidx.cardview.widget.CardView cardView = new androidx.cardview.widget.CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 32);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(32f);
        cardView.setCardElevation(plan.isPopular ? 16f : 8f);
        cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        
        // Create main container
        LinearLayout mainContainer = new LinearLayout(this);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.setPadding(40, 40, 40, 40);
        
        // Popular badge
        if (plan.isPopular) {
            TextView popularBadge = new TextView(this);
            popularBadge.setText("🔥 PHỔ BIẾN NHẤT");
            popularBadge.setTextColor(getResources().getColor(android.R.color.white));
            popularBadge.setTextSize(12f);
            popularBadge.setTypeface(null, android.graphics.Typeface.BOLD);
            popularBadge.setPadding(24, 8, 24, 8);
            popularBadge.setBackgroundResource(R.drawable.badge_popular);
            LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            badgeParams.gravity = android.view.Gravity.END;
            badgeParams.setMargins(0, 0, 0, 16);
            popularBadge.setLayoutParams(badgeParams);
            mainContainer.addView(popularBadge);
        }
        
        // Header with icon and name
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        headerLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        headerParams.setMargins(0, 0, 0, 24);
        headerLayout.setLayoutParams(headerParams);
        
        // Plan icon
        TextView planIcon = new TextView(this);
        planIcon.setText(plan.icon);
        planIcon.setTextSize(32f);
        planIcon.setGravity(android.view.Gravity.CENTER);
        planIcon.setBackgroundResource(R.drawable.gradient_card_2);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(96, 96);
        iconParams.setMargins(0, 0, 32, 0);
        planIcon.setLayoutParams(iconParams);
        headerLayout.addView(planIcon);
        
        // Name and savings container
        LinearLayout nameContainer = new LinearLayout(this);
        nameContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams nameContainerParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        nameContainer.setLayoutParams(nameContainerParams);
        
        // Plan name
        TextView planName = new TextView(this);
        planName.setText(plan.name);
        planName.setTextColor(getResources().getColor(R.color.text_primary));
        planName.setTextSize(20f);
        planName.setTypeface(null, android.graphics.Typeface.BOLD);
        nameContainer.addView(planName);
        
        // Savings label
        String savings = plan.getSavingsText();
        if (!savings.isEmpty()) {
            TextView savingsLabel = new TextView(this);
            savingsLabel.setText(savings);
            savingsLabel.setTextColor(getResources().getColor(R.color.primary_darker));
            savingsLabel.setTextSize(14f);
            savingsLabel.setTypeface(null, android.graphics.Typeface.BOLD);
            savingsLabel.setPadding(16, 4, 16, 4);
            savingsLabel.setBackgroundColor(getResources().getColor(R.color.primary_light));
            LinearLayout.LayoutParams savingsParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            savingsParams.setMargins(0, 8, 0, 0);
            savingsLabel.setLayoutParams(savingsParams);
            nameContainer.addView(savingsLabel);
        }
        
        headerLayout.addView(nameContainer);
        mainContainer.addView(headerLayout);
        
        // Price section
        LinearLayout priceLayout = new LinearLayout(this);
        priceLayout.setOrientation(LinearLayout.HORIZONTAL);
        priceLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams priceLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        priceLayoutParams.setMargins(0, 0, 0, 16);
        priceLayout.setLayoutParams(priceLayoutParams);
        
        // Main price
        TextView planPrice = new TextView(this);
        planPrice.setText(plan.getFormattedPrice());
        planPrice.setTextColor(getResources().getColor(R.color.secondary));
        planPrice.setTextSize(28f);
        planPrice.setTypeface(null, android.graphics.Typeface.BOLD);
        priceLayout.addView(planPrice);
        
        // Price per month
        if ("5months".equals(plan.id)) {
            double monthlyPrice = plan.price / 5;
            TextView pricePerMonth = new TextView(this);
            pricePerMonth.setText(String.format(" (%.0f VND/tháng)", monthlyPrice));
            pricePerMonth.setTextColor(getResources().getColor(R.color.text_secondary));
            pricePerMonth.setTextSize(16f);
            LinearLayout.LayoutParams monthlyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            monthlyParams.setMargins(16, 0, 0, 0);
            pricePerMonth.setLayoutParams(monthlyParams);
            priceLayout.addView(pricePerMonth);
        } else if ("yearly".equals(plan.id)) {
            double monthlyPrice = plan.price / 12;
            TextView pricePerMonth = new TextView(this);
            pricePerMonth.setText(String.format(" (%.0f VND/tháng)", monthlyPrice));
            pricePerMonth.setTextColor(getResources().getColor(R.color.text_secondary));
            pricePerMonth.setTextSize(16f);
            LinearLayout.LayoutParams monthlyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            monthlyParams.setMargins(16, 0, 0, 0);
            pricePerMonth.setLayoutParams(monthlyParams);
            priceLayout.addView(pricePerMonth);
        } else if ("5years".equals(plan.id)) {
            double monthlyPrice = plan.price / 60;
            TextView pricePerMonth = new TextView(this);
            pricePerMonth.setText(String.format(" (%.0f VND/tháng)", monthlyPrice));
            pricePerMonth.setTextColor(getResources().getColor(R.color.text_secondary));
            pricePerMonth.setTextSize(16f);
            LinearLayout.LayoutParams monthlyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            monthlyParams.setMargins(16, 0, 0, 0);
            pricePerMonth.setLayoutParams(monthlyParams);
            priceLayout.addView(pricePerMonth);
        }
        
        mainContainer.addView(priceLayout);
        
        // Description
        TextView planDescription = new TextView(this);
        planDescription.setText(plan.description);
        planDescription.setTextColor(getResources().getColor(R.color.text_secondary));
        planDescription.setTextSize(16f);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        descParams.setMargins(0, 0, 0, 24);
        planDescription.setLayoutParams(descParams);
        mainContainer.addView(planDescription);
        
        // Features list
        String[] features = {
            "✅ Chương trình tập luyện 60 & 90 ngày",
            "✅ Kế hoạch dinh dưỡng nâng cao", 
            "✅ Tư vấn cá nhân hóa & Không quảng cáo"
        };
        
        for (String feature : features) {
            TextView featureText = new TextView(this);
            featureText.setText(feature);
            featureText.setTextColor(getResources().getColor(R.color.text_primary));
            featureText.setTextSize(15f);
            LinearLayout.LayoutParams featureParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            featureParams.setMargins(0, 0, 0, 8);
            featureText.setLayoutParams(featureParams);
            mainContainer.addView(featureText);
        }
        
        // Select button
        Button selectButton = new Button(this);
        selectButton.setText("Chọn gói này");
        selectButton.setTextColor(getResources().getColor(android.R.color.white));
        selectButton.setTextSize(18f);
        selectButton.setTypeface(null, android.graphics.Typeface.BOLD);
        selectButton.setBackgroundResource(R.drawable.button_premium);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            120
        );
        buttonParams.setMargins(0, 32, 0, 0);
        selectButton.setLayoutParams(buttonParams);
        selectButton.setOnClickListener(v -> processPurchase(plan));
        mainContainer.addView(selectButton);
        
        cardView.addView(mainContainer);
        return cardView;
    }
    
    private void selectPremiumPlan(PremiumManager.PremiumPlan plan) {
        // Show payment options dialog
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Chọn phương thức thanh toán")
            .setMessage(String.format("Gói: %s\nGiá: %s", plan.name, plan.getFormattedPrice()))
            .setItems(new String[]{"💳 Chuyển khoản ngân hàng", "🎁 Mã khuyến mãi"}, (dialog, which) -> {
                switch (which) {
                    case 0:
                        // Bank transfer - use processPurchase for consistency
                        processPurchase(plan);
                        break;
                    case 1:
                        // Promo code
                        showPromoCodeDialog(plan);
                        break;
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void processPurchase(PremiumManager.PremiumPlan plan) {
        // Debug log
        android.util.Log.d("PremiumActivity", "processPurchase called with plan: " + plan.name + ", price: " + plan.price);
        
        // Show confirmation dialog first
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Xác nhận thanh toán")
            .setMessage("Bạn muốn thanh toán gói " + plan.name + " với giá " + plan.getFormattedPrice() + "?")
            .setPositiveButton("Tiếp tục", (dialog, which) -> {
                // Navigate to PaymentActivity for QR code payment
                Intent paymentIntent = new Intent(this, com.htdgym.app.activities.PaymentActivity.class);
                paymentIntent.putExtra("plan_id", plan.id);
                paymentIntent.putExtra("plan_name", plan.name);
                paymentIntent.putExtra("plan_price", plan.price);
                paymentIntent.putExtra("plan_description", plan.description);
                
                android.util.Log.d("PremiumActivity", "Starting PaymentActivity with intent extras");
                
                try {
                    startActivityForResult(paymentIntent, 2001);
                    android.widget.Toast.makeText(this, "Đang mở trang thanh toán...", android.widget.Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    android.util.Log.e("PremiumActivity", "Error starting PaymentActivity", e);
                    android.widget.Toast.makeText(this, "Lỗi: Không thể mở trang thanh toán - " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        upgradeButton.setOnClickListener(v -> {
            if (premiumManager.isPremiumUser()) {
                // Show premium management options
                showPremiumManagement();
            } else {
                // Scroll to plans section
                plansContainer.requestFocus();
            }
        });
    }
    
    private void showPremiumManagement() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Quản lý Premium")
            .setItems(new String[]{"Xem thông tin gói", "Lịch sử giao dịch", "Hủy gói Premium"}, (dialog, which) -> {
                switch (which) {
                    case 0:
                        showPremiumInfo();
                        break;
                    case 1:
                        Intent historyIntent = new Intent(this, com.htdgym.app.activities.TransactionHistoryActivity.class);
                        startActivity(historyIntent);
                        break;
                    case 2:
                        cancelPremium();
                        break;
                }
            })
            .show();
    }
    
    private void showPremiumInfo() {
        premiumManager.getPremiumUserInfo(premiumUser -> {
            runOnUiThread(() -> {
                if (premiumUser != null) {
                    String info = String.format(
                        "Loại gói: %s\nNgày bắt đầu: %s\nTrạng thái: %s",
                        premiumUser.getSubscriptionType(),
                        premiumUser.getSubscriptionStartDate().toString(),
                        premiumUser.isSubscriptionValid() ? "Đang hoạt động" : "Hết hạn"
                    );
                    
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Thông tin Premium")
                        .setMessage(info)
                        .setPositiveButton("OK", null)
                        .show();
                }
            });
        });
    }
    
    private void cancelPremium() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Hủy Premium")
            .setMessage("Bạn có chắc chắn muốn hủy gói Premium không? Bạn sẽ mất quyền truy cập vào các tính năng Premium.")
            .setPositiveButton("Hủy gói", (dialog, which) -> {
                premiumManager.deactivatePremium();
                Toast.makeText(this, "Đã hủy gói Premium", Toast.LENGTH_SHORT).show();
                loadPremiumContent();
            })
            .setNegativeButton("Giữ lại", null)
            .show();
    }
    
    private void showPromoCodeDialog(PremiumManager.PremiumPlan plan) {
        android.widget.EditText editText = new android.widget.EditText(this);
        editText.setHint("Nhập mã khuyến mãi");
        editText.setPadding(50, 30, 50, 30);
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Mã khuyến mãi")
            .setMessage("Nhập mã khuyến mãi để kích hoạt Premium miễn phí")
            .setView(editText)
            .setPositiveButton("Kích hoạt", (dialog, which) -> {
                String promoCode = editText.getText().toString().trim();
                validatePromoCode(promoCode, plan);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    private void validatePromoCode(String promoCode, PremiumManager.PremiumPlan plan) {
        // Demo promo codes
        String[] validCodes = {"HTDGYM2024", "PREMIUM50", "FREEMONTH", "TESTCODE"};
        
        boolean isValid = false;
        for (String code : validCodes) {
            if (code.equalsIgnoreCase(promoCode)) {
                isValid = true;
                break;
            }
        }
        
        if (isValid) {
            // Activate premium with promo code
            premiumManager.activatePremium(
                plan.id,
                "promo_code",
                "PROMO_" + promoCode + "_" + System.currentTimeMillis(),
                0.0 // Free with promo code
            );
            
            Toast.makeText(this, "🎉 Mã khuyến mãi hợp lệ! Premium đã được kích hoạt!", 
                Toast.LENGTH_LONG).show();
            
            loadPremiumContent();
            setResult(RESULT_OK);
        } else {
            Toast.makeText(this, "❌ Mã khuyến mãi không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 2001) { // PaymentActivity result
            if (resultCode == RESULT_OK) {
                // Payment successful, premium activated
                if (data != null && data.getBooleanExtra("premium_activated", false)) {
                    // Refresh the UI to show premium status
                    loadPremiumContent();
                    
                    // Set result to notify calling activity (HomeFragment)
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("premium_activated", true);
                    setResult(RESULT_OK, resultIntent);
                    
                    // Show success message and close
                    Toast.makeText(this, "🎉 Premium đã được kích hoạt thành công!", 
                        Toast.LENGTH_LONG).show();
                    
                    // Close PremiumActivity after 1 second
                    new android.os.Handler().postDelayed(() -> {
                        finish();
                    }, 1000);
                }
            } else {
                // Payment cancelled or failed
                Toast.makeText(this, "Thanh toán đã bị hủy", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 1002 && resultCode == RESULT_OK) {
            // Legacy payment successful, refresh UI
            loadPremiumContent();
            setResult(RESULT_OK);
        }
    }
}