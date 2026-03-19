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
import com.htdgym.app.activities.AddEquipmentActivity;
import com.htdgym.app.adapters.EquipmentAdapter;
import com.htdgym.app.viewmodels.EquipmentViewModel;

public class EquipmentFragment extends Fragment {
    private EquipmentViewModel equipmentViewModel;
    private RecyclerView recyclerView;
    private EquipmentAdapter adapter;
    private FloatingActionButton fabAddEquipment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupViewModel();
        observeData();
        setupClickListeners();
        
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_equipment);
        fabAddEquipment = view.findViewById(R.id.fab_add_equipment);
    }

    private void setupRecyclerView() {
        adapter = new EquipmentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        equipmentViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);
    }

    private void observeData() {
        equipmentViewModel.getAllEquipment().observe(getViewLifecycleOwner(), equipment -> {
            adapter.setEquipment(equipment);
        });
    }

    private void setupClickListeners() {
        fabAddEquipment.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEquipmentActivity.class);
            startActivity(intent);
        });
    }
}