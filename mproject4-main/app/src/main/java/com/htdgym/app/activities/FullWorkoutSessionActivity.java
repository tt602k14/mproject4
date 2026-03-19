package com.htdgym.app.activities;

import android.content.Intent;
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
import com.htdgym.app.utils.WorkoutSaver;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FullWorkoutSessionActivity extends BaseActivity {

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private CardView btnClose, btnPrevious, btnNext, btnPlay, btnRest, btnExit;
    private TextView tvExerciseName, tvExerciseSets, tvTimer, tvProgressText, tvPlayIcon;
    private ProgressBar progressBar;
    
    private String workoutCategory;
    private List<WorkoutExercise> exercises = new ArrayList<>();
    private int currentExerciseIndex = 0;
    private WorkoutExercise currentExercise;
    
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isTimerRunning = false;
    private boolean isRestTime = false;

    // Inner class for workout exercise
    private static class WorkoutExercise {
        String name;
        String sets;
        String videoUrl;
        int duration;
        
        WorkoutExercise(String name, String sets, String videoUrl, int duration) {
            this.name = name;
            this.sets = sets;
            this.videoUrl = videoUrl;
            this.duration = duration;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_video);

        workoutCategory = getIntent().getStringExtra("category");
        if (workoutCategory == null) workoutCategory = "all";

        initViews();
        setupYouTubePlayer();
        setupClickListeners();
        loadExercises();
        startWorkout();

        // Lưu ngay khi bắt đầu tập (không chờ hoàn thành)
        WorkoutSaver.save(this, getCategoryName(), getTotalDuration(), exercises.size() * 15);
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
                loadCurrentExerciseVideo();
            }
        });
    }

    private void setupClickListeners() {
        btnClose.setOnClickListener(v -> showExitDialog());
        
        btnPlay.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });
        
        btnPrevious.setOnClickListener(v -> previousExercise());
        btnNext.setOnClickListener(v -> nextExercise());
        btnRest.setOnClickListener(v -> showRestDialog());
        btnExit.setOnClickListener(v -> showExitDialog());
    }

    private void loadExercises() {
        exercises.clear();
        
        switch (workoutCategory) {
            case "chest":
                loadChestExercises();
                break;
            case "shoulder":
                loadShoulderExercises();
                break;
            case "legs":
                loadLegsExercises();
                break;
            case "abs":
                loadAbsExercises();
                break;
            case "back":
                loadBackExercises();
                break;
            default:
                loadAllExercises();
                break;
        }
    }
    private void loadChestExercises() {
        exercises.add(new WorkoutExercise("💪 Hít đất cơ bản", "4 x 15 lần", "https://youtu.be/-R5sH2iG9Gw", 40));
        exercises.add(new WorkoutExercise("💪 Hít đất nghiêng", "3 x 12 lần", "https://youtu.be/4dF1DOWzf20", 40));
        exercises.add(new WorkoutExercise("💪 Hít đất rộng", "3 x 12 lần", "https://www.youtube.com/shorts/hX7yJcwfQWk", 40));
        exercises.add(new WorkoutExercise("💪 Hít đất kim cương", "3 x 10 lần", "https://youtu.be/J0DnG1_S92I", 40));
        exercises.add(new WorkoutExercise("💪 Hít đất xuống dốc", "3 x 12 lần", "https://youtu.be/SKPab2YC8BE", 40));
        exercises.add(new WorkoutExercise("💪 Chống đẩy ngực", "3 x 10 lần", "https://youtu.be/6BBemoyGI-8", 40));
        exercises.add(new WorkoutExercise("💪 Ép ngực", "3 x 15 lần", "https://youtu.be/Xf83QChAoIE", 40));
        exercises.add(new WorkoutExercise("🧘 Giãn cơ ngực", "2 x 30 giây", "https://youtu.be/4Aec59oC09Q", 45));
    }

    private void loadShoulderExercises() {
        exercises.add(new WorkoutExercise("🏋️ Hít đất vai", "4 x 12 lần", "https://youtu.be/d1HZBdD0idE", 40));
        exercises.add(new WorkoutExercise("🏋️ Chạm vai", "3 x 20 lần", "https://youtu.be/qp75z3XInOI", 40));
        exercises.add(new WorkoutExercise("🏋️ Vòng tay", "3 x 30 giây", "https://youtu.be/UVMEnIaY8aU", 40));
        exercises.add(new WorkoutExercise("🏋️ Nâng tay ngang", "3 x 15 lần", "https://youtu.be/XPPfnSEATJA", 40));
        exercises.add(new WorkoutExercise("🏋️ Nâng tay trước", "3 x 15 lần", "https://youtu.be/hRJ6tR5-if0", 40));
        exercises.add(new WorkoutExercise("🏋️ Đẩy vai", "4 x 12 lần", "https://www.youtube.com/shorts/ZICoJhVzFCc", 40));
        exercises.add(new WorkoutExercise("🧘 Giãn cơ vai", "2 x 30 giây", "https://youtu.be/kbAes2QDMXM", 45));
    }

    private void loadLegsExercises() {
        exercises.add(new WorkoutExercise("🦵 Gánh đùi", "4 x 20 lần", "https://youtu.be/XtoO9YwNEqA", 40));
        exercises.add(new WorkoutExercise("🦵 Chùng chân", "3 x 15 lần/chân", "https://youtu.be/QOVaHwm-Q6U", 40));
        exercises.add(new WorkoutExercise("🦵 Gánh đùi nhảy", "3 x 15 lần", "https://youtu.be/A-cFYWvaHr0", 30));
        exercises.add(new WorkoutExercise("🦵 Ngồi tường", "3 x 45 giây", "https://youtu.be/cWTZ8Am1Ee0", 60));
        exercises.add(new WorkoutExercise("🦵 Nâng bắp chân", "4 x 20 lần", "https://www.youtube.com/shorts/a-x_NR-ibos", 40));
        exercises.add(new WorkoutExercise("🦵 Chùng chân ngang", "3 x 12 lần/chân", "https://www.youtube.com/shorts/nIk1evRzlyA", 40));
        exercises.add(new WorkoutExercise("🦵 Gánh đùi Bulgaria", "3 x 12 lần/chân", "https://www.youtube.com/shorts/AaKASiVCZP4", 40));
        exercises.add(new WorkoutExercise("🦵 Nâng mông", "4 x 15 lần", "https://www.youtube.com/shorts/39NOLur3pSs", 40));
        exercises.add(new WorkoutExercise("🦵 Bước lên bậc", "3 x 15 lần/chân", "https://www.youtube.com/shorts/htUOXeEZUiI", 40));
        exercises.add(new WorkoutExercise("🧘 Giãn cơ chân", "2 x 30 giây", "https://youtu.be/g_tea8ZNk5A", 45));
    }

    private void loadAbsExercises() {
        exercises.add(new WorkoutExercise("🔥 Gập bụng", "4 x 20 lần", "https://youtu.be/Y8VflnViz78", 40));
        exercises.add(new WorkoutExercise("🔥 Plank cơ bản", "3 x 60 giây", "https://youtu.be/pSHjTRCQxIw", 60));
        exercises.add(new WorkoutExercise("🔥 Gập bụng xe đạp", "3 x 20 lần", "https://www.youtube.com/shorts/4h9WoYOVqw4", 40));
        exercises.add(new WorkoutExercise("🔥 Nâng chân", "3 x 15 lần", "https://www.youtube.com/shorts/HFHn5AXeJgI", 40));
        exercises.add(new WorkoutExercise("🔥 Xoay người Nga", "3 x 30 lần", "https://youtu.be/aKoJRKucDJY", 40));
        exercises.add(new WorkoutExercise("⚡ Leo núi", "3 x 30 giây", "https://youtu.be/nmwgirgXLYM", 30));
        exercises.add(new WorkoutExercise("🔥 Plank nghiêng", "2 x 45 giây/bên", "https://www.youtube.com/shorts/vLtwnh-X82s", 60));
        exercises.add(new WorkoutExercise("🔥 Đá chân nhanh", "3 x 30 giây", "https://www.youtube.com/shorts/dottsAhnC3w", 30));
        exercises.add(new WorkoutExercise("🧘 Giãn cơ bụng", "2 x 30 giây", "https://youtu.be/7Rl_E7L7rQY", 45));
    }
    private void loadBackExercises() {
        exercises.add(new WorkoutExercise("🏃 Tư thế siêu nhân", "4 x 15 lần", "https://youtu.be/7SLbUk-qTTM", 40));
        exercises.add(new WorkoutExercise("🏃 Duỗi lưng", "3 x 15 lần", "https://youtu.be/RqcOCBb4arc", 40));
        exercises.add(new WorkoutExercise("🏃 Thiên thần ngược", "3 x 12 lần", "https://youtu.be/LbTx6Y4ALZ0", 40));
        exercises.add(new WorkoutExercise("🏃 Nâng tay chữ Y", "3 x 12 lần", "https://www.youtube.com/shorts/iK22GwXJji0", 40));
        exercises.add(new WorkoutExercise("🏃 Nâng tay chữ T", "3 x 12 lần", "https://www.youtube.com/shorts/i-jEU2pBdzM", 40));
        exercises.add(new WorkoutExercise("🏃 Chó chim", "3 x 12 lần/bên", "https://www.youtube.com/shorts/p8qrmBvewls", 40));
        exercises.add(new WorkoutExercise("🏃 Mèo - Bò", "3 x 15 lần", "https://www.youtube.com/shorts/FDckLem5r44", 40));
        exercises.add(new WorkoutExercise("🧘 Giãn cơ lưng", "2 x 30 giây", "https://youtu.be/DWmGArQBtFI", 45));
    }

    private void loadAllExercises() {
        exercises.add(new WorkoutExercise("⚡ Burpees", "4 x 15 lần", "https://youtu.be/JZQA08SlJnM", 30));
        exercises.add(new WorkoutExercise("💪 Hít đất", "3 x 15 lần", "https://youtu.be/-R5sH2iG9Gw", 40));
        exercises.add(new WorkoutExercise("🦵 Gánh đùi", "4 x 20 lần", "https://youtu.be/XtoO9YwNEqA", 40));
        exercises.add(new WorkoutExercise("🔥 Plank", "3 x 60 giây", "https://youtu.be/pSHjTRCQxIw", 60));
        exercises.add(new WorkoutExercise("🦵 Chùng chân", "3 x 15 lần/chân", "https://youtu.be/QOVaHwm-Q6U", 40));
        exercises.add(new WorkoutExercise("⚡ Leo núi", "3 x 30 giây", "https://youtu.be/nmwgirgXLYM", 30));
        exercises.add(new WorkoutExercise("⚡ Bật nhảy", "3 x 30 lần", "https://youtu.be/c4DAnQ6DtF8", 30));
        exercises.add(new WorkoutExercise("⚡ Chạy tại chỗ", "3 x 30 giây", "https://youtu.be/QmlBTEvBGMw", 30));
        exercises.add(new WorkoutExercise("🔥 Gập bụng", "3 x 20 lần", "https://youtu.be/Y8VflnViz78", 40));
        exercises.add(new WorkoutExercise("🧘 Giãn cơ", "2 x 30 giây", "https://youtu.be/5Qv2T8VusME", 45));
    }

    private void startWorkout() {
        if (exercises.isEmpty()) {
            Toast.makeText(this, "Không có bài tập nào!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        currentExerciseIndex = 0;
        loadCurrentExercise();
        showWelcomeMessage();
    }

    private void showWelcomeMessage() {
        String categoryName = getCategoryName();
        String message = String.format("🔥 Bắt đầu buổi tập %s!\n💪 %d bài tập đang chờ bạn!\n⏱️ Tổng thời gian: %d phút", 
                categoryName, exercises.size(), getTotalDuration());
        
        new android.app.AlertDialog.Builder(this)
                .setTitle("🏋️ Sẵn sàng tập luyện?")
                .setMessage(message)
                .setPositiveButton("💪 BẮT ĐẦU!", (dialog, which) -> {
                    Toast.makeText(this, "🔥 Hãy cố gắng hết mình!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("❌ Hủy", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }
    private String getCategoryName() {
        switch (workoutCategory) {
            case "chest": return "Ngực";
            case "shoulder": return "Vai";
            case "legs": return "Chân";
            case "abs": return "Bụng";
            case "back": return "Lưng";
            default: return "Toàn thân";
        }
    }

    private int getTotalDuration() {
        int total = 0;
        for (WorkoutExercise exercise : exercises) {
            total += exercise.duration;
        }
        return (total + (exercises.size() * 15)) / 60; // Add 15s rest between exercises
    }

    private void loadCurrentExercise() {
        if (currentExerciseIndex >= exercises.size()) {
            completeWorkout();
            return;
        }
        
        currentExercise = exercises.get(currentExerciseIndex);
        displayExerciseInfo();
        loadCurrentExerciseVideo();
    }

    private void displayExerciseInfo() {
        tvExerciseName.setText(currentExercise.name);
        tvExerciseSets.setText(currentExercise.sets);
        tvProgressText.setText(String.format(Locale.getDefault(), "%d/%d", 
                currentExerciseIndex + 1, exercises.size()));
        
        timeLeftInMillis = currentExercise.duration * 1000L;
        updateTimerText();
        progressBar.setProgress(0);
        
        // Show exercise start message
        Toast.makeText(this, "▶️ " + currentExercise.name, Toast.LENGTH_SHORT).show();
    }

    private void loadCurrentExerciseVideo() {
        if (youTubePlayer != null && currentExercise != null) {
            String videoId = extractVideoId(currentExercise.videoUrl);
            if (videoId != null && !videoId.isEmpty()) {
                youTubePlayer.cueVideo(videoId, 0);
            }
        }
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
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                
                long totalTime = currentExercise.duration * 1000L;
                int progress = (int) ((totalTime - timeLeftInMillis) * 100 / totalTime);
                progressBar.setProgress(progress);
                
                // Vibration feedback in last 10 seconds
                if (timeLeftInMillis <= 10000 && timeLeftInMillis % 1000 == 0) {
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
                
                if (isRestTime) {
                    // End of rest, go to next exercise
                    isRestTime = false;
                    nextExercise();
                } else {
                    // End of exercise, start rest or go to next
                    completeCurrentExercise();
                }
            }
        }.start();
        
        isTimerRunning = true;
        updatePlayButton();
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

    private void completeCurrentExercise() {
        // Show completion animation
        tvTimer.setText("DONE!");
        tvTimer.setTextColor(0xFF4CAF50);
        
        // Vibration feedback
        android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(new long[]{0, 200, 100, 200}, -1);
        }
        
        Toast.makeText(this, "✅ Hoàn thành: " + currentExercise.name, Toast.LENGTH_SHORT).show();
        
        // Auto advance to next exercise after 2 seconds
        new android.os.Handler().postDelayed(() -> {
            tvTimer.setTextColor(0xFFFFFFFF);
            
            if (currentExerciseIndex < exercises.size() - 1) {
                // Start rest period before next exercise
                startRestPeriod();
            } else {
                // This was the last exercise
                completeWorkout();
            }
        }, 2000);
    }
    private void startRestPeriod() {
        isRestTime = true;
        timeLeftInMillis = 15000; // 15 seconds rest
        
        tvExerciseName.setText("😴 Thời gian nghỉ ngơi");
        tvExerciseSets.setText("Chuẩn bị cho bài tập tiếp theo: " + exercises.get(currentExerciseIndex + 1).name);
        progressBar.setProgress(0);
        
        Toast.makeText(this, "😴 Nghỉ ngơi 15 giây...", Toast.LENGTH_SHORT).show();
        
        // Auto start rest timer
        startTimer();
    }

    private void nextExercise() {
        if (currentExerciseIndex < exercises.size() - 1) {
            currentExerciseIndex++;
            loadCurrentExercise();
        } else {
            completeWorkout();
        }
    }

    private void previousExercise() {
        if (currentExerciseIndex > 0) {
            currentExerciseIndex--;
            loadCurrentExercise();
            Toast.makeText(this, "⏮️ Quay lại bài tập trước", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Đây là bài tập đầu tiên!", Toast.LENGTH_SHORT).show();
        }
    }

    private void completeWorkout() {
        // Stop any running timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        int totalCalories = exercises.size() * 15;
        int totalDuration = getTotalDuration();
        // Không lưu lại ở đây vì đã lưu khi bắt đầu (onCreate)
        
        // Show completion dialog
        String message = String.format("🎉 Chúc mừng! Bạn đã hoàn thành buổi tập %s!\n\n" +
                "📊 Thống kê:\n" +
                "• %d bài tập hoàn thành\n" +
                "• ~%d calories đã đốt cháy\n" +
                "• %d phút tập luyện\n\n" +
                "💪 Bạn thật tuyệt vời!", 
                getCategoryName(), exercises.size(), totalCalories, totalDuration);
        
        new android.app.AlertDialog.Builder(this)
                .setTitle("🏆 Hoàn thành xuất sắc!")
                .setMessage(message)
                .setPositiveButton("🎯 Tập tiếp", (dialog, which) -> {
                    currentExerciseIndex = 0;
                    loadCurrentExercise();
                    showWelcomeMessage();
                })
                .setNegativeButton("🏠 Về trang chủ", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void saveWorkoutLog(String name, int durationMinutes, int calories) {
        WorkoutSaver.save(this, name, durationMinutes, calories);
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
            startManualRest(30);
        });
        
        btn60Sec.setOnClickListener(v -> {
            dialog.dismiss();
            startManualRest(60);
        });
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }
    private void startManualRest(int seconds) {
        isRestTime = true;
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
                    Toast.makeText(FullWorkoutSessionActivity.this, "🫁 Hít thở sâu...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                isRestTime = false;
                displayExerciseInfo(); // Return to current exercise
                
                // Gentle vibration
                android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(new long[]{0, 200, 100, 200}, -1);
                }
                
                Toast.makeText(FullWorkoutSessionActivity.this, 
                        "⚡ Hết giờ nghỉ! Sẵn sàng tiếp tục!", Toast.LENGTH_LONG).show();
            }
        }.start();
        
        isTimerRunning = true;
        updatePlayButton();
    }

    private void showExitDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("🏃‍♂️ Thoát buổi tập?")
                .setMessage(String.format("Bạn đã hoàn thành %d/%d bài tập.\n\n" +
                           "💡 Tip: Hãy hoàn thành buổi tập để đạt hiệu quả tốt nhất!", 
                           currentExerciseIndex, exercises.size()))
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