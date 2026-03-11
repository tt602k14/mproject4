package com.htdgym.app.api;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiClient {
    
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    
    public interface ApiCallback {
        void onSuccess(String response);
        void onError(String error);
    }
    
    public static void get(String endpoint, ApiCallback callback) {
        get(endpoint, null, callback);
    }
    
    public static void get(String endpoint, String params, ApiCallback callback) {
        executor.execute(() -> {
            try {
                String urlString = ApiConfig.getUrl(endpoint);
                if (params != null && !params.isEmpty()) {
                    urlString += "?" + params;
                }
                
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                
                int responseCode = conn.getResponseCode();
                
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    String finalResponse = response.toString();
                    mainHandler.post(() -> callback.onSuccess(finalResponse));
                } else {
                    mainHandler.post(() -> callback.onError("HTTP Error: " + responseCode));
                }
                
                conn.disconnect();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }
    
    public static void post(String endpoint, JSONObject data, ApiCallback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(ApiConfig.getUrl(endpoint));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                
                OutputStream os = conn.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.close();
                
                int responseCode = conn.getResponseCode();
                
                BufferedReader reader;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                String finalResponse = response.toString();
                
                // Check if response is valid JSON
                if (!finalResponse.trim().startsWith("{") && !finalResponse.trim().startsWith("[")) {
                    mainHandler.post(() -> callback.onError("Server trả về HTML thay vì JSON. Kiểm tra:\n1. MySQL đã start chưa?\n2. Database đã tạo chưa?\n3. Backend đã copy đúng chỗ chưa?"));
                    return;
                }
                
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    mainHandler.post(() -> callback.onSuccess(finalResponse));
                } else {
                    mainHandler.post(() -> callback.onError(finalResponse));
                }
                
                conn.disconnect();
            } catch (java.net.ConnectException e) {
                mainHandler.post(() -> callback.onError("Không kết nối được server.\nKiểm tra:\n1. XAMPP Apache đã start?\n2. Backend đã copy vào htdocs?\n3. IP đúng chưa? (10.0.2.2 cho emulator)"));
            } catch (java.net.UnknownHostException e) {
                mainHandler.post(() -> callback.onError("Không tìm thấy server.\nKiểm tra IP trong ApiConfig.java"));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError("Lỗi: " + e.getMessage()));
            }
        });
    }
    
    public static void put(String endpoint, JSONObject data, ApiCallback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(ApiConfig.getUrl(endpoint));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                
                OutputStream os = conn.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.close();
                
                int responseCode = conn.getResponseCode();
                
                BufferedReader reader;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                String finalResponse = response.toString();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    mainHandler.post(() -> callback.onSuccess(finalResponse));
                } else {
                    mainHandler.post(() -> callback.onError(finalResponse));
                }
                
                conn.disconnect();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }
    
    public static void delete(String endpoint, JSONObject data, ApiCallback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(ApiConfig.getUrl(endpoint));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                
                OutputStream os = conn.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.close();
                
                int responseCode = conn.getResponseCode();
                
                BufferedReader reader;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                String finalResponse = response.toString();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    mainHandler.post(() -> callback.onSuccess(finalResponse));
                } else {
                    mainHandler.post(() -> callback.onError(finalResponse));
                }
                
                conn.disconnect();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }
}
