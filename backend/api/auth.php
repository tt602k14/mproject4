<?php
// Disable error display and force JSON output
error_reporting(0);
ini_set('display_errors', 0);

// Set JSON header first
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle preflight
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

try {
    require_once '../config.php';
    $conn = getDBConnection();
    $method = $_SERVER['REQUEST_METHOD'];
} catch (Exception $e) {
    echo json_encode([
        'success' => false,
        'message' => 'Database connection failed. Please check if MySQL is running in XAMPP.'
    ]);
    exit();
}

switch ($method) {
    case 'POST':
        $data = getPostData();
        if (isset($data['action'])) {
            switch ($data['action']) {
                case 'register':
                    register($conn, $data);
                    break;
                case 'login':
                    login($conn, $data);
                    break;
                default:
                    sendResponse(false, 'Invalid action');
            }
        } else {
            sendResponse(false, 'Action required');
        }
        break;
        
    default:
        sendResponse(false, 'Method not allowed');
}

function register($conn, $data) {
    try {
        // Validate input
        if (!isset($data['username']) || !isset($data['email']) || !isset($data['password'])) {
            sendResponse(false, 'Username, email and password are required');
            return;
        }
        
        $username = trim($data['username']);
        $email = trim($data['email']);
        $password = $data['password'];
        $full_name = isset($data['full_name']) ? trim($data['full_name']) : '';
        $phone = isset($data['phone']) ? trim($data['phone']) : '';
        
        // Check if username already exists
        $stmt = $conn->prepare("SELECT id FROM users WHERE username = ?");
        if (!$stmt) {
            sendResponse(false, 'Database error: ' . $conn->error);
            return;
        }
        
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $result = $stmt->get_result();
        
        if ($result->num_rows > 0) {
            sendResponse(false, 'Email đã được sử dụng');
            return;
        }
        
        // Check if email already exists
        $stmt = $conn->prepare("SELECT id FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $result = $stmt->get_result();
        
        if ($result->num_rows > 0) {
            sendResponse(false, 'Email đã được sử dụng');
            return;
        }
        
        // Hash password
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);
        
        // Insert new user
        $stmt = $conn->prepare("INSERT INTO users (username, email, password, full_name, phone) VALUES (?, ?, ?, ?, ?)");
        if (!$stmt) {
            sendResponse(false, 'Database error: ' . $conn->error);
            return;
        }
        
        $stmt->bind_param("sssss", $username, $email, $hashed_password, $full_name, $phone);
        
        if ($stmt->execute()) {
            $user_id = $conn->insert_id;
            
            // Get user data
            $stmt = $conn->prepare("SELECT id, username, email, full_name, phone, created_at FROM users WHERE id = ?");
            $stmt->bind_param("i", $user_id);
            $stmt->execute();
            $result = $stmt->get_result();
            $user = $result->fetch_assoc();
            
            sendResponse(true, 'Đăng ký thành công', $user);
        } else {
            sendResponse(false, 'Đăng ký thất bại: ' . $stmt->error);
        }
    } catch (Exception $e) {
        sendResponse(false, 'Lỗi: ' . $e->getMessage());
    }
}

function login($conn, $data) {
    // Validate input
    if (!isset($data['username']) || !isset($data['password'])) {
        sendResponse(false, 'Username and password are required');
        return;
    }
    
    $username = trim($data['username']);
    $password = $data['password'];
    
    // Get user by username
    $stmt = $conn->prepare("SELECT id, username, email, password, full_name, phone, profile_image, created_at FROM users WHERE username = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows === 0) {
        sendResponse(false, 'Invalid username or password');
        return;
    }
    
    $user = $result->fetch_assoc();
    
    // Verify password
    if (password_verify($password, $user['password'])) {
        // Remove password from response
        unset($user['password']);
        
        sendResponse(true, 'Login successful', $user);
    } else {
        sendResponse(false, 'Invalid username or password');
    }
}

$conn->close();
?>
