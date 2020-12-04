<%-- 
    Document   : viewUpdate
    Created on : Oct 16, 2020, 9:55:04 PM
    Author     : Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update History</title>
        <link href="css/table.css" rel="stylesheet" type="text/css">
    </head>
    
    <c:if test="${empty sessionScope.USER}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <c:if test="${sessionScope.USER.role eq 1}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <c:if test="${empty requestScope.LOG}">
        <c:redirect url="login.jsp"/>
    </c:if>
    
    <body class="bg">
        <div class="navbar">
            <a href="Logout">Logout</a>
        </div>

        <header>
            <h1>YELLOW MOON CAKE SHOP</h1>

            <nav>
                <a href="SearchCake" style="text-decoration: none">Home</a>
                <a href="LoadCategory" style="text-decoration: none">Add new Cake</a>
                <a href="ViewUpdate" class ="active" style="text-decoration: none">View Update History</a>
            </nav>
        </header>

        <main>
            <aside class="left">
                <h2>Welcome, ${USER.name}.</h2>
            </aside>

            <section class="right">
                <c:if test="${not empty requestScope.LOG}" var="checkLog">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Update User</th>
                                <th>Updated Cake</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="log" items="${requestScope.LOG}">
                                <tr>
                                    <td>${log.userName}</td>
                                    <td>${log.cakeName}</td>
                                    <td>${log.date}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${!checkLog}">
                    No logs found.
                </c:if>
            </section>
    </body>
</html>
