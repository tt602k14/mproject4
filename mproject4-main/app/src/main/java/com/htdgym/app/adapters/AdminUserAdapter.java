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
import com.htdgym.app.models.User;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {
    
    private List<User> users;
    private OnUserActionListener listener;
    
    public interface OnUserActionListener {
        void onUserClick(User user);
        void onToggleUserStatus(User user);
        void onDeleteUser(User user);
    }
    
    public AdminUserAdapter(List<User> users, OnUserActionListener listener) {
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
        private TextView tvUserName, tvUserEmail, tvUserId, tvUserStatus, tvJoinDate;
        private Button btnToggleStatus, btnDeleteUser;
        
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            cardUser = itemView.findViewById(R.id.card_user);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserEmail = itemView.findViewById(R.id.tv_user_email);
            tvUserId = itemView.findViewById(R.id.tv_user_id);
            tvUserStatus = itemView.findViewById(R.id.tv_user_status);
            tvJoinDate = itemView.findViewById(R.id.tv_join_date);
            btnToggleStatus = itemView.findViewById(R.id.btn_toggle_status);
            btnDeleteUser = itemView.findViewById(R.id.btn_delete_user);
        }
        
        public void bind(User user) {
            tvUserName.setText(user.getFullName());
            tvUserEmail.setText(user.getEmail());
            tvUserId.setText("ID: " + user.getUserId());
            
            // Status
            if (user.isActive()) {
                tvUserStatus.setText("🟢 Hoạt động");
                tvUserStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.green_primary));
                btnToggleStatus.setText("Tạm khóa");
                btnToggleStatus.setBackgroundTintList(
                        itemView.getContext().getResources().getColorStateList(android.R.color.holo_orange_light));
            } else {
                tvUserStatus.setText("🔴 Đã khóa");
                tvUserStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_light));
                btnToggleStatus.setText("Kích hoạt");
                btnToggleStatus.setBackgroundTintList(
                        itemView.getContext().getResources().getColorStateList(R.color.green_primary));
            }
            
            // Join date
            if (user.getCreatedAt() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                tvJoinDate.setText("Tham gia: " + sdf.format(new java.util.Date(user.getCreatedAt())));
            } else {
                tvJoinDate.setText("Tham gia: --");
            }
            
            // Click listeners
            cardUser.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUserClick(user);
                }
            });
            
            btnToggleStatus.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onToggleUserStatus(user);
                }
            });
            
            btnDeleteUser.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteUser(user);
                }
            });
        }
    }
}