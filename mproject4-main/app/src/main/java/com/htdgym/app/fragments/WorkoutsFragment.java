package com.htdgym.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.activities.WorkoutDetailActivity;
import com.htdgym.app.activities.ExerciseVideoActivity;
import com.htdgym.app.activities.WorkoutSessionActivity;
import com.htdgym.app.adapters.ExerciseAdapter;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.utils.YouTubeHelper;
import com.htdgym.app.utils.PremiumManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkoutsFragment extends Fragment implements ExerciseAdapter.OnExerciseClickListener {

    private CardView cardAll, cardChest, cardShoulder, cardLegs, cardAbs, cardBack;
    private LinearLayout layoutWorkoutList, layoutExerciseList;
    private TextView tvTotalExercises, tvFavorites, tvCompleted, tvCalories;
    private TextView btnShowFavorites, btnClearSearch;
    private EditText etSearch;
    
    // Filter chips
    private TextView chipCategoryAll, chipCategoryChest, chipCategoryShoulder, 
                     chipCategoryLegs, chipCategoryAbs, chipCategoryBack;
    private TextView chipDifficultyAll, chipDifficultyBeginner, 
                     chipDifficultyIntermediate, chipDifficultyAdvanced;
    private TextView chipDurationAll, chipDurationShort, chipDurationMedium, chipDurationLong;
    private TextView chipEquipmentAll, chipEquipmentNone, chipEquipmentDumbbell, chipEquipmentResistance;
    
    // Advanced filters
    private String currentCategory = "all";
    private String currentDifficulty = "all";
    private String currentDuration = "all";
    private String currentEquipment = "all";
    private String currentSearch = "";
    private boolean showingFavorites = false;
    private boolean showingCompleted = false;
    
    // Database and adapter
    private GymDatabase database;
    private ExecutorService executor;
    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> allExercises = new ArrayList<>();
    private List<Exercise> filteredExercises = new ArrayList<>();
    private PremiumManager premiumManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);
        
        database = GymDatabase.getInstance(requireContext());
        executor = Executors.newSingleThreadExecutor();
        premiumManager = PremiumManager.getInstance(requireContext());
        
        initViews(view);
        setupClickListeners();
        setupSearchAndFilters();
        setupExerciseList();
        loadStatistics();
        loadExercises();
        loadPopularWorkouts();
        
        // Debug YouTube thumbnail URLs
        YouTubeHelper.testVideoIdExtraction();
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh exercises when returning to fragment (in case premium status changed)
        loadExercises();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }

    private void initViews(View view) {
        // Category cards
        cardAll = view.findViewById(R.id.card_all);
        cardChest = view.findViewById(R.id.card_chest);
        cardShoulder = view.findViewById(R.id.card_shoulder);
        cardLegs = view.findViewById(R.id.card_legs);
        cardAbs = view.findViewById(R.id.card_abs);
        cardBack = view.findViewById(R.id.card_back);
        
        // Lists
        layoutWorkoutList = view.findViewById(R.id.layout_workout_list);
        layoutExerciseList = view.findViewById(R.id.layout_exercise_list);
        
        // Statistics
        tvTotalExercises = view.findViewById(R.id.tv_total_exercises);
        tvFavorites = view.findViewById(R.id.tv_favorites);
        tvCompleted = view.findViewById(R.id.tv_completed);
        tvCalories = view.findViewById(R.id.tv_calories);
        
        // Search and buttons
        etSearch = view.findViewById(R.id.et_search);
        btnClearSearch = view.findViewById(R.id.btn_clear_search);
        btnShowFavorites = view.findViewById(R.id.btn_show_favorites);
        
        // Category filter chips
        chipCategoryAll = view.findViewById(R.id.chip_category_all);
        chipCategoryChest = view.findViewById(R.id.chip_category_chest);
        chipCategoryShoulder = view.findViewById(R.id.chip_category_shoulder);
        chipCategoryLegs = view.findViewById(R.id.chip_category_legs);
        chipCategoryAbs = view.findViewById(R.id.chip_category_abs);
        chipCategoryBack = view.findViewById(R.id.chip_category_back);
        
        // Difficulty filter chips
        chipDifficultyAll = view.findViewById(R.id.chip_difficulty_all);
        chipDifficultyBeginner = view.findViewById(R.id.chip_difficulty_beginner);
        chipDifficultyIntermediate = view.findViewById(R.id.chip_difficulty_intermediate);
        chipDifficultyAdvanced = view.findViewById(R.id.chip_difficulty_advanced);
    }

    private void setupClickListeners() {
        cardAll.setOnClickListener(v -> openWorkoutDetail("all"));
        cardChest.setOnClickListener(v -> openWorkoutDetail("chest"));
        cardShoulder.setOnClickListener(v -> openWorkoutDetail("shoulder"));
        cardLegs.setOnClickListener(v -> openWorkoutDetail("legs"));
        cardAbs.setOnClickListener(v -> openWorkoutDetail("abs"));
        cardBack.setOnClickListener(v -> openWorkoutDetail("back"));
        
        btnShowFavorites.setOnClickListener(v -> toggleFavorites());
        btnClearSearch.setOnClickListener(v -> clearSearch());
    }
    
    private void setupSearchAndFilters() {
        // Search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearch = s.toString().trim();
                btnClearSearch.setVisibility(currentSearch.isEmpty() ? View.GONE : View.VISIBLE);
                filterExercises();
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Category filter chips
        chipCategoryAll.setOnClickListener(v -> selectCategoryFilter("all"));
        chipCategoryChest.setOnClickListener(v -> selectCategoryFilter("chest"));
        chipCategoryShoulder.setOnClickListener(v -> selectCategoryFilter("shoulder"));
        chipCategoryLegs.setOnClickListener(v -> selectCategoryFilter("legs"));
        chipCategoryAbs.setOnClickListener(v -> selectCategoryFilter("abs"));
        chipCategoryBack.setOnClickListener(v -> selectCategoryFilter("back"));
        
        // Difficulty filter chips
        chipDifficultyAll.setOnClickListener(v -> selectDifficultyFilter("all"));
        chipDifficultyBeginner.setOnClickListener(v -> selectDifficultyFilter("beginner"));
        chipDifficultyIntermediate.setOnClickListener(v -> selectDifficultyFilter("intermediate"));
        chipDifficultyAdvanced.setOnClickListener(v -> selectDifficultyFilter("advanced"));
    }
    
    private void setupExerciseList() {
        exerciseAdapter = new ExerciseAdapter(requireContext(), filteredExercises);
        exerciseAdapter.setOnExerciseClickListener(this);
        
        // Create RecyclerView programmatically
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(exerciseAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        
        layoutExerciseList.addView(recyclerView);
    }

    private void openWorkoutDetail(String category) {
        String programType;
        String programTitle;
        switch (category) {
            case "chest":    programType = "bench_press";     programTitle = "💪 Bài Tập Ngực";    break;
            case "shoulder": programType = "overhead_press";  programTitle = "💥 Bài Tập Vai";     break;
            case "legs":     programType = "squat_strength";  programTitle = "🦵 Bài Tập Chân";    break;
            case "abs":      programType = "abs_beginner";    programTitle = "🔥 Bài Tập Bụng";    break;
            case "back":     programType = "barbell_row";     programTitle = "🏋️ Bài Tập Lưng";   break;
            default:         programType = "muscle_building"; programTitle = "🏋️ Tất Cả Bài Tập"; break;
        }
        Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramExerciseListActivity.class);
        intent.putExtra("program_type", programType);
        intent.putExtra("program_title", programTitle);
        startActivity(intent);
    }

    private void loadPopularWorkouts() {
        // Chương trình 30-60-90 ngày với kiểm tra Premium
        addWorkoutItem("Chương Trình Cho Người Mới - 30 Ngày", "30 ngày để có cơ bụng 6 múi • 50 bài tập • MIỄN PHÍ", "https://youtu.be/Y8VflnViz78", "30", false);
        addWorkoutItem("Chương Trình Trung Cấp - 60 Ngày", "60 ngày xây dựng cơ bắp toàn thân • 15 bài tập • PREMIUM", "https://youtu.be/-R5sH2iG9Gw", "60", true);
        addWorkoutItem("Chương Trình Nâng Cao - 90 Ngày", "90 ngày transformation hoàn toàn • 8 bài tập • PREMIUM", "https://youtu.be/JZQA08SlJnM", "90", true);
        
        // === CHƯƠNG TRÌNH 30 NGÀY ===
        // Ngày 1-5: Khởi động cơ bản
        addWorkoutItem("Jumping Jacks - Khởi động toàn thân", "Ngày 1-5 • Khởi động cơ bản • 30 giây", "https://youtu.be/c4DAnQ6DtF8");
        addWorkoutItem("Gập bụng cơ bản", "Ngày 1-5 • Tập cơ bụng trên • 15 lần", "https://youtu.be/Xyd_fa5zoEU");
        addWorkoutItem("Plank - Tăng cường core", "Ngày 1-5 • Ổn định core • 30 giây", "https://youtu.be/ASdvN_XEl_c");
        addWorkoutItem("Mountain Climbers", "Ngày 1-5 • Cardio và core • 20 lần", "https://youtu.be/kLh-uczlPLg");
        addWorkoutItem("Leg Raises", "Ngày 1-5 • Tập cơ bụng dưới • 12 lần", "https://youtu.be/Xyd_fa5zoEU");
        
        // Ngày 6-10: Tăng cường sức bền
        addWorkoutItem("Burpees - Toàn thân", "Ngày 6-10 • Đốt mỡ toàn thân • 8 lần", "https://youtu.be/JZQA08SlJnM");
        addWorkoutItem("Bicycle Crunches", "Ngày 6-10 • Gập bụng xe đạp • 20 lần", "https://youtu.be/Xyd_fa5zoEU");
        addWorkoutItem("Side Plank", "Ngày 6-10 • Plank nghiêng • 20s/bên", "https://youtu.be/ASdvN_XEl_c");
        addWorkoutItem("Russian Twists", "Ngày 6-10 • Xoay thân • 15 lần", "https://youtu.be/Xyd_fa5zoEU");
        addWorkoutItem("High Knees", "Ngày 6-10 • Cardio cường độ cao • 30 giây", "https://youtu.be/c4DAnQ6DtF8");
        addWorkoutItem("Flutter Kicks", "Ngày 6-10 • Tập cơ bụng dưới • 20 lần", "https://youtu.be/Xyd_fa5zoEU");
        
        // Ngày 11-15: Gập bụng cơ bản
        addWorkoutItem("Crunches", "Ngày 11-15 • Gập bụng cơ bản • 18 lần", "https://youtu.be/Xyd_fa5zoEU");
        addWorkoutItem("Reverse Crunches", "Ngày 11-15 • Gập bụng ngược • 15 lần", "https://www.youtube.com/watch?v=hyv14e2QDq0");
        addWorkoutItem("Toe Touches", "Ngày 11-15 • Chạm ngón chân • 15 lần", "https://www.youtube.com/watch?v=4hmQA3snN3Q");
        addWorkoutItem("V-Ups", "Ngày 11-15 • Gập bụng chữ V • 12 lần", "https://www.youtube.com/watch?v=iP2fjvG0g3w");
        addWorkoutItem("Dead Bug", "Ngày 11-15 • Ổn định core • 10 lần/bên", "https://www.youtube.com/watch?v=g_BYB0R-4Ws");
        addWorkoutItem("Heel Touches", "Ngày 11-15 • Chạm gót chân • 20 lần", "https://www.youtube.com/watch?v=9bR-EL9yUeA");
        addWorkoutItem("Sit-ups", "Ngày 11-15 • Ngồi dậy cơ bản • 15 lần", "https://www.youtube.com/watch?v=1fbU_MkV7NE");
        
        // Ngày 16-20: Plank và biến thể
        addWorkoutItem("Plank 45 giây", "Ngày 16-20 • Plank cơ bản • 45 giây", "https://youtu.be/ASdvN_XEl_c");
        addWorkoutItem("Plank to Downward Dog", "Ngày 16-20 • Plank động • 12 lần", "https://www.youtube.com/watch?v=F1RzP5g0yJk");
        addWorkoutItem("Plank Jacks", "Ngày 16-20 • Plank nhảy • 15 lần", "https://www.youtube.com/watch?v=c4DAnQ6DtF8");
        addWorkoutItem("Shoulder Taps", "Ngày 16-20 • Chạm vai • 20 lần", "https://www.youtube.com/watch?v=gWHQpMUd2nA");
        addWorkoutItem("Plank Up-Downs", "Ngày 16-20 • Plank lên xuống • 10 lần", "https://www.youtube.com/watch?v=E1IH5tZl8bE");
        
        // Ngày 21-25: Cardio kết hợp
        addWorkoutItem("Jump Rope", "Ngày 21-25 • Nhảy dây • 45 giây", "https://www.youtube.com/watch?v=1BZM2Vre5oc");
        addWorkoutItem("Jumping Lunges", "Ngày 21-25 • Lunge nhảy • 12 lần", "https://www.youtube.com/watch?v=3XDriUn0udo");
        addWorkoutItem("Butt Kicks", "Ngày 21-25 • Đá mông • 30 giây", "https://www.youtube.com/watch?v=ZpUre6uZQ7E");
        
        // === CHƯƠNG TRÌNH 60 NGÀY ===
        // Tuần 1-2: Nền tảng sức mạnh
        addWorkoutItem("Push-ups - Hít đất cơ bản", "Tuần 1-2 • Tập ngực và tay • 12 lần", "https://youtu.be/IODxDxX7oi4");
        addWorkoutItem("Squats - Squat cơ bản", "Tuần 1-2 • Tập chân và mông • 15 lần", "https://youtu.be/YaXPRqUwItQ");
        addWorkoutItem("Lunges - Bước chân", "Tuần 1-2 • Tăng cường chân • 12 lần/chân", "https://youtu.be/QOVaHwm-Q6U");
        addWorkoutItem("Dumbbell Rows", "Tuần 1-2 • Chèo tạ tập lưng • 12 lần", "https://www.youtube.com/watch?v=pYcpY20QaE8");
        addWorkoutItem("Shoulder Press", "Tuần 1-2 • Đẩy vai với tạ • 12 lần", "https://youtu.be/qEwKCR5JCog");
        addWorkoutItem("Bicep Curls", "Tuần 1-2 • Cuốn tay tập bắp • 15 lần", "https://www.youtube.com/watch?v=ykJmrZ5v0Oo");
        
        // Tuần 3-4: Tập ngực và tay
        addWorkoutItem("Incline Push-ups", "Tuần 3-4 • Hít đất nghiêng • 15 lần", "https://youtu.be/IODxDxX7oi4");
        addWorkoutItem("Diamond Push-ups", "Tuần 3-4 • Hít đất kim cương • 10 lần", "https://youtu.be/J0DnG1_S92I");
        addWorkoutItem("Tricep Extensions", "Tuần 3-4 • Duỗi tay sau • 12 lần", "https://www.youtube.com/watch?v=_gsUck-7M74");
        addWorkoutItem("Hammer Curls", "Tuần 3-4 • Cuốn tay búa • 12 lần", "https://www.youtube.com/watch?v=zC3nLlEvin4");
        addWorkoutItem("Chest Dips", "Tuần 3-4 • Chống đẩy ngực • 8 lần", "https://www.youtube.com/watch?v=2z8JmcrW-As");
        
        // === CHƯƠNG TRÌNH 90 NGÀY ===
        // Tháng 1: Giai đoạn thích nghi
        addWorkoutItem("Full Body Warm-up", "Tháng 1 • Khởi động toàn thân • 10 phút", "https://www.youtube.com/watch?v=UBMk30rjy0o");
        addWorkoutItem("Compound Movements", "Tháng 1 • Động tác phức hợp • 8 bài", "https://www.youtube.com/watch?v=U0bhE67HuDY");
        addWorkoutItem("Core Stability", "Tháng 1 • Ổn định core • 12 phút", "https://www.youtube.com/watch?v=DWmGArQBtFI");
        addWorkoutItem("Cardio Intervals", "Tháng 1 • Cardio ngắt quãng • 15 phút", "https://www.youtube.com/watch?v=ml6cT4AZdqI");
        addWorkoutItem("Flexibility Training", "Tháng 1 • Tăng độ dẻo • 10 phút", "https://www.youtube.com/watch?v=L_xrDAtykMI");
        addWorkoutItem("Recovery Stretches", "Tháng 1 • Giãn cơ phục hồi • 8 phút", "https://www.youtube.com/watch?v=qULTwquOuT4");
        addWorkoutItem("Mobility Drills", "Tháng 1 • Bài tập vận động • 10 phút", "https://www.youtube.com/watch?v=TSIbzfcnv_8");
        addWorkoutItem("Balance Exercises", "Tháng 1 • Bài tập thăng bằng • 8 phút", "https://www.youtube.com/watch?v=R0mMyV5OtcM");
    }

    private void addWorkoutItem(String title, String subtitle, String videoUrl) {
        addWorkoutItem(title, subtitle, videoUrl, null, false);
    }
    
    private void addWorkoutItem(String title, String subtitle, String videoUrl, String programType, boolean isPremium) {
        CardView workoutCard = new CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (16 * getResources().getDisplayMetrics().density));
        workoutCard.setLayoutParams(cardParams);
        workoutCard.setRadius(16 * getResources().getDisplayMetrics().density);
        workoutCard.setCardElevation(4 * getResources().getDisplayMetrics().density);
        workoutCard.setCardBackgroundColor(0xFFFFFFFF);

        LinearLayout contentLayout = new LinearLayout(requireContext());
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density));
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Exercise image with rounded corners
        CardView imageCard = new CardView(requireContext());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                (int) (80 * getResources().getDisplayMetrics().density),
                (int) (80 * getResources().getDisplayMetrics().density));
        imageParams.setMargins(0, 0, (int) (16 * getResources().getDisplayMetrics().density), 0);
        imageCard.setLayoutParams(imageParams);
        imageCard.setRadius(12 * getResources().getDisplayMetrics().density);
        imageCard.setCardElevation(2 * getResources().getDisplayMetrics().density);

        ImageView exerciseImage = new ImageView(requireContext());
        exerciseImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        exerciseImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        // Load YouTube thumbnail image using YouTubeHelper
        if (videoUrl != null && !videoUrl.isEmpty()) {
            String videoId = YouTubeHelper.getVideoIdFromUrl(videoUrl);
            
            // Debug logging
            android.util.Log.d("WorkoutThumbnail", "Title: " + title);
            android.util.Log.d("WorkoutThumbnail", "Video URL: " + videoUrl);
            android.util.Log.d("WorkoutThumbnail", "Video ID: " + videoId);
            
            if (videoId != null && !videoId.isEmpty()) {
                // Use YouTubeHelper's method with rounded corners
                int cornerRadius = (int) (12 * getResources().getDisplayMetrics().density);
                YouTubeHelper.loadThumbnailWithRoundedCorners(
                    exerciseImage, 
                    videoUrl, 
                    YouTubeHelper.ThumbnailQuality.HIGH,
                    cornerRadius,
                    getDefaultExerciseIcon(title)
                );
            } else {
                android.util.Log.e("WorkoutThumbnail", "Failed to extract video ID for: " + title);
                exerciseImage.setImageResource(getDefaultExerciseIcon(title));
            }
        } else {
            android.util.Log.w("WorkoutThumbnail", "No video URL for: " + title);
            exerciseImage.setImageResource(getDefaultExerciseIcon(title));
        }
        
        imageCard.addView(exerciseImage);

        // Info section
        LinearLayout infoLayout = new LinearLayout(requireContext());
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        infoLayout.setLayoutParams(infoParams);

        // Exercise title
        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(title);
        tvTitle.setTextColor(0xFF1A1A1A);
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setMaxLines(2);
        tvTitle.setEllipsize(android.text.TextUtils.TruncateAt.END);
        
        // Add premium lock icon if needed
        if (isPremium) {
            if (!premiumManager.isPremiumUser()) {
                // Add lock icon to title
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_lock_idle_lock, 0);
                tvTitle.setCompoundDrawablePadding((int) (8 * getResources().getDisplayMetrics().density));
                // Dim the content
                workoutCard.setAlpha(0.7f);
            }
        }

        // Exercise description
        TextView tvSubtitle = new TextView(requireContext());
        tvSubtitle.setText(subtitle);
        tvSubtitle.setTextColor(0xFF666666);
        tvSubtitle.setTextSize(14);
        tvSubtitle.setMaxLines(2);
        tvSubtitle.setEllipsize(android.text.TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams subtitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        subtitleParams.setMargins(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        tvSubtitle.setLayoutParams(subtitleParams);

        // Exercise details (difficulty, duration, calories)
        LinearLayout detailsLayout = new LinearLayout(requireContext());
        detailsLayout.setOrientation(LinearLayout.HORIZONTAL);
        detailsLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        detailsParams.setMargins(0, (int) (8 * getResources().getDisplayMetrics().density), 0, 0);
        detailsLayout.setLayoutParams(detailsParams);

        // Difficulty level
        TextView tvDifficulty = new TextView(requireContext());
        if (title.contains("30 Ngày") || title.contains("Gập bụng") || title.contains("Jumping Jacks")) {
            tvDifficulty.setText("● Mới bắt đầu");
            tvDifficulty.setTextColor(0xFF4CAF50);
        } else if (title.contains("60 Ngày") || title.contains("Plank") || title.contains("Squats")) {
            tvDifficulty.setText("● Trung cấp");
            tvDifficulty.setTextColor(0xFFFF9800);
        } else if (title.contains("90 Ngày") || title.contains("Burpees") || title.contains("Advanced")) {
            tvDifficulty.setText("● Nâng cao");
            tvDifficulty.setTextColor(0xFFE91E63);
        } else {
            tvDifficulty.setText("● Trung cấp");
            tvDifficulty.setTextColor(0xFFFF9800);
        }
        tvDifficulty.setTextSize(12);
        tvDifficulty.setTypeface(null, android.graphics.Typeface.BOLD);

        // Duration
        TextView tvDuration = new TextView(requireContext());
        if (title.contains("Plank")) {
            tvDuration.setText("⏱ 45 phút");
        } else if (title.contains("Cardio") || title.contains("HIIT")) {
            tvDuration.setText("⏱ 20 phút");
        } else if (title.contains("30 Ngày")) {
            tvDuration.setText("⏱ 15 phút");
        } else if (title.contains("60 Ngày")) {
            tvDuration.setText("⏱ 25 phút");
        } else if (title.contains("90 Ngày")) {
            tvDuration.setText("⏱ 35 phút");
        } else {
            tvDuration.setText("⏱ 15 phút");
        }
        tvDuration.setTextColor(0xFF666666);
        tvDuration.setTextSize(12);
        LinearLayout.LayoutParams durationParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        durationParams.setMargins((int) (12 * getResources().getDisplayMetrics().density), 0, 0, 0);
        tvDuration.setLayoutParams(durationParams);

        // Calories
        TextView tvCalories = new TextView(requireContext());
        if (title.contains("Burpees") || title.contains("HIIT")) {
            tvCalories.setText("🔥 200 cal");
        } else if (title.contains("Cardio") || title.contains("Jumping")) {
            tvCalories.setText("🔥 150 cal");
        } else if (title.contains("Squats") || title.contains("Lunges")) {
            tvCalories.setText("🔥 120 cal");
        } else if (title.contains("Push-ups") || title.contains("Plank")) {
            tvCalories.setText("🔥 100 cal");
        } else {
            tvCalories.setText("🔥 120 cal");
        }
        tvCalories.setTextColor(0xFF666666);
        tvCalories.setTextSize(12);
        LinearLayout.LayoutParams caloriesParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        caloriesParams.setMargins((int) (12 * getResources().getDisplayMetrics().density), 0, 0, 0);
        tvCalories.setLayoutParams(caloriesParams);

        // Completion indicator
        TextView tvCompleted = new TextView(requireContext());
        tvCompleted.setText("✅ 12 lần");
        tvCompleted.setTextColor(0xFF4CAF50);
        tvCompleted.setTextSize(12);
        tvCompleted.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams completedParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        completedParams.setMargins((int) (12 * getResources().getDisplayMetrics().density), 0, 0, 0);
        tvCompleted.setLayoutParams(completedParams);

        detailsLayout.addView(tvDifficulty);
        detailsLayout.addView(tvDuration);
        detailsLayout.addView(tvCalories);
        detailsLayout.addView(tvCompleted);

        infoLayout.addView(tvTitle);
        infoLayout.addView(tvSubtitle);
        infoLayout.addView(detailsLayout);

        // Favorite star
        TextView tvFavorite = new TextView(requireContext());
        tvFavorite.setText("⭐");
        tvFavorite.setTextSize(24);
        tvFavorite.setTextColor(0xFFFFD700);
        LinearLayout.LayoutParams favoriteParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        favoriteParams.gravity = android.view.Gravity.TOP;
        tvFavorite.setLayoutParams(favoriteParams);

        contentLayout.addView(imageCard);
        contentLayout.addView(infoLayout);
        contentLayout.addView(tvFavorite);

        workoutCard.addView(contentLayout);

        // Click listener
        workoutCard.setOnClickListener(v -> {
            // Check premium access for premium programs
            if (isPremium) {
                if (!premiumManager.isPremiumUser()) {
                    // Show premium upgrade dialog
                    showPremiumUpgradeDialog(programType);
                    return;
                }
            }
            
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
            
            // Open workout session for individual exercises
            if (videoUrl != null && !videoUrl.isEmpty()) {
                Intent intent = new Intent(requireContext(), WorkoutSessionActivity.class);
                intent.putExtra("exercise_name", title);
                intent.putExtra("exercise_description", subtitle);
                intent.putExtra("video_url", videoUrl);
                intent.putExtra("exercise_duration", getDurationFromTitle(title));
                intent.putExtra("exercise_calories", getCaloriesFromTitle(title));
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Chưa có video hướng dẫn - Bạn hãy gửi URL cho bài tập này!", Toast.LENGTH_LONG).show();
            }
        });

        layoutWorkoutList.addView(workoutCard);
    }
    
    private int getDefaultExerciseIcon(String title) {
        if (title.contains("Plank") || title.contains("Core") || title.contains("Gập bụng") || title.contains("Abs") || 
            title.contains("Crunches") || title.contains("V-Ups") || title.contains("Russian") || title.contains("Dead Bug") ||
            title.contains("Bicycle") || title.contains("Leg Raises") || title.contains("Flutter") || title.contains("Heel")) {
            return R.drawable.ic_exercise_abs;
        } else if (title.contains("Push-ups") || title.contains("Ngực") || title.contains("Chest") || 
                   title.contains("Diamond") || title.contains("Incline")) {
            return R.drawable.ic_exercise_chest;
        } else if (title.contains("Squats") || title.contains("Lunges") || title.contains("Chân") || title.contains("Legs") ||
                   title.contains("Jump") || title.contains("Jumping Lunges")) {
            return R.drawable.ic_exercise_legs;
        } else if (title.contains("Cardio") || title.contains("Jumping") || title.contains("Burpees") || 
                   title.contains("High Knees") || title.contains("Jump Rope") || title.contains("Butt Kicks") ||
                   title.contains("Mountain Climbers") || title.contains("HIIT")) {
            return R.drawable.ic_exercise_cardio;
        } else if (title.contains("Rows") || title.contains("Lưng") || title.contains("Back") || 
                   title.contains("Superman") || title.contains("Pull-ups")) {
            return R.drawable.ic_exercise_back;
        } else if (title.contains("Chương Trình") || title.contains("30 Ngày") || title.contains("60 Ngày") || 
                   title.contains("90 Ngày") || title.contains("Full Body") || title.contains("Compound")) {
            return R.drawable.ic_exercise_program;
        } else {
            return R.drawable.ic_exercise_chest;
        }
    }
    
    private int getDurationFromTitle(String title) {
        if (title.contains("Plank")) return 45;
        else if (title.contains("Cardio") || title.contains("HIIT")) return 20;
        else if (title.contains("30 Ngày")) return 15;
        else if (title.contains("60 Ngày")) return 25;
        else if (title.contains("90 Ngày")) return 35;
        else return 15;
    }
    
    private int getCaloriesFromTitle(String title) {
        if (title.contains("Burpees") || title.contains("HIIT")) return 200;
        else if (title.contains("Cardio") || title.contains("Jumping")) return 150;
        else if (title.contains("Squats") || title.contains("Lunges")) return 120;
        else if (title.contains("Push-ups") || title.contains("Plank")) return 100;
        else return 120;
    }

    private void loadExercises() {
        executor.execute(() -> {
            try {
                // Check if exercises exist, if not create sample data
                List<Exercise> exercises = database.exerciseDao().getAllExercises();
                if (exercises.isEmpty()) {
                    createSampleExercises();
                    exercises = database.exerciseDao().getAllExercises();
                }
                
                // Filter exercises based on premium status
                boolean isPremiumUser = premiumManager.isPremiumUser();
                List<Exercise> filteredExercises = database.exerciseDao().getExercisesForUser(isPremiumUser);
                
                final List<Exercise> finalExercises = filteredExercises;
                if (getActivity() != null && !getActivity().isFinishing()) {
                    requireActivity().runOnUiThread(() -> {
                        allExercises.clear();
                        allExercises.addAll(finalExercises);
                        filterExercises();
                    });
                }
            } catch (Exception e) {
                android.util.Log.e("WorkoutsFragment", "Error loading exercises", e);
            }
        });
    }
    
    private void createSampleExercises() {
        List<Exercise> sampleExercises = new ArrayList<>();
        
        // CHƯƠNG TRÌNH 30 NGÀY - Ngày 1-5: Khởi động cơ bản
        sampleExercises.add(new Exercise("Jumping Jacks", "Khởi động toàn thân", "all", "beginner", 30, 150, "https://youtu.be/c4DAnQ6DtF8"));
        sampleExercises.add(new Exercise("Gập bụng cơ bản", "Tập cơ bụng trên", "abs", "beginner", 15, 80, "https://youtu.be/Xyd_fa5zoEU"));
        sampleExercises.add(new Exercise("Plank", "Tăng cường core", "abs", "intermediate", 10, 60, "https://youtu.be/ASdvN_XEl_c"));
        sampleExercises.add(new Exercise("Mountain Climbers", "Cardio và core", "abs", "advanced", 12, 150, "https://youtu.be/kLh-uczlPLg"));
        sampleExercises.add(new Exercise("Leg Raises", "Tập cơ bụng dưới", "abs", "intermediate", 15, 110, "https://youtu.be/Xyd_fa5zoEU"));
        
        // Ngày 6-10: Tăng cường sức bền
        sampleExercises.add(new Exercise("Burpees", "Toàn thân và cardio", "all", "advanced", 10, 200, "https://youtu.be/JZQA08SlJnM"));
        sampleExercises.add(new Exercise("Bicycle Crunches", "Cơ bụng chéo", "abs", "intermediate", 20, 120, "https://youtu.be/Xyd_fa5zoEU"));
        sampleExercises.add(new Exercise("Side Plank", "Cơ bụng bên", "abs", "intermediate", 8, 80, "https://youtu.be/ASdvN_XEl_c"));
        sampleExercises.add(new Exercise("Russian Twists", "Xoay thân", "abs", "intermediate", 20, 100, "https://youtu.be/Xyd_fa5zoEU"));
        sampleExercises.add(new Exercise("High Knees", "Cardio cường độ cao", "all", "beginner", 30, 120, "https://youtu.be/c4DAnQ6DtF8"));
        sampleExercises.add(new Exercise("Flutter Kicks", "Cơ bụng dưới", "abs", "intermediate", 20, 90, "https://youtu.be/Xyd_fa5zoEU"));
        
        // Ngày 11-15: Gập bụng cơ bản
        sampleExercises.add(new Exercise("Crunches", "Gập bụng cơ bản", "abs", "beginner", 15, 80, "https://youtu.be/Xyd_fa5zoEU"));
        sampleExercises.add(new Exercise("Reverse Crunches", "Gập bụng ngược", "abs", "intermediate", 12, 90, "https://www.youtube.com/watch?v=hyv14e2QDq0"));
        sampleExercises.add(new Exercise("Toe Touches", "Chạm ngón chân", "abs", "beginner", 15, 70, "https://www.youtube.com/watch?v=4hmQA3snN3Q"));
        sampleExercises.add(new Exercise("V-Ups", "Gập bụng chữ V", "abs", "advanced", 10, 120, "https://www.youtube.com/watch?v=iP2fjvG0g3w"));
        sampleExercises.add(new Exercise("Dead Bug", "Ổn định core", "abs", "beginner", 12, 90, "https://www.youtube.com/watch?v=g_BYB0R-4Ws"));
        sampleExercises.add(new Exercise("Heel Touches", "Chạm gót chân", "abs", "beginner", 20, 60, "https://www.youtube.com/watch?v=9bR-EL9yUeA"));
        sampleExercises.add(new Exercise("Sit-ups", "Ngồi dậy cơ bản", "abs", "intermediate", 15, 100, "https://www.youtube.com/watch?v=1fbU_MkV7NE"));
        
        // Ngày 16-20: Plank và biến thể
        sampleExercises.add(new Exercise("Plank 45s", "Plank cơ bản 45 giây", "abs", "intermediate", 1, 45, "https://youtu.be/ASdvN_XEl_c"));
        sampleExercises.add(new Exercise("Plank to Downward Dog", "Plank động", "abs", "intermediate", 10, 80, "https://www.youtube.com/watch?v=F1RzP5g0yJk"));
        sampleExercises.add(new Exercise("Plank Jacks", "Plank nhảy", "abs", "advanced", 15, 100, "https://www.youtube.com/watch?v=c4DAnQ6DtF8"));
        sampleExercises.add(new Exercise("Shoulder Taps", "Chạm vai", "abs", "intermediate", 20, 90, "https://www.youtube.com/watch?v=gWHQpMUd2nA"));
        sampleExercises.add(new Exercise("Plank Up-Downs", "Plank lên xuống", "abs", "advanced", 10, 120, "https://www.youtube.com/watch?v=E1IH5tZl8bE"));
        
        // CHƯƠNG TRÌNH 60 NGÀY - Nền tảng sức mạnh
        sampleExercises.add(new Exercise("Push-ups", "Hít đất cơ bản", "chest", "beginner", 15, 120, "https://youtu.be/IODxDxX7oi4"));
        sampleExercises.add(new Exercise("Squats", "Squat cơ bản", "legs", "beginner", 20, 200, "https://youtu.be/YaXPRqUwItQ"));
        sampleExercises.add(new Exercise("Lunges", "Lunge", "legs", "intermediate", 18, 180, "https://youtu.be/QOVaHwm-Q6U"));
        sampleExercises.add(new Exercise("Dumbbell Rows", "Chèo tạ", "back", "intermediate", 15, 140, "https://www.youtube.com/watch?v=pYcpY20QaE8"));
        sampleExercises.add(new Exercise("Shoulder Press", "Đẩy vai", "shoulder", "intermediate", 18, 140, "https://youtu.be/qEwKCR5JCog"));
        sampleExercises.add(new Exercise("Bicep Curls", "Cuốn tay", "shoulder", "beginner", 15, 100, "https://www.youtube.com/watch?v=ykJmrZ5v0Oo"));
        
        // Tập ngực và tay
        sampleExercises.add(new Exercise("Incline Push-ups", "Hít đất nghiêng", "chest", "beginner", 20, 100, "https://youtu.be/IODxDxX7oi4"));
        sampleExercises.add(new Exercise("Diamond Push-ups", "Hít đất kim cương", "chest", "advanced", 12, 150, "https://youtu.be/J0DnG1_S92I"));
        sampleExercises.add(new Exercise("Tricep Extensions", "Duỗi tay sau", "shoulder", "intermediate", 15, 120, "https://www.youtube.com/watch?v=_gsUck-7M74"));
        sampleExercises.add(new Exercise("Hammer Curls", "Cuốn tay búa", "shoulder", "intermediate", 12, 110, "https://www.youtube.com/watch?v=zC3nLlEvin4"));
        
        // CHƯƠNG TRÌNH 90 NGÀY - Giai đoạn thích nghi
        sampleExercises.add(new Exercise("Full Body Warm-up", "Khởi động toàn thân", "all", "beginner", 10, 100, "https://www.youtube.com/watch?v=UBMk30rjy0o"));
        sampleExercises.add(new Exercise("Compound Movements", "Động tác phức hợp", "all", "advanced", 8, 180, "https://www.youtube.com/watch?v=U0bhE67HuDY"));
        sampleExercises.add(new Exercise("Core Stability", "Ổn định core", "abs", "intermediate", 12, 120, "https://www.youtube.com/watch?v=DWmGArQBtFI"));
        sampleExercises.add(new Exercise("Cardio Intervals", "Cardio ngắt quãng", "all", "advanced", 15, 200, "https://www.youtube.com/watch?v=ml6cT4AZdqI"));
        sampleExercises.add(new Exercise("Flexibility Training", "Tăng độ dẻo", "all", "beginner", 10, 60, "https://www.youtube.com/watch?v=L_xrDAtykMI"));
        
        // Thêm các bài tập bổ sung
        sampleExercises.add(new Exercise("Jump Rope", "Nhảy dây", "all", "intermediate", 30, 180, "https://www.youtube.com/watch?v=1BZM2Vre5oc"));
        sampleExercises.add(new Exercise("Jumping Lunges", "Lunge nhảy", "legs", "advanced", 15, 160, "https://www.youtube.com/watch?v=3XDriUn0udo"));
        sampleExercises.add(new Exercise("Wall Sit", "Ngồi tựa tường", "legs", "intermediate", 10, 120, "https://youtu.be/YaXPRqUwItQ"));
        sampleExercises.add(new Exercise("Calf Raises", "Nâng bắp chân", "legs", "beginner", 25, 100, "https://youtu.be/YaXPRqUwItQ"));
        sampleExercises.add(new Exercise("Superman", "Bài tập siêu nhân", "back", "beginner", 12, 90, "https://youtu.be/cc6UVRS7PW4"));
        sampleExercises.add(new Exercise("Pull-ups", "Kéo xà", "back", "advanced", 10, 180, "https://youtu.be/eGo4IYlbE5g"));
        
        // Set additional properties
        for (Exercise exercise : sampleExercises) {
            exercise.setEquipment("none");
            exercise.setTargetMuscles(exercise.getCategory());
            exercise.setRating(4.0f + (float)(Math.random() * 1.0f)); // Random rating 4.0-5.0
            exercise.setRatingCount((int)(Math.random() * 100) + 10); // Random rating count 10-110
            exercise.setCompletedCount((int)(Math.random() * 20)); // Random completed count 0-20
            
            // Mark some exercises as premium (advanced and specific ones)
            if (exercise.getDifficulty().equals("advanced") || 
                exercise.getName().contains("Diamond") ||
                exercise.getName().contains("V-Ups") ||
                exercise.getName().contains("Compound") ||
                exercise.getName().contains("Pull-ups") ||
                exercise.getName().contains("Plank Jacks") ||
                exercise.getName().contains("Cardio Intervals")) {
                exercise.setPremium(true);
            } else {
                exercise.setPremium(false);
            }
        }
        
        // Add some exclusive premium exercises
        List<Exercise> premiumExercises = new ArrayList<>();
        
        // Premium Chest Workouts
        premiumExercises.add(new Exercise("💎 Advanced Chest Blast", "Bài tập ngực nâng cao với kỹ thuật đặc biệt", "chest", "advanced", 25, 300, "https://youtu.be/IODxDxX7oi4", true));
        premiumExercises.add(new Exercise("💎 Explosive Push-up Variations", "Biến thể hít đất bùng nổ", "chest", "advanced", 20, 280, "https://youtu.be/J0DnG1_S92I", true));
        
        // Premium Abs Workouts
        premiumExercises.add(new Exercise("💎 6-Pack Destroyer", "Phá hủy mỡ bụng 6 múi", "abs", "advanced", 30, 350, "https://youtu.be/Xyd_fa5zoEU", true));
        premiumExercises.add(new Exercise("💎 Core Shredder Pro", "Xé nát cơ core chuyên nghiệp", "abs", "advanced", 25, 320, "https://youtu.be/ASdvN_XEl_c", true));
        
        // Premium Full Body
        premiumExercises.add(new Exercise("💎 HIIT Beast Mode", "Chế độ quái vật HIIT", "all", "advanced", 35, 450, "https://www.youtube.com/watch?v=ml6cT4AZdqI", true));
        premiumExercises.add(new Exercise("💎 Metabolic Meltdown", "Tan chảy trao đổi chất", "all", "advanced", 40, 500, "https://www.youtube.com/watch?v=U0bhE67HuDY", true));
        
        // Premium Legs
        premiumExercises.add(new Exercise("💎 Leg Annihilator", "Hủy diệt cơ chân", "legs", "advanced", 30, 380, "https://youtu.be/YaXPRqUwItQ", true));
        premiumExercises.add(new Exercise("💎 Glute Sculptor", "Điêu khắc cơ mông", "legs", "advanced", 25, 300, "https://youtu.be/QOVaHwm-Q6U", true));
        
        // Premium Back & Shoulders
        premiumExercises.add(new Exercise("💎 Back Widener Pro", "Mở rộng lưng chuyên nghiệp", "back", "advanced", 28, 320, "https://youtu.be/eGo4IYlbE5g", true));
        premiumExercises.add(new Exercise("💎 Shoulder Boulder Builder", "Xây dựng vai tảng đá", "shoulder", "advanced", 22, 280, "https://youtu.be/qEwKCR5JCog", true));
        
        // Set properties for premium exercises
        for (Exercise exercise : premiumExercises) {
            exercise.setEquipment("premium");
            exercise.setTargetMuscles(exercise.getCategory());
            exercise.setRating(4.5f + (float)(Math.random() * 0.5f)); // Higher rating 4.5-5.0
            exercise.setRatingCount((int)(Math.random() * 200) + 50); // Higher rating count 50-250
            exercise.setCompletedCount((int)(Math.random() * 10)); // Lower completed count for premium
        }
        
        sampleExercises.addAll(premiumExercises);
        database.exerciseDao().insertExercises(sampleExercises);
    }
    
    private void loadStatistics() {
        executor.execute(() -> {
            int totalExercises = database.exerciseDao().getTotalExerciseCount();
            int favoriteCount = database.exerciseDao().getFavoriteExerciseCount();
            int completedCount = database.exerciseDao().getTotalCompletedCount();
            int totalCalories = database.exerciseDao().getTotalCaloriesBurned();
            
            requireActivity().runOnUiThread(() -> {
                tvTotalExercises.setText(String.valueOf(totalExercises));
                tvFavorites.setText(String.valueOf(favoriteCount));
                tvCompleted.setText(String.valueOf(completedCount));
                tvCalories.setText(String.valueOf(totalCalories));
            });
        });
    }

    private void filterExercises() {
        filteredExercises.clear();
        boolean isPremiumUser = premiumManager.isPremiumUser();
        
        for (Exercise exercise : allExercises) {
            boolean matchesCategory = currentCategory.equals("all") || exercise.getCategory().equals(currentCategory);
            boolean matchesDifficulty = currentDifficulty.equals("all") || exercise.getDifficulty().equals(currentDifficulty);
            boolean matchesSearch = currentSearch.isEmpty() || 
                exercise.getName().toLowerCase().contains(currentSearch.toLowerCase()) ||
                exercise.getDescription().toLowerCase().contains(currentSearch.toLowerCase());
            boolean matchesFavorites = !showingFavorites || exercise.isFavorite();
            boolean matchesCompleted = !showingCompleted || exercise.getCompletedCount() > 0;
            
            // Premium filter - only show premium exercises if user has premium
            boolean matchesPremium = !exercise.isPremium() || isPremiumUser;
            
            // Duration filter
            boolean matchesDuration = true;
            if (!currentDuration.equals("all")) {
                int duration = exercise.getDuration();
                switch (currentDuration) {
                    case "short":
                        matchesDuration = duration <= 15;
                        break;
                    case "medium":
                        matchesDuration = duration > 15 && duration <= 30;
                        break;
                    case "long":
                        matchesDuration = duration > 30;
                        break;
                }
            }
            
            // Equipment filter
            boolean matchesEquipment = true;
            if (!currentEquipment.equals("all")) {
                String equipment = exercise.getEquipment() != null ? exercise.getEquipment() : "none";
                matchesEquipment = equipment.equals(currentEquipment);
            }
            
            if (matchesCategory && matchesDifficulty && matchesSearch && matchesFavorites && 
                matchesCompleted && matchesDuration && matchesEquipment && matchesPremium) {
                filteredExercises.add(exercise);
            }
        }
        
        if (exerciseAdapter != null) {
            exerciseAdapter.updateExercises(filteredExercises);
        }
        
        // Update statistics
        updateFilteredStatistics();
    }
    
    private void selectCategoryFilter(String category) {
        currentCategory = category;
        updateCategoryChips();
        filterExercises();
    }
    
    private void selectDifficultyFilter(String difficulty) {
        currentDifficulty = difficulty;
        updateDifficultyChips();
        filterExercises();
    }
    
    private void selectDurationFilter(String duration) {
        currentDuration = duration;
        updateDurationChips();
        filterExercises();
    }
    
    private void selectEquipmentFilter(String equipment) {
        currentEquipment = equipment;
        updateEquipmentChips();
        filterExercises();
    }
    
    private void toggleFavorites() {
        showingFavorites = !showingFavorites;
        btnShowFavorites.setText(showingFavorites ? "Tất cả" : "Yêu thích");
        filterExercises();
    }
    
    private void toggleCompleted() {
        showingCompleted = !showingCompleted;
        filterExercises();
    }
    
    private void clearSearch() {
        etSearch.setText("");
        currentSearch = "";
        btnClearSearch.setVisibility(View.GONE);
        filterExercises();
    }

    // ExerciseAdapter.OnExerciseClickListener implementation
    @Override
    public void onFavoriteClick(Exercise exercise) {
        executor.execute(() -> {
            boolean newFavoriteStatus = !exercise.isFavorite();
            database.exerciseDao().updateFavoriteStatus(exercise.getId(), newFavoriteStatus);
            exercise.setFavorite(newFavoriteStatus);
            
            requireActivity().runOnUiThread(() -> {
                exerciseAdapter.notifyDataSetChanged();
                loadStatistics();
                Toast.makeText(requireContext(), 
                    newFavoriteStatus ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích", 
                    Toast.LENGTH_SHORT).show();
            });
        });
    }
    
    @Override
    public void onExerciseClick(Exercise exercise) {
        // Increment completed count
        executor.execute(() -> {
            database.exerciseDao().incrementCompletedCount(exercise.getId());
            exercise.setCompletedCount(exercise.getCompletedCount() + 1);
            
            requireActivity().runOnUiThread(() -> {
                exerciseAdapter.notifyDataSetChanged();
                loadStatistics();
            });
        });
        
        // Open exercise video
        if (exercise.getVideoUrl() != null && !exercise.getVideoUrl().isEmpty()) {
            Intent intent = new Intent(requireContext(), ExerciseVideoActivity.class);
            intent.putExtra("video_url", exercise.getVideoUrl());
            intent.putExtra("exercise_name", exercise.getName());
            intent.putExtra("exercise_description", exercise.getDescription());
            intent.putExtra("exercise_duration", exercise.getDuration());
            startActivity(intent);
        } else {
            Toast.makeText(requireContext(), "Video không khả dụng", Toast.LENGTH_SHORT).show();
        }
    }
    
    // UI Helper Methods
    private void updateCategoryChips() {
        // Reset all chips
        resetChipStyle(chipCategoryAll);
        resetChipStyle(chipCategoryChest);
        resetChipStyle(chipCategoryShoulder);
        resetChipStyle(chipCategoryLegs);
        resetChipStyle(chipCategoryAbs);
        resetChipStyle(chipCategoryBack);
        
        // Highlight selected chip
        TextView selectedChip = null;
        switch (currentCategory) {
            case "all": selectedChip = chipCategoryAll; break;
            case "chest": selectedChip = chipCategoryChest; break;
            case "shoulder": selectedChip = chipCategoryShoulder; break;
            case "legs": selectedChip = chipCategoryLegs; break;
            case "abs": selectedChip = chipCategoryAbs; break;
            case "back": selectedChip = chipCategoryBack; break;
        }
        if (selectedChip != null) {
            highlightChip(selectedChip);
        }
    }
    
    private void updateDifficultyChips() {
        resetChipStyle(chipDifficultyAll);
        resetChipStyle(chipDifficultyBeginner);
        resetChipStyle(chipDifficultyIntermediate);
        resetChipStyle(chipDifficultyAdvanced);
        
        TextView selectedChip = null;
        switch (currentDifficulty) {
            case "all": selectedChip = chipDifficultyAll; break;
            case "beginner": selectedChip = chipDifficultyBeginner; break;
            case "intermediate": selectedChip = chipDifficultyIntermediate; break;
            case "advanced": selectedChip = chipDifficultyAdvanced; break;
        }
        if (selectedChip != null) {
            highlightChip(selectedChip);
        }
    }
    
    private void updateDurationChips() {
        if (chipDurationAll != null) {
            resetChipStyle(chipDurationAll);
            resetChipStyle(chipDurationShort);
            resetChipStyle(chipDurationMedium);
            resetChipStyle(chipDurationLong);
            
            TextView selectedChip = null;
            switch (currentDuration) {
                case "all": selectedChip = chipDurationAll; break;
                case "short": selectedChip = chipDurationShort; break;
                case "medium": selectedChip = chipDurationMedium; break;
                case "long": selectedChip = chipDurationLong; break;
            }
            if (selectedChip != null) {
                highlightChip(selectedChip);
            }
        }
    }
    
    private void updateEquipmentChips() {
        if (chipEquipmentAll != null) {
            resetChipStyle(chipEquipmentAll);
            resetChipStyle(chipEquipmentNone);
            resetChipStyle(chipEquipmentDumbbell);
            resetChipStyle(chipEquipmentResistance);
            
            TextView selectedChip = null;
            switch (currentEquipment) {
                case "all": selectedChip = chipEquipmentAll; break;
                case "none": selectedChip = chipEquipmentNone; break;
                case "dumbbell": selectedChip = chipEquipmentDumbbell; break;
                case "resistance": selectedChip = chipEquipmentResistance; break;
            }
            if (selectedChip != null) {
                highlightChip(selectedChip);
            }
        }
    }
    
    private void resetChipStyle(TextView chip) {
        if (chip != null) {
            chip.setBackgroundResource(R.drawable.chip_unselected);
            chip.setTextColor(0xFF666666);
        }
    }
    
    private void highlightChip(TextView chip) {
        if (chip != null) {
            chip.setBackgroundResource(R.drawable.chip_selected);
            chip.setTextColor(0xFFFFFFFF);
        }
    }
    
    private void updateFilteredStatistics() {
        int totalFiltered = filteredExercises.size();
        int favoriteFiltered = 0;
        int completedFiltered = 0;
        int totalCaloriesFiltered = 0;
        
        for (Exercise exercise : filteredExercises) {
            if (exercise.isFavorite()) favoriteFiltered++;
            if (exercise.getCompletedCount() > 0) completedFiltered++;
            totalCaloriesFiltered += exercise.getCalories() * exercise.getCompletedCount();
        }
        
        tvTotalExercises.setText(String.valueOf(totalFiltered));
        tvFavorites.setText(String.valueOf(favoriteFiltered));
        tvCompleted.setText(String.valueOf(completedFiltered));
        tvCalories.setText(String.valueOf(totalCaloriesFiltered));
    }
    
    private void showPremiumUpgradeDialog(String programType) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("🔒 Nội dung Premium")
            .setMessage(String.format("Chương trình %s ngày chỉ dành cho thành viên Premium.\n\n" +
                "Nâng cấp ngay để truy cập:\n" +
                "• Chương trình tập luyện nâng cao\n" +
                "• Kế hoạch dinh dưỡng cá nhân hóa\n" +
                "• Theo dõi tiến độ chi tiết\n" +
                "• Không quảng cáo", programType))
            .setPositiveButton("Nâng cấp Premium", (dialog, which) -> {
                Intent intent = new Intent(requireContext(), com.htdgym.app.activities.PremiumActivity.class);
                startActivityForResult(intent, 1001);
            })
            .setNegativeButton("Để sau", null)
            .setIcon(android.R.drawable.ic_lock_idle_lock)
            .show();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == getActivity().RESULT_OK) {
            // Premium upgrade successful, refresh the UI
            loadPopularWorkouts();
            Toast.makeText(requireContext(), "🎉 Chào mừng bạn đến với Premium!", Toast.LENGTH_LONG).show();
        }
    }
}