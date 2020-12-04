<%-- 
    Document   : viewOrderDetail
    Created on : Oct 16, 2020, 8:12:58 AM
    Author     : Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order Detail</title>
        <link href="css/table.css" rel="stylesheet" type="text/css">
    </head>

    <c:if test="${empty sessionScope.USER}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <c:if test="${sessionScope.USER.role eq 2}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <c:if test="${empty requestScope.DETAIL}">
        <c:redirect url="login.jsp"/>
    </c:if>
    
    <body class="bg">
        <div class="navbar">
            <a href="Logout">Logout</a>

            <search>
                <form action="SearchOrder" method="POST">
                    Search ID:<input type="text" name="txtOrderSearch" value="${param.txtOrderSearch}"/>
                    <input type="submit" value="Search"/>
                </form>
            </search>
        </div>

        <header>
            <h1>YELLOW MOON CAKE SHOP</h1>

            <nav>
                <a href="SearchCake" style="text-decoration: none">Home</a>
                <a href="SearchOrder" class ="active" style="text-decoration: none">View Order History</a>
            </nav>
        </header>


        <main>
            <aside class="left">
                <h2>Welcome, ${USER.name}.</h2>

                <c:if test="${!checkAdmin}">
                    <c:if test="${not empty sessionScope.CART}">

                        <h2>Your Cart:</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Items</th>
                                    <th>Price</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cart" items="${sessionScope.CART.cart}">
                                    <tr>
                                        <td>${cart.value.name}<br/>x ${cart.value.cartQty}</td>
                                        <td>${cart.value.price * cart.value.cartQty}$</td>
                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td>Total:</td>
                                    <td>${sessionScope.CART.total}$</td>
                                </tr>
                            </tbody>
                        </table>
                        <form action="detailCart.jsp" method="POST">
                            <input type="submit" value="View Cart Detail"/>
                        </form>
                    </c:if>
                </c:if>
            </aside>


            <section class="right">
                <h3>Order ${requestScope.DETAIL.id}'s Detail</h3>
                <table>
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>User ID</th>
                            <th>Total</th>
                            <th>Date</th>
                            <th>Name</th>
                            <th>Phone</th>
                            <th>Address</th>
                            <th>Payment Method</th>
                            <th>Payment Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>${requestScope.DETAIL.id}</td>
                            <td>${requestScope.DETAIL.userId}</td>
                            <td>${requestScope.DETAIL.total}$</td>
                            <td>${requestScope.DETAIL.date}</td>
                            <td>${requestScope.DETAIL.name}</td>
                            <td>${requestScope.DETAIL.phone}</td>
                            <td>${requestScope.DETAIL.address}</td>
                            <td>${requestScope.DETAIL.paymentMethod}</td>
                            <c:if test="${requestScope.DETAIL.paymentStatus eq false}" var="checkStatus">
                                <td>Not yet</td>
                            </c:if>

                            <c:if test="${!checkStatus}">
                                <td>Paid</td>
                            </c:if>
                        </tr>
                    </tbody>
                </table>
                <br/>
                <table>
                    <thead>
                        <tr>
                            <th>Cake Name</th>
                            <th>Quantity</th>
                            <th>Total Price</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cake" items="${requestScope.ORDER}">
                            <tr>
                                <td>${cake.cakeName}</td>
                                <td>${cake.quantity}</td>
                                <td>${cake.total}$</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

            </section>
    </body>
</html>
