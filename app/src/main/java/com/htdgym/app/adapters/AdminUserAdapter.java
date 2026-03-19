package com.htdgym.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {

    private List<User> users;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public AdminUserAdapter(List<User> users, OnUserClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private CardView cardUser;
        private ImageView ivAvatar, ivPremium;
        private TextView tvName, tvUsername, tvEmail, tvJoinDate, tvStatus;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            cardUser = itemView.findViewById(R.id.card_user);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivPremium = itemView.findViewById(R.id.iv_premium);
            tvName = itemView.findViewById(R.id.tv_name);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvJoinDate = itemView.findViewById(R.id.tv_join_date);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }

        public void bind(User user) {
            tvName.setText(user.getFullName());
            tvUsername.setText(user.getEmail()); // Hiển thị email
            tvEmail.setVisibility(View.GONE); // Ẩn trường email thừa
            
            // Format join date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvJoinDate.setText("Tham gia: " + sdf.format(new Date(user.getCreatedAt())));
            
            // Set status (you can customize this based on your logic)
            tvStatus.setText("Hoạt động");
            tvStatus.setTextColor(0xFF6FCF97);
            
            // Show premium badge (you'll need to check premium status)
            // For now, randomly show some as premium
            if (user.getId() % 3 == 0) {
                ivPremium.setVisibility(View.VISIBLE);
            } else {
                ivPremium.setVisibility(View.GONE);
            }
            
            cardUser.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUserClick(user);
                }
            });
        }
    }
}