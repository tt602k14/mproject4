package com.htdgym.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.ExerciseCardAdapter;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.utils.HIITWorkoutManager;
import com.htdgym.app.utils.MuscleBuilderManager;
import com.htdgym.app.utils.PremiumManager;
import com.htdgym.app.utils.ThumbnailManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Workout Program Activity - Chương trình tập luyện chuyên sâu
 */
public class WorkoutProgramActivity extends AppCompatActivity {
    
    private static final String TAG = "WorkoutProgramActivity";
    
    private ImageView btnBack;
    private TextView tvTitle, tvDescription, tvDuration, tvDifficulty;
    private LinearLayout layoutProgramCards;
    private RecyclerView recyclerExercises;
    private ExerciseCardAdapter adapter;
    
    private String programType;
    private List<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_program);
        
        try {
            initViews();
            getProgramType();
            setupProgram();
            setupRecyclerView();
            loadProgramCards();
            
            Log.d(TAG, "WorkoutProgramActivity initialized for: " + programType);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing WorkoutProgramActivity", e);
            handleError("Lỗi khởi tạo chương trình", e);
        }
    }
    
    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        tvDuration = findViewById(R.id.tv_duration);
        tvDifficulty = findViewById(R.id.tv_difficulty);
        layoutProgramCards = findViewById(R.id.layout_program_cards);
        recyclerExercises = findViewById(R.id.recycler_exercises);
        
        btnBack.setOnClickListener(v -> finish());
    }
    
    private void getProgramType() {
        programType = getIntent().getStringExtra("program_type");
        if (programType == null) {
            programType = "hiit_beginner";
        }
    }
    
    private void setupProgram() {
        switch (programType) {
            case "hiit_beginner":
                setupHIITBeginner();
                break;
            case "hiit_advanced":
                setupHIITAdvanced();
                break;
            case "muscle_builder":
                setupMuscleBuilder();
                break;
            case "upper_body":
                setupUpperBody();
                break;
            case "lower_body":
                setupLowerBody();
                break;
            case "progressive_overload":
                setupProgressiveOverload();
                break;
            case "fat_burning":
                setupFatBurning();
                break;
            case "core_strength":
                setupCoreStrength();
                break;
            default:
                setupHIITBeginner();
                break;
        }
    }
    
    private void setupHIITBeginner() {
        tvTitle.setText("🔥 HIIT Đốt Mỡ - Người Mới");
        tvDescription.setText("Chương trình HIIT 4 tuần cho người mới bắt đầu. Đốt mỡ hiệu quả với cường độ tăng dần.");
        tvDuration.setText("4 tuần • 28 ngày");
        tvDifficulty.setText("Dễ → Khó");
        
        exercises = HIITWorkoutManager.getHIITBeginner();
    }
    
    private void setupHIITAdvanced() {
        tvTitle.setText("🔥 HIIT Đốt Mỡ - Nâng Cao");
        tvDescription.setText("Chương trình HIIT cường độ cao cho người có kinh nghiệm. Thử thách giới hạn bản thân.");
        tvDuration.setText("Linh hoạt");
        tvDifficulty.setText("Cực khó");
        
        exercises = HIITWorkoutManager.getHIITAdvanced();
    }
    
    private void setupMuscleBuilder() {
        tvTitle.setText("💪 Xây Dựng Cơ Bắp");
        tvDescription.setText("Chương trình tăng cơ toàn diện với các bài tập compound và isolation.");
        tvDuration.setText("8-12 tuần");
        tvDifficulty.setText("Khó");
        
        exercises = MuscleBuilderManager.getAllMuscleBuilderExercises();
    }
    
    private void setupUpperBody() {
        tvTitle.setText("🏋️ Tăng Cơ Thân Trên");
        tvDescription.setText("Phát triển cơ ngực, lưng, vai và tay một cách toàn diện.");
        tvDuration.setText("6-8 tuần");
        tvDifficulty.setText("Khó");
        
        exercises = MuscleBuilderManager.getUpperBodyStrength();
    }
    
    private void setupLowerBody() {
        tvTitle.setText("🦵 Tăng Cơ Thân Dưới");
        tvDescription.setText("Xây dựng sức mạnh chân, mông và bắp chân vững chắc.");
        tvDuration.setText("6-8 tuần");
        tvDifficulty.setText("Khó");
        
        exercises = MuscleBuilderManager.getLowerBodyStrength();
    }
    
    private void setupProgressiveOverload() {
        tvTitle.setText("🎯 Progressive Overload");
        tvDescription.setText("Tăng tải dần để tối ưu hóa quá trình tăng cơ và sức mạnh.");
        tvDuration.setText("7 tuần");
        tvDifficulty.setText("Trung bình → Cực khó");
        
        exercises = MuscleBuilderManager.getProgressiveOverloadProgram();
    }
    
    private void setupFatBurning() {
        tvTitle.setText("🔥 Đốt Mỡ Toàn Diện");
        tvDescription.setText("Kết hợp HIIT và Cardio để đốt mỡ hiệu quả nhất.");
        tvDuration.setText("6-8 tuần");
        tvDifficulty.setText("Trung bình");
        
        List<Exercise> hiitExercises = HIITWorkoutManager.getFatBurningCardio();
        List<Exercise> coreExercises = HIITWorkoutManager.getCoreFatBurning();
        exercises = new ArrayList<>();
        exercises.addAll(hiitExercises);
        exercises.addAll(coreExercises);
    }
    
    private void setupCoreStrength() {
        tvTitle.setText("🏋️ Tăng Cơ Core");
        tvDescription.setText("Phát triển sức mạnh cơ core với các bài tập chuyên sâu.");
        tvDuration.setText("6 tuần");
        tvDifficulty.setText("Trung bình → Khó");
        
        exercises = MuscleBuilderManager.getCoreStrength();
    }
    
    private void setupRecyclerView() {
        adapter = new ExerciseCardAdapter(exercises, new ExerciseCardAdapter.OnExerciseClickListener() {
            @Override
            public void onExerciseClick(Exercise exercise) {
                openExerciseDetail(exercise);
            }

            @Override
            public void onFavoriteClick(Exercise exercise) {
                exercise.setFavorite(!exercise.isFavorite());
                adapter.notifyDataSetChanged();
                String message = exercise.isFavorite() ? "Đã thêm vào yêu thích ❤️" : "Đã xóa khỏi yêu thích 💔";
                Toast.makeText(WorkoutProgramActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        
        recyclerExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerExercises.setAdapter(adapter);
    }
    
    private void loadProgramCards() {
        layoutProgramCards.removeAllViews();
        
        // Add program overview cards
        addProgramCard("📊 Tổng Quan", 
            "• " + exercises.size() + " bài tập\n• Cường độ: " + tvDifficulty.getText() + "\n• Thời gian: " + tvDuration.getText(),
            "#2196F3", null);
        
        addProgramCard("🎯 Mục Tiêu", 
            getProgramGoals(),
            "#4CAF50", null);
        
        addProgramCard("⚡ Lợi Ích", 
            getProgramBenefits(),
            "#FF9800", null);
        
        addProgramCard("📋 Hướng Dẫn", 
            getProgramInstructions(),
            "#9C27B0", v -> showDetailedInstructions());
    }
    
    private String getProgramGoals() {
        switch (programType) {
            case "hiit_beginner":
            case "hiit_advanced":
            case "fat_burning":
                return "• Đốt mỡ hiệu quả\n• Tăng sức bền tim mạch\n• Cải thiện trao đổi chất\n• Giảm cân nhanh chóng";
            case "muscle_builder":
                return "• Tăng khối lượng cơ\n• Phát triển sức mạnh\n• Cải thiện thể hình\n• Tăng mật độ xương";
            case "core_strength":
                return "• Tăng cơ core\n• Cải thiện tư thế\n• Giảm đau lưng\n• Tăng sự ổn định";
            default:
                return "• Cải thiện thể lực\n• Tăng sức khỏe\n• Đạt mục tiêu fitness";
        }
    }
    
    private String getProgramBenefits() {
        switch (programType) {
            case "hiit_beginner":
            case "hiit_advanced":
                return "• Đốt calo sau tập\n• Tiết kiệm thời gian\n• Không cần dụng cụ\n• Kết quả nhanh chóng";
            case "muscle_builder":
                return "• Tăng cơ bền vững\n• Cải thiện chuyển hóa\n• Tăng tự tin\n• Sức khỏe tổng thể";
            case "fat_burning":
                return "• Giảm mỡ thừa\n• Tăng năng lượng\n• Cải thiện tim mạch\n• Thay đổi thể hình";
            default:
                return "• Sức khỏe tốt hơn\n• Tinh thần tích cực\n• Thể lực dẻo dai";
        }
    }
    
    private String getProgramInstructions() {
        return "• Khởi động 5-10 phút\n• Thực hiện đúng kỹ thuật\n• Nghỉ ngơi đầy đủ\n• Uống nước thường xuyên";
    }
    
    private void addProgramCard(String title, String content, String color, View.OnClickListener clickListener) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (12 * getResources().getDisplayMetrics().density));
        card.setLayoutParams(cardParams);
        card.setRadius(12 * getResources().getDisplayMetrics().density);
        card.setCardElevation(4 * getResources().getDisplayMetrics().density);
        card.setCardBackgroundColor(android.graphics.Color.parseColor(color));
        
        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density));
        
        TextView tvCardTitle = new TextView(this);
        tvCardTitle.setText(title);
        tvCardTitle.setTextColor(0xFFFFFFFF);
        tvCardTitle.setTextSize(16);
        tvCardTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView tvCardContent = new TextView(this);
        tvCardContent.setText(content);
        tvCardContent.setTextColor(0xE0FFFFFF);
        tvCardContent.setTextSize(14);
        tvCardContent.setPadding(0, (int) (8 * getResources().getDisplayMetrics().density), 0, 0);
        
        contentLayout.addView(tvCardTitle);
        contentLayout.addView(tvCardContent);
        card.addView(contentLayout);
        
        if (clickListener != null) {
            card.setOnClickListener(clickListener);
            card.setForeground(getDrawable(android.R.attr.selectableItemBackground));
        }
        
        layoutProgramCards.addView(card);
    }
    
    private void openExerciseDetail(Exercise exercise) {
        try {
            Intent intent = new Intent(this, ExerciseDetailActivity.class);
            intent.putExtra("exercise_name", exercise.getName());
            intent.putExtra("muscle_group", exercise.getMuscleGroup());
            intent.putExtra("sets_reps", exercise.getFormattedSetsReps());
            intent.putExtra("rest_time", exercise.getRestTime());
            intent.putExtra("difficulty", exercise.getDifficulty());
            intent.putExtra("video_url", exercise.getVideoUrl());
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error opening exercise detail", e);
            Toast.makeText(this, "Lỗi mở chi tiết bài tập", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showDetailedInstructions() {
        String instructions = getDetailedInstructions();
        
        new android.app.AlertDialog.Builder(this)
            .setTitle("📋 Hướng Dẫn Chi Tiết")
            .setMessage(instructions)
            .setPositiveButton("Hiểu rồi", null)
            .show();
    }
    
    private String getDetailedInstructions() {
        switch (programType) {
            case "hiit_beginner":
                return "🔥 HƯỚNG DẪN HIIT CHO NGƯỜI MỚI:\n\n" +
                       "1. KHỞI ĐỘNG (5-10 phút):\n" +
                       "• Đi bộ tại chỗ\n" +
                       "• Xoay khớp\n" +
                       "• Giãn cơ nhẹ\n\n" +
                       "2. THỰC HIỆN:\n" +
                       "• Tuần 1-2: 20s tập / 40s nghỉ\n" +
                       "• Tuần 3-4: 30s tập / 30s nghỉ\n" +
                       "• Tập 3-4 ngày/tuần\n\n" +
                       "3. NGHỈ NGƠI:\n" +
                       "• Uống nước đầy đủ\n" +
                       "• Nghỉ 1-2 ngày giữa các buổi\n" +
                       "• Ngủ đủ 7-8 tiếng\n\n" +
                       "4. DINH DƯỠNG:\n" +
                       "• Ăn protein đầy đủ\n" +
                       "• Giảm carb tinh chế\n" +
                       "• Tăng rau xanh";
                       
            case "muscle_builder":
                return "💪 HƯỚNG DẪN XÂY DỰNG CƠ BẮP:\n\n" +
                       "1. NGUYÊN TẮC PROGRESSIVE OVERLOAD:\n" +
                       "• Tăng trọng lượng dần\n" +
                       "• Tăng số lần lặp\n" +
                       "• Tăng số set\n\n" +
                       "2. LỊCH TẬP:\n" +
                       "• Tập 4-5 ngày/tuần\n" +
                       "• Nghỉ 48h giữa các nhóm cơ\n" +
                       "• Compound exercises ưu tiên\n\n" +
                       "3. DINH DƯỠNG:\n" +
                       "• Protein: 1.6-2.2g/kg thể trọng\n" +
                       "• Carb: 4-7g/kg thể trọng\n" +
                       "• Thặng dư calo 300-500 cal\n\n" +
                       "4. PHỤC HỒI:\n" +
                       "• Ngủ 7-9 tiếng/đêm\n" +
                       "• Massage, foam rolling\n" +
                       "• Quản lý stress";
                       
            default:
                return "📋 HƯỚNG DẪN CHUNG:\n\n" +
                       "• Khởi động trước khi tập\n" +
                       "• Thực hiện đúng kỹ thuật\n" +
                       "• Nghỉ ngơi đầy đủ\n" +
                       "• Uống nước thường xuyên\n" +
                       "• Lắng nghe cơ thể\n" +
                       "• Tăng cường độ dần dần";
        }
    }
    
    private void handleError(String message, Exception e) {
        String fullMessage = message + ": " + e.getMessage();
        Toast.makeText(this, fullMessage, Toast.LENGTH_LONG).show();
        Log.e(TAG, fullMessage, e);
    }
}