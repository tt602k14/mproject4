package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.htdgym.app.models.FoodItem;
import java.util.List;

@Dao
public interface FoodItemDao {
    @Insert
    long insert(FoodItem foodItem);
    
    @Update
    void update(FoodItem foodItem);
    
    @Query("SELECT * FROM food_items ORDER BY name ASC")
    List<FoodItem> getAllFoods();
    
    @Query("SELECT * FROM food_items WHERE category = :category ORDER BY name ASC")
    List<FoodItem> getFoodsByCategory(String category);
    
    @Query("SELECT * FROM food_items WHERE isFavorite = 1 ORDER BY name ASC")
    List<FoodItem> getFavoriteFoods();
    
    @Query("SELECT * FROM food_items WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    List<FoodItem> searchFoods(String query);
    
    @Query("SELECT * FROM food_items WHERE id = :id")
    FoodItem getFoodById(int id);
}
