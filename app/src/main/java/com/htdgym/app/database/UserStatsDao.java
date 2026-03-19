package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.htdgym.app.models.UserStats;

import java.util.Date;
import java.util.List;

@Dao
public interface UserStatsDao {
    
    @Query("SELECT * FROM user_stats WHERE userId = :userId ORDER BY date DESC LIMIT 1")
    LiveData<UserStats> getUserStats(int userId);
    
    @Query("SELECT * FROM user_stats WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<UserStats>> getAllUserStats(int userId);
    
    @Query("SELECT * FROM user_stats WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    LiveData<List<UserStats>> getUserStatsByDateRange(int userId, Date startDate, Date endDate);
    
    @Insert
    void insertUserStats(UserStats userStats);
    
    @Update
    void updateUserStats(UserStats userStats);
    
    @Query("DELETE FROM user_stats WHERE userId = :userId")
    void deleteUserStats(int userId);
}
