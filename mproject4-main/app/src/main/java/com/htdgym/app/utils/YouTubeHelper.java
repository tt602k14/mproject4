package com.htdgym.app.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Helper class để lấy YouTube thumbnail từ video URL
 */
public class YouTubeHelper {
    
    /**
     * Lấy video ID từ YouTube URL
     * Ví dụ: https://youtu.be/dQw4w9WgXcQ -> dQw4w9WgXcQ
     * Hoặc: https://www.youtube.com/watch?v=dQw4w9WgXcQ -> dQw4w9WgXcQ
     */
    public static String getVideoIdFromUrl(String videoUrl) {
        if (videoUrl == null || videoUrl.isEmpty()) {
            return null;
        }
        
        try {
            // Format: https://youtu.be/VIDEO_ID
            if (videoUrl.contains("youtu.be/")) {
                String[] parts = videoUrl.split("youtu.be/");
                if (parts.length > 1) {
                    String videoId = parts[1];
                    // Remove any query parameters (?feature=share, etc)
                    if (videoId.contains("?")) {
                        videoId = videoId.split("\\?")[0];
                    }
                    if (videoId.contains("&")) {
                        videoId = videoId.split("&")[0];
                    }
                    return videoId;
                }
            }
            
            // Format: https://www.youtube.com/watch?v=VIDEO_ID
            if (videoUrl.contains("youtube.com/watch?v=") || videoUrl.contains("www.youtube.com/watch?v=")) {
                String[] parts = videoUrl.split("v=");
                if (parts.length > 1) {
                    String videoId = parts[1];
                    // Remove any additional parameters
                    if (videoId.contains("&")) {
                        videoId = videoId.split("&")[0];
                    }
                    if (videoId.contains("?")) {
                        videoId = videoId.split("\\?")[0];
                    }
                    return videoId;
                }
            }
            
            // Format: https://www.youtube.com/shorts/VIDEO_ID?feature=share
            if (videoUrl.contains("youtube.com/shorts/")) {
                String[] parts = videoUrl.split("shorts/");
                if (parts.length > 1) {
                    String videoId = parts[1];
                    // Remove any query parameters (?feature=share, etc)
                    if (videoId.contains("?")) {
                        videoId = videoId.split("\\?")[0];
                    }
                    if (videoId.contains("&")) {
                        videoId = videoId.split("&")[0];
                    }
                    return videoId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Lấy URL thumbnail từ video ID
     * YouTube cung cấp nhiều chất lượng:
     * - default.jpg (120x90)
     * - mqdefault.jpg (320x180)
     * - hqdefault.jpg (480x360)
     * - sddefault.jpg (640x480)
     * - maxresdefault.jpg (1280x720)
     */
    public static String getThumbnailUrl(String videoUrl, ThumbnailQuality quality) {
        String videoId = getVideoIdFromUrl(videoUrl);
        if (videoId == null) {
            return null;
        }
        
        String qualityStr;
        switch (quality) {
            case HIGH:
                qualityStr = "hqdefault.jpg";
                break;
            case MEDIUM:
                qualityStr = "mqdefault.jpg";
                break;
            case MAX:
                qualityStr = "maxresdefault.jpg";
                break;
            case DEFAULT:
            default:
                qualityStr = "default.jpg";
                break;
        }
        
        return "https://img.youtube.com/vi/" + videoId + "/" + qualityStr;
    }
    
    /**
     * Load thumbnail vào ImageView sử dụng Glide
     */
    public static void loadThumbnail(ImageView imageView, String videoUrl) {
        loadThumbnail(imageView, videoUrl, ThumbnailQuality.HIGH);
    }
    
    /**
     * Load thumbnail vào ImageView với chất lượng tùy chọn
     */
    public static void loadThumbnail(ImageView imageView, String videoUrl, ThumbnailQuality quality) {
        String thumbnailUrl = getThumbnailUrl(videoUrl, quality);
        
        if (thumbnailUrl != null && imageView != null) {
            Glide.with(imageView.getContext())
                    .load(thumbnailUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.darker_gray)
                    .centerCrop()
                    .into(imageView);
        }
    }
    
    /**
     * Load thumbnail với rounded corners
     */
    public static void loadThumbnailWithRoundedCorners(ImageView imageView, String videoUrl, ThumbnailQuality quality, int cornerRadius, int placeholderRes) {
        String thumbnailUrl = getThumbnailUrl(videoUrl, quality);
        
        if (thumbnailUrl != null && imageView != null) {
            Glide.with(imageView.getContext())
                    .load(thumbnailUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new com.bumptech.glide.load.resource.bitmap.RoundedCorners(cornerRadius))
                    .placeholder(placeholderRes)
                    .error(placeholderRes)
                    .into(imageView);
        } else if (imageView != null) {
            imageView.setImageResource(placeholderRes);
        }
    }
    
    /**
     * Test method để debug video ID extraction
     */
    public static void testVideoIdExtraction() {
        String[] testUrls = {
            "https://youtu.be/c4DAnQ6DtF8",
            "https://www.youtube.com/watch?v=hyv14e2QDq0",
            "https://youtu.be/Xyd_fa5zoEU",
            "https://www.youtube.com/watch?v=4hmQA3snN3Q"
        };
        
        for (String url : testUrls) {
            String videoId = getVideoIdFromUrl(url);
            String thumbnailUrl = getThumbnailUrl(url, ThumbnailQuality.HIGH);
            android.util.Log.d("YouTubeHelper", "URL: " + url);
            android.util.Log.d("YouTubeHelper", "Video ID: " + videoId);
            android.util.Log.d("YouTubeHelper", "Thumbnail: " + thumbnailUrl);
            android.util.Log.d("YouTubeHelper", "---");
        }
    }
    
    /**
     * Enum cho chất lượng thumbnail
     */
    public enum ThumbnailQuality {
        DEFAULT,  // 120x90
        MEDIUM,   // 320x180
        HIGH,     // 480x360
        MAX       // 1280x720
    }
}