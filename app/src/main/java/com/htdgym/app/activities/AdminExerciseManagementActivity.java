package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htdgym.app.R;
import com.htdgym.app.adapters.AdminExerciseAdapter;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.utils.AdminManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminExerciseManagementActivity extends AppCompatActivity {

    private static final String TAG = "AdminExerciseManagement";
    
    // UI Components
    private ImageView btnBack;
    private TextView tvTitle, tvTotalExercises, tvFilterStatus;
    private EditText etSearch;
    private RecyclerView recyclerViewExercises;
    private FloatingActionButton fabAddExercise;
    private CardView cardStats;
    private LinearLayout layoutFilters;
    
    // Data
    private AdminExerciseAdapter adapter;
    private List<Exercise> exerciseList;
    private List<Exercise> filteredList;
    private GymDatabase database;
    private ExecutorService executorService;
    
    // Filter states
    private String currentMuscleFilter = "Tất cả";
    private String currentDifficultyFilter = "Tất cả";
    private String currentSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            if (!validateAdminAccess()) {
                return;
            }
            
            setContentView(R.layout.activity_admin_exercise_management);
            
            initializeComponents();
            setupRecyclerView();
            setupClickListeners();
            loadExercises();
            
            Log.d(TAG, "AdminExerciseManagement initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing AdminExerciseManagement", e);
            handleError("Lỗi khởi tạo quản lý bài tập", e);
        }
    }

    private boolean validateAdminAccess() {
        if (!AdminManager.isAdminLoggedIn(this)) {
            Log.w(TAG, "Admin not logged in, redirecting to login");
            finish();
            return false;
        }
        return true;
    }

    private void initializeComponents() {
        executorService = Executors.newSingleThreadExecutor();
        database = GymDatabase.getInstance(this);
        
        exerciseList = new ArrayList<>();
        filteredList = new ArrayList<>();
        
        // Initialize UI components
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTotalExercises = findViewById(R.id.tv_total_exercises);
        tvFilterStatus = findViewById(R.id.tv_filter_status);
        etSearch = findViewById(R.id.et_search);
        recyclerViewExercises = findViewById(R.id.recycler_exercises);
        fabAddExercise = findViewById(R.id.fab_add_exercise);
        cardStats = findViewById(R.id.card_stats);
        layoutFilters = findViewById(R.id.layout_filters);
        
        tvTitle.setText("Quản lý bài tập");
        
        Log.d(TAG, "Components initialized successfully");
    }

    private void setupRecyclerView() {
        adapter = new AdminExerciseAdapter(filteredList, new AdminExerciseAdapter.OnExerciseActionListener() {
            @Override
            public void onEditExercise(Exercise exercise) {
                showEditExerciseDialog(exercise);
            }

            @Override
            public void onDeleteExercise(Exercise exercise) {
                showDeleteConfirmDialog(exercise);
            }

            @Override
            public void onViewExercise(Exercise exercise) {
                showExerciseDetailDialog(exercise);
            }
        });
        
        recyclerViewExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExercises.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        fabAddExercise.setOnClickListener(v -> showAddExerciseDialog());
        
        cardStats.setOnClickListener(v -> showStatsDialog());
        
        layoutFilters.setOnClickListener(v -> showFilterDialog());
        
        // Search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().trim();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        Log.d(TAG, "Click listeners setup completed");
    }

    private void loadExercises() {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Loading exercises from database");
                
                List<Exercise> exercises = database.exerciseDao().getAllExercisesSync();
                
                runOnUiThread(() -> {
                    exerciseList.clear();
                    exerciseList.addAll(exercises);
                    applyFilters();
                    updateStats();
                    
                    Log.d(TAG, "Loaded " + exercises.size() + " exercises");
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Error loading exercises", e);
                runOnUiThread(() -> handleError("Lỗi tải danh sách bài tập", e));
            }
        });
    }

    private void applyFilters() {
        filteredList.clear();
        
        for (Exercise exercise : exerciseList) {
            boolean matchesSearch = currentSearchQuery.isEmpty() || 
                exercise.getName().toLowerCase().contains(currentSearchQuery.toLowerCase()) ||
                exercise.getMuscleGroup().toLowerCase().contains(currentSearchQuery.toLowerCase());
            
            boolean matchesMuscle = currentMuscleFilter.equals("Tất cả") || 
                exercise.getMuscleGroup().equals(currentMuscleFilter);
            
            boolean matchesDifficulty = currentDifficultyFilter.equals("Tất cả") || 
                exercise.getDifficulty().equals(currentDifficultyFilter);
            
            if (matchesSearch && matchesMuscle && matchesDifficulty) {
                filteredList.add(exercise);
            }
        }
        
        adapter.notifyDataSetChanged();
        updateFilterStatus();
    }

    private void updateFilterStatus() {
        String status = "Hiển thị " + filteredList.size() + "/" + exerciseList.size() + " bài tập";
        if (!currentMuscleFilter.equals("Tất cả") || !currentDifficultyFilter.equals("Tất cả")) {
            status += " (Đã lọc)";
        }
        tvFilterStatus.setText(status);
    }

    private void updateStats() {
        tvTotalExercises.setText(String.valueOf(exerciseList.size()));
    }

    private void showAddExerciseDialog() {
        showExerciseFormDialog(null, "Thêm bài tập mới");
    }

    private void showEditExerciseDialog(Exercise exercise) {
        showExerciseFormDialog(exercise, "Chỉnh sửa bài tập");
    }

    private void showExerciseFormDialog(Exercise exercise, String title) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exercise_form, null);
        
        EditText etName = dialogView.findViewById(R.id.et_exercise_name);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etInstructions = dialogView.findViewById(R.id.et_instructions);
        EditText etVideoUrl = dialogView.findViewById(R.id.et_video_url);
        EditText etSets = dialogView.findViewById(R.id.et_sets);
        EditText etReps = dialogView.findViewById(R.id.et_reps);
        EditText etDuration = dialogView.findViewById(R.id.et_duration);
        EditText etCalories = dialogView.findViewById(R.id.et_calories);
        EditText etEquipment = dialogView.findViewById(R.id.et_equipment);
        Spinner spinnerMuscleGroup = dialogView.findViewById(R.id.spinner_muscle_group);
        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinner_difficulty);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        
        // Setup spinners
        setupSpinners(spinnerMuscleGroup, spinnerDifficulty, spinnerCategory);
        
        // Fill data if editing
        if (exercise != null) {
            fillExerciseForm(exercise, etName, etDescription, etInstructions, etVideoUrl,
                etSets, etReps, etDuration, etCalories, etEquipment,
                spinnerMuscleGroup, spinnerDifficulty, spinnerCategory);
        }
        
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("Lưu", (dialog, which) -> {
                if (validateExerciseForm(etName, etDescription)) {
                    saveExercise(exercise, dialogView);
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void setupSpinners(Spinner spinnerMuscleGroup, Spinner spinnerDifficulty, Spinner spinnerCategory) {
        String[] muscleGroups = {"Ngực", "Lưng", "Vai", "Tay", "Chân", "Bụng", "Mông", "Toàn thân", "Cardio"};
        String[] difficulties = {"Dễ", "Trung bình", "Khó", "Rất khó"};
        String[] categories = {"Strength", "Cardio", "HIIT", "Yoga", "Stretching", "Functional"};
        
        ArrayAdapter<String> muscleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, muscleGroups);
        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMuscleGroup.setAdapter(muscleAdapter);
        
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
        
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
    }

    private void fillExerciseForm(Exercise exercise, EditText etName, EditText etDescription,
                                EditText etInstructions, EditText etVideoUrl, EditText etSets,
                                EditText etReps, EditText etDuration, EditText etCalories,
                                EditText etEquipment, Spinner spinnerMuscleGroup,
                                Spinner spinnerDifficulty, Spinner spinnerCategory) {
        
        etName.setText(exercise.getName());
        etDescription.setText(exercise.getDescription());
        etInstructions.setText(""); // Add instructions field to Exercise model if needed
        etVideoUrl.setText(exercise.getVideoUrl());
        etSets.setText(String.valueOf(exercise.getSets()));
        etReps.setText(String.valueOf(exercise.getReps()));
        etDuration.setText(String.valueOf(exercise.getDuration()));
        etCalories.setText(String.valueOf(exercise.getCalories()));
        etEquipment.setText(exercise.getEquipment());
        
        // Set spinner selections
        setSpinnerSelection(spinnerMuscleGroup, exercise.getMuscleGroup());
        setSpinnerSelection(spinnerDifficulty, exercise.getDifficulty());
        setSpinnerSelection(spinnerCategory, exercise.getCategory());
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        if (value != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
            int position = adapter.getPosition(value);
            if (position >= 0) {
                spinner.setSelection(position);
            }
        }
    }

    private boolean validateExerciseForm(EditText etName, EditText etDescription) {
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Tên bài tập không được để trống");
            etName.requestFocus();
            return false;
        }
        
        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Mô tả không được để trống");
            etDescription.requestFocus();
            return false;
        }
        
        return true;
    }

    private void saveExercise(Exercise existingExercise, View dialogView) {
        executorService.execute(() -> {
            try {
                Exercise exercise = existingExercise != null ? existingExercise : new Exercise();
                
                // Extract data from form
                extractExerciseData(exercise, dialogView);
                
                if (existingExercise != null) {
                    database.exerciseDao().updateExercise(exercise);
                    Log.d(TAG, "Updated exercise: " + exercise.getName());
                } else {
                    long id = database.exerciseDao().insertExercise(exercise);
                    exercise.setId((int) id);
                    Log.d(TAG, "Added new exercise: " + exercise.getName());
                }
                
                runOnUiThread(() -> {
                    Toast.makeText(this, 
                        existingExercise != null ? "Cập nhật bài tập thành công" : "Thêm bài tập thành công", 
                        Toast.LENGTH_SHORT).show();
                    loadExercises();
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Error saving exercise", e);
                runOnUiThread(() -> handleError("Lỗi lưu bài tập", e));
            }
        });
    }

    private void extractExerciseData(Exercise exercise, View dialogView) {
        EditText etName = dialogView.findViewById(R.id.et_exercise_name);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etVideoUrl = dialogView.findViewById(R.id.et_video_url);
        EditText etSets = dialogView.findViewById(R.id.et_sets);
        EditText etReps = dialogView.findViewById(R.id.et_reps);
        EditText etDuration = dialogView.findViewById(R.id.et_duration);
        EditText etCalories = dialogView.findViewById(R.id.et_calories);
        EditText etEquipment = dialogView.findViewById(R.id.et_equipment);
        Spinner spinnerMuscleGroup = dialogView.findViewById(R.id.spinner_muscle_group);
        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinner_difficulty);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        
        exercise.setName(etName.getText().toString().trim());
        exercise.setDescription(etDescription.getText().toString().trim());
        exercise.setVideoUrl(etVideoUrl.getText().toString().trim());
        exercise.setSets(parseIntSafely(etSets.getText().toString(), 0));
        exercise.setReps(parseIntSafely(etReps.getText().toString(), 0));
        exercise.setDuration(parseIntSafely(etDuration.getText().toString(), 0));
        exercise.setCalories(parseIntSafely(etCalories.getText().toString(), 0));
        exercise.setEquipment(etEquipment.getText().toString().trim());
        exercise.setMuscleGroup(spinnerMuscleGroup.getSelectedItem().toString());
        exercise.setDifficulty(spinnerDifficulty.getSelectedItem().toString());
        exercise.setCategory(spinnerCategory.getSelectedItem().toString());
        
        // Generate thumbnail URL from video URL if provided
        if (!exercise.getVideoUrl().isEmpty()) {
            String thumbnailUrl = generateThumbnailUrl(exercise.getVideoUrl());
            exercise.setThumbnailUrl(thumbnailUrl);
        }
    }

    private int parseIntSafely(String value, int defaultValue) {
        try {
            return value.isEmpty() ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String generateThumbnailUrl(String videoUrl) {
        // Extract YouTube video ID and generate thumbnail URL
        String videoId = extractVideoId(videoUrl);
        if (videoId != null) {
            return "https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
        }
        return "";
    }

    private String extractVideoId(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) return null;
        
        String videoId = null;
        if (youtubeUrl.contains("youtube.com/watch?v=")) {
            videoId = youtubeUrl.split("v=")[1];
            int ampersandPosition = videoId.indexOf('&');
            if (ampersandPosition != -1) {
                videoId = videoId.substring(0, ampersandPosition);
            }
        } else if (youtubeUrl.contains("youtu.be/")) {
            videoId = youtubeUrl.split("youtu.be/")[1];
            int questionMarkPosition = videoId.indexOf('?');
            if (questionMarkPosition != -1) {
                videoId = videoId.substring(0, questionMarkPosition);
            }
        }
        return videoId;
    }

    private void showDeleteConfirmDialog(Exercise exercise) {
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa bài tập \"" + exercise.getName() + "\"?")
            .setPositiveButton("Xóa", (dialog, which) -> deleteExercise(exercise))
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteExercise(Exercise exercise) {
        executorService.execute(() -> {
            try {
                database.exerciseDao().deleteExercise(exercise);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đã xóa bài tập: " + exercise.getName(), Toast.LENGTH_SHORT).show();
                    loadExercises();
                });
                
                Log.d(TAG, "Deleted exercise: " + exercise.getName());
                
            } catch (Exception e) {
                Log.e(TAG, "Error deleting exercise", e);
                runOnUiThread(() -> handleError("Lỗi xóa bài tập", e));
            }
        });
    }

    private void showExerciseDetailDialog(Exercise exercise) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exercise_detail, null);
        
        TextView tvName = dialogView.findViewById(R.id.tv_exercise_name);
        TextView tvDescription = dialogView.findViewById(R.id.tv_description);
        TextView tvMuscleGroup = dialogView.findViewById(R.id.tv_muscle_group);
        TextView tvDifficulty = dialogView.findViewById(R.id.tv_difficulty);
        TextView tvSetsReps = dialogView.findViewById(R.id.tv_sets_reps);
        TextView tvDuration = dialogView.findViewById(R.id.tv_duration);
        TextView tvCalories = dialogView.findViewById(R.id.tv_calories);
        TextView tvEquipment = dialogView.findViewById(R.id.tv_equipment);
        TextView tvVideoUrl = dialogView.findViewById(R.id.tv_video_url);
        
        tvName.setText(exercise.getName());
        tvDescription.setText(exercise.getDescription());
        tvMuscleGroup.setText(exercise.getMuscleGroup());
        tvDifficulty.setText(exercise.getDifficulty());
        tvSetsReps.setText(exercise.getSets() + " sets × " + exercise.getReps() + " reps");
        tvDuration.setText(exercise.getDuration() + " phút");
        tvCalories.setText(exercise.getCalories() + " calo");
        tvEquipment.setText(exercise.getEquipment().isEmpty() ? "Không cần dụng cụ" : exercise.getEquipment());
        tvVideoUrl.setText(exercise.getVideoUrl().isEmpty() ? "Chưa có video" : exercise.getVideoUrl());
        
        new AlertDialog.Builder(this)
            .setTitle("Chi tiết bài tập")
            .setView(dialogView)
            .setPositiveButton("Đóng", null)
            .show();
    }

    private void showFilterDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exercise_filter, null);
        
        Spinner spinnerMuscle = dialogView.findViewById(R.id.spinner_muscle_filter);
        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinner_difficulty_filter);
        
        String[] muscleOptions = {"Tất cả", "Ngực", "Lưng", "Vai", "Tay", "Chân", "Bụng", "Mông", "Toàn thân", "Cardio"};
        String[] difficultyOptions = {"Tất cả", "Dễ", "Trung bình", "Khó", "Rất khó"};
        
        ArrayAdapter<String> muscleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, muscleOptions);
        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMuscle.setAdapter(muscleAdapter);
        
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficultyOptions);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
        
        // Set current selections
        setSpinnerSelection(spinnerMuscle, currentMuscleFilter);
        setSpinnerSelection(spinnerDifficulty, currentDifficultyFilter);
        
        new AlertDialog.Builder(this)
            .setTitle("Lọc bài tập")
            .setView(dialogView)
            .setPositiveButton("Áp dụng", (dialog, which) -> {
                currentMuscleFilter = spinnerMuscle.getSelectedItem().toString();
                currentDifficultyFilter = spinnerDifficulty.getSelectedItem().toString();
                applyFilters();
            })
            .setNegativeButton("Đặt lại", (dialog, which) -> {
                currentMuscleFilter = "Tất cả";
                currentDifficultyFilter = "Tất cả";
                applyFilters();
            })
            .setNeutralButton("Hủy", null)
            .show();
    }

    private void showStatsDialog() {
        executorService.execute(() -> {
            try {
                // Calculate statistics
                int totalExercises = exerciseList.size();
                int easyCount = 0, mediumCount = 0, hardCount = 0, veryHardCount = 0;
                int[] muscleGroupCounts = new int[9]; // For each muscle group
                
                for (Exercise exercise : exerciseList) {
                    switch (exercise.getDifficulty()) {
                        case "Dễ": easyCount++; break;
                        case "Trung bình": mediumCount++; break;
                        case "Khó": hardCount++; break;
                        case "Rất khó": veryHardCount++; break;
                    }
                }
                
                // Make variables final for lambda
                final int finalEasyCount = easyCount;
                final int finalMediumCount = mediumCount;
                final int finalHardCount = hardCount;
                final int finalVeryHardCount = veryHardCount;
                
                runOnUiThread(() -> {
                    String stats = "📊 Thống kê bài tập\n\n" +
                        "Tổng số bài tập: " + totalExercises + "\n\n" +
                        "Theo độ khó:\n" +
                        "• Dễ: " + finalEasyCount + "\n" +
                        "• Trung bình: " + finalMediumCount + "\n" +
                        "• Khó: " + finalHardCount + "\n" +
                        "• Rất khó: " + finalVeryHardCount;
                    
                    new AlertDialog.Builder(this)
                        .setTitle("Thống kê")
                        .setMessage(stats)
                        .setPositiveButton("Đóng", null)
                        .show();
                });
                
            } catch (Exception e) {
                Log.e(TAG, "Error calculating stats", e);
                runOnUiThread(() -> handleError("Lỗi tính toán thống kê", e));
            }
        });
    }

    private void handleError(String message, Exception e) {
        String fullMessage = message + ": " + e.getMessage();
        Toast.makeText(this, fullMessage, Toast.LENGTH_LONG).show();
        Log.e(TAG, fullMessage, e);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        
        Log.d(TAG, "AdminExerciseManagement destroyed and resources cleaned up");
    }
}