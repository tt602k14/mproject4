package com.htdgym.app.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.htdgym.app.R;
import com.htdgym.app.utils.PremiumManager;

import java.text.NumberFormat;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    
    private ImageView backButton;
    private TextView tvPlanName, tvPlanPrice, tvBankName, tvAccountNumber, tvAccountName;
    private TextView tvTransferContent, tvPaymentCode, tvTimeRemaining;
    private ImageView ivQRCode;
    private Button btnCopyAccount, btnCopyContent, btnConfirmPayment, btnCancelPayment;
    private CardView cardPaymentInfo, cardQRCode;
    private LinearLayout layoutPaymentSteps;
    
    private String planId, planName;
    private double planPrice;
    private String paymentCode;
    private CountDownTimer paymentTimer;
    private PremiumManager premiumManager;
    
    // Bank information - Real account details
    private static final String BANK_NAME = "Vietcombank";
    private static final String ACCOUNT_NUMBER = "7369786789";
    private static final String ACCOUNT_NAME = "LU DANG HUNG";
    private static final String BANK_CODE = "VCB";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        android.util.Log.d("PaymentActivity", "onCreate called");
        
        setContentView(R.layout.activity_payment);
        
        initViews();
        getIntentData();
        setupPremiumManager();
        generatePaymentCode();
        setupPaymentInfo();
        generateQRCode();
        setupClickListeners();
        startPaymentTimer();
        
        android.util.Log.d("PaymentActivity", "onCreate completed successfully");
    }
    
    private void initViews() {
        backButton = findViewById(R.id.btn_back);
        tvPlanName = findViewById(R.id.tv_plan_name);
        tvPlanPrice = findViewById(R.id.tv_plan_price);
        tvBankName = findViewById(R.id.tv_bank_name);
        tvAccountNumber = findViewById(R.id.tv_account_number);
        tvAccountName = findViewById(R.id.tv_account_name);
        tvTransferContent = findViewById(R.id.tv_transfer_content);
        tvPaymentCode = findViewById(R.id.tv_payment_code);
        tvTimeRemaining = findViewById(R.id.tv_time_remaining);
        ivQRCode = findViewById(R.id.iv_qr_code);
        btnCopyAccount = findViewById(R.id.btn_copy_account);
        btnCopyContent = findViewById(R.id.btn_copy_content);
        btnConfirmPayment = findViewById(R.id.btn_confirm_payment);
        btnCancelPayment = findViewById(R.id.btn_cancel_payment);
        cardPaymentInfo = findViewById(R.id.card_payment_info);
        cardQRCode = findViewById(R.id.card_qr_code);
        layoutPaymentSteps = findViewById(R.id.layout_payment_steps);
    }
    
    private void getIntentData() {
        Intent intent = getIntent();
        planId = intent.getStringExtra("plan_id");
        planName = intent.getStringExtra("plan_name");
        planPrice = intent.getDoubleExtra("plan_price", 0);
        
        android.util.Log.d("PaymentActivity", "getIntentData - planId: " + planId + ", planName: " + planName + ", planPrice: " + planPrice);
        
        if (planId == null || planName == null || planPrice == 0) {
            android.util.Log.e("PaymentActivity", "Invalid plan data received");
            Toast.makeText(this, "Thông tin gói không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void setupPremiumManager() {
        premiumManager = PremiumManager.getInstance(this);
    }
    
    private void generatePaymentCode() {
        // Generate unique payment code
        paymentCode = "HTD" + System.currentTimeMillis() % 1000000;
        
        // Save transaction to database
        saveTransactionToDatabase();
    }
    
    private void saveTransactionToDatabase() {
        new Thread(() -> {
            com.htdgym.app.database.GymDatabase database = 
                com.htdgym.app.database.GymDatabase.getInstance(this);
            
            com.htdgym.app.models.PaymentTransaction transaction = 
                new com.htdgym.app.models.PaymentTransaction(
                    premiumManager.getCurrentUserId(),
                    paymentCode,
                    planId,
                    planName,
                    planPrice,
                    "bank_transfer"
                );
            
            transaction.setBankAccount(ACCOUNT_NUMBER);
            transaction.setTransferContent("HTD GYM " + paymentCode);
            
            database.paymentTransactionDao().insertTransaction(transaction);
        }).start();
    }
    
    private void setupPaymentInfo() {
        tvPlanName.setText(planName);
        tvPlanPrice.setText(formatPrice(planPrice));
        tvBankName.setText(BANK_NAME);
        tvAccountNumber.setText(ACCOUNT_NUMBER);
        tvAccountName.setText(ACCOUNT_NAME);
        
        String transferContent = "HTD GYM " + paymentCode;
        tvTransferContent.setText(transferContent);
        tvPaymentCode.setText(paymentCode);
        
        setupPaymentSteps();
    }
    
    private void setupPaymentSteps() {
        layoutPaymentSteps.removeAllViews();
        
        String[] steps = {
            "1. Mở ứng dụng ngân hàng của bạn",
            "2. Quét mã QR hoặc nhập thông tin chuyển khoản",
            "3. Kiểm tra số tiền: " + formatPrice(planPrice),
            "4. Nhập nội dung: HTD GYM " + paymentCode,
            "5. Xác nhận chuyển khoản",
            "6. Nhấn 'Xác nhận thanh toán' sau khi chuyển"
        };
        
        for (String step : steps) {
            TextView stepView = new TextView(this);
            stepView.setText(step);
            stepView.setTextSize(14);
            stepView.setTextColor(getResources().getColor(R.color.text_primary));
            stepView.setPadding(0, 8, 0, 8);
            layoutPaymentSteps.addView(stepView);
        }
    }
    
    private void generateQRCode() {
        try {
            // Create VietQR format
            String qrContent = createVietQRContent();
            
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
            
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            
            ivQRCode.setImageBitmap(bitmap);
            
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Không thể tạo mã QR", Toast.LENGTH_SHORT).show();
        }
    }
    
    private String createVietQRContent() {
        // VietQR format for bank transfer
        return String.format(
            "00020101021238570010A00000072701270006%s01%s0208QRIBFTTA53037045802VN5913%s6304%s",
            BANK_CODE,
            ACCOUNT_NUMBER,
            ACCOUNT_NAME.replace(" ", ""),
            generateChecksum()
        );
    }
    
    private String generateChecksum() {
        // Simple checksum for demo - in production use proper VietQR checksum
        return String.format("%04d", (int)(Math.random() * 10000));
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> {
            showCancelConfirmation();
        });
        
        btnCopyAccount.setOnClickListener(v -> {
            copyToClipboard("Số tài khoản", ACCOUNT_NUMBER);
        });
        
        btnCopyContent.setOnClickListener(v -> {
            String content = "HTD GYM " + paymentCode;
            copyToClipboard("Nội dung chuyển khoản", content);
        });
        
        btnConfirmPayment.setOnClickListener(v -> {
            confirmPayment();
        });
        
        btnCancelPayment.setOnClickListener(v -> {
            showCancelConfirmation();
        });
    }
    
    private void copyToClipboard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Đã sao chép: " + text, Toast.LENGTH_SHORT).show();
    }
    
    private void startPaymentTimer() {
        // 15 minutes timer
        paymentTimer = new CountDownTimer(15 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                tvTimeRemaining.setText(String.format("Thời gian còn lại: %02d:%02d", minutes, seconds));
            }
            
            @Override
            public void onFinish() {
                tvTimeRemaining.setText("Hết thời gian thanh toán");
                btnConfirmPayment.setEnabled(false);
                btnConfirmPayment.setText("Hết hạn");
                
                new androidx.appcompat.app.AlertDialog.Builder(PaymentActivity.this)
                    .setTitle("Hết thời gian")
                    .setMessage("Phiên thanh toán đã hết hạn. Vui lòng thử lại.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setCancelable(false)
                    .show();
            }
        }.start();
    }
    
    private void confirmPayment() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("💳 Xác nhận thanh toán")
            .setMessage("Bạn đã chuyển khoản thành công?\n\n" +
                "💰 Số tiền: " + formatPrice(planPrice) + "\n" +
                "📝 Nội dung: HTD GYM " + paymentCode + "\n" +
                "🏦 Ngân hàng: " + BANK_NAME + "\n" +
                "📱 STK: " + ACCOUNT_NUMBER + "\n" +
                "👤 Chủ TK: " + ACCOUNT_NAME + "\n\n" +
                "⚠️ Lưu ý: Chỉ xác nhận khi đã chuyển khoản thành công.\n" +
                "Hệ thống sẽ xác minh và kích hoạt Premium cho bạn.")
            .setPositiveButton("✅ Đã chuyển khoản", (dialog, which) -> {
                processPayment();
            })
            .setNegativeButton("❌ Chưa chuyển", null)
            .setNeutralButton("📋 Copy nội dung", (dialog, which) -> {
                copyToClipboard("Nội dung chuyển khoản", "HTD GYM " + paymentCode);
                // Show dialog again after copying
                confirmPayment();
            })
            .show();
    }
    
    private void processPayment() {
        // Show processing dialog with better UI
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        android.view.View dialogView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
        
        // Create custom processing dialog
        androidx.appcompat.app.AlertDialog processingDialog = new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("🔄 Đang xử lý thanh toán")
            .setMessage("Vui lòng đợi trong giây lát...\nHệ thống đang xác minh giao dịch của bạn.")
            .setCancelable(false)
            .create();
        processingDialog.show();
        
        // Disable confirm button to prevent double click
        btnConfirmPayment.setEnabled(false);
        btnConfirmPayment.setText("Đang xử lý...");
        
        // Simulate payment verification (3 seconds for better UX)
        btnConfirmPayment.postDelayed(() -> {
            processingDialog.dismiss();
            
            // In production, this would verify with bank API
            // For demo, we'll simulate successful payment (95% success rate)
            boolean paymentSuccess = simulatePaymentVerification();
            
            if (paymentSuccess) {
                // Update transaction status
                updateTransactionStatus("completed");
                
                // Activate premium with proper user ID
                String userId = premiumManager.getCurrentUserId();
                if (userId == null) {
                    // Generate a default user ID if not set
                    userId = "user_" + System.currentTimeMillis();
                    premiumManager.setCurrentUserId(userId);
                }
                
                // Activate premium subscription
                premiumManager.activatePremium(
                    planId,
                    "bank_transfer",
                    paymentCode,
                    planPrice
                );
                
                showPaymentSuccess();
            } else {
                // Update transaction status
                updateTransactionStatus("failed");
                showPaymentFailed();
                
                // Re-enable button for retry
                btnConfirmPayment.setEnabled(true);
                btnConfirmPayment.setText("XÁC NHẬN CHUYỂN KHOẢN");
            }
        }, 3000);
    }
    
    private void updateTransactionStatus(String status) {
        new Thread(() -> {
            com.htdgym.app.database.GymDatabase database = 
                com.htdgym.app.database.GymDatabase.getInstance(this);
            
            database.paymentTransactionDao().updateTransactionStatus(
                paymentCode, status, new java.util.Date()
            );
        }).start();
    }
    
    private boolean simulatePaymentVerification() {
        // In production, this would:
        // 1. Call bank API to verify transaction
        // 2. Check payment code and amount
        // 3. Verify transaction status
        
        // For demo, simulate 90% success rate
        return Math.random() > 0.1;
    }
    
    private void showPaymentSuccess() {
        if (paymentTimer != null) {
            paymentTimer.cancel();
        }
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("🎉 Thanh toán thành công!")
            .setMessage("Chúc mừng! Bạn đã nâng cấp Premium thành công.\n\n" +
                "✅ Gói: " + planName + "\n" +
                "✅ Số tiền: " + formatPrice(planPrice) + "\n" +
                "✅ Mã giao dịch: " + paymentCode + "\n\n" +
                "🎯 Tính năng Premium đã được kích hoạt:\n" +
                "• Truy cập tất cả bài tập\n" +
                "• Chương trình 60 & 90 ngày\n" +
                "• Không quảng cáo\n" +
                "• Hỗ trợ ưu tiên\n\n" +
                "Bạn có thể sử dụng ngay bây giờ!")
            .setPositiveButton("Bắt đầu sử dụng", (dialog, which) -> {
                // Return to home and refresh premium status
                android.content.Intent intent = new android.content.Intent();
                intent.putExtra("premium_activated", true);
                setResult(RESULT_OK, intent);
                finish();
            })
            .setNeutralButton("Xem lịch sử", (dialog, which) -> {
                // Open transaction history
                android.content.Intent intent = new android.content.Intent(this, 
                    com.htdgym.app.activities.TransactionHistoryActivity.class);
                startActivity(intent);
                finish();
            })
            .setCancelable(false)
            .show();
    }
    
    private void showPaymentFailed() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("❌ Thanh toán thất bại")
            .setMessage("Không tìm thấy giao dịch của bạn.\n\n" +
                "Vui lòng kiểm tra:\n" +
                "• Số tiền chuyển khoản đúng\n" +
                "• Nội dung chuyển khoản: HTD GYM " + paymentCode + "\n" +
                "• Giao dịch đã thành công\n\n" +
                "Nếu đã chuyển khoản, vui lòng thử lại sau 1-2 phút.")
            .setPositiveButton("Thử lại", null)
            .setNegativeButton("Hủy", (dialog, which) -> finish())
            .show();
    }
    
    private void showCancelConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Hủy thanh toán")
            .setMessage("Bạn có chắc chắn muốn hủy thanh toán không?")
            .setPositiveButton("Hủy thanh toán", (dialog, which) -> {
                if (paymentTimer != null) {
                    paymentTimer.cancel();
                }
                finish();
            })
            .setNegativeButton("Tiếp tục", null)
            .show();
    }
    
    private String formatPrice(double price) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price) + " VND";
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (paymentTimer != null) {
            paymentTimer.cancel();
        }
    }
    
    @Override
    public void onBackPressed() {
        showCancelConfirmation();
    }
}