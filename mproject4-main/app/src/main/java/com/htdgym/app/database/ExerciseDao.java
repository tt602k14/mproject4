package com.htdgym.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.htdgym.app.models.Exercise;
import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM exercises WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name ASC")
    LiveData<List<Exercise>> searchExercises(String searchTerm);

    @Query("SELECT * FROM exercises WHERE id = :id")
    LiveData<Exercise> getExerciseById(int id);

    @Insert
    void insertExercise(Exercise exercise);

    @Insert
    void insertExercises(List<Exercise> exercises);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);

    @Query("DELETE FROM exercises")
    void deleteAllExercises();

    @Query("SELECT COUNT(*) FROM exercises")
    LiveData<Integer> getExerciseCount();
    
    @Query("SELECT * FROM exercises")
    List<Exercise> getAllExercisesList();
}
