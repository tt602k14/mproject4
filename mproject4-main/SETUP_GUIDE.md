# Hướng dẫn cài đặt HTD GYM App

## ✅ Đã hoàn thành

1. ✅ Hệ thống đăng nhập/đăng xuất
2. ✅ Tích hợp Firebase Authentication
3. ✅ Đăng nhập bằng Google
4. ✅ Cấu trúc cho Facebook Login
5. ✅ SharedPreferences để lưu session
6. ✅ Model User và Exercise
7. ✅ Layout đăng nhập đẹp mắt

## 🔧 Cần hoàn thiện

### 1. Cấu hình Firebase

**Bước 1:** Tạo project Firebase
- Truy cập https://console.firebase.google.com/
- Tạo project mới tên "HTD GYM"
- Thêm Android app với package name: `com.htdgym.app`

**Bước 2:** Download google-services.json
- Sau khi tạo app, download file `google-services.json`
- Thay thế file `app/google-services.json` hiện tại

**Bước 3:** Enable Authentication
- Vào Firebase Console → Authentication
- Enable Email/Password
- Enable Google Sign-In
- Copy Web client ID và paste vào `strings.xml`

### 2. Cấu hình Google Sign-In

Trong file `app/src/main/res/values/strings.xml`:
```xml
<string name="default_web_client_id">YOUR_ACTUAL_WEB_CLIENT_ID</string>
```

### 3. Cấu hình Facebook Login

**Bước 1:** Tạo Facebook App
- Truy cập https://developers.facebook.com/
- Tạo app mới
- Thêm Facebook Login product

**Bước 2:** Cập nhật strings.xml
```xml
<string name="facebook_app_id">YOUR_FACEBOOK_APP_ID</string>
```

**Bước 3:** Cập nhật AndroidManifest.xml
Thêm vào trong `<application>`:
```xml
<meta-data
    android:name="com.facebook.sdk.ApplicationId"
    android:value="@string/facebook_app_id"/>
```

### 4. Các Activity cần tạo thêm

Tôi đã tạo LoginActivity. Bạn cần tạo thêm:

1. **RegisterActivity** - Đăng ký tài khoản mới
2. **ForgotPasswordActivity** - Quên mật khẩu
3. **ExerciseLibraryActivity** - Thư viện bài tập
4. **VideoPlayerActivity** - Xem video hướng dẫn
5. **WorkoutPlanActivity** - Lộ trình tập luyện
6. **QuickWorkoutActivity** - Tập nhanh (Toàn thân, Cardio, Bụng)

### 5. Chức năng cần implement

#### Thư viện bài tập
- Danh sách bài tập theo nhóm cơ (Tất cả, Ngực, Lưng, Chân, Tay, Bụng)
- Chi tiết bài tập với video hướng dẫn
- Lưu bài tập yêu thích

#### Video hướng dẫn
- Tích hợp ExoPlayer để phát video
- Danh sách video theo cấp độ (Cơ bản, Trung cấp, Nâng cao)
- Đánh dấu video đã xem

#### Lộ trình tập luyện
- Tạo lộ trình tập theo tuần
- Theo dõi tiến độ
- Tính toán calories đã đốt

#### Tập nhanh
- Workout templates có sẵn
- Timer cho mỗi bài tập
- Âm thanh hướng dẫn

### 6. Database Schema

Cần cập nhật GymDatabase.java để thêm:
```java
@Database(
    entities = {
        Member.class, 
        Workout.class, 
        Payment.class, 
        Equipment.class, 
        Trainer.class,
        User.class,        // ← Thêm
        Exercise.class     // ← Thêm
    },
    version = 2,  // ← Tăng version
    exportSchema = false
)
```

### 7. Permissions cần thêm vào AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### 8. Sample Data cho Exercise

Tạo file để insert sample exercises:
- Bench Press (Ngực)
- Squat (Chân)
- Deadlift (Lưng)
- Pull-up (Lưng)
- Push-up (Ngực)
- Plank (Bụng)
- Bicep Curl (Tay)
- Leg Press (Chân)

## 📱 Cấu trúc App hoàn chỉnh

```
HTD GYM
├── Đăng nhập/Đăng ký
├── Trang chủ (Dashboard)
├── Thư viện bài tập
│   ├── Tất cả
│   ├── Ngực
│   ├── Lưng
│   ├── Chân
│   ├── Tay
│   └── Bụng
├── Tập nhanh
│   ├── Toàn thân
│   ├── Cardio
│   └── Bụng
├── Lộ trình tập
│   ├── Xem lộ trình
│   └── Tạo lộ trình mới
├── Video hướng dẫn
│   ├── Cơ bản
│   ├── Trung cấp
│   └── Nâng cao
├── Quản lý (Admin)
│   ├── Thành viên
│   ├── Thanh toán
│   ├── Thiết bị
│   └── Huấn luyện viên
└── Cài đặt
    ├── Hồ sơ
    ├── Thông báo
    └── Đăng xuất
```

## 🚀 Build và Run

1. Sync Gradle files
2. Đảm bảo đã cấu hình Firebase
3. Run app trên emulator hoặc device thật
4. Test đăng nhập với email/password
5. Test Google Sign-In

## 📝 Notes

- File `google-services.json` hiện tại chỉ là template
- Cần thay thế bằng file thật từ Firebase Console
- Facebook Login cần cấu hình thêm trong Facebook Developer Console
- Apple Sign-In chỉ hoạt động trên iOS

## 🎯 Next Steps

1. Hoàn thiện Firebase setup
2. Tạo RegisterActivity và ForgotPasswordActivity
3. Implement Exercise Library với sample data
4. Tạo Video Player với ExoPlayer
5. Implement Workout Plans
6. Thêm AI Coach (optional - có thể dùng ChatGPT API)
7. Thêm Community features
8. Implement Push Notifications

Chúc bạn thành công! 💪
