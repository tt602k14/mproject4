package com.htdgym.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htdgym.app.R;
import com.htdgym.app.activities.AddMemberActivity;
import com.htdgym.app.adapters.MemberAdapter;
import com.htdgym.app.viewmodels.MemberViewModel;

public class MembersFragment extends Fragment {
    private MemberViewModel memberViewModel;
    private RecyclerView recyclerView;
    private MemberAdapter adapter;
    private FloatingActionButton fabAddMember;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_members, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupViewModel();
        observeData();
        setupClickListeners();
        
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_members);
        fabAddMember = view.findViewById(R.id.fab_add_member);
    }

    private void setupRecyclerView() {
        adapter = new MemberAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
    }

    private void observeData() {
        memberViewModel.getAllMembers().observe(getViewLifecycleOwner(), members -> {
            adapter.setMembers(members);
        });
    }

    private void setupClickListeners() {
        fabAddMember.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddMemberActivity.class);
            startActivity(intent);
        });
    }
}