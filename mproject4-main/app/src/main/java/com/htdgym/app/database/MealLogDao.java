package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.htdgym.app.models.MealLog;
import java.util.Date;
import java.util.List;

@Dao
public interface MealLogDao {
    @Insert
    long insert(MealLog mealLog);
    
    @Query("SELECT * FROM meal_logs WHERE userId = :userId AND date = :date ORDER BY mealType ASC")
    List<MealLog> getMealLogsByDate(int userId, Date date);
    
    @Query("SELECT * FROM meal_logs WHERE userId = :userId AND mealType = :mealType AND date = :date")
    List<MealLog> getMealLogsByTypeAndDate(int userId, String mealType, Date date);
    
    @Query("SELECT SUM(totalCalories) FROM meal_logs WHERE userId = :userId AND date = :date")
    Integer getTotalCaloriesByDate(int userId, Date date);
    
    @Query("SELECT SUM(totalProtein) FROM meal_logs WHERE userId = :userId AND date = :date")
    Float getTotalProteinByDate(int userId, Date date);
    
    @Query("SELECT SUM(totalCarbs) FROM meal_logs WHERE userId = :userId AND date = :date")
    Float getTotalCarbsByDate(int userId, Date date);
    
    @Query("SELECT SUM(totalFat) FROM meal_logs WHERE userId = :userId AND date = :date")
    Float getTotalFatByDate(int userId, Date date);
    
    @Query("DELETE FROM meal_logs WHERE id = :id")
    void deleteMealLog(int id);
}
