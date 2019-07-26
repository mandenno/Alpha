<?php
    include('db_conn.php');
    session_start();

    if(isset($_POST['submit'])){
      $username = $_POST['username'];
      $pass = $_POST['password'];
    $sql = mysqli_query($conn,"SELECT * FROM user WHERE username='$username' AND password = '$pass' ");
    if(mysqli_num_rows($sql)>0){
    
    $_SESSION['username'] = $username;
    
    ?>
      <script>
    var x=window.onbeforeunload;
    // logic to make the confirm and alert boxes
    if (confirm("Login successful") == true) {
        window.location.href = "hospital.php";
    }
    else{
      window.location.href = "hospital.php";
    }</script><?php
      
    }
    else{
    
    ?>
      <script>
    var x=window.onbeforeunload;
    // logic to make the confirm and alert boxes
    if (confirm("Incorrect login details. please try again") == true) {
        window.location.href = "login.php";
    }
    else{
      window.location.href = "login.php";
    }</script><?php
      
    }
    
    }
?>

<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Alpha | Login</title>

  <!-- Custom fonts for this template-->
  <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

  <!-- Custom styles for this template-->
  <link href="css/sb-admin-2.min.css" rel="stylesheet">
  <link rel="stylesheet" href="css/ibm.css">

</head>
<style type="text/css">
#accordionSidebar{
    background-image: linear-gradient(to bottom right, #800000, #990000, #f00, #f00, #ff1a1a);
}
.btn-secondary{
  background-image: linear-gradient(to bottom right, #800000, #990000, #f00, #f00);
}
</style>

<body class="bg-gradient-primary" id="b_login">


  <div class="container">

    <!-- Outer Row -->
    <div class="row justify-content-center">

      <div class="col-xl-10 col-lg-12 col-md-9">
          <br><br><br>
        <div class="card o-hidden border-0 shadow-lg my-5">
         
          <div class="card-body p-0">
            <!-- Nested Row within Card Body -->
            <div class="row">
              <div class="col-lg-6 d-none d-lg-block"><img src="img/logos.png" alt="" id="logos" style="max-width: 450px;"></div>
              <div class="col-lg-6">
                <div class="p-5">
                  <div class="text-center">
                    <h1 class="h4 text-gray-900 mb-4">Sign In</h1>
                  </div>
                  <form class="user" action="login.php" method="POST" >
                    <div class="form-group">
                      <input type="text" name="username" class="form-control form-control-user" placeholder="Enter your username...">
                    </div>
                    <div class="form-group">
                      <input type="password" name="password" class="form-control form-control-user" id="exampleInputPassword" placeholder="Password">
                    </div>
                      <div>
                          <input type="submit" name="submit" class="btn btn-primary btn-user btn-block"  id="login_button" value="Login">
                      </div>
                    <hr>
                    </form>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>

    </div>

  </div>

  <!-- Bootstrap core JavaScript-->
  <script src="vendor/jquery/jquery.min.js"></script>
  <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  <!-- Core plugin JavaScript-->
  <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

  <!-- Custom scripts for all pages-->
  <script src="js/sb-admin-2.min.js"></script>

</body>

</html>
