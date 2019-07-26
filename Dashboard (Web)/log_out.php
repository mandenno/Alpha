<?php


 session_start();
 session_destroy();

 if(isset($_COOKIE['phone']) && $_COOKIE['password']){
     $phone_no = $_COOKIE['phone'];
     $password = $_COOKIE['password'];
     setcookie('phone', $phone_no, time() -1);
     setcookie('password', $password, time() -1);
    
    
    } 
    
    if(isset($_COOKIE['admin']) && $_COOKIE['password']){
     $user_id = $_COOKIE['admin'];
     $password = $_COOKIE['password'];
     setcookie('admin', $user_id, time() -1);
     setcookie('password', $password, time() -1);
    
    
    }
    
    if(isset($_COOKIE['username']) && $_COOKIE['password']){
     $username = $_COOKIE['username'];
     $password = $_COOKIE['password'];
     setcookie('username', $username, time() -1);
     setcookie('password', $password, time() -1);
    
    
    }



 header('location:index.php');
?>
