package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.ExerciseAdapter;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.Exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdvancedSearchActivity extends AppCompatActivity implements ExerciseAdapter.OnExerciseClickListener {

    private SeekBar seekBarDuration, seekBarCalories;
    private TextView tvDurationValue, tvCaloriesValue;
    private CheckBox cbFavorites, cbCompleted, cbHasVideo;
    private CheckBox cbChest, cbAbs, cbLegs, cbBack, cbShoulder;
    private CheckBox cbBeginner, cbIntermediate, cbAdvanced;
    private CheckBox cbNoEquipment, cbDumbbell, cbResistance;
    private Button btnSearch, btnReset;
    private RecyclerView recyclerViewResults;
    
    private GymDatabase database;
    private ExecutorService executor;
    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> allExercises = new ArrayList<>();
    private List<Exercise> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        
        database = GymDatabase.getInstance(this);
        executor = Executors.newSingleThreadExecutor();
        
        initViews();
        setupListeners();
        setupRecyclerView();
        loadExercises();
    }

    private void initViews() {
        seekBarDuration = findViewById(R.id.seekbar_duration);
        seekBarCalories = findViewById(R.id.seekbar_calories);
        tvDurationValue = findViewById(R.id.tv_duration_value);
        tvCaloriesValue = findViewById(R.id.tv_calories_value);
        
        cbFavorites = findViewById(R.id.cb_favorites);
        cbCompleted = findViewById(R.id.cb_completed);
        cbHasVideo = findViewById(R.id.cb_has_video);
        
        cbChest = findViewById(R.id.cb_chest);
        cbAbs = findViewById(R.id.cb_abs);
        cbLegs = findViewById(R.id.cb_legs);
        cbBack = findViewById(R.id.cb_back);
        cbShoulder = findViewById(R.id.cb_shoulder);
        
        cbBeginner = findViewById(R.id.cb_beginner);
        cbIntermediate = findViewById(R.id.cb_intermediate);
        cbAdvanced = findViewById(R.id.cb_advanced);
        
        cbNoEquipment = findViewById(R.id.cb_no_equipment);
        cbDumbbell = findViewById(R.id.cb_dumbbell);
        cbResistance = findViewById(R.id.cb_resistance);
        
        btnSearch = findViewById(R.id.btn_search);
        btnReset = findViewById(R.id.btn_reset);
        recyclerViewResults = findViewById(R.id.recycler_view_results);
    }
    private void setupListeners() {
        seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDurationValue.setText(progress + " phút");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        seekBarCalories.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvCaloriesValue.setText(progress + " cal");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        btnSearch.setOnClickListener(v -> performAdvancedSearch());
        btnReset.setOnClickListener(v -> resetFilters());
        
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
    
    private void setupRecyclerView() {
        exerciseAdapter = new ExerciseAdapter(this, searchResults);
        exerciseAdapter.setOnExerciseClickListener(this);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewResults.setAdapter(exerciseAdapter);
    }
    
    private void loadExercises() {
        executor.execute(() -> {
            List<Exercise> exercises = database.exerciseDao().getAllExercises();
            runOnUiThread(() -> {
                allExercises.clear();
                allExercises.addAll(exercises);
                searchResults.clear();
                searchResults.addAll(exercises);
                exerciseAdapter.notifyDataSetChanged();
            });
        });
    }
    
    private void performAdvancedSearch() {
        List<Exercise> filtered = new ArrayList<>();
        
        int maxDuration = seekBarDuration.getProgress();
        int minCalories = seekBarCalories.getProgress();
        
        for (Exercise exercise : allExercises) {
            boolean matches = true;
            
            // Duration filter
            if (maxDuration > 0 && exercise.getDuration() > maxDuration) {
                matches = false;
            }
            
            // Calories filter
            if (minCalories > 0 && exercise.getCalories() < minCalories) {
                matches = false;
            }
            
            // Status filters
            if (cbFavorites.isChecked() && !exercise.isFavorite()) {
                matches = false;
            }
            
            if (cbCompleted.isChecked() && exercise.getCompletedCount() == 0) {
                matches = false;
            }
            
            if (cbHasVideo.isChecked() && (exercise.getVideoUrl() == null || exercise.getVideoUrl().isEmpty())) {
                matches = false;
            }
            
            // Category filters
            if (isCategoryFilterActive()) {
                boolean categoryMatch = false;
                if (cbChest.isChecked() && "chest".equals(exercise.getCategory())) categoryMatch = true;
                if (cbAbs.isChecked() && "abs".equals(exercise.getCategory())) categoryMatch = true;
                if (cbLegs.isChecked() && "legs".equals(exercise.getCategory())) categoryMatch = true;
                if (cbBack.isChecked() && "back".equals(exercise.getCategory())) categoryMatch = true;
                if (cbShoulder.isChecked() && "shoulder".equals(exercise.getCategory())) categoryMatch = true;
                
                if (!categoryMatch) matches = false;
            }
            
            // Difficulty filters
            if (isDifficultyFilterActive()) {
                boolean difficultyMatch = false;
                if (cbBeginner.isChecked() && "beginner".equals(exercise.getDifficulty())) difficultyMatch = true;
                if (cbIntermediate.isChecked() && "intermediate".equals(exercise.getDifficulty())) difficultyMatch = true;
                if (cbAdvanced.isChecked() && "advanced".equals(exercise.getDifficulty())) difficultyMatch = true;
                
                if (!difficultyMatch) matches = false;
            }
            
            // Equipment filters
            if (isEquipmentFilterActive()) {
                boolean equipmentMatch = false;
                String equipment = exercise.getEquipment() != null ? exercise.getEquipment() : "none";
                if (cbNoEquipment.isChecked() && "none".equals(equipment)) equipmentMatch = true;
                if (cbDumbbell.isChecked() && "dumbbell".equals(equipment)) equipmentMatch = true;
                if (cbResistance.isChecked() && "resistance".equals(equipment)) equipmentMatch = true;
                
                if (!equipmentMatch) matches = false;
            }
            
            if (matches) {
                filtered.add(exercise);
            }
        }
        
        searchResults.clear();
        searchResults.addAll(filtered);
        exerciseAdapter.notifyDataSetChanged();
        
        // Show results count
        TextView tvResultsCount = findViewById(R.id.tv_results_count);
        tvResultsCount.setText("Tìm thấy " + filtered.size() + " bài tập");
    }
    private boolean isCategoryFilterActive() {
        return cbChest.isChecked() || cbAbs.isChecked() || cbLegs.isChecked() || 
               cbBack.isChecked() || cbShoulder.isChecked();
    }
    
    private boolean isDifficultyFilterActive() {
        return cbBeginner.isChecked() || cbIntermediate.isChecked() || cbAdvanced.isChecked();
    }
    
    private boolean isEquipmentFilterActive() {
        return cbNoEquipment.isChecked() || cbDumbbell.isChecked() || cbResistance.isChecked();
    }
    
    private void resetFilters() {
        seekBarDuration.setProgress(0);
        seekBarCalories.setProgress(0);
        
        cbFavorites.setChecked(false);
        cbCompleted.setChecked(false);
        cbHasVideo.setChecked(false);
        
        cbChest.setChecked(false);
        cbAbs.setChecked(false);
        cbLegs.setChecked(false);
        cbBack.setChecked(false);
        cbShoulder.setChecked(false);
        
        cbBeginner.setChecked(false);
        cbIntermediate.setChecked(false);
        cbAdvanced.setChecked(false);
        
        cbNoEquipment.setChecked(false);
        cbDumbbell.setChecked(false);
        cbResistance.setChecked(false);
        
        // Reset results to show all exercises
        searchResults.clear();
        searchResults.addAll(allExercises);
        exerciseAdapter.notifyDataSetChanged();
        
        TextView tvResultsCount = findViewById(R.id.tv_results_count);
        tvResultsCount.setText("Hiển thị tất cả " + allExercises.size() + " bài tập");
    }

    @Override
    public void onFavoriteClick(Exercise exercise) {
        executor.execute(() -> {
            boolean newFavoriteStatus = !exercise.isFavorite();
            database.exerciseDao().updateFavoriteStatus(exercise.getId(), newFavoriteStatus);
            exercise.setFavorite(newFavoriteStatus);
            
            runOnUiThread(() -> {
                exerciseAdapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    public void onExerciseClick(Exercise exercise) {
        // Return selected exercise to WorkoutsFragment
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selected_exercise_id", exercise.getId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}