# Room Database Fix - Sửa lỗi Room Database

## Vấn đề hiện tại
Lỗi Room database khi đăng nhập admin có thể do:
1. Database schema conflicts
2. Missing DAOs
3. Version mismatch
4. Entity relationship issues

## Giải pháp đã áp dụng

### 1. Simplified Database Schema
```java
@Database(
    entities = {
        User.class,
        Admin.class,
        PremiumUser.class,
        PaymentTransaction.class,
        Exercise.class,
        FoodItem.class,
        MealLog.class,
        BodyMeasurement.class,
        WorkoutLog.class
    },
    version = 12,
    exportSchema = false
)
```

### 2. Enhanced Error Handling
- Added try-catch blocks in AdminManager
- Added database null checks
- Added detailed logging

### 3. Temporary Workaround
Nếu vẫn gặp lỗi Room, có thể sử dụng temporary bypass:

```java
// Trong AdminManager, thêm method test
public void testAdminLogin(String email, String password, AdminAuthCallback callback) {
    // Hardcoded test for admin@gmail.com / admin123
    if ("admin@gmail.com".equals(email) && "admin123".equals(password)) {
        Admin testAdmin = new Admin();
        testAdmin.setAdminId("TEST_ADMIN");
        testAdmin.setEmail(email);
        testAdmin.setFullName("HTD Gym Admin");
        testAdmin.setRole("super_admin");
        testAdmin.setActive(true);
        
        // Save session
        currentAdmin = testAdmin;
        prefs.edit()
            .putString(KEY_CURRENT_ADMIN_ID, testAdmin.getAdminId())
            .putBoolean(KEY_IS_ADMIN_LOGGED_IN, true)
            .apply();
            
        callback.onSuccess(testAdmin);
    } else {
        callback.onError("Tài khoản test không đúng");
    }
}
```

## Cách khắc phục

### Bước 1: Clear App Data
```bash
adb shell pm clear com.htdgym.app
```

### Bước 2: Rebuild Project
```bash
./gradlew clean
./gradlew assembleDebug
```

### Bước 3: Test với logs
```bash
adb logcat | grep -E "(AdminManager|GymDatabase|Room)"
```

## Logs để theo dõi
```
GymDatabase: Database instance created successfully
AdminManager: Database initialized successfully
AdminManager: Checking if email admin exists...
AdminManager: Email admin created successfully with ID: [id]
AdminManager: Starting admin authentication for email: admin@gmail.com
AdminManager: Admin lookup result: Found
AdminManager: Admin authenticated successfully
```

## Nếu vẫn lỗi
1. Sử dụng temporary bypass method
2. Kiểm tra AndroidManifest.xml có AdminDashboardActivity
3. Clear cache và rebuild
4. Restart device/emulator

## Kết luận
Database đã được đơn giản hóa và thêm error handling. Nếu vẫn gặp vấn đề, có thể sử dụng temporary workaround để test admin login trước.