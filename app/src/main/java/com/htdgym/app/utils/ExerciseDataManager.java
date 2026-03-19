package com.htdgym.app.utils;

import com.htdgym.app.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDataManager {
    
    /**
     * Get exercises for chest workouts with video URLs
     */
    public static List<Exercise> getChestExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Hít đất cơ bản", "chest", "4×15", "60s", "#FF6B6B", "Dễ", "https://youtu.be/-R5sH2iG9Gw"));
        exercises.add(new Exercise("Hít đất nghiêng", "chest", "3×12", "45s", "#FF6B6B", "Dễ", "https://youtu.be/4dF1DOWzf20"));
        exercises.add(new Exercise("Hít đất rộng", "chest", "3×12", "45s", "#FF6B6B", "Trung bình", "https://youtu.be/5Qv2T8VusME"));
        exercises.add(new Exercise("Hít đất kim cương", "chest", "3×10", "60s", "#FF6B6B", "Khó", "https://youtu.be/J0DnG1_S92I"));
        exercises.add(new Exercise("Hít đất xuống dốc", "chest", "3×12", "45s", "#FF6B6B", "Trung bình", "https://youtu.be/SKPab2YC8BE"));
        exercises.add(new Exercise("Chống đẩy ngực", "chest", "3×10", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/2z8JmcrW-As"));
        
        return exercises;
    }
    
    /**
     * Get exercises for shoulder workouts with video URLs
     */
    public static List<Exercise> getShoulderExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Hít đất vai", "shoulder", "4×12", "60s", "#4ECDC4", "Trung bình", "https://youtu.be/d1HZBdD0idE"));
        exercises.add(new Exercise("Chạm vai", "shoulder", "3×20", "30s", "#4ECDC4", "Dễ", "https://youtu.be/1We3zKXj_uA"));
        exercises.add(new Exercise("Vòng tay", "shoulder", "3×30s", "30s", "#4ECDC4", "Dễ", "https://youtu.be/5Qv2T8VusME"));
        exercises.add(new Exercise("Nâng tay ngang", "shoulder", "3×15", "45s", "#4ECDC4", "Trung bình", "https://youtu.be/3VcKaXpzqRo"));
        exercises.add(new Exercise("Nâng tay trước", "shoulder", "3×15", "45s", "#4ECDC4", "Trung bình", "https://youtu.be/qM5X-_FfCQE"));
        exercises.add(new Exercise("Đẩy vai", "shoulder", "4×12", "60s", "#4ECDC4", "Khó", "https://youtu.be/qEwKCR5JCog"));
        
        return exercises;
    }
    
    /**
     * Get exercises for legs workouts with video URLs
     */
    public static List<Exercise> getLegsExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Gánh đùi", "legs", "4×20", "60s", "#FFA726", "Dễ", "https://youtu.be/XtoO9YwNEqA"));
        exercises.add(new Exercise("Chùng chân", "legs", "3×15", "45s", "#FFA726", "Trung bình", "https://youtu.be/QOVaHwm-Q6U"));
        exercises.add(new Exercise("Gánh đùi nhảy", "legs", "3×15", "60s", "#FFA726", "Khó", "https://youtu.be/A-cFYWvaHr0"));
        exercises.add(new Exercise("Ngồi tường", "legs", "3×45s", "60s", "#FFA726", "Trung bình", "https://youtu.be/y-wV4Venusw"));
        exercises.add(new Exercise("Nâng bắp chân", "legs", "4×20", "30s", "#FFA726", "Dễ", "https://youtu.be/gwLzBJYoWlI"));
        exercises.add(new Exercise("Chùng chân ngang", "legs", "3×12", "45s", "#FFA726", "Trung bình", "https://youtu.be/8pl4hWllMPQ"));
        exercises.add(new Exercise("Gánh đùi Bulgaria", "legs", "3×12", "60s", "#FFA726", "Khó", "https://youtu.be/2C-uStzPJWY"));
        exercises.add(new Exercise("Nâng mông", "legs", "4×15", "45s", "#FFA726", "Dễ", "https://youtu.be/wPM8icPu6H8"));
        
        return exercises;
    }
    
    /**
     * Get exercises for abs workouts with video URLs
     */
    public static List<Exercise> getAbsExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Gập bụng", "abs", "4×20", "30s", "#9C27B0", "Dễ", "https://youtu.be/Y8VflnViz78"));
        exercises.add(new Exercise("Plank cơ bản", "abs", "3×60s", "60s", "#9C27B0", "Trung bình", "https://youtu.be/pSHjTRCQxIw"));
        exercises.add(new Exercise("Gập bụng xe đạp", "abs", "3×20", "30s", "#9C27B0", "Trung bình", "https://youtu.be/9FGilxCbdz8"));
        exercises.add(new Exercise("Nâng chân", "abs", "3×15", "45s", "#9C27B0", "Trung bình", "https://youtu.be/JB2oyawG9KI"));
        exercises.add(new Exercise("Xoay người Nga", "abs", "3×30", "30s", "#9C27B0", "Khó", "https://youtu.be/wkD8rjkodUI"));
        exercises.add(new Exercise("Leo núi", "abs", "3×30s", "30s", "#9C27B0", "Khó", "https://youtu.be/nmwgirgXLYM"));
        exercises.add(new Exercise("Plank nghiêng", "abs", "2×45s", "60s", "#9C27B0", "Khó", "https://youtu.be/K2VljzCC16g"));
        exercises.add(new Exercise("Đá chân nhanh", "abs", "3×30s", "30s", "#9C27B0", "Trung bình", "https://youtu.be/ANVdMDaYRts"));
        
        return exercises;
    }
    
    /**
     * Get exercises for back workouts with video URLs
     */
    public static List<Exercise> getBackExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Tư thế siêu nhân", "back", "4×15", "45s", "#795548", "Trung bình", "https://youtu.be/7SLbUk-qTTM"));
        exercises.add(new Exercise("Duỗi lưng", "back", "3×15", "45s", "#795548", "Dễ", "https://youtu.be/cc7kIfSUWEY"));
        exercises.add(new Exercise("Thiên thần ngược", "back", "3×12", "45s", "#795548", "Trung bình", "https://youtu.be/HSoHeSjvIdY"));
        exercises.add(new Exercise("Nâng tay chữ Y", "back", "3×12", "45s", "#795548", "Trung bình", "https://youtu.be/aKNn76T3RPE"));
        exercises.add(new Exercise("Nâng tay chữ T", "back", "3×12", "45s", "#795548", "Trung bình", "https://youtu.be/T3b5yJNhjhQ"));
        exercises.add(new Exercise("Chó chim", "back", "3×12", "45s", "#795548", "Trung bình", "https://youtu.be/wiFNA3sqjCA"));
        exercises.add(new Exercise("Mèo - Bò", "back", "3×15", "30s", "#795548", "Dễ", "https://youtu.be/kqnua4rHVVA"));
        
        return exercises;
    }
    
    /**
     * Get HIIT exercises with video URLs
     */
    public static List<Exercise> getHIITExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Burpees", "hiit", "4×15", "30s", "#E91E63", "Khó", "https://youtu.be/JZQA08SlJnM"));
        exercises.add(new Exercise("Bật nhảy", "hiit", "3×30", "30s", "#E91E63", "Dễ", "https://youtu.be/c4DAnQ6DtF8"));
        exercises.add(new Exercise("Leo núi", "hiit", "3×30s", "30s", "#E91E63", "Trung bình", "https://youtu.be/nmwgirgXLYM"));
        exercises.add(new Exercise("Chạy tại chỗ", "hiit", "3×30s", "30s", "#E91E63", "Dễ", "https://youtu.be/8opcQdC-V-U"));
        exercises.add(new Exercise("Squat nhảy", "hiit", "3×15", "30s", "#E91E63", "Trung bình", "https://youtu.be/A-cFYWvaHr0"));
        exercises.add(new Exercise("Tabata 4 phút", "hiit", "8×20s", "10s", "#E91E63", "Khó", "https://youtu.be/20Khkl95_qA"));
        
        return exercises;
    }
    
    /**
     * Get all exercises combined
     */
    public static List<Exercise> getAllExercises() {
        List<Exercise> allExercises = new ArrayList<>();
        allExercises.addAll(getChestExercises());
        allExercises.addAll(getShoulderExercises());
        allExercises.addAll(getLegsExercises());
        allExercises.addAll(getAbsExercises());
        allExercises.addAll(getBackExercises());
        allExercises.addAll(getHIITExercises());
        return allExercises;
    }
    
    /**
     * Get exercises by category
     */
    public static List<Exercise> getExercisesByCategory(String category) {
        switch (category.toLowerCase()) {
            case "chest":
                return getChestExercises();
            case "shoulder":
                return getShoulderExercises();
            case "legs":
                return getLegsExercises();
            case "abs":
                return getAbsExercises();
            case "back":
                return getBackExercises();
            case "hiit":
            case "cardio":
                return getHIITExercises();
            default:
                return getAllExercises();
        }
    }
}