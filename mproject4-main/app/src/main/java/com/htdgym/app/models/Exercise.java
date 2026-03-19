package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String category; // chest, shoulder, legs, abs, back, all
    private String difficulty; // beginner, intermediate, advanced
    private int duration; // in minutes
    private int calories; // estimated calories burned
    private String videoUrl;
    private String thumbnailUrl;
    private String instructions; // Step-by-step instructions
    private String imageUrl; // Exercise image URL
    private boolean isFavorite;
    private int completedCount;
    private String equipment; // none, dumbbells, resistance_band, etc.
    private String targetMuscles;
    private float rating;
    private int ratingCount;
    private boolean isPremium; // Indicates if this exercise requires premium subscription

    public Exercise() {}

    @Ignore
    public Exercise(String name, String description, String category, String difficulty, 
                   int duration, int calories, String videoUrl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.duration = duration;
        this.calories = calories;
        this.videoUrl = videoUrl;
        this.isFavorite = false;
        this.completedCount = 0;
        this.rating = 0.0f;
        this.ratingCount = 0;
        this.isPremium = false; // Default to free exercise
    }
    
    @Ignore
    public Exercise(String name, String description, String category, String difficulty, 
                   int duration, int calories, String videoUrl, boolean isPremium) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.duration = duration;
        this.calories = calories;
        this.videoUrl = videoUrl;
        this.isFavorite = false;
        this.completedCount = 0;
        this.rating = 0.0f;
        this.ratingCount = 0;
        this.isPremium = isPremium;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public int getCompletedCount() { return completedCount; }
    public void setCompletedCount(int completedCount) { this.completedCount = completedCount; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public String getTargetMuscles() { return targetMuscles; }
    public void setTargetMuscles(String targetMuscles) { this.targetMuscles = targetMuscles; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public int getRatingCount() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }

    public boolean isPremium() { return isPremium; }
    public void setPremium(boolean premium) { isPremium = premium; }

    // Helper methods
    public String getDifficultyEmoji() {
        switch (difficulty.toLowerCase()) {
            case "beginner": return "🟢";
            case "intermediate": return "🟡";
            case "advanced": return "🔴";
            default: return "⚪";
        }
    }

    public String getCategoryEmoji() {
        switch (category.toLowerCase()) {
            case "chest": return "🦾";
            case "shoulder": return "🏋️";
            case "legs": return "🦵";
            case "abs": return "🔥";
            case "back": return "🏃";
            default: return "💪";
        }
    }

    public String getFormattedDuration() {
        if (duration < 60) {
            return duration + " phút";
        } else {
            int hours = duration / 60;
            int minutes = duration % 60;
            if (minutes == 0) {
                return hours + " giờ";
            } else {
                return hours + "h " + minutes + "m";
            }
        }
    }
}