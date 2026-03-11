package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "body_measurements")
public class BodyMeasurement {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private Date date;
    private float weight;
    private float chest;
    private float waist;
    private float hips;
    private float arms;
    private float thighs;
    private String notes;

    public BodyMeasurement(int userId, Date date, float weight, float chest, float waist, 
                          float hips, float arms, float thighs) {
        this.userId = userId;
        this.date = date;
        this.weight = weight;
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
        this.arms = arms;
        this.thighs = thighs;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }
    
    public float getChest() { return chest; }
    public void setChest(float chest) { this.chest = chest; }
    
    public float getWaist() { return waist; }
    public void setWaist(float waist) { this.waist = waist; }
    
    public float getHips() { return hips; }
    public void setHips(float hips) { this.hips = hips; }
    
    public float getArms() { return arms; }
    public void setArms(float arms) { this.arms = arms; }
    
    public float getThighs() { return thighs; }
    public void setThighs(float thighs) { this.thighs = thighs; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
