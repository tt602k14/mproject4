package com.htdgym.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.htdgym.app.R;

public class CommunityFragment extends Fragment {

    private LinearLayout layoutLeaderboard, layoutChallenges;
    private Button btnShareProgress, btnInviteFriends;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        
        initViews(view);
        setupLeaderboard();
        setupChallenges();
        setupClickListeners();
        
        return view;
    }

    private void initViews(View view) {
        layoutLeaderboard = view.findViewById(R.id.layout_leaderboard);
        layoutChallenges = view.findViewById(R.id.layout_challenges);
        // btnShareProgress = view.findViewById(R.id.btn_share_progress);
        // btnInviteFriends = view.findViewById(R.id.btn_invite_friends);
    }

    private void setupLeaderboard() {
        addLeaderboardItem(1, "Nguyen Van A", "2,450", "🥇", true);
        addLeaderboardItem(2, "Tran Thi B", "2,380", "🥈", false);
        addLeaderboardItem(3, "Le Van C", "2,290", "🥉", false);
        addLeaderboardItem(4, "Ban", "2,150", "👤", true);
        addLeaderboardItem(5, "Pham Thi D", "2,100", "", false);
    }

    private void addLeaderboardItem(int rank, String name, String points, String icon, boolean isCurrentUser) {
        LinearLayout itemLayout = new LinearLayout(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 12);
        itemLayout.setLayoutParams(params);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setBackgroundColor(getResources().getColor(
                isCurrentUser ? R.color.green_primary : R.color.gray_dark));
        itemLayout.setPadding(16, 16, 16, 16);
        
        TextView tvRank = new TextView(requireContext());
        tvRank.setText(icon.isEmpty() ? String.valueOf(rank) : icon);
        tvRank.setTextColor(getResources().getColor(android.R.color.white));
        tvRank.setTextSize(18);
        tvRank.setWidth(80);
        
        TextView tvName = new TextView(requireContext());
        tvName.setText(name);
        tvName.setTextColor(getResources().getColor(android.R.color.white));
        tvName.setTextSize(16);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        tvName.setLayoutParams(nameParams);
        
        TextView tvPoints = new TextView(requireContext());
        tvPoints.setText(points + " điểm");
        tvPoints.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        tvPoints.setTextSize(14);
        tvPoints.setTypeface(null, android.graphics.Typeface.BOLD);
        
        itemLayout.addView(tvRank);
        itemLayout.addView(tvName);
        itemLayout.addView(tvPoints);
        
        layoutLeaderboard.addView(itemLayout);
    }

    private void setupChallenges() {
        addChallengeCard("30 Ngày Squat", "Hoàn thành 100 squat mỗi ngày", 
                        "15/30 ngày", "Tham gia", false);
        
        addChallengeCard("Chạy 50km", "Chạy tổng cộng 50km trong tháng", 
                        "32/50 km", "Đang tham gia", true);
        
        addChallengeCard("Plank Master", "Giữ plank 5 phút liên tục", 
                        "3:20/5:00", "Tham gia", false);
    }

    private void addChallengeCard(String title, String description, String progress, 
                                 String buttonText, boolean isJoined) {
        LinearLayout cardLayout = new LinearLayout(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        cardLayout.setLayoutParams(params);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setBackgroundColor(getResources().getColor(R.color.gray_dark));
        cardLayout.setPadding(16, 16, 16, 16);
        
        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(title);
        tvTitle.setTextColor(getResources().getColor(android.R.color.white));
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView tvDescription = new TextView(requireContext());
        tvDescription.setText(description);
        tvDescription.setTextColor(getResources().getColor(R.color.gray_light));
        tvDescription.setTextSize(14);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        descParams.setMargins(0, 8, 0, 8);
        tvDescription.setLayoutParams(descParams);
        
        TextView tvProgress = new TextView(requireContext());
        tvProgress.setText("Tiến độ: " + progress);
        tvProgress.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        tvProgress.setTextSize(14);
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressParams.setMargins(0, 0, 0, 12);
        tvProgress.setLayoutParams(progressParams);
        
        Button btnChallenge = new Button(requireContext());
        btnChallenge.setText(buttonText);
        if (isJoined) {
            btnChallenge.setBackgroundTintList(getResources().getColorStateList(R.color.green_primary));
        } else {
            btnChallenge.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_orange_light));
        }
        btnChallenge.setOnClickListener(v -> {
            Toast.makeText(requireContext(), 
                    isJoined ? "Đang tham gia " + title : "Đã tham gia " + title, 
                    Toast.LENGTH_SHORT).show();
        });
        
        cardLayout.addView(tvTitle);
        cardLayout.addView(tvDescription);
        cardLayout.addView(tvProgress);
        cardLayout.addView(btnChallenge);
        
        layoutChallenges.addView(cardLayout);
    }

    private void setupClickListeners() {
        // btnShareProgress.setOnClickListener(v -> shareProgress());
        // btnInviteFriends.setOnClickListener(v -> inviteFriends());
    }

    private void shareProgress() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, 
                "Tôi đã tập luyện 34 buổi và đốt cháy 8,500 calo với HTD GYM! 💪🔥");
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ tiến độ"));
    }

    private void inviteFriends() {
        Intent inviteIntent = new Intent(Intent.ACTION_SEND);
        inviteIntent.setType("text/plain");
        inviteIntent.putExtra(Intent.EXTRA_TEXT, 
                "Tham gia HTD GYM cùng tôi! Tải app tại: https://htdgym.app 🏋️");
        startActivity(Intent.createChooser(inviteIntent, "Mời bạn bè"));
    }
}
