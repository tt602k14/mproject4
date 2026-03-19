package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.utils.YouTubeHelper;

import java.util.ArrayList;
import java.util.List;

public class ProgramPhaseDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvPhaseTitle, tvPhaseDescription;
    private LinearLayout layoutExercises;
    
    private String programType;
    private int phaseIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_phase_detail);

        programType = getIntent().getStringExtra("program_type");
        phaseIndex = getIntent().getIntExtra("phase_index", 0);

        initViews();
        setupClickListeners();
        loadPhaseExercises();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvPhaseTitle = findViewById(R.id.tv_phase_title);
        tvPhaseDescription = findViewById(R.id.tv_phase_description);
        layoutExercises = findViewById(R.id.layout_exercises);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadPhaseExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        int totalExercises = 0;
        int totalMinutes = 0;
        int totalCalories = 0;
        
        if ("30".equals(programType)) {
            switch (phaseIndex) {
                case 0: // Ngày 1-5
                    tvPhaseTitle.setText("Ngày 1-5: Khởi động cơ bản");
                    tvPhaseDescription.setText("Làm quen với các động tác cơ bản");
                    exercises.add(new Exercise("Jumping Jacks", "3 hiệp x 20 lần", "Khởi động toàn thân", 30, "PLACEHOLDER_URL_1_1"));
                    exercises.add(new Exercise("Gập bụng cơ bản", "3 hiệp x 15 lần", "Tập cơ bụng trên", 30, "PLACEHOLDER_URL_1_2"));
                    exercises.add(new Exercise("Plank", "3 hiệp x 30 giây", "Tăng cường core", 30, "PLACEHOLDER_URL_1_3"));
                    exercises.add(new Exercise("Mountain Climbers", "3 hiệp x 15 lần", "Cardio và core", 30, "PLACEHOLDER_URL_1_4"));
                    exercises.add(new Exercise("Leg Raises", "3 hiệp x 12 lần", "Tập cơ bụng dưới", 30, "PLACEHOLDER_URL_1_5"));
                    break;
                case 1: // Ngày 6-10
                    tvPhaseTitle.setText("Ngày 6-10: Tăng cường sức bền");
                    tvPhaseDescription.setText("Tăng thời gian và số lần tập");
                    exercises.add(new Exercise("Burpees", "3 hiệp x 10 lần", "Toàn thân và cardio", 30, "PLACEHOLDER_URL_2_1"));
                    exercises.add(new Exercise("Bicycle Crunches", "3 hiệp x 20 lần", "Cơ bụng chéo", 30, "PLACEHOLDER_URL_2_2"));
                    exercises.add(new Exercise("Side Plank", "3 hiệp x 30 giây/bên", "Cơ bụng bên", 30, "PLACEHOLDER_URL_2_3"));
                    exercises.add(new Exercise("Russian Twists", "3 hiệp x 20 lần", "Xoay thân", 30, "PLACEHOLDER_URL_2_4"));
                    exercises.add(new Exercise("High Knees", "3 hiệp x 30 giây", "Cardio cường độ cao", 30, "PLACEHOLDER_URL_2_5"));
                    exercises.add(new Exercise("Flutter Kicks", "3 hiệp x 20 lần", "Cơ bụng dưới", 30, "PLACEHOLDER_URL_2_6"));
                    break;
                case 2: // Ngày 11-15
                    tvPhaseTitle.setText("Ngày 11-15: Gập bụng cơ bản");
                    tvPhaseDescription.setText("Tập trung vào cơ bụng trên");
                    exercises.add(new Exercise("Crunches", "4 hiệp x 20 lần", "Gập bụng cơ bản", 30, "PLACEHOLDER_URL_3_1"));
                    exercises.add(new Exercise("Reverse Crunches", "4 hiệp x 15 lần", "Gập bụng ngược", 30, "PLACEHOLDER_URL_3_2"));
                    exercises.add(new Exercise("Toe Touches", "3 hiệp x 15 lần", "Chạm ngón chân", 30, "PLACEHOLDER_URL_3_3"));
                    exercises.add(new Exercise("V-Ups", "3 hiệp x 12 lần", "Gập bụng chữ V", 30, "PLACEHOLDER_URL_3_4"));
                    exercises.add(new Exercise("Dead Bug", "3 hiệp x 15 lần", "Ổn định core", 30, "PLACEHOLDER_URL_3_5"));
                    exercises.add(new Exercise("Heel Touches", "3 hiệp x 20 lần", "Chạm gót chân", 30, "PLACEHOLDER_URL_3_6"));
                    exercises.add(new Exercise("Sit-ups", "3 hiệp x 15 lần", "Ngồi dậy cơ bản", 30, "PLACEHOLDER_URL_3_7"));
                    break;
                case 3: // Ngày 16-20
                    tvPhaseTitle.setText("Ngày 16-20: Plank và biến thể");
                    tvPhaseDescription.setText("Tăng cường cơ core");
                    exercises.add(new Exercise("Plank", "4 hiệp x 45 giây", "Plank cơ bản", 45, "PLACEHOLDER_URL_4_1"));
                    exercises.add(new Exercise("Side Plank", "3 hiệp x 40 giây/bên", "Plank nghiêng", 40, "PLACEHOLDER_URL_4_2"));
                    exercises.add(new Exercise("Plank to Downward Dog", "3 hiệp x 12 lần", "Plank động", 30, "PLACEHOLDER_URL_4_3"));
                    exercises.add(new Exercise("Plank Jacks", "3 hiệp x 15 lần", "Plank nhảy", 30, "PLACEHOLDER_URL_4_4"));
                    exercises.add(new Exercise("Shoulder Taps", "3 hiệp x 20 lần", "Chạm vai", 30, "PLACEHOLDER_URL_4_5"));
                    exercises.add(new Exercise("Plank Up-Downs", "3 hiệp x 12 lần", "Plank lên xuống", 30, "PLACEHOLDER_URL_4_6"));
                    break;
                case 4: // Ngày 21-25
                    tvPhaseTitle.setText("Ngày 21-25: Cardio kết hợp");
                    tvPhaseDescription.setText("Đốt mỡ hiệu quả");
                    exercises.add(new Exercise("Jump Rope", "4 hiệp x 1 phút", "Nhảy dây", 60, "PLACEHOLDER_URL_5_1"));
                    exercises.add(new Exercise("Mountain Climbers", "4 hiệp x 30 giây", "Leo núi", 30, "PLACEHOLDER_URL_5_2"));
                    exercises.add(new Exercise("Burpees", "3 hiệp x 12 lần", "Burpees", 30, "PLACEHOLDER_URL_5_3"));
                    exercises.add(new Exercise("High Knees", "4 hiệp x 45 giây", "Chạy nâng cao đầu gối", 45, "PLACEHOLDER_URL_5_4"));
                    exercises.add(new Exercise("Jumping Lunges", "3 hiệp x 12 lần/chân", "Lunge nhảy", 30, "PLACEHOLDER_URL_5_5"));
                    exercises.add(new Exercise("Plank Jacks", "3 hiệp x 20 lần", "Plank nhảy", 30, "PLACEHOLDER_URL_5_6"));
                    exercises.add(new Exercise("Bicycle Crunches", "3 hiệp x 25 lần", "Gập bụng xe đạp", 30, "PLACEHOLDER_URL_5_7"));
                    break;
                case 5: // Ngày 26-28
                    tvPhaseTitle.setText("Ngày 26-28: Tăng cường độ");
                    tvPhaseDescription.setText("Nâng cao độ khó");
                    exercises.add(new Exercise("Advanced Crunches", "4 hiệp x 25 lần", "Gập bụng nâng cao", 30, "PLACEHOLDER_URL_6_1"));
                    exercises.add(new Exercise("Plank", "4 hiệp x 60 giây", "Plank dài", 60, "PLACEHOLDER_URL_6_2"));
                    exercises.add(new Exercise("V-Ups", "4 hiệp x 15 lần", "V-ups nâng cao", 30, "PLACEHOLDER_URL_6_3"));
                    exercises.add(new Exercise("Russian Twists", "4 hiệp x 30 lần", "Xoay thân có tạ", 30, "PLACEHOLDER_URL_6_4"));
                    exercises.add(new Exercise("Burpees", "4 hiệp x 15 lần", "Burpees nhanh", 30, "PLACEHOLDER_URL_6_5"));
                    exercises.add(new Exercise("Mountain Climbers", "4 hiệp x 45 giây", "Leo núi nhanh", 45, "PLACEHOLDER_URL_6_6"));
                    break;
                case 6: // Ngày 29
                    tvPhaseTitle.setText("Ngày 29: Ôn tập tổng hợp");
                    tvPhaseDescription.setText("Kết hợp tất cả động tác");
                    exercises.add(new Exercise("Circuit 1: Crunches", "3 hiệp x 20 lần", "Gập bụng", 30, "PLACEHOLDER_URL_7_1"));
                    exercises.add(new Exercise("Circuit 2: Plank", "3 hiệp x 45 giây", "Plank", 45, "PLACEHOLDER_URL_7_2"));
                    exercises.add(new Exercise("Circuit 3: Mountain Climbers", "3 hiệp x 30 giây", "Leo núi", 30, "PLACEHOLDER_URL_7_3"));
                    exercises.add(new Exercise("Circuit 4: Leg Raises", "3 hiệp x 15 lần", "Nâng chân", 30, "PLACEHOLDER_URL_7_4"));
                    exercises.add(new Exercise("Circuit 5: Burpees", "3 hiệp x 12 lần", "Burpees", 30, "PLACEHOLDER_URL_7_5"));
                    exercises.add(new Exercise("Circuit 6: Russian Twists", "3 hiệp x 25 lần", "Xoay thân", 30, "PLACEHOLDER_URL_7_6"));
                    exercises.add(new Exercise("Circuit 7: Bicycle Crunches", "3 hiệp x 20 lần", "Gập bụng xe đạp", 30, "PLACEHOLDER_URL_7_7"));
                    break;
                case 7: // Ngày 30
                    tvPhaseTitle.setText("Ngày 30: Thử thách cuối");
                    tvPhaseDescription.setText("Kiểm tra kết quả đạt được");
                    exercises.add(new Exercise("Max Crunches", "1 phút", "Gập bụng tối đa", 60, "PLACEHOLDER_URL_8_1"));
                    exercises.add(new Exercise("Max Plank", "Càng lâu càng tốt", "Plank tối đa", 90, "PLACEHOLDER_URL_8_2"));
                    exercises.add(new Exercise("Max Burpees", "2 phút", "Burpees tối đa", 120, "PLACEHOLDER_URL_8_3"));
                    exercises.add(new Exercise("Max Mountain Climbers", "1 phút", "Leo núi tối đa", 60, "PLACEHOLDER_URL_8_4"));
                    exercises.add(new Exercise("Max V-Ups", "1 phút", "V-ups tối đa", 60, "PLACEHOLDER_URL_8_5"));
                    exercises.add(new Exercise("Final Circuit", "3 vòng", "Tổng hợp tất cả", 180, "PLACEHOLDER_URL_8_6"));
                    break;
            }
        } else if ("60".equals(programType)) {
            // 60-day program phases
            switch (phaseIndex) {
                case 0: // Tuần 1-2
                    tvPhaseTitle.setText("Tuần 1-2: Nền tảng sức mạnh");
                    tvPhaseDescription.setText("Xây dựng nền tảng cơ bắp");
                    exercises.add(new Exercise("Push-ups", "4 hiệp x 12 lần", "Hít đất cơ bản", 30, "PLACEHOLDER_URL_9_1"));
                    exercises.add(new Exercise("Squats", "4 hiệp x 15 lần", "Squat cơ bản", 30, "PLACEHOLDER_URL_9_2"));
                    exercises.add(new Exercise("Lunges", "3 hiệp x 12 lần/chân", "Lunge", 30, "PLACEHOLDER_URL_9_3"));
                    exercises.add(new Exercise("Plank", "3 hiệp x 45 giây", "Plank", 45, "PLACEHOLDER_URL_9_4"));
                    exercises.add(new Exercise("Dumbbell Rows", "4 hiệp x 12 lần", "Chèo tạ", 30, "PLACEHOLDER_URL_9_5"));
                    exercises.add(new Exercise("Shoulder Press", "3 hiệp x 12 lần", "Đẩy vai", 30, "PLACEHOLDER_URL_9_6"));
                    exercises.add(new Exercise("Bicep Curls", "3 hiệp x 15 lần", "Cuốn tay", 30, "PLACEHOLDER_URL_9_7"));
                    break;
                case 1: // Tuần 3-4
                    tvPhaseTitle.setText("Tuần 3-4: Tập ngực và tay");
                    tvPhaseDescription.setText("Phát triển cơ thể phần trên");
                    exercises.add(new Exercise("Bench Press", "4 hiệp x 10 lần", "Đẩy ngực", 30, "PLACEHOLDER_URL_10_1"));
                    exercises.add(new Exercise("Incline Push-ups", "4 hiệp x 15 lần", "Hít đất nghiêng", 30, "PLACEHOLDER_URL_10_2"));
                    exercises.add(new Exercise("Dips", "3 hiệp x 12 lần", "Chống đẩy xà kép", 30, "PLACEHOLDER_URL_10_3"));
                    exercises.add(new Exercise("Tricep Extensions", "3 hiệp x 15 lần", "Duỗi tay sau", 30, "PLACEHOLDER_URL_10_4"));
                    exercises.add(new Exercise("Hammer Curls", "3 hiệp x 12 lần", "Cuốn tay búa", 30, "PLACEHOLDER_URL_10_5"));
                    exercises.add(new Exercise("Cable Flyes", "3 hiệp x 15 lần", "Kéo cáp ngực", 30, "PLACEHOLDER_URL_10_6"));
                    exercises.add(new Exercise("Diamond Push-ups", "3 hiệp x 10 lần", "Hít đất kim cương", 30, "PLACEHOLDER_URL_10_7"));
                    exercises.add(new Exercise("Chest Dips", "3 hiệp x 12 lần", "Chống đẩy ngực", 30, "PLACEHOLDER_URL_10_8"));
                    break;
                // Add more 60-day phases...
                default:
                    tvPhaseTitle.setText("Đang cập nhật");
                    tvPhaseDescription.setText("Nội dung sẽ được cập nhật sớm");
                    exercises.add(new Exercise("Bài tập 1", "Đang cập nhật", "Mô tả", 30, "PLACEHOLDER_URL"));
                    break;
            }
        } else if ("90".equals(programType)) {
            // 90-day program phases
            switch (phaseIndex) {
                case 0: // Tháng 1
                    tvPhaseTitle.setText("Tháng 1: Giai đoạn thích nghi");
                    tvPhaseDescription.setText("Làm quen với cường độ cao");
                    exercises.add(new Exercise("Full Body Warm-up", "10 phút", "Khởi động toàn thân", 600, "PLACEHOLDER_URL_19_1"));
                    exercises.add(new Exercise("Compound Movements", "4 hiệp x 12 lần", "Động tác phức hợp", 30, "PLACEHOLDER_URL_19_2"));
                    exercises.add(new Exercise("Core Stability", "4 hiệp x 45 giây", "Ổn định core", 45, "PLACEHOLDER_URL_19_3"));
                    exercises.add(new Exercise("Cardio Intervals", "5 hiệp x 2 phút", "Cardio ngắt quãng", 120, "PLACEHOLDER_URL_19_4"));
                    exercises.add(new Exercise("Flexibility Training", "15 phút", "Tăng độ dẻo", 900, "PLACEHOLDER_URL_19_5"));
                    exercises.add(new Exercise("Recovery Stretches", "10 phút", "Giãn cơ phục hồi", 600, "PLACEHOLDER_URL_19_6"));
                    exercises.add(new Exercise("Mobility Drills", "3 hiệp x 10 lần", "Bài tập vận động", 30, "PLACEHOLDER_URL_19_7"));
                    exercises.add(new Exercise("Balance Exercises", "3 hiệp x 30 giây", "Bài tập thăng bằng", 30, "PLACEHOLDER_URL_19_8"));
                    break;
                // Add more 90-day phases...
                default:
                    tvPhaseTitle.setText("Đang cập nhật");
                    tvPhaseDescription.setText("Nội dung sẽ được cập nhật sớm");
                    exercises.add(new Exercise("Bài tập 1", "Đang cập nhật", "Mô tả", 30, "PLACEHOLDER_URL"));
                    break;
            }
        }

        // Add exercises to layout
        int exerciseIndex = 0;
        for (Exercise exercise : exercises) {
            addExerciseCard(exercise, exerciseIndex);
            exerciseIndex++;
        }
        
        // Update stats
        totalExercises = exercises.size();
        totalMinutes = 0;
        for (Exercise ex : exercises) {
            totalMinutes += (ex.durationSeconds / 60);
        }
        totalCalories = totalMinutes * 8; // Estimate 8 calories per minute
        
        TextView tvTotalExercises = findViewById(R.id.tv_total_exercises);
        TextView tvTotalMinutes = findViewById(R.id.tv_total_minutes);
        TextView tvTotalCalories = findViewById(R.id.tv_total_calories);
        
        if (tvTotalExercises != null) tvTotalExercises.setText(String.valueOf(totalExercises));
        if (tvTotalMinutes != null) tvTotalMinutes.setText(String.valueOf(totalMinutes));
        if (tvTotalCalories != null) tvTotalCalories.setText(String.valueOf(totalCalories));
    }

    private void addExerciseCard(Exercise exercise, int exerciseIndex) {
        CardView exerciseCard = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (16 * getResources().getDisplayMetrics().density));
        exerciseCard.setLayoutParams(cardParams);
        exerciseCard.setRadius(16 * getResources().getDisplayMetrics().density);
        exerciseCard.setCardElevation(3 * getResources().getDisplayMetrics().density);
        exerciseCard.setCardBackgroundColor(0xFFFFFFFF);

        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density),
                (int) (12 * getResources().getDisplayMetrics().density));
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Thumbnail with play overlay
        android.widget.FrameLayout thumbnailContainer = new android.widget.FrameLayout(this);
        LinearLayout.LayoutParams thumbParams = new LinearLayout.LayoutParams(
                (int) (100 * getResources().getDisplayMetrics().density),
                (int) (75 * getResources().getDisplayMetrics().density));
        thumbParams.setMargins(0, 0, (int) (16 * getResources().getDisplayMetrics().density), 0);
        thumbnailContainer.setLayoutParams(thumbParams);

        CardView imageCard = new CardView(this);
        imageCard.setLayoutParams(new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));
        imageCard.setRadius(12 * getResources().getDisplayMetrics().density);

        ImageView thumbnailImage = new ImageView(this);
        thumbnailImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        thumbnailImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        if (!exercise.videoUrl.startsWith("PLACEHOLDER")) {
            YouTubeHelper.loadThumbnail(thumbnailImage, exercise.videoUrl, YouTubeHelper.ThumbnailQuality.MEDIUM);
        } else {
            thumbnailImage.setBackgroundColor(0xFFF0F0F0);
        }
        
        imageCard.addView(thumbnailImage);
        
        // Play icon overlay
        CardView playOverlay = new CardView(this);
        android.widget.FrameLayout.LayoutParams playParams = new android.widget.FrameLayout.LayoutParams(
                (int) (40 * getResources().getDisplayMetrics().density),
                (int) (40 * getResources().getDisplayMetrics().density));
        playParams.gravity = android.view.Gravity.CENTER;
        playOverlay.setLayoutParams(playParams);
        playOverlay.setRadius(20 * getResources().getDisplayMetrics().density);
        playOverlay.setCardBackgroundColor(0xCC6FCF97);
        playOverlay.setCardElevation(4 * getResources().getDisplayMetrics().density);
        
        TextView playIcon = new TextView(this);
        playIcon.setText("▶");
        playIcon.setTextColor(0xFFFFFFFF);
        playIcon.setTextSize(18);
        playIcon.setGravity(android.view.Gravity.CENTER);
        playIcon.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        playOverlay.addView(playIcon);
        
        thumbnailContainer.addView(imageCard);
        thumbnailContainer.addView(playOverlay);

        // Exercise info
        LinearLayout infoLayout = new LinearLayout(this);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        infoLayout.setLayoutParams(infoParams);

        TextView tvName = new TextView(this);
        tvName.setText(exercise.name);
        tvName.setTextColor(0xFF1A1A1A);
        tvName.setTextSize(16);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvSets = new TextView(this);
        tvSets.setText(exercise.sets);
        tvSets.setTextColor(0xFF6FCF97);
        tvSets.setTextSize(14);
        tvSets.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams setsParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        setsParams.setMargins(0, (int) (6 * getResources().getDisplayMetrics().density), 0, 0);
        tvSets.setLayoutParams(setsParams);

        TextView tvDescription = new TextView(this);
        tvDescription.setText(exercise.description);
        tvDescription.setTextColor(0xFF999999);
        tvDescription.setTextSize(13);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        descParams.setMargins(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        tvDescription.setLayoutParams(descParams);

        infoLayout.addView(tvName);
        infoLayout.addView(tvSets);
        infoLayout.addView(tvDescription);

        // Arrow icon
        TextView btnArrow = new TextView(this);
        btnArrow.setText("›");
        btnArrow.setTextColor(0xFFCCCCCC);
        btnArrow.setTextSize(32);

        contentLayout.addView(thumbnailContainer);
        contentLayout.addView(infoLayout);
        contentLayout.addView(btnArrow);

        exerciseCard.addView(contentLayout);

        exerciseCard.setOnClickListener(v -> {
            // Open exercise session activity
            Intent intent = new Intent(this, ProgramExerciseSessionActivity.class);
            intent.putExtra("program_type", programType);
            intent.putExtra("phase_index", phaseIndex);
            intent.putExtra("exercise_index", exerciseIndex);
            startActivity(intent);
        });

        layoutExercises.addView(exerciseCard);
    }

    private void openYouTubeVideo(String videoUrl) {
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
                Toast.makeText(this, "Không thể mở video", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class Exercise {
        String name;
        String sets;
        String description;
        int durationSeconds;
        String videoUrl;

        Exercise(String name, String sets, String description, int durationSeconds, String videoUrl) {
            this.name = name;
            this.sets = sets;
            this.description = description;
            this.durationSeconds = durationSeconds;
            this.videoUrl = videoUrl;
        }
    }
}
