package com.htdgym.app.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.R;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.ProgressPhoto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhotoComparisonActivity extends AppCompatActivity {

    private ImageView btnBack, imageOld, imageNew;
    private TextView tvOldDate, tvNewDate, tvWeightDiff, tvTimeDiff;
    private GymDatabase database;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_comparison);

        database = GymDatabase.getInstance(this);
        userId = getIntent().getIntExtra("user_id", 1);

        initViews();
        loadComparisonPhotos();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        imageOld = findViewById(R.id.image_old);
        imageNew = findViewById(R.id.image_new);
        tvOldDate = findViewById(R.id.tv_old_date);
        tvNewDate = findViewById(R.id.tv_new_date);
        tvWeightDiff = findViewById(R.id.tv_weight_diff);
        tvTimeDiff = findViewById(R.id.tv_time_diff);

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadComparisonPhotos() {
        new Thread(() -> {
            try {
                List<ProgressPhoto> photos = database.progressPhotoDao().getPhotosByUserId(userId);
                
                if (photos.size() >= 2) {
                    ProgressPhoto oldPhoto = photos.get(photos.size() - 1); // Oldest
                    ProgressPhoto newPhoto = photos.get(0); // Newest
                    
                    runOnUiThread(() -> displayComparison(oldPhoto, newPhoto));
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Cần ít nhất 2 ảnh để so sánh", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi khi tải ảnh", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }).start();
    }

    private void displayComparison(ProgressPhoto oldPhoto, ProgressPhoto newPhoto) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        
        // Load old photo
        File oldFile = new File(oldPhoto.getPhotoPath());
        if (oldFile.exists()) {
            Bitmap oldBitmap = BitmapFactory.decodeFile(oldPhoto.getPhotoPath());
            if (oldBitmap != null) {
                imageOld.setImageBitmap(oldBitmap);
            }
        }
        tvOldDate.setText("Trước: " + sdf.format(new Date(oldPhoto.getDate())));
        
        // Load new photo
        File newFile = new File(newPhoto.getPhotoPath());
        if (newFile.exists()) {
            Bitmap newBitmap = BitmapFactory.decodeFile(newPhoto.getPhotoPath());
            if (newBitmap != null) {
                imageNew.setImageBitmap(newBitmap);
            }
        }
        tvNewDate.setText("Sau: " + sdf.format(new Date(newPhoto.getDate())));
        
        // Calculate differences
        if (oldPhoto.getWeight() > 0 && newPhoto.getWeight() > 0) {
            float weightDiff = newPhoto.getWeight() - oldPhoto.getWeight();
            if (weightDiff > 0) {
                tvWeightDiff.setText(String.format("📈 Tăng %.1f kg", weightDiff));
                tvWeightDiff.setTextColor(getColor(R.color.danger));
            } else if (weightDiff < 0) {
                tvWeightDiff.setText(String.format("📉 Giảm %.1f kg", Math.abs(weightDiff)));
                tvWeightDiff.setTextColor(getColor(R.color.primary));
            } else {
                tvWeightDiff.setText("➡️ Không đổi");
                tvWeightDiff.setTextColor(getColor(R.color.gray));
            }
        } else {
            tvWeightDiff.setText("⚖️ Chưa có dữ liệu cân nặng");
            tvWeightDiff.setTextColor(getColor(R.color.gray));
        }
        
        // Calculate time difference
        long timeDiff = newPhoto.getDate() - oldPhoto.getDate();
        long daysDiff = timeDiff / (1000 * 60 * 60 * 24);
        tvTimeDiff.setText(String.format("⏱️ Khoảng cách: %d ngày", daysDiff));
    }
}