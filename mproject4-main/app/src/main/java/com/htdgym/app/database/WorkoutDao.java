package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.htdgym.app.models.Workout;
import java.util.List;

@Dao
public interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    LiveData<List<Workout>> getAllWorkouts();

    @Query("SELECT * FROM workouts WHERE exerciseName LIKE '%' || :searchTerm || '%' ORDER BY date DESC")
    LiveData<List<Workout>> searchWorkouts(String searchTerm);

    @Query("SELECT * FROM workouts WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Workout>> getWorkoutsByDateRange(long startDate, long endDate);

    @Insert
    void insertWorkout(Workout workout);

    @Update
    void updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);

    @Query("SELECT COUNT(*) FROM workouts WHERE date BETWEEN :startDate AND :endDate")
    LiveData<Integer> getWorkoutCountByDateRange(long startDate, long endDate);
    
    @Query("SELECT * FROM workouts")
    List<Workout> getAllWorkoutsList();
}