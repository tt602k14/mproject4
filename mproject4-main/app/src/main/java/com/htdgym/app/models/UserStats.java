package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "user_stats")
public class UserStats {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int userId;
    private Date date;
    private int streakDays;
    private int totalWorkouts;
    private int caloriesToday;
    private int waterIntake;
    private int todayGoalProgress;
    private double currentWeight;
    private double targetWeight;
    private int waistSize;
    private Date lastUpdated;

    public UserStats() {
        this.userId = 1;
        this.date = new Date();
        this.streakDays = 0;
        this.totalWorkouts = 0;
        this.caloriesToday = 0;
        this.waterIntake = 0;
        this.todayGoalProgress = 0;
        this.currentWeight = 0;
        this.targetWeight = 0;
        this.waistSize = 0;
        this.lastUpdated = new Date();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getStreakDays() { return streakDays; }
    public void setStreakDays(int streakDays) { this.streakDays = streakDays; }

    public int getTotalWorkouts() { return totalWorkouts; }
    public void setTotalWorkouts(int totalWorkouts) { this.totalWorkouts = totalWorkouts; }

    public int getCaloriesToday() { return caloriesToday; }
    public void setCaloriesToday(int caloriesToday) { this.caloriesToday = caloriesToday; }

    public int getWaterIntake() { return waterIntake; }
    public void setWaterIntake(int waterIntake) { this.waterIntake = waterIntake; }

    public int getTodayGoalProgress() { return todayGoalProgress; }
    public void setTodayGoalProgress(int todayGoalProgress) { this.todayGoalProgress = todayGoalProgress; }

    public double getCurrentWeight() { return currentWeight; }
    public void setCurrentWeight(double currentWeight) { this.currentWeight = currentWeight; }

    public double getTargetWeight() { return targetWeight; }
    public void setTargetWeight(double targetWeight) { this.targetWeight = targetWeight; }

    public int getWaistSize() { return waistSize; }
    public void setWaistSize(int waistSize) { this.waistSize = waistSize; }

    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
}
