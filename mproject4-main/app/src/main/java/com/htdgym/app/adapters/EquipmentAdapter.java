package com.htdgym.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.htdgym.app.R;
import com.htdgym.app.models.Equipment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder> {
    private List<Equipment> equipment = new ArrayList<>();
    private OnEquipmentClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public interface OnEquipmentClickListener {
        void onEquipmentClick(Equipment equipment);
        void onEquipmentLongClick(Equipment equipment);
    }

    public void setOnEquipmentClickListener(OnEquipmentClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EquipmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_equipment, parent, false);
        return new EquipmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentViewHolder holder, int position) {
        Equipment currentEquipment = equipment.get(position);
        holder.bind(currentEquipment);
    }

    @Override
    public int getItemCount() {
        return equipment.size();
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
        notifyDataSetChanged();
    }

    class EquipmentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvCategory, tvBrand, tvCondition, tvLocation, tvStatus;

        public EquipmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_equipment_name);
            tvCategory = itemView.findViewById(R.id.tv_equipment_category);
            tvBrand = itemView.findViewById(R.id.tv_equipment_brand);
            tvCondition = itemView.findViewById(R.id.tv_equipment_condition);
            tvLocation = itemView.findViewById(R.id.tv_equipment_location);
            tvStatus = itemView.findViewById(R.id.tv_equipment_status);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onEquipmentClick(equipment.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onEquipmentLongClick(equipment.get(position));
                    return true;
                }
                return false;
            });
        }

        public void bind(Equipment equipment) {
            tvName.setText(equipment.getName());
            tvCategory.setText(equipment.getCategory());
            tvBrand.setText(equipment.getBrand());
            tvCondition.setText(equipment.getCondition());
            tvLocation.setText("Vị trí: " + equipment.getLocation());
            
            tvStatus.setText(equipment.isAvailable() ? "Khả dụng" : "Không khả dụng");
            tvStatus.setTextColor(equipment.isAvailable() ? 
                itemView.getContext().getColor(android.R.color.holo_green_light) :
                itemView.getContext().getColor(android.R.color.holo_red_light));
        }
    }
}