package com.htdgym.app.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.database.FoodItemDao;
import com.htdgym.app.database.MealLogDao;
import com.htdgym.app.models.FoodItem;
import com.htdgym.app.models.MealLog;
import com.htdgym.app.utils.SharedPrefManager;
import com.htdgym.app.utils.PremiumManager;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NutritionFragment extends Fragment {

    private TextView tvCaloriesConsumed, tvCaloriesGoal, tvProteinConsumed, tvProteinGoal;
    private TextView tvCarbsConsumed, tvCarbsGoal, tvFatConsumed, tvFatGoal;
    private ProgressBar pbCalories, pbProtein, pbCarbs, pbFat;
    private TextView tvWaterStatus;
    private GridLayout gridWaterGlasses;
    private Button btnResetWater, btnAddFood, btnScanFood;
    private LinearLayout layoutBreakfast, layoutLunch, layoutDinner, layoutSnack;
    
    private int waterIntake = 0;
    private final int maxWater = 8;
    private int userId;
    private int caloriesGoal = 1800;
    private int proteinGoal = 140;
    private int carbsGoal = 150;
    private int fatGoal = 60;
    
    private GymDatabase database;
    private FoodItemDao foodItemDao;
    private MealLogDao mealLogDao;
    private ExecutorService executorService;
    private PremiumManager premiumManager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        
        database = GymDatabase.getInstance(requireContext());
        foodItemDao = database.foodItemDao();
        mealLogDao = database.mealLogDao();
        executorService = Executors.newSingleThreadExecutor();
        premiumManager = PremiumManager.getInstance(requireContext());
        
        String userIdStr = SharedPrefManager.getInstance(requireContext()).getUserId();
        userId = (userIdStr != null) ? Integer.parseInt(userIdStr) : 1;
        
        initViews(view);
        setupWaterTracker();
        loadTodayData();
        setupClickListeners();
        initializeFoodDatabase();
        setupAnimations();
        
        return view;
    }

    private void initViews(View view) {
        tvCaloriesConsumed = view.findViewById(R.id.tv_calories_consumed);
        tvCaloriesGoal = view.findViewById(R.id.tv_calories_goal);
        tvProteinConsumed = view.findViewById(R.id.tv_protein_consumed);
        tvProteinGoal = view.findViewById(R.id.tv_protein_goal);
        tvCarbsConsumed = view.findViewById(R.id.tv_carbs_consumed);
        tvCarbsGoal = view.findViewById(R.id.tv_carbs_goal);
        tvFatConsumed = view.findViewById(R.id.tv_fat_consumed);
        tvFatGoal = view.findViewById(R.id.tv_fat_goal);
        
        pbCalories = view.findViewById(R.id.pb_calories);
        pbProtein = view.findViewById(R.id.pb_protein);
        pbCarbs = view.findViewById(R.id.pb_carbs);
        pbFat = view.findViewById(R.id.pb_fat);
        
        tvWaterStatus = view.findViewById(R.id.tv_water_status);
        gridWaterGlasses = view.findViewById(R.id.grid_water_glasses);
        btnResetWater = view.findViewById(R.id.btn_reset_water);
        btnAddFood = view.findViewById(R.id.btn_add_food);
        btnScanFood = view.findViewById(R.id.btn_scan_food);
        
        layoutBreakfast = view.findViewById(R.id.layout_breakfast);
        layoutLunch = view.findViewById(R.id.layout_lunch);
        layoutDinner = view.findViewById(R.id.layout_dinner);
        layoutSnack = view.findViewById(R.id.layout_snack);
        
        tvCaloriesGoal.setText(String.valueOf(caloriesGoal));
        tvProteinGoal.setText(proteinGoal + "g");
        tvCarbsGoal.setText(carbsGoal + "g");
        tvFatGoal.setText(fatGoal + "g");
        
        pbCalories.setMax(caloriesGoal);
        pbProtein.setMax(proteinGoal);
        pbCarbs.setMax(carbsGoal);
        pbFat.setMax(fatGoal);
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
        // Animate cards with staggered delay - simplified approach
        if (layoutBreakfast != null) {
            View breakfastCard = (View) layoutBreakfast.getParent().getParent();
            if (breakfastCard != null) {
                breakfastCard.setAlpha(0f);
                breakfastCard.setTranslationY(100f);
                breakfastCard.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .setStartDelay(0)
                    .start();
            }
        }
        
        if (layoutLunch != null) {
            View lunchCard = (View) layoutLunch.getParent().getParent();
            if (lunchCard != null) {
                lunchCard.setAlpha(0f);
                lunchCard.setTranslationY(100f);
                lunchCard.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .setStartDelay(150)
                    .start();
            }
        }
        
        if (layoutDinner != null) {
            View dinnerCard = (View) layoutDinner.getParent().getParent();
            if (dinnerCard != null) {
                dinnerCard.setAlpha(0f);
                dinnerCard.setTranslationY(100f);
                dinnerCard.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .setStartDelay(300)
                    .start();
            }
        }
        
        if (layoutSnack != null) {
            View snackCard = (View) layoutSnack.getParent().getParent();
            if (snackCard != null) {
                snackCard.setAlpha(0f);
                snackCard.setTranslationY(100f);
                snackCard.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .setStartDelay(450)
                    .start();
            }
        }
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
        params.height = 100;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(12, 12, 12, 12);
        waterGlass.setLayoutParams(params);
        
        waterGlass.setText("💧");
        waterGlass.setTextSize(28);
        waterGlass.setGravity(android.view.Gravity.CENTER);
        waterGlass.setBackgroundResource(R.drawable.card_shadow);
        waterGlass.setBackgroundTintList(getResources().getColorStateList(R.color.gray_light));
        waterGlass.setElevation(8f);
        
        waterGlass.setOnClickListener(v -> {
            // Add click animation
            v.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction(() -> {
                    v.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start();
                    drinkWater(number);
                })
                .start();
        });
        
        return waterGlass;
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
            showWaterCompletionDialog();
        } else if (waterIntake > 0 && waterIntake % 2 == 0) {
            Toast.makeText(requireContext(), "💧 Tuyệt vời! Đã uống " + waterIntake + " cốc nước!", 
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showWaterCompletionDialog() {
        new AlertDialog.Builder(requireContext())
            .setTitle("🎉 Chúc mừng!")
            .setMessage("Bạn đã hoàn thành mục tiêu uống nước hôm nay!\n\n" +
                "💧 8 cốc nước = 2000ml\n" +
                "🏆 Điều này rất tốt cho sức khỏe của bạn!")
            .setPositiveButton("Tuyệt vời! 🎯", null)
            .setIcon(R.drawable.ic_check_circle)
            .show();
    }

    private void updateWaterGlasses() {
        for (int i = 0; i < gridWaterGlasses.getChildCount(); i++) {
            TextView waterGlass = (TextView) gridWaterGlasses.getChildAt(i);
            int glassNumber = i + 1;
            
            if (glassNumber <= waterIntake) {
                waterGlass.setBackgroundTintList(getResources().getColorStateList(R.color.green_primary));
            } else {
                waterGlass.setBackgroundTintList(getResources().getColorStateList(R.color.gray_dark));
            }
        }
    }

    private void updateWaterStatus() {
        int ml = waterIntake * 250;
        int totalMl = maxWater * 250;
        tvWaterStatus.setText(ml + "ml / " + totalMl + "ml (" + waterIntake + "/" + maxWater + " cốc)");
    }

    private void setupClickListeners() {
        btnResetWater.setOnClickListener(v -> {
            waterIntake = 0;
            updateWaterGlasses();
            updateWaterStatus();
            Toast.makeText(requireContext(), "🔄 Đã đặt lại bộ đếm nước", Toast.LENGTH_SHORT).show();
        });
        
        btnAddFood.setOnClickListener(v -> showAddFoodDialog());
        
        btnScanFood.setOnClickListener(v -> {
            if (premiumManager.isPremiumUser()) {
                showBarcodeScanner();
            } else {
                showPremiumRequiredDialog("Quét Barcode");
            }
        });
    }

    private void showBarcodeScanner() {
        Toast.makeText(requireContext(), "📷 Tính năng quét barcode sẽ được cập nhật sớm!", 
                Toast.LENGTH_LONG).show();
        // TODO: Implement barcode scanner functionality
    }

    private void showPremiumRequiredDialog(String feature) {
        new AlertDialog.Builder(requireContext())
            .setTitle("🔒 Tính năng Premium")
            .setMessage("Tính năng \"" + feature + "\" chỉ dành cho người dùng Premium.\n\n" +
                "Nâng cấp để sử dụng:\n" +
                "📷 Quét barcode thực phẩm\n" +
                "🥗 Gợi ý thực đơn cá nhân hóa\n" +
                "📊 Phân tích dinh dưỡng chi tiết\n" +
                "🎯 Mục tiêu dinh dưỡng tùy chỉnh")
            .setPositiveButton("💎 Nâng cấp Premium", (dialog, which) -> {
                Intent intent = new Intent(requireContext(), com.htdgym.app.activities.PremiumActivity.class);
                startActivity(intent);
            })
            .setNegativeButton("Để sau", null)
            .setIcon(R.drawable.ic_premium_crown)
            .show();
    }

    private void loadTodayData() {
        executorService.execute(() -> {
            Date today = getTodayDate();
            
            Integer totalCal = mealLogDao.getTotalCaloriesByDate(userId, today);
            Float totalPro = mealLogDao.getTotalProteinByDate(userId, today);
            Float totalCarb = mealLogDao.getTotalCarbsByDate(userId, today);
            Float totalFt = mealLogDao.getTotalFatByDate(userId, today);
            
            int calories = totalCal != null ? totalCal : 0;
            float protein = totalPro != null ? totalPro : 0;
            float carbs = totalCarb != null ? totalCarb : 0;
            float fat = totalFt != null ? totalFt : 0;
            
            requireActivity().runOnUiThread(() -> {
                updateMacros(calories, protein, carbs, fat);
                loadMealLogs();
            });
        });
    }

    private void updateMacros(int calories, float protein, float carbs, float fat) {
        tvCaloriesConsumed.setText(String.valueOf(calories));
        tvProteinConsumed.setText(String.format("%.0f", protein) + "g");
        tvCarbsConsumed.setText(String.format("%.0f", carbs) + "g");
        tvFatConsumed.setText(String.format("%.0f", fat) + "g");
        
        pbCalories.setProgress(calories);
        pbProtein.setProgress((int) protein);
        pbCarbs.setProgress((int) carbs);
        pbFat.setProgress((int) fat);
    }

    private void loadMealLogs() {
        executorService.execute(() -> {
            Date today = getTodayDate();
            
            List<MealLog> breakfast = mealLogDao.getMealLogsByTypeAndDate(userId, "Breakfast", today);
            List<MealLog> lunch = mealLogDao.getMealLogsByTypeAndDate(userId, "Lunch", today);
            List<MealLog> dinner = mealLogDao.getMealLogsByTypeAndDate(userId, "Dinner", today);
            List<MealLog> snack = mealLogDao.getMealLogsByTypeAndDate(userId, "Snack", today);
            
            requireActivity().runOnUiThread(() -> {
                displayMealLogs(layoutBreakfast, breakfast, "Breakfast");
                displayMealLogs(layoutLunch, lunch, "Lunch");
                displayMealLogs(layoutDinner, dinner, "Dinner");
                displayMealLogs(layoutSnack, snack, "Snack");
            });
        });
    }

    private void displayMealLogs(LinearLayout layout, List<MealLog> meals, String mealType) {
        layout.removeAllViews();
        
        if (meals.isEmpty()) {
            TextView emptyText = new TextView(requireContext());
            emptyText.setText("Chưa có món ăn nào");
            emptyText.setTextColor(getResources().getColor(R.color.gray_light));
            emptyText.setPadding(16, 8, 16, 8);
            layout.addView(emptyText);
            return;
        }
        
        for (MealLog meal : meals) {
            executorService.execute(() -> {
                FoodItem food = foodItemDao.getFoodById(meal.getFoodItemId());
                requireActivity().runOnUiThread(() -> {
                    if (food != null) {
                        addMealItem(layout, food, meal);
                    }
                });
            });
        }
    }

    private void addMealItem(LinearLayout layout, FoodItem food, MealLog meal) {
        androidx.cardview.widget.CardView itemCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 12);
        itemCard.setLayoutParams(cardParams);
        itemCard.setRadius(16f);
        itemCard.setCardElevation(6f);
        itemCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        
        LinearLayout itemLayout = new LinearLayout(requireContext());
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(20, 16, 20, 16);
        itemLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        // Food emoji/icon
        TextView foodIcon = new TextView(requireContext());
        foodIcon.setText(getFoodEmoji(food.getName()));
        foodIcon.setTextSize(24);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        iconParams.setMargins(0, 0, 16, 0);
        foodIcon.setLayoutParams(iconParams);
        
        LinearLayout textLayout = new LinearLayout(requireContext());
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textLayout.setLayoutParams(textParams);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView tvName = new TextView(requireContext());
        tvName.setText(food.getName());
        tvName.setTextColor(getResources().getColor(R.color.text_primary));
        tvName.setTextSize(16);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView tvMacros = new TextView(requireContext());
        tvMacros.setText("P: " + String.format("%.0f", meal.getTotalProtein()) + "g | " +
                "C: " + String.format("%.0f", meal.getTotalCarbs()) + "g | " +
                "F: " + String.format("%.0f", meal.getTotalFat()) + "g");
        tvMacros.setTextColor(getResources().getColor(R.color.text_secondary));
        tvMacros.setTextSize(12);
        
        textLayout.addView(tvName);
        textLayout.addView(tvMacros);
        
        // Calories badge
        androidx.cardview.widget.CardView caloriesBadge = new androidx.cardview.widget.CardView(requireContext());
        caloriesBadge.setRadius(12f);
        caloriesBadge.setCardElevation(4f);
        caloriesBadge.setCardBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        
        TextView tvCalories = new TextView(requireContext());
        tvCalories.setText(meal.getTotalCalories() + " cal");
        tvCalories.setTextColor(getResources().getColor(android.R.color.white));
        tvCalories.setTextSize(14);
        tvCalories.setTypeface(null, android.graphics.Typeface.BOLD);
        tvCalories.setPadding(12, 8, 12, 8);
        
        caloriesBadge.addView(tvCalories);
        
        itemLayout.addView(foodIcon);
        itemLayout.addView(textLayout);
        itemLayout.addView(caloriesBadge);
        
        itemCard.addView(itemLayout);
        layout.addView(itemCard);
        
        // Add entrance animation
        itemCard.setAlpha(0f);
        itemCard.setTranslationY(50f);
        itemCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(300)
            .start();
    }

    private String getFoodEmoji(String foodName) {
        String name = foodName.toLowerCase();
        if (name.contains("phở") || name.contains("bún")) return "🍜";
        if (name.contains("bánh mì")) return "🥖";
        if (name.contains("xôi")) return "🍚";
        if (name.contains("cơm")) return "🍛";
        if (name.contains("gà") || name.contains("chicken")) return "🍗";
        if (name.contains("cá") || name.contains("fish")) return "🐟";
        if (name.contains("trứng") || name.contains("egg")) return "🥚";
        if (name.contains("chuối") || name.contains("banana")) return "🍌";
        if (name.contains("táo") || name.contains("apple")) return "🍎";
        if (name.contains("sữa") || name.contains("milk")) return "🥛";
        if (name.contains("yến mạch") || name.contains("oat")) return "🥣";
        if (name.contains("khoai")) return "🍠";
        return "🍽️";
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn loại bữa ăn");
        
        String[] mealTypes = {"Bữa sáng", "Bữa trưa", "Bữa tối", "Ăn vặt"};
        String[] mealTypesEn = {"Breakfast", "Lunch", "Dinner", "Snack"};
        
        builder.setItems(mealTypes, (dialog, which) -> {
            showFoodSelectionDialog(mealTypesEn[which]);
        });
        
        builder.show();
    }

    private void showFoodSelectionDialog(String mealType) {
        executorService.execute(() -> {
            List<FoodItem> foods = foodItemDao.getAllFoods();
            
            requireActivity().runOnUiThread(() -> {
                if (foods.isEmpty()) {
                    Toast.makeText(requireContext(), "Chưa có món ăn trong database", 
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Chọn món ăn");
                
                String[] foodNames = new String[foods.size()];
                for (int i = 0; i < foods.size(); i++) {
                    FoodItem food = foods.get(i);
                    foodNames[i] = food.getName() + " (" + food.getCalories() + " cal)";
                }
                
                builder.setItems(foodNames, (dialog, which) -> {
                    FoodItem selectedFood = foods.get(which);
                    addMealLog(selectedFood, mealType);
                });
                
                builder.setNegativeButton("Hủy", null);
                builder.show();
            });
        });
    }

    private void addMealLog(FoodItem food, String mealType) {
        executorService.execute(() -> {
            MealLog mealLog = new MealLog(
                    userId,
                    food.getId(),
                    mealType,
                    getTodayDate(),
                    1.0f,
                    food.getCalories(),
                    food.getProtein(),
                    food.getCarbs(),
                    food.getFat()
            );
            
            mealLogDao.insert(mealLog);
            
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Đã thêm " + food.getName(), 
                        Toast.LENGTH_SHORT).show();
                loadTodayData();
            });
        });
    }

    private void initializeFoodDatabase() {
        executorService.execute(() -> {
            List<FoodItem> existingFoods = foodItemDao.getAllFoods();
            if (!existingFoods.isEmpty()) {
                return;
            }
            
            // Vietnamese foods
            foodItemDao.insert(new FoodItem("Phở bò", "Breakfast", 350, 20, 50, 8, "1 tô"));
            foodItemDao.insert(new FoodItem("Bánh mì thịt", "Breakfast", 280, 15, 35, 10, "1 ổ"));
            foodItemDao.insert(new FoodItem("Xôi gà", "Breakfast", 400, 18, 60, 12, "1 phần"));
            foodItemDao.insert(new FoodItem("Bún chả", "Lunch", 450, 25, 55, 15, "1 phần"));
            foodItemDao.insert(new FoodItem("Cơm gà", "Lunch", 500, 30, 65, 12, "1 phần"));
            foodItemDao.insert(new FoodItem("Cơm tấm", "Lunch", 550, 28, 70, 18, "1 phần"));
            foodItemDao.insert(new FoodItem("Ức gà nướng", "Dinner", 165, 31, 0, 3.6f, "100g"));
            foodItemDao.insert(new FoodItem("Cá hồi nướng", "Dinner", 206, 22, 0, 13, "100g"));
            foodItemDao.insert(new FoodItem("Cơm gạo lứt", "Lunch", 110, 2.6f, 23, 0.9f, "100g"));
            foodItemDao.insert(new FoodItem("Khoai lang", "Snack", 86, 1.6f, 20, 0.1f, "100g"));
            foodItemDao.insert(new FoodItem("Trứng luộc", "Breakfast", 155, 13, 1.1f, 11, "2 quả"));
            foodItemDao.insert(new FoodItem("Yến mạch", "Breakfast", 389, 17, 66, 7, "100g"));
            foodItemDao.insert(new FoodItem("Chuối", "Snack", 89, 1.1f, 23, 0.3f, "1 quả"));
            foodItemDao.insert(new FoodItem("Táo", "Snack", 52, 0.3f, 14, 0.2f, "1 quả"));
            foodItemDao.insert(new FoodItem("Sữa chua", "Snack", 59, 3.5f, 4.7f, 3.3f, "100g"));
        });
    }

    private Date getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
