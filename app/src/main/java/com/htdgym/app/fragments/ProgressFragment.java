package com.htdgym.app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.htdgym.app.R;
import com.htdgym.app.utils.PremiumManager;

import java.util.ArrayList;
import java.util.List;

public class ProgressFragment extends Fragment {

    private LineChart chartWeight;
    private BarChart chartWorkouts;
    private TextView tvCurrentWeight, tvTargetWeight, tvWeightChange;
    private TextView tvTotalWorkouts, tvTotalMinutes, tvCaloriesBurned;
    private TextView tvStreak, tvBestMonth, tvAvgDuration;
    private Button btnUpgradePremium;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        
        initViews(view);
        setupCharts();
        loadStats();
        setupPremiumButton();
        
        return view;
    }

    private void initViews(View view) {
        // chartWeight = view.findViewById(R.id.chart_weight);
        // chartWorkouts = view.findViewById(R.id.chart_workouts);
        
        tvCurrentWeight = view.findViewById(R.id.tv_current_weight);
        tvTargetWeight = view.findViewById(R.id.tv_target_weight);
        tvWeightChange = view.findViewById(R.id.tv_weight_change);
        
        tvTotalWorkouts = view.findViewById(R.id.tv_total_workouts);
        btnUpgradePremium = view.findViewById(R.id.btn_upgrade_premium);
        
        // Hide premium card for premium users
        androidx.cardview.widget.CardView premiumCard = view.findViewById(R.id.card_premium_upgrade);
        if (premiumCard != null && PremiumManager.isPremiumUser(requireContext())) {
            premiumCard.setVisibility(android.view.View.GONE);
        }
        // tvTotalMinutes = view.findViewById(R.id.tv_total_minutes);
        // tvCaloriesBurned = view.findViewById(R.id.tv_calories_burned);
        
        // tvStreak = view.findViewById(R.id.tv_streak);
        // tvBestMonth = view.findViewById(R.id.tv_best_month);
        // tvAvgDuration = view.findViewById(R.id.tv_avg_duration);
    }

    private void setupCharts() {
        // setupWeightChart();
        // setupWorkoutChart();
    }

    private void setupWeightChart() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 75f));
        entries.add(new Entry(1, 74.5f));
        entries.add(new Entry(2, 74f));
        entries.add(new Entry(3, 73.5f));
        entries.add(new Entry(4, 73f));
        entries.add(new Entry(5, 72.5f));
        entries.add(new Entry(6, 72f));

        LineDataSet dataSet = new LineDataSet(entries, "Cân nặng (kg)");
        dataSet.setColor(Color.parseColor("#4CAF50"));
        dataSet.setCircleColor(Color.parseColor("#4CAF50"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#4CAF50"));
        dataSet.setFillAlpha(50);

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

    private void setupWorkoutChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 3));
        entries.add(new BarEntry(1, 5));
        entries.add(new BarEntry(2, 4));
        entries.add(new BarEntry(3, 6));
        entries.add(new BarEntry(4, 5));
        entries.add(new BarEntry(5, 7));
        entries.add(new BarEntry(6, 4));

        BarDataSet dataSet = new BarDataSet(entries, "Buổi tập");
        dataSet.setColor(Color.parseColor("#FF9800"));
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        chartWorkouts.setData(barData);
        chartWorkouts.getDescription().setEnabled(false);
        chartWorkouts.getLegend().setTextColor(Color.WHITE);
        chartWorkouts.getXAxis().setTextColor(Color.WHITE);
        chartWorkouts.getAxisLeft().setTextColor(Color.WHITE);
        chartWorkouts.getAxisRight().setEnabled(false);
        chartWorkouts.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartWorkouts.animateY(1000);
        chartWorkouts.invalidate();
    }

    private void loadStats() {
        tvCurrentWeight.setText("72.0 kg");
        tvTargetWeight.setText("70.0 kg");
        tvWeightChange.setText("-3.0 kg");
        
        tvTotalWorkouts.setText("34");
        // tvTotalMinutes.setText("1,240");
        // tvCaloriesBurned.setText("8,500");
        
        // tvStreak.setText("7 ngày");
        // tvBestMonth.setText("Tháng 1");
        // tvAvgDuration.setText("36 phút");
    }
    
    private void setupPremiumButton() {
        if (PremiumManager.isPremiumUser(requireContext())) {
            btnUpgradePremium.setVisibility(android.view.View.GONE);
        } else {
            btnUpgradePremium.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(requireContext(), 
                    com.htdgym.app.activities.PremiumActivity.class);
                startActivity(intent);
            });
        }
    }
}
