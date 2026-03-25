package com.htdgym.app.utils;

import com.htdgym.app.models.Exercise;
import java.util.ArrayList;
import java.util.List;

/**
 * Muscle Builder Manager - Chương trình xây dựng cơ bắp chuyên sâu
 */
public class MuscleBuilderManager {
    
    /**
     * Upper Body Strength - Tăng cơ thân trên
     */
    public static List<Exercise> getUpperBodyStrength() {
        List<Exercise> exercises = new ArrayList<>();
        
        // NGỰC - Chest Development
        exercises.add(new Exercise("Bench Press", "chest", "4×8-12", "90s", "#FF6B6B", "Khó", 
            "https://youtu.be/rT7DgCr-3pg", "Ép ngực với tạ đòn"));
        exercises.add(new Exercise("Incline Dumbbell Press", "chest", "4×10-12", "75s", "#FF6B6B", "Khó", 
            "https://youtu.be/DbFgADa2PL8", "Ép ngực nghiêng với tạ đơn"));
        exercises.add(new Exercise("Decline Push-ups", "chest", "3×12-15", "60s", "#FF6B6B", "Trung bình", 
            "https://youtu.be/SKPab2YC8BE", "Hít đất xuống dốc"));
        exercises.add(new Exercise("Chest Fly", "chest", "3×12-15", "60s", "#FF6B6B", "Trung bình", 
            "https://youtu.be/eozdVDA78K0", "Dạng chim ngực"));
        exercises.add(new Exercise("Diamond Push-ups", "chest", "3×8-12", "60s", "#FF6B6B", "Khó", 
            "https://youtu.be/J0DnG1_S92I", "Hít đất kim cương"));
        
        // LƯNG - Back Development
        exercises.add(new Exercise("Deadlift", "back", "4×6-8", "120s", "#795548", "Cực khó", 
            "https://youtu.be/op9kVnSso6Q", "Nâng tạ từ sàn"));
        exercises.add(new Exercise("Pull-ups", "back", "4×6-10", "90s", "#795548", "Khó", 
            "https://youtu.be/eGo4IYlbE5g", "Kéo xà đơn"));
        exercises.add(new Exercise("Barbell Row", "back", "4×8-12", "75s", "#795548", "Khó", 
            "https://youtu.be/G8l_8chR5BE", "Chèo tạ đòn"));
        exercises.add(new Exercise("Lat Pulldown", "back", "4×10-12", "60s", "#795548", "Trung bình", 
            "https://youtu.be/CAwf7n6Luuc", "Kéo xà lat"));
        exercises.add(new Exercise("T-Bar Row", "back", "3×10-12", "75s", "#795548", "Khó", 
            "https://youtu.be/j3Igk5nyZE4", "Chèo T-Bar"));
        
        // VAI - Shoulder Development
        exercises.add(new Exercise("Overhead Press", "shoulder", "4×8-10", "90s", "#4ECDC4", "Khó", 
            "https://youtu.be/2yjwXTZQDDI", "Đẩy vai qua đầu"));
        exercises.add(new Exercise("Lateral Raises", "shoulder", "4×12-15", "45s", "#4ECDC4", "Trung bình", 
            "https://youtu.be/3VcKaXpzqRo", "Nâng tay ngang"));
        exercises.add(new Exercise("Front Raises", "shoulder", "3×12-15", "45s", "#4ECDC4", "Trung bình", 
            "https://youtu.be/qM5X-_FfCQE", "Nâng tay trước"));
        exercises.add(new Exercise("Rear Delt Fly", "shoulder", "4×12-15", "45s", "#4ECDC4", "Trung bình", 
            "https://youtu.be/T3b5yJNhjhQ", "Dạng chim vai sau"));
        exercises.add(new Exercise("Arnold Press", "shoulder", "3×10-12", "60s", "#4ECDC4", "Khó", 
            "https://youtu.be/6Z15_WdXmVw", "Đẩy vai Arnold"));
        
        // TAY - Arms Development
        exercises.add(new Exercise("Barbell Curl", "arms", "4×10-12", "60s", "#FF9800", "Trung bình", 
            "https://youtu.be/ykJmrZ5v0Oo", "Cuốn tạ đòn"));
        exercises.add(new Exercise("Hammer Curl", "arms", "3×12-15", "45s", "#FF9800", "Trung bình", 
            "https://youtu.be/zC3nLlEvin4", "Cuốn búa"));
        exercises.add(new Exercise("Tricep Dips", "arms", "4×10-15", "60s", "#FF9800", "Khó", 
            "https://youtu.be/2z8JmcrW-As", "Chống đẩy tay sau"));
        exercises.add(new Exercise("Close-Grip Push-ups", "arms", "3×12-15", "45s", "#FF9800", "Trung bình", 
            "https://youtu.be/J0DnG1_S92I", "Hít đất tay hẹp"));
        exercises.add(new Exercise("Tricep Extension", "arms", "3×12-15", "45s", "#FF9800", "Trung bình", 
            "https://youtu.be/nRiJVZDpdL0", "Duỗi tay sau"));
        
        return exercises;
    }
    
    /**
     * Lower Body Strength - Tăng cơ thân dưới
     */
    public static List<Exercise> getLowerBodyStrength() {
        List<Exercise> exercises = new ArrayList<>();
        
        // CHÂN - Leg Development
        exercises.add(new Exercise("Barbell Squat", "legs", "4×8-12", "120s", "#FFA726", "Khó", 
            "https://youtu.be/ultWZbUMPL8", "Gánh đùi với tạ đòn"));
        exercises.add(new Exercise("Romanian Deadlift", "legs", "4×10-12", "90s", "#FFA726", "Khó", 
            "https://youtu.be/jEy_czb3RKA", "Deadlift Romania"));
        exercises.add(new Exercise("Bulgarian Split Squat", "legs", "3×12/leg", "60s", "#FFA726", "Khó", 
            "https://youtu.be/2C-uStzPJWY", "Gánh đùi Bulgaria"));
        exercises.add(new Exercise("Walking Lunges", "legs", "3×15/leg", "60s", "#FFA726", "Trung bình", 
            "https://youtu.be/QOVaHwm-Q6U", "Chùng chân di chuyển"));
        exercises.add(new Exercise("Leg Press", "legs", "4×12-15", "75s", "#FFA726", "Trung bình", 
            "https://youtu.be/IZxyjW7MPJQ", "Ép chân với máy"));
        
        // BẮP CHÂN - Calf Development
        exercises.add(new Exercise("Standing Calf Raise", "legs", "4×15-20", "45s", "#FFA726", "Dễ", 
            "https://youtu.be/gwLzBJYoWlI", "Nâng bắp chân đứng"));
        exercises.add(new Exercise("Seated Calf Raise", "legs", "4×15-20", "45s", "#FFA726", "Dễ", 
            "https://youtu.be/JL3-W4KBKd8", "Nâng bắp chân ngồi"));
        
        // MÔNG - Glute Development
        exercises.add(new Exercise("Hip Thrust", "legs", "4×12-15", "60s", "#FFA726", "Trung bình", 
            "https://youtu.be/wPM8icPu6H8", "Nâng mông"));
        exercises.add(new Exercise("Glute Bridge", "legs", "3×15-20", "45s", "#FFA726", "Dễ", 
            "https://youtu.be/wPM8icPu6H8", "Cầu mông"));
        exercises.add(new Exercise("Single Leg Glute Bridge", "legs", "3×12/leg", "45s", "#FFA726", "Trung bình", 
            "https://youtu.be/wPM8icPu6H8", "Cầu mông một chân"));
        
        return exercises;
    }
    
    /**
     * Core Strength - Tăng cơ core
     */
    public static List<Exercise> getCoreStrength() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Plank", "abs", "3×60s", "60s", "#9C27B0", "Trung bình", 
            "https://youtu.be/pSHjTRCQxIw", "Plank cơ bản"));
        exercises.add(new Exercise("Side Plank", "abs", "3×45s/side", "45s", "#9C27B0", "Khó", 
            "https://youtu.be/K2VljzCC16g", "Plank nghiêng"));
        exercises.add(new Exercise("Dead Bug", "abs", "3×12/side", "45s", "#9C27B0", "Trung bình", 
            "https://youtu.be/g_BYB0R-4Ws", "Dead bug"));
        exercises.add(new Exercise("Bird Dog", "abs", "3×12/side", "45s", "#9C27B0", "Trung bình", 
            "https://youtu.be/wiFNA3sqjCA", "Chó chim"));
        exercises.add(new Exercise("Hanging Leg Raise", "abs", "3×10-15", "60s", "#9C27B0", "Khó", 
            "https://youtu.be/JB2oyawG9KI", "Nâng chân treo xà"));
        exercises.add(new Exercise("Ab Wheel Rollout", "abs", "3×8-12", "60s", "#9C27B0", "Cực khó", 
            "https://youtu.be/UbzWOBmBbg4", "Lăn bánh xe bụng"));
        
        return exercises;
    }
    
    /**
     * Compound Movements - Bài tập phức hợp
     */
    public static List<Exercise> getCompoundMovements() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Clean and Press", "fullbody", "4×6", "120s", "#673AB7", "Cực khó", 
            "https://youtu.be/KwYJTpQ_x_A", "Giật và đẩy"));
        exercises.add(new Exercise("Thrusters", "fullbody", "4×10", "90s", "#673AB7", "Khó", 
            "https://youtu.be/L219ltL15zk", "Thruster toàn thân"));
        exercises.add(new Exercise("Man Makers", "fullbody", "3×8", "90s", "#673AB7", "Cực khó", 
            "https://youtu.be/Pf7wZvraWV0", "Man maker"));
        exercises.add(new Exercise("Turkish Get-up", "fullbody", "3×5/side", "90s", "#673AB7", "Cực khó", 
            "https://youtu.be/0bWRPC49-KI", "Turkish get-up"));
        exercises.add(new Exercise("Farmer's Walk", "fullbody", "3×50m", "60s", "#673AB7", "Khó", 
            "https://youtu.be/p3G2u2KvE7s", "Đi bộ nông dân"));
        
        return exercises;
    }
    
    /**
     * Progressive Overload Program - Chương trình tăng tải dần
     */
    public static List<Exercise> getProgressiveOverloadProgram() {
        List<Exercise> exercises = new ArrayList<>();
        
        // TUẦN 1-2: Foundation (3 sets)
        exercises.add(new Exercise("Tuần 1-2: Foundation", "program", "3 sets", "60-90s", "#2196F3", "Trung bình", 
            "https://youtu.be/rT7DgCr-3pg", "Xây dựng nền tảng"));
        
        // TUẦN 3-4: Volume (4 sets)
        exercises.add(new Exercise("Tuần 3-4: Volume", "program", "4 sets", "60-90s", "#2196F3", "Khó", 
            "https://youtu.be/rT7DgCr-3pg", "Tăng khối lượng"));
        
        // TUẦN 5-6: Intensity (5 sets)
        exercises.add(new Exercise("Tuần 5-6: Intensity", "program", "5 sets", "90-120s", "#2196F3", "Cực khó", 
            "https://youtu.be/rT7DgCr-3pg", "Tăng cường độ"));
        
        // TUẦN 7: Deload
        exercises.add(new Exercise("Tuần 7: Deload", "program", "2-3 sets", "45-60s", "#2196F3", "Dễ", 
            "https://youtu.be/v7AYKMP6rOE", "Giảm tải phục hồi"));
        
        return exercises;
    }
    
    /**
     * Get all muscle building exercises
     */
    public static List<Exercise> getAllMuscleBuilderExercises() {
        List<Exercise> allExercises = new ArrayList<>();
        allExercises.addAll(getUpperBodyStrength());
        allExercises.addAll(getLowerBodyStrength());
        allExercises.addAll(getCoreStrength());
        allExercises.addAll(getCompoundMovements());
        allExercises.addAll(getProgressiveOverloadProgram());
        return allExercises;
    }
    
    /**
     * Get exercises by muscle group
     */
    public static List<Exercise> getExercisesByMuscleGroup(String muscleGroup) {
        switch (muscleGroup.toLowerCase()) {
            case "chest":
            case "ngực":
                return getUpperBodyStrength().subList(0, 5); // First 5 are chest
            case "back":
            case "lưng":
                return getUpperBodyStrength().subList(5, 10); // Next 5 are back
            case "shoulder":
            case "vai":
                return getUpperBodyStrength().subList(10, 15); // Next 5 are shoulders
            case "arms":
            case "tay":
                return getUpperBodyStrength().subList(15, 20); // Next 5 are arms
            case "legs":
            case "chân":
                return getLowerBodyStrength();
            case "abs":
            case "bụng":
                return getCoreStrength();
            case "fullbody":
            case "toàn thân":
                return getCompoundMovements();
            default:
                return getAllMuscleBuilderExercises();
        }
    }
}