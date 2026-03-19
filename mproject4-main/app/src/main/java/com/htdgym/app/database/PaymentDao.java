package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.htdgym.app.models.Payment;
import java.util.List;

@Dao
public interface PaymentDao {
    @Query("SELECT * FROM payments ORDER BY paymentDate DESC")
    LiveData<List<Payment>> getAllPayments();

    @Query("SELECT * FROM payments WHERE memberId = :memberId ORDER BY paymentDate DESC")
    LiveData<List<Payment>> getPaymentsByMember(int memberId);

    @Query("SELECT * FROM payments WHERE paymentDate BETWEEN :startDate AND :endDate ORDER BY paymentDate DESC")
    LiveData<List<Payment>> getPaymentsByDateRange(long startDate, long endDate);

    @Query("SELECT SUM(amount) FROM payments WHERE paymentDate BETWEEN :startDate AND :endDate")
    LiveData<Double> getTotalRevenueByDateRange(long startDate, long endDate);

    @Insert
    void insertPayment(Payment payment);

    @Update
    void updatePayment(Payment payment);

    @Delete
    void deletePayment(Payment payment);

    @Query("SELECT COUNT(*) FROM payments WHERE paymentDate BETWEEN :startDate AND :endDate")
    LiveData<Integer> getPaymentCountByDateRange(long startDate, long endDate);
}