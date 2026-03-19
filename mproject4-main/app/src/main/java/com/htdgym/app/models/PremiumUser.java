package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import java.util.Date;

@Entity(tableName = "premium_users")
public class PremiumUser {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String userId;
    private boolean isPremium;
    private String subscriptionType; // "monthly", "yearly", "lifetime"
    private Date subscriptionStartDate;
    private Date subscriptionEndDate;
    private String paymentMethod; // "google_play", "paypal", "credit_card"
    private String transactionId;
    private double price;
    private String currency;
    private boolean isActive;

    // Constructors
    public PremiumUser() {}

    @Ignore
    public PremiumUser(String userId, boolean isPremium, String subscriptionType, 
                      Date subscriptionStartDate, Date subscriptionEndDate) {
        this.userId = userId;
        this.isPremium = isPremium;
        this.subscriptionType = subscriptionType;
        this.subscriptionStartDate = subscriptionStartDate;
        this.subscriptionEndDate = subscriptionEndDate;
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public boolean isPremium() { return isPremium; }
    public void setPremium(boolean premium) { isPremium = premium; }

    public String getSubscriptionType() { return subscriptionType; }
    public void setSubscriptionType(String subscriptionType) { this.subscriptionType = subscriptionType; }

    public Date getSubscriptionStartDate() { return subscriptionStartDate; }
    public void setSubscriptionStartDate(Date subscriptionStartDate) { this.subscriptionStartDate = subscriptionStartDate; }

    public Date getSubscriptionEndDate() { return subscriptionEndDate; }
    public void setSubscriptionEndDate(Date subscriptionEndDate) { this.subscriptionEndDate = subscriptionEndDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    // Helper methods
    public boolean isSubscriptionValid() {
        if (!isPremium || !isActive) return false;
        if (subscriptionEndDate == null) return true; // Lifetime
        return new Date().before(subscriptionEndDate);
    }

    public long getDaysRemaining() {
        if (subscriptionEndDate == null) return Long.MAX_VALUE; // Lifetime
        long diff = subscriptionEndDate.getTime() - new Date().getTime();
        return diff / (24 * 60 * 60 * 1000);
    }
}