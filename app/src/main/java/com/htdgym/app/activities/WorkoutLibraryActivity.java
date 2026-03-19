package com.htdgym.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htdgym.app.R;
import com.htdgym.app.adapters.ExerciseCardAdapter;
import com.htdgym.app.models.Exercise;
import com.htdgym.app.utils.ExerciseDataManager;
import com.htdgym.app.utils.PremiumManager;

import java.util.ArrayList;
import java.util.List;

public class WorkoutLibraryActivity extends AppCompatActivity {

    private RecyclerView recyclerExercises;
    private ExerciseCardAdapter adapter;
    private CardView tabAll, tabChest, tabBack, tabLegs, tabArms, tabAbs, tabFavorites;
    private EditText etSearch;
    private ImageView btnBack, btnSearch, btnFilter;
    private FloatingActionButton fabAddExercise;
    private String currentFilter = "all";
    private List<Exercise> allExercises;
    private List<Exercise> filteredExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_library);

        initViews();
        setupRecyclerView();
        setupTabs();
        setupSearch();
        setupClickListeners();
        loadExercises();
    }

    private void initViews() {
        recyclerExercises = findViewById(R.id.recycler_exercises);
        tabAll = findViewById(R.id.tab_all);
        tabChest = findViewById(R.id.tab_chest);
        tabBack = findViewById(R.id.tab_back);
        tabLegs = findViewById(R.id.tab_legs);
        tabArms = findViewById(R.id.tab_arms);
        tabAbs = findViewById(R.id.tab_abs);
        tabFavorites = findViewById(R.id.tab_favorites);
        etSearch = findViewById(R.id.et_search);
        btnBack = findViewById(R.id.btn_back);
        btnSearch = findViewById(R.id.btn_search);
        btnFilter = findViewById(R.id.btn_filter);
        fabAddExercise = findViewById(R.id.fab_add_exercise);
    }

    private void setupRecyclerView() {
        adapter = new ExerciseCardAdapter(new ArrayList<>(), new ExerciseCardAdapter.OnExerciseClickListener() {
            @Override
            public void onExerciseClick(Exercise exercise) {
                try {
                    // Open exercise detail with video URL
                    Intent intent = new Intent(WorkoutLibraryActivity.this, ExerciseDetailActivity.class);
                    intent.putExtra("exercise_name", exercise.getName());
                    intent.putExtra("muscle_group", exercise.getMuscleGroup());
                    intent.putExtra("sets_reps", exercise.getFormattedSetsReps());
                    intent.putExtra("rest_time", exercise.getRestTime());
                    intent.putExtra("difficulty", exercise.getDifficulty());
                    intent.putExtra("video_url", exercise.getVideoUrl());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(WorkoutLibraryActivity.this, 
                        "Lỗi mở chi tiết bài tập: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFavoriteClick(Exercise exercise) {
                try {
                    // Handle favorite toggle
                    exercise.setFavorite(!exercise.isFavorite());
                    adapter.notifyDataSetChanged();
                    String message = exercise.isFavorite() ? "Đã thêm vào yêu thích ❤️" : "Đã xóa khỏi yêu thích 💔";
                    Toast.makeText(WorkoutLibraryActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(WorkoutLibraryActivity.this, 
                        "Lỗi cập nhật yêu thích: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerExercises.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerExercises.setAdapter(adapter);
        
        // Add spacing between items
        int spacing = (int) (12 * getResources().getDisplayMetrics().density);
        recyclerExercises.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true));
    }
    
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterExercisesBySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnSearch.setOnClickListener(v -> {
            etSearch.requestFocus();
            // Show keyboard
            android.view.inputmethod.InputMethodManager imm = 
                (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSearch, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
        });
        
        btnFilter.setOnClickListener(v -> showFilterDialog());
        
        fabAddExercise.setOnClickListener(v -> {
            if (PremiumManager.isPremiumUser(this)) {
                Toast.makeText(this, "Tính năng thêm bài tập tùy chỉnh - Coming soon! 🚀", Toast.LENGTH_SHORT).show();
            } else {
                PremiumManager.showPremiumDialog(this, "Thêm bài tập tùy chỉnh");
            }
        });
    }

    private void setupTabs() {
        tabAll.setOnClickListener(v -> filterExercises("all"));
        tabChest.setOnClickListener(v -> filterExercises("chest"));
        tabBack.setOnClickListener(v -> filterExercises("back"));
        tabLegs.setOnClickListener(v -> filterExercises("legs"));
        tabArms.setOnClickListener(v -> filterExercises("shoulder")); // Arms = Shoulder exercises
        tabAbs.setOnClickListener(v -> filterExercises("abs"));
        tabFavorites.setOnClickListener(v -> filterExercises("favorites"));
    }

    private void filterExercises(String filter) {
        currentFilter = filter;
        
        // Reset all tabs
        resetTabStyles();
        
        // Highlight selected tab and update text color
        CardView selectedTab = null;
        switch (filter) {
            case "all":
                selectedTab = tabAll;
                break;
            case "chest":
                selectedTab = tabChest;
                break;
            case "back":
                selectedTab = tabBack;
                break;
            case "legs":
                selectedTab = tabLegs;
                break;
            case "shoulder":
                selectedTab = tabArms;
                break;
            case "abs":
                selectedTab = tabAbs;
                break;
            case "favorites":
                selectedTab = tabFavorites;
                break;
        }
        
        if (selectedTab != null) {
            selectedTab.setCardBackgroundColor(0xFF6FCF97);
            // Update text color to white for selected tab
            android.widget.TextView textView = (android.widget.TextView) selectedTab.getChildAt(0);
            textView.setTextColor(0xFFFFFFFF);
        }
        
        applyFilters();
    }

    private void resetTabStyles() {
        // Reset background colors
        tabAll.setCardBackgroundColor(0xFFF0F0F0);
        tabChest.setCardBackgroundColor(0xFFF0F0F0);
        tabBack.setCardBackgroundColor(0xFFF0F0F0);
        tabLegs.setCardBackgroundColor(0xFFF0F0F0);
        tabArms.setCardBackgroundColor(0xFFF0F0F0);
        tabAbs.setCardBackgroundColor(0xFFF0F0F0);
        tabFavorites.setCardBackgroundColor(0xFFF0F0F0);
        
        // Reset text colors
        ((android.widget.TextView) tabAll.getChildAt(0)).setTextColor(0xFF666666);
        ((android.widget.TextView) tabChest.getChildAt(0)).setTextColor(0xFF666666);
        ((android.widget.TextView) tabBack.getChildAt(0)).setTextColor(0xFF666666);
        ((android.widget.TextView) tabLegs.getChildAt(0)).setTextColor(0xFF666666);
        ((android.widget.TextView) tabArms.getChildAt(0)).setTextColor(0xFF666666);
        ((android.widget.TextView) tabAbs.getChildAt(0)).setTextColor(0xFF666666);
        ((android.widget.TextView) tabFavorites.getChildAt(0)).setTextColor(0xFF666666);
    }
    
    private void filterExercisesBySearch(String query) {
        if (allExercises == null) return;
        
        List<Exercise> searchResults = new ArrayList<>();
        String lowerQuery = query.toLowerCase().trim();
        
        if (lowerQuery.isEmpty()) {
            filteredExercises = null;
        } else {
            for (Exercise exercise : allExercises) {
                if (exercise.getName().toLowerCase().contains(lowerQuery) ||
                    exercise.getMuscleGroup().toLowerCase().contains(lowerQuery) ||
                    exercise.getDifficulty().toLowerCase().contains(lowerQuery)) {
                    searchResults.add(exercise);
                }
            }
            filteredExercises = searchResults;
        }
        
        applyFilters();
    }
    
    private void applyFilters() {
        List<Exercise> exercises = filteredExercises != null ? filteredExercises : allExercises;
        
        if (currentFilter.equals("favorites")) {
            // Show only favorite exercises
            List<Exercise> favorites = new ArrayList<>();
            for (Exercise exercise : exercises) {
                if (exercise.isFavorite()) {
                    favorites.add(exercise);
                }
            }
            adapter.updateExercises(favorites);
        } else if (!currentFilter.equals("all")) {
            // Filter by muscle group
            List<Exercise> filtered = new ArrayList<>();
            for (Exercise exercise : exercises) {
                if (exercise.getMuscleGroup().toLowerCase().equals(currentFilter.toLowerCase())) {
                    filtered.add(exercise);
                }
            }
            adapter.updateExercises(filtered);
        } else {
            // Show one exercise from each category for "all"
            List<Exercise> allCategoryExercises = new ArrayList<>();
            
            // Add one exercise from each category
            List<Exercise> chestExercises = ExerciseDataManager.getChestExercises();
            if (!chestExercises.isEmpty()) allCategoryExercises.add(chestExercises.get(0));
            
            List<Exercise> shoulderExercises = ExerciseDataManager.getShoulderExercises();
            if (!shoulderExercises.isEmpty()) allCategoryExercises.add(shoulderExercises.get(0));
            
            List<Exercise> legsExercises = ExerciseDataManager.getLegsExercises();
            if (!legsExercises.isEmpty()) allCategoryExercises.add(legsExercises.get(0));
            
            List<Exercise> absExercises = ExerciseDataManager.getAbsExercises();
            if (!absExercises.isEmpty()) allCategoryExercises.add(absExercises.get(0));
            
            List<Exercise> backExercises = ExerciseDataManager.getBackExercises();
            if (!backExercises.isEmpty()) allCategoryExercises.add(backExercises.get(0));
            
            adapter.updateExercises(allCategoryExercises);
        }
    }
    
    private void showFilterDialog() {
        String[] difficulties = {"Tất cả", "Dễ", "Trung bình", "Khó"};
        
        new android.app.AlertDialog.Builder(this)
            .setTitle("Lọc theo độ khó")
            .setItems(difficulties, (dialog, which) -> {
                String selectedDifficulty = difficulties[which];
                if (selectedDifficulty.equals("Tất cả")) {
                    // Reset filter
                    loadExercises();
                } else {
                    filterByDifficulty(selectedDifficulty);
                }
                Toast.makeText(this, "Lọc: " + selectedDifficulty, Toast.LENGTH_SHORT).show();
            })
            .show();
    }
    
    private void filterByDifficulty(String difficulty) {
        if (allExercises == null) return;
        
        List<Exercise> filtered = new ArrayList<>();
        for (Exercise exercise : allExercises) {
            if (exercise.getDifficulty().equals(difficulty)) {
                filtered.add(exercise);
            }
        }
        
        filteredExercises = filtered;
        applyFilters();
    }

    private void loadExercises() {
        // Load all exercises from ExerciseDataManager
        allExercises = ExerciseDataManager.getAllExercises();
        filteredExercises = null;
        
        // Set initial filter to "all"
        currentFilter = "all";
        filterExercises("all");
    }
    
    /**
     * Open YouTube video
     */
    private void openYouTubeVideo(String videoUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(videoUrl));
            intent.setPackage("com.google.android.youtube"); // Try to open in YouTube app
            startActivity(intent);
        } catch (Exception e) {
            // If YouTube app not installed, open in browser
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(videoUrl));
                startActivity(intent);
            } catch (Exception ex) {
                Toast.makeText(this, "Không thể mở video", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // GridSpacingItemDecoration class
    public static class GridSpacingItemDecoration extends androidx.recyclerview.widget.RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(android.graphics.Rect outRect, android.view.View view, 
                androidx.recyclerview.widget.RecyclerView parent, 
                androidx.recyclerview.widget.RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
}
