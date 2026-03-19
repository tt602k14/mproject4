package com.htdgym.app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.R;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.utils.ExerciseDataManager;
import com.htdgym.app.utils.YouTubeThumbnailHelper;

public class ExerciseDetailActivity extends AppCompatActivity {

    private ImageView btnBack, btnFavorite, btnPlayVideo, ivVideoThumbnail;
    private TextView tvExerciseName, tvMuscleGroup, tvSetsReps, tvRestTime, tvDifficulty, tvDescription;
    private TextView tvTimer;
    private Button btnStartTimer, btnPauseTimer, btnResetTimer;
    private WebView webviewVideo;
    private LinearLayout videoOverlay;
    
    private CountDownTimer restTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 60000; // Default 60 seconds
    private long originalTimeInMillis = 60000;
    private boolean isFavorite = false;
    private String videoUrl;
    private Exercise currentExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_exercise_detail);

            initViews();
            setupClickListeners();
            loadExerciseData();
            setupWebView();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        try {
            btnBack = findViewById(R.id.btn_back);
            btnFavorite = findViewById(R.id.btn_favorite);
            btnPlayVideo = findViewById(R.id.btn_play_video);
            ivVideoThumbnail = findViewById(R.id.iv_video_thumbnail);
            tvExerciseName = findViewById(R.id.tv_exercise_name);
            tvMuscleGroup = findViewById(R.id.tv_muscle_group);
            tvSetsReps = findViewById(R.id.tv_sets_reps);
            tvRestTime = findViewById(R.id.tv_rest_time);
            tvDifficulty = findViewById(R.id.tv_difficulty);
            tvDescription = findViewById(R.id.tv_description);
            tvTimer = findViewById(R.id.tv_timer);
            btnStartTimer = findViewById(R.id.btn_start_timer);
            btnPauseTimer = findViewById(R.id.btn_pause_timer);
            btnResetTimer = findViewById(R.id.btn_reset_timer);
            webviewVideo = findViewById(R.id.webview_video);
            videoOverlay = findViewById(R.id.video_overlay);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo giao diện: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupClickListeners() {
        try {
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> finish());
            }
            
            if (btnFavorite != null) {
                btnFavorite.setOnClickListener(v -> toggleFavorite());
            }
            
            if (btnPlayVideo != null) {
                btnPlayVideo.setOnClickListener(v -> playVideo());
            }
            
            if (btnStartTimer != null) {
                btnStartTimer.setOnClickListener(v -> startTimer());
            }
            
            if (btnPauseTimer != null) {
                btnPauseTimer.setOnClickListener(v -> pauseTimer());
            }
            
            if (btnResetTimer != null) {
                btnResetTimer.setOnClickListener(v -> resetTimer());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi thiết lập sự kiện: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings webSettings = webviewVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        
        webviewVideo.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Video loaded successfully
            }
        });
    }

    private void loadExerciseData() {
        try {
            // Get data from intent
            String exerciseName = getIntent().getStringExtra("exercise_name");
            String muscleGroup = getIntent().getStringExtra("muscle_group");
            String setsReps = getIntent().getStringExtra("sets_reps");
            String restTime = getIntent().getStringExtra("rest_time");
            String difficulty = getIntent().getStringExtra("difficulty");
            videoUrl = getIntent().getStringExtra("video_url");

            // Try to find exercise in data manager for complete info
            if (exerciseName != null) {
                currentExercise = findExerciseByName(exerciseName);
                if (currentExercise != null) {
                    exerciseName = currentExercise.getName();
                    muscleGroup = currentExercise.getMuscleGroup();
                    setsReps = currentExercise.getSetsReps();
                    restTime = currentExercise.getRestTime();
                    difficulty = currentExercise.getDifficulty();
                    videoUrl = currentExercise.getVideoUrl();
                }
            }

            // Set data to views with null checks
            if (tvExerciseName != null && exerciseName != null) {
                tvExerciseName.setText(exerciseName);
            }
            
            if (tvMuscleGroup != null && muscleGroup != null) {
                String muscleGroupText = getMuscleGroupEmoji(muscleGroup) + " " + getMuscleGroupName(muscleGroup);
                tvMuscleGroup.setText(muscleGroupText);
            }
            
            if (tvSetsReps != null && setsReps != null) {
                tvSetsReps.setText(setsReps);
            }
            
            if (tvRestTime != null && restTime != null) {
                tvRestTime.setText(restTime);
                // Parse rest time for timer
                parseRestTime(restTime);
            }
            
            if (tvDifficulty != null && difficulty != null) {
                tvDifficulty.setText(difficulty);
            }

            // Set description based on exercise name
            if (tvDescription != null) {
                tvDescription.setText(getExerciseDescription(exerciseName));
            }
            
            // Load video thumbnail if available
            if (ivVideoThumbnail != null && videoUrl != null && !videoUrl.isEmpty()) {
                YouTubeThumbnailHelper.loadThumbnail(ivVideoThumbnail, videoUrl, "hqdefault");
            }
            
            // Update timer display
            updateTimerDisplay();
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi tải dữ liệu bài tập: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Exercise findExerciseByName(String name) {
        for (Exercise exercise : ExerciseDataManager.getAllExercises()) {
            if (exercise.getName().equalsIgnoreCase(name)) {
                return exercise;
            }
        }
        return null;
    }

    private String getMuscleGroupEmoji(String muscleGroup) {
        if (muscleGroup == null) return "💪";
        switch (muscleGroup.toLowerCase()) {
            case "chest": return "💪";
            case "shoulder": return "🏋️";
            case "legs": return "🦵";
            case "abs": return "🔥";
            case "back": return "🏃";
            case "hiit": return "⚡";
            default: return "💪";
        }
    }

    private String getMuscleGroupName(String muscleGroup) {
        if (muscleGroup == null) return "Tổng thể";
        switch (muscleGroup.toLowerCase()) {
            case "chest": return "Ngực";
            case "shoulder": return "Vai";
            case "legs": return "Chân";
            case "abs": return "Bụng";
            case "back": return "Lưng";
            case "hiit": return "HIIT";
            default: return "Tổng thể";
        }
    }

    private void parseRestTime(String restTime) {
        if (restTime == null) return;
        
        try {
            // Extract number from rest time (e.g., "60s" -> 60)
            String timeStr = restTime.replaceAll("[^0-9]", "");
            if (!timeStr.isEmpty()) {
                int seconds = Integer.parseInt(timeStr);
                originalTimeInMillis = seconds * 1000L;
                timeLeftInMillis = originalTimeInMillis;
            }
        } catch (NumberFormatException e) {
            // Default to 60 seconds if parsing fails
            originalTimeInMillis = 60000;
            timeLeftInMillis = 60000;
        }
    }

    private void toggleFavorite() {
        isFavorite = !isFavorite;
        btnFavorite.setImageResource(isFavorite ? 
            R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);
        
        String message = isFavorite ? "Đã thêm vào yêu thích ❤️" : "Đã bỏ khỏi yêu thích";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void playVideo() {
        if (videoUrl != null && !videoUrl.isEmpty()) {
            // Show WebView and hide overlay
            webviewVideo.setVisibility(WebView.VISIBLE);
            videoOverlay.setVisibility(LinearLayout.GONE);
            
            // Convert YouTube URL to embed format
            String embedUrl = convertToEmbedUrl(videoUrl);
            String html = "<html><body style='margin:0;padding:0;background:#000;'>" +
                    "<iframe width='100%' height='100%' src='" + embedUrl + 
                    "' frameborder='0' allowfullscreen></iframe></body></html>";
            
            webviewVideo.loadData(html, "text/html", "utf-8");
        } else {
            Toast.makeText(this, "Video không khả dụng", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertToEmbedUrl(String youtubeUrl) {
        if (youtubeUrl.contains("youtu.be/")) {
            String videoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("/") + 1);
            if (videoId.contains("?")) {
                videoId = videoId.substring(0, videoId.indexOf("?"));
            }
            return "https://www.youtube.com/embed/" + videoId + "?autoplay=1";
        } else if (youtubeUrl.contains("youtube.com/watch?v=")) {
            String videoId = youtubeUrl.substring(youtubeUrl.indexOf("v=") + 2);
            if (videoId.contains("&")) {
                videoId = videoId.substring(0, videoId.indexOf("&"));
            }
            return "https://www.youtube.com/embed/" + videoId + "?autoplay=1";
        }
        return youtubeUrl;
    }

    private void startTimer() {
        if (!isTimerRunning) {
            restTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimerDisplay();
                }

                @Override
                public void onFinish() {
                    isTimerRunning = false;
                    btnStartTimer.setText("▶️ Bắt đầu");
                    Toast.makeText(ExerciseDetailActivity.this, "⏰ Hết thời gian nghỉ! Tiếp tục tập luyện!", Toast.LENGTH_LONG).show();
                    
                    // Reset timer to original time
                    timeLeftInMillis = originalTimeInMillis;
                    updateTimerDisplay();
                }
            }.start();
            
            isTimerRunning = true;
            btnStartTimer.setText("⏸️ Tạm dừng");
        } else {
            pauseTimer();
        }
    }

    private void pauseTimer() {
        if (restTimer != null) {
            restTimer.cancel();
        }
        isTimerRunning = false;
        btnStartTimer.setText("▶️ Tiếp tục");
    }

    private void resetTimer() {
        if (restTimer != null) {
            restTimer.cancel();
        }
        isTimerRunning = false;
        timeLeftInMillis = originalTimeInMillis;
        updateTimerDisplay();
        btnStartTimer.setText("▶️ Bắt đầu");
        Toast.makeText(this, "🔄 Đã reset bộ đếm thời gian", Toast.LENGTH_SHORT).show();
    }

    private void updateTimerDisplay() {
        try {
            if (tvTimer != null) {
                int minutes = (int) (timeLeftInMillis / 1000) / 60;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;
                
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                tvTimer.setText(timeFormatted);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startWorkoutSession() {
        try {
            // Start workout session activity
            Intent intent = new Intent(this, WorkoutSessionActivity.class);
            if (currentExercise != null) {
                intent.putExtra("exercise_name", currentExercise.getName());
                intent.putExtra("sets_reps", currentExercise.getSetsReps());
                intent.putExtra("rest_time", currentExercise.getRestTime());
                intent.putExtra("video_url", currentExercise.getVideoUrl());
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Không thể mở phiên tập luyện: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addToPlan() {
        Toast.makeText(this, "📋 Đã thêm bài tập vào kế hoạch tập luyện!", Toast.LENGTH_SHORT).show();
        // TODO: Implement add to workout plan functionality
    }

    private String getExerciseDescription(String exerciseName) {
        if (exerciseName == null) return "Mô tả bài tập sẽ được cập nhật.";
        
        switch (exerciseName.toLowerCase()) {
            case "hít đất cơ bản":
            case "push-up":
                return "🔥 Bài tập hít đất cơ bản giúp phát triển cơ ngực, vai và tay sau.\n\n" +
                       "✅ Cách thực hiện:\n" +
                       "• Nằm sấp, tay đặt rộng bằng vai\n" +
                       "• Giữ thân người thẳng từ đầu đến chân\n" +
                       "• Hạ xuống cho đến khi ngực gần chạm đất\n" +
                       "• Đẩy mạnh lên vị trí ban đầu\n\n" +
                       "⚠️ Lưu ý: Thở ra khi đẩy lên, thở vào khi hạ xuống";
                       
            case "gánh đùi":
            case "squat":
                return "🦵 Squat là bài tập cơ bản cho chân và mông, giúp tăng cường sức mạnh toàn thân.\n\n" +
                       "✅ Cách thực hiện:\n" +
                       "• Đứng thẳng, chân rộng bằng vai\n" +
                       "• Hạ xuống như ngồi ghế, đùi song song với sàn\n" +
                       "• Giữ lưng thẳng, ngực hướng lên\n" +
                       "• Đứng lên bằng cách đẩy gót chân\n\n" +
                       "⚠️ Lưu ý: Không để đầu gối vượt quá mũi chân";
                       
            case "plank":
            case "plank cơ bản":
                return "🔥 Plank giúp tăng cường cơ core, cải thiện tư thế và sự ổn định.\n\n" +
                       "✅ Cách thực hiện:\n" +
                       "• Nằm sấp, tựa trên cẳng tay và mũi chân\n" +
                       "• Giữ thân người thẳng như một đường thẳng\n" +
                       "• Siết chặt cơ bụng và mông\n" +
                       "• Thở đều, không nín thở\n\n" +
                       "⚠️ Lưu ý: Không để hông quá cao hoặc quá thấp";
                       
            case "burpees":
                return "⚡ Burpees là bài tập toàn thân cường độ cao, kết hợp cardio và strength.\n\n" +
                       "✅ Cách thực hiện:\n" +
                       "• Đứng thẳng, sau đó squat xuống\n" +
                       "• Đặt tay xuống sàn, nhảy chân ra sau thành plank\n" +
                       "• Thực hiện 1 push-up (tùy chọn)\n" +
                       "• Nhảy chân về phía trước, đứng lên và nhảy cao\n\n" +
                       "⚠️ Lưu ý: Giữ nhịp thở đều, không làm quá nhanh";
                       
            case "gập bụng":
                return "💪 Gập bụng giúp tăng cường cơ bụng trên, tạo định hình cho vùng core.\n\n" +
                       "✅ Cách thực hiện:\n" +
                       "• Nằm ngửa, gập đầu gối 90 độ\n" +
                       "• Đặt tay sau đầu, không kéo cổ\n" +
                       "• Gập bụng lên, vai rời khỏi sàn\n" +
                       "• Hạ xuống chậm, không để vai chạm sàn\n\n" +
                       "⚠️ Lưu ý: Tập trung vào cơ bụng, không dùng đà";
                       
            default:
                return "💪 Bài tập " + exerciseName + " giúp phát triển sức mạnh và thể lực tổng thể.\n\n" +
                       "🎯 Lợi ích:\n" +
                       "• Tăng cường sức mạnh cơ bắp\n" +
                       "• Cải thiện sự dẻo dai và linh hoạt\n" +
                       "• Đốt cháy calories hiệu quả\n" +
                       "• Tăng cường sức khỏe tim mạch\n\n" +
                       "⚠️ Lưu ý: Thực hiện đúng kỹ thuật, khởi động trước khi tập";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (restTimer != null) {
            restTimer.cancel();
        }
    }
}