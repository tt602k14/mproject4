package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private int sets;
    private int reps;
    private int weight;
    private String muscleGroup;
    private String restTime;
    private String setsReps; // New field for sets×reps format
    private String iconColor;
    private String difficulty;
    private boolean isFavorite = false;
    private String videoUrl; // YouTube video URL for exercise demonstration
    private String thumbnailUrl; // Thumbnail image URL
    
    // Additional fields for WorkoutsFragment compatibility
    private String description;
    private String category;
    private int duration;
    private int calories;
    private String equipment;
    private String targetMuscles;
    private float rating;
    private int ratingCount;
    private int completedCount;
    private boolean isPremium;

    // Default constructor for Room
    public Exercise() {}

    // Constructor for database operations
    @Ignore
    public Exercise(String name, int sets, int reps, int weight) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    // Constructor for workout library (backward compatibility)
    @Ignore
    public Exercise(String name, String muscleGroup, String setsReps, String restTime, 
                   String iconColor, String difficulty) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.setsReps = setsReps;
        this.restTime = restTime;
        this.iconColor = iconColor;
        this.difficulty = difficulty;
        this.videoUrl = "https://youtu.be/-R5sH2iG9Gw"; // Default video URL
    }

    // Constructor for workout library with video URL and thumbnail
    @Ignore
    public Exercise(String name, String muscleGroup, String setsReps, String restTime, 
                   String iconColor, String difficulty, String videoUrl, String thumbnailUrl) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.setsReps = setsReps;
        this.restTime = restTime;
        this.iconColor = iconColor;
        this.difficulty = difficulty;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    // Constructor for workout library with video URL (7 parameters - backward compatibility)
    @Ignore
    public Exercise(String name, String muscleGroup, String setsReps, String restTime, 
                   String iconColor, String difficulty, String videoUrl) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.setsReps = setsReps;
        this.restTime = restTime;
        this.iconColor = iconColor;
        this.difficulty = difficulty;
        this.videoUrl = videoUrl;
        // Auto-generate thumbnail URL from video URL
        if (videoUrl != null && videoUrl.contains("youtu")) {
            String videoId = extractVideoId(videoUrl);
            if (videoId != null) {
                this.thumbnailUrl = "https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
            }
        }
    }

    // Constructor for WorkoutsFragment (7 parameters)
    @Ignore
    public Exercise(String name, String description, String category, String difficulty, 
                   int duration, int calories, String videoUrl) {
        this.name = name;
        this.description = description;
        this.muscleGroup = category;
        this.category = category;
        this.difficulty = difficulty;
        this.duration = duration;
        this.calories = calories;
        this.videoUrl = videoUrl;
        // Generate thumbnail URL from video URL
        if (videoUrl != null && videoUrl.contains("youtu")) {
            String videoId = extractVideoId(videoUrl);
            if (videoId != null) {
                this.thumbnailUrl = "https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
            }
        }
    }

    // Constructor for WorkoutsFragment with premium flag (8 parameters)
    @Ignore
    public Exercise(String name, String description, String category, String difficulty, 
                   int duration, int calories, String videoUrl, boolean isPremium) {
        this.name = name;
        this.description = description;
        this.muscleGroup = category;
        this.category = category;
        this.difficulty = difficulty;
        this.duration = duration;
        this.calories = calories;
        this.videoUrl = videoUrl;
        this.isPremium = isPremium;
        // Generate thumbnail URL from video URL
        if (videoUrl != null && videoUrl.contains("youtu")) {
            String videoId = extractVideoId(videoUrl);
            if (videoId != null) {
                this.thumbnailUrl = "https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
            }
        }
    }

    // ===== Getter / Setter =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getSets() {
        return sets;
    }
    
    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }
    
    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getRestTime() {
        return restTime;
    }

    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }
    
    public String getSetsReps() {
        return setsReps;
    }
    
    public void setSetsReps(String setsReps) {
        this.setsReps = setsReps;
    }
    
    // Method to get formatted sets×reps string
    public String getFormattedSetsReps() {
        if (setsReps != null) {
            return setsReps;
        }
        return sets + "×" + reps;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }
    
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    
    // Additional getters and setters for WorkoutsFragment compatibility
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category != null ? category : muscleGroup;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public int getCalories() {
        return calories;
    }
    
    public void setCalories(int calories) {
        this.calories = calories;
    }
    
    public String getEquipment() {
        return equipment;
    }
    
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
    
    public String getTargetMuscles() {
        return targetMuscles;
    }
    
    public void setTargetMuscles(String targetMuscles) {
        this.targetMuscles = targetMuscles;
    }
    
    public float getRating() {
        return rating;
    }
    
    public void setRating(float rating) {
        this.rating = rating;
    }
    
    public int getRatingCount() {
        return ratingCount;
    }
    
    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
    
    public int getCompletedCount() {
        return completedCount;
    }
    
    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }
    
    public boolean isPremium() {
        return isPremium;
    }
    
    public void setPremium(boolean premium) {
        isPremium = premium;
    }
    
    // Helper method to extract video ID from YouTube URL
    private String extractVideoId(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) return null;
        
        // Handle youtu.be format
        if (youtubeUrl.contains("youtu.be/")) {
            String[] parts = youtubeUrl.split("youtu.be/");
            if (parts.length > 1) {
                String videoId = parts[1];
                if (videoId.contains("?")) {
                    videoId = videoId.split("\\?")[0];
                }
                return videoId;
            }
        }
        
        // Handle youtube.com format
        if (youtubeUrl.contains("watch?v=")) {
            String[] parts = youtubeUrl.split("watch\\?v=");
            if (parts.length > 1) {
                String videoId = parts[1];
                if (videoId.contains("&")) {
                    videoId = videoId.split("&")[0];
                }
                return videoId;
            }
        }
        
        return null;
    }
}
