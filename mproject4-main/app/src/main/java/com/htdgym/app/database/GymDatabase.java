package com.htdgym.app.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.htdgym.app.models.*;

@Database(
    entities = {
        User.class,
        Admin.class,
        PremiumUser.class,
        PaymentTransaction.class,
        Exercise.class,
        FoodItem.class,
        MealLog.class,
        BodyMeasurement.class,
        WorkoutLog.class,
        WorkoutStats.class
    },
    version = 15,
    exportSchema = false
)
@TypeConverters({DateConverter.class})
public abstract class GymDatabase extends RoomDatabase {
    
    private static GymDatabase INSTANCE;
    
    // Essential DAOs
    public abstract UserDao userDao();
    public abstract AdminDao adminDao();
    public abstract PremiumUserDao premiumUserDao();
    public abstract PaymentTransactionDao paymentTransactionDao();
    public abstract ExerciseDao exerciseDao();
    public abstract FoodItemDao foodItemDao();
    public abstract MealLogDao mealLogDao();
    public abstract BodyMeasurementDao bodyMeasurementDao();
    public abstract WorkoutLogDao workoutLogDao();
    public abstract WorkoutStatsDao workoutStatsDao();
    
    public static synchronized GymDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            try {
                INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    GymDatabase.class,
                    "gym_database"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
                android.util.Log.d("GymDatabase", "Database instance created v15");
            } catch (Exception e) {
                android.util.Log.e("GymDatabase", "Error creating database", e);
                throw e;
            }
        }
        return INSTANCE;
    }

    public static synchronized void resetInstance() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}