package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.WorkoutTemplateAdapter;
import com.htdgym.app.models.WorkoutTemplate;

import java.util.ArrayList;
import java.util.List;

public class WorkoutTemplatesActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvTitle, tvDescription;
    private RecyclerView recyclerTemplates;
    private WorkoutTemplateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_templates);

        initViews();
        setupRecyclerView();
        loadTemplates();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        recyclerTemplates = findViewById(R.id.recycler_templates);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new WorkoutTemplateAdapter(new ArrayList<>(), template -> {
            // Open template workout
            Intent intent = new Intent(WorkoutTemplatesActivity.this, ProgramExerciseListActivity.class);
            intent.putExtra("program_type", template.getType());
            intent.putExtra("program_title", template.getName());
            startActivity(intent);
        });

        recyclerTemplates.setLayoutManager(new LinearLayoutManager(this));
        recyclerTemplates.setAdapter(adapter);
    }

    private void loadTemplates() {
        List<WorkoutTemplate> templates = new ArrayList<>();

        // Beginner Templates
        templates.add(new WorkoutTemplate(
            "beginner_fullbody",
            "🌱 Toàn Thân Người Mới",
            "Workout cơ bản cho người mới bắt đầu",
            "30-40 phút",
            "Dễ",
            "6 bài tập compound cơ bản",
            "#4CAF50",
            "https://youtu.be/-R5sH2iG9Gw"
        ));

        templates.add(new WorkoutTemplate(
            "mobility_recovery",
            "🧘 Phục Hồi & Linh Hoạt",
            "Cải thiện độ linh hoạt và phục hồi cơ thể",
            "20-30 phút",
            "Dễ",
            "6 bài tập stretching và mobility",
            "#9C27B0",
            "https://youtu.be/kqnua4rHVVA"
        ));

        // Intermediate Templates
        templates.add(new WorkoutTemplate(
            "intermediate_push",
            "💪 Push Day Trung Cấp",
            "Tập trung phát triển ngực, vai và tay sau",
            "45-50 phút",
            "Trung bình",
            "6 bài tập push movements",
            "#FF6B6B",
            "https://youtu.be/qEwKCR5JCog"
        ));

        templates.add(new WorkoutTemplate(
            "hiit_cardio",
            "🔥 HIIT Cardio Blast",
            "Đốt mỡ nhanh với HIIT cường độ cao",
            "25-35 phút",
            "Trung bình",
            "8 bài tập HIIT intervals",
            "#E91E63",
            "https://youtu.be/JZQA08SlJnM"
        ));

        // Advanced Templates
        templates.add(new WorkoutTemplate(
            "advanced_hiit",
            "⚡ HIIT Nâng Cao",
            "Thử thách giới hạn với HIIT cực mạnh",
            "35-45 phút",
            "Khó",
            "6 bài tập HIIT advanced",
            "#FF5722",
            "https://youtu.be/20Khkl95_qA"
        ));

        templates.add(new WorkoutTemplate(
            "strength_focused",
            "🏋️ Sức Mạnh Tối Đa",
            "Phát triển sức mạnh với các bài tập nâng cao",
            "50-60 phút",
            "Khó",
            "5 bài tập strength progressions",
            "#795548",
            "https://youtu.be/HSoHeSjvIdY"
        ));

        // Specialized Templates
        templates.add(new WorkoutTemplate(
            "abs_focused",
            "🔥 Cơ Bụng 6 Múi",
            "Tập trung phát triển cơ bụng hoàn hảo",
            "25-30 phút",
            "Trung bình",
            "8 bài tập abs chuyên sâu",
            "#9C27B0",
            "https://youtu.be/Y8VflnViz78"
        ));

        templates.add(new WorkoutTemplate(
            "upper_body",
            "💪 Thân Trên Hoàn Chỉnh",
            "Phát triển toàn bộ cơ thân trên",
            "40-50 phút",
            "Trung bình",
            "7 bài tập upper body",
            "#4ECDC4",
            "https://youtu.be/d1HZBdD0idE"
        ));

        templates.add(new WorkoutTemplate(
            "lower_body",
            "🦵 Thân Dưới Mạnh Mẽ",
            "Xây dựng đôi chân và mông mạnh mẽ",
            "40-50 phút",
            "Trung bình",
            "7 bài tập lower body",
            "#FFA726",
            "https://youtu.be/XtoO9YwNEqA"
        ));

        adapter.updateTemplates(templates);
    }
}