package com.htdgym.app.utils;

import com.htdgym.app.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class WorkoutTemplateManager {
    
    /**
     * Get beginner full body workout (3x/week)
     */
    public static List<Exercise> getBeginnerFullBody() {
        List<Exercise> exercises = new ArrayList<>();
        
        // Compound movements for beginners
        exercises.add(new Exercise("Bodyweight Squat", "legs", "3×12-15", "60s", "#4ECDC4", "Dễ", "https://youtu.be/XtoO9YwNEqA"));
        exercises.add(new Exercise("Push-up (Knee)", "chest", "3×8-12", "60s", "#FF6B6B", "Dễ", "https://youtu.be/-R5sH2iG9Gw"));
        exercises.add(new Exercise("Assisted Pull-up", "back", "3×5-8", "90s", "#795548", "Dễ", "https://youtu.be/7SLbUk-qTTM"));
        exercises.add(new Exercise("Plank Hold", "abs", "3×30-45s", "60s", "#9C27B0", "Dễ", "https://youtu.be/pSHjTRCQxIw"));
        exercises.add(new Exercise("Glute Bridge", "legs", "3×15-20", "45s", "#FFA726", "Dễ", "https://youtu.be/wPM8icPu6H8"));
        exercises.add(new Exercise("Wall Sit", "legs", "3×30-45s", "60s", "#4ECDC4", "Dễ", "https://youtu.be/y-wV4Venusw"));
        
        return exercises;
    }
    
    /**
     * Get intermediate push/pull/legs split
     */
    public static List<Exercise> getIntermediatePush() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Standard Push-up", "chest", "4×12-15", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/-R5sH2iG9Gw"));
        exercises.add(new Exercise("Pike Push-up", "shoulder", "4×8-12", "75s", "#4ECDC4", "Trung bình", "https://youtu.be/qEwKCR5JCog"));
        exercises.add(new Exercise("Diamond Push-up", "chest", "3×8-10", "75s", "#FF6B6B", "Khó", "https://youtu.be/J0DnG1_S92I"));
        exercises.add(new Exercise("Tricep Dips", "chest", "3×10-15", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/2z8JmcrW-As"));
        exercises.add(new Exercise("Lateral Raises", "shoulder", "4×15-20", "45s", "#4ECDC4", "Trung bình", "https://youtu.be/3VcKaXpzqRo"));
        exercises.add(new Exercise("Overhead Press", "shoulder", "4×8-12", "75s", "#4ECDC4", "Khó", "https://youtu.be/qEwKCR5JCog"));
        
        return exercises;
    }
    
    /**
     * Get advanced HIIT circuit
     */
    public static List<Exercise> getAdvancedHIIT() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Burpee Box Jump", "hiit", "5×8", "20s", "#E91E63", "Khó", "https://youtu.be/JZQA08SlJnM"));
        exercises.add(new Exercise("Single Leg Burpee", "hiit", "4×6/leg", "30s", "#E91E63", "Khó", "https://youtu.be/JZQA08SlJnM"));
        exercises.add(new Exercise("Explosive Push-up", "chest", "4×8", "45s", "#FF6B6B", "Khó", "https://youtu.be/J0DnG1_S92I"));
        exercises.add(new Exercise("Jump Squat 180°", "legs", "4×10", "30s", "#FFA726", "Khó", "https://youtu.be/A-cFYWvaHr0"));
        exercises.add(new Exercise("Mountain Climber Sprint", "abs", "5×20s", "20s", "#9C27B0", "Khó", "https://youtu.be/nmwgirgXLYM"));
        exercises.add(new Exercise("Plank Up-Down", "abs", "4×12", "45s", "#9C27B0", "Khó", "https://youtu.be/K2VljzCC16g"));
        
        return exercises;
    }
    
    /**
     * Get strength focused workout
     */
    public static List<Exercise> getStrengthFocused() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Pistol Squat Progression", "legs", "5×3-5/leg", "120s", "#FFA726", "Khó", "https://youtu.be/2C-uStzPJWY"));
        exercises.add(new Exercise("One Arm Push-up Progression", "chest", "5×2-3/arm", "120s", "#FF6B6B", "Khó", "https://youtu.be/J0DnG1_S92I"));
        exercises.add(new Exercise("Archer Pull-up", "back", "5×3-5/side", "120s", "#795548", "Khó", "https://youtu.be/HSoHeSjvIdY"));
        exercises.add(new Exercise("Handstand Push-up Progression", "shoulder", "5×2-5", "120s", "#4ECDC4", "Khó", "https://youtu.be/qEwKCR5JCog"));
        exercises.add(new Exercise("Human Flag Progression", "abs", "5×10-20s", "120s", "#9C27B0", "Khó", "https://youtu.be/K2VljzCC16g"));
        
        return exercises;
    }
    
    /**
     * Get mobility and recovery workout
     */
    public static List<Exercise> getMobilityRecovery() {
        List<Exercise> exercises = new ArrayList<>();
        
        exercises.add(new Exercise("Cat-Cow Stretch", "back", "2×15", "30s", "#795548", "Dễ", "https://youtu.be/kqnua4rHVVA"));
        exercises.add(new Exercise("Hip Flexor Stretch", "legs", "2×45s/side", "30s", "#FFA726", "Dễ", "https://youtu.be/8pl4hWllMPQ"));
        exercises.add(new Exercise("Shoulder Dislocations", "shoulder", "2×20", "30s", "#4ECDC4", "Dễ", "https://youtu.be/5Qv2T8VusME"));
        exercises.add(new Exercise("Thoracic Spine Rotation", "back", "2×10/side", "30s", "#795548", "Dễ", "https://youtu.be/cc7kIfSUWEY"));
        exercises.add(new Exercise("Deep Squat Hold", "legs", "3×60s", "60s", "#FFA726", "Dễ", "https://youtu.be/y-wV4Venusw"));
        exercises.add(new Exercise("Pigeon Pose", "legs", "2×60s/side", "30s", "#FFA726", "Dễ", "https://youtu.be/8pl4hWllMPQ"));
        
        return exercises;
    }
    
    /**
     * Get workout template by type
     */
    public static List<Exercise> getWorkoutTemplate(String type) {
        switch (type.toLowerCase()) {
            case "beginner_fullbody":
                return getBeginnerFullBody();
            case "intermediate_push":
                return getIntermediatePush();
            case "advanced_hiit":
                return getAdvancedHIIT();
            case "strength_focused":
                return getStrengthFocused();
            case "mobility_recovery":
                return getMobilityRecovery();
            default:
                return getBeginnerFullBody();
        }
    }
    
    /**
     * Get all available templates
     */
    public static List<String> getAvailableTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("beginner_fullbody");
        templates.add("intermediate_push");
        templates.add("advanced_hiit");
        templates.add("strength_focused");
        templates.add("mobility_recovery");
        return templates;
    }
}