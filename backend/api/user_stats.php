<?php
require_once '../config.php';

$conn = getDBConnection();
$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        if (isset($_GET['user_id']) && isset($_GET['date'])) {
            getStatsByDate($conn, $_GET['user_id'], $_GET['date']);
        } elseif (isset($_GET['user_id'])) {
            getUserStats($conn, $_GET['user_id']);
        } else {
            sendResponse(false, 'User ID required');
        }
        break;
        
    case 'POST':
        updateStats($conn);
        break;
        
    default:
        sendResponse(false, 'Method not allowed');
}

function getUserStats($conn, $user_id) {
    $stmt = $conn->prepare("SELECT * FROM user_stats WHERE user_id = ? ORDER BY date DESC LIMIT 30");
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    $stats = [];
    while ($row = $result->fetch_assoc()) {
        $stats[] = $row;
    }
    
    sendResponse(true, 'Stats retrieved successfully', $stats);
}

function getStatsByDate($conn, $user_id, $date) {
    $stmt = $conn->prepare("SELECT * FROM user_stats WHERE user_id = ? AND date = ?");
    $stmt->bind_param("is", $user_id, $date);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($stat = $result->fetch_assoc()) {
        sendResponse(true, 'Stats retrieved successfully', $stat);
    } else {
        // Return empty stats for the date
        sendResponse(true, 'No stats for this date', [
            'user_id' => $user_id,
            'date' => $date,
            'calories_burned' => 0,
            'workout_duration' => 0,
            'workouts_completed' => 0,
            'steps' => 0,
            'water_intake' => 0
        ]);
    }
}

function updateStats($conn) {
    $data = getPostData();
    
    // Check if stats exist for this date
    $stmt = $conn->prepare("SELECT id FROM user_stats WHERE user_id = ? AND date = ?");
    $stmt->bind_param("is", $data['user_id'], $data['date']);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows > 0) {
        // Update existing stats
        $stmt = $conn->prepare("UPDATE user_stats SET calories_burned = ?, workout_duration = ?, workouts_completed = ?, steps = ?, water_intake = ? WHERE user_id = ? AND date = ?");
        $stmt->bind_param("iiiiiss", 
            $data['calories_burned'],
            $data['workout_duration'],
            $data['workouts_completed'],
            $data['steps'],
            $data['water_intake'],
            $data['user_id'],
            $data['date']
        );
    } else {
        // Insert new stats
        $stmt = $conn->prepare("INSERT INTO user_stats (user_id, date, calories_burned, workout_duration, workouts_completed, steps, water_intake) VALUES (?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("isiiii", 
            $data['user_id'],
            $data['date'],
            $data['calories_burned'],
            $data['workout_duration'],
            $data['workouts_completed'],
            $data['steps'],
            $data['water_intake']
        );
    }
    
    if ($stmt->execute()) {
        sendResponse(true, 'Stats updated successfully');
    } else {
        sendResponse(false, 'Failed to update stats');
    }
}

$conn->close();
?>
