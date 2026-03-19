package com.htdgym.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import java.util.Date;

@Entity(tableName = "payment_transactions")
public class PaymentTransaction {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String userId;
    private String paymentCode;
    private String planId;
    private String planName;
    private double amount;
    private String currency;
    private String paymentMethod; // "bank_transfer", "promo_code"
    private String status; // "pending", "completed", "failed", "expired"
    private Date createdAt;
    private Date completedAt;
    private String transactionReference;
    private String bankAccount;
    private String transferContent;
    
    // Constructors
    public PaymentTransaction() {}
    
    @Ignore
    public PaymentTransaction(String userId, String paymentCode, String planId, 
                            String planName, double amount, String paymentMethod) {
        this.userId = userId;
        this.paymentCode = paymentCode;
        this.planId = planId;
        this.planName = planName;
        this.amount = amount;
        this.currency = "VND";
        this.paymentMethod = paymentMethod;
        this.status = "pending";
        this.createdAt = new Date();
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getPaymentCode() { return paymentCode; }
    public void setPaymentCode(String paymentCode) { this.paymentCode = paymentCode; }
    
    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }
    
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getCompletedAt() { return completedAt; }
    public void setCompletedAt(Date completedAt) { this.completedAt = completedAt; }
    
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    
    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }
    
    public String getTransferContent() { return transferContent; }
    public void setTransferContent(String transferContent) { this.transferContent = transferContent; }
    
    // Helper methods
    public boolean isPending() { return "pending".equals(status); }
    public boolean isCompleted() { return "completed".equals(status); }
    public boolean isFailed() { return "failed".equals(status); }
    public boolean isExpired() { return "expired".equals(status); }
    
    public void markCompleted() {
        this.status = "completed";
        this.completedAt = new Date();
    }
    
    public void markFailed() {
        this.status = "failed";
        this.completedAt = new Date();
    }
    
    public void markExpired() {
        this.status = "expired";
        this.completedAt = new Date();
    }
    
    public String getTransactionId() { return paymentCode; } // Alias for paymentCode
    public String getDescription() { return planName; } // Alias for planName
}