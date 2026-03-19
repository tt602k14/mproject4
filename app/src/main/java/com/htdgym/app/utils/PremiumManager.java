package com.htdgym.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.PremiumSubscription;

public class PremiumManager {
    
    private static final String PREFS_NAME = "HTDGymPrefs";
    private static final String KEY_IS_PREMIUM = "isPremium";
    private static final String KEY_PREMIUM_END_DATE = "premiumEndDate";
    
    public static boolean isPremiumUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isPremium = prefs.getBoolean(KEY_IS_PREMIUM, false);
        long endDate = prefs.getLong(KEY_PREMIUM_END_DATE, 0);
        
        if (isPremium && endDate > System.currentTimeMillis()) {
            return true;
        } else if (isPremium && endDate <= System.currentTimeMillis()) {
            // Premium expired, update preferences
            prefs.edit()
                .putBoolean(KEY_IS_PREMIUM, false)
                .remove(KEY_PREMIUM_END_DATE)
                .apply();
            return false;
        }
        
        return false;
    }
    
    public static void checkPremiumStatus(Context context, int userId) {
        new Thread(() -> {
            GymDatabase database = GymDatabase.getInstance(context);
            boolean isPremium = database.premiumSubscriptionDao()
                .isUserPremium(userId, System.currentTimeMillis());
            
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putBoolean(KEY_IS_PREMIUM, isPremium).apply();
            
            if (isPremium) {
                PremiumSubscription subscription = database.premiumSubscriptionDao()
                    .getActivePremiumSubscriptionSync(userId);
                if (subscription != null) {
                    prefs.edit().putLong(KEY_PREMIUM_END_DATE, subscription.getEndDate()).apply();
                }
            }
        }).start();
    }
    
    public static void showPremiumDialog(Context context, String feature) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("🔒 Nội dung Premium")
            .setMessage("Bài tập này chỉ dành cho thành viên Premium.\n\n" +
                "Nâng cấp ngay để truy cập:\n" +
                "• Tất cả bài tập nâng cao\n" +
                "• Kế hoạch tập luyện chuyên nghiệp\n" +
                "• Theo dõi tiến độ chi tiết\n" +
                "• Không quảng cáo\n\n" +
                "Chỉ từ 50,000 VND/tháng!")
            .setPositiveButton("Nâng cấp Premium", (dialog, which) -> {
                android.content.Intent intent = new android.content.Intent(context, 
                    com.htdgym.app.activities.PremiumActivity.class);
                context.startActivity(intent);
            })
            .setNegativeButton("Để sau", null)
            .show();
    }
}