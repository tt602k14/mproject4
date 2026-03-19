# Tính Năng Avatar và Chỉnh Sửa Profile

## Mục Tiêu
Thêm khả năng chọn ảnh avatar từ thư viện điện thoại và chỉnh sửa thông tin cá nhân trong trang Profile.

## Đã Hoàn Thành
✅ Cập nhật layout XML với ImageView cho avatar
✅ Thêm camera icon overlay
✅ Thêm permissions READ_MEDIA_IMAGES và READ_EXTERNAL_STORAGE
✅ Thêm ActivityResultLauncher cho image picker
✅ Thêm permission launcher

## Cần Sửa
❌ ProfileFragment.java bị lỗi cú pháp do code duplicate
❌ Cần viết lại file ProfileFragment.java hoàn chỉnh

## Chức Năng Cần Implement
1. Click vào avatar để mở thư viện ảnh
2. Request permission runtime (Android 13+)
3. Lưu ảnh vào internal storage
4. Resize ảnh để tiết kiệm dung lượng (500x500)
5. Hiển thị initials nếu chưa có avatar
6. Button "Chỉnh sửa hồ sơ" để mở EditProfileDialog

## Code Cần Thêm Vào ProfileFragment
```java
// Trong onCreate()
imagePickerLauncher = registerForActivityResult(...);
permissionLauncher = registerForActivityResult(...);

// Trong setupClickListeners()
imgAvatar.setOnClickListener(v -> openImagePicker());

// Methods cần có:
- loadAvatarImage()
- showInitials()
- saveAvatarImage(Uri)
- openImagePicker() với permission check
```

## Lỗi Hiện Tại
File ProfileFragment.java có code bị duplicate ở dòng 229, gây ra 64 lỗi biên dịch.
Cần xóa code duplicate và viết lại file đúng cấu trúc.

## Permissions Đã Thêm
```xml
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
```

## Layout Đã Cập Nhật
- ImageView với id `img_avatar`
- TextView với id `tv_avatar_initials` (hiển thị chữ cái đầu)
- Camera icon overlay

## Bước Tiếp Theo
1. Sửa lỗi ProfileFragment.java
2. Test chức năng chọn ảnh
3. Test permission request
4. Test lưu và load avatar
