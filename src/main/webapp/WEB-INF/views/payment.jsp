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
        <form action="${pageContext.request.contextPath}/user/payment/process" method="post" class="pay-form" id="paymentForm">
            <div class="payment-content">
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
                                <td>
                                    <span>${item.quantity}</span>
                                </td>
                                <td><span style="white-space: nowrap;"><fmt:formatNumber value="${item.effectiveUnitPrice}" type="number" maxFractionDigits="0"/> ₫</span></td>
                                <td><span style="white-space: nowrap;"><fmt:formatNumber value="${item.subtotal}" type="number" maxFractionDigits="0"/> ₫</span></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="pay-summary">
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

                    <div class="payment-section">
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
                            <a href="${pageContext.request.contextPath}/user/address/new" class="add-address-btn">Add Address</a>
                        </c:if>
                    </div>

                    <div class="payment-section">
                        <h3>Payment Method</h3>
                        <div class="payment-methods">
                            <label class="payment-option">
                                <input type="radio" name="paymentMethod" value="cod" checked>
                                <span>Cash on Delivery (COD)</span>
                            </label>
                            <label class="payment-option">
                                <input type="radio" name="paymentMethod" value="bank">
                                <span>Bank Transfer</span>
                            </label>
                            <label class="payment-option">
                                <input type="radio" name="paymentMethod" value="card">
                                <span>Credit/Debit Card</span>
                            </label>
                            <label class="payment-option">
                                <input type="radio" name="paymentMethod" value="momo">
                                <span>MoMo E-Wallet</span>
                            </label>
                            <label class="payment-option">
                                <input type="radio" name="paymentMethod" value="zalo">
                                <span>ZaloPay</span>
                            </label>
                        </div>
                    </div>
                    <div class="payment-section">
                        <h3>Order Notes</h3>
                        <textarea name="notes" rows="3" placeholder="Any special instructions?" class="order-notes"></textarea>
                    </div>
                    <div class="pay-actions" style="margin-top: 24px;">
                        <button type="submit" class="pay-btn" style="width:100%;font-size:1.1rem;letter-spacing:0.5px;">Pay Now</button>
                    </div>
                </div>
            </div>
        </form>
    </c:if>
</div>