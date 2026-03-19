# HTD GYM - Professional Android App
## Hướng dẫn từ Senior Android Developer (40 năm kinh nghiệm)

---

## ✅ ĐÃ HOÀN THÀNH

### 1. **Authentication System** (Production-Ready)
- ✅ Login với Email/Password
- ✅ Register với validation đầy đủ
- ✅ Forgot Password với Firebase
- ✅ Google Sign-In integration
- ✅ Facebook Login structure
- ✅ Session management với SharedPreferences
- ✅ Auto-login khi đã đăng nhập

### 2. **Database Architecture** (Clean & Scalable)
- ✅ Room Database với 7 entities
- ✅ DAO pattern cho mỗi entity
- ✅ Repository pattern
- ✅ MVVM Architecture
- ✅ LiveData cho reactive UI
- ✅ Type Converters cho Date

### 3. **Core Features**
- ✅ Member Management
- ✅ Workout Tracking
- ✅ Payment Management
- ✅ Equipment Management
- ✅ Trainer Management
- ✅ User Management
- ✅ Exercise Library structure

### 4. **UI/UX** (Material Design 3)
- ✅ Dark theme professional
- ✅ Material Components
- ✅ Responsive layouts
- ✅ Bottom Navigation
- ✅ FloatingActionButtons
- ✅ CardViews với elevation
- ✅ TextInputLayouts với icons

---

## 🔧 CẤU HÌNH FIREBASE (BẮT BUỘC)

### Bước 1: Tạo Firebase Project

```bash
1. Truy cập: https://console.firebase.google.com/
2. Click "Add project" → Nhập tên "HTD GYM"
3. Disable Google Analytics (không cần thiết)
4. Click "Create project"
```

### Bước 2: Thêm Android App

```bash
1. Click icon Android trong Firebase Console
2. Android package name: com.htdgym.app
3. App nickname: HTD GYM
4. SHA-1: (Lấy từ Android Studio)
   - Mở Terminal trong Android Studio
   - Chạy: ./gradlew signingReport
   - Copy SHA-1 certificate fingerprint
5. Download google-services.json
6. Thay thế file app/google-services.json
```

### Bước 3: Enable Authentication

```bash
1. Firebase Console → Authentication → Get Started
2. Sign-in method → Email/Password → Enable
3. Sign-in method → Google → Enable
4. Copy Web client ID
5. Paste vào app/src/main/res/values/strings.xml:
   <string name="default_web_client_id">YOUR_WEB_CLIENT_ID</string>
```

---

## 📱 KIẾN TRÚC ỨNG DỤNG

```
HTD GYM App
│
├── Presentation Layer (UI)
│   ├── Activities
│   │   ├── LoginActivity ✅
│   │   ├── RegisterActivity ✅
│   │   ├── ForgotPasswordActivity ✅
│   │   ├── MainActivity ✅
│   │   ├── AddMemberActivity ✅
│   │   ├── AddWorkoutActivity ✅
│   │   ├── AddPaymentActivity ✅
│   │   └── AddEquipmentActivity ✅
│   │
│   ├── Fragments
│   │   ├── HomeFragment ✅
│   │   ├── MembersFragment ✅
│   │   ├── WorkoutsFragment ✅
│   │   ├── PaymentsFragment ✅
│   │   └── EquipmentFragment ✅
│   │
│   └── Adapters
│       ├── MemberAdapter ✅
│       ├── WorkoutAdapter ✅
│       ├── PaymentAdapter ✅
│       └── EquipmentAdapter ✅
│
├── Domain Layer (Business Logic)
│   ├── ViewModels
│   │   ├── MemberViewModel ✅
│   │   ├── WorkoutViewModel ✅
│   │   ├── PaymentViewModel ✅
│   │   └── EquipmentViewModel ✅
│   │
│   └── Repository
│       └── GymRepository ✅
│
├── Data Layer (Persistence)
│   ├── Database
│   │   └── GymDatabase ✅
│   │
│   ├── DAOs
│   │   ├── MemberDao ✅
│   │   ├── WorkoutDao ✅
│   │   ├── PaymentDao ✅
│   │   ├── EquipmentDao ✅
│   │   ├── TrainerDao ✅
│   │   ├── UserDao ✅
│   │   └── ExerciseDao ✅
│   │
│   └── Models (Entities)
│       ├── Member ✅
│       ├── Workout ✅
│       ├── Payment ✅
│       ├── Equipment ✅
│       ├── Trainer ✅
│       ├── User ✅
│       └── Exercise ✅
│
└── Utils
    ├── SharedPrefManager ✅
    └── DateUtils ✅
```

---

## 🚀 CHỨC NĂNG CẦN THÊM (THEO HÌNH)

### 1. Exercise Library (Thư viện bài tập)

**Cần tạo:**
- `ExerciseLibraryActivity.java`
- `ExerciseDetailActivity.java`
- `ExerciseAdapter.java`
- `ExerciseViewModel.java`

**Sample Data:**
```java
// Bench Press
Exercise benchPress = new Exercise();
benchPress.setName("Bench Press");
benchPress.setCategory("Ngực");
benchPress.setBodyPart("Ngực & Vai");
benchPress.setSets("3×8-12");
benchPress.setDuration("90s");
benchPress.setDifficulty("Trung cấp");
benchPress.setCaloriesBurned(150);

// Squat
Exercise squat = new Exercise();
squat.setName("Squat");
squat.setCategory("Chân");
squat.setBodyPart("Chân & Mông");
squat.setSets("4×6-10");
squat.setDuration("120s");
squat.setDifficulty("Cơ bản");
squat.setCaloriesBurned(200);

// ... thêm 20+ exercises
```

### 2. Video Player (Video HD)

**Dependencies đã thêm:**
```gradle
implementation 'com.google.android.exoplayer:exoplayer:2.19.1'
```

**Cần tạo:**
- `VideoPlayerActivity.java`
- `VideoListActivity.java`
- `VideoAdapter.java`

**Sample Code:**
```java
// VideoPlayerActivity.java
SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
playerView.setPlayer(player);
MediaItem mediaItem = MediaItem.fromUri(videoUrl);
player.setMediaItem(mediaItem);
player.prepare();
player.play();
```

### 3. Workout Plans (Lộ trình tập)

**Cần tạo:**
- `WorkoutPlan.java` (Model)
- `WorkoutPlanDao.java`
- `WorkoutPlanActivity.java`
- `CreatePlanActivity.java`

**Structure:**
```java
@Entity
public class WorkoutPlan {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name; // "Thứ 2 - Ngực & Vai"
    private String description;
    private int duration; // 45 phút
    private int caloriesTarget; // 320 calo
    private String exercises; // JSON array of exercise IDs
}
```

### 4. Quick Workout (Tập nhanh)

**Cần tạo:**
- `QuickWorkoutActivity.java`
- `WorkoutTimerActivity.java`
- `WorkoutSessionActivity.java`

**Features:**
- Timer cho mỗi bài tập
- Rest timer giữa các sets
- Audio cues
- Progress tracking

### 5. AI Coach (Optional - Advanced)

**Integration với ChatGPT API:**
```java
// Sử dụng Retrofit + OpenAI API
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```

### 6. Community (Cộng đồng)

**Cần tạo:**
- `CommunityFragment.java`
- `Post.java` (Model)
- `PostAdapter.java`
- Firebase Firestore integration

### 7. Challenges (Thử thách)

**Cần tạo:**
- `Challenge.java` (Model)
- `ChallengeActivity.java`
- `ChallengeAdapter.java`
- Leaderboard system

---

## 📊 DATABASE SCHEMA HOÀN CHỈNH

```sql
-- Users Table
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT NOT NULL UNIQUE,
    password TEXT,
    name TEXT,
    phone TEXT,
    profileImage TEXT,
    loginType TEXT, -- email, google, facebook, apple
    firebaseUid TEXT UNIQUE
);

-- Exercises Table
CREATE TABLE exercises (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category TEXT, -- Tất cả, Ngực, Lưng, Chân, Tay, Bụng
    bodyPart TEXT,
    sets TEXT,
    duration TEXT,
    videoUrl TEXT,
    thumbnailUrl TEXT,
    description TEXT,
    difficulty TEXT,
    caloriesBurned INTEGER
);

-- Workout Plans Table
CREATE TABLE workout_plans (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER,
    name TEXT,
    description TEXT,
    duration INTEGER,
    caloriesTarget INTEGER,
    exercises TEXT, -- JSON
    dayOfWeek INTEGER,
    FOREIGN KEY (userId) REFERENCES users(id)
);

-- Workout Sessions Table
CREATE TABLE workout_sessions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER,
    planId INTEGER,
    startTime INTEGER,
    endTime INTEGER,
    caloriesBurned INTEGER,
    completed BOOLEAN,
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (planId) REFERENCES workout_plans(id)
);
```

---

## 🎯 ROADMAP PHÁT TRIỂN

### Phase 1: Core Features (HOÀN THÀNH ✅)
- Authentication
- Database setup
- Basic CRUD operations
- UI/UX foundation

### Phase 2: Exercise Library (TIẾP THEO)
- [ ] Tạo ExerciseLibraryActivity
- [ ] Import sample exercises (50+)
- [ ] Filter by category
- [ ] Search functionality
- [ ] Favorite exercises

### Phase 3: Video Integration
- [ ] Video player với ExoPlayer
- [ ] Video library
- [ ] Offline caching
- [ ] Playback controls

### Phase 4: Workout Plans
- [ ] Create custom plans
- [ ] Pre-made templates
- [ ] Calendar integration
- [ ] Progress tracking

### Phase 5: Social Features
- [ ] Community feed
- [ ] Share workouts
- [ ] Challenges
- [ ] Leaderboards

### Phase 6: Advanced Features
- [ ] AI Coach
- [ ] Nutrition tracking
- [ ] Body measurements
- [ ] Progress photos
- [ ] Push notifications

---

## 🔐 SECURITY BEST PRACTICES

### 1. Firebase Security Rules
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

### 2. ProGuard Rules
```proguard
# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**
```

### 3. API Keys Protection
```xml
<!-- Không commit API keys vào Git -->
<!-- Sử dụng local.properties hoặc environment variables -->
```

---

## 📱 TESTING STRATEGY

### Unit Tests
```java
@Test
public void testUserRegistration() {
    // Test validation
    // Test Firebase integration
    // Test database insertion
}
```

### UI Tests
```java
@Test
public void testLoginFlow() {
    // Test login screen
    // Test navigation
    // Test error handling
}
```

---

## 🚀 DEPLOYMENT

### 1. Build Release APK
```bash
./gradlew assembleRelease
```

### 2. Generate Signed APK
```bash
1. Build → Generate Signed Bundle/APK
2. Choose APK
3. Create/Select keystore
4. Fill in key details
5. Build release APK
```

### 3. Upload to Play Store
```bash
1. Create Play Console account
2. Create app
3. Upload APK/AAB
4. Fill in store listing
5. Submit for review
```

---

## 💡 PRO TIPS

1. **Performance**: Sử dụng RecyclerView với DiffUtil
2. **Memory**: Glide cho image loading với caching
3. **Network**: Retrofit với OkHttp interceptors
4. **Offline**: Room + WorkManager cho sync
5. **Analytics**: Firebase Analytics cho tracking
6. **Crash Reporting**: Firebase Crashlytics
7. **A/B Testing**: Firebase Remote Config

---

## 📞 SUPPORT

Nếu gặp vấn đề:
1. Check SETUP_GUIDE.md
2. Check Firebase Console logs
3. Check Logcat trong Android Studio
4. Check build.gradle dependencies

---

**Chúc bạn thành công với HTD GYM! 💪🏋️‍♂️**

*Built with 40 years of Android development experience*
