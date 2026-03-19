package com.htdgym.app.models;

public class Food {
    private String name;
    private int caloriesPer100g;
    private String category;
    private double protein;
    private double carbs;
    private double fat;

    public Food(String name, int caloriesPer100g, String category, double protein, double carbs, double fat) {
        this.name = name;
        this.caloriesPer100g = caloriesPer100g;
        this.category = category;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCaloriesPer100g() { return caloriesPer100g; }
    public void setCaloriesPer100g(int caloriesPer100g) { this.caloriesPer100g = caloriesPer100g; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }

    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
}