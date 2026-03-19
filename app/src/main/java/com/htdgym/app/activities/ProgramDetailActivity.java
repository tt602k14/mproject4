package com.htdgym.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.tabs.TabLayout;
import com.htdgym.app.R;
import com.htdgym.app.utils.PremiumManager;
import com.htdgym.app.utils.ProgramScheduleData;
import com.htdgym.app.utils.ProgramScheduleData.DaySchedule;

import java.util.List;

public class ProgramDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvProgramTitle, tvProgramDescription;
    private TextView tvStatDays, tvStatLevel;
    private TabLayout tabLayout;
    private LinearLayout layoutSchedule;
    private LinearLayout premiumLockCard;
    private String programType;
    private String currentTab = "30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);

        programType = getIntent().getStringExtra("program_type");
        if (programType == null) programType = "30";
        currentTab = programType;

        initViews();
        setupTabs();
        loadProgramData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvProgramTitle = findViewById(R.id.tv_program_title);
        tvProgramDescription = findViewById(R.id.tv_program_description);
        tvStatDays = findViewById(R.id.tv_stat_days);
        tvStatLevel = findViewById(R.id.tv_stat_level);
        tabLayout = findViewById(R.id.tab_layout);
        premiumLockCard = findViewById(R.id.premium_lock_card);
        layoutSchedule = findViewById(R.id.layout_schedule);

        btnBack.setOnClickListener(v -> finish());
        premiumLockCard.setOnClickListener(v ->
                PremiumManager.showPremiumDialog(this, "Chương trình " + currentTab + " ngày"));
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("30 Ngày"));
        tabLayout.addTab(tabLayout.newTab().setText("60 Ngày"));
        tabLayout.addTab(tabLayout.newTab().setText("90 Ngày"));

        switch (programType) {
            case "60": tabLayout.selectTab(tabLayout.getTabAt(1)); break;
            case "90": tabLayout.selectTab(tabLayout.getTabAt(2)); break;
            default:   tabLayout.selectTab(tabLayout.getTabAt(0)); break;
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition() == 0 ? "30" : tab.getPosition() == 1 ? "60" : "90";
                loadProgramData();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadProgramData() {
        boolean isPremium = PremiumManager.isPremiumUser(this);
        boolean needsPremium = !currentTab.equals("30");
        View scrollView = findViewById(R.id.recycler_exercises);
        View heroBanner = findViewById(R.id.hero_banner);

        // Đổi màu hero theo tab
        if (heroBanner != null) {
            int color;
            switch (currentTab) {
                case "60": color = Color.parseColor("#11998e"); break;
                case "90": color = Color.parseColor("#eb3349"); break;
                default:   color = Color.parseColor("#667eea"); break;
            }
            heroBanner.setBackgroundColor(color);
        }

        // Cập nhật stats
        if (tvStatDays != null) tvStatDays.setText(currentTab);
        if (tvStatLevel != null) {
            switch (currentTab) {
                case "60": tvStatLevel.setText("Trung cấp"); break;
                case "90": tvStatLevel.setText("Nâng cao"); break;
                default:   tvStatLevel.setText("Cơ bản"); break;
            }
        }

        if (needsPremium && !isPremium) {
            premiumLockCard.setVisibility(View.VISIBLE);
            if (scrollView != null) scrollView.setVisibility(View.GONE);
            tvProgramTitle.setText("Chương trình " + currentTab + " ngày 🔒");
            tvProgramDescription.setText("Yêu cầu Premium để truy cập.");
            return;
        }

        premiumLockCard.setVisibility(View.GONE);
        if (scrollView != null) scrollView.setVisibility(View.VISIBLE);

        String desc;
        switch (currentTab) {
            case "60":
                tvProgramTitle.setText("Chương trình 60 ngày 💪");
                desc = "Tăng cường sức mạnh & phát triển cơ bắp toàn diện.";
                break;
            case "90":
                tvProgramTitle.setText("Chương trình 90 ngày 🏆");
                desc = "Transformation hoàn toàn — sức mạnh, cơ bắp & thể lực đỉnh cao.";
                break;
            default:
                tvProgramTitle.setText("Chương trình 30 ngày 🌱");
                desc = "Xây dựng thói quen tập luyện & làm quen bài tập cơ bản.";
                break;
        }
        tvProgramDescription.setText(desc);
        renderSchedule(ProgramScheduleData.getSchedule(currentTab));
    }

    private void renderSchedule(List<DaySchedule> schedule) {
        layoutSchedule.removeAllViews();
        int currentWeek = 0;
        for (DaySchedule day : schedule) {
            int week = (day.day - 1) / 7 + 1;
            if (week != currentWeek) { currentWeek = week; addWeekHeader(week); }
            addDayCard(day);
        }
    }

    private void addWeekHeader(int week) {
        TextView tv = new TextView(this);
        tv.setText("📅 Tuần " + week);
        tv.setTextSize(16);
        tv.setTextColor(Color.parseColor("#667eea"));
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, dp(week == 1 ? 0 : 16), 0, dp(8));
        tv.setLayoutParams(p);
        layoutSchedule.addView(tv);
    }

    private void addDayCard(DaySchedule day) {
        float density = getResources().getDisplayMetrics().density;

        CardView card = new CardView(this);
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cp.setMargins(0, 0, 0, dp(10));
        card.setLayoutParams(cp);
        card.setRadius(dp(16));
        card.setCardElevation(day.isRest ? dp(1) : dp(3));
        card.setCardBackgroundColor(day.isRest
                ? Color.parseColor("#F8F9FA")
                : Color.WHITE);

        LinearLayout outer = new LinearLayout(this);
        outer.setOrientation(LinearLayout.VERTICAL);
        outer.setPadding(dp(16), dp(14), dp(16), dp(14));

        // ── Header row ──
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Accent color theo emoji/loại bài
        int accentColor = getAccentColor(day.emoji, day.isRest);

        // Day badge (circle)
        CardView badge = new CardView(this);
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(dp(46), dp(46));
        bp.setMargins(0, 0, dp(14), 0);
        badge.setLayoutParams(bp);
        badge.setRadius(dp(23));
        badge.setCardElevation(0);
        badge.setCardBackgroundColor(day.isRest
                ? Color.parseColor("#E9ECEF")
                : accentColor);

        TextView tvDay = new TextView(this);
        tvDay.setText(day.isRest ? "😴" : "N" + day.day);
        tvDay.setTextColor(Color.WHITE);
        tvDay.setTextSize(day.isRest ? 18 : 11);
        tvDay.setTypeface(null, android.graphics.Typeface.BOLD);
        tvDay.setGravity(android.view.Gravity.CENTER);
        tvDay.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        badge.addView(tvDay);

        // Title + focus
        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvTitle = new TextView(this);
        tvTitle.setText(day.emoji + "  " + day.title);
        tvTitle.setTextColor(day.isRest ? Color.parseColor("#AAAAAA") : Color.parseColor("#1A1A1A"));
        tvTitle.setTextSize(15);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvFocus = new TextView(this);
        tvFocus.setText(day.focus);
        tvFocus.setTextColor(Color.parseColor("#999999"));
        tvFocus.setTextSize(12);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fp.setMargins(0, dp(2), 0, 0);
        tvFocus.setLayoutParams(fp);

        info.addView(tvTitle);
        info.addView(tvFocus);

        // Arrow / rest label
        TextView tvArrow = new TextView(this);
        tvArrow.setText(day.isRest ? "" : "›");
        tvArrow.setTextColor(Color.parseColor("#CCCCCC"));
        tvArrow.setTextSize(26);

        header.addView(badge);
        header.addView(info);
        header.addView(tvArrow);
        outer.addView(header);

        // ── Exercise pills ──
        if (!day.isRest && !day.exercises.isEmpty()) {
            // Thin divider
            View divider = new View(this);
            LinearLayout.LayoutParams dp1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dp(1));
            dp1.setMargins(0, dp(10), 0, dp(10));
            divider.setLayoutParams(dp1);
            divider.setBackgroundColor(Color.parseColor("#F0F0F0"));
            outer.addView(divider);

            // Wrap flow of pills
            android.widget.LinearLayout pillRow = new android.widget.LinearLayout(this);
            pillRow.setOrientation(android.widget.LinearLayout.VERTICAL);

            for (String[] ex : day.exercises) {
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(android.view.Gravity.CENTER_VERTICAL);
                LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                rp.setMargins(0, 0, 0, dp(5));
                row.setLayoutParams(rp);

                // Dot
                View dot = new View(this);
                LinearLayout.LayoutParams dotp = new LinearLayout.LayoutParams(dp(6), dp(6));
                dotp.setMargins(0, 0, dp(10), 0);
                dot.setLayoutParams(dotp);
                dot.setBackgroundColor(accentColor);

                TextView tvName = new TextView(this);
                tvName.setText(ex[0]);
                tvName.setTextColor(Color.parseColor("#444444"));
                tvName.setTextSize(13);
                tvName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

                // Sets pill
                CardView pill = new CardView(this);
                LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                pill.setLayoutParams(pp);
                pill.setRadius(dp(10));
                pill.setCardElevation(0);
                pill.setCardBackgroundColor(lightenColor(accentColor));

                TextView tvSets = new TextView(this);
                tvSets.setText(ex[1]);
                tvSets.setTextColor(accentColor);
                tvSets.setTextSize(11);
                tvSets.setTypeface(null, android.graphics.Typeface.BOLD);
                tvSets.setPadding(dp(8), dp(3), dp(8), dp(3));
                pill.addView(tvSets);

                row.addView(dot);
                row.addView(tvName);
                row.addView(pill);
                pillRow.addView(row);
            }
            outer.addView(pillRow);
        }

        card.addView(outer);

        if (!day.isRest) {
            card.setForeground(getDrawable(android.R.drawable.list_selector_background));
            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProgramExerciseSessionActivity.class);
                intent.putExtra("day_number", day.day);
                intent.putExtra("day_title", day.title);
                intent.putExtra("program_tab", currentTab);
                startActivity(intent);
            });
        }

        layoutSchedule.addView(card);
    }

    /** Màu accent theo emoji bài tập */
    private int getAccentColor(String emoji, boolean isRest) {
        if (isRest) return Color.parseColor("#ADB5BD");
        if (emoji == null) return Color.parseColor("#667eea");
        if (emoji.contains("🏋")) return Color.parseColor("#667eea");
        if (emoji.contains("🔥")) return Color.parseColor("#FF6B6B");
        if (emoji.contains("🏃")) return Color.parseColor("#F7971E");
        if (emoji.contains("🦵")) return Color.parseColor("#11998e");
        if (emoji.contains("💪")) return Color.parseColor("#4ECDC4");
        if (emoji.contains("💥")) return Color.parseColor("#A855F7");
        if (emoji.contains("⚡")) return Color.parseColor("#F59E0B");
        if (emoji.contains("🏆")) return Color.parseColor("#FFD700");
        if (emoji.contains("🎉")) return Color.parseColor("#EC4899");
        if (emoji.contains("🔄")) return Color.parseColor("#6B7280");
        return Color.parseColor("#667eea");
    }

    /** Màu nhạt hơn cho pill background */
    private int lightenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] = hsv[1] * 0.25f;
        hsv[2] = Math.min(1f, hsv[2] * 1.5f + 0.3f);
        return Color.HSVToColor(hsv);
    }

    private int dp(int val) {
        return (int) (val * getResources().getDisplayMetrics().density);
    }
}
