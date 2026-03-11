package com.htdgym.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.R;
import com.htdgym.app.activities.BaseActivity;

public class TrainingScheduleActivity extends BaseActivity {

    private LinearLayout layoutSchedule;
    private LinearLayout btnVideoLibrary, btnCreateSchedule;
    private LinearLayout cardFullBody, cardCardio, cardAbs;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_schedule);

        initViews();
        setupClickListeners();
        loadSchedule();
    }

    private void initViews() {
        layoutSchedule = findViewById(R.id.layout_schedule);
        btnVideoLibrary = findViewById(R.id.btn_video_library);
        btnCreateSchedule = findViewById(R.id.btn_create_schedule);
        cardFullBody = findViewById(R.id.card_full_body);
        cardCardio = findViewById(R.id.card_cardio);
        cardAbs = findViewById(R.id.card_abs);
        ivBack = findViewById(R.id.iv_back);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());
        
        btnVideoLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(TrainingScheduleActivity.this, VideoLibraryActivity.class);
            startActivity(intent);
        });

        btnCreateSchedule.setOnClickListener(v -> {
            Toast.makeText(this, "Tạo lộ trình mới", Toast.LENGTH_SHORT).show();
        });

        cardFullBody.setOnClickListener(v -> startQuickWorkout("Toàn thân"));
        cardCardio.setOnClickListener(v -> startQuickWorkout("Cardio"));
        cardAbs.setOnClickListener(v -> startQuickWorkout("Bụng"));
    }

    private void startQuickWorkout(String type) {
        // Intent intent = new Intent(TrainingScheduleActivity.this, WorkoutSessionActivity.class);
        // intent.putExtra("workout_name", "Tập " + type);
        // intent.putExtra("duration", 30);
        // startActivity(intent);
        Toast.makeText(this, "Bắt đầu tập " + type, Toast.LENGTH_SHORT).show();
    }

    private void loadSchedule() {
        addScheduleDay("Thứ 2", "Ngực & Vai", "45 phút", "320 calo", true);
        addScheduleDay("Thứ 3", "Chân & Mông", "50 phút", "380 calo", false);
        addScheduleDay("Thứ 4", "Lưng & Tay sau", "40 phút", "300 calo", false);
        addScheduleDay("Thứ 5", "Toàn thân", "55 phút", "400 calo", false);
    }

    private void addScheduleDay(String day, String workout, String duration, String calories, boolean isActive) {
        LinearLayout dayLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 12);
        dayLayout.setLayoutParams(params);
        dayLayout.setOrientation(LinearLayout.HORIZONTAL);
        dayLayout.setBackgroundColor(Color.parseColor(isActive ? "#E8F5E9" : "#FFFFFF"));
        dayLayout.setPadding(16, 16, 16, 16);

        // Left bar
        View leftBar = new View(this);
        LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(4, LinearLayout.LayoutParams.MATCH_PARENT);
        barParams.setMargins(0, 0, 12, 0);
        leftBar.setLayoutParams(barParams);
        leftBar.setBackgroundColor(Color.parseColor(isActive ? "#4CAF50" : "#CCCCCC"));

        // Content
        LinearLayout contentLayout = new LinearLayout(this);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        contentLayout.setLayoutParams(contentParams);
        contentLayout.setOrientation(LinearLayout.VERTICAL);

        TextView tvDay = new TextView(this);
        tvDay.setText(day);
        tvDay.setTextColor(Color.parseColor(isActive ? "#4CAF50" : "#FF9800"));
        tvDay.setTextSize(14);
        tvDay.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvWorkout = new TextView(this);
        tvWorkout.setText(workout);
        tvWorkout.setTextColor(Color.parseColor("#000000"));
        tvWorkout.setTextSize(16);
        tvWorkout.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams workoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        workoutParams.setMargins(0, 4, 0, 4);
        tvWorkout.setLayoutParams(workoutParams);

        TextView tvDetails = new TextView(this);
        tvDetails.setText(duration + " • " + calories);
        tvDetails.setTextColor(Color.parseColor("#666666"));
        tvDetails.setTextSize(12);

        contentLayout.addView(tvDay);
        contentLayout.addView(tvWorkout);
        contentLayout.addView(tvDetails);

        // Right info
        LinearLayout rightLayout = new LinearLayout(this);
        rightLayout.setOrientation(LinearLayout.VERTICAL);
        rightLayout.setGravity(android.view.Gravity.CENTER);

        TextView tvStatus = new TextView(this);
        if (isActive) {
            tvStatus.setText("✓");
            tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            tvStatus.setTextSize(24);
        } else {
            tvStatus.setText("→");
            tvStatus.setTextColor(Color.parseColor("#CCCCCC"));
            tvStatus.setTextSize(20);
        }

        rightLayout.addView(tvStatus);

        dayLayout.addView(leftBar);
        dayLayout.addView(contentLayout);
        dayLayout.addView(rightLayout);

        dayLayout.setOnClickListener(v -> {
            // Intent intent = new Intent(TrainingScheduleActivity.this, WorkoutSessionActivity.class);
            // intent.putExtra("workout_name", workout);
            // intent.putExtra("duration", 45);
            // startActivity(intent);
            Toast.makeText(TrainingScheduleActivity.this, "Bắt đầu: " + workout, Toast.LENGTH_SHORT).show();
        });

        layoutSchedule.addView(dayLayout);
    }
}
