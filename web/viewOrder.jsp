<%-- 
    Document   : viewOrder.jsp
    Created on : Oct 15, 2020, 6:11:43 PM
    Author     : Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Order Page</title>
        <link href="css/table.css" rel="stylesheet" type="text/css">
    </head>

    <c:if test="${empty sessionScope.USER}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <c:if test="${sessionScope.USER.role eq 2}">
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
                <c:if test="${empty requestScope.ORDER}" var="checkOrder">
                    <font color="red">
                    No orders found. Order something.
                    </font>
                </c:if>

                <c:if test="${!checkOrder}">
                    <table>
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Order Date</th>
                                <th>View Detail</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${requestScope.ORDER}">
                                <tr>
                            <form action="DetailOrder" method="POST">
                                <td>${order.id}</td>
                                <td>${order.date}</td>
                                <td><input type="submit" value="View Detail"/>
                                    <input type="hidden" name="txtOrderId" value="${order.id}"/>
                            </form>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </section>
        </main>
    </body>
</html>
