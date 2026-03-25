package com.htdgym.app.utils;

import android.content.Context;
import android.util.Log;

import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.Exercise;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Utility class để tự động cập nhật thumbnailUrl cho các bài tập chưa có ảnh
 */
public class ThumbnailUpdater {
    
    private static final String TAG = "ThumbnailUpdater";
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    
    /**
     * Cập nhật thumbnailUrl cho tất cả các bài tập chưa có ảnh
     */
    public static void updateMissingThumbnails(Context context) {
        executor.execute(() -> {
            try {
                GymDatabase database = GymDatabase.getInstance(context);
                List<Exercise> exercises = database.exerciseDao().getAllExercisesList();
                
                int updatedCount = 0;
                for (Exercise exercise : exercises) {
                    if (shouldUpdateThumbnail(exercise)) {
                        String thumbnailUrl = generateThumbnailUrl(exercise.getVideoUrl());
                        if (thumbnailUrl != null) {
                            exercise.setThumbnailUrl(thumbnailUrl);
                            database.exerciseDao().updateExercise(exercise);
                            updatedCount++;
                            Log.d(TAG, "Updated thumbnail for: " + exercise.getName() + " -> " + thumbnailUrl);
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
     * Force update tất cả thumbnails (bao gồm cả những cái đã có)
     */
    public static void forceUpdateAllThumbnails(Context context) {
        executor.execute(() -> {
            try {
                GymDatabase database = GymDatabase.getInstance(context);
                List<Exercise> exercises = database.exerciseDao().getAllExercisesList();
                
                int updatedCount = 0;
                for (Exercise exercise : exercises) {
                    if (exercise.getVideoUrl() != null && !exercise.getVideoUrl().isEmpty() 
                        && exercise.getVideoUrl().contains("youtu")) {
                        String thumbnailUrl = generateThumbnailUrl(exercise.getVideoUrl());
                        if (thumbnailUrl != null) {
                            exercise.setThumbnailUrl(thumbnailUrl);
                            database.exerciseDao().updateExercise(exercise);
                            updatedCount++;
                            Log.d(TAG, "Force updated thumbnail for: " + exercise.getName() + " -> " + thumbnailUrl);
                        }
                    }
                }
                
                Log.i(TAG, "Force updated thumbnails for " + updatedCount + " exercises");
                
            } catch (Exception e) {
                Log.e(TAG, "Error force updating thumbnails", e);
            }
        });
    }
    
    /**
     * Kiểm tra xem bài tập có cần cập nhật thumbnail không
     */
    private static boolean shouldUpdateThumbnail(Exercise exercise) {
        return (exercise.getThumbnailUrl() == null || exercise.getThumbnailUrl().isEmpty()) 
                && exercise.getVideoUrl() != null 
                && !exercise.getVideoUrl().isEmpty()
                && exercise.getVideoUrl().contains("youtu");
    }
    
    /**
     * Tạo thumbnail URL từ YouTube video URL
     */
    private static String generateThumbnailUrl(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) return null;
        
        String videoId = extractVideoId(youtubeUrl);
        if (videoId != null) {
            // Use the same URL format as YouTubeHelper (i.ytimg.com)
            return "https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
        }
        return null;
    }
    
    /**
     * Trích xuất video ID từ YouTube URL
     */
    private static String extractVideoId(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) return null;
        
        try {
            // Handle youtu.be format
            if (youtubeUrl.contains("youtu.be/")) {
                String[] parts = youtubeUrl.split("youtu.be/");
                if (parts.length > 1) {
                    String videoId = parts[1];
                    if (videoId.contains("?")) {
                        videoId = videoId.split("\\?")[0];
                    }
                    return videoId;
                }
            }
            
            // Handle youtube.com format
            if (youtubeUrl.contains("watch?v=")) {
                String[] parts = youtubeUrl.split("watch\\?v=");
                if (parts.length > 1) {
                    String videoId = parts[1];
                    if (videoId.contains("&")) {
                        videoId = videoId.split("&")[0];
                    }
                    return videoId;
                }
            }
            
            // Handle youtube.com/embed format
            if (youtubeUrl.contains("/embed/")) {
                String[] parts = youtubeUrl.split("/embed/");
                if (parts.length > 1) {
                    String videoId = parts[1];
                    if (videoId.contains("?")) {
                        videoId = videoId.split("\\?")[0];
                    }
                    return videoId;
                }
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error extracting video ID from: " + youtubeUrl, e);
        }
        
        return null;
    }
    
    /**
     * Cập nhật thumbnail cho một bài tập cụ thể
     */
    public static void updateExerciseThumbnail(Context context, Exercise exercise) {
        if (shouldUpdateThumbnail(exercise)) {
            executor.execute(() -> {
                try {
                    String thumbnailUrl = generateThumbnailUrl(exercise.getVideoUrl());
                    if (thumbnailUrl != null) {
                        exercise.setThumbnailUrl(thumbnailUrl);
                        GymDatabase database = GymDatabase.getInstance(context);
                        database.exerciseDao().updateExercise(exercise);
                        Log.d(TAG, "Updated thumbnail for: " + exercise.getName());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error updating thumbnail for exercise: " + exercise.getName(), e);
                }
            });
        }
    }
    
    /**
     * Kiểm tra xem thumbnail URL có hợp lệ không
     */
    public static boolean isValidThumbnailUrl(String thumbnailUrl) {
        return thumbnailUrl != null 
                && !thumbnailUrl.isEmpty() 
                && (thumbnailUrl.startsWith("https://i.ytimg.com/") 
                    || thumbnailUrl.startsWith("https://img.youtube.com/"));
    }
    
    /**
     * Debug method để log tất cả exercises và thumbnail status
     */
    public static void debugLogExercises(Context context) {
        executor.execute(() -> {
            try {
                GymDatabase database = GymDatabase.getInstance(context);
                List<Exercise> exercises = database.exerciseDao().getAllExercisesList();
                
                Log.d(TAG, "=== DEBUG: Exercise Thumbnail Status ===");
                Log.d(TAG, "Total exercises: " + exercises.size());
                
                int hasVideo = 0;
                int hasThumbnail = 0;
                int missingThumbnail = 0;
                int noVideo = 0;
                
                for (Exercise exercise : exercises) {
                    String status = "NO_VIDEO";
                    if (exercise.getVideoUrl() != null && !exercise.getVideoUrl().isEmpty()) {
                        hasVideo++;
                        if (exercise.getThumbnailUrl() != null && !exercise.getThumbnailUrl().isEmpty()) {
                            status = "HAS_THUMBNAIL";
                            hasThumbnail++;
                        } else {
                            status = "MISSING_THUMBNAIL";
                            missingThumbnail++;
                        }
                    } else {
                        noVideo++;
                    }
                    Log.d(TAG, String.format("Exercise: %s | Status: %s | Video: %s | Thumbnail: %s", 
                        exercise.getName(), status, exercise.getVideoUrl(), exercise.getThumbnailUrl()));
                }
                
                Log.d(TAG, "=== SUMMARY ===");
                Log.d(TAG, "Has video: " + hasVideo);
                Log.d(TAG, "Has thumbnail: " + hasThumbnail);
                Log.d(TAG, "Missing thumbnail: " + missingThumbnail);
                Log.d(TAG, "No video: " + noVideo);
                Log.d(TAG, "=== END DEBUG ===");
                
            } catch (Exception e) {
                Log.e(TAG, "Error debugging exercises", e);
            }
        });
    }
}