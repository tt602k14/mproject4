package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.ExerciseCardAdapter;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.utils.PremiumManager;

import java.util.ArrayList;
import java.util.List;

public class HIITWorkoutActivity extends AppCompatActivity implements ExerciseCardAdapter.OnExerciseClickListener {

    private ImageView btnBack;
    private TextView tvTitle, tvDescription;
    private RecyclerView recyclerExercises;
    private CardView premiumLockCard;
    private ExerciseCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiit_workout);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        checkPremiumAccess();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        recyclerExercises = findViewById(R.id.recycler_exercises);
        premiumLockCard = findViewById(R.id.premium_lock_card);

        tvTitle.setText("🔥 HIIT Đốt Mỡ Cực Mạnh");
        tvDescription.setText("10 bài tập HIIT cường độ cao giúp đốt mỡ nhanh chóng và hiệu quả. Chương trình Premium dành cho người có kinh nghiệm.");
    }

    private void setupRecyclerView() {
        adapter = new ExerciseCardAdapter(new ArrayList<>(), this);
        recyclerExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerExercises.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        premiumLockCard.setOnClickListener(v -> {
            PremiumManager.showPremiumDialog(this, "Chương trình HIIT đốt mỡ");
        });
    }

    private void checkPremiumAccess() {
        boolean isPremium = PremiumManager.isPremiumUser(this);
        
        if (!isPremium) {
            premiumLockCard.setVisibility(View.VISIBLE);
            recyclerExercises.setVisibility(View.GONE);
        } else {
            premiumLockCard.setVisibility(View.GONE);
            recyclerExercises.setVisibility(View.VISIBLE);
            loadHIITExercises();
        }
    }

    private void loadHIITExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        // 10 intensive HIIT exercises
        exercises.add(new Exercise("Burpee Tuck Jumps", "Toàn thân", "4×45s", "15s", "#E91E63", "advanced"));
        exercises.add(new Exercise("Mountain Climber Sprints", "Bụng", "4×40s", "20s", "#AB47BC", "advanced"));
        exercises.add(new Exercise("Jump Squat Pulses", "Chân", "4×35s", "25s", "#4ECDC4", "advanced"));
        exercises.add(new Exercise("High Knee Sprints", "Chân", "4×30s", "30s", "#4ECDC4", "advanced"));
        exercises.add(new Exercise("Plank Jacks", "Bụng", "4×40s", "20s", "#AB47BC", "advanced"));
        
        exercises.add(new Exercise("Star Jumps", "Toàn thân", "4×45s", "15s", "#E91E63", "advanced"));
        exercises.add(new Exercise("Russian Twist Sprints", "Bụng", "4×35s", "25s", "#AB47BC", "advanced"));
        exercises.add(new Exercise("Explosive Push-ups", "Ngực", "4×30s", "30s", "#FF6B6B", "advanced"));
        exercises.add(new Exercise("Lateral Bounds", "Chân", "4×40s", "20s", "#4ECDC4", "advanced"));
        exercises.add(new Exercise("Battle Rope Slams", "Toàn thân", "4×45s", "15s", "#E91E63", "advanced"));
        
        adapter.updateExercises(exercises);
    }

    @Override
    public void onExerciseClick(Exercise exercise) {
        try {
            Intent intent = new Intent(this, ExerciseDetailActivity.class);
            intent.putExtra("exercise_name", exercise.getName());
            intent.putExtra("muscle_group", exercise.getMuscleGroup());
            intent.putExtra("sets_reps", exercise.getFormattedSetsReps());
            intent.putExtra("rest_time", exercise.getRestTime());
            intent.putExtra("difficulty", exercise.getDifficulty());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi mở chi tiết bài tập: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFavoriteClick(Exercise exercise) {
        exercise.setFavorite(!exercise.isFavorite());
        adapter.notifyDataSetChanged();
        
        String message = exercise.isFavorite() ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}