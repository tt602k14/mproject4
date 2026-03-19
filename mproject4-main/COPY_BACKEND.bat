@echo off
echo ========================================
echo COPY BACKEND VAO XAMPP
echo ========================================
echo.

REM Tao thu muc htd_gym neu chua co
if not exist "C:\xampp\htdocs\htd_gym" (
    mkdir "C:\xampp\htdocs\htd_gym"
    echo [OK] Tao thu muc htd_gym
)

REM Copy backend
echo [COPYING] Dang copy backend...
xcopy /E /I /Y "backend" "C:\xampp\htdocs\htd_gym\backend"

echo.
echo ========================================
echo HOAN THANH!
echo ========================================
echo.
echo Backend da duoc copy vao:
echo C:\xampp\htdocs\htd_gym\backend\
echo.
echo Bay gio mo trinh duyet va test:
echo http://localhost/htd_gym/backend/check_mysql.php
echo.
pause
