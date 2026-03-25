package com.htdgym.app.utils;

import android.widget.ImageView;
import com.htdgym.app.R;

/**
 * Helper class để load thumbnail cho các bài tập
 */
public class ThumbnailHelper {
    
    /**
     * Load thumbnail cho bài tập dựa trên tên và video URL
     */
    public static void loadExerciseThumbnail(ImageView imageView, String exerciseName, String videoUrl) {
        int thumbnailRes = getThumbnailResource(exerciseName, videoUrl);
        imageView.setImageResource(thumbnailRes);
    }
    
    /**
     * Get thumbnail resource dựa trên tên bài tập và video URL
     */
    public static int getThumbnailResource(String exerciseName, String videoUrl) {
        if (exerciseName == null) exerciseName = "";
        if (videoUrl == null) videoUrl = "";
        
        // Cardio Chạy Bộ Tại Chỗ
        if (exerciseName.contains("Cardio") || exerciseName.contains("Chạy Bộ") || 
            videoUrl.contains("8opcQdC-V-U")) {
            return R.drawable.thumbnail_cardio_running;
        }
        
        // Tabata - Đốt Calo Tối Đa
        if (exerciseName.contains("Tabata") || exerciseName.contains("Đốt Calo") || 
            videoUrl.contains("20Khkl95_qA")) {
            return R.drawable.thumbnail_tabata_hiit;
        }
        
        // HIIT Toàn Thân
        if (exerciseName.contains("HIIT") || exerciseName.contains("Toàn Thân") || 
            videoUrl.contains("ml6cT4AZdqI")) {
            return R.drawable.thumbnail_hiit_fullbody;
        }
        
        // Jumping Jacks & Burpees
        if (exerciseName.contains("Jumping") || exerciseName.contains("Burpees") || 
            videoUrl.contains("JZQA08SlJnM")) {
            return R.drawable.thumbnail_hiit_fullbody; // Use existing HIIT thumbnail
        }
        
        // Jump Rope - Nhảy Dây
        if (exerciseName.contains("Jump Rope") || exerciseName.contains("Nhảy Dây") || 
            videoUrl.contains("FJmRQ5iTXKE")) {
            return R.drawable.thumbnail_cardio_running; // Use cardio running thumbnail
        }
        
        // Mountain Climber Circuit
        if (exerciseName.contains("Mountain Climber") || exerciseName.contains("Circuit") || 
            videoUrl.contains("nmwgirgXLYM")) {
            return R.drawable.thumbnail_hiit_fullbody; // Use HIIT thumbnail
        }
        
        // Squat Jump & Lunge Circuit
        if (exerciseName.contains("Squat Jump") || exerciseName.contains("Lunge Circuit") || 
            videoUrl.contains("A-cFYWvaHr0")) {
            return R.drawable.thumbnail_squats; // Use basic squats thumbnail
        }
        
        // Bench Press - Tập Ngực
        if (exerciseName.contains("Bench Press") || exerciseName.contains("Tập Ngực") || 
            videoUrl.contains("rT7DgCr-3pg")) {
            return R.drawable.thumbnail_chest_default; // Use chest default
        }
        
        // Deadlift - Kéo Tạ Đất
        if (exerciseName.contains("Deadlift") || exerciseName.contains("Kéo Tạ") || 
            videoUrl.contains("op9kVnSso6Q")) {
            return R.drawable.thumbnail_back_default; // Use back default
        }
        
        // Squat - Vua Của Các Bài Tập
        if (exerciseName.contains("Squat") && (exerciseName.contains("Vua") || exerciseName.contains("Strength")) || 
            videoUrl.contains("ultWZbUMPL8")) {
            return R.drawable.thumbnail_squats; // Use basic squats
        }
        
        // Hít đất cơ bản
        if (exerciseName.contains("Push") || exerciseName.contains("Hít đất") || 
            videoUrl.contains("IODxDxX7oi4")) {
            return R.drawable.thumbnail_pushups;
        }
        
        // Gập bụng
        if (exerciseName.contains("Gập bụng") || exerciseName.contains("Crunches") || 
            videoUrl.contains("1fbU_MkV7NE")) {
            return R.drawable.thumbnail_crunches;
        }
        
        // Plank
        if (exerciseName.contains("Plank") || videoUrl.contains("pSHjTRCQxIw")) {
            return R.drawable.thumbnail_plank;
        }
        
        // Squat
        if (exerciseName.contains("Squat") || exerciseName.contains("Gánh đùi") || 
            videoUrl.contains("Xe1mCFljUN0")) {
            return R.drawable.thumbnail_squats;
        }
        
        // Default thumbnails by category
        if (exerciseName.contains("chest") || exerciseName.contains("ngực")) {
            return R.drawable.thumbnail_chest_default;
        }
        if (exerciseName.contains("legs") || exerciseName.contains("chân")) {
            return R.drawable.thumbnail_legs_default;
        }
        if (exerciseName.contains("abs") || exerciseName.contains("bụng")) {
            return R.drawable.thumbnail_abs_default;
        }
        if (exerciseName.contains("back") || exerciseName.contains("lưng")) {
            return R.drawable.thumbnail_back_default;
        }
        if (exerciseName.contains("shoulder") || exerciseName.contains("vai")) {
            return R.drawable.thumbnail_shoulder_default;
        }
        if (exerciseName.contains("hiit") || exerciseName.contains("cardio")) {
            return R.drawable.thumbnail_cardio_default;
        }
        
        // Default fallback
        return R.drawable.thumbnail_exercise_default;
    }
    
    /**
     * Get color for exercise category
     */
    public static int getCategoryColor(String category) {
        if (category == null) return 0xFF6FCF97;
        
        switch (category.toLowerCase()) {
            case "chest":
            case "ngực":
                return 0xFFFF6B6B;
            case "legs":
            case "chân":
                return 0xFF4ECDC4;
            case "back":
            case "lưng":
                return 0xFF4CAF50;
            case "shoulder":
            case "vai":
            case "tay":
                return 0xFFFFA726;
            case "abs":
            case "bụng":
                return 0xFFAB47BC;
            case "hiit":
            case "cardio":
            case "toàn thân":
                return 0xFFE91E63;
            default:
                return 0xFF6FCF97;
        }
    }
}