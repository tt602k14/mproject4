package com.htdgym.app.utils;

import com.htdgym.app.models.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodDatabaseHelper {

    public static List<Food> getVietnameseFoods() {
        List<Food> foods = new ArrayList<>();
        
        // Cơm, bánh mì
        foods.add(new Food("Cơm trắng", 130, "Tinh bột", 2.7, 28.0, 0.3));
        foods.add(new Food("Bánh mì", 265, "Tinh bột", 9.0, 49.0, 3.2));
        foods.add(new Food("Phở bò", 215, "Món chính", 15.0, 25.0, 5.5));
        foods.add(new Food("Bún bò Huế", 180, "Món chính", 12.0, 20.0, 6.0));
        foods.add(new Food("Bánh cuốn", 160, "Món chính", 8.0, 25.0, 3.0));
        
        // Thịt, cá
        foods.add(new Food("Thịt bò", 250, "Protein", 26.0, 0.0, 15.0));
        foods.add(new Food("Thịt heo", 242, "Protein", 27.0, 0.0, 14.0));
        foods.add(new Food("Thịt gà", 165, "Protein", 31.0, 0.0, 3.6));
        foods.add(new Food("Cá thu", 184, "Protein", 25.0, 0.0, 8.1));
        foods.add(new Food("Cá hồi", 208, "Protein", 20.0, 0.0, 13.0));
        foods.add(new Food("Tôm", 99, "Protein", 18.0, 0.9, 1.4));
        foods.add(new Food("Cua", 97, "Protein", 19.0, 0.0, 1.8));
        
        // Rau củ
        foods.add(new Food("Rau muống", 19, "Rau xanh", 2.6, 2.9, 0.2));
        foods.add(new Food("Cải thảo", 16, "Rau xanh", 1.5, 3.2, 0.2));
        foods.add(new Food("Cà rót", 25, "Rau củ", 1.0, 6.0, 0.2));
        foods.add(new Food("Cà chua", 18, "Rau củ", 0.9, 3.9, 0.2));
        foods.add(new Food("Dưa chuột", 16, "Rau củ", 0.7, 4.0, 0.1));
        foods.add(new Food("Khoai tây", 77, "Rau củ", 2.0, 17.0, 0.1));
        foods.add(new Food("Khoai lang", 86, "Rau củ", 1.6, 20.0, 0.1));
        
        // Trái cây
        foods.add(new Food("Chuối", 89, "Trái cây", 1.1, 23.0, 0.3));
        foods.add(new Food("Táo", 52, "Trái cây", 0.3, 14.0, 0.2));
        foods.add(new Food("Cam", 47, "Trái cây", 0.9, 12.0, 0.1));
        foods.add(new Food("Xoài", 60, "Trái cây", 0.8, 15.0, 0.4));
        foods.add(new Food("Đu đủ", 43, "Trái cây", 0.5, 11.0, 0.3));
        foods.add(new Food("Dưa hấu", 30, "Trái cây", 0.6, 8.0, 0.2));
        
        // Đồ uống
        foods.add(new Food("Sữa tươi", 42, "Đồ uống", 3.4, 5.0, 1.0));
        foods.add(new Food("Cà phê đen", 2, "Đồ uống", 0.3, 0.0, 0.0));
        foods.add(new Food("Trà xanh", 1, "Đồ uống", 0.0, 0.0, 0.0));
        foods.add(new Food("Nước dừa", 19, "Đồ uống", 0.7, 3.7, 0.2));
        
        // Đậu, hạt
        foods.add(new Food("Đậu phụ", 76, "Protein", 8.0, 1.9, 4.8));
        foods.add(new Food("Đậu xanh", 347, "Đậu hạt", 23.0, 59.0, 1.2));
        foods.add(new Food("Đậu đỏ", 333, "Đậu hạt", 20.0, 61.0, 1.1));
        foods.add(new Food("Lạc", 567, "Đậu hạt", 26.0, 16.0, 49.0));
        
        // Gia vị, dầu ăn
        foods.add(new Food("Dầu ăn", 884, "Chất béo", 0.0, 0.0, 100.0));
        foods.add(new Food("Nước mắm", 10, "Gia vị", 1.5, 0.6, 0.0));
        foods.add(new Food("Đường", 387, "Đường", 0.0, 100.0, 0.0));
        
        // Món ăn vặt
        foods.add(new Food("Bánh tráng nướng", 350, "Snack", 12.0, 70.0, 2.0));
        foods.add(new Food("Chè đậu xanh", 120, "Tráng miệng", 4.0, 25.0, 1.0));
        foods.add(new Food("Bánh flan", 150, "Tráng miệng", 4.0, 22.0, 5.0));
        
        return foods;
    }
    
    public static List<Food> searchFoods(String query) {
        List<Food> allFoods = getVietnameseFoods();
        List<Food> results = new ArrayList<>();
        
        for (Food food : allFoods) {
            if (food.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(food);
            }
        }
        
        return results;
    }
    
    public static List<Food> getFoodsByCategory(String category) {
        List<Food> allFoods = getVietnameseFoods();
        List<Food> results = new ArrayList<>();
        
        for (Food food : allFoods) {
            if (food.getCategory().equals(category)) {
                results.add(food);
            }
        }
        
        return results;
    }
}