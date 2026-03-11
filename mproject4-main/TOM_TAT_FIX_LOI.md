# 🔧 TÓM TẮT FIX LỖI ĐĂNG KÝ

## ❓ VẤN ĐỀ

Bạn không thể đăng ký tài khoản trong app HTD GYM và muốn dữ liệu được lưu vào MySQL.

---

## ✅ GIẢI PHÁP

Hệ thống đăng ký đã được cài đặt hoàn chỉnh! Tất cả code đã sẵn sàng.

### Những gì đã có:
1. ✅ Backend API hoàn chỉnh (`backend/api/auth.php`)
2. ✅ Android app có form đăng ký (`LoginActivity.java`)
3. ✅ Kết nối MySQL đã setup (`ApiClient.java`, `ApiConfig.java`)
4. ✅ Database schema đã có (`backend/database.sql`)
5. ✅ Validation đầy đủ
6. ✅ Mã hóa mật khẩu an toàn

### Vấn đề duy nhất:
**MySQL chưa được start trong XAMPP!**

---

## 🚀 CÁCH FIX (3 BƯỚC)

### Bước 1: Start MySQL
```
1. Mở XAMPP Control Panel
2. Nhấn nút "Start" bên cạnh MySQL
3. Đợi đến khi MySQL hiển thị màu xanh
```

### Bước 2: Tạo Database
```
1. Mở trình duyệt: http://localhost/phpmyadmin
2. Nhấn tab "SQL"
3. Copy toàn bộ nội dung file: backend/database.sql
4. Paste vào ô SQL và nhấn "Go"
```

### Bước 3: Test
```
1. Mở: http://localhost/htd_gym/backend/test_connection.php
2. Phải thấy tất cả ✅
3. Mở: http://localhost/htd_gym/backend/test_register.html
4. Đăng ký thử 1 tài khoản
5. Kiểm tra trong phpMyAdmin → Bảng users
```

---

## 📱 CÁCH ĐĂNG KÝ TRONG APP

### Từ Android App:
```
1. Chạy app HTD GYM
2. Nhấn tab "ĐĂNG KÝ" (giữa 3 tab: Đăng nhập | Đăng ký | Quên MK)
3. Điền thông tin:
   - Họ tên: Nguyễn Văn A
   - Email: test@gmail.com
   - Mật khẩu: 123456
   - Xác nhận: 123456
4. Nhấn nút "ĐĂNG KÝ"
5. Nếu thành công → Toast "Đăng ký thành công!"
6. Tự động đăng nhập và chuyển đến MainActivity
```

### Kiểm tra dữ liệu:
```
1. Mở: http://localhost/phpmyadmin
2. Chọn database: htd_gym
3. Nhấn vào bảng: users
4. Xem tài khoản vừa đăng ký
```

---

## 🔍 LUỒNG ĐĂNG KÝ

```
[App Android]
    ↓ Nhập thông tin
    ↓ Validation (email, password, etc.)
    ↓ Gửi POST request
    ↓
[Backend API: auth.php]
    ↓ Nhận request
    ↓ Kiểm tra email đã tồn tại chưa
    ↓ Mã hóa mật khẩu (password_hash)
    ↓ INSERT vào MySQL
    ↓ Trả về thông tin user
    ↓
[MySQL Database: htd_gym]
    ↓ Lưu vào bảng users
    ↓ id, username, email, password (hash), full_name, created_at
    ↓
[App Android]
    ↓ Nhận response
    ↓ Lưu session (SharedPreferences)
    ↓ Chuyển đến MainActivity
```

---

## 📂 FILES ĐÃ TẠO

### Backend:
- `backend/api/auth.php` - API đăng ký & đăng nhập
- `backend/test_connection.php` - Test kết nối MySQL
- `backend/test_register.html` - Test đăng ký từ web
- `backend/database.sql` - Script tạo database

### Android:
- `app/.../LoginActivity.java` - Form đăng ký/đăng nhập (3 tabs)
- `app/.../RegisterActivity.java` - Form đăng ký riêng
- `app/.../ApiClient.java` - HTTP client
- `app/.../ApiConfig.java` - Cấu hình API URL

### Tài liệu:
- `HUONG_DAN_DANG_KY.md` - Hướng dẫn chi tiết
- `QUICK_START.md` - Hướng dẫn nhanh
- `TOM_TAT_FIX_LOI.md` - File này

---

## 🎯 CHECKLIST

Để đăng ký thành công, bạn cần:

- [ ] XAMPP đã cài đặt
- [ ] Apache đã start (màu xanh)
- [ ] MySQL đã start (màu xanh) ← QUAN TRỌNG!
- [ ] Backend đã copy vào: `C:\xampp\htdocs\htd_gym\backend\`
- [ ] Database đã tạo (chạy database.sql)
- [ ] test_connection.php hiển thị tất cả ✅
- [ ] ApiConfig.java có đúng IP (10.0.2.2 cho emulator)

---

## ❌ LỖI THƯỜNG GẶP

### 1. "Lỗi kết nối: Unable to resolve host"
**Nguyên nhân**: MySQL chưa start
**Fix**: Mở XAMPP → Start MySQL

### 2. "Connection failed"
**Nguyên nhân**: Database chưa tạo
**Fix**: Chạy database.sql trong phpMyAdmin

### 3. "Email already exists"
**Nguyên nhân**: Email đã đăng ký
**Fix**: Dùng email khác hoặc xóa user cũ

### 4. App không kết nối (thiết bị thật)
**Nguyên nhân**: IP không đúng
**Fix**: 
```
1. Gõ ipconfig trong CMD
2. Tìm IPv4 Address (vd: 192.168.1.100)
3. Sửa ApiConfig.java:
   BASE_URL = "http://192.168.1.100/htd_gym/backend/api/"
```

---

## 🧪 TEST NHANH

### Test 1: Backend hoạt động?
```
http://localhost/htd_gym/backend/test_connection.php
→ Phải thấy tất cả ✅
```

### Test 2: Đăng ký từ web?
```
http://localhost/htd_gym/backend/test_register.html
→ Điền form → Nhấn ĐĂNG KÝ
→ Phải thấy "Đăng ký thành công!"
```

### Test 3: Dữ liệu trong MySQL?
```
http://localhost/phpmyadmin
→ Database: htd_gym
→ Bảng: users
→ Phải thấy tài khoản vừa tạo
```

### Test 4: App Android?
```
Chạy app → Tab ĐĂNG KÝ → Điền form → ĐĂNG KÝ
→ Phải vào được trang chủ
```

---

## 💡 LƯU Ý QUAN TRỌNG

1. **MySQL phải start trước khi test**
   - Không start MySQL = Không kết nối được

2. **Database phải tạo trước**
   - Chạy database.sql trong phpMyAdmin

3. **IP phải đúng**
   - Emulator: 10.0.2.2
   - Thiết bị thật: IP máy tính (ipconfig)

4. **Backend phải đúng chỗ**
   - `C:\xampp\htdocs\htd_gym\backend\`

5. **Mật khẩu tối thiểu 6 ký tự**
   - Validation sẽ báo lỗi nếu ngắn hơn

---

## 🎉 KẾT LUẬN

Hệ thống đã hoàn chỉnh 100%! 

Bạn chỉ cần:
1. Start MySQL trong XAMPP
2. Tạo database (chạy database.sql)
3. Chạy app và đăng ký

**Tất cả code đã sẵn sàng, chỉ cần MySQL chạy là OK!**

---

## 📞 HỖ TRỢ

Nếu vẫn gặp lỗi:
1. Đọc file `HUONG_DAN_DANG_KY.md` (hướng dẫn chi tiết)
2. Đọc file `QUICK_START.md` (hướng dẫn nhanh)
3. Chạy `test_connection.php` để debug
4. Kiểm tra XAMPP Control Panel (Apache + MySQL màu xanh)

**Chúc bạn thành công! 🚀**
