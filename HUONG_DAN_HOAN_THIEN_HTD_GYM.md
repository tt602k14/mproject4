# Hướng dẫn hoàn thiện HTD GYM App theo thiết kế gốc

## 📱 Thiết kế gốc (từ screenshots)

### 1. Màn hình đăng nhập
- Background: Gradient xanh lá
- Tabs: Đăng nhập / Đăng ký / Quên mật khẩu
- Input: Email, Mật khẩu
- Nút đăng nhập: Xanh lá, bo tròn
- Social login: Google (đỏ), Facebook (xanh dương)

### 2. Trang chủ (Home)
- Header: "HTD GYM" + icon settings
- Circular progress: Hiển thị calories (35 cal's)
- 3 stats: Ngày (2), Thời lượng (00:03:30), Bài tập (2)
- Banner: "LỊCH TẬP GIẢM CÂN - CHƯƠNG TRÌNH CHO NGƯỜI MỚI"

### 3. Trang Bài tập (Workouts)
- 6 category cards với icon emoji:
  - 💪 Tay cả (xanh lá)
  - 🏃 Ngực (xanh dương)
  - 🤸 Vai (cam)
  - 🦵 Chân (tím)
  - 🔥 Bụng (đỏ)
  - 🏋️ Lưng (xanh dương đậm)
- Danh sách chương trình:
  - Chương Trình Cho Người Mới - 30 Ngày
  - Chương Trình Trung Cấp - 60 Ngày
  - Chương Trình Nâng Cao - 90 Ngày
  - Tập Tay Trong 45 Ngày
  - Bộ Ngực Vạm Vỡ Trong 45 Ngày
  - Đôi Chân Mạnh Mẽ Trong 45 Ngày
  - Bài Tập Lưng Và Vai
  - Tập Luyện Toàn Thân
  - Giảm Mỡ Bụng - Người Mới
  - Cardio Giảm Cân
  - HIIT Đốt Mỡ

### 4. Trang Tiến trình (Progress)
- ⚡ 34 Buổi tập
- 🔥 18,500 Calo đốt
- ⚖️ Cân nặng: Hiện tại 72.0 kg, Thay đổi -3.0 kg, Mục tiêu 70.0 kg
- Biểu đồ 30 ngày
- Nút "Cập nhật cân nặng"
- 🎯 Mục tiêu tuần này:
  - Buổi tập: 4/5
  - Calo đốt: 2,800/3,500
  - Ngày hoạt động: 5/7
- 📏 Số đo cơ thể:
  - Ngực: 98 cm
  - Eo: 78 cm
  - Mông: 95 cm
  - Tay: 35 cm
  - Đùi: 58 cm
  - Bắp chân: 38 cm
- Nút "Cập nhật số đo"
- 📅 Lịch sử tập luyện
- Nút "Xem toàn bộ lịch sử"

### 5. Màn hình tập luyện
- Video thumbnail với nút play
- Tên bài tập: "Hit đất cơ bản"
- Thông tin: "3 hiệp x 15 lần"
- Timer: 00:30 (màu xanh lá)
- Progress bar
- Text: "Bài tập 1/8"
- 3 nút control:
  - ⏮️ Previous (đen)
  - ▶️ Play (xanh lá, to)
  - ⏭️ Next (đen)
- 2 nút action:
  - ⏸️ NGHỈ (cam)
  - ❌ THOÁT (đỏ)

## 🎨 Màu sắc chính
- Primary: #4CAF50 (xanh lá)
- Background: #FFFFFF (trắng)
- Card: #F5F5F5 (xám nhạt)
- Text: #000000 (đen)
- Secondary text: #666666 (xám)

## 📝 Các bước thực hiện

### Bước 1: Cập nhật HomeFragment ✅
File đã được tạo với đầy đủ chức năng load stats từ SQLite.

### Bước 2: Cập nhật WorkoutsFragment
Cần tạo:
- 6 category cards với màu sắc và icon
- RecyclerView cho danh sách chương trình
- Adapter để hiển thị workout programs

### Bước 3: Cập nhật ProgressFragment  
Cần hiển thị:
- Stats tổng quan
- Biểu đồ cân nặng
- Mục tiêu tuần
- Số đo cơ thể
- Lịch sử tập luyện

### Bước 4: Màn hình tập luyện
Sử dụng `ProgramExerciseSessionActivity` hoặc `WorkoutSessionActivity`
Cần:
- Video player (YouTube hoặc local)
- Timer với animation
- Control buttons
- Save workout history khi hoàn thành

### Bước 5: Màn hình đăng nhập đẹp
Cập nhật `activity_login.xml` và `LoginActivity.java`

## 🔧 Code mẫu quan trọng

### Load workout programs
```java
WorkoutDataHelper.getInstance().getAllWorkouts(context, 
    new WorkoutDataHelper.DataCallback<List<Workout>>() {
        @Override
        public void onSuccess(List<Workout> workouts) {
            // Display in RecyclerView
        }
        
        @Override
        public void onError(String error) {
            // Handle error
        }
    });
```

### Save workout history
```java
WorkoutDataHelper.getInstance().saveWorkoutHistory(
    context,
    "30day",  // programType
    0,        // phaseIndex
    0,        // exerciseIndex
    "Push-ups", // exerciseName
    1800,     // durationSeconds
    250,      // caloriesBurned
    callback
);
```

## 📦 Tài nguyên cần thêm
- Icon emoji cho categories
- Ảnh thumbnail cho workout programs
- Video hoặc GIF cho bài tập
- Sound effects cho timer

## ✅ Checklist
- [x] SQLite database hoạt động
- [x] HomeFragment load stats
- [ ] WorkoutsFragment với categories
- [ ] ProgressFragment đầy đủ
- [ ] Workout session screen
- [ ] Login screen đẹp
- [ ] Test toàn bộ flow

## 🚀 Chạy app
```bash
./gradlew clean assembleDebug
./gradlew installDebug
```

Hoặc trong Android Studio: Run > Run 'app'
