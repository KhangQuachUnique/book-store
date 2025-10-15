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

    <!-- Status filter bar -->
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
                            <span class="order-id">Mã đơn hàng: #${order.id}</span>
                            <span class="order-status ${orderStatusService.getCssClass(order.status)}">
                                    ${orderStatusService.getDisplayName(order.status)}
                            </span>
                        </div>

                        <p><strong>Ngày đặt:</strong>
                            <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                        </p>

                        <p><strong>Phương thức thanh toán:</strong> ${order.paymentMethod}</p>

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
                                        <!-- Create URL to product details page -->
                                        <c:url var="bookDetailUrl" value="/book-detail">
                                            <c:param name="id" value="${item.book.id}" />
                                        </c:url>

                                        <!-- Click on the image to go to details -->
                                        <a href="${bookDetailUrl}" class="book-link">
                                            <img src="${item.book.thumbnailUrl}" alt="${item.book.title}" class="book-thumbnail"/>
                                        </a>

                                        <div class="book-info">
                                            <!-- Click on title to go to details -->
                                            <a href="${bookDetailUrl}" class="book-title-link">
                                                <p class="book"><strong>${item.book.title}</strong></p>
                                            </a>

                                            <p class="price">
                                                <c:if test="${item.book.discountRate > 0}">
                                                    <span class="original-price">
                                                        <fmt:formatNumber value="${item.book.originalPrice}" type="number"/> VND
                                                    </span>
                                                    <span class="discount-rate">-${item.book.discountRate}%</span>
                                                </c:if>
                                                <span class="current-price">
                                                    <fmt:formatNumber value="${item.price}" type="number"/> VND
                                                </span>
                                            </p>
                                            <p class="qty">x ${item.quantity}</p>

                                            <!-- Write Review or badge -->
                                            <c:if test="${order.status == 'DELIVERED'}">
                                                <c:choose>
                                                    <c:when test="${reviewedMap[item.book.id]}">
                                                        <span class="reviewed-badge" title="Bạn đã đánh giá sản phẩm này">Đã đánh giá</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button type="button" class="open-review-btn" aria-haspopup="dialog" aria-expanded="false">
                                                            Viết đánh giá
                                                        </button>

                                                        <div class="review-popover" role="dialog" aria-hidden="true">
                                                            <div class="popover-arrow" aria-hidden="true"></div>
                                                            <form method="post" action="${pageContext.request.contextPath}/user/review" class="write-review-form">
                                                                <input type="hidden" name="bookId" value="${item.book.id}" />
                                                                <input type="hidden" name="rating" value="" />

                                                                <div class="star-input" aria-label="Chọn số sao" role="group">
                                                                    <button type="button" class="star" data-value="1" aria-label="1 sao" aria-pressed="false">
                                                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M259.3 17.8 194 150.2 47.9 171.5c-26.2 3.8-36.7 36-17.7 54.6l105.7 103-25 145.5c-4.5 26.1 23 46 46.4 33.7L288 439.6 418.7 508.3c23.4 12.3 50.9-7.6 46.4-33.7l-25-145.5 105.7-103c19-18.6 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"/></svg>
                                                                    </button>
                                                                    <button type="button" class="star" data-value="2" aria-label="2 sao" aria-pressed="false">
                                                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M259.3 17.8 194 150.2 47.9 171.5c-26.2 3.8-36.7 36-17.7 54.6l105.7 103-25 145.5c-4.5 26.1 23 46 46.4 33.7L288 439.6 418.7 508.3c23.4 12.3 50.9-7.6 46.4-33.7l-25-145.5 105.7-103c19-18.6 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"/></svg>
                                                                    </button>
                                                                    <button type="button" class="star" data-value="3" aria-label="3 sao" aria-pressed="false">
                                                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M259.3 17.8 194 150.2 47.9 171.5c-26.2 3.8-36.7 36-17.7 54.6l105.7 103-25 145.5c-4.5 26.1 23 46 46.4  33.7L288 439.6 418.7 508.3c23.4 12.3 50.9-7.6 46.4-33.7l-25-145.5 105.7-103c19-18.6 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"/></svg>
                                                                    </button>
                                                                    <button type="button" class="star" data-value="4" aria-label="4 sao" aria-pressed="false">
                                                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M259.3 17.8 194 150.2 47.9 171.5c-26.2 3.8-36.7 36-17.7 54.6l105.7 103-25 145.5c-4.5 26.1 23 46 46.4 33.7L288 439.6 418.7 508.3c23.4 12.3 50.9-7.6 46.4-33.7l-25-145.5 105.7-103c19-18.6 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"/></svg>
                                                                    </button>
                                                                    <button type="button" class="star" data-value="5" aria-label="5 sao" aria-pressed="false">
                                                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path d="M259.3 17.8 194 150.2 47.9 171.5c-26.2 3.8-36.7 36-17.7 54.6l105.7 103-25 145.5c-4.5 26.1 23 46 46.4 33.7L288 439.6 418.7 508.3c23.4 12.3 50.9-7.6 46.4-33.7l-25-145.5 105.7-103c19-18.6 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"/></svg>
                                                                    </button>
                                                                </div>

                                                                <label>
                                                                    Nhận xét:
                                                                    <textarea name="comment" rows="2" placeholder="Chia sẻ cảm nhận..." maxlength="1000"></textarea>
                                                                </label>
                                                                <div class="actions">
                                                                    <button type="submit">Gửi đánh giá</button>
                                                                    <button type="button" class="close-popover">Hủy</button>
                                                                </div>
                                                            </form>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>

                            <div class="price-summary">
                                <p>Tạm tính:
                                    <fmt:formatNumber value="${order.subtotal}" type="number"/> VND
                                </p>
                                <c:if test="${order.discountAmount > 0}">
                                    <p>Mã giảm giá:
                                        -<fmt:formatNumber value="${order.discountAmount}" type="number"/> VND
                                    </p>
                                </c:if>
                                <p><strong>Tổng cộng:
                                    <fmt:formatNumber value="${order.finalTotal}" type="number"/> VND
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
<script type="module" src="${pageContext.request.contextPath}/assets/js/pages/OrderTrackingPage.js"></script>
</body>
</html>
