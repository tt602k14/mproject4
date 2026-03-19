package com.htdgym.app.fragments;

import android.app.AlertDialog;
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
    private Button btnShareProgress, btnInviteFriends, btnCreateChallenge;
    private Button btnViewAllLeaderboard, btnUpgradePremium;
    private Button btnShareFacebook, btnShareInstagram, btnShareWhatsapp;
    private PremiumManager premiumManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        
        premiumManager = PremiumManager.getInstance(requireContext());
        
        initViews(view);
        setupLeaderboard();
        setupChallenges();
        setupClickListeners();
        setupAnimations();
        
        return view;
    }

    private void initViews(View view) {
        layoutLeaderboard = view.findViewById(R.id.layout_leaderboard);
        layoutChallenges = view.findViewById(R.id.layout_challenges);
        btnShareProgress = view.findViewById(R.id.btn_share_progress);
        btnInviteFriends = view.findViewById(R.id.btn_invite_friends);
        btnCreateChallenge = view.findViewById(R.id.btn_create_challenge);
        btnViewAllLeaderboard = view.findViewById(R.id.btn_view_all_leaderboard);
        btnUpgradePremium = view.findViewById(R.id.btn_upgrade_premium);
        btnShareFacebook = view.findViewById(R.id.btn_share_facebook);
        btnShareInstagram = view.findViewById(R.id.btn_share_instagram);
        btnShareWhatsapp = view.findViewById(R.id.btn_share_whatsapp);
    }

    private void setupAnimations() {
        // Add entrance animations for cards
        if (getView() != null) {
            getView().post(() -> {
                animateViewsIn();
            });
        }
    }

    private void animateViewsIn() {
        // Animate leaderboard items with staggered delay
        for (int i = 0; i < layoutLeaderboard.getChildCount(); i++) {
            View child = layoutLeaderboard.getChildAt(i);
            if (child != null) {
                child.setAlpha(0f);
                child.setTranslationX(-100f);
                child.animate()
                    .alpha(1f)
                    .translationX(0f)
                    .setDuration(400)
                    .setStartDelay(i * 100)
                    .start();
            }
        }
        
        // Animate challenge items
        for (int i = 0; i < layoutChallenges.getChildCount(); i++) {
            View child = layoutChallenges.getChildAt(i);
            if (child != null) {
                child.setAlpha(0f);
                child.setTranslationY(100f);
                child.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .setStartDelay((i + 3) * 150)
                    .start();
            }
        }
    }

    private void setupLeaderboard() {
        addLeaderboardItem(1, "Nguyen Van A", "2,450", "🥇", true);
        addLeaderboardItem(2, "Tran Thi B", "2,380", "🥈", false);
        addLeaderboardItem(3, "Le Van C", "2,290", "🥉", false);
        addLeaderboardItem(4, "Ban", "2,150", "👤", true);
        addLeaderboardItem(5, "Pham Thi D", "2,100", "", false);
    }

    private void addLeaderboardItem(int rank, String name, String points, String icon, boolean isCurrentUser) {
        // Create enhanced leaderboard card
        androidx.cardview.widget.CardView cardView = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 16);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(16f);
        cardView.setCardElevation(8f);
        
        // Set background color based on user type
        if (isCurrentUser) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.green_primary));
        } else if (rank <= 3) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.premium_gold_light));
        } else {
            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        }
        
        LinearLayout itemLayout = new LinearLayout(requireContext());
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(20, 20, 20, 20);
        itemLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        // Rank/Icon section
        androidx.cardview.widget.CardView rankCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams rankParams = new LinearLayout.LayoutParams(60, 60);
        rankParams.setMargins(0, 0, 16, 0);
        rankCard.setLayoutParams(rankParams);
        rankCard.setRadius(30f);
        rankCard.setCardElevation(6f);
        
        if (rank <= 3) {
            rankCard.setCardBackgroundColor(getResources().getColor(R.color.premium_gold));
        } else {
            rankCard.setCardBackgroundColor(getResources().getColor(R.color.primary));
        }
        
        TextView tvRank = new TextView(requireContext());
        tvRank.setText(icon.isEmpty() ? String.valueOf(rank) : icon);
        tvRank.setTextColor(getResources().getColor(android.R.color.white));
        tvRank.setTextSize(18);
        tvRank.setTypeface(null, android.graphics.Typeface.BOLD);
        tvRank.setGravity(android.view.Gravity.CENTER);
        tvRank.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.MATCH_PARENT));
        
        rankCard.addView(tvRank);
        
        // Name and info section
        LinearLayout infoLayout = new LinearLayout(requireContext());
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        infoLayout.setLayoutParams(infoParams);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView tvName = new TextView(requireContext());
        tvName.setText(name);
        tvName.setTextColor(getResources().getColor(
                isCurrentUser || rank <= 3 ? android.R.color.white : R.color.text_primary));
        tvName.setTextSize(16);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView tvRankText = new TextView(requireContext());
        tvRankText.setText("Hạng " + rank + " tuần này");
        tvRankText.setTextColor(getResources().getColor(
                isCurrentUser || rank <= 3 ? android.R.color.white : R.color.text_secondary));
        tvRankText.setTextSize(12);
        LinearLayout.LayoutParams rankTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rankTextParams.setMargins(0, 4, 0, 0);
        tvRankText.setLayoutParams(rankTextParams);
        
        infoLayout.addView(tvName);
        infoLayout.addView(tvRankText);
        
        // Points section
        LinearLayout pointsLayout = new LinearLayout(requireContext());
        pointsLayout.setOrientation(LinearLayout.VERTICAL);
        pointsLayout.setGravity(android.view.Gravity.END);
        
        TextView tvPoints = new TextView(requireContext());
        tvPoints.setText(points);
        tvPoints.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        tvPoints.setTextSize(18);
        tvPoints.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView tvPointsLabel = new TextView(requireContext());
        tvPointsLabel.setText("điểm");
        tvPointsLabel.setTextColor(getResources().getColor(
                isCurrentUser || rank <= 3 ? android.R.color.white : R.color.text_secondary));
        tvPointsLabel.setTextSize(12);
        LinearLayout.LayoutParams pointsLabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pointsLabelParams.setMargins(0, 2, 0, 0);
        tvPointsLabel.setLayoutParams(pointsLabelParams);
        
        pointsLayout.addView(tvPoints);
        pointsLayout.addView(tvPointsLabel);
        
        itemLayout.addView(rankCard);
        itemLayout.addView(infoLayout);
        itemLayout.addView(pointsLayout);
        
        cardView.addView(itemLayout);
        layoutLeaderboard.addView(cardView);
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
        // Create enhanced challenge card
        androidx.cardview.widget.CardView cardView = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 20);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(16f);
        cardView.setCardElevation(8f);
        cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        
        LinearLayout cardLayout = new LinearLayout(requireContext());
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setPadding(24, 24, 24, 24);
        
        // Challenge header with icon
        LinearLayout headerLayout = new LinearLayout(requireContext());
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        headerLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerParams.setMargins(0, 0, 0, 16);
        headerLayout.setLayoutParams(headerParams);
        
        // Challenge icon
        androidx.cardview.widget.CardView iconCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(48, 48);
        iconParams.setMargins(0, 0, 16, 0);
        iconCard.setLayoutParams(iconParams);
        iconCard.setRadius(24f);
        iconCard.setCardElevation(4f);
        iconCard.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
        
        TextView iconText = new TextView(requireContext());
        iconText.setText("🏁");
        iconText.setTextSize(20);
        iconText.setGravity(android.view.Gravity.CENTER);
        iconText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        iconCard.addView(iconText);
        
        // Title and status
        LinearLayout titleLayout = new LinearLayout(requireContext());
        titleLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        titleLayout.setLayoutParams(titleLayoutParams);
        
        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(title);
        tvTitle.setTextColor(getResources().getColor(R.color.text_primary));
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        
        // Status badge
        androidx.cardview.widget.CardView statusCard = new androidx.cardview.widget.CardView(requireContext());
        LinearLayout.LayoutParams statusParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        statusParams.setMargins(0, 8, 0, 0);
        statusCard.setLayoutParams(statusParams);
        statusCard.setRadius(12f);
        statusCard.setCardElevation(2f);
        statusCard.setCardBackgroundColor(getResources().getColor(
                isJoined ? R.color.green_primary : R.color.secondary));
        
        TextView statusText = new TextView(requireContext());
        statusText.setText(isJoined ? "🔥 Đang tham gia" : "⭐ Mở");
        statusText.setTextColor(getResources().getColor(android.R.color.white));
        statusText.setTextSize(10);
        statusText.setTypeface(null, android.graphics.Typeface.BOLD);
        statusText.setPadding(12, 6, 12, 6);
        statusCard.addView(statusText);
        
        titleLayout.addView(tvTitle);
        titleLayout.addView(statusCard);
        
        headerLayout.addView(iconCard);
        headerLayout.addView(titleLayout);
        
        // Description
        TextView tvDescription = new TextView(requireContext());
        tvDescription.setText(description);
        tvDescription.setTextColor(getResources().getColor(R.color.text_secondary));
        tvDescription.setTextSize(14);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        descParams.setMargins(0, 0, 0, 16);
        tvDescription.setLayoutParams(descParams);
        
        // Progress section
        LinearLayout progressLayout = new LinearLayout(requireContext());
        progressLayout.setOrientation(LinearLayout.HORIZONTAL);
        progressLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams progressLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressLayoutParams.setMargins(0, 0, 0, 20);
        progressLayout.setLayoutParams(progressLayoutParams);
        
        TextView progressIcon = new TextView(requireContext());
        progressIcon.setText("📊");
        progressIcon.setTextSize(16);
        LinearLayout.LayoutParams progressIconParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressIconParams.setMargins(0, 0, 12, 0);
        progressIcon.setLayoutParams(progressIconParams);
        
        TextView tvProgress = new TextView(requireContext());
        tvProgress.setText("Tiến độ: " + progress);
        tvProgress.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        tvProgress.setTextSize(14);
        tvProgress.setTypeface(null, android.graphics.Typeface.BOLD);
        
        progressLayout.addView(progressIcon);
        progressLayout.addView(tvProgress);
        
        // Action button
        androidx.cardview.widget.CardView buttonCard = new androidx.cardview.widget.CardView(requireContext());
        buttonCard.setRadius(16f);
        buttonCard.setCardElevation(6f);
        buttonCard.setCardBackgroundColor(getResources().getColor(
                isJoined ? R.color.green_primary : R.color.secondary));
        
        Button btnChallenge = new Button(requireContext());
        btnChallenge.setText(buttonText);
        btnChallenge.setTextColor(getResources().getColor(android.R.color.white));
        btnChallenge.setTextSize(16);
        btnChallenge.setTypeface(null, android.graphics.Typeface.BOLD);
        btnChallenge.setBackground(null);
        btnChallenge.setPadding(24, 16, 24, 16);
        btnChallenge.setOnClickListener(v -> {
            Toast.makeText(requireContext(), 
                    isJoined ? "Đang tham gia " + title : "Đã tham gia " + title, 
                    Toast.LENGTH_SHORT).show();
        });
        
        buttonCard.addView(btnChallenge);
        
        cardLayout.addView(headerLayout);
        cardLayout.addView(tvDescription);
        cardLayout.addView(progressLayout);
        cardLayout.addView(buttonCard);
        
        cardView.addView(cardLayout);
        layoutChallenges.addView(cardView);
    }

    private void setupClickListeners() {
        btnShareProgress.setOnClickListener(v -> shareProgress());
        btnInviteFriends.setOnClickListener(v -> inviteFriends());
        btnCreateChallenge.setOnClickListener(v -> createChallenge());
        btnViewAllLeaderboard.setOnClickListener(v -> viewAllLeaderboard());
        btnUpgradePremium.setOnClickListener(v -> upgradeToPremium());
        
        // Social media sharing
        btnShareFacebook.setOnClickListener(v -> shareToSocialMedia("facebook"));
        btnShareInstagram.setOnClickListener(v -> shareToSocialMedia("instagram"));
        btnShareWhatsapp.setOnClickListener(v -> shareToSocialMedia("whatsapp"));
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

    private void createChallenge() {
        if (!premiumManager.isPremiumUser()) {
            showPremiumRequiredDialog("Tạo thử thách tùy chỉnh");
            return;
        }

        // Show create challenge dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("🎯 Tạo thử thách mới")
                .setMessage("Tính năng tạo thử thách tùy chỉnh đang được phát triển.\n\nVới Premium, bạn có thể:\n• Tạo thử thách riêng\n• Thiết lập giải thưởng\n• Mời bạn bè tham gia")
                .setPositiveButton("Đã hiểu", null)
                .show();
    }

    private void viewAllLeaderboard() {
        Toast.makeText(requireContext(), "📊 Đang mở bảng xếp hạng đầy đủ...", Toast.LENGTH_SHORT).show();
        // TODO: Navigate to full leaderboard activity
    }

    private void upgradeToPremium() {
        // Navigate to Premium activity
        Intent intent = new Intent(requireContext(), com.htdgym.app.activities.PremiumActivity.class);
        startActivity(intent);
    }

    private void shareToSocialMedia(String platform) {
        String message = "🏋️ Tôi đang tập luyện với HTD GYM!\n\n" +
                        "📊 Thành tích của tôi:\n" +
                        "• 34 buổi tập hoàn thành\n" +
                        "• 8,500 calo đã đốt cháy\n" +
                        "• 7 ngày tập liên tiếp\n\n" +
                        "Tham gia cùng tôi tại: https://htdgym.app 💪";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);

        switch (platform) {
            case "facebook":
                shareIntent.setPackage("com.facebook.katana");
                break;
            case "instagram":
                shareIntent.setPackage("com.instagram.android");
                break;
            case "whatsapp":
                shareIntent.setPackage("com.whatsapp");
                break;
        }

        try {
            startActivity(shareIntent);
        } catch (Exception e) {
            // Fallback to general share if specific app not found
            shareIntent.setPackage(null);
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua " + platform));
        }
    }

    private void showPremiumRequiredDialog(String feature) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("👑 Tính năng Premium")
                .setMessage("Tính năng \"" + feature + "\" chỉ dành cho thành viên Premium.\n\n" +
                           "Nâng cấp ngay để trải nghiệm:\n" +
                           "• Nhóm riêng tư\n" +
                           "• Thử thách tùy chỉnh\n" +
                           "• Phân tích nâng cao\n" +
                           "• Và nhiều tính năng khác!")
                .setPositiveButton("Nâng cấp ngay", (dialog, which) -> upgradeToPremium())
                .setNegativeButton("Để sau", null)
                .show();
    }
}
