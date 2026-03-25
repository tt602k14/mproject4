package com.htdgym.app.utils;

import android.content.Context;
import android.util.Log;

import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.Exercise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Utility class to update missing thumbnails for exercises
 */
public class ExerciseThumbnailUpdater {
    
    private static final String TAG = "ExerciseThumbnailUpdater";
    
    // Map of exercise names to YouTube URLs for missing thumbnails
    private static final Map<String, String> EXERCISE_THUMBNAILS = new HashMap<>();
    
    static {
        // Basic exercises from WorkoutLibraryActivity
        EXERCISE_THUMBNAILS.put("Bench Press", "https://youtu.be/rT7DgCr-3pg");
        EXERCISE_THUMBNAILS.put("Push-up", "https://youtu.be/IODxDxX7oi4");
        EXERCISE_THUMBNAILS.put("Squat", "https://youtu.be/ultWZbUMPL8");
        EXERCISE_THUMBNAILS.put("Leg Press", "https://youtu.be/IZxyjW7MPJQ");
        EXERCISE_THUMBNAILS.put("Deadlift", "https://youtu.be/op9kVnSso6Q");
        EXERCISE_THUMBNAILS.put("Pull-up", "https://youtu.be/eGo4IYlbE5g");
        EXERCISE_THUMBNAILS.put("Bicep Curl", "https://youtu.be/ykJmrZ5v0Oo");
        EXERCISE_THUMBNAILS.put("Plank", "https://youtu.be/pSHjTRCQxIw");
        
        // Rest day exercises - relaxing/meditation videos
        EXERCISE_THUMBNAILS.put("Nghỉ ngơi", "https://youtu.be/inpok4MKVLM"); // Meditation & Rest
        EXERCISE_THUMBNAILS.put("Ngày 2: Nghỉ ngơi", "https://youtu.be/ZToicYcHIOU"); // Gentle Stretching
        EXERCISE_THUMBNAILS.put("Ngày 4: Nghỉ ngơi", "https://youtu.be/v7AYKMP6rOE"); // Relaxation
        EXERCISE_THUMBNAILS.put("Ngày 6: Nghỉ ngơi", "https://youtu.be/1ZYbU82GVz4"); // Recovery Yoga
        EXERCISE_THUMBNAILS.put("Ngày 7: Nghỉ ngơi", "https://youtu.be/COp7BR_Dvps"); // Mindfulness
        EXERCISE_THUMBNAILS.put("Ngày 9: Nghỉ ngơi", "https://youtu.be/ZToicYcHIOU"); // Gentle Stretching
        EXERCISE_THUMBNAILS.put("Ngày 11: Nghỉ ngơi", "https://youtu.be/v7AYKMP6rOE"); // Relaxation
        EXERCISE_THUMBNAILS.put("Ngày 13: Nghỉ ngơi", "https://youtu.be/1ZYbU82GVz4"); // Recovery Yoga
        EXERCISE_THUMBNAILS.put("Ngày 14: Nghỉ ngơi", "https://youtu.be/COp7BR_Dvps"); // Mindfulness
        EXERCISE_THUMBNAILS.put("Ngày 17: Nghỉ ngơi", "https://youtu.be/inpok4MKVLM"); // Meditation
        EXERCISE_THUMBNAILS.put("Ngày 20: Nghỉ ngơi", "https://youtu.be/ZToicYcHIOU"); // Gentle Stretching
        EXERCISE_THUMBNAILS.put("Ngày 21: Nghỉ ngơi", "https://youtu.be/v7AYKMP6rOE"); // Relaxation
        EXERCISE_THUMBNAILS.put("Ngày 24: Nghỉ ngơi", "https://youtu.be/1ZYbU82GVz4"); // Recovery Yoga
        EXERCISE_THUMBNAILS.put("Ngày 27: Nghỉ ngơi", "https://youtu.be/COp7BR_Dvps"); // Mindfulness
        EXERCISE_THUMBNAILS.put("Ngày 28: Nghỉ ngơi", "https://youtu.be/inpok4MKVLM"); // Meditation
        EXERCISE_THUMBNAILS.put("Ngày 34: Nghỉ ngơi", "https://youtu.be/ZToicYcHIOU"); // Gentle Stretching
        EXERCISE_THUMBNAILS.put("Ngày 35: Nghỉ ngơi", "https://youtu.be/v7AYKMP6rOE"); // Relaxation
        EXERCISE_THUMBNAILS.put("Ngày 41: Nghỉ ngơi", "https://youtu.be/1ZYbU82GVz4"); // Recovery Yoga
        EXERCISE_THUMBNAILS.put("Ngày 42: Nghỉ ngơi", "https://youtu.be/COp7BR_Dvps"); // Mindfulness
        
        // Additional common exercises that might be missing thumbnails
        EXERCISE_THUMBNAILS.put("Hít đất cơ bản", "https://youtu.be/IODxDxX7oi4");
        EXERCISE_THUMBNAILS.put("Gánh đùi", "https://youtu.be/Xe1mCFljUN0");
        EXERCISE_THUMBNAILS.put("Gập bụng", "https://youtu.be/1fbU_MkV7NE");
        EXERCISE_THUMBNAILS.put("Tư thế siêu nhân", "https://youtu.be/cc7kIfSUWEY");
        EXERCISE_THUMBNAILS.put("Hít đất vai", "https://youtu.be/d1HZBdD0idE");
        EXERCISE_THUMBNAILS.put("Burpees", "https://youtu.be/JZQA08SlJnM");
        
        // Muscle building exercises
        EXERCISE_THUMBNAILS.put("Dumbbell Press", "https://youtu.be/VmB1G1K7v94");
        EXERCISE_THUMBNAILS.put("Incline Press", "https://youtu.be/DbFgADa2PL8");
        EXERCISE_THUMBNAILS.put("Chest Fly", "https://youtu.be/eozdVDA78K0");
        EXERCISE_THUMBNAILS.put("Shoulder Press", "https://youtu.be/2yjwXTZQDDI");
        EXERCISE_THUMBNAILS.put("Lateral Raise", "https://youtu.be/3VcKaXpzqRo");
        EXERCISE_THUMBNAILS.put("Front Raise", "https://youtu.be/qM5X-_FfCQE");
        EXERCISE_THUMBNAILS.put("Leg Extension", "https://youtu.be/YyvSfVjQeL0");
        EXERCISE_THUMBNAILS.put("Leg Curl", "https://youtu.be/ELOCsoDSmrg");
        EXERCISE_THUMBNAILS.put("Calf Raise", "https://youtu.be/gwLzBJYoWlI");
        EXERCISE_THUMBNAILS.put("Tricep Dips", "https://youtu.be/2z8JmcrW-As");
        EXERCISE_THUMBNAILS.put("Tricep Extension", "https://youtu.be/nRiJVZDpdL0");
        EXERCISE_THUMBNAILS.put("Hammer Curl", "https://youtu.be/zC3nLlEvin4");
        EXERCISE_THUMBNAILS.put("Preacher Curl", "https://youtu.be/fIWP-FRFNU0");
        
        // Core and abs exercises
        EXERCISE_THUMBNAILS.put("Russian Twist", "https://youtu.be/wkD8rjkodUI");
        EXERCISE_THUMBNAILS.put("Leg Raise", "https://youtu.be/JB2oyawG9KI");
        EXERCISE_THUMBNAILS.put("Bicycle Crunch", "https://youtu.be/9FGilxCbdz8");
        EXERCISE_THUMBNAILS.put("Side Plank", "https://youtu.be/K2VljzCC16g");
        EXERCISE_THUMBNAILS.put("Mountain Climber", "https://youtu.be/nmwgirgXLYM");
        EXERCISE_THUMBNAILS.put("Dead Bug", "https://youtu.be/g_BYB0R-4Ws");
        
        // Back exercises
        EXERCISE_THUMBNAILS.put("Lat Pulldown", "https://youtu.be/CAwf7n6Luuc");
        EXERCISE_THUMBNAILS.put("Seated Row", "https://youtu.be/GZbfZ033f74");
        EXERCISE_THUMBNAILS.put("T-Bar Row", "https://youtu.be/j3Igk5nyZE4");
        EXERCISE_THUMBNAILS.put("Face Pull", "https://youtu.be/rep-qVOkqgk");
        EXERCISE_THUMBNAILS.put("Reverse Fly", "https://youtu.be/T3b5yJNhjhQ");
        
        // Cardio exercises
        EXERCISE_THUMBNAILS.put("Jumping Jacks", "https://youtu.be/c4DAnQ6DtF8");
        EXERCISE_THUMBNAILS.put("High Knees", "https://youtu.be/8opcQdC-V-U");
        EXERCISE_THUMBNAILS.put("Butt Kicks", "https://youtu.be/8opcQdC-V-U");
        EXERCISE_THUMBNAILS.put("Jump Rope", "https://youtu.be/FJmRQ5iTXKE");
        EXERCISE_THUMBNAILS.put("Box Jump", "https://youtu.be/A-cFYWvaHr0");
        EXERCISE_THUMBNAILS.put("Sprint", "https://youtu.be/8opcQdC-V-U");
    }
    
    /**
     * Update all exercises in database that have missing or empty thumbnail URLs
     */
    public static void updateMissingThumbnails(Context context) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                GymDatabase database = GymDatabase.getInstance(context);
                List<Exercise> exercises = database.exerciseDao().getAllExercisesList();
                
                int updatedCount = 0;
                
                for (Exercise exercise : exercises) {
                    String currentUrl = exercise.getVideoUrl();
                    String currentThumbnail = exercise.getThumbnailUrl();
                    
                    // Check if exercise needs thumbnail update
                    if ((currentUrl == null || currentUrl.isEmpty()) && 
                        (currentThumbnail == null || currentThumbnail.isEmpty())) {
                        
                        String newUrl = findThumbnailUrl(exercise.getName());
                        if (newUrl != null) {
                            exercise.setVideoUrl(newUrl);
                            exercise.setThumbnailUrl(generateThumbnailUrl(newUrl));
                            database.exerciseDao().updateExercise(exercise);
                            updatedCount++;
                            
                            Log.d(TAG, "Updated thumbnail for: " + exercise.getName() + " -> " + newUrl);
                        }
                    }
                }
                
                Log.i(TAG, "Updated thumbnails for " + updatedCount + " exercises");
                
            } catch (Exception e) {
                Log.e(TAG, "Error updating thumbnails", e);
            }
        });
    }
    
    /**
     * Find thumbnail URL for exercise name
     */
    private static String findThumbnailUrl(String exerciseName) {
        if (exerciseName == null) return null;
        
        // Direct match
        if (EXERCISE_THUMBNAILS.containsKey(exerciseName)) {
            return EXERCISE_THUMBNAILS.get(exerciseName);
        }
        
        // Partial match for rest days
        if (exerciseName.toLowerCase().contains("nghỉ ngơi")) {
            return EXERCISE_THUMBNAILS.get("Nghỉ ngơi");
        }
        
        // Partial match for common exercises
        String lowerName = exerciseName.toLowerCase();
        if (lowerName.contains("hít đất") || lowerName.contains("push")) {
            return EXERCISE_THUMBNAILS.get("Hít đất cơ bản");
        }
        if (lowerName.contains("gánh đùi") || lowerName.contains("squat")) {
            return EXERCISE_THUMBNAILS.get("Gánh đùi");
        }
        if (lowerName.contains("gập bụng") || lowerName.contains("crunch")) {
            return EXERCISE_THUMBNAILS.get("Gập bụng");
        }
        if (lowerName.contains("plank")) {
            return EXERCISE_THUMBNAILS.get("Plank");
        }
        if (lowerName.contains("burpee")) {
            return EXERCISE_THUMBNAILS.get("Burpees");
        }
        
        return null;
    }
    
    /**
     * Generate thumbnail URL from YouTube video URL
     */
    private static String generateThumbnailUrl(String videoUrl) {
        String videoId = YouTubeHelper.extractVideoId(videoUrl);
        if (videoId != null) {
            return "https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
        }
        return "";
    }
    
    /**
     * Add new exercise thumbnail mapping
     */
    public static void addExerciseThumbnail(String exerciseName, String youtubeUrl) {
        EXERCISE_THUMBNAILS.put(exerciseName, youtubeUrl);
    }
    
    /**
     * Get all available exercise thumbnails
     */
    public static Map<String, String> getAllExerciseThumbnails() {
        return new HashMap<>(EXERCISE_THUMBNAILS);
    }
    
    /**
     * Update specific exercise with new thumbnail
     */
    public static void updateExerciseThumbnail(Context context, String exerciseName, String youtubeUrl) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                GymDatabase database = GymDatabase.getInstance(context);
                List<Exercise> exercises = database.exerciseDao().getAllExercisesList();
                
                for (Exercise exercise : exercises) {
                    if (exercise.getName().equals(exerciseName)) {
                        exercise.setVideoUrl(youtubeUrl);
                        exercise.setThumbnailUrl(generateThumbnailUrl(youtubeUrl));
                        database.exerciseDao().updateExercise(exercise);
                        
                        Log.d(TAG, "Updated specific exercise: " + exerciseName + " -> " + youtubeUrl);
                        break;
                    }
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error updating specific exercise thumbnail", e);
            }
        });
    }
}