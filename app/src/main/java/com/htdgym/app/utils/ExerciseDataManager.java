package com.htdgym.app.utils;

import com.htdgym.app.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDataManager {
    
    /**
     * Get exercises for chest workouts with video URLs and thumbnails
     */
    public static List<Exercise> getChestExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Hít đất cơ bản", "chest", "4×15", "60s", "#FF6B6B", "Dễ", 
            "https://youtu.be/IODxDxX7oi4", "https://i.ytimg.com/vi/IODxDxX7oi4/hqdefault.jpg"));
        exercises.add(new Exercise("Hít đất nghiêng", "chest", "3×12", "45s", "#FF6B6B", "Dễ", 
            "https://youtu.be/cfQBmpfjObs", "https://i.ytimg.com/vi/cfQBmpfjObs/hqdefault.jpg"));
        exercises.add(new Exercise("Hít đất rộng", "chest", "3×12", "45s", "#FF6B6B", "Trung bình", 
            "https://youtu.be/rr5akuBqbfU", "https://i.ytimg.com/vi/rr5akuBqbfU/hqdefault.jpg"));
        exercises.add(new Exercise("Hít đất kim cương", "chest", "3×10", "60s", "#FF6B6B", "Khó", 
            "https://youtu.be/J0DnG1_S92I", "https://i.ytimg.com/vi/J0DnG1_S92I/hqdefault.jpg"));
        exercises.add(new Exercise("Hít đất xuống dốc", "chest", "3×12", "45s", "#FF6B6B", "Trung bình", 
            "https://youtu.be/SKPab2YC8BE", "https://i.ytimg.com/vi/SKPab2YC8BE/hqdefault.jpg"));
        exercises.add(new Exercise("Chống đẩy ngực", "chest", "3×10", "60s", "#FF6B6B", "Trung bình", 
            "https://youtu.be/2z8JmcrW-As", "https://i.ytimg.com/vi/2z8JmcrW-As/hqdefault.jpg"));
        
        return exercises;
    }
    
    /**
     * Get exercises for shoulder workouts with video URLs and thumbnails
     */
    public static List<Exercise> getShoulderExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Hít đất vai", "shoulder", "4×12", "60s", "#4ECDC4", "Trung bình", 
            "https://youtu.be/d1HZBdD0idE", "https://i.ytimg.com/vi/d1HZBdD0idE/hqdefault.jpg"));
        exercises.add(new Exercise("Chạm vai", "shoulder", "3×20", "30s", "#4ECDC4", "Dễ", 
            "https://youtu.be/1We3zKXj_uA", "https://i.ytimg.com/vi/1We3zKXj_uA/hqdefault.jpg"));
        exercises.add(new Exercise("Vòng tay", "shoulder", "3×30s", "30s", "#4ECDC4", "Dễ", 
            "https://youtu.be/qEwKCR5JCog", "https://i.ytimg.com/vi/qEwKCR5JCog/hqdefault.jpg"));
        exercises.add(new Exercise("Nâng tay ngang", "shoulder", "3×15", "45s", "#4ECDC4", "Trung bình", 
            "https://youtu.be/3VcKaXpzqRo", "https://i.ytimg.com/vi/3VcKaXpzqRo/hqdefault.jpg"));
        exercises.add(new Exercise("Nâng tay trước", "shoulder", "3×15", "45s", "#4ECDC4", "Trung bình", 
            "https://youtu.be/qM5X-_FfCQE", "https://i.ytimg.com/vi/qM5X-_FfCQE/hqdefault.jpg"));
        exercises.add(new Exercise("Đẩy vai", "shoulder", "4×12", "60s", "#4ECDC4", "Khó", 
            "https://youtu.be/HSoHeSjvIdY", "https://i.ytimg.com/vi/HSoHeSjvIdY/hqdefault.jpg"));
        
        return exercises;
    }
    
    /**
     * Get exercises for legs workouts with video URLs and thumbnails
     */
    public static List<Exercise> getLegsExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Gánh đùi", "legs", "4×20", "60s", "#FFA726", "Dễ", 
            "https://youtu.be/Xe1mCFljUN0", "https://i.ytimg.com/vi/Xe1mCFljUN0/hqdefault.jpg"));
        exercises.add(new Exercise("Chùng chân", "legs", "3×15", "45s", "#FFA726", "Trung bình", 
            "https://youtu.be/QOVaHwm-Q6U", "https://i.ytimg.com/vi/QOVaHwm-Q6U/hqdefault.jpg"));
        exercises.add(new Exercise("Gánh đùi nhảy", "legs", "3×15", "60s", "#FFA726", "Khó", 
            "https://youtu.be/A-cFYWvaHr0", "https://i.ytimg.com/vi/A-cFYWvaHr0/hqdefault.jpg"));
        exercises.add(new Exercise("Ngồi tường", "legs", "3×45s", "60s", "#FFA726", "Trung bình", 
            "https://youtu.be/y-wV4Venusw", "https://i.ytimg.com/vi/y-wV4Venusw/hqdefault.jpg"));
        exercises.add(new Exercise("Nâng bắp chân", "legs", "4×20", "30s", "#FFA726", "Dễ", 
            "https://youtu.be/gwLzBJYoWlI", "https://i.ytimg.com/vi/gwLzBJYoWlI/hqdefault.jpg"));
        exercises.add(new Exercise("Chùng chân ngang", "legs", "3×12", "45s", "#FFA726", "Trung bình", 
            "https://youtu.be/8pl4hWllMPQ", "https://i.ytimg.com/vi/8pl4hWllMPQ/hqdefault.jpg"));
        exercises.add(new Exercise("Gánh đùi Bulgaria", "legs", "3×12", "60s", "#FFA726", "Khó", 
            "https://youtu.be/2C-uStzPJWY", "https://i.ytimg.com/vi/2C-uStzPJWY/hqdefault.jpg"));
        exercises.add(new Exercise("Nâng mông", "legs", "4×15", "45s", "#FFA726", "Dễ", 
            "https://youtu.be/wPM8icPu6H8", "https://i.ytimg.com/vi/wPM8icPu6H8/hqdefault.jpg"));
        
        return exercises;
    }
    
    /**
     * Get exercises for abs workouts with video URLs and thumbnails
     */
    public static List<Exercise> getAbsExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Gập bụng", "abs", "4×20", "30s", "#9C27B0", "Dễ", 
            "https://youtu.be/1fbU_MkV7NE", "https://i.ytimg.com/vi/1fbU_MkV7NE/hqdefault.jpg"));
        exercises.add(new Exercise("Plank cơ bản", "abs", "3×60s", "60s", "#9C27B0", "Trung bình", 
            "https://youtu.be/pSHjTRCQxIw", "https://i.ytimg.com/vi/pSHjTRCQxIw/hqdefault.jpg"));
        exercises.add(new Exercise("Gập bụng xe đạp", "abs", "3×20", "30s", "#9C27B0", "Trung bình", 
            "https://youtu.be/9FGilxCbdz8", "https://i.ytimg.com/vi/9FGilxCbdz8/hqdefault.jpg"));
        exercises.add(new Exercise("Nâng chân", "abs", "3×15", "45s", "#9C27B0", "Trung bình", 
            "https://youtu.be/JB2oyawG9KI", "https://i.ytimg.com/vi/JB2oyawG9KI/hqdefault.jpg"));
        exercises.add(new Exercise("Xoay người Nga", "abs", "3×30", "30s", "#9C27B0", "Khó", 
            "https://youtu.be/wkD8rjkodUI", "https://i.ytimg.com/vi/wkD8rjkodUI/hqdefault.jpg"));
        exercises.add(new Exercise("Leo núi", "abs", "3×30s", "30s", "#9C27B0", "Khó", 
            "https://youtu.be/nmwgirgXLYM", "https://i.ytimg.com/vi/nmwgirgXLYM/hqdefault.jpg"));
        exercises.add(new Exercise("Plank nghiêng", "abs", "2×45s", "60s", "#9C27B0", "Khó", 
            "https://youtu.be/K2VljzCC16g", "https://i.ytimg.com/vi/K2VljzCC16g/hqdefault.jpg"));
        exercises.add(new Exercise("Đá chân nhanh", "abs", "3×30s", "30s", "#9C27B0", "Trung bình", 
            "https://youtu.be/ANVdMDaYRts", "https://i.ytimg.com/vi/ANVdMDaYRts/hqdefault.jpg"));
        
        return exercises;
    }
    
    /**
     * Get exercises for back workouts with video URLs and thumbnails
     */
    public static List<Exercise> getBackExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Tư thế siêu nhân", "back", "4×15", "45s", "#795548", "Trung bình", 
            "https://youtu.be/cc7kIfSUWEY", "https://i.ytimg.com/vi/cc7kIfSUWEY/hqdefault.jpg"));
        exercises.add(new Exercise("Duỗi lưng", "back", "3×15", "45s", "#795548", "Dễ", 
            "https://youtu.be/4BOTvaRaDjI", "https://i.ytimg.com/vi/4BOTvaRaDjI/hqdefault.jpg"));
        exercises.add(new Exercise("Thiên thần ngược", "back", "3×12", "45s", "#795548", "Trung bình", 
            "https://youtu.be/HSoHeSjvIdY", "https://i.ytimg.com/vi/HSoHeSjvIdY/hqdefault.jpg"));
        exercises.add(new Exercise("Nâng tay chữ Y", "back", "3×12", "45s", "#795548", "Trung bình", 
            "https://youtu.be/aKNn76T3RPE", "https://i.ytimg.com/vi/aKNn76T3RPE/hqdefault.jpg"));
        exercises.add(new Exercise("Nâng tay chữ T", "back", "3×12", "45s", "#795548", "Trung bình", 
            "https://youtu.be/T3b5yJNhjhQ", "https://i.ytimg.com/vi/T3b5yJNhjhQ/hqdefault.jpg"));
        exercises.add(new Exercise("Chó chim", "back", "3×12", "45s", "#795548", "Trung bình", 
            "https://youtu.be/wiFNA3sqjCA", "https://i.ytimg.com/vi/wiFNA3sqjCA/hqdefault.jpg"));
        exercises.add(new Exercise("Mèo - Bò", "back", "3×15", "30s", "#795548", "Dễ", 
            "https://youtu.be/kqnua4rHVVA", "https://i.ytimg.com/vi/kqnua4rHVVA/hqdefault.jpg"));
        
        return exercises;
    }
    
    /**
     * Get HIIT exercises with video URLs and thumbnails
     */
    public static List<Exercise> getHIITExercises() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Burpees", "hiit", "4×15", "30s", "#E91E63", "Khó", 
            "https://youtu.be/JZQA08SlJnM", "https://i.ytimg.com/vi/JZQA08SlJnM/hqdefault.jpg"));
        exercises.add(new Exercise("Bật nhảy", "hiit", "3×30", "30s", "#E91E63", "Dễ", 
            "https://youtu.be/c4DAnQ6DtF8", "https://i.ytimg.com/vi/c4DAnQ6DtF8/hqdefault.jpg"));
        exercises.add(new Exercise("Leo núi", "hiit", "3×30s", "30s", "#E91E63", "Trung bình", 
            "https://youtu.be/nmwgirgXLYM", "https://i.ytimg.com/vi/nmwgirgXLYM/hqdefault.jpg"));
        exercises.add(new Exercise("Chạy tại chỗ", "hiit", "3×30s", "30s", "#E91E63", "Dễ", 
            "https://youtu.be/8opcQdC-V-U", "https://i.ytimg.com/vi/8opcQdC-V-U/hqdefault.jpg"));
        exercises.add(new Exercise("Squat nhảy", "hiit", "3×15", "30s", "#E91E63", "Trung bình", 
            "https://youtu.be/A-cFYWvaHr0", "https://i.ytimg.com/vi/A-cFYWvaHr0/hqdefault.jpg"));
        exercises.add(new Exercise("Tabata 4 phút", "hiit", "8×20s", "10s", "#E91E63", "Khó", 
            "https://youtu.be/20Khkl95_qA", "https://i.ytimg.com/vi/20Khkl95_qA/hqdefault.jpg"));
        
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