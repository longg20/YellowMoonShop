<%-- 
    Document   : index
    Created on : Oct 3, 2020, 3:09:10 PM
    Author     : Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <link href="css/login.css" rel="stylesheet" type="text/css">
    </head>
    <body class="bg">

        <form action="Login" method="POST">
            <div class="imgcontainer">
                <img src="css/login.png" alt="Avatar" class="avatar">
            </div>

            <div class="container">
                <label for="txtId"><b>Username</b></label>
                <input type="text" placeholder="Enter Username" name="txtId" required>

                <label for="txtPassword"><b>Password</b></label>
                <input type="password" placeholder="Enter Password" name="txtPassword" required>
                
                <font color="red">${requestScope.INVALID}</font>
                
                <button type="submit">Login</button>
            </div>
        </form>



        <form action="SearchCake" method="POST">
            <button type="submit">Back to Home</button>
        </form>
    </body>
</html>
