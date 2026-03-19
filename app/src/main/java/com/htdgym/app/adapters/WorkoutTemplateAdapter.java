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
import com.htdgym.app.models.WorkoutTemplate;
import com.htdgym.app.utils.YouTubeThumbnailHelper;

import java.util.List;

public class WorkoutTemplateAdapter extends RecyclerView.Adapter<WorkoutTemplateAdapter.ViewHolder> {

    private List<WorkoutTemplate> templates;
    private OnTemplateClickListener listener;

    public interface OnTemplateClickListener {
        void onTemplateClick(WorkoutTemplate template);
    }

    public WorkoutTemplateAdapter(List<WorkoutTemplate> templates, OnTemplateClickListener listener) {
        this.templates = templates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkoutTemplate template = templates.get(position);
        
        holder.tvName.setText(template.getName());
        holder.tvDescription.setText(template.getDescription());
        holder.tvDuration.setText(template.getDuration());
        holder.tvDifficulty.setText(template.getDifficultyEmoji() + " " + template.getDifficulty());
        holder.tvExerciseCount.setText(template.getExerciseCount());
        
        // Set color accent
        try {
            int color = Color.parseColor(template.getColor());
            holder.viewColorAccent.setBackgroundColor(color);
        } catch (Exception e) {
            holder.viewColorAccent.setBackgroundColor(Color.parseColor("#4CAF50"));
        }
        
        // Load thumbnail
        if (template.getThumbnailUrl() != null && !template.getThumbnailUrl().isEmpty()) {
            YouTubeThumbnailHelper.loadThumbnail(holder.ivThumbnail, template.getThumbnailUrl(), "mqdefault");
        }
        
        // Click listener
        holder.cardTemplate.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTemplateClick(template);
            }
        });
    }

    @Override
    public int getItemCount() {
        return templates.size();
    }

    public void updateTemplates(List<WorkoutTemplate> newTemplates) {
        this.templates = newTemplates;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardTemplate;
        ImageView ivThumbnail;
        TextView tvName, tvDescription, tvDuration, tvDifficulty, tvExerciseCount;
        View viewColorAccent;

        ViewHolder(View itemView) {
            super(itemView);
            cardTemplate = itemView.findViewById(R.id.card_template);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
            tvExerciseCount = itemView.findViewById(R.id.tv_exercise_count);
            viewColorAccent = itemView.findViewById(R.id.view_color_accent);
        }
    }
}