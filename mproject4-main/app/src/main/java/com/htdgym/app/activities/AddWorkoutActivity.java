package com.htdgym.app.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.htdgym.app.R;
import com.htdgym.app.models.Workout;
import com.htdgym.app.viewmodels.WorkoutViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.htdgym.app.activities.BaseActivity;

public class AddWorkoutActivity extends BaseActivity {
    private EditText etExerciseName, etSets, etReps, etWeight, etDuration, etNotes, etWorkoutDateTime;
    private Spinner spinnerCategory;
    private Button btnSave, btnCancel;
    private WorkoutViewModel workoutViewModel;
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private Date workoutDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        initViews();
        setupSpinner();
        setupViewModel();
        setupClickListeners();
    }

    private void initViews() {
        etExerciseName = findViewById(R.id.et_exercise_name);
        etSets = findViewById(R.id.et_sets);
        etReps = findViewById(R.id.et_reps);
        etWeight = findViewById(R.id.et_weight);
        etDuration = findViewById(R.id.et_duration);
        etNotes = findViewById(R.id.et_notes);
        etWorkoutDateTime = findViewById(R.id.et_workout_datetime);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnSave = findViewById(R.id.btn_save_workout);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void setupSpinner() {
        String[] categories = {"Cardio", "Sức mạnh", "Linh hoạt", "Cân bằng", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void setupViewModel() {
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
    }

    private void setupClickListeners() {
        etWorkoutDateTime.setOnClickListener(v -> showDateTimePicker());
        btnSave.setOnClickListener(v -> saveWorkout());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (timeView, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        workoutDateTime = calendar.getTime();
                        etWorkoutDateTime.setText(dateTimeFormat.format(workoutDateTime));
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                );
                timePickerDialog.show();
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveWorkout() {
        String exerciseName = etExerciseName.getText().toString().trim();
        String setsStr = etSets.getText().toString().trim();
        String repsStr = etReps.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String durationStr = etDuration.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (exerciseName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên bài tập", Toast.LENGTH_SHORT).show();
            return;
        }

        if (workoutDateTime == null) {
            Toast.makeText(this, "Vui lòng chọn ngày và giờ tập", Toast.LENGTH_SHORT).show();
            return;
        }

        int sets = setsStr.isEmpty() ? 0 : Integer.parseInt(setsStr);
        int reps = repsStr.isEmpty() ? 0 : Integer.parseInt(repsStr);
        double weight = weightStr.isEmpty() ? 0.0 : Double.parseDouble(weightStr);
        int duration = durationStr.isEmpty() ? 0 : Integer.parseInt(durationStr);

        Workout workout = new Workout(exerciseName, sets, reps, weight, duration, workoutDateTime);
        
        workoutViewModel.insert(workout);

        Toast.makeText(this, "Đã thêm bài tập thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}