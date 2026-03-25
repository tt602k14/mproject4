package com.htdgym.app.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.htdgym.app.R;
import com.htdgym.app.activities.PremiumActivity;
import com.htdgym.app.activities.WorkoutDetailActivity;
import com.htdgym.app.utils.PremiumManager;
import com.htdgym.app.utils.YouTubeHelper;
import com.bumptech.glide.Glide;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private TextView tabBuildMuscle, tabBurnFat;
    private androidx.cardview.widget.CardView tabBuildMuscleCard, tabBurnFatCard;
    private LinearLayout layoutPrograms, layoutStats, layoutProgramCards;
    private TextView tvSectionTitle;
    private ImageView imgFeatured, btnSettings;
    private TextView tvCalories, tvTime, tvDays, tvWorkouts;
    private ProgressBar progressCircle;
    private CardView premiumBanner;

    // New fields
    private TextView tvUsername, tvGreeting;
    private CardView cardContinueWorkout;
    private TextView tvLastWorkoutName, tvLastWorkoutInfo;

    private String currentTab = "build_muscle";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initViews(view);
        setupTabs();
        setupPremiumBanner(view);
        loadProgramCards();
        loadBuildMusclePrograms();
        loadWorkoutStats();
        loadUserInfo();
        loadContinueWorkout();
        
        // Pre-download important thumbnails in background
        com.htdgym.app.utils.ThumbnailManager.preDownloadImportantThumbnails(requireContext());
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Reload stats when returning to home screen
        loadWorkoutStats();
        loadContinueWorkout();
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
        tabBuildMuscleCard = view.findViewById(R.id.tab_build_muscle_card);
        tabBurnFatCard = view.findViewById(R.id.tab_burn_fat_card);
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

        // New views
        tvUsername = view.findViewById(R.id.tv_username);
        tvGreeting = view.findViewById(R.id.tv_greeting);
        cardContinueWorkout = view.findViewById(R.id.card_continue_workout);
        tvLastWorkoutName = view.findViewById(R.id.tv_last_workout_name);
        tvLastWorkoutInfo = view.findViewById(R.id.tv_last_workout_info);
        
        // Settings button click
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), com.htdgym.app.activities.SettingsActivity.class);
            startActivity(intent);
        });
        
        // Smart Workout Planner button click
        LinearLayout btnSmartPlanner = view.findViewById(R.id.btn_smart_planner);
        btnSmartPlanner.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), com.htdgym.app.activities.SmartWorkoutPlannerActivity.class);
            startActivity(intent);
        });
        
        // Featured image click - open YouTube video with better URLs
        imgFeatured.setOnClickListener(v -> {
            String videoUrl = currentTab.equals("build_muscle") 
                    ? "https://youtu.be/IODxDxX7oi4" // Full body muscle building workout
                    : "https://youtu.be/1fbU_MkV7NE"; // Fat burning HIIT workout
            openYouTubeVideo(videoUrl);
        });
    }

    private void setupPremiumBanner(View view) {
        premiumBanner = view.findViewById(R.id.premium_banner);
        
        if (premiumBanner != null) {
            boolean isPremium = PremiumManager.isPremiumUser(requireContext());
            
            if (isPremium) {
                premiumBanner.setVisibility(View.GONE);
            } else {
                premiumBanner.setVisibility(View.VISIBLE);
                premiumBanner.setOnClickListener(v -> {
                    Intent intent = new Intent(requireContext(), PremiumActivity.class);
                    startActivity(intent);
                });
            }
        }
    }

    private void setupTabs() {
        tabBuildMuscle.setOnClickListener(v -> switchTab("build_muscle"));
        tabBurnFat.setOnClickListener(v -> switchTab("burn_fat"));
        
        // Load initial featured image for build muscle tab
        loadThumbnailForProgram(imgFeatured, "https://youtu.be/YTOGoC2HEf8", "Featured Image");
    }

    private void switchTab(String tab) {
        currentTab = tab;
        
        if (tab.equals("build_muscle")) {
            // Update tab appearance
            tabBuildMuscle.setTextColor(0xFFFFFFFF);
            tabBurnFat.setTextColor(0xFF999999);
            tabBuildMuscleCard.setCardBackgroundColor(0xFF6FCF97);
            tabBurnFatCard.setCardBackgroundColor(0x00000000); // Transparent
            
            // Load featured image for build muscle with specific workout video
            loadThumbnailForProgram(imgFeatured, "https://youtu.be/YTOGoC2HEf8", "Build Muscle");
            
            loadBuildMusclePrograms();
        } else {
            // Update tab appearance
            tabBuildMuscle.setTextColor(0xFF999999);
            tabBurnFat.setTextColor(0xFFFFFFFF);
            tabBuildMuscleCard.setCardBackgroundColor(0x00000000); // Transparent
            tabBurnFatCard.setCardBackgroundColor(0xFF6FCF97);
            
            // Load featured image for burn fat with specific HIIT video
            loadThumbnailForProgram(imgFeatured, "https://youtu.be/GU5_SdDdc8o", "Burn Fat");
            
            loadBurnFatPrograms();
        }
    }

    private void loadProgramCards() {
        layoutProgramCards.removeAllViews();
        
        // Enhanced program cards with specific video URLs for each program
        addProgramCard("🏃‍♂️ CHƯƠNG TRÌNH CHO NGƯỜI MỚI", "30 ngày để có cơ bụng 6 múi", 
                "Ngày 1", "Ngày 30", 
                "https://youtu.be/1919eTCoESo", // Day 1 - Basic abs workout
                "https://youtu.be/Y8VflnViz78", // Day 30 - Advanced abs
                "https://youtu.be/1919eTCoESo"); // Card image
        
        addProgramCard("💪 CHƯƠNG TRÌNH TRUNG CẤP", "60 ngày xây dựng cơ bắp toàn thân", 
                "Ngày 1", "Ngày 60", 
                "https://youtu.be/YTOGoC2HEf8", // Day 1 - Full body beginner
                "https://youtu.be/HSoHeSjvIdY", // Day 60 - Advanced full body
                "https://youtu.be/YTOGoC2HEf8"); // Card image
        
        addProgramCard("🔥 CHƯƠNG TRÌNH NÂNG CAO", "90 ngày transformation hoàn toàn", 
                "Ngày 1", "Ngày 90", 
                "https://youtu.be/GU5_SdDdc8o", // Day 1 - HIIT beginner
                "https://youtu.be/ML6XLzLH12A", // Day 90 - Advanced HIIT
                "https://youtu.be/GU5_SdDdc8o"); // Card image
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
        loadThumbnailForProgram(cardImage, cardImageUrl, "Card Image");
        
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
            if (title.contains("TRUNG CẤP") || title.contains("60")) programType = "60";
            else if (title.contains("NÂNG CAO") || title.contains("90")) programType = "90";
            
            // Check premium access for 60 and 90 day programs
            if (!programType.equals("30") && !PremiumManager.isPremiumUser(requireContext())) {
                PremiumManager.showPremiumDialog(requireContext(), "Chương trình " + programType + " ngày");
                return;
            }
            
            Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramDetailActivity.class);
            intent.putExtra("program_type", programType);
            startActivity(intent);
        });
        
        layoutProgramCards.addView(card);
    }

    private void loadBuildMusclePrograms() {
        layoutPrograms.removeAllViews();

        // Add special muscle building programs
        addSpecialProgramItem("💪 Chương Trình Xây Dựng Cơ Bắp", "Tăng cơ toàn diện với bài tập compound", 
                "https://youtu.be/rT7DgCr-3pg", "muscle_builder");
        addSpecialProgramItem("🏋️ Tăng Cơ Thân Trên", "Ngực, lưng, vai, tay chuyên sâu", 
                "https://youtu.be/rT7DgCr-3pg", "upper_body");
        addSpecialProgramItem("🦵 Tăng Cơ Thân Dưới", "Chân, mông, bắp chân mạnh mẽ", 
                "https://youtu.be/ultWZbUMPL8", "lower_body");
        addSpecialProgramItem("🎯 Progressive Overload", "Tăng tải dần cho tăng cơ tối ưu", 
                "https://youtu.be/op9kVnSso6Q", "progressive_overload");

        addProgramItem("🏋️ Bench Press - Tập Ngực",        "https://youtu.be/rT7DgCr-3pg", "bench_press");
        addProgramItem("💀 Deadlift - Kéo Tạ Đất",          "https://youtu.be/op9kVnSso6Q", "deadlift");
        addProgramItem("🦵 Squat - Vua Của Các Bài Tập",    "https://youtu.be/ultWZbUMPL8", "squat_strength");
        addProgramItem("🔥 Pull-Up - Xà Đơn Tăng Cơ",      "https://youtu.be/eGo4IYlbE5g", "pull_up");
        addProgramItem("💥 Overhead Press - Đẩy Vai",       "https://youtu.be/2yjwXTZQDDI", "overhead_press");
        addProgramItem("🔥 Barbell Row - Tập Lưng Dày",     "https://youtu.be/G8l_8chR5BE", "barbell_row");
        addProgramItem("⚡ Dips - Tăng Cơ Tay Sau & Ngực", "https://youtu.be/2z8JmcrW-As", "dips");
    }

    private void loadBurnFatPrograms() {
        layoutPrograms.removeAllViews();

        // Add special HIIT fat burning programs
        addSpecialProgramItem("🔥 HIIT Đốt Mỡ - Người Mới", "4 tuần HIIT cho người mới bắt đầu", 
                "https://youtu.be/ml6cT4AZdqI", "hiit_beginner");
        addSpecialProgramItem("🔥 HIIT Đốt Mỡ - Nâng Cao", "Thử thách HIIT cường độ cao", 
                "https://youtu.be/JZQA08SlJnM", "hiit_advanced");
        addSpecialProgramItem("🏃 Cardio Đốt Mỡ Toàn Diện", "Kết hợp HIIT và Cardio hiệu quả", 
                "https://youtu.be/8opcQdC-V-U", "fat_burning");
        addSpecialProgramItem("🎯 Đốt Mỡ Bụng Chuyên Sâu", "Tập trung vào vùng core", 
                "https://youtu.be/nmwgirgXLYM", "core_strength");

        addProgramItem("🔥 HIIT Toàn Thân - Đốt Mỡ Nhanh",  "https://youtu.be/ml6cT4AZdqI", "hiit_fullbody");
        addProgramItem("🏃 Cardio Chạy Bộ Tại Chỗ",          "https://youtu.be/8opcQdC-V-U", "cardio_running");
        addProgramItem("⚡ Tabata - Đốt Calo Tối Đa",         "https://youtu.be/20Khkl95_qA", "tabata_burn");
        addProgramItem("💨 Jumping Jacks & Burpees",          "https://youtu.be/JZQA08SlJnM", "jumping_burpees");
        addProgramItem("🎯 Jump Rope - Nhảy Dây Đốt Mỡ",     "https://youtu.be/FJmRQ5iTXKE", "jump_rope");
        addProgramItem("🌀 Mountain Climber Circuit",         "https://youtu.be/nmwgirgXLYM", "mountain_climber");
        addProgramItem("🚀 Squat Jump & Lunge Circuit",       "https://youtu.be/A-cFYWvaHr0", "squat_lunge_circuit");
    }

    private void addSpecialProgramItem(String title, String description, String videoUrl, String programType) {
        androidx.cardview.widget.CardView programCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (12 * getResources().getDisplayMetrics().density));
        programCard.setLayoutParams(cardParams);
        programCard.setCardBackgroundColor(0xFFFFFFFF);
        programCard.setRadius(12 * getResources().getDisplayMetrics().density);
        programCard.setCardElevation(4 * getResources().getDisplayMetrics().density);
        
        // Add special gradient border for special programs
        android.graphics.drawable.GradientDrawable border = new android.graphics.drawable.GradientDrawable();
        border.setColor(0xFFFFFFFF);
        border.setStroke((int) (2 * getResources().getDisplayMetrics().density), 0xFF6FCF97);
        border.setCornerRadius(12 * getResources().getDisplayMetrics().density);
        programCard.setBackground(border);
        
        LinearLayout contentLayout = new LinearLayout(requireContext());
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density));
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        // Thumbnail with special indicator
        android.widget.FrameLayout thumbnailContainer = new android.widget.FrameLayout(requireContext());
        LinearLayout.LayoutParams thumbnailContainerParams = new LinearLayout.LayoutParams(
                (int) (80 * getResources().getDisplayMetrics().density),
                (int) (80 * getResources().getDisplayMetrics().density));
        thumbnailContainerParams.setMargins(0, 0, (int) (12 * getResources().getDisplayMetrics().density), 0);
        thumbnailContainer.setLayoutParams(thumbnailContainerParams);
        
        androidx.cardview.widget.CardView thumbnailCard = new androidx.cardview.widget.CardView(requireContext());
        thumbnailCard.setLayoutParams(new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));
        thumbnailCard.setRadius(8 * getResources().getDisplayMetrics().density);
        thumbnailCard.setCardBackgroundColor(0xFFE8E8E8);
        
        ImageView thumbnail = new ImageView(requireContext());
        thumbnail.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        // Load YouTube thumbnail
        loadThumbnailForProgram(thumbnail, videoUrl, title);
        
        thumbnailCard.addView(thumbnail);
        
        // Special program indicator
        TextView specialIndicator = new TextView(requireContext());
        android.widget.FrameLayout.LayoutParams indicatorParams = new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
        indicatorParams.gravity = android.view.Gravity.TOP | android.view.Gravity.END;
        indicatorParams.setMargins(0, (int) (4 * getResources().getDisplayMetrics().density), 
                (int) (4 * getResources().getDisplayMetrics().density), 0);
        specialIndicator.setLayoutParams(indicatorParams);
        specialIndicator.setText("⭐");
        specialIndicator.setTextSize(16);
        
        thumbnailContainer.addView(thumbnailCard);
        thumbnailContainer.addView(specialIndicator);
        
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
        
        TextView tvDescription = new TextView(requireContext());
        tvDescription.setText(description);
        tvDescription.setTextColor(0xFF666666);
        tvDescription.setTextSize(12);
        tvDescription.setPadding(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        
        TextView tvSpecialLabel = new TextView(requireContext());
        tvSpecialLabel.setText("🎯 Chương trình chuyên sâu");
        tvSpecialLabel.setTextColor(0xFF6FCF97);
        tvSpecialLabel.setTextSize(11);
        tvSpecialLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        tvSpecialLabel.setPadding(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        
        infoLayout.addView(tvTitle);
        infoLayout.addView(tvDescription);
        infoLayout.addView(tvSpecialLabel);
        
        // Arrow
        TextView tvArrow = new TextView(requireContext());
        tvArrow.setText("›");
        tvArrow.setTextColor(0xFF6FCF97);
        tvArrow.setTextSize(28);
        tvArrow.setTypeface(null, android.graphics.Typeface.BOLD);
        
        contentLayout.addView(thumbnailContainer);
        contentLayout.addView(infoLayout);
        contentLayout.addView(tvArrow);
        
        programCard.addView(contentLayout);
        programCard.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), com.htdgym.app.activities.WorkoutProgramActivity.class);
            intent.putExtra("program_type", programType);
            intent.putExtra("program_title", title);
            startActivity(intent);
        });
        
        layoutPrograms.addView(programCard);
    }

    private void addProgramItem(String title, String videoUrl, String programType) {
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
        loadThumbnailForProgram(thumbnail, videoUrl, title);
        
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
        progressBar.setProgress(0);
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
            Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramExerciseListActivity.class);
            intent.putExtra("program_type", programType);
            intent.putExtra("program_title", title);
            startActivity(intent);
        });
        
        layoutPrograms.addView(programCard);
    }
    
    private void addPremiumProgramItem(String title, String videoUrl, String programType) {
        androidx.cardview.widget.CardView programCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (12 * getResources().getDisplayMetrics().density));
        programCard.setLayoutParams(cardParams);
        programCard.setCardBackgroundColor(0xFFFFFFFF);
        programCard.setRadius(12 * getResources().getDisplayMetrics().density);
        programCard.setCardElevation(2 * getResources().getDisplayMetrics().density);
        
        // Add premium border (using background instead of stroke for compatibility)
        android.graphics.drawable.GradientDrawable border = new android.graphics.drawable.GradientDrawable();
        border.setColor(0xFFFFFFFF);
        border.setStroke((int) (2 * getResources().getDisplayMetrics().density), 0xFFFFD700);
        border.setCornerRadius(12 * getResources().getDisplayMetrics().density);
        programCard.setBackground(border);
        
        LinearLayout contentLayout = new LinearLayout(requireContext());
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density));
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        // Thumbnail with premium overlay
        android.widget.FrameLayout thumbnailContainer = new android.widget.FrameLayout(requireContext());
        LinearLayout.LayoutParams thumbnailContainerParams = new LinearLayout.LayoutParams(
                (int) (80 * getResources().getDisplayMetrics().density),
                (int) (80 * getResources().getDisplayMetrics().density));
        thumbnailContainerParams.setMargins(0, 0, (int) (12 * getResources().getDisplayMetrics().density), 0);
        thumbnailContainer.setLayoutParams(thumbnailContainerParams);
        
        androidx.cardview.widget.CardView thumbnailCard = new androidx.cardview.widget.CardView(requireContext());
        thumbnailCard.setLayoutParams(new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));
        thumbnailCard.setRadius(8 * getResources().getDisplayMetrics().density);
        thumbnailCard.setCardBackgroundColor(0xFFE8E8E8);
        
        ImageView thumbnail = new ImageView(requireContext());
        thumbnail.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        // Load YouTube thumbnail
        loadThumbnailForProgram(thumbnail, videoUrl, title);
        
        thumbnailCard.addView(thumbnail);
        
        // Premium crown overlay
        ImageView crownIcon = new ImageView(requireContext());
        android.widget.FrameLayout.LayoutParams crownParams = new android.widget.FrameLayout.LayoutParams(
                (int) (24 * getResources().getDisplayMetrics().density),
                (int) (24 * getResources().getDisplayMetrics().density));
        crownParams.gravity = android.view.Gravity.TOP | android.view.Gravity.END;
        crownParams.setMargins(0, (int) (4 * getResources().getDisplayMetrics().density), 
                (int) (4 * getResources().getDisplayMetrics().density), 0);
        crownIcon.setLayoutParams(crownParams);
        crownIcon.setImageResource(R.drawable.ic_crown);
        crownIcon.setColorFilter(0xFFFFD700);
        
        thumbnailContainer.addView(thumbnailCard);
        thumbnailContainer.addView(crownIcon);
        
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
        
        TextView tvPremiumLabel = new TextView(requireContext());
        tvPremiumLabel.setText("👑 Premium");
        tvPremiumLabel.setTextColor(0xFFFFD700);
        tvPremiumLabel.setTextSize(12);
        tvPremiumLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        tvPremiumLabel.setPadding(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        
        infoLayout.addView(tvTitle);
        infoLayout.addView(tvPremiumLabel);
        
        // Lock icon or arrow
        boolean isPremium = PremiumManager.isPremiumUser(requireContext());
        ImageView iconRight = new ImageView(requireContext());
        iconRight.setLayoutParams(new LinearLayout.LayoutParams(
                (int) (24 * getResources().getDisplayMetrics().density),
                (int) (24 * getResources().getDisplayMetrics().density)));
        
        if (isPremium) {
            iconRight.setImageResource(R.drawable.ic_arrow_forward);
            iconRight.setColorFilter(0xFFC0C0C0);
        } else {
            iconRight.setImageResource(R.drawable.ic_lock);
            iconRight.setColorFilter(0xFFFFD700);
        }
        
        contentLayout.addView(thumbnailContainer);
        contentLayout.addView(infoLayout);
        contentLayout.addView(iconRight);
        
        programCard.addView(contentLayout);
        programCard.setOnClickListener(v -> {
            if (!PremiumManager.isPremiumUser(requireContext())) {
                PremiumManager.showPremiumDialog(requireContext(), title);
                return;
            }

            Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramExerciseListActivity.class);
            intent.putExtra("program_type", programType);
            intent.putExtra("program_title", title);
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
    
    private String getProgramType(String title, String category) {
        // Map program titles to specific program types
        if (title.contains("Giảm Mỡ Bụng - Người Mới")) {
            return "abs_beginner";
        } else if (title.contains("Giảm Mỡ Toàn Thân - Trung Cấp")) {
            return "fullbody_intermediate";
        } else if (title.contains("Đốt Mỡ Nhanh - Nâng Cao")) {
            return "hiit_advanced";
        } else if (title.contains("Cardio Giảm Cân Hiệu Quả")) {
            return "cardio";
        } else if (title.contains("Tabata 4 Phút Đốt Mỡ")) {
            return "tabata";
        } else if (title.contains("Tập Tay Trong 45 Ngày")) {
            return "shoulder_45";
        } else if (title.contains("Bộ Ngực Vạm Vỡ Trong 45 Ngày")) {
            return "chest_45";
        } else if (title.contains("Đôi Chân Mạnh Mẽ Trong 45 Ngày")) {
            return "legs_45";
        } else if (title.contains("Bài Tập Lưng Và Vai")) {
            return "back_shoulder";
        } else if (title.contains("Cơ Bụng 6 Múi Hoàn Hảo")) {
            return "abs_6pack";
        } else if (title.contains("HIIT Đốt Mỡ Cực Mạnh")) {
            return "hiit_advanced";
        } else if (title.contains("Xây Dựng Cơ Bắp Mạnh Mẽ")) {
            return "muscle_building";
        } else {
            return category; // fallback to category
        }
    }

    private void loadUserInfo() {
        android.content.SharedPreferences prefs = requireContext()
                .getSharedPreferences("HTDGymPrefs", android.content.Context.MODE_PRIVATE);
        String fullName = prefs.getString("full_name", "");
        String email = prefs.getString("email", "");

        String displayName;
        if (fullName != null && !fullName.isEmpty()) {
            displayName = fullName;
        } else if (email != null && email.contains("@")) {
            displayName = email.substring(0, email.indexOf("@"));
        } else {
            displayName = "HTD GYM";
        }

        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour < 12) {
            greeting = "Chào buổi sáng! ☀️";
        } else if (hour < 18) {
            greeting = "Chào buổi chiều! 👋";
        } else {
            greeting = "Chào buổi tối! 🌙";
        }

        if (tvGreeting != null) tvGreeting.setText(greeting);
        if (tvUsername != null) tvUsername.setText(displayName);
    }

    private void loadContinueWorkout() {
        new Thread(() -> {
            try {
                android.content.SharedPreferences prefs = requireContext()
                        .getSharedPreferences("HTDGymPrefs", android.content.Context.MODE_PRIVATE);
                String userId = prefs.getString("userId", "guest");

                com.htdgym.app.database.GymDatabase db =
                        com.htdgym.app.database.GymDatabase.getInstance(requireContext());
                com.htdgym.app.models.WorkoutStats stats =
                        db.workoutStatsDao().getStatsByUserId(userId);

                if (stats == null || stats.getTotalWorkouts() == 0 || stats.getLastWorkoutDate() == 0) {
                    requireActivity().runOnUiThread(() -> {
                        if (cardContinueWorkout != null)
                            cardContinueWorkout.setVisibility(View.GONE);
                    });
                    return;
                }

                long lastDate = stats.getLastWorkoutDate();
                long diffMs = System.currentTimeMillis() - lastDate;
                long diffHours = diffMs / (1000 * 60 * 60);
                long diffDays = diffHours / 24;

                String timeAgo;
                if (diffHours < 1) {
                    timeAgo = "Vừa xong";
                } else if (diffHours < 24) {
                    timeAgo = diffHours + " giờ trước";
                } else if (diffDays == 1) {
                    timeAgo = "Hôm qua";
                } else {
                    timeAgo = diffDays + " ngày trước";
                }

                final String infoText = timeAgo + "  •  " + stats.getTotalWorkouts() + " buổi tập";
                final String lastWorkoutName = prefs.getString("last_workout_name", "Bài tập gần nhất");

                requireActivity().runOnUiThread(() -> {
                    if (cardContinueWorkout != null) {
                        cardContinueWorkout.setVisibility(View.VISIBLE);
                        tvLastWorkoutName.setText(lastWorkoutName);
                        tvLastWorkoutInfo.setText(infoText);
                        cardContinueWorkout.setOnClickListener(v -> {
                            Intent intent = new Intent(requireContext(),
                                    com.htdgym.app.activities.WorkoutLibraryActivity.class);
                            startActivity(intent);
                        });
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * Helper method để load thumbnail cho program items
     */
    private void loadThumbnailForProgram(ImageView thumbnail, String videoUrl, String title) {
        // Use the new ThumbnailManager for better thumbnail loading
        com.htdgym.app.utils.ThumbnailManager.loadThumbnail(requireContext(), thumbnail, title, videoUrl);
    }
}
