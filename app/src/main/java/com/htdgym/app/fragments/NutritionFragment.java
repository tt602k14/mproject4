package com.htdgym.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.htdgym.app.R;
import com.htdgym.app.utils.PremiumManager;

public class NutritionFragment extends Fragment {

    private Spinner spinnerNutritionGoal;
    private TextView tvDailyCalories, tvProteinGoal, tvCarbGoal, tvFatGoal;
    private TextView tvWaterStatus;
    private Button btnGenerateMealPlan, btnResetWater;
    private GridLayout gridWaterGlasses;
    private LinearLayout layoutMeals;
    
    private int waterIntake = 3;
    private final int maxWater = 8;
    
    private String[] nutritionGoals = {"Giam mo", "Tang co", "Giu dang"};
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        
        initViews(view);
        setupSpinner();
        setupWaterTracker();
        setupMeals();
        setupClickListeners();
        
        // Set initial nutrition values (default to "Giảm mỡ")
        updateNutritionGoals(0);
        
        return view;
    }

    private void initViews(View view) {
        spinnerNutritionGoal = view.findViewById(R.id.spinner_nutrition_goal);
        tvDailyCalories = view.findViewById(R.id.tv_daily_calories);
        tvProteinGoal = view.findViewById(R.id.tv_protein_goal);
        tvCarbGoal = view.findViewById(R.id.tv_carb_goal);
        tvFatGoal = view.findViewById(R.id.tv_fat_goal);
        tvWaterStatus = view.findViewById(R.id.tv_water_status);
        btnGenerateMealPlan = view.findViewById(R.id.btn_generate_meal_plan);
        btnResetWater = view.findViewById(R.id.btn_reset_water);
        gridWaterGlasses = view.findViewById(R.id.grid_water_glasses);
        layoutMeals = view.findViewById(R.id.layout_meals);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.spinner_item, nutritionGoals);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerNutritionGoal.setAdapter(adapter);
        
        spinnerNutritionGoal.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                // Set text color to black for selected item
                if (view != null) {
                    ((TextView) view).setTextColor(0xFF1A1A1A);
                }
                updateNutritionGoals(position);
            }
            
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void setupWaterTracker() {
        for (int i = 1; i <= maxWater; i++) {
            TextView waterGlass = createWaterGlass(i);
            gridWaterGlasses.addView(waterGlass);
        }
        updateWaterStatus();
    }

    private TextView createWaterGlass(int number) {
        TextView waterGlass = new TextView(requireContext());
        
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = (int) (60 * getResources().getDisplayMetrics().density);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(
                (int) (4 * getResources().getDisplayMetrics().density), 
                (int) (4 * getResources().getDisplayMetrics().density), 
                (int) (4 * getResources().getDisplayMetrics().density), 
                (int) (4 * getResources().getDisplayMetrics().density));
        waterGlass.setLayoutParams(params);
        
        waterGlass.setText(String.valueOf(number));
        waterGlass.setTextColor(0xFFFFFFFF);
        waterGlass.setTextSize(16);
        waterGlass.setGravity(android.view.Gravity.CENTER);
        waterGlass.setTypeface(null, android.graphics.Typeface.BOLD);
        
        // Create rounded background
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setCornerRadius(12 * getResources().getDisplayMetrics().density);
        
        if (number <= waterIntake) {
            drawable.setColor(0xFF6FCF97);
        } else {
            drawable.setColor(0xFFE0E0E0);
        }
        
        waterGlass.setBackground(drawable);
        waterGlass.setOnClickListener(v -> drinkWater(number));
        
        return waterGlass;
    }

    private void setupMeals() {
        addMealCard("Bua sang (07:00)", "Yen mach, trung luoc, sinh to chuoi", 
                   "450 calo  30g protein", "Da an", true);
        
        addMealCard("Bua trua (12:30)", "Uc ga nuong, com gao lut, rau cu hap", 
                   "650 calo  45g protein", "Ghi nhan", false);
        
        addMealCard("Bua toi (19:00)", "Ca hoi, khoai lang, salad rau", 
                   "550 calo  40g protein", "Ghi nhan", false);
    }

    private void addMealCard(String mealTime, String description, String macros, 
                           String buttonText, boolean isCompleted) {
        
        androidx.cardview.widget.CardView mealCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (12 * getResources().getDisplayMetrics().density));
        mealCard.setLayoutParams(cardParams);
        mealCard.setRadius(12 * getResources().getDisplayMetrics().density);
        mealCard.setCardElevation(2 * getResources().getDisplayMetrics().density);
        mealCard.setCardBackgroundColor(0xFFF8F9FA);
        
        LinearLayout mealLayout = new LinearLayout(requireContext());
        mealLayout.setOrientation(LinearLayout.HORIZONTAL);
        mealLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density));
        mealLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        View leftBar = new View(requireContext());
        LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(
                (int) (4 * getResources().getDisplayMetrics().density), 
                LinearLayout.LayoutParams.MATCH_PARENT);
        barParams.setMargins(0, 0, (int) (12 * getResources().getDisplayMetrics().density), 0);
        leftBar.setLayoutParams(barParams);
        leftBar.setBackgroundColor(isCompleted ? 0xFF6FCF97 : 0xFFE0E0E0);
        
        LinearLayout contentLayout = new LinearLayout(requireContext());
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        contentLayout.setLayoutParams(contentParams);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView tvMealTime = new TextView(requireContext());
        tvMealTime.setText(mealTime);
        tvMealTime.setTextColor(isCompleted ? 0xFF6FCF97 : 0xFFFFA726);
        tvMealTime.setTextSize(16);
        tvMealTime.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView tvDescription = new TextView(requireContext());
        tvDescription.setText(description);
        tvDescription.setTextColor(0xFF1A1A1A);
        tvDescription.setTextSize(14);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        descParams.setMargins(0, (int) (4 * getResources().getDisplayMetrics().density), 0, (int) (4 * getResources().getDisplayMetrics().density));
        tvDescription.setLayoutParams(descParams);
        
        TextView tvMacros = new TextView(requireContext());
        tvMacros.setText(macros);
        tvMacros.setTextColor(0xFF999999);
        tvMacros.setTextSize(12);
        
        contentLayout.addView(tvMealTime);
        contentLayout.addView(tvDescription);
        contentLayout.addView(tvMacros);
        
        Button btnMeal = new Button(requireContext());
        btnMeal.setText(buttonText);
        btnMeal.setTextSize(12);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) (36 * getResources().getDisplayMetrics().density));
        btnMeal.setLayoutParams(btnParams);
        
        if (isCompleted) {
            btnMeal.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF6FCF97));
            btnMeal.setTextColor(0xFFFFFFFF);
        } else {
            btnMeal.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE0E0E0));
            btnMeal.setTextColor(0xFF6FCF97);
        }
        btnMeal.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Đã ghi nhận " + mealTime, Toast.LENGTH_SHORT).show();
        });
        
        mealLayout.addView(leftBar);
        mealLayout.addView(contentLayout);
        mealLayout.addView(btnMeal);
        
        mealCard.addView(mealLayout);
        layoutMeals.addView(mealCard);
    }

    private void setupClickListeners() {
        btnGenerateMealPlan.setOnClickListener(v -> generateMealPlan());
        btnResetWater.setOnClickListener(v -> resetWater());
    }

    private void updateNutritionGoals(int goalIndex) {
        switch (goalIndex) {
            case 0:
                tvDailyCalories.setText("1,800");
                tvProteinGoal.setText("140g");
                tvCarbGoal.setText("150g");
                tvFatGoal.setText("60g");
                break;
            case 1:
                tvDailyCalories.setText("2,800");
                tvProteinGoal.setText("180g");
                tvCarbGoal.setText("300g");
                tvFatGoal.setText("80g");
                break;
            case 2:
                tvDailyCalories.setText("2,300");
                tvProteinGoal.setText("160g");
                tvCarbGoal.setText("250g");
                tvFatGoal.setText("70g");
                break;
        }
    }

    private void drinkWater(int glassNumber) {
        if (glassNumber <= waterIntake) {
            waterIntake = glassNumber - 1;
        } else {
            waterIntake = glassNumber;
        }
        
        updateWaterGlasses();
        updateWaterStatus();
        
        if (waterIntake == maxWater) {
            Toast.makeText(requireContext(), "Tuyet voi! Ban da uong du nuoc hom nay!", 
                    Toast.LENGTH_LONG).show();
        }
    }

    private void updateWaterGlasses() {
        for (int i = 0; i < gridWaterGlasses.getChildCount(); i++) {
            TextView waterGlass = (TextView) gridWaterGlasses.getChildAt(i);
            int glassNumber = i + 1;
            
            android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
            drawable.setCornerRadius(12 * getResources().getDisplayMetrics().density);
            
            if (glassNumber <= waterIntake) {
                drawable.setColor(0xFF6FCF97);
            } else {
                drawable.setColor(0xFFE0E0E0);
            }
            
            waterGlass.setBackground(drawable);
        }
    }

    private void updateWaterStatus() {
        tvWaterStatus.setText("Da uong: " + waterIntake + "/" + maxWater + " coc");
    }

    private void resetWater() {
        waterIntake = 0;
        updateWaterGlasses();
        updateWaterStatus();
        Toast.makeText(requireContext(), "Da dat lai bo dem nuoc", Toast.LENGTH_SHORT).show();
    }

    private void generateMealPlan() {
        if (!PremiumManager.isPremiumUser(requireContext())) {
            PremiumManager.showPremiumDialog(requireContext(), "Tạo thực đơn tự động");
            return;
        }
        
        String goal = nutritionGoals[spinnerNutritionGoal.getSelectedItemPosition()];
        Toast.makeText(requireContext(), "Đã tạo thực đơn " + goal.toLowerCase(), 
                Toast.LENGTH_SHORT).show();
    }
}
