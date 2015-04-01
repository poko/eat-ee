<?php
include 'json_headers.php';

if (version_compare(PHP_VERSION, '5.1.0', '>=')) { date_default_timezone_set('UTC'); }

//$base_upload_dir = "/home/ecoar4/public_html/edible_ecologies/uploads/";
$base_upload_dir = "/Applications/MAMP/htdocs/ee/uploads/";

function createThumbnail($inFile, $outFile, $maxWidth, $maxHeight) {
    $pathinfo = pathinfo($inFile);

    $srcImg = null;
    switch(strtolower($pathinfo['extension'])) {
        case 'gif'  : $srcImg = imagecreatefromgif($inFile); break;
        case 'png'  : $srcImg = imagecreatefrompng($inFile); break;
        case 'jpg'  : 
        case 'jpeg' : $srcImg = imagecreatefromjpeg($inFile); break;
        default: die("can't open this type of image");
    }

    $origWidth = imagesx($srcImg);
    $origHeight = imagesy($srcImg);

    $ratioH = $maxHeight/$origHeight;
    $ratioW = $maxWidth/$origWidth;

    $ratio = min($ratioH, $ratioW);

    $newWidth = intval($ratio * $origWidth);
    $newHeight = intval($ratio * $origHeight);

    $thumbImg = imagecreatetruecolor($newWidth, $newHeight);

    imagecopyresampled($thumbImg, $srcImg, 0, 0, 0, 0, $newWidth, $newHeight, $origWidth, $origHeight);

    imagejpeg($thumbImg, $pathinfo['dirname'] . "/" . $outFile, 95);
}

$lat = $_POST["lat"];
$lng = $_POST["lng"];
$interview = $_POST["interview"];
$geo = $_POST["geocoded_area"];

include 'db_open.php';

// insert waypoint data
$query = "INSERT into $WAYPOINT_TABLE ($COL_WAYPOINT_LAT, $COL_WAYPOINT_LNG, $COL_WAYPOINT_INTERVIEW, $COL_WAYPOINT_AREA) values (?, ?, ?, ?)";
$stmt = mysqli_prepare($conn, $query);

$stmt->bind_param('ddss', $lat, $lng, $interview, $geo);
$result = $stmt->execute();

if (!$result) {
    $message  = 'Invalid query: ' . mysqli_error() . "\n";
    $message .= 'Whole query: ' . $query;
    die($message);
}

$waypoint_id = mysqli_insert_id($conn);


// create target folder
$upload_dir = $base_upload_dir . $waypoint_id;

is_dir($upload_dir) || mkdir($upload_dir, 0755);

// save the photos
$success = true;
foreach ($_FILES as $file){
	// create target folder/filename and move it there
	$uploadfile = $upload_dir . "/".$waypoint_id."_".basename($file['name']);
	if (move_uploaded_file($file['tmp_name'], $uploadfile)) {
	    //echo "File is valid, and was successfully uploaded.\n";
	} else {
		$success = false;
	    error_log("Couldn't upload file.  Maybe it's too big?");
	}
}

include 'db_close.php';

$resp = ["success"=>$success, "waypoint_id"=>$waypoint_id];
echo json_encode($resp);
?>
