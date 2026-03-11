# Hướng Dẫn Cài Đặt AI Coach

## ✅ Đã hoàn thành

Tôi đã hoàn thiện AI Coach với các tính năng:

### 🎯 Tính năng chính:
1. **Chat Interface đẹp mắt**
   - Giao diện chat hiện đại
   - Tin nhắn user (màu xanh, bên phải)
   - Tin nhắn AI (màu trắng, bên trái)
   - Hiển thị thời gian

2. **Gemini AI Integration**
   - Tích hợp Google Gemini API (miễn phí)
   - Context-aware (biết thông tin user)
   - Trả lời bằng tiếng Việt
   - Chuyên về fitness & gym

3. **Fallback Responses**
   - Nếu chưa có API key, dùng câu trả lời thông minh
   - Phân loại câu hỏi (tập luyện, dinh dưỡng, giảm cân, etc.)
   - Không cần API vẫn hoạt động tốt

4. **User Context**
   - Load thông tin user từ database
   - Biết workout stats của user
   - Tư vấn cá nhân hóa

### 📁 Files đã tạo:
1. `AICoachActivity.java` - Activity chính (đã cập nhật)
2. `GeminiApiService.java` - Service gọi Gemini API
3. `ChatAdapter.java` - Adapter cho RecyclerView
4. `ChatMessage.java` - Model cho tin nhắn
5. `activity_ai_coach.xml` - Layout chính
6. `item_chat_ai.xml` - Layout tin nhắn AI
7. `item_chat_user.xml` - Layout tin nhắn user

---

## 🔑 Cách lấy Gemini API Key (MIỄN PHÍ)

### Bước 1: Truy cập Google AI Studio
Mở trình duyệt và vào: https://makersuite.google.com/app/apikey

### Bước 2: Đăng nhập
- Đăng nhập bằng tài khoản Google của bạn

### Bước 3: Tạo API Key
- Click nút "Create API Key"
- Chọn project (hoặc tạo mới)
- Copy API key

### Bước 4: Cập nhật vào code
Mở file: `app/src/main/java/com/htdgym/app/api/GeminiApiService.java`

Tìm dòng:
```java
private static final String API_KEY = "YOUR_GEMINI_API_KEY_HERE";
```

Thay thế bằng:
```java
private static final String API_KEY = "AIzaSy..."; // API key của bạn
```

### Bước 5: Build lại app
```bash
./gradlew assembleDebug
```

---

## 🎮 Cách sử dụng

### Không có API Key:
App vẫn hoạt động với fallback responses thông minh:
- Hỏi về tập luyện → Gợi ý bài tập
- Hỏi về dinh dưỡng → Gợi ý chế độ ăn
- Hỏi về giảm cân → Tips giảm cân
- Hỏi về tăng cơ → Tips tăng cơ
- Hỏi về nghỉ ngơi → Tips recovery

### Có API Key:
AI Coach sẽ:
- Trả lời thông minh hơn
- Hiểu context câu hỏi
- Tư vấn cá nhân hóa dựa trên stats của user
- Trả lời đa dạng hơn

---

## 💡 Ví dụ câu hỏi

### Về tập luyện:
- "Hôm nay tôi nên tập gì?"
- "Làm sao để tăng cơ tay nhanh?"
- "Bài tập nào tốt cho người mới?"
- "Tôi muốn tập 6 múi"

### Về dinh dưỡng:
- "Tôi nên ăn gì để tăng cơ?"
- "Chế độ ăn giảm mỡ như thế nào?"
- "Cần bao nhiêu protein mỗi ngày?"
- "Thực đơn cho người tập gym"

### Về giảm cân:
- "Làm sao để giảm cân hiệu quả?"
- "Tập cardio bao lâu mỗi ngày?"
- "Giảm mỡ bụng nhanh nhất"

### Về tăng cơ:
- "Làm sao để tăng cơ nhanh?"
- "Nên tập nặng hay nhẹ?"
- "Bao lâu thì thấy kết quả?"

---

## 🎨 Tính năng nổi bật

### 1. Context-Aware
AI biết thông tin của bạn:
- Tên
- Email
- Số buổi tập
- Calories đã đốt
- Số ngày tập

### 2. Smart Fallback
Nếu API lỗi hoặc chưa có key:
- Tự động chuyển sang fallback
- Vẫn trả lời thông minh
- Phân loại câu hỏi tự động

### 3. Beautiful UI
- Chat bubbles đẹp
- Màu sắc phân biệt rõ
- Smooth scrolling
- Loading indicator
- Timestamp cho mỗi tin nhắn

### 4. Vietnamese Support
- Prompt tiếng Việt
- Responses tiếng Việt
- Emoji phù hợp
- Ngữ cảnh Việt Nam

---

## 🔧 Troubleshooting

### Lỗi: "API Key không hợp lệ"
**Giải pháp:**
1. Kiểm tra API key đã copy đúng chưa
2. Đảm bảo không có khoảng trắng thừa
3. Thử tạo API key mới

### Lỗi: "Lỗi kết nối"
**Giải pháp:**
1. Kiểm tra internet
2. Thử lại sau vài giây
3. App sẽ tự động dùng fallback

### AI trả lời chậm
**Nguyên nhân:**
- Gemini API đôi khi chậm (5-10 giây)
- Kết nối internet chậm

**Giải pháp:**
- Đợi loading indicator
- Hoặc dùng fallback (nhanh hơn)

---

## 📊 So sánh Gemini vs Fallback

| Tính năng | Gemini API | Fallback |
|-----------|------------|----------|
| Tốc độ | 5-10s | <1s |
| Độ thông minh | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| Đa dạng | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| Context-aware | ✅ | ❌ |
| Cần internet | ✅ | ✅ |
| Cần API key | ✅ | ❌ |
| Chi phí | Miễn phí | Miễn phí |

---

## 🚀 Nâng cấp trong tương lai

### Phase 1 (Đã xong):
- ✅ Chat interface
- ✅ Gemini integration
- ✅ Fallback responses
- ✅ User context

### Phase 2 (Có thể làm):
- 💾 Lưu lịch sử chat vào database
- 🎤 Voice input (speech-to-text)
- 🔊 Voice output (text-to-speech)
- 📸 Phân tích ảnh form tập
- 📊 Phân tích progress photos

### Phase 3 (Nâng cao):
- 🤖 Custom AI model
- 📱 Offline AI
- 🎯 Personalized workout plans
- 🥗 Meal plan generator
- 📈 Progress predictions

---

## 💰 Chi phí

### Gemini API (Free tier):
- **60 requests/minute**
- **1,500 requests/day**
- **1 million tokens/month**

→ Đủ cho app cá nhân hoặc startup nhỏ!

### Nếu cần nhiều hơn:
- Gemini Pro: $0.00025/1K characters
- Rất rẻ so với ChatGPT

---

## 🎓 Tips sử dụng hiệu quả

1. **Hỏi cụ thể:**
   - ❌ "Tập gì?"
   - ✅ "Tôi muốn tăng cơ tay, nên tập bài nào?"

2. **Cung cấp context:**
   - ❌ "Ăn gì?"
   - ✅ "Tôi 70kg, muốn giảm xuống 65kg, nên ăn gì?"

3. **Hỏi follow-up:**
   - Sau khi AI trả lời, hỏi thêm chi tiết
   - "Còn về dinh dưỡng thì sao?"

4. **Sử dụng emoji:**
   - AI hiểu và phản hồi với emoji
   - Làm chat thân thiện hơn

---

## 📝 Kết luận

AI Coach giờ đã hoàn chỉnh với:
- ✅ Chat interface đẹp
- ✅ Gemini AI integration
- ✅ Smart fallback
- ✅ User context
- ✅ Vietnamese support
- ✅ Free to use

**Không cần API key vẫn dùng được tốt!**

Nếu muốn AI thông minh hơn, chỉ cần:
1. Lấy free API key (2 phút)
2. Paste vào code
3. Build lại

Enjoy your AI Coach! 🚀💪
