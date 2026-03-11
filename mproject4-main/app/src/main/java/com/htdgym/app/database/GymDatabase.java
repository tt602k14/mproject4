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
        FoodItem.class,
        MealLog.class,
        BodyMeasurement.class,
        WorkoutLog.class
    },
    version = 7,
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
    public abstract FoodItemDao foodItemDao();
    public abstract MealLogDao mealLogDao();
    public abstract BodyMeasurementDao bodyMeasurementDao();
    public abstract WorkoutLogDao workoutLogDao();
    
    public static synchronized GymDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.getApplicationContext(),
                GymDatabase.class,
                "gym_database"
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return INSTANCE;
    }
}