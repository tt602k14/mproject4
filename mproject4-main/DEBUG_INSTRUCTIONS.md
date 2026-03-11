# 🔍 HƯỚNG DẪN DEBUG LỖI ĐĂNG KÝ

## 📋 KIỂM TRA TỪNG BƯỚC

### Bước 1: Xem LogCat
```
Android Studio → Logcat
Filter: LoginActivity
```

Tìm các dòng log:
- `registerUser: Starting registration process`
- `registerUser: Password hashed`
- `registerUser: Checking if email exists`
- `registerUser: Creating new user`
- `registerUser: User created successfully with ID: X`
- `saveUserSession: Saving session`
- `navigateToMain: Starting MainActivity`

**Nếu thấy lỗi ở đâu, chụp màn hình gửi tôi!**

### Bước 2: Kiểm tra lỗi cụ thể

**Lỗi 1: "Error navigating to MainActivity"**
→ MainActivity không tồn tại hoặc crash

**Lỗi 2: "Error during registration"**
→ Database có vấn đề

**Lỗi 3: "Email đã được sử dụng"**
→ Email đã tồn tại trong database

### Bước 3: Test đơn giản

1. **Xóa app và cài lại**
   ```
   Uninstall app từ device/emulator
   Run lại từ Android Studio
   ```

2. **Thử email khác**
   ```
   Email: test123@gmail.com
   Password: 123456
   ```

3. **Kiểm tra database**
   ```
   Android Studio → App Inspection → Database Inspector
   → gym_database → users
   ```

---

## 🔧 FIX NHANH

Nếu vẫn lỗi, làm theo:

### Fix 1: Xóa database cũ
```java
// Trong LoginActivity onCreate, thêm dòng này (tạm thời):
getApplicationContext().deleteDatabase("gym_database");
```

### Fix 2: Kiểm tra MainActivity
```java
// Mở MainActivity.java
// Kiểm tra onCreate() có lỗi không
```

### Fix 3: Simplify navigation
```java
// Thay thế navigateToMain() bằng:
private void navigateToMain() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
}
```

---

## 📞 GỬI CHO TÔI

Chụp màn hình:
1. **LogCat** (filter: LoginActivity)
2. **Lỗi hiển thị trên app** (Toast hoặc crash)
3. **Database Inspector** (bảng users)

Tôi sẽ fix ngay!
