package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.htdgym.app.models.Video;

import java.util.List;

@Dao
public interface VideoDao {
    
    @Query("SELECT * FROM videos ORDER BY id DESC")
    LiveData<List<Video>> getAllVideos();
    
    @Query("SELECT * FROM videos WHERE category = :category ORDER BY id DESC")
    LiveData<List<Video>> getVideosByCategory(String category);
    
    @Query("SELECT * FROM videos WHERE isSaved = 1 ORDER BY id DESC")
    LiveData<List<Video>> getSavedVideos();
    
    @Query("SELECT * FROM videos WHERE id = :videoId")
    LiveData<Video> getVideoById(int videoId);
    
    @Insert
    void insertVideo(Video video);
    
    @Insert
    void insertVideos(List<Video> videos);
    
    @Update
    void updateVideo(Video video);
    
    @Delete
    void deleteVideo(Video video);
    
    @Query("DELETE FROM videos")
    void deleteAllVideos();
}
