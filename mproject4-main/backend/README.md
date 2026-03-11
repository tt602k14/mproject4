# HTD GYM Backend - Hướng dẫn cài đặt siêu đơn giản

## Bước 1: Cài XAMPP
1. Download XAMPP: https://www.apachefriends.org/
2. Cài đặt XAMPP
3. Mở XAMPP Control Panel
4. Click "Start" cho Apache và MySQL

## Bước 2: Copy files
1. Copy toàn bộ thư mục `backend` này
2. Paste vào `C:\xampp\htdocs\htd_gym\`

Kết quả:
```
C:\xampp\htdocs\htd_gym\backend\
```

## Bước 3: Chạy setup tự động
1. Mở trình duyệt
2. Vào: http://localhost/htd_gym/backend/setup.php
3. Click nút "Bắt đầu cài đặt"
4. Xong! ✅

## Bước 4: Test Kết Nối
Mở trình duyệt và test:
- http://localhost/htd_gym/backend/test_connection.php ← Kiểm tra MySQL
- http://localhost/htd_gym/backend/test_register.html ← Test đăng ký/đăng nhập

Nếu thấy tất cả ✅ → Thành công! 🎉

## Bước 5: Test API
Mở trình duyệt và test:
- http://localhost/htd_gym/backend/api/auth.php ← Đăng ký/Đăng nhập
- http://localhost/htd_gym/backend/api/workouts.php ← Bài tập
- http://localhost/htd_gym/backend/api/members.php ← Thành viên

Nếu thấy JSON data → API hoạt động! 🎉

## Bước 5: Cấu hình Android App

### Test trên Emulator:
Mở file `app/src/main/java/com/htdgym/app/api/ApiConfig.java`

Sửa dòng:
```java
public static final String BASE_URL = "http://10.0.2.2/htd_gym/backend/api/";
```

### Test trên điện thoại thật:
1. Kiểm tra IP máy tính:
   - Mở CMD
   - Gõ: `ipconfig`
   - Tìm "IPv4 Address" (ví dụ: 192.168.1.100)

2. Sửa file `ApiConfig.java`:
```java
public static final String BASE_URL = "http://192.168.1.100/htd_gym/backend/api/";
```

3. Đảm bảo điện thoại và máy tính cùng WiFi

## Bước 6: Chạy App và Đăng Ký
1. Build và chạy Android app
2. Nhấn vào tab "ĐĂNG KÝ"
3. Điền thông tin:
   - Họ tên: Test User
   - Email: test@gmail.com
   - Mật khẩu: 123456
4. Nhấn "ĐĂNG KÝ"
5. Nếu thành công → Tự động đăng nhập và vào trang chủ!

## Kiểm tra dữ liệu trong MySQL
1. Mở: http://localhost/phpmyadmin
2. Chọn database "htd_gym"
3. Nhấn vào bảng "users"
4. Xem tài khoản vừa đăng ký!

## Xong rồi!
Hệ thống đã sẵn sàng! App sẽ tự động lấy data từ MySQL! 🚀

**Tính năng đã có:**
- ✅ Đăng ký tài khoản (lưu vào MySQL)
- ✅ Đăng nhập (xác thực từ MySQL)
- ✅ Quên mật khẩu
- ✅ Quản lý bài tập
- ✅ Quản lý thành viên
- ✅ Thống kê người dùng
- ✅ Quản lý thanh toán
- ✅ Quản lý thiết bị
- ✅ Quản lý video

## Nếu có lỗi:

### "Connection refused"
- Kiểm tra Apache đã Start chưa
- Kiểm tra IP đúng chưa

### "Database connection failed"  
- Kiểm tra MySQL đã Start chưa
- Chạy lại setup.php

### "404 Not Found"
- Kiểm tra đường dẫn: `C:\xampp\htdocs\htd_gym\backend\`
- Đảm bảo có thư mục `api` bên trong

## Cấu trúc thư mục đúng:
```
C:\xampp\htdocs\htd_gym\
└── backend\
    ├── setup.php              ← Chạy file này đầu tiên
    ├── test_connection.php    ← Test kết nối MySQL
    ├── test_register.html     ← Test đăng ký/đăng nhập
    ├── config.php
    ├── database.sql
    ├── README.md
    └── api\
        ├── auth.php           ← Đăng ký & Đăng nhập
        ├── workouts.php
        ├── members.php
        ├── user_stats.php
        ├── payments.php
        ├── equipment.php
        └── videos.php
```

## Hỗ trợ
Nếu vẫn gặp lỗi, kiểm tra:
1. XAMPP Apache và MySQL đã Start chưa (màu xanh)
2. Đường dẫn file đúng chưa
3. Firewall có chặn không
