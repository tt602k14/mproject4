package com.htdgym.app.utils;

import com.htdgym.app.models.Workout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkoutDataHelper {
    
    /**
     * Lấy danh sách bài tập ngực với video YouTube
     */
    public static List<Workout> getChestWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        
        // Bài 1: Push-up
        Workout pushup = new Workout("Push-up", 4, 15, 0, 10, new Date());
        pushup.setCategory("chest");
        pushup.setVideoUrl("https://youtu.be/IODxDxX7oi4"); // Video tập ngực
        workouts.add(pushup);
        
        // Bài 2: Incline Push-up
        Workout inclinePushup = new Workout("Incline Push-up", 3, 12, 0, 8, new Date());
        inclinePushup.setCategory("chest");
        inclinePushup.setVideoUrl("https://youtu.be/cfQBmpfjObs");
        workouts.add(inclinePushup);
        
        // Bài 3: Decline Push-up
        Workout declinePushup = new Workout("Decline Push-up", 3, 10, 0, 8, new Date());
        declinePushup.setCategory("chest");
        declinePushup.setVideoUrl("https://youtu.be/rr5akuBqbfU");
        workouts.add(declinePushup);
        
        // Bài 4: Diamond Push-up
        Workout diamondPushup = new Workout("Diamond Push-up", 3, 10, 0, 8, new Date());
        diamondPushup.setCategory("chest");
        diamondPushup.setVideoUrl("https://youtu.be/J0DnG1_S92I");
        workouts.add(diamondPushup);
        
        // Bài 5: Wide Push-up
        Workout widePushup = new Workout("Wide Push-up", 3, 12, 0, 8, new Date());
        widePushup.setCategory("chest");
        widePushup.setVideoUrl("https://youtu.be/rr5akuBqbfU");
        workouts.add(widePushup);
        
        return workouts;
    }
    
    /**
     * Lấy danh sách bài tập vai
     */
    public static List<Workout> getShoulderWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        
        Workout shoulderPress = new Workout("Shoulder Press", 4, 10, 10, 10, new Date());
        shoulderPress.setCategory("shoulder");
        shoulderPress.setVideoUrl("https://youtu.be/qEwKCR5JCog"); // Thay bằng video vai
        workouts.add(shoulderPress);
        
        Workout lateralRaise = new Workout("Lateral Raise", 3, 12, 5, 8, new Date());
        lateralRaise.setCategory("shoulder");
        lateralRaise.setVideoUrl("https://youtu.be/3VcKaXpzqRo");
        workouts.add(lateralRaise);
        
        return workouts;
    }
    
    /**
     * Lấy danh sách bài tập chân
     */
    public static List<Workout> getLegsWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        
        Workout squat = new Workout("Squat", 4, 12, 20, 12, new Date());
        squat.setCategory("legs");
        squat.setVideoUrl("https://youtu.be/Xe1mCFljUN0"); // Thay bằng video chân
        workouts.add(squat);
        
        Workout lunge = new Workout("Lunge", 3, 10, 0, 10, new Date());
        lunge.setCategory("legs");
        lunge.setVideoUrl("https://youtu.be/QOVaHwm-Q6U");
        workouts.add(lunge);
        
        return workouts;
    }
    
    /**
     * Lấy danh sách bài tập bụng
     */
    public static List<Workout> getAbsWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        
        Workout plank = new Workout("Plank", 3, 30, 0, 5, new Date());
        plank.setCategory("abs");
        plank.setVideoUrl("https://youtu.be/pSHjTRCQxIw"); // Thay bằng video bụng
        workouts.add(plank);
        
        Workout crunch = new Workout("Crunch", 4, 20, 0, 8, new Date());
        crunch.setCategory("abs");
        crunch.setVideoUrl("https://youtu.be/1fbU_MkV7NE");
        workouts.add(crunch);
        
        return workouts;
    }
    
    /**
     * Lấy danh sách bài tập lưng
     */
    public static List<Workout> getBackWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        
        Workout pullup = new Workout("Pull-up", 4, 8, 0, 10, new Date());
        pullup.setCategory("back");
        pullup.setVideoUrl("https://youtu.be/HSoHeSjvIdY"); // Thay bằng video lưng
        workouts.add(pullup);
        
        Workout row = new Workout("Bent Over Row", 4, 10, 15, 10, new Date());
        row.setCategory("back");
        row.setVideoUrl("https://youtu.be/cc7kIfSUWEY");
        workouts.add(row);
        
        return workouts;
    }
    
    /**
     * Lấy tất cả bài tập
     */
    public static List<Workout> getAllWorkouts() {
        List<Workout> all = new ArrayList<>();
        all.addAll(getChestWorkouts());
        all.addAll(getShoulderWorkouts());
        all.addAll(getLegsWorkouts());
        all.addAll(getAbsWorkouts());
        all.addAll(getBackWorkouts());
        return all;
    }
}
