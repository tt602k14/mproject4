package com.htdgym.app.utils;

import com.htdgym.app.models.Exercise;
import java.util.ArrayList;
import java.util.List;

/**
 * HIIT Workout Manager - Chương trình đốt mỡ chuyên sâu
 */
public class HIITWorkoutManager {
    
    /**
     * HIIT Beginner - 4 tuần đầu (Người mới bắt đầu)
     */
    public static List<Exercise> getHIITBeginner() {
        List<Exercise> exercises = new ArrayList<>();
        
        // TUẦN 1: Làm quen HIIT (20s work / 40s rest)
        exercises.add(new Exercise("Ngày 1: HIIT Cơ Bản", "hiit", "4×20s", "40s", "#E91E63", "Dễ", 
            "https://youtu.be/c4DAnQ6DtF8", "Jumping Jacks + High Knees"));
        exercises.add(new Exercise("Ngày 2: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/ZToicYcHIOU"));
        exercises.add(new Exercise("Ngày 3: Cardio Nhẹ", "hiit", "6×20s", "40s", "#E91E63", "Dễ", 
            "https://youtu.be/8opcQdC-V-U", "Chạy tại chỗ + Butt Kicks"));
        exercises.add(new Exercise("Ngày 4: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/v7AYKMP6rOE"));
        exercises.add(new Exercise("Ngày 5: HIIT Toàn Thân", "hiit", "5×20s", "40s", "#E91E63", "Dễ", 
            "https://youtu.be/ml6cT4AZdqI", "Squat + Push-up"));
        exercises.add(new Exercise("Ngày 6: Active Recovery", "rest", "Nghỉ tích cực", "30min", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/1ZYbU82GVz4"));
        exercises.add(new Exercise("Ngày 7: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/COp7BR_Dvps"));
        
        // TUẦN 2: Tăng cường độ (25s work / 35s rest)
        exercises.add(new Exercise("Ngày 8: HIIT Nâng Cao", "hiit", "5×25s", "35s", "#E91E63", "Trung bình", 
            "https://youtu.be/JZQA08SlJnM", "Burpees + Mountain Climber"));
        exercises.add(new Exercise("Ngày 9: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/inpok4MKVLM"));
        exercises.add(new Exercise("Ngày 10: Tabata Cơ Bản", "hiit", "4×25s", "35s", "#E91E63", "Trung bình", 
            "https://youtu.be/20Khkl95_qA", "Tabata Protocol"));
        exercises.add(new Exercise("Ngày 11: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/ZToicYcHIOU"));
        exercises.add(new Exercise("Ngày 12: HIIT Circuit", "hiit", "6×25s", "35s", "#E91E63", "Trung bình", 
            "https://youtu.be/A-cFYWvaHr0", "Squat Jump + Lunge"));
        exercises.add(new Exercise("Ngày 13: Yoga Recovery", "rest", "Phục hồi", "45min", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/v7AYKMP6rOE"));
        exercises.add(new Exercise("Ngày 14: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/1ZYbU82GVz4"));
        
        // TUẦN 3: Thử thách (30s work / 30s rest)
        exercises.add(new Exercise("Ngày 15: HIIT Thử Thách", "hiit", "6×30s", "30s", "#E91E63", "Khó", 
            "https://youtu.be/nmwgirgXLYM", "Mountain Climber Circuit"));
        exercises.add(new Exercise("Ngày 16: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/COp7BR_Dvps"));
        exercises.add(new Exercise("Ngày 17: Tabata Nâng Cao", "hiit", "8×30s", "30s", "#E91E63", "Khó", 
            "https://youtu.be/20Khkl95_qA", "Advanced Tabata"));
        exercises.add(new Exercise("Ngày 18: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/inpok4MKVLM"));
        exercises.add(new Exercise("Ngày 19: HIIT Extreme", "hiit", "7×30s", "30s", "#E91E63", "Khó", 
            "https://youtu.be/FJmRQ5iTXKE", "Jump Rope Circuit"));
        exercises.add(new Exercise("Ngày 20: Active Recovery", "rest", "Nghỉ tích cực", "30min", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/ZToicYcHIOU"));
        exercises.add(new Exercise("Ngày 21: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/v7AYKMP6rOE"));
        
        // TUẦN 4: Mastery (40s work / 20s rest)
        exercises.add(new Exercise("Ngày 22: HIIT Master", "hiit", "8×40s", "20s", "#E91E63", "Cực khó", 
            "https://youtu.be/ml6cT4AZdqI", "Full Body HIIT"));
        exercises.add(new Exercise("Ngày 23: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/1ZYbU82GVz4"));
        exercises.add(new Exercise("Ngày 24: Tabata Master", "hiit", "10×40s", "20s", "#E91E63", "Cực khó", 
            "https://youtu.be/20Khkl95_qA", "Master Tabata"));
        exercises.add(new Exercise("Ngày 25: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/COp7BR_Dvps"));
        exercises.add(new Exercise("Ngày 26: HIIT Ultimate", "hiit", "12×40s", "20s", "#E91E63", "Cực khó", 
            "https://youtu.be/JZQA08SlJnM", "Ultimate Burpee Challenge"));
        exercises.add(new Exercise("Ngày 27: Recovery", "rest", "Phục hồi", "60min", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/inpok4MKVLM"));
        exercises.add(new Exercise("Ngày 28: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", 
            "https://youtu.be/ZToicYcHIOU"));
        
        return exercises;
    }
    
    /**
     * HIIT Advanced - Chương trình đốt mỡ nâng cao
     */
    public static List<Exercise> getHIITAdvanced() {
        List<Exercise> exercises = new ArrayList<>();
        
        // Advanced HIIT với các bài tập phức tạp hơn
        exercises.add(new Exercise("Burpee Box Jump", "hiit", "45s", "15s", "#E91E63", "Cực khó", 
            "https://youtu.be/JZQA08SlJnM", "Burpee + Box Jump combo"));
        exercises.add(new Exercise("Kettlebell Swing HIIT", "hiit", "45s", "15s", "#E91E63", "Cực khó", 
            "https://youtu.be/GYHbu2LRqD0", "Kettlebell swings"));
        exercises.add(new Exercise("Battle Rope Waves", "hiit", "45s", "15s", "#E91E63", "Cực khó", 
            "https://youtu.be/w8ZdJ2JaGZs", "Battle rope training"));
        exercises.add(new Exercise("Plyometric Circuit", "hiit", "45s", "15s", "#E91E63", "Cực khó", 
            "https://youtu.be/A-cFYWvaHr0", "Jump training"));
        exercises.add(new Exercise("Sprint Intervals", "hiit", "30s", "90s", "#E91E63", "Cực khó", 
            "https://youtu.be/8opcQdC-V-U", "High intensity sprints"));
        
        return exercises;
    }
    
    /**
     * Fat Burning Cardio - Cardio đốt mỡ
     */
    public static List<Exercise> getFatBurningCardio() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Steady State Cardio", "cardio", "30min", "0s", "#FF9800", "Trung bình", 
            "https://youtu.be/8opcQdC-V-U", "Chạy bộ nhịp độ ổn định"));
        exercises.add(new Exercise("Incline Walking", "cardio", "45min", "0s", "#FF9800", "Dễ", 
            "https://youtu.be/8opcQdC-V-U", "Đi bộ lên dốc"));
        exercises.add(new Exercise("Cycling HIIT", "cardio", "20min", "0s", "#FF9800", "Khó", 
            "https://youtu.be/8opcQdC-V-U", "Đạp xe HIIT"));
        exercises.add(new Exercise("Swimming Intervals", "cardio", "30min", "0s", "#FF9800", "Trung bình", 
            "https://youtu.be/8opcQdC-V-U", "Bơi lội khoảng cách"));
        exercises.add(new Exercise("Rowing Machine", "cardio", "25min", "0s", "#FF9800", "Khó", 
            "https://youtu.be/8opcQdC-V-U", "Máy chèo thuyền"));
        
        return exercises;
    }
    
    /**
     * Core Fat Burning - Đốt mỡ bụng
     */
    public static List<Exercise> getCoreFatBurning() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Plank to Push-up", "abs", "3×15", "60s", "#9C27B0", "Khó", 
            "https://youtu.be/pSHjTRCQxIw", "Plank chuyển push-up"));
        exercises.add(new Exercise("Russian Twist HIIT", "abs", "45s", "15s", "#9C27B0", "Khó", 
            "https://youtu.be/wkD8rjkodUI", "Xoay người Nga tốc độ cao"));
        exercises.add(new Exercise("Mountain Climber", "abs", "45s", "15s", "#9C27B0", "Khó", 
            "https://youtu.be/nmwgirgXLYM", "Leo núi tại chỗ"));
        exercises.add(new Exercise("Bicycle Crunch Fast", "abs", "45s", "15s", "#9C27B0", "Khó", 
            "https://youtu.be/9FGilxCbdz8", "Gập bụng xe đạp nhanh"));
        exercises.add(new Exercise("Dead Bug HIIT", "abs", "45s", "15s", "#9C27B0", "Khó", 
            "https://youtu.be/g_BYB0R-4Ws", "Dead bug tốc độ cao"));
        
        return exercises;
    }
    
    /**
     * Get all HIIT exercises
     */
    public static List<Exercise> getAllHIITExercises() {
        List<Exercise> allExercises = new ArrayList<>();
        allExercises.addAll(getHIITBeginner());
        allExercises.addAll(getHIITAdvanced());
        allExercises.addAll(getFatBurningCardio());
        allExercises.addAll(getCoreFatBurning());
        return allExercises;
    }
}