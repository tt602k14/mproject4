package com.htdgym.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.htdgym.app.R;
import com.htdgym.app.activities.WorkoutDetailActivity;

public class HomeFragment extends Fragment {

    private TextView tabBuildMuscle, tabBurnFat;
    private View tabIndicator;
    private LinearLayout layoutPrograms, layoutStats, layoutProgramCards;
    private TextView tvSectionTitle;
    private ImageView imgFeatured, btnSettings;
    private TextView tvCalories, tvTime, tvDays, tvWorkouts;
    private ProgressBar progressCircle;
    
    private String currentTab = "build_muscle";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initViews(view);
        setupTabs();
        loadProgramCards();
        loadBuildMusclePrograms();
        loadWorkoutStats();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Reload stats when returning to home screen
        loadWorkoutStats();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Setup FAB click listener
        com.google.android.material.floatingactionbutton.FloatingActionButton fabAiCoach = 
            view.findViewById(R.id.fab_ai_coach);
        
        if (fabAiCoach != null) {
            fabAiCoach.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), com.htdgym.app.activities.AICoachActivity.class);
                startActivity(intent);
            });
        }
    }
    
    private void loadWorkoutStats() {
        new Thread(() -> {
            try {
                android.content.SharedPreferences prefs = requireContext().getSharedPreferences("HTDGymPrefs", android.content.Context.MODE_PRIVATE);
                String userId = prefs.getString("userId", "guest");
                
                com.htdgym.app.database.GymDatabase db = com.htdgym.app.database.GymDatabase.getInstance(requireContext());
                com.htdgym.app.models.WorkoutStats stats = db.workoutStatsDao().getStatsByUserId(userId);
                
                if (stats == null) {
                    stats = new com.htdgym.app.models.WorkoutStats();
                    stats.setUserId(userId);
                }
                
                final com.htdgym.app.models.WorkoutStats finalStats = stats;
                
                requireActivity().runOnUiThread(() -> {
                    tvCalories.setText(String.valueOf(finalStats.getTotalCalories()));
                    tvDays.setText(String.valueOf(finalStats.getTotalDays()));
                    tvTime.setText(finalStats.getFormattedTime());
                    tvWorkouts.setText(String.valueOf(finalStats.getTotalWorkouts()));
                    
                    // Update progress circle (max 1000 calories = 100%)
                    int progress = Math.min(100, (finalStats.getTotalCalories() * 100) / 1000);
                    progressCircle.setProgress(progress);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void initViews(View view) {
        tabBuildMuscle = view.findViewById(R.id.tab_build_muscle);
        tabBurnFat = view.findViewById(R.id.tab_burn_fat);
        tabIndicator = view.findViewById(R.id.tab_indicator);
        layoutPrograms = view.findViewById(R.id.layout_programs);
        layoutStats = view.findViewById(R.id.layout_stats);
        layoutProgramCards = view.findViewById(R.id.layout_program_cards);
        tvSectionTitle = view.findViewById(R.id.tv_section_title);
        imgFeatured = view.findViewById(R.id.img_featured);
        btnSettings = view.findViewById(R.id.btn_settings);
        
        tvCalories = view.findViewById(R.id.tv_calories);
        tvTime = view.findViewById(R.id.tv_time);
        tvDays = view.findViewById(R.id.tv_days);
        tvWorkouts = view.findViewById(R.id.tv_workouts);
        progressCircle = view.findViewById(R.id.progress_circle);
        
        // Settings button click
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), com.htdgym.app.activities.SettingsActivity.class);
            startActivity(intent);
        });
        
        // Featured image click - open YouTube video
        imgFeatured.setOnClickListener(v -> {
            String videoUrl = currentTab.equals("build_muscle") 
                    ? "https://youtu.be/YTOGoC2HEf8" 
                    : "https://youtu.be/GU5_SdDdc8o";
            openYouTubeVideo(videoUrl);
        });
    }

    private void setupTabs() {
        tabBuildMuscle.setOnClickListener(v -> switchTab("build_muscle"));
        tabBurnFat.setOnClickListener(v -> switchTab("burn_fat"));
        
        // Set initial tab indicator width
        tabBuildMuscle.post(() -> {
            ViewGroup.LayoutParams params = tabIndicator.getLayoutParams();
            params.width = tabBuildMuscle.getWidth();
            tabIndicator.setLayoutParams(params);
        });
        
        // Load initial featured image for build muscle tab
        com.htdgym.app.utils.YouTubeHelper.loadThumbnail(imgFeatured, "https://youtu.be/YTOGoC2HEf8", 
                com.htdgym.app.utils.YouTubeHelper.ThumbnailQuality.HIGH);
    }

    private void switchTab(String tab) {
        currentTab = tab;
        
        if (tab.equals("build_muscle")) {
            tabBuildMuscle.setTextColor(0xFF6FCF97);
            tabBurnFat.setTextColor(0xFF999999);
            
            // Animate indicator
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tabIndicator.getLayoutParams();
            params.setMarginStart((int) (16 * getResources().getDisplayMetrics().density));
            tabIndicator.setLayoutParams(params);
            
            // Load featured image for build muscle
            com.htdgym.app.utils.YouTubeHelper.loadThumbnail(imgFeatured, "https://youtu.be/YTOGoC2HEf8", 
                    com.htdgym.app.utils.YouTubeHelper.ThumbnailQuality.HIGH);
            
            loadBuildMusclePrograms();
        } else {
            tabBuildMuscle.setTextColor(0xFF999999);
            tabBurnFat.setTextColor(0xFF6FCF97);
            
            // Animate indicator
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tabIndicator.getLayoutParams();
            params.setMarginStart(screenWidth / 2);
            tabIndicator.setLayoutParams(params);
            
            // Load featured image for burn fat
            com.htdgym.app.utils.YouTubeHelper.loadThumbnail(imgFeatured, "https://youtu.be/GU5_SdDdc8o", 
                    com.htdgym.app.utils.YouTubeHelper.ThumbnailQuality.HIGH);
            
            loadBurnFatPrograms();
        }
    }

    private void loadProgramCards() {
        layoutProgramCards.removeAllViews();
        
        // Different video URLs for each program
        addProgramCard("CHƯƠNG TRÌNH CHO NGƯỜI MỚI", "30 ngày để có cơ bụng 6 múi", 
                "Ngày 1", "Ngày 30", 
                "https://youtu.be/Y8VflnViz78", // Abs workout
                "https://youtu.be/pSHjTRCQxIw", // Plank
                "https://youtu.be/nmwgirgXLYM"); // Mountain climbers
        
        addProgramCard("CHƯƠNG TRÌNH TRUNG CẤP", "60 ngày xây dựng cơ bắp toàn thân", 
                "Ngày 1", "Ngày 60", 
                "https://youtu.be/-R5sH2iG9Gw", // Push-ups
                "https://youtu.be/XtoO9YwNEqA", // Squats
                "https://youtu.be/JZQA08SlJnM"); // Burpees
        
        addProgramCard("CHƯƠNG TRÌNH NÂNG CAO", "90 ngày transformation hoàn toàn", 
                "Ngày 1", "Ngày 90", 
                "https://youtu.be/J0DnG1_S92I", // Diamond push-ups
                "https://youtu.be/A-cFYWvaHr0", // Jump squats
                "https://youtu.be/7SLbUk-qTTM"); // Superman
    }

    private void addProgramCard(String title, String subtitle, String startDay, String endDay, String startDayVideoUrl, String endDayVideoUrl, String cardImageUrl) {
        androidx.cardview.widget.CardView card = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                (int) (320 * getResources().getDisplayMetrics().density),
                (int) (280 * getResources().getDisplayMetrics().density));
        cardParams.setMargins(0, 0, (int) (12 * getResources().getDisplayMetrics().density), 0);
        card.setLayoutParams(cardParams);
        card.setRadius(16 * getResources().getDisplayMetrics().density);
        card.setCardElevation(2 * getResources().getDisplayMetrics().density);
        
        LinearLayout contentLayout = new LinearLayout(requireContext());
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        
        // Image area
        androidx.cardview.widget.CardView imageCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (180 * getResources().getDisplayMetrics().density));
        imageCard.setLayoutParams(imageParams);
        imageCard.setRadius(0);
        imageCard.setCardBackgroundColor(0xFFE8E8E8);
        
        // Add ImageView for thumbnail
        ImageView cardImage = new ImageView(requireContext());
        cardImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        cardImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        // Load thumbnail from YouTube
        com.htdgym.app.utils.YouTubeHelper.loadThumbnail(cardImage, cardImageUrl, 
                com.htdgym.app.utils.YouTubeHelper.ThumbnailQuality.HIGH);
        
        imageCard.addView(cardImage);
        
        // Day labels on image (overlay on top of image)
        android.widget.FrameLayout imageContainer = new android.widget.FrameLayout(requireContext());
        imageContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (180 * getResources().getDisplayMetrics().density)));
        imageContainer.addView(imageCard);
        
        LinearLayout dayLabelsLayout = new LinearLayout(requireContext());
        dayLabelsLayout.setOrientation(LinearLayout.HORIZONTAL);
        dayLabelsLayout.setLayoutParams(new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));
        dayLabelsLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density));
        
        // Start day label
        androidx.cardview.widget.CardView startDayCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams startDayParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        startDayCard.setLayoutParams(startDayParams);
        startDayCard.setRadius(12 * getResources().getDisplayMetrics().density);
        startDayCard.setCardBackgroundColor(0xFF6FCF97);
        startDayCard.setCardElevation(0);
        
        TextView tvStartDay = new TextView(requireContext());
        tvStartDay.setText(startDay);
        tvStartDay.setTextColor(0xFFFFFFFF);
        tvStartDay.setTextSize(12);
        tvStartDay.setTypeface(null, android.graphics.Typeface.BOLD);
        tvStartDay.setPadding(
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (6 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (6 * getResources().getDisplayMetrics().density));
        startDayCard.addView(tvStartDay);
        
        // Add click listener to start day card
        startDayCard.setOnClickListener(v -> openYouTubeVideo(startDayVideoUrl));
        
        // Spacer
        View spacer = new View(requireContext());
        LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                0, 0, 1f);
        spacer.setLayoutParams(spacerParams);
        
        // End day label
        androidx.cardview.widget.CardView endDayCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams endDayParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        endDayCard.setLayoutParams(endDayParams);
        endDayCard.setRadius(12 * getResources().getDisplayMetrics().density);
        endDayCard.setCardBackgroundColor(0xFF6FCF97);
        endDayCard.setCardElevation(0);
        
        TextView tvEndDay = new TextView(requireContext());
        tvEndDay.setText(endDay);
        tvEndDay.setTextColor(0xFFFFFFFF);
        tvEndDay.setTextSize(12);
        tvEndDay.setTypeface(null, android.graphics.Typeface.BOLD);
        tvEndDay.setPadding(
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (6 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (6 * getResources().getDisplayMetrics().density));
        endDayCard.addView(tvEndDay);
        
        // Add click listener to end day card
        endDayCard.setOnClickListener(v -> openYouTubeVideo(endDayVideoUrl));
        
        dayLabelsLayout.addView(startDayCard);
        dayLabelsLayout.addView(spacer);
        dayLabelsLayout.addView(endDayCard);
        imageContainer.addView(dayLabelsLayout);
        
        // Text area
        LinearLayout textLayout = new LinearLayout(requireContext());
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        textLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density));
        textLayout.setGravity(android.view.Gravity.CENTER);
        
        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(title);
        tvTitle.setTextColor(0xFFFFFFFF);
        tvTitle.setTextSize(14);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setGravity(android.view.Gravity.CENTER);
        
        TextView tvSubtitle = new TextView(requireContext());
        tvSubtitle.setText(subtitle);
        tvSubtitle.setTextColor(0xFFFFFFFF);
        tvSubtitle.setTextSize(11);
        tvSubtitle.setGravity(android.view.Gravity.CENTER);
        tvSubtitle.setPadding(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        
        textLayout.setBackgroundColor(0xFF1A1A1A);
        textLayout.addView(tvTitle);
        textLayout.addView(tvSubtitle);
        
        contentLayout.addView(imageContainer);
        contentLayout.addView(textLayout);
        card.addView(contentLayout);
        
        card.setOnClickListener(v -> {
            // Navigate to ProgramDetailActivity based on title
            String programType = "30";
            if (title.contains("TRUNG CẤP")) programType = "60";
            else if (title.contains("NÂNG CAO")) programType = "90";
            
            Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramDetailActivity.class);
            intent.putExtra("program_type", programType);
            startActivity(intent);
        });
        
        layoutProgramCards.addView(card);
    }

    private void loadBuildMusclePrograms() {
        layoutPrograms.removeAllViews();
        
        // Different videos for each muscle group
        addProgramItem("Tập Tay Trong 45 Ngày", 0, "https://youtu.be/hRJ6tR5-if0"); // Shoulder raises
        addProgramItem("Bộ Ngực Vạm Vỡ Trong 45 Ngày", 0, "https://youtu.be/-R5sH2iG9Gw"); // Push-ups
        addProgramItem("Đôi Chân Mạnh Mẽ Trong 45 Ngày", 0, "https://youtu.be/XtoO9YwNEqA"); // Squats
        addProgramItem("Bài Tập Lưng Và Vai", 0, "https://youtu.be/7SLbUk-qTTM"); // Superman
        addProgramItem("Tập Luyện Toàn Thân", 0, "https://youtu.be/JZQA08SlJnM"); // Burpees
    }

    private void loadBurnFatPrograms() {
        layoutPrograms.removeAllViews();
        
        // Different videos for fat burning programs
        addProgramItem("Giảm Mỡ Bụng - Dành Cho Người Mới", 0, "https://youtu.be/Y8VflnViz78"); // Crunches
        addProgramItem("Giảm Mỡ Toàn Thân - Trung Cấp", 0, "https://youtu.be/nmwgirgXLYM"); // Mountain climbers
        addProgramItem("Đốt Mỡ Nhanh - Nâng Cao", 0, "https://youtu.be/JZQA08SlJnM"); // Burpees
        addProgramItem("Cardio Giảm Cân Hiệu Quả", 0, "https://youtu.be/c4DAnQ6DtF8"); // Jumping jacks
        addProgramItem("HIIT Đốt Mỡ Cực Mạnh", 0, "https://youtu.be/A-cFYWvaHr0"); // Jump squats
    }

    private void addProgramItem(String title, int progress, String videoUrl) {
        androidx.cardview.widget.CardView programCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (12 * getResources().getDisplayMetrics().density));
        programCard.setLayoutParams(cardParams);
        programCard.setCardBackgroundColor(0xFFFFFFFF);
        programCard.setRadius(12 * getResources().getDisplayMetrics().density);
        programCard.setCardElevation(2 * getResources().getDisplayMetrics().density);
        
        LinearLayout contentLayout = new LinearLayout(requireContext());
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density));
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        // Thumbnail
        androidx.cardview.widget.CardView thumbnailCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams thumbnailParams = new LinearLayout.LayoutParams(
                (int) (80 * getResources().getDisplayMetrics().density),
                (int) (80 * getResources().getDisplayMetrics().density));
        thumbnailParams.setMargins(0, 0, (int) (12 * getResources().getDisplayMetrics().density), 0);
        thumbnailCard.setLayoutParams(thumbnailParams);
        thumbnailCard.setRadius(8 * getResources().getDisplayMetrics().density);
        thumbnailCard.setCardBackgroundColor(0xFFE8E8E8);
        
        ImageView thumbnail = new ImageView(requireContext());
        thumbnail.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        // Load YouTube thumbnail
        com.htdgym.app.utils.YouTubeHelper.loadThumbnail(thumbnail, videoUrl, 
                com.htdgym.app.utils.YouTubeHelper.ThumbnailQuality.MEDIUM);
        
        thumbnailCard.addView(thumbnail);
        
        // Info Layout
        LinearLayout infoLayout = new LinearLayout(requireContext());
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        infoLayout.setLayoutParams(infoParams);
        
        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(title);
        tvTitle.setTextColor(0xFF1A1A1A);
        tvTitle.setTextSize(15);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        
        ProgressBar progressBar = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (6 * getResources().getDisplayMetrics().density));
        progressParams.setMargins(0, (int) (8 * getResources().getDisplayMetrics().density), 0, 0);
        progressBar.setLayoutParams(progressParams);
        progressBar.setProgress(progress);
        progressBar.setMax(100);
        progressBar.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFF6FCF97));
        progressBar.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE0E0E0));
        
        infoLayout.addView(tvTitle);
        infoLayout.addView(progressBar);
        
        // Arrow
        TextView tvArrow = new TextView(requireContext());
        tvArrow.setText("›");
        tvArrow.setTextColor(0xFFC0C0C0);
        tvArrow.setTextSize(28);
        
        contentLayout.addView(thumbnailCard);
        contentLayout.addView(infoLayout);
        contentLayout.addView(tvArrow);
        
        programCard.addView(contentLayout);
        programCard.setOnClickListener(v -> {
            // Determine category based on title
            String category = "all";
            if (title.contains("Tay")) category = "shoulder";
            else if (title.contains("Ngực")) category = "chest";
            else if (title.contains("Chân")) category = "legs";
            else if (title.contains("Bụng")) category = "abs";
            else if (title.contains("Lưng")) category = "back";
            else if (title.contains("Giảm Mỡ") || title.contains("Đốt Mỡ") || title.contains("Cardio") || title.contains("HIIT")) {
                category = "abs"; // Fat burning exercises focus on abs/cardio
            }
            
            Intent intent = new Intent(requireContext(), WorkoutDetailActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        });
        
        layoutPrograms.addView(programCard);
    }
    
    private void openYouTubeVideo(String videoUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse(videoUrl));
            intent.setPackage("com.google.android.youtube"); // Try to open in YouTube app
            startActivity(intent);
        } catch (Exception e) {
            // If YouTube app not installed, open in browser
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(android.net.Uri.parse(videoUrl));
                startActivity(intent);
            } catch (Exception ex) {
                Toast.makeText(requireContext(), "Không thể mở video", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
