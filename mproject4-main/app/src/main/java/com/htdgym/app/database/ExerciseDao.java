package com.htdgym.app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.htdgym.app.models.Exercise;
import java.util.List;

@Dao
public interface ExerciseDao {
    
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    List<Exercise> getAllExercises();
    
    @Query("SELECT * FROM exercises WHERE isPremium = 0 ORDER BY name ASC")
    List<Exercise> getFreeExercises();
    
    @Query("SELECT * FROM exercises WHERE isPremium = 0 OR :isPremiumUser = 1 ORDER BY name ASC")
    List<Exercise> getExercisesForUser(boolean isPremiumUser);
    
    @Query("SELECT * FROM exercises WHERE category = :category ORDER BY name ASC")
    List<Exercise> getExercisesByCategory(String category);
    
    @Query("SELECT * FROM exercises WHERE category = :category AND (isPremium = 0 OR :isPremiumUser = 1) ORDER BY name ASC")
    List<Exercise> getExercisesByCategoryForUser(String category, boolean isPremiumUser);
    
    @Query("SELECT * FROM exercises WHERE difficulty = :difficulty ORDER BY name ASC")
    List<Exercise> getExercisesByDifficulty(String difficulty);
    
    @Query("SELECT * FROM exercises WHERE difficulty = :difficulty AND (isPremium = 0 OR :isPremiumUser = 1) ORDER BY name ASC")
    List<Exercise> getExercisesByDifficultyForUser(String difficulty, boolean isPremiumUser);
    
    @Query("SELECT * FROM exercises WHERE isFavorite = 1 ORDER BY name ASC")
    List<Exercise> getFavoriteExercises();
    
    @Query("SELECT * FROM exercises WHERE isFavorite = 1 AND (isPremium = 0 OR :isPremiumUser = 1) ORDER BY name ASC")
    List<Exercise> getFavoriteExercisesForUser(boolean isPremiumUser);
    
    @Query("SELECT * FROM exercises WHERE name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    List<Exercise> searchExercises(String searchQuery);
    
    @Query("SELECT * FROM exercises WHERE (name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%') AND (isPremium = 0 OR :isPremiumUser = 1) ORDER BY name ASC")
    List<Exercise> searchExercisesForUser(String searchQuery, boolean isPremiumUser);
    
    @Query("SELECT * FROM exercises WHERE category = :category AND difficulty = :difficulty ORDER BY name ASC")
    List<Exercise> getExercisesByCategoryAndDifficulty(String category, String difficulty);
    
    @Query("SELECT * FROM exercises WHERE category = :category AND difficulty = :difficulty AND (isPremium = 0 OR :isPremiumUser = 1) ORDER BY name ASC")
    List<Exercise> getExercisesByCategoryAndDifficultyForUser(String category, String difficulty, boolean isPremiumUser);
    
    @Query("SELECT * FROM exercises WHERE (category = :category OR :category = 'all') AND (difficulty = :difficulty OR :difficulty = 'all') AND (name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%') ORDER BY name ASC")
    List<Exercise> getFilteredExercises(String category, String difficulty, String searchQuery);
    
    @Query("SELECT * FROM exercises WHERE (category = :category OR :category = 'all') AND (difficulty = :difficulty OR :difficulty = 'all') AND (name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%') AND (isPremium = 0 OR :isPremiumUser = 1) ORDER BY name ASC")
    List<Exercise> getFilteredExercisesForUser(String category, String difficulty, String searchQuery, boolean isPremiumUser);
    
    @Query("SELECT * FROM exercises WHERE id = :id")
    Exercise getExerciseById(int id);
    
    @Query("UPDATE exercises SET isFavorite = :isFavorite WHERE id = :id")
    void updateFavoriteStatus(int id, boolean isFavorite);
    
    @Query("UPDATE exercises SET completedCount = completedCount + 1 WHERE id = :id")
    void incrementCompletedCount(int id);
    
    @Query("SELECT COUNT(*) FROM exercises")
    int getTotalExerciseCount();
    
    @Query("SELECT COUNT(*) FROM exercises WHERE isFavorite = 1")
    int getFavoriteExerciseCount();
    
    @Query("SELECT SUM(completedCount) FROM exercises")
    int getTotalCompletedCount();
    
    @Query("SELECT SUM(calories * completedCount) FROM exercises")
    int getTotalCaloriesBurned();
    
    @Query("SELECT AVG(rating) FROM exercises WHERE ratingCount > 0")
    float getAverageRating();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExercise(Exercise exercise);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExercises(List<Exercise> exercises);
    
    @Update
    void updateExercise(Exercise exercise);
    
    @Delete
    void deleteExercise(Exercise exercise);
    
    @Query("DELETE FROM exercises")
    void deleteAllExercises();
}