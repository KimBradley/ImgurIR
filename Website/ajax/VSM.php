<?php
    include "PorterStemmer.php";

if(!isset($_GET["searchbar"]) || $_GET["searchbar"]=="")
    die("No query entered");
$con = mysqli_connect("localhost","imgurir","imgurir","imgurir");
if(mysqli_connect_errno())
    echo "Failed to connect to MySql: ".mysqli_connect_error();
 

    $userQuery = mysqli_real_escape_string($con,$_GET["searchbar"]);

    $words = explode(' ',$userQuery);

    $stemmer = new PorterStemmer();
   

    foreach($words as $word)
        $word = $stemmer->stem($word);

    $query = "SELECT * FROM wordsearch WHERE link In (SELECT link FROM wordsearch WHERE ";
    foreach($words as $word)
    {
        $query.="word= '$word' OR ";
    }

    $query = substr($query, 0, -3);


    $query.=") ORDER BY gallerylink";
    $result = mysqli_query($con,$query);

/*
while($row=mysqli_fetch_array($result))
{
    echo '<a href="'.$row["gallerylink"].'"><img src="'.$row["link"].'"/></a>';
}
*/


$file = fopen("output.txt","w") or die("Unable to open file?");


while($row=mysqli_fetch_array($result))
{
    if($currLink!= $row["gallerylink"])
    {
        $currLink = $row["gallerylink"];
        fwrite($file,".I ".$currLink."\r\n");
    }

    fwrite($file,$row["word"]." ".$row["frequency"]." ".$row["normalizedFrequency"]."\r\n");

    
}

fclose($file);
echo "done writing";

//find frequency of query words and normalize
$frequencyArray = array();

foreach($words as $word)
{
    if(!isset($frequencyArray[$word]))
    {
        $frequencyArray[$word]=0;
    }
    $frequencyArray[$word]++;
}

$normalizedArray = $frequencyArray;
$max = max($normalizedArray);

foreach($normalizedArray as $key=>$value)
{
    $normalizedArray[$key]=$value/$max;
   echo  $word." ".$frequencyArray[$word]." ".$normalizedArray[$word]."<br/>";
}
$fileQueryDoc = fopen("queryDoc.txt","w") or die("Unable to open query doc?");
fwrite($fileQueryDoc,".I query\r\n");
foreach($normalizedArray  as $word=>$val)
{
    fwrite($fileQueryDoc,$word." ".$frequencyArray[$word]." ".$normalizedArray[$word]."\r\n");

}

fclose($fileQueryDoc);

echo exec("java HelloWorld");

?>
