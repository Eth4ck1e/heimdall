<?php
// Get the YAML content from the request body
$yamlContent = file_get_contents('php://input');

// Save the YAML content to the file
$file = 'config.yml';
$result = file_put_contents($file, $yamlContent);

// Prepare the response based on the save result
if ($result !== false) {
    http_response_code(200);
    echo 'Configuration saved successfully';
} else {
    http_response_code(500);
    echo 'Error saving configuration';
}
