<?php
$url = $_GET['url'];
$context = stream_context_create([
    "http" => ["follow_location" => true]
]);

header("Content-Type: image/jpeg");
readfile($url, false, $context);
?>
