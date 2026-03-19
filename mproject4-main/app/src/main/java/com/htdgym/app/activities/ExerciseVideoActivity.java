package com.htdgym.app.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Locale;
import com.htdgym.app.activities.BaseActivity;

public class ExerciseVideoActivity extends BaseActivity {

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private CardView btnClose, btnPrevious, btnNext, btnPlay, btnRest, btnExit;
    private TextView tvExerciseName, tvExerciseSets, tvTimer, tvProgressText, tvPlayIcon;
    private ProgressBar progressBar;
    
    private String exerciseName;
    private String exerciseSets;
    private String videoUrl;
    private int durationSeconds;
    private int currentExercise;
    private int totalExercises;
    
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_video);

        // Get data from intent
        exerciseName = getIntent().getStringExtra("exercise_name");
        exerciseSets = getIntent().getStringExtra("exercise_sets");
        videoUrl = getIntent().getStringExtra("video_url");
        durationSeconds = getIntent().getIntExtra("duration_seconds", 30);
        currentExercise = getIntent().getIntExtra("current_exercise", 1);
        totalExercises = getIntent().getIntExtra("total_exercises", 1);

        initViews();
        setupYouTubePlayer();
        setupClickListeners();
        displayExerciseInfo();
    }

    private void initViews() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        btnClose = findViewById(R.id.btn_close);
        btnPlay = findViewById(R.id.btn_play);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
        btnRest = findViewById(R.id.btn_rest);
        btnExit = findViewById(R.id.btn_exit);
        tvExerciseName = findViewById(R.id.tv_exercise_name);
        tvExerciseSets = findViewById(R.id.tv_exercise_sets);
        tvTimer = findViewById(R.id.tv_timer);
        tvProgressText = findViewById(R.id.tv_progress_text);
        tvPlayIcon = findViewById(R.id.tv_play_icon);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupYouTubePlayer() {
        getLifecycle().addObserver(youTubePlayerView);
        
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                youTubePlayer = player;
                String videoId = extractVideoId(videoUrl);
                if (videoId != null && !videoId.isEmpty()) {
                    youTubePlayer.cueVideo(videoId, 0);
                }
            }
        });
    }

    private String extractVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.isEmpty()) return null;
        
        if (videoUrl.contains("youtu.be/")) {
            String[] parts = videoUrl.split("youtu.be/");
            if (parts.length > 1) {
                return parts[1].split("\\?")[0];
            }
        } else if (videoUrl.contains("youtube.com/watch?v=")) {
            String[] parts = videoUrl.split("v=");
            if (parts.length > 1) {
                return parts[1].split("&")[0];
            }
        } else if (videoUrl.contains("youtube.com/shorts/")) {
            String[] parts = videoUrl.split("shorts/");
            if (parts.length > 1) {
                return parts[1].split("\\?")[0];
            }
        }
        
        return null;
    }

    private void displayExerciseInfo() {
        tvExerciseName.setText(exerciseName);
        tvExerciseSets.setText(exerciseSets != null ? exerciseSets : "Tập theo video hướng dẫn");
        tvProgressText.setText(String.format(Locale.getDefault(), "%d/%d", 
                currentExercise, totalExercises));
        
        timeLeftInMillis = durationSeconds * 1000L;
        updateTimerText();
        progressBar.setProgress(0);
        
        // Add motivational messages
        showMotivationalMessage();
    }
    
    private void showMotivationalMessage() {
        String[] messages = {
            "💪 Sẵn sàng chinh phục thử thách!",
            "🔥 Hãy đốt cháy calories!", 
            "⚡ Năng lượng tích cực!",
            "🎯 Tập trung và quyết tâm!",
            "🏆 Bạn có thể làm được!"
        };
        
        String message = messages[(int) (Math.random() * messages.length)];
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setupClickListeners() {
        btnClose.setOnClickListener(v -> finish());
        
        btnPlay.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });
        
        btnPrevious.setOnClickListener(v -> {
            Toast.makeText(this, "Bài tập trước", Toast.LENGTH_SHORT).show();
            // TODO: Implement previous exercise
        });
        
        btnNext.setOnClickListener(v -> {
            Toast.makeText(this, "Bài tập tiếp theo", Toast.LENGTH_SHORT).show();
            // TODO: Implement next exercise
        });
        
        btnRest.setOnClickListener(v -> showRestDialog());
        
        btnExit.setOnClickListener(v -> showExitDialog());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                
                long totalTime = durationSeconds * 1000L;
                int progress = (int) ((totalTime - timeLeftInMillis) * 100 / totalTime);
                progressBar.setProgress(progress);
                
                // Add vibration feedback at certain intervals
                if (timeLeftInMillis <= 10000 && timeLeftInMillis % 1000 == 0) {
                    // Last 10 seconds countdown
                    android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
                    if (vibrator != null) {
                        vibrator.vibrate(100);
                    }
                }
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                updatePlayButton();
                progressBar.setProgress(100);
                
                // Celebration animation
                showCompletionAnimation();
                
                Toast.makeText(ExerciseVideoActivity.this, 
                        "🎉 Tuyệt vời! Bạn đã hoàn thành bài tập!", Toast.LENGTH_LONG).show();
            }
        }.start();
        
        isTimerRunning = true;
        updatePlayButton();
    }
    
    private void showCompletionAnimation() {
        // Simple completion feedback
        tvTimer.setText("DONE!");
        tvTimer.setTextColor(0xFF4CAF50);
        
        // Reset after 2 seconds
        new android.os.Handler().postDelayed(() -> {
            tvTimer.setTextColor(0xFFFFFFFF);
            displayExerciseInfo();
        }, 2000);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        updatePlayButton();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    private void updatePlayButton() {
        if (tvPlayIcon != null) {
            tvPlayIcon.setText(isTimerRunning ? "⏸" : "▶");
        }
    }

    private void showRestDialog() {
        pauseTimer();
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_NoActionBar);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rest_options, null);
        builder.setView(dialogView);
        
        android.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        
        androidx.cardview.widget.CardView btn30Sec = dialogView.findViewById(R.id.btn_rest_30);
        androidx.cardview.widget.CardView btn60Sec = dialogView.findViewById(R.id.btn_rest_60);
        androidx.cardview.widget.CardView btnCancel = dialogView.findViewById(R.id.btn_cancel);
        
        btn30Sec.setOnClickListener(v -> {
            dialog.dismiss();
            startRestTimer(30);
        });
        
        btn60Sec.setOnClickListener(v -> {
            dialog.dismiss();
            startRestTimer(60);
        });
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }

    private void startRestTimer(int seconds) {
        timeLeftInMillis = seconds * 1000L;
        
        tvExerciseName.setText("😴 Thời gian nghỉ ngơi");
        tvExerciseSets.setText("Thư giãn và hít thở sâu để phục hồi sức lực");
        progressBar.setProgress(0);
        
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                
                long totalTime = seconds * 1000L;
                int progress = (int) ((totalTime - timeLeftInMillis) * 100 / totalTime);
                progressBar.setProgress(progress);
                
                // Breathing guidance
                if (millisUntilFinished % 4000 == 0) {
                    Toast.makeText(ExerciseVideoActivity.this, "🫁 Hít thở sâu...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                displayExerciseInfo();
                
                // Gentle vibration to signal end of rest
                android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(new long[]{0, 200, 100, 200}, -1);
                }
                
                Toast.makeText(ExerciseVideoActivity.this, 
                        "⚡ Hết giờ nghỉ! Sẵn sàng tiếp tục!", Toast.LENGTH_LONG).show();
            }
        }.start();
        
        isTimerRunning = true;
        updatePlayButton();
    }

    private void showExitDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("🏃‍♂️ Thoát buổi tập?")
                .setMessage("Bạn có chắc muốn dừng buổi tập này?\n\n" +
                           "💡 Tip: Hãy hoàn thành bài tập để đạt hiệu quả tốt nhất!")
                .setPositiveButton("✅ Thoát", (dialog, which) -> {
                    Toast.makeText(this, "Hẹn gặp lại bạn trong buổi tập tiếp theo! 👋", Toast.LENGTH_LONG).show();
                    finish();
                })
                .setNegativeButton("💪 Tiếp tục", (dialog, which) -> {
                    Toast.makeText(this, "Tuyệt vời! Hãy tiếp tục cố gắng! 🔥", Toast.LENGTH_SHORT).show();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
        }
    }
}
