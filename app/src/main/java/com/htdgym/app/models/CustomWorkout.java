package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "custom_workouts")
public class CustomWorkout {

    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String name;
    private String description;
    private String difficulty;
    private String category;
    private int duration; // in minutes
    private long createdAt;
    private int userId;
    private boolean isActive;

    // Default constructor (required by Room)
    public CustomWorkout() {
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
    }

    // Constructor with parameters
    @Ignore
    public CustomWorkout(String name, String description, String difficulty, 
                        String category, int duration, int userId) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
        this.duration = duration;
        this.userId = userId;
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}