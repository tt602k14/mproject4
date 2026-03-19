package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.htdgym.app.models.CustomWorkout;

import java.util.List;

@Dao
public interface CustomWorkoutDao {

    @Insert
    long insert(CustomWorkout customWorkout);

    @Update
    void update(CustomWorkout customWorkout);

    @Delete
    void delete(CustomWorkout customWorkout);

    @Query("SELECT * FROM custom_workouts WHERE userId = :userId AND isActive = 1 ORDER BY createdAt DESC")
    List<CustomWorkout> getWorkoutsByUserId(int userId);

    @Query("SELECT * FROM custom_workouts WHERE id = :id")
    CustomWorkout getWorkoutById(int id);

    @Query("SELECT * FROM custom_workouts WHERE userId = :userId AND name = :name")
    CustomWorkout getWorkoutByName(int userId, String name);

    @Query("SELECT COUNT(*) FROM custom_workouts WHERE userId = :userId AND isActive = 1")
    int getWorkoutCount(int userId);

    @Query("UPDATE custom_workouts SET isActive = 0 WHERE id = :id")
    void deactivateWorkout(int id);

    @Query("SELECT * FROM custom_workouts WHERE userId = :userId AND difficulty = :difficulty AND isActive = 1")
    List<CustomWorkout> getWorkoutsByDifficulty(int userId, String difficulty);
}