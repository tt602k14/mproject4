# 🔧 HƯỚNG DẪN DEBUG PAYMENT SYSTEM

## ❓ Vấn đề: PaymentActivity không hiển thị

### 🔍 **Các bước debug đã thực hiện:**

1. **✅ Kiểm tra AndroidManifest.xml** - PaymentActivity đã được khai báo đúng
2. **✅ Sửa lỗi navigation** - Cập nhật `loadPremiumPlans()` để gọi `processPurchase()` thay vì `selectPremiumPlan()`
3. **✅ Thêm debug logs** - Logs trong PremiumActivity và PaymentActivity
4. **✅ Thêm error handling** - Try-catch và toast messages
5. **✅ Thêm confirmation dialog** - Dialog xác nhận trước khi mở PaymentActivity

### 📱 **Cách test hệ thống:**

#### **Bước 1: Mở app và navigate đến Premium**
1. Mở app
2. Click vào nội dung premium (có icon khóa 🔒)
3. Click "Nâng cấp Premium" trong dialog

#### **Bước 2: Chọn gói premium**
1. Trong PremiumActivity, chọn gói "Gói tháng" hoặc "Gói năm"
2. Sẽ hiện dialog xác nhận với thông tin gói
3. Click "Tiếp tục"

#### **Bước 3: Kiểm tra PaymentActivity**
- Nếu thành công: PaymentActivity sẽ mở với QR code
- Nếu lỗi: Sẽ hiện toast message với thông tin lỗi

### 🔍 **Debug logs để kiểm tra:**

Mở **Android Studio Logcat** và filter theo tag:
- `PremiumActivity` - Logs từ PremiumActivity
- `PaymentActivity` - Logs từ PaymentActivity

#### **Logs mong đợi:**
```
D/PremiumActivity: processPurchase called with plan: Gói tháng, price: 50000.0
D/PremiumActivity: Starting PaymentActivity with intent extras
D/PaymentActivity: onCreate called
D/PaymentActivity: getIntentData - planId: monthly, planName: Gói tháng, planPrice: 50000.0
D/PaymentActivity: onCreate completed successfully
```

### 🚨 **Các lỗi có thể xảy ra:**

#### **1. ClassNotFoundException**
- **Nguyên nhân**: PaymentActivity không được build đúng
- **Giải pháp**: Clean và rebuild project
```bash
./gradlew clean
./gradlew app:assembleDebug
```

#### **2. Intent data null**
- **Nguyên nhân**: Dữ liệu gói premium không được truyền đúng
- **Kiểm tra**: Logs trong `getIntentData()`
- **Giải pháp**: Đảm bảo PremiumManager.getPremiumPlans() trả về data đúng

#### **3. Layout inflation error**
- **Nguyên nhân**: Layout XML có lỗi hoặc thiếu resource
- **Kiểm tra**: Logcat sẽ hiện InflateException
- **Giải pháp**: Kiểm tra activity_payment.xml

#### **4. Permission denied**
- **Nguyên nhân**: Activity không được export trong manifest
- **Giải pháp**: Đảm bảo `android:exported="false"` trong manifest

### 🛠️ **Troubleshooting Steps:**

#### **Step 1: Kiểm tra PremiumManager**
```java
// Trong PremiumActivity, thêm log này:
PremiumManager.PremiumPlan[] plans = premiumManager.getPremiumPlans();
Log.d("PremiumActivity", "Number of plans: " + plans.length);
for (PremiumManager.PremiumPlan plan : plans) {
    Log.d("PremiumActivity", "Plan: " + plan.name + ", ID: " + plan.id + ", Price: " + plan.price);
}
```

#### **Step 2: Test direct navigation**
Thêm button test trong PremiumActivity:
```java
Button testButton = new Button(this);
testButton.setText("Test Payment");
testButton.setOnClickListener(v -> {
    Intent intent = new Intent(this, PaymentActivity.class);
    intent.putExtra("plan_id", "monthly");
    intent.putExtra("plan_name", "Test Plan");
    intent.putExtra("plan_price", 50000.0);
    startActivity(intent);
});
```

#### **Step 3: Kiểm tra dependencies**
Đảm bảo các dependencies cần thiết:
- ZXing library cho QR code
- Glide cho image loading
- Room database

### 📋 **Checklist debug:**

- [ ] Build thành công không có lỗi
- [ ] PaymentActivity trong AndroidManifest.xml
- [ ] PremiumManager trả về plans đúng
- [ ] Click listener được set đúng
- [ ] Intent extras được truyền đúng
- [ ] Layout files tồn tại và đúng
- [ ] Permissions đầy đủ

### 💡 **Gợi ý:**

1. **Thử clean rebuild:**
```bash
./gradlew clean
./gradlew app:assembleDebug
```

2. **Kiểm tra Logcat** khi click button premium
3. **Test trên device thật** thay vì emulator
4. **Kiểm tra ProGuard** nếu build release

---

**Nếu vẫn không hoạt động, hãy chia sẻ logs từ Logcat để debug chi tiết hơn!** 🔍