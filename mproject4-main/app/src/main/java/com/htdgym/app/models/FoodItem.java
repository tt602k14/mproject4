package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_items")
public class FoodItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String category; // Breakfast, Lunch, Dinner, Snack
    private int calories;
    private float protein;
    private float carbs;
    private float fat;
    private String servingSize;
    private boolean isFavorite;

    public FoodItem(String name, String category, int calories, float protein, float carbs, float fat, String servingSize) {
        this.name = name;
        this.category = category;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.servingSize = servingSize;
        this.isFavorite = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
    
    public float getProtein() { return protein; }
    public void setProtein(float protein) { this.protein = protein; }
    
    public float getCarbs() { return carbs; }
    public void setCarbs(float carbs) { this.carbs = carbs; }
    
    public float getFat() { return fat; }
    public void setFat(float fat) { this.fat = fat; }
    
    public String getServingSize() { return servingSize; }
    public void setServingSize(String servingSize) { this.servingSize = servingSize; }
    
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
