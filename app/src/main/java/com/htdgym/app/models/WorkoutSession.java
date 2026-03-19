package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.htdgym.app.database.DateConverter;

import java.util.Date;
import java.util.List;

@Entity(tableName = "workout_sessions")
@TypeConverters({DateConverter.class})
public class WorkoutSession {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String workoutName;
    private Date startTime;
    private Date endTime;
    private int totalExercises;
    private int completedExercises;
    private double totalVolume; // sets × reps × weight
    private int totalCaloriesBurned;
    private String workoutType; // "strength", "cardio", "hiit", etc.
    private String difficulty;
    private double averageHeartRate;
    private int restTimeTotal; // in seconds
    private String notes;
    private double userRating; // 1-5 stars
    private boolean isCompleted;
    
    // Constructors
    public WorkoutSession() {}
    
    public WorkoutSession(String workoutName, String workoutType, String difficulty) {
        this.workoutName = workoutName;
        this.workoutType = workoutType;
        this.difficulty = difficulty;
        this.startTime = new Date();
        this.isCompleted = false;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getWorkoutName() {
        return workoutName;
    }
    
    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }
    
    public Date getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    public Date getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public int getTotalExercises() {
        return totalExercises;
    }
    
    public void setTotalExercises(int totalExercises) {
        this.totalExercises = totalExercises;
    }
    
    public int getCompletedExercises() {
        return completedExercises;
    }
    
    public void setCompletedExercises(int completedExercises) {
        this.completedExercises = completedExercises;
    }
    
    public double getTotalVolume() {
        return totalVolume;
    }
    
    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }
    
    public int getTotalCaloriesBurned() {
        return totalCaloriesBurned;
    }
    
    public void setTotalCaloriesBurned(int totalCaloriesBurned) {
        this.totalCaloriesBurned = totalCaloriesBurned;
    }
    
    public String getWorkoutType() {
        return workoutType;
    }
    
    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public double getAverageHeartRate() {
        return averageHeartRate;
    }
    
    public void setAverageHeartRate(double averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }
    
    public int getRestTimeTotal() {
        return restTimeTotal;
    }
    
    public void setRestTimeTotal(int restTimeTotal) {
        this.restTimeTotal = restTimeTotal;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public double getUserRating() {
        return userRating;
    }
    
    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }
    
    public boolean isCompleted() {
        return isCompleted;
    }
    
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    
    // Helper methods
    public long getDurationInMinutes() {
        if (startTime != null && endTime != null) {
            return (endTime.getTime() - startTime.getTime()) / (1000 * 60);
        }
        return 0;
    }
    
    public double getCompletionPercentage() {
        if (totalExercises > 0) {
            return (double) completedExercises / totalExercises * 100;
        }
        return 0;
    }
    
    public String getWorkoutSummary() {
        return String.format("%s - %d phút - %.1f%% hoàn thành", 
                workoutName, getDurationInMinutes(), getCompletionPercentage());
    }
}