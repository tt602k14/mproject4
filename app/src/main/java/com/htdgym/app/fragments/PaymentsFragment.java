package com.htdgym.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htdgym.app.R;
import com.htdgym.app.activities.AddPaymentActivity;
import com.htdgym.app.adapters.PaymentAdapter;
import com.htdgym.app.viewmodels.PaymentViewModel;

public class PaymentsFragment extends Fragment {
    private PaymentViewModel paymentViewModel;
    private RecyclerView recyclerView;
    private PaymentAdapter adapter;
    private FloatingActionButton fabAddPayment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payments, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupViewModel();
        observeData();
        setupClickListeners();
        
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_payments);
        fabAddPayment = view.findViewById(R.id.fab_add_payment);
    }

    private void setupRecyclerView() {
        adapter = new PaymentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
    }

    private void observeData() {
        paymentViewModel.getAllPayments().observe(getViewLifecycleOwner(), payments -> {
            adapter.setPayments(payments);
        });
    }

    private void setupClickListeners() {
        fabAddPayment.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddPaymentActivity.class);
            startActivity(intent);
        });
    }
}