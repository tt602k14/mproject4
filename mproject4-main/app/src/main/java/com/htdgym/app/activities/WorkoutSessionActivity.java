package com.htdgym.app.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.R;
import com.htdgym.app.utils.YouTubeHelper;
import com.htdgym.app.utils.WorkoutSaver;
import com.bumptech.glide.Glide;

public class WorkoutSessionActivity extends AppCompatActivity {

    private TextView tvExerciseName, tvExerciseDescription, tvTimer, tvInstructions;
    private ImageView ivExerciseThumbnail;
    private ImageButton btnPrevious, btnPlay, btnNext, btnPause, btnStop;
    private WebView webViewVideo;
    
    private String exerciseName, exerciseDescription, videoUrl;
    private int exerciseDuration, exerciseCalories;
    private CountDownTimer countDownTimer;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private long timeRemaining;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session);
        
        initViews();
        getIntentData();
        setupUI();
        setupClickListeners();

        // Lưu ngay khi mở màn hình tập (sau getIntentData để có đủ dữ liệu)
        WorkoutSaver.save(this,
                exerciseName != null ? exerciseName : "Bài tập",
                Math.max(1, exerciseDuration),
                exerciseCalories > 0 ? exerciseCalories : 30);
    }

    private void initViews() {
        tvExerciseName = findViewById(R.id.tv_exercise_name);
        tvExerciseDescription = findViewById(R.id.tv_exercise_description);
        tvTimer = findViewById(R.id.tv_timer);
        tvInstructions = findViewById(R.id.tv_instructions);
        ivExerciseThumbnail = findViewById(R.id.iv_exercise_thumbnail);
        webViewVideo = findViewById(R.id.webview_video);
        
        btnPrevious = findViewById(R.id.btn_previous);
        btnPlay = findViewById(R.id.btn_play);
        btnNext = findViewById(R.id.btn_next);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
    }
    private void getIntentData() {
        Intent intent = getIntent();
        exerciseName = intent.getStringExtra("exercise_name");
        exerciseDescription = intent.getStringExtra("exercise_description");
        videoUrl = intent.getStringExtra("video_url");
        exerciseDuration = intent.getIntExtra("exercise_duration", 15);
        exerciseCalories = intent.getIntExtra("exercise_calories", 120);
        
        timeRemaining = exerciseDuration * 60 * 1000; // Convert to milliseconds
    }
    
    private void setupUI() {
        tvExerciseName.setText(exerciseName);
        tvExerciseDescription.setText(exerciseDescription);
        updateTimer();
        
        // Load exercise thumbnail
        if (videoUrl != null && !videoUrl.isEmpty()) {
            String thumbnailUrl = YouTubeHelper.getThumbnailUrl(videoUrl, YouTubeHelper.ThumbnailQuality.MAX);
            Glide.with(this)
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.ic_exercise_program)
                    .error(R.drawable.ic_exercise_program)
                    .into(ivExerciseThumbnail);
        }
        
        // Setup instructions
        tvInstructions.setText("Sẵn sàng để bắt đầu tập luyện với " + exerciseName + "!");
        
        // Setup WebView for YouTube video
        setupWebView();
    }
    
    private void setupWebView() {
        webViewVideo.getSettings().setJavaScriptEnabled(true);
        webViewVideo.setWebViewClient(new WebViewClient());
        
        if (videoUrl != null && !videoUrl.isEmpty()) {
            String videoId = YouTubeHelper.getVideoIdFromUrl(videoUrl);
            if (videoId != null) {
                String embedUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=0&controls=1";
                webViewVideo.loadUrl(embedUrl);
            }
        }
    }
    private void setupClickListeners() {
        btnPlay.setOnClickListener(v -> startWorkout());
        btnPause.setOnClickListener(v -> pauseWorkout());
        btnStop.setOnClickListener(v -> stopWorkout());
        btnPrevious.setOnClickListener(v -> previousExercise());
        btnNext.setOnClickListener(v -> nextExercise());
        
        // Close button
        findViewById(R.id.btn_close).setOnClickListener(v -> finish());
    }
    
    private void startWorkout() {
        if (isPaused) {
            resumeWorkout();
            return;
        }
        
        isPlaying = true;
        btnPlay.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
        
        tvInstructions.setText("Đang tập luyện... Hãy theo dõi video hướng dẫn!");
        
        countDownTimer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                updateTimer();
            }
            
            @Override
            public void onFinish() {
                completeWorkout();
            }
        }.start();
    }
    
    private void pauseWorkout() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isPaused = true;
        isPlaying = false;
        btnPause.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
        tvInstructions.setText("Tạm dừng - Nhấn Play để tiếp tục");
    }
    private void resumeWorkout() {
        isPaused = false;
        isPlaying = true;
        btnPlay.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
        tvInstructions.setText("Tiếp tục tập luyện!");
        
        countDownTimer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                updateTimer();
            }
            
            @Override
            public void onFinish() {
                completeWorkout();
            }
        }.start();
    }
    
    private void stopWorkout() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isPlaying = false;
        isPaused = false;
        timeRemaining = exerciseDuration * 60 * 1000;
        updateTimer();
        
        btnPause.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
        tvInstructions.setText("Đã dừng - Nhấn Play để bắt đầu lại");
    }
    
    private void completeWorkout() {
        isPlaying = false;
        isPaused = false;
        btnPause.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
        
        tvInstructions.setText("🎉 Hoàn thành! Bạn đã đốt cháy " + exerciseCalories + " calories!");
        Toast.makeText(this, "Chúc mừng! Bạn đã hoàn thành bài tập!", Toast.LENGTH_LONG).show();
        // Không lưu lại ở đây vì đã lưu khi bắt đầu (onCreate)
    }

    private void saveWorkoutLog(String name, int durationMinutes, int calories) {
        WorkoutSaver.save(this, name, durationMinutes, calories);
    }
    private void updateTimer() {
        int minutes = (int) (timeRemaining / 1000) / 60;
        int seconds = (int) (timeRemaining / 1000) % 60;
        tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
    }
    
    private void previousExercise() {
        // TODO: Implement previous exercise logic
        Toast.makeText(this, "Bài tập trước (chức năng sẽ được thêm)", Toast.LENGTH_SHORT).show();
    }
    
    private void nextExercise() {
        // TODO: Implement next exercise logic  
        Toast.makeText(this, "Bài tập tiếp theo (chức năng sẽ được thêm)", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    
    @Override
    public void onBackPressed() {
        if (isPlaying) {
            pauseWorkout();
            Toast.makeText(this, "Đã tạm dừng bài tập", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}