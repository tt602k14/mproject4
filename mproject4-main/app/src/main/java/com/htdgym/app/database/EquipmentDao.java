package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.htdgym.app.models.Equipment;
import java.util.List;

@Dao
public interface EquipmentDao {
    @Query("SELECT * FROM equipment ORDER BY name ASC")
    LiveData<List<Equipment>> getAllEquipment();

    @Query("SELECT * FROM equipment WHERE isAvailable = 1 ORDER BY name ASC")
    LiveData<List<Equipment>> getAvailableEquipment();

    @Query("SELECT * FROM equipment WHERE category = :category ORDER BY name ASC")
    LiveData<List<Equipment>> getEquipmentByCategory(String category);

    @Query("SELECT * FROM equipment WHERE nextMaintenance < :currentDate")
    LiveData<List<Equipment>> getEquipmentNeedingMaintenance(long currentDate);

    @Insert
    void insertEquipment(Equipment equipment);

    @Update
    void updateEquipment(Equipment equipment);

    @Delete
    void deleteEquipment(Equipment equipment);

    @Query("SELECT COUNT(*) FROM equipment WHERE isAvailable = 1")
    LiveData<Integer> getAvailableEquipmentCount();
}