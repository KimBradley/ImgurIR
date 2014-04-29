<?php
if(!isset($_GET["searchbar"]))
    die("Nothing found");
    

$con = mysqli_connect("localhost","imgurir","imgurir","imgurir");
if(mysqli_connect_errno())
    echo "Failed to connect to MySql: ".mysqli_connect_error();

$query = sprintf("SELECT link FROM wordsearch WHERE word='%s'",mysqli_real_escape_string($con,$_GET["searchbar"]));
$result = mysqli_query($con,$query);

while($row=mysqli_fetch_array($result))
{
    echo '<img src="'.$row["link"].'"/>';
}
mysqli_close($con);
?>