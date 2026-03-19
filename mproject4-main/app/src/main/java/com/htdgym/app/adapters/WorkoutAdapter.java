package com.htdgym.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.models.Workout;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private final List<Workout> workoutList;

    public WorkoutAdapter(List<Workout> workoutList) {
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workoutList.get(position);

        holder.tvName.setText(workout.getExerciseName());

        holder.tvDetails.setText(
                "Sets: " + workout.getSets() +
                        " | Reps: " + workout.getReps() +
                        " | Weight: " + workout.getWeight() + "kg"
        );
    }

    @Override
    public int getItemCount() {
        return workoutList == null ? 0 : workoutList.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDetails;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvWorkoutName);
            tvDetails = itemView.findViewById(R.id.tvWorkoutDetails);
        }
    }
}
