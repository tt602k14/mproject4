package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "videos")
public class Video {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String title;
    private String description;
    private String youtubeId;
    private String thumbnail;
    private String duration;
    private String difficulty;
    private String category;
    private int views;
    private int likes;
    private boolean isSaved;
    private boolean isWatched;

    public Video(String title, String description, String youtubeId, String thumbnail,
                 String duration, String difficulty, String category) {
        this.title = title;
        this.description = description;
        this.youtubeId = youtubeId;
        this.thumbnail = thumbnail;
        this.duration = duration;
        this.difficulty = difficulty;
        this.category = category;
        this.views = 0;
        this.likes = 0;
        this.isSaved = false;
        this.isWatched = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getYoutubeId() { return youtubeId; }
    public void setYoutubeId(String youtubeId) { this.youtubeId = youtubeId; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public boolean isSaved() { return isSaved; }
    public void setSaved(boolean saved) { isSaved = saved; }

    public boolean isWatched() { return isWatched; }
    public void setWatched(boolean watched) { isWatched = watched; }
}
