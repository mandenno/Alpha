<?php
  $conn = mysqli_connect('localhost', 'tbcappor_mandenno', 'mandenno@123', 'tbcappor_alpha');
  
  if(!$conn){
      exit('Database not connected'.mysqli_error($conn));
  }
  
  else{
     $select_db = mysqli_select_db($conn, 'tbcappor_alpha'); 
     
     if(!$select_db){
         exit('Database not selected'. mysqli_error($conn));
     }
  }
  
  
  
  
?>