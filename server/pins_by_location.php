<?php
include 'json_headers.php';
include 'db_open.php';

$user_lat = $_GET["lat"];
$user_lng = $_GET["lng"];

$query = "SELECT *, ( 3959 * acos( cos( radians($user_lat) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians($user_lng) ) + sin( radians($user_lat) ) * sin( radians( lat ) ) ) ) AS distance FROM pins HAVING distance < 25 ORDER BY distance LIMIT 0 , 20";

$rs = mysqli_query($conn, $query);

echo "["; 
if($rs === false) {
  trigger_error('Wrong SQL: ' . $sql . ' Error: ' . $conn->error, E_USER_ERROR);
} else {
  $rs->data_seek(0);
  while($row = $rs->fetch_assoc()){
    echo json_encode($row);
  }
}
echo "]"; 

include 'db_close.php';
?>