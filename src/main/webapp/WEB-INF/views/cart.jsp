<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/cart.css">

<div class="cart-container">
    <h2>Your Shopping Cart</h2>

    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>

    <c:if test="${empty cart}">
        <p class="empty-cart">Your shopping cart is empty.</p>
    </c:if>

    <c:if test="${not empty cart}">
        <div class="cart-content">
            <div class="cart-items">
                <table class="cart-table">
                    <thead>
                    <tr>
                        <th>Book</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${cart}">
                        <tr>
                            <td>
                                <div style="display: flex; align-items: center; gap: 10px;">
                                    <img src="${item.book.thumbnailUrl}" alt="${item.book.title}" style="width: 40px; height: 40px; object-fit: cover; border-radius: 4px;">
                                    <span>${item.book.title}</span>
                                </div>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/user/cart" method="post" style="display: flex; align-items: center; gap: 4px;">
                                    <input type="hidden" name="action" value="update"/>
                                    <input type="hidden" name="cartId" value="${item.id}"/>
                                    <input type="number" name="quantity" value="${item.quantity}" min="1" class="cart-quantity-input"/>
                                </form>
</div>
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
                            </td>
                            <td><span style="white-space: nowrap;"><fmt:formatNumber value="${item.effectiveUnitPrice}" type="number" maxFractionDigits="0"/> ₫</span></td>
                            <td><span style="white-space: nowrap;"><fmt:formatNumber value="${item.subtotal}" type="number" maxFractionDigits="0"/> ₫</span></td>
                            <td class="cart-actions">
                                <form action="${pageContext.request.contextPath}/user/cart" method="post">
                                    <input type="hidden" name="action" value="remove"/>
                                    <input type="hidden" name="cartId" value="${item.id}"/>
                                    <button type="submit">Remove</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="checkout-summary">
                <div class="summary-section">
                    <h3>Order Summary</h3>
                    <c:set var="shippingCost" value="${not empty shippingFee ? shippingFee : 30000}"/>
                    <c:set var="subtotal" value="${cartTotal}"/>
                    <c:set var="total" value="${subtotal + shippingCost}"/>
                    
                    <div class="summary-row">
                        <span>Subtotal</span>
                        <span style="white-space: nowrap;"><fmt:formatNumber value="${subtotal}" type="number" maxFractionDigits="0"/> ₫</span>
                    </div>
                    <div class="summary-row">
                        <span>Shipping</span>
                        <span style="white-space: nowrap;"><fmt:formatNumber value="${shippingCost}" type="number" maxFractionDigits="0"/> ₫</span>
                    </div>
                    <div class="summary-row total">
                        <span>Total</span>
                        <span style="white-space: nowrap;"><fmt:formatNumber value="${total}" type="number" maxFractionDigits="0"/> ₫</span>
                    </div>
                </div>

                <div class="shipping-section">
                    <h3>Shipping Address</h3>
                    <c:if test="${not empty addresses}">
                        <select name="addressId" class="address-select">
                            <c:forEach var="address" items="${addresses}">
                                <option value="${address.id}">
                                    ${address.streetAddress}, ${address.city}
                                </option>
                            </c:forEach>
                        </select>
                    </c:if>
                    <c:if test="${empty addresses}">
                        <p class="no-address">Please add a shipping address</p>
                        <a href="address/new" class="add-address-btn">Add Address</a>
                    </c:if>
                </div>

                <div class="cart-actions" style="margin-top: 24px;">
                    <a href="${pageContext.request.contextPath}/user/payment" class="checkout-btn" style="width:100%;font-size:1.1rem;letter-spacing:0.5px;">Proceed to Payment</a>
                </div>
            </div>
        </div>
    </c:if>
</div>
