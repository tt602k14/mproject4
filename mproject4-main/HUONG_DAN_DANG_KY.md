# 📱 HƯỚNG DẪN ĐĂNG KÝ TÀI KHOẢN - HTD GYM

## ✅ TÌNH TRẠNG HỆ THỐNG

Hệ thống đăng ký đã được cài đặt hoàn chỉnh và sẵn sàng sử dụng!

### Các tính năng đã có:
- ✅ Đăng ký tài khoản mới
- ✅ Đăng nhập
- ✅ Quên mật khẩu
- ✅ Lưu dữ liệu vào MySQL
- ✅ Mã hóa mật khẩu an toàn (password_hash)
- ✅ Kiểm tra email/username trùng lặp
- ✅ Validation đầy đủ

---

## 🚀 CÁCH ĐĂNG KÝ TÀI KHOẢN

### Bước 1: Khởi động XAMPP
1. Mở **XAMPP Control Panel**
2. Nhấn nút **Start** bên cạnh **Apache**
3. Nhấn nút **Start** bên cạnh **MySQL**
4. Đợi đến khi cả 2 đều hiển thị màu xanh

### Bước 2: Tạo Database (chỉ làm 1 lần)
1. Mở trình duyệt, truy cập: `http://localhost/phpmyadmin`
2. Nhấn vào tab **SQL** ở trên cùng
3. Copy toàn bộ nội dung file `backend/database.sql`
4. Paste vào ô SQL và nhấn **Go**
5. Database `htd_gym` sẽ được tạo với tất cả bảng

### Bước 3: Copy Backend vào XAMPP
1. Copy thư mục `backend` từ project
2. Paste vào: `C:\xampp\htdocs\htd_gym\`
3. Kết quả: `C:\xampp\htdocs\htd_gym\backend\`

### Bước 4: Test Backend
1. Mở trình duyệt: `http://localhost/htd_gym/backend/api/auth.php`
2. Nếu thấy: `{"success":false,"message":"Action required"}` → **THÀNH CÔNG!**
3. Nếu lỗi kết nối → Kiểm tra lại MySQL đã start chưa

### Bước 5: Chạy App Android
1. Mở Android Studio
2. Chạy app trên **Emulator** (khuyến nghị)
3. Hoặc chạy trên **thiết bị thật** (cần đổi IP trong ApiConfig.java)

### Bước 6: Đăng ký tài khoản
1. Mở app HTD GYM
2. Nhấn vào tab **ĐĂNG KÝ**
3. Điền thông tin:
   - **Họ tên**: Nguyễn Văn A
   - **Email**: test@gmail.com
   - **Mật khẩu**: 123456 (tối thiểu 6 ký tự)
   - **Xác nhận mật khẩu**: 123456
4. Nhấn nút **ĐĂNG KÝ**
5. Nếu thành công → Tự động đăng nhập và vào trang chủ

---

## 🔍 KIỂM TRA DỮ LIỆU TRONG MYSQL

### Cách 1: Qua phpMyAdmin
1. Truy cập: `http://localhost/phpmyadmin`
2. Chọn database **htd_gym** bên trái
3. Nhấn vào bảng **users**
4. Xem danh sách tài khoản đã đăng ký

### Cách 2: Qua SQL
```sql
SELECT * FROM users ORDER BY created_at DESC;
```

Bạn sẽ thấy:
- `id`: ID tự động tăng
- `username`: Email đã nhập
- `email`: Email đã nhập
- `password`: Mật khẩu đã được mã hóa (hash)
- `full_name`: Họ tên
- `created_at`: Thời gian đăng ký

---

## 🎯 LUỒNG ĐĂNG KÝ CHI TIẾT

### 1. Người dùng nhập thông tin
```
Họ tên: Nguyễn Văn A
Email: test@gmail.com
Mật khẩu: 123456
```

### 2. App kiểm tra validation
- ✅ Họ tên không được để trống
- ✅ Email phải đúng định dạng
- ✅ Mật khẩu tối thiểu 6 ký tự
- ✅ Mật khẩu xác nhận phải khớp

### 3. App gửi request đến API
```json
POST http://10.0.2.2/htd_gym/backend/api/auth.php
{
  "action": "register",
  "full_name": "Nguyễn Văn A",
  "username": "test@gmail.com",
  "email": "test@gmail.com",
  "password": "123456"
}
```

### 4. Backend xử lý
- Kiểm tra email đã tồn tại chưa
- Mã hóa mật khẩu bằng `password_hash()`
- Lưu vào database MySQL
- Trả về thông tin user

### 5. App nhận response
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "id": 1,
    "username": "test@gmail.com",
    "email": "test@gmail.com",
    "full_name": "Nguyễn Văn A",
    "created_at": "2024-01-15 10:30:00"
  }
}
```

### 6. App lưu session
- Lưu thông tin user vào SharedPreferences
- Đánh dấu `is_logged_in = true`
- Chuyển đến MainActivity

---

## ❌ XỬ LÝ LỖI THƯỜNG GẶP

### Lỗi 1: "Lỗi kết nối: Unable to resolve host"
**Nguyên nhân**: MySQL chưa được start hoặc backend chưa copy đúng chỗ

**Giải pháp**:
1. Mở XAMPP Control Panel
2. Nhấn **Start** cho Apache và MySQL
3. Kiểm tra: `http://localhost/htd_gym/backend/api/auth.php`

### Lỗi 2: "Email already exists"
**Nguyên nhân**: Email đã được đăng ký trước đó

**Giải pháp**:
- Dùng email khác
- Hoặc xóa user cũ trong phpMyAdmin

### Lỗi 3: "Connection failed"
**Nguyên nhân**: Database chưa được tạo

**Giải pháp**:
1. Truy cập phpMyAdmin
2. Chạy file `backend/database.sql`

### Lỗi 4: App không kết nối được (thiết bị thật)
**Nguyên nhân**: IP không đúng

**Giải pháp**:
1. Mở Command Prompt, gõ: `ipconfig`
2. Tìm **IPv4 Address** (vd: 192.168.1.100)
3. Sửa file `ApiConfig.java`:
```java
public static final String BASE_URL = "http://192.168.1.100/htd_gym/backend/api/";
```

---

## 📊 CẤU TRÚC DATABASE

### Bảng `users`
```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    profile_image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Dữ liệu mẫu sau khi đăng ký
| id | username | email | password | full_name | created_at |
|----|----------|-------|----------|-----------|------------|
| 1 | test@gmail.com | test@gmail.com | $2y$10$... | Nguyễn Văn A | 2024-01-15 10:30:00 |

---

## 🔐 BẢO MẬT

### Mật khẩu được mã hóa
- Sử dụng `password_hash()` của PHP
- Algorithm: bcrypt (mặc định)
- Không thể giải mã ngược lại
- Xác thực bằng `password_verify()`

### Ví dụ:
```
Mật khẩu gốc: 123456
Mật khẩu hash: $2y$10$abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOP
```

---

## 📱 TEST CASES

### Test 1: Đăng ký thành công
```
Input:
- Họ tên: Test User
- Email: test1@gmail.com
- Mật khẩu: 123456
- Xác nhận: 123456

Expected: 
- Toast: "Đăng ký thành công!"
- Chuyển đến MainActivity
- Dữ liệu lưu trong MySQL
```

### Test 2: Email trùng lặp
```
Input:
- Email: test1@gmail.com (đã tồn tại)

Expected:
- Toast: "Email already exists"
```

### Test 3: Mật khẩu không khớp
```
Input:
- Mật khẩu: 123456
- Xác nhận: 654321

Expected:
- Error: "Mật khẩu không khớp"
```

### Test 4: Email không hợp lệ
```
Input:
- Email: test@

Expected:
- Error: "Email không hợp lệ"
```

---

## 🎉 KẾT LUẬN

Hệ thống đăng ký đã hoàn chỉnh và sẵn sàng sử dụng!

### Checklist:
- [x] Backend API hoàn chỉnh
- [x] Database MySQL setup
- [x] Android app kết nối API
- [x] Validation đầy đủ
- [x] Mã hóa mật khẩu
- [x] Lưu session
- [x] Xử lý lỗi

### Bước tiếp theo:
1. Start XAMPP (Apache + MySQL)
2. Copy backend vào htdocs
3. Chạy app và test đăng ký
4. Kiểm tra dữ liệu trong phpMyAdmin

**Chúc bạn thành công! 🚀**
