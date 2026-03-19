@echo off
REM Script tự động build và chạy HTD Gym App cho Windows
REM Sử dụng: build_and_run.bat

echo ========================================
echo 🏋️ HTD GYM - Auto Build ^& Run Script
echo ========================================
echo.

REM Kiểm tra ANDROID_HOME
echo 📱 Kiểm tra Android SDK...
if "%ANDROID_HOME%"=="" (
    echo ❌ ANDROID_HOME chưa được set!
    echo Vui lòng set ANDROID_HOME trong Environment Variables
    echo Ví dụ: C:\Users\YourName\AppData\Local\Android\Sdk
    pause
    exit /b 1
)
echo ✅ Android SDK found: %ANDROID_HOME%
echo.

REM Kiểm tra ADB
echo 🔌 Kiểm tra ADB...
where adb >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ ADB không tìm thấy!
    echo Vui lòng cài đặt Android SDK Platform Tools
    pause
    exit /b 1
)
echo ✅ ADB found
echo.

REM Kiểm tra device
echo 📱 Kiểm tra devices...
adb devices
echo.

REM Clean project
echo 🧹 Cleaning project...
call gradlew.bat clean
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Clean failed!
    pause
    exit /b 1
)
echo ✅ Clean completed
echo.

REM Build debug APK
echo 🔨 Building debug APK...
call gradlew.bat assembleDebug
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Build failed!
    echo Vui lòng kiểm tra lỗi ở trên
    pause
    exit /b 1
)
echo ✅ Build completed
echo.

REM Install APK
echo 📲 Installing APK...
call gradlew.bat installDebug
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Install failed!
    pause
    exit /b 1
)
echo ✅ APK installed
echo.

REM Launch app
echo 🚀 Launching app...
adb shell am start -n com.htdgym.app/.activities.LoginActivity
if %ERRORLEVEL% NEQ 0 (
    echo ⚠️ Launch failed, trying alternative method...
    adb shell monkey -p com.htdgym.app -c android.intent.category.LAUNCHER 1
)
echo ✅ App launched!
echo.

REM Show logs
echo 📋 Showing app logs (Ctrl+C to stop)...
echo ========================================
adb logcat | findstr "htdgym DatabaseInitializer WorkoutDataHelper LoginActivity"
