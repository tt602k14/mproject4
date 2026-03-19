package com.htdgym.app.models;

public class WorkoutTemplate {
    
    private String type;
    private String name;
    private String description;
    private String duration;
    private String difficulty;
    private String exerciseCount;
    private String color;
    private String thumbnailUrl;
    
    public WorkoutTemplate() {}
    
    public WorkoutTemplate(String type, String name, String description, String duration, 
                          String difficulty, String exerciseCount, String color, String thumbnailUrl) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.difficulty = difficulty;
        this.exerciseCount = exerciseCount;
        this.color = color;
        this.thumbnailUrl = thumbnailUrl;
    }
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getExerciseCount() {
        return exerciseCount;
    }
    
    public void setExerciseCount(String exerciseCount) {
        this.exerciseCount = exerciseCount;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    
    public String getDifficultyEmoji() {
        switch (difficulty) {
            case "Dễ": return "🟢";
            case "Trung bình": return "🟡";
            case "Khó": return "🔴";
            default: return "⚪";
        }
    }
}