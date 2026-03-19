package com.htdgym.app.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.htdgym.app.R;
import com.htdgym.app.adapters.AdminUserAdapter;
import com.htdgym.app.models.User;
import com.htdgym.app.utils.AdminManager;

import java.util.ArrayList;
import java.util.List;

public class AdminUserManagementFragment extends Fragment {
    
    private TextInputEditText etSearchUsers;
    private Button btnFilterAll, btnFilterPremium, btnFilterActive;
    private TextView tvTotalUsersCount, tvPremiumUsersCount, tvActiveUsersCount;
    private TextView tvRefreshUsers;
    private RecyclerView rvUsers;
    private ProgressBar progressUsers;
    private LinearLayout layoutEmptyUsers;
    
    private AdminManager adminManager;
    private AdminUserAdapter userAdapter;
    private List<User> allUsers = new ArrayList<>();
    private List<User> filteredUsers = new ArrayList<>();
    private String currentFilter = "all";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user_management, container, false);
        
        adminManager = AdminManager.getInstance(requireContext());
        
        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        setupSearchFilter();
        
        loadUsers();
        
        return view;
    }
    
    private void initViews(View view) {
        etSearchUsers = view.findViewById(R.id.et_search_users);
        btnFilterAll = view.findViewById(R.id.btn_filter_all);
        btnFilterPremium = view.findViewById(R.id.btn_filter_premium);
        btnFilterActive = view.findViewById(R.id.btn_filter_active);
        tvTotalUsersCount = view.findViewById(R.id.tv_total_users_count);
        tvPremiumUsersCount = view.findViewById(R.id.tv_premium_users_count);
        tvActiveUsersCount = view.findViewById(R.id.tv_active_users_count);
        tvRefreshUsers = view.findViewById(R.id.tv_refresh_users);
        rvUsers = view.findViewById(R.id.rv_users);
        progressUsers = view.findViewById(R.id.progress_users);
        layoutEmptyUsers = view.findViewById(R.id.layout_empty_users);
    }
    
    private void setupRecyclerView() {
        userAdapter = new AdminUserAdapter(filteredUsers, new AdminUserAdapter.OnUserActionListener() {
            @Override
            public void onUserClick(User user) {
                showUserDetailsDialog(user);
            }
            
            @Override
            public void onToggleUserStatus(User user) {
                toggleUserStatus(user);
            }
            
            @Override
            public void onDeleteUser(User user) {
                showDeleteUserConfirmation(user);
            }
        });
        
        rvUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvUsers.setAdapter(userAdapter);
    }
    
    private void setupClickListeners() {
        btnFilterAll.setOnClickListener(v -> applyFilter("all"));
        btnFilterPremium.setOnClickListener(v -> applyFilter("premium"));
        btnFilterActive.setOnClickListener(v -> applyFilter("active"));
        
        tvRefreshUsers.setOnClickListener(v -> loadUsers());
    }
    
    private void setupSearchFilter() {
        etSearchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void loadUsers() {
        showLoading(true);
        
        adminManager.getAllUsers(new AdminManager.UserListCallback() {
            @Override
            public void onSuccess(List<User> users) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        allUsers.clear();
                        allUsers.addAll(users);
                        applyFilter(currentFilter);
                        updateStatistics();
                        showLoading(false);
                    });
                }
            }
            
            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showLoading(false);
                        Toast.makeText(requireContext(), "Lỗi tải dữ liệu: " + error, 
                                Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
    
    private void applyFilter(String filter) {
        currentFilter = filter;
        
        // Update button states
        resetFilterButtons();
        switch (filter) {
            case "all":
                btnFilterAll.setBackgroundTintList(
                        getResources().getColorStateList(R.color.primary));
                break;
            case "premium":
                btnFilterPremium.setBackgroundTintList(
                        getResources().getColorStateList(R.color.premium_gold));
                break;
            case "active":
                btnFilterActive.setBackgroundTintList(
                        getResources().getColorStateList(R.color.green_primary));
                break;
        }
        
        // Filter users
        filteredUsers.clear();
        for (User user : allUsers) {
            boolean shouldInclude = false;
            switch (filter) {
                case "all":
                    shouldInclude = true;
                    break;
                case "premium":
                    // TODO: Check if user is premium
                    shouldInclude = user.isActive(); // Placeholder
                    break;
                case "active":
                    shouldInclude = user.isActive();
                    break;
            }
            
            if (shouldInclude) {
                filteredUsers.add(user);
            }
        }
        
        // Apply search filter if exists
        String searchQuery = etSearchUsers.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            filterUsers(searchQuery);
        } else {
            userAdapter.notifyDataSetChanged();
            updateEmptyState();
        }
    }
    
    private void resetFilterButtons() {
        btnFilterAll.setBackgroundTintList(getResources().getColorStateList(R.color.gray_light));
        btnFilterPremium.setBackgroundTintList(getResources().getColorStateList(R.color.gray_light));
        btnFilterActive.setBackgroundTintList(getResources().getColorStateList(R.color.gray_light));
    }
    
    private void filterUsers(String query) {
        if (query.isEmpty()) {
            applyFilter(currentFilter);
            return;
        }
        
        List<User> searchResults = new ArrayList<>();
        for (User user : filteredUsers) {
            if (user.getFullName().toLowerCase().contains(query.toLowerCase()) ||
                user.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                user.getUserId().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(user);
            }
        }
        
        filteredUsers.clear();
        filteredUsers.addAll(searchResults);
        userAdapter.notifyDataSetChanged();
        updateEmptyState();
    }
    
    private void updateStatistics() {
        int totalUsers = allUsers.size();
        int premiumUsers = 0;
        int activeUsers = 0;
        
        for (User user : allUsers) {
            if (user.isActive()) {
                activeUsers++;
            }
            // TODO: Check premium status
            // if (user.isPremium()) premiumUsers++;
        }
        
        tvTotalUsersCount.setText(String.valueOf(totalUsers));
        tvPremiumUsersCount.setText(String.valueOf(premiumUsers));
        tvActiveUsersCount.setText(String.valueOf(activeUsers));
    }
    
    private void showUserDetailsDialog(User user) {
        androidx.appcompat.app.AlertDialog.Builder builder = 
                new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_user_details, null);
        
        // TODO: Populate user details in dialog
        
        builder.setView(dialogView)
                .setTitle("👤 Chi tiết người dùng")
                .setPositiveButton("Đóng", null)
                .show();
    }
    
    private void toggleUserStatus(User user) {
        boolean newStatus = !user.isActive();
        
        adminManager.updateUserStatus(user.getUserId(), newStatus, 
                new AdminManager.AdminActionCallback() {
            @Override
            public void onSuccess(String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        user.setActive(newStatus);
                        userAdapter.notifyDataSetChanged();
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    });
                }
            }
            
            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
    
    private void showDeleteUserConfirmation(User user) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("⚠️ Xóa người dùng")
                .setMessage("Bạn có chắc chắn muốn xóa người dùng \"" + user.getFullName() + "\"?\n\n" +
                           "Hành động này không thể hoàn tác!")
                .setPositiveButton("Xóa", (dialog, which) -> deleteUser(user))
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    private void deleteUser(User user) {
        adminManager.deleteUser(user.getUserId(), new AdminManager.AdminActionCallback() {
            @Override
            public void onSuccess(String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        allUsers.remove(user);
                        filteredUsers.remove(user);
                        userAdapter.notifyDataSetChanged();
                        updateStatistics();
                        updateEmptyState();
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    });
                }
            }
            
            @Override
            public void onError(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
    
    private void showLoading(boolean show) {
        progressUsers.setVisibility(show ? View.VISIBLE : View.GONE);
        rvUsers.setVisibility(show ? View.GONE : View.VISIBLE);
        layoutEmptyUsers.setVisibility(View.GONE);
    }
    
    private void updateEmptyState() {
        boolean isEmpty = filteredUsers.isEmpty();
        rvUsers.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        layoutEmptyUsers.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}