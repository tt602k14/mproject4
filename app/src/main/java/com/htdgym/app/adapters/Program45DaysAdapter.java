package com.htdgym.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.models.Exercise;

import java.util.List;

public class Program45DaysAdapter extends RecyclerView.Adapter<Program45DaysAdapter.DayViewHolder> {

    public interface OnDayClickListener {
        void onDayClick(Exercise exercise, int day);
    }

    private Context context;
    private List<Exercise> exercises;
    private OnDayClickListener listener;

    public Program45DaysAdapter(Context context, OnDayClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void updateExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_program_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        if (exercises != null && position < exercises.size()) {
            Exercise exercise = exercises.get(position);
            int dayNumber = position + 1;
            
            holder.tvDayNumber.setText(String.valueOf(dayNumber));
            
            // Set exercise name (shortened)
            String exerciseName = exercise.getName();
            if (exerciseName.length() > 15) {
                exerciseName = exerciseName.substring(0, 12) + "...";
            }
            holder.tvExerciseName.setText(exerciseName);
            
            // Set difficulty indicator
            holder.tvDifficulty.setText(exercise.getDifficulty());
            
            // Color coding based on exercise type
            int backgroundColor = getBackgroundColor(exercise, dayNumber);
            holder.cardDay.setCardBackgroundColor(backgroundColor);
            
            // Set text color based on background
            int textColor = isLightColor(backgroundColor) ? Color.BLACK : Color.WHITE;
            holder.tvDayNumber.setTextColor(textColor);
            holder.tvExerciseName.setTextColor(textColor);
            holder.tvDifficulty.setTextColor(textColor);
            
            // Click listener
            holder.cardDay.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDayClick(exercise, dayNumber);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return exercises != null ? exercises.size() : 0;
    }

    private int getBackgroundColor(Exercise exercise, int dayNumber) {
        // Rest days
        if (exercise.getMuscleGroup().equals("rest")) {
            return Color.parseColor("#E0E0E0");
        }
        
        // Week-based coloring
        int week = (dayNumber - 1) / 7 + 1;
        
        switch (week) {
            case 1:
            case 2:
                return Color.parseColor("#81C784"); // Light green - Basic
            case 3:
            case 4:
                return Color.parseColor("#FFB74D"); // Orange - Intermediate
            case 5:
            case 6:
                return Color.parseColor("#E57373"); // Red - Advanced
            case 7:
                return Color.parseColor("#FFD700"); // Gold - Recovery/Completion
            default:
                return Color.parseColor("#90A4AE"); // Gray - Default
        }
    }
    
    private boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        CardView cardDay;
        TextView tvDayNumber, tvExerciseName, tvDifficulty;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            cardDay = (CardView) itemView;
            tvDayNumber = itemView.findViewById(R.id.tv_day_number);
            tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
        }
    }
}