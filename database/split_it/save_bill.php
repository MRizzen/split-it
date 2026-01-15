<?php
$conn = new mysqli("localhost", "root", "", "split_itdb");

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$title = $_POST['title'];
$total = $_POST['total'];
$payer = $_POST['payer'];
$date = $_POST['date'];
$split_type = $_POST['split_type'];

$names = explode(",", $_POST['names']);
$amounts = explode(",", $_POST['amounts']); 

$sql = "INSERT INTO bills (title, total_amount, payer_name, due_date, split_type) 
        VALUES ('$title', '$total', '$payer', '$date', '$split_type')";

if ($conn->query($sql) === TRUE) {

    $bill_id = $conn->insert_id;

    for ($i = 0; $i < count($names); $i++) {
        $name = $conn->real_escape_string($names[$i]);
        $amt = $amounts[$i]; 

        $sql_member = "INSERT INTO bill_members (bill_id, person_name, individual_amount, paid_amount) 
                       VALUES ('$bill_id', '$name', '$amt', 0.00)";
        
        if (!$conn->query($sql_member)) {
            error_log("Error inserting member: " . $conn->error);
        }
    }
    echo "Success";
} else {
    echo "Error: " . $conn->error;
}

$conn->close();
?>