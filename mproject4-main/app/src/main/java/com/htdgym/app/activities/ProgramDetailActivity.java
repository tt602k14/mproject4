package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.htdgym.app.R;
import com.htdgym.app.utils.YouTubeHelper;
import com.htdgym.app.activities.BaseActivity;

public class ProgramDetailActivity extends BaseActivity {

    private ImageView btnBack;
    private TextView tvProgramTitle, tvProgramDescription, tvTotalExercises, tvDuration;
    private LinearLayout layoutExercises;
    
    private String programType; // "30", "60", "90"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);

        programType = getIntent().getStringExtra("program_type");
        if (programType == null) {
            programType = "30";
        }

        initViews();
        setupClickListeners();
        loadProgramData();
    }

    private void initViews() {
        tvProgramTitle = findViewById(R.id.tv_program_title);
        tvProgramDescription = findViewById(R.id.tv_program_description);
        tvTotalExercises = findViewById(R.id.tv_total_exercises);
        tvDuration = findViewById(R.id.tv_duration);
        layoutExercises = findViewById(R.id.layout_exercises);
    }

    private void setupClickListeners() {
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void loadProgramData() {
        switch (programType) {
            case "30":
                load30DayProgram();
                break;
            case "60":
                load60DayProgram();
                break;
            case "90":
                load90DayProgram();
                break;
        }
    }

    private void load30DayProgram() {
        tvProgramTitle.setText("Chương Trình 30 Ngày - Người Mới");
        tvProgramDescription.setText("30 ngày để có cơ bụng 6 múi và cơ thể khỏe mạnh");
        tvTotalExercises.setText("8");
        tvDuration.setText("25-30");

        // 8 giai đoạn cho chương trình 30 ngày
        addPhaseItem(0, "Ngày 1-5: Khởi động cơ bản", "Làm quen với các động tác cơ bản", "5 bài tập");
        addPhaseItem(1, "Ngày 6-10: Tăng cường sức bền", "Tăng thời gian và số lần tập", "6 bài tập");
        addPhaseItem(2, "Ngày 11-15: Gập bụng cơ bản", "Tập trung vào cơ bụng trên", "7 bài tập");
        addPhaseItem(3, "Ngày 16-20: Plank và biến thể", "Tăng cường cơ core", "6 bài tập");
        addPhaseItem(4, "Ngày 21-25: Cardio kết hợp", "Đốt mỡ hiệu quả", "7 bài tập");
        addPhaseItem(5, "Ngày 26-28: Tăng cường độ", "Nâng cao độ khó", "6 bài tập");
        addPhaseItem(6, "Ngày 29: Ôn tập tổng hợp", "Kết hợp tất cả động tác", "7 bài tập");
        addPhaseItem(7, "Ngày 30: Thử thách cuối", "Kiểm tra kết quả đạt được", "6 bài tập");
    }

    private void load60DayProgram() {
        tvProgramTitle.setText("Chương Trình 60 Ngày - Trung Cấp");
        tvProgramDescription.setText("60 ngày xây dựng cơ bắp toàn thân và tăng sức mạnh");
        tvTotalExercises.setText("10");
        tvDuration.setText("35-45");

        // 10 giai đoạn cho chương trình 60 ngày
        addPhaseItem(0, "Tuần 1-2: Nền tảng sức mạnh", "Xây dựng nền tảng cơ bắp", "7 bài tập");
        addPhaseItem(1, "Tuần 3-4: Tập ngực và tay", "Phát triển cơ thể phần trên", "8 bài tập");
        addPhaseItem(2, "Tuần 5-6: Tập chân và mông", "Tăng cường cơ thể phần dưới", "Đang cập nhật");
        addPhaseItem(3, "Tuần 7-8: Tập lưng và vai", "Cân bằng cơ thể toàn diện", "Đang cập nhật");
        addPhaseItem(4, "Tuần 9-10: Cardio nâng cao", "Tăng sức bền tim mạch", "Đang cập nhật");
        addPhaseItem(5, "Tuần 11-12: Kết hợp sức mạnh", "Tập luyện compound", "Đang cập nhật");
        addPhaseItem(6, "Tuần 13-14: HIIT chuyên sâu", "Đốt mỡ tối đa", "Đang cập nhật");
        addPhaseItem(7, "Tuần 15-16: Tăng cường độ", "Nâng cao thử thách", "Đang cập nhật");
        addPhaseItem(8, "Tuần 17: Ôn tập và đánh giá", "Kiểm tra tiến độ", "Đang cập nhật");
        addPhaseItem(9, "Tuần 18: Thử thách 60 ngày", "Hoàn thành mục tiêu", "Đang cập nhật");
    }

    private void load90DayProgram() {
        tvProgramTitle.setText("Chương Trình 90 Ngày - Nâng Cao");
        tvProgramDescription.setText("90 ngày transformation hoàn toàn cơ thể và tinh thần");
        tvTotalExercises.setText("12");
        tvDuration.setText("45-60");

        // 12 giai đoạn cho chương trình 90 ngày
        addPhaseItem(0, "Tháng 1: Giai đoạn thích nghi", "Làm quen với cường độ cao", "8 bài tập");
        addPhaseItem(1, "Tuần 5-8: Xây dựng khối lượng", "Tăng khối lượng cơ bắp", "Đang cập nhật");
        addPhaseItem(2, "Tuần 9-12: Tăng sức mạnh", "Nâng cao sức mạnh tối đa", "Đang cập nhật");
        addPhaseItem(3, "Tuần 13-16: Cardio chuyên nghiệp", "Tối ưu hóa tim mạch", "Đang cập nhật");
        addPhaseItem(4, "Tuần 17-20: Functional training", "Tập luyện chức năng", "Đang cập nhật");
        addPhaseItem(5, "Tuần 21-24: Plyometric", "Tăng sức bật và tốc độ", "Đang cập nhật");
        addPhaseItem(6, "Tuần 25-28: Endurance", "Tăng sức bền tối đa", "Đang cập nhật");
        addPhaseItem(7, "Tuần 29-32: Power training", "Tập luyện sức mạnh bùng nổ", "Đang cập nhật");
        addPhaseItem(8, "Tuần 33-36: Flexibility & Recovery", "Tăng tính linh hoạt", "Đang cập nhật");
        addPhaseItem(9, "Tuần 37-40: Competition prep", "Chuẩn bị thi đấu", "Đang cập nhật");
        addPhaseItem(10, "Tuần 41-44: Peak performance", "Đạt đỉnh cao thể lực", "Đang cập nhật");
        addPhaseItem(11, "Tuần 45-48: Transformation finale", "Hoàn thành transformation", "Đang cập nhật");
    }

    private void addPhaseItem(int phaseIndex, String title, String description, String exerciseCount) {
        CardView phaseCard = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (12 * getResources().getDisplayMetrics().density));
        phaseCard.setLayoutParams(cardParams);
        phaseCard.setRadius(12 * getResources().getDisplayMetrics().density);
        phaseCard.setCardElevation(2 * getResources().getDisplayMetrics().density);
        phaseCard.setCardBackgroundColor(0xFFFFFFFF);

        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density));
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Icon
        CardView iconCard = new CardView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                (int) (60 * getResources().getDisplayMetrics().density),
                (int) (60 * getResources().getDisplayMetrics().density));
        iconParams.setMargins(0, 0, (int) (16 * getResources().getDisplayMetrics().density), 0);
        iconCard.setLayoutParams(iconParams);
        iconCard.setRadius(30 * getResources().getDisplayMetrics().density);
        iconCard.setCardBackgroundColor(0xFFF0F0F0);

        TextView iconText = new TextView(this);
        iconText.setText("▶");
        iconText.setTextColor(0xFF6FCF97);
        iconText.setTextSize(24);
        iconText.setGravity(android.view.Gravity.CENTER);
        iconText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        
        iconCard.addView(iconText);

        // Phase info
        LinearLayout infoLayout = new LinearLayout(this);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        infoLayout.setLayoutParams(infoParams);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTextColor(0xFF1A1A1A);
        tvTitle.setTextSize(16);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvDescription = new TextView(this);
        tvDescription.setText(description);
        tvDescription.setTextColor(0xFF666666);
        tvDescription.setTextSize(13);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        descParams.setMargins(0, (int) (4 * getResources().getDisplayMetrics().density), 0, 0);
        tvDescription.setLayoutParams(descParams);

        TextView tvCount = new TextView(this);
        tvCount.setText(exerciseCount);
        tvCount.setTextColor(0xFF6FCF97);
        tvCount.setTextSize(12);
        LinearLayout.LayoutParams countParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        countParams.setMargins(0, (int) (2 * getResources().getDisplayMetrics().density), 0, 0);
        tvCount.setLayoutParams(countParams);

        infoLayout.addView(tvTitle);
        infoLayout.addView(tvDescription);
        infoLayout.addView(tvCount);

        // Arrow
        TextView btnArrow = new TextView(this);
        btnArrow.setText("›");
        btnArrow.setTextColor(0xFFC0C0C0);
        btnArrow.setTextSize(28);

        contentLayout.addView(iconCard);
        contentLayout.addView(infoLayout);
        contentLayout.addView(btnArrow);

        phaseCard.addView(contentLayout);

        // Click listener
        phaseCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProgramPhaseDetailActivity.class);
            intent.putExtra("program_type", programType);
            intent.putExtra("phase_index", phaseIndex);
            startActivity(intent);
        });

        layoutExercises.addView(phaseCard);
    }

    private void addExerciseItem(String title, String description, String videoUrl) {
        CardView exerciseCard = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (16 * getResources().getDisplayMetrics().density));
        exerciseCard.setLayoutParams(cardParams);
        exerciseCard.setRadius(16 * getResources().getDisplayMetrics().density);
        exerciseCard.setCardElevation(4 * getResources().getDisplayMetrics().density);
        exerciseCard.setCardBackgroundColor(0xFFFFFFFF);

        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(
                (int) (20 * getResources().getDisplayMetrics().density),
                (int) (20 * getResources().getDisplayMetrics().density),
                (int) (20 * getResources().getDisplayMetrics().density),
                (int) (20 * getResources().getDisplayMetrics().density));
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Thumbnail image with play icon overlay
        CardView imageCard = new CardView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                (int) (90 * getResources().getDisplayMetrics().density),
                (int) (70 * getResources().getDisplayMetrics().density));
        imageParams.setMargins(0, 0, (int) (20 * getResources().getDisplayMetrics().density), 0);
        imageCard.setLayoutParams(imageParams);
        imageCard.setRadius(12 * getResources().getDisplayMetrics().density);

        // Container for image and play button
        android.widget.FrameLayout imageContainer = new android.widget.FrameLayout(this);
        imageContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        ImageView thumbnailImage = new ImageView(this);
        thumbnailImage.setLayoutParams(new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT));
        thumbnailImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        // Load thumbnail if URL is not placeholder
        if (!videoUrl.startsWith("PLACEHOLDER")) {
            YouTubeHelper.loadThumbnail(thumbnailImage, videoUrl, YouTubeHelper.ThumbnailQuality.MEDIUM);
        } else {
            // Show placeholder with gradient background
            thumbnailImage.setBackgroundColor(0xFFE8F5E8);
        }

        // Play button overlay
        CardView playButtonCard = new CardView(this);
        android.widget.FrameLayout.LayoutParams playParams = new android.widget.FrameLayout.LayoutParams(
                (int) (36 * getResources().getDisplayMetrics().density),
                (int) (36 * getResources().getDisplayMetrics().density));
        playParams.gravity = android.view.Gravity.CENTER;
        playButtonCard.setLayoutParams(playParams);
        playButtonCard.setRadius(18 * getResources().getDisplayMetrics().density);
        playButtonCard.setCardBackgroundColor(0x88000000);
        playButtonCard.setCardElevation(0);

        TextView playIcon = new TextView(this);
        playIcon.setText("▶");
        playIcon.setTextColor(0xFFFFFFFF);
        playIcon.setTextSize(16);
        playIcon.setGravity(android.view.Gravity.CENTER);
        playIcon.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        playButtonCard.addView(playIcon);

        imageContainer.addView(thumbnailImage);
        imageContainer.addView(playButtonCard);
        imageCard.addView(imageContainer);

        // Exercise info
        LinearLayout infoLayout = new LinearLayout(this);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        infoLayout.setLayoutParams(infoParams);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTextColor(0xFF1A1A1A);
        tvTitle.setTextSize(17);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvDescription = new TextView(this);
        tvDescription.setText(description);
        tvDescription.setTextColor(0xFF666666);
        tvDescription.setTextSize(14);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        descParams.setMargins(0, (int) (6 * getResources().getDisplayMetrics().density), 0, 0);
        tvDescription.setLayoutParams(descParams);

        infoLayout.addView(tvTitle);
        infoLayout.addView(tvDescription);

        // Arrow button
        CardView arrowCard = new CardView(this);
        LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(
                (int) (40 * getResources().getDisplayMetrics().density),
                (int) (40 * getResources().getDisplayMetrics().density));
        arrowCard.setLayoutParams(arrowParams);
        arrowCard.setRadius(20 * getResources().getDisplayMetrics().density);
        arrowCard.setCardBackgroundColor(0xFF6FCF97);
        arrowCard.setCardElevation(2 * getResources().getDisplayMetrics().density);

        TextView btnArrow = new TextView(this);
        btnArrow.setText("▶");
        btnArrow.setTextColor(0xFFFFFFFF);
        btnArrow.setTextSize(16);
        btnArrow.setGravity(android.view.Gravity.CENTER);
        btnArrow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        arrowCard.addView(btnArrow);

        contentLayout.addView(imageCard);
        contentLayout.addView(infoLayout);
        contentLayout.addView(arrowCard);

        exerciseCard.addView(contentLayout);

        // Click listener
        exerciseCard.setOnClickListener(v -> {
            if (!videoUrl.startsWith("PLACEHOLDER")) {
                openYouTubeVideo(videoUrl);
            } else {
                Toast.makeText(this, "Video sẽ được cập nhật sớm!", Toast.LENGTH_SHORT).show();
            }
        });

        layoutExercises.addView(exerciseCard);
    }

    private void openYouTubeVideo(String videoUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse(videoUrl));
            intent.setPackage("com.google.android.youtube");
            startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(android.net.Uri.parse(videoUrl));
                startActivity(intent);
            } catch (Exception ex) {
                Toast.makeText(this, "Không thể mở video", Toast.LENGTH_SHORT).show();
            }
        }
    }
}