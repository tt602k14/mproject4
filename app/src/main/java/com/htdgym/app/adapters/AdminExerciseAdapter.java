package com.htdgym.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.utils.ThumbnailManager;

import java.util.List;

public class AdminExerciseAdapter extends RecyclerView.Adapter<AdminExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises;
    private OnExerciseActionListener listener;

    public interface OnExerciseActionListener {
        void onEditExercise(Exercise exercise);
        void onDeleteExercise(Exercise exercise);
        void onViewExercise(Exercise exercise);
    }

    public AdminExerciseAdapter(List<Exercise> exercises, OnExerciseActionListener listener) {
        this.exercises = exercises;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise, listener);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        
        private CardView cardExercise;
        private ImageView ivThumbnail, btnEdit, btnDelete, btnView;
        private TextView tvName, tvMuscleGroup, tvDifficulty, tvSetsReps, 
                        tvDuration, tvCalories, tvCategory;
        private View difficultyIndicator;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardExercise = itemView.findViewById(R.id.card_exercise);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnView = itemView.findViewById(R.id.btn_view);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMuscleGroup = itemView.findViewById(R.id.tv_muscle_group);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
            tvSetsReps = itemView.findViewById(R.id.tv_sets_reps);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvCalories = itemView.findViewById(R.id.tv_calories);
            tvCategory = itemView.findViewById(R.id.tv_category);
            difficultyIndicator = itemView.findViewById(R.id.difficulty_indicator);
        }

        public void bind(Exercise exercise, OnExerciseActionListener listener) {
            tvName.setText(exercise.getName());
            tvMuscleGroup.setText(exercise.getMuscleGroup());
            tvDifficulty.setText(exercise.getDifficulty());
            tvCategory.setText(exercise.getCategory());
            
            // Format sets and reps
            if (exercise.getSets() > 0 && exercise.getReps() > 0) {
                tvSetsReps.setText(exercise.getSets() + " sets × " + exercise.getReps() + " reps");
            } else {
                tvSetsReps.setText("Chưa thiết lập");
            }
            
            // Duration
            if (exercise.getDuration() > 0) {
                tvDuration.setText(exercise.getDuration() + " phút");
            } else {
                tvDuration.setText("--");
            }
            
            // Calories
            if (exercise.getCalories() > 0) {
                tvCalories.setText(exercise.getCalories() + " calo");
            } else {
                tvCalories.setText("--");
            }
            
            // Load thumbnail
            ThumbnailManager.loadThumbnail(itemView.getContext(), ivThumbnail, 
                exercise.getName(), exercise.getVideoUrl());
            
            // Set difficulty indicator color
            setDifficultyIndicator(exercise.getDifficulty());
            
            // Set click listeners
            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditExercise(exercise);
                }
            });
            
            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteExercise(exercise);
                }
            });
            
            btnView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewExercise(exercise);
                }
            });
            
            cardExercise.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewExercise(exercise);
                }
            });
        }

        private void setDifficultyIndicator(String difficulty) {
            int color;
            switch (difficulty) {
                case "Dễ":
                    color = 0xFF4CAF50; // Green
                    break;
                case "Trung bình":
                    color = 0xFFFF9800; // Orange
                    break;
                case "Khó":
                    color = 0xFFFF5722; // Red-Orange
                    break;
                case "Rất khó":
                    color = 0xFFF44336; // Red
                    break;
                default:
                    color = 0xFF9E9E9E; // Gray
                    break;
            }
            difficultyIndicator.setBackgroundColor(color);
        }
    }
}