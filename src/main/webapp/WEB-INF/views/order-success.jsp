<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    .order-success-wrapper {
        background: linear-gradient(135deg, #f8f0f3 0%, #fff 100%);
        min-height: calc(100vh - 200px);
        padding: 40px 20px;
    }

    .order-success-container {
        max-width: 700px;
        margin: 0 auto;
        padding: 50px 40px;
        text-align: center;
        background: white;
        border-radius: 16px;
        box-shadow: 0 10px 40px rgba(128, 0, 32, 0.1);
        position: relative;
        overflow: hidden;
    }

    .order-success-container::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 5px;
        background: linear-gradient(90deg, #800020 0%, #b33939 100%);
    }

    .success-header {
        margin-bottom: 30px;
    }

    .success-icon {
        width: 100px;
        height: 100px;
        margin: 0 auto 24px;
        background: linear-gradient(135deg, #800020 0%, #a52a2a 100%);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        animation: scaleIn 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55);
        box-shadow: 0 8px 25px rgba(128, 0, 32, 0.3);
        position: relative;
    }

    .success-icon::after {
        content: '';
        position: absolute;
        width: 120px;
        height: 120px;
        border: 3px solid #800020;
        border-radius: 50%;
        opacity: 0.2;
        animation: pulse 2s ease-out infinite;
    }

    @keyframes pulse {
        0% {
            transform: scale(1);
            opacity: 0.3;
        }
        100% {
            transform: scale(1.3);
            opacity: 0;
        }
    }

    .success-icon svg {
        width: 56px;
        height: 56px;
        fill: white;
        position: relative;
        z-index: 1;
    }

    @keyframes scaleIn {
        0% {
            transform: scale(0) rotate(-180deg);
            opacity: 0;
        }
        50% {
            transform: scale(1.1) rotate(0deg);
        }
        100% {
            transform: scale(1) rotate(0deg);
            opacity: 1;
        }
    }

    .bookie-logo {
        width: 80px;
        height: 80px;
        margin: 0 auto 20px;
        animation: fadeInDown 0.8s ease-out;
    }

    @keyframes fadeInDown {
        0% {
            opacity: 0;
            transform: translateY(-20px);
        }
        100% {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .order-success-container h1 {
        color: #800020;
        font-size: 32px;
        margin-bottom: 12px;
        font-weight: 700;
        animation: fadeIn 0.8s ease-out 0.2s both;
    }

    @keyframes fadeIn {
        0% {
            opacity: 0;
        }
        100% {
            opacity: 1;
        }
    }

    .order-success-container > p {
        color: #6b7280;
        font-size: 16px;
        line-height: 1.8;
        margin-bottom: 30px;
        animation: fadeIn 0.8s ease-out 0.3s both;
    }

    .order-info {
        background: linear-gradient(135deg, #f8f0f3 0%, #fff5f7 100%);
        border-radius: 12px;
        padding: 30px;
        margin: 30px 0;
        text-align: left;
        border: 2px solid rgba(128, 0, 32, 0.1);
        animation: slideUp 0.8s ease-out 0.4s both;
    }

    @keyframes slideUp {
        0% {
            opacity: 0;
            transform: translateY(20px);
        }
        100% {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .order-info-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16px 0;
        border-bottom: 1px solid rgba(128, 0, 32, 0.1);
    }

    .order-info-row:last-child {
        border-bottom: none;
        padding-top: 20px;
        margin-top: 10px;
        border-top: 2px dashed rgba(128, 0, 32, 0.2);
    }

    .order-info-label {
        color: #6b7280;
        font-size: 15px;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .order-info-label svg {
        width: 18px;
        height: 18px;
        fill: #800020;
    }

    .order-info-value {
        color: #111827;
        font-weight: 600;
        font-size: 15px;
    }

    .order-info-row:last-child .order-info-value {
        font-size: 24px;
        color: #800020;
        font-weight: 700;
    }

    .payment-method {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        background: linear-gradient(135deg, #800020 0%, #a52a2a 100%);
        color: white;
        padding: 6px 16px;
        border-radius: 20px;
        font-size: 13px;
        font-weight: 600;
        box-shadow: 0 2px 8px rgba(128, 0, 32, 0.2);
    }

    .payment-method svg {
        width: 16px;
        height: 16px;
        fill: white;
    }

    .button-group {
        display: flex;
        gap: 16px;
        justify-content: center;
        margin-top: 40px;
        animation: fadeIn 0.8s ease-out 0.5s both;
        flex-wrap: wrap;
    }

    .btn {
        padding: 14px 36px;
        border-radius: 10px;
        font-size: 16px;
        font-weight: 600;
        text-decoration: none;
        cursor: pointer;
        transition: all 0.3s ease;
        border: none;
        display: inline-flex;
        align-items: center;
        gap: 10px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    .btn svg {
        width: 20px;
        height: 20px;
    }

    .btn-primary {
        background: linear-gradient(135deg, #800020 0%, #a52a2a 100%);
        color: white;
    }

    .btn-primary:hover {
        background: linear-gradient(135deg, #a52a2a 0%, #b33939 100%);
        transform: translateY(-3px);
        box-shadow: 0 6px 20px rgba(128, 0, 32, 0.4);
    }

    .btn-primary svg {
        fill: white;
    }

    .btn-secondary {
        background: white;
        color: #800020;
        border: 2px solid #800020;
    }

    .btn-secondary:hover {
        background: #f8f0f3;
        border-color: #a52a2a;
        transform: translateY(-3px);
        box-shadow: 0 6px 20px rgba(128, 0, 32, 0.2);
    }

    .btn-secondary svg {
        fill: #800020;
    }

    .success-message {
        background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
        border: 2px solid #10b981;
        border-radius: 10px;
        padding: 18px;
        margin-bottom: 30px;
        color: #065f46;
        font-size: 15px;
        font-weight: 500;
        animation: fadeIn 0.8s ease-out 0.3s both;
    }

    .order-divider {
        width: 60px;
        height: 4px;
        background: linear-gradient(90deg, #800020 0%, #a52a2a 100%);
        margin: 30px auto;
        border-radius: 2px;
    }

    .info-note {
        background: #fff9f0;
        border-left: 4px solid #f59e0b;
        padding: 16px;
        border-radius: 8px;
        margin-top: 30px;
        text-align: left;
    }

    .info-note p {
        color: #92400e;
        font-size: 14px;
        margin: 0;
        line-height: 1.6;
    }

    @media (max-width: 768px) {
        .order-success-container {
            padding: 40px 24px;
        }

        .button-group {
            flex-direction: column;
            gap: 12px;
        }

        .btn {
            width: 100%;
            justify-content: center;
        }

        .order-info-row {
            flex-direction: column;
            align-items: flex-start;
            gap: 8px;
        }
    }
</style>

<div class="order-success-wrapper">
    <div class="order-success-container">
        <div class="success-header">
            <img src="${pageContext.request.contextPath}/assets/images/BookieCakeLogo.svg"
                 alt="BookieCake Logo"
                 class="bookie-logo">

            <div class="success-icon">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                    <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
                </svg>
            </div>

            <h1>🎉 Đặt hàng thành công!</h1>
            <div class="order-divider"></div>
        </div>

        <p>Cảm ơn bạn đã tin tưởng và mua sắm tại <strong style="color: #800020;">BookieCake</strong>.<br>
        Đơn hàng của bạn đã được tiếp nhận và đang được xử lý.</p>

        <c:if test="${not empty success}">
            <div class="success-message">
                ✓ ${success}
            </div>
        </c:if>

        <div class="order-info">
            <c:if test="${not empty orderId}">
                <div class="order-info-row">
                    <span class="order-info-label">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                            <path fill-rule="evenodd" d="M5.625 1.5c-1.036 0-1.875.84-1.875 1.875v17.25c0 1.035.84 1.875 1.875 1.875h12.75c1.035 0 1.875-.84 1.875-1.875V12.75A3.75 3.75 0 0 0 16.5 9h-1.875a1.875 1.875 0 0 1-1.875-1.875V5.25A3.75 3.75 0 0 0 9 1.5H5.625ZM7.5 15a.75.75 0 0 1 .75-.75h7.5a.75.75 0 0 1 0 1.5h-7.5A.75.75 0 0 1 7.5 15Zm.75 2.25a.75.75 0 0 0 0 1.5H12a.75.75 0 0 0 0-1.5H8.25Z" clip-rule="evenodd" />
                            <path d="M12.971 1.816A5.23 5.23 0 0 1 14.25 5.25v1.875c0 .207.168.375.375.375H16.5a5.23 5.23 0 0 1 3.434 1.279 9.768 9.768 0 0 0-6.963-6.963Z" />
                        </svg>
                        Mã đơn hàng
                    </span>
                    <span class="order-info-value">#${orderId}</span>
                </div>
            </c:if>

            <c:if test="${not empty paymentMethod}">
                <div class="order-info-row">
                    <span class="order-info-label">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                            <path d="M4.5 3.75a3 3 0 0 0-3 3v.75h21v-.75a3 3 0 0 0-3-3h-15Z" />
                            <path fill-rule="evenodd" d="M22.5 9.75h-21v7.5a3 3 0 0 0 3 3h15a3 3 0 0 0 3-3v-7.5Zm-18 3.75a.75.75 0 0 1 .75-.75h6a.75.75 0 0 1 0 1.5h-6a.75.75 0 0 1-.75-.75Zm.75 2.25a.75.75 0 0 0 0 1.5h3a.75.75 0 0 0 0-1.5h-3Z" clip-rule="evenodd" />
                        </svg>
                        Phương thức thanh toán
                    </span>
                    <span class="payment-method">
                        <c:choose>
                            <c:when test="${paymentMethod == 'MOMO'}">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                                    <path d="M2.273 5.625A4.483 4.483 0 0 1 5.25 4.5h13.5c1.141 0 2.183.425 2.977 1.125A3 3 0 0 0 18.75 3H5.25a3 3 0 0 0-2.977 2.625ZM2.273 8.625A4.483 4.483 0 0 1 5.25 7.5h13.5c1.141 0 2.183.425 2.977 1.125A3 3 0 0 0 18.75 6H5.25a3 3 0 0 0-2.977 2.625ZM5.25 9a3 3 0 0 0-3 3v6a3 3 0 0 0 3 3h13.5a3 3 0 0 0 3-3v-6a3 3 0 0 0-3-3H15a.75.75 0 0 0-.75.75 2.25 2.25 0 0 1-4.5 0A.75.75 0 0 0 9 9H5.25Z" />
                                </svg>
                                MoMo E-Wallet
                            </c:when>
                            <c:when test="${paymentMethod == 'COD'}">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                                    <path d="M10.464 8.746c.227-.18.497-.311.786-.394v2.795a2.252 2.252 0 0 1-.786-.393c-.394-.313-.546-.681-.546-1.004 0-.323.152-.691.546-1.004ZM12.75 15.662v-2.824c.347.085.664.228.921.421.427.32.579.686.579.991 0 .305-.152.671-.579.991a2.534 2.534 0 0 1-.921.42Z" />
                                    <path fill-rule="evenodd" d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25ZM12.75 6a.75.75 0 0 0-1.5 0v.816a3.836 3.836 0 0 0-1.72.756c-.712.566-1.112 1.35-1.112 2.178 0 .829.4 1.612 1.113 2.178.502.4 1.102.647 1.719.756v2.978a2.536 2.536 0 0 1-.921-.421l-.879-.66a.75.75 0 0 0-.9 1.2l.879.66c.533.4 1.169.645 1.821.75V18a.75.75 0 0 0 1.5 0v-.81a4.124 4.124 0 0 0 1.821-.749c.745-.559 1.179-1.344 1.179-2.191 0-.847-.434-1.632-1.179-2.191a4.122 4.122 0 0 0-1.821-.75V8.354c.29.082.559.213.786.393l.415.33a.75.75 0 0 0 .933-1.175l-.415-.33a3.836 3.836 0 0 0-1.719-.755V6Z" clip-rule="evenodd" />
                                </svg>
                                Thanh toán khi nhận hàng
                            </c:when>
                            <c:when test="${paymentMethod == 'BANK_TRANSFER'}">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                                    <path d="M11.584 2.376a.75.75 0 0 1 .832 0l9 6a.75.75 0 1 1-.832 1.248L12 3.901 3.416 9.624a.75.75 0 0 1-.832-1.248l9-6Z" />
                                    <path fill-rule="evenodd" d="M20.25 10.332v9.918H21a.75.75 0 0 1 0 1.5H3a.75.75 0 0 1 0-1.5h.75v-9.918a.75.75 0 0 1 .634-.74A49.109 49.109 0 0 1 12 9c2.59 0 5.134.202 7.616.592a.75.75 0 0 1 .634.74Zm-7.5 2.418a.75.75 0 0 0-1.5 0v6.75a.75.75 0 0 0 1.5 0v-6.75Zm3-.75a.75.75 0 0 1 .75.75v6.75a.75.75 0 0 1-1.5 0v-6.75a.75.75 0 0 1 .75-.75ZM9 12.75a.75.75 0 0 0-1.5 0v6.75a.75.75 0 0 0 1.5 0v-6.75Z" clip-rule="evenodd" />
                                    <path d="M12 7.875a1.125 1.125 0 1 0 0-2.25 1.125 1.125 0 0 0 0 2.25Z" />
                                </svg>
                                Chuyển khoản ngân hàng
                            </c:when>
                            <c:otherwise>
                                ${paymentMethod}
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </c:if>

            <c:if test="${not empty orderTotal}">
                <div class="order-info-row">
                    <span class="order-info-label">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                            <path d="M10.464 8.746c.227-.18.497-.311.786-.394v2.795a2.252 2.252 0 0 1-.786-.393c-.394-.313-.546-.681-.546-1.004 0-.323.152-.691.546-1.004ZM12.75 15.662v-2.824c.347.085.664.228.921.421.427.32.579.686.579.991 0 .305-.152.671-.579.991a2.534 2.534 0 0 1-.921.42Z" />
                            <path fill-rule="evenodd" d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25ZM12.75 6a.75.75 0 0 0-1.5 0v.816a3.836 3.836 0 0 0-1.72.756c-.712.566-1.112 1.35-1.112 2.178 0 .829.4 1.612 1.113 2.178.502.4 1.102.647 1.719.756v2.978a2.536 2.536 0 0 1-.921-.421l-.879-.66a.75.75 0 0 0-.9 1.2l.879.66c.533.4 1.169.645 1.821.75V18a.75.75 0 0 0 1.5 0v-.81a4.124 4.124 0 0 0 1.821-.749c.745-.559 1.179-1.344 1.179-2.191 0-.847-.434-1.632-1.179-2.191a4.122 4.122 0 0 0-1.821-.75V8.354c.29.082.559.213.786.393l.415.33a.75.75 0 0 0 .933-1.175l-.415-.33a3.836 3.836 0 0 0-1.719-.755V6Z" clip-rule="evenodd" />
                        </svg>
                        Tổng thanh toán
                    </span>
                    <span class="order-info-value">
                        <fmt:formatNumber value="${orderTotal}" type="currency" currencyCode="VND" pattern="#,##0 ₫"/>
                    </span>
                </div>
            </c:if>
        </div>

        <div class="info-note">
            <p>
                <strong>📧 Thông báo:</strong> Chúng tôi sẽ gửi email xác nhận đơn hàng đến địa chỉ email của bạn.
                Vui lòng kiểm tra hộp thư để cập nhật thông tin đơn hàng.
            </p>
        </div>

        <div class="button-group">
            <a href="${pageContext.request.contextPath}/user/order-tracking" class="btn btn-primary">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                    <path fill-rule="evenodd" d="M7.502 6h7.128A3.375 3.375 0 0 1 18 9.375v9.375a3 3 0 0 0 3-3V6.108c0-1.505-1.125-2.811-2.664-2.94a48.972 48.972 0 0 0-.673-.05A3 3 0 0 0 15 1.5h-1.5a3 3 0 0 0-2.663 1.618c-.225.015-.45.032-.673.05C8.662 3.295 7.554 4.542 7.502 6ZM13.5 3A1.5 1.5 0 0 0 12 4.5h4.5A1.5 1.5 0 0 0 15 3h-1.5Z" clip-rule="evenodd" />
                    <path fill-rule="evenodd" d="M3 9.375C3 8.339 3.84 7.5 4.875 7.5h9.75c1.036 0 1.875.84 1.875 1.875v11.25c0 1.035-.84 1.875-1.875 1.875h-9.75A1.875 1.875 0 0 1 3 20.625V9.375Zm9.586 4.594a.75.75 0 0 0-1.172-.938l-2.476 3.096-.908-.907a.75.75 0 0 0-1.06 1.06l1.5 1.5a.75.75 0 0 0 1.116-.062l3-3.75Z" clip-rule="evenodd" />
                </svg>
                Kiểm tra đơn hàng
            </a>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M11.584 2.376a.75.75 0 0 1 .832 0l9 6a.75.75 0 1 1-.832 1.248L12 3.901 3.416 9.624a.75.75 0 0 1-.832-1.248l9-6Z" />
                    <path fill-rule="evenodd" d="M20.25 10.332v9.918H21a.75.75 0 0 1 0 1.5H3a.75.75 0 0 1 0-1.5h.75v-9.918a.75.75 0 0 1 .634-.74A49.109 49.109 0 0 1 12 9c2.59 0 5.134.202 7.616.592a.75.75 0 0 1 .634.74Zm-7.5 2.418a.75.75 0 0 0-1.5 0v6.75a.75.75 0 0 0 1.5 0v-6.75Zm3-.75a.75.75 0 0 1 .75.75v6.75a.75.75 0 0 1-1.5 0v-6.75a.75.75 0 0 1 .75-.75ZM9 12.75a.75.75 0 0 0-1.5 0v6.75a.75.75 0 0 0 1.5 0v-6.75Z" clip-rule="evenodd" />
                    <path d="M12 7.875a1.125 1.125 0 1 0 0-2.25 1.125 1.125 0 0 0 0 2.25Z" />
                </svg>
                Tiếp tục mua sắm
            </a>
        </div>
    </div>
</div>
