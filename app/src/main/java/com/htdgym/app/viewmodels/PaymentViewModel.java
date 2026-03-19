package com.htdgym.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.htdgym.app.models.Payment;
import com.htdgym.app.repository.GymRepository;
import java.util.List;

public class PaymentViewModel extends AndroidViewModel {
    private GymRepository repository;
    private LiveData<List<Payment>> allPayments;

    public PaymentViewModel(@NonNull Application application) {
        super(application);
        repository = new GymRepository(application);
        allPayments = repository.getAllPayments();
    }

    public LiveData<List<Payment>> getAllPayments() {
        return allPayments;
    }

    public LiveData<List<Payment>> getPaymentsByMember(int memberId) {
        return repository.getPaymentsByMember(memberId);
    }

    public LiveData<Double> getTotalRevenueByDateRange(long startDate, long endDate) {
        return repository.getTotalRevenueByDateRange(startDate, endDate);
    }

    public void insert(Payment payment) {
        repository.insertPayment(payment);
    }

    public void update(Payment payment) {
        repository.updatePayment(payment);
    }

    public void delete(Payment payment) {
        repository.deletePayment(payment);
    }
}