<%-- 
    Document   : detailCart
    Created on : Oct 13, 2020, 5:17:41 PM
    Author     : Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cart Detail</title>
        <link href="css/table.css" rel="stylesheet" type="text/css">
    </head>

    <c:if test="${sessionScope.USER.role eq 2}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <body class="bg">

        <div class="navbar">
            <c:if test="${not empty sessionScope.USER}" var="checkLogin">
                <a href="Logout">Logout</a>
            </c:if>

            <c:if test="${!checkLogin}">
                <a href="login.jsp">Login</a>
            </c:if>
        </div>


        <header>
            <h1>YELLOW MOON CAKE SHOP</h1>

            <nav>
                <a href="SearchCake" style="text-decoration: none">Home</a>
                <c:if test="${checkLogin}">
                    <a href="SearchOrder" style="text-decoration: none">View Order History</a>
                </c:if>
            </nav>
        </header>


        <main>
            <c:if test="${empty sessionScope.CART}" var="checkCart">
                <c:redirect url="SearchCake"/>
            </c:if>

            <h3>Cart Detail</h3>

            <font color="red">
            ${requestScope.NOTICE}
            </font>

            <c:if test="${!checkCart}">
                <table>
                    <thead>
                        <tr>
                            <th>Cake's Name</th>
                            <th>Quantity in Stock</th>
                            <th>Quantity in Cart</th>
                            <th>Price</th>
                            <th>Total</th>
                            <th>Update</th>
                            <th>Delete</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cart" items="${sessionScope.CART.cart}">
                            <tr>
                        <form action="UpdateCart" method="POST">
                            <td>${cart.value.name}</td>
                            <td>${cart.value.quantity}</td>
                            <td><input type="number" name="txtCartQty" required min="0" value="${cart.value.cartQty}"</td>
                            <td>${cart.value.price}$</td>
                            <td>${cart.value.price * cart.value.cartQty}$</td>
                            <td><input type="submit" value="Update"<td/>
                            <input type="hidden" name="txtId" value="${cart.value.id}"/>
                            <input type="hidden" name="txtQuantity" value="${cart.value.quantity}"/>
                        </form>
                        <form action="DeleteCart" method="POST">
                            <td><input type="submit" value="Delete" onclick="return confirm('Are you sure you want to delete Cake?')"/></td>
                            <input type="hidden" name="txtId" value="${cart.value.id}"/>
                        </form>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="3"></td>
                        <td>Total:</td>
                        <td>${sessionScope.CART.total}$</td>
                    </tr>
                    </tbody>
                </table>

                <c:if test="${not empty sessionScope.USER}" var="checkLogin">
                    <form action="ConfirmCart" method="POST">
                        <font color="white">Payment Method:</font><select name="txtPaymentMethod">
                            <option value="Cash upon Delivery">Cash upon Delivery</option>
                        </select><br/>
                        <input type="submit" value="Confirm"/>
                    </form>
                </c:if>

                <c:if test="${!checkLogin}">
                    <form action="ConfirmCart" method="POST">
                        <font color="white">Name:</font><br/>
                        <input type="text" name="txtName" required value="${param.txtName}"/><br/>

                        <font color="white">Phone:</font><br/>
                        <input type="text" name="txtPhone"required pattern="\d*" minlength="10" maxlength="10" value="${param.txtPhone}"/><br/>

                        <font color="white">Address:</font><br/>
                        <input type="text" name="txtAddress" required value="${param.txtAddress}"/><br/>

                        <font color="white">Payment Method:</font><br/>
                        <select name="txtPaymentMethod">
                            <option value="Cash upon Delivery">Cash upon Delivery</option>
                        </select><br/><br/>
                        <input type="submit" value="Confirm"/>
                    </form>
                </c:if>

            </c:if>
        </main>
    </body>
</html>
