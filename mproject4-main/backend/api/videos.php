<?php
require_once '../config.php';

$conn = getDBConnection();
$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        if (isset($_GET['id'])) {
            getVideoById($conn, $_GET['id']);
        } elseif (isset($_GET['category'])) {
            getVideosByCategory($conn, $_GET['category']);
        } else {
            getAllVideos($conn);
        }
        break;
        
    case 'POST':
        createVideo($conn);
        break;
        
    case 'PUT':
        updateVideo($conn);
        break;
        
    case 'DELETE':
        deleteVideo($conn);
        break;
        
    default:
        sendResponse(false, 'Method not allowed');
}

function getAllVideos($conn) {
    $sql = "SELECT * FROM videos ORDER BY created_at DESC";
    $result = $conn->query($sql);
    
    $videos = [];
    while ($row = $result->fetch_assoc()) {
        $videos[] = $row;
    }
    
    sendResponse(true, 'Videos retrieved successfully', $videos);
}

function getVideoById($conn, $id) {
    $stmt = $conn->prepare("SELECT * FROM videos WHERE id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($video = $result->fetch_assoc()) {
        // Increment views
        $update = $conn->prepare("UPDATE videos SET views = views + 1 WHERE id = ?");
        $update->bind_param("i", $id);
        $update->execute();
        
        sendResponse(true, 'Video retrieved successfully', $video);
    } else {
        sendResponse(false, 'Video not found');
    }
}

function getVideosByCategory($conn, $category) {
    $stmt = $conn->prepare("SELECT * FROM videos WHERE category = ? ORDER BY created_at DESC");
    $stmt->bind_param("s", $category);
    $stmt->execute();
    $result = $stmt->get_result();
    
    $videos = [];
    while ($row = $result->fetch_assoc()) {
        $videos[] = $row;
    }
    
    sendResponse(true, 'Videos retrieved successfully', $videos);
}

function createVideo($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("INSERT INTO videos (title, description, category, duration, thumbnail_url, video_url) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("sssiss", 
        $data['title'],
        $data['description'],
        $data['category'],
        $data['duration'],
        $data['thumbnail_url'],
        $data['video_url']
    );
    
    if ($stmt->execute()) {
        sendResponse(true, 'Video created successfully', ['id' => $conn->insert_id]);
    } else {
        sendResponse(false, 'Failed to create video');
    }
}

function updateVideo($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("UPDATE videos SET title = ?, description = ?, category = ?, duration = ?, thumbnail_url = ?, video_url = ? WHERE id = ?");
    $stmt->bind_param("sssissi", 
        $data['title'],
        $data['description'],
        $data['category'],
        $data['duration'],
        $data['thumbnail_url'],
        $data['video_url'],
        $data['id']
    );
    
    if ($stmt->execute()) {
        sendResponse(true, 'Video updated successfully');
    } else {
        sendResponse(false, 'Failed to update video');
    }
}

function deleteVideo($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("DELETE FROM videos WHERE id = ?");
    $stmt->bind_param("i", $data['id']);
    
    if ($stmt->execute()) {
        sendResponse(true, 'Video deleted successfully');
    } else {
        sendResponse(false, 'Failed to delete video');
    }
}

$conn->close();
?>
