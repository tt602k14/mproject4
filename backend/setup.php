<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HTD GYM - Setup Database</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            max-width: 600px;
            width: 100%;
            padding: 40px;
        }
        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 32px;
        }
        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-size: 16px;
        }
        .status {
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 15px;
            font-size: 14px;
        }
        .success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .info {
            background: #d1ecf1;
            color: #0c5460;
            border: 1px solid #bee5eb;
        }
        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 15px 30px;
            border-radius: 10px;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
            margin-top: 20px;
            transition: transform 0.2s;
        }
        .btn:hover {
            transform: translateY(-2px);
        }
        .btn:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }
        .progress {
            margin: 20px 0;
        }
        .progress-item {
            display: flex;
            align-items: center;
            padding: 10px;
            margin-bottom: 10px;
            background: #f8f9fa;
            border-radius: 8px;
        }
        .progress-icon {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            font-size: 16px;
        }
        .progress-icon.pending { background: #ffc107; }
        .progress-icon.success { background: #28a745; color: white; }
        .progress-icon.error { background: #dc3545; color: white; }
        .api-test {
            margin-top: 30px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 10px;
        }
        .api-test h3 {
            margin-bottom: 15px;
            color: #333;
        }
        .api-endpoint {
            background: white;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 10px;
            font-family: monospace;
            font-size: 12px;
            word-break: break-all;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏋️ HTD GYM</h1>
        <p class="subtitle">Cài đặt Database & API</p>

        <?php
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            echo '<div class="progress">';
            
            // Step 1: Connect to MySQL
            echo '<div class="progress-item">';
            echo '<div class="progress-icon pending">1</div>';
            echo '<div>Kết nối MySQL...</div>';
            echo '</div>';
            
            $conn = new mysqli('localhost', 'root', '');
            
            if ($conn->connect_error) {
                echo '<div class="status error">❌ Lỗi kết nối: ' . $conn->connect_error . '</div>';
                echo '<div class="status info">💡 Hãy đảm bảo MySQL đã được khởi động trong XAMPP Control Panel</div>';
                exit;
            }
            
            echo '<script>document.querySelectorAll(".progress-icon")[0].classList.remove("pending"); document.querySelectorAll(".progress-icon")[0].classList.add("success"); document.querySelectorAll(".progress-icon")[0].innerHTML = "✓";</script>';
            
            // Step 2: Create Database
            echo '<div class="progress-item">';
            echo '<div class="progress-icon pending">2</div>';
            echo '<div>Tạo database htd_gym...</div>';
            echo '</div>';
            
            $sql = "CREATE DATABASE IF NOT EXISTS htd_gym CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
            if ($conn->query($sql) === TRUE) {
                echo '<script>document.querySelectorAll(".progress-icon")[1].classList.remove("pending"); document.querySelectorAll(".progress-icon")[1].classList.add("success"); document.querySelectorAll(".progress-icon")[1].innerHTML = "✓";</script>';
            } else {
                echo '<div class="status error">❌ Lỗi tạo database: ' . $conn->error . '</div>';
                exit;
            }
            
            $conn->select_db('htd_gym');
            
            // Step 3: Create Tables
            echo '<div class="progress-item">';
            echo '<div class="progress-icon pending">3</div>';
            echo '<div>Tạo các bảng...</div>';
            echo '</div>';
            
            $sql_file = file_get_contents('database.sql');
            $queries = explode(';', $sql_file);
            
            $success_count = 0;
            foreach ($queries as $query) {
                $query = trim($query);
                if (!empty($query)) {
                    if ($conn->query($query)) {
                        $success_count++;
                    }
                }
            }
            
            echo '<script>document.querySelectorAll(".progress-icon")[2].classList.remove("pending"); document.querySelectorAll(".progress-icon")[2].classList.add("success"); document.querySelectorAll(".progress-icon")[2].innerHTML = "✓";</script>';
            
            echo '<div class="status success">✅ Cài đặt thành công! Database đã sẵn sàng.</div>';
            echo '<div class="status info">📊 Đã tạo ' . $success_count . ' câu lệnh SQL</div>';
            
            // API Test Section
            $base_url = 'http://' . $_SERVER['HTTP_HOST'] . dirname($_SERVER['PHP_SELF']) . '/api/';
            
            echo '<div class="api-test">';
            echo '<h3>🔗 API Endpoints</h3>';
            echo '<p style="margin-bottom: 15px; color: #666;">Copy các URL này để test API:</p>';
            echo '<div class="api-endpoint">GET: ' . $base_url . 'workouts.php</div>';
            echo '<div class="api-endpoint">GET: ' . $base_url . 'members.php</div>';
            echo '<div class="api-endpoint">GET: ' . $base_url . 'equipment.php</div>';
            echo '<div class="api-endpoint">GET: ' . $base_url . 'videos.php</div>';
            echo '</div>';
            
            echo '<div class="status info">';
            echo '📱 <strong>Cấu hình Android App:</strong><br>';
            echo 'Mở file <code>ApiConfig.java</code> và sửa:<br>';
            echo '<code>BASE_URL = "' . $base_url . '"</code>';
            echo '</div>';
            
            $conn->close();
        } else {
        ?>
            <div class="status info">
                ℹ️ Chương trình này sẽ tự động:<br>
                • Tạo database <strong>htd_gym</strong><br>
                • Tạo tất cả các bảng cần thiết<br>
                • Thêm dữ liệu mẫu<br>
                • Cấu hình API endpoints
            </div>

            <div class="status error">
                ⚠️ <strong>Lưu ý:</strong> Đảm bảo MySQL đã được khởi động trong XAMPP Control Panel
            </div>

            <form method="POST">
                <button type="submit" class="btn">🚀 Bắt đầu cài đặt</button>
            </form>
        <?php } ?>
    </div>
</body>
</html>
