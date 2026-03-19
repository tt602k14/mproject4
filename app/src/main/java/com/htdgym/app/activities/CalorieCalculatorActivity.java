package com.htdgym.app.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.models.Food;
import com.htdgym.app.utils.FoodDatabaseHelper;

import java.util.List;

public class CalorieCalculatorActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etAge, etWeight, etHeight, etFoodSearch;
    private RadioGroup rgGender, rgActivityLevel;
    private TextView tvBMR, tvTDEE, tvRecommendation;
    private Button btnCalculate, btnSearchFood;
    private RecyclerView recyclerFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_calculator);

        initViews();
        setupClickListeners();
        loadFoods();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etAge = findViewById(R.id.et_age);
        etWeight = findViewById(R.id.et_weight);
        etHeight = findViewById(R.id.et_height);
        etFoodSearch = findViewById(R.id.et_food_search);
        rgGender = findViewById(R.id.rg_gender);
        rgActivityLevel = findViewById(R.id.rg_activity_level);
        tvBMR = findViewById(R.id.tv_bmr);
        tvTDEE = findViewById(R.id.tv_tdee);
        tvRecommendation = findViewById(R.id.tv_recommendation);
        btnCalculate = findViewById(R.id.btn_calculate);
        btnSearchFood = findViewById(R.id.btn_search_food);
        recyclerFoods = findViewById(R.id.recycler_foods);
        
        recyclerFoods.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnCalculate.setOnClickListener(v -> calculateCalories());
        
        btnSearchFood.setOnClickListener(v -> searchFoods());
    }

    private void loadFoods() {
        List<Food> foods = FoodDatabaseHelper.getVietnameseFoods();
        // Set adapter here when created
        Toast.makeText(this, "Đã tải " + foods.size() + " thực phẩm", Toast.LENGTH_SHORT).show();
    }

    private void searchFoods() {
        String query = etFoodSearch.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            loadFoods();
            return;
        }
        
        List<Food> results = FoodDatabaseHelper.searchFoods(query);
        // Update adapter here
        Toast.makeText(this, "Tìm thấy " + results.size() + " kết quả", Toast.LENGTH_SHORT).show();
    }

    private void calculateCalories() {
        String ageStr = etAge.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();
        
        if (TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(heightStr)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            int age = Integer.parseInt(ageStr);
            double weight = Double.parseDouble(weightStr);
            double height = Double.parseDouble(heightStr);
            
            // Check gender
            boolean isMale = rgGender.getCheckedRadioButtonId() == R.id.rb_male;
            
            // Calculate BMR using Mifflin-St Jeor Equation
            double bmr;
            if (isMale) {
                bmr = 10 * weight + 6.25 * height - 5 * age + 5;
            } else {
                bmr = 10 * weight + 6.25 * height - 5 * age - 161;
            }
            
            // Calculate TDEE based on activity level
            double activityMultiplier = getActivityMultiplier();
            double tdee = bmr * activityMultiplier;
            
            // Display results
            tvBMR.setText(String.format("%.0f calo/ngày", bmr));
            tvTDEE.setText(String.format("%.0f calo/ngày", tdee));
            
            // Provide recommendations
            String recommendation = getRecommendation(tdee);
            tvRecommendation.setText(recommendation);
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private double getActivityMultiplier() {
        int checkedId = rgActivityLevel.getCheckedRadioButtonId();
        
        if (checkedId == R.id.rb_sedentary) {
            return 1.2; // Ít vận động
        } else if (checkedId == R.id.rb_light) {
            return 1.375; // Vận động nhẹ
        } else if (checkedId == R.id.rb_moderate) {
            return 1.55; // Vận động vừa
        } else if (checkedId == R.id.rb_active) {
            return 1.725; // Vận động nhiều
        } else {
            return 1.9; // Vận động rất nhiều
        }
    }

    private String getRecommendation(double tdee) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("🎯 Khuyến nghị:\n\n");
        sb.append("• Giảm cân: ").append(String.format("%.0f", tdee - 500)).append(" calo/ngày\n");
        sb.append("• Duy trì: ").append(String.format("%.0f", tdee)).append(" calo/ngày\n");
        sb.append("• Tăng cân: ").append(String.format("%.0f", tdee + 500)).append(" calo/ngày\n\n");
        
        sb.append("💡 Lưu ý:\n");
        sb.append("• Ăn đủ protein: 1.6-2.2g/kg cân nặng\n");
        sb.append("• Uống đủ nước: 35ml/kg cân nặng\n");
        sb.append("• Ăn nhiều rau xanh và trái cây");
        
        return sb.toString();
    }
}