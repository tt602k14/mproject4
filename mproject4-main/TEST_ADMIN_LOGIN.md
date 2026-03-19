# 🔧 TEST ADMIN LOGIN

## Các bước test:

### 1. **Chạy app**
```bash
./gradlew assembleDebug
# Sau đó install APK lên device/emulator
```

### 2. **Truy cập Admin Login**
- Mở app
- Ở màn hình Login thường, tìm link "Admin Access" 
- Click vào để mở AdminLoginActivity

### 3. **Đăng nhập Admin**
- Email: `admin@gmail.com`
- Password: `admin123`
- Click "Đăng nhập"

### 4. **Kiểm tra kết quả**
- ✅ **Thành công**: Chuyển đến AdminDashboardActivity
- ❌ **Thất bại**: Hiển thị toast lỗi

## Debug thông tin:

### **Tài khoản Admin được tạo tự động:**
- Email: `admin@gmail.com`
- Password: `admin123`
- Username: `admin_email`
- Role: `super_admin`

### **Nếu không đăng nhập được:**
1. Kiểm tra toast message để xem lỗi gì
2. Xem logcat để debug
3. Đảm bảo database được khởi tạo đúng

### **Credentials debug (đã pre-fill):**
- AdminLoginActivity sẽ tự động điền sẵn email và password
- Chỉ cần click "Đăng nhập"