<%-- 
    Document   : home.jsp
    Created on : Oct 9, 2020, 10:54:48 AM
    Author     : Long
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Yellow Moon - Home</title>
        <link href="css/home.css" rel="stylesheet" type="text/css">
    </head>
    <body class="bg">
        <div class="navbar">
            <c:if test="${not empty sessionScope.USER}" var="checkLogin">            
                <a href="Logout">Logout</a>
            </c:if>

            <c:if test="${!checkLogin}">
                <a href="login.jsp">Login</a>
            </c:if>

            <search>
                <form action="SearchCake" method="POST">
                    Name:
                    <input type="text" name="txtNameSearch" value="${param.txtNameSearch}"/>

                    Range of Money:
                    <input type="number" name="txtMoneyMin" value="${param.txtMoneyMin}" min="0" max="1000000000"/> -
                    <input type="number" name="txtMoneyMax" value="${param.txtMoneyMax}" min="0" max="1000000000"/>

                    Category:
                    <select name="txtCategorySearch">
                        <option value=""></option>
                        <c:forEach items="${requestScope.CATEGORY}" var="category">
                            <option value="${category.name}">${category.name}</option>
                        </c:forEach>
                    </select>

                    <input type="submit" value="Search"/>
                </form>
            </search>
        </div>

        <header>
            <h1>YELLOW MOON CAKE SHOP</h1>

            <nav>
                <a href="SearchCake" class ="active" style="text-decoration: none">Home</a>

                <c:if test="${not empty sessionScope.USER}" var="checkLogin">
                    <c:if test="${sessionScope.USER.role eq 2}" var="checkAdmin">

                        <a href="LoadCategory" style="text-decoration: none">Add new Cake</a>

                        <a href="ViewUpdate" style="text-decoration: none">View Update History</a>

                    </c:if>

                    <c:if test="${!checkAdmin}">
                        <a href="SearchOrder" style="text-decoration: none">View Order History</a>
                    </c:if>
                </c:if>
            </nav>
        </header>

        <main>
            <aside class="left">

                <c:if test="${checkLogin}">
                    <h2>Welcome, ${USER.name}.</h2>
                </c:if>

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

                <font color="red">
                ${requestScope.NOTICE}
                </font>

                <c:if test="${not empty requestScope.INFO}" var="checkList">
                    <table>
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Image</th>
                                <th>Description</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Create Date</th>
                                <th>Expiration Date</th>
                                <th>Quantity</th>
                                    <c:if test="${!checkAdmin}">
                                    <th>Add to Cart</th>
                                    </c:if>
                                    <c:if test="${checkAdmin}">
                                    <th>Status</th>
                                    <th>Update</th>
                                    </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cake" items="${requestScope.INFO}">
                                <tr>
                                    <td>${cake.name}</td>
                                    <td><img src="images/${cake.image}" height="90" width="100"/></td>
                                    <td>${cake.description}</td>
                                    <td>${cake.categoryName}</td>
                                    <td>${cake.price}$</td>
                                    <td>${cake.createDate}</td>
                                    <td>${cake.expirationDate}</td>
                                    <td>${cake.quantity}</td>
                                    <c:if test="${!checkAdmin}">
                                        <td>
                                            <form action="AddCart" method="POST">
                                                <input type="submit" value="Add to Cart"/>
                                                <input type="hidden" name="txtPage" value="${requestScope.PAGE_CURRENT}"/>
                                                <input type="hidden" name="txtId" value="${cake.id}"/>
                                                <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}"/>
                                                <input type="hidden" name="txtMoneyMin" value="${param.txtMoneyMin}"/>
                                                <input type="hidden" name="txtMoneyMax" value="${param.txtMoneyMax}"/>
                                                <input type="hidden" name="txtCategorySearch" value="${param.txtCategorySearch}"/>
                                            </form>
                                        </td>
                                    </c:if>
                                    <c:if test="${checkAdmin}">
                                        <c:if test="${cake.status eq 1}" var="checkStatus">
                                            <td>Active</td>
                                        </c:if>

                                        <c:if test="${!checkStatus}">
                                            <td>Deactived</td>
                                        </c:if>

                                        <td>
                                            <form action="DetailCake" method="POST">
                                                <input type="hidden" name="txtId" value="${cake.id}"/>
                                                <input type="submit" value="Update"/>
                                            </form>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="move">
                        <h3>Page ${requestScope.PAGE_CURRENT} / ${requestScope.PAGE_MAX}</h3>

                        <form action="SearchCake" method="POST">
                            <input type="hidden" name="txtPage" value="${requestScope.PAGE_CURRENT}"/>
                            <input type="hidden" name="movePage" value="first"/>
                            <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}"/>
                            <input type="hidden" name="txtMoneyMin" value="${param.txtMoneyMin}"/>
                            <input type="hidden" name="txtMoneyMax" value="${param.txtMoneyMax}"/>
                            <input type="hidden" name="txtCategorySearch" value="${param.txtCategorySearch}"/>
                            <input type="submit" value="<< First"/>
                        </form>

                        <form action="SearchCake" method="POST">
                            <input type="hidden" name="txtPage" value="${requestScope.PAGE_CURRENT}"/>
                            <input type="hidden" name="movePage" value="prev"/>
                            <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}"/>
                            <input type="hidden" name="txtMoneyMin" value="${param.txtMoneyMin}"/>
                            <input type="hidden" name="txtMoneyMax" value="${param.txtMoneyMax}"/>
                            <input type="hidden" name="txtCategorySearch" value="${param.txtCategorySearch}"/>
                            <input type="submit" value="< Previous"/>
                        </form>

                        <form action="SearchCake" method="POST">
                            <input type="hidden" name="txtPage" value="${requestScope.PAGE_CURRENT}"/>
                            <input type="hidden" name="movePage" value="next"/>
                            <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}"/>
                            <input type="hidden" name="txtMoneyMin" value="${param.txtMoneyMin}"/>
                            <input type="hidden" name="txtMoneyMax" value="${param.txtMoneyMax}"/>
                            <input type="hidden" name="txtCategorySearch" value="${param.txtCategorySearch}"/>
                            <input type="submit" value="Next >"/>
                        </form>

                        <form action="SearchCake" method="POST">
                            <input type="hidden" name="txtPage" value="${requestScope.PAGE_CURRENT}"/>
                            <input type="hidden" name="movePage" value="last"/>
                            <input type="hidden" name="txtNameSearch" value="${param.txtNameSearch}"/>
                            <input type="hidden" name="txtMoneyMin" value="${param.txtMoneyMin}"/>
                            <input type="hidden" name="txtMoneyMax" value="${param.txtMoneyMax}"/>
                            <input type="hidden" name="txtCategorySearch" value="${param.txtCategorySearch}"/>
                            <input type="submit" value="Last >>"/>
                        </form>
                    </div>
                </c:if>

                <c:if test="${!checkList}">
                    <font color="red">
                    No Cakes found.
                    </font>
                </c:if>
            </section>

        </main>
    </body>
</html>
