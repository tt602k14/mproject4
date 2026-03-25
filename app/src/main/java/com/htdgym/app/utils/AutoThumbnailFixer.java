package com.htdgym.app.utils;

import android.content.Context;
import android.util.Log;

/**
 * Automatically fix all missing thumbnails in the app
 */
public class AutoThumbnailFixer {
    
    private static final String TAG = "AutoThumbnailFixer";
    
    /**
     * Fix all missing thumbnails in the app
     */
    public static void fixAllMissingThumbnails(Context context) {
        Log.i(TAG, "🚀 Starting automatic thumbnail fix...");
        
        // 1. Update database exercises
        ExerciseThumbnailUpdater.updateMissingThumbnails(context);
        
        // 2. Pre-download important thumbnails
        ThumbnailManager.preDownloadImportantThumbnails(context);
        
        // 3. Add additional exercise mappings
        addAdditionalExerciseMappings();
        
        Log.i(TAG, "✅ Automatic thumbnail fix completed!");
    }
    
    /**
     * Add additional exercise mappings for better coverage
     */
    private static void addAdditionalExerciseMappings() {
        // Rest day variations
        ExerciseThumbnailUpdater.addExerciseThumbnail("Rest Day", "https://youtu.be/inpok4MKVLM");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Recovery", "https://youtu.be/ZToicYcHIOU");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Active Rest", "https://youtu.be/v7AYKMP6rOE");
        
        // Common exercise variations
        ExerciseThumbnailUpdater.addExerciseThumbnail("Push Up", "https://youtu.be/IODxDxX7oi4");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Pushup", "https://youtu.be/IODxDxX7oi4");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Push-Up", "https://youtu.be/IODxDxX7oi4");
        
        ExerciseThumbnailUpdater.addExerciseThumbnail("Sit Up", "https://youtu.be/1fbU_MkV7NE");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Situp", "https://youtu.be/1fbU_MkV7NE");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Sit-Up", "https://youtu.be/1fbU_MkV7NE");
        
        // Vietnamese exercise names
        ExerciseThumbnailUpdater.addExerciseThumbnail("Hít đất", "https://youtu.be/IODxDxX7oi4");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Gập bụng", "https://youtu.be/1fbU_MkV7NE");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Chạy bộ", "https://youtu.be/8opcQdC-V-U");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Nhảy dây", "https://youtu.be/FJmRQ5iTXKE");
        
        Log.d(TAG, "Added additional exercise mappings");
    }
    
    /**
     * Fix thumbnails for specific exercise categories
     */
    public static void fixCategoryThumbnails(Context context, String category) {
        Log.i(TAG, "🎯 Fixing thumbnails for category: " + category);
        
        switch (category.toLowerCase()) {
            case "chest":
            case "ngực":
                fixChestExercises(context);
                break;
            case "legs":
            case "chân":
                fixLegExercises(context);
                break;
            case "abs":
            case "bụng":
                fixAbsExercises(context);
                break;
            case "back":
            case "lưng":
                fixBackExercises(context);
                break;
            case "shoulder":
            case "vai":
                fixShoulderExercises(context);
                break;
            case "rest":
            case "nghỉ":
                fixRestDays(context);
                break;
            default:
                fixAllMissingThumbnails(context);
                break;
        }
    }
    
    private static void fixChestExercises(Context context) {
        ExerciseThumbnailUpdater.addExerciseThumbnail("Bench Press", "https://youtu.be/rT7DgCr-3pg");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Incline Press", "https://youtu.be/DbFgADa2PL8");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Chest Fly", "https://youtu.be/eozdVDA78K0");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Dips", "https://youtu.be/2z8JmcrW-As");
    }
    
    private static void fixLegExercises(Context context) {
        ExerciseThumbnailUpdater.addExerciseThumbnail("Squat", "https://youtu.be/ultWZbUMPL8");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Leg Press", "https://youtu.be/IZxyjW7MPJQ");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Lunges", "https://youtu.be/QOVaHwm-Q6U");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Calf Raise", "https://youtu.be/gwLzBJYoWlI");
    }
    
    private static void fixAbsExercises(Context context) {
        ExerciseThumbnailUpdater.addExerciseThumbnail("Plank", "https://youtu.be/pSHjTRCQxIw");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Crunches", "https://youtu.be/1fbU_MkV7NE");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Russian Twist", "https://youtu.be/wkD8rjkodUI");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Leg Raise", "https://youtu.be/JB2oyawG9KI");
    }
    
    private static void fixBackExercises(Context context) {
        ExerciseThumbnailUpdater.addExerciseThumbnail("Pull-up", "https://youtu.be/eGo4IYlbE5g");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Deadlift", "https://youtu.be/op9kVnSso6Q");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Barbell Row", "https://youtu.be/G8l_8chR5BE");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Lat Pulldown", "https://youtu.be/CAwf7n6Luuc");
    }
    
    private static void fixShoulderExercises(Context context) {
        ExerciseThumbnailUpdater.addExerciseThumbnail("Shoulder Press", "https://youtu.be/2yjwXTZQDDI");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Lateral Raise", "https://youtu.be/3VcKaXpzqRo");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Front Raise", "https://youtu.be/qM5X-_FfCQE");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Rear Delt Fly", "https://youtu.be/T3b5yJNhjhQ");
    }
    
    private static void fixRestDays(Context context) {
        ExerciseThumbnailUpdater.addExerciseThumbnail("Nghỉ ngơi", "https://youtu.be/inpok4MKVLM");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Rest Day", "https://youtu.be/ZToicYcHIOU");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Recovery", "https://youtu.be/v7AYKMP6rOE");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Meditation", "https://youtu.be/1ZYbU82GVz4");
        ExerciseThumbnailUpdater.addExerciseThumbnail("Stretching", "https://youtu.be/COp7BR_Dvps");
    }
}