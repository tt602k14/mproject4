<?php
require_once '../config.php';

$conn = getDBConnection();
$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        if (isset($_GET['id'])) {
            getWorkoutById($conn, $_GET['id']);
        } elseif (isset($_GET['category'])) {
            getWorkoutsByCategory($conn, $_GET['category']);
        } else {
            getAllWorkouts($conn);
        }
        break;
        
    case 'POST':
        createWorkout($conn);
        break;
        
    case 'PUT':
        updateWorkout($conn);
        break;
        
    case 'DELETE':
        deleteWorkout($conn);
        break;
        
    default:
        sendResponse(false, 'Method not allowed');
}

function getAllWorkouts($conn) {
    $sql = "SELECT * FROM workouts ORDER BY created_at DESC";
    $result = $conn->query($sql);
    
    $workouts = [];
    while ($row = $result->fetch_assoc()) {
        $workouts[] = $row;
    }
    
    sendResponse(true, 'Workouts retrieved successfully', $workouts);
}

function getWorkoutById($conn, $id) {
    $stmt = $conn->prepare("SELECT * FROM workouts WHERE id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($workout = $result->fetch_assoc()) {
        // Get exercises for this workout
        $stmt2 = $conn->prepare("SELECT * FROM exercises WHERE workout_id = ? ORDER BY order_index");
        $stmt2->bind_param("i", $id);
        $stmt2->execute();
        $exercises_result = $stmt2->get_result();
        
        $exercises = [];
        while ($exercise = $exercises_result->fetch_assoc()) {
            $exercises[] = $exercise;
        }
        
        $workout['exercises'] = $exercises;
        sendResponse(true, 'Workout retrieved successfully', $workout);
    } else {
        sendResponse(false, 'Workout not found');
    }
}

function getWorkoutsByCategory($conn, $category) {
    $stmt = $conn->prepare("SELECT * FROM workouts WHERE category = ? ORDER BY created_at DESC");
    $stmt->bind_param("s", $category);
    $stmt->execute();
    $result = $stmt->get_result();
    
    $workouts = [];
    while ($row = $result->fetch_assoc()) {
        $workouts[] = $row;
    }
    
    sendResponse(true, 'Workouts retrieved successfully', $workouts);
}

function createWorkout($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("INSERT INTO workouts (name, category, description, duration, calories, difficulty) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("sssiis", 
        $data['name'], 
        $data['category'], 
        $data['description'], 
        $data['duration'], 
        $data['calories'], 
        $data['difficulty']
    );
    
    if ($stmt->execute()) {
        sendResponse(true, 'Workout created successfully', ['id' => $conn->insert_id]);
    } else {
        sendResponse(false, 'Failed to create workout');
    }
}

function updateWorkout($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("UPDATE workouts SET name = ?, category = ?, description = ?, duration = ?, calories = ?, difficulty = ? WHERE id = ?");
    $stmt->bind_param("sssiisi", 
        $data['name'], 
        $data['category'], 
        $data['description'], 
        $data['duration'], 
        $data['calories'], 
        $data['difficulty'],
        $data['id']
    );
    
    if ($stmt->execute()) {
        sendResponse(true, 'Workout updated successfully');
    } else {
        sendResponse(false, 'Failed to update workout');
    }
}

function deleteWorkout($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("DELETE FROM workouts WHERE id = ?");
    $stmt->bind_param("i", $data['id']);
    
    if ($stmt->execute()) {
        sendResponse(true, 'Workout deleted successfully');
    } else {
        sendResponse(false, 'Failed to delete workout');
    }
}

$conn->close();
?>
