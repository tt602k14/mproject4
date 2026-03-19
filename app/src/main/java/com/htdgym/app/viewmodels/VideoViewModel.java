package com.htdgym.app.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.database.VideoDao;
import com.htdgym.app.models.Video;
import com.htdgym.app.repository.GymRepository;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {

    private GymRepository repository;
    private LiveData<List<Video>> allVideos;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        repository = new GymRepository(application);
        allVideos = repository.getAllVideos();
    }

    public LiveData<List<Video>> getAllVideos() {
        return allVideos;
    }

    public LiveData<List<Video>> getVideosByCategory(String category) {
        return repository.getVideosByCategory(category);
    }

    public LiveData<List<Video>> getSavedVideos() {
        return repository.getSavedVideos();
    }

    public void insert(Video video) {
        repository.insert(video);
    }

    public void update(Video video) {
        repository.update(video);
    }
}
