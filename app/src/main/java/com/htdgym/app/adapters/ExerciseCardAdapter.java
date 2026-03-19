package com.htdgym.app.adapters;

import android.graphics.Color;
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
import com.htdgym.app.utils.YouTubeThumbnailHelper;

import java.util.List;

public class ExerciseCardAdapter extends RecyclerView.Adapter<ExerciseCardAdapter.ViewHolder> {

    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise);
        void onFavoriteClick(Exercise exercise);
    }

    private List<Exercise> exercises;
    private OnExerciseClickListener listener;

    public ExerciseCardAdapter(List<Exercise> exercises, OnExerciseClickListener listener) {
        this.exercises = exercises;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise, listener);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void updateExercises(List<Exercise> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private View iconBg;
        private ImageView ivIcon, ivThumbnail;
        private CardView btnPlay;
        // btn_favorite là ImageView bên trong CardView — tìm trực tiếp ImageView
        private ImageView btnFavorite;
        private TextView tvName, tvMuscleGroup, tvSets, tvDuration, tvDifficulty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            iconBg = itemView.findViewById(R.id.view_icon_bg);
            ivIcon = itemView.findViewById(R.id.iv_exercise_icon);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            // btn_play là CardView trong layout
            btnPlay = itemView.findViewById(R.id.btn_play);
            // btn_favorite là ImageView bên trong CardView
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            tvName = itemView.findViewById(R.id.tv_exercise_name);
            tvMuscleGroup = itemView.findViewById(R.id.tv_muscle_group);
            tvSets = itemView.findViewById(R.id.tv_sets);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
        }

        public void bind(Exercise exercise, OnExerciseClickListener listener) {
            tvName.setText(exercise.getName());
            tvMuscleGroup.setText(exercise.getMuscleGroup());
            tvSets.setText(exercise.getFormattedSetsReps());
            tvDuration.setText(exercise.getRestTime());
            
            // Set difficulty with appropriate styling
            if (tvDifficulty != null) {
                tvDifficulty.setText(exercise.getDifficulty());
                tvDifficulty.setTextColor(getDifficultyColor(exercise.getDifficulty()));
            }
            
            // Load video thumbnail if available
            if (ivThumbnail != null && exercise.getVideoUrl() != null && !exercise.getVideoUrl().isEmpty()) {
                YouTubeThumbnailHelper.loadThumbnail(ivThumbnail, exercise.getVideoUrl(), "hqdefault");
                ivThumbnail.setVisibility(View.VISIBLE);
                if (btnPlay != null) {
                    btnPlay.setVisibility(View.VISIBLE);
                }
            } else {
                // Fallback to colored background
                if (iconBg != null) {
                    int color = getColorForMuscleGroup(exercise.getMuscleGroup());
                    iconBg.setBackgroundColor(color);
                    iconBg.setVisibility(View.VISIBLE);
                }
                
                if (ivThumbnail != null) {
                    ivThumbnail.setVisibility(View.GONE);
                }
                if (btnPlay != null) {
                    btnPlay.setVisibility(View.GONE);
                }
            }
            
            // Set favorite button state
            if (btnFavorite != null) {
                btnFavorite.setImageResource(exercise.isFavorite() ? 
                    R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);
                btnFavorite.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onFavoriteClick(exercise);
                    }
                });
            }
            
            // Set click listener for the whole item
            itemView.setOnClickListener(v -> {
                if (listener != null && !"rest".equalsIgnoreCase(exercise.getMuscleGroup())) {
                    listener.onExerciseClick(exercise);
                }
            });
        }

        private int getColorForMuscleGroup(String muscleGroup) {
            switch (muscleGroup.toLowerCase()) {
                case "chest":
                case "ngực":
                    return Color.parseColor("#FF6B6B");
                case "legs":
                case "chân":
                    return Color.parseColor("#4ECDC4");
                case "back":
                case "lưng":
                    return Color.parseColor("#4CAF50");
                case "shoulder":
                case "vai":
                case "tay":
                    return Color.parseColor("#FFA726");
                case "abs":
                case "bụng":
                    return Color.parseColor("#AB47BC");
                case "hiit":
                case "toàn thân":
                    return Color.parseColor("#E91E63");
                default:
                    return Color.parseColor("#6FCF97");
            }
        }
        
        private int getDifficultyColor(String difficulty) {
            switch (difficulty.toLowerCase()) {
                case "dễ":
                case "beginner":
                    return Color.parseColor("#4CAF50");
                case "trung bình":
                case "intermediate":
                    return Color.parseColor("#FF9800");
                case "khó":
                case "advanced":
                    return Color.parseColor("#F44336");
                default:
                    return Color.parseColor("#666666");
            }
        }
    }
}