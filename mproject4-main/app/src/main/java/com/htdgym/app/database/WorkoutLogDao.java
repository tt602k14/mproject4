package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.htdgym.app.models.WorkoutLog;
import java.util.List;

@Dao
public interface WorkoutLogDao {
    @Insert
    long insert(WorkoutLog workoutLog);
    
    @Query("SELECT * FROM workout_logs WHERE userId = :userId ORDER BY date DESC LIMIT :limit")
    List<WorkoutLog> getRecentWorkouts(int userId, int limit);
    
    @Query("SELECT * FROM workout_logs WHERE userId = :userId ORDER BY date DESC")
    List<WorkoutLog> getAllWorkouts(int userId);
    
    @Query("SELECT COUNT(*) FROM workout_logs WHERE userId = :userId")
    int getTotalWorkouts(int userId);
    
    @Query("SELECT SUM(caloriesBurned) FROM workout_logs WHERE userId = :userId")
    Integer getTotalCalories(int userId);
    
    @Query("SELECT SUM(duration) FROM workout_logs WHERE userId = :userId")
    Integer getTotalMinutes(int userId);
    
    @Query("DELETE FROM workout_logs WHERE id = :id")
    void deleteWorkoutLog(int id);

    @Query("SELECT * FROM workout_logs WHERE userId = :userId AND date >= :since ORDER BY date DESC")
    List<WorkoutLog> getWorkoutsSince(int userId, long since);

    @Query("SELECT SUM(caloriesBurned) FROM workout_logs WHERE userId = :userId AND date >= :since")
    Integer getCaloriesSince(int userId, long since);
}
