package com.htdgym.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.models.PremiumSubscription;
import com.htdgym.app.models.User;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminPremiumUserAdapter extends RecyclerView.Adapter<AdminPremiumUserAdapter.PremiumUserViewHolder> {

    private List<PremiumSubscription> premiumSubscriptions;
    private Map<Integer, User> userMap; // userId -> User
    private OnPremiumUserActionListener listener;

    public interface OnPremiumUserActionListener {
        void onViewDetails(PremiumSubscription subscription, User user);
        void onCancelPremium(PremiumSubscription subscription, User user);
    }

    public AdminPremiumUserAdapter(List<PremiumSubscription> premiumSubscriptions, 
                                   Map<Integer, User> userMap,
                                   OnPremiumUserActionListener listener) {
        this.premiumSubscriptions = premiumSubscriptions;
        this.userMap = userMap;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PremiumUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_premium_user, parent, false);
        return new PremiumUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PremiumUserViewHolder holder, int position) {
        PremiumSubscription subscription = premiumSubscriptions.get(position);
        User user = userMap.get(subscription.getUserId());
        holder.bind(subscription, user);
    }

    @Override
    public int getItemCount() {
        return premiumSubscriptions.size();
    }

    class PremiumUserViewHolder extends RecyclerView.ViewHolder {
        private CardView cardPremiumUser;
        private ImageView ivAvatar;
        private TextView tvUserName, tvUserEmail, tvSubscriptionType, tvPrice, tvEndDate;
        private Button btnViewDetails, btnCancelPremium;

        public PremiumUserViewHolder(@NonNull View itemView) {
            super(itemView);
            cardPremiumUser = itemView.findViewById(R.id.card_premium_user);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserEmail = itemView.findViewById(R.id.tv_user_email);
            tvSubscriptionType = itemView.findViewById(R.id.tv_subscription_type);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvEndDate = itemView.findViewById(R.id.tv_end_date);
            btnViewDetails = itemView.findViewById(R.id.btn_view_details);
            btnCancelPremium = itemView.findViewById(R.id.btn_cancel_premium);
        }

        public void bind(PremiumSubscription subscription, User user) {
            if (user != null) {
                tvUserName.setText(user.getFullName() != null ? user.getFullName() : "Người dùng");
                tvUserEmail.setText(user.getEmail());
            } else {
                tvUserName.setText("Người dùng không tồn tại");
                tvUserEmail.setText("N/A");
            }

            // Subscription type
            String subscriptionType = getSubscriptionTypeDisplay(subscription.getSubscriptionType());
            tvSubscriptionType.setText(subscriptionType);

            // Price formatting
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvPrice.setText(formatter.format(subscription.getPrice()) + "đ");

            // End date formatting
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvEndDate.setText(sdf.format(new Date(subscription.getEndDate())));

            // Check if subscription is expired
            boolean isExpired = subscription.getEndDate() < System.currentTimeMillis();
            if (isExpired) {
                tvEndDate.setTextColor(0xFFFF6B6B); // Red for expired
                btnCancelPremium.setText("Đã hết hạn");
                btnCancelPremium.setEnabled(false);
            } else {
                tvEndDate.setTextColor(0xFF6FCF97); // Green for active
                btnCancelPremium.setText("Hủy Premium");
                btnCancelPremium.setEnabled(true);
            }

            // Click listeners
            btnViewDetails.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewDetails(subscription, user);
                }
            });

            btnCancelPremium.setOnClickListener(v -> {
                if (listener != null && !isExpired) {
                    listener.onCancelPremium(subscription, user);
                }
            });

            cardPremiumUser.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewDetails(subscription, user);
                }
            });
        }

        private String getSubscriptionTypeDisplay(String type) {
            switch (type) {
                case "monthly":
                    return "1 Tháng";
                case "7months":
                    return "7 Tháng";
                case "yearly":
                    return "1 Năm";
                default:
                    return type;
            }
        }
    }

    public void updateData(List<PremiumSubscription> newSubscriptions, Map<Integer, User> newUserMap) {
        this.premiumSubscriptions = newSubscriptions;
        this.userMap = newUserMap;
        notifyDataSetChanged();
    }
}