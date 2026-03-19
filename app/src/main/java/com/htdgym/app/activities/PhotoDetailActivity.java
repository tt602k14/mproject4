package com.htdgym.app.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.htdgym.app.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PhotoDetailActivity extends AppCompatActivity {

    private ImageView btnBack, imagePhoto;
    private TextView tvDate, tvWeight, tvNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        initViews();
        loadPhotoData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        imagePhoto = findViewById(R.id.image_photo);
        tvDate = findViewById(R.id.tv_date);
        tvWeight = findViewById(R.id.tv_weight);
        tvNotes = findViewById(R.id.tv_notes);

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadPhotoData() {
        String photoPath = getIntent().getStringExtra("photo_path");
        long photoDate = getIntent().getLongExtra("photo_date", 0);
        float photoWeight = getIntent().getFloatExtra("photo_weight", 0);
        String photoNotes = getIntent().getStringExtra("photo_notes");

        // Load image
        if (photoPath != null) {
            File imageFile = new File(photoPath);
            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                if (bitmap != null) {
                    imagePhoto.setImageBitmap(bitmap);
                }
            }
        }

        // Set date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        tvDate.setText(sdf.format(new Date(photoDate)));

        // Set weight
        if (photoWeight > 0) {
            tvWeight.setText(String.format("Cân nặng: %.1f kg", photoWeight));
        } else {
            tvWeight.setText("Chưa ghi nhận cân nặng");
        }

        // Set notes
        if (photoNotes != null && !photoNotes.trim().isEmpty()) {
            tvNotes.setText(photoNotes);
        } else {
            tvNotes.setText("Không có ghi chú");
        }
    }
}