package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "workouts")
public class Workout {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String exerciseName;
    private int sets;
    private int reps;
    private double weight;
    private int duration;
    private Date date;
    private String videoUrl; // YouTube video URL
    private String category; // chest, shoulder, legs, abs, back

    // ===== Constructor =====
    public Workout(String exerciseName, int sets, int reps,
                   double weight, int duration, Date date) {
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.duration = duration;
        this.date = date;
    }

    // ===== Getter / Setter (BẮT BUỘC) =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }
    
    public int getSets() { return sets; }
    public void setSets(int sets) { this.sets = sets; }
    
    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
    
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
