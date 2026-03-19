package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.htdgym.app.models.Trainer;
import java.util.List;

@Dao
public interface TrainerDao {
    @Query("SELECT * FROM trainers ORDER BY name ASC")
    LiveData<List<Trainer>> getAllTrainers();

    @Query("SELECT * FROM trainers WHERE isAvailable = 1 ORDER BY name ASC")
    LiveData<List<Trainer>> getAvailableTrainers();

    @Query("SELECT * FROM trainers WHERE specialization = :specialization ORDER BY name ASC")
    LiveData<List<Trainer>> getTrainersBySpecialization(String specialization);

    @Insert
    void insertTrainer(Trainer trainer);

    @Update
    void updateTrainer(Trainer trainer);

    @Delete
    void deleteTrainer(Trainer trainer);

    @Query("SELECT COUNT(*) FROM trainers WHERE isAvailable = 1")
    LiveData<Integer> getAvailableTrainerCount();
}