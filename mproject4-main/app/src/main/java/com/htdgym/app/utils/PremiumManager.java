package com.htdgym.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.PremiumUser;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PremiumManager {
    private static final String PREF_NAME = "premium_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_IS_PREMIUM = "is_premium";
    private static final String KEY_SUBSCRIPTION_END = "subscription_end";
    
    private static PremiumManager instance;
    private Context context;
    private SharedPreferences prefs;
    private GymDatabase database;
    private ExecutorService executor;
    
    private PremiumManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.database = GymDatabase.getInstance(context);
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    public static synchronized PremiumManager getInstance(Context context) {
        if (instance == null) {
            instance = new PremiumManager(context);
        }
        return instance;
    }
    
    // Check if user has premium access
    public boolean isPremiumUser() {
        String userId = getCurrentUserId();
        if (userId == null) return false;
        
        // Check from SharedPreferences first (for quick access)
        boolean isPremium = prefs.getBoolean(KEY_IS_PREMIUM, false);
        long subscriptionEnd = prefs.getLong(KEY_SUBSCRIPTION_END, 0);
        
        if (isPremium && subscriptionEnd > 0) {
            // Check if subscription is still valid
            return new Date().getTime() < subscriptionEnd;
        }
        
        return false;
    }
    
    // Check if specific program requires premium
    public boolean isProgramPremium(String programType) {
        switch (programType) {
            case "30":
                return false; // Free program
            case "60":
            case "90":
                return true; // Premium programs
            default:
                return false;
        }
    }
    
    // Check if user can access premium program
    public boolean canAccessPremiumProgram(String programType) {
        if (!isProgramPremium(programType)) {
            return true; // Free program, always accessible
        }
        return isPremiumUser(); // Premium program, check premium status
    }
    
    // Activate premium subscription
    public void activatePremium(String subscriptionType, String paymentMethod, 
                               String transactionId, double price) {
        String userId = getCurrentUserId();
        if (userId == null) return;
        
        executor.execute(() -> {
            Date startDate = new Date();
            Date endDate = calculateEndDate(subscriptionType);
            
            PremiumUser premiumUser = new PremiumUser(userId, true, subscriptionType, startDate, endDate);
            premiumUser.setPaymentMethod(paymentMethod);
            premiumUser.setTransactionId(transactionId);
            premiumUser.setPrice(price);
            premiumUser.setCurrency("VND");
            
            // Save to database
            PremiumUser existing = database.premiumUserDao().getPremiumUserByUserId(userId);
            if (existing != null) {
                existing.setPremium(true);
                existing.setSubscriptionType(subscriptionType);
                existing.setSubscriptionStartDate(startDate);
                existing.setSubscriptionEndDate(endDate);
                existing.setPaymentMethod(paymentMethod);
                existing.setTransactionId(transactionId);
                existing.setPrice(price);
                existing.setActive(true);
                database.premiumUserDao().updatePremiumUser(existing);
            } else {
                database.premiumUserDao().insertPremiumUser(premiumUser);
            }
            
            // Update SharedPreferences for quick access
            prefs.edit()
                .putBoolean(KEY_IS_PREMIUM, true)
                .putLong(KEY_SUBSCRIPTION_END, endDate != null ? endDate.getTime() : Long.MAX_VALUE)
                .apply();
        });
    }
    
    // Deactivate premium
    public void deactivatePremium() {
        String userId = getCurrentUserId();
        if (userId == null) return;
        
        executor.execute(() -> {
            database.premiumUserDao().deactivatePremium(userId);
            prefs.edit()
                .putBoolean(KEY_IS_PREMIUM, false)
                .putLong(KEY_SUBSCRIPTION_END, 0)
                .apply();
        });
    }
    
    // Get premium user info
    public void getPremiumUserInfo(PremiumInfoCallback callback) {
        String userId = getCurrentUserId();
        if (userId == null) {
            callback.onResult(null);
            return;
        }
        
        executor.execute(() -> {
            PremiumUser premiumUser = database.premiumUserDao().getPremiumUserByUserId(userId);
            callback.onResult(premiumUser);
        });
    }
    
    // Calculate subscription end date
    private Date calculateEndDate(String subscriptionType) {
        Calendar calendar = Calendar.getInstance();
        
        switch (subscriptionType) {
            case "monthly":
                calendar.add(Calendar.MONTH, 1);
                break;
            case "5months":
                calendar.add(Calendar.MONTH, 5);
                break;
            case "yearly":
                calendar.add(Calendar.YEAR, 1);
                break;
            case "5years":
                calendar.add(Calendar.YEAR, 5);
                break;
            default:
                calendar.add(Calendar.MONTH, 1);
                break;
        }
        
        return calendar.getTime();
    }
    
    // Get current user ID (you may need to implement this based on your auth system)
    public String getCurrentUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }
    
    // Set current user ID
    public void setCurrentUserId(String userId) {
        prefs.edit().putString(KEY_USER_ID, userId).apply();
    }
    
    // Get premium features list
    public String[] getPremiumFeatures() {
        return new String[] {
            "Chương trình tập luyện 60 ngày",
            "Chương trình tập luyện 90 ngày", 
            "Kế hoạch dinh dưỡng nâng cao",
            "Theo dõi tiến độ chi tiết",
            "Tư vấn cá nhân hóa",
            "Không quảng cáo",
            "Tải video offline",
            "Hỗ trợ ưu tiên"
        };
    }
    
    // Get subscription prices
    public PremiumPlan[] getPremiumPlans() {
        return new PremiumPlan[] {
            new PremiumPlan("monthly", "Gói 1 Tháng",  50000,   "VND", "Thanh toán hàng tháng",          false, "📅"),
            new PremiumPlan("5months", "Gói 5 Tháng",  300000,  "VND", "Tiết kiệm 40% so với gói tháng", true,  "⭐"),
            new PremiumPlan("yearly",  "Gói 1 Năm",    500000,  "VND", "Tiết kiệm 58% so với gói tháng", false, "�"),
            new PremiumPlan("5years",  "Gói 5 Năm",    1000000, "VND", "Tiết kiệm tối đa — dùng 5 năm",  false, "👑")
        };
    }
    
    // Premium plan class
    public static class PremiumPlan {
        public String id;
        public String name;
        public double price;
        public String currency;
        public String description;
        public boolean isPopular;
        public String icon;
        
        public PremiumPlan(String id, String name, double price, String currency, String description) {
            this(id, name, price, currency, description, false, "💎");
        }
        
        public PremiumPlan(String id, String name, double price, String currency, String description, boolean isPopular, String icon) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.currency = currency;
            this.description = description;
            this.isPopular = isPopular;
            this.icon = icon;
        }
        
        public String getFormattedPrice() {
            return String.format("%,.0f %s", price, currency);
        }
        
        public String getSavingsText() {
            if ("5months".equals(id)) {
                return "Tiết kiệm 40%";
            } else if ("yearly".equals(id)) {
                return "Tiết kiệm 58%";
            } else if ("5years".equals(id)) {
                return "Tiết kiệm tối đa";
            }
            return "";
        }
    }
    
    // Callback interface
    public interface PremiumInfoCallback {
        void onResult(PremiumUser premiumUser);
    }
}