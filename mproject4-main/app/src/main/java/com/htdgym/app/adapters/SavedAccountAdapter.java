package com.htdgym.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.htdgym.app.R;
import com.htdgym.app.utils.AccountManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SavedAccountAdapter extends RecyclerView.Adapter<SavedAccountAdapter.AccountViewHolder> {
    
    private Context context;
    private List<AccountManager.SavedAccount> accounts;
    private OnAccountClickListener listener;
    
    public interface OnAccountClickListener {
        void onAccountClick(AccountManager.SavedAccount account);
        void onAccountRemove(AccountManager.SavedAccount account);
    }
    
    public SavedAccountAdapter(Context context, List<AccountManager.SavedAccount> accounts) {
        this.context = context;
        this.accounts = accounts;
    }
    
    public void setOnAccountClickListener(OnAccountClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_saved_account, parent, false);
        return new AccountViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        AccountManager.SavedAccount account = accounts.get(position);
        
        // Set email
        holder.tvEmail.setText(account.email);
        
        // Set name or email if name is empty
        if (account.name != null && !account.name.trim().isEmpty()) {
            holder.tvName.setText(account.name);
        } else {
            holder.tvName.setText(account.email.split("@")[0]);
        }
        
        // Set last login time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String lastLogin = sdf.format(new Date(account.lastLoginTime));
        holder.tvLastLogin.setText("Lần cuối: " + lastLogin);
        
        // Set profile image (placeholder for now)
        holder.ivProfile.setImageResource(R.drawable.ic_person);
        
        // Click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAccountClick(account);
            }
        });
        
        holder.ivRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAccountRemove(account);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return accounts.size();
    }
    
    public void updateAccounts(List<AccountManager.SavedAccount> newAccounts) {
        this.accounts = newAccounts;
        notifyDataSetChanged();
    }
    
    static class AccountViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile, ivRemove;
        TextView tvName, tvEmail, tvLastLogin;
        
        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            ivRemove = itemView.findViewById(R.id.iv_remove);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvLastLogin = itemView.findViewById(R.id.tv_last_login);
        }
    }
}