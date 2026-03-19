package com.htdgym.app.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htdgym.app.R;
import com.htdgym.app.adapters.ChatAdapter;
import com.htdgym.app.api.GeminiApiService;
import com.htdgym.app.database.GymDatabase;
import com.htdgym.app.models.ChatMessage;
import com.htdgym.app.models.User;
import com.htdgym.app.models.WorkoutStats;
import com.htdgym.app.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import com.htdgym.app.activities.BaseActivity;

public class AICoachActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messages;
    private EditText etMessage;
    private ImageView btnSend;
    private ProgressBar progressBar;
    private String userContext = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_coach);

        initViews();
        setupRecyclerView();
        loadUserContext();
        setupClickListeners();
        showWelcomeMessage();
        checkApiKey();
    }

    private void initViews() {
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_chat);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupRecyclerView() {
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);
    }

    private void loadUserContext() {
        new Thread(() -> {
            try {
                SharedPrefManager prefManager = SharedPrefManager.getInstance(this);
                String userId = prefManager.getUserId();
                String userName = prefManager.getUserName();
                
                StringBuilder context = new StringBuilder();
                context.append("Tên: ").append(userName != null ? userName : "Người dùng").append("\n");
                
                if (userId != null) {
                    GymDatabase db = GymDatabase.getInstance(this);
                    
                    // Get user info
                    User user = db.userDao().getUserById(Integer.parseInt(userId));
                    if (user != null) {
                        context.append("Email: ").append(user.getEmail()).append("\n");
                    }
                    
                    // Get workout stats
                    WorkoutStats stats = db.workoutStatsDao().getStatsByUserId(userId);
                    if (stats != null) {
                        context.append("Tổng buổi tập: ").append(stats.getTotalWorkouts()).append("\n");
                        context.append("Tổng calories đốt: ").append(stats.getTotalCalories()).append("\n");
                        context.append("Số ngày tập: ").append(stats.getTotalDays()).append("\n");
                    }
                }
                
                userContext = context.toString();
            } catch (Exception e) {
                userContext = "Người dùng mới";
            }
        }).start();
    }

    private void setupClickListeners() {
        btnSend.setOnClickListener(v -> sendMessage());
        
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    private void showWelcomeMessage() {
        ChatMessage welcomeMsg = new ChatMessage(
            "👋 Xin chào! Tôi là AI Coach của bạn.\n\n" +
            "Tôi có thể giúp bạn:\n" +
            "💪 Tư vấn bài tập phù hợp\n" +
            "🥗 Gợi ý chế độ dinh dưỡng\n" +
            "📊 Phân tích tiến độ tập luyện\n" +
            "❓ Trả lời các câu hỏi về fitness\n\n" +
            "Hãy hỏi tôi bất cứ điều gì! 😊",
            false
        );
        chatAdapter.addMessage(welcomeMsg);
        scrollToBottom();
    }

    private void checkApiKey() {
        if (!GeminiApiService.isApiKeyConfigured()) {
            new AlertDialog.Builder(this)
                .setTitle("⚠️ Cần cấu hình API Key")
                .setMessage("Để sử dụng AI Coach, bạn cần:\n\n" +
                        "1. Lấy Gemini API key miễn phí tại:\n" +
                        "https://makersuite.google.com/app/apikey\n\n" +
                        "2. Mở file GeminiApiService.java\n\n" +
                        "3. Thay thế YOUR_GEMINI_API_KEY_HERE bằng key của bạn\n\n" +
                        "Hiện tại AI Coach sẽ dùng câu trả lời mẫu.")
                .setPositiveButton("OK", null)
                .show();
        }
    }

    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        
        if (TextUtils.isEmpty(message)) {
            return;
        }
        
        // Add user message
        ChatMessage userMsg = new ChatMessage(message, true);
        chatAdapter.addMessage(userMsg);
        scrollToBottom();
        
        // Clear input
        etMessage.setText("");
        
        // Show loading
        showLoading(true);
        
        // Check if API key is configured
        if (!GeminiApiService.isApiKeyConfigured()) {
            // Use fallback responses
            showFallbackResponse(message);
            return;
        }
        
        // Send to Gemini API
        GeminiApiService.sendMessage(message, userContext, new GeminiApiService.GeminiCallback() {
            @Override
            public void onSuccess(String response) {
                showLoading(false);
                ChatMessage aiMsg = new ChatMessage(response, false);
                chatAdapter.addMessage(aiMsg);
                scrollToBottom();
            }
            
            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(AICoachActivity.this, error, Toast.LENGTH_SHORT).show();
                
                // Show fallback response on error
                showFallbackResponse(message);
            }
        });
    }

    private void showFallbackResponse(String userMessage) {
        String response = getFallbackResponse(userMessage.toLowerCase());
        
        new android.os.Handler().postDelayed(() -> {
            showLoading(false);
            ChatMessage aiMsg = new ChatMessage(response, false);
            chatAdapter.addMessage(aiMsg);
            scrollToBottom();
        }, 1000);
    }

    private String getFallbackResponse(String message) {
        if (message.contains("tập") || message.contains("workout") || message.contains("exercise")) {
            return "💪 Hôm nay bạn nên tập Ngực + Tay sau trong 45 phút.\n\n" +
                   "Gợi ý bài tập:\n" +
                   "• Bench Press: 4 sets x 8-10 reps\n" +
                   "• Dumbbell Flyes: 3 sets x 12 reps\n" +
                   "• Tricep Dips: 3 sets x 10-12 reps\n\n" +
                   "🔥 Đừng quên khởi động 5-10 phút!";
        } else if (message.contains("ăn") || message.contains("dinh dưỡng") || message.contains("nutrition")) {
            return "🥗 Gợi ý chế độ ăn cho người tập gym:\n\n" +
                   "Bữa sáng (7:00):\n" +
                   "• Yến mạch + trứng luộc + chuối\n" +
                   "• 450 calo, 30g protein\n\n" +
                   "Bữa trưa (12:30):\n" +
                   "• Ức gà nướng + cơm gạo lứt + rau\n" +
                   "• 650 calo, 45g protein\n\n" +
                   "💧 Nhớ uống đủ 2-3 lít nước/ngày!";
        } else if (message.contains("giảm cân") || message.contains("giảm mỡ") || message.contains("lose weight")) {
            return "🔥 Để giảm cân hiệu quả:\n\n" +
                   "1. Tạo deficit calories (ăn ít hơn đốt)\n" +
                   "2. Tập cardio 3-4 lần/tuần (30-45 phút)\n" +
                   "3. Tập tạ để giữ cơ bắp\n" +
                   "4. Ngủ đủ 7-8 tiếng\n" +
                   "5. Uống nhiều nước\n\n" +
                   "⚡ Kiên trì là chìa khóa thành công!";
        } else if (message.contains("tăng cân") || message.contains("tăng cơ") || message.contains("gain")) {
            return "💪 Để tăng cơ hiệu quả:\n\n" +
                   "1. Ăn thừa calories (300-500 calo/ngày)\n" +
                   "2. Protein: 1.6-2.2g/kg cân nặng\n" +
                   "3. Tập tạ nặng, ít reps (6-12 reps)\n" +
                   "4. Progressive overload\n" +
                   "5. Nghỉ ngơi đầy đủ\n\n" +
                   "🏋️ Tập 4-5 ngày/tuần là tối ưu!";
        } else if (message.contains("nghỉ") || message.contains("rest") || message.contains("recovery")) {
            return "😴 Nghỉ ngơi rất quan trọng:\n\n" +
                   "• Ngủ 7-8 tiếng/đêm\n" +
                   "• Rest day: 1-2 ngày/tuần\n" +
                   "• Nghỉ giữa các sets: 60-90 giây\n" +
                   "• Massage/foam rolling\n" +
                   "• Uống đủ nước\n\n" +
                   "💤 Cơ bắp phát triển khi nghỉ ngơi!";
        } else {
            return "🤖 Tôi có thể giúp bạn về:\n\n" +
                   "💪 Bài tập và lịch tập\n" +
                   "🥗 Dinh dưỡng và chế độ ăn\n" +
                   "📊 Phân tích tiến độ\n" +
                   "❓ Các câu hỏi về fitness\n\n" +
                   "Hãy hỏi tôi cụ thể hơn nhé! 😊";
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSend.setEnabled(!show);
        etMessage.setEnabled(!show);
    }

    private void scrollToBottom() {
        if (messages.size() > 0) {
            recyclerView.smoothScrollToPosition(messages.size() - 1);
        }
    }
}
