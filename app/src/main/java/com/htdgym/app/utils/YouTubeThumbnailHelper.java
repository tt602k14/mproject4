package com.htdgym.app.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class YouTubeThumbnailHelper {

    public static String extractVideoId(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) return null;
        String videoId = null;
        if (youtubeUrl.contains("youtu.be/")) {
            videoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("/") + 1);
            if (videoId.contains("?")) videoId = videoId.substring(0, videoId.indexOf("?"));
        } else if (youtubeUrl.contains("youtube.com/watch?v=")) {
            String[] parts = youtubeUrl.split("v=");
            if (parts.length > 1) {
                videoId = parts[1];
                if (videoId.contains("&")) videoId = videoId.substring(0, videoId.indexOf("&"));
            }
        } else if (youtubeUrl.contains("youtube.com/embed/")) {
            videoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("/") + 1);
            if (videoId.contains("?")) videoId = videoId.substring(0, videoId.indexOf("?"));
        }
        return videoId;
    }

    public static String getThumbnailUrl(String youtubeUrl) {
        String videoId = extractVideoId(youtubeUrl);
        return videoId != null ? "https://i.ytimg.com/vi/" + videoId + "/mqdefault.jpg" : null;
    }

    public static String getThumbnailUrl(String youtubeUrl, String quality) {
        String videoId = extractVideoId(youtubeUrl);
        return videoId != null ? "https://i.ytimg.com/vi/" + videoId + "/" + quality + ".jpg" : null;
    }

    private static void glideLoad(RequestManager rm, ImageView imageView, String videoId) {
        String url = "https://i.ytimg.com/vi/" + videoId + "/mqdefault.jpg";
        rm.load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(com.htdgym.app.R.drawable.ic_image_placeholder)
            .error(com.htdgym.app.R.drawable.ic_image_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(imageView);
    }

    /** Dùng trong Fragment — truyền fragment để Glide bind đúng lifecycle */
    public static void loadThumbnail(Fragment fragment, ImageView imageView, String youtubeUrl) {
        String videoId = extractVideoId(youtubeUrl);
        if (videoId == null) {
            imageView.setImageResource(com.htdgym.app.R.drawable.ic_image_placeholder);
            return;
        }
        try {
            glideLoad(Glide.with(fragment), imageView, videoId);
        } catch (Exception e) {
            imageView.setImageResource(com.htdgym.app.R.drawable.ic_image_placeholder);
        }
    }

    /** Dùng trong Activity hoặc khi có Context */
    public static void loadThumbnail(Context context, ImageView imageView, String youtubeUrl) {
        String videoId = extractVideoId(youtubeUrl);
        if (videoId == null || context == null) {
            imageView.setImageResource(com.htdgym.app.R.drawable.ic_image_placeholder);
            return;
        }
        try {
            glideLoad(Glide.with(context), imageView, videoId);
        } catch (Exception e) {
            imageView.setImageResource(com.htdgym.app.R.drawable.ic_image_placeholder);
        }
    }

    /** Overload tương thích ngược — dùng imageView.getContext() */
    public static void loadThumbnail(ImageView imageView, String youtubeUrl) {
        loadThumbnail(imageView.getContext(), imageView, youtubeUrl);
    }

    public static void loadThumbnail(ImageView imageView, String youtubeUrl, String quality) {
        // quality bị ignore, luôn dùng mqdefault vì ổn định nhất
        loadThumbnail(imageView.getContext(), imageView, youtubeUrl);
    }
}
