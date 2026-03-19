package com.htdgym.app.activities;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.htdgym.app.R;
import java.util.Random;

public class AICoachActivity extends AppCompatActivity {

    private final String[] aiAnswers = {
            "💪 Hôm nay bạn nên tập Ngực + Tay sau trong 45 phút.",
            "🔥 Đừng quên khởi động 5–10 phút trước khi tập nhé!",
            "🥗 Gợi ý ăn uống: Ức gà + cơm gạo lứt + rau xanh.",
            "💧 Nhớ uống đủ 2–3 lít nước mỗi ngày.",
            "⏱ Nghỉ 60–90 giây giữa các hiệp để đạt hiệu quả tốt nhất.",
            "😴 Ngủ đủ 7–8 tiếng giúp cơ bắp phục hồi nhanh hơn.",
            "🏋️‍♂️ Nếu mới tập, hãy dùng mức tạ nhẹ và đúng form."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_coach);

        // Nút quay lại
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Nút hỏi AI Coach
        findViewById(R.id.btnAskAI).setOnClickListener(v -> showAIAnswer());
    }

    // ================= AI FAKE LOGIC =================
    private void showAIAnswer() {
        String answer = aiAnswers[new Random().nextInt(aiAnswers.length)];

        new AlertDialog.Builder(this)
                .setTitle("🤖 AI Coach")
                .setMessage(answer)
                .setPositiveButton("OK", null)
                .show();
    }
}
