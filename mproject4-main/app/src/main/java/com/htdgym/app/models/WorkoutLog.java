package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "workout_logs")
public class WorkoutLog {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private String workoutName;
    private Date date;
    private int duration; // in minutes
    private int caloriesBurned;
    private String notes;

    public WorkoutLog(int userId, String workoutName, Date date, int duration, int caloriesBurned) {
        this.userId = userId;
        this.workoutName = workoutName;
        this.date = date;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getWorkoutName() { return workoutName; }
    public void setWorkoutName(String workoutName) { this.workoutName = workoutName; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
