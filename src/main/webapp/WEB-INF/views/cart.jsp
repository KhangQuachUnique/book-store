<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/cart.css">

            <div class="cart-container">
                <h2>Giỏ hàng của bạn</h2>

                <c:if test="${empty cart}">
                    <p class="empty-cart">Giỏ hàng của bạn đang trống.</p>
                </c:if>

                <c:if test="${not empty cart}">
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Sách</th>
                                <th>Số lượng</th>
                                <th>Giá</th>
                                <th>Tổng</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cart}">
                                <tr>
                                    <td>${item.title}</td>
                                    <td>${item.quantity}</td>
                                    <td>
                                        <fmt:formatNumber value="${item.price}" type="number" /> VND
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${item.price * item.quantity}" type="number" /> VND
                                    </td>
                                    <td class="cart-actions">
                                        <form action="cart" method="post">
                                            <input type="hidden" name="action" value="remove" />
                                            <input type="hidden" name="cartId" value="${item.id}" />
                                            <button type="submit">Xóa</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="checkout-container">
                        <form action="order" method="post">
                            <button type="submit" class="checkout-btn">Thanh toán (COD)</button>
                        </form>
                    </div>
                </c:if>
            </div>
