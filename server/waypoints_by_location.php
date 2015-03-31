<?php
include 'json_headers.php';
include 'db_open.php';

$user_lat = $_GET["lat"];
$user_lng = $_GET["lng"];

$query = "SELECT *, ( 3959 * acos( cos( radians($user_lat) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians($user_lng) ) + sin( radians($user_lat) ) * sin( radians( lat ) ) ) ) AS distance FROM $WAYPOINT_TABLE HAVING distance < 25 ORDER BY distance LIMIT 0 , 20";

$rs = mysqli_query($conn, $query);

$result = "["; 
if($rs === false) {
  trigger_error('Wrong SQL: ' . $query . ' Error: ' . $conn->error, E_USER_ERROR);
} else {
  $rs->data_seek(0);
  while($row = $rs->fetch_assoc()){
    $result .= json_encode($row);
    $result .= ",";
  }
}
$result = rtrim($result, ",");
$result .=  "]"; 

echo $result;

include 'db_close.php';
?>