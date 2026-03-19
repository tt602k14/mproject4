package com.htdgym.app.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.ProgressPhotoAdapter;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.ProgressPhoto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressPhotosActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICK = 102;

    private ImageView btnBack;
    private Button btnTakePhoto, btnSelectPhoto, btnComparePhotos;
    private TextView tvPhotoCount, tvLatestDate;
    private RecyclerView recyclerPhotos;
    private CardView cardEmptyState;
    
    private ProgressPhotoAdapter adapter;
    private List<ProgressPhoto> progressPhotos;
    private GymDatabase database;
    private String currentPhotoPath;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_photos);

        database = GymDatabase.getInstance(this);
        userId = getSharedPreferences("HTDGymPrefs", MODE_PRIVATE).getInt("user_id", 1);
        
        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadProgressPhotos();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnSelectPhoto = findViewById(R.id.btn_select_photo);
        btnComparePhotos = findViewById(R.id.btn_compare_photos);
        tvPhotoCount = findViewById(R.id.tv_photo_count);
        tvLatestDate = findViewById(R.id.tv_latest_date);
        recyclerPhotos = findViewById(R.id.recycler_photos);
        cardEmptyState = findViewById(R.id.card_empty_state);
        
        progressPhotos = new ArrayList<>();
    }

    private void setupRecyclerView() {
        recyclerPhotos.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProgressPhotoAdapter(progressPhotos, this::onPhotoClick);
        recyclerPhotos.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnTakePhoto.setOnClickListener(v -> checkCameraPermissionAndTakePhoto());
        
        btnSelectPhoto.setOnClickListener(v -> selectPhotoFromGallery());
        
        btnComparePhotos.setOnClickListener(v -> comparePhotos());
    }

    private void checkCameraPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            
            // Create file for the photo
            File photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                    "com.htdgym.app.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(this, "Không thể mở camera", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "PROGRESS_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        
        try {
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);
            currentPhotoPath = image.getAbsolutePath();
            return image;
        } catch (IOException ex) {
            Toast.makeText(this, "Lỗi tạo file ảnh", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    if (currentPhotoPath != null) {
                        saveProgressPhoto(currentPhotoPath);
                    }
                    break;
                    
                case REQUEST_IMAGE_PICK:
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        saveProgressPhotoFromUri(selectedImageUri);
                    }
                    break;
            }
        }
    }

    private void saveProgressPhoto(String photoPath) {
        showWeightInputDialog(photoPath);
    }

    private void saveProgressPhotoFromUri(Uri imageUri) {
        // Copy image to app directory and save
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            String savedPath = saveBitmapToFile(bitmap);
            if (savedPath != null) {
                showWeightInputDialog(savedPath);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveBitmapToFile(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "PROGRESS_" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, fileName);
        
        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            return null;
        }
    }

    private void showWeightInputDialog(String photoPath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("📸 Lưu ảnh tiến độ");
        builder.setMessage("Nhập cân nặng hiện tại (tùy chọn):");
        
        android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("VD: 70.5");
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);
        
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String weightStr = input.getText().toString().trim();
            float weight = 0;
            if (!weightStr.isEmpty()) {
                try {
                    weight = Float.parseFloat(weightStr);
                } catch (NumberFormatException e) {
                    weight = 0;
                }
            }
            
            saveProgressPhotoToDatabase(photoPath, weight);
        });
        
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            // Delete the photo file if user cancels
            new File(photoPath).delete();
        });
        
        builder.show();
    }

    private void saveProgressPhotoToDatabase(String photoPath, float weight) {
        ProgressPhoto progressPhoto = new ProgressPhoto();
        progressPhoto.setUserId(userId);
        progressPhoto.setPhotoPath(photoPath);
        progressPhoto.setWeight(weight);
        progressPhoto.setDate(System.currentTimeMillis());
        progressPhoto.setNotes("");
        progressPhoto.setBodyPart("front");
        
        new Thread(() -> {
            try {
                database.progressPhotoDao().insert(progressPhoto);
                runOnUiThread(() -> {
                    Toast.makeText(this, "✅ Đã lưu ảnh tiến độ!", Toast.LENGTH_SHORT).show();
                    loadProgressPhotos();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "❌ Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void loadProgressPhotos() {
        new Thread(() -> {
            List<ProgressPhoto> photos = database.progressPhotoDao().getPhotosByUserId(userId);
            runOnUiThread(() -> {
                progressPhotos.clear();
                progressPhotos.addAll(photos);
                adapter.notifyDataSetChanged();
                
                updateUI();
            });
        }).start();
    }

    private void updateUI() {
        if (progressPhotos.isEmpty()) {
            cardEmptyState.setVisibility(CardView.VISIBLE);
            recyclerPhotos.setVisibility(RecyclerView.GONE);
            btnComparePhotos.setEnabled(false);
            tvPhotoCount.setText("0 ảnh");
            tvLatestDate.setText("Chưa có ảnh nào");
        } else {
            cardEmptyState.setVisibility(CardView.GONE);
            recyclerPhotos.setVisibility(RecyclerView.VISIBLE);
            btnComparePhotos.setEnabled(progressPhotos.size() >= 2);
            
            tvPhotoCount.setText(progressPhotos.size() + " ảnh");
            
            if (!progressPhotos.isEmpty()) {
                ProgressPhoto latest = progressPhotos.get(0); // Assuming sorted by date desc
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                tvLatestDate.setText("Ảnh mới nhất: " + sdf.format(new Date(latest.getDate())));
            }
        }
    }

    private void onPhotoClick(ProgressPhoto photo) {
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra("photo_path", photo.getPhotoPath());
        intent.putExtra("photo_date", photo.getDate());
        intent.putExtra("photo_weight", photo.getWeight());
        intent.putExtra("photo_notes", photo.getNotes());
        startActivity(intent);
    }

    private void comparePhotos() {
        if (progressPhotos.size() < 2) {
            Toast.makeText(this, "Cần ít nhất 2 ảnh để so sánh", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Intent intent = new Intent(this, PhotoComparisonActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "Cần quyền camera để chụp ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }
}