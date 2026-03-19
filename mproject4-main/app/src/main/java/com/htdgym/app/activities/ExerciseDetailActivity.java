package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.R;
import com.htdgym.app.utils.WorkoutSaver;

public class ExerciseDetailActivity extends AppCompatActivity {

    private TextView tvExerciseName, tvMuscleGroup, tvDifficulty;
    private TextView tvSetsReps, tvRestTime, tvInstructions;
    private Button btnStartWorkout, btnWatchVideo;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        initViews();
        loadExerciseData();
        setupClickListeners();
    }

    private void initViews() {
        tvExerciseName = findViewById(R.id.tv_exercise_name);
        tvMuscleGroup = findViewById(R.id.tv_muscle_group);
        tvDifficulty = findViewById(R.id.tv_difficulty);
        tvSetsReps = findViewById(R.id.tv_sets_reps);
        tvRestTime = findViewById(R.id.tv_rest_time);
        tvInstructions = findViewById(R.id.tv_instructions);
        btnStartWorkout = findViewById(R.id.btn_start_workout);
        btnWatchVideo = findViewById(R.id.btn_watch_video);
        ivBack = findViewById(R.id.iv_back);
    }

    private void loadExerciseData() {
        String exerciseName = getIntent().getStringExtra("exercise_name");
        if (exerciseName != null) {
            tvExerciseName.setText(exerciseName);
            
            // Parse exercise name to get muscle group
            if (exerciseName.contains("Ngực")) {
                tvMuscleGroup.setText("Ngực");
                tvDifficulty.setText("Trung bình");
                tvSetsReps.setText("3 x 8-12");
                tvRestTime.setText("90 giây");
            } else if (exerciseName.contains("Chân")) {
                tvMuscleGroup.setText("Chân");
                tvDifficulty.setText("Khó");
                tvSetsReps.setText("4 x 6-10");
                tvRestTime.setText("120 giây");
            } else if (exerciseName.contains("Lưng")) {
                tvMuscleGroup.setText("Lưng");
                tvDifficulty.setText("Trung bình");
                tvSetsReps.setText("3 x 8-10");
                tvRestTime.setText("90 giây");
            } else if (exerciseName.contains("Tay")) {
                tvMuscleGroup.setText("Tay");
                tvDifficulty.setText("Dễ");
                tvSetsReps.setText("3 x 10-15");
                tvRestTime.setText("60 giây");
            } else if (exerciseName.contains("Bụng")) {
                tvMuscleGroup.setText("Bụng");
                tvDifficulty.setText("Dễ");
                tvSetsReps.setText("3 x 15-20");
                tvRestTime.setText("45 giây");
            } else {
                tvMuscleGroup.setText("Vai");
                tvDifficulty.setText("Trung bình");
                tvSetsReps.setText("3 x 10-12");
                tvRestTime.setText("75 giây");
            }
        }
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnStartWorkout.setOnClickListener(v -> {
            String name = tvExerciseName.getText().toString();
            Toast.makeText(this, "Bắt đầu tập " + name, Toast.LENGTH_SHORT).show();

            // Mở WorkoutSessionActivity — nó sẽ tự lưu WorkoutSaver trong onCreate
            Intent sessionIntent = new Intent(this, WorkoutSessionActivity.class);
            sessionIntent.putExtra("exercise_name", name);
            sessionIntent.putExtra("exercise_description", tvMuscleGroup.getText() + " - " + tvDifficulty.getText());
            sessionIntent.putExtra("exercise_duration", 15);
            sessionIntent.putExtra("exercise_calories", 120);
            startActivity(sessionIntent);
        });

        btnWatchVideo.setOnClickListener(v -> {
            Intent intent = new Intent(ExerciseDetailActivity.this, VideoLibraryActivity.class);
            startActivity(intent);
        });
    }
}
