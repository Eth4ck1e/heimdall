<?php
// Get the form data
$title = $_POST['title'];
$questions = $_POST['question'];
$responseTypes = $_POST['responseType'];

// Create or open the SQLite database
$db = new SQLite3('form_database.db');

// Create the table if it does not exist
$query = "CREATE TABLE IF NOT EXISTS " . $title . " (question_number INTEGER PRIMARY KEY, question_text TEXT, response_type TEXT)";
$db->exec($query);

// Prepare the INSERT statement
$insertStatement = $db->prepare("INSERT INTO " . $title . " (question_number, question_text, response_type) VALUES (:question_number, :question_text, :response_type)");

// Insert each question into the table
for ($i = 0; $i < count($questions); $i++) {
    $insertStatement->bindValue(':question_number', $i + 1, SQLITE3_INTEGER);
    $insertStatement->bindValue(':question_text', $questions[$i], SQLITE3_TEXT);
    $insertStatement->bindValue(':response_type', $responseTypes[$i], SQLITE3_TEXT);
    $insertStatement->execute();
}

// Close the database connection
$db->close();

// Redirect back to the form page
header("Location: index.html");
exit();
?>
