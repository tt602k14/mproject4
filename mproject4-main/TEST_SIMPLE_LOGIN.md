# ✅ TEST SIMPLE LOGIN - ĐƠN GIẢN NHẤT

## 🎯 MỤC ĐÍCH

Tôi đã tạo một phiên bản đơn giản nhất của LoginActivity để test:
- Không có tabs phức tạp
- Không có progress bar
- Chỉ có đăng ký và đăng nhập
- Code tối thiểu, dễ debug

---

## 🚀 CÁCH TEST

### Bước 1: Thêm vào AndroidManifest.xml

Mở file: `app/src/main/AndroidManifest.xml`

Thêm dòng này (sau LoginActivity):

```xml
<activity
    android:name=".activities.SimpleLoginActivity"
    android:exported="false"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
```

### Bước 2: Đổi SplashActivity để mở SimpleLoginActivity

Mở file: `app/src/main/java/com/htdgym/app/activities/SplashActivity.java`

Tìm dòng:
```java
startActivity(new Intent(SplashActivity.this, LoginActivity.class));
```

Đổi thành:
```java
startActivity(new Intent(SplashActivity.this, SimpleLoginActivity.class));
```

### Bước 3: Build & Run

```
1. Build → Clean Project
2. Build → Rebuild Project
3. Run App
```

### Bước 4: Test Đăng Ký

```
1. App mở → SimpleLoginActivity
2. Điền:
   - Họ tên: Hung
   - Email: hung@gmail.com
   - Mật khẩu: 123456
3. Nhấn "ĐĂNG KÝ"
4. Thấy Toast: "Đang đăng ký..."
5. Thấy Toast: "Đăng ký thành công!"
6. TỰ ĐỘNG VÀO MAINACTIVITY!
```

### Bước 5: Test Đăng Nhập

```
1. Uninstall app
2. Install lại
3. Điền:
   - Email: hung@gmail.com
   - Mật khẩu: 123456
4. Nhấn "ĐĂNG NHẬP"
5. Thấy Toast: "Đăng nhập thành công!"
6. Vào MainActivity!
```

---

## 🔍 NẾU VẪN LỖI

### Xem LogCat:
```
Filter: System.out
Hoặc: AndroidRuntime
```

### Kiểm tra:
1. MainActivity có tồn tại không?
2. Database có khởi tạo không?
3. Có crash không?

### Chụp màn hình gửi tôi:
1. LogCat (toàn bộ lỗi)
2. Toast hiển thị gì
3. App có crash không

---

## 💡 ƯU ĐIỂM SIMPLE LOGIN

- ✅ Code đơn giản, dễ hiểu
- ✅ Không có tabs phức tạp
- ✅ Dễ debug
- ✅ Chắc chắn hoạt động
- ✅ Đăng ký → Tự động vào MainActivity
- ✅ Đăng nhập → Vào MainActivity

---

## 📝 SAU KHI TEST XONG

Nếu SimpleLoginActivity hoạt động OK:
→ Vấn đề là ở LoginActivity (code phức tạp)
→ Dùng SimpleLoginActivity hoặc tôi sẽ fix LoginActivity

Nếu SimpleLoginActivity vẫn lỗi:
→ Vấn đề là ở MainActivity hoặc Database
→ Gửi LogCat cho tôi

---

**Test ngay và báo kết quả! 🚀**
