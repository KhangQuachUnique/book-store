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
            <div class="success-icon">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                    <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41L9 16.17z"/>
                </svg>
            </div>

            <h1 class="success-title">Đặt Hàng Thành Công!</h1>
            <p class="success-message">Cảm ơn bạn đã mua hàng. Đơn hàng của bạn đang được xử lý.</p>
        </div>

        <div class="order-details">
            <h3 class="section-title">Chi Tiết Đơn Hàng</h3>

            <div class="detail-row">
                <span class="detail-label">Mã đơn hàng:</span>
                <span class="detail-value order-id">#${order.id}</span>
            </div>

            <div class="detail-row">
                <span class="detail-label">Ngày đặt:</span>
                <span class="detail-value">
                    <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                </span>
            </div>

            <div class="detail-row">
                <span class="detail-label">Phương thức thanh toán:</span>
                <span class="detail-value">${order.paymentMethod}</span>
            </div>

            <div class="detail-row total-row">
                <span class="detail-label">Tổng tiền:</span>
                <span class="detail-value total-amount">
                    <fmt:formatNumber value="${order.finalTotal}" type="number" maxFractionDigits="0"/> ₫
                </span>
            </div>
        </div>

        <div class="order-actions">
            <a href="${pageContext.request.contextPath}/user/order-tracking" class="btn btn-primary">
                Xem Đơn Hàng
            </a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                Tiếp Tục Mua Sắm
            </a>
        </div>

        <p class="follow-up-message">
            Chúng tôi đã gửi email xác nhận đến <strong>${sessionScope.user.email}</strong>
        </p>
    </div>
</div>
