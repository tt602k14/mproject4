# 🚀 HƯỚNG DẪN NHANH - HTD GYM

## ✅ CHUẨN BỊ (5 phút)

### 1. Start XAMPP
```
Mở XAMPP Control Panel
→ Nhấn START cho Apache
→ Nhấn START cho MySQL
→ Đợi cả 2 màu xanh
```

### 2. Copy Backend
```
Copy thư mục: backend
Paste vào: C:\xampp\htdocs\htd_gym\
```

### 3. Tạo Database
```
Mở trình duyệt
→ http://localhost/phpmyadmin
→ Tab "SQL"
→ Copy nội dung file backend/database.sql
→ Paste và nhấn "Go"
```

---

## 🧪 TEST HỆ THỐNG (2 phút)

### Test 1: Kiểm tra kết nối
```
http://localhost/htd_gym/backend/test_connection.php
```
Phải thấy tất cả ✅

### Test 2: Test đăng ký từ web
```
http://localhost/htd_gym/backend/test_register.html
```
Điền form và nhấn "ĐĂNG KÝ"

### Test 3: Kiểm tra dữ liệu
```
http://localhost/phpmyadmin
→ Database: htd_gym
→ Bảng: users
→ Xem tài khoản vừa tạo
```

---

## 📱 CHẠY APP ANDROID (3 phút)

### Bước 1: Cấu hình IP
Mở file: `app/src/main/java/com/htdgym/app/api/ApiConfig.java`

**Nếu dùng Emulator:**
```java
public static final String BASE_URL = "http://10.0.2.2/htd_gym/backend/api/";
```

**Nếu dùng thiết bị thật:**
```java
// Thay 192.168.1.100 bằng IP máy bạn (gõ ipconfig trong CMD)
public static final String BASE_URL = "http://192.168.1.100/htd_gym/backend/api/";
```

### Bước 2: Build & Run
```
Android Studio → Run App
```

### Bước 3: Đăng ký tài khoản
```
1. Mở app HTD GYM
2. Nhấn tab "ĐĂNG KÝ"
3. Điền thông tin:
   - Họ tên: Test User
   - Email: test@gmail.com
   - Mật khẩu: 123456
   - Xác nhận: 123456
4. Nhấn "ĐĂNG KÝ"
5. Thành công → Tự động vào trang chủ!
```

---

## ❌ LỖI THƯỜNG GẶP

### "Lỗi kết nối"
→ MySQL chưa start trong XAMPP
→ Nhấn START cho MySQL

### "Database not found"
→ Chưa chạy file database.sql
→ Vào phpMyAdmin và import lại

### "Email already exists"
→ Email đã được đăng ký
→ Dùng email khác hoặc xóa user cũ

### App không kết nối (thiết bị thật)
→ IP không đúng
→ Gõ `ipconfig` trong CMD để xem IP
→ Sửa lại trong ApiConfig.java

---

## 📊 KIỂM TRA THÀNH CÔNG

✅ XAMPP Apache và MySQL đang chạy (màu xanh)
✅ test_connection.php hiển thị tất cả ✅
✅ test_register.html đăng ký được
✅ Dữ liệu xuất hiện trong phpMyAdmin
✅ App Android đăng ký được và vào trang chủ

---

## 📚 TÀI LIỆU CHI TIẾT

- **HUONG_DAN_DANG_KY.md** - Hướng dẫn chi tiết đăng ký
- **backend/README.md** - Hướng dẫn backend
- **backend/test_connection.php** - Test kết nối
- **backend/test_register.html** - Test đăng ký

---

## 🎯 CHECKLIST

- [ ] XAMPP đã cài đặt
- [ ] Apache và MySQL đã start
- [ ] Backend đã copy vào htdocs
- [ ] Database đã tạo (chạy database.sql)
- [ ] test_connection.php hiển thị OK
- [ ] test_register.html hoạt động
- [ ] ApiConfig.java đã cấu hình đúng IP
- [ ] App chạy và đăng ký được

**Hoàn thành tất cả → Hệ thống sẵn sàng! 🎉**
