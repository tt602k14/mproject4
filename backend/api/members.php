<?php
require_once '../config.php';

$conn = getDBConnection();
$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        if (isset($_GET['id'])) {
            getMemberById($conn, $_GET['id']);
        } else {
            getAllMembers($conn);
        }
        break;
        
    case 'POST':
        createMember($conn);
        break;
        
    case 'PUT':
        updateMember($conn);
        break;
        
    case 'DELETE':
        deleteMember($conn);
        break;
        
    default:
        sendResponse(false, 'Method not allowed');
}

function getAllMembers($conn) {
    $sql = "SELECT * FROM members ORDER BY created_at DESC";
    $result = $conn->query($sql);
    
    $members = [];
    while ($row = $result->fetch_assoc()) {
        $members[] = $row;
    }
    
    sendResponse(true, 'Members retrieved successfully', $members);
}

function getMemberById($conn, $id) {
    $stmt = $conn->prepare("SELECT * FROM members WHERE id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($member = $result->fetch_assoc()) {
        sendResponse(true, 'Member retrieved successfully', $member);
    } else {
        sendResponse(false, 'Member not found');
    }
}

function createMember($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("INSERT INTO members (name, email, phone, membership_type, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("sssssss", 
        $data['name'], 
        $data['email'], 
        $data['phone'], 
        $data['membership_type'], 
        $data['start_date'], 
        $data['end_date'],
        $data['status']
    );
    
    if ($stmt->execute()) {
        sendResponse(true, 'Member created successfully', ['id' => $conn->insert_id]);
    } else {
        sendResponse(false, 'Failed to create member');
    }
}

function updateMember($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("UPDATE members SET name = ?, email = ?, phone = ?, membership_type = ?, start_date = ?, end_date = ?, status = ? WHERE id = ?");
    $stmt->bind_param("sssssssi", 
        $data['name'], 
        $data['email'], 
        $data['phone'], 
        $data['membership_type'], 
        $data['start_date'], 
        $data['end_date'],
        $data['status'],
        $data['id']
    );
    
    if ($stmt->execute()) {
        sendResponse(true, 'Member updated successfully');
    } else {
        sendResponse(false, 'Failed to update member');
    }
}

function deleteMember($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("DELETE FROM members WHERE id = ?");
    $stmt->bind_param("i", $data['id']);
    
    if ($stmt->execute()) {
        sendResponse(true, 'Member deleted successfully');
    } else {
        sendResponse(false, 'Failed to delete member');
    }
}

$conn->close();
?>
