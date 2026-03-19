package com.htdgym.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.htdgym.app.R;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.activities.ExerciseVideoActivity;
import com.htdgym.app.utils.YouTubeHelper;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    
    private Context context;
    private List<Exercise> exercises;
    private OnExerciseClickListener listener;
    
    public interface OnExerciseClickListener {
        void onFavoriteClick(Exercise exercise);
        void onExerciseClick(Exercise exercise);
    }
    
    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }
    
    public void setOnExerciseClickListener(OnExerciseClickListener listener) {
        this.listener = listener;
    }
    
    public void updateExercises(List<Exercise> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise);
    }
    
    @Override
    public int getItemCount() {
        return exercises.size();
    }
    
    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private CardView cardExercise;
        private ImageView ivThumbnail, ivFavorite;
        private TextView tvName, tvDescription, tvDifficulty, tvDuration, tvCalories, tvCompleted, tvPremiumBadge;
        private LinearLayout layoutStats;
        
        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardExercise = itemView.findViewById(R.id.card_exercise);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvCalories = itemView.findViewById(R.id.tv_calories);
            tvCompleted = itemView.findViewById(R.id.tv_completed);
            tvPremiumBadge = itemView.findViewById(R.id.tv_premium_badge);
            layoutStats = itemView.findViewById(R.id.layout_stats);
        }
        
        public void bind(Exercise exercise) {
            tvName.setText(exercise.getName());
            tvDescription.setText(exercise.getDescription());
            tvDifficulty.setText(exercise.getDifficultyEmoji() + " " + exercise.getDifficulty());
            tvDuration.setText("⏱️ " + exercise.getFormattedDuration());
            tvCalories.setText("🔥 " + exercise.getCalories() + " cal");
            tvCompleted.setText("✅ " + exercise.getCompletedCount() + " lần");
            
            // Show/hide premium badge
            if (exercise.isPremium()) {
                tvPremiumBadge.setVisibility(View.VISIBLE);
            } else {
                tvPremiumBadge.setVisibility(View.GONE);
            }
            
            // Load thumbnail
            if (exercise.getVideoUrl() != null && !exercise.getVideoUrl().isEmpty()) {
                YouTubeHelper.loadThumbnail(ivThumbnail, exercise.getVideoUrl(), 
                    YouTubeHelper.ThumbnailQuality.MEDIUM);
            } else {
                ivThumbnail.setImageResource(R.drawable.ic_ai_coach);
            }
            
            // Favorite button
            ivFavorite.setImageResource(exercise.isFavorite() ? 
                android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            
            ivFavorite.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFavoriteClick(exercise);
                }
            });
            
            // Card click
            cardExercise.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onExerciseClick(exercise);
                }
            });
        }
    }
}