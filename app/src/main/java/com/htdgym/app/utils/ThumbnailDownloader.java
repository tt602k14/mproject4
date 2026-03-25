package com.htdgym.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Utility class để download và lưu thumbnail từ YouTube
 */
public class ThumbnailDownloader {
    
    private static final String TAG = "ThumbnailDownloader";
    private static ExecutorService executor = Executors.newFixedThreadPool(3);
    
    public interface DownloadCallback {
        void onSuccess(String localPath);
        void onError(String error);
    }
    
    /**
     * Download thumbnail từ YouTube và lưu vào internal storage
     */
    public static void downloadThumbnail(Context context, String videoUrl, String exerciseName, DownloadCallback callback) {
        executor.execute(() -> {
            try {
                // Extract video ID
                String videoId = YouTubeHelper.extractVideoId(videoUrl);
                if (videoId == null) {
                    callback.onError("Cannot extract video ID from: " + videoUrl);
                    return;
                }
                
                // Generate thumbnail URL
                String thumbnailUrl = "https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
                Log.d(TAG, "Downloading thumbnail: " + thumbnailUrl);
                
                // Create thumbnails directory
                File thumbnailsDir = new File(context.getFilesDir(), "thumbnails");
                if (!thumbnailsDir.exists()) {
                    thumbnailsDir.mkdirs();
                }
                
                // Create local file name
                String fileName = videoId + "_" + exerciseName.replaceAll("[^a-zA-Z0-9]", "_") + ".jpg";
                File localFile = new File(thumbnailsDir, fileName);
                
                // Check if file already exists
                if (localFile.exists()) {
                    Log.d(TAG, "Thumbnail already exists: " + localFile.getAbsolutePath());
                    callback.onSuccess(localFile.getAbsolutePath());
                    return;
                }
                
                // Download image
                URL url = new URL(thumbnailUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    input.close();
                    
                    if (bitmap != null) {
                        // Save bitmap to file
                        FileOutputStream out = new FileOutputStream(localFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        
                        Log.i(TAG, "✅ Successfully downloaded thumbnail: " + localFile.getAbsolutePath());
                        callback.onSuccess(localFile.getAbsolutePath());
                    } else {
                        callback.onError("Failed to decode bitmap from: " + thumbnailUrl);
                    }
                } else {
                    callback.onError("HTTP error: " + connection.getResponseCode() + " for URL: " + thumbnailUrl);
                }
                
                connection.disconnect();
                
            } catch (Exception e) {
                Log.e(TAG, "Error downloading thumbnail for: " + exerciseName, e);
                callback.onError("Download failed: " + e.getMessage());
            }
        });
    }
    
    /**
     * Download thumbnails cho các bài tập cụ thể
     */
    public static void downloadSpecificThumbnails(Context context) {
        Log.i(TAG, "=== DOWNLOADING SPECIFIC THUMBNAILS ===");
        
        // Cardio Chạy Bộ Tại Chỗ
        downloadThumbnail(context, "https://youtu.be/8opcQdC-V-U", "Cardio_Chay_Bo_Tai_Cho", new DownloadCallback() {
            @Override
            public void onSuccess(String localPath) {
                Log.i(TAG, "🎯 Cardio thumbnail saved: " + localPath);
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "❌ Cardio thumbnail failed: " + error);
            }
        });
        
        // Tabata - Đốt Calo Tối Đa
        downloadThumbnail(context, "https://youtu.be/20Khkl95_qA", "Tabata_Dot_Calo_Toi_Da", new DownloadCallback() {
            @Override
            public void onSuccess(String localPath) {
                Log.i(TAG, "🎯 Tabata thumbnail saved: " + localPath);
            }
            
            @Override
            public void onError(String error) {
                Log.e(TAG, "❌ Tabata thumbnail failed: " + error);
            }
        });
        
        // Download thêm một số thumbnails khác
        String[] exercises = {
            "https://youtu.be/IODxDxX7oi4|Hit_Dat_Co_Ban",
            "https://youtu.be/c4DAnQ6DtF8|Jumping_Jacks", 
            "https://youtu.be/1fbU_MkV7NE|Gap_Bung_Co_Ban",
            "https://youtu.be/pSHjTRCQxIw|Plank_Co_Ban",
            "https://youtu.be/JZQA08SlJnM|Burpees"
        };
        
        for (String exercise : exercises) {
            String[] parts = exercise.split("\\|");
            if (parts.length == 2) {
                downloadThumbnail(context, parts[0], parts[1], new DownloadCallback() {
                    @Override
                    public void onSuccess(String localPath) {
                        Log.d(TAG, "✅ Downloaded: " + parts[1] + " -> " + localPath);
                    }
                    
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "❌ Failed: " + parts[1] + " -> " + error);
                    }
                });
            }
        }
    }
    
    /**
     * Get local thumbnail path if exists
     */
    public static String getLocalThumbnailPath(Context context, String videoUrl, String exerciseName) {
        try {
            String videoId = YouTubeHelper.extractVideoId(videoUrl);
            if (videoId == null) return null;
            
            File thumbnailsDir = new File(context.getFilesDir(), "thumbnails");
            String fileName = videoId + "_" + exerciseName.replaceAll("[^a-zA-Z0-9]", "_") + ".jpg";
            File localFile = new File(thumbnailsDir, fileName);
            
            return localFile.exists() ? localFile.getAbsolutePath() : null;
        } catch (Exception e) {
            Log.e(TAG, "Error getting local thumbnail path", e);
            return null;
        }
    }
    
    /**
     * Clear all downloaded thumbnails
     */
    public static void clearThumbnails(Context context) {
        try {
            File thumbnailsDir = new File(context.getFilesDir(), "thumbnails");
            if (thumbnailsDir.exists()) {
                File[] files = thumbnailsDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.delete()) {
                            Log.d(TAG, "Deleted: " + file.getName());
                        }
                    }
                }
            }
            Log.i(TAG, "Cleared all thumbnails");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing thumbnails", e);
        }
    }
}