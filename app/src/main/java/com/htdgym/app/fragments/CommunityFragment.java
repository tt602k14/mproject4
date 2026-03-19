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
import com.htdgym.app.utils.PremiumManager;

public class CommunityFragment extends Fragment {

    private LinearLayout layoutLeaderboard, layoutChallenges;
    private Button btnShareProgress, btnInviteFriends;
    private androidx.cardview.widget.CardView cardShare, cardInvite;

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
        
        // Get quick action cards
        LinearLayout quickActionsLayout = (LinearLayout) view.findViewById(R.id.layout_quick_actions);
        if (quickActionsLayout != null && quickActionsLayout.getChildCount() >= 2) {
            cardShare = (androidx.cardview.widget.CardView) quickActionsLayout.getChildAt(0);
            cardInvite = (androidx.cardview.widget.CardView) quickActionsLayout.getChildAt(1);
            
            cardShare.setOnClickListener(v -> shareProgress());
            cardInvite.setOnClickListener(v -> inviteFriends());
        }
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
        androidx.cardview.widget.CardView itemCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (12 * getResources().getDisplayMetrics().density));
        itemCard.setLayoutParams(cardParams);
        itemCard.setRadius(12 * getResources().getDisplayMetrics().density);
        itemCard.setCardElevation(2 * getResources().getDisplayMetrics().density);
        itemCard.setCardBackgroundColor(isCurrentUser ? 0xFF6FCF97 : 0xFFF8F9FA);
        
        LinearLayout itemLayout = new LinearLayout(requireContext());
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density));
        itemLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        TextView tvRank = new TextView(requireContext());
        tvRank.setText(icon.isEmpty() ? String.valueOf(rank) : icon);
        tvRank.setTextColor(isCurrentUser ? 0xFFFFFFFF : 0xFF1A1A1A);
        tvRank.setTextSize(18);
        tvRank.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams rankParams = new LinearLayout.LayoutParams(
                (int) (60 * getResources().getDisplayMetrics().density), 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvRank.setLayoutParams(rankParams);
        
        TextView tvName = new TextView(requireContext());
        tvName.setText(name);
        tvName.setTextColor(isCurrentUser ? 0xFFFFFFFF : 0xFF1A1A1A);
        tvName.setTextSize(16);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        tvName.setLayoutParams(nameParams);
        
        TextView tvPoints = new TextView(requireContext());
        tvPoints.setText(points + " điểm");
        tvPoints.setTextColor(isCurrentUser ? 0xE0FFFFFF : 0xFFFFA726);
        tvPoints.setTextSize(14);
        tvPoints.setTypeface(null, android.graphics.Typeface.BOLD);
        
        itemLayout.addView(tvRank);
        itemLayout.addView(tvName);
        itemLayout.addView(tvPoints);
        
        itemCard.addView(itemLayout);
        layoutLeaderboard.addView(itemCard);
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
        androidx.cardview.widget.CardView challengeCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, (int) (16 * getResources().getDisplayMetrics().density));
        challengeCard.setLayoutParams(cardParams);
        challengeCard.setRadius(12 * getResources().getDisplayMetrics().density);
        challengeCard.setCardElevation(2 * getResources().getDisplayMetrics().density);
        challengeCard.setCardBackgroundColor(0xFFF8F9FA);
        
        LinearLayout cardLayout = new LinearLayout(requireContext());
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setPadding(
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density),
                (int) (16 * getResources().getDisplayMetrics().density));
        
        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(title);
        tvTitle.setTextColor(0xFF1A1A1A);
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView tvDescription = new TextView(requireContext());
        tvDescription.setText(description);
        tvDescription.setTextColor(0xFF666666);
        tvDescription.setTextSize(14);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        descParams.setMargins(0, (int) (8 * getResources().getDisplayMetrics().density), 0, (int) (8 * getResources().getDisplayMetrics().density));
        tvDescription.setLayoutParams(descParams);
        
        TextView tvProgress = new TextView(requireContext());
        tvProgress.setText("Tiến độ: " + progress);
        tvProgress.setTextColor(0xFFFFA726);
        tvProgress.setTextSize(14);
        tvProgress.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressParams.setMargins(0, 0, 0, (int) (12 * getResources().getDisplayMetrics().density));
        tvProgress.setLayoutParams(progressParams);
        
        Button btnChallenge = new Button(requireContext());
        btnChallenge.setText(buttonText);
        btnChallenge.setTextSize(14);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) (40 * getResources().getDisplayMetrics().density));
        btnChallenge.setLayoutParams(btnParams);
        
        if (isJoined) {
            btnChallenge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF6FCF97));
            btnChallenge.setTextColor(0xFFFFFFFF);
        } else {
            btnChallenge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFA726));
            btnChallenge.setTextColor(0xFFFFFFFF);
        }
        btnChallenge.setOnClickListener(v -> {
            if (!isJoined && !PremiumManager.isPremiumUser(requireContext())) {
                PremiumManager.showPremiumDialog(requireContext(), "Tham gia thử thách " + title);
                return;
            }
            
            Toast.makeText(requireContext(), 
                    isJoined ? "Đang tham gia " + title : "Đã tham gia " + title, 
                    Toast.LENGTH_SHORT).show();
        });
        
        cardLayout.addView(tvTitle);
        cardLayout.addView(tvDescription);
        cardLayout.addView(tvProgress);
        cardLayout.addView(btnChallenge);
        
        challengeCard.addView(cardLayout);
        layoutChallenges.addView(challengeCard);
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
