package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "progress_photos")
public class ProgressPhoto {

    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int userId;
    private String photoPath;
    private long date;
    private float weight;
    private String notes;
    private String bodyPart; // front, side, back
    private boolean isActive;

    public ProgressPhoto() {
        this.isActive = true;
        this.date = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getBodyPart() { return bodyPart; }
    public void setBodyPart(String bodyPart) { this.bodyPart = bodyPart; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}