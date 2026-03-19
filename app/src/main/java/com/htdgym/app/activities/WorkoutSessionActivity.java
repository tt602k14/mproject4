package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.utils.YouTubeHelper;

import java.util.ArrayList;
import java.util.List;

public class WorkoutSessionActivity extends AppCompatActivity {

    private ImageView btnClose, imgVideoPreview;
    private CardView btnPlayVideo, btnPlayPause, btnPrevious, btnNext, btnRest, btnExit;
    private TextView tvExerciseName, tvSetsInfo, tvTimer, tvExerciseCounter, tvExerciseDescription, tvPlayPause;
    private ProgressBar progressTimer;
    
    private List<Exercise> exercises;
    private int currentExerciseIndex = 0;
    private CountDownTimer countDownTimer;
    private boolean isPlaying = false;
    private long timeLeftInMillis = 30000; // 30 seconds default
    private long totalTimeInMillis = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session);
        
        initViews();
        loadExercises();
        setupClickListeners();
        displayCurrentExercise();

        // Save last workout name immediately so home screen can show "Continue" card
        String cat = getIntent().getStringExtra("category");
        if (cat != null) {
            getSharedPreferences("HTDGymPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("last_workout_name", getCategoryDisplayName(cat))
                    .apply();
        }
    }

    private void initViews() {
        btnClose = findViewById(R.id.btn_close);
        imgVideoPreview = findViewById(R.id.img_video_preview);
        btnPlayVideo = findViewById(R.id.btn_play_video);
        btnPlayPause = findViewById(R.id.btn_play_pause);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
        btnRest = findViewById(R.id.btn_rest);
        btnExit = findViewById(R.id.btn_exit);
        tvExerciseName = findViewById(R.id.tv_exercise_name);
        tvSetsInfo = findViewById(R.id.tv_sets_info);
        tvTimer = findViewById(R.id.tv_timer);
        tvExerciseCounter = findViewById(R.id.tv_exercise_counter);
        tvExerciseDescription = findViewById(R.id.tv_exercise_description);
        tvPlayPause = findViewById(R.id.tv_play_pause);
        progressTimer = findViewById(R.id.progress_timer);
    }

    private void setupClickListeners() {
        btnClose.setOnClickListener(v -> showExitConfirmation());
        
        btnPlayVideo.setOnClickListener(v -> {
            if (exercises != null && currentExerciseIndex < exercises.size()) {
                openYouTubeVideo(exercises.get(currentExerciseIndex).videoUrl);
            }
        });
        
        btnPlayPause.setOnClickListener(v -> {
            if (isPlaying) {
                pauseTimer();
            } else {
                startTimer();
            }
        });
        
        btnPrevious.setOnClickListener(v -> {
            if (currentExerciseIndex > 0) {
                pauseTimer();
                currentExerciseIndex--;
                displayCurrentExercise();
            }
        });
        
        btnNext.setOnClickListener(v -> {
            if (currentExerciseIndex < exercises.size() - 1) {
                pauseTimer();
                currentExerciseIndex++;
                displayCurrentExercise();
            } else {
                // Workout completed - save stats
                saveWorkoutStats();
                Toast.makeText(this, "Hoàn thành buổi tập! 🎉", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        
        // Rest button - pause and show rest dialog
        btnRest.setOnClickListener(v -> {
            pauseTimer();
            showRestDialog();
        });
        
        // Exit button - show confirmation dialog
        btnExit.setOnClickListener(v -> showExitConfirmation());
    }

    private void loadExercises() {
        exercises = new ArrayList<>();
        String category = getIntent().getStringExtra("category");
        
        if (category == null) category = "chest";
        
        switch (category) {
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
                loadChestExercises();
        }
    }

    private void loadChestExercises() {
        exercises.add(new Exercise("Hít đất cơ bản", "3 hiệp x 15 lần", 
            "Giữ thân thẳng, hạ người xuống và đẩy lên", 
            "https://youtu.be/-R5sH2iG9Gw", 30));
        exercises.add(new Exercise("Hít đất rộng", "3 hiệp x 12 lần", 
            "Đặt tay rộng hơn vai, hạ người xuống", 
            "https://youtu.be/5Qv2T8VusME", 30));
        exercises.add(new Exercise("Hít đất kim cương", "3 hiệp x 10 lần", 
            "Đặt tay sát nhau tạo hình kim cương", 
            "https://youtu.be/J0DnG1_S92I", 30));
        exercises.add(new Exercise("Chống đẩy nghiêng", "3 hiệp x 12 lần", 
            "Đặt chân lên ghế, thực hiện hít đất", 
            "https://youtu.be/SKPab2YC8BE", 30));
        exercises.add(new Exercise("Plank chuyển hít đất", "3 hiệp x 10 lần", 
            "Từ tư thế plank chuyển sang hít đất", 
            "https://youtu.be/5Qv2T8VusME", 30));
        exercises.add(new Exercise("Hít đất một tay", "3 hiệp x 8 lần mỗi tay", 
            "Thực hiện hít đất với một tay", 
            "https://youtu.be/5Qv2T8VusME", 40));
        exercises.add(new Exercise("Hít đất vỗ tay", "3 hiệp x 10 lần", 
            "Đẩy mạnh và vỗ tay giữa không trung", 
            "https://youtu.be/5Qv2T8VusME", 30));
        exercises.add(new Exercise("Giãn cơ ngực", "1 phút", 
            "Thư giãn và giãn cơ ngực", 
            "https://youtu.be/5Qv2T8VusME", 60));
    }

    private void loadShoulderExercises() {
        exercises.add(new Exercise("Hít đất vai", "3 hiệp x 12 lần", 
            "Tư thế chữ V ngược, hít đất xuống", 
            "https://youtu.be/d1HZBdD0idE", 30));
        exercises.add(new Exercise("Chạm vai", "3 hiệp x 20 lần", 
            "Plank và chạm tay vào vai đối diện", 
            "https://youtu.be/1We3zKXj_uA", 30));
        exercises.add(new Exercise("Vòng tay", "3 hiệp x 30 giây", 
            "Xoay tay theo vòng tròn lớn", 
            "https://youtu.be/5Qv2T8VusME", 30));
        exercises.add(new Exercise("Nâng tay ngang", "3 hiệp x 15 lần", 
            "Nâng tay ra hai bên đến ngang vai", 
            "https://youtu.be/3VcKaXpzqRo", 30));
        exercises.add(new Exercise("Đẩy vai", "3 hiệp x 15 lần", 
            "Đẩy tạ hoặc chai nước lên trên đầu", 
            "https://youtu.be/qEwKCR5JCog", 30));
    }

    private void loadLegsExercises() {
        exercises.add(new Exercise("Gánh đùi cơ bản", "3 hiệp x 20 lần", 
            "Hạ người xuống như ngồi ghế", 
            "https://youtu.be/XtoO9YwNEqA", 30));
        exercises.add(new Exercise("Chùng chân", "3 hiệp x 15 lần mỗi chân", 
            "Bước ra trước và hạ người xuống", 
            "https://youtu.be/QOVaHwm-Q6U", 40));
        exercises.add(new Exercise("Gánh đùi nhảy", "3 hiệp x 15 lần", 
            "Squat và nhảy lên cao", 
            "https://youtu.be/A-cFYWvaHr0", 30));
        exercises.add(new Exercise("Ngồi tường", "3 hiệp x 45 giây", 
            "Tựa lưng vào tường, ngồi 90 độ", 
            "https://youtu.be/y-wV4Venusw", 45));
        exercises.add(new Exercise("Nâng bắp chân", "3 hiệp x 25 lần", 
            "Đứng trên mũi chân", 
            "https://youtu.be/gwLzBJYoWlI", 30));
    }

    private void loadAbsExercises() {
        exercises.add(new Exercise("Gập bụng cơ bản", "3 hiệp x 20 lần", 
            "Nằm ngửa, gập người lên", 
            "https://youtu.be/Y8VflnViz78", 30));
        exercises.add(new Exercise("Plank", "3 hiệp x 45 giây", 
            "Giữ thân thẳng trên khuỷu tay", 
            "https://youtu.be/pSHjTRCQxIw", 45));
        exercises.add(new Exercise("Gập bụng xe đạp", "3 hiệp x 30 lần", 
            "Nằm ngửa, đạp chân như đạp xe", 
            "https://youtu.be/9FGilxCbdz8", 30));
        exercises.add(new Exercise("Nâng chân", "3 hiệp x 20 lần", 
            "Nằm ngửa, nâng chân lên cao", 
            "https://youtu.be/JB2oyawG9KI", 30));
        exercises.add(new Exercise("Leo núi", "3 hiệp x 30 giây", 
            "Tư thế plank, kéo đầu gối lên ngực", 
            "https://youtu.be/nmwgirgXLYM", 30));
    }

    private void loadBackExercises() {
        exercises.add(new Exercise("Tư thế siêu nhân", "3 hiệp x 15 lần", 
            "Nằm sấp, nâng tay chân lên cao", 
            "https://youtu.be/7SLbUk-qTTM", 30));
        exercises.add(new Exercise("Duỗi lưng", "3 hiệp x 15 lần", 
            "Nằm sấp, nâng thân người lên", 
            "https://youtu.be/cc7kIfSUWEY", 30));
        exercises.add(new Exercise("Chó chim", "3 hiệp x 12 lần mỗi bên", 
            "Quỳ, duỗi tay chân đối diện", 
            "https://youtu.be/wiFNA3sqjCA", 30));
        exercises.add(new Exercise("Mèo - Bò", "3 hiệp x 15 lần", 
            "Quỳ, uốn cong và võng lưng", 
            "https://youtu.be/kqnua4rHVVA", 30));
    }

    private void displayCurrentExercise() {
        if (exercises == null || exercises.isEmpty()) return;
        
        Exercise exercise = exercises.get(currentExerciseIndex);
        tvExerciseName.setText(exercise.name);
        tvSetsInfo.setText(exercise.sets);
        tvExerciseDescription.setText(exercise.description);
        tvExerciseCounter.setText("Bài tập " + (currentExerciseIndex + 1) + "/" + exercises.size());
        
        // Load YouTube thumbnail vào video preview
        YouTubeHelper.loadThumbnail(imgVideoPreview, exercise.videoUrl, YouTubeHelper.ThumbnailQuality.HIGH);
        
        // Reset timer
        totalTimeInMillis = exercise.durationSeconds * 1000L;
        timeLeftInMillis = totalTimeInMillis;
        updateTimerText();
        progressTimer.setProgress(100);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                // Auto move to next exercise
                if (currentExerciseIndex < exercises.size() - 1) {
                    currentExerciseIndex++;
                    displayCurrentExercise();
                    startTimer(); // Auto start next exercise
                } else {
                    Toast.makeText(WorkoutSessionActivity.this, 
                        "Hoàn thành buổi tập! 🎉", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }.start();
        
        isPlaying = true;
        tvPlayPause.setText("⏸");
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isPlaying = false;
        tvPlayPause.setText("▶");
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    private void updateProgressBar() {
        int progress = (int) ((timeLeftInMillis * 100) / totalTimeInMillis);
        progressTimer.setProgress(progress);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    
    /**
     * Hiển thị dialog nghỉ ngơi
     */
    private void showRestDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("⏸ Nghỉ ngơi")
                .setMessage("Bạn đang nghỉ. Chọn thời gian nghỉ hoặc tiếp tục tập.")
                .setPositiveButton("Nghỉ 30 giây", (dialog, which) -> {
                    startRestTimer(30);
                })
                .setNeutralButton("Nghỉ 60 giây", (dialog, which) -> {
                    startRestTimer(60);
                })
                .setNegativeButton("Tiếp tục", (dialog, which) -> {
                    // Just close dialog, user can press play when ready
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }
    
    /**
     * Bắt đầu đếm ngược thời gian nghỉ
     */
    private void startRestTimer(int seconds) {
        pauseTimer(); // Make sure workout timer is paused
        
        final android.app.AlertDialog restDialog = new android.app.AlertDialog.Builder(this)
                .setTitle("⏸ Đang nghỉ...")
                .setMessage("Thời gian còn lại: " + seconds + " giây")
                .setNegativeButton("Bỏ qua", null)
                .setCancelable(false)
                .create();
        
        restDialog.show();
        
        new CountDownTimer(seconds * 1000L, 1000) {
            int timeLeft = seconds;
            
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) (millisUntilFinished / 1000);
                restDialog.setMessage("Thời gian còn lại: " + timeLeft + " giây\n\nChuẩn bị tiếp tục tập! 💪");
            }

            @Override
            public void onFinish() {
                restDialog.dismiss();
                Toast.makeText(WorkoutSessionActivity.this, "Hết giờ nghỉ! Tiếp tục tập nào! 🔥", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }
    
    /**
     * Hiển thị dialog xác nhận thoát
     */
    private void showExitConfirmation() {
        pauseTimer(); // Pause timer when showing exit dialog
        
        new android.app.AlertDialog.Builder(this)
                .setTitle("⚠ Thoát buổi tập?")
                .setMessage("Bạn có chắc muốn thoát? Tiến trình sẽ không được lưu.")
                .setPositiveButton("Thoát", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("Tiếp tục tập", (dialog, which) -> {
                    dialog.dismiss();
                    // User can press play to continue
                })
                .setCancelable(true)
                .show();
    }
    
    private void saveWorkoutStats() {
        new Thread(() -> {
            try {
                android.content.SharedPreferences prefs = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);
                String userId = prefs.getString("userId", "guest");
                
                com.htdgym.app.database.GymDatabase db = com.htdgym.app.database.GymDatabase.getInstance(this);
                com.htdgym.app.models.WorkoutStats stats = db.workoutStatsDao().getStatsByUserId(userId);
                
                if (stats == null) {
                    stats = new com.htdgym.app.models.WorkoutStats();
                    stats.setUserId(userId);
                }
                
                // Calculate total workout time (all exercises)
                int totalSeconds = 0;
                for (Exercise ex : exercises) {
                    totalSeconds += ex.durationSeconds;
                }
                
                // Update stats
                stats.setTotalCalories(stats.getTotalCalories() + (totalSeconds / 6)); // ~10 cal per minute
                stats.setTotalSeconds(stats.getTotalSeconds() + totalSeconds);
                stats.setTotalWorkouts(stats.getTotalWorkouts() + 1);
                
                // Check if it's a new day
                long today = System.currentTimeMillis() / (1000 * 60 * 60 * 24);
                long lastDay = stats.getLastWorkoutDate() / (1000 * 60 * 60 * 24);
                if (today > lastDay) {
                    stats.setTotalDays(stats.getTotalDays() + 1);
                }
                stats.setLastWorkoutDate(System.currentTimeMillis());

                // Save last workout name for "Continue Workout" card on home screen
                String category = getIntent().getStringExtra("category");
                String workoutLabel = category != null ? getCategoryDisplayName(category) : "Bài tập vừa rồi";
                prefs.edit().putString("last_workout_name", workoutLabel).apply();

                // Save to database
                if (stats.getId() == 0) {
                    db.workoutStatsDao().insert(stats);
                } else {
                    db.workoutStatsDao().update(stats);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String getCategoryDisplayName(String category) {
        switch (category) {
            case "chest": return "💪 Tập Ngực";
            case "shoulder": return "🏋️ Tập Vai";
            case "legs": return "🦵 Tập Chân";
            case "back": return "🔥 Tập Lưng";
            case "abs": return "💯 Tập Bụng";
            case "hiit": return "⚡ HIIT";
            case "cardio": return "🏃 Cardio";
            case "tabata": return "🎯 Tabata";
            default: return "🏋️ Bài tập vừa rồi";
        }
    }

    // Exercise model class
    private static class Exercise {
        String name;
        String sets;
        String description;
        String videoUrl;
        int durationSeconds;

        Exercise(String name, String sets, String description, String videoUrl, int durationSeconds) {
            this.name = name;
            this.sets = sets;
            this.description = description;
            this.videoUrl = videoUrl;
            this.durationSeconds = durationSeconds;
        }
    }
}
