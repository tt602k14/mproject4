package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.models.CustomWorkout;
import com.htdgym.app.utils.ExerciseDataManager;

import java.util.ArrayList;
import java.util.List;

public class CustomWorkoutBuilderActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etWorkoutName, etWorkoutDescription;
    private TextView tvSelectedCount, tvEstimatedTime;
    private RecyclerView recyclerAvailableExercises, recyclerSelectedExercises;
    private Button btnSaveWorkout, btnPreviewWorkout;
    
    private List<Exercise> availableExercises;
    private List<Exercise> selectedExercises;
    private GymDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_workout_builder);

        database = GymDatabase.getInstance(this);
        initViews();
        setupRecyclerViews();
        setupClickListeners();
        loadAvailableExercises();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etWorkoutName = findViewById(R.id.et_workout_name);
        etWorkoutDescription = findViewById(R.id.et_workout_description);
        tvSelectedCount = findViewById(R.id.tv_selected_count);
        tvEstimatedTime = findViewById(R.id.tv_estimated_time);
        recyclerAvailableExercises = findViewById(R.id.recycler_available_exercises);
        recyclerSelectedExercises = findViewById(R.id.recycler_selected_exercises);
        btnSaveWorkout = findViewById(R.id.btn_save_workout);
        btnPreviewWorkout = findViewById(R.id.btn_preview_workout);
        
        selectedExercises = new ArrayList<>();
    }

    private void setupRecyclerViews() {
        // Available exercises
        recyclerAvailableExercises.setLayoutManager(new LinearLayoutManager(this));
        
        // Selected exercises
        recyclerSelectedExercises.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnSaveWorkout.setOnClickListener(v -> saveCustomWorkout());
        
        btnPreviewWorkout.setOnClickListener(v -> previewWorkout());
    }

    private void loadAvailableExercises() {
        availableExercises = ExerciseDataManager.getAllExercises();
        Toast.makeText(this, "Đã tải " + availableExercises.size() + " bài tập", Toast.LENGTH_SHORT).show();
    }

    private void addExercise(Exercise exercise) {
        if (!selectedExercises.contains(exercise)) {
            selectedExercises.add(exercise);
            updateWorkoutStats();
            Toast.makeText(this, "Đã thêm: " + exercise.getName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Bài tập đã được thêm", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeExercise(Exercise exercise) {
        int position = selectedExercises.indexOf(exercise);
        if (position != -1) {
            selectedExercises.remove(position);
            updateWorkoutStats();
            Toast.makeText(this, "Đã xóa: " + exercise.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateWorkoutStats() {
        int count = selectedExercises.size();
        tvSelectedCount.setText(count + " bài tập");
        
        // Calculate estimated time (average 3 minutes per exercise)
        int estimatedMinutes = count * 3;
        tvEstimatedTime.setText(estimatedMinutes + " phút");
    }

    private void saveCustomWorkout() {
        String name = etWorkoutName.getText().toString().trim();
        String description = etWorkoutDescription.getText().toString().trim();
        
        if (TextUtils.isEmpty(name)) {
            etWorkoutName.setError("Vui lòng nhập tên workout");
            return;
        }
        
        if (selectedExercises.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 bài tập", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Get current user ID
        int userId = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE).getInt("user_id", 1);
        
        // Create custom workout
        CustomWorkout customWorkout = new CustomWorkout();
        customWorkout.setName(name);
        customWorkout.setDescription(description);
        customWorkout.setDuration(selectedExercises.size() * 3); // 3 minutes per exercise
        customWorkout.setDifficulty(calculateDifficulty());
        customWorkout.setCategory("Custom");
        customWorkout.setUserId(userId);
        customWorkout.setCreatedAt(System.currentTimeMillis());
        
        // Save to database
        new Thread(() -> {
            try {
                long workoutId = database.customWorkoutDao().insert(customWorkout);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "✅ Đã lưu workout: " + name, Toast.LENGTH_LONG).show();
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "❌ Lỗi khi lưu workout", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private String calculateDifficulty() {
        int easyCount = 0, mediumCount = 0, hardCount = 0;
        
        for (Exercise exercise : selectedExercises) {
            String difficulty = exercise.getDifficulty();
            if (difficulty != null) {
                switch (difficulty.toLowerCase()) {
                    case "dễ":
                    case "easy":
                        easyCount++;
                        break;
                    case "trung bình":
                    case "medium":
                        mediumCount++;
                        break;
                    case "khó":
                    case "hard":
                        hardCount++;
                        break;
                }
            }
        }
        
        if (hardCount > selectedExercises.size() / 2) return "Khó";
        if (mediumCount > selectedExercises.size() / 2) return "Trung bình";
        return "Dễ";
    }

    private void previewWorkout() {
        if (selectedExercises.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 bài tập", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Toast.makeText(this, "Preview: " + selectedExercises.size() + " bài tập, " + 
                      (selectedExercises.size() * 3) + " phút", Toast.LENGTH_LONG).show();
    }
}