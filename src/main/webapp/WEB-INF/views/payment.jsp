<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/payment.css">

<div class="payment-container">
    <h2>Thanh Toán</h2>

    <!-- Show server-side error if present -->
    <c:if test="${not empty error}">
        <div class="payment-status error">${error}</div>
    </c:if>

    <c:if test="${not empty paymentMessage}">
        <div class="payment-status ${paymentSuccess ? 'success' : 'error'}">${paymentMessage}</div>
    </c:if>

    <c:if test="${empty cart}">
        <div class="payment-content">
            <p class="empty-payment">Giỏ hàng của bạn đang trống.</p>
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
                            <th>Sách</th>
                            <th>Số Lượng</th>
                            <th>Giá</th>
                            <th>Tổng</th>
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
                        <h3>Tóm Tắt Đơn Hàng</h3>

                        <c:set var="shippingCost" value="${not empty shippingFee ? shippingFee : 30000}"/>
                        <c:set var="subtotal" value="${cartTotal}"/>
                        <c:set var="total" value="${subtotal + shippingCost}"/>

                        <div class="summary-row">
                            <span>Tạm tính</span>
                            <span><fmt:formatNumber value="${subtotal}" type="number" maxFractionDigits="0"/> ₫</span>
                        </div>

                        <div class="summary-row">
                            <span>Phí vận chuyển</span>
                            <span><fmt:formatNumber value="${shippingCost}" type="number" maxFractionDigits="0"/> ₫</span>
                        </div>

                        <!-- 🧾 Promotion -->
                        <div class="summary-section">
                            <h3>Khuyến Mãi</h3>
                            <div class="promo-wrapper">
                                <label class="promo-label" for="promoCodeInput">Nhập mã khuyến mãi:</label>
                                <div class="promo-flex">
                                    <input type="text" name="promoCode" id="promoCodeInput"
                                           placeholder="Ví dụ: BOOK20"
                                           value="${appliedCode != null ? appliedCode : ''}" />
                                    <button type="button" class="apply-btn">Áp Dụng</button>
                                </div>
                                <p id="promoMessage" class="promo-message"></p>
                                <div id="discountRow"></div>
                            </div>
                        </div>

                        <!-- 💰 Total -->
                        <div class="summary-row total" id="totalRow">
                            <span>Tổng cộng</span>
                            <span id="totalValue"><fmt:formatNumber value="${total}" type="number" maxFractionDigits="0"/> ₫</span>
                        </div>
                    </div>

                    <!-- 📦 Shipping Address -->
                    <div class="payment-section">
                        <h3>Địa Chỉ Giao Hàng</h3>
                        <c:if test="${not empty addresses}">
                            <select name="addressId" class="address-select" required>
                                <c:forEach var="address" items="${addresses}">
                                    <option value="${address.id}">${address.address}</option>
                                </c:forEach>
                            </select>
                        </c:if>
                        <c:if test="${empty addresses}">
                            <div class="payment-status error">Bạn chưa có địa chỉ giao hàng. Vui lòng thêm địa chỉ trong tài khoản trước khi thanh toán.</div>
                        </c:if>
                    </div>

                    <!-- 💳 Payment Method -->
                    <div class="payment-section">
                        <h3>Phương Thức Thanh Toán</h3>
                        <div class="payment-methods">
                            <label class="payment-option"><input type="radio" name="paymentMethod" value="cod" checked>Thanh toán khi nhận hàng (COD)</label>
                            <label class="payment-option"><input type="radio" name="paymentMethod" value="momo">Ví điện tử MoMo</label>
                        </div>
                    </div>

                    <!-- 📝 Notes -->
                    <div class="payment-section">
                        <h3>Ghi Chú Đơn Hàng</h3>
                        <textarea name="notes" rows="3" placeholder="Có yêu cầu đặc biệt nào không?" class="order-notes"></textarea>
                    </div>

                    <div class="pay-actions" style="margin-top: 24px;">
                        <!-- Only show Place Order when user has at least one address -->
                        <c:if test="${not empty addresses}">
                            <button type="submit" class="pay-btn" style="width:100%;font-size:1.1rem;letter-spacing:0.5px;">Đặt Hàng</button>
                        </c:if>
                        <!-- No button when there is no address; only the error label above -->
                    </div>
                </div>
            </div>
        </form>
    </c:if>
</div>

<script type="module" src="${pageContext.request.contextPath}/assets/js/payment.js"></script>
<script>
    // Extra client-side guard: block submission if no address select exists
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.getElementById('paymentForm');
        if (!form) return;
        form.addEventListener('submit', function(e) {
            const addr = document.querySelector('select[name="addressId"]');
            if (!addr) {
                e.preventDefault();
                alert('Vui lòng thêm địa chỉ giao hàng trước khi thanh toán.');
            }
        });
    });
</script>
