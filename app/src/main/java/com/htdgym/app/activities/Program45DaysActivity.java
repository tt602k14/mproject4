package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.Program45DaysAdapter;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.utils.Program45DaysManager;

import java.util.List;

public class Program45DaysActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvProgramTitle, tvProgramDescription, tvProgress;
    private RecyclerView recyclerDays;
    private Program45DaysAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_45_days);

        initViews();
        setupRecyclerView();
        loadProgramData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvProgramTitle = findViewById(R.id.tv_program_title);
        tvProgramDescription = findViewById(R.id.tv_program_description);
        tvProgress = findViewById(R.id.tv_progress);
        recyclerDays = findViewById(R.id.recycler_days);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new Program45DaysAdapter(this, (exercise, day) -> {
            try {
                // Open exercise detail for specific day
                Intent intent = new Intent(Program45DaysActivity.this, ExerciseDetailActivity.class);
                intent.putExtra("exercise_name", exercise.getName());
                intent.putExtra("muscle_group", exercise.getMuscleGroup());
                intent.putExtra("sets_reps", exercise.getFormattedSetsReps());
                intent.putExtra("rest_time", exercise.getRestTime());
                intent.putExtra("difficulty", exercise.getDifficulty());
                intent.putExtra("video_url", exercise.getVideoUrl());
                intent.putExtra("day_number", day);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Program45DaysActivity.this, 
                    "Lỗi mở chi tiết bài tập: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        recyclerDays.setLayoutManager(new GridLayoutManager(this, 7)); // 7 days per week
        recyclerDays.setAdapter(adapter);
    }

    private void loadProgramData() {
        String programType = getIntent().getStringExtra("program_type");
        String programTitle = getIntent().getStringExtra("program_title");

        if (programTitle != null) {
            tvProgramTitle.setText(programTitle);
        }

        List<Exercise> exercises = getProgramExercises(programType);
        tvProgramDescription.setText("Chương trình 45 ngày hoàn chỉnh - Tập luyện khoa học từ cơ bản đến nâng cao");
        
        // Calculate progress (example: user completed 10 days)
        int completedDays = 0; // This should come from user preferences/database
        tvProgress.setText("Tiến độ: " + completedDays + "/45 ngày (" + (completedDays * 100 / 45) + "%)");
        
        adapter.updateExercises(exercises);
    }

    private List<Exercise> getProgramExercises(String programType) {
        switch (programType) {
            case "shoulder_45":
                return Program45DaysManager.getShoulder45DaysProgram();
            case "chest_45":
                return Program45DaysManager.getChest45DaysProgram();
            case "legs_45":
                return Program45DaysManager.getLegs45DaysProgram();
            default:
                return Program45DaysManager.getShoulder45DaysProgram();
        }
    }
}