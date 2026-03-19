<?php
require_once '../config.php';

$conn = getDBConnection();
$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        if (isset($_GET['id'])) {
            getEquipmentById($conn, $_GET['id']);
        } else {
            getAllEquipment($conn);
        }
        break;
        
    case 'POST':
        createEquipment($conn);
        break;
        
    case 'PUT':
        updateEquipment($conn);
        break;
        
    case 'DELETE':
        deleteEquipment($conn);
        break;
        
    default:
        sendResponse(false, 'Method not allowed');
}

function getAllEquipment($conn) {
    $sql = "SELECT * FROM equipment ORDER BY name";
    $result = $conn->query($sql);
    
    $equipment = [];
    while ($row = $result->fetch_assoc()) {
        $equipment[] = $row;
    }
    
    sendResponse(true, 'Equipment retrieved successfully', $equipment);
}

function getEquipmentById($conn, $id) {
    $stmt = $conn->prepare("SELECT * FROM equipment WHERE id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($equip = $result->fetch_assoc()) {
        sendResponse(true, 'Equipment retrieved successfully', $equip);
    } else {
        sendResponse(false, 'Equipment not found');
    }
}

function createEquipment($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("INSERT INTO equipment (name, category, quantity, condition_status, purchase_date, last_maintenance, notes) VALUES (?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("ssissss", 
        $data['name'],
        $data['category'],
        $data['quantity'],
        $data['condition_status'],
        $data['purchase_date'],
        $data['last_maintenance'],
        $data['notes']
    );
    
    if ($stmt->execute()) {
        sendResponse(true, 'Equipment created successfully', ['id' => $conn->insert_id]);
    } else {
        sendResponse(false, 'Failed to create equipment');
    }
}

function updateEquipment($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("UPDATE equipment SET name = ?, category = ?, quantity = ?, condition_status = ?, purchase_date = ?, last_maintenance = ?, notes = ? WHERE id = ?");
    $stmt->bind_param("ssissssi", 
        $data['name'],
        $data['category'],
        $data['quantity'],
        $data['condition_status'],
        $data['purchase_date'],
        $data['last_maintenance'],
        $data['notes'],
        $data['id']
    );
    
    if ($stmt->execute()) {
        sendResponse(true, 'Equipment updated successfully');
    } else {
        sendResponse(false, 'Failed to update equipment');
    }
}

function deleteEquipment($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("DELETE FROM equipment WHERE id = ?");
    $stmt->bind_param("i", $data['id']);
    
    if ($stmt->execute()) {
        sendResponse(true, 'Equipment deleted successfully');
    } else {
        sendResponse(false, 'Failed to delete equipment');
    }
}

$conn->close();
?>
