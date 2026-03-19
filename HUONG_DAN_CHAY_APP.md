# 🚀 HƯỚNG DẪN BUILD VÀ CHẠY APP HTD GYM

## 📋 YÊU CẦU HỆ THỐNG

### 1. Phần mềm cần cài đặt:
- ✅ **Android Studio** (phiên bản mới nhất - Arctic Fox trở lên)
- ✅ **JDK 11** hoặc cao hơn
- ✅ **Android SDK** (API Level 21-34)
- ✅ **Git** (để clone project)

### 2. Thiết bị test:
- **Android Emulator** (API 21+) HOẶC
- **Điện thoại Android thật** (Android 5.0+)

---

## 🔧 BƯỚC 1: CÀI ĐẶT ANDROID STUDIO

### Windows:
1. Download Android Studio: https://developer.android.com/studio
2. Chạy file `.exe` và làm theo hướng dẫn
3. Chọn "Standard Installation"
4. Đợi download SDK và tools

### Mac:
1. Download Android Studio `.dmg`
2. Kéo vào thư mục Applications
3. Mở và làm theo hướng dẫn setup

### Linux:
```bash
sudo snap install android-studio --classic
```

---

## 📂 BƯỚC 2: MỞ PROJECT

### Cách 1: Mở project có sẵn
```bash
# Nếu đã có project trong máy
1. Mở Android Studio
2. File → Open
3. Chọn thư mục project (chứa file build.gradle)
4. Click OK
5. Đợi Gradle sync xong
```

### Cách 2: Clone từ Git (nếu có)
```bash
# Mở Terminal/Command Prompt
git clone <repository-url>
cd <project-folder>

# Mở Android Studio
# File → Open → Chọn thư mục vừa clone
```

---

## ⚙️ BƯỚC 3: SYNC GRADLE

Sau khi mở project:

1. Android Studio sẽ tự động sync Gradle
2. Nếu không tự động, click: **File → Sync Project with Gradle Files**
3. Đợi download dependencies (có thể mất 5-10 phút lần đầu)

### Nếu gặp lỗi Gradle:
```bash
# Trong Android Studio Terminal:
./gradlew clean
./gradlew build
```

---

## 📱 BƯỚC 4: SETUP EMULATOR (Nếu không có điện thoại thật)

### Tạo Android Virtual Device (AVD):

1. Click icon **AVD Manager** (hoặc Tools → AVD Manager)
2. Click **Create Virtual Device**
3. Chọn device: **Pixel 5** (recommended)
4. Chọn System Image: **API 30** (Android 11) hoặc cao hơn
5. Click **Download** nếu chưa có
6. Đặt tên AVD: "HTD_Gym_Test"
7. Click **Finish**

### Khởi động Emulator:
1. Trong AVD Manager, click ▶️ bên cạnh AVD vừa tạo
2. Đợi emulator khởi động (30-60 giây)

---

## 🔌 BƯỚC 5: SETUP ĐIỆN THOẠI THẬT (Tùy chọn)

### Bật USB Debugging:

1. **Vào Settings** → About Phone
2. **Tap 7 lần** vào "Build Number" để bật Developer Mode
3. **Quay lại Settings** → Developer Options
4. **Bật "USB Debugging"**
5. **Cắm USB** vào máy tính
6. **Chấp nhận** popup "Allow USB Debugging"

### Kiểm tra kết nối:
```bash
# Trong Android Studio Terminal:
adb devices

# Kết quả mong đợi:
# List of devices attached
# ABC123XYZ    device
```

---

## ▶️ BƯỚC 6: BUILD VÀ CHẠY APP

### Cách 1: Dùng nút Run (Đơn giản nhất)

1. Chọn device/emulator từ dropdown (góc trên bên phải)
2. Click nút **Run** ▶️ (màu xanh lá)
3. Đợi build xong (2-5 phút lần đầu)
4. App sẽ tự động cài và mở trên device

### Cách 2: Dùng Gradle Command

```bash
# Build debug APK
./gradlew assembleDebug

# Cài đặt lên device
./gradlew installDebug

# Build và chạy luôn
./gradlew installDebug && adb shell am start -n com.htdgym.app/.activities.LoginActivity
```

### Cách 3: Build APK để chia sẻ

```bash
# Build APK
./gradlew assembleDebug

# File APK sẽ ở:
# app/build/outputs/apk/debug/app-debug.apk

# Copy APK ra Desktop
cp app/build/outputs/apk/debug/app-debug.apk ~/Desktop/HTDGym.apk
```

---

## 🎯 BƯỚC 7: SỬ DỤNG APP

### Lần đầu mở app:

1. **Màn hình Login** sẽ hiện ra
2. **Nhập email/password bất kỳ** (ví dụ: test@gmail.com / 123456)
3. App sẽ **tự động tạo tài khoản** nếu chưa có
4. **Đăng nhập thành công** → Vào màn hình chính

### Các tính năng có thể test:

✅ **Home Tab**: Xem stats tổng quan
✅ **Workouts Tab**: Chọn bài tập và bắt đầu tập
✅ **Nutrition Tab**: Xem thực đơn và water tracker
✅ **Progress Tab**: Xem biểu đồ tiến độ
✅ **Community Tab**: Xem challenges và leaderboard

### Test SQLite Database:

1. Vào **Settings** (icon ⚙️ góc trên)
2. Click **"Lịch sử tập luyện"**
3. Xem **TestSQLiteActivity** với tất cả dữ liệu

---

## 🐛 XỬ LÝ LỖI THƯỜNG GẶP

### Lỗi 1: "Gradle sync failed"
```bash
# Giải pháp:
File → Invalidate Caches → Invalidate and Restart
```

### Lỗi 2: "SDK not found"
```bash
# Giải pháp:
File → Project Structure → SDK Location
→ Chọn đường dẫn Android SDK
```

### Lỗi 3: "Device not found"
```bash
# Kiểm tra ADB:
adb kill-server
adb start-server
adb devices
```

### Lỗi 4: "Build failed - Out of memory"
```bash
# Tăng memory cho Gradle
# Tạo/sửa file: gradle.properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=1024m
```

### Lỗi 5: "App crashes on startup"
```bash
# Xem log:
adb logcat | grep "htdgym"

# Hoặc trong Android Studio:
View → Tool Windows → Logcat
```

---

## 📊 KIỂM TRA APP HOẠT ĐỘNG

### Checklist sau khi chạy app:

- [ ] App mở được không crash
- [ ] Login screen hiển thị đúng
- [ ] Có thể đăng nhập với email/password bất kỳ
- [ ] Home screen hiển thị stats
- [ ] Bottom navigation hoạt động
- [ ] Có thể chuyển giữa các tabs
- [ ] Database có dữ liệu mẫu
- [ ] Workout session có thể bắt đầu
- [ ] Stats được lưu sau khi tập

---

## 🔍 DEBUG VÀ MONITOR

### Xem Logs:
```bash
# Trong Android Studio:
View → Tool Windows → Logcat

# Filter theo tag:
tag:DatabaseInitializer
tag:WorkoutDataHelper
tag:LoginActivity
```

### Xem Database:
```bash
# Trong Android Studio:
View → Tool Windows → App Inspection → Database Inspector

# Hoặc dùng ADB:
adb shell
run-as com.htdgym.app
cd databases
sqlite3 gym_database
.tables
SELECT * FROM users;
```

### Monitor Performance:
```bash
# CPU/Memory usage:
View → Tool Windows → Profiler
```

---

## 📦 BUILD APK RELEASE (Để phát hành)

### Tạo Keystore (Lần đầu):
```bash
keytool -genkey -v -keystore htdgym-release.keystore -alias htdgym -keyalg RSA -keysize 2048 -validity 10000
```

### Build Signed APK:
1. **Build → Generate Signed Bundle/APK**
2. Chọn **APK**
3. Chọn keystore file
4. Nhập password
5. Chọn **release** build variant
6. Click **Finish**

APK sẽ ở: `app/release/app-release.apk`

---

## 🚀 CHẠY APP NHANH (TÓM TẮT)

```bash
# 1. Mở Android Studio
# 2. Open project
# 3. Đợi Gradle sync
# 4. Chọn device/emulator
# 5. Click Run ▶️
# 6. Đợi build và cài đặt
# 7. App tự động mở
# 8. Login với email/password bất kỳ
# 9. Enjoy! 🎉
```

---

## 📞 HỖ TRỢ

### Nếu gặp vấn đề:

1. **Xem Logcat** để tìm lỗi cụ thể
2. **Clean và Rebuild**: Build → Clean Project → Rebuild Project
3. **Invalidate Caches**: File → Invalidate Caches → Restart
4. **Update Android Studio** lên phiên bản mới nhất
5. **Kiểm tra internet** (để download dependencies)

### Các file quan trọng:

- `app/build.gradle` - Dependencies và config
- `app/src/main/AndroidManifest.xml` - App permissions
- `app/src/main/java/com/htdgym/app/` - Source code
- `app/src/main/res/` - Resources (layouts, drawables)

---

## ✅ HOÀN THÀNH!

App HTD Gym giờ đã sẵn sàng chạy trên thiết bị của bạn!

**Các tính năng đã có:**
- ✅ SQLite Database với dữ liệu mẫu
- ✅ Login/Authentication
- ✅ Workout tracking và timer
- ✅ Nutrition planning
- ✅ Progress tracking với charts
- ✅ AI Coach chatbot
- ✅ BMI Calculator
- ✅ Video library
- ✅ Challenges system
- ✅ Real-time stats updates

**Enjoy your workout! 💪🏋️‍♂️**
