package com.htdgym.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.htdgym.app.R;
import com.htdgym.app.activities.WorkoutDetailActivity;

public class WorkoutsFragment extends Fragment {

    private CardView cardAll, cardChest, cardShoulder, cardLegs, cardAbs, cardBack;
    private LinearLayout layoutWorkoutList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);
        
        initViews(view);
        setupClickListeners();
        loadPopularWorkouts();
        
        return view;
    }

    private void initViews(View view) {
        cardAll = view.findViewById(R.id.card_all);
        cardChest = view.findViewById(R.id.card_chest);
        cardShoulder = view.findViewById(R.id.card_shoulder);
        cardLegs = view.findViewById(R.id.card_legs);
        cardAbs = view.findViewById(R.id.card_abs);
        cardBack = view.findViewById(R.id.card_back);
        layoutWorkoutList = view.findViewById(R.id.layout_workout_list);
    }

    private void setupClickListeners() {
        cardAll.setOnClickListener(v -> openWorkoutDetail("all"));
        cardChest.setOnClickListener(v -> openWorkoutDetail("chest"));
        cardShoulder.setOnClickListener(v -> openWorkoutDetail("shoulder"));
        cardLegs.setOnClickListener(v -> openWorkoutDetail("legs"));
        cardAbs.setOnClickListener(v -> openWorkoutDetail("abs"));
        cardBack.setOnClickListener(v -> openWorkoutDetail("back"));
    }

    private void openWorkoutDetail(String category) {
        Intent intent = new Intent(requireContext(), WorkoutDetailActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void loadPopularWorkouts() {
        // Chương trình 30-60-90 ngày
        addWorkoutItem("Chương Trình Cho Người Mới - 30 Ngày", "30 ngày để có cơ bụng 6 múi", "https://youtu.be/Y8VflnViz78");
        addWorkoutItem("Chương Trình Trung Cấp - 60 Ngày", "60 ngày xây dựng cơ bắp toàn thân", "https://youtu.be/-R5sH2iG9Gw");
        addWorkoutItem("Chương Trình Nâng Cao - 90 Ngày", "90 ngày transformation hoàn toàn", "https://youtu.be/JZQA08SlJnM");
        
        // Các bài tập cụ thể
        addWorkoutItem("Tập Tay Trong 45 Ngày", "45 ngày • 5-7 bài tập/ngày", "https://youtu.be/-R5sH2iG9Gw");
        addWorkoutItem("Bộ Ngực Vạm Vỡ Trong 45 Ngày", "45 ngày • 6-8 bài tập/ngày", "https://youtu.be/-R5sH2iG9Gw");
        addWorkoutItem("Đôi Chân Mạnh Mẽ Trong 45 Ngày", "45 ngày • 8-10 bài tập/ngày", "https://youtu.be/XtoO9YwNEqA");
        addWorkoutItem("Bài Tập Lưng Và Vai", "30 ngày • 6-8 bài tập/ngày", "https://youtu.be/d1HZBdD0idE");
        addWorkoutItem("Tập Luyện Toàn Thân", "30 ngày • 8-10 bài tập/ngày", "https://youtu.be/JZQA08SlJnM");
        addWorkoutItem("Giảm Mỡ Bụng - Người Mới", "30 ngày • 7-9 bài tập/ngày", "https://youtu.be/Y8VflnViz78");
        addWorkoutItem("Cardio Giảm Cân", "21 ngày • 5-7 bài tập/ngày", "https://youtu.be/c4DAnQ6DtF8");
        addWorkoutItem("HIIT Đốt Mỡ", "14 ngày • 6-8 bài tập/ngày", "https://youtu.be/JZQA08SlJnM");
    }

    private void addWorkoutItem(String title, String subtitle, String videoUrl) {
        CardView workoutCard = new CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (12 * getResources().getDisplayMetrics().density));
        workoutCard.setLayoutParams(cardParams);
        workoutCard.setRadius(12 * getResources().getDisplayMetrics().density);
        workoutCard.setCardElevation(2 * getResources().getDisplayMetrics().density);
        workoutCard.setCardBackgroundColor(0xFFFFFFFF);

        LinearLayout contentLayout = new LinearLayout(requireContext());
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density));
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Icon with thumbnail image
        CardView iconCard = new CardView(requireContext());
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                (int) (80 * getResources().getDisplayMetrics().density),
                (int) (60 * getResources().getDisplayMetrics().density));
        iconParams.setMargins(0, 0, (int) (16 * getResources().getDisplayMetrics().density), 0);
        iconCard.setLayoutParams(iconParams);
        iconCard.setRadius(8 * getResources().getDisplayMetrics().density);
        iconCard.setCardBackgroundColor(0xFFF0F0F0);

        ImageView thumbnailImage = new ImageView(requireContext());
        thumbnailImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        thumbnailImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        // Load YouTube thumbnail
        com.htdgym.app.utils.YouTubeHelper.loadThumbnail(thumbnailImage, videoUrl, 
                com.htdgym.app.utils.YouTubeHelper.ThumbnailQuality.MEDIUM);
        
        iconCard.addView(thumbnailImage);

        // Info
        LinearLayout infoLayout = new LinearLayout(requireContext());
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        infoLayout.setLayoutParams(infoParams);

        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(title);
        tvTitle.setTextColor(0xFF1A1A1A);
        tvTitle.setTextSize(16);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvSubtitle = new TextView(requireContext());
        tvSubtitle.setText(subtitle);
        tvSubtitle.setTextColor(0xFF999999);
        tvSubtitle.setTextSize(13);
        LinearLayout.LayoutParams subtitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        subtitleParams.setMargins(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        tvSubtitle.setLayoutParams(subtitleParams);

        infoLayout.addView(tvTitle);
        infoLayout.addView(tvSubtitle);

        // Arrow
        TextView tvArrow = new TextView(requireContext());
        tvArrow.setText("›");
        tvArrow.setTextColor(0xFFC0C0C0);
        tvArrow.setTextSize(28);

        contentLayout.addView(iconCard);
        contentLayout.addView(infoLayout);
        contentLayout.addView(tvArrow);

        workoutCard.addView(contentLayout);

        workoutCard.setOnClickListener(v -> {
            // Check if this is a 30/60/90 day program
            if (title.contains("30 Ngày")) {
                Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramDetailActivity.class);
                intent.putExtra("program_type", "30");
                startActivity(intent);
                return;
            } else if (title.contains("60 Ngày")) {
                Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramDetailActivity.class);
                intent.putExtra("program_type", "60");
                startActivity(intent);
                return;
            } else if (title.contains("90 Ngày")) {
                Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramDetailActivity.class);
                intent.putExtra("program_type", "90");
                startActivity(intent);
                return;
            }
            
            // Open YouTube video for other workouts
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(android.net.Uri.parse(videoUrl));
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(android.net.Uri.parse(videoUrl));
                    startActivity(intent);
                } catch (Exception ex) {
                    // Fallback to workout detail
                    String category = "all";
                    if (title.contains("Ngực")) category = "chest";
                    else if (title.contains("Vai")) category = "shoulder";
                    else if (title.contains("Chân")) category = "legs";
                    else if (title.contains("Bụng")) category = "abs";
                    else if (title.contains("Lưng")) category = "back";
                    
                    openWorkoutDetail(category);
                }
            }
        });

        layoutWorkoutList.addView(workoutCard);
    }
}
