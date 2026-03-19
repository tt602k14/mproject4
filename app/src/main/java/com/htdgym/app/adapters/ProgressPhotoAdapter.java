package com.htdgym.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.models.ProgressPhoto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressPhotoAdapter extends RecyclerView.Adapter<ProgressPhotoAdapter.PhotoViewHolder> {

    private List<ProgressPhoto> progressPhotos;
    private OnPhotoClickListener onPhotoClickListener;
    private Context context;

    public interface OnPhotoClickListener {
        void onPhotoClick(ProgressPhoto photo);
    }

    public ProgressPhotoAdapter(List<ProgressPhoto> progressPhotos, OnPhotoClickListener listener) {
        this.progressPhotos = progressPhotos;
        this.onPhotoClickListener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_progress_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        ProgressPhoto photo = progressPhotos.get(position);
        
        // Load image from file path
        File imageFile = new File(photo.getPhotoPath());
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(photo.getPhotoPath());
            if (bitmap != null) {
                holder.imagePhoto.setImageBitmap(bitmap);
            } else {
                holder.imagePhoto.setImageResource(R.drawable.ic_image_placeholder);
            }
        } else {
            holder.imagePhoto.setImageResource(R.drawable.ic_image_placeholder);
        }
        
        // Format date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tvDate.setText(sdf.format(new Date(photo.getDate())));
        
        // Show weight if available
        if (photo.getWeight() > 0) {
            holder.tvWeight.setText(String.format("%.1f kg", photo.getWeight()));
            holder.tvWeight.setVisibility(View.VISIBLE);
        } else {
            holder.tvWeight.setVisibility(View.GONE);
        }
        
        // Click listener
        holder.cardPhoto.setOnClickListener(v -> {
            if (onPhotoClickListener != null) {
                onPhotoClickListener.onPhotoClick(photo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return progressPhotos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        CardView cardPhoto;
        ImageView imagePhoto;
        TextView tvDate, tvWeight;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            cardPhoto = itemView.findViewById(R.id.card_photo);
            imagePhoto = itemView.findViewById(R.id.image_photo);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvWeight = itemView.findViewById(R.id.tv_weight);
        }
    }
}