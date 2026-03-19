<?php
// File kiểm tra MySQL đơn giản
header('Content-Type: text/html; charset=utf-8');
?>
<!DOCTYPE html>
<html>
<head>
    <title>Kiểm tra MySQL</title>
    <style>
        body { font-family: Arial; padding: 20px; background: #f5f5f5; }
        .box { background: white; padding: 20px; border-radius: 10px; margin: 10px 0; }
        .success { color: green; font-weight: bold; }
        .error { color: red; font-weight: bold; }
        .code { background: #f0f0f0; padding: 10px; border-radius: 5px; margin: 10px 0; }
    </style>
</head>
<body>
    <h1>🔍 Kiểm tra MySQL - HTD GYM</h1>
    
    <?php
    // Test 1: Kết nối MySQL
    echo '<div class="box">';
    echo '<h2>Test 1: Kết nối MySQL</h2>';
    
    $conn = @new mysqli('localhost', 'root', '');
    
    if ($conn->connect_error) {
        echo '<p class="error">❌ KHÔNG KẾT NỐI ĐƯỢC!</p>';
        echo '<p>Lỗi: ' . $conn->connect_error . '</p>';
        echo '<p><strong>Cách fix:</strong></p>';
        echo '<ol>';
        echo '<li>Mở XAMPP Control Panel</li>';
        echo '<li>Nhấn START bên cạnh MySQL</li>';
        echo '<li>Đợi MySQL màu xanh</li>';
        echo '<li>Refresh lại trang này</li>';
        echo '</ol>';
        echo '</div>';
        exit;
    } else {
        echo '<p class="success">✅ Kết nối MySQL thành công!</p>';
        echo '<p>Server: ' . $conn->host_info . '</p>';
    }
    echo '</div>';
    
    // Test 2: Kiểm tra database
    echo '<div class="box">';
    echo '<h2>Test 2: Database "htd_gym"</h2>';
    
    $db_exists = $conn->select_db('htd_gym');
    
    if (!$db_exists) {
        echo '<p class="error">❌ Database "htd_gym" chưa tạo!</p>';
        echo '<p><strong>Cách fix:</strong></p>';
        echo '<ol>';
        echo '<li>Mở: <a href="http://localhost/phpmyadmin" target="_blank">http://localhost/phpmyadmin</a></li>';
        echo '<li>Nhấn tab "SQL"</li>';
        echo '<li>Copy file: <code>backend/create_database_simple.sql</code></li>';
        echo '<li>Paste vào ô SQL và nhấn "Go"</li>';
        echo '<li>Refresh lại trang này</li>';
        echo '</ol>';
        echo '</div>';
        exit;
    } else {
        echo '<p class="success">✅ Database "htd_gym" đã tồn tại!</p>';
    }
    echo '</div>';
    
    // Test 3: Kiểm tra bảng users
    echo '<div class="box">';
    echo '<h2>Test 3: Bảng "users"</h2>';
    
    $result = $conn->query("SHOW TABLES LIKE 'users'");
    
    if ($result->num_rows == 0) {
        echo '<p class="error">❌ Bảng "users" chưa tạo!</p>';
        echo '<p>Chạy lại file create_database_simple.sql</p>';
        echo '</div>';
        exit;
    } else {
        echo '<p class="success">✅ Bảng "users" đã tồn tại!</p>';
        
        // Đếm số user
        $count = $conn->query("SELECT COUNT(*) as total FROM users")->fetch_assoc();
        echo '<p>Số tài khoản: <strong>' . $count['total'] . '</strong></p>';
        
        // Hiển thị users
        $users = $conn->query("SELECT id, username, email, full_name, created_at FROM users ORDER BY id DESC LIMIT 5");
        if ($users->num_rows > 0) {
            echo '<table border="1" cellpadding="10" style="width:100%; border-collapse: collapse;">';
            echo '<tr><th>ID</th><th>Username</th><th>Email</th><th>Họ tên</th><th>Ngày tạo</th></tr>';
            while ($user = $users->fetch_assoc()) {
                echo '<tr>';
                echo '<td>' . $user['id'] . '</td>';
                echo '<td>' . $user['username'] . '</td>';
                echo '<td>' . $user['email'] . '</td>';
                echo '<td>' . $user['full_name'] . '</td>';
                echo '<td>' . $user['created_at'] . '</td>';
                echo '</tr>';
            }
            echo '</table>';
        }
    }
    echo '</div>';
    
    // Test 4: Test API
    echo '<div class="box">';
    echo '<h2>Test 4: API Endpoint</h2>';
    echo '<p class="success">✅ Tất cả OK! Bây giờ test API:</p>';
    echo '<div class="code">';
    echo '<strong>URL cho Emulator:</strong><br>';
    echo 'http://10.0.2.2/htd_gym/backend/api/auth.php';
    echo '</div>';
    echo '<p><a href="test_register.html" style="background:#4CAF50; color:white; padding:10px 20px; text-decoration:none; border-radius:5px; display:inline-block;">Test Đăng Ký</a></p>';
    echo '</div>';
    
    $conn->close();
    ?>
    
    <div class="box" style="background: #e8f5e9;">
        <h2>🎉 Hệ thống sẵn sàng!</h2>
        <p>Bây giờ bạn có thể:</p>
        <ol>
            <li>Test đăng ký từ web: <a href="test_register.html">test_register.html</a></li>
            <li>Chạy app Android và đăng ký</li>
            <li>Kiểm tra dữ liệu trong <a href="http://localhost/phpmyadmin" target="_blank">phpMyAdmin</a></li>
        </ol>
    </div>
</body>
</html>
