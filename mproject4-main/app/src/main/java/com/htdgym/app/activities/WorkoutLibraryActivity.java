package com.htdgym.app.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.ExerciseCardAdapter;
import com.htdgym.app.models.Exercise;

import java.util.ArrayList;
import java.util.List;
import com.htdgym.app.activities.BaseActivity;

public class WorkoutLibraryActivity extends BaseActivity {

    private RecyclerView recyclerExercises;
    private ExerciseCardAdapter adapter;
    private TextView tabAll, tabChest, tabBack, tabLegs, tabArms, tabAbs;
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_library);

        initViews();
        setupRecyclerView();
        setupTabs();
        loadExercises();
    }

    private void initViews() {
        recyclerExercises = findViewById(R.id.recycler_exercises);
        tabAll = findViewById(R.id.tab_all);
        tabChest = findViewById(R.id.tab_chest);
        tabBack = findViewById(R.id.tab_back);
        tabLegs = findViewById(R.id.tab_legs);
        tabArms = findViewById(R.id.tab_arms);
        tabAbs = findViewById(R.id.tab_abs);
    }

    private void setupRecyclerView() {
        adapter = new ExerciseCardAdapter(new ArrayList<>());
        recyclerExercises.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerExercises.setAdapter(adapter);
    }

    private void setupTabs() {
        tabAll.setOnClickListener(v -> filterExercises("all"));
        tabChest.setOnClickListener(v -> filterExercises("Ngực"));
        tabBack.setOnClickListener(v -> filterExercises("Lưng"));
        tabLegs.setOnClickListener(v -> filterExercises("Chân"));
        tabArms.setOnClickListener(v -> filterExercises("Tay"));
        tabAbs.setOnClickListener(v -> filterExercises("Bụng"));
    }

    private void filterExercises(String filter) {
        currentFilter = filter;
        
        // Reset all tabs
        resetTabStyles();
        
        // Highlight selected tab
        TextView selectedTab = null;
        switch (filter) {
            case "all":
                selectedTab = tabAll;
                break;
            case "Ngực":
                selectedTab = tabChest;
                break;
            case "Lưng":
                selectedTab = tabBack;
                break;
            case "Chân":
                selectedTab = tabLegs;
                break;
            case "Tay":
                selectedTab = tabArms;
                break;
            case "Bụng":
                selectedTab = tabAbs;
                break;
        }
        
        if (selectedTab != null) {
            selectedTab.setBackgroundResource(R.drawable.button_green);
            selectedTab.setTextColor(getResources().getColor(android.R.color.white));
        }
        
        loadExercises();
    }

    private void resetTabStyles() {
        int grayBg = getResources().getColor(android.R.color.darker_gray);
        int grayText = getResources().getColor(android.R.color.darker_gray);
        
        tabAll.setBackgroundColor(0xFFE0E0E0);
        tabAll.setTextColor(0xFF666666);
        tabChest.setBackgroundColor(0xFFE0E0E0);
        tabChest.setTextColor(0xFF666666);
        tabBack.setBackgroundColor(0xFFE0E0E0);
        tabBack.setTextColor(0xFF666666);
        tabLegs.setBackgroundColor(0xFFE0E0E0);
        tabLegs.setTextColor(0xFF666666);
        tabArms.setBackgroundColor(0xFFE0E0E0);
        tabArms.setTextColor(0xFF666666);
        tabAbs.setBackgroundColor(0xFFE0E0E0);
        tabAbs.setTextColor(0xFF666666);
    }

    private void loadExercises() {
        List<Exercise> exercises = getSampleExercises();
        
        if (!currentFilter.equals("all")) {
            List<Exercise> filtered = new ArrayList<>();
            for (Exercise exercise : exercises) {
                if (exercise.getMuscleGroup().equals(currentFilter)) {
                    filtered.add(exercise);
                }
            }
            adapter.updateExercises(filtered);
        } else {
            adapter.updateExercises(exercises);
        }
    }

    private List<Exercise> getSampleExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        // Ngực
        exercises.add(new Exercise("Bench Press", "Ngực", "3×8-12", "90s", 
                "#FF6B6B", "intermediate"));
        exercises.add(new Exercise("Push-up", "Ngực", "3×15-20", "60s", 
                "#FF6B6B", "beginner"));
        
        // Chân
        exercises.add(new Exercise("Squat", "Chân", "4×6-10", "120s", 
                "#4ECDC4", "intermediate"));
        exercises.add(new Exercise("Leg Press", "Chân", "4×10-12", "90s", 
                "#4ECDC4", "intermediate"));
        
        // Lưng
        exercises.add(new Exercise("Deadlift", "Lưng", "3×5-8", "120s", 
                "#4CAF50", "advanced"));
        exercises.add(new Exercise("Pull-up", "Lưng", "3×max", "90s", 
                "#4CAF50", "intermediate"));
        
        // Tay
        exercises.add(new Exercise("Bicep Curl", "Tay", "3×10-15", "60s", 
                "#FFA726", "beginner"));
        
        // Bụng
        exercises.add(new Exercise("Plank", "Bụng", "3×30-60s", "45s", 
                "#AB47BC", "beginner"));
        
        return exercises;
    }
}
