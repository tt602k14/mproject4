package com.htdgym.app.utils;

import com.htdgym.app.models.Exercise;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkoutPlannerAI {
    
    public enum FitnessLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
    
    public enum WorkoutGoal {
        FAT_LOSS, MUSCLE_BUILDING, STRENGTH, ENDURANCE, GENERAL_FITNESS
    }
    
    public enum TimeAvailable {
        SHORT(15), MEDIUM(30), LONG(45), EXTENDED(60);
        
        private final int minutes;
        
        TimeAvailable(int minutes) {
            this.minutes = minutes;
        }
        
        public int getMinutes() {
            return minutes;
        }
    }
    
    /**
     * Generate personalized workout based on user preferences
     */
    public static List<Exercise> generatePersonalizedWorkout(
            FitnessLevel level, 
            WorkoutGoal goal, 
            TimeAvailable time,
            List<String> availableEquipment) {
        
        List<Exercise> workout = new ArrayList<>();
        
        // Determine exercise count based on time
        int exerciseCount = calculateExerciseCount(time);
        
        // Select exercises based on goal and level
        List<Exercise> candidateExercises = selectCandidateExercises(goal, level);
        
        // Filter by equipment availability
        candidateExercises = filterByEquipment(candidateExercises, availableEquipment);
        
        // Create balanced workout
        workout = createBalancedWorkout(candidateExercises, exerciseCount, goal, level);
        
        // Adjust sets/reps based on goal and level
        workout = adjustWorkoutParameters(workout, goal, level, time);
        
        return workout;
    }
    
    private static int calculateExerciseCount(TimeAvailable time) {
        switch (time) {
            case SHORT: return 4;
            case MEDIUM: return 6;
            case LONG: return 8;
            case EXTENDED: return 10;
            default: return 6;
        }
    }
    
    private static List<Exercise> selectCandidateExercises(WorkoutGoal goal, FitnessLevel level) {
        List<Exercise> candidates = new ArrayList<>();
        
        switch (goal) {
            case FAT_LOSS:
                candidates.addAll(ExerciseDataManager.getHIITExercises());
                candidates.addAll(ExerciseDataManager.getAbsExercises());
                break;
            case MUSCLE_BUILDING:
                candidates.addAll(ExerciseDataManager.getChestExercises());
                candidates.addAll(ExerciseDataManager.getBackExercises());
                candidates.addAll(ExerciseDataManager.getLegsExercises());
                break;
            case STRENGTH:
                candidates.addAll(WorkoutTemplateManager.getStrengthFocused());
                break;
            case ENDURANCE:
                candidates.addAll(ExerciseDataManager.getHIITExercises());
                candidates.addAll(ExerciseDataManager.getLegsExercises());
                break;
            case GENERAL_FITNESS:
            default:
                candidates.addAll(ExerciseDataManager.getAllExercises());
                break;
        }
        
        // Filter by fitness level
        return filterByFitnessLevel(candidates, level);
    }
    
    private static List<Exercise> filterByFitnessLevel(List<Exercise> exercises, FitnessLevel level) {
        List<Exercise> filtered = new ArrayList<>();
        
        for (Exercise exercise : exercises) {
            String difficulty = exercise.getDifficulty();
            
            switch (level) {
                case BEGINNER:
                    if ("Dễ".equals(difficulty)) {
                        filtered.add(exercise);
                    }
                    break;
                case INTERMEDIATE:
                    if ("Dễ".equals(difficulty) || "Trung bình".equals(difficulty)) {
                        filtered.add(exercise);
                    }
                    break;
                case ADVANCED:
                    filtered.add(exercise); // All difficulties
                    break;
            }
        }
        
        return filtered;
    }
    
    private static List<Exercise> filterByEquipment(List<Exercise> exercises, List<String> equipment) {
        // For now, assume all exercises are bodyweight
        // In future, add equipment field to Exercise model
        return exercises;
    }
    
    private static List<Exercise> createBalancedWorkout(
            List<Exercise> candidates, 
            int count, 
            WorkoutGoal goal, 
            FitnessLevel level) {
        
        List<Exercise> workout = new ArrayList<>();
        List<String> usedMuscleGroups = new ArrayList<>();
        Random random = new Random();
        
        // Ensure muscle group balance
        while (workout.size() < count && !candidates.isEmpty()) {
            Exercise candidate = candidates.get(random.nextInt(candidates.size()));
            
            // Avoid consecutive same muscle groups
            if (!usedMuscleGroups.contains(candidate.getMuscleGroup()) || 
                usedMuscleGroups.size() >= 3) {
                
                workout.add(candidate);
                usedMuscleGroups.add(candidate.getMuscleGroup());
                
                // Keep only last 2 muscle groups in memory
                if (usedMuscleGroups.size() > 2) {
                    usedMuscleGroups.remove(0);
                }
            }
            
            candidates.remove(candidate);
        }
        
        return workout;
    }
    
    private static List<Exercise> adjustWorkoutParameters(
            List<Exercise> workout, 
            WorkoutGoal goal, 
            FitnessLevel level, 
            TimeAvailable time) {
        
        for (Exercise exercise : workout) {
            // Adjust based on goal
            switch (goal) {
                case FAT_LOSS:
                    // Higher reps, shorter rest
                    exercise.setSetsReps(adjustForFatLoss(exercise.getSetsReps()));
                    exercise.setRestTime("30s");
                    break;
                case MUSCLE_BUILDING:
                    // Moderate reps, moderate rest
                    exercise.setSetsReps(adjustForMuscleBuilding(exercise.getSetsReps()));
                    exercise.setRestTime("60s");
                    break;
                case STRENGTH:
                    // Lower reps, longer rest
                    exercise.setSetsReps(adjustForStrength(exercise.getSetsReps()));
                    exercise.setRestTime("90s");
                    break;
                case ENDURANCE:
                    // High reps, short rest
                    exercise.setSetsReps(adjustForEndurance(exercise.getSetsReps()));
                    exercise.setRestTime("45s");
                    break;
            }
            
            // Adjust based on fitness level
            switch (level) {
                case BEGINNER:
                    exercise.setSetsReps(reduceIntensity(exercise.getSetsReps()));
                    break;
                case ADVANCED:
                    exercise.setSetsReps(increaseIntensity(exercise.getSetsReps()));
                    break;
            }
        }
        
        return workout;
    }
    
    private static String adjustForFatLoss(String setsReps) {
        // Convert to higher rep ranges
        return setsReps.replaceAll("×\\d+", "×20");
    }
    
    private static String adjustForMuscleBuilding(String setsReps) {
        // Moderate rep ranges
        return setsReps.replaceAll("×\\d+", "×12");
    }
    
    private static String adjustForStrength(String setsReps) {
        // Lower rep ranges
        return setsReps.replaceAll("×\\d+", "×6");
    }
    
    private static String adjustForEndurance(String setsReps) {
        // High rep ranges or time-based
        return setsReps.replaceAll("×\\d+", "×25");
    }
    
    private static String reduceIntensity(String setsReps) {
        // Reduce sets for beginners
        return setsReps.replaceAll("^\\d+", "2");
    }
    
    private static String increaseIntensity(String setsReps) {
        // Increase sets for advanced
        return setsReps.replaceAll("^\\d+", "5");
    }
    
    /**
     * Get workout recommendation message
     */
    public static String getWorkoutRecommendation(WorkoutGoal goal, FitnessLevel level) {
        StringBuilder recommendation = new StringBuilder();
        
        recommendation.append("🎯 Mục tiêu: ").append(getGoalDescription(goal)).append("\n");
        recommendation.append("📊 Trình độ: ").append(getLevelDescription(level)).append("\n\n");
        
        switch (goal) {
            case FAT_LOSS:
                recommendation.append("💡 Lời khuyên:\n");
                recommendation.append("• Tập trung vào HIIT và cardio\n");
                recommendation.append("• Giữ nhịp tim cao\n");
                recommendation.append("• Nghỉ ngắn giữa các set\n");
                recommendation.append("• Kết hợp với chế độ ăn deficit calories");
                break;
            case MUSCLE_BUILDING:
                recommendation.append("💡 Lời khuyên:\n");
                recommendation.append("• Tập trung vào compound movements\n");
                recommendation.append("• Progressive overload\n");
                recommendation.append("• Nghỉ đủ giữa các set\n");
                recommendation.append("• Ăn đủ protein và calories");
                break;
            case STRENGTH:
                recommendation.append("💡 Lời khuyên:\n");
                recommendation.append("• Ít reps, nhiều sets\n");
                recommendation.append("• Nghỉ dài giữa các set\n");
                recommendation.append("• Tập trung vào form\n");
                recommendation.append("• Tăng cường độ từ từ");
                break;
        }
        
        return recommendation.toString();
    }
    
    private static String getGoalDescription(WorkoutGoal goal) {
        switch (goal) {
            case FAT_LOSS: return "Giảm mỡ";
            case MUSCLE_BUILDING: return "Tăng cơ";
            case STRENGTH: return "Tăng sức mạnh";
            case ENDURANCE: return "Tăng sức bền";
            case GENERAL_FITNESS: return "Thể lực tổng quát";
            default: return "Không xác định";
        }
    }
    
    private static String getLevelDescription(FitnessLevel level) {
        switch (level) {
            case BEGINNER: return "Người mới";
            case INTERMEDIATE: return "Trung cấp";
            case ADVANCED: return "Nâng cao";
            default: return "Không xác định";
        }
    }
}