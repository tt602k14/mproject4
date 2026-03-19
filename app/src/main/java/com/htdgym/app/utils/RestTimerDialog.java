package com.htdgym.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.htdgym.app.R;

public class RestTimerDialog extends Dialog {

    private TextView tvTimer, tvTitle;
    private ProgressBar progressTimer;
    private Button btnStart, btnPause, btnReset, btnClose;
    private CountDownTimer countDownTimer;
    private Vibrator vibrator;
    
    private long totalTimeInMillis;
    private long timeLeftInMillis;
    private boolean isTimerRunning = false;
    private boolean isPaused = false;
    
    private OnTimerFinishedListener onTimerFinishedListener;

    public interface OnTimerFinishedListener {
        void onTimerFinished();
    }

    public RestTimerDialog(@NonNull Context context, int restTimeInSeconds) {
        super(context, R.style.FullScreenDialog);
        setContentView(R.layout.dialog_rest_timer);
        
        this.totalTimeInMillis = restTimeInSeconds * 1000L;
        this.timeLeftInMillis = totalTimeInMillis;
        
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        
        initViews();
        setupClickListeners();
        updateTimerDisplay();
        updateProgressBar();
    }

    private void initViews() {
        tvTimer = findViewById(R.id.tv_timer);
        tvTitle = findViewById(R.id.tv_title);
        progressTimer = findViewById(R.id.progress_timer);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnReset = findViewById(R.id.btn_reset);
        btnClose = findViewById(R.id.btn_close);
        
        // Set initial states
        btnPause.setVisibility(View.GONE);
        progressTimer.setMax((int) (totalTimeInMillis / 1000));
        
        tvTitle.setText("⏱️ Thời gian nghỉ ngơi");
    }

    private void setupClickListeners() {
        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());
        btnClose.setOnClickListener(v -> {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            dismiss();
        });
    }

    private void startTimer() {
        if (isPaused) {
            // Resume from pause
            isPaused = false;
        }
        
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                timeLeftInMillis = 0;
                updateTimerDisplay();
                updateProgressBar();
                
                // Timer finished - play sound and vibrate
                onTimerComplete();
                
                // Show completion message
                tvTitle.setText("✅ Hết giờ nghỉ!");
                btnStart.setText("🔄 Bắt đầu lại");
                btnStart.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
                
                if (onTimerFinishedListener != null) {
                    onTimerFinishedListener.onTimerFinished();
                }
            }
        }.start();
        
        isTimerRunning = true;
        btnStart.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        isTimerRunning = false;
        isPaused = true;
        
        btnStart.setText("▶️ Tiếp tục");
        btnStart.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        timeLeftInMillis = totalTimeInMillis;
        isTimerRunning = false;
        isPaused = false;
        
        updateTimerDisplay();
        updateProgressBar();
        
        btnStart.setText("▶️ Bắt đầu");
        btnStart.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
        tvTitle.setText("⏱️ Thời gian nghỉ ngơi");
    }

    private void updateTimerDisplay() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    private void updateProgressBar() {
        int progress = (int) ((totalTimeInMillis - timeLeftInMillis) / 1000);
        progressTimer.setProgress(progress);
    }

    private void onTimerComplete() {
        // Vibrate
        if (vibrator != null) {
            vibrator.vibrate(new long[]{0, 500, 200, 500}, -1);
        }
        
        // Play notification sound (optional)
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
            if (mediaPlayer != null) {
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            }
        } catch (Exception e) {
            // Ignore if sound fails
        }
    }

    public void setOnTimerFinishedListener(OnTimerFinishedListener listener) {
        this.onTimerFinishedListener = listener;
    }

    @Override
    public void onBackPressed() {
        // Prevent accidental close during timer
        if (isTimerRunning) {
            pauseTimer();
        } else {
            super.onBackPressed();
        }
    }
}