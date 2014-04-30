<?php
    
include_once "PorterStemmer.php"; 
if(!isset($_GET["searchbar"]))
    die("Nothing found");   

$con = mysqli_connect("localhost","imgurir","imgurir","imgurir");
if(mysqli_connect_errno())
    echo "Failed to connect to MySql: ".mysqli_connect_error();

    $word = mysqli_real_escape_string($con,$_GET["searchbar"]);

$stemmer = new PorterStemmer();
$word = $stemmer->stem($word);
$query = sprintf("SELECT link, gallerylink FROM wordsearch WHERE word='%s'", $word);
$result = mysqli_query($con,$query);


while($row=mysqli_fetch_array($result))
{
    echo '<a href="'.$row["gallerylink"].'"><img src="'.$row["link"].'"/></a>';
}
mysqli_close($con);
?>