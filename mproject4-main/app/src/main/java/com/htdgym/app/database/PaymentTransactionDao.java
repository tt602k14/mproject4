package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.htdgym.app.models.PaymentTransaction;

import java.util.List;

@Dao
public interface PaymentTransactionDao {
    
    @Insert
    long insertTransaction(PaymentTransaction transaction);
    
    @Update
    void updateTransaction(PaymentTransaction transaction);
    
    @Query("SELECT * FROM payment_transactions WHERE paymentCode = :paymentCode LIMIT 1")
    PaymentTransaction getTransactionByPaymentCode(String paymentCode);
    
    @Query("SELECT * FROM payment_transactions WHERE userId = :userId ORDER BY createdAt DESC")
    List<PaymentTransaction> getTransactionsByUserId(String userId);
    
    @Query("SELECT * FROM payment_transactions WHERE status = :status ORDER BY createdAt DESC")
    List<PaymentTransaction> getTransactionsByStatus(String status);
    
    @Query("UPDATE payment_transactions SET status = :status, completedAt = :completedAt WHERE paymentCode = :paymentCode")
    void updateTransactionStatus(String paymentCode, String status, java.util.Date completedAt);
    
    @Query("SELECT * FROM payment_transactions WHERE status = 'pending' AND createdAt < :expiredTime")
    List<PaymentTransaction> getExpiredTransactions(java.util.Date expiredTime);
    
    @Query("DELETE FROM payment_transactions WHERE id = :id")
    void deleteTransaction(int id);
    
    @Query("SELECT COUNT(*) FROM payment_transactions WHERE userId = :userId AND status = 'completed'")
    int getCompletedTransactionCount(String userId);
    
    @Query("SELECT * FROM payment_transactions ORDER BY createdAt DESC")
    List<PaymentTransaction> getAllTransactions();
    
    @Query("SELECT SUM(amount) FROM payment_transactions WHERE status = 'completed' AND DATE(createdAt) = DATE('now')")
    double getTodayRevenue();
    
    @Query("SELECT SUM(amount) FROM payment_transactions WHERE status = 'completed' AND strftime('%Y-%m', createdAt) = strftime('%Y-%m', 'now')")
    double getMonthRevenue();
    
    @Query("SELECT COUNT(*) FROM payment_transactions")
    int getTotalTransactionCount();
    
    @Query("SELECT COUNT(*) FROM payment_transactions WHERE status = 'completed'")
    int getSuccessfulTransactionCount();
}