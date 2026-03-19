package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "equipment")
public class Equipment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String category;
    private String brand;
    private Date purchaseDate;
    private double purchasePrice;
    private String condition; // Excellent, Good, Fair, Poor
    private Date lastMaintenance;
    private Date nextMaintenance;
    private String location;
    private boolean isAvailable;
    private String notes;

    // Constructors
    public Equipment() {}

    @Ignore
    public Equipment(String name, String category, String brand, Date purchaseDate, 
                    double purchasePrice, String condition, String location) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.condition = condition;
        this.location = location;
        this.isAvailable = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    public double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public Date getLastMaintenance() { return lastMaintenance; }
    public void setLastMaintenance(Date lastMaintenance) { this.lastMaintenance = lastMaintenance; }

    public Date getNextMaintenance() { return nextMaintenance; }
    public void setNextMaintenance(Date nextMaintenance) { this.nextMaintenance = nextMaintenance; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}