<?php
require_once '../config.php';

$conn = getDBConnection();
$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        if (isset($_GET['id'])) {
            getPaymentById($conn, $_GET['id']);
        } elseif (isset($_GET['member_id'])) {
            getPaymentsByMember($conn, $_GET['member_id']);
        } else {
            getAllPayments($conn);
        }
        break;
        
    case 'POST':
        createPayment($conn);
        break;
        
    case 'PUT':
        updatePayment($conn);
        break;
        
    case 'DELETE':
        deletePayment($conn);
        break;
        
    default:
        sendResponse(false, 'Method not allowed');
}

function getAllPayments($conn) {
    $sql = "SELECT p.*, m.name as member_name FROM payments p 
            LEFT JOIN members m ON p.member_id = m.id 
            ORDER BY p.payment_date DESC";
    $result = $conn->query($sql);
    
    $payments = [];
    while ($row = $result->fetch_assoc()) {
        $payments[] = $row;
    }
    
    sendResponse(true, 'Payments retrieved successfully', $payments);
}

function getPaymentById($conn, $id) {
    $stmt = $conn->prepare("SELECT p.*, m.name as member_name FROM payments p 
                           LEFT JOIN members m ON p.member_id = m.id 
                           WHERE p.id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($payment = $result->fetch_assoc()) {
        sendResponse(true, 'Payment retrieved successfully', $payment);
    } else {
        sendResponse(false, 'Payment not found');
    }
}

function getPaymentsByMember($conn, $member_id) {
    $stmt = $conn->prepare("SELECT * FROM payments WHERE member_id = ? ORDER BY payment_date DESC");
    $stmt->bind_param("i", $member_id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    $payments = [];
    while ($row = $result->fetch_assoc()) {
        $payments[] = $row;
    }
    
    sendResponse(true, 'Payments retrieved successfully', $payments);
}

function createPayment($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("INSERT INTO payments (member_id, amount, payment_date, payment_method, description, status) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("idssss", 
        $data['member_id'],
        $data['amount'],
        $data['payment_date'],
        $data['payment_method'],
        $data['description'],
        $data['status']
    );
    
    if ($stmt->execute()) {
        sendResponse(true, 'Payment created successfully', ['id' => $conn->insert_id]);
    } else {
        sendResponse(false, 'Failed to create payment');
    }
}

function updatePayment($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("UPDATE payments SET member_id = ?, amount = ?, payment_date = ?, payment_method = ?, description = ?, status = ? WHERE id = ?");
    $stmt->bind_param("idssssi", 
        $data['member_id'],
        $data['amount'],
        $data['payment_date'],
        $data['payment_method'],
        $data['description'],
        $data['status'],
        $data['id']
    );
    
    if ($stmt->execute()) {
        sendResponse(true, 'Payment updated successfully');
    } else {
        sendResponse(false, 'Failed to update payment');
    }
}

function deletePayment($conn) {
    $data = getPostData();
    
    $stmt = $conn->prepare("DELETE FROM payments WHERE id = ?");
    $stmt->bind_param("i", $data['id']);
    
    if ($stmt->execute()) {
        sendResponse(true, 'Payment deleted successfully');
    } else {
        sendResponse(false, 'Failed to delete payment');
    }
}

$conn->close();
?>
