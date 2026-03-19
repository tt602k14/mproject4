package com.htdgym.app.api;

public class ApiConfig {
    // Thay đổi IP này thành IP máy tính chạy XAMPP của bạn
    // Nếu test trên emulator: dùng 10.0.2.2
    // Nếu test trên thiết bị thật: dùng IP local của máy (vd: 192.168.1.100)
    public static final String BASE_URL = "http://10.0.2.2/htd_gym/backend/api/";
    
    // Endpoints
    public static final String WORKOUTS = "workouts.php";
    public static final String MEMBERS = "members.php";
    public static final String USER_STATS = "user_stats.php";
    public static final String PAYMENTS = "payments.php";
    public static final String EQUIPMENT = "equipment.php";
    public static final String VIDEOS = "videos.php";
    
    public static String getUrl(String endpoint) {
        return BASE_URL + endpoint;
    }
}
