package com.htdgym.app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.WorkoutAdapter;
import com.htdgym.app.models.Workout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private List<Workout> workoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        recyclerView = findViewById(R.id.recyclerWorkout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workoutList = new ArrayList<>();

        workoutList.add(new Workout(
                "Bench Press",
                4,
                10,
                60.0,
                30,
                new Date()
        ));

        workoutList.add(new Workout(
                "Squat",
                4,
                12,
                80.0,
                40,
                new Date()
        ));

        adapter = new WorkoutAdapter(workoutList);
        recyclerView.setAdapter(adapter);
    }
}
