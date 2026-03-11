package com.htdgym.app.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.htdgym.app.models.Workout;
import com.htdgym.app.repository.GymRepository;
import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private GymRepository repository;
    private LiveData<List<Workout>> allWorkouts;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        repository = new GymRepository(application);
        allWorkouts = repository.getAllWorkouts();
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }

    public LiveData<List<Workout>> searchWorkouts(String searchTerm) {
        return repository.searchWorkouts(searchTerm);
    }

    public void insert(Workout workout) {
        repository.insertWorkout(workout);
    }

    public void update(Workout workout) {
        repository.updateWorkout(workout);
    }

    public void delete(Workout workout) {
        repository.deleteWorkout(workout);
    }
}