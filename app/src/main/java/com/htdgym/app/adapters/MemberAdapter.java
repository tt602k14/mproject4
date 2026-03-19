package com.htdgym.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.htdgym.app.R;
import com.htdgym.app.models.Member;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private List<Member> members = new ArrayList<>();
    private OnMemberClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public interface OnMemberClickListener {
        void onMemberClick(Member member);
        void onMemberLongClick(Member member);
    }

    public void setOnMemberClickListener(OnMemberClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member currentMember = members.get(position);
        holder.bind(currentMember);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void setMembers(List<Member> members) {
        this.members = members;
        notifyDataSetChanged();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPhone, tvMembershipType, tvExpiry, tvStatus;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_member_name);
            tvPhone = itemView.findViewById(R.id.tv_member_phone);
            tvMembershipType = itemView.findViewById(R.id.tv_membership_type);
            tvExpiry = itemView.findViewById(R.id.tv_membership_expiry);
            tvStatus = itemView.findViewById(R.id.tv_member_status);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onMemberClick(members.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onMemberLongClick(members.get(position));
                    return true;
                }
                return false;
            });
        }

        public void bind(Member member) {
            tvName.setText(member.getName());
            tvPhone.setText(member.getPhone());
            tvMembershipType.setText(member.getMembershipType());
            
            if (member.getMembershipExpiry() != null) {
                tvExpiry.setText("Hết hạn: " + dateFormat.format(member.getMembershipExpiry()));
            }
            
            tvStatus.setText(member.isActive() ? "Hoạt động" : "Không hoạt động");
            tvStatus.setTextColor(member.isActive() ? 
                itemView.getContext().getColor(android.R.color.holo_green_light) :
                itemView.getContext().getColor(android.R.color.holo_red_light));
        }
    }
}