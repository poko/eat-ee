<?php
// tables
$QUESTION_TABLE = "questions";
$WAYPOINT_TABLE = "waypoints";

// columns - waypoints
$COL_WAYPOINT_LAT = "lat";
$COL_WAYPOINT_LNG = "lng";
$COL_WAYPOINT_PHOTO_FILES = "photo_files";
$COL_WAYPOINT_INTERVIEW = "interview";
$COL_WAYPOINT_AREA = "geocoded_area";

// columns - questions
$COL_QUESTION_ID = "_id";
$COL_QUESTION_TEXT = "text";
$COL_QUESTION_ACTIVE = "active";

$conn = mysqli_connect("localhost", "ecoar4_edibleeco", "ee2015***", "ecoar4_edible_eco") or die('Cannot connect to the database because: ' . mysqli_error());
?>
