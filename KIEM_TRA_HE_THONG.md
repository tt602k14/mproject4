# ✅ KIỂM TRA HỆ THỐNG - HTD GYM

## 🎯 MỤC ĐÍCH
Đảm bảo tất cả các thành phần đang hoạt động trước khi làm tiếp.

---

## 📋 CHECKLIST KIỂM TRA

### 1. XAMPP
- [ ] XAMPP đã cài đặt
- [ ] Apache đang chạy (màu xanh)
- [ ] MySQL đang chạy (màu xanh)
- [ ] Có thể truy cập: http://localhost/phpmyadmin

### 2. Backend
- [ ] Backend đã copy vào: `C:\xampp\htdocs\htd_gym\backend\`
- [ ] Test: http://localhost/htd_gym/backend/api/test.php → Thấy JSON
- [ ] Test: http://localhost/htd_gym/backend/test_connection.php → Tất cả ✅
- [ ] Test: http://localhost/htd_gym/backend/api/auth.php → Thấy JSON "Action required"

### 3. Database
- [ ] Database "htd_gym" đã tạo
- [ ] Có 10 bảng: users, members, workouts, exercises, user_stats, workout_sessions, payments, equipment, videos, trainers
- [ ] Bảng users có thể insert dữ liệu
- [ ] Test đăng ký từ web: http://localhost/htd_gym/backend/test_register.html

### 4. Android App
- [ ] ApiConfig.java có đúng IP (10.0.2.2 cho emulator)
- [ ] App build thành công không có lỗi
- [ ] App chạy được và hiển thị màn hình Welcome
- [ ] Có thể chuyển từ Welcome → MainActivity
- [ ] Có thể mở LoginActivity

### 5. Đăng ký/Đăng nhập
- [ ] Có thể đăng ký tài khoản mới từ app
- [ ] Dữ liệu xuất hiện trong phpMyAdmin → users
- [ ] Có thể đăng nhập với tài khoản vừa tạo
- [ ] Sau đăng nhập vào được MainActivity
- [ ] Session được lưu (đóng app mở lại vẫn đăng nhập)

---

## 🧪 TEST NHANH (5 PHÚT)

### Test 1: Backend hoạt động
```bash
# Mở trình duyệt
http://localhost/htd_gym/backend/api/test.php

# Kết quả mong đợi:
{
  "success": true,
  "message": "Backend is working!",
  ...
}
```

### Test 2: MySQL kết nối
```bash
http://localhost/htd_gym/backend/test_connection.php

# Phải thấy:
✅ Test 1: PHP Version
✅ Test 2: MySQL Extension
✅ Test 3: Kết nối MySQL
✅ Test 4: Database "htd_gym"
✅ Test 5: Bảng Users
✅ Test 6: API Endpoint
```

### Test 3: Đăng ký từ web
```bash
http://localhost/htd_gym/backend/test_register.html

# Điền form:
Họ tên: Test User
Email: test123@gmail.com
Mật khẩu: 123456

# Nhấn ĐĂNG KÝ
# Phải thấy: "✅ Đăng ký thành công!"
```

### Test 4: Kiểm tra database
```bash
http://localhost/phpmyadmin
→ Database: htd_gym
→ Bảng: users
→ Phải thấy user "test123@gmail.com"
```

### Test 5: Đăng ký từ app
```
1. Chạy app Android
2. Mở LoginActivity
3. Tab "ĐĂNG KÝ"
4. Điền thông tin
5. Nhấn "ĐĂNG KÝ"
6. Phải vào được MainActivity
```

---

## ❌ NẾU CÓ LỖI

### Lỗi Test 1: 404 Not Found
**Nguyên nhân:** Backend chưa copy đúng chỗ
**Fix:** Copy lại backend vào `C:\xampp\htdocs\htd_gym\`

### Lỗi Test 2: Connection failed
**Nguyên nhân:** MySQL chưa start
**Fix:** XAMPP Control Panel → Start MySQL

### Lỗi Test 2: Database not found
**Nguyên nhân:** Database chưa tạo
**Fix:** phpMyAdmin → SQL → Chạy file database.sql

### Lỗi Test 3: Email already exists
**Nguyên nhân:** Email đã đăng ký
**Fix:** Dùng email khác hoặc xóa user cũ

### Lỗi Test 5: Cannot convert to JSONObject
**Nguyên nhân:** Backend trả về HTML thay vì JSON
**Fix:** Đọc file FIX_LOI_JSON.md

### Lỗi Test 5: Connection refused
**Nguyên nhân:** IP không đúng hoặc Apache chưa start
**Fix:** 
- Kiểm tra Apache đã start
- Kiểm tra IP trong ApiConfig.java

---

## 📊 TÌNH TRẠNG HỆ THỐNG

### ✅ Đã hoàn thành:
- [x] Splash Screen
- [x] Welcome Screen với chọn ngôn ngữ
- [x] Home Screen với progress tracking
- [x] Workout categories (All, Chest, Shoulder, Legs, Abs, Back)
- [x] Workout Detail với danh sách bài tập
- [x] Backend API (auth, workouts, members, payments, equipment, videos)
- [x] MySQL Database với 10 bảng
- [x] Đăng ký tài khoản (lưu vào MySQL)
- [x] Đăng nhập (xác thực từ MySQL)
- [x] Session management

### 🔄 Đang làm:
- [ ] Fix lỗi JSON (nếu còn)
- [ ] Test toàn bộ hệ thống

### 📝 Chưa làm:
- [ ] Tích hợp API cho Workouts (hiện tại dùng data tĩnh)
- [ ] Tích hợp API cho Members
- [ ] Tích hợp API cho Payments
- [ ] Tích hợp API cho Equipment
- [ ] Tích hợp API cho Videos
- [ ] Progress tracking (lưu tiến độ tập luyện)
- [ ] Nutrition (dinh dưỡng)
- [ ] AI Coach
- [ ] Community features
- [ ] Video player
- [ ] Workout session (đếm thời gian, set, rep)
- [ ] Profile management
- [ ] Settings

---

## 🎯 BƯỚC TIẾP THEO

Sau khi tất cả checklist trên PASS, bạn có thể:

### Option 1: Tích hợp API cho các trang
- Workouts: Lấy danh sách bài tập từ MySQL
- Members: Quản lý thành viên
- Payments: Quản lý thanh toán
- Equipment: Quản lý thiết bị
- Videos: Quản lý video

### Option 2: Làm tính năng mới
- Progress tracking: Theo dõi tiến độ
- Nutrition: Dinh dưỡng
- AI Coach: Huấn luyện viên AI
- Workout session: Tập luyện với timer

### Option 3: Cải thiện UI/UX
- Animation
- Better design
- Dark mode
- Responsive layout

---

## 💡 KHUYẾN NGHỊ

**Làm theo thứ tự:**
1. ✅ Đảm bảo checklist trên PASS hết
2. ✅ Test đăng ký/đăng nhập hoạt động 100%
3. ➡️ Tích hợp API cho Workouts (quan trọng nhất)
4. ➡️ Làm Progress tracking
5. ➡️ Làm Workout session với timer
6. ➡️ Các tính năng khác

**Lý do:** 
- Workouts là tính năng chính của app gym
- Progress tracking giúp user theo dõi tiến độ
- Workout session giúp user tập luyện hiệu quả

---

## 📞 HỖ TRỢ

Nếu có vấn đề:
1. Đọc file FIX_LOI_JSON.md
2. Đọc file HUONG_DAN_DANG_KY.md
3. Đọc file QUICK_START.md
4. Chạy test_connection.php để debug

**Sẵn sàng làm tiếp! 🚀**
