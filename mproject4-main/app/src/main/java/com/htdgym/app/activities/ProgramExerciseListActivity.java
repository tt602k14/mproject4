package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ProgramExerciseListActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvProgramTitle, tvProgramDescription;
    private RecyclerView recyclerExercises;
    private ExerciseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_exercise_list);

        btnBack = findViewById(R.id.btn_back);
        tvProgramTitle = findViewById(R.id.tv_program_title);
        tvProgramDescription = findViewById(R.id.tv_program_description);
        recyclerExercises = findViewById(R.id.recycler_exercises);

        btnBack.setOnClickListener(v -> finish());

        String programType = getIntent().getStringExtra("program_type");
        String programTitle = getIntent().getStringExtra("program_title");

        if (programTitle != null) tvProgramTitle.setText(programTitle);

        List<Exercise> exercises = getExercisesForType(programType);
        tvProgramDescription.setText(exercises.size() + " bài tập");

        adapter = new ExerciseListAdapter(exercises, exercise -> {
            Intent intent = new Intent(this, ExerciseDetailActivity.class);
            intent.putExtra("exercise_name", exercise.getName());
            intent.putExtra("muscle_group", exercise.getCategory());
            intent.putExtra("sets_reps", exercise.getFormattedDuration());
            intent.putExtra("difficulty", exercise.getDifficulty());
            intent.putExtra("video_url", exercise.getVideoUrl());
            startActivity(intent);
        });

        recyclerExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerExercises.setAdapter(adapter);
    }

    private List<Exercise> getExercisesForType(String type) {
        List<Exercise> list = new ArrayList<>();
        if (type == null) type = "muscle_building";

        switch (type) {
            case "bench_press":
                list.add(new Exercise("Bench Press cơ bản", "Tập ngực với tạ đòn", "chest", "intermediate", 45, 150, "https://youtu.be/rT7DgCr-3pg"));
                list.add(new Exercise("Incline Bench Press", "Tập ngực trên với tạ đòn", "chest", "intermediate", 45, 140, "https://youtu.be/DbFgADa2PL8"));
                list.add(new Exercise("Decline Bench Press", "Tập ngực dưới", "chest", "intermediate", 45, 140, "https://youtu.be/LfyQBUKR8SE"));
                list.add(new Exercise("Dumbbell Fly", "Mở rộng cơ ngực với tạ đơn", "chest", "beginner", 40, 120, "https://youtu.be/eozdVDA78K0"));
                list.add(new Exercise("Hít đất rộng", "Push-up tay rộng tập ngực", "chest", "beginner", 30, 100, "https://youtu.be/5Qv2T8VusME"));
                list.add(new Exercise("Hít đất kim cương", "Push-up tay hẹp tập tay sau", "chest", "advanced", 30, 110, "https://youtu.be/J0DnG1_S92I"));
                list.add(new Exercise("Chest Dip", "Chống đẩy tập ngực", "chest", "intermediate", 40, 130, "https://youtu.be/2z8JmcrW-As"));
                list.add(new Exercise("Cable Crossover", "Kéo cáp chéo tập ngực", "chest", "intermediate", 40, 120, "https://youtu.be/taI4XduLpTk"));
                list.add(new Exercise("Hít đất Archer", "Push-up một tay nâng cao", "chest", "advanced", 35, 120, "https://youtu.be/J0DnG1_S92I"));
                list.add(new Exercise("Incline Dumbbell Fly", "Mở rộng ngực trên với tạ đơn", "chest", "intermediate", 40, 120, "https://youtu.be/eozdVDA78K0"));
                list.add(new Exercise("Svend Press", "Ép tạ đĩa tập ngực giữa", "chest", "beginner", 30, 90, "https://youtu.be/rT7DgCr-3pg"));
                list.add(new Exercise("Dumbbell Pullover", "Kéo tạ qua đầu tập ngực & lưng", "chest", "intermediate", 40, 120, "https://youtu.be/eozdVDA78K0"));
                list.add(new Exercise("🧘 Giãn cơ ngực - Doorway Stretch", "Giãn cơ ngực sau tập", "chest", "beginner", 10, 10, "https://youtu.be/eozdVDA78K0"));
                list.add(new Exercise("🧘 Giãn cơ ngực - Chest Opener", "Mở rộng lồng ngực", "chest", "beginner", 10, 10, "https://youtu.be/5Qv2T8VusME"));
                break;

            case "overhead_press":
                list.add(new Exercise("Overhead Press", "Đẩy tạ đòn qua đầu tập vai", "shoulder", "advanced", 45, 150, "https://youtu.be/2yjwXTZQDDI"));
                list.add(new Exercise("Dumbbell Shoulder Press", "Đẩy tạ đơn tập vai", "shoulder", "intermediate", 40, 130, "https://youtu.be/qEwKCR5JCog"));
                list.add(new Exercise("Arnold Press", "Đẩy tạ xoay tập vai toàn diện", "shoulder", "intermediate", 40, 130, "https://youtu.be/6Z15_WdXmVw"));
                list.add(new Exercise("Lateral Raise", "Nâng tạ ngang tập vai giữa", "shoulder", "beginner", 35, 100, "https://youtu.be/3VcKaXpzqRo"));
                list.add(new Exercise("Front Raise", "Nâng tạ trước tập vai trước", "shoulder", "beginner", 35, 100, "https://youtu.be/qM5X-_FfCQE"));
                list.add(new Exercise("Rear Delt Fly", "Nâng tạ sau tập vai sau", "shoulder", "beginner", 35, 100, "https://youtu.be/EA7u4Q_8HQ0"));
                list.add(new Exercise("Upright Row", "Kéo tạ đứng tập vai & thang", "shoulder", "intermediate", 40, 120, "https://youtu.be/um3LNkMEFMg"));
                list.add(new Exercise("Face Pull", "Kéo dây tập vai sau", "shoulder", "beginner", 35, 100, "https://youtu.be/rep-qVOkqgk"));
                list.add(new Exercise("Shrug - Nâng vai", "Nâng vai tập cơ thang", "shoulder", "beginner", 35, 100, "https://youtu.be/um3LNkMEFMg"));
                list.add(new Exercise("Push Press", "Đẩy tạ có đà tập vai & chân", "shoulder", "advanced", 45, 160, "https://youtu.be/2yjwXTZQDDI"));
                list.add(new Exercise("Cable Lateral Raise", "Nâng cáp ngang tập vai giữa", "shoulder", "beginner", 35, 100, "https://youtu.be/3VcKaXpzqRo"));
                list.add(new Exercise("Bent-Over Lateral Raise", "Nâng tạ cúi tập vai sau", "shoulder", "beginner", 35, 100, "https://youtu.be/EA7u4Q_8HQ0"));
                list.add(new Exercise("Seated Dumbbell Press", "Đẩy tạ ngồi tập vai", "shoulder", "intermediate", 40, 130, "https://youtu.be/qEwKCR5JCog"));
                list.add(new Exercise("🧘 Giãn cơ vai - Cross-Body Stretch", "Giãn vai sau tập", "shoulder", "beginner", 10, 10, "https://youtu.be/aKNn76T3RPE"));
                list.add(new Exercise("🧘 Giãn cổ - Neck Side Stretch", "Giãn cổ và vai", "shoulder", "beginner", 10, 10, "https://youtu.be/qEwKCR5JCog"));
                break;

            case "squat_strength":
                list.add(new Exercise("Back Squat", "Squat tạ đòn sau tập chân", "legs", "advanced", 50, 200, "https://youtu.be/ultWZbUMPL8"));
                list.add(new Exercise("Front Squat", "Squat tạ đòn trước tập chân", "legs", "advanced", 50, 190, "https://youtu.be/m4ytaCJZpl0"));
                list.add(new Exercise("Goblet Squat", "Squat tạ đơn tập chân", "legs", "intermediate", 40, 160, "https://youtu.be/MxsFDhcyFyE"));
                list.add(new Exercise("Bulgarian Split Squat", "Squat một chân tập chân & mông", "legs", "advanced", 45, 180, "https://youtu.be/2C-uStzPJWY"));
                list.add(new Exercise("Leg Press", "Đẩy chân máy tập chân", "legs", "intermediate", 45, 170, "https://youtu.be/IZxyjW7MPJQ"));
                list.add(new Exercise("Hack Squat", "Squat máy tập chân", "legs", "intermediate", 45, 170, "https://youtu.be/0tn5K9NlCfo"));
                list.add(new Exercise("Nâng bắp chân đứng", "Calf raise tập bắp chân", "legs", "beginner", 30, 80, "https://youtu.be/gwLzBJYoWlI"));
                list.add(new Exercise("Leg Extension", "Duỗi chân máy tập đùi trước", "legs", "beginner", 35, 100, "https://youtu.be/YyvSfVjQeL0"));
                list.add(new Exercise("Leg Curl", "Gập chân máy tập đùi sau", "legs", "beginner", 35, 100, "https://youtu.be/1Tq3QdYUuHs"));
                list.add(new Exercise("Lunge bước tiến", "Bước lunge tập chân & mông", "legs", "intermediate", 40, 150, "https://youtu.be/QOVaHwm-Q6U"));
                list.add(new Exercise("Step-Up", "Bước lên bục tập chân", "legs", "intermediate", 40, 140, "https://youtu.be/ultWZbUMPL8"));
                list.add(new Exercise("Sumo Squat", "Squat chân rộng tập đùi trong", "legs", "beginner", 35, 130, "https://youtu.be/WsepIUGZu0c"));
                list.add(new Exercise("Wall Sit", "Ngồi tường tập đùi trước", "legs", "intermediate", 30, 100, "https://youtu.be/ultWZbUMPL8"));
                list.add(new Exercise("Nordic Hamstring Curl", "Gập chân nâng cao tập đùi sau", "legs", "advanced", 40, 150, "https://youtu.be/1Tq3QdYUuHs"));
                list.add(new Exercise("🧘 Giãn cơ tứ đầu - Standing Quad", "Giãn đùi trước sau tập", "legs", "beginner", 10, 10, "https://youtu.be/gwLzBJYoWlI"));
                list.add(new Exercise("🧘 Giãn gân kheo - Seated Forward Fold", "Giãn đùi sau", "legs", "beginner", 10, 10, "https://youtu.be/JCXUYuzwNrM"));
                list.add(new Exercise("🧘 Giãn bắp chân - Calf Stretch", "Giãn bắp chân", "legs", "beginner", 10, 10, "https://youtu.be/gwLzBJYoWlI"));
                break;

            case "abs_beginner":
                list.add(new Exercise("Gập bụng cơ bản", "Crunch tập cơ bụng trên", "abs", "beginner", 30, 80, "https://youtu.be/Y8VflnViz78"));
                list.add(new Exercise("Plank cơ bản", "Plank tập core toàn diện", "abs", "beginner", 30, 70, "https://youtu.be/pSHjTRCQxIw"));
                list.add(new Exercise("Nâng chân nằm", "Leg raise tập cơ bụng dưới", "abs", "beginner", 30, 80, "https://youtu.be/JB2oyawG9KI"));
                list.add(new Exercise("Gập bụng chéo", "Bicycle crunch tập bụng chéo", "abs", "intermediate", 30, 90, "https://youtu.be/9FGilxCbdz8"));
                list.add(new Exercise("Plank nghiêng", "Side plank tập bụng bên", "abs", "intermediate", 30, 80, "https://youtu.be/K2VljzCC16g"));
                list.add(new Exercise("Leo núi", "Mountain climber tập core & cardio", "abs", "intermediate", 30, 100, "https://youtu.be/nmwgirgXLYM"));
                list.add(new Exercise("Russian Twist", "Xoay thân tập bụng chéo", "abs", "intermediate", 30, 90, "https://youtu.be/wkD8rjkodUI"));
                list.add(new Exercise("V-Up", "Gập bụng chữ V tập toàn bộ core", "abs", "advanced", 30, 100, "https://youtu.be/7UVgs18Y1P4"));
                list.add(new Exercise("Dead Bug", "Bài tập ổn định core", "abs", "beginner", 30, 70, "https://youtu.be/g_BYB0R-4Ws"));
                list.add(new Exercise("Hollow Body Hold", "Giữ tư thế hollow tập core", "abs", "intermediate", 30, 80, "https://youtu.be/LlDNef_Ztsc"));
                list.add(new Exercise("Ab Wheel Rollout", "Lăn bánh xe tập core nâng cao", "abs", "advanced", 35, 110, "https://youtu.be/LlDNef_Ztsc"));
                list.add(new Exercise("Dragon Flag", "Dragon flag tập core cực khó", "abs", "advanced", 40, 120, "https://youtu.be/LlDNef_Ztsc"));
                list.add(new Exercise("Hanging Knee Raise", "Nâng gối treo tập bụng dưới", "abs", "intermediate", 35, 100, "https://youtu.be/JB2oyawG9KI"));
                list.add(new Exercise("🧘 Giãn cơ Cobra Stretch", "Giãn cơ bụng sau tập", "abs", "beginner", 10, 10, "https://youtu.be/JnHnd4SNKR8"));
                list.add(new Exercise("🧘 Giãn cơ Cat-Cow", "Giãn lưng và bụng", "abs", "beginner", 10, 10, "https://youtu.be/kqnua4rHVVA"));
                list.add(new Exercise("🧘 Child's Pose - Giãn lưng dưới", "Giãn lưng dưới sau tập", "abs", "beginner", 10, 10, "https://youtu.be/eqVMAPM00GM"));
                break;

            case "barbell_row":
                list.add(new Exercise("Barbell Row", "Chèo tạ đòn tập lưng giữa", "back", "advanced", 45, 160, "https://youtu.be/G8l_8chR5BE"));
                list.add(new Exercise("Dumbbell Row", "Chèo tạ đơn tập lưng", "back", "intermediate", 40, 140, "https://youtu.be/pYcpY20QaE8"));
                list.add(new Exercise("T-Bar Row", "Chèo T-bar tập lưng dày", "back", "advanced", 45, 160, "https://youtu.be/j3Igk5nyZE4"));
                list.add(new Exercise("Seated Cable Row", "Chèo cáp ngồi tập lưng", "back", "intermediate", 40, 140, "https://youtu.be/GZbfZ033f74"));
                list.add(new Exercise("Pull-Up cơ bản", "Kéo xà tập lưng & tay trước", "back", "advanced", 40, 150, "https://youtu.be/eGo4IYlbE5g"));
                list.add(new Exercise("Lat Pulldown", "Kéo cáp xuống tập lưng rộng", "back", "intermediate", 40, 140, "https://youtu.be/CAwf7n6Luuc"));
                list.add(new Exercise("Mèo - Bò", "Cat-cow tập lưng linh hoạt", "back", "beginner", 20, 50, "https://youtu.be/kqnua4rHVVA"));
                list.add(new Exercise("Chó chim", "Bird dog tập ổn định lưng", "back", "beginner", 25, 60, "https://youtu.be/wiFNA3sqjCA"));
                list.add(new Exercise("Duỗi lưng", "Back extension tập lưng dưới", "back", "beginner", 30, 80, "https://youtu.be/cc7kIfSUWEY"));
                list.add(new Exercise("Chest-Supported Row", "Chèo ngực tựa tập lưng", "back", "intermediate", 40, 140, "https://youtu.be/pYcpY20QaE8"));
                list.add(new Exercise("Inverted Row", "Chèo ngược tập lưng", "back", "intermediate", 35, 120, "https://youtu.be/G8l_8chR5BE"));
                list.add(new Exercise("Renegade Row", "Chèo tạ plank tập lưng & core", "back", "advanced", 40, 150, "https://youtu.be/G8l_8chR5BE"));
                list.add(new Exercise("Deadlift cơ bản", "Deadlift tập lưng & chân", "back", "advanced", 50, 200, "https://youtu.be/op9kVnSso6Q"));
                list.add(new Exercise("Romanian Deadlift", "RDL tập lưng dưới & đùi sau", "back", "intermediate", 45, 170, "https://youtu.be/JCXUYuzwNrM"));
                list.add(new Exercise("🧘 Giãn cơ lưng - Lat Stretch", "Giãn lưng rộng sau tập", "back", "beginner", 10, 10, "https://youtu.be/CAwf7n6Luuc"));
                list.add(new Exercise("🧘 Child's Pose - Giãn lưng toàn diện", "Giãn lưng toàn diện", "back", "beginner", 10, 10, "https://youtu.be/eqVMAPM00GM"));
                break;

            default: // muscle_building - tất cả
                list.add(new Exercise("Bench Press cơ bản", "Tập ngực với tạ đòn", "chest", "intermediate", 45, 150, "https://youtu.be/rT7DgCr-3pg"));
                list.add(new Exercise("Overhead Press", "Đẩy tạ đòn qua đầu tập vai", "shoulder", "advanced", 45, 150, "https://youtu.be/2yjwXTZQDDI"));
                list.add(new Exercise("Back Squat", "Squat tạ đòn sau tập chân", "legs", "advanced", 50, 200, "https://youtu.be/ultWZbUMPL8"));
                list.add(new Exercise("Gập bụng cơ bản", "Crunch tập cơ bụng trên", "abs", "beginner", 30, 80, "https://youtu.be/Y8VflnViz78"));
                list.add(new Exercise("Barbell Row", "Chèo tạ đòn tập lưng giữa", "back", "advanced", 45, 160, "https://youtu.be/G8l_8chR5BE"));
                list.add(new Exercise("Lateral Raise", "Nâng tạ ngang tập vai giữa", "shoulder", "beginner", 35, 100, "https://youtu.be/3VcKaXpzqRo"));
                list.add(new Exercise("Hít đất rộng", "Push-up tay rộng tập ngực", "chest", "beginner", 30, 100, "https://youtu.be/5Qv2T8VusME"));
                list.add(new Exercise("Plank cơ bản", "Plank tập core toàn diện", "abs", "beginner", 30, 70, "https://youtu.be/pSHjTRCQxIw"));
                list.add(new Exercise("Lunge bước tiến", "Bước lunge tập chân & mông", "legs", "intermediate", 40, 150, "https://youtu.be/QOVaHwm-Q6U"));
                list.add(new Exercise("Pull-Up cơ bản", "Kéo xà tập lưng & tay trước", "back", "advanced", 40, 150, "https://youtu.be/eGo4IYlbE5g"));
                list.add(new Exercise("Dumbbell Shoulder Press", "Đẩy tạ đơn tập vai", "shoulder", "intermediate", 40, 130, "https://youtu.be/qEwKCR5JCog"));
                list.add(new Exercise("Deadlift cơ bản", "Deadlift tập lưng & chân", "back", "advanced", 50, 200, "https://youtu.be/op9kVnSso6Q"));
                list.add(new Exercise("Goblet Squat", "Squat tạ đơn tập chân", "legs", "intermediate", 40, 160, "https://youtu.be/MxsFDhcyFyE"));
                list.add(new Exercise("Dumbbell Fly", "Mở rộng cơ ngực với tạ đơn", "chest", "beginner", 40, 120, "https://youtu.be/eozdVDA78K0"));
                list.add(new Exercise("Russian Twist", "Xoay thân tập bụng chéo", "abs", "intermediate", 30, 90, "https://youtu.be/wkD8rjkodUI"));
                break;
        }
        return list;
    }

    // Simple inline adapter
    private static class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.VH> {
        interface OnClick { void onClick(Exercise e); }
        private final List<Exercise> list;
        private final OnClick listener;

        ExerciseListAdapter(List<Exercise> list, OnClick listener) {
            this.list = list;
            this.listener = listener;
        }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_exercise_card, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            Exercise ex = list.get(pos);
            h.tvName.setText(ex.getName());
            h.tvMuscle.setText(getCategoryLabel(ex.getCategory()));
            h.tvSets.setText(ex.getDifficulty());
            h.tvDuration.setText(ex.getFormattedDuration());
            h.itemView.setOnClickListener(v -> listener.onClick(ex));
        }

        @Override public int getItemCount() { return list.size(); }

        private String getCategoryLabel(String cat) {
            if (cat == null) return "Tổng thể";
            switch (cat) {
                case "chest": return "💪 Ngực";
                case "shoulder": return "🏋️ Vai";
                case "legs": return "🦵 Chân";
                case "abs": return "🔥 Bụng";
                case "back": return "🏃 Lưng";
                case "stretch": return "🧘 Giãn cơ";
                default: return "💪 Tổng thể";
            }
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvName, tvMuscle, tvSets, tvDuration;
            VH(@NonNull View v) {
                super(v);
                tvName = v.findViewById(R.id.tv_exercise_name);
                tvMuscle = v.findViewById(R.id.tv_muscle_group);
                tvSets = v.findViewById(R.id.tv_sets);
                tvDuration = v.findViewById(R.id.tv_duration);
            }
        }
    }
}
