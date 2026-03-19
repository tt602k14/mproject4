package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.ExerciseCardAdapter;
import com.htdgym.app.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class GeneratedWorkoutActivity extends AppCompatActivity {

    private ImageView btnBack, btnRegenerate;
    private TextView tvWorkoutName, tvWorkoutInfo, tvAITips;
    private RecyclerView recyclerExercises;
    private Button btnStartWorkout, btnSaveWorkout;
    private ExerciseCardAdapter adapter;
    
    private List<Exercise> generatedExercises;
    private String workoutName;
    private String fitnessLevel;
    private String goal;
    private String timeAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_workout);

        initViews();
        loadWorkoutData();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnRegenerate = findViewById(R.id.btn_regenerate);
        tvWorkoutName = findViewById(R.id.tv_workout_name);
        tvWorkoutInfo = findViewById(R.id.tv_workout_info);
        tvAITips = findViewById(R.id.tv_ai_tips);
        recyclerExercises = findViewById(R.id.recycler_exercises);
        btnStartWorkout = findViewById(R.id.btn_start_workout);
        btnSaveWorkout = findViewById(R.id.btn_save_workout);
    }

    private void loadWorkoutData() {
        // Get data from intent
        workoutName = getIntent().getStringExtra("workout_name");
        fitnessLevel = getIntent().getStringExtra("fitness_level");
        goal = getIntent().getStringExtra("goal");
        timeAvailable = getIntent().getStringExtra("time_available");
        
        ArrayList<String> exerciseNames = getIntent().getStringArrayListExtra("exercise_names");
        ArrayList<String> muscleGroups = getIntent().getStringArrayListExtra("muscle_groups");
        ArrayList<String> setsReps = getIntent().getStringArrayListExtra("sets_reps");
        ArrayList<String> restTimes = getIntent().getStringArrayListExtra("rest_times");
        ArrayList<String> difficulties = getIntent().getStringArrayListExtra("difficulties");
        ArrayList<String> videoUrls = getIntent().getStringArrayListExtra("video_urls");
        
        // Reconstruct exercises
        generatedExercises = new ArrayList<>();
        if (exerciseNames != null && !exerciseNames.isEmpty()) {
            for (int i = 0; i < exerciseNames.size(); i++) {
                Exercise exercise = new Exercise(
                    exerciseNames.get(i),
                    muscleGroups.get(i),
                    setsReps.get(i),
                    restTimes.get(i),
                    "#4CAF50", // Default color
                    difficulties.get(i),
                    videoUrls.get(i)
                );
                generatedExercises.add(exercise);
            }
        }
        
        // Update UI
        tvWorkoutName.setText(workoutName != null ? workoutName : "AI Generated Workout");
        
        String workoutInfo = String.format("📊 %s • 🎯 %s • ⏰ %s phút • 💪 %d bài tập", 
            getLevelText(fitnessLevel), 
            getGoalText(goal), 
            getTimeText(timeAvailable),
            generatedExercises.size());
        tvWorkoutInfo.setText(workoutInfo);
        
        // AI Tips
        String aiTips = generateAITips();
        tvAITips.setText(aiTips);
    }

    private void setupRecyclerView() {
        adapter = new ExerciseCardAdapter(generatedExercises, new ExerciseCardAdapter.OnExerciseClickListener() {
            @Override
            public void onExerciseClick(Exercise exercise) {
                try {
                    // Open exercise detail
                    Intent intent = new Intent(GeneratedWorkoutActivity.this, ExerciseDetailActivity.class);
                    intent.putExtra("exercise_name", exercise.getName());
                    intent.putExtra("muscle_group", exercise.getMuscleGroup());
                    intent.putExtra("sets_reps", exercise.getFormattedSetsReps());
                    intent.putExtra("rest_time", exercise.getRestTime());
                    intent.putExtra("difficulty", exercise.getDifficulty());
                    intent.putExtra("video_url", exercise.getVideoUrl());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(GeneratedWorkoutActivity.this, 
                        "Lỗi mở chi tiết bài tập: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFavoriteClick(Exercise exercise) {
                exercise.setFavorite(!exercise.isFavorite());
                adapter.notifyDataSetChanged();
                String message = exercise.isFavorite() ? "Đã thêm vào yêu thích ❤️" : "Đã xóa khỏi yêu thích";
                Toast.makeText(GeneratedWorkoutActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerExercises.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnRegenerate.setOnClickListener(v -> {
            Toast.makeText(this, "🔄 Đang tạo workout mới...", Toast.LENGTH_SHORT).show();
            finish(); // Go back to planner to regenerate
        });
        
        btnStartWorkout.setOnClickListener(v -> {
            if (generatedExercises.isEmpty()) {
                Toast.makeText(this, "Không có bài tập để bắt đầu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Truyền toàn bộ danh sách AI tạo sang ProgramExerciseSessionActivity
            String[] names  = new String[generatedExercises.size()];
            String[] sets   = new String[generatedExercises.size()];
            String[] videos = new String[generatedExercises.size()];
            String[] rests  = new String[generatedExercises.size()];
            for (int i = 0; i < generatedExercises.size(); i++) {
                Exercise ex = generatedExercises.get(i);
                names[i]  = ex.getName();
                sets[i]   = ex.getFormattedSetsReps();
                videos[i] = ex.getVideoUrl() != null ? ex.getVideoUrl() : "";
                rests[i]  = ex.getRestTime() != null ? ex.getRestTime() : "60s";
            }
            Intent intent = new Intent(this, ProgramExerciseSessionActivity.class);
            intent.putExtra("exercise_names",  names);
            intent.putExtra("exercise_sets",   sets);
            intent.putExtra("exercise_videos", videos);
            intent.putExtra("exercise_rests",  rests);
            intent.putExtra("day_title", workoutName != null ? workoutName : "AI Workout");
            startActivity(intent);
        });
        
        btnSaveWorkout.setOnClickListener(v -> {
            // TODO: Implement save workout to favorites/custom workouts
            Toast.makeText(this, "💾 Đã lưu workout vào thư viện cá nhân!", Toast.LENGTH_SHORT).show();
        });
    }

    private String getLevelText(String level) {
        if (level == null) return "Không xác định";
        switch (level) {
            case "BEGINNER": return "Người mới";
            case "INTERMEDIATE": return "Trung cấp";
            case "ADVANCED": return "Nâng cao";
            default: return level;
        }
    }

    private String getGoalText(String goal) {
        if (goal == null) return "Không xác định";
        switch (goal) {
            case "FAT_LOSS": return "Giảm mỡ";
            case "MUSCLE_BUILDING": return "Tăng cơ";
            case "STRENGTH": return "Sức mạnh";
            case "ENDURANCE": return "Sức bền";
            case "GENERAL_FITNESS": return "Tổng quát";
            default: return goal;
        }
    }

    private String getTimeText(String time) {
        if (time == null) return "30";
        switch (time) {
            case "SHORT": return "15";
            case "MEDIUM": return "30";
            case "LONG": return "45";
            case "EXTENDED": return "60";
            default: return "30";
        }
    }

    private String generateAITips() {
        StringBuilder tips = new StringBuilder();
        tips.append("🤖 AI Coach Tips:\n\n");
        
        if ("FAT_LOSS".equals(goal)) {
            tips.append("• Giữ nhịp tim cao trong suốt workout\n");
            tips.append("• Nghỉ ngắn giữa các set (30-45s)\n");
            tips.append("• Uống nước đều đặn\n");
            tips.append("• Kết hợp với chế độ ăn deficit calories");
        } else if ("MUSCLE_BUILDING".equals(goal)) {
            tips.append("• Tập trung vào form chuẩn\n");
            tips.append("• Nghỉ đủ giữa các set (60-90s)\n");
            tips.append("• Progressive overload từng tuần\n");
            tips.append("• Ăn đủ protein sau tập");
        } else if ("STRENGTH".equals(goal)) {
            tips.append("• Ít reps, nhiều sets\n");
            tips.append("• Nghỉ dài giữa các set (90-120s)\n");
            tips.append("• Khởi động kỹ trước khi tập\n");
            tips.append("• Tăng cường độ từ từ");
        } else {
            tips.append("• Khởi động 5-10 phút trước khi tập\n");
            tips.append("• Thực hiện đúng kỹ thuật\n");
            tips.append("• Nghỉ ngơi hợp lý\n");
            tips.append("• Giãn cơ sau khi tập");
        }
        
        return tips.toString();
    }
}