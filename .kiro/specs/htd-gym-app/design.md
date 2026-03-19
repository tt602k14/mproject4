# Design Document - HTD GYM App

## Overview

HTD GYM là một ứng dụng Android native được phát triển bằng Java, sử dụng kiến trúc Fragment-based với Bottom Navigation. Ứng dụng tập trung vào trải nghiệm người dùng đơn giản, giao diện dark mode với màu chủ đạo xanh lá, và cung cấp đầy đủ chức năng quản lý tập luyện thể hình.

## Architecture

### Application Architecture Pattern
- **Pattern**: Fragment-based Architecture với Single Activity
- **Main Activity**: MainActivity làm container chính
- **Navigation**: Bottom Navigation với 4 tab chính
- **Data Layer**: SharedPreferences cho local storage, Room Database cho dữ liệu phức tạp
- **Authentication**: Session-based với SharedPreferences

### Project Structure
```
com.htdgym.app/
├── activities/
│   ├── MainActivity.java (Container chính)
│   ├── LoginActivity.java (Đăng nhập)
│   └── SplashActivity.java (Màn hình chào)
├── fragments/
│   ├── HomeFragment.java (Tổng quan)
│   ├── WorkoutFragment.java (Tập luyện)
│   ├── ProgressFragment.java (Tiến trình)
│   └── CommunityFragment.java (Cộng đồng)
├── adapters/
│   ├── ExerciseAdapter.java (Danh sách bài tập)
│   └── CommunityPostAdapter.java (Bài viết cộng đồng)
├── models/
│   ├── User.java
│   ├── Exercise.java
│   ├── WorkoutSession.java
│   └── CommunityPost.java
├── database/
│   ├── AppDatabase.java (Room Database)
│   ├── dao/ (Data Access Objects)
│   └── entities/ (Database Entities)
├── utils/
│   ├── SharedPrefManager.java (Quản lý session)
│   ├── DatabaseHelper.java (Database utilities)
│   └── Constants.java (Hằng số)
└── services/
    └── WorkoutService.java (Background services)
```

## Components and Interfaces

### 1. Authentication System

#### LoginActivity
- **Purpose**: Xử lý đăng nhập, đăng ký, quên mật khẩu theo Requirements 6
- **Layout**: activity_login.xml
- **Key Methods**:
  - `validateLogin()`: Xác thực thông tin đăng nhập (Req 6.1, 6.4)
  - `registerUser()`: Đăng ký tài khoản mới (Req 6.2)
  - `resetPassword()`: Khôi phục mật khẩu (Req 6.3)
  - `checkAuthenticationState()`: Kiểm tra trạng thái đăng nhập để ngăn truy cập không hợp lệ (Req 6.5)
- **Navigation**: Chuyển đến MainActivity sau khi đăng nhập thành công
- **Security**: Implement secure session management và access control (Req 6.6)

#### SharedPrefManager
- **Purpose**: Quản lý session và user preferences, đảm bảo data persistence theo Requirements 7
- **Key Methods**:
  - `saveUserSession(User user)`: Lưu thông tin session an toàn (Req 7.1)
  - `isLoggedIn()`: Kiểm tra trạng thái đăng nhập
  - `clearSession()`: Xóa session khi đăng xuất
  - `persistWorkoutData()`: Lưu trữ dữ liệu workout session (Req 7.2, 7.3)
  - `ensureDataIntegrity()`: Đảm bảo tính toàn vẹn dữ liệu (Req 7.5)

### 2. Main Navigation System

#### MainActivity
- **Purpose**: Container chính chứa Bottom Navigation và Fragment theo Requirements 5
- **Layout**: activity_main.xml với BottomNavigationView và FrameLayout
- **Key Components**:
  - Bottom Navigation với 4 items: Home, Workout, Progress, Community (Req 5.2)
  - Fragment container để hiển thị nội dung
  - Navigation state management để duy trì trạng thái (Req 5.4)
  - Active item highlighting system (Req 5.5)
- **Navigation Logic**: Sử dụng FragmentTransaction để chuyển đổi fragments (Req 5.3)
- **Access Control**: Chỉ cho phép truy cập khi user đã authenticated (Req 6.5)

### 3. Home Module

#### HomeFragment
- **Purpose**: Màn hình tổng quan theo Requirements 1
- **Layout**: fragment_home.xml
- **Key Components**:
  - TextView hiển thị "HTD GYM" prominently (Req 1.1)
  - TextView lời chào cá nhân hóa cho người dùng (Req 1.2)
  - CardView "Hôm nay bạn đã tập gì?" (Req 1.3)
  - Button "Bắt đầu tập" (Req 1.4)
- **Interactions**: Chuyển đến WorkoutFragment khi nhấn "Bắt đầu tập" (Req 1.5)
- **Design Rationale**: Giao diện đơn giản, tập trung vào call-to-action chính để khuyến khích người dùng bắt đầu tập luyện

### 4. Workout Module

#### WorkoutFragment
- **Purpose**: Hiển thị danh sách bài tập trong buổi tập theo Requirements 2
- **Layout**: fragment_workout.xml với RecyclerView
- **Key Components**:
  - TextView "Workout hôm nay" (Req 2.1)
  - RecyclerView hiển thị danh sách exercises cho session hiện tại (Req 2.2, 2.4)
  - Button "Hoàn thành buổi tập" (Req 2.5)
- **Functionality**:
  - Hiển thị tên bài tập, số hiệp, số lần/thời gian cho mỗi exercise (Req 2.3)
  - Đánh dấu workout session hoàn thành khi nhấn button (Req 2.6)
  - Chuyển đến ProgressFragment sau khi hoàn thành (Req 2.7)
- **Design Rationale**: RecyclerView cho phép hiển thị danh sách bài tập dài một cách hiệu quả

#### ExerciseAdapter
- **Purpose**: Adapter cho RecyclerView hiển thị danh sách bài tập theo Requirements 2
- **Item Layout**: item_exercise.xml
- **Data Binding**: Exercise object → UI components
- **Key Fields**: Tên bài tập, số hiệp, số lần/thời gian (Req 2.3)
- **Design Rationale**: Sử dụng ViewHolder pattern để tối ưu hiệu suất khi scroll

### 5. Progress Module

#### ProgressFragment
- **Purpose**: Theo dõi tiến trình tập luyện theo Requirements 3
- **Layout**: fragment_progress.xml
- **Key Components**:
  - CardView hiển thị tổng số buổi tập đã hoàn thành (Req 3.1)
  - TextView ngày tập gần nhất (Req 3.2)
  - TextView trạng thái tập luyện hiện tại (đang tập/đã hoàn thành) (Req 3.3)
  - Chart view cho biểu đồ tiến trình theo ngày/tuần (Req 3.4 - advanced feature)
- **Data Management**: Duy trì lịch sử tất cả workout sessions đã hoàn thành (Req 3.5)
- **Design Rationale**: Sử dụng cards để tổ chức thông tin một cách rõ ràng, charts để visualization dữ liệu tiến trình

### 6. Community Module

#### CommunityFragment
- **Purpose**: Hiển thị bài viết và thông báo cộng đồng theo Requirements 4
- **Layout**: fragment_community.xml với RecyclerView
- **Key Components**:
  - RecyclerView hiển thị danh sách posts và announcements (Req 4.1)
  - Motivational workout content display (Req 4.2)
  - FloatingActionButton để tạo bài viết mới (Req 4.3 - advanced feature)
  - Content moderation system để đảm bảo nội dung phù hợp (Req 4.5)
- **Advanced Features**:
  - Like và comment functionality (Req 4.4)
  - Content filtering và search
- **Design Rationale**: Community features tăng engagement và motivation cho người dùng

#### CommunityPostAdapter
- **Purpose**: Adapter cho bài viết cộng đồng theo Requirements 4
- **Item Layout**: item_community_post.xml
- **Features**: 
  - Hiển thị nội dung post với author info
  - Like và comment interactions (advanced)
  - Content moderation indicators
- **Design Rationale**: Flexible adapter design để support các loại content khác nhau

## Data Models

### User Model
```java
public class User {
    private int userId;
    private String username;
    private String email;
    private String password; // Hashed với BCrypt
    private Date registrationDate;
    private boolean isAdmin; // Support cho Requirements 9
    private String profileImageUrl;
    private UserPreferences preferences;
    
    // Constructors, getters, setters
    // Business logic methods
    public boolean hasAdminPrivileges() { return isAdmin; }
    public void updateLastLoginDate() { /* implementation */ }
}
```

### Exercise Model
```java
public class Exercise {
    private int exerciseId;
    private String exerciseName;
    private int sets;
    private int reps;
    private int duration; // seconds, for time-based exercises
    private String description;
    private String category; // Chest, Back, Legs, etc.
    private String difficulty; // Beginner, Intermediate, Advanced
    private String instructions;
    private List<String> muscleGroups;
    
    // Constructors, getters, setters
    // Validation methods
    public boolean isValidExercise() { /* validation logic */ }
}
```

### WorkoutSession Model
```java
public class WorkoutSession {
    private int sessionId;
    private int userId;
    private Date workoutDate;
    private List<Exercise> exercises;
    private boolean isCompleted;
    private int totalDuration;
    private Date startTime;
    private Date endTime;
    private String notes;
    private double caloriesBurned; // Estimated
    
    // Constructors, getters, setters
    // Business logic methods
    public void markAsCompleted() { 
        this.isCompleted = true;
        this.endTime = new Date();
    }
    public int calculateTotalSets() { /* implementation */ }
}
```

### CommunityPost Model
```java
public class CommunityPost {
    private int postId;
    private int authorId;
    private String authorName;
    private String content;
    private Date postDate;
    private int likes;
    private List<Comment> comments;
    private PostType type; // MOTIVATION, ACHIEVEMENT, TIP, etc.
    private boolean isModerated;
    
    // Constructors, getters, setters
    // Business logic methods
    public void addLike() { this.likes++; }
    public boolean isAppropriateContent() { /* content validation */ }
}
```

### UserPreferences Model (New)
```java
public class UserPreferences {
    private boolean darkModeEnabled; // Requirements 8
    private String preferredLanguage;
    private boolean notificationsEnabled;
    private int dailyWorkoutGoal;
    private List<String> favoriteExercises;
    
    // Constructors, getters, setters
}
```

## Database Design

### Room Database Setup
- **Database Name**: htd_gym_database
- **Version**: 1
- **Tables**: users, exercises, workout_sessions, community_posts

### Entity Relationships
- User 1:N WorkoutSession
- WorkoutSession N:M Exercise (through junction table)
- User 1:N CommunityPost

### Data Access Objects (DAOs)
- UserDao: CRUD operations cho User
- ExerciseDao: CRUD operations cho Exercise
- WorkoutSessionDao: CRUD operations cho WorkoutSession
- CommunityPostDao: CRUD operations cho CommunityPost

## UI/UX Design

### Theme and Styling (Requirements 8)
- **Primary Theme**: Dark Mode as default (Req 8.1)
- **Primary Color**: Green (#4CAF50) (Req 8.2)
- **Secondary Color**: Dark Green (#388E3C)
- **Accent Color**: Light Green (#81C784)
- **Background**: Dark Gray (#121212)
- **Surface**: Dark Gray (#1E1E1E)
- **Text**: White (#FFFFFF)
- **Text Secondary**: Light Gray (#B0B0B0)

### Layout Specifications (Requirements 8)
- **Card Corner Radius**: 12dp (Req 8.3)
- **Padding Standard**: 16dp
- **Margin Standard**: 8dp
- **Button Height**: 48dp
- **Typography**: Roboto font family, clear and readable (Req 8.4)
- **Icon Size**: 24dp for navigation, 20dp for actions
- **Elevation**: 4dp for cards, 8dp for FAB

### User Experience Design (Requirements 8)
- **Interface Style**: User-friendly for beginners (Req 8.5)
- **Navigation**: Intuitive bottom navigation
- **Feedback**: Visual feedback for all user actions
- **Loading States**: Skeleton screens and progress indicators
- **Error States**: Clear error messages with recovery options

### Responsive Design
- **Target Screen Sizes**: 5" - 6.5" phones
- **Orientation**: Portrait primary, landscape supported
- **Density**: mdpi to xxxhdpi
- **Accessibility**: Support for TalkBack, large text sizes

## Error Handling

### Authentication Errors
- Invalid credentials → Show error message
- Network timeout → Retry mechanism
- Account not found → Redirect to registration

### Data Errors
- Database connection failure → Show offline mode
- Data corruption → Reset to default state
- Sync failures → Queue for retry

### UI Error States
- Empty states for lists
- Loading states for async operations
- Error dialogs for critical failures

## Testing Strategy

### Unit Testing
- Model classes validation
- Database operations
- Utility functions
- Authentication logic

### Integration Testing
- Fragment navigation
- Database integration
- SharedPreferences integration
- API integration (if applicable)

### UI Testing
- User flows (login → home → workout → progress)
- Navigation testing
- Form validation
- Button interactions

### Performance Testing
- Database query performance
- UI rendering performance
- Memory usage optimization
- Battery usage optimization

## Security Considerations

### Data Protection
- Password hashing using BCrypt
- Sensitive data encryption in SharedPreferences
- Input validation and sanitization
- SQL injection prevention

### Session Management
- Secure session tokens
- Session timeout handling
- Automatic logout on app backgrounding
- Biometric authentication (future enhancement)

## Advanced Features & Admin System

### Admin Management System (Requirements 9)
- **Purpose**: Provide administrative capabilities for content and user management
- **Access Control**: Role-based access với User/Admin differentiation (Req 9.1)
- **Key Features**:
  - Exercise management interface (Req 9.2)
  - Workout plan creation và updates (Req 9.3)
  - User statistics dashboard (Req 9.4)
  - Content moderation tools
- **Security**: Proper access control implementation (Req 9.5)

### Data Synchronization (Requirements 7.4)
- **Cloud Sync**: Optional server synchronization for data backup
- **Offline Support**: Full offline functionality với sync khi có network
- **Conflict Resolution**: Smart merge strategies cho data conflicts
- **Data Migration**: Seamless data transfer between devices

### Performance Optimizations
- **Database Indexing**: Optimized queries cho large datasets
- **Image Caching**: Efficient image loading và caching
- **Memory Management**: Proper lifecycle management
- **Battery Optimization**: Background task optimization

### Analytics & Insights
- **Workout Analytics**: Detailed progress tracking và insights
- **Performance Metrics**: Strength gains, endurance improvements
- **Goal Setting**: SMART goals với progress tracking
- **Achievements System**: Gamification elements để increase engagement