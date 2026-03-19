# 🔐 TÀI KHOẢN MẶC ĐỊNH HTD GYM

## 👨‍💼 Tài khoản Admin

**Email:** `admin@htdgym.com`  
**Mật khẩu:** `admin123`  
**Quyền:** Super Admin  
**Chức năng:** Truy cập Admin Dashboard, quản lý toàn bộ hệ thống

---

## 👤 Tài khoản User thường

### 1. User cơ bản
**Email:** `user@htdgym.com`  
**Mật khẩu:** `user123`  
**Loại:** Người dùng thường (Free)  
**Thông tin:**
- Tên: Người dùng thường
- Tuổi: 25
- Cân nặng: 70kg
- Chiều cao: 170cm
- Mục tiêu: Tăng cơ bắp

### 2. User Premium
**Email:** `premium@htdgym.com`  
**Mật khẩu:** `premium123`  
**Loại:** Người dùng Premium (Paid)  
**Thông tin:**
- Tên: Người dùng Premium
- Tuổi: 30
- Cân nặng: 75kg
- Chiều cao: 175cm
- Mục tiêu: Giảm cân và tăng cơ
- **Gói Premium:** Yearly (365 ngày)
- **Giá:** 299,000 VND

### 3. User Demo
**Email:** `demo@htdgym.com`  
**Mật khẩu:** `demo123`  
**Loại:** Tài khoản Demo (Free)  
**Thông tin:**
- Tên: Tài khoản Demo
- Tuổi: 28
- Cân nặng: 65kg
- Chiều cao: 165cm
- Mục tiêu: Duy trì sức khỏe

---

## 🚀 Cách sử dụng

### **Cài đặt trên máy mới:**
1. Cài đặt APK HTD Gym
2. Mở app lần đầu
3. Database sẽ tự động tạo các tài khoản mặc định
4. Đăng nhập bằng bất kỳ tài khoản nào ở trên

### **Kiểm tra tài khoản đã tạo:**
- Mở app và thử đăng nhập
- Nếu thành công = tài khoản đã được tạo
- Nếu thất bại = kiểm tra log hoặc reset database

### **Reset database (nếu cần):**
1. Đăng nhập admin: `admin@htdgym.com` / `admin123`
2. Vào Admin Dashboard > System Settings
3. Chọn Database Settings > Reset Database
4. Khởi động lại app

---

## 🔧 Tính năng theo loại tài khoản

### **Admin (`admin@htdgym.com`)**
✅ Admin Dashboard  
✅ Quản lý nội dung  
✅ Quản lý người dùng  
✅ Quản lý Premium  
✅ Cài đặt hệ thống  
✅ Tất cả chức năng user  

### **User Premium (`premium@htdgym.com`)**
✅ Trang chủ đầy đủ  
✅ Tất cả chương trình tập (30/60/90 ngày)  
✅ Bài tập Premium (HIIT, Muscle Builder)  
✅ Không hiển thị Premium banner  
✅ Truy cập toàn bộ nội dung  

### **User thường (`user@htdgym.com`, `demo@htdgym.com`)**
✅ Trang chủ cơ bản  
✅ Chương trình 30 ngày (miễn phí)  
❌ Chương trình 60/90 ngày (Premium)  
❌ Bài tập Premium  
✅ Hiển thị Premium banner  
✅ Có thể nâng cấp Premium  

---

## 📱 Lưu ý quan trọng

### **Bảo mật:**
- Mật khẩu được hash SHA-256
- Tài khoản chỉ tồn tại local database
- Không đồng bộ với server

### **Tương thích:**
- ✅ Hoạt động trên mọi thiết bị Android
- ✅ Tự động tạo khi cài app mới
- ✅ Không cần kết nối internet
- ✅ Database local SQLite

### **Khắc phục sự cố:**
- Nếu không đăng nhập được → Reset database
- Nếu thiếu tài khoản → Kiểm tra log
- Nếu lỗi Premium → Kiểm tra PremiumSubscription table

---

## 🎯 Mục đích sử dụng

**Cho Developer:**
- Test các tính năng Admin
- Test Premium features
- Demo cho khách hàng

**Cho End User:**
- Trải nghiệm ngay khi cài app
- Không cần đăng ký tài khoản
- Test trước khi tạo tài khoản thật

**Cho Demo/Presentation:**
- Có sẵn data để demo
- Không cần setup phức tạp
- Showcase đầy đủ tính năng

---

**🔥 Tất cả tài khoản này sẽ tự động có sẵn khi cài app trên bất kỳ máy nào! 🔥**