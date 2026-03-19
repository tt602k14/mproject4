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

public class MuscleBuilderActivity extends AppCompatActivity implements ExerciseCardAdapter.OnExerciseClickListener {

    private ImageView btnBack;
    private TextView tvTitle, tvDescription;
    private RecyclerView recyclerExercises;
    private CardView premiumLockCard;
    private ExerciseCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_builder);

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

        tvTitle.setText("💪 Xây Dựng Cơ Bắp Mạnh Mẽ");
        tvDescription.setText("10 bài tập nặng chuyên sâu để xây dựng khối lượng cơ bắp tối đa. Chương trình Premium dành cho người có kinh nghiệm.");
    }

    private void setupRecyclerView() {
        adapter = new ExerciseCardAdapter(new ArrayList<>(), this);
        recyclerExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerExercises.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        premiumLockCard.setOnClickListener(v -> {
            PremiumManager.showPremiumDialog(this, "Chương trình xây dựng cơ bắp");
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
            loadMuscleBuilderExercises();
        }
    }

    private void loadMuscleBuilderExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        // 10 intensive muscle building exercises
        exercises.add(new Exercise("Weighted Pull-ups", "Lưng", "5×6-8", "180s", "#4CAF50", "advanced"));
        exercises.add(new Exercise("Heavy Squats", "Chân", "5×5-6", "240s", "#4ECDC4", "advanced"));
        exercises.add(new Exercise("Weighted Dips", "Ngực", "5×6-8", "180s", "#FF6B6B", "advanced"));
        exercises.add(new Exercise("Overhead Press", "Vai", "5×5-6", "180s", "#9C27B0", "advanced"));
        exercises.add(new Exercise("Barbell Rows", "Lưng", "5×6-8", "180s", "#4CAF50", "advanced"));
        
        exercises.add(new Exercise("Bulgarian Split Squats", "Chân", "4×8-10/leg", "120s", "#4ECDC4", "advanced"));
        exercises.add(new Exercise("Weighted Push-ups", "Ngực", "4×8-12", "120s", "#FF6B6B", "advanced"));
        exercises.add(new Exercise("Pike Push-ups", "Vai", "4×8-12", "120s", "#9C27B0", "advanced"));
        exercises.add(new Exercise("Weighted Lunges", "Chân", "4×10/leg", "120s", "#4ECDC4", "advanced"));
        exercises.add(new Exercise("Archer Pull-ups", "Lưng", "4×5/side", "150s", "#4CAF50", "advanced"));
        
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