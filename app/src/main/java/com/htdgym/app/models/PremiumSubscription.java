package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "premium_subscriptions")
public class PremiumSubscription {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private String subscriptionType; // monthly, yearly
    private long startDate;
    private long endDate;
    private boolean isActive;
    private String paymentMethod;
    private double price;

    public PremiumSubscription() {}

    @Ignore
    public PremiumSubscription(int userId, String subscriptionType, long startDate, long endDate, double price) {
        this.userId = userId;
        this.subscriptionType = subscriptionType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSubscriptionType() { return subscriptionType; }
    public void setSubscriptionType(String subscriptionType) { this.subscriptionType = subscriptionType; }

    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }

    public long getEndDate() { return endDate; }
    public void setEndDate(long endDate) { this.endDate = endDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}