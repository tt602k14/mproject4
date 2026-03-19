<?php
/**
 * Test Connection Script
 * Kiểm tra kết nối MySQL và hiển thị thông tin hệ thống
 */

header('Content-Type: text/html; charset=utf-8');
?>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HTD GYM - Test Connection</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
        }
        .header {
            background: linear-gradient(135deg, #6FCF97 0%, #4CAF50 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        .header h1 {
            font-size: 32px;
            margin-bottom: 10px;
        }
        .header p {
            font-size: 16px;
            opacity: 0.9;
        }
        .content {
            padding: 30px;
        }
        .test-item {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            border-left: 5px solid #ddd;
        }
        .test-item.success {
            border-left-color: #4CAF50;
            background: #e8f5e9;
        }
        .test-item.error {
            border-left-color: #f44336;
            background: #ffebee;
        }
        .test-item h3 {
            margin-bottom: 10px;
            color: #333;
        }
        .test-item p {
            color: #666;
            line-height: 1.6;
        }
        .status {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: bold;
            margin-top: 10px;
        }
        .status.success {
            background: #4CAF50;
            color: white;
        }
        .status.error {
            background: #f44336;
            color: white;
        }
        .info-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        .info-table td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }
        .info-table td:first-child {
            font-weight: bold;
            width: 200px;
            color: #555;
        }
        .code {
            background: #2d2d2d;
            color: #f8f8f2;
            padding: 15px;
            border-radius: 5px;
            font-family: 'Courier New', monospace;
            font-size: 14px;
            overflow-x: auto;
            margin-top: 10px;
        }
        .btn {
            display: inline-block;
            padding: 12px 30px;
            background: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 25px;
            font-weight: bold;
            margin-top: 20px;
            transition: all 0.3s;
        }
        .btn:hover {
            background: #45a049;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(76, 175, 80, 0.3);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🏋️ HTD GYM</h1>
            <p>Kiểm tra kết nối hệ thống</p>
        </div>
        
        <div class="content">
            <?php
            // Test 1: PHP Version
            echo '<div class="test-item success">';
            echo '<h3>✅ Test 1: PHP Version</h3>';
            echo '<p>PHP đang chạy phiên bản: <strong>' . phpversion() . '</strong></p>';
            echo '<span class="status success">THÀNH CÔNG</span>';
            echo '</div>';
            
            // Test 2: MySQL Extension
            echo '<div class="test-item ' . (extension_loaded('mysqli') ? 'success' : 'error') . '">';
            echo '<h3>' . (extension_loaded('mysqli') ? '✅' : '❌') . ' Test 2: MySQL Extension</h3>';
            if (extension_loaded('mysqli')) {
                echo '<p>Extension MySQLi đã được cài đặt và kích hoạt</p>';
                echo '<span class="status success">THÀNH CÔNG</span>';
            } else {
                echo '<p>Extension MySQLi chưa được cài đặt. Vui lòng cài đặt MySQLi extension.</p>';
                echo '<span class="status error">LỖI</span>';
            }
            echo '</div>';
            
            // Test 3: Database Connection
            require_once 'config.php';
            
            $conn = @new mysqli(DB_HOST, DB_USER, DB_PASS);
            
            if ($conn->connect_error) {
                echo '<div class="test-item error">';
                echo '<h3>❌ Test 3: Kết nối MySQL</h3>';
                echo '<p>Không thể kết nối đến MySQL Server</p>';
                echo '<p><strong>Lỗi:</strong> ' . $conn->connect_error . '</p>';
                echo '<p><strong>Giải pháp:</strong></p>';
                echo '<ul style="margin-left: 20px; margin-top: 10px;">';
                echo '<li>Mở XAMPP Control Panel</li>';
                echo '<li>Nhấn nút START bên cạnh MySQL</li>';
                echo '<li>Đợi đến khi MySQL hiển thị màu xanh</li>';
                echo '<li>Refresh lại trang này</li>';
                echo '</ul>';
                echo '<span class="status error">LỖI</span>';
                echo '</div>';
            } else {
                echo '<div class="test-item success">';
                echo '<h3>✅ Test 3: Kết nối MySQL</h3>';
                echo '<p>Kết nối đến MySQL Server thành công!</p>';
                echo '<table class="info-table">';
                echo '<tr><td>Host:</td><td>' . DB_HOST . '</td></tr>';
                echo '<tr><td>User:</td><td>' . DB_USER . '</td></tr>';
                echo '<tr><td>Server Version:</td><td>' . $conn->server_info . '</td></tr>';
                echo '</table>';
                echo '<span class="status success">THÀNH CÔNG</span>';
                echo '</div>';
                
                // Test 4: Database Exists
                $db_selected = $conn->select_db(DB_NAME);
                
                if (!$db_selected) {
                    echo '<div class="test-item error">';
                    echo '<h3>❌ Test 4: Database "' . DB_NAME . '"</h3>';
                    echo '<p>Database chưa được tạo</p>';
                    echo '<p><strong>Giải pháp:</strong></p>';
                    echo '<ol style="margin-left: 20px; margin-top: 10px;">';
                    echo '<li>Truy cập: <a href="http://localhost/phpmyadmin" target="_blank">http://localhost/phpmyadmin</a></li>';
                    echo '<li>Nhấn vào tab "SQL"</li>';
                    echo '<li>Copy nội dung file <code>backend/database.sql</code></li>';
                    echo '<li>Paste vào ô SQL và nhấn "Go"</li>';
                    echo '</ol>';
                    echo '<span class="status error">LỖI</span>';
                    echo '</div>';
                } else {
                    echo '<div class="test-item success">';
                    echo '<h3>✅ Test 4: Database "' . DB_NAME . '"</h3>';
                    echo '<p>Database đã tồn tại và có thể truy cập</p>';
                    
                    // Get tables
                    $result = $conn->query("SHOW TABLES");
                    $tables = [];
                    while ($row = $result->fetch_array()) {
                        $tables[] = $row[0];
                    }
                    
                    echo '<p><strong>Các bảng trong database:</strong></p>';
                    echo '<ul style="margin-left: 20px; margin-top: 10px;">';
                    foreach ($tables as $table) {
                        echo '<li>' . $table . '</li>';
                    }
                    echo '</ul>';
                    echo '<span class="status success">THÀNH CÔNG</span>';
                    echo '</div>';
                    
                    // Test 5: Users Table
                    $result = $conn->query("SELECT COUNT(*) as count FROM users");
                    if ($result) {
                        $row = $result->fetch_assoc();
                        echo '<div class="test-item success">';
                        echo '<h3>✅ Test 5: Bảng Users</h3>';
                        echo '<p>Số lượng tài khoản đã đăng ký: <strong>' . $row['count'] . '</strong></p>';
                        
                        if ($row['count'] > 0) {
                            $users = $conn->query("SELECT id, username, email, full_name, created_at FROM users ORDER BY created_at DESC LIMIT 5");
                            echo '<p><strong>5 tài khoản mới nhất:</strong></p>';
                            echo '<table class="info-table">';
                            echo '<tr><td><strong>ID</strong></td><td><strong>Username</strong></td><td><strong>Email</strong></td><td><strong>Họ tên</strong></td></tr>';
                            while ($user = $users->fetch_assoc()) {
                                echo '<tr>';
                                echo '<td>' . $user['id'] . '</td>';
                                echo '<td>' . $user['username'] . '</td>';
                                echo '<td>' . $user['email'] . '</td>';
                                echo '<td>' . $user['full_name'] . '</td>';
                                echo '</tr>';
                            }
                            echo '</table>';
                        }
                        echo '<span class="status success">THÀNH CÔNG</span>';
                        echo '</div>';
                    }
                }
                
                $conn->close();
            }
            
            // Test 6: API Endpoint
            echo '<div class="test-item success">';
            echo '<h3>✅ Test 6: API Endpoint</h3>';
            echo '<p>URL API của bạn:</p>';
            echo '<div class="code">http://localhost/htd_gym/backend/api/auth.php</div>';
            echo '<p style="margin-top: 15px;">Để test từ Android Emulator, sử dụng:</p>';
            echo '<div class="code">http://10.0.2.2/htd_gym/backend/api/auth.php</div>';
            echo '<p style="margin-top: 15px;">Để test từ thiết bị thật, sử dụng IP máy tính:</p>';
            echo '<div class="code">http://[IP_CUA_BAN]/htd_gym/backend/api/auth.php</div>';
            echo '<p style="margin-top: 10px; font-size: 14px; color: #666;">Gõ <code>ipconfig</code> trong Command Prompt để xem IP</p>';
            echo '<span class="status success">THÀNH CÔNG</span>';
            echo '</div>';
            ?>
            
            <div style="text-align: center; margin-top: 30px;">
                <a href="http://localhost/phpmyadmin" class="btn" target="_blank">Mở phpMyAdmin</a>
                <a href="test_connection.php" class="btn" style="background: #2196F3;">Refresh</a>
            </div>
        </div>
    </div>
</body>
</html>
