<%-- 
    Document   : insert
    Created on : Oct 10, 2020, 6:58:41 PM
    Author     : Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Cake</title>
        <link href="css/crud.css" rel="stylesheet" type="text/css">
    </head>

    <c:if test="${empty sessionScope.USER}">
        <c:redirect url="login.jsp"/>
    </c:if>
    
    <c:if test="${sessionScope.USER.role eq 1}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <script>
        function checkDates() {
            var dateForm = document.forms['createForm'];
            var createDate = new Date(dateForm['txtCreateDate'].value);
            var expirationDate = new Date(dateForm['txtExpirationDate'].value);

            if (createDate >= expirationDate) {
                alert("Expiration Date can't >= Create Date");
                return false;
            }
        }

        function setMaxDate() {
            var today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth() + 1; //January = 0;
            var yyyy = today.getFullYear();
            if (dd < 10) {
                dd = '0' + dd
            }
            if (mm < 10) {
                mm = '0' + mm
            }

            today = yyyy + '-' + mm + '-' + dd;
            document.getElementById("createDate").setAttribute("max", today);
        }

        function setMinDate() {
            var today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth() + 1; //January = 0;
            var yyyy = today.getFullYear();
            if (dd < 10) {
                dd = '0' + dd
            }
            if (mm < 10) {
                mm = '0' + mm
            }

            today = yyyy + '-' + mm + '-' + dd;
            document.getElementById("expirationDate").setAttribute("min", today);
        }
    </script>

    <body class="bg" onload="setMaxDate(); setMinDate()">
        <div class="navbar">
            <c:if test="${not empty sessionScope.USER}" var="checkLogin">            
                <a href="Logout">Logout</a>
            </c:if>
        </div>

        <header>
            <h1>YELLOW MOON CAKE SHOP</h1>

            <nav>
                <a href="SearchCake" style="text-decoration: none">Home</a>
                <a href="LoadCategory" class ="active" style="text-decoration: none">Add new Cake</a>
                <a href="ViewUpdate" style="text-decoration: none">View Update History</a>
            </nav>
        </header>


        <main>
            <aside class="left">
                <c:if test="${checkLogin}">
                    <h2>Welcome, ${USER.name}.</h2>
                </c:if>
            </aside>

            <section class="right">
                <h2 style="text-align: center;">Add Cake</h2>
                <form action="Create" method="POST" name="createForm" onsubmit="return checkDates()" enctype="multipart/form-data">
                    Name:<br/>
                    <input type="text" name="txtName" required value="${param.txtName}"/><br/>
                    </br>
                    Image:<br/>
                    <input type="file" name="txtImage" required value="${param.txtImage}" accept="image/*"/><br/>
                    </br>
                    Description:<br/>
                    <input type="text" name="txtDescription" required value="${param.txtDescription}"/><br/>
                    </br>
                    Category:<br/>
                    <select name="txtCategory">
                        <c:forEach items="${requestScope.CATEGORY}" var="category">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select><br/>
                    </br>
                    Create Date:<br/>
                    <input type="date" name="txtCreateDate" id="createDate" required value="${param.txtCreateDate}" max="2020-01-01"/><br/>
                    </br>
                    Expiration Date:<br/>
                    <input type="date" name="txtExpirationDate" id="expirationDate" required value="${param.txtExpirationDate}" min="2020-01-01"/><br/>
                    </br>
                    Price:<br/>
                    <input type="range" name="txtPrice" value="10" min="10" max="1000" step="10" oninput="this.nextElementSibling.value = this.value">
                    <output>10</output>$
                    </br><br/>
                    Quantity:<br/>
                    <input type="number" name="txtQuantity" required value="${param.txtQuantity}"/><br/>
                    </br>
                    <input type="submit" value="Create"/>
                    <input type="hidden" name="action" value="create"/>
                </form>
            </section>
    </body>
</html>
