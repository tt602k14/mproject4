package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "meal_logs")
public class MealLog {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int foodItemId;
    private String mealType; // Breakfast, Lunch, Dinner, Snack
    private Date date;
    private float servings;
    private int totalCalories;
    private float totalProtein;
    private float totalCarbs;
    private float totalFat;

    public MealLog(int userId, int foodItemId, String mealType, Date date, float servings,
                   int totalCalories, float totalProtein, float totalCarbs, float totalFat) {
        this.userId = userId;
        this.foodItemId = foodItemId;
        this.mealType = mealType;
        this.date = date;
        this.servings = servings;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalCarbs = totalCarbs;
        this.totalFat = totalFat;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getFoodItemId() { return foodItemId; }
    public void setFoodItemId(int foodItemId) { this.foodItemId = foodItemId; }
    
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public float getServings() { return servings; }
    public void setServings(float servings) { this.servings = servings; }
    
    public int getTotalCalories() { return totalCalories; }
    public void setTotalCalories(int totalCalories) { this.totalCalories = totalCalories; }
    
    public float getTotalProtein() { return totalProtein; }
    public void setTotalProtein(float totalProtein) { this.totalProtein = totalProtein; }
    
    public float getTotalCarbs() { return totalCarbs; }
    public void setTotalCarbs(float totalCarbs) { this.totalCarbs = totalCarbs; }
    
    public float getTotalFat() { return totalFat; }
    public void setTotalFat(float totalFat) { this.totalFat = totalFat; }
}
