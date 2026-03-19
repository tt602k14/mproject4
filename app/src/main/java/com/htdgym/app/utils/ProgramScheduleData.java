package com.htdgym.app.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class chứa toàn bộ lịch trình 30-60-90 ngày.
 * Dùng chung cho ProgramDetailActivity và ProgramExerciseSessionActivity.
 */
public class ProgramScheduleData {

    public static class DaySchedule {
        public int day;
        public String title;
        public String focus;
        public String emoji;
        public boolean isRest;
        public List<String[]> exercises; // [name, sets, rest]

        public DaySchedule(int day, String title, String focus, String emoji, boolean isRest) {
            this.day = day; this.title = title; this.focus = focus;
            this.emoji = emoji; this.isRest = isRest;
            this.exercises = new ArrayList<>();
        }

        public void add(String name, String sets, String rest) {
            exercises.add(new String[]{name, sets, rest});
        }
    }

    public static List<DaySchedule> getSchedule(String tab) {
        switch (tab) {
            case "60": return get60Days();
            case "90": return get90Days();
            default:   return get30Days();
        }
    }

    // ==================== 30 NGÀY ====================
    public static List<DaySchedule> get30Days() {
        List<DaySchedule> s = new ArrayList<>();

        // Tuần 1
        DaySchedule d1 = new DaySchedule(1, "Khởi động toàn thân", "Ngực + Tay", "🏋️", false);
        d1.add("Push-up cơ bản", "3×10", "60s");
        d1.add("Plank", "3×20s", "45s");
        d1.add("Squat không tạ", "3×12", "60s");
        s.add(d1);

        DaySchedule d2 = new DaySchedule(2, "Cardio nhẹ", "Tim mạch", "🏃", false);
        d2.add("Chạy bộ tại chỗ", "3×30s", "30s");
        d2.add("Jumping Jacks", "3×20", "30s");
        d2.add("High Knees", "3×20s", "30s");
        s.add(d2);

        DaySchedule d3 = new DaySchedule(3, "Lưng & Bụng", "Lưng + Core", "💪", false);
        d3.add("Superman Hold", "3×10", "45s");
        d3.add("Crunches", "3×15", "45s");
        d3.add("Bird Dog", "3×8/bên", "45s");
        s.add(d3);

        DaySchedule d4 = new DaySchedule(4, "Chân & Mông", "Chân toàn diện", "🦵", false);
        d4.add("Squat không tạ", "4×15", "60s");
        d4.add("Lunges", "3×10/chân", "60s");
        d4.add("Glute Bridge", "3×15", "45s");
        s.add(d4);

        DaySchedule d5 = new DaySchedule(5, "HIIT nhẹ", "Đốt mỡ", "🔥", false);
        d5.add("Burpees", "3×5", "90s");
        d5.add("Mountain Climbers", "3×15s", "45s");
        d5.add("Jump Squats", "3×8", "75s");
        s.add(d5);

        DaySchedule d6 = new DaySchedule(6, "Vai & Tay", "Vai + Tay trước/sau", "💥", false);
        d6.add("Pike Push-up", "3×8", "60s");
        d6.add("Tricep Dips (ghế)", "3×10", "60s");
        d6.add("Arm Circles", "3×20", "30s");
        s.add(d6);

        s.add(new DaySchedule(7, "Nghỉ ngơi & Phục hồi", "Giãn cơ nhẹ", "😴", true));

        // Tuần 2
        DaySchedule d8 = new DaySchedule(8, "Ngực nâng cao", "Ngực + Vai", "🏋️", false);
        d8.add("Push-up rộng tay", "4×10", "60s");
        d8.add("Push-up hẹp tay", "3×10", "60s");
        d8.add("Plank to Push-up", "3×8", "60s");
        s.add(d8);

        DaySchedule d9 = new DaySchedule(9, "Cardio trung bình", "Tim mạch", "🏃", false);
        d9.add("Jump Rope (mô phỏng)", "4×30s", "30s");
        d9.add("Butt Kicks", "3×20s", "30s");
        d9.add("Lateral Shuffles", "3×20s", "30s");
        s.add(d9);

        DaySchedule d10 = new DaySchedule(10, "Lưng & Core", "Lưng + Bụng", "💪", false);
        d10.add("Reverse Snow Angels", "3×12", "45s");
        d10.add("Bicycle Crunches", "3×15", "45s");
        d10.add("Dead Bug", "3×8/bên", "45s");
        s.add(d10);

        DaySchedule d11 = new DaySchedule(11, "Chân mạnh hơn", "Chân + Mông", "🦵", false);
        d11.add("Sumo Squat", "4×12", "60s");
        d11.add("Step-up (ghế)", "3×10/chân", "60s");
        d11.add("Calf Raises", "3×20", "30s");
        s.add(d11);

        DaySchedule d12 = new DaySchedule(12, "HIIT trung bình", "Đốt mỡ toàn thân", "🔥", false);
        d12.add("Burpees", "4×6", "90s");
        d12.add("Mountain Climbers", "4×20s", "45s");
        d12.add("Squat Jumps", "3×10", "75s");
        s.add(d12);

        DaySchedule d13 = new DaySchedule(13, "Vai & Tay nâng cao", "Vai + Tay", "💥", false);
        d13.add("Pike Push-up", "4×10", "60s");
        d13.add("Diamond Push-up", "3×8", "60s");
        d13.add("Plank Shoulder Tap", "3×10/bên", "45s");
        s.add(d13);

        s.add(new DaySchedule(14, "Nghỉ ngơi & Phục hồi", "Yoga nhẹ", "😴", true));

        // Tuần 3
        DaySchedule d15 = new DaySchedule(15, "Ngực & Vai mạnh", "Ngực + Vai", "🏋️", false);
        d15.add("Decline Push-up", "4×10", "60s");
        d15.add("Incline Push-up", "3×12", "60s");
        d15.add("Lateral Raise (chai nước)", "3×12", "45s");
        s.add(d15);

        DaySchedule d16 = new DaySchedule(16, "Cardio mạnh", "Tim mạch + Đốt mỡ", "🏃", false);
        d16.add("Tabata Jumping Jacks", "4×20s", "10s");
        d16.add("Tabata High Knees", "4×20s", "10s");
        d16.add("Tabata Burpees", "4×20s", "10s");
        s.add(d16);

        DaySchedule d17 = new DaySchedule(17, "Lưng & Core mạnh", "Lưng + Bụng", "💪", false);
        d17.add("Superman Pulse", "4×12", "45s");
        d17.add("V-Up", "3×10", "60s");
        d17.add("Side Plank", "3×20s/bên", "45s");
        s.add(d17);

        DaySchedule d18 = new DaySchedule(18, "Chân & Mông mạnh", "Chân toàn diện", "🦵", false);
        d18.add("Jump Squat", "4×12", "75s");
        d18.add("Reverse Lunges", "4×10/chân", "60s");
        d18.add("Single Leg Glute Bridge", "3×10/chân", "45s");
        s.add(d18);

        DaySchedule d19 = new DaySchedule(19, "HIIT nâng cao", "Đốt mỡ tối đa", "🔥", false);
        d19.add("Burpees", "5×8", "90s");
        d19.add("Tuck Jumps", "4×8", "75s");
        d19.add("Speed Skaters", "4×10/bên", "45s");
        s.add(d19);

        DaySchedule d20 = new DaySchedule(20, "Full Body Circuit", "Toàn thân", "⚡", false);
        d20.add("Push-up", "3×12", "30s");
        d20.add("Squat", "3×15", "30s");
        d20.add("Plank", "3×30s", "30s");
        d20.add("Lunges", "3×10/chân", "30s");
        s.add(d20);

        s.add(new DaySchedule(21, "Nghỉ ngơi & Phục hồi", "Giãn cơ sâu", "😴", true));

        // Tuần 4
        DaySchedule d22 = new DaySchedule(22, "Ngực & Tay đỉnh", "Ngực + Tay sau", "🏋️", false);
        d22.add("Push-up 4 biến thể", "4×10", "60s");
        d22.add("Tricep Dips", "4×12", "60s");
        d22.add("Chest Squeeze Push-up", "3×8", "60s");
        s.add(d22);

        DaySchedule d23 = new DaySchedule(23, "Cardio đỉnh cao", "Tim mạch", "🏃", false);
        d23.add("Sprint tại chỗ", "5×20s", "20s");
        d23.add("Box Jumps (mô phỏng)", "4×8", "60s");
        d23.add("Jump Rope nhanh", "5×30s", "20s");
        s.add(d23);

        DaySchedule d24 = new DaySchedule(24, "Lưng & Core đỉnh", "Lưng + Bụng", "💪", false);
        d24.add("Hollow Body Hold", "4×20s", "45s");
        d24.add("Superman + Squeeze", "4×12", "45s");
        d24.add("Russian Twist", "4×15", "45s");
        s.add(d24);

        DaySchedule d25 = new DaySchedule(25, "Chân & Mông đỉnh", "Chân toàn diện", "🦵", false);
        d25.add("Pistol Squat (hỗ trợ)", "4×6/chân", "90s");
        d25.add("Jump Lunge", "4×8/chân", "75s");
        d25.add("Wall Sit", "4×30s", "60s");
        s.add(d25);

        DaySchedule d26 = new DaySchedule(26, "HIIT tối đa", "Đốt mỡ cực đại", "🔥", false);
        d26.add("Burpee + Push-up", "5×6", "90s");
        d26.add("Tuck Jump", "5×8", "75s");
        d26.add("Mountain Climber nhanh", "5×20s", "30s");
        s.add(d26);

        DaySchedule d27 = new DaySchedule(27, "Full Body Strength", "Toàn thân sức mạnh", "⚡", false);
        d27.add("Push-up", "4×15", "45s");
        d27.add("Squat Jump", "4×12", "60s");
        d27.add("Plank", "4×40s", "30s");
        d27.add("Burpees", "3×8", "90s");
        s.add(d27);

        s.add(new DaySchedule(28, "Nghỉ ngơi & Phục hồi", "Giãn cơ toàn thân", "😴", true));

        DaySchedule d29 = new DaySchedule(29, "Kiểm tra thể lực", "Test toàn thân", "🏆", false);
        d29.add("Max Push-up", "1×max", "-");
        d29.add("Max Squat", "1×max", "-");
        d29.add("Plank tối đa", "1×max", "-");
        s.add(d29);

        DaySchedule d30 = new DaySchedule(30, "Hoàn thành 30 ngày!", "Celebration Workout", "🎉", false);
        d30.add("Full Body Circuit x3", "3 vòng", "30s");
        d30.add("Stretch toàn thân", "10 phút", "-");
        s.add(d30);

        return s;
    }

    // ==================== 60 NGÀY (30 + thêm) ====================
    public static List<DaySchedule> get60Days() {
        List<DaySchedule> s = new ArrayList<>(get30Days());

        // Tuần 5
        DaySchedule d31 = new DaySchedule(31, "Bench Press nặng", "Ngực + Tay sau", "🏋️", false);
        d31.add("Bench Press", "5×5", "120s"); d31.add("Incline Dumbbell Press", "4×8", "90s"); d31.add("Tricep Pushdown", "4×12", "60s"); s.add(d31);

        DaySchedule d32 = new DaySchedule(32, "Deadlift ngày", "Lưng + Chân sau", "💀", false);
        d32.add("Deadlift", "5×5", "180s"); d32.add("Romanian Deadlift", "4×8", "120s"); d32.add("Barbell Row", "4×8", "90s"); s.add(d32);

        DaySchedule d33 = new DaySchedule(33, "Cardio + Core", "Tim mạch + Bụng", "🏃", false);
        d33.add("HIIT 20 phút", "4 vòng", "30s"); d33.add("Hanging Knee Raise", "4×12", "45s"); d33.add("Ab Wheel Rollout", "3×8", "60s"); s.add(d33);

        DaySchedule d34 = new DaySchedule(34, "Squat nặng", "Chân toàn diện", "🦵", false);
        d34.add("Back Squat", "5×5", "180s"); d34.add("Leg Press", "4×10", "90s"); d34.add("Leg Curl", "4×12", "60s"); s.add(d34);

        DaySchedule d35 = new DaySchedule(35, "Vai & Tay", "Vai + Tay trước", "💥", false);
        d35.add("Overhead Press", "5×5", "120s"); d35.add("Lateral Raise", "4×12", "60s"); d35.add("Barbell Curl", "4×10", "60s"); s.add(d35);

        DaySchedule d36 = new DaySchedule(36, "Pull-up ngày", "Lưng + Tay trước", "🔝", false);
        d36.add("Pull-up", "5×max", "120s"); d36.add("Chin-up", "4×6", "90s"); d36.add("Face Pull", "4×15", "60s"); s.add(d36);

        s.add(new DaySchedule(37, "Nghỉ ngơi tích cực", "Đi bộ + Giãn cơ", "😴", true));

        // Tuần 6
        DaySchedule d38 = new DaySchedule(38, "Ngực & Vai nặng", "Ngực + Vai", "🏋️", false);
        d38.add("Bench Press", "5×4", "180s"); d38.add("Dips có tạ", "4×8", "90s"); d38.add("Arnold Press", "4×10", "75s"); s.add(d38);

        DaySchedule d39 = new DaySchedule(39, "Deadlift nặng hơn", "Lưng + Chân sau", "💀", false);
        d39.add("Deadlift", "5×4", "180s"); d39.add("Good Morning", "4×10", "90s"); d39.add("Seated Row", "4×10", "75s"); s.add(d39);

        DaySchedule d40 = new DaySchedule(40, "HIIT nâng cao", "Đốt mỡ + Sức bền", "🔥", false);
        d40.add("Tabata Burpees", "8×20s", "10s"); d40.add("Tabata Squat Jump", "8×20s", "10s"); d40.add("Cool down", "5 phút", "-"); s.add(d40);

        DaySchedule d41 = new DaySchedule(41, "Squat nặng hơn", "Chân toàn diện", "🦵", false);
        d41.add("Back Squat", "5×4", "180s"); d41.add("Front Squat", "4×6", "120s"); d41.add("Walking Lunges", "4×12/chân", "75s"); s.add(d41);

        DaySchedule d42 = new DaySchedule(42, "Vai & Tay nặng", "Vai + Tay", "💥", false);
        d42.add("Overhead Press", "5×4", "120s"); d42.add("Upright Row", "4×10", "75s"); d42.add("Hammer Curl", "4×10", "60s"); s.add(d42);

        DaySchedule d43 = new DaySchedule(43, "Pull-up nặng", "Lưng + Tay trước", "🔝", false);
        d43.add("Weighted Pull-up", "5×5", "120s"); d43.add("T-Bar Row", "4×8", "90s"); d43.add("Preacher Curl", "4×10", "60s"); s.add(d43);

        s.add(new DaySchedule(44, "Nghỉ ngơi tích cực", "Foam rolling + Giãn cơ", "😴", true));

        // Tuần 7
        DaySchedule d45 = new DaySchedule(45, "Ngực tối đa", "Ngực + Tay sau", "🏋️", false);
        d45.add("Bench Press 1RM test", "3×3", "180s"); d45.add("Cable Fly", "4×12", "60s"); d45.add("Skull Crusher", "4×10", "75s"); s.add(d45);

        DaySchedule d46 = new DaySchedule(46, "Deadlift tối đa", "Lưng + Chân sau", "💀", false);
        d46.add("Deadlift 1RM test", "3×3", "180s"); d46.add("Rack Pull", "4×6", "120s"); d46.add("Hyperextension", "4×12", "60s"); s.add(d46);

        DaySchedule d47 = new DaySchedule(47, "Cardio & Core", "Tim mạch + Bụng", "🏃", false);
        d47.add("Steady State Cardio", "30 phút", "-"); d47.add("Dragon Flag", "4×5", "90s"); d47.add("L-Sit Hold", "4×10s", "60s"); s.add(d47);

        DaySchedule d48 = new DaySchedule(48, "Squat tối đa", "Chân toàn diện", "🦵", false);
        d48.add("Back Squat 1RM test", "3×3", "180s"); d48.add("Hack Squat", "4×8", "90s"); d48.add("Leg Extension", "4×15", "60s"); s.add(d48);

        DaySchedule d49 = new DaySchedule(49, "Vai tối đa", "Vai + Tay", "💥", false);
        d49.add("OHP 1RM test", "3×3", "180s"); d49.add("Cable Lateral Raise", "4×15", "45s"); d49.add("Concentration Curl", "4×10", "60s"); s.add(d49);

        DaySchedule d50 = new DaySchedule(50, "Pull-up tối đa", "Lưng + Tay trước", "🔝", false);
        d50.add("Max Pull-up test", "3×max", "120s"); d50.add("Lat Pulldown", "4×6", "90s"); d50.add("Incline Curl", "4×10", "60s"); s.add(d50);

        s.add(new DaySchedule(51, "Nghỉ ngơi sâu", "Massage + Giãn cơ", "😴", true));

        // Tuần 8 - Deload
        DaySchedule d52 = new DaySchedule(52, "Deload Ngực", "Ngực nhẹ", "🔄", false);
        d52.add("Bench Press 60%", "3×8", "90s"); d52.add("Push-up", "3×15", "60s"); s.add(d52);

        DaySchedule d53 = new DaySchedule(53, "Deload Lưng", "Lưng nhẹ", "🔄", false);
        d53.add("Deadlift 60%", "3×6", "90s"); d53.add("Pull-up", "3×8", "75s"); s.add(d53);

        DaySchedule d54 = new DaySchedule(54, "Cardio vui vẻ", "Tim mạch", "🏃", false);
        d54.add("Đi bộ nhanh", "30 phút", "-"); d54.add("Stretch toàn thân", "15 phút", "-"); s.add(d54);

        DaySchedule d55 = new DaySchedule(55, "Deload Chân", "Chân nhẹ", "🔄", false);
        d55.add("Squat 60%", "3×8", "90s"); d55.add("Lunges", "3×10/chân", "60s"); s.add(d55);

        DaySchedule d56 = new DaySchedule(56, "Deload Vai & Tay", "Vai + Tay nhẹ", "🔄", false);
        d56.add("OHP 60%", "3×8", "90s"); d56.add("Barbell Curl", "3×12", "60s"); s.add(d56);

        s.add(new DaySchedule(57, "Nghỉ ngơi", "Phục hồi hoàn toàn", "😴", true));
        s.add(new DaySchedule(58, "Nghỉ ngơi", "Phục hồi hoàn toàn", "😴", true));

        DaySchedule d59 = new DaySchedule(59, "Kiểm tra thể lực 60 ngày", "Test toàn thân", "🏆", false);
        d59.add("Max Push-up", "1×max", "-"); d59.add("Max Pull-up", "1×max", "-"); d59.add("Max Squat", "1×max", "-"); s.add(d59);

        DaySchedule d60 = new DaySchedule(60, "Hoàn thành 60 ngày!", "Celebration!", "🎉", false);
        d60.add("Full Body Circuit", "3 vòng", "30s"); d60.add("Stretch toàn thân", "15 phút", "-"); s.add(d60);

        return s;
    }

    // ==================== 90 NGÀY (60 + thêm) ====================
    public static List<DaySchedule> get90Days() {
        List<DaySchedule> s = new ArrayList<>(get60Days());

        // Tuần 9-10: Hypertrophy
        int[][] hypDays = {{61,62,63,64,65,66,67},{68,69,70,71,72,73,74}};
        String[][] hypTitles = {
            {"Ngực Hypertrophy","Lưng Hypertrophy","Cardio LISS","Chân Hypertrophy","Vai Hypertrophy","Full Body Pump","Nghỉ ngơi"},
            {"Ngực nặng","Lưng nặng","HIIT 30 phút","Chân nặng","Vai nặng","Full Body Power","Nghỉ ngơi"}
        };
        String[][] hypFocus = {
            {"Ngực + Tay sau","Lưng + Tay trước","Tim mạch nhẹ","Chân toàn diện","Vai + Tay","Toàn thân","Phục hồi"},
            {"Ngực + Tay sau","Lưng + Tay trước","Đốt mỡ","Chân toàn diện","Vai + Tay","Toàn thân","Phục hồi"}
        };
        String[][] hypEmoji = {
            {"🏋️","💪","🏃","🦵","💥","⚡","😴"},
            {"🏋️","💪","🔥","🦵","💥","⚡","😴"}
        };
        String[][][][] hypEx = {
            {
                {{"Bench Press","5×8","90s"},{"Incline Press","4×10","75s"},{"Cable Fly","4×12","60s"}},
                {{"Deadlift","5×6","120s"},{"Pull-up","5×8","90s"},{"Seated Row","4×12","60s"}},
                {{"Đi bộ nhanh","40 phút","-"},{"Core Circuit","3 vòng","30s"}},
                {{"Back Squat","5×8","120s"},{"Leg Press","4×12","75s"},{"Romanian DL","4×10","90s"}},
                {{"Overhead Press","5×8","90s"},{"Lateral Raise","4×15","45s"},{"Hammer Curl","4×12","60s"}},
                {{"Push-up","4×15","30s"},{"Pull-up","4×10","30s"},{"Squat","4×15","30s"}},
                {}
            },
            {
                {{"Bench Press","5×6","120s"},{"Dips có tạ","4×10","90s"},{"Skull Crusher","4×10","75s"}},
                {{"Deadlift","5×5","150s"},{"Barbell Row","5×8","90s"},{"Lat Pulldown","4×10","75s"}},
                {{"Tabata x8 rounds","20s on/10s off","-"},{"Burpees","5×10","90s"},{"Mountain Climbers","5×30s","30s"}},
                {{"Back Squat","5×5","150s"},{"Front Squat","4×6","120s"},{"Jump Squat","3×10","75s"}},
                {{"Overhead Press","5×5","120s"},{"Arnold Press","4×10","75s"},{"Incline Curl","4×10","60s"}},
                {{"Power Clean","5×3","150s"},{"Push Press","4×5","120s"},{"Box Jump","4×8","90s"}},
                {}
            }
        };
        for (int w = 0; w < 2; w++) {
            for (int d = 0; d < 7; d++) {
                boolean isRest = hypTitles[w][d].equals("Nghỉ ngơi");
                DaySchedule ds = new DaySchedule(hypDays[w][d], hypTitles[w][d], hypFocus[w][d], hypEmoji[w][d], isRest);
                if (!isRest) for (String[] ex : hypEx[w][d]) ds.add(ex[0], ex[1], ex[2]);
                s.add(ds);
            }
        }

        // Tuần 11-12: Strength Peak
        for (int w = 0; w < 2; w++) {
            int base = 75 + w * 7;
            DaySchedule da = new DaySchedule(base, "Ngực Strength", "Ngực + Tay sau", "🏋️", false);
            da.add("Bench Press", "5×3", "180s"); da.add("Weighted Dips", "4×6", "120s"); da.add("Tricep Dips", "4×8", "90s"); s.add(da);

            DaySchedule db = new DaySchedule(base+1, "Lưng Strength", "Lưng + Tay trước", "💪", false);
            db.add("Deadlift", "5×3", "180s"); db.add("Weighted Pull-up", "4×5", "120s"); db.add("Barbell Row", "4×6", "120s"); s.add(db);

            DaySchedule dc = new DaySchedule(base+2, "Cardio & Mobility", "Tim mạch + Linh hoạt", "🏃", false);
            dc.add("Steady State Cardio", "30 phút", "-"); dc.add("Mobility Flow", "15 phút", "-"); s.add(dc);

            DaySchedule dd = new DaySchedule(base+3, "Chân Strength", "Chân toàn diện", "🦵", false);
            dd.add("Back Squat", "5×3", "180s"); dd.add("Romanian DL", "4×5", "150s"); dd.add("Bulgarian Split Squat", "4×6/chân", "90s"); s.add(dd);

            DaySchedule de = new DaySchedule(base+4, "Vai Strength", "Vai + Tay", "💥", false);
            de.add("Overhead Press", "5×3", "180s"); de.add("Push Press", "4×5", "120s"); de.add("Barbell Curl", "4×6", "90s"); s.add(de);

            DaySchedule df = new DaySchedule(base+5, "Olympic Lifts", "Sức mạnh bùng nổ", "⚡", false);
            df.add("Power Clean", "5×3", "180s"); df.add("Hang Snatch", "4×3", "150s"); df.add("Box Jump", "4×5", "90s"); s.add(df);

            s.add(new DaySchedule(base+6, "Nghỉ ngơi sâu", "Phục hồi + Massage", "😴", true));
        }

        // Tuần 13: Deload + Finish
        DaySchedule d87 = new DaySchedule(87, "Deload Ngực & Lưng", "Nhẹ 50%", "🔄", false);
        d87.add("Bench Press 50%", "3×8", "90s"); d87.add("Deadlift 50%", "3×6", "90s"); s.add(d87);

        DaySchedule d88 = new DaySchedule(88, "Deload Chân & Vai", "Nhẹ 50%", "🔄", false);
        d88.add("Squat 50%", "3×8", "90s"); d88.add("OHP 50%", "3×6", "90s"); s.add(d88);

        s.add(new DaySchedule(89, "Nghỉ ngơi hoàn toàn", "Phục hồi", "😴", true));

        DaySchedule d90 = new DaySchedule(90, "HOÀN THÀNH 90 NGÀY! 🏆", "Transformation hoàn tất", "🎉", false);
        d90.add("1RM Bench Press test", "1×max", "-");
        d90.add("1RM Deadlift test", "1×max", "-");
        d90.add("1RM Squat test", "1×max", "-");
        d90.add("Max Pull-up test", "1×max", "-");
        s.add(d90);

        return s;
    }
}
