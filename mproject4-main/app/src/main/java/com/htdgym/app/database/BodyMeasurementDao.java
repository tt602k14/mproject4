package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.htdgym.app.models.BodyMeasurement;
import java.util.List;

@Dao
public interface BodyMeasurementDao {
    @Insert
    long insert(BodyMeasurement measurement);
    
    @Query("SELECT * FROM body_measurements WHERE userId = :userId ORDER BY date DESC")
    List<BodyMeasurement> getAllMeasurements(int userId);
    
    @Query("SELECT * FROM body_measurements WHERE userId = :userId ORDER BY date DESC LIMIT 1")
    BodyMeasurement getLatestMeasurement(int userId);
    
    @Query("SELECT * FROM body_measurements WHERE userId = :userId ORDER BY date DESC LIMIT :limit")
    List<BodyMeasurement> getRecentMeasurements(int userId, int limit);
}
