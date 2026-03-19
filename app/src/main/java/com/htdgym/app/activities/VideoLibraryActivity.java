package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.VideoAdapter;
import com.htdgym.app.models.Video;
import com.htdgym.app.viewmodels.VideoViewModel;

import java.util.ArrayList;
import java.util.List;

public class VideoLibraryActivity extends AppCompatActivity {

    private RecyclerView recyclerVideos;
    private VideoAdapter videoAdapter;
    private VideoViewModel videoViewModel;
    private TextView tabAll, tabWorkout, tabYoga, tabCardio;
    private String currentCategory = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_library);

        initViews();
        setupRecyclerView();
        setupTabs();
        loadVideos();
    }

    private void initViews() {
        recyclerVideos = findViewById(R.id.recycler_videos);
        tabAll = findViewById(R.id.tab_all);
        tabWorkout = findViewById(R.id.tab_workout);
        tabYoga = findViewById(R.id.tab_yoga);
        tabCardio = findViewById(R.id.tab_cardio);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thư viện Video");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        videoAdapter = new VideoAdapter(new ArrayList<>(), video -> {
            Intent intent = new Intent(VideoLibraryActivity.this, VideoPlayerActivity.class);
            intent.putExtra("video_id", video.getId());
            intent.putExtra("video_title", video.getTitle());
            intent.putExtra("youtube_id", video.getYoutubeId());
            intent.putExtra("video_duration", video.getDuration());
            startActivity(intent);
        });
        
        recyclerVideos.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerVideos.setAdapter(videoAdapter);
    }

    private void setupTabs() {
        tabAll.setOnClickListener(v -> filterVideos("all"));
        tabWorkout.setOnClickListener(v -> filterVideos("workout"));
        tabYoga.setOnClickListener(v -> filterVideos("yoga"));
        tabCardio.setOnClickListener(v -> filterVideos("cardio"));
    }

    private void filterVideos(String category) {
        currentCategory = category;
        
        tabAll.setBackgroundColor(getResources().getColor(R.color.gray_dark));
        tabWorkout.setBackgroundColor(getResources().getColor(R.color.gray_dark));
        tabYoga.setBackgroundColor(getResources().getColor(R.color.gray_dark));
        tabCardio.setBackgroundColor(getResources().getColor(R.color.gray_dark));
        
        switch (category) {
            case "all":
                tabAll.setBackgroundColor(getResources().getColor(R.color.green_primary));
                break;
            case "workout":
                tabWorkout.setBackgroundColor(getResources().getColor(R.color.green_primary));
                break;
            case "yoga":
                tabYoga.setBackgroundColor(getResources().getColor(R.color.green_primary));
                break;
            case "cardio":
                tabCardio.setBackgroundColor(getResources().getColor(R.color.green_primary));
                break;
        }
        
        loadVideos();
    }

    private void loadVideos() {
        List<Video> sampleVideos = getSampleVideos();
        
        if (!currentCategory.equals("all")) {
            List<Video> filtered = new ArrayList<>();
            for (Video video : sampleVideos) {
                if (video.getCategory().equalsIgnoreCase(currentCategory)) {
                    filtered.add(video);
                }
            }
            videoAdapter.updateVideos(filtered);
        } else {
            videoAdapter.updateVideos(sampleVideos);
        }
    }

    private List<Video> getSampleVideos() {
        List<Video> videos = new ArrayList<>();
        
        videos.add(new Video("Bai tap nguc co ban", "Tap nguc hieu qua", 
                "IODxDxX7oi4", "https://img.youtube.com/vi/IODxDxX7oi4/mqdefault.jpg", 
                "15:30", "Trung binh", "workout"));
        
        videos.add(new Video("Yoga buoi sang", "Yoga nhe nhang", 
                "v7AYKMP6rOE", "https://img.youtube.com/vi/v7AYKMP6rOE/mqdefault.jpg", 
                "20:00", "De", "yoga"));
        
        videos.add(new Video("Cardio dot mo", "Dot chay mo thua", 
                "ml6cT4AZdqI", "https://img.youtube.com/vi/ml6cT4AZdqI/mqdefault.jpg", 
                "25:00", "Kho", "cardio"));
        
        videos.add(new Video("Tap lung hieu qua", "Tap lung manh me", 
                "eE7P4ekCsC4", "https://img.youtube.com/vi/eE7P4ekCsC4/mqdefault.jpg", 
                "18:45", "Trung binh", "workout"));
        
        videos.add(new Video("Yoga giam stress", "Thư gian tinh than", 
                "COp7BR_Dvps", "https://img.youtube.com/vi/COp7BR_Dvps/mqdefault.jpg", 
                "30:00", "De", "yoga"));
        
        videos.add(new Video("HIIT toan than", "Tap toan than", 
                "M0uO8X3_tEA", "https://img.youtube.com/vi/M0uO8X3_tEA/mqdefault.jpg", 
                "22:30", "Kho", "cardio"));
        
        return videos;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
