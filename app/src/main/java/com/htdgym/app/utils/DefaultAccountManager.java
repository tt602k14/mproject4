package com.htdgym.app.utils;

import android.content.Context;
import android.util.Log;

import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.Admin;
import com.htdgym.app.models.User;
import com.htdgym.app.models.PremiumSubscription;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class DefaultAccountManager {
    
    private static final String TAG = "DefaultAccountManager";
    
    public static class AccountInfo {
        public String email;
        public String password;
        public String name;
        public String type;
        public String description;
        
        public AccountInfo(String email, String password, String name, String type, String description) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.type = type;
            this.description = description;
        }
    }
    
    /**
     * Get all default accounts information
     */
    public static List<AccountInfo> getAllDefaultAccounts() {
        List<AccountInfo> accounts = new ArrayList<>();
        
        // Admin account
        accounts.add(new AccountInfo(
            "admin@htdgym.com", 
            "admin123", 
            "HTD Gym Administrator", 
            "admin", 
            "Super Admin - Truy cập toàn bộ hệ thống"
        ));
        
        // Regular user
        accounts.add(new AccountInfo(
            "user@htdgym.com", 
            "user123", 
            "Người dùng thường", 
            "user", 
            "User thường - Chỉ truy cập nội dung miễn phí"
        ));
        
        // Premium user
        accounts.add(new AccountInfo(
            "premium@htdgym.com", 
            "premium123", 
            "Người dùng Premium", 
            "premium", 
            "User Premium - Truy cập toàn bộ nội dung"
        ));
        
        // Demo user
        accounts.add(new AccountInfo(
            "demo@htdgym.com", 
            "demo123", 
            "Tài khoản Demo", 
            "demo", 
            "Tài khoản demo cho presentation"
        ));
        
        return accounts;
    }
    
    /**
     * Check if all default accounts exist in database
     */
    public static boolean checkDefaultAccountsExist(Context context) {
        try {
            GymDatabase database = GymDatabase.getInstance(context);
            
            // Check admin
            Admin admin = database.adminDao().getAdminByEmail("admin@htdgym.com");
            if (admin == null) return false;
            
            // Check users
            User user = database.userDao().getUserByEmail("user@htdgym.com");
            if (user == null) return false;
            
            User premium = database.userDao().getUserByEmail("premium@htdgym.com");
            if (premium == null) return false;
            
            User demo = database.userDao().getUserByEmail("demo@htdgym.com");
            if (demo == null) return false;
            
            Log.d(TAG, "All default accounts exist");
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "Error checking default accounts", e);
            return false;
        }
    }
    
    /**
     * Force create all default accounts (useful for testing)
     */
    public static void forceCreateDefaultAccounts(Context context) {
        new Thread(() -> {
            try {
                GymDatabase database = GymDatabase.getInstance(context);
                
                // Create admin if not exists
                createAdminAccount(database);
                
                // Create users if not exist
                createUserAccounts(database);
                
                Log.d(TAG, "Force creation of default accounts completed");
                
            } catch (Exception e) {
                Log.e(TAG, "Error force creating default accounts", e);
            }
        }).start();
    }
    
    private static void createAdminAccount(GymDatabase database) {
        try {
            Admin existingAdmin = database.adminDao().getAdminByEmail("admin@htdgym.com");
            
            if (existingAdmin == null) {
                Admin admin = new Admin();
                admin.setEmail("admin@htdgym.com");
                admin.setUsername("admin");
                admin.setFullName("HTD Gym Administrator");
                admin.setPassword(hashPassword("admin123"));
                admin.setRole("super_admin");
                admin.setActive(true);
                admin.setCreatedAt(System.currentTimeMillis());
                
                database.adminDao().insertAdmin(admin);
                Log.d(TAG, "Admin account created");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error creating admin account", e);
        }
    }
    
    private static void createUserAccounts(GymDatabase database) {
        try {
            // Regular user
            User existingUser = database.userDao().getUserByEmail("user@htdgym.com");
            if (existingUser == null) {
                User user = new User();
                user.setEmail("user@htdgym.com");
                user.setPassword(hashPassword("user123"));
                user.setName("Người dùng thường");
                user.setPhone("0123456789");
                user.setLoginType("email");
                user.setCreatedAt(System.currentTimeMillis());
                user.setLastLoginTime(System.currentTimeMillis());
                user.setActive(true);
                user.setAge(25);
                user.setWeight(70.0f);
                user.setHeight(170.0f);
                user.setFitnessGoal("Tăng cơ bắp");
                
                database.userDao().insertUser(user);
                Log.d(TAG, "Regular user account created");
            }
            
            // Premium user
            User existingPremium = database.userDao().getUserByEmail("premium@htdgym.com");
            if (existingPremium == null) {
                User premiumUser = new User();
                premiumUser.setEmail("premium@htdgym.com");
                premiumUser.setPassword(hashPassword("premium123"));
                premiumUser.setName("Người dùng Premium");
                premiumUser.setPhone("0987654321");
                premiumUser.setLoginType("email");
                premiumUser.setCreatedAt(System.currentTimeMillis());
                premiumUser.setLastLoginTime(System.currentTimeMillis());
                premiumUser.setActive(true);
                premiumUser.setAge(30);
                premiumUser.setWeight(75.0f);
                premiumUser.setHeight(175.0f);
                premiumUser.setFitnessGoal("Giảm cân và tăng cơ");
                
                long userId = database.userDao().insertUser(premiumUser);
                
                // Create premium subscription
                PremiumSubscription premiumSub = new PremiumSubscription();
                premiumSub.setUserId((int) userId);
                premiumSub.setSubscriptionType("yearly");
                premiumSub.setStartDate(System.currentTimeMillis());
                premiumSub.setEndDate(System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000));
                premiumSub.setActive(true);
                premiumSub.setPrice(299000);
                premiumSub.setPaymentMethod("default");
                
                database.premiumSubscriptionDao().insertSubscription(premiumSub);
                Log.d(TAG, "Premium user account created");
            }
            
            // Demo user
            User existingDemo = database.userDao().getUserByEmail("demo@htdgym.com");
            if (existingDemo == null) {
                User demoUser = new User();
                demoUser.setEmail("demo@htdgym.com");
                demoUser.setPassword(hashPassword("demo123"));
                demoUser.setName("Tài khoản Demo");
                demoUser.setPhone("0111222333");
                demoUser.setLoginType("email");
                demoUser.setCreatedAt(System.currentTimeMillis());
                demoUser.setLastLoginTime(System.currentTimeMillis());
                demoUser.setActive(true);
                demoUser.setAge(28);
                demoUser.setWeight(65.0f);
                demoUser.setHeight(165.0f);
                demoUser.setFitnessGoal("Duy trì sức khỏe");
                
                database.userDao().insertUser(demoUser);
                Log.d(TAG, "Demo user account created");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating user accounts", e);
        }
    }
    
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            Log.e(TAG, "Error hashing password", e);
            return password;
        }
    }
    
    /**
     * Get account info by email
     */
    public static AccountInfo getAccountInfo(String email) {
        List<AccountInfo> accounts = getAllDefaultAccounts();
        for (AccountInfo account : accounts) {
            if (account.email.equals(email)) {
                return account;
            }
        }
        return null;
    }
    
    /**
     * Print all default accounts to log (for debugging)
     */
    public static void printDefaultAccounts() {
        Log.d(TAG, "=== DEFAULT ACCOUNTS ===");
        List<AccountInfo> accounts = getAllDefaultAccounts();
        for (AccountInfo account : accounts) {
            Log.d(TAG, String.format("%s: %s / %s (%s)", 
                account.type.toUpperCase(), 
                account.email, 
                account.password, 
                account.description));
        }
        Log.d(TAG, "========================");
    }
}