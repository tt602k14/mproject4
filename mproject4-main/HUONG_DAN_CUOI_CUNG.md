# 🎯 HƯỚNG DẪN CUỐI CÙNG - ĐĂNG KÝ VÀO DATABASE

## ✅ HỆ THỐNG ĐÃ SẴN SÀNG!

Tất cả code đã hoàn hảo:
- ✅ Database có bảng `users` với đầy đủ trường
- ✅ Backend API `auth.php` hoạt động
- ✅ Android app `LoginActivity` kết nối API
- ✅ Đăng ký → Lưu vào MySQL → Vào MainActivity

---

## 🚀 3 BƯỚC ĐỂ HOẠT ĐỘNG

### BƯỚC 1: START MYSQL (30 giây)
```
1. Mở XAMPP Control Panel
2. Nhấn START cho Apache
3. Nhấn START cho MySQL
4. Đợi cả 2 màu xanh
```

### BƯỚC 2: TẠO DATABASE (1 phút)
```
1. Mở trình duyệt
2. Vào: http://localhost/phpmyadmin
3. Nhấn tab "SQL"
4. Mở file: backend/database.sql
5. Copy TOÀN BỘ nội dung
6. Paste vào ô SQL
7. Nhấn "Go"
8. Phải thấy: "Database htd_gym created"
```

### BƯỚC 3: TEST (2 phút)

#### Test 1: Kiểm tra database
```
http://localhost/phpmyadmin
→ Chọn database: htd_gym
→ Phải thấy 10 bảng
→ Nhấn vào bảng: users
→ Phải thấy 1 user admin
```

#### Test 2: Test backend
```
http://localhost/htd_gym/backend/check_mysql.php
→ Phải thấy tất cả ✅
```

#### Test 3: Đăng ký từ web
```
http://localhost/htd_gym/backend/test_register.html

Điền form:
- Họ tên: Test User
- Email: test999@gmail.com
- Mật khẩu: 123456

Nhấn "ĐĂNG KÝ"
→ Phải thấy: "✅ Đăng ký thành công!"
```

#### Test 4: Kiểm tra dữ liệu
```
http://localhost/phpmyadmin
→ Database: htd_gym
→ Bảng: users
→ Phải thấy user "test999@gmail.com"
```

#### Test 5: Đăng ký từ app
```
1. Chạy app Android
2. Từ WelcomeActivity → Nhấn "BẮT ĐẦU" → MainActivity
3. Từ MainActivity → Mở menu → Nhấn "Đăng nhập"
4. Hoặc từ SplashActivity → LoginActivity
5. Tab "ĐĂNG KÝ"
6. Điền:
   - Họ tên: Hung
   - Email: hung@gmail.com
   - Mật khẩu: 123456
   - Xác nhận: 123456
7. Nhấn "ĐĂNG KÝ"
8. Phải thấy Toast: "Đăng ký thành công!"
9. Tự động vào MainActivity
```

---

## 📊 LUỒNG HOẠT ĐỘNG

```
[App Android - LoginActivity]
    ↓
    User điền form đăng ký
    ↓
    Validation (email, password, etc.)
    ↓
    Gửi POST request đến API
    ↓
[Backend - auth.php]
    ↓
    Nhận request
    ↓
    Kiểm tra email đã tồn tại chưa
    ↓
    Mã hóa mật khẩu (password_hash)
    ↓
    INSERT INTO users (username, email, password, full_name, phone)
    ↓
    Trả về JSON: {success: true, data: {...}}
    ↓
[MySQL Database - htd_gym.users]
    ↓
    Lưu dữ liệu:
    - id: auto increment
    - username: email
    - email: email
    - password: hashed password
    - full_name: họ tên
    - phone: null
    - created_at: timestamp
    ↓
[App Android]
    ↓
    Nhận response
    ↓
    Lưu session vào SharedPreferences:
    - user_id
    - username
    - email
    - full_name
    - is_logged_in = true
    ↓
    Intent → MainActivity
    ↓
    finish() LoginActivity
```

---

## 🗄️ DỮ LIỆU TRONG DATABASE

Sau khi đăng ký, bảng `users` sẽ có:

| id | username | email | password | full_name | phone | created_at |
|----|----------|-------|----------|-----------|-------|------------|
| 1 | admin | admin@htdgym.com | $2y$10$... | Admin HTD GYM | NULL | 2024-01-15 10:00:00 |
| 2 | test999@gmail.com | test999@gmail.com | $2y$10$... | Test User | NULL | 2024-01-15 10:30:00 |
| 3 | hung@gmail.com | hung@gmail.com | $2y$10$... | Hung | NULL | 2024-01-15 11:00:00 |

**Lưu ý:** Password được mã hóa bằng bcrypt, không thể giải mã ngược lại!

---

## 🔍 CÁCH KIỂM TRA

### Kiểm tra trong phpMyAdmin
```sql
-- Xem tất cả users
SELECT * FROM users ORDER BY created_at DESC;

-- Đếm số users
SELECT COUNT(*) as total FROM users;

-- Xem user mới nhất
SELECT * FROM users ORDER BY id DESC LIMIT 1;

-- Tìm user theo email
SELECT * FROM users WHERE email = 'hung@gmail.com';
```

### Kiểm tra trong app
```
1. Đăng ký tài khoản mới
2. Đóng app
3. Mở lại app
4. Phải tự động đăng nhập (vì session đã lưu)
5. Vào MainActivity luôn
```

---

## ❌ XỬ LÝ LỖI

### Lỗi: "Value <br cannot be converted to JSONObject"
**Nguyên nhân:** MySQL chưa start hoặc database chưa tạo
**Fix:**
1. XAMPP → Start MySQL
2. phpMyAdmin → Chạy database.sql
3. Test lại

### Lỗi: "Email đã được sử dụng"
**Nguyên nhân:** Email đã đăng ký
**Fix:** Dùng email khác hoặc xóa user cũ

### Lỗi: "Connection refused"
**Nguyên nhân:** Apache chưa start hoặc IP sai
**Fix:**
1. XAMPP → Start Apache
2. Kiểm tra ApiConfig.java:
   - Emulator: `http://10.0.2.2/htd_gym/backend/api/`
   - Thiết bị thật: `http://[IP_MAY_TINH]/htd_gym/backend/api/`

### Lỗi: "Database not found"
**Nguyên nhân:** Database chưa tạo
**Fix:** Chạy file database.sql trong phpMyAdmin

---

## 📱 CÁCH MỞ LOGINACTIVITY

### Cách 1: Từ WelcomeActivity
```java
// Nếu chưa đăng nhập
Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
startActivity(intent);
```

### Cách 2: Từ MainActivity (menu)
```java
// Thêm button "Đăng nhập" trong menu
Intent intent = new Intent(MainActivity.this, LoginActivity.class);
startActivity(intent);
```

### Cách 3: Tự động (SplashActivity)
```java
// Kiểm tra session
SharedPreferences prefs = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);
boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

if (isLoggedIn) {
    // Đã đăng nhập → MainActivity
    startActivity(new Intent(this, MainActivity.class));
} else {
    // Chưa đăng nhập → LoginActivity
    startActivity(new Intent(this, LoginActivity.class));
}
finish();
```

---

## 🎯 CHECKLIST HOÀN THÀNH

- [ ] XAMPP Apache đã start (màu xanh)
- [ ] XAMPP MySQL đã start (màu xanh)
- [ ] Database htd_gym đã tạo (10 bảng)
- [ ] Bảng users có user admin
- [ ] Backend đã copy vào: C:\xampp\htdocs\htd_gym\backend\
- [ ] Test: http://localhost/htd_gym/backend/check_mysql.php → Tất cả ✅
- [ ] Test: http://localhost/htd_gym/backend/test_register.html → Đăng ký OK
- [ ] Kiểm tra phpMyAdmin → Dữ liệu đã lưu
- [ ] ApiConfig.java có đúng IP (10.0.2.2)
- [ ] App chạy được
- [ ] Đăng ký từ app → Thành công
- [ ] Dữ liệu xuất hiện trong MySQL
- [ ] Sau đăng ký vào được MainActivity
- [ ] Đóng app mở lại vẫn đăng nhập

---

## 🎉 HOÀN THÀNH!

Nếu tất cả checklist trên PASS:
- ✅ Hệ thống đăng ký hoạt động 100%
- ✅ Dữ liệu lưu vào MySQL
- ✅ Sau đăng ký vào MainActivity
- ✅ Session được lưu

**Bạn đã thành công! 🚀**

---

## 📞 NẾU VẪN CÓ LỖI

Chụp màn hình và gửi cho tôi:
1. XAMPP Control Panel (Apache + MySQL màu xanh)
2. phpMyAdmin → Database htd_gym → Bảng users
3. check_mysql.php (kết quả kiểm tra)
4. Lỗi từ app (nếu có)

Tôi sẽ giúp bạn fix ngay! 💪
