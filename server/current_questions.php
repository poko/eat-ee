<?php
include 'json_headers.php';
include 'db_open.php';

$query = "SELECT * FROM $QUESTION_TABLE WHERE $COL_QUESTION_ACTIVE = 1";

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