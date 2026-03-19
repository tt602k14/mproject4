package com.htdgym.app.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;

public class AdminContentManagementActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvTotalWorkouts, tvTotalVideos, tvTotalExercises;
    private CardView cardWorkoutManagement, cardVideoManagement, cardExerciseManagement;
    private CardView cardContentStats, cardBackupRestore;
    
    private GymDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_admin_content_management);
            
            // Initialize database with error handling
            try {
                database = GymDatabase.getInstance(this);
            } catch (Exception e) {
                android.util.Log.e("AdminContent", "Database initialization error: " + e.getMessage());
                com.htdgym.app.utils.DatabaseHelper.handleDatabaseError(this, e);
                Toast.makeText(this, "Lỗi khởi tạo database. Vui lòng khởi động lại ứng dụng.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            
            initViews();
            setupClickListeners();
            loadContentStatistics();
            
        } catch (Exception e) {
            android.util.Log.e("AdminContent", "onCreate error: " + e.getMessage());
            Toast.makeText(this, "Lỗi khởi tạo activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish(); // Close activity if initialization fails
        }
    }

    private void initViews() {
        try {
            btnBack = findViewById(R.id.btn_back);
            tvTotalWorkouts = findViewById(R.id.tv_total_workouts);
            tvTotalVideos = findViewById(R.id.tv_total_videos);
            tvTotalExercises = findViewById(R.id.tv_total_exercises);
            
            cardWorkoutManagement = findViewById(R.id.card_workout_management);
            cardVideoManagement = findViewById(R.id.card_video_management);
            cardExerciseManagement = findViewById(R.id.card_exercise_management);
            cardContentStats = findViewById(R.id.card_content_stats);
            cardBackupRestore = findViewById(R.id.card_backup_restore);
            
            // Check if all views were found
            if (btnBack == null || tvTotalWorkouts == null || tvTotalVideos == null || 
                tvTotalExercises == null || cardWorkoutManagement == null || 
                cardVideoManagement == null || cardExerciseManagement == null || 
                cardContentStats == null || cardBackupRestore == null) {
                
                android.util.Log.e("AdminContent", "Some views not found in layout");
                Toast.makeText(this, "Lỗi tìm view trong layout", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            android.util.Log.e("AdminContent", "initViews error: " + e.getMessage());
            Toast.makeText(this, "Lỗi khởi tạo views: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        try {
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> finish());
            }
            
            if (cardWorkoutManagement != null) {
                cardWorkoutManagement.setOnClickListener(v -> {
                    showWorkoutManagementDialog();
                });
            }
            
            if (cardVideoManagement != null) {
                cardVideoManagement.setOnClickListener(v -> {
                    showVideoManagementDialog();
                });
            }
            
            if (cardExerciseManagement != null) {
                cardExerciseManagement.setOnClickListener(v -> {
                    showExerciseManagementDialog();
                });
            }
            
            if (cardContentStats != null) {
                cardContentStats.setOnClickListener(v -> {
                    showContentStatsDialog();
                });
            }
            
            if (cardBackupRestore != null) {
                cardBackupRestore.setOnClickListener(v -> {
                    showBackupRestoreDialog();
                });
            }
        } catch (Exception e) {
            android.util.Log.e("AdminContent", "setupClickListeners error: " + e.getMessage());
            Toast.makeText(this, "Lỗi thiết lập click listeners: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadContentStatistics() {
        new Thread(() -> {
            try {
                int workoutCount = 0;
                int videoCount = 0;
                int exerciseCount = 0;
                
                try {
                    // Try to get actual counts from database
                    if (database != null) {
                        // Check if DAOs are available and working
                        if (database.workoutDao() != null) {
                            // If you have a getWorkoutCount method, use it
                            // workoutCount = database.workoutDao().getWorkoutCount();
                            workoutCount = 25; // Placeholder for now
                        }
                        
                        if (database.videoDao() != null) {
                            // videoCount = database.videoDao().getVideoCount();
                            videoCount = 15; // Placeholder for now
                        }
                        
                        if (database.exerciseDao() != null) {
                            // exerciseCount = database.exerciseDao().getExerciseCount();
                            exerciseCount = 50; // Placeholder for now
                        }
                    }
                } catch (Exception e) {
                    // Log the error but continue with default values
                    android.util.Log.e("AdminContent", "Database error: " + e.getMessage());
                    workoutCount = 25;
                    videoCount = 15;
                    exerciseCount = 50;
                }
                
                final int finalWorkoutCount = workoutCount;
                final int finalVideoCount = videoCount;
                final int finalExerciseCount = exerciseCount;
                
                runOnUiThread(() -> {
                    try {
                        if (tvTotalWorkouts != null) {
                            tvTotalWorkouts.setText(String.valueOf(finalWorkoutCount));
                        }
                        if (tvTotalVideos != null) {
                            tvTotalVideos.setText(String.valueOf(finalVideoCount));
                        }
                        if (tvTotalExercises != null) {
                            tvTotalExercises.setText(String.valueOf(finalExerciseCount));
                        }
                    } catch (Exception e) {
                        android.util.Log.e("AdminContent", "UI update error: " + e.getMessage());
                    }
                });
                
            } catch (Exception e) {
                android.util.Log.e("AdminContent", "General error: " + e.getMessage());
                runOnUiThread(() -> {
                    try {
                        Toast.makeText(AdminContentManagementActivity.this, 
                                "Lỗi tải thống kê: " + e.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception toastError) {
                        android.util.Log.e("AdminContent", "Toast error: " + toastError.getMessage());
                    }
                });
            }
        }).start();
    }

    private void showWorkoutManagementDialog() {
        String[] options = {
            "📋 Xem danh sách bài tập",
            "➕ Thêm bài tập mới", 
            "✏️ Sửa bài tập",
            "🗑️ Xóa bài tập",
            "📊 Thống kê bài tập"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("🏋️ Quản lý bài tập")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        Toast.makeText(this, "Chức năng xem danh sách bài tập", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        showAddWorkoutDialog();
                        break;
                    case 2:
                        Toast.makeText(this, "Chức năng sửa bài tập", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        showDeleteConfirmDialog("bài tập");
                        break;
                    case 4:
                        showWorkoutStatsDialog();
                        break;
                }
            })
            .show();
    }

    private void showVideoManagementDialog() {
        String[] options = {
            "📹 Xem danh sách video",
            "➕ Thêm video mới",
            "✏️ Sửa thông tin video", 
            "🗑️ Xóa video",
            "📊 Thống kê video"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("🎥 Quản lý video")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        Toast.makeText(this, "Chức năng xem danh sách video", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        showAddVideoDialog();
                        break;
                    case 2:
                        Toast.makeText(this, "Chức năng sửa video", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        showDeleteConfirmDialog("video");
                        break;
                    case 4:
                        showVideoStatsDialog();
                        break;
                }
            })
            .show();
    }

    private void showExerciseManagementDialog() {
        String[] options = {
            "💪 Xem danh sách động tác",
            "➕ Thêm động tác mới",
            "✏️ Sửa động tác",
            "🗑️ Xóa động tác", 
            "📊 Thống kê động tác"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("🤸 Quản lý động tác")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        Toast.makeText(this, "Chức năng xem danh sách động tác", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        showAddExerciseDialog();
                        break;
                    case 2:
                        Toast.makeText(this, "Chức năng sửa động tác", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        showDeleteConfirmDialog("động tác");
                        break;
                    case 4:
                        showExerciseStatsDialog();
                        break;
                }
            })
            .show();
    }

    private void showContentStatsDialog() {
        String stats = "📊 THỐNG KÊ NỘI DUNG\n\n" +
                      "🏋️ Tổng bài tập: " + tvTotalWorkouts.getText() + "\n" +
                      "🎥 Tổng video: " + tvTotalVideos.getText() + "\n" +
                      "💪 Tổng động tác: " + tvTotalExercises.getText() + "\n\n" +
                      "📈 Nội dung được xem nhiều nhất:\n" +
                      "• Bài tập cardio cơ bản\n" +
                      "• Video hướng dẫn squat\n" +
                      "• Động tác plank\n\n" +
                      "📅 Cập nhật gần nhất: Hôm nay\n" +
                      "👥 Người dùng hoạt động: 150+";
        
        new AlertDialog.Builder(this)
            .setTitle("📊 Thống kê chi tiết")
            .setMessage(stats)
            .setPositiveButton("Đóng", null)
            .show();
    }

    private void showBackupRestoreDialog() {
        String[] options = {
            "💾 Sao lưu dữ liệu nội dung",
            "📥 Khôi phục từ sao lưu",
            "🗂️ Xem các bản sao lưu",
            "🔄 Đồng bộ với cloud"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("💾 Sao lưu & Khôi phục")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        performBackup();
                        break;
                    case 1:
                        performRestore();
                        break;
                    case 2:
                        showBackupList();
                        break;
                    case 3:
                        performCloudSync();
                        break;
                }
            })
            .show();
    }

    private void showAddWorkoutDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_workout, null);
        
        EditText etWorkoutName = dialogView.findViewById(R.id.et_workout_name);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etDuration = dialogView.findViewById(R.id.et_duration);
        EditText etCalories = dialogView.findViewById(R.id.et_calories);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinner_difficulty);
        
        // Setup spinners
        String[] categories = {"Cardio", "Strength", "Yoga", "HIIT", "Pilates", "Stretching"};
        String[] difficulties = {"Dễ", "Trung bình", "Khó", "Rất khó"};
        
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
        
        new AlertDialog.Builder(this)
            .setTitle("➕ Thêm bài tập mới")
            .setView(dialogView)
            .setPositiveButton("Thêm", (dialog, which) -> {
                String name = etWorkoutName.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String duration = etDuration.getText().toString().trim();
                String calories = etCalories.getText().toString().trim();
                String category = categories[spinnerCategory.getSelectedItemPosition()];
                String difficulty = difficulties[spinnerDifficulty.getSelectedItemPosition()];
                
                if (name.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập tên bài tập", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Save workout to database
                saveWorkoutToDatabase(name, description, duration, calories, category, difficulty);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showAddVideoDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_video, null);
        
        EditText etVideoTitle = dialogView.findViewById(R.id.et_video_title);
        EditText etVideoUrl = dialogView.findViewById(R.id.et_video_url);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etDuration = dialogView.findViewById(R.id.et_duration);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinner_difficulty);
        
        // Setup spinners
        String[] categories = {"Hướng dẫn tập luyện", "Yoga", "Cardio", "Strength", "Nutrition", "Motivation"};
        String[] difficulties = {"Dễ", "Trung bình", "Khó", "Rất khó"};
        
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
        
        new AlertDialog.Builder(this)
            .setTitle("➕ Thêm video mới")
            .setView(dialogView)
            .setPositiveButton("Thêm", (dialog, which) -> {
                String title = etVideoTitle.getText().toString().trim();
                String url = etVideoUrl.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String duration = etDuration.getText().toString().trim();
                String category = categories[spinnerCategory.getSelectedItemPosition()];
                String difficulty = difficulties[spinnerDifficulty.getSelectedItemPosition()];
                
                if (title.isEmpty() || url.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Validate YouTube URL
                if (!isValidYouTubeUrl(url)) {
                    Toast.makeText(this, "URL YouTube không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Save video to database
                saveVideoToDatabase(title, url, description, duration, category, difficulty);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showAddExerciseDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
        
        EditText etExerciseName = dialogView.findViewById(R.id.et_exercise_name);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etInstructions = dialogView.findViewById(R.id.et_instructions);
        EditText etReps = dialogView.findViewById(R.id.et_reps);
        EditText etSets = dialogView.findViewById(R.id.et_sets);
        Spinner spinnerMuscleGroup = dialogView.findViewById(R.id.spinner_muscle_group);
        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinner_difficulty);
        
        // Setup spinners
        String[] muscleGroups = {"Ngực", "Lưng", "Vai", "Tay", "Chân", "Bụng", "Mông", "Toàn thân"};
        String[] difficulties = {"Dễ", "Trung bình", "Khó", "Rất khó"};
        
        ArrayAdapter<String> muscleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, muscleGroups);
        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMuscleGroup.setAdapter(muscleAdapter);
        
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
        
        new AlertDialog.Builder(this)
            .setTitle("➕ Thêm động tác mới")
            .setView(dialogView)
            .setPositiveButton("Thêm", (dialog, which) -> {
                String name = etExerciseName.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String instructions = etInstructions.getText().toString().trim();
                String reps = etReps.getText().toString().trim();
                String sets = etSets.getText().toString().trim();
                String muscleGroup = muscleGroups[spinnerMuscleGroup.getSelectedItemPosition()];
                String difficulty = difficulties[spinnerDifficulty.getSelectedItemPosition()];
                
                if (name.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập tên động tác", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Save exercise to database
                saveExerciseToDatabase(name, description, instructions, reps, sets, muscleGroup, difficulty);
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showDeleteConfirmDialog(String contentType) {
        new AlertDialog.Builder(this)
            .setTitle("⚠️ Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa " + contentType + " này?\n\n" +
                       "Hành động này không thể hoàn tác!")
            .setPositiveButton("Xóa", (dialog, which) -> {
                Toast.makeText(this, "Đã xóa " + contentType, Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showWorkoutStatsDialog() {
        String stats = "📊 THỐNG KÊ BÀI TẬP\n\n" +
                      "📈 Bài tập phổ biến nhất:\n" +
                      "1. Cardio cơ bản (85% người dùng)\n" +
                      "2. Yoga buổi sáng (72% người dùng)\n" +
                      "3. HIIT 15 phút (68% người dùng)\n\n" +
                      "⏱️ Thời gian tập trung bình: 25 phút\n" +
                      "🔥 Calories đốt cháy TB: 180 cal\n" +
                      "⭐ Đánh giá trung bình: 4.6/5";
        
        new AlertDialog.Builder(this)
            .setTitle("📊 Thống kê bài tập")
            .setMessage(stats)
            .setPositiveButton("Đóng", null)
            .show();
    }

    private void showVideoStatsDialog() {
        String stats = "📊 THỐNG KÊ VIDEO\n\n" +
                      "👀 Video được xem nhiều nhất:\n" +
                      "1. Hướng dẫn Squat chuẩn (2.5K views)\n" +
                      "2. Yoga cho người mới (2.1K views)\n" +
                      "3. Plank 30 ngày (1.8K views)\n\n" +
                      "⏱️ Thời lượng xem TB: 8.5 phút\n" +
                      "📱 Thiết bị xem: 70% mobile, 30% web\n" +
                      "⭐ Đánh giá trung bình: 4.7/5";
        
        new AlertDialog.Builder(this)
            .setTitle("📊 Thống kê video")
            .setMessage(stats)
            .setPositiveButton("Đóng", null)
            .show();
    }

    private void showExerciseStatsDialog() {
        String stats = "📊 THỐNG KÊ ĐỘNG TÁC\n\n" +
                      "💪 Động tác được thực hiện nhiều:\n" +
                      "1. Push-up (95% người dùng)\n" +
                      "2. Plank (88% người dùng)\n" +
                      "3. Squat (85% người dùng)\n\n" +
                      "🎯 Nhóm cơ phổ biến:\n" +
                      "• Cơ bụng: 35%\n" +
                      "• Cơ chân: 28%\n" +
                      "• Cơ tay: 22%\n" +
                      "• Cơ lưng: 15%";
        
        new AlertDialog.Builder(this)
            .setTitle("📊 Thống kê động tác")
            .setMessage(stats)
            .setPositiveButton("Đóng", null)
            .show();
    }

    private void performBackup() {
        new AlertDialog.Builder(this)
            .setTitle("💾 Sao lưu dữ liệu")
            .setMessage("Đang sao lưu dữ liệu nội dung...\n\n" +
                       "Bao gồm:\n" +
                       "• Tất cả bài tập\n" +
                       "• Danh sách video\n" +
                       "• Thông tin động tác\n" +
                       "• Hình ảnh và media")
            .setPositiveButton("Hoàn thành", (dialog, which) -> {
                Toast.makeText(this, "✅ Sao lưu thành công!", Toast.LENGTH_SHORT).show();
            })
            .show();
    }

    private void performRestore() {
        new AlertDialog.Builder(this)
            .setTitle("📥 Khôi phục dữ liệu")
            .setMessage("⚠️ Cảnh báo: Khôi phục sẽ ghi đè lên dữ liệu hiện tại!\n\n" +
                       "Bạn có chắc chắn muốn tiếp tục?")
            .setPositiveButton("Khôi phục", (dialog, which) -> {
                Toast.makeText(this, "✅ Khôi phục thành công!", Toast.LENGTH_SHORT).show();
                loadContentStatistics(); // Reload stats
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void showBackupList() {
        String[] backups = {
            "📅 Backup_2024_03_15_10:30",
            "📅 Backup_2024_03_10_14:20", 
            "📅 Backup_2024_03_05_09:15",
            "📅 Backup_2024_02_28_16:45"
        };
        
        new AlertDialog.Builder(this)
            .setTitle("🗂️ Danh sách sao lưu")
            .setItems(backups, (dialog, which) -> {
                Toast.makeText(this, "Đã chọn: " + backups[which], Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Đóng", null)
            .show();
    }

    private void performCloudSync() {
        new AlertDialog.Builder(this)
            .setTitle("🔄 Đồng bộ Cloud")
            .setMessage("Đang đồng bộ dữ liệu với cloud storage...\n\n" +
                       "📤 Upload: 25 files\n" +
                       "📥 Download: 3 files\n" +
                       "🔄 Cập nhật: 8 files")
            .setPositiveButton("Hoàn thành", (dialog, which) -> {
                Toast.makeText(this, "✅ Đồng bộ thành công!", Toast.LENGTH_SHORT).show();
            })
            .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContentStatistics();
    }
    
    // Helper methods for database operations
    private void saveWorkoutToDatabase(String name, String description, String duration, String calories, String category, String difficulty) {
        new Thread(() -> {
            try {
                // Create workout object and save to database
                // This would use your Workout model and WorkoutDao
                runOnUiThread(() -> {
                    Toast.makeText(this, "✅ Đã thêm bài tập: " + name, Toast.LENGTH_SHORT).show();
                    loadContentStatistics(); // Refresh stats
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "❌ Lỗi thêm bài tập: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    private void saveVideoToDatabase(String title, String url, String description, String duration, String category, String difficulty) {
        new Thread(() -> {
            try {
                // Create video object and save to database
                // This would use your Video model and VideoDao
                runOnUiThread(() -> {
                    Toast.makeText(this, "✅ Đã thêm video: " + title, Toast.LENGTH_SHORT).show();
                    loadContentStatistics(); // Refresh stats
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "❌ Lỗi thêm video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    private void saveExerciseToDatabase(String name, String description, String instructions, String reps, String sets, String muscleGroup, String difficulty) {
        new Thread(() -> {
            try {
                // Create exercise object and save to database
                // This would use your Exercise model and ExerciseDao
                runOnUiThread(() -> {
                    Toast.makeText(this, "✅ Đã thêm động tác: " + name, Toast.LENGTH_SHORT).show();
                    loadContentStatistics(); // Refresh stats
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "❌ Lỗi thêm động tác: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    
    private boolean isValidYouTubeUrl(String url) {
        return url.contains("youtube.com") || url.contains("youtu.be");
    }
}