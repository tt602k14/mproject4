package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.utils.WorkoutPlannerAI;

import java.util.ArrayList;
import java.util.List;

public class SmartWorkoutPlannerActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvTitle, tvDescription, tvRecommendation;
    private LinearLayout layoutFitnessLevel, layoutGoal, layoutTime;
    private Button btnGenerateWorkout, btnViewTemplates;
    
    // Selection variables
    private WorkoutPlannerAI.FitnessLevel selectedLevel = WorkoutPlannerAI.FitnessLevel.BEGINNER;
    private WorkoutPlannerAI.WorkoutGoal selectedGoal = WorkoutPlannerAI.WorkoutGoal.GENERAL_FITNESS;
    private WorkoutPlannerAI.TimeAvailable selectedTime = WorkoutPlannerAI.TimeAvailable.MEDIUM;
    
    // UI Cards
    private CardView cardBeginner, cardIntermediate, cardAdvanced;
    private CardView cardFatLoss, cardMuscle, cardStrength, cardEndurance, cardGeneral;
    private CardView cardShort, cardMedium, cardLong, cardExtended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_workout_planner);

        initViews();
        setupClickListeners();
        updateRecommendation();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        tvRecommendation = findViewById(R.id.tv_recommendation);
        
        layoutFitnessLevel = findViewById(R.id.layout_fitness_level);
        layoutGoal = findViewById(R.id.layout_goal);
        layoutTime = findViewById(R.id.layout_time);
        
        btnGenerateWorkout = findViewById(R.id.btn_generate_workout);
        btnViewTemplates = findViewById(R.id.btn_view_templates);
        
        // Fitness Level Cards
        cardBeginner = findViewById(R.id.card_beginner);
        cardIntermediate = findViewById(R.id.card_intermediate);
        cardAdvanced = findViewById(R.id.card_advanced);
        
        // Goal Cards
        cardFatLoss = findViewById(R.id.card_fat_loss);
        cardMuscle = findViewById(R.id.card_muscle);
        cardStrength = findViewById(R.id.card_strength);
        cardEndurance = findViewById(R.id.card_endurance);
        cardGeneral = findViewById(R.id.card_general);
        
        // Time Cards
        cardShort = findViewById(R.id.card_short);
        cardMedium = findViewById(R.id.card_medium);
        cardLong = findViewById(R.id.card_long);
        cardExtended = findViewById(R.id.card_extended);
        
        // Set initial selection
        updateFitnessLevelSelection();
        updateGoalSelection();
        updateTimeSelection();
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        // Fitness Level Selection
        cardBeginner.setOnClickListener(v -> {
            selectedLevel = WorkoutPlannerAI.FitnessLevel.BEGINNER;
            updateFitnessLevelSelection();
            updateRecommendation();
        });
        
        cardIntermediate.setOnClickListener(v -> {
            selectedLevel = WorkoutPlannerAI.FitnessLevel.INTERMEDIATE;
            updateFitnessLevelSelection();
            updateRecommendation();
        });
        
        cardAdvanced.setOnClickListener(v -> {
            selectedLevel = WorkoutPlannerAI.FitnessLevel.ADVANCED;
            updateFitnessLevelSelection();
            updateRecommendation();
        });
        
        // Goal Selection
        cardFatLoss.setOnClickListener(v -> {
            selectedGoal = WorkoutPlannerAI.WorkoutGoal.FAT_LOSS;
            updateGoalSelection();
            updateRecommendation();
        });
        
        cardMuscle.setOnClickListener(v -> {
            selectedGoal = WorkoutPlannerAI.WorkoutGoal.MUSCLE_BUILDING;
            updateGoalSelection();
            updateRecommendation();
        });
        
        cardStrength.setOnClickListener(v -> {
            selectedGoal = WorkoutPlannerAI.WorkoutGoal.STRENGTH;
            updateGoalSelection();
            updateRecommendation();
        });
        
        cardEndurance.setOnClickListener(v -> {
            selectedGoal = WorkoutPlannerAI.WorkoutGoal.ENDURANCE;
            updateGoalSelection();
            updateRecommendation();
        });
        
        cardGeneral.setOnClickListener(v -> {
            selectedGoal = WorkoutPlannerAI.WorkoutGoal.GENERAL_FITNESS;
            updateGoalSelection();
            updateRecommendation();
        });
        
        // Time Selection
        cardShort.setOnClickListener(v -> {
            selectedTime = WorkoutPlannerAI.TimeAvailable.SHORT;
            updateTimeSelection();
            updateRecommendation();
        });
        
        cardMedium.setOnClickListener(v -> {
            selectedTime = WorkoutPlannerAI.TimeAvailable.MEDIUM;
            updateTimeSelection();
            updateRecommendation();
        });
        
        cardLong.setOnClickListener(v -> {
            selectedTime = WorkoutPlannerAI.TimeAvailable.LONG;
            updateTimeSelection();
            updateRecommendation();
        });
        
        cardExtended.setOnClickListener(v -> {
            selectedTime = WorkoutPlannerAI.TimeAvailable.EXTENDED;
            updateTimeSelection();
            updateRecommendation();
        });
        
        // Generate Workout
        btnGenerateWorkout.setOnClickListener(v -> generatePersonalizedWorkout());
        
        // View Templates
        btnViewTemplates.setOnClickListener(v -> {
            Intent intent = new Intent(this, WorkoutTemplatesActivity.class);
            startActivity(intent);
        });
    }

    private void updateFitnessLevelSelection() {
        resetCardSelection(cardBeginner, cardIntermediate, cardAdvanced);
        
        switch (selectedLevel) {
            case BEGINNER:
                selectCard(cardBeginner);
                break;
            case INTERMEDIATE:
                selectCard(cardIntermediate);
                break;
            case ADVANCED:
                selectCard(cardAdvanced);
                break;
        }
    }

    private void updateGoalSelection() {
        resetCardSelection(cardFatLoss, cardMuscle, cardStrength, cardEndurance, cardGeneral);
        
        switch (selectedGoal) {
            case FAT_LOSS:
                selectCard(cardFatLoss);
                break;
            case MUSCLE_BUILDING:
                selectCard(cardMuscle);
                break;
            case STRENGTH:
                selectCard(cardStrength);
                break;
            case ENDURANCE:
                selectCard(cardEndurance);
                break;
            case GENERAL_FITNESS:
                selectCard(cardGeneral);
                break;
        }
    }

    private void updateTimeSelection() {
        resetCardSelection(cardShort, cardMedium, cardLong, cardExtended);
        
        switch (selectedTime) {
            case SHORT:
                selectCard(cardShort);
                break;
            case MEDIUM:
                selectCard(cardMedium);
                break;
            case LONG:
                selectCard(cardLong);
                break;
            case EXTENDED:
                selectCard(cardExtended);
                break;
        }
    }

    private void resetCardSelection(CardView... cards) {
        for (CardView card : cards) {
            card.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            card.setCardElevation(4f);
        }
    }

    private void selectCard(CardView card) {
        card.setCardBackgroundColor(getResources().getColor(R.color.primary_color));
        card.setCardElevation(8f);
    }

    private void updateRecommendation() {
        String recommendation = WorkoutPlannerAI.getWorkoutRecommendation(selectedGoal, selectedLevel);
        tvRecommendation.setText(recommendation);
    }

    private void generatePersonalizedWorkout() {
        try {
            // Generate personalized workout
            List<String> equipment = new ArrayList<>(); // For now, bodyweight only
            List<Exercise> workout = WorkoutPlannerAI.generatePersonalizedWorkout(
                selectedLevel, selectedGoal, selectedTime, equipment);
            
            if (workout.isEmpty()) {
                Toast.makeText(this, "Không thể tạo workout. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Create workout name
            String workoutName = createWorkoutName();
            
            // Pass to workout session
            Intent intent = new Intent(this, GeneratedWorkoutActivity.class);
            intent.putExtra("workout_name", workoutName);
            intent.putExtra("fitness_level", selectedLevel.name());
            intent.putExtra("goal", selectedGoal.name());
            intent.putExtra("time_available", selectedTime.name());
            
            // Pass exercises (you might need to implement Parcelable for Exercise)
            ArrayList<String> exerciseNames = new ArrayList<>();
            ArrayList<String> muscleGroups = new ArrayList<>();
            ArrayList<String> setsReps = new ArrayList<>();
            ArrayList<String> restTimes = new ArrayList<>();
            ArrayList<String> difficulties = new ArrayList<>();
            ArrayList<String> videoUrls = new ArrayList<>();
            
            for (Exercise exercise : workout) {
                exerciseNames.add(exercise.getName());
                muscleGroups.add(exercise.getMuscleGroup());
                setsReps.add(exercise.getSetsReps());
                restTimes.add(exercise.getRestTime());
                difficulties.add(exercise.getDifficulty());
                videoUrls.add(exercise.getVideoUrl());
            }
            
            intent.putStringArrayListExtra("exercise_names", exerciseNames);
            intent.putStringArrayListExtra("muscle_groups", muscleGroups);
            intent.putStringArrayListExtra("sets_reps", setsReps);
            intent.putStringArrayListExtra("rest_times", restTimes);
            intent.putStringArrayListExtra("difficulties", difficulties);
            intent.putStringArrayListExtra("video_urls", videoUrls);
            
            startActivity(intent);
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi tạo workout: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String createWorkoutName() {
        String levelName = "";
        switch (selectedLevel) {
            case BEGINNER: levelName = "Người mới"; break;
            case INTERMEDIATE: levelName = "Trung cấp"; break;
            case ADVANCED: levelName = "Nâng cao"; break;
        }
        
        String goalName = "";
        switch (selectedGoal) {
            case FAT_LOSS: goalName = "Giảm mỡ"; break;
            case MUSCLE_BUILDING: goalName = "Tăng cơ"; break;
            case STRENGTH: goalName = "Sức mạnh"; break;
            case ENDURANCE: goalName = "Sức bền"; break;
            case GENERAL_FITNESS: goalName = "Thể lực"; break;
        }
        
        return String.format("🤖 AI %s - %s (%d phút)", goalName, levelName, selectedTime.getMinutes());
    }
}