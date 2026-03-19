package com.htdgym.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.htdgym.app.R;
import com.htdgym.app.activities.WorkoutDetailActivity;
import com.htdgym.app.activities.WorkoutLibraryActivity;
import com.htdgym.app.utils.PremiumManager;

public class WorkoutsFragment extends Fragment {

    private CardView cardAll, cardChest, cardShoulder, cardLegs, cardAbs, cardBack;
    private TextView btnViewAll;
    private LinearLayout layoutWorkoutList, layoutFeaturedPrograms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);
        initViews(view);
        setupClickListeners();
        loadFeaturedPrograms();
        loadPopularWorkouts();
        return view;
    }

    private void initViews(View view) {
        cardAll = view.findViewById(R.id.card_all);
        cardChest = view.findViewById(R.id.card_chest);
        cardShoulder = view.findViewById(R.id.card_shoulder);
        cardLegs = view.findViewById(R.id.card_legs);
        cardAbs = view.findViewById(R.id.card_abs);
        cardBack = view.findViewById(R.id.card_back);
        btnViewAll = view.findViewById(R.id.btn_view_all);
        layoutWorkoutList = view.findViewById(R.id.layout_workout_list);
        layoutFeaturedPrograms = view.findViewById(R.id.layout_featured_programs);
    }

    private void setupClickListeners() {
        cardAll.setOnClickListener(v -> openWorkoutDetail("all"));
        cardChest.setOnClickListener(v -> openWorkoutDetail("chest"));
        cardShoulder.setOnClickListener(v -> openWorkoutDetail("shoulder"));
        cardLegs.setOnClickListener(v -> openWorkoutDetail("legs"));
        cardAbs.setOnClickListener(v -> openWorkoutDetail("abs"));
        cardBack.setOnClickListener(v -> openWorkoutDetail("back"));

        btnViewAll.setOnClickListener(v -> startActivity(new Intent(requireContext(), WorkoutLibraryActivity.class)));
    }

    private void openWorkoutDetail(String category) {
        String programType;
        String programTitle;
        switch (category) {
            case "chest":    programType = "bench_press";     programTitle = "💪 Bài Tập Ngực";    break;
            case "shoulder": programType = "overhead_press";  programTitle = "💥 Bài Tập Vai";     break;
            case "legs":     programType = "squat_strength";  programTitle = "🦵 Bài Tập Chân";    break;
            case "abs":      programType = "abs_beginner";    programTitle = "🔥 Bài Tập Bụng";    break;
            case "back":     programType = "barbell_row";     programTitle = "🏋️ Bài Tập Lưng";   break;
            default:         programType = "muscle_building"; programTitle = "🏋️ Tất Cả Bài Tập"; break;
        }
        Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramExerciseListActivity.class);
        intent.putExtra("program_type", programType);
        intent.putExtra("program_title", programTitle);
        startActivity(intent);
    }

    private void loadFeaturedPrograms() {
        addFeaturedProgramCard("30-60-90 Ngày", "Chương trình toàn diện", "Người mới bắt đầu", "#667eea");
        addFeaturedProgramCard("HIIT Đốt Mỡ", "Giảm cân nhanh chóng", "14 ngày", "#FF6B6B");
        addFeaturedProgramCard("Xây Dựng Cơ Bắp", "Tăng khối lượng cơ", "8 tuần", "#4ECDC4");
    }

    private void addFeaturedProgramCard(String title, String subtitle, String duration, String color) {
        float density = getResources().getDisplayMetrics().density;
        CardView programCard = new CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                (int) (280 * density), (int) (160 * density));
        cardParams.setMargins(0, 0, (int) (16 * density), 0);
        programCard.setLayoutParams(cardParams);
        programCard.setRadius(16 * density);
        programCard.setCardElevation(4 * density);
        programCard.setCardBackgroundColor(android.graphics.Color.parseColor(color));

        LinearLayout contentLayout = new LinearLayout(requireContext());
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        int dp20 = (int) (20 * density);
        contentLayout.setPadding(dp20, dp20, dp20, dp20);
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

        boolean isPremiumProgram = !title.contains("30-60-90");
        if (isPremiumProgram) {
            TextView premiumBadge = new TextView(requireContext());
            premiumBadge.setText("👑 PREMIUM");
            premiumBadge.setTextColor(0xFFFFD700);
            premiumBadge.setTextSize(10);
            premiumBadge.setTypeface(null, android.graphics.Typeface.BOLD);
            LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            badgeParams.setMargins(0, 0, 0, (int) (8 * density));
            premiumBadge.setLayoutParams(badgeParams);
            contentLayout.addView(premiumBadge);
        }

        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(title);
        tvTitle.setTextColor(0xFFFFFFFF);
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvSubtitle = new TextView(requireContext());
        tvSubtitle.setText(subtitle);
        tvSubtitle.setTextColor(0xE0FFFFFF);
        tvSubtitle.setTextSize(14);
        LinearLayout.LayoutParams subtitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subtitleParams.setMargins(0, (int) (8 * density), 0, (int) (16 * density));
        tvSubtitle.setLayoutParams(subtitleParams);

        TextView tvDuration = new TextView(requireContext());
        tvDuration.setText("⏱️ " + duration);
        tvDuration.setTextColor(0xE0FFFFFF);
        tvDuration.setTextSize(12);

        contentLayout.addView(tvTitle);
        contentLayout.addView(tvSubtitle);
        contentLayout.addView(tvDuration);
        programCard.addView(contentLayout);

        programCard.setOnClickListener(v -> {
            com.htdgym.app.utils.AnimationHelper.scaleIn(programCard);
            if (isPremiumProgram && !PremiumManager.isPremiumUser(requireContext())) {
                PremiumManager.showPremiumDialog(requireContext(), title);
                return;
            }
            if (title.contains("30-60-90")) {
                Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramDetailActivity.class);
                intent.putExtra("program_type", "30");
                startActivity(intent);
            } else {
                startActivity(new Intent(requireContext(), WorkoutLibraryActivity.class));
            }
        });

        layoutFeaturedPrograms.addView(programCard);
    }

    private void loadPopularWorkouts() {
        layoutWorkoutList.removeAllViews();

        // Tăng cơ bắp
        addWorkoutItem("🏋️ Bench Press - Tập Ngực",        "https://youtu.be/rT7DgCr-3pg", "bench_press");
        addWorkoutItem("💀 Deadlift - Kéo Tạ Đất",          "https://youtu.be/op9kVnSso6Q", "deadlift");
        addWorkoutItem("🦵 Squat - Vua Của Các Bài Tập",    "https://youtu.be/ultWZbUMPL8", "squat_strength");
        addWorkoutItem("🔝 Pull-Up - Xà Đơn Tăng Cơ",      "https://youtu.be/eGo4IYlbE5g", "pull_up");
        addWorkoutItem("💥 Overhead Press - Đẩy Vai",       "https://youtu.be/2yjwXTZQDDI", "overhead_press");
        addWorkoutItem("🔥 Barbell Row - Tập Lưng Dày",     "https://youtu.be/G8l_8chR5BE", "barbell_row");
        addWorkoutItem("⚡ Dips - Tăng Cơ Tay Sau & Ngực", "https://youtu.be/2z8JmcrW-As", "dips");

        // Đốt mỡ
        addWorkoutItem("🔥 HIIT Toàn Thân - Đốt Mỡ Nhanh", "https://youtu.be/ml6cT4AZdqI", "hiit_fullbody");
        addWorkoutItem("🏃 Cardio Chạy Bộ Tại Chỗ",         "https://youtu.be/8opcQdC-V-U", "cardio_running");
        addWorkoutItem("⚡ Tabata - Đốt Calo Tối Đa",        "https://youtu.be/20Khkl95_qA", "tabata_burn");
        addWorkoutItem("💨 Jumping Jacks & Burpees",         "https://youtu.be/JZQA08SlJnM", "jumping_burpees");
        addWorkoutItem("🎯 Jump Rope - Nhảy Dây Đốt Mỡ",    "https://youtu.be/FJmRQ5iTXKE", "jump_rope");
        addWorkoutItem("🌀 Mountain Climber Circuit",        "https://youtu.be/nmwgirgXLYM", "mountain_climber");
        addWorkoutItem("🚀 Squat Jump & Lunge Circuit",      "https://youtu.be/A-cFYWvaHr0", "squat_lunge_circuit");
    }

    private void addWorkoutItem(String title, String videoUrl, String programType) {
        float density = getResources().getDisplayMetrics().density;

        CardView workoutCard = new CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (12 * density));
        workoutCard.setLayoutParams(cardParams);
        workoutCard.setRadius(12 * density);
        workoutCard.setCardElevation(2 * density);
        workoutCard.setCardBackgroundColor(0xFFFFFFFF);

        LinearLayout contentLayout = new LinearLayout(requireContext());
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        int dp16 = (int) (16 * density);
        contentLayout.setPadding(dp16, dp16, dp16, dp16);
        contentLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Thumbnail
        CardView iconCard = new CardView(requireContext());
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                (int) (80 * density), (int) (60 * density));
        iconParams.setMargins(0, 0, dp16, 0);
        iconCard.setLayoutParams(iconParams);
        iconCard.setRadius(8 * density);
        iconCard.setCardBackgroundColor(0xFFF0F0F0);

        ImageView thumbnailImage = new ImageView(requireContext());
        thumbnailImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        thumbnailImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        com.htdgym.app.utils.YouTubeThumbnailHelper.loadThumbnail(this, thumbnailImage, videoUrl);
        iconCard.addView(thumbnailImage);

        // Info
        LinearLayout infoLayout = new LinearLayout(requireContext());
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        infoLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(title);
        tvTitle.setTextColor(0xFF1A1A1A);
        tvTitle.setTextSize(15);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvTag = new TextView(requireContext());
        tvTag.setText(getCategoryTag(programType));
        tvTag.setTextColor(0xFF6FCF97);
        tvTag.setTextSize(12);
        LinearLayout.LayoutParams tagParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tagParams.setMargins(0, (int) (4 * density), 0, 0);
        tvTag.setLayoutParams(tagParams);

        infoLayout.addView(tvTitle);
        infoLayout.addView(tvTag);

        // Arrow
        TextView tvArrow = new TextView(requireContext());
        tvArrow.setText("›");
        tvArrow.setTextColor(0xFFC0C0C0);
        tvArrow.setTextSize(28);

        contentLayout.addView(iconCard);
        contentLayout.addView(infoLayout);
        contentLayout.addView(tvArrow);
        workoutCard.addView(contentLayout);

        workoutCard.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), com.htdgym.app.activities.ProgramExerciseListActivity.class);
            intent.putExtra("program_type", programType);
            intent.putExtra("program_title", title);
            startActivity(intent);
        });

        layoutWorkoutList.addView(workoutCard);
    }

    private String getCategoryTag(String programType) {
        switch (programType) {
            case "bench_press": case "dips":                     return "💪 Tăng cơ ngực";
            case "deadlift": case "barbell_row": case "pull_up": return "🏋️ Tăng cơ lưng";
            case "squat_strength":                               return "🦵 Tăng cơ chân";
            case "overhead_press":                               return "💥 Tăng cơ vai";
            case "hiit_fullbody": case "jumping_burpees":        return "🔥 HIIT đốt mỡ";
            case "cardio_running":                               return "🏃 Cardio";
            case "tabata_burn":                                  return "⚡ Tabata";
            case "jump_rope":                                    return "🎯 Nhảy dây";
            case "mountain_climber":                             return "🌀 Core & Plank";
            case "squat_lunge_circuit":                          return "🚀 Circuit đốt mỡ";
            default:                                             return "🏋️ Tập luyện";
        }
    }
}
