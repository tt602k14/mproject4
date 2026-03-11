package com.htdgym.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.htdgym.app.models.Equipment;
import com.htdgym.app.repository.GymRepository;
import java.util.List;

public class EquipmentViewModel extends AndroidViewModel {
    private GymRepository repository;
    private LiveData<List<Equipment>> allEquipment;
    private LiveData<List<Equipment>> availableEquipment;

    public EquipmentViewModel(@NonNull Application application) {
        super(application);
        repository = new GymRepository(application);
        allEquipment = repository.getAllEquipment();
        availableEquipment = repository.getAvailableEquipment();
    }

    public LiveData<List<Equipment>> getAllEquipment() {
        return allEquipment;
    }

    public LiveData<List<Equipment>> getAvailableEquipment() {
        return availableEquipment;
    }

    public void insert(Equipment equipment) {
        repository.insertEquipment(equipment);
    }

    public void update(Equipment equipment) {
        repository.updateEquipment(equipment);
    }

    public void delete(Equipment equipment) {
        repository.deleteEquipment(equipment);
    }
}