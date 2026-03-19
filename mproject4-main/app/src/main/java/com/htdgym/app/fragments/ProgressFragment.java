package com.htdgym.app.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.database.BodyMeasurementDao;
import com.htdgym.app.database.WorkoutLogDao;
import com.htdgym.app.models.BodyMeasurement;
import com.htdgym.app.models.WorkoutLog;
import com.htdgym.app.utils.SharedPrefManager;
import com.htdgym.app.utils.PremiumManager;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ProgressFragment extends Fragment {

    private LineChart chartWeight;
    private TextView tvCurrentWeight, tvWeightChange, tvTargetWeight;
    private TextView tvChest, tvWaist, tvHips, tvArms, tvThighs, tvCalves;
    private Button btnUpdateWeight, btnUpdateMeasurements, btnViewHistory;
    private LinearLayout layoutWorkoutHistory;

    // Combo cards
    private TextView tvStreakDays, tvStreakMessage;
    private TextView tvTotalWorkouts, tvTotalMinutes;
    private TextView tvWeeklyCount, tvWeeklySubtitle;
    private ProgressBar progressWeekly;
    private TextView dot1, dot2, dot3, dot4, dot5;
    private TextView tvCaloriesToday, tvCaloriesGoal, tvCaloriesStatus;
    private TextView tvDurationToday, tvCaloriesWeek;
    private ProgressBar progressCaloriesRing;
    
    private int userId;
    private GymDatabase database;
    private BodyMeasurementDao measurementDao;
    private WorkoutLogDao workoutLogDao;
    private ExecutorService executorService;
    private PremiumManager premiumManager;
    
    private float targetWeight = 70.0f;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        
        try {
            database = GymDatabase.getInstance(requireContext());
            measurementDao = database.bodyMeasurementDao();
            workoutLogDao = database.workoutLogDao();
            executorService = Executors.newSingleThreadExecutor();
            premiumManager = PremiumManager.getInstance(requireContext());
            
            String userIdStr = SharedPrefManager.getInstance(requireContext()).getUserId();
            userId = (userIdStr != null && !userIdStr.isEmpty()) ? Integer.parseInt(userIdStr) : 1;
            
            initViews(view);
            loadData();
            setupClickListeners();
            initializeSampleData();
            setupAnimations();
            
        } catch (Exception e) {
            android.util.Log.e("ProgressFragment", "Error initializing fragment", e);
            // Show error message to user
            if (getContext() != null) {
                Toast.makeText(getContext(), "Lỗi khởi tạo trang tiến trình: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload data mỗi khi quay lại tab để cập nhật workout vừa tập
        if (workoutLogDao != null) {
            loadData();
        }
    }

    private void setupAnimations() {
        // Add entrance animations for cards
        if (getView() != null) {
            getView().post(() -> {
                animateViewsIn();
            });
        }
    }

    private void animateViewsIn() {
        // Animate measurement cards with staggered delay - simplified approach
        if (tvChest != null) {
            View chestCard = (View) tvChest.getParent().getParent();
            if (chestCard != null) {
                chestCard.setAlpha(0f);
                chestCard.setScaleX(0.8f);
                chestCard.setScaleY(0.8f);
                chestCard.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(400)
                    .setStartDelay(0)
                    .start();
            }
        }
        
        if (tvWaist != null) {
            View waistCard = (View) tvWaist.getParent().getParent();
            if (waistCard != null) {
                waistCard.setAlpha(0f);
                waistCard.setScaleX(0.8f);
                waistCard.setScaleY(0.8f);
                waistCard.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(400)
                    .setStartDelay(100)
                    .start();
            }
        }
        
        if (tvHips != null) {
            View hipsCard = (View) tvHips.getParent().getParent();
            if (hipsCard != null) {
                hipsCard.setAlpha(0f);
                hipsCard.setScaleX(0.8f);
                hipsCard.setScaleY(0.8f);
                hipsCard.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(400)
                    .setStartDelay(200)
                    .start();
            }
        }
        
        if (tvArms != null) {
            View armsCard = (View) tvArms.getParent().getParent();
            if (armsCard != null) {
                armsCard.setAlpha(0f);
                armsCard.setScaleX(0.8f);
                armsCard.setScaleY(0.8f);
                armsCard.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(400)
                    .setStartDelay(300)
                    .start();
            }
        }
        
        if (tvThighs != null) {
            View thighsCard = (View) tvThighs.getParent().getParent();
            if (thighsCard != null) {
                thighsCard.setAlpha(0f);
                thighsCard.setScaleX(0.8f);
                thighsCard.setScaleY(0.8f);
                thighsCard.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(400)
                    .setStartDelay(400)
                    .start();
            }
        }
        
        if (tvCalves != null) {
            View calvesCard = (View) tvCalves.getParent().getParent();
            if (calvesCard != null) {
                calvesCard.setAlpha(0f);
                calvesCard.setScaleX(0.8f);
                calvesCard.setScaleY(0.8f);
                calvesCard.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(400)
                    .setStartDelay(500)
                    .start();
            }
        }
    }

    private void initViews(View view) {
        chartWeight = view.findViewById(R.id.chart_weight);
        tvCurrentWeight = view.findViewById(R.id.tv_current_weight);
        tvWeightChange = view.findViewById(R.id.tv_weight_change);
        tvTargetWeight = view.findViewById(R.id.tv_target_weight);
        
        tvChest = view.findViewById(R.id.tv_chest);
        tvWaist = view.findViewById(R.id.tv_waist);
        tvHips = view.findViewById(R.id.tv_hips);
        tvArms = view.findViewById(R.id.tv_arms);
        tvThighs = view.findViewById(R.id.tv_thighs);
        tvCalves = view.findViewById(R.id.tv_calves);
        
        btnUpdateWeight = view.findViewById(R.id.btn_update_weight);
        btnUpdateMeasurements = view.findViewById(R.id.btn_update_measurements);
        btnViewHistory = view.findViewById(R.id.btn_view_history);
        layoutWorkoutHistory = view.findViewById(R.id.layout_workout_history);

        // Combo cards
        tvStreakDays = view.findViewById(R.id.tv_streak_days);
        tvStreakMessage = view.findViewById(R.id.tv_streak_message);
        tvTotalWorkouts = view.findViewById(R.id.tv_total_workouts);
        tvTotalMinutes = view.findViewById(R.id.tv_total_minutes);
        tvWeeklyCount = view.findViewById(R.id.tv_weekly_count);
        tvWeeklySubtitle = view.findViewById(R.id.tv_weekly_subtitle);
        progressWeekly = view.findViewById(R.id.progress_weekly);
        dot1 = view.findViewById(R.id.dot_day1);
        dot2 = view.findViewById(R.id.dot_day2);
        dot3 = view.findViewById(R.id.dot_day3);
        dot4 = view.findViewById(R.id.dot_day4);
        dot5 = view.findViewById(R.id.dot_day5);
        tvCaloriesToday = view.findViewById(R.id.tv_calories_today);
        tvCaloriesGoal = view.findViewById(R.id.tv_calories_goal);
        tvCaloriesStatus = view.findViewById(R.id.tv_calories_status);
        tvDurationToday = view.findViewById(R.id.tv_duration_today);
        tvCaloriesWeek = view.findViewById(R.id.tv_calories_week);
        progressCaloriesRing = view.findViewById(R.id.progress_calories_ring);
        
        tvTargetWeight.setText(targetWeight + " kg");
    }

    private void loadData() {
        if (executorService == null || measurementDao == null || workoutLogDao == null) {
            android.util.Log.e("ProgressFragment", "DAOs not initialized properly");
            return;
        }
        
        executorService.execute(() -> {
            try {
                // Load latest measurement
                BodyMeasurement latest = measurementDao.getLatestMeasurement(userId);
                
                // Load recent measurements for chart
                List<BodyMeasurement> recent = measurementDao.getRecentMeasurements(userId, 30);
                
                // Load workout history
                List<WorkoutLog> workouts = workoutLogDao.getRecentWorkouts(userId, 10);

                // Combo data
                long todayStart = getTodayStartMillis();
                long weekStart = getWeekStartMillis();
                Integer caloriesToday = workoutLogDao.getCaloriesSince(userId, todayStart);
                List<WorkoutLog> weekWorkouts = workoutLogDao.getWorkoutsSince(userId, weekStart);
                int streakDays = calculateStreak(workoutLogDao.getAllWorkouts(userId));
                int totalWorkouts = workoutLogDao.getTotalWorkouts(userId);
                Integer totalMinutes = workoutLogDao.getTotalMinutes(userId);
                int durationToday = calcDurationToday(workoutLogDao.getWorkoutsSince(userId, todayStart));
                int caloriesWeek = 0;
                Integer cw = workoutLogDao.getCaloriesSince(userId, weekStart);
                if (cw != null) caloriesWeek = cw;
                
                if (getActivity() != null && !getActivity().isFinishing()) {
                    requireActivity().runOnUiThread(() -> {
                        try {
                            if (latest != null) {
                                displayLatestMeasurement(latest);
                                setupWeightChart(recent);
                            } else {
                                // Show default values
                                if (tvCurrentWeight != null) tvCurrentWeight.setText("-- kg");
                                if (tvWeightChange != null) tvWeightChange.setText("--");
                                if (tvChest != null) tvChest.setText("--");
                                if (tvWaist != null) tvWaist.setText("--");
                                if (tvHips != null) tvHips.setText("--");
                                if (tvArms != null) tvArms.setText("--");
                                if (tvThighs != null) tvThighs.setText("--");
                                if (tvCalves != null) tvCalves.setText("--");
                            }
                            
                            displayWorkoutHistory(workouts);
                            updateComboCards(streakDays, weekWorkouts, caloriesToday != null ? caloriesToday : 0,
                                    totalWorkouts, totalMinutes != null ? totalMinutes : 0,
                                    durationToday, caloriesWeek);
                        } catch (Exception e) {
                            android.util.Log.e("ProgressFragment", "Error updating UI", e);
                        }
                    });
                }
            } catch (Exception e) {
                android.util.Log.e("ProgressFragment", "Error loading data", e);
            }
        });
    }

    private void displayLatestMeasurement(BodyMeasurement measurement) {
        tvCurrentWeight.setText(String.format("%.1f kg", measurement.getWeight()));
        
        // Calculate weight change
        executorService.execute(() -> {
            List<BodyMeasurement> all = measurementDao.getAllMeasurements(userId);
            if (all.size() > 1) {
                float firstWeight = all.get(all.size() - 1).getWeight();
                float change = measurement.getWeight() - firstWeight;
                requireActivity().runOnUiThread(() -> {
                    tvWeightChange.setText(String.format("%+.1f kg", change));
                    tvWeightChange.setTextColor(getResources().getColor(
                            change < 0 ? R.color.green_primary : android.R.color.holo_red_light));
                });
            }
        });
        
        tvChest.setText(String.format("%.0f cm", measurement.getChest()));
        tvWaist.setText(String.format("%.0f cm", measurement.getWaist()));
        tvHips.setText(String.format("%.0f cm", measurement.getHips()));
        tvArms.setText(String.format("%.0f cm", measurement.getArms()));
        tvThighs.setText(String.format("%.0f cm", measurement.getThighs()));
        
        // Calves might be 0 if not set
        if (measurement.getThighs() > 0) {
            tvCalves.setText(String.format("%.0f cm", measurement.getThighs() * 0.65f));
        } else {
            tvCalves.setText("--");
        }
    }

    private void setupWeightChart(List<BodyMeasurement> measurements) {
        if (measurements.isEmpty()) {
            chartWeight.setNoDataText("Chưa có dữ liệu cân nặng");
            return;
        }
        
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < measurements.size(); i++) {
            entries.add(new Entry(i, measurements.get(measurements.size() - 1 - i).getWeight()));
        }
        
        LineDataSet dataSet = new LineDataSet(entries, "Cân nặng (kg)");
        dataSet.setColor(Color.parseColor("#6FCF97"));
        dataSet.setCircleColor(Color.parseColor("#6FCF97"));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#6FCF97"));
        dataSet.setFillAlpha(50);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        
        LineData lineData = new LineData(dataSet);
        chartWeight.setData(lineData);
        chartWeight.getDescription().setEnabled(false);
        chartWeight.getLegend().setTextColor(Color.WHITE);
        chartWeight.getXAxis().setTextColor(Color.WHITE);
        chartWeight.getAxisLeft().setTextColor(Color.WHITE);
        chartWeight.getAxisRight().setEnabled(false);
        chartWeight.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartWeight.animateX(1000);
        chartWeight.invalidate();
    }

    private void displayWorkoutHistory(List<WorkoutLog> workouts) {
        layoutWorkoutHistory.removeAllViews();
        
        if (workouts.isEmpty()) {
            TextView emptyText = new TextView(requireContext());
            emptyText.setText("Chưa có lịch sử tập luyện");
            emptyText.setTextColor(getResources().getColor(R.color.gray_light));
            emptyText.setPadding(16, 16, 16, 16);
            layoutWorkoutHistory.addView(emptyText);
            return;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        
        for (WorkoutLog workout : workouts) {
            addWorkoutItem(workout, dateFormat);
        }
    }

    private void addWorkoutItem(WorkoutLog workout, SimpleDateFormat dateFormat) {
        androidx.cardview.widget.CardView itemCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 16);
        itemCard.setLayoutParams(cardParams);
        itemCard.setRadius(16f);
        itemCard.setCardElevation(6f);
        itemCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        
        LinearLayout itemLayout = new LinearLayout(requireContext());
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(20, 16, 20, 16);
        itemLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        // Workout icon with gradient background
        androidx.cardview.widget.CardView iconContainer = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(60, 60);
        iconParams.setMargins(0, 0, 16, 0);
        iconContainer.setLayoutParams(iconParams);
        iconContainer.setRadius(30f);
        iconContainer.setCardElevation(4f);
        iconContainer.setCardBackgroundColor(getResources().getColor(R.color.green_primary));
        
        TextView workoutIcon = new TextView(requireContext());
        workoutIcon.setText(getWorkoutEmoji(workout.getWorkoutName()));
        workoutIcon.setTextSize(20);
        workoutIcon.setGravity(android.view.Gravity.CENTER);
        workoutIcon.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.MATCH_PARENT));
        
        iconContainer.addView(workoutIcon);
        
        // Content layout
        LinearLayout contentLayout = new LinearLayout(requireContext());
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        contentLayout.setLayoutParams(contentParams);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView tvWorkoutName = new TextView(requireContext());
        tvWorkoutName.setText(workout.getWorkoutName());
        tvWorkoutName.setTextColor(getResources().getColor(R.color.text_primary));
        tvWorkoutName.setTextSize(16);
        tvWorkoutName.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView tvDate = new TextView(requireContext());
        tvDate.setText(dateFormat.format(workout.getDate()));
        tvDate.setTextColor(getResources().getColor(R.color.text_secondary));
        tvDate.setTextSize(12);
        
        TextView tvDetails = new TextView(requireContext());
        tvDetails.setText("⏱️ " + workout.getDuration() + " phút | 🔥 " + workout.getCaloriesBurned() + " cal");
        tvDetails.setTextColor(getResources().getColor(R.color.secondary));
        tvDetails.setTextSize(14);
        
        contentLayout.addView(tvWorkoutName);
        contentLayout.addView(tvDate);
        contentLayout.addView(tvDetails);
        
        // Achievement badge for premium users
        if (premiumManager.isPremiumUser() && workout.getCaloriesBurned() > 300) {
            androidx.cardview.widget.CardView achievementBadge = new androidx.cardview.widget.CardView(requireContext());
            achievementBadge.setRadius(12f);
            achievementBadge.setCardElevation(4f);
            achievementBadge.setCardBackgroundColor(getResources().getColor(R.color.premium_gold));
            
            TextView badgeText = new TextView(requireContext());
            badgeText.setText("🏆");
            badgeText.setTextSize(16);
            badgeText.setPadding(8, 4, 8, 4);
            
            achievementBadge.addView(badgeText);
            itemLayout.addView(achievementBadge);
        }
        
        itemLayout.addView(iconContainer);
        itemLayout.addView(contentLayout);
        
        itemCard.addView(itemLayout);
        layoutWorkoutHistory.addView(itemCard);
        
        // Add entrance animation
        itemCard.setAlpha(0f);
        itemCard.setTranslationX(100f);
        itemCard.animate()
            .alpha(1f)
            .translationX(0f)
            .setDuration(300)
            .start();
    }

    private String getWorkoutEmoji(String workoutName) {
        String name = workoutName.toLowerCase();
        if (name.contains("chest") || name.contains("ngực")) return "💪";
        if (name.contains("back") || name.contains("lưng")) return "🏋️";
        if (name.contains("legs") || name.contains("chân")) return "🦵";
        if (name.contains("shoulders") || name.contains("vai")) return "🤸";
        if (name.contains("arms") || name.contains("tay")) return "💪";
        if (name.contains("abs") || name.contains("bụng")) return "🔥";
        if (name.contains("cardio") || name.contains("tim mạch")) return "❤️";
        if (name.contains("full body") || name.contains("toàn thân")) return "🏃";
        return "💪";
    }

    private void setupClickListeners() {
        if (btnUpdateWeight != null) {
            btnUpdateWeight.setOnClickListener(v -> showUpdateWeightDialog());
        }
        
        if (btnUpdateMeasurements != null) {
            btnUpdateMeasurements.setOnClickListener(v -> {
                if (premiumManager != null && premiumManager.isPremiumUser()) {
                    showUpdateMeasurementsDialog();
                } else {
                    showPremiumRequiredDialog("Cập nhật số đo chi tiết");
                }
            });
        }
        
        if (btnViewHistory != null) {
            btnViewHistory.setOnClickListener(v -> showFullHistory());
        }
    }

    private void showPremiumRequiredDialog(String feature) {
        new AlertDialog.Builder(requireContext())
            .setTitle("🔒 Tính năng Premium")
            .setMessage("Tính năng \"" + feature + "\" chỉ dành cho người dùng Premium.\n\n" +
                "Nâng cấp để sử dụng:\n" +
                "📏 Theo dõi số đo chi tiết 6 vùng cơ thể\n" +
                "📊 Biểu đồ tiến độ nâng cao\n" +
                "🎯 Mục tiêu cá nhân hóa\n" +
                "🏆 Huy hiệu thành tích")
            .setPositiveButton("💎 Nâng cấp Premium", (dialog, which) -> {
                Intent intent = new Intent(requireContext(), com.htdgym.app.activities.PremiumActivity.class);
                startActivity(intent);
            })
            .setNegativeButton("Để sau", null)
            .setIcon(R.drawable.ic_premium_crown)
            .show();
    }

    private void showUpdateWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Cập nhật cân nặng");
        
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);
        
        EditText etWeight = new EditText(requireContext());
        etWeight.setHint("Cân nặng (kg)");
        etWeight.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etWeight);
        
        builder.setView(layout);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String weightStr = etWeight.getText().toString().trim();
            if (weightStr.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập cân nặng", Toast.LENGTH_SHORT).show();
                return;
            }
            
            float weight = Float.parseFloat(weightStr);
            updateWeight(weight);
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void updateWeight(float weight) {
        executorService.execute(() -> {
            BodyMeasurement latest = measurementDao.getLatestMeasurement(userId);
            
            BodyMeasurement newMeasurement = new BodyMeasurement(
                    userId,
                    new Date(),
                    weight,
                    latest != null ? latest.getChest() : 0,
                    latest != null ? latest.getWaist() : 0,
                    latest != null ? latest.getHips() : 0,
                    latest != null ? latest.getArms() : 0,
                    latest != null ? latest.getThighs() : 0
            );
            
            measurementDao.insert(newMeasurement);
            
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Đã cập nhật cân nặng", Toast.LENGTH_SHORT).show();
                loadData();
            });
        });
    }

    private void showUpdateMeasurementsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Cập nhật số đo cơ thể");
        
        ScrollView scrollView = new ScrollView(requireContext());
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);
        
        EditText etWeight = new EditText(requireContext());
        etWeight.setHint("Cân nặng (kg)");
        etWeight.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etWeight);
        
        EditText etChest = new EditText(requireContext());
        etChest.setHint("Ngực (cm)");
        etChest.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etChest);
        
        EditText etWaist = new EditText(requireContext());
        etWaist.setHint("Eo (cm)");
        etWaist.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etWaist);
        
        EditText etHips = new EditText(requireContext());
        etHips.setHint("Mông (cm)");
        etHips.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etHips);
        
        EditText etArms = new EditText(requireContext());
        etArms.setHint("Tay (cm)");
        etArms.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etArms);
        
        EditText etThighs = new EditText(requireContext());
        etThighs.setHint("Đùi (cm)");
        etThighs.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(etThighs);
        
        scrollView.addView(layout);
        builder.setView(scrollView);
        
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            try {
                float weight = Float.parseFloat(etWeight.getText().toString().trim());
                float chest = Float.parseFloat(etChest.getText().toString().trim());
                float waist = Float.parseFloat(etWaist.getText().toString().trim());
                float hips = Float.parseFloat(etHips.getText().toString().trim());
                float arms = Float.parseFloat(etArms.getText().toString().trim());
                float thighs = Float.parseFloat(etThighs.getText().toString().trim());
                
                updateMeasurements(weight, chest, waist, hips, arms, thighs);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void updateMeasurements(float weight, float chest, float waist, float hips, float arms, float thighs) {
        executorService.execute(() -> {
            BodyMeasurement newMeasurement = new BodyMeasurement(
                    userId,
                    new Date(),
                    weight,
                    chest,
                    waist,
                    hips,
                    arms,
                    thighs
            );
            
            measurementDao.insert(newMeasurement);
            
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Đã cập nhật số đo", Toast.LENGTH_SHORT).show();
                loadData();
            });
        });
    }

    private void showFullHistory() {
        executorService.execute(() -> {
            List<WorkoutLog> allWorkouts = workoutLogDao.getAllWorkouts(userId);
            
            requireActivity().runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("📅 Lịch sử tập luyện");
                
                if (allWorkouts.isEmpty()) {
                    builder.setMessage("Chưa có lịch sử tập luyện");
                } else {
                    ScrollView scrollView = new ScrollView(requireContext());
                    LinearLayout layout = new LinearLayout(requireContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(20, 20, 20, 20);
                    
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    
                    for (WorkoutLog workout : allWorkouts) {
                        TextView tv = new TextView(requireContext());
                        tv.setText(dateFormat.format(workout.getDate()) + "\n" +
                                workout.getWorkoutName() + "\n" +
                                workout.getDuration() + " phút | " + workout.getCaloriesBurned() + " cal");
                        tv.setTextColor(getResources().getColor(android.R.color.white));
                        tv.setPadding(0, 0, 0, 20);
                        layout.addView(tv);
                    }
                    
                    scrollView.addView(layout);
                    builder.setView(scrollView);
                }
                
                builder.setPositiveButton("Đóng", null);
                builder.show();
            });
        });
    }

    private void initializeSampleData() {
        if (executorService == null || workoutLogDao == null || measurementDao == null) {
            return;
        }
        
        executorService.execute(() -> {
            try {
                // Check if data already exists
                int workoutCount = workoutLogDao.getTotalWorkouts(userId);
                if (workoutCount > 0) {
                    return; // Data already exists
                }
                
                // Add sample workout logs
                Calendar cal = Calendar.getInstance();
                
                // Today
                workoutLogDao.insert(new WorkoutLog(userId, "Chest & Triceps", cal.getTime(), 45, 320));
                
                // Yesterday
                cal.add(Calendar.DAY_OF_MONTH, -1);
                workoutLogDao.insert(new WorkoutLog(userId, "Back & Biceps", cal.getTime(), 50, 350));
                
                // 2 days ago
                cal.add(Calendar.DAY_OF_MONTH, -1);
                workoutLogDao.insert(new WorkoutLog(userId, "Legs", cal.getTime(), 60, 420));
                
                // 3 days ago
                cal.add(Calendar.DAY_OF_MONTH, -1);
                workoutLogDao.insert(new WorkoutLog(userId, "Shoulders & Abs", cal.getTime(), 40, 280));
                
                // 5 days ago
                cal.add(Calendar.DAY_OF_MONTH, -2);
                workoutLogDao.insert(new WorkoutLog(userId, "Full Body", cal.getTime(), 55, 380));
                
                // Add sample body measurements
                BodyMeasurement latest = measurementDao.getLatestMeasurement(userId);
                if (latest == null) {
                    cal = Calendar.getInstance();
                    
                    // Current measurement
                    measurementDao.insert(new BodyMeasurement(userId, cal.getTime(), 72.0f, 98, 78, 95, 35, 58));
                    
                    // 7 days ago
                    cal.add(Calendar.DAY_OF_MONTH, -7);
                    measurementDao.insert(new BodyMeasurement(userId, cal.getTime(), 72.5f, 97, 79, 94, 34, 57));
                    
                    // 14 days ago
                    cal.add(Calendar.DAY_OF_MONTH, -7);
                    measurementDao.insert(new BodyMeasurement(userId, cal.getTime(), 73.0f, 96, 80, 93, 34, 57));
                    
                    // 21 days ago
                    cal.add(Calendar.DAY_OF_MONTH, -7);
                    measurementDao.insert(new BodyMeasurement(userId, cal.getTime(), 73.5f, 95, 81, 92, 33, 56));
                    
                    // 30 days ago
                    cal.add(Calendar.DAY_OF_MONTH, -9);
                    measurementDao.insert(new BodyMeasurement(userId, cal.getTime(), 75.0f, 94, 82, 91, 33, 56));
                }
                
                if (getActivity() != null && !getActivity().isFinishing()) {
                    requireActivity().runOnUiThread(() -> {
                        loadData(); // Reload to show sample data
                    });
                }
            } catch (Exception e) {
                android.util.Log.e("ProgressFragment", "Error initializing sample data", e);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    // ===== COMBO CARDS METHODS =====

    private void updateComboCards(int streakDays, List<WorkoutLog> weekWorkouts, int caloriesToday,
                                   int totalWorkouts, int totalMinutes, int durationToday, int caloriesWeek) {
        int weeklyCount = weekWorkouts.size();

        // --- Streak card ---
        if (tvStreakDays != null) tvStreakDays.setText(String.valueOf(streakDays));
        if (tvStreakMessage != null) {
            String msg;
            if (streakDays == 0) msg = "Bắt đầu thôi! 💪";
            else if (streakDays < 3) msg = "Tiếp tục duy trì!";
            else if (streakDays < 7) msg = "Đang trên đà tốt 🔥";
            else if (streakDays < 14) msg = "Xuất sắc! Giữ vững 🏆";
            else msg = "Huyền thoại! 🌟";
            tvStreakMessage.setText(msg);
        }

        // --- Total workouts card ---
        if (tvTotalWorkouts != null) tvTotalWorkouts.setText(String.valueOf(totalWorkouts));
        if (tvTotalMinutes != null) {
            int hours = totalMinutes / 60;
            int mins = totalMinutes % 60;
            tvTotalMinutes.setText(hours > 0 ? hours + "h " + mins + "m tổng" : totalMinutes + " phút tổng");
        }

        // --- Weekly goal ---
        int weeklyGoal = 5;
        if (progressWeekly != null) progressWeekly.setProgress(Math.min(weeklyCount, weeklyGoal));
        if (tvWeeklyCount != null) tvWeeklyCount.setText(weeklyCount + "/" + weeklyGoal);
        if (tvWeeklySubtitle != null) {
            int remaining = Math.max(0, weeklyGoal - weeklyCount);
            tvWeeklySubtitle.setText(remaining == 0 ? "🎉 Đã đạt mục tiêu!" : "Còn " + remaining + " buổi nữa");
        }
        updateWeekDots(weekWorkouts);

        // --- Calories card ---
        int caloriesGoal = 500;
        if (progressCaloriesRing != null) progressCaloriesRing.setProgress(Math.min(caloriesToday, caloriesGoal));
        if (tvCaloriesToday != null) tvCaloriesToday.setText(String.valueOf(caloriesToday));
        if (tvCaloriesGoal != null) tvCaloriesGoal.setText(caloriesGoal + " cal");
        if (tvCaloriesStatus != null) {
            if (caloriesToday == 0) tvCaloriesStatus.setText("Chưa tập hôm nay");
            else if (caloriesToday < caloriesGoal) tvCaloriesStatus.setText("Còn " + (caloriesGoal - caloriesToday) + " cal");
            else tvCaloriesStatus.setText("🎯 Đạt mục tiêu!");
        }
        if (tvDurationToday != null) tvDurationToday.setText(durationToday + " phút");
        if (tvCaloriesWeek != null) tvCaloriesWeek.setText(caloriesWeek + " cal");
    }

    private void updateWeekDots(List<WorkoutLog> weekWorkouts) {
        if (dot1 == null) return;
        TextView[] dots = {dot1, dot2, dot3, dot4, dot5};
        // Map workout dates to day-of-week (Mon=2..Fri=6 in Calendar)
        Set<Integer> workedDays = new HashSet<>();
        for (WorkoutLog log : weekWorkouts) {
            if (log.getDate() != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(log.getDate());
                workedDays.add(c.get(Calendar.DAY_OF_WEEK)); // 1=Sun,2=Mon...
            }
        }
        // dots[0]=Mon(2), dots[1]=Tue(3), dots[2]=Wed(4), dots[3]=Thu(5), dots[4]=Fri(6)
        int[] calDays = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY};
        int activeColor = getResources().getColor(R.color.green_primary);
        int inactiveColor = getResources().getColor(R.color.gray_light);
        for (int i = 0; i < 5; i++) {
            if (workedDays.contains(calDays[i])) {
                dots[i].setText("✓");
                dots[i].setBackgroundTintList(android.content.res.ColorStateList.valueOf(activeColor));
            } else {
                dots[i].setText(new String[]{"T2","T3","T4","T5","T6"}[i]);
                dots[i].setBackgroundTintList(android.content.res.ColorStateList.valueOf(inactiveColor));
            }
        }
    }

    private int calcDurationToday(List<WorkoutLog> todayWorkouts) {
        int total = 0;
        for (WorkoutLog log : todayWorkouts) total += log.getDuration();
        return total;
    }

    private int calculateStreak(List<WorkoutLog> allWorkouts) {
        if (allWorkouts == null || allWorkouts.isEmpty()) return 0;

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // Collect unique workout days
        Set<String> workoutDays = new HashSet<>();
        SimpleDateFormat dayFmt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        for (WorkoutLog log : allWorkouts) {
            if (log.getDate() != null) workoutDays.add(dayFmt.format(log.getDate()));
        }

        int streak = 0;
        Calendar check = (Calendar) today.clone();
        // If no workout today, start checking from yesterday
        if (!workoutDays.contains(dayFmt.format(check.getTime()))) {
            check.add(Calendar.DAY_OF_MONTH, -1);
        }
        while (workoutDays.contains(dayFmt.format(check.getTime()))) {
            streak++;
            check.add(Calendar.DAY_OF_MONTH, -1);
        }
        return streak;
    }

    private long getTodayStartMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private long getWeekStartMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
