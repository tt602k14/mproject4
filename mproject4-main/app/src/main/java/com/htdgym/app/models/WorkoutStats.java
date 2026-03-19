package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_stats")
public class WorkoutStats {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String userId;
    private int totalCalories;
    private int totalDays;
    private int totalSeconds; // Total workout time in seconds
    private int totalWorkouts;
    private long lastWorkoutDate; // Timestamp of last workout
    
    public WorkoutStats() {
        this.totalCalories = 0;
        this.totalDays = 0;
        this.totalSeconds = 0;
        this.totalWorkouts = 0;
        this.lastWorkoutDate = 0;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public int getTotalCalories() {
        return totalCalories;
    }
    
    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }
    
    public int getTotalDays() {
        return totalDays;
    }
    
    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }
    
    public int getTotalSeconds() {
        return totalSeconds;
    }
    
    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }
    
    public int getTotalWorkouts() {
        return totalWorkouts;
    }
    
    public void setTotalWorkouts(int totalWorkouts) {
        this.totalWorkouts = totalWorkouts;
    }
    
    public long getLastWorkoutDate() {
        return lastWorkoutDate;
    }
    
    public void setLastWorkoutDate(long lastWorkoutDate) {
        this.lastWorkoutDate = lastWorkoutDate;
    }
    
    // Helper method to format time
    public String getFormattedTime() {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
