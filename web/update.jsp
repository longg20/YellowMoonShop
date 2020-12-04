<%-- 
    Document   : update
    Created on : Oct 11, 2020, 10:06:35 PM
    Author     : Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Cake</title>
        <link href="css/crud.css" rel="stylesheet" type="text/css">
    </head>

    <c:if test="${empty sessionScope.USER}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <c:if test="${sessionScope.USER.role eq 1}">
        <c:redirect url="login.jsp"/>
    </c:if>
    
    <c:if test="${empty requestScope.DETAIL}">
        <c:redirect url="SearchCake"/>
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
                <a href="LoadCategory" style="text-decoration: none">Add new Cake</a>
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
                <h2 style="text-align: center;">Update Cake</h2>
                <form action="Update" method="POST" enctype="multipart/form-data" onsubmit="return checkDates()">
                    ID:<br/>
                    <input type="text" name="txtId" required value="${requestScope.DETAIL.id}" readonly="true"/><br/>
                    <br/>
                    Name:<br/>
                    <input type="text" name="txtName" required value="${requestScope.DETAIL.name}"/>
                    <br/><br/>
                    Current Image:<br/>
                    <img src="images/${requestScope.DETAIL.image}" width='200' height='180'/><br/>
                    <br/>
                    Image:<br/>
                    <input type="file" name="txtImage" required value="${requestScope.DETAIL.image}"/>
                    <br/>
                    Description:<br/>
                    <input type="text" name="txtDescription" required value="${requestScope.DETAIL.description}"/><br/>
                    <br/>
                    Category:<br/>
                    <select name="txtCategory">
                        <c:forEach items="${requestScope.CATEGORY}" var="category">
                            <c:if test="${requestScope.DETAIL.category eq category.id}" var="checkCategory">
                                <option value="${category.id}" selected="selected">${category.name}</option>
                            </c:if>
                            <c:if test="${!checkCategory}">
                                <option value="${category.id}">${category.name}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    <br/>
                    Create Date:<br/>
                    <input type="date" name="txtCreateDate" id="createDate" required value="${requestScope.DETAIL.createDate}" max="2020-01-01"/>
                    <br/>
                    Expiration Date:<br/>
                    <input type="date" name="txtExpirationDate" id="expirationDate" required value="${requestScope.DETAIL.expirationDate}" min="2020-01-01"/>
                    <br/>
                    Price:<br/>
                    <input type="range" name="txtPrice" value="${requestScope.DETAIL.price}" min="10" max="1000" step="10" oninput="this.nextElementSibling.value = this.value">
                    <output>${requestScope.DETAIL.price}</output>$
                    <br/>
                    Quantity:<br/>
                    <input type="number" name="txtQuantity" min="0" max="1000" required value="${requestScope.DETAIL.quantity}"/>
                    <br/>
                    Status:<br/>
                    <select name="txtStatus" >
                        <c:if test="${requestScope.DETAIL.status eq 1}" var="checkStatus">
                            <option value="1" selected="selected">Active</option>
                            <option value="0">De-active</option>
                        </c:if>

                        <c:if test="${!checkStatus}">
                            <option value="1">Active</option>
                            <option value="0" selected="selected">Deactive</option>
                        </c:if>
                    </select><br/>
                    <br/>
                    <input type="submit" value="Update"/>
                </form>
            </section>
    </body>
</html>
