package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.R;

public class TestWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_workout);

        Button btnWorkoutLibrary = findViewById(R.id.btn_open_workout_library);
        Button btnTrainingSchedule = findViewById(R.id.btn_open_training_schedule);
        Button btnVideoLibrary = findViewById(R.id.btn_open_video_library);

        btnWorkoutLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(TestWorkoutActivity.this, WorkoutLibraryActivity.class);
            startActivity(intent);
        });

        btnTrainingSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(TestWorkoutActivity.this, TrainingScheduleActivity.class);
            startActivity(intent);
        });

        btnVideoLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(TestWorkoutActivity.this, VideoLibraryActivity.class);
            startActivity(intent);
        });
    }
}
