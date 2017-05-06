<?php
$conn = mysqli_connect("localhost", "root", 111111);
mysqli_select_db($conn, "database");
$result1 = mysqli_query($conn, "SELECT * FROM S_detection");
$result2 = mysqli_query($conn, "SELECT * FROM L_indoor");
?>
<?php
if (SENSOR_DETECTED) //센서가 감지된 경우
	$a='SELECT * FROM S_detection WHERE id='.$_GET['time']; 
	$b='SELECT * FROM L_indoor WHERE id='.$_GET['time'];
	$result1 = mysqli_query($conn, $a);
    $row1 = mysqli_fetch_assoc($result1);
    $result2 = mysqli_query($conn, $b);
    $row2 = mysqli_fetch_assoc($result2);
    //S_detection(센서 감지 테이블)과 L_indoor(환자 위치)테이블에서 시간 비교
    if($row1[S_district]==$row2[L_district]) //같은 시간이 찍힌 튜플에서 위치 비교
    	if($row1[S_detected]=="S_door") //센서 위치가 문일 경우
    		echo "Protect patient from leaving the house. district[3] door"
    	else //센서 위치가 가스일 경우
    		echo "Protect patient from using gas. district[4] kitchen"
 ?>
