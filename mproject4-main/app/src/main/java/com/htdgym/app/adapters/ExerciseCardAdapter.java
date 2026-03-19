package com.htdgym.app.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.models.Exercise;

import java.util.List;

public class ExerciseCardAdapter extends RecyclerView.Adapter<ExerciseCardAdapter.ViewHolder> {

    private List<Exercise> exercises;

    public ExerciseCardAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
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
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void updateExercises(List<Exercise> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View iconBg;
        private ImageView ivIcon;
        private TextView tvName, tvMuscleGroup, tvSets, tvDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconBg = itemView.findViewById(R.id.view_icon_bg);
            ivIcon = itemView.findViewById(R.id.iv_exercise_icon);
            tvName = itemView.findViewById(R.id.tv_exercise_name);
            tvMuscleGroup = itemView.findViewById(R.id.tv_muscle_group);
            tvSets = itemView.findViewById(R.id.tv_sets);
            tvDuration = itemView.findViewById(R.id.tv_duration);
        }

        public void bind(Exercise exercise) {
            tvName.setText(exercise.getName());
            tvMuscleGroup.setText(exercise.getCategory());
            tvSets.setText(exercise.getFormattedDuration());
            tvDuration.setText(exercise.getDifficulty());
            
            // Set icon color based on muscle group
            int color = getColorForMuscleGroup(exercise.getCategory());
            iconBg.setBackgroundColor(color);
        }

        private int getColorForMuscleGroup(String muscleGroup) {
            switch (muscleGroup) {
                case "chest":
                    return Color.parseColor("#FF6B6B");
                case "legs":
                    return Color.parseColor("#4ECDC4");
                case "back":
                    return Color.parseColor("#4CAF50");
                case "shoulder":
                    return Color.parseColor("#FFA726");
                case "abs":
                    return Color.parseColor("#AB47BC");
                default:
                    return Color.parseColor("#4CAF50");
            }
        }
    }
}
