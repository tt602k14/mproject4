package com.htdgym.app.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.htdgym.app.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;
    private TextView tvVideoTitle, tvVideoDescription, tvVideoDifficulty, tvVideoDuration, tvVideoCategory;
    private Button btnAddToWorkout, btnSaveVideo;
    
    private String videoId;
    private String videoTitle;
    private boolean isSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        initViews();
        setupToolbar();
        loadVideoData();
        setupYouTubePlayer();
        setupClickListeners();
    }

    private void initViews() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        tvVideoTitle = findViewById(R.id.tv_video_title);
        tvVideoDescription = findViewById(R.id.tv_video_description);
        tvVideoDifficulty = findViewById(R.id.tv_video_difficulty);
        tvVideoDuration = findViewById(R.id.tv_video_duration);
        tvVideoCategory = findViewById(R.id.tv_video_category);
        btnAddToWorkout = findViewById(R.id.btn_add_to_workout);
        btnSaveVideo = findViewById(R.id.btn_save_video);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadVideoData() {
        // Get data from intent
        videoId = getIntent().getStringExtra("video_id");
        videoTitle = getIntent().getStringExtra("video_title");
        String description = getIntent().getStringExtra("video_description");
        String difficulty = getIntent().getStringExtra("video_difficulty");
        String duration = getIntent().getStringExtra("video_duration");
        String category = getIntent().getStringExtra("video_category");

        // Set default if null
        if (videoId == null) videoId = "hWbUlkb5Ms4"; // Default Bench Press video
        if (videoTitle == null) videoTitle = "Hướng dẫn Bench Press";
        if (description == null) description = "Learn proper bench press technique";
        if (difficulty == null) difficulty = "Trung cấp";
        if (duration == null) duration = "07:34";
        if (category == null) category = "Ngực";

        tvVideoTitle.setText(videoTitle);
        tvVideoDescription.setText(description);
        tvVideoDifficulty.setText(difficulty);
        tvVideoDuration.setText(duration);
        tvVideoCategory.setText(category);
    }

    private void setupYouTubePlayer() {
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }

    private void setupClickListeners() {
        btnAddToWorkout.setOnClickListener(v -> {
            Toast.makeText(this, "Đã thêm video vào buổi tập", Toast.LENGTH_SHORT).show();
        });

        btnSaveVideo.setOnClickListener(v -> {
            isSaved = !isSaved;
            if (isSaved) {
                btnSaveVideo.setText("✓ Đã lưu");
                Toast.makeText(this, "Đã lưu video", Toast.LENGTH_SHORT).show();
            } else {
                btnSaveVideo.setText("🔖 Lưu");
                Toast.makeText(this, "Đã bỏ lưu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
}
