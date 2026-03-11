package com.htdgym.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.htdgym.app.activities.BaseActivity;

public class ProgramExerciseSessionActivity extends BaseActivity {

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private CardView btnClose, btnPrevious, btnNext, btnPlay;
    private TextView tvExerciseName, tvExerciseSets, tvTimer, tvProgress, tvPlayIcon;
    private ProgressBar progressBar;
    private CardView btnRest, btnExit;
    
    private List<Exercise> exercises;
    private int currentExerciseIndex = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000;
    private boolean isTimerRunning = false;
    private boolean isRestMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_exercise_session);

        initViews();
        loadExercises();
        
        currentExerciseIndex = getIntent().getIntExtra("exercise_index", 0);
        
        setupYouTubePlayer();
        setupClickListeners();
        displayCurrentExercise();
    }

    private void initViews() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        btnClose = findViewById(R.id.btn_close);
        btnPlay = findViewById(R.id.btn_play);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
        tvExerciseName = findViewById(R.id.tv_exercise_name);
        tvExerciseSets = findViewById(R.id.tv_exercise_sets);
        tvTimer = findViewById(R.id.tv_timer);
        tvProgress = findViewById(R.id.tv_progress);
        tvPlayIcon = findViewById(R.id.tv_play_icon);
        progressBar = findViewById(R.id.progress_bar);
        btnRest = findViewById(R.id.btn_rest);
        btnExit = findViewById(R.id.btn_exit);
    }

    private void setupYouTubePlayer() {
        getLifecycle().addObserver(youTubePlayerView);
        
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                youTubePlayer = player;
                if (!exercises.isEmpty()) {
                    loadVideoForCurrentExercise();
                }
            }
        });
    }

    private void loadVideoForCurrentExercise() {
        if (youTubePlayer != null && currentExerciseIndex < exercises.size()) {
            Exercise exercise = exercises.get(currentExerciseIndex);
            String videoId = extractVideoId(exercise.videoUrl);
            if (videoId != null && !videoId.isEmpty()) {
                youTubePlayer.cueVideo(videoId, 0);
            }
        }
    }

    private String extractVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.isEmpty()) return null;
        
        // Handle different YouTube URL formats
        if (videoUrl.contains("youtu.be/")) {
            String[] parts = videoUrl.split("youtu.be/");
            if (parts.length > 1) {
                String id = parts[1].split("\\?")[0];
                return id;
            }
        } else if (videoUrl.contains("youtube.com/watch?v=")) {
            String[] parts = videoUrl.split("v=");
            if (parts.length > 1) {
                String id = parts[1].split("&")[0];
                return id;
            }
        } else if (videoUrl.contains("youtube.com/shorts/")) {
            String[] parts = videoUrl.split("shorts/");
            if (parts.length > 1) {
                String id = parts[1].split("\\?")[0];
                return id;
            }
        }
        
        return null;
    }

    private void loadExercises() {
        exercises = new ArrayList<>();
        
        // Get data from intent
        String programType = getIntent().getStringExtra("program_type");
        int phaseIndex = getIntent().getIntExtra("phase_index", 0);
        
        // Load exercises based on program and phase
        if ("30".equals(programType)) {
            switch (phaseIndex) {
                case 0: // Ngày 1-5
                    exercises.add(new Exercise("Jumping Jacks", "3 hiệp x 20 lần", 30, "https://youtu.be/iSSAk4XCsRA"));
                    exercises.add(new Exercise("Gập bụng cơ bản", "3 hiệp x 15 lần", 30, "https://www.youtube.com/shorts/09UYCDL3CRc"));
                    exercises.add(new Exercise("Plank", "3 hiệp x 30 giây", 30, "https://www.youtube.com/shorts/xe2MXatLTUw"));
                    exercises.add(new Exercise("Mountain Climbers", "3 hiệp x 15 lần", 30, "https://www.youtube.com/shorts/7W4JEfEKuC4"));
                    exercises.add(new Exercise("Leg Raises", "3 hiệp x 12 lần", 30, "https://youtu.be/U4L_6JEv9Jg"));
                    break;
                case 1: // Ngày 6-10
                    exercises.add(new Exercise("Burpees", "3 hiệp x 10 lần", 30, "https://www.youtube.com/shorts/McK6y7t5_XY"));
                    exercises.add(new Exercise("Bicycle Crunches", "3 hiệp x 20 lần", 30, "https://youtu.be/cbKIDZ_XyjY"));
                    exercises.add(new Exercise("Side Plank", "3 hiệp x 30 giây/bên", 30, "https://www.youtube.com/shorts/BtM0a9x1F5o"));
                    exercises.add(new Exercise("Russian Twists", "3 hiệp x 20 lần", 30, "https://youtu.be/VfWoNC-NMII"));
                    exercises.add(new Exercise("High Knees", "3 hiệp x 30 giây", 30, "https://www.youtube.com/shorts/LJMrXG_vPQ8"));
                    exercises.add(new Exercise("Flutter Kicks", "3 hiệp x 20 lần", 30, "https://www.youtube.com/shorts/2DL3HVW93_0"));
                    break;
                case 2: // Ngày 11-15
                    exercises.add(new Exercise("Crunches", "4 hiệp x 20 lần", 30, "https://www.youtube.com/watch?v=Xyd_fa5zoEU"));
                    exercises.add(new Exercise("Reverse Crunches", "4 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=hyv14e2QDq0"));
                    exercises.add(new Exercise("Toe Touches", "3 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=3p8EBPVZ2Iw"));
                    exercises.add(new Exercise("V-Ups", "3 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=iP2fjvG0g3w"));
                    exercises.add(new Exercise("Dead Bug", "3 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=4XLEnwUr0xg"));
                    exercises.add(new Exercise("Heel Touches", "3 hiệp x 20 lần", 30, "https://www.youtube.com/watch?v=9bR-ELq3FQk"));
                    exercises.add(new Exercise("Sit-ups", "3 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=jDwoBqPH0jk"));
                    break;
                case 3: // Ngày 16-20
                    exercises.add(new Exercise("Plank", "4 hiệp x 45 giây", 45, "https://www.youtube.com/watch?v=pSHjTRCQxIw"));
                    exercises.add(new Exercise("Side Plank", "3 hiệp x 40 giây/bên", 40, "https://www.youtube.com/watch?v=K2VljzCC16g"));
                    exercises.add(new Exercise("Plank to Downward Dog", "3 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=2V2V5a7mK3c"));
                    exercises.add(new Exercise("Plank Jacks", "3 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=Pr1ieGZ5atk"));
                    exercises.add(new Exercise("Shoulder Taps", "3 hiệp x 20 lần", 30, "https://www.youtube.com/watch?v=gWHQpMUd9Mo"));
                    exercises.add(new Exercise("Plank Up-Downs", "3 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=6bYQdYw9YjE"));
                    break;
                case 4: // Ngày 21-25
                    exercises.add(new Exercise("Jump Rope", "4 hiệp x 1 phút", 60, "https://www.youtube.com/watch?v=1BZM2Vre5oc"));
                    exercises.add(new Exercise("Mountain Climbers", "4 hiệp x 30 giây", 30, "https://www.youtube.com/watch?v=nmwgirgXLYM"));
                    exercises.add(new Exercise("Burpees", "3 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=TU8QYVW0gDU"));
                    exercises.add(new Exercise("High Knees", "4 hiệp x 45 giây", 45, "https://www.youtube.com/watch?v=OAJ_J3EZkdY"));
                    exercises.add(new Exercise("Jumping Lunges", "3 hiệp x 12 lần/chân", 30, "https://www.youtube.com/watch?v=3XDriUn0udo"));
                    exercises.add(new Exercise("Plank Jacks", "3 hiệp x 20 lần", 30, "https://www.youtube.com/watch?v=Pr1ieGZ5atk"));
                    exercises.add(new Exercise("Bicycle Crunches", "3 hiệp x 25 lần", 30, "https://www.youtube.com/watch?v=9FGilxCbdz8"));
                    break;
                case 5: // Ngày 26-28
                    exercises.add(new Exercise("Advanced Crunches", "4 hiệp x 25 lần", 30, "https://www.youtube.com/watch?v=vkKCVCZe474"));
                    exercises.add(new Exercise("Plank", "4 hiệp x 60 giây", 60, "https://www.youtube.com/watch?v=pSHjTRCQxIw"));
                    exercises.add(new Exercise("V-Ups", "4 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=iP2fjvG0g3w"));
                    exercises.add(new Exercise("Russian Twists", "4 hiệp x 30 lần", 30, "https://www.youtube.com/watch?v=wkD8rjkodUI"));
                    exercises.add(new Exercise("Burpees", "4 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=TU8QYVW0gDU"));
                    exercises.add(new Exercise("Mountain Climbers", "4 hiệp x 45 giây", 45, "https://www.youtube.com/watch?v=nmwgirgXLYM"));
                    break;
                case 6: // Ngày 29
                    exercises.add(new Exercise("Circuit 1: Crunches", "3 hiệp x 20 lần", 30, "https://www.youtube.com/watch?v=Xyd_fa5zoEU"));
                    exercises.add(new Exercise("Circuit 2: Plank", "3 hiệp x 45 giây", 45, "https://www.youtube.com/watch?v=pSHjTRCQxIw"));
                    exercises.add(new Exercise("Circuit 3: Mountain Climbers", "3 hiệp x 30 giây", 30, "https://www.youtube.com/watch?v=nmwgirgXLYM"));
                    exercises.add(new Exercise("Circuit 4: Leg Raises", "3 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=l4kQd9eWclE"));
                    exercises.add(new Exercise("Circuit 5: Burpees", "3 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=TU8QYVW0gDU"));
                    exercises.add(new Exercise("Circuit 6: Russian Twists", "3 hiệp x 25 lần", 30, "https://www.youtube.com/watch?v=wkD8rjkodUI"));
                    exercises.add(new Exercise("Circuit 7: Bicycle Crunches", "3 hiệp x 20 lần", 30, "https://www.youtube.com/watch?v=9FGilxCbdz8"));
                    break;
                case 7: // Ngày 30
                    exercises.add(new Exercise("Max Crunches", "1 phút", 60, "https://www.youtube.com/watch?v=Xyd_fa5zoEU"));
                    exercises.add(new Exercise("Max Plank", "Càng lâu càng tốt", 90, "https://www.youtube.com/watch?v=pSHjTRCQxIw"));
                    exercises.add(new Exercise("Max Burpees", "2 phút", 120, "https://www.youtube.com/watch?v=TU8QYVW0gDU"));
                    exercises.add(new Exercise("Max Mountain Climbers", "1 phút", 60, "https://www.youtube.com/watch?v=nmwgirgXLYM"));
                    exercises.add(new Exercise("Max V-Ups", "1 phút", 60, "https://www.youtube.com/watch?v=iP2fjvG0g3w"));
                    exercises.add(new Exercise("Final Circuit", "3 vòng", 180, "https://www.youtube.com/watch?v=ml6cT4AZdqI"));
                    break;
            }
        } else if ("60".equals(programType)) {
            switch (phaseIndex) {
                case 0: // Tuần 1-2
                    exercises.add(new Exercise("Push-ups", "4 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=IODxDxX7oi4"));
                    exercises.add(new Exercise("Squats", "4 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=aclHkVaku9U"));
                    exercises.add(new Exercise("Lunges", "3 hiệp x 12 lần/chân", 30, "https://www.youtube.com/watch?v=QOVaHwm-Q6U"));
                    exercises.add(new Exercise("Plank", "3 hiệp x 45 giây", 45, "https://www.youtube.com/watch?v=pSHjTRCQxIw"));
                    exercises.add(new Exercise("Dumbbell Rows", "4 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=roCP6wCXPqo"));
                    exercises.add(new Exercise("Shoulder Press", "3 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=qEwKCR5JCog"));
                    exercises.add(new Exercise("Bicep Curls", "3 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=ykJmrZ5v0Oo"));
                    break;
                case 1: // Tuần 3-4
                    exercises.add(new Exercise("Bench Press", "4 hiệp x 10 lần", 30, "https://www.youtube.com/watch?v=rT7DgCr-3pg"));
                    exercises.add(new Exercise("Incline Push-ups", "4 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=cfns5VDVVvk"));
                    exercises.add(new Exercise("Dips", "3 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=2z8JmcrW-As"));
                    exercises.add(new Exercise("Tricep Extensions", "3 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=_gsUck-7M74"));
                    exercises.add(new Exercise("Hammer Curls", "3 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=zC3nLlEvin4"));
                    exercises.add(new Exercise("Cable Flyes", "3 hiệp x 15 lần", 30, "https://www.youtube.com/watch?v=eozdVDA78K0"));
                    exercises.add(new Exercise("Diamond Push-ups", "3 hiệp x 10 lần", 30, "https://www.youtube.com/watch?v=J0DnG1_S92I"));
                    exercises.add(new Exercise("Chest Dips", "3 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=2z8JmcrW-As"));
                    break;
            }
        } else if ("90".equals(programType)) {
            switch (phaseIndex) {
                case 0: // Tháng 1
                    exercises.add(new Exercise("Full Body Warm-up", "10 phút", 600, "https://www.youtube.com/watch?v=R0mMyV5OtcM"));
                    exercises.add(new Exercise("Compound Movements", "4 hiệp x 12 lần", 30, "https://www.youtube.com/watch?v=U0bhE67HuDY"));
                    exercises.add(new Exercise("Core Stability", "4 hiệp x 45 giây", 45, "https://www.youtube.com/watch?v=4XLEnwUr0xg"));
                    exercises.add(new Exercise("Cardio Intervals", "5 hiệp x 2 phút", 120, "https://www.youtube.com/watch?v=ml6cT4AZdqI"));
                    exercises.add(new Exercise("Flexibility Training", "15 phút", 900, "https://www.youtube.com/watch?v=v7AYKMP6rOE"));
                    exercises.add(new Exercise("Recovery Stretches", "10 phút", 600, "https://www.youtube.com/watch?v=qULTwquOuT4"));
                    exercises.add(new Exercise("Mobility Drills", "3 hiệp x 10 lần", 30, "https://www.youtube.com/watch?v=TSIbzfcnv_8"));
                    exercises.add(new Exercise("Balance Exercises", "3 hiệp x 30 giây", 30, "https://www.youtube.com/watch?v=2pLT-olgUJs"));
                    break;
            }
        }
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
        
        btnPrevious.setOnClickListener(v -> {
            if (currentExerciseIndex > 0) {
                stopTimer();
                currentExerciseIndex--;
                displayCurrentExercise();
                loadVideoForCurrentExercise();
            }
        });
        
        btnNext.setOnClickListener(v -> {
            if (currentExerciseIndex < exercises.size() - 1) {
                stopTimer();
                currentExerciseIndex++;
                displayCurrentExercise();
                loadVideoForCurrentExercise();
            } else {
                showCompletionDialog();
            }
        });
        
        btnRest.setOnClickListener(v -> showRestDialog());
        
        btnExit.setOnClickListener(v -> showExitDialog());
    }

    private void displayCurrentExercise() {
        if (exercises.isEmpty()) return;
        
        Exercise exercise = exercises.get(currentExerciseIndex);
        
        tvExerciseName.setText(exercise.name);
        tvExerciseSets.setText(exercise.sets);
        tvProgress.setText(String.format(Locale.getDefault(), "Bài tập %d/%d", 
                currentExerciseIndex + 1, exercises.size()));
        
        timeLeftInMillis = exercise.durationSeconds * 1000L;
        updateTimerText();
        
        progressBar.setProgress(0);
        
        btnPrevious.setEnabled(currentExerciseIndex > 0);
        btnPrevious.setAlpha(currentExerciseIndex > 0 ? 1.0f : 0.5f);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                
                // Update progress bar
                Exercise currentExercise = exercises.get(currentExerciseIndex);
                long totalTime = currentExercise.durationSeconds * 1000L;
                int progress = (int) ((totalTime - timeLeftInMillis) * 100 / totalTime);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                updatePlayButton();
                progressBar.setProgress(100);
                
                // Auto move to next exercise
                if (currentExerciseIndex < exercises.size() - 1) {
                    currentExerciseIndex++;
                    displayCurrentExercise();
                } else {
                    showCompletionDialog();
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

    private void stopTimer() {
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
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rest_options, null);
        builder.setView(dialogView);
        
        AlertDialog dialog = builder.create();
        
        Button btn30Sec = dialogView.findViewById(R.id.btn_rest_30);
        Button btn60Sec = dialogView.findViewById(R.id.btn_rest_60);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        
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
        isRestMode = true;
        timeLeftInMillis = seconds * 1000L;
        
        tvExerciseName.setText("Thời gian nghỉ");
        tvExerciseSets.setText("Thư giãn và hít thở sâu");
        progressBar.setProgress(0);
        
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                
                // Update progress bar for rest
                long totalTime = seconds * 1000L;
                int progress = (int) ((totalTime - timeLeftInMillis) * 100 / totalTime);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                isRestMode = false;
                isTimerRunning = false;
                displayCurrentExercise();
                Toast.makeText(ProgramExerciseSessionActivity.this, 
                        "Hết giờ nghỉ! Tiếp tục tập luyện", Toast.LENGTH_SHORT).show();
            }
        }.start();
        
        isTimerRunning = true;
        updatePlayButton();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Thoát tập luyện?")
                .setMessage("Bạn có chắc muốn dừng buổi tập này?")
                .setPositiveButton("Thoát", (dialog, which) -> finish())
                .setNegativeButton("Tiếp tục", null)
                .show();
    }

    private void showCompletionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hoàn thành! 🎉")
                .setMessage("Chúc mừng bạn đã hoàn thành buổi tập!")
                .setPositiveButton("Xong", (dialog, which) -> finish())
                .setCancelable(false)
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

    private static class Exercise {
        String name;
        String sets;
        int durationSeconds;
        String videoUrl;

        Exercise(String name, String sets, int durationSeconds, String videoUrl) {
            this.name = name;
            this.sets = sets;
            this.durationSeconds = durationSeconds;
            this.videoUrl = videoUrl;
        }
    }
}
