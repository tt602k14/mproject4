# 🏋️ HTD GYM - Hướng dẫn sử dụng MySQL Database

## ✅ ĐÃ HOÀN THÀNH

Tôi đã tạo đầy đủ hệ thống MySQL + PHP API cho bạn:

### 📦 Backend (PHP + MySQL)
- ✅ Database schema với 10 bảng
- ✅ 6 PHP API endpoints đầy đủ
- ✅ Setup tự động với giao diện đẹp
- ✅ Sample data sẵn có

### 📱 Android App
- ✅ API Client (GET, POST, PUT, DELETE)
- ✅ API Config
- ✅ Tích hợp vào WorkoutDetailActivity
- ✅ Fallback về local data nếu không có internet

---

## 🚀 CÁCH CÀI ĐẶT (3 BƯỚC)

### Bước 1: Cài XAMPP
1. Download: https://www.apachefriends.org/
2. Cài đặt XAMPP
3. Mở XAMPP Control Panel
4. Click "Start" cho **Apache** và **MySQL** (đợi đến khi màu xanh)

### Bước 2: Copy Backend
1. Tìm thư mục `backend` trong project của bạn
2. Copy toàn bộ thư mục `backend`
3. Paste vào: `C:\xampp\htdocs\htd_gym\`

**Kết quả phải như này:**
```
C:\xampp\htdocs\htd_gym\backend\
├── setup.php          ← File quan trọng nhất
├── config.php
├── database.sql
├── README.md
└── api\
    ├── workouts.php
    ├── members.php
    ├── user_stats.php
    ├── payments.php
    ├── equipment.php
    └── videos.php
```

### Bước 3: Chạy Setup
1. Mở trình duyệt (Chrome, Firefox, Edge...)
2. Vào: **http://localhost/htd_gym/backend/setup.php**
3. Click nút **"Bắt đầu cài đặt"**
4. Đợi 5 giây → Xong! ✅

Setup sẽ tự động:
- ✅ Tạo database `htd_gym`
- ✅ Tạo 10 bảng
- ✅ Thêm data mẫu
- ✅ Hiển thị API URLs

---

## 🧪 TEST API

Sau khi setup xong, test các URL này trong trình duyệt:

1. **Workouts**: http://localhost/htd_gym/backend/api/workouts.php
2. **Members**: http://localhost/htd_gym/backend/api/members.php
3. **Equipment**: http://localhost/htd_gym/backend/api/equipment.php

Nếu thấy JSON data → **Thành công!** 🎉

---

## 📱 CẤU HÌNH ANDROID APP

### Test trên Android Emulator:
File `app/src/main/java/com/htdgym/app/api/ApiConfig.java` đã được cấu hình sẵn:
```java
public static final String BASE_URL = "http://10.0.2.2/htd_gym/backend/api/";
```
→ **Không cần sửa gì!**

### Test trên điện thoại thật:
1. **Kiểm tra IP máy tính:**
   - Mở CMD (Command Prompt)
   - Gõ: `ipconfig`
   - Tìm dòng "IPv4 Address" (ví dụ: 192.168.1.100)

2. **Sửa ApiConfig.java:**
   ```java
   public static final String BASE_URL = "http://192.168.1.100/htd_gym/backend/api/";
   ```
   (Thay 192.168.1.100 bằng IP của bạn)

3. **Quan trọng:** Điện thoại và máy tính phải cùng mạng WiFi!

---

## 🎯 CÁCH SỬ DỤNG API TRONG CODE

### Ví dụ 1: Lấy danh sách workouts
```java
ApiClient.get("workouts.php", new ApiClient.ApiCallback() {
    @Override
    public void onSuccess(String response) {
        try {
            JSONObject json = new JSONObject(response);
            if (json.getBoolean("success")) {
                JSONArray workouts = json.getJSONArray("data");
                // Xử lý data
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(context, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
    }
});
```

### Ví dụ 2: Lấy workout theo category
```java
String params = "category=chest";
ApiClient.get("workouts.php", params, new ApiClient.ApiCallback() {
    @Override
    public void onSuccess(String response) {
        // Xử lý response
    }

    @Override
    public void onError(String error) {
        // Xử lý lỗi
    }
});
```

### Ví dụ 3: Tạo member mới
```java
try {
    JSONObject data = new JSONObject();
    data.put("name", "Nguyễn Văn A");
    data.put("email", "nguyenvana@gmail.com");
    data.put("phone", "0901234567");
    data.put("membership_type", "Premium");
    data.put("start_date", "2024-01-01");
    data.put("end_date", "2024-12-31");
    data.put("status", "active");
    
    ApiClient.post("members.php", data, new ApiClient.ApiCallback() {
        @Override
        public void onSuccess(String response) {
            Toast.makeText(context, "Tạo thành công!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String error) {
            Toast.makeText(context, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
        }
    });
} catch (Exception e) {
    e.printStackTrace();
}
```

---

## 🗄️ DATABASE SCHEMA

### Các bảng chính:
1. **users** - Người dùng app
2. **members** - Thành viên gym
3. **workouts** - Bài tập
4. **exercises** - Động tác tập (chi tiết của workout)
5. **user_stats** - Thống kê người dùng (calories, thời gian...)
6. **workout_sessions** - Phiên tập luyện
7. **payments** - Thanh toán
8. **equipment** - Thiết bị gym
9. **videos** - Video hướng dẫn
10. **trainers** - Huấn luyện viên

### Xem chi tiết:
- Mở phpMyAdmin: http://localhost/phpmyadmin
- Chọn database `htd_gym`
- Xem cấu trúc các bảng

---

## ❓ TROUBLESHOOTING

### Lỗi: "Connection refused"
**Nguyên nhân:** Apache chưa start hoặc IP sai

**Giải pháp:**
1. Kiểm tra XAMPP Control Panel → Apache phải màu xanh
2. Kiểm tra IP trong ApiConfig.java
3. Tắt Firewall tạm thời để test

### Lỗi: "Database connection failed"
**Nguyên nhân:** MySQL chưa start

**Giải pháp:**
1. Kiểm tra XAMPP Control Panel → MySQL phải màu xanh
2. Chạy lại setup.php

### Lỗi: "404 Not Found"
**Nguyên nhân:** Đường dẫn file sai

**Giải pháp:**
1. Kiểm tra thư mục: `C:\xampp\htdocs\htd_gym\backend\`
2. Đảm bảo có thư mục `api` bên trong
3. Kiểm tra URL: http://localhost/htd_gym/backend/api/workouts.php

### App không load data
**Giải pháp:**
1. Test API trong trình duyệt trước
2. Kiểm tra LogCat trong Android Studio
3. Đảm bảo có permission INTERNET trong AndroidManifest.xml
4. App sẽ tự động fallback về local data nếu API lỗi

---

## 📊 API ENDPOINTS

### Workouts
- `GET workouts.php` - Lấy tất cả
- `GET workouts.php?id=1` - Lấy theo ID
- `GET workouts.php?category=chest` - Lấy theo category
- `POST workouts.php` - Tạo mới
- `PUT workouts.php` - Cập nhật
- `DELETE workouts.php` - Xóa

### Members
- `GET members.php` - Lấy tất cả
- `GET members.php?id=1` - Lấy theo ID
- `POST members.php` - Tạo mới
- `PUT members.php` - Cập nhật
- `DELETE members.php` - Xóa

### User Stats
- `GET user_stats.php?user_id=1` - Lấy stats của user
- `GET user_stats.php?user_id=1&date=2024-01-15` - Lấy theo ngày
- `POST user_stats.php` - Cập nhật stats

### Payments, Equipment, Videos
- Tương tự như trên

---

## 🎉 HOÀN TẤT!

Bây giờ app của bạn đã:
- ✅ Kết nối với MySQL database
- ✅ Lưu data trên server
- ✅ Có thể chia sẻ data giữa nhiều thiết bị
- ✅ Tự động fallback về local data nếu mất mạng

**Build và chạy app để test!** 🚀

---

## 📞 HỖ TRỢ

Nếu gặp vấn đề:
1. Kiểm tra XAMPP Apache và MySQL đã Start (màu xanh)
2. Test API trong trình duyệt trước
3. Xem LogCat trong Android Studio
4. Đọc lại hướng dẫn từng bước

**Chúc bạn thành công!** 💪
