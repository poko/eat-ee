<?php
include 'json_headers.php';

if (version_compare(PHP_VERSION, '5.1.0', '>=')) { date_default_timezone_set('UTC'); }

//$base_upload_dir = "/home/ecoar4/public_html/edible_ecologies/uploads/";
$base_upload_dir = "/Applications/MAMP/htdocs/ee/uploads/";

$lat = $_POST["lat"];
$lng = $_POST["lng"];
$text = $_POST["text"];
$mission_text = $_POST["mission_text"];
$geo = $_POST["geocoded_area"];

include 'db_open.php';

// insert pin data
$query = "INSERT into $PINS_TABLE ($COL_PIN_LAT, $COL_PIN_LNG, $COL_PIN_TEXT, $COL_PIN_MISSION, $COL_PIN_AREA) values (?, ?, ?, ?, ?)";
$stmt = mysqli_prepare($conn, $query);
$stmt->bind_param('ddsss', $lat, $lng, $text, $mission_text, $geo);
$result = $stmt->execute();

if (!$result) {
    $message  = 'Invalid query: ' . mysql_error() . "\n";
    $message .= 'Whole query: ' . $query;
    die($message);
}
include 'db_close.php';

$resp = ["success"=>true];
echo json_encode($resp);

?>
