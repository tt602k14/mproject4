# Hướng dẫn xem SQLite Database trong Android

## ✅ Đã hoàn thành

### 1. Tích hợp SQLite Database
- ✅ LoginActivity: Xác thực user từ database
- ✅ RegisterActivity: Lưu user mới vào database
- ✅ Password được hash bằng SHA-256
- ✅ Kiểm tra email trùng lặp
- ✅ Lưu session vào SharedPreferences

### 2. Cấu trúc Database

**Database name:** `gym_database`

**User table (users):**
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT,
    password TEXT,
    name TEXT,
    phone TEXT,
    profileImage TEXT,
    loginType TEXT,
    firebaseUid TEXT
)
```

## 📱 Cách xem Database trên thiết bị Android

### Phương pháp 1: Sử dụng Android Studio Database Inspector (Khuyến nghị)

1. **Chạy app trên emulator hoặc thiết bị thật**
2. **Mở Database Inspector:**
   - View → Tool Windows → App Inspection
   - Chọn tab "Database Inspector"
3. **Chọn app:** `com.htdgym.app`
4. **Xem tables:** Bạn sẽ thấy tất cả tables bao gồm `users`
5. **Query data:** Click vào table để xem dữ liệu hoặc chạy SQL query

### Phương pháp 2: Sử dụng ADB (Android Debug Bridge)

```bash
# 1. Kết nối thiết bị và mở shell
adb shell

# 2. Chuyển sang user app (nếu cần)
run-as com.htdgym.app

# 3. Vào thư mục database
cd databases

# 4. Mở SQLite
sqlite3 gym_database

# 5. Xem danh sách tables
.tables

# 6. Xem cấu trúc table users
.schema users

# 7. Xem tất cả users
SELECT * FROM users;

# 8. Xem user cụ thể
SELECT * FROM users WHERE email = 'test@gmail.com';

# 9. Thoát
.exit
```

### Phương pháp 3: Export database ra máy tính

```bash
# 1. Export database từ thiết bị
adb exec-out run-as com.htdgym.app cat databases/gym_database > gym_database.db

# 2. Mở bằng DB Browser for SQLite
# Download tại: https://sqlitebrowser.org/
```

### Phương pháp 4: Sử dụng Stetho (Facebook Debug Tool)

**Thêm vào build.gradle:**
```gradle
dependencies {
    debugImplementation 'com.facebook.stetho:stetho:1.6.0'
}
```

**Thêm vào Application class:**
```java
Stetho.initializeWithDefaults(this);
```

**Xem database:**
1. Chạy app
2. Mở Chrome browser
3. Vào: `chrome://inspect`
4. Click "inspect" trên app của bạn
5. Chọn tab "Resources" → "Web SQL" → "gym_database"

## 🧪 Test Database

### Test đăng ký user mới:
1. Mở app
2. Chọn tab "Đăng ký"
3. Nhập thông tin:
   - Họ tên: Nguyễn Văn A
   - Username: nguyenvana
   - Email: test@gmail.com
   - Phone: 0123456789
   - Password: 123456
   - Confirm: 123456
4. Click "Đăng ký"
5. Kiểm tra database → table `users` sẽ có 1 record mới

### Test đăng nhập:
1. Logout khỏi app
2. Mở LoginActivity
3. Nhập:
   - Email: test@gmail.com
   - Password: 123456
4. Click "Đăng nhập"
5. Nếu thành công → chuyển sang MainActivity

## 📊 Các tables khác trong database

```
- users: Thông tin người dùng
- members: Thành viên gym
- workouts: Bài tập
- payments: Thanh toán
- equipment: Thiết bị
- trainers: Huấn luyện viên
- exercises: Bài tập chi tiết
- videos: Video hướng dẫn
- user_stats: Thống kê người dùng
- workout_stats: Thống kê workout
- food_items: Thực phẩm
- meal_logs: Nhật ký ăn uống
- body_measurements: Đo lường cơ thể
- workout_logs: Nhật ký tập luyện
```

## 🔐 Bảo mật

- Password được hash bằng SHA-256 trước khi lưu
- Không lưu plain text password
- Session được lưu trong SharedPreferences
- Database chỉ truy cập được từ app

## 🛠️ Troubleshooting

### Không thấy database trong Database Inspector?
- Đảm bảo app đang chạy
- Refresh Database Inspector
- Restart Android Studio

### Lỗi "database is locked"?
- Đóng tất cả connections
- Restart app
- Clear app data

### Không thể truy cập database qua ADB?
- Kiểm tra USB debugging đã bật
- Thử: `adb root` (nếu có root)
- Sử dụng emulator thay vì thiết bị thật

## 📝 Notes

- Database version hiện tại: 7
- Database được tạo tự động khi app chạy lần đầu
- Sử dụng Room Database (Android Jetpack)
- Fallback to destructive migration khi upgrade version
