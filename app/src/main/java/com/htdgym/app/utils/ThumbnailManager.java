package com.htdgym.app.utils;

import android.content.Context;
import android.widget.ImageView;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.htdgym.app.R;
import java.io.File;

/**
 * Manages thumbnail loading with fallback system
 */
public class ThumbnailManager {
    
    private static final String TAG = "ThumbnailManager";
    
    /**
     * Load thumbnail with multiple fallback options
     */
    public static void loadThumbnail(Context context, ImageView imageView, String exerciseName, String videoUrl) {
        if (exerciseName == null) exerciseName = "";
        if (videoUrl == null) videoUrl = "";
        
        Log.d(TAG, "Loading thumbnail for: " + exerciseName + " | URL: " + videoUrl);
        
        // Step 1: Try to load from local downloaded file
        String localPath = getLocalThumbnailPath(context, exerciseName);
        if (localPath != null) {
            Log.i(TAG, "✅ Loading local thumbnail: " + exerciseName);
            Glide.with(context)
                .load(new File(localPath))
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .into(imageView);
            return;
        }
        
        // Step 2: Try to load from YouTube URL
        String videoId = YouTubeHelper.extractVideoId(videoUrl);
        if (videoId != null) {
            String thumbnailUrl = "https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
            Log.d(TAG, "🌐 Loading YouTube thumbnail: " + exerciseName + " -> " + thumbnailUrl);
            
            // Make variables final for use in inner class
            final String finalExerciseName = exerciseName;
            final String finalVideoUrl = videoUrl;
            
            Glide.with(context)
                .load(thumbnailUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable com.bumptech.glide.load.engine.GlideException e, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "❌ Failed to load YouTube thumbnail for: " + finalExerciseName, e);
                        // Download for future use
                        downloadThumbnailForFuture(context, finalVideoUrl, finalExerciseName);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        Log.i(TAG, "✅ Successfully loaded YouTube thumbnail: " + finalExerciseName);
                        // Download for future use
                        downloadThumbnailForFuture(context, finalVideoUrl, finalExerciseName);
                        return false;
                    }
                })
                .into(imageView);
            return;
        }
        
        // Step 3: Fallback to drawable resource
        int drawableRes = ThumbnailHelper.getThumbnailResource(exerciseName, videoUrl);
        Log.d(TAG, "📁 Using drawable resource for: " + exerciseName);
        imageView.setImageResource(drawableRes);
    }
    
    /**
     * Get local thumbnail path if exists
     */
    private static String getLocalThumbnailPath(Context context, String exerciseName) {
        return YouTubeThumbnailDownloader.getLocalThumbnailPath(context, exerciseName);
    }
    
    /**
     * Download thumbnail for future use (async, no callback)
     */
    private static void downloadThumbnailForFuture(Context context, String videoUrl, String exerciseName) {
        YouTubeThumbnailDownloader.downloadThumbnail(context, videoUrl, exerciseName, 
            new YouTubeThumbnailDownloader.DownloadCallback() {
                @Override
                public void onSuccess(String filePath) {
                    Log.i(TAG, "💾 Downloaded thumbnail for future use: " + exerciseName + " -> " + filePath);
                }
                
                @Override
                public void onError(String error) {
                    Log.w(TAG, "💾 Failed to download thumbnail: " + exerciseName + " -> " + error);
                }
            });
    }
    
    /**
     * Pre-download important thumbnails
     */
    public static void preDownloadImportantThumbnails(Context context) {
        Log.i(TAG, "🚀 Pre-downloading important thumbnails...");
        
        // Download key exercise thumbnails
        downloadThumbnailForFuture(context, "https://youtu.be/8opcQdC-V-U", "Cardio Chạy Bộ Tại Chỗ");
        downloadThumbnailForFuture(context, "https://youtu.be/20Khkl95_qA", "Tabata - Đốt Calo Tối Đa");
        downloadThumbnailForFuture(context, "https://youtu.be/ml6cT4AZdqI", "HIIT Toàn Thân - Đốt Mỡ Nhanh");
        downloadThumbnailForFuture(context, "https://youtu.be/JZQA08SlJnM", "Jumping Jacks & Burpees");
        downloadThumbnailForFuture(context, "https://youtu.be/FJmRQ5iTXKE", "Jump Rope - Nhảy Dây Đốt Mỡ");
        downloadThumbnailForFuture(context, "https://youtu.be/nmwgirgXLYM", "Mountain Climber Circuit");
        downloadThumbnailForFuture(context, "https://youtu.be/A-cFYWvaHr0", "Squat Jump & Lunge Circuit");
        
        // Download muscle building thumbnails
        downloadThumbnailForFuture(context, "https://youtu.be/rT7DgCr-3pg", "Bench Press - Tập Ngực");
        downloadThumbnailForFuture(context, "https://youtu.be/op9kVnSso6Q", "Deadlift - Kéo Tạ Đất");
        downloadThumbnailForFuture(context, "https://youtu.be/ultWZbUMPL8", "Squat - Vua Của Các Bài Tập");
        downloadThumbnailForFuture(context, "https://youtu.be/eGo4IYlbE5g", "Pull-Up - Xà Đơn Tăng Cơ");
        downloadThumbnailForFuture(context, "https://youtu.be/2yjwXTZQDDI", "Overhead Press - Đẩy Vai");
        downloadThumbnailForFuture(context, "https://youtu.be/G8l_8chR5BE", "Barbell Row - Tập Lưng Dày");
        downloadThumbnailForFuture(context, "https://youtu.be/2z8JmcrW-As", "Dips - Tăng Cơ Tay Sau & Ngực");
    }
}