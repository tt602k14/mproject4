# HTD GYM - Ứng dụng quản lý phòng gym

## Tổng quan
HTD GYM là một ứng dụng Android hoàn chỉnh để quản lý phòng gym, bao gồm các chức năng:

### 🏋️ Chức năng chính
- **Quản lý thành viên**: Thêm, sửa, xóa thông tin thành viên
- **Quản lý bài tập**: Theo dõi các buổi tập luyện của thành viên
- **Quản lý thanh toán**: Ghi nhận các khoản thu từ thành viên
- **Quản lý thiết bị**: Theo dõi tình trạng thiết bị trong phòng gym
- **Dashboard**: Thống kê tổng quan về hoạt động của phòng gym

### 🛠️ Công nghệ sử dụng
- **Ngôn ngữ**: Java
- **Database**: Room (SQLite)
- **Architecture**: MVVM với LiveData
- **UI**: Material Design với Dark Theme
- **Navigation**: Bottom Navigation với Fragments

### 📱 Cấu trúc ứng dụng

#### Models
- `Member`: Thông tin thành viên
- `Workout`: Thông tin bài tập
- `Payment`: Thông tin thanh toán
- `Equipment`: Thông tin thiết bị
- `Trainer`: Thông tin huấn luyện viên

#### Database
- Room Database với TypeConverters cho Date
- DAO classes cho mỗi entity
- Repository pattern để quản lý data

#### UI Components
- **MainActivity**: Activity chính với Bottom Navigation
- **HomeFragment**: Dashboard với thống kê tổng quan
- **MembersFragment**: Danh sách và quản lý thành viên
- **WorkoutsFragment**: Danh sách và quản lý bài tập
- **PaymentsFragment**: Danh sách và quản lý thanh toán
- **EquipmentFragment**: Danh sách và quản lý thiết bị

#### Activities
- `AddMemberActivity`: Thêm thành viên mới
- `AddWorkoutActivity`: Thêm bài tập mới
- `AddPaymentActivity`: Thêm thanh toán mới
- `AddEquipmentActivity`: Thêm thiết bị mới

### 🎨 Giao diện
- Dark theme với màu chủ đạo xanh lá cây
- Material Design components
- Responsive layout cho các màn hình khác nhau
- CardView để hiển thị thông tin
- FloatingActionButton để thêm dữ liệu mới

### 📊 Tính năng Dashboard
- Số lượng thành viên đang hoạt động
- Doanh thu tháng hiện tại
- Số buổi tập trong ngày
- Các thao tác nhanh

### 🔧 Cài đặt và chạy ứng dụng

1. **Yêu cầu hệ thống**:
   - Android Studio Arctic Fox trở lên
   - Android SDK 21 trở lên
   - Java 8

2. **Cài đặt**:
   ```bash
   git clone [repository-url]
   cd HTDGymApp
   ```

3. **Mở trong Android Studio**:
   - File → Open → Chọn thư mục dự án
   - Sync Gradle files
   - Run app

### 📝 Hướng dẫn sử dụng

#### Thêm thành viên mới
1. Vào tab "Thành viên"
2. Nhấn nút "+" ở góc dưới bên phải
3. Điền thông tin: tên, số điện thoại, email, địa chỉ
4. Chọn loại thành viên và ngày hết hạn
5. Nhấn "Lưu"

#### Thêm bài tập
1. Vào tab "Tập luyện"
2. Nhấn nút "+" 
3. Điền thông tin bài tập: tên, loại, số set, số lần, trọng lượng
4. Chọn ngày và giờ tập
5. Nhấn "Lưu"

#### Ghi nhận thanh toán
1. Vào tab "Thanh toán"
2. Nhấn nút "+"
3. Nhập số tiền và mô tả
4. Chọn phương thức và loại thanh toán
5. Chọn ngày thanh toán
6. Nhấn "Lưu"

#### Quản lý thiết bị
1. Vào tab "Thiết bị"
2. Nhấn nút "+" để thêm thiết bị mới
3. Điền thông tin: tên, thương hiệu, loại, tình trạng
4. Nhập giá mua và vị trí
5. Nhấn "Lưu"

### 🚀 Tính năng có thể mở rộng
- Thông báo hết hạn thành viên
- Báo cáo chi tiết theo thời gian
- Quản lý lịch tập với huấn luyện viên
- Tích hợp thanh toán online
- Backup và restore dữ liệu
- Quản lý nhiều chi nhánh
- Ứng dụng cho thành viên

### 🐛 Báo lỗi và đóng góp
Nếu bạn gặp lỗi hoặc có ý tưởng cải thiện, vui lòng tạo issue hoặc pull request.

### 📄 License
MIT License - Xem file LICENSE để biết thêm chi tiết.

---
**HTD GYM** - Giải pháp quản lý phòng gym hiện đại và hiệu quả! 💪