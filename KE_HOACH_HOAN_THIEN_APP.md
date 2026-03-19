# Kế hoạch hoàn thiện HTD GYM App

## ✅ Đã hoàn thành
1. SQLite database hoạt động tốt
2. DatabaseInitializer với dữ liệu mẫu
3. WorkoutDataHelper để truy vấn dữ liệu
4. LoginActivity tự động tạo tài khoản
5. App build thành công

## 🎯 Cần làm tiếp (theo thứ tự ưu tiên)

### 1. Hoàn thiện Trang chủ (Home Fragment)
**File cần sửa:** `app/src/main/java/com/htdgym/app/fragments/HomeFragment.java`

**Cần làm:**
- Load dữ liệu thống kê từ SQLite (calories, ngày tập, thời gian, số bài tập)
- Hiển thị circular progress
- Thêm click listeners cho các nút (AI Coach, Challenges, Settings)
- Load danh sách chương trình tập

**Code mẫu:**
```java
private void loadUserStats() {
    WorkoutDataHelper.getInstance().getUserStats(getContext(), 
        new WorkoutDataHelper.DataCallback<Integer[]>() {
            @Override
            public void onSuccess(Integer[] stats) {
                // stats[0] = totalWorkouts
                // stats[1] = totalCalories  
                // stats[2] = totalDuration (seconds)
                // stats[3] = totalDays
                
                tvCalories.setText(String.valueOf(stats[1]));
                tvDays.setText(String.valueOf(stats[3]));
                tvWorkouts.setText(String.valueOf(stats[0]));
                
                int hours = stats[2] / 3600;
                int minutes = (stats[2] % 3600) / 60;
                tvTime.setText(String.format("%02d:%02d", hours, minutes));
            }
            
            @Override
            public void onError(String error) {
                // Keep default values
            }
        });
}
```

### 2. Hoàn thiện Trang Tập luyện (Workouts Fragment)
**File:** `app/src/main/java/com/htdgym/app/fragments/WorkoutsFragment.java`

**Cần làm:**
- Tạo 6 category cards (Tay cả, Ngực, Vai, Chân, Bụng, Lưng)
- Load danh sách chương trình từ SQLite
- Hiển thị thumbnail và thông tin chương trình
- Click vào category để filter bài tập

**Layout cần:** Đã có `fragment_workouts.xml` - cần cập nhật

### 3. Tạo màn hình chi tiết chương trình
**File mới:** `ProgramDetailActivity.java` (đã có nhưng cần cập nhật)

**Cần làm:**
- Hiển thị thông tin chương trình (30 ngày, 60 ngày, 90 ngày)
- Danh sách các phase (giai đoạn)
- Danh sách bài tập trong mỗi phase
- Nút bắt đầu tập

### 4. Màn hình tập luyện (Workout Session)
**File:** `WorkoutSessionActivity.java` hoặc `ProgramExerciseSessionActivity.java`

**Cần làm:**
- Hiển thị video YouTube (nếu có)
- Timer đếm ngược
- Nút Play/Pause/Next/Previous
- Nút Nghỉ và Thoát
- Lưu lịch sử tập vào SQLite khi hoàn thành

### 5. Trang Tiến trình (Progress Fragment)
**File:** `app/src/main/java/com/htdgym/app/fragments/ProgressFragment.java`

**Cần làm:**
- Hiển thị số buổi tập, calo đốt
- Biểu đồ cân nặng
- Mục tiêu tuần này (progress bars)
- Số đo cơ thể (ngực, eo, mông, tay, đùi, bắp chân)
- Lịch sử tập luyện

### 6. Màn hình đăng nhập đẹp hơn
**File:** `activity_login.xml` và `LoginActivity.java`

**Cần làm:**
- Tabs: Đăng nhập / Đăng ký / Quên mật khẩu
- Nút đăng nhập Google/Facebook (UI only, chưa cần chức năng)
- Gradient background đẹp

## 📝 Ghi chú quan trọng

### Database đã có sẵn:
- `exercises` - 17 bài tập mẫu
- `workouts` - 8 workout mẫu
- `videos` - 6 video mẫu
- `challenges` - 3 thử thách mẫu

### Helper classes có sẵn:
- `WorkoutDataHelper` - Truy vấn dữ liệu
- `DatabaseInitializer` - Khởi tạo dữ liệu mẫu
- `StreakManager` - Quản lý streak
- `SoundManager` - Âm thanh

### Models đã có:
- User, Exercise, Workout, Video, Challenge
- WorkoutHistory, UserStats, WeightRecord
- UserStreak, BodyMeasurement

## 🚀 Bước tiếp theo ngay

1. Sửa `HomeFragment.java` để load stats từ SQLite
2. Test và chạy thử
3. Tiếp tục với WorkoutsFragment

## 💡 Tips
- Dùng `WorkoutDataHelper.getInstance()` để truy vấn dữ liệu
- Tất cả callback đều chạy trên background thread, nhớ dùng `runOnUiThread()`
- Database tự động khởi tạo dữ liệu mẫu lần đầu chạy app
