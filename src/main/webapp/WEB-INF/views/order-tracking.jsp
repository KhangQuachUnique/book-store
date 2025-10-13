<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Theo dõi đơn hàng</title>
    <link rel="stylesheet" href="<c:url value='/assets/styles/OrderTracking.css'/>">
</head>

<body>
<div class="order-tracking">
    <h2 class="page-title">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.1.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M465.4 192L431.1 144L209 144L174.7 192L465.4 192zM96 212.5C96 199.2 100.2 186.2 107.9 175.3L156.9 106.8C168.9 90 188.3 80 208.9 80L431 80C451.7 80 471.1 90 483.1 106.8L532 175.3C539.8 186.2 543.9 199.2 543.9 212.5L544 480C544 515.3 515.3 544 480 544L160 544C124.7 544 96 515.3 96 480L96 212.5z"></path></svg>
        Danh sách đơn hàng
    </h2>

    <!-- Thanh bar filter trạng thái -->
    <div class="status-bar">
        <a href="${pageContext.request.contextPath}/user/order-tracking"
           class="${selectedStatus eq 'ALL' ? 'active' : ''}">Tất cả</a>

        <c:forEach var="st" items="${statuses}">
            <c:if test="${st ne 'ALL'}">
                <a href="${pageContext.request.contextPath}/user/order-tracking?status=${st}"
                   class="${selectedStatus eq st ? 'active' : ''}">
                        ${orderStatusService.getDisplayName(st)}
                </a>
            </c:if>
        </c:forEach>
    </div>

    <div class="orders-container">
        <c:choose>
            <c:when test="${not empty orders}">
                <c:forEach var="order" items="${orders}">
                    <div class="order-card">
                        <div class="order-header">
                            <span class="order-id">Mã đơn: #${order.id}</span>
                            <span class="order-status ${orderStatusService.getCssClass(order.status)}">
                                    ${orderStatusService.getDisplayName(order.status)}
                            </span>
                        </div>

                        <p><strong>Ngày đặt:</strong>
                            <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                        </p>

                        <p><strong>Thanh toán:</strong> ${order.paymentMethod}</p>

                        <c:if test="${not empty order.promotion}">
                            <p class="promo-info">
                                Khuyến mãi: ${order.promotion.code} – Giảm
                                <fmt:formatNumber value="${order.promotion.discount}" maxFractionDigits="0"/>%
                            </p>
                        </c:if>

                        <div class="order-items">
                            <h4>Sản phẩm:</h4>
                            <ul>
                                <c:forEach var="item" items="${order.items}">
                                    <li>
                                        <img src="${item.book.thumbnailUrl}" alt="${item.book.title}"
                                             class="book-thumbnail"/>
                                        <div class="book-info">
                                            <p><strong>${item.book.title}</strong></p>
                                            <p class="price">
                                                <c:if test="${item.book.discountRate > 0}">
                                                    <span class="original-price">
                                                        <fmt:formatNumber value="${item.book.originalPrice}"
                                                                          type="number"/> VNĐ
                                                    </span>
                                                    <span class="discount-rate">-${item.book.discountRate}%</span>
                                                </c:if>
                                                <span class="current-price">
                                                    <fmt:formatNumber value="${item.price}" type="number"/> VNĐ
                                                </span>
                                            </p>
                                            <p class="qty">x ${item.quantity}</p>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>

                            <div class="price-summary">
                                <p>Tạm tính:
                                    <fmt:formatNumber value="${order.subtotal}" type="number"/> VNĐ
                                </p>
                                <c:if test="${order.discountAmount > 0}">
                                    <p>Giảm mã:
                                        -<fmt:formatNumber value="${order.discountAmount}" type="number"/> VNĐ
                                    </p>
                                </c:if>
                                <p><strong>Thành tiền:
                                    <fmt:formatNumber value="${order.finalTotal}" type="number"/> VNĐ
                                </strong></p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="no-orders">Bạn chưa có đơn hàng nào!</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>