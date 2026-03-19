package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.htdgym.app.models.WorkoutStats;

@Dao
public interface WorkoutStatsDao {
    
    @Insert
    long insert(WorkoutStats stats);
    
    @Update
    void update(WorkoutStats stats);
    
    @Query("SELECT * FROM workout_stats WHERE userId = :userId LIMIT 1")
    WorkoutStats getStatsByUserId(String userId);
    
    @Query("DELETE FROM workout_stats WHERE userId = :userId")
    void deleteByUserId(String userId);
}
