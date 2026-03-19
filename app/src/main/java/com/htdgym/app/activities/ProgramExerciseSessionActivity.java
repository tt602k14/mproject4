package com.htdgym.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.utils.ProgramScheduleData;
import com.htdgym.app.utils.YouTubeThumbnailHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProgramExerciseSessionActivity extends AppCompatActivity {

    private ImageView btnClose, imgVideoPreview;
    private CardView btnPlay, btnPrevious, btnNext, btnPlayVideo;
    private TextView tvExerciseName, tvExerciseSets, tvTimer, tvProgress, tvDayTitle, tvProgressCount;
    private ProgressBar progressBar;
    private Button btnRest, btnExit;
    private android.widget.LinearLayout layoutExerciseList;

    private List<Exercise> exercises;
    private int currentExerciseIndex = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000;
    private boolean isTimerRunning = false;
    private boolean isRestMode = false;

    // Map video URL cho từng bài tập
    private static final Map<String, String> VIDEO_MAP = new HashMap<String, String>() {{
        put("Push-up cơ bản",       "https://youtu.be/IODxDxX7oi4");
        put("Push-up rộng tay",     "https://youtu.be/rT7DgCr-3pg");
        put("Push-up hẹp tay",      "https://youtu.be/rT7DgCr-3pg");
        put("Decline Push-up",      "https://youtu.be/rT7DgCr-3pg");
        put("Incline Push-up",      "https://youtu.be/rT7DgCr-3pg");
        put("Diamond Push-up",      "https://youtu.be/rT7DgCr-3pg");
        put("Plank to Push-up",     "https://youtu.be/IODxDxX7oi4");
        put("Squat không tạ",       "https://youtu.be/ultWZbUMPL8");
        put("Jump Squats",          "https://youtu.be/ultWZbUMPL8");
        put("Sumo Squat",           "https://youtu.be/ultWZbUMPL8");
        put("Back Squat",           "https://youtu.be/ultWZbUMPL8");
        put("Front Squat",          "https://youtu.be/ultWZbUMPL8");
        put("Pistol Squat (hỗ trợ)","https://youtu.be/ultWZbUMPL8");
        put("Plank",                "https://youtu.be/pSHjTRCQxIw");
        put("Side Plank",           "https://youtu.be/pSHjTRCQxIw");
        put("Hollow Body Hold",     "https://youtu.be/pSHjTRCQxIw");
        put("Lunges",               "https://youtu.be/A-cFYWvaHr0");
        put("Reverse Lunges",       "https://youtu.be/A-cFYWvaHr0");
        put("Jump Lunge",           "https://youtu.be/A-cFYWvaHr0");
        put("Walking Lunges",       "https://youtu.be/A-cFYWvaHr0");
        put("Glute Bridge",         "https://youtu.be/8bbE64NuDTU");
        put("Single Leg Glute Bridge","https://youtu.be/8bbE64NuDTU");
        put("Mountain Climbers",    "https://youtu.be/nmwgirgXLYM");
        put("Mountain Climber nhanh","https://youtu.be/nmwgirgXLYM");
        put("Burpees",              "https://youtu.be/JZQA08SlJnM");
        put("Burpee + Push-up",     "https://youtu.be/JZQA08SlJnM");
        put("High Knees",           "https://youtu.be/8opcQdC-V-U");
        put("Jumping Jacks",        "https://youtu.be/JZQA08SlJnM");
        put("Crunches",             "https://youtu.be/Xyd_fa5zoEU");
        put("Bicycle Crunches",     "https://youtu.be/Xyd_fa5zoEU");
        put("V-Up",                 "https://youtu.be/Xyd_fa5zoEU");
        put("Russian Twist",        "https://youtu.be/Xyd_fa5zoEU");
        put("Superman Hold",        "https://youtu.be/z6PJMT2y8GQ");
        put("Superman Pulse",       "https://youtu.be/z6PJMT2y8GQ");
        put("Bird Dog",             "https://youtu.be/z6PJMT2y8GQ");
        put("Dead Bug",             "https://youtu.be/z6PJMT2y8GQ");
        put("Pike Push-up",         "https://youtu.be/2yjwXTZQDDI");
        put("Tricep Dips (ghế)",    "https://youtu.be/2z8JmcrW-As");
        put("Tricep Dips",          "https://youtu.be/2z8JmcrW-As");
        put("Bench Press",          "https://youtu.be/rT7DgCr-3pg");
        put("Incline Dumbbell Press","https://youtu.be/rT7DgCr-3pg");
        put("Deadlift",             "https://youtu.be/op9kVnSso6Q");
        put("Romanian Deadlift",    "https://youtu.be/op9kVnSso6Q");
        put("Romanian DL",          "https://youtu.be/op9kVnSso6Q");
        put("Barbell Row",          "https://youtu.be/G8l_8chR5BE");
        put("Pull-up",              "https://youtu.be/eGo4IYlbE5g");
        put("Chin-up",              "https://youtu.be/eGo4IYlbE5g");
        put("Weighted Pull-up",     "https://youtu.be/eGo4IYlbE5g");
        put("Overhead Press",       "https://youtu.be/2yjwXTZQDDI");
        put("OHP",                  "https://youtu.be/2yjwXTZQDDI");
        put("Lateral Raise",        "https://youtu.be/2yjwXTZQDDI");
        put("Calf Raises",          "https://youtu.be/ultWZbUMPL8");
        put("Step-up (ghế)",        "https://youtu.be/A-cFYWvaHr0");
        put("Tuck Jumps",           "https://youtu.be/JZQA08SlJnM");
        put("Speed Skaters",        "https://youtu.be/ml6cT4AZdqI");
        put("Tabata Jumping Jacks", "https://youtu.be/JZQA08SlJnM");
        put("Tabata High Knees",    "https://youtu.be/8opcQdC-V-U");
        put("Tabata Burpees",       "https://youtu.be/JZQA08SlJnM");
        put("Power Clean",          "https://youtu.be/op9kVnSso6Q");
        put("Box Jump",             "https://youtu.be/JZQA08SlJnM");
        put("Bulgarian Split Squat","https://youtu.be/A-cFYWvaHr0");
        put("Wall Sit",             "https://youtu.be/ultWZbUMPL8");
        put("Leg Press",            "https://youtu.be/ultWZbUMPL8");
        put("Leg Curl",             "https://youtu.be/ultWZbUMPL8");
        put("Arnold Press",         "https://youtu.be/2yjwXTZQDDI");
        put("Hammer Curl",          "https://youtu.be/eGo4IYlbE5g");
        put("Barbell Curl",         "https://youtu.be/eGo4IYlbE5g");
        put("Dragon Flag",          "https://youtu.be/pSHjTRCQxIw");
        put("L-Sit Hold",           "https://youtu.be/pSHjTRCQxIw");
        put("Plank Shoulder Tap",   "https://youtu.be/pSHjTRCQxIw");
        put("Full Body Circuit x3", "https://youtu.be/ml6cT4AZdqI");
        put("Full Body Circuit",    "https://youtu.be/ml6cT4AZdqI");
        put("HIIT 20 phút",         "https://youtu.be/ml6cT4AZdqI");
        put("Steady State Cardio",  "https://youtu.be/8opcQdC-V-U");
        put("Chạy bộ tại chỗ",      "https://youtu.be/8opcQdC-V-U");
        put("Jump Rope (mô phỏng)", "https://youtu.be/FJmRQ5iTXKE");
        put("Butt Kicks",           "https://youtu.be/8opcQdC-V-U");
        put("Lateral Shuffles",     "https://youtu.be/8opcQdC-V-U");
        put("Arm Circles",          "https://youtu.be/2yjwXTZQDDI");
        put("Reverse Snow Angels",  "https://youtu.be/z6PJMT2y8GQ");
        put("Hyperextension",       "https://youtu.be/z6PJMT2y8GQ");
        put("Hanging Knee Raise",   "https://youtu.be/pSHjTRCQxIw");
        put("Ab Wheel Rollout",     "https://youtu.be/pSHjTRCQxIw");
        put("Dips có tạ",           "https://youtu.be/2z8JmcrW-As");
        put("Weighted Dips",        "https://youtu.be/2z8JmcrW-As");
        put("Skull Crusher",        "https://youtu.be/2z8JmcrW-As");
        put("Seated Row",           "https://youtu.be/G8l_8chR5BE");
        put("Lat Pulldown",         "https://youtu.be/eGo4IYlbE5g");
        put("T-Bar Row",            "https://youtu.be/G8l_8chR5BE");
        put("Face Pull",            "https://youtu.be/2yjwXTZQDDI");
        put("Upright Row",          "https://youtu.be/2yjwXTZQDDI");
        put("Front Raise",          "https://youtu.be/2yjwXTZQDDI");
        put("Incline Curl",         "https://youtu.be/eGo4IYlbE5g");
        put("Preacher Curl",        "https://youtu.be/eGo4IYlbE5g");
        put("Concentration Curl",   "https://youtu.be/eGo4IYlbE5g");
        put("Hack Squat",           "https://youtu.be/ultWZbUMPL8");
        put("Leg Extension",        "https://youtu.be/ultWZbUMPL8");
        put("Rack Pull",            "https://youtu.be/op9kVnSso6Q");
        put("Good Morning",         "https://youtu.be/op9kVnSso6Q");
        put("Push Press",           "https://youtu.be/2yjwXTZQDDI");
        put("Farmer Walk",          "https://youtu.be/op9kVnSso6Q");
        put("Hang Snatch",          "https://youtu.be/op9kVnSso6Q");
        put("Cable Fly",            "https://youtu.be/rT7DgCr-3pg");
        put("Cable Lateral Raise",  "https://youtu.be/2yjwXTZQDDI");
        put("Pec Deck",             "https://youtu.be/rT7DgCr-3pg");
        put("Chest Squeeze Push-up","https://youtu.be/rT7DgCr-3pg");
        put("Archer Push-ups",      "https://youtu.be/rT7DgCr-3pg");
        put("Sprint tại chỗ",       "https://youtu.be/8opcQdC-V-U");
        put("Box Jumps (mô phỏng)", "https://youtu.be/JZQA08SlJnM");
        put("Jump Rope nhanh",      "https://youtu.be/FJmRQ5iTXKE");
        put("Tabata x8 rounds",     "https://youtu.be/20Khkl95_qA");
        put("Jump Squat",           "https://youtu.be/ultWZbUMPL8");
        put("Olympic Lifts",        "https://youtu.be/op9kVnSso6Q");
        put("Mobility Flow",        "https://youtu.be/8opcQdC-V-U");
        put("Đi bộ nhanh",          "https://youtu.be/8opcQdC-V-U");
        put("Stretch toàn thân",    "https://youtu.be/8opcQdC-V-U");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_program_exercise_session);

            initViews();

            int dayNumber   = getIntent().getIntExtra("day_number", -1);
            String dayTitle = getIntent().getStringExtra("day_title");
            String tab      = getIntent().getStringExtra("program_tab");
            if (tab == null) tab = "30";

            if (tvDayTitle != null && dayTitle != null) {
                if (dayNumber > 0) {
                    tvDayTitle.setText("Ngày " + dayNumber + " — " + dayTitle);
                } else {
                    tvDayTitle.setText(dayTitle);
                }
            }

            String programType = getIntent().getStringExtra("program_type");
            int phaseIndex     = getIntent().getIntExtra("phase_index", 0);
            currentExerciseIndex = getIntent().getIntExtra("exercise_index", 0);

            if (dayNumber > 0) {
                exercises = getExercisesForDay(tab, dayNumber);
            } else if (getIntent().hasExtra("exercise_names")) {
                exercises = buildFromArrays(
                    getIntent().getStringArrayExtra("exercise_names"),
                    getIntent().getStringArrayExtra("exercise_sets"),
                    getIntent().getStringArrayExtra("exercise_videos"),
                    getIntent().getStringArrayExtra("exercise_rests")
                );
            } else if (getIntent().hasExtra("single_exercise_name")) {
                exercises = getSingleExercise(
                    getIntent().getStringExtra("single_exercise_name"),
                    getIntent().getStringExtra("single_exercise_sets"),
                    getIntent().getStringExtra("single_exercise_video"),
                    getIntent().getStringExtra("single_exercise_rest")
                );
            } else {
                exercises = getLegacyExercises(programType, phaseIndex);
            }

            if (exercises == null || exercises.isEmpty()) {
                exercises = getDefaultExercises();
            }

            // Đảm bảo index hợp lệ
            if (currentExerciseIndex >= exercises.size()) currentExerciseIndex = 0;

            setupClickListeners();
            renderExerciseList();
            displayCurrentExercise();

        } catch (Exception e) {
            e.printStackTrace();
            android.widget.Toast.makeText(this, "Lỗi: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        btnClose        = findViewById(R.id.btn_close);
        imgVideoPreview = findViewById(R.id.img_video_preview);
        btnPlay         = findViewById(R.id.btn_play);
        btnPrevious     = findViewById(R.id.btn_previous);
        btnNext         = findViewById(R.id.btn_next);
        tvExerciseName  = findViewById(R.id.tv_exercise_name);
        tvExerciseSets  = findViewById(R.id.tv_exercise_sets);
        tvTimer         = findViewById(R.id.tv_timer);
        tvProgress      = findViewById(R.id.tv_progress);
        progressBar     = findViewById(R.id.progress_bar);
        btnRest         = findViewById(R.id.btn_rest);
        btnExit         = findViewById(R.id.btn_exit);
        tvDayTitle         = findViewById(R.id.tv_day_title);
        tvProgressCount    = findViewById(R.id.tv_progress_count);
        layoutExerciseList = findViewById(R.id.layout_exercise_list);
        btnPlayVideo       = findViewById(R.id.btn_play_video);
    }

    // ===================== DATA THEO NGÀY =====================
    private List<Exercise> getExercisesForDay(String tab, int day) {
        List<ProgramScheduleData.DaySchedule> schedule = ProgramScheduleData.getSchedule(tab);
        for (ProgramScheduleData.DaySchedule ds : schedule) {
            if (ds.day == day) {
                return buildExerciseList(ds);
            }
        }
        return getDefaultExercises();
    }

    private List<Exercise> buildExerciseList(ProgramScheduleData.DaySchedule ds) {
        List<Exercise> list = new ArrayList<>();
        for (String[] ex : ds.exercises) {
            String name = ex[0];
            String sets = ex[1];
            int duration = parseDuration(ex.length > 2 ? ex[2] : "60s");
            String videoUrl = getVideoUrl(name);
            list.add(new Exercise(name, sets, duration, videoUrl));
        }
        return list;
    }

    private String getVideoUrl(String name) {
        // Tìm chính xác trước
        if (VIDEO_MAP.containsKey(name)) return VIDEO_MAP.get(name);
        // Tìm gần đúng
        for (Map.Entry<String, String> entry : VIDEO_MAP.entrySet()) {
            if (name.toLowerCase().contains(entry.getKey().toLowerCase()) ||
                entry.getKey().toLowerCase().contains(name.toLowerCase())) {
                return entry.getValue();
            }
        }
        return "https://youtu.be/ml6cT4AZdqI"; // fallback
    }

    private int parseDuration(String rest) {
        if (rest == null || rest.equals("-")) return 45;
        try {
            return Integer.parseInt(rest.replace("s", "").replace("m", "").trim());
        } catch (Exception e) { return 45; }
    }

    private List<Exercise> getSingleExercise(String name, String sets, String videoUrl, String rest) {
        List<Exercise> list = new ArrayList<>();
        if (name == null) return getDefaultExercises();
        if (sets == null) sets = "3×10";
        if (videoUrl == null || videoUrl.isEmpty()) videoUrl = getVideoUrl(name);
        int dur = parseDuration(rest != null ? rest : "60s");
        list.add(new Exercise(name, sets, dur, videoUrl));
        return list;
    }

    private List<Exercise> buildFromArrays(String[] names, String[] sets, String[] videos, String[] rests) {
        List<Exercise> list = new ArrayList<>();
        if (names == null) return getDefaultExercises();
        for (int i = 0; i < names.length; i++) {
            String name  = names[i];
            String s     = (sets   != null && i < sets.length)   ? sets[i]   : "3×10";
            String video = (videos != null && i < videos.length) ? videos[i] : getVideoUrl(name);
            String rest  = (rests  != null && i < rests.length)  ? rests[i]  : "60s";
            if (video == null || video.isEmpty()) video = getVideoUrl(name);
            int dur = parseDuration(rest);
            list.add(new Exercise(name, s, dur, video));
        }
        return list.isEmpty() ? getDefaultExercises() : list;
    }

    private List<Exercise> getDefaultExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Push-up cơ bản",   "3×10", 45, "https://youtu.be/IODxDxX7oi4"));
        list.add(new Exercise("Squat không tạ",    "3×15", 45, "https://youtu.be/ultWZbUMPL8"));
        list.add(new Exercise("Plank",             "3×30s", 30, "https://youtu.be/pSHjTRCQxIw"));
        list.add(new Exercise("Lunges",            "3×10/chân", 45, "https://youtu.be/A-cFYWvaHr0"));
        list.add(new Exercise("Mountain Climbers", "3×20s", 30, "https://youtu.be/nmwgirgXLYM"));
        return list;
    }

    private List<Exercise> getLegacyExercises(String programType, int phaseIndex) {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Jumping Jacks",    "3×20",  30, "https://youtu.be/JZQA08SlJnM"));
        list.add(new Exercise("Gập bụng cơ bản", "3×15",  30, "https://youtu.be/Xyd_fa5zoEU"));
        list.add(new Exercise("Plank",            "3×30s", 30, "https://youtu.be/pSHjTRCQxIw"));
        list.add(new Exercise("Mountain Climbers","3×15s", 30, "https://youtu.be/nmwgirgXLYM"));
        list.add(new Exercise("Leg Raises",       "3×12",  30, "https://youtu.be/Xyd_fa5zoEU"));
        return list;
    }

    // ===================== UI & TIMER =====================
    private void renderExerciseList() {
        if (layoutExerciseList == null) return;
        layoutExerciseList.removeAllViews();

        if (tvProgressCount != null) {
            tvProgressCount.setText(currentExerciseIndex + " / " + exercises.size());
        }

        float dp = getResources().getDisplayMetrics().density;

        for (int i = 0; i < exercises.size(); i++) {
            Exercise ex = exercises.get(i);
            final int idx = i;
            boolean isCurrent = (i == currentExerciseIndex);
            boolean isDone    = (i < currentExerciseIndex);

            // Row container
            android.widget.LinearLayout row = new android.widget.LinearLayout(this);
            row.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            android.widget.LinearLayout.LayoutParams rp = new android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    (int)(56 * dp));
            rp.setMargins(0, 0, 0, (int)(2 * dp));
            row.setLayoutParams(rp);
            row.setPadding((int)(12*dp), 0, (int)(12*dp), 0);

            if (isCurrent) {
                // Highlight row với rounded background
                android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
                bg.setColor(android.graphics.Color.parseColor("#1A3A2A"));
                bg.setCornerRadius(12 * dp);
                bg.setStroke((int)(1.5f * dp), android.graphics.Color.parseColor("#6FCF97"));
                row.setBackground(bg);
            } else {
                row.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            }

            // Status badge (circle)
            android.widget.FrameLayout badge = new android.widget.FrameLayout(this);
            android.widget.LinearLayout.LayoutParams bp = new android.widget.LinearLayout.LayoutParams(
                    (int)(32*dp), (int)(32*dp));
            bp.setMargins(0, 0, (int)(12*dp), 0);
            badge.setLayoutParams(bp);

            android.graphics.drawable.GradientDrawable circle = new android.graphics.drawable.GradientDrawable();
            circle.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            if (isCurrent) {
                circle.setColor(android.graphics.Color.parseColor("#6FCF97"));
            } else if (isDone) {
                circle.setColor(android.graphics.Color.parseColor("#1A3A2A"));
                circle.setStroke((int)(1*dp), android.graphics.Color.parseColor("#6FCF97"));
            } else {
                circle.setColor(android.graphics.Color.parseColor("#1A2744"));
            }
            badge.setBackground(circle);

            TextView tvBadge = new TextView(this);
            android.widget.FrameLayout.LayoutParams tvbp = new android.widget.FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                    android.widget.FrameLayout.LayoutParams.MATCH_PARENT);
            tvBadge.setLayoutParams(tvbp);
            tvBadge.setGravity(android.view.Gravity.CENTER);
            if (isCurrent) {
                tvBadge.setText("▶");
                tvBadge.setTextColor(android.graphics.Color.parseColor("#080B14"));
                tvBadge.setTextSize(10);
            } else if (isDone) {
                tvBadge.setText("✓");
                tvBadge.setTextColor(android.graphics.Color.parseColor("#6FCF97"));
                tvBadge.setTextSize(13);
                tvBadge.setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                tvBadge.setText(String.valueOf(i + 1));
                tvBadge.setTextColor(android.graphics.Color.parseColor("#445577"));
                tvBadge.setTextSize(11);
                tvBadge.setTypeface(null, android.graphics.Typeface.BOLD);
            }
            badge.addView(tvBadge);

            // Name
            TextView tvName = new TextView(this);
            tvName.setText(ex.name);
            if (isCurrent) {
                tvName.setTextColor(android.graphics.Color.WHITE);
                tvName.setTypeface(null, android.graphics.Typeface.BOLD);
                tvName.setTextSize(14);
            } else if (isDone) {
                tvName.setTextColor(android.graphics.Color.parseColor("#556677"));
                tvName.setTextSize(13);
            } else {
                tvName.setTextColor(android.graphics.Color.parseColor("#8899BB"));
                tvName.setTextSize(13);
            }
            tvName.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                    0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            // Sets pill
            android.widget.FrameLayout setsPill = new android.widget.FrameLayout(this);
            android.graphics.drawable.GradientDrawable pillBg = new android.graphics.drawable.GradientDrawable();
            pillBg.setCornerRadius(20 * dp);
            if (isCurrent) {
                pillBg.setColor(android.graphics.Color.parseColor("#1E1040"));
            } else if (isDone) {
                pillBg.setColor(android.graphics.Color.parseColor("#0D1520"));
            } else {
                pillBg.setColor(android.graphics.Color.parseColor("#0D1520"));
            }
            setsPill.setBackground(pillBg);
            setsPill.setPadding((int)(12*dp), (int)(4*dp), (int)(12*dp), (int)(4*dp));

            TextView tvSets = new TextView(this);
            tvSets.setText(ex.sets);
            tvSets.setTextColor(isCurrent
                    ? android.graphics.Color.parseColor("#A78BFA")
                    : (isDone ? android.graphics.Color.parseColor("#334455")
                              : android.graphics.Color.parseColor("#445577")));
            tvSets.setTextSize(12);
            tvSets.setTypeface(null, android.graphics.Typeface.BOLD);
            setsPill.addView(tvSets);

            row.addView(badge);
            row.addView(tvName);
            row.addView(setsPill);

            row.setOnClickListener(v -> {
                stopTimer();
                currentExerciseIndex = idx;
                renderExerciseList();
                displayCurrentExercise();
            });
            layoutExerciseList.addView(row);

            // Divider (không thêm sau row cuối)
            if (i < exercises.size() - 1) {
                View divider = new View(this);
                android.widget.LinearLayout.LayoutParams dp2 = new android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT, (int)(0.5f * dp));
                dp2.setMargins((int)(44*dp), 0, 0, 0);
                divider.setLayoutParams(dp2);
                divider.setBackgroundColor(android.graphics.Color.parseColor("#1A2744"));
                layoutExerciseList.addView(divider);
            }
        }
    }

    private void setupClickListeners() {
        if (btnClose != null) btnClose.setOnClickListener(v -> showExitDialog());

        if (btnPlay != null) btnPlay.setOnClickListener(v -> {
            if (isTimerRunning) pauseTimer(); else startTimer();
        });

        if (btnPlayVideo != null) {
            btnPlayVideo.setOnClickListener(v -> {
                if (!exercises.isEmpty()) openYouTubeVideo(exercises.get(currentExerciseIndex).videoUrl);
            });
        }
        if (imgVideoPreview != null) {
            imgVideoPreview.setOnClickListener(v -> {
                if (!exercises.isEmpty()) openYouTubeVideo(exercises.get(currentExerciseIndex).videoUrl);
            });
        }

        if (btnPrevious != null) btnPrevious.setOnClickListener(v -> {
            if (currentExerciseIndex > 0) {
                stopTimer(); currentExerciseIndex--; displayCurrentExercise();
            }
        });

        if (btnNext != null) btnNext.setOnClickListener(v -> {
            if (currentExerciseIndex < exercises.size() - 1) {
                stopTimer(); currentExerciseIndex++; displayCurrentExercise();
            } else {
                showCompletionDialog();
            }
        });

        if (btnRest != null) btnRest.setOnClickListener(v -> showRestDialog());
        if (btnExit != null) btnExit.setOnClickListener(v -> showExitDialog());
    }

    private void displayCurrentExercise() {
        if (exercises.isEmpty()) return;
        Exercise ex = exercises.get(currentExerciseIndex);

        tvExerciseName.setText(ex.name);
        tvExerciseSets.setText(ex.sets);
        tvProgress.setText(String.format(Locale.getDefault(),
                "BÀI %d / %d", currentExerciseIndex + 1, exercises.size()));

        timeLeftInMillis = ex.durationSeconds * 1000L;
        updateTimerText();
        progressBar.setProgress(0);

        YouTubeThumbnailHelper.loadThumbnail(imgVideoPreview, ex.videoUrl, "hqdefault");

        btnPrevious.setAlpha(currentExerciseIndex > 0 ? 1.0f : 0.3f);
        renderExerciseList();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override public void onTick(long ms) {
                timeLeftInMillis = ms;
                updateTimerText();
                Exercise ex = exercises.get(currentExerciseIndex);
                long total = ex.durationSeconds * 1000L;
                progressBar.setProgress((int) ((total - ms) * 100 / total));
            }
            @Override public void onFinish() {
                isTimerRunning = false; updatePlayButton(); progressBar.setProgress(100);
                if (currentExerciseIndex < exercises.size() - 1) {
                    currentExerciseIndex++; displayCurrentExercise();
                } else { showCompletionDialog(); }
            }
        }.start();
        isTimerRunning = true; updatePlayButton();
    }

    private void pauseTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        isTimerRunning = false; updatePlayButton();
    }

    private void stopTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        isTimerRunning = false; updatePlayButton();
    }

    private void updateTimerText() {
        int m = (int)(timeLeftInMillis / 1000) / 60;
        int s = (int)(timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", m, s));
    }

    private void updatePlayButton() {
        TextView icon = btnPlay.findViewById(R.id.tv_play_icon);
        if (icon != null) icon.setText(isTimerRunning ? "⏸" : "▶");
    }

    private void showRestDialog() {
        pauseTimer();
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_rest_options, null);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            Button btn30    = dialogView.findViewById(R.id.btn_rest_30);
            Button btn60    = dialogView.findViewById(R.id.btn_rest_60);
            Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
            if (btn30 != null)    btn30.setOnClickListener(v -> { dialog.dismiss(); startRestTimer(30); });
            if (btn60 != null)    btn60.setOnClickListener(v -> { dialog.dismiss(); startRestTimer(60); });
            if (btnCancel != null) btnCancel.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        } catch (Exception e) {
            // Fallback: dialog đơn giản nếu layout lỗi
            new AlertDialog.Builder(this)
                .setTitle("Nghỉ giữa hiệp")
                .setItems(new String[]{"Nghỉ 30 giây", "Nghỉ 60 giây", "Huỷ"}, (d, which) -> {
                    if (which == 0) startRestTimer(30);
                    else if (which == 1) startRestTimer(60);
                }).show();
        }
    }

    private void startRestTimer(int seconds) {
        isRestMode = true;
        timeLeftInMillis = seconds * 1000L;
        tvExerciseName.setText("⏱ Thời gian nghỉ");
        tvExerciseSets.setText("Hít thở sâu, thư giãn cơ");
        progressBar.setProgress(0);
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override public void onTick(long ms) {
                timeLeftInMillis = ms; updateTimerText();
                progressBar.setProgress((int) ((seconds * 1000L - ms) * 100 / (seconds * 1000L)));
            }
            @Override public void onFinish() {
                isRestMode = false; isTimerRunning = false;
                displayCurrentExercise();
                Toast.makeText(ProgramExerciseSessionActivity.this,
                        "Hết giờ nghỉ! Tiếp tục tập luyện 💪", Toast.LENGTH_SHORT).show();
            }
        }.start();
        isTimerRunning = true; updatePlayButton();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Thoát buổi tập?")
                .setMessage("Bạn có chắc muốn dừng buổi tập này?")
                .setPositiveButton("Thoát", (d, w) -> finish())
                .setNegativeButton("Tiếp tục", null).show();
    }

    private void showCompletionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("🎉 Hoàn thành!")
                .setMessage("Xuất sắc! Bạn đã hoàn thành tất cả bài tập hôm nay!")
                .setPositiveButton("Xong", (d, w) -> finish())
                .setCancelable(false).show();
    }

    private void openYouTubeVideo(String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
            i.setPackage("com.google.android.youtube");
            startActivity(i);
        } catch (Exception e) {
            try { startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))); }
            catch (Exception ex) { Toast.makeText(this, "Không thể mở video", Toast.LENGTH_SHORT).show(); }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }

    static class Exercise {
        String name, sets, videoUrl;
        int durationSeconds;
        Exercise(String name, String sets, int dur, String url) {
            this.name = name; this.sets = sets; this.durationSeconds = dur; this.videoUrl = url;
        }
    }
}
