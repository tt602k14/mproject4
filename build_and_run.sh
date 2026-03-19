#!/bin/bash

# Script tự động build và chạy HTD Gym App
# Sử dụng: ./build_and_run.sh

echo "🏋️ HTD GYM - Auto Build & Run Script"
echo "======================================"
echo ""

# Màu sắc cho output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Kiểm tra Android SDK
echo "📱 Kiểm tra Android SDK..."
if [ -z "$ANDROID_HOME" ]; then
    echo -e "${RED}❌ ANDROID_HOME chưa được set!${NC}"
    echo "Vui lòng set ANDROID_HOME trong ~/.bashrc hoặc ~/.zshrc"
    echo "Ví dụ: export ANDROID_HOME=~/Android/Sdk"
    exit 1
fi
echo -e "${GREEN}✅ Android SDK found: $ANDROID_HOME${NC}"
echo ""

# Kiểm tra ADB
echo "🔌 Kiểm tra ADB..."
if ! command -v adb &> /dev/null; then
    echo -e "${RED}❌ ADB không tìm thấy!${NC}"
    echo "Vui lòng cài đặt Android SDK Platform Tools"
    exit 1
fi
echo -e "${GREEN}✅ ADB found${NC}"
echo ""

# Kiểm tra device
echo "📱 Kiểm tra devices..."
DEVICES=$(adb devices | grep -v "List" | grep "device" | wc -l)
if [ $DEVICES -eq 0 ]; then
    echo -e "${YELLOW}⚠️  Không tìm thấy device/emulator!${NC}"
    echo "Vui lòng:"
    echo "  1. Kết nối điện thoại qua USB và bật USB Debugging"
    echo "  2. Hoặc khởi động Android Emulator"
    echo ""
    read -p "Bạn có muốn tiếp tục không? (y/n) " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
else
    echo -e "${GREEN}✅ Tìm thấy $DEVICES device(s)${NC}"
    adb devices
fi
echo ""

# Clean project
echo "🧹 Cleaning project..."
./gradlew clean
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Clean failed!${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Clean completed${NC}"
echo ""

# Build debug APK
echo "🔨 Building debug APK..."
./gradlew assembleDebug
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Build failed!${NC}"
    echo "Vui lòng kiểm tra lỗi ở trên"
    exit 1
fi
echo -e "${GREEN}✅ Build completed${NC}"
echo ""

# Install APK
echo "📲 Installing APK..."
./gradlew installDebug
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Install failed!${NC}"
    exit 1
fi
echo -e "${GREEN}✅ APK installed${NC}"
echo ""

# Launch app
echo "🚀 Launching app..."
adb shell am start -n com.htdgym.app/.activities.LoginActivity
if [ $? -ne 0 ]; then
    echo -e "${YELLOW}⚠️  Launch failed, trying alternative method...${NC}"
    adb shell monkey -p com.htdgym.app -c android.intent.category.LAUNCHER 1
fi
echo -e "${GREEN}✅ App launched!${NC}"
echo ""

# Show logs
echo "📋 Showing app logs (Ctrl+C to stop)..."
echo "======================================"
adb logcat | grep -E "htdgym|DatabaseInitializer|WorkoutDataHelper|LoginActivity"
