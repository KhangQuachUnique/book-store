<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/payment.css">

<div class="payment-container">
    <h2>Payment</h2>

    <c:if test="${not empty paymentMessage}">
        <div class="payment-status ${paymentSuccess ? 'success' : 'error'}">${paymentMessage}</div>
    </c:if>

    <c:if test="${empty cart}">
        <div class="payment-content">
            <p class="empty-payment">Your shopping cart is empty.</p>
        </div>
    </c:if>

    <c:if test="${not empty cart}">
        <form action="${pageContext.request.contextPath}/user/checkout/process" method="POST" class="pay-form" id="paymentForm">
            <div class="payment-content">

                <!-- 🛒 CART TABLE -->
                <div class="payment-items">
                    <table class="payment-table">
                        <thead>
                        <tr>
                            <th>Book</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Total</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${cart}">
                            <tr>
                                <td>
                                    <div class="book-col">
                                        <img src="${item.book.thumbnailUrl}" alt="${item.book.title}">
                                        <span>${item.book.title}</span>
                                    </div>
                                </td>
                                <td>${item.quantity}</td>
                                <td><fmt:formatNumber value="${item.effectiveUnitPrice}" type="number" maxFractionDigits="0"/> ₫</td>
                                <td><fmt:formatNumber value="${item.subtotal}" type="number" maxFractionDigits="0"/> ₫</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- 💳 ORDER SUMMARY -->
                <div class="pay-summary">
                    <div class="summary-section">
                        <h3>Order Summary</h3>

                        <c:set var="shippingCost" value="${not empty shippingFee ? shippingFee : 30000}"/>
                        <c:set var="subtotal" value="${cartTotal}"/>
                        <c:set var="total" value="${subtotal + shippingCost}"/>

                        <div class="summary-row">
                            <span>Subtotal</span>
                            <span><fmt:formatNumber value="${subtotal}" type="number" maxFractionDigits="0"/> ₫</span>
                        </div>

                        <div class="summary-row">
                            <span>Shipping</span>
                            <span><fmt:formatNumber value="${shippingCost}" type="number" maxFractionDigits="0"/> ₫</span>
                        </div>

                        <!-- 🧾 Promotion -->
                        <div class="summary-section">
                            <h3>Promotion</h3>
                            <div class="promo-container">
                                <input type="text" name="promoCode" id="promoCodeInput"
                                       placeholder="Enter promotion code"
                                       value="${appliedCode != null ? appliedCode : ''}" />
                                <button type="button" class="apply-btn">Apply</button>
                            </div>

                            <p id="promoMessage" style="font-size:0.9rem;margin:5px 0 0 0;"></p>
                            <div id="discountRow"></div>
                        </div>

                        <!-- 💰 Total -->
                        <div class="summary-row total" id="totalRow">
                            <span>Total</span>
                            <span id="totalValue"><fmt:formatNumber value="${total}" type="number" maxFractionDigits="0"/> ₫</span>
                        </div>
                    </div>

                    <!-- 📦 Shipping Address -->
                    <div class="payment-section">
                        <h3>Shipping Address</h3>
                        <c:if test="${not empty addresses}">
                            <select name="addressId" class="address-select">
                                <c:forEach var="address" items="${addresses}">
                                    <option value="${address.id}">${address.address}</option>
                                </c:forEach>
                            </select>
                        </c:if>
                        <c:if test="${empty addresses}">
                            <p class="no-address">Please add a shipping address</p>
                            <a href="${pageContext.request.contextPath}/user/address/new" class="add-address-btn">Add Address</a>
                        </c:if>
                    </div>

                    <!-- 💳 Payment Method -->
                    <div class="payment-section">
                        <h3>Payment Method</h3>
                        <div class="payment-methods">
                            <label class="payment-option"><input type="radio" name="paymentMethod" value="cod" checked>Cash on Delivery (COD)</label>
                            <label class="payment-option"><input type="radio" name="paymentMethod" value="momo">MoMo E-Wallet</label>
                        </div>
                    </div>

                    <!-- 📝 Notes -->
                    <div class="payment-section">
                        <h3>Order Notes</h3>
                        <textarea name="notes" rows="3" placeholder="Any special instructions?" class="order-notes"></textarea>
                    </div>

                    <div class="pay-actions" style="margin-top: 24px;">
                        <button type="submit" class="pay-btn" style="width:100%;font-size:1.1rem;letter-spacing:0.5px;">Place Order</button>
                    </div>
                </div>
            </div>
        </form>
    </c:if>
</div>

<script type="module" src="${pageContext.request.contextPath}/assets/js/payment.js"></script>