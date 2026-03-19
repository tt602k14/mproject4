package com.htdgym.app.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.htdgym.app.models.*;

@Database(
    entities = {
        Member.class, 
        Workout.class, 
        Payment.class, 
        Equipment.class, 
        Trainer.class,
        User.class,
        Exercise.class,
        Video.class,
        UserStats.class,
        WorkoutStats.class,
        PremiumSubscription.class,
        Admin.class,
        CustomWorkout.class,
        ProgressPhoto.class
    },
    version = 12,
    exportSchema = false
)
@TypeConverters({DateConverter.class})
public abstract class GymDatabase extends RoomDatabase {
    
    private static GymDatabase INSTANCE;
    
    public abstract MemberDao memberDao();
    public abstract WorkoutDao workoutDao();
    public abstract PaymentDao paymentDao();
    public abstract EquipmentDao equipmentDao();
    public abstract TrainerDao trainerDao();
    public abstract UserDao userDao();
    public abstract ExerciseDao exerciseDao();
    public abstract VideoDao videoDao();
    public abstract UserStatsDao userStatsDao();
    public abstract WorkoutStatsDao workoutStatsDao();
    public abstract PremiumSubscriptionDao premiumSubscriptionDao();
    public abstract AdminDao adminDao();
    public abstract CustomWorkoutDao customWorkoutDao();
    public abstract ProgressPhotoDao progressPhotoDao();
    
    public static synchronized GymDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            // Force delete old database to avoid migration issues
            try {
                String[] dbFiles = {"gym_database", "gym_database-shm", "gym_database-wal"};
                for (String dbFile : dbFiles) {
                    context.deleteDatabase(dbFile);
                }
                android.util.Log.d("GymDatabase", "Old database files deleted");
            } catch (Exception e) {
                android.util.Log.w("GymDatabase", "Could not delete old database files", e);
            }
            
            INSTANCE = Room.databaseBuilder(
                context.getApplicationContext(),
                GymDatabase.class,
                "gym_database"
            )
            .fallbackToDestructiveMigration() // Always recreate if schema changes
            .build();
            
            android.util.Log.d("GymDatabase", "New database created successfully");
            
            // Create default admin account in background
            createDefaultAdminAccount(INSTANCE);
        }
        return INSTANCE;
    }
    
    private static void createDefaultAdminAccount(GymDatabase database) {
        new Thread(() -> {
            try {
                // Create default admin account
                Admin existingAdmin = database.adminDao().getAdminByEmail("admin@htdgym.com");
                
                if (existingAdmin == null) {
                    Admin defaultAdmin = new Admin();
                    defaultAdmin.setEmail("admin@htdgym.com");
                    defaultAdmin.setUsername("admin");
                    defaultAdmin.setFullName("HTD Gym Administrator");
                    
                    // Hash the default password "admin123"
                    String hashedPassword = hashPassword("admin123");
                    defaultAdmin.setPassword(hashedPassword);
                    
                    defaultAdmin.setRole("super_admin");
                    defaultAdmin.setActive(true);
                    defaultAdmin.setCreatedAt(System.currentTimeMillis());
                    
                    // Insert admin
                    database.adminDao().insertAdmin(defaultAdmin);
                    
                    android.util.Log.d("GymDatabase", "Default admin account created successfully");
                    android.util.Log.d("GymDatabase", "Admin Login - Email: admin@htdgym.com, Password: admin123");
                } else {
                    android.util.Log.d("GymDatabase", "Admin account already exists");
                }
                
                // Create default user accounts
                createDefaultUserAccounts(database);
                
            } catch (Exception e) {
                android.util.Log.e("GymDatabase", "Error creating default admin account", e);
            }
        }).start();
    }
    
    private static void createDefaultUserAccounts(GymDatabase database) {
        try {
            // Create regular user account
            User existingUser = database.userDao().getUserByEmail("user@htdgym.com");
            if (existingUser == null) {
                User regularUser = new User();
                regularUser.setEmail("user@htdgym.com");
                regularUser.setPassword(hashPassword("user123"));
                regularUser.setName("Người dùng thường");
                regularUser.setPhone("0123456789");
                regularUser.setLoginType("email");
                regularUser.setCreatedAt(System.currentTimeMillis());
                regularUser.setLastLoginTime(System.currentTimeMillis());
                regularUser.setActive(true);
                regularUser.setAge(25);
                regularUser.setWeight(70.0f);
                regularUser.setHeight(170.0f);
                regularUser.setFitnessGoal("Tăng cơ bắp");
                
                database.userDao().insertUser(regularUser);
                android.util.Log.d("GymDatabase", "Regular user created - Email: user@htdgym.com, Password: user123");
            }
            
            // Create premium user account
            User existingPremiumUser = database.userDao().getUserByEmail("premium@htdgym.com");
            if (existingPremiumUser == null) {
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
                
                // Create premium subscription for this user
                PremiumSubscription premiumSub = new PremiumSubscription();
                premiumSub.setUserId((int) userId);
                premiumSub.setSubscriptionType("yearly");
                premiumSub.setStartDate(System.currentTimeMillis());
                premiumSub.setEndDate(System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000)); // 1 year from now
                premiumSub.setActive(true);
                premiumSub.setPrice(299000); // 299k VND
                premiumSub.setPaymentMethod("default");
                
                database.premiumSubscriptionDao().insertSubscription(premiumSub);
                android.util.Log.d("GymDatabase", "Premium user created - Email: premium@htdgym.com, Password: premium123");
            }
            
            // Create demo user account
            User existingDemoUser = database.userDao().getUserByEmail("demo@htdgym.com");
            if (existingDemoUser == null) {
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
                android.util.Log.d("GymDatabase", "Demo user created - Email: demo@htdgym.com, Password: demo123");
            }
            
        } catch (Exception e) {
            android.util.Log.e("GymDatabase", "Error creating default user accounts", e);
        }
    }
    
    private static String hashPassword(String password) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            android.util.Log.e("GymDatabase", "Error hashing password", e);
            return password; // Fallback
        }
    }
    
    // Method to reset database instance (useful for testing or when schema changes)
    public static synchronized void resetInstance() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}