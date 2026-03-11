package com.htdgym.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.htdgym.app.R;
import com.htdgym.app.models.Payment;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private List<Payment> payments = new ArrayList<>();
    private OnPaymentClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public interface OnPaymentClickListener {
        void onPaymentClick(Payment payment);
        void onPaymentLongClick(Payment payment);
    }

    public void setOnPaymentClickListener(OnPaymentClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment currentPayment = payments.get(position);
        holder.bind(currentPayment);
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
        notifyDataSetChanged();
    }

    class PaymentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAmount, tvDescription, tvPaymentMethod, tvType, tvDate;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tv_payment_amount);
            tvDescription = itemView.findViewById(R.id.tv_payment_description);
            tvPaymentMethod = itemView.findViewById(R.id.tv_payment_method);
            tvType = itemView.findViewById(R.id.tv_payment_type);
            tvDate = itemView.findViewById(R.id.tv_payment_date);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onPaymentClick(payments.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onPaymentLongClick(payments.get(position));
                    return true;
                }
                return false;
            });
        }

        public void bind(Payment payment) {
            tvAmount.setText(String.format("%.0fđ", payment.getAmount()));
            tvDescription.setText(payment.getDescription());
            tvPaymentMethod.setText(payment.getPaymentMethod());
            tvType.setText(payment.getType());
            
            if (payment.getPaymentDate() != null) {
                tvDate.setText(dateFormat.format(payment.getPaymentDate()));
            }
        }
    }
}