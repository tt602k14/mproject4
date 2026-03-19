# Admin Login Debug Guide - Hướng dẫn Debug Đăng nhập Admin

## Vấn đề đã được sửa

### 🔧 Các sửa đổi chính:
1. **Timing Issue**: Admin có thể chưa được tạo khi user đăng nhập ngay lập tức
2. **Synchronous Admin Creation**: Đảm bảo admin được tạo trước khi xác thực
3. **Enhanced Logging**: Thêm debug logs để theo dõi quá trình đăng nhập
4. **Password Hash Verification**: Kiểm tra và so sánh password hash

### 📋 Cách test đăng nhập admin:

#### Bước 1: Mở app và kiểm tra logs
```
adb logcat | grep AdminManager
```

#### Bước 2: Thử đăng nhập
- Email: `admin@gmail.com`
- Password: `admin123`

#### Bước 3: Kiểm tra logs debug
Tìm các log sau trong logcat:
```
AdminManager: Admin email detected, attempting admin login
AdminManager: DEBUG - Admin found:
AdminManager: Email: admin@gmail.com
AdminManager: Password Hash: [hash_value]
AdminManager: Test Hash: [test_hash]
AdminManager: Hash Match: true/false
AdminManager: Admin authenticated successfully by email: admin@gmail.com
```

## Troubleshooting

### ❌ Nếu vẫn không đăng nhập được:

#### 1. Kiểm tra Admin có tồn tại không:
```java
// Trong LoginActivity, thêm debug call:
AdminManager adminManager = AdminManager.getInstance(this);
adminManager.debugAdminStatus("admin@gmail.com");
```

#### 2. Kiểm tra Database:
- Xóa app data và cài đặt lại
- Hoặc clear app cache

#### 3. Kiểm tra Password Hash:
Logs sẽ hiển thị:
```
AdminManager: Comparing passwords - Input hash: [input_hash]
AdminManager: Stored hash: [stored_hash]
```

### ✅ Nếu đăng nhập thành công:
```
AdminManager: Admin authenticated successfully by email: admin@gmail.com
LoginActivity: Admin login successful
Toast: "Chào mừng Admin HTD Gym Admin!"
```

## Các tính năng Debug mới

### 1. Enhanced Logging
- Log chi tiết quá trình xác thực
- Hiển thị password hash comparison
- Theo dõi admin creation process

### 2. Synchronous Admin Creation
```java
// Method ensureEmailAdminExists() đảm bảo admin được tạo trước khi xác thực
private void ensureEmailAdminExists() {
    Admin emailAdmin = database.adminDao().getAdminByEmail("admin@gmail.com");
    if (emailAdmin == null) {
        // Tạo admin ngay lập tức
    }
}
```

### 3. Debug Status Method
```java
// Method để kiểm tra trạng thái admin
public void debugAdminStatus(String email) {
    // Hiển thị thông tin chi tiết admin
    // Kiểm tra password hash
    // Liệt kê tất cả admin trong database
}
```

## Thông tin Admin

### Tài khoản Admin chính:
- **Username**: `htdgym_admin`
- **Email**: `admin@htdgym.com`
- **Password**: `HTDGym@2024`

### Tài khoản Admin tích hợp:
- **Email**: `admin@gmail.com`
- **Password**: `admin123`
- **Username**: `admin_email`
- **Role**: `super_admin`

## Cách kiểm tra nhanh

### 1. Kiểm tra AdminManager initialization:
```bash
adb logcat | grep "AdminManager.*created successfully"
```

### 2. Kiểm tra admin login attempt:
```bash
adb logcat | grep "Admin email detected"
```

### 3. Kiểm tra authentication result:
```bash
adb logcat | grep "Admin authenticated successfully"
```

## Lỗi thường gặp và cách sửa

### 1. "Tài khoản admin không tồn tại"
- **Nguyên nhân**: Admin chưa được tạo trong database
- **Giải pháp**: Clear app data và restart app

### 2. "Mật khẩu không chính xác"
- **Nguyên nhân**: Password hash không khớp
- **Giải pháp**: Kiểm tra logs để so sánh hash values

### 3. App crash khi đăng nhập
- **Nguyên nhân**: Missing AdminDashboardActivity
- **Giải pháp**: Đảm bảo AdminDashboardActivity được khai báo trong AndroidManifest.xml

## Kết luận

Với các sửa đổi này, admin login sẽ hoạt động ổn định hơn. Nếu vẫn gặp vấn đề, hãy kiểm tra logs để xác định nguyên nhân cụ thể.