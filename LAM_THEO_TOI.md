# 🚀 LÀM THEO TÔI - 5 PHÚT XONG!

## BƯỚC 1: START XAMPP (30 giây)

1. Mở **XAMPP Control Panel**
2. Nhấn **Start** cho **Apache**
3. Nhấn **Start** cho **MySQL**
4. Đợi cả 2 **màu xanh**

✅ Xong bước 1!

---

## BƯỚC 2: COPY BACKEND (30 giây)

1. Tìm file: **COPY_BACKEND.bat** (trong thư mục project)
2. **Click đúp** vào file đó
3. Đợi nó copy xong
4. Nhấn phím bất kỳ để đóng

✅ Xong bước 2!

---

## BƯỚC 3: TẠO DATABASE (1 phút)

1. Mở trình duyệt
2. Vào: **http://localhost/phpmyadmin**
3. Nhấn tab **SQL** (ở trên cùng)
4. Mở file: **backend/database.sql** (bằng Notepad)
5. **Ctrl+A** (chọn tất cả) → **Ctrl+C** (copy)
6. Quay lại trình duyệt
7. **Ctrl+V** (paste vào ô SQL)
8. Nhấn nút **Go** (bên dưới)
9. Đợi 2-3 giây
10. Phải thấy: "Query OK" hoặc "Database created"

✅ Xong bước 3!

---

## BƯỚC 4: KIỂM TRA (30 giây)

Mở trình duyệt, vào:
```
http://localhost/htd_gym/backend/check_mysql.php
```

**Phải thấy:**
- ✅ Test 1: PHP Version
- ✅ Test 2: MySQL Extension
- ✅ Test 3: Kết nối MySQL
- ✅ Test 4: Database "htd_gym"
- ✅ Test 5: Bảng Users
- ✅ Test 6: API Endpoint

**Nếu thấy tất cả ✅ → Hoàn hảo!**

**Nếu có ❌ → Chụp màn hình gửi cho tôi!**

✅ Xong bước 4!

---

## BƯỚC 5: TEST ĐĂNG KÝ TỪ WEB (1 phút)

Mở trình duyệt, vào:
```
http://localhost/htd_gym/backend/test_register.html
```

**Điền form:**
- Họ tên: **Test User**
- Email: **test123@gmail.com**
- Số điện thoại: **0123456789** (không bắt buộc)
- Mật khẩu: **123456**

**Nhấn nút: ĐĂNG KÝ**

**Phải thấy:**
```
✅ Đăng ký thành công! Tài khoản đã được lưu vào MySQL.
```

**Và phải thấy JSON response:**
```json
{
  "success": true,
  "message": "Đăng ký thành công",
  "data": {
    "id": 2,
    "username": "test123@gmail.com",
    "email": "test123@gmail.com",
    "full_name": "Test User",
    ...
  }
}
```

✅ Xong bước 5!

---

## BƯỚC 6: KIỂM TRA DATABASE (30 giây)

Mở trình duyệt, vào:
```
http://localhost/phpmyadmin
```

1. Nhấn vào database: **htd_gym** (bên trái)
2. Nhấn vào bảng: **users**
3. Phải thấy 2 users:
   - admin@htdgym.com
   - test123@gmail.com

✅ Xong bước 6!

---

## BƯỚC 7: CHẠY APP ANDROID (2 phút)

### 7.1. Kiểm tra IP trong ApiConfig.java

Mở file: **app/src/main/java/com/htdgym/app/api/ApiConfig.java**

**Nếu dùng Emulator:**
```java
public static final String BASE_URL = "http://10.0.2.2/htd_gym/backend/api/";
```

**Nếu dùng thiết bị thật:**
1. Mở CMD
2. Gõ: `ipconfig`
3. Tìm "IPv4 Address" (ví dụ: 192.168.1.100)
4. Sửa:
```java
public static final String BASE_URL = "http://192.168.1.100/htd_gym/backend/api/";
```

### 7.2. Build và chạy app

1. Android Studio → **Run App**
2. Đợi app chạy

✅ Xong bước 7!

---

## BƯỚC 8: ĐĂNG KÝ TỪ APP (1 phút)

### 8.1. Mở LoginActivity

**Cách 1:** Từ WelcomeActivity
- Nhấn "BẮT ĐẦU" → MainActivity
- Mở menu → Tìm "Đăng nhập"

**Cách 2:** Từ SplashActivity
- App tự động mở LoginActivity nếu chưa đăng nhập

### 8.2. Đăng ký tài khoản

1. Nhấn tab **ĐĂNG KÝ** (giữa 3 tab)
2. Điền form:
   - Họ tên: **Hung**
   - Email: **hung@gmail.com**
   - Mật khẩu: **123456**
   - Xác nhận: **123456**
3. Nhấn nút: **ĐĂNG KÝ TÀI KHOẢN**

**Phải thấy:**
- Toast: "Đăng ký thành công!"
- Tự động chuyển đến **MainActivity** (trang chủ)

✅ Xong bước 8!

---

## BƯỚC 9: KIỂM TRA DỮ LIỆU (30 giây)

Quay lại trình duyệt:
```
http://localhost/phpmyadmin
→ Database: htd_gym
→ Bảng: users
```

**Phải thấy 3 users:**
1. admin@htdgym.com
2. test123@gmail.com
3. hung@gmail.com ← **MỚI ĐĂNG KÝ TỪ APP!**

✅ Xong bước 9!

---

## BƯỚC 10: TEST ĐĂNG NHẬP (30 giây)

1. **Đóng app** (force close)
2. **Mở lại app**
3. Phải **tự động vào MainActivity** (không cần đăng nhập lại)

**Hoặc:**

1. Trong app, logout (nếu có)
2. Mở LoginActivity
3. Tab **ĐĂNG NHẬP**
4. Điền:
   - Email: **hung@gmail.com**
   - Mật khẩu: **123456**
5. Nhấn **ĐĂNG NHẬP**
6. Phải vào **MainActivity**

✅ Xong bước 10!

---

## 🎉 HOÀN THÀNH!

Nếu tất cả 10 bước trên OK:
- ✅ Backend hoạt động
- ✅ MySQL kết nối
- ✅ Database đã tạo
- ✅ Đăng ký từ web OK
- ✅ Đăng ký từ app OK
- ✅ Dữ liệu lưu vào MySQL
- ✅ Đăng nhập vào MainActivity
- ✅ Session được lưu

**BẠN ĐÃ THÀNH CÔNG! 🚀**

---

## ❌ NẾU CÓ LỖI

### Lỗi ở bước nào?

**Bước 4:** check_mysql.php có ❌
→ Chụp màn hình gửi tôi

**Bước 5:** test_register.html lỗi
→ Chụp màn hình lỗi gửi tôi

**Bước 8:** App đăng ký lỗi
→ Chụp màn hình lỗi gửi tôi

**Bước 9:** Không thấy dữ liệu trong MySQL
→ Chụp màn hình phpMyAdmin gửi tôi

---

## 📞 HỖ TRỢ

Gửi cho tôi:
1. Screenshot XAMPP Control Panel (Apache + MySQL màu xanh)
2. Screenshot check_mysql.php
3. Screenshot phpMyAdmin → users table
4. Screenshot lỗi từ app (nếu có)

**Tôi sẽ giúp bạn fix ngay! 💪**
