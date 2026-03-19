package com.htdgym.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.models.PaymentTransaction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdminPaymentAdapter extends RecyclerView.Adapter<AdminPaymentAdapter.PaymentViewHolder> {
    
    private List<PaymentTransaction> payments;
    private OnPaymentActionListener listener;
    
    public interface OnPaymentActionListener {
        void onPaymentClick(PaymentTransaction payment);
        void onRefundPayment(PaymentTransaction payment);
        void onViewUserDetails(PaymentTransaction payment);
    }
    
    public AdminPaymentAdapter(List<PaymentTransaction> payments, OnPaymentActionListener listener) {
        this.payments = payments;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_payment, parent, false);
        return new PaymentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentTransaction payment = payments.get(position);
        holder.bind(payment);
    }
    
    @Override
    public int getItemCount() {
        return payments.size();
    }
    
    class PaymentViewHolder extends RecyclerView.ViewHolder {
        private CardView cardPayment;
        private TextView tvTransactionId, tvUserId, tvAmount, tvStatus, tvPaymentMethod, tvDescription, tvDate;
        private Button btnRefund, btnViewUser;
        
        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            cardPayment = itemView.findViewById(R.id.card_payment);
            tvTransactionId = itemView.findViewById(R.id.tv_transaction_id);
            tvUserId = itemView.findViewById(R.id.tv_user_id);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvPaymentMethod = itemView.findViewById(R.id.tv_payment_method);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvDate = itemView.findViewById(R.id.tv_date);
            btnRefund = itemView.findViewById(R.id.btn_refund);
            btnViewUser = itemView.findViewById(R.id.btn_view_user);
        }
        
        public void bind(PaymentTransaction payment) {
            tvTransactionId.setText("ID: " + payment.getTransactionId());
            tvUserId.setText("User: " + payment.getUserId());
            tvAmount.setText(formatCurrency(payment.getAmount()));
            tvDescription.setText(payment.getDescription());
            tvPaymentMethod.setText(getPaymentMethodText(payment.getPaymentMethod()));
            
            // Status styling
            String status = payment.getStatus();
            switch (status) {
                case "SUCCESS":
                    tvStatus.setText("✅ Thành công");
                    tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.green_primary));
                    btnRefund.setVisibility(View.VISIBLE);
                    break;
                case "PENDING":
                    tvStatus.setText("⏳ Đang xử lý");
                    tvStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_orange_light));
                    btnRefund.setVisibility(View.GONE);
                    break;
                case "FAILED":
                    tvStatus.setText("❌ Thất bại");
                    tvStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
                    btnRefund.setVisibility(View.GONE);
                    break;
                default:
                    tvStatus.setText(status);
                    tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.text_secondary));
                    btnRefund.setVisibility(View.GONE);
                    break;
            }
            
            // Date
            if (payment.getCreatedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                tvDate.setText(sdf.format(payment.getCreatedAt()));
            } else {
                tvDate.setText("--");
            }
            
            // Click listeners
            cardPayment.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPaymentClick(payment);
                }
            });
            
            btnRefund.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRefundPayment(payment);
                }
            });
            
            btnViewUser.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewUserDetails(payment);
                }
            });
        }
        
        private String formatCurrency(double amount) {
            if (amount >= 1000000) {
                return String.format("%.1fM VND", amount / 1000000.0);
            } else if (amount >= 1000) {
                return String.format("%.0fK VND", amount / 1000.0);
            } else {
                return String.format("%.0f VND", amount);
            }
        }
        
        private String getPaymentMethodText(String method) {
            switch (method) {
                case "BANK_TRANSFER": return "🏦 Chuyển khoản";
                case "MOMO": return "📱 MoMo";
                case "ZALOPAY": return "💙 ZaloPay";
                case "VNPAY": return "🔵 VNPay";
                case "CREDIT_CARD": return "💳 Thẻ tín dụng";
                default: return method;
            }
        }
    }
}