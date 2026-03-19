package com.htdgym.app.utils;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeHelper {

    public enum ThumbnailQuality {
        DEFAULT("default"),
        MEDIUM("mqdefault"),
        HIGH("hqdefault"),
        STANDARD("sddefault"),
        MAXRES("maxresdefault");

        private final String quality;

        ThumbnailQuality(String quality) {
            this.quality = quality;
        }

        public String getQuality() {
            return quality;
        }
    }

    public static String extractVideoId(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) return null;
        String regex = "(?:youtube(?:-nocookie)?\\.com/(?:[^/]+/.+/|(?:v|e(?:mbed)?)/|.*[?&]v=)|youtu\\.be/)([^\"&?/ ]{11})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(youtubeUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getThumbnailUrl(String youtubeUrl, ThumbnailQuality quality) {
        String videoId = extractVideoId(youtubeUrl);
        if (videoId != null) {
            // Dùng i.ytimg.com CDN — ổn định hơn img.youtube.com
            return "https://i.ytimg.com/vi/" + videoId + "/" + quality.getQuality() + ".jpg";
        }
        return null;
    }

    public static void loadThumbnail(ImageView imageView, String youtubeUrl, ThumbnailQuality quality) {
        String thumbnailUrl = getThumbnailUrl(youtubeUrl, quality);
        if (thumbnailUrl != null && imageView.getContext() != null) {
            try {
                Glide.with(imageView.getContext())
                    .load(thumbnailUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(com.htdgym.app.R.drawable.ic_image_placeholder)
                    .error(com.htdgym.app.R.drawable.ic_image_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .into(imageView);
            } catch (Exception e) {
                imageView.setImageResource(com.htdgym.app.R.drawable.ic_image_placeholder);
            }
        } else {
            imageView.setImageResource(com.htdgym.app.R.drawable.ic_image_placeholder);
        }
    }
}
