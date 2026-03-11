# 🎯 HƯỚNG DẪN HOÀN CHỈNH - ĐĂNG KÝ/ĐĂNG NHẬP VỚI SQLITE

## ✅ HỆ THỐNG ĐÃ XONG!

Tôi đã làm xong hệ thống đăng ký/đăng nhập với SQLite:
- ✅ Đăng ký → Lưu vào SQLite
- ✅ Đăng nhập → Kiểm tra từ SQLite
- ✅ Password được hash (SHA-256)
- ✅ Session được lưu
- ✅ Tự động vào MainActivity sau đăng ký/đăng nhập

---

## 📊 LUỒNG HOẠT ĐỘNG

### Đăng ký:
```
[LoginActivity - Tab ĐĂNG KÝ]
    ↓
User điền form:
- Họ tên: Hung
- Email: hung@gmail.com
- Mật khẩu: 123456
- Xác nhận: 123456
    ↓
Validation (email hợp lệ, password >= 6 ký tự, password khớp)
    ↓
Hash password (SHA-256)
    ↓
Kiểm tra email đã tồn tại chưa
    ↓
INSERT INTO users (email, password, name, loginType)
    ↓
[SQLite Database]
    ↓
Lưu session vào SharedPreferences:
- user_id
- username
- email
- full_name
- is_logged_in = true
    ↓
Toast: "Đăng ký thành công!"
    ↓
Intent → MainActivity
    ↓
✅ XONG!
```

### Đăng nhập:
```
[LoginActivity - Tab ĐĂNG NHẬP]
    ↓
User điền form:
- Email: hung@gmail.com
- Mật khẩu: 123456
    ↓
Validation
    ↓
Hash password (SHA-256)
    ↓
SELECT * FROM users WHERE email = ? AND password = ?
    ↓
[SQLite Database]
    ↓
Nếu tìm thấy user:
    ↓
Lưu session vào SharedPreferences
    ↓
Toast: "Đăng nhập thành công!"
    ↓
Intent → MainActivity
    ↓
✅ XONG!
```

---

## 🗄️ CẤU TRÚC DATABASE

### Bảng users trong SQLite:
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    name TEXT,
    phone TEXT,
    profileImage TEXT,
    loginType TEXT,
    firebaseUid TEXT
)
```

### Dữ liệu mẫu sau khi đăng ký:
| id | email | password | name | loginType |
|----|-------|----------|------|-----------|
| 1 | hung@gmail.com | 8d969eef6ecad3c29a3a629280e686cf... | Hung | email |
| 2 | test@gmail.com | 8d969eef6ecad3c29a3a629280e686cf... | Test | email |

**Lưu ý:** Password là SHA-256 hash, không thể giải mã ngược!

---

## 🚀 CÁCH SỬ DỤNG

### Bước 1: Build Project
```
Android Studio → Build → Clean Project
Android Studio → Build → Rebuild Project
```

### Bước 2: Chạy App
```
Android Studio → Run 'app' (Shift + F10)
```

### Bước 3: Đăng Ký Tài Khoản

1. App mở → LoginActivity
2. Nhấn tab **"ĐĂNG KÝ"** (giữa 3 tab)
3. Điền thông tin:
   ```
   Họ tên: Hung
   Email: hung@gmail.com
   Mật khẩu: 123456
   Xác nhận mật khẩu: 123456
   ```
4. Nhấn nút **"ĐĂNG KÝ TÀI KHOẢN"**
5. Thấy Toast: **"Đăng ký thành công!"**
6. Tự động chuyển đến **MainActivity** (trang chủ)

### Bước 4: Kiểm Tra Dữ Liệu

**Cách 1: Dùng Android Studio Database Inspector**
```
1. Chạy app trên emulator/device
2. Android Studio → View → Tool Windows → App Inspection
3. Tab "Database Inspector"
4. Chọn app: com.htdgym.app
5. Chọn database: gym_database
6. Xem bảng: users
7. Phải thấy user "hung@gmail.com"
```

**Cách 2: Dùng ADB**
```bash
adb shell
cd /data/data/com.htdgym.app/databases/
sqlite3 gym_database
SELECT * FROM users;
.exit
```

### Bước 5: Test Đăng Nhập

1. Đóng app (force close)
2. Mở lại app
3. **Tự động vào MainActivity** (vì session đã lưu)

**Hoặc test logout:**
1. Logout từ app (nếu có chức năng)
2. Quay lại LoginActivity
3. Tab **"ĐĂNG NHẬP"**
4. Điền:
   ```
   Email: hung@gmail.com
   Mật khẩu: 123456
   ```
5. Nhấn **"ĐĂNG NHẬP"**
6. Thấy Toast: **"Đăng nhập thành công!"**
7. Vào **MainActivity**

---

## 🔐 BẢO MẬT

### Password Hashing (SHA-256)
```java
Input:  "123456"
Output: "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"
```

- Password không bao giờ lưu dạng plain text
- Dùng SHA-256 để hash
- Không thể giải mã ngược
- So sánh hash khi đăng nhập

### Session Management
```java
SharedPreferences: HTDGymPrefs
- user_id: 1
- username: hung@gmail.com
- email: hung@gmail.com
- full_name: Hung
- is_logged_in: true
```

---

## 📱 CODE ĐÃ HOÀN CHỈNH

### LoginActivity.java
- ✅ 3 tabs: Đăng nhập, Đăng ký, Quên mật khẩu
- ✅ Validation đầy đủ
- ✅ Password hashing
- ✅ SQLite queries (Room Database)
- ✅ Background thread (ExecutorService)
- ✅ Error handling
- ✅ Logging (LogCat)
- ✅ Session management
- ✅ Navigation to MainActivity

### GymDatabase.java
- ✅ Room Database
- ✅ 9 entities (User, Member, Workout, Exercise, etc.)
- ✅ Singleton pattern
- ✅ Type converters

### UserDao.java
- ✅ getUserByEmail(email)
- ✅ login(email, password)
- ✅ insertUser(user)
- ✅ updateUser(user)
- ✅ deleteUser(user)

### User.java
- ✅ Entity model
- ✅ All fields (id, email, password, name, phone, etc.)
- ✅ Getters/Setters

---

## 🧪 TEST CASES

### Test 1: Đăng ký thành công
```
Input:
- Họ tên: Test User
- Email: test@gmail.com
- Mật khẩu: 123456
- Xác nhận: 123456

Expected:
- Toast: "Đăng ký thành công!"
- Vào MainActivity
- Database có user mới
- Session được lưu
```

### Test 2: Đăng ký email trùng
```
Input:
- Email: test@gmail.com (đã tồn tại)

Expected:
- Toast: "Email đã được sử dụng!"
- Không vào MainActivity
```

### Test 3: Đăng ký password không khớp
```
Input:
- Mật khẩu: 123456
- Xác nhận: 654321

Expected:
- Error: "Mật khẩu không khớp"
```

### Test 4: Đăng nhập thành công
```
Input:
- Email: test@gmail.com
- Mật khẩu: 123456

Expected:
- Toast: "Đăng nhập thành công!"
- Vào MainActivity
- Session được lưu
```

### Test 5: Đăng nhập sai password
```
Input:
- Email: test@gmail.com
- Mật khẩu: 654321 (sai)

Expected:
- Toast: "Email hoặc mật khẩu không đúng!"
```

### Test 6: Session persistence
```
1. Đăng nhập
2. Đóng app
3. Mở lại app

Expected:
- Tự động vào MainActivity (không cần đăng nhập lại)
```

---

## 🔍 DEBUG

### Xem Log trong LogCat
```
Filter: LoginActivity
Tag: LoginActivity

Logs:
- onCreate: Starting LoginActivity
- loginUser: Starting login process
- loginUser: Password hashed
- loginUser: Querying database for user: hung@gmail.com
- loginUser: Login successful for user: hung@gmail.com
- saveUserSession: Saving session for user: hung@gmail.com
- navigateToMain: Starting MainActivity
```

### Kiểm tra database
```
Android Studio → App Inspection → Database Inspector
→ gym_database → users
```

---

## ❌ XỬ LÝ LỖI

### Lỗi: App crash khi đăng ký
**Kiểm tra:**
1. LogCat có lỗi gì?
2. Database đã khởi tạo chưa?
3. Room dependencies đã thêm chưa?

**Fix:**
```gradle
// build.gradle (app)
dependencies {
    implementation "androidx.room:room-runtime:2.5.0"
    annotationProcessor "androidx.room:room-compiler:2.5.0"
}
```

### Lỗi: Không vào MainActivity
**Kiểm tra:**
1. MainActivity có trong AndroidManifest.xml chưa?
2. Intent có đúng không?
3. LogCat có lỗi gì?

**Fix:**
```xml
<!-- AndroidManifest.xml -->
<activity
    android:name=".MainActivity"
    android:exported="false" />
```

### Lỗi: Email đã tồn tại nhưng vẫn insert
**Kiểm tra:**
1. UserDao.getUserByEmail() có hoạt động không?
2. Email có unique constraint không?

**Fix:** Đã có trong code, kiểm tra lại logic

---

## 💡 TÍNH NĂNG BỔ SUNG (TÙY CHỌN)

### 1. Logout
```java
// Trong MainActivity hoặc ProfileFragment
private void logout() {
    SharedPreferences prefs = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE);
    prefs.edit().clear().apply();
    
    Intent intent = new Intent(this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
}
```

### 2. Remember Me
```java
// Thêm checkbox trong LoginActivity
CheckBox cbRememberMe = findViewById(R.id.cb_remember_me);

// Khi đăng nhập
if (cbRememberMe.isChecked()) {
    prefs.edit().putBoolean("remember_me", true).apply();
}
```

### 3. Change Password
```java
// Trong SettingsActivity
private void changePassword(String oldPassword, String newPassword) {
    executorService.execute(() -> {
        User user = database.userDao().getUserByEmail(currentEmail);
        String oldHash = hashPassword(oldPassword);
        
        if (user.getPassword().equals(oldHash)) {
            user.setPassword(hashPassword(newPassword));
            database.userDao().updateUser(user);
            
            runOnUiThread(() -> {
                Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
            });
        }
    });
}
```

---

## 🎉 KẾT LUẬN

Hệ thống đăng ký/đăng nhập với SQLite đã hoàn chỉnh 100%!

### ✅ Đã có:
- Đăng ký tài khoản
- Đăng nhập
- Quên mật khẩu (kiểm tra email)
- Password hashing
- Session management
- Auto login
- Error handling
- Logging

### ✅ Ưu điểm:
- Không cần internet
- Không cần backend
- Không cần MySQL/XAMPP
- Nhanh (local database)
- Đơn giản (chỉ cần chạy app)
- An toàn (password hash)

### 🚀 Chạy ngay:
```
1. Build → Rebuild Project
2. Run App
3. Đăng ký tài khoản
4. Vào MainActivity
5. XONG!
```

**Hệ thống đã sẵn sàng! Chạy app và test ngay! 🎉**
