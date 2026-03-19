# 🚨 FIX LỖI NGAY BÂY GIỜ - 3 BƯỚC

## Lỗi của bạn:
```
Value <br of type java.lang.String cannot be converted to JSONObject
```

**Nghĩa là:** Backend trả về HTML thay vì JSON
**Nguyên nhân:** MySQL chưa start hoặc database chưa tạo

---

## ✅ BƯỚC 1: START MYSQL (1 phút)

1. Mở **XAMPP Control Panel**
2. Tìm dòng **MySQL**
3. Nhấn nút **Start** bên cạnh MySQL
4. Đợi đến khi MySQL hiển thị **màu xanh**

**Chụp màn hình XAMPP cho tôi xem!**

---

## ✅ BƯỚC 2: TẠO DATABASE (2 phút)

### Cách 1: Dùng file kiểm tra (KHUYẾN NGHỊ)
1. Mở trình duyệt
2. Vào: **http://localhost/htd_gym/backend/check_mysql.php**
3. Làm theo hướng dẫn trên trang

### Cách 2: Thủ công
1. Mở: **http://localhost/phpmyadmin**
2. Nhấn tab **SQL** ở trên
3. Mở file: **backend/create_database_simple.sql**
4. Copy toàn bộ nội dung
5. Paste vào ô SQL
6. Nhấn nút **Go**
7. Phải thấy: "Database htd_gym created"

---

## ✅ BƯỚC 3: TEST (1 phút)

### Test 1: Kiểm tra hệ thống
```
http://localhost/htd_gym/backend/check_mysql.php
```
Phải thấy tất cả ✅

### Test 2: Đăng ký từ web
```
http://localhost/htd_gym/backend/test_register.html
```
Điền form và nhấn "ĐĂNG KÝ"
Phải thấy: "✅ Đăng ký thành công!"

### Test 3: Kiểm tra database
```
http://localhost/phpmyadmin
→ Database: htd_gym
→ Bảng: users
→ Phải thấy tài khoản vừa tạo
```

### Test 4: Đăng ký từ app
1. Chạy app Android
2. Mở LoginActivity
3. Tab "ĐĂNG KÝ"
4. Điền:
   - Họ tên: Test
   - Email: test999@gmail.com
   - Mật khẩu: 123456
   - Xác nhận: 123456
5. Nhấn "ĐĂNG KÝ"
6. **PHẢI VÀO ĐƯỢC TRANG CHỦ!**

---

## 📋 CHECKLIST

Làm theo thứ tự:

- [ ] XAMPP MySQL đã start (màu xanh)
- [ ] Truy cập được: http://localhost/phpmyadmin
- [ ] Chạy file: create_database_simple.sql
- [ ] Database "htd_gym" đã xuất hiện
- [ ] Bảng "users" đã có
- [ ] Test: http://localhost/htd_gym/backend/check_mysql.php → Tất cả ✅
- [ ] Test: http://localhost/htd_gym/backend/test_register.html → Đăng ký OK
- [ ] Kiểm tra phpMyAdmin → Có dữ liệu trong users
- [ ] Chạy app → Đăng ký → Vào được trang chủ

---

## ❌ NẾU VẪN LỖI

### Lỗi: Cannot connect to MySQL
**Fix:** MySQL chưa start → Mở XAMPP → Start MySQL

### Lỗi: Database not found
**Fix:** Chạy file create_database_simple.sql trong phpMyAdmin

### Lỗi: 404 Not Found
**Fix:** Backend chưa copy đúng chỗ
→ Copy vào: `C:\xampp\htdocs\htd_gym\backend\`

### Lỗi: Email already exists
**Fix:** Email đã đăng ký → Dùng email khác

---

## 📞 BÁO CÁO KẾT QUẢ

Sau khi làm xong, báo cho tôi:

1. **XAMPP MySQL có màu xanh không?** (Chụp màn hình)
2. **check_mysql.php hiển thị gì?** (Chụp màn hình)
3. **test_register.html đăng ký được không?** (Chụp màn hình)
4. **App đăng ký được không?** (Chụp màn hình lỗi nếu có)

**Tôi sẽ giúp bạn fix tiếp!**
