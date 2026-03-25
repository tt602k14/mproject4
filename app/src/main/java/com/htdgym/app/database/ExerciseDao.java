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
    long insertExercise(Exercise exercise);

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
    
    // Synchronous version for admin management
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    List<Exercise> getAllExercisesSync();
    
    // Additional methods for WorkoutsFragment
    @Query("SELECT * FROM exercises WHERE isPremium = 0 OR :isPremiumUser = 1")
    List<Exercise> getExercisesForUser(boolean isPremiumUser);
    
    @Query("SELECT COUNT(*) FROM exercises")
    int getTotalExerciseCount();
    
    @Query("SELECT COUNT(*) FROM exercises WHERE isFavorite = 1")
    int getFavoriteExerciseCount();
    
    @Query("SELECT SUM(completedCount) FROM exercises")
    int getTotalCompletedCount();
    
    @Query("SELECT SUM(calories * completedCount) FROM exercises")
    int getTotalCaloriesBurned();
    
    @Query("UPDATE exercises SET isFavorite = :isFavorite WHERE id = :exerciseId")
    void updateFavoriteStatus(int exerciseId, boolean isFavorite);
    
    @Query("UPDATE exercises SET completedCount = completedCount + 1 WHERE id = :exerciseId")
    void incrementCompletedCount(int exerciseId);
}
