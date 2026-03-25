package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.ExerciseCardAdapter;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.utils.ExerciseDataManager;
import com.htdgym.app.utils.Program45DaysManager;

import java.util.ArrayList;
import java.util.List;

public class ProgramExerciseListActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvProgramTitle, tvProgramDescription;
    private Button btnViewCalendar;
    private RecyclerView recyclerExercises;
    private ExerciseCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_exercise_list);

        initViews();
        setupRecyclerView();
        loadProgramData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvProgramTitle = findViewById(R.id.tv_program_title);
        tvProgramDescription = findViewById(R.id.tv_program_description);
        btnViewCalendar = findViewById(R.id.btn_view_calendar);
        recyclerExercises = findViewById(R.id.recycler_exercises);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new ExerciseCardAdapter(new ArrayList<>(), new ExerciseCardAdapter.OnExerciseClickListener() {
            @Override
            public void onExerciseClick(Exercise exercise) {
                try {
                    Intent intent = new Intent(ProgramExerciseListActivity.this, ExerciseDetailActivity.class);
                    intent.putExtra("exercise_name", exercise.getName());
                    intent.putExtra("muscle_group", exercise.getMuscleGroup());
                    intent.putExtra("sets_reps", exercise.getFormattedSetsReps());
                    intent.putExtra("rest_time", exercise.getRestTime());
                    intent.putExtra("difficulty", exercise.getDifficulty());
                    intent.putExtra("video_url", exercise.getVideoUrl());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ProgramExerciseListActivity.this,
                        "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFavoriteClick(Exercise exercise) {
                try {
                    exercise.setFavorite(!exercise.isFavorite());
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ProgramExerciseListActivity.this, 
                        "Lỗi cập nhật yêu thích: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerExercises.setAdapter(adapter);
    }

    private void loadProgramData() {
        String programType = getIntent().getStringExtra("program_type");
        String programTitle = getIntent().getStringExtra("program_title");

        if (programTitle != null) {
            tvProgramTitle.setText(programTitle);
        }

        List<Exercise> exercises = getProgramExercises(programType);
        
        // Cập nhật mô tả dựa trên loại chương trình
        if (programType != null && programType.contains("45")) {
            tvProgramDescription.setText("Chương trình 45 ngày hoàn chỉnh với " + exercises.size() + " bài tập được sắp xếp theo từng ngày");
            
            // Show calendar button for 45-day programs
            btnViewCalendar.setVisibility(android.view.View.VISIBLE);
            btnViewCalendar.setOnClickListener(v -> {
                Intent intent = new Intent(this, Program45DaysActivity.class);
                intent.putExtra("program_type", programType);
                intent.putExtra("program_title", programTitle);
                startActivity(intent);
            });
        } else {
            tvProgramDescription.setText("Chương trình bao gồm " + exercises.size() + " bài tập được thiết kế khoa học");
            btnViewCalendar.setVisibility(android.view.View.GONE);
        }
        
        adapter.updateExercises(exercises);
    }

    private List<Exercise> getProgramExercises(String programType) {
        List<Exercise> exercises = new ArrayList<>();

        if (programType == null) {
            exercises.addAll(ExerciseDataManager.getAllExercises());
            return exercises;
        }

        switch (programType) {
            case "abs_beginner":
                // Giảm Mỡ Bụng - Người Mới
                exercises.addAll(getAbsBeginnerExercises());
                break;
            case "fullbody_intermediate":
                // Giảm Mỡ Toàn Thân - Trung Cấp
                exercises.addAll(getFullBodyIntermediateExercises());
                break;
            case "hiit_advanced":
                // Đốt Mỡ Nhanh - Nâng Cao
                exercises.addAll(getHIITAdvancedExercises());
                break;
            case "cardio":
                // Cardio Giảm Cân Hiệu Quả
                exercises.addAll(getCardioExercises());
                break;
            case "tabata":
                // Tabata 4 Phút Đốt Mỡ
                exercises.addAll(getTabataExercises());
                break;
            case "shoulder_45":
                // Tập Tay Trong 45 Ngày - CHƯƠNG TRÌNH HOÀN CHỈNH
                exercises.addAll(Program45DaysManager.getShoulder45DaysProgram());
                break;
            case "chest_45":
                // Bộ Ngực Vạm Vỡ Trong 45 Ngày - CHƯƠNG TRÌNH HOÀN CHỈNH
                exercises.addAll(Program45DaysManager.getChest45DaysProgram());
                break;
            case "legs_45":
                // Đôi Chân Mạnh Mẽ Trong 45 Ngày - CHƯƠNG TRÌNH HOÀN CHỈNH
                exercises.addAll(Program45DaysManager.getLegs45DaysProgram());
                break;
            case "back_shoulder":
                // Bài Tập Lưng Và Vai
                exercises.addAll(getBackShoulderExercises());
                break;
            case "abs_6pack":
                // Cơ Bụng 6 Múi Hoàn Hảo
                exercises.addAll(getAbs6PackExercises());
                break;
            case "muscle_building":
                exercises.addAll(getBenchPressExercises());
                exercises.addAll(getOverheadPressExercises());
                exercises.addAll(getSquatStrengthExercises());
                exercises.addAll(getAbsBeginnerExercises());
                exercises.addAll(getBarbellRowExercises());
                break;
            case "bench_press":
                exercises.addAll(getBenchPressExercises());
                break;
            case "deadlift":
                exercises.addAll(getDeadliftExercises());
                break;
            case "squat_strength":
                exercises.addAll(getSquatStrengthExercises());
                break;
            case "pull_up":
                exercises.addAll(getPullUpExercises());
                break;
            case "overhead_press":
                exercises.addAll(getOverheadPressExercises());
                break;
            case "barbell_row":
                exercises.addAll(getBarbellRowExercises());
                break;
            case "dips":
                exercises.addAll(getDipsExercises());
                break;
            case "hiit_fullbody":
                exercises.addAll(getHiitFullbodyExercises());
                break;
            case "cardio_running":
                exercises.addAll(getCardioRunningExercises());
                break;
            case "tabata_burn":
                exercises.addAll(getTabataBurnExercises());
                break;
            case "jumping_burpees":
                exercises.addAll(getJumpingBurpeesExercises());
                break;
            case "jump_rope":
                exercises.addAll(getJumpRopeExercises());
                break;
            case "mountain_climber":
                exercises.addAll(getMountainClimberExercises());
                break;
            case "squat_lunge_circuit":
                exercises.addAll(getSquatLungeCircuitExercises());
                break;
            default:
                List<Exercise> all = ExerciseDataManager.getAllExercises();
                exercises.addAll(all.size() > 7 ? all.subList(0, 7) : all);
                break;
        }

        return exercises;
    }

    private List<Exercise> getAbsBeginnerExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise("Gập bụng cơ bản", "abs", "3×15", "30s", "#9C27B0", "Dễ", "https://youtu.be/1fbU_MkV7NE"));
        exercises.add(new Exercise("Plank cơ bản", "abs", "3×30s", "45s", "#9C27B0", "Dễ", "https://youtu.be/pSHjTRCQxIw"));
        exercises.add(new Exercise("Nâng chân nằm", "abs", "3×12", "30s", "#9C27B0", "Dễ", "https://youtu.be/JB2oyawG9KI"));
        exercises.add(new Exercise("Gập bụng chéo", "abs", "3×20", "30s", "#9C27B0", "Trung bình", "https://youtu.be/9FGilxCbdz8"));
        exercises.add(new Exercise("Plank nghiêng", "abs", "2×20s", "45s", "#9C27B0", "Trung bình", "https://youtu.be/K2VljzCC16g"));
        exercises.add(new Exercise("Đá chân nhanh", "abs", "3×20s", "30s", "#9C27B0", "Dễ", "https://youtu.be/ANVdMDaYRts"));
        exercises.add(new Exercise("Leo núi", "abs", "3×20s", "30s", "#9C27B0", "Trung bình", "https://youtu.be/nmwgirgXLYM"));
        exercises.add(new Exercise("Hollow Body Hold", "abs", "3×20s", "30s", "#9C27B0", "Trung bình", "https://youtu.be/pSHjTRCQxIw"));
        exercises.add(new Exercise("Dead Bug", "abs", "3×10", "30s", "#9C27B0", "Dễ", "https://youtu.be/wiFNA3sqjCA"));
        exercises.add(new Exercise("Toe Touch Crunch", "abs", "3×15", "30s", "#9C27B0", "Dễ", "https://youtu.be/ANVdMDaYRts"));
        exercises.add(new Exercise("V-Up", "abs", "3×12", "30s", "#9C27B0", "Khó", "https://youtu.be/7UVgs18Y1P4"));
        exercises.add(new Exercise("Russian Twist", "abs", "3×20", "30s", "#9C27B0", "Trung bình", "https://youtu.be/wkD8rjkodUI"));
        exercises.add(new Exercise("Bicycle Crunch", "abs", "3×20", "30s", "#9C27B0", "Trung bình", "https://youtu.be/9FGilxCbdz8"));
        exercises.add(new Exercise("Ab Wheel Rollout", "abs", "3×10", "45s", "#9C27B0", "Khó", "https://youtu.be/pSHjTRCQxIw"));
        exercises.add(new Exercise("Dragon Flag", "abs", "3×6", "60s", "#9C27B0", "Khó", "https://youtu.be/pSHjTRCQxIw"));
        exercises.add(new Exercise("Hanging Knee Raise", "abs", "3×15", "45s", "#9C27B0", "Trung bình", "https://youtu.be/JB2oyawG9KI"));
        // Giãn cơ bụng
        exercises.add(new Exercise("🧘 Giãn cơ Cobra Stretch", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/JnHnd4SNKR8"));
        exercises.add(new Exercise("🧘 Giãn cơ Cat-Cow", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/kqnua4rHVVA"));
        exercises.add(new Exercise("🧘 Child's Pose - Giãn lưng dưới", "stretch", "1×60s", "0s", "#66BB6A", "Dễ", "https://youtu.be/eqVMAPM00GM"));
        return exercises;
    }

    private List<Exercise> getFullBodyIntermediateExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise("Burpees", "hiit", "3×10", "45s", "#E91E63", "Khó", "https://youtu.be/JZQA08SlJnM"));
        exercises.add(new Exercise("Squat nhảy", "legs", "3×15", "30s", "#FFA726", "Trung bình", "https://youtu.be/A-cFYWvaHr0"));
        exercises.add(new Exercise("Hít đất", "chest", "3×12", "45s", "#FF6B6B", "Trung bình", "https://youtu.be/IODxDxX7oi4"));
        exercises.add(new Exercise("Leo núi", "abs", "3×30s", "30s", "#9C27B0", "Trung bình", "https://youtu.be/nmwgirgXLYM"));
        exercises.add(new Exercise("Plank", "abs", "3×45s", "60s", "#9C27B0", "Trung bình", "https://youtu.be/pSHjTRCQxIw"));
        exercises.add(new Exercise("Bật nhảy", "hiit", "3×20", "30s", "#E91E63", "Dễ", "https://youtu.be/c4DAnQ6DtF8"));
        exercises.add(new Exercise("Chạy nâng đùi", "hiit", "3×30s", "30s", "#E91E63", "Trung bình", "https://youtu.be/8opcQdC-V-U"));
        exercises.add(new Exercise("Gánh đùi", "legs", "3×20", "45s", "#FFA726", "Dễ", "https://youtu.be/Xe1mCFljUN0"));
        return exercises;
    }

    private List<Exercise> getHIITAdvancedExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise("Burpees nâng cao", "hiit", "4×15", "30s", "#E91E63", "Khó", "https://youtu.be/JZQA08SlJnM"));
        exercises.add(new Exercise("Tabata 4 phút", "hiit", "8×20s", "10s", "#E91E63", "Khó", "https://youtu.be/20Khkl95_qA"));
        exercises.add(new Exercise("Squat nhảy cao", "legs", "4×20", "30s", "#FFA726", "Khó", "https://youtu.be/A-cFYWvaHr0"));
        exercises.add(new Exercise("Leo núi nhanh", "abs", "4×45s", "30s", "#9C27B0", "Khó", "https://youtu.be/nmwgirgXLYM"));
        exercises.add(new Exercise("Hít đất kim cương", "chest", "3×12", "60s", "#FF6B6B", "Khó", "https://youtu.be/J0DnG1_S92I"));
        exercises.add(new Exercise("Plank nhảy", "abs", "3×30s", "45s", "#9C27B0", "Khó", "https://youtu.be/K2VljzCC16g"));
        exercises.add(new Exercise("Chạy tại chỗ", "hiit", "4×60s", "30s", "#E91E63", "Trung bình", "https://youtu.be/8opcQdC-V-U"));
        return exercises;
    }

    private List<Exercise> getCardioExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise("Chạy tại chỗ", "hiit", "5×60s", "30s", "#E91E63", "Dễ", "https://youtu.be/8opcQdC-V-U"));
        exercises.add(new Exercise("Bật nhảy", "hiit", "4×30", "30s", "#E91E63", "Dễ", "https://youtu.be/c4DAnQ6DtF8"));
        exercises.add(new Exercise("Leo núi", "abs", "4×45s", "30s", "#9C27B0", "Trung bình", "https://youtu.be/nmwgirgXLYM"));
        exercises.add(new Exercise("Squat nhảy", "legs", "4×20", "30s", "#FFA726", "Trung bình", "https://youtu.be/A-cFYWvaHr0"));
        exercises.add(new Exercise("Burpees", "hiit", "3×12", "45s", "#E91E63", "Khó", "https://youtu.be/JZQA08SlJnM"));
        exercises.add(new Exercise("Đá chân nhanh", "abs", "4×30s", "30s", "#9C27B0", "Trung bình", "https://youtu.be/ANVdMDaYRts"));
        exercises.add(new Exercise("Chạy nâng đùi", "hiit", "4×45s", "30s", "#E91E63", "Trung bình", "https://youtu.be/8opcQdC-V-U"));
        return exercises;
    }

    private List<Exercise> getTabataExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise("Tabata Burpees", "hiit", "4×20s", "10s", "#E91E63", "Khó", "https://youtu.be/JZQA08SlJnM"));
        exercises.add(new Exercise("Tabata Squat", "legs", "4×20s", "10s", "#FFA726", "Trung bình", "https://youtu.be/A-cFYWvaHr0"));
        exercises.add(new Exercise("Tabata Leo núi", "abs", "4×20s", "10s", "#9C27B0", "Khó", "https://youtu.be/nmwgirgXLYM"));
        exercises.add(new Exercise("Tabata Hít đất", "chest", "4×20s", "10s", "#FF6B6B", "Trung bình", "https://youtu.be/IODxDxX7oi4"));
        exercises.add(new Exercise("Tabata Bật nhảy", "hiit", "4×20s", "10s", "#E91E63", "Dễ", "https://youtu.be/c4DAnQ6DtF8"));
        exercises.add(new Exercise("Tabata Plank", "abs", "4×20s", "10s", "#9C27B0", "Trung bình", "https://youtu.be/pSHjTRCQxIw"));
        exercises.add(new Exercise("Tabata Chạy", "hiit", "4×20s", "10s", "#E91E63", "Dễ", "https://youtu.be/8opcQdC-V-U"));
        return exercises;
    }

    private List<Exercise> getShoulderExercises() {
        return ExerciseDataManager.getShoulderExercises();
    }

    private List<Exercise> getChestExercises() {
        return ExerciseDataManager.getChestExercises();
    }

    private List<Exercise> getLegsExercises() {
        return ExerciseDataManager.getLegsExercises().subList(0, 7);
    }

    private List<Exercise> getBackShoulderExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exercises.addAll(ExerciseDataManager.getBackExercises().subList(0, 4));
        exercises.addAll(ExerciseDataManager.getShoulderExercises().subList(0, 3));
        return exercises;
    }

    private List<Exercise> getAbs6PackExercises() {
        return ExerciseDataManager.getAbsExercises().subList(0, 7);
    }

    private List<Exercise> getBenchPressExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Bench Press cơ bản", "chest", "4×8", "90s", "#FF6B6B", "Trung bình", "https://youtu.be/rT7DgCr-3pg"));
        list.add(new Exercise("Incline Bench Press", "chest", "4×10", "90s", "#FF6B6B", "Trung bình", "https://youtu.be/DbFgADa2PL8"));
        list.add(new Exercise("Decline Bench Press", "chest", "3×10", "90s", "#FF6B6B", "Trung bình", "https://youtu.be/LfyQBUKR8SE"));
        list.add(new Exercise("Dumbbell Fly", "chest", "3×12", "60s", "#FF6B6B", "Dễ", "https://youtu.be/eozdVDA78K0"));
        list.add(new Exercise("Hít đất rộng", "chest", "3×15", "60s", "#FF6B6B", "Dễ", "https://youtu.be/5Qv2T8VusME"));
        list.add(new Exercise("Cable Crossover", "chest", "3×12", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/taI4XduLpTk"));
        list.add(new Exercise("Hít đất kim cương", "chest", "3×12", "60s", "#FF6B6B", "Khó", "https://youtu.be/J0DnG1_S92I"));
        list.add(new Exercise("Pec Deck Machine", "chest", "3×12", "60s", "#FF6B6B", "Dễ", "https://youtu.be/Z57CtFmRMxA"));
        list.add(new Exercise("Push-Up to T", "chest", "3×10", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/5Qv2T8VusME"));
        list.add(new Exercise("Svend Press", "chest", "3×15", "45s", "#FF6B6B", "Dễ", "https://youtu.be/rT7DgCr-3pg"));
        list.add(new Exercise("Landmine Press", "chest", "3×10", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/DbFgADa2PL8"));
        list.add(new Exercise("Dumbbell Pullover", "chest", "3×12", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/eozdVDA78K0"));
        list.add(new Exercise("Hít đất nâng tay", "chest", "3×10", "60s", "#FF6B6B", "Khó", "https://youtu.be/5Qv2T8VusME"));
        list.add(new Exercise("Chest Dip", "chest", "3×12", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/2z8JmcrW-As"));
        list.add(new Exercise("Incline Dumbbell Fly", "chest", "3×12", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/eozdVDA78K0"));
        list.add(new Exercise("Hít đất Archer", "chest", "3×8", "60s", "#FF6B6B", "Khó", "https://youtu.be/J0DnG1_S92I"));
        // Giãn cơ ngực
        list.add(new Exercise("🧘 Giãn cơ ngực - Doorway Stretch", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/eozdVDA78K0"));
        list.add(new Exercise("🧘 Giãn cơ ngực - Chest Opener", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/5Qv2T8VusME"));
        list.add(new Exercise("🧘 Foam Roll ngực", "stretch", "1×60s", "0s", "#66BB6A", "Dễ", "https://youtu.be/rT7DgCr-3pg"));
        return list;
    }

    private List<Exercise> getDeadliftExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Deadlift cơ bản", "back", "4×5", "120s", "#795548", "Khó", "https://youtu.be/op9kVnSso6Q"));
        list.add(new Exercise("Romanian Deadlift", "back", "4×8", "90s", "#795548", "Trung bình", "https://youtu.be/JCXUYuzwNrM"));
        list.add(new Exercise("Sumo Deadlift", "legs", "3×6", "120s", "#FFA726", "Khó", "https://youtu.be/WsepIUGZu0c"));
        list.add(new Exercise("Hex Bar Deadlift", "back", "3×8", "90s", "#795548", "Trung bình", "https://youtu.be/XxWcirHIwVo"));
        list.add(new Exercise("Tư thế siêu nhân", "back", "3×15", "45s", "#795548", "Dễ", "https://youtu.be/7SLbUk-qTTM"));
        list.add(new Exercise("Good Morning", "back", "3×12", "60s", "#795548", "Trung bình", "https://youtu.be/YA-h3n9L4YU"));
        list.add(new Exercise("Hyperextension", "back", "3×15", "45s", "#795548", "Dễ", "https://youtu.be/ph3pddpKzzw"));
        list.add(new Exercise("Stiff-Leg Deadlift", "back", "3×10", "90s", "#795548", "Trung bình", "https://youtu.be/JCXUYuzwNrM"));
        list.add(new Exercise("Single-Leg Deadlift", "back", "3×10", "60s", "#795548", "Khó", "https://youtu.be/op9kVnSso6Q"));
        list.add(new Exercise("Rack Pull", "back", "3×6", "120s", "#795548", "Khó", "https://youtu.be/op9kVnSso6Q"));
        // Giãn cơ lưng & gân kheo
        list.add(new Exercise("🧘 Giãn gân kheo - Standing Hamstring", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/JCXUYuzwNrM"));
        list.add(new Exercise("🧘 Giãn lưng dưới - Knee to Chest", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/eqVMAPM00GM"));
        list.add(new Exercise("🧘 Pigeon Pose - Giãn hông", "stretch", "2×30s", "0s", "#66BB6A", "Dễ", "https://youtu.be/kqnua4rHVVA"));
        return list;
    }

    private List<Exercise> getSquatStrengthExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Back Squat", "legs", "4×6", "120s", "#FFA726", "Khó", "https://youtu.be/ultWZbUMPL8"));
        list.add(new Exercise("Front Squat", "legs", "3×8", "90s", "#FFA726", "Khó", "https://youtu.be/m4ytaCJZpl0"));
        list.add(new Exercise("Goblet Squat", "legs", "3×12", "60s", "#FFA726", "Trung bình", "https://youtu.be/MxsFDhcyFyE"));
        list.add(new Exercise("Bulgarian Split Squat", "legs", "3×10", "90s", "#FFA726", "Khó", "https://youtu.be/2C-uStzPJWY"));
        list.add(new Exercise("Leg Press", "legs", "4×12", "90s", "#FFA726", "Trung bình", "https://youtu.be/IZxyjW7MPJQ"));
        list.add(new Exercise("Hack Squat", "legs", "3×10", "90s", "#FFA726", "Trung bình", "https://youtu.be/0tn5K9NlCfo"));
        list.add(new Exercise("Nâng bắp chân đứng", "legs", "4×20", "30s", "#FFA726", "Dễ", "https://youtu.be/gwLzBJYoWlI"));
        list.add(new Exercise("Leg Extension", "legs", "3×15", "60s", "#FFA726", "Dễ", "https://youtu.be/YyvSfVjQeL0"));
        list.add(new Exercise("Leg Curl", "legs", "3×12", "60s", "#FFA726", "Dễ", "https://youtu.be/1Tq3QdYUuHs"));
        list.add(new Exercise("Lunge bước tiến", "legs", "3×12", "60s", "#FFA726", "Trung bình", "https://youtu.be/QOVaHwm-Q6U"));
        list.add(new Exercise("Step-Up", "legs", "3×12", "60s", "#FFA726", "Trung bình", "https://youtu.be/ultWZbUMPL8"));
        list.add(new Exercise("Sumo Squat", "legs", "3×15", "60s", "#FFA726", "Dễ", "https://youtu.be/WsepIUGZu0c"));
        list.add(new Exercise("Wall Sit", "legs", "3×45s", "45s", "#FFA726", "Trung bình", "https://youtu.be/ultWZbUMPL8"));
        list.add(new Exercise("Box Squat", "legs", "3×8", "90s", "#FFA726", "Trung bình", "https://youtu.be/ultWZbUMPL8"));
        list.add(new Exercise("Sissy Squat", "legs", "3×12", "60s", "#FFA726", "Khó", "https://youtu.be/m4ytaCJZpl0"));
        list.add(new Exercise("Nordic Hamstring Curl", "legs", "3×8", "90s", "#FFA726", "Khó", "https://youtu.be/1Tq3QdYUuHs"));
        // Giãn cơ chân
        list.add(new Exercise("🧘 Giãn cơ tứ đầu - Standing Quad", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/gwLzBJYoWlI"));
        list.add(new Exercise("🧘 Giãn gân kheo - Seated Forward Fold", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/JCXUYuzwNrM"));
        list.add(new Exercise("🧘 Giãn bắp chân - Calf Stretch", "stretch", "2×30s", "0s", "#66BB6A", "Dễ", "https://youtu.be/gwLzBJYoWlI"));
        list.add(new Exercise("🧘 Pigeon Pose - Giãn hông & mông", "stretch", "2×30s", "0s", "#66BB6A", "Dễ", "https://youtu.be/kqnua4rHVVA"));
        return list;
    }

    private List<Exercise> getPullUpExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Pull-Up cơ bản", "back", "4×8", "90s", "#795548", "Khó", "https://youtu.be/eGo4IYlbE5g"));
        list.add(new Exercise("Chin-Up", "back", "3×10", "90s", "#795548", "Trung bình", "https://youtu.be/sIiargZZS3s"));
        list.add(new Exercise("Wide Grip Pull-Up", "back", "3×8", "90s", "#795548", "Khó", "https://youtu.be/G_j_to_3_Hs"));
        list.add(new Exercise("Lat Pulldown", "back", "4×12", "60s", "#795548", "Trung bình", "https://youtu.be/CAwf7n6Luuc"));
        list.add(new Exercise("Seated Cable Row", "back", "3×12", "60s", "#795548", "Trung bình", "https://youtu.be/GZbfZ033f74"));
        list.add(new Exercise("Thiên thần ngược", "back", "3×12", "45s", "#795548", "Trung bình", "https://youtu.be/HSoHeSjvIdY"));
        list.add(new Exercise("Nâng tay chữ Y", "back", "3×15", "45s", "#795548", "Dễ", "https://youtu.be/aKNn76T3RPE"));
        list.add(new Exercise("Neutral Grip Pull-Up", "back", "3×8", "90s", "#795548", "Khó", "https://youtu.be/eGo4IYlbE5g"));
        list.add(new Exercise("Assisted Pull-Up", "back", "4×10", "60s", "#795548", "Dễ", "https://youtu.be/eGo4IYlbE5g"));
        list.add(new Exercise("Scapular Pull-Up", "back", "3×12", "45s", "#795548", "Dễ", "https://youtu.be/eGo4IYlbE5g"));
        list.add(new Exercise("Straight-Arm Pulldown", "back", "3×15", "45s", "#795548", "Dễ", "https://youtu.be/CAwf7n6Luuc"));
        list.add(new Exercise("Single-Arm Lat Pulldown", "back", "3×12", "60s", "#795548", "Trung bình", "https://youtu.be/CAwf7n6Luuc"));
        list.add(new Exercise("Renegade Row", "back", "3×10", "60s", "#795548", "Khó", "https://youtu.be/G8l_8chR5BE"));
        // Giãn cơ lưng & vai
        list.add(new Exercise("🧘 Giãn cơ lưng - Lat Stretch", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/CAwf7n6Luuc"));
        list.add(new Exercise("🧘 Giãn cơ vai - Cross-Body Stretch", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/aKNn76T3RPE"));
        list.add(new Exercise("🧘 Child's Pose - Giãn lưng toàn diện", "stretch", "1×60s", "0s", "#66BB6A", "Dễ", "https://youtu.be/eqVMAPM00GM"));
        return list;
    }

    private List<Exercise> getOverheadPressExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Overhead Press", "shoulder", "4×8", "90s", "#4ECDC4", "Khó", "https://youtu.be/2yjwXTZQDDI"));
        list.add(new Exercise("Dumbbell Shoulder Press", "shoulder", "3×10", "90s", "#4ECDC4", "Trung bình", "https://youtu.be/qEwKCR5JCog"));
        list.add(new Exercise("Arnold Press", "shoulder", "3×12", "60s", "#4ECDC4", "Trung bình", "https://youtu.be/6Z15_WdXmVw"));
        list.add(new Exercise("Lateral Raise", "shoulder", "4×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/3VcKaXpzqRo"));
        list.add(new Exercise("Front Raise", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/qM5X-_FfCQE"));
        list.add(new Exercise("Rear Delt Fly", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/EA7u4Q_8HQ0"));
        list.add(new Exercise("Upright Row", "shoulder", "3×12", "60s", "#4ECDC4", "Trung bình", "https://youtu.be/um3LNkMEFMg"));
        list.add(new Exercise("Face Pull", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/rep-qVOkqgk"));
        list.add(new Exercise("Cable Lateral Raise", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/3VcKaXpzqRo"));
        list.add(new Exercise("Shrug - Nâng vai", "shoulder", "4×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/um3LNkMEFMg"));
        list.add(new Exercise("Bent-Over Lateral Raise", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/EA7u4Q_8HQ0"));
        list.add(new Exercise("Push Press", "shoulder", "3×8", "90s", "#4ECDC4", "Khó", "https://youtu.be/2yjwXTZQDDI"));
        list.add(new Exercise("Plate Front Raise", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/qM5X-_FfCQE"));
        list.add(new Exercise("Cable Face Pull", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/rep-qVOkqgk"));
        list.add(new Exercise("Dumbbell Y-Raise", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/aKNn76T3RPE"));
        list.add(new Exercise("Seated Dumbbell Press", "shoulder", "3×10", "90s", "#4ECDC4", "Trung bình", "https://youtu.be/qEwKCR5JCog"));
        // Giãn cơ vai & cổ
        list.add(new Exercise("🧘 Giãn cơ vai - Shoulder Cross Stretch", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/aKNn76T3RPE"));
        list.add(new Exercise("🧘 Giãn cổ - Neck Side Stretch", "stretch", "2×20s", "15s", "#66BB6A", "Dễ", "https://youtu.be/qEwKCR5JCog"));
        list.add(new Exercise("🧘 Giãn vai sau - Doorway Stretch", "stretch", "2×30s", "0s", "#66BB6A", "Dễ", "https://youtu.be/EA7u4Q_8HQ0"));
        return list;
    }

    private List<Exercise> getBarbellRowExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Barbell Row", "back", "4×8", "90s", "#795548", "Khó", "https://youtu.be/G8l_8chR5BE"));
        list.add(new Exercise("Dumbbell Row", "back", "3×10", "60s", "#795548", "Trung bình", "https://youtu.be/pYcpY20QaE8"));
        list.add(new Exercise("T-Bar Row", "back", "3×10", "90s", "#795548", "Khó", "https://youtu.be/j3Igk5nyZE4"));
        list.add(new Exercise("Seated Cable Row", "back", "3×12", "60s", "#795548", "Trung bình", "https://youtu.be/GZbfZ033f74"));
        list.add(new Exercise("Mèo - Bò", "back", "3×15", "30s", "#795548", "Dễ", "https://youtu.be/kqnua4rHVVA"));
        list.add(new Exercise("Chó chim", "back", "3×12", "45s", "#795548", "Dễ", "https://youtu.be/wiFNA3sqjCA"));
        list.add(new Exercise("Duỗi lưng", "back", "3×15", "45s", "#795548", "Dễ", "https://youtu.be/cc7kIfSUWEY"));
        list.add(new Exercise("Chest-Supported Row", "back", "3×12", "60s", "#795548", "Trung bình", "https://youtu.be/pYcpY20QaE8"));
        list.add(new Exercise("Inverted Row", "back", "3×12", "60s", "#795548", "Trung bình", "https://youtu.be/G8l_8chR5BE"));
        list.add(new Exercise("Meadows Row", "back", "3×10", "60s", "#795548", "Khó", "https://youtu.be/j3Igk5nyZE4"));
        list.add(new Exercise("Pendlay Row", "back", "3×8", "90s", "#795548", "Khó", "https://youtu.be/G8l_8chR5BE"));
        list.add(new Exercise("Kroc Row", "back", "3×15", "60s", "#795548", "Trung bình", "https://youtu.be/pYcpY20QaE8"));
        list.add(new Exercise("Seal Row", "back", "3×10", "60s", "#795548", "Trung bình", "https://youtu.be/G8l_8chR5BE"));
        list.add(new Exercise("Rack Pull", "back", "3×6", "120s", "#795548", "Khó", "https://youtu.be/op9kVnSso6Q"));
        list.add(new Exercise("Cable Straight-Arm Pulldown", "back", "3×15", "45s", "#795548", "Dễ", "https://youtu.be/CAwf7n6Luuc"));
        list.add(new Exercise("Dumbbell Pullover - Lưng", "back", "3×12", "60s", "#795548", "Trung bình", "https://youtu.be/pYcpY20QaE8"));
        // Giãn cơ lưng
        list.add(new Exercise("🧘 Giãn lưng - Thoracic Rotation", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/kqnua4rHVVA"));
        list.add(new Exercise("🧘 Giãn lưng giữa - Thread the Needle", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/wiFNA3sqjCA"));
        list.add(new Exercise("🧘 Child's Pose mở rộng", "stretch", "1×60s", "0s", "#66BB6A", "Dễ", "https://youtu.be/eqVMAPM00GM"));
        return list;
    }

    private List<Exercise> getDipsExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Dips cơ bản", "chest", "4×10", "90s", "#FF6B6B", "Trung bình", "https://youtu.be/2z8JmcrW-As"));
        list.add(new Exercise("Bench Dips", "chest", "3×15", "60s", "#FF6B6B", "Dễ", "https://youtu.be/c3ZGl4pnLZs"));
        list.add(new Exercise("Weighted Dips", "chest", "3×8", "120s", "#FF6B6B", "Khó", "https://youtu.be/sM_s835WNDI"));
        list.add(new Exercise("Tricep Pushdown", "shoulder", "4×12", "60s", "#4ECDC4", "Dễ", "https://youtu.be/2-LAMcpzODU"));
        list.add(new Exercise("Skull Crusher", "shoulder", "3×12", "60s", "#4ECDC4", "Trung bình", "https://youtu.be/d_KZxkY_0cM"));
        list.add(new Exercise("Overhead Tricep Extension", "shoulder", "3×12", "60s", "#4ECDC4", "Trung bình", "https://youtu.be/nRiJVZDpdL0"));
        list.add(new Exercise("Diamond Push-Up", "chest", "3×12", "60s", "#FF6B6B", "Khó", "https://youtu.be/J0DnG1_S92I"));
        list.add(new Exercise("Close-Grip Bench Press", "chest", "3×10", "90s", "#FF6B6B", "Trung bình", "https://youtu.be/rT7DgCr-3pg"));
        list.add(new Exercise("Tricep Kickback", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/2-LAMcpzODU"));
        list.add(new Exercise("Cable Tricep Extension", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/nRiJVZDpdL0"));
        // Giãn cơ tay sau & ngực
        list.add(new Exercise("🧘 Giãn cơ tay sau - Tricep Stretch", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/nRiJVZDpdL0"));
        list.add(new Exercise("🧘 Giãn ngực - Chest Opener", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/eozdVDA78K0"));
        list.add(new Exercise("🧘 Giãn vai trước - Front Shoulder Stretch", "stretch", "2×20s", "0s", "#66BB6A", "Dễ", "https://youtu.be/EA7u4Q_8HQ0"));
        return list;
    }

    private List<Exercise> getHiitFullbodyExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Burpees toàn thân", "hiit", "4×15", "30s", "#E91E63", "Khó", "https://youtu.be/JZQA08SlJnM"));
        list.add(new Exercise("High Knees", "hiit", "4×45s", "20s", "#E91E63", "Trung bình", "https://youtu.be/8opcQdC-V-U"));
        list.add(new Exercise("Squat nhảy", "hiit", "4×20", "30s", "#E91E63", "Trung bình", "https://youtu.be/A-cFYWvaHr0"));
        list.add(new Exercise("Leo núi nhanh", "hiit", "4×45s", "20s", "#E91E63", "Khó", "https://youtu.be/nmwgirgXLYM"));
        list.add(new Exercise("Hít đất nhanh", "hiit", "3×15", "30s", "#E91E63", "Trung bình", "https://youtu.be/-R5sH2iG9Gw"));
        list.add(new Exercise("Plank to Push-Up", "hiit", "3×12", "45s", "#E91E63", "Khó", "https://youtu.be/pSHjTRCQxIw"));
        list.add(new Exercise("Box Jump", "hiit", "3×15", "45s", "#E91E63", "Khó", "https://youtu.be/c4DAnQ6DtF8"));
        list.add(new Exercise("Bear Crawl", "hiit", "3×30s", "30s", "#E91E63", "Trung bình", "https://youtu.be/JZQA08SlJnM"));
        list.add(new Exercise("Inchworm", "hiit", "3×10", "30s", "#E91E63", "Trung bình", "https://youtu.be/nmwgirgXLYM"));
        list.add(new Exercise("Jumping Lunge", "hiit", "3×16", "45s", "#E91E63", "Khó", "https://youtu.be/A-cFYWvaHr0"));
        // Giãn cơ toàn thân
        list.add(new Exercise("🧘 Giãn toàn thân - World's Greatest Stretch", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/kqnua4rHVVA"));
        list.add(new Exercise("🧘 Giãn hông - Hip Flexor Stretch", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/eqVMAPM00GM"));
        list.add(new Exercise("🧘 Hít thở sâu - Cool Down", "stretch", "1×60s", "0s", "#66BB6A", "Dễ", "https://youtu.be/pSHjTRCQxIw"));
        return list;
    }

    private List<Exercise> getCardioRunningExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Chạy tại chỗ", "hiit", "5×60s", "20s", "#E91E63", "Dễ", "https://youtu.be/8opcQdC-V-U"));
        list.add(new Exercise("High Knees", "hiit", "4×45s", "20s", "#E91E63", "Trung bình", "https://youtu.be/8opcQdC-V-U"));
        list.add(new Exercise("Butt Kicks", "hiit", "4×45s", "20s", "#E91E63", "Dễ", "https://youtu.be/ANVdMDaYRts"));
        list.add(new Exercise("Lateral Shuffle", "hiit", "4×30s", "30s", "#E91E63", "Trung bình", "https://youtu.be/c4DAnQ6DtF8"));
        list.add(new Exercise("Sprint tại chỗ", "hiit", "6×20s", "10s", "#E91E63", "Khó", "https://youtu.be/8opcQdC-V-U"));
        list.add(new Exercise("Bước chân nhanh", "hiit", "4×30s", "20s", "#E91E63", "Dễ", "https://youtu.be/ANVdMDaYRts"));
        list.add(new Exercise("Chạy zigzag", "hiit", "4×30s", "30s", "#E91E63", "Trung bình", "https://youtu.be/8opcQdC-V-U"));
        return list;
    }

    private List<Exercise> getTabataBurnExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Tabata Burpees", "hiit", "8×20s", "10s", "#E91E63", "Khó", "https://youtu.be/JZQA08SlJnM"));
        list.add(new Exercise("Tabata Squat nhảy", "hiit", "8×20s", "10s", "#E91E63", "Trung bình", "https://youtu.be/A-cFYWvaHr0"));
        list.add(new Exercise("Tabata Leo núi", "hiit", "8×20s", "10s", "#E91E63", "Khó", "https://youtu.be/nmwgirgXLYM"));
        list.add(new Exercise("Tabata High Knees", "hiit", "8×20s", "10s", "#E91E63", "Trung bình", "https://youtu.be/8opcQdC-V-U"));
        list.add(new Exercise("Tabata Hít đất", "hiit", "8×20s", "10s", "#E91E63", "Trung bình", "https://youtu.be/-R5sH2iG9Gw"));
        list.add(new Exercise("Tabata Plank", "hiit", "8×20s", "10s", "#E91E63", "Trung bình", "https://youtu.be/pSHjTRCQxIw"));
        list.add(new Exercise("Tabata Bật nhảy", "hiit", "8×20s", "10s", "#E91E63", "Dễ", "https://youtu.be/c4DAnQ6DtF8"));
        return list;
    }

    private List<Exercise> getJumpingBurpeesExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Burpees cơ bản", "hiit", "4×12", "45s", "#E91E63", "Khó", "https://youtu.be/JZQA08SlJnM"));
        list.add(new Exercise("Jumping Jacks", "hiit", "4×40", "20s", "#E91E63", "Dễ", "https://youtu.be/c4DAnQ6DtF8"));
        list.add(new Exercise("Burpees không nhảy", "hiit", "3×15", "30s", "#E91E63", "Trung bình", "https://youtu.be/JZQA08SlJnM"));
        list.add(new Exercise("Star Jumps", "hiit", "3×20", "30s", "#E91E63", "Trung bình", "https://youtu.be/c4DAnQ6DtF8"));
        list.add(new Exercise("Tuck Jumps", "hiit", "3×15", "45s", "#E91E63", "Khó", "https://youtu.be/A-cFYWvaHr0"));
        list.add(new Exercise("Broad Jumps", "hiit", "3×10", "60s", "#E91E63", "Khó", "https://youtu.be/c4DAnQ6DtF8"));
        list.add(new Exercise("Lateral Jumps", "hiit", "3×20", "30s", "#E91E63", "Trung bình", "https://youtu.be/c4DAnQ6DtF8"));
        return list;
    }

    private List<Exercise> getJumpRopeExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Nhảy dây cơ bản", "hiit", "5×60s", "30s", "#E91E63", "Dễ", "https://youtu.be/FJmRQ5iTXKE"));
        list.add(new Exercise("Nhảy dây nhanh", "hiit", "5×30s", "20s", "#E91E63", "Trung bình", "https://youtu.be/FJmRQ5iTXKE"));
        list.add(new Exercise("Nhảy dây 1 chân", "hiit", "3×30s", "30s", "#E91E63", "Khó", "https://youtu.be/FJmRQ5iTXKE"));
        list.add(new Exercise("Double Under", "hiit", "3×20", "45s", "#E91E63", "Khó", "https://youtu.be/FJmRQ5iTXKE"));
        list.add(new Exercise("Nhảy dây chéo tay", "hiit", "3×30s", "30s", "#E91E63", "Trung bình", "https://youtu.be/FJmRQ5iTXKE"));
        list.add(new Exercise("Nhảy dây nâng đùi cao", "hiit", "4×30s", "20s", "#E91E63", "Trung bình", "https://youtu.be/FJmRQ5iTXKE"));
        list.add(new Exercise("Nhảy dây boxing", "hiit", "4×45s", "20s", "#E91E63", "Khó", "https://youtu.be/FJmRQ5iTXKE"));
        return list;
    }

    private List<Exercise> getMountainClimberExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Leo núi cơ bản", "hiit", "4×45s", "20s", "#E91E63", "Trung bình", "https://youtu.be/nmwgirgXLYM"));
        list.add(new Exercise("Leo núi chéo", "hiit", "4×45s", "20s", "#E91E63", "Trung bình", "https://youtu.be/nmwgirgXLYM"));
        list.add(new Exercise("Leo núi siêu tốc", "hiit", "4×30s", "20s", "#E91E63", "Khó", "https://youtu.be/nmwgirgXLYM"));
        list.add(new Exercise("Plank cơ bản", "hiit", "4×45s", "30s", "#E91E63", "Trung bình", "https://youtu.be/pSHjTRCQxIw"));
        list.add(new Exercise("Plank to Downdog", "hiit", "3×12", "30s", "#E91E63", "Trung bình", "https://youtu.be/pSHjTRCQxIw"));
        list.add(new Exercise("Spider-Man Plank", "hiit", "3×12", "30s", "#E91E63", "Khó", "https://youtu.be/nmwgirgXLYM"));
        list.add(new Exercise("Plank Hip Dip", "hiit", "3×20", "30s", "#E91E63", "Trung bình", "https://youtu.be/K2VljzCC16g"));
        return list;
    }

    private List<Exercise> getSquatLungeCircuitExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Squat nhảy", "hiit", "4×20", "30s", "#E91E63", "Trung bình", "https://youtu.be/A-cFYWvaHr0"));
        list.add(new Exercise("Lunge bước tiến", "hiit", "3×16", "45s", "#E91E63", "Trung bình", "https://youtu.be/QOVaHwm-Q6U"));
        list.add(new Exercise("Lunge nhảy", "hiit", "3×12", "45s", "#E91E63", "Khó", "https://youtu.be/QOVaHwm-Q6U"));
        list.add(new Exercise("Squat Pulse", "hiit", "3×30s", "30s", "#E91E63", "Trung bình", "https://youtu.be/A-cFYWvaHr0"));
        list.add(new Exercise("Curtsy Lunge", "hiit", "3×16", "45s", "#E91E63", "Trung bình", "https://youtu.be/QOVaHwm-Q6U"));
        list.add(new Exercise("Lateral Lunge", "hiit", "3×14", "45s", "#E91E63", "Trung bình", "https://youtu.be/8pl4hWllMPQ"));
        list.add(new Exercise("Squat to Kick", "hiit", "3×20", "30s", "#E91E63", "Trung bình", "https://youtu.be/A-cFYWvaHr0"));
        return list;
    }

    private List<Exercise> getMuscleBuildingExercises() {
        List<Exercise> list = new ArrayList<>();
        list.add(new Exercise("Bench Press - Ngực", "chest", "4×8", "90s", "#FF6B6B", "Trung bình", "https://youtu.be/rT7DgCr-3pg"));
        list.add(new Exercise("Deadlift - Lưng & Chân", "back", "4×5", "120s", "#795548", "Khó", "https://youtu.be/op9kVnSso6Q"));
        list.add(new Exercise("Back Squat - Chân", "legs", "4×6", "120s", "#FFA726", "Khó", "https://youtu.be/ultWZbUMPL8"));
        list.add(new Exercise("Overhead Press - Vai", "shoulder", "4×8", "90s", "#4ECDC4", "Khó", "https://youtu.be/2yjwXTZQDDI"));
        list.add(new Exercise("Pull-Up - Lưng", "back", "4×8", "90s", "#795548", "Khó", "https://youtu.be/eGo4IYlbE5g"));
        list.add(new Exercise("Dips - Ngực & Tay", "chest", "3×10", "90s", "#FF6B6B", "Trung bình", "https://youtu.be/2z8JmcrW-As"));
        list.add(new Exercise("Barbell Row - Lưng", "back", "4×8", "90s", "#795548", "Khó", "https://youtu.be/G8l_8chR5BE"));
        list.add(new Exercise("Lateral Raise - Vai", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/3VcKaXpzqRo"));
        list.add(new Exercise("Lunge - Chân", "legs", "3×12", "60s", "#FFA726", "Trung bình", "https://youtu.be/QOVaHwm-Q6U"));
        list.add(new Exercise("Dumbbell Fly - Ngực", "chest", "3×12", "60s", "#FF6B6B", "Dễ", "https://youtu.be/eozdVDA78K0"));
        list.add(new Exercise("Face Pull - Vai sau", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/rep-qVOkqgk"));
        list.add(new Exercise("Romanian Deadlift - Lưng", "back", "3×10", "90s", "#795548", "Trung bình", "https://youtu.be/JCXUYuzwNrM"));
        list.add(new Exercise("Goblet Squat - Chân", "legs", "3×12", "60s", "#FFA726", "Trung bình", "https://youtu.be/MxsFDhcyFyE"));
        list.add(new Exercise("Chin-Up - Lưng & Tay", "back", "3×8", "90s", "#795548", "Trung bình", "https://youtu.be/sIiargZZS3s"));
        list.add(new Exercise("Lunge - Chân", "legs", "3×12", "60s", "#FFA726", "Trung bình", "https://youtu.be/QOVaHwm-Q6U"));
        list.add(new Exercise("Dumbbell Fly - Ngực", "chest", "3×12", "60s", "#FF6B6B", "Dễ", "https://youtu.be/eozdVDA78K0"));
        list.add(new Exercise("Face Pull - Vai sau", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/rep-qVOkqgk"));
        // Giãn cơ toàn thân
        list.add(new Exercise("🧘 Giãn ngực & vai - Doorway Stretch", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/eozdVDA78K0"));
        list.add(new Exercise("🧘 Giãn lưng & chân - Forward Fold", "stretch", "2×30s", "15s", "#66BB6A", "Dễ", "https://youtu.be/JCXUYuzwNrM"));
        list.add(new Exercise("🧘 Giãn toàn thân - Lying Twist", "stretch", "2×30s", "0s", "#66BB6A", "Dễ", "https://youtu.be/kqnua4rHVVA"));
        return list;
    }
}