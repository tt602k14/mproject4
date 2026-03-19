package com.htdgym.app.utils;

import android.content.Context;
import android.util.Log;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.WorkoutLog;
import com.htdgym.app.models.WorkoutStats;
import java.util.Date;

public class WorkoutSaver {

    private static final String TAG = "WorkoutSaver";

    public static void save(Context context, String workoutName, int durationMinutes, int calories) {
        final Context appCtx = context.getApplicationContext();
        new Thread(() -> {
            try {
                GymDatabase db = GymDatabase.getInstance(appCtx);

                // Lấy userId — dùng cùng key với HomeFragment
                android.content.SharedPreferences prefs =
                        appCtx.getSharedPreferences("HTDGymPrefs", Context.MODE_PRIVATE);
                String statsUserId = prefs.getString("userId", "guest");
                if (statsUserId == null || statsUserId.isEmpty()) statsUserId = "guest";

                int numericUserId;
                try {
                    numericUserId = Integer.parseInt(statsUserId);
                } catch (NumberFormatException e) {
                    numericUserId = 1;
                }

                Log.d(TAG, "Saving workout: name=" + workoutName
                        + " dur=" + durationMinutes + " cal=" + calories
                        + " userId=" + statsUserId);

                // 1. WorkoutLog (cho ProgressFragment)
                db.workoutLogDao().insert(
                        new WorkoutLog(numericUserId, workoutName, new Date(), durationMinutes, calories));

                // 2. WorkoutStats (cho HomeFragment)
                WorkoutStats stats = db.workoutStatsDao().getStatsByUserId(statsUserId);
                if (stats == null) {
                    stats = new WorkoutStats();
                    stats.setUserId(statsUserId);
                    Log.d(TAG, "Creating new WorkoutStats for userId=" + statsUserId);
                } else {
                    Log.d(TAG, "Updating existing WorkoutStats id=" + stats.getId());
                }

                stats.setTotalCalories(stats.getTotalCalories() + calories);
                stats.setTotalSeconds(stats.getTotalSeconds() + durationMinutes * 60);
                stats.setTotalWorkouts(stats.getTotalWorkouts() + 1);

                long todayDay = System.currentTimeMillis() / (1000L * 60 * 60 * 24);
                long lastDay  = stats.getLastWorkoutDate()  / (1000L * 60 * 60 * 24);
                if (todayDay > lastDay) {
                    stats.setTotalDays(stats.getTotalDays() + 1);
                }
                stats.setLastWorkoutDate(System.currentTimeMillis());

                if (stats.getId() == 0) {
                    db.workoutStatsDao().insert(stats);
                } else {
                    db.workoutStatsDao().update(stats);
                }

                Log.d(TAG, "Saved OK — totalCal=" + stats.getTotalCalories()
                        + " totalWorkouts=" + stats.getTotalWorkouts()
                        + " totalDays=" + stats.getTotalDays());

            } catch (Exception e) {
                Log.e(TAG, "Error saving workout", e);
            }
        }).start();
    }
}
