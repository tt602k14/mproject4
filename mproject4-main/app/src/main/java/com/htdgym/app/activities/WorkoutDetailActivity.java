package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.api.ApiClient;
import com.htdgym.app.utils.YouTubeHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import com.htdgym.app.activities.BaseActivity;

public class WorkoutDetailActivity extends BaseActivity {

    private ImageView btnBack;
    private TextView tvWorkoutTitle, tvTotalExercises, tvDuration, tvCalories;
    private LinearLayout layoutExerciseSets;
    private CardView btnStartWorkout;
    
    private String workoutCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        workoutCategory = getIntent().getStringExtra("category");
        if (workoutCategory == null) {
            workoutCategory = "all";
        }

        initViews();
        setupClickListeners();
        loadWorkoutData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvWorkoutTitle = findViewById(R.id.tv_workout_title);
        tvTotalExercises = findViewById(R.id.tv_total_exercises);
        tvDuration = findViewById(R.id.tv_duration);
        tvCalories = findViewById(R.id.tv_calories);
        layoutExerciseSets = findViewById(R.id.layout_exercise_sets);
        btnStartWorkout = findViewById(R.id.btn_start_workout);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnStartWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(this, FullWorkoutSessionActivity.class);
            intent.putExtra("category", workoutCategory);
            startActivity(intent);
        });
    }

    private void loadWorkoutData() {
        // Load from API
        String params = "category=" + workoutCategory;
        
        ApiClient.get("workouts.php", params, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getBoolean("success")) {
                        JSONArray workouts = jsonResponse.getJSONArray("data");
                        
                        if (workouts.length() > 0) {
                            JSONObject workout = workouts.getJSONObject(0);
                            
                            // Update UI with API data
                            tvWorkoutTitle.setText(workout.getString("name"));
                            tvDuration.setText(String.valueOf(workout.getInt("duration")));
                            tvCalories.setText(String.valueOf(workout.getInt("calories")));
                            
                            // Load exercises for this workout
                            loadExercisesFromAPI(workout.getInt("id"));
                        } else {
                            // No data from API, use local data
                            loadLocalWorkoutData();
                        }
                    } else {
                        loadLocalWorkoutData();
                    }
                } catch (Exception e) {
                    Log.e("WorkoutDetail", "Error parsing API response", e);
                    loadLocalWorkoutData();
                }
            }

            @Override
            public void onError(String error) {
                Log.e("WorkoutDetail", "API Error: " + error);
                // Fallback to local data
                loadLocalWorkoutData();
            }
        });
    }
    
    private void loadExercisesFromAPI(int workoutId) {
        String params = "id=" + workoutId;
        
        ApiClient.get("workouts.php", params, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.getBoolean("success")) {
                        JSONObject workout = jsonResponse.getJSONObject("data");
                        
                        if (workout.has("exercises")) {
                            JSONArray exercises = workout.getJSONArray("exercises");
                            tvTotalExercises.setText(String.valueOf(exercises.length()));
                            
                            for (int i = 0; i < exercises.length(); i++) {
                                JSONObject exercise = exercises.getJSONObject(i);
                                addExerciseSet(
                                    exercise.getString("name"),
                                    exercise.getString("sets"),
                                    exercise.getString("description")
                                );
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("WorkoutDetail", "Error loading exercises", e);
                }
            }

            @Override
            public void onError(String error) {
                Log.e("WorkoutDetail", "Error loading exercises: " + error);
            }
        });
    }
    
    private void loadLocalWorkoutData() {
        switch (workoutCategory) {
            case "chest":
                tvWorkoutTitle.setText("Bài tập ngực");
                tvTotalExercises.setText("8");
                tvDuration.setText("35");
                tvCalories.setText("280");
                loadChestExercises();
                break;
            case "shoulder":
                tvWorkoutTitle.setText("Bài tập vai");
                tvTotalExercises.setText("7");
                tvDuration.setText("30");
                tvCalories.setText("240");
                loadShoulderExercises();
                break;
            case "legs":
                tvWorkoutTitle.setText("Bài tập chân");
                tvTotalExercises.setText("10");
                tvDuration.setText("45");
                tvCalories.setText("350");
                loadLegsExercises();
                break;
            case "abs":
                tvWorkoutTitle.setText("Bài tập bụng");
                tvTotalExercises.setText("9");
                tvDuration.setText("25");
                tvCalories.setText("200");
                loadAbsExercises();
                break;
            case "back":
                tvWorkoutTitle.setText("Bài tập lưng");
                tvTotalExercises.setText("8");
                tvDuration.setText("35");
                tvCalories.setText("270");
                loadBackExercises();
                break;
            default:
                tvWorkoutTitle.setText("Tất cả bài tập");
                tvTotalExercises.setText("10");
                tvDuration.setText("40");
                tvCalories.setText("300");
                loadAllExercises();
                break;
        }
    }

    private void loadChestExercises() {
        addExerciseSet("Hít đất cơ bản", "4 x 15 lần", "Giữ thân thẳng, hạ người xuống và đẩy lên", "https://youtu.be/-R5sH2iG9Gw");
        addExerciseSet("Hít đất nghiêng", "3 x 12 lần", "Đặt tay lên bề mặt cao hơn", "https://youtu.be/4dF1DOWzf20");
        addExerciseSet("Hít đất rộng", "3 x 12 lần", "Đặt tay rộng hơn vai", "https://www.youtube.com/shorts/hX7yJcwfQWk?feature=share");
        addExerciseSet("Hít đất kim cương", "3 x 10 lần", "Đặt tay sát nhau tạo hình kim cương", "https://youtu.be/J0DnG1_S92I");
        addExerciseSet("Hít đất xuống dốc", "3 x 12 lần", "Đặt chân lên cao, tay ở dưới", "https://youtu.be/SKPab2YC8BE");
        addExerciseSet("Chống đẩy ngực", "3 x 10 lần", "Dùng ghế hoặc xà kép, hạ người xuống", "https://youtu.be/6BBemoyGI-8");
        addExerciseSet("Ép ngực", "3 x 15 lần", "Ép hai tay vào nhau trước ngực", "https://youtu.be/Xf83QChAoIE");
        addExerciseSet("Giãn cơ ngực", "2 x 30 giây", "Duỗi tay ra sau để giãn ngực", "https://youtu.be/4Aec59oC09Q");
    }

    private void loadShoulderExercises() {
        addExerciseSet("Hít đất vai", "4 x 12 lần", "Tư thế chữ V ngược, hít đất", "https://youtu.be/d1HZBdD0idE");
        addExerciseSet("Chạm vai", "3 x 20 lần", "Plank và chạm tay vào vai đối diện", "https://youtu.be/qp75z3XInOI");
        addExerciseSet("Vòng tay", "3 x 30 giây", "Xoay tay theo vòng tròn", "https://youtu.be/UVMEnIaY8aU");
        addExerciseSet("Nâng tay ngang", "3 x 15 lần", "Nâng tay ra hai bên đến ngang vai", "https://youtu.be/XPPfnSEATJA");
        addExerciseSet("Nâng tay trước", "3 x 15 lần", "Nâng tay lên phía trước", "https://youtu.be/hRJ6tR5-if0");
        addExerciseSet("Đẩy vai", "4 x 12 lần", "Đẩy tạ hoặc chai nước lên trên đầu", "https://www.youtube.com/shorts/ZICoJhVzFCc?feature=share");
        addExerciseSet("Giãn cơ vai", "2 x 30 giây", "Các động tác giãn vai", "https://youtu.be/kbAes2QDMXM");
    }

    private void loadLegsExercises() {
        addExerciseSet("Gánh đùi", "4 x 20 lần", "Hạ người xuống như ngồi ghế", "https://youtu.be/XtoO9YwNEqA");
        addExerciseSet("Chùng chân", "3 x 15 lần/chân", "Bước ra trước và hạ người xuống", "https://youtu.be/QOVaHwm-Q6U");
        addExerciseSet("Gánh đùi nhảy", "3 x 15 lần", "Squat và nhảy lên cao", "https://youtu.be/A-cFYWvaHr0");
        addExerciseSet("Ngồi tường", "3 x 45 giây", "Tựa lưng vào tường, ngồi 90 độ", "https://youtu.be/cWTZ8Am1Ee0");
        addExerciseSet("Nâng bắp chân", "4 x 20 lần", "Đứng trên mũi chân", "https://www.youtube.com/shorts/a-x_NR-ibos?feature=share");
        addExerciseSet("Chùng chân ngang", "3 x 12 lần/chân", "Bước sang bên và hạ người", "https://www.youtube.com/shorts/nIk1evRzlyA?feature=share");
        addExerciseSet("Gánh đùi Bulgaria", "3 x 12 lần/chân", "Một chân lên ghế, chân kia squat", "https://www.youtube.com/shorts/AaKASiVCZP4?feature=share");
        addExerciseSet("Nâng mông", "4 x 15 lần", "Nằm ngửa, nâng hông lên cao", "https://www.youtube.com/shorts/39NOLur3pSs?feature=share");
        addExerciseSet("Bước lên bậc", "3 x 15 lần/chân", "Bước lên ghế hoặc bậc thang", "https://www.youtube.com/shorts/htUOXeEZUiI?feature=share");
        addExerciseSet("Giãn cơ chân", "2 x 30 giây", "Các động tác giãn chân", "https://youtu.be/g_tea8ZNk5A");
    }

    private void loadAbsExercises() {
        addExerciseSet("Gập bụng", "4 x 20 lần", "Nằm ngửa, gập người lên", "https://youtu.be/Y8VflnViz78");
        addExerciseSet("Plank cơ bản", "3 x 60 giây", "Giữ thân thẳng trên khuỷu tay", "https://youtu.be/pSHjTRCQxIw");
        addExerciseSet("Gập bụng xe đạp", "3 x 20 lần", "Nằm ngửa, đạp chân như đạp xe", "https://www.youtube.com/shorts/4h9WoYOVqw4?feature=share");
        addExerciseSet("Nâng chân", "3 x 15 lần", "Nằm ngửa, nâng chân lên cao", "https://www.youtube.com/shorts/HFHn5AXeJgI?feature=share");
        addExerciseSet("Xoay người Nga", "3 x 30 lần", "Ngồi, xoay thân sang hai bên", "https://youtu.be/aKoJRKucDJY");
        addExerciseSet("Leo núi", "3 x 30 giây", "Tư thế plank, kéo đầu gối lên ngực", "https://youtu.be/nmwgirgXLYM");
        addExerciseSet("Plank nghiêng", "2 x 45 giây/bên", "Plank nghiêng sang một bên", "https://www.youtube.com/shorts/vLtwnh-X82s?feature=share");
        addExerciseSet("Đá chân nhanh", "3 x 30 giây", "Nằm ngửa, đá chân lên xuống", "https://www.youtube.com/shorts/dottsAhnC3w?feature=share");
        addExerciseSet("Giãn cơ bụng", "2 x 30 giây", "Các động tác giãn bụng", "https://youtu.be/7Rl_E7L7rQY");
    }

    private void loadBackExercises() {
        addExerciseSet("Tư thế siêu nhân", "4 x 15 lần", "Nằm sấp, nâng tay chân lên cao", "https://youtu.be/7SLbUk-qTTM");
        addExerciseSet("Duỗi lưng", "3 x 15 lần", "Nằm sấp, nâng thân người lên", "https://youtu.be/RqcOCBb4arc");
        addExerciseSet("Thiên thần ngược", "3 x 12 lần", "Nằm sấp, vẫy tay như thiên thần", "https://youtu.be/LbTx6Y4ALZ0");
        addExerciseSet("Nâng tay chữ Y", "3 x 12 lần", "Nằm sấp, nâng tay tạo hình chữ Y", "https://www.youtube.com/shorts/iK22GwXJji0?feature=share");
        addExerciseSet("Nâng tay chữ T", "3 x 12 lần", "Nằm sấp, nâng tay tạo hình chữ T", "https://www.youtube.com/shorts/i-jEU2pBdzM?feature=share");
        addExerciseSet("Chó chim", "3 x 12 lần/bên", "Quỳ, duỗi tay chân đối diện", "https://www.youtube.com/shorts/p8qrmBvewls?feature=share");
        addExerciseSet("Mèo - Bò", "3 x 15 lần", "Quỳ, uốn cong và võng lưng", "https://www.youtube.com/shorts/FDckLem5r44?feature=share");
        addExerciseSet("Giãn cơ lưng", "2 x 30 giây", "Các động tác giãn cơ lưng", "https://youtu.be/DWmGArQBtFI");
    }

    private void loadAllExercises() {
        addExerciseSet("Burpees", "4 x 15 lần", "Bài tập toàn thân", "https://youtu.be/JZQA08SlJnM");
        addExerciseSet("Hít đất", "3 x 15 lần", "Hít đất cơ bản", "https://youtu.be/-R5sH2iG9Gw");
        addExerciseSet("Gánh đùi", "4 x 20 lần", "Squat cơ bản", "https://youtu.be/XtoO9YwNEqA");
        addExerciseSet("Plank", "3 x 60 giây", "Giữ thân thẳng", "https://youtu.be/pSHjTRCQxIw");
        addExerciseSet("Chùng chân", "3 x 15 lần/chân", "Lunge cơ bản", "https://youtu.be/QOVaHwm-Q6U");
        addExerciseSet("Leo núi", "3 x 30 giây", "Mountain climbers", "https://youtu.be/nmwgirgXLYM");
        addExerciseSet("Bật nhảy", "3 x 30 lần", "Jumping jacks", "https://youtu.be/c4DAnQ6DtF8");
        addExerciseSet("Chạy tại chỗ", "3 x 30 giây", "Nâng cao đùi", "https://youtu.be/QmlBTEvBGMw");
        addExerciseSet("Gập bụng", "3 x 20 lần", "Crunches cơ bản", "https://youtu.be/Y8VflnViz78");
        addExerciseSet("Giãn cơ", "2 x 30 giây", "Thư giãn toàn thân", "https://youtu.be/5Qv2T8VusME");
    }

    private void addExerciseSet(String exerciseName, String sets, String description) {
        addExerciseSet(exerciseName, sets, description, "https://youtu.be/-R5sH2iG9Gw"); // Default video
    }
    
    private void addExerciseSet(String exerciseName, String sets, String description, String videoUrl) {
        CardView exerciseCard = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (16 * getResources().getDisplayMetrics().density));
        exerciseCard.setLayoutParams(cardParams);
        exerciseCard.setRadius(16 * getResources().getDisplayMetrics().density);
        exerciseCard.setCardElevation(6 * getResources().getDisplayMetrics().density);
        exerciseCard.setCardBackgroundColor(0xFFFFFFFF);

        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(
                (int) (20 * getResources().getDisplayMetrics().density),
                (int) (20 * getResources().getDisplayMetrics().density),
                (int) (20 * getResources().getDisplayMetrics().density),
                (int) (20 * getResources().getDisplayMetrics().density));
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Exercise thumbnail image with gradient overlay
        CardView iconCard = new CardView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                (int) (90 * getResources().getDisplayMetrics().density),
                (int) (70 * getResources().getDisplayMetrics().density));
        iconParams.setMargins(0, 0, (int) (20 * getResources().getDisplayMetrics().density), 0);
        iconCard.setLayoutParams(iconParams);
        iconCard.setRadius(12 * getResources().getDisplayMetrics().density);
        iconCard.setCardElevation(4 * getResources().getDisplayMetrics().density);

        ImageView thumbnailImage = new ImageView(this);
        thumbnailImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        thumbnailImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        // Load YouTube thumbnail with rounded corners
        YouTubeHelper.loadThumbnailWithRoundedCorners(
            thumbnailImage, 
            videoUrl, 
            YouTubeHelper.ThumbnailQuality.MEDIUM,
            (int) (12 * getResources().getDisplayMetrics().density),
            getExerciseIcon(exerciseName)
        );
        
        iconCard.addView(thumbnailImage);

        // Exercise info with better typography
        LinearLayout infoLayout = new LinearLayout(this);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        infoLayout.setLayoutParams(infoParams);

        // Exercise name with emoji
        TextView tvName = new TextView(this);
        tvName.setText(getExerciseEmoji(exerciseName) + " " + exerciseName);
        tvName.setTextColor(0xFF1A1A1A);
        tvName.setTextSize(18);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setLetterSpacing(0.02f);

        // Sets with background
        TextView tvSets = new TextView(this);
        tvSets.setText(sets);
        tvSets.setTextColor(0xFFFFFFFF);
        tvSets.setTextSize(13);
        tvSets.setTypeface(null, android.graphics.Typeface.BOLD);
        tvSets.setBackground(getResources().getDrawable(R.drawable.badge_premium));
        tvSets.setPadding(
            (int) (12 * getResources().getDisplayMetrics().density),
            (int) (6 * getResources().getDisplayMetrics().density),
            (int) (12 * getResources().getDisplayMetrics().density),
            (int) (6 * getResources().getDisplayMetrics().density)
        );
        LinearLayout.LayoutParams setsParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        setsParams.setMargins(0, (int) (8 * getResources().getDisplayMetrics().density), 0, 0);
        tvSets.setLayoutParams(setsParams);

        // Description with better styling
        TextView tvDesc = new TextView(this);
        tvDesc.setText(description);
        tvDesc.setTextColor(0xFF666666);
        tvDesc.setTextSize(14);
        tvDesc.setMaxLines(2);
        tvDesc.setEllipsize(android.text.TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        descParams.setMargins(0, (int) (6 * getResources().getDisplayMetrics().density), 0, 0);
        tvDesc.setLayoutParams(descParams);

        // Duration info
        TextView tvDuration = new TextView(this);
        tvDuration.setText("⏱️ " + getDurationFromExercise(exerciseName) + "s");
        tvDuration.setTextColor(0xFF6FCF97);
        tvDuration.setTextSize(12);
        tvDuration.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams durationParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        durationParams.setMargins(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        tvDuration.setLayoutParams(durationParams);

        infoLayout.addView(tvName);
        infoLayout.addView(tvSets);
        infoLayout.addView(tvDesc);
        infoLayout.addView(tvDuration);

        // Play button with gradient background
        CardView playButtonCard = new CardView(this);
        LinearLayout.LayoutParams playParams = new LinearLayout.LayoutParams(
                (int) (50 * getResources().getDisplayMetrics().density),
                (int) (50 * getResources().getDisplayMetrics().density));
        playButtonCard.setLayoutParams(playParams);
        playButtonCard.setRadius(25 * getResources().getDisplayMetrics().density);
        playButtonCard.setCardElevation(4 * getResources().getDisplayMetrics().density);
        playButtonCard.setCardBackgroundColor(0xFF6FCF97);

        TextView btnPlay = new TextView(this);
        btnPlay.setText("▶");
        btnPlay.setTextColor(0xFFFFFFFF);
        btnPlay.setTextSize(20);
        btnPlay.setGravity(android.view.Gravity.CENTER);
        btnPlay.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        
        playButtonCard.addView(btnPlay);
        
        // Click play button to open YouTube
        playButtonCard.setOnClickListener(v -> openYouTubeVideo(videoUrl));

        contentLayout.addView(iconCard);
        contentLayout.addView(infoLayout);
        contentLayout.addView(playButtonCard);

        exerciseCard.addView(contentLayout);

        exerciseCard.setOnClickListener(v -> {
            // Open ExerciseVideoActivity with beautiful UI
            Intent intent = new Intent(this, ExerciseVideoActivity.class);
            intent.putExtra("exercise_name", exerciseName);
            intent.putExtra("exercise_sets", sets);
            intent.putExtra("video_url", videoUrl);
            intent.putExtra("duration_seconds", getDurationFromExercise(exerciseName));
            intent.putExtra("current_exercise", layoutExerciseSets.getChildCount());
            intent.putExtra("total_exercises", Integer.parseInt(tvTotalExercises.getText().toString()));
            startActivity(intent);
        });

        layoutExerciseSets.addView(exerciseCard);
    }
    
    /**
     * Mở video YouTube
     */
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
                Toast.makeText(this, "Không thể mở video", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * Tính thời gian tập luyện dựa trên tên bài tập
     */
    private int getDurationFromExercise(String exerciseName) {
        if (exerciseName.contains("Plank") || exerciseName.contains("Ngồi tường")) {
            return 60; // 1 minute for static exercises
        } else if (exerciseName.contains("Burpees") || exerciseName.contains("Leo núi") || 
                   exerciseName.contains("Bật nhảy") || exerciseName.contains("Chạy tại chỗ")) {
            return 30; // 30 seconds for high intensity
        } else if (exerciseName.contains("Giãn") || exerciseName.contains("Thư giãn")) {
            return 45; // 45 seconds for stretching
        } else {
            return 40; // 40 seconds for regular exercises
        }
    }
    
    /**
     * Lấy emoji phù hợp cho bài tập
     */
    private String getExerciseEmoji(String exerciseName) {
        if (exerciseName.contains("Hít đất") || exerciseName.contains("ngực")) {
            return "💪";
        } else if (exerciseName.contains("Gánh đùi") || exerciseName.contains("Chùng chân") || exerciseName.contains("chân")) {
            return "🦵";
        } else if (exerciseName.contains("Gập bụng") || exerciseName.contains("Plank") || exerciseName.contains("bụng")) {
            return "🔥";
        } else if (exerciseName.contains("vai") || exerciseName.contains("Nâng tay")) {
            return "🏋️";
        } else if (exerciseName.contains("lưng") || exerciseName.contains("Siêu nhân")) {
            return "🏃";
        } else if (exerciseName.contains("Burpees") || exerciseName.contains("Leo núi")) {
            return "⚡";
        } else if (exerciseName.contains("Giãn") || exerciseName.contains("Thư giãn")) {
            return "🧘";
        } else {
            return "💯";
        }
    }
    
    /**
     * Lấy icon drawable cho bài tập
     */
    private int getExerciseIcon(String exerciseName) {
        if (exerciseName.contains("Hít đất") || exerciseName.contains("ngực")) {
            return R.drawable.ic_exercise_chest;
        } else if (exerciseName.contains("Gánh đùi") || exerciseName.contains("Chùng chân") || exerciseName.contains("chân")) {
            return R.drawable.ic_exercise_legs;
        } else if (exerciseName.contains("Gập bụng") || exerciseName.contains("Plank") || exerciseName.contains("bụng")) {
            return R.drawable.ic_exercise_abs;
        } else if (exerciseName.contains("lưng") || exerciseName.contains("Siêu nhân")) {
            return R.drawable.ic_exercise_back;
        } else if (exerciseName.contains("Burpees") || exerciseName.contains("Leo núi") || exerciseName.contains("Bật nhảy")) {
            return R.drawable.ic_exercise_cardio;
        } else {
            return R.drawable.ic_exercise_chest;
        }
    }
}
