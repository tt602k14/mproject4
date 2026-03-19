package com.htdgym.app.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.htdgym.app.R;
import com.htdgym.app.activities.LoginActivity;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.dialogs.ChangePasswordDialog;
import com.htdgym.app.dialogs.EditProfileDialog;
import com.htdgym.app.dialogs.LanguageDialog;
import com.htdgym.app.dialogs.UnitsDialog;
import com.htdgym.app.models.User;
import com.htdgym.app.utils.SharedPrefManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName, tvProfileEmail, tvLanguageValue, tvUnitsValue;
    private ImageView imgAvatar;
    private TextView tvAvatarInitials;
    private Button btnEditProfile;
    private LinearLayout layoutChangePassword, layoutPrivacy;
    private LinearLayout layoutNotifications, layoutLanguage, layoutUnits, layoutTheme;
    private LinearLayout layoutHelp, layoutFeedback, layoutAbout;
    private Button btnLogout;
    private User currentUser;
    
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private Uri tempCameraUri;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        saveAvatarImage(imageUri);
                    }
                }
            }
        );
        
        // Initialize camera launcher
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (success && tempCameraUri != null) {
                    saveAvatarImage(tempCameraUri);
                }
            }
        );
        
        // Initialize permission launcher
        permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    showAvatarOptionsDialog();
                } else {
                    Toast.makeText(requireContext(), "Cần quyền để thay đổi avatar", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        initViews(view);
        loadUserData();
        setupClickListeners();
        
        return view;
    }

    private void initViews(View view) {
        tvProfileName = view.findViewById(R.id.tv_profile_name);
        tvProfileEmail = view.findViewById(R.id.tv_profile_email);
        imgAvatar = view.findViewById(R.id.img_avatar);
        tvAvatarInitials = view.findViewById(R.id.tv_avatar_initials);
        
        // Optional TextViews - only if they exist in layout
        int languageValueId = getResources().getIdentifier("tv_language_value", "id", requireContext().getPackageName());
        int unitsValueId = getResources().getIdentifier("tv_units_value", "id", requireContext().getPackageName());
        
        if (languageValueId != 0) {
            tvLanguageValue = view.findViewById(languageValueId);
        }
        if (unitsValueId != 0) {
            tvUnitsValue = view.findViewById(unitsValueId);
        }
        
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        
        layoutChangePassword = view.findViewById(R.id.layout_change_password);
        layoutPrivacy = view.findViewById(R.id.layout_privacy);
        
        layoutNotifications = view.findViewById(R.id.layout_notifications);
        layoutLanguage = view.findViewById(R.id.layout_language);
        layoutUnits = view.findViewById(R.id.layout_units);
        layoutTheme = view.findViewById(R.id.layout_theme);
        
        layoutHelp = view.findViewById(R.id.layout_help);
        layoutFeedback = view.findViewById(R.id.layout_feedback);
        layoutAbout = view.findViewById(R.id.layout_about);
        
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    private void loadUserData() {
        SharedPrefManager prefManager = SharedPrefManager.getInstance(requireContext());
        String userId = prefManager.getUserId();
        
        // Load avatar if exists
        loadAvatarImage();
        
        if (userId != null) {
            new Thread(() -> {
                try {
                    GymDatabase db = GymDatabase.getInstance(requireContext());
                    currentUser = db.userDao().getUserById(Integer.parseInt(userId));
                    
                    requireActivity().runOnUiThread(() -> {
                        if (currentUser != null) {
                            tvProfileName.setText(currentUser.getName());
                            tvProfileEmail.setText(currentUser.getEmail());
                        } else {
                            tvProfileName.setText(prefManager.getUserName());
                            tvProfileEmail.setText(prefManager.getUserEmail());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() -> {
                        tvProfileName.setText(prefManager.getUserName());
                        tvProfileEmail.setText(prefManager.getUserEmail());
                    });
                }
            }).start();
        } else {
            tvProfileName.setText(prefManager.getUserName());
            tvProfileEmail.setText(prefManager.getUserEmail());
        }
        
        // Load language and units settings
        loadSettings();
    }
    
    private void loadSettings() {
        SharedPrefManager prefManager = SharedPrefManager.getInstance(requireContext());
        
        // Load language
        String language = prefManager.getSharedPreferences().getString("selected_language", "vi");
        String languageDisplay = "Tiếng Việt";
        switch (language) {
            case "en": languageDisplay = "English"; break;
            case "fr": languageDisplay = "Français"; break;
            case "de": languageDisplay = "Deutsch"; break;
        }
        if (tvLanguageValue != null) {
            tvLanguageValue.setText(languageDisplay);
        }
        
        // Load units
        String units = prefManager.getSharedPreferences().getString("units_system", "metric");
        String unitsDisplay = units.equals("metric") ? "Hệ mét (kg, cm)" : "Hệ Anh (lb, in)";
        if (tvUnitsValue != null) {
            tvUnitsValue.setText(unitsDisplay);
        }
    }
    
    private void loadAvatarImage() {
        try {
            File avatarFile = new File(requireContext().getFilesDir(), "avatar.jpg");
            if (avatarFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(avatarFile.getAbsolutePath());
                if (bitmap != null) {
                    imgAvatar.setImageBitmap(bitmap);
                    imgAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (tvAvatarInitials != null) {
                        tvAvatarInitials.setVisibility(View.GONE);
                    }
                } else {
                    showInitials();
                }
            } else {
                showInitials();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showInitials();
        }
    }
    
    private void showInitials() {
        if (tvAvatarInitials != null && currentUser != null) {
            String name = currentUser.getName();
            if (name != null && !name.isEmpty()) {
                String initials = "";
                String[] parts = name.trim().split("\\s+");
                if (parts.length >= 2) {
                    initials = parts[0].substring(0, 1).toUpperCase() + 
                              parts[parts.length - 1].substring(0, 1).toUpperCase();
                } else {
                    initials = name.substring(0, Math.min(2, name.length())).toUpperCase();
                }
                tvAvatarInitials.setText(initials);
                tvAvatarInitials.setVisibility(View.VISIBLE);
            }
        }
    }
    
    private void saveAvatarImage(Uri imageUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            
            if (bitmap != null) {
                // Resize bitmap to save space
                int maxSize = 500;
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float scale = Math.min(((float) maxSize / width), ((float) maxSize / height));
                
                int newWidth = Math.round(width * scale);
                int newHeight = Math.round(height * scale);
                
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                
                // Save to internal storage
                File avatarFile = new File(requireContext().getFilesDir(), "avatar.jpg");
                FileOutputStream fos = new FileOutputStream(avatarFile);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
                
                // Update ImageView
                imgAvatar.setImageBitmap(resizedBitmap);
                imgAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (tvAvatarInitials != null) {
                    tvAvatarInitials.setVisibility(View.GONE);
                }
                
                Toast.makeText(requireContext(), "Đã cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showAvatarOptionsDialog() {
        String[] options = {"Chụp ảnh", "Chọn từ thư viện", "Xóa ảnh"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Thay đổi ảnh đại diện");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Camera
                    openCamera();
                    break;
                case 1: // Gallery
                    openImagePicker();
                    break;
                case 2: // Remove
                    removeAvatar();
                    break;
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    
    private void openCamera() {
        // Check camera permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.CAMERA);
            return;
        }
        
        try {
            // Create temp file for camera image
            File photoFile = new File(requireContext().getCacheDir(), "temp_avatar.jpg");
            tempCameraUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".fileprovider",
                photoFile
            );
            cameraLauncher.launch(tempCameraUri);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Không thể mở camera", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void removeAvatar() {
        try {
            File avatarFile = new File(requireContext().getFilesDir(), "avatar.jpg");
            if (avatarFile.exists()) {
                avatarFile.delete();
            }
            
            // Reset to default
            imgAvatar.setImageResource(0);
            showInitials();
            
            Toast.makeText(requireContext(), "Đã xóa ảnh đại diện", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Lỗi khi xóa ảnh", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void openImagePicker() {
        // Check permission based on Android version
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        } else {
            permissionLauncher.launch(permission);
        }
    }

    private void setupClickListeners() {
        // Avatar click to show options dialog
        imgAvatar.setOnClickListener(v -> showAvatarOptionsDialog());
        
        btnEditProfile.setOnClickListener(v -> openEditProfileDialog());
        
        layoutChangePassword.setOnClickListener(v -> openChangePasswordDialog());
        
        layoutPrivacy.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
        
        layoutNotifications.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
        
        layoutLanguage.setOnClickListener(v -> openLanguageDialog());
        
        layoutUnits.setOnClickListener(v -> openUnitsDialog());
        
        layoutTheme.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
        
        layoutHelp.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Liên hệ: support@htdgym.com", Toast.LENGTH_LONG).show();
        });
        
        layoutFeedback.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Gửi feedback tới: feedback@htdgym.com", Toast.LENGTH_LONG).show();
        });
        
        layoutAbout.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "HTD Gym App v1.0.0\n© 2026 HTD Gym\nPhát triển bởi HTD Team", 
                    Toast.LENGTH_LONG).show();
        });
        
        btnLogout.setOnClickListener(v -> logout());
    }
    
    private void openEditProfileDialog() {
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            return;
        }
        
        EditProfileDialog dialog = new EditProfileDialog(requireContext(), currentUser, 
            (name, email) -> {
                tvProfileName.setText(name);
                tvProfileEmail.setText(email);
            });
        dialog.show();
    }
    
    private void openChangePasswordDialog() {
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!"email".equals(currentUser.getLoginType())) {
            Toast.makeText(requireContext(), "Chỉ tài khoản email mới có thể đổi mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        
        ChangePasswordDialog dialog = new ChangePasswordDialog(requireContext(), currentUser);
        dialog.show();
    }
    
    private void openLanguageDialog() {
        LanguageDialog dialog = new LanguageDialog(requireContext(), language -> {
            if (tvLanguageValue != null) {
                tvLanguageValue.setText(language);
            }
        });
        dialog.show();
    }
    
    private void openUnitsDialog() {
        UnitsDialog dialog = new UnitsDialog(requireContext());
        dialog.setOnDismissListener(d -> loadSettings());
        dialog.show();
    }

    private void logout() {
        // Clear user session
        SharedPrefManager.getInstance(requireContext()).logout();
        
        Toast.makeText(requireContext(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
        
        // Navigate to login screen
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
