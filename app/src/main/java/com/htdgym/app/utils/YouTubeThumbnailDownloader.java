package com.htdgym.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Downloads YouTube thumbnails and saves them as PNG files
 */
public class YouTubeThumbnailDownloader {
    
    private static final String TAG = "YouTubeThumbnailDownloader";
    
    public interface DownloadCallback {
        void onSuccess(String filePath);
        void onError(String error);
    }
    
    /**
     * Download thumbnail from YouTube and save as PNG file
     */
    public static void downloadThumbnail(Context context, String videoUrl, String fileName, DownloadCallback callback) {
        new AsyncTask<Void, Void, String>() {
            private String errorMessage = null;
            
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Extract video ID
                    String videoId = YouTubeHelper.extractVideoId(videoUrl);
                    if (videoId == null) {
                        errorMessage = "Could not extract video ID from URL: " + videoUrl;
                        return null;
                    }
                    
                    // Get high quality thumbnail URL
                    String thumbnailUrl = "https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
                    Log.d(TAG, "Downloading thumbnail: " + thumbnailUrl);
                    
                    // Download image
                    URL url = new URL(thumbnailUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        errorMessage = "HTTP error: " + connection.getResponseCode();
                        return null;
                    }
                    
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    input.close();
                    
                    if (bitmap == null) {
                        errorMessage = "Failed to decode image";
                        return null;
                    }
                    
                    // Save as PNG file in app's internal storage
                    File thumbnailsDir = new File(context.getFilesDir(), "thumbnails");
                    if (!thumbnailsDir.exists()) {
                        thumbnailsDir.mkdirs();
                    }
                    
                    String safeFileName = fileName.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();
                    File outputFile = new File(thumbnailsDir, safeFileName + ".png");
                    
                    FileOutputStream out = new FileOutputStream(outputFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.close();
                    
                    Log.i(TAG, "Successfully saved thumbnail: " + outputFile.getAbsolutePath());
                    return outputFile.getAbsolutePath();
                    
                } catch (Exception e) {
                    Log.e(TAG, "Error downloading thumbnail", e);
                    errorMessage = e.getMessage();
                    return null;
                }
            }
            
            @Override
            protected void onPostExecute(String filePath) {
                if (filePath != null && callback != null) {
                    callback.onSuccess(filePath);
                } else if (callback != null) {
                    callback.onError(errorMessage != null ? errorMessage : "Unknown error");
                }
            }
        }.execute();
    }
    
    /**
     * Check if thumbnail file exists locally
     */
    public static String getLocalThumbnailPath(Context context, String fileName) {
        File thumbnailsDir = new File(context.getFilesDir(), "thumbnails");
        String safeFileName = fileName.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();
        File thumbnailFile = new File(thumbnailsDir, safeFileName + ".png");
        
        if (thumbnailFile.exists()) {
            return thumbnailFile.getAbsolutePath();
        }
        return null;
    }
}