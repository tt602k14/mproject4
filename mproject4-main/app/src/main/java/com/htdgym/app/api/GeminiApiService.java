package com.htdgym.app.api;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GeminiApiService {
    
    // Gemini API key - Người dùng cần thay thế bằng key của mình
    // Lấy free tại: https://makersuite.google.com/app/apikey
    private static final String API_KEY = "YOUR_GEMINI_API_KEY_HERE";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY;
    
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    
    public interface GeminiCallback {
        void onSuccess(String response);
        void onError(String error);
    }
    
    public static void sendMessage(String userMessage, String context, GeminiCallback callback) {
        executor.execute(() -> {
            try {
                // Build prompt with context
                String fullPrompt = buildPrompt(userMessage, context);
                
                // Create request body
                JSONObject requestBody = new JSONObject();
                JSONArray contents = new JSONArray();
                JSONObject content = new JSONObject();
                JSONArray parts = new JSONArray();
                JSONObject part = new JSONObject();
                
                part.put("text", fullPrompt);
                parts.put(part);
                content.put("parts", parts);
                contents.put(content);
                requestBody.put("contents", contents);
                
                // Make API call
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                
                // Send request
                OutputStream os = conn.getOutputStream();
                os.write(requestBody.toString().getBytes("UTF-8"));
                os.close();
                
                int responseCode = conn.getResponseCode();
                
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    // Parse response
                    String aiResponse = parseGeminiResponse(response.toString());
                    mainHandler.post(() -> callback.onSuccess(aiResponse));
                } else {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    StringBuilder error = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        error.append(line);
                    }
                    reader.close();
                    
                    String errorMsg = error.toString();
                    if (errorMsg.contains("API_KEY_INVALID") || errorMsg.contains("API key not valid")) {
                        errorMsg = "API Key không hợp lệ. Vui lòng cập nhật API key trong GeminiApiService.java";
                    }
                    
                    String finalError = errorMsg;
                    mainHandler.post(() -> callback.onError("Lỗi API: " + finalError));
                }
                
                conn.disconnect();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError("Lỗi kết nối: " + e.getMessage()));
            }
        });
    }
    
    private static String buildPrompt(String userMessage, String context) {
        return "Bạn là AI Coach chuyên nghiệp về fitness và gym tại Việt Nam. " +
                "Nhiệm vụ của bạn là tư vấn về tập luyện, dinh dưỡng, và sức khỏe.\n\n" +
                "Thông tin người dùng:\n" + context + "\n\n" +
                "Câu hỏi: " + userMessage + "\n\n" +
                "Hãy trả lời bằng tiếng Việt, ngắn gọn (2-4 câu), thân thiện và chuyên nghiệp. " +
                "Sử dụng emoji phù hợp. Nếu cần, đưa ra lời khuyên cụ thể về bài tập hoặc dinh dưỡng.";
    }
    
    private static String parseGeminiResponse(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray candidates = json.getJSONArray("candidates");
            if (candidates.length() > 0) {
                JSONObject candidate = candidates.getJSONObject(0);
                JSONObject content = candidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                if (parts.length() > 0) {
                    JSONObject part = parts.getJSONObject(0);
                    return part.getString("text");
                }
            }
            return "Xin lỗi, tôi không thể trả lời câu hỏi này.";
        } catch (Exception e) {
            return "Lỗi xử lý phản hồi: " + e.getMessage();
        }
    }
    
    public static boolean isApiKeyConfigured() {
        return !API_KEY.equals("YOUR_GEMINI_API_KEY_HERE") && !API_KEY.isEmpty();
    }
}
