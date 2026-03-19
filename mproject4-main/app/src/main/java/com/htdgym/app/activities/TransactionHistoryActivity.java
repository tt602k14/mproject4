package com.htdgym.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.PaymentTransaction;
import com.htdgym.app.utils.PremiumManager;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionHistoryActivity extends AppCompatActivity {
    
    private ImageView backButton;
    private LinearLayout transactionContainer;
    private TextView emptyMessage;
    private PremiumManager premiumManager;
    private GymDatabase database;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        
        initViews();
        setupDatabase();
        loadTransactions();
    }
    
    private void initViews() {
        backButton = findViewById(R.id.btn_back);
        transactionContainer = findViewById(R.id.transaction_container);
        emptyMessage = findViewById(R.id.tv_empty_message);
        
        backButton.setOnClickListener(v -> finish());
    }
    
    private void setupDatabase() {
        premiumManager = PremiumManager.getInstance(this);
        database = GymDatabase.getInstance(this);
    }
    
    private void loadTransactions() {
        new Thread(() -> {
            String userId = premiumManager.getCurrentUserId();
            if (userId != null) {
                List<PaymentTransaction> transactions = 
                    database.paymentTransactionDao().getTransactionsByUserId(userId);
                
                runOnUiThread(() -> {
                    if (transactions.isEmpty()) {
                        showEmptyState();
                    } else {
                        displayTransactions(transactions);
                    }
                });
            } else {
                runOnUiThread(this::showEmptyState);
            }
        }).start();
    }
    
    private void showEmptyState() {
        transactionContainer.setVisibility(View.GONE);
        emptyMessage.setVisibility(View.VISIBLE);
    }
    
    private void displayTransactions(List<PaymentTransaction> transactions) {
        transactionContainer.setVisibility(View.VISIBLE);
        emptyMessage.setVisibility(View.GONE);
        transactionContainer.removeAllViews();
        
        for (PaymentTransaction transaction : transactions) {
            addTransactionCard(transaction);
        }
    }
    
    private void addTransactionCard(PaymentTransaction transaction) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        card.setLayoutParams(cardParams);
        card.setRadius(12);
        card.setCardElevation(4);
        card.setUseCompatPadding(true);
        
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(20, 20, 20, 20);
        
        // Header with status
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(android.view.Gravity.CENTER_VERTICAL);
        
        TextView planName = new TextView(this);
        planName.setText(transaction.getPlanName());
        planName.setTextSize(16);
        planName.setTextColor(getResources().getColor(R.color.text_primary));
        planName.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams planParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        planName.setLayoutParams(planParams);
        
        TextView status = new TextView(this);
        status.setText(getStatusText(transaction.getStatus()));
        status.setTextSize(12);
        status.setTextColor(getStatusColor(transaction.getStatus()));
        status.setTypeface(null, android.graphics.Typeface.BOLD);
        status.setBackground(getStatusBackground(transaction.getStatus()));
        status.setPadding(12, 6, 12, 6);
        
        header.addView(planName);
        header.addView(status);
        
        // Amount
        TextView amount = new TextView(this);
        amount.setText(formatPrice(transaction.getAmount()));
        amount.setTextSize(18);
        amount.setTextColor(getResources().getColor(R.color.premium_gold));
        amount.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams amountParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        amountParams.setMargins(0, 8, 0, 8);
        amount.setLayoutParams(amountParams);
        
        // Payment details
        LinearLayout details = new LinearLayout(this);
        details.setOrientation(LinearLayout.VERTICAL);
        details.setBackgroundColor(0xFFF5F5F5);
        details.setPadding(16, 12, 16, 12);
        LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        detailsParams.setMargins(0, 8, 0, 0);
        details.setLayoutParams(detailsParams);
        
        // Payment code
        addDetailRow(details, "Mã thanh toán:", transaction.getPaymentCode());
        
        // Payment method
        String methodText = "bank_transfer".equals(transaction.getPaymentMethod()) 
            ? "Chuyển khoản ngân hàng" : "Mã khuyến mãi";
        addDetailRow(details, "Phương thức:", methodText);
        
        // Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        addDetailRow(details, "Ngày tạo:", dateFormat.format(transaction.getCreatedAt()));
        
        if (transaction.getCompletedAt() != null) {
            addDetailRow(details, "Hoàn thành:", dateFormat.format(transaction.getCompletedAt()));
        }
        
        content.addView(header);
        content.addView(amount);
        content.addView(details);
        card.addView(content);
        
        transactionContainer.addView(card);
    }
    
    private void addDetailRow(LinearLayout parent, String label, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.setMargins(0, 4, 0, 4);
        row.setLayoutParams(rowParams);
        
        TextView labelView = new TextView(this);
        labelView.setText(label);
        labelView.setTextSize(12);
        labelView.setTextColor(getResources().getColor(R.color.text_secondary));
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        );
        labelView.setLayoutParams(labelParams);
        
        TextView valueView = new TextView(this);
        valueView.setText(value);
        valueView.setTextSize(12);
        valueView.setTextColor(getResources().getColor(R.color.text_primary));
        valueView.setTypeface(null, android.graphics.Typeface.BOLD);
        
        row.addView(labelView);
        row.addView(valueView);
        parent.addView(row);
    }
    
    private String getStatusText(String status) {
        switch (status) {
            case "pending": return "Đang chờ";
            case "completed": return "Thành công";
            case "failed": return "Thất bại";
            case "expired": return "Hết hạn";
            default: return "Không xác định";
        }
    }
    
    private int getStatusColor(String status) {
        switch (status) {
            case "pending": return getResources().getColor(R.color.warning);
            case "completed": return getResources().getColor(R.color.primary);
            case "failed": return getResources().getColor(R.color.danger);
            case "expired": return getResources().getColor(R.color.text_secondary);
            default: return getResources().getColor(R.color.text_secondary);
        }
    }
    
    private android.graphics.drawable.Drawable getStatusBackground(String status) {
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(12);
        
        switch (status) {
            case "pending":
                drawable.setColor(0x20FFC107);
                break;
            case "completed":
                drawable.setColor(0x204CAF50);
                break;
            case "failed":
                drawable.setColor(0x20F44336);
                break;
            case "expired":
                drawable.setColor(0x20757575);
                break;
            default:
                drawable.setColor(0x20757575);
                break;
        }
        
        return drawable;
    }
    
    private String formatPrice(double price) {
        if (price == 0) {
            return "Miễn phí";
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price) + " VND";
    }
}