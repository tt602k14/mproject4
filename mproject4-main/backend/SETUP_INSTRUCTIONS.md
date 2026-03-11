# Hướng dẫn cài đặt HTD GYM Backend với XAMPP

## Bước 1: Cài đặt XAMPP
1. Download XAMPP từ: https://www.apachefriends.org/
2. Cài đặt XAMPP (chọn Apache và MySQL)
3. Khởi động XAMPP Control Panel
4. Start Apache và MySQL

## Bước 2: Tạo Database
1. Mở trình duyệt, truy cập: http://localhost/phpmyadmin
2. Click "New" để tạo database mới
3. Hoặc import file `database.sql`:
   - Click tab "Import"
   - Chọn file `database.sql`
   - Click "Go"

## Bước 3: Copy Backend Files
1. Mở thư mục XAMPP (thường là `C:\xampp\htdocs\`)
2. Tạo thư mục mới tên `htd_gym`
3. Copy toàn bộ thư mục `backend` vào `C:\xampp\htdocs\htd_gym\`

Cấu trúc thư mục:
```
C:\xampp\htdocs\htd_gym\
├── backend\
│   ├── config.php
│   ├── database.sql
│   └── api\
│       ├── workouts.php
│       ├── members.php
│       ├── user_stats.php
│       └── ...
```

## Bước 4: Kiểm tra API
1. Mở trình duyệt
2. Test API workouts: http://localhost/htd_gym/backend/api/workouts.php
3. Bạn sẽ thấy JSON response với danh sách workouts

## Bước 5: Cấu hình Android App

### Nếu test trên Android Emulator:
- Trong file `ApiConfig.java`, giữ nguyên:
```java
public static final String BASE_URL = "http://10.0.2.2/htd_gym/backend/api/";
```

### Nếu test trên thiết bị thật:
1. Kiểm tra IP máy tính:
   - Windows: Mở CMD, gõ `ipconfig`
   - Tìm "IPv4 Address" (ví dụ: 192.168.1.100)

2. Sửa file `ApiConfig.java`:
```java
public static final String BASE_URL = "http://192.168.1.100/htd_gym/backend/api/";
```

3. Đảm bảo điện thoại và máy tính cùng mạng WiFi

## Bước 6: Test kết nối
1. Build và chạy Android app
2. App sẽ tự động load data từ MySQL database
3. Kiểm tra log để xem có lỗi kết nối không

## Troubleshooting

### Lỗi "Connection refused"
- Kiểm tra Apache đã start chưa
- Kiểm tra IP address đúng chưa
- Tắt firewall tạm thời để test

### Lỗi "Database connection failed"
- Kiểm tra MySQL đã start chưa
- Kiểm tra username/password trong `config.php`
- Mặc định XAMPP: user=root, password=rỗng

### Lỗi "404 Not Found"
- Kiểm tra đường dẫn file có đúng không
- Kiểm tra cấu trúc thư mục

## API Endpoints

### Workouts
- GET all: `workouts.php`
- GET by ID: `workouts.php?id=1`
- GET by category: `workouts.php?category=chest`
- POST create: `workouts.php` (with JSON body)
- PUT update: `workouts.php` (with JSON body)
- DELETE: `workouts.php` (with JSON body)

### Members
- GET all: `members.php`
- GET by ID: `members.php?id=1`
- POST create: `members.php` (with JSON body)
- PUT update: `members.php` (with JSON body)
- DELETE: `members.php` (with JSON body)

### User Stats
- GET stats: `user_stats.php?user_id=1`
- GET by date: `user_stats.php?user_id=1&date=2024-01-15`
- POST update: `user_stats.php` (with JSON body)

## Database Schema
Xem chi tiết trong file `database.sql`

Các bảng chính:
- users: Người dùng
- members: Thành viên gym
- workouts: Bài tập
- exercises: Động tác tập
- user_stats: Thống kê người dùng
- workout_sessions: Phiên tập luyện
- payments: Thanh toán
- equipment: Thiết bị
- videos: Video hướng dẫn
- trainers: Huấn luyện viên
