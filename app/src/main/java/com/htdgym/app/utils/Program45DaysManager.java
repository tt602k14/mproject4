package com.htdgym.app.utils;

import com.htdgym.app.models.Exercise;
import java.util.ArrayList;
import java.util.List;

public class Program45DaysManager {

    /**
     * Chương trình Tập Tay Trong 45 Ngày
     * Tuần 1-2: Cơ bản (3 ngày/tuần)
     * Tuần 3-4: Trung bình (4 ngày/tuần) 
     * Tuần 5-6: Nâng cao (5 ngày/tuần)
     * Tuần 7: Nghỉ ngơi và phục hồi
     */
    public static List<Exercise> getShoulder45DaysProgram() {
        List<Exercise> exercises = new ArrayList<>();
        
        // TUẦN 1-2: CƠ BẢN (Ngày 1-14)
        // Ngày 1, 3, 5, 8, 10, 12
        exercises.add(new Exercise("Ngày 1: Khởi động vai", "shoulder", "3×10", "45s", "#4ECDC4", "Dễ", "https://youtu.be/1We3zKXj_uA"));
        exercises.add(new Exercise("Ngày 2: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", "https://youtu.be/ZToicYcHIOU"));
        exercises.add(new Exercise("Ngày 3: Vòng tay cơ bản", "shoulder", "3×15", "30s", "#4ECDC4", "Dễ", "https://youtu.be/5Qv2T8VusME"));
        exercises.add(new Exercise("Ngày 4: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", "https://youtu.be/v7AYKMP6rOE"));
        exercises.add(new Exercise("Ngày 5: Nâng tay ngang", "shoulder", "3×12", "45s", "#4ECDC4", "Dễ", "https://youtu.be/3VcKaXpzqRo"));
        exercises.add(new Exercise("Ngày 6: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", "https://youtu.be/1ZYbU82GVz4"));
        exercises.add(new Exercise("Ngày 7: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", "https://youtu.be/COp7BR_Dvps"));
        
        exercises.add(new Exercise("Ngày 8: Hít đất vai", "shoulder", "3×8", "60s", "#4ECDC4", "Trung bình", "https://youtu.be/d1HZBdD0idE"));
        exercises.add(new Exercise("Ngày 9: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", "https://youtu.be/inpok4MKVLM"));
        exercises.add(new Exercise("Ngày 10: Nâng tay trước", "shoulder", "3×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/qM5X-_FfCQE"));
        exercises.add(new Exercise("Ngày 11: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", "https://youtu.be/ZToicYcHIOU"));
        exercises.add(new Exercise("Ngày 12: Đẩy vai", "shoulder", "3×10", "60s", "#4ECDC4", "Trung bình", "https://youtu.be/qEwKCR5JCog"));
        exercises.add(new Exercise("Ngày 13: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", "https://youtu.be/v7AYKMP6rOE"));
        exercises.add(new Exercise("Ngày 14: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", "https://youtu.be/1ZYbU82GVz4"));
        
        // TUẦN 3-4: TRUNG BÌNH (Ngày 15-28)
        // 4 ngày/tuần
        exercises.add(new Exercise("Ngày 15: Combo vai cơ bản", "shoulder", "4×12", "45s", "#4ECDC4", "Trung bình", "https://youtu.be/1We3zKXj_uA"));
        exercises.add(new Exercise("Ngày 16: Hít đất vai nâng cao", "shoulder", "3×10", "60s", "#4ECDC4", "Trung bình", "https://youtu.be/d1HZBdD0idE"));
        exercises.add(new Exercise("Ngày 17: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 18: Vòng tay + Nâng tay", "shoulder", "4×15", "30s", "#4ECDC4", "Trung bình", "https://youtu.be/5Qv2T8VusME"));
        exercises.add(new Exercise("Ngày 19: Đẩy vai mạnh", "shoulder", "4×12", "60s", "#4ECDC4", "Trung bình", "https://youtu.be/qEwKCR5JCog"));
        exercises.add(new Exercise("Ngày 20: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 21: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        
        exercises.add(new Exercise("Ngày 22: Siêu combo vai", "shoulder", "4×15", "45s", "#4ECDC4", "Khó", "https://youtu.be/3VcKaXpzqRo"));
        exercises.add(new Exercise("Ngày 23: Hít đất vai + Nâng tay", "shoulder", "4×12", "60s", "#4ECDC4", "Khó", "https://youtu.be/d1HZBdD0idE"));
        exercises.add(new Exercise("Ngày 24: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 25: Đẩy vai cực mạnh", "shoulder", "5×12", "60s", "#4ECDC4", "Khó", "https://youtu.be/qEwKCR5JCog"));
        exercises.add(new Exercise("Ngày 26: Combo toàn diện", "shoulder", "4×20", "45s", "#4ECDC4", "Khó", "https://youtu.be/qM5X-_FfCQE"));
        exercises.add(new Exercise("Ngày 27: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 28: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        
        // TUẦN 5-6: NÂNG CAO (Ngày 29-42)
        // 5 ngày/tuần
        exercises.add(new Exercise("Ngày 29: Thử thách vai 1", "shoulder", "5×15", "45s", "#4ECDC4", "Khó", "https://youtu.be/1We3zKXj_uA"));
        exercises.add(new Exercise("Ngày 30: Thử thách vai 2", "shoulder", "5×12", "60s", "#4ECDC4", "Khó", "https://youtu.be/d1HZBdD0idE"));
        exercises.add(new Exercise("Ngày 31: Thử thách vai 3", "shoulder", "5×20", "30s", "#4ECDC4", "Khó", "https://youtu.be/5Qv2T8VusME"));
        exercises.add(new Exercise("Ngày 32: Thử thách vai 4", "shoulder", "5×15", "45s", "#4ECDC4", "Khó", "https://youtu.be/3VcKaXpzqRo"));
        exercises.add(new Exercise("Ngày 33: Thử thách vai 5", "shoulder", "5×18", "60s", "#4ECDC4", "Khó", "https://youtu.be/qEwKCR5JCog"));
        exercises.add(new Exercise("Ngày 34: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 35: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        
        exercises.add(new Exercise("Ngày 36: Siêu thử thách 1", "shoulder", "6×15", "45s", "#4ECDC4", "Cực khó", "https://youtu.be/qM5X-_FfCQE"));
        exercises.add(new Exercise("Ngày 37: Siêu thử thách 2", "shoulder", "6×12", "60s", "#4ECDC4", "Cực khó", "https://youtu.be/d1HZBdD0idE"));
        exercises.add(new Exercise("Ngày 38: Siêu thử thách 3", "shoulder", "6×20", "30s", "#4ECDC4", "Cực khó", "https://youtu.be/1We3zKXj_uA"));
        exercises.add(new Exercise("Ngày 39: Siêu thử thách 4", "shoulder", "6×15", "45s", "#4ECDC4", "Cực khó", "https://youtu.be/5Qv2T8VusME"));
        exercises.add(new Exercise("Ngày 40: Siêu thử thách 5", "shoulder", "6×18", "60s", "#4ECDC4", "Cực khó", "https://youtu.be/3VcKaXpzqRo"));
        exercises.add(new Exercise("Ngày 41: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 42: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        
        // TUẦN 7: PHỤC HỒI (Ngày 43-45)
        exercises.add(new Exercise("Ngày 43: Thư giãn vai", "shoulder", "2×10", "60s", "#4ECDC4", "Dễ", "https://youtu.be/1We3zKXj_uA"));
        exercises.add(new Exercise("Ngày 44: Giãn cơ vai", "shoulder", "2×15", "45s", "#4ECDC4", "Dễ", "https://youtu.be/5Qv2T8VusME"));
        exercises.add(new Exercise("Ngày 45: Hoàn thành! 🎉", "shoulder", "Celebration", "0s", "#FFD700", "Hoàn thành", "https://youtu.be/qEwKCR5JCog"));
        
        return exercises;
    }

    /**
     * Chương trình Bộ Ngực Vạm Vỡ Trong 45 Ngày
     */
    public static List<Exercise> getChest45DaysProgram() {
        List<Exercise> exercises = new ArrayList<>();
        
        // TUẦN 1-2: CƠ BẢN (Ngày 1-14)
        exercises.add(new Exercise("Ngày 1: Hít đất cơ bản", "chest", "3×8", "60s", "#FF6B6B", "Dễ", "https://youtu.be/-R5sH2iG9Gw"));
        exercises.add(new Exercise("Ngày 2: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 3: Hít đất nghiêng", "chest", "3×6", "60s", "#FF6B6B", "Dễ", "https://youtu.be/4dF1DOWzf20"));
        exercises.add(new Exercise("Ngày 4: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 5: Hít đất rộng", "chest", "3×10", "45s", "#FF6B6B", "Trung bình", "https://youtu.be/5Qv2T8VusME"));
        exercises.add(new Exercise("Ngày 6: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 7: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        
        exercises.add(new Exercise("Ngày 8: Hít đất + Nghiêng", "chest", "3×12", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/-R5sH2iG9Gw"));
        exercises.add(new Exercise("Ngày 9: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 10: Hít đất xuống dốc", "chest", "3×8", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/SKPab2YC8BE"));
        exercises.add(new Exercise("Ngày 11: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 12: Chống đẩy ngực", "chest", "3×10", "60s", "#FF6B6B", "Trung bình", "https://youtu.be/2z8JmcrW-As"));
        exercises.add(new Exercise("Ngày 13: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 14: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        
        // TUẦN 3-4: TRUNG BÌNH (Ngày 15-28)
        exercises.add(new Exercise("Ngày 15: Combo ngực 1", "chest", "4×12", "45s", "#FF6B6B", "Trung bình", "https://youtu.be/-R5sH2iG9Gw"));
        exercises.add(new Exercise("Ngày 16: Hít đất kim cương", "chest", "3×6", "90s", "#FF6B6B", "Khó", "https://youtu.be/J0DnG1_S92I"));
        exercises.add(new Exercise("Ngày 17: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 18: Combo ngực 2", "chest", "4×15", "45s", "#FF6B6B", "Trung bình", "https://youtu.be/4dF1DOWzf20"));
        exercises.add(new Exercise("Ngày 19: Siêu combo ngực", "chest", "4×10", "60s", "#FF6B6B", "Khó", "https://youtu.be/5Qv2T8VusME"));
        exercises.add(new Exercise("Ngày 20: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 21: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        
        // Tiếp tục pattern tương tự cho đến ngày 45...
        exercises.add(new Exercise("Ngày 22-28: Tăng cường", "chest", "4×15", "45s", "#FF6B6B", "Khó", "https://youtu.be/SKPab2YC8BE"));
        
        // TUẦN 5-6: NÂNG CAO (Ngày 29-42)
        exercises.add(new Exercise("Ngày 29-35: Thử thách ngực", "chest", "5×15", "45s", "#FF6B6B", "Cực khó", "https://youtu.be/J0DnG1_S92I"));
        exercises.add(new Exercise("Ngày 36-42: Siêu thử thách", "chest", "6×12", "60s", "#FF6B6B", "Cực khó", "https://youtu.be/2z8JmcrW-As"));
        
        // TUẦN 7: PHỤC HỒI (Ngày 43-45)
        exercises.add(new Exercise("Ngày 43: Thư giãn ngực", "chest", "2×10", "60s", "#FF6B6B", "Dễ", "https://youtu.be/-R5sH2iG9Gw"));
        exercises.add(new Exercise("Ngày 44: Giãn cơ ngực", "chest", "2×8", "60s", "#FF6B6B", "Dễ", "https://youtu.be/4dF1DOWzf20"));
        exercises.add(new Exercise("Ngày 45: Hoàn thành! 🎉", "chest", "Celebration", "0s", "#FFD700", "Hoàn thành", "https://youtu.be/2z8JmcrW-As"));
        
        return exercises;
    }

    /**
     * Chương trình Đôi Chân Mạnh Mẽ Trong 45 Ngày
     */
    public static List<Exercise> getLegs45DaysProgram() {
        List<Exercise> exercises = new ArrayList<>();
        
        // TUẦN 1-2: CƠ BẢN (Ngày 1-14)
        exercises.add(new Exercise("Ngày 1: Gánh đùi cơ bản", "legs", "3×15", "45s", "#FFA726", "Dễ", "https://youtu.be/XtoO9YwNEqA"));
        exercises.add(new Exercise("Ngày 2: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 3: Chùng chân", "legs", "3×12", "60s", "#FFA726", "Trung bình", "https://youtu.be/QOVaHwm-Q6U"));
        exercises.add(new Exercise("Ngày 4: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 5: Ngồi tường", "legs", "3×30s", "60s", "#FFA726", "Trung bình", "https://youtu.be/y-wV4Venusw"));
        exercises.add(new Exercise("Ngày 6: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 7: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        
        exercises.add(new Exercise("Ngày 8: Nâng bắp chân", "legs", "4×20", "30s", "#FFA726", "Dễ", "https://youtu.be/gwLzBJYoWlI"));
        exercises.add(new Exercise("Ngày 9: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 10: Chùng chân ngang", "legs", "3×12", "45s", "#FFA726", "Trung bình", "https://youtu.be/8pl4hWllMPQ"));
        exercises.add(new Exercise("Ngày 11: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 12: Nâng mông", "legs", "4×15", "45s", "#FFA726", "Dễ", "https://youtu.be/wPM8icPu6H8"));
        exercises.add(new Exercise("Ngày 13: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 14: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        
        // TUẦN 3-4: TRUNG BÌNH (Ngày 15-28)
        exercises.add(new Exercise("Ngày 15: Combo chân 1", "legs", "4×15", "45s", "#FFA726", "Trung bình", "https://youtu.be/XtoO9YwNEqA"));
        exercises.add(new Exercise("Ngày 16: Gánh đùi nhảy", "legs", "3×12", "60s", "#FFA726", "Khó", "https://youtu.be/A-cFYWvaHr0"));
        exercises.add(new Exercise("Ngày 17: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 18: Combo chân 2", "legs", "4×18", "45s", "#FFA726", "Trung bình", "https://youtu.be/QOVaHwm-Q6U"));
        exercises.add(new Exercise("Ngày 19: Gánh đùi Bulgaria", "legs", "3×10", "60s", "#FFA726", "Khó", "https://youtu.be/2C-uStzPJWY"));
        exercises.add(new Exercise("Ngày 20: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        exercises.add(new Exercise("Ngày 21: Nghỉ ngơi", "rest", "Nghỉ", "0s", "#E0E0E0", "Nghỉ", ""));
        
        // Tiếp tục pattern...
        exercises.add(new Exercise("Ngày 22-28: Tăng cường chân", "legs", "4×20", "45s", "#FFA726", "Khó", "https://youtu.be/y-wV4Venusw"));
        
        // TUẦN 5-6: NÂNG CAO (Ngày 29-42)
        exercises.add(new Exercise("Ngày 29-35: Thử thách chân", "legs", "5×20", "45s", "#FFA726", "Cực khó", "https://youtu.be/A-cFYWvaHr0"));
        exercises.add(new Exercise("Ngày 36-42: Siêu thử thách", "legs", "6×15", "60s", "#FFA726", "Cực khó", "https://youtu.be/2C-uStzPJWY"));
        
        // TUẦN 7: PHỤC HỒI (Ngày 43-45)
        exercises.add(new Exercise("Ngày 43: Thư giãn chân", "legs", "2×15", "60s", "#FFA726", "Dễ", "https://youtu.be/XtoO9YwNEqA"));
        exercises.add(new Exercise("Ngày 44: Giãn cơ chân", "legs", "2×12", "60s", "#FFA726", "Dễ", "https://youtu.be/gwLzBJYoWlI"));
        exercises.add(new Exercise("Ngày 45: Hoàn thành! 🎉", "legs", "Celebration", "0s", "#FFD700", "Hoàn thành", "https://youtu.be/wPM8icPu6H8"));
        
        return exercises;
    }
}