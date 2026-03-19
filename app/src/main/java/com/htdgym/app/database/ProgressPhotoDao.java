package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.htdgym.app.models.ProgressPhoto;

import java.util.List;

@Dao
public interface ProgressPhotoDao {

    @Insert
    long insert(ProgressPhoto progressPhoto);

    @Update
    void update(ProgressPhoto progressPhoto);

    @Delete
    void delete(ProgressPhoto progressPhoto);

    @Query("SELECT * FROM progress_photos WHERE userId = :userId AND isActive = 1 ORDER BY date DESC")
    List<ProgressPhoto> getPhotosByUserId(int userId);

    @Query("SELECT * FROM progress_photos WHERE id = :id")
    ProgressPhoto getPhotoById(int id);

    @Query("SELECT * FROM progress_photos WHERE userId = :userId AND bodyPart = :bodyPart AND isActive = 1 ORDER BY date DESC")
    List<ProgressPhoto> getPhotosByBodyPart(int userId, String bodyPart);

    @Query("SELECT COUNT(*) FROM progress_photos WHERE userId = :userId AND isActive = 1")
    int getPhotoCount(int userId);

    @Query("UPDATE progress_photos SET isActive = 0 WHERE id = :id")
    void deactivatePhoto(int id);

    @Query("SELECT * FROM progress_photos WHERE userId = :userId AND date BETWEEN :startDate AND :endDate AND isActive = 1 ORDER BY date DESC")
    List<ProgressPhoto> getPhotosByDateRange(int userId, long startDate, long endDate);
}