package com.htdgym.app.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.LineChart;
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
    
    private int userId;
    private GymDatabase database;
    private BodyMeasurementDao measurementDao;
    private WorkoutLogDao workoutLogDao;
    private ExecutorService executorService;
    
    private float targetWeight = 70.0f;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        
        database = GymDatabase.getInstance(requireContext());
        measurementDao = database.bodyMeasurementDao();
        workoutLogDao = database.workoutLogDao();
        executorService = Executors.newSingleThreadExecutor();
        
        String userIdStr = SharedPrefManager.getInstance(requireContext()).getUserId();
        userId = (userIdStr != null) ? Integer.parseInt(userIdStr) : 1;
        
        initViews(view);
        loadData();
        setupClickListeners();
        initializeSampleData();
        
        return view;
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
        
        tvTargetWeight.setText(targetWeight + " kg");
    }

    private void loadData() {
        executorService.execute(() -> {
            // Load latest measurement
            BodyMeasurement latest = measurementDao.getLatestMeasurement(userId);
            
            // Load recent measurements for chart
            List<BodyMeasurement> recent = measurementDao.getRecentMeasurements(userId, 30);
            
            // Load workout history
            List<WorkoutLog> workouts = workoutLogDao.getRecentWorkouts(userId, 10);
            
            requireActivity().runOnUiThread(() -> {
                if (latest != null) {
                    displayLatestMeasurement(latest);
                    setupWeightChart(recent);
                } else {
                    // Show default values
                    tvCurrentWeight.setText("-- kg");
                    tvWeightChange.setText("--");
                    tvChest.setText("--");
                    tvWaist.setText("--");
                    tvHips.setText("--");
                    tvArms.setText("--");
                    tvThighs.setText("--");
                    tvCalves.setText("--");
                }
                
                displayWorkoutHistory(workouts);
            });
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
        LinearLayout itemLayout = new LinearLayout(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 12);
        itemLayout.setLayoutParams(params);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(16, 16, 16, 16);
        itemLayout.setBackgroundColor(getResources().getColor(R.color.gray_dark));
        
        // Left bar
        View leftBar = new View(requireContext());
        LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(4, LinearLayout.LayoutParams.MATCH_PARENT);
        barParams.setMargins(0, 0, 12, 0);
        leftBar.setLayoutParams(barParams);
        leftBar.setBackgroundColor(getResources().getColor(R.color.green_primary));
        
        // Content
        LinearLayout contentLayout = new LinearLayout(requireContext());
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        contentLayout.setLayoutParams(contentParams);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView tvWorkoutName = new TextView(requireContext());
        tvWorkoutName.setText(workout.getWorkoutName());
        tvWorkoutName.setTextColor(getResources().getColor(android.R.color.white));
        tvWorkoutName.setTextSize(16);
        tvWorkoutName.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView tvDate = new TextView(requireContext());
        tvDate.setText(dateFormat.format(workout.getDate()));
        tvDate.setTextColor(getResources().getColor(R.color.gray_light));
        tvDate.setTextSize(12);
        
        TextView tvDetails = new TextView(requireContext());
        tvDetails.setText(workout.getDuration() + " phút | " + workout.getCaloriesBurned() + " cal");
        tvDetails.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        tvDetails.setTextSize(14);
        
        contentLayout.addView(tvWorkoutName);
        contentLayout.addView(tvDate);
        contentLayout.addView(tvDetails);
        
        itemLayout.addView(leftBar);
        itemLayout.addView(contentLayout);
        
        layoutWorkoutHistory.addView(itemLayout);
    }

    private void setupClickListeners() {
        btnUpdateWeight.setOnClickListener(v -> showUpdateWeightDialog());
        btnUpdateMeasurements.setOnClickListener(v -> showUpdateMeasurementsDialog());
        btnViewHistory.setOnClickListener(v -> showFullHistory());
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
        executorService.execute(() -> {
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
            
            requireActivity().runOnUiThread(() -> {
                loadData(); // Reload to show sample data
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
