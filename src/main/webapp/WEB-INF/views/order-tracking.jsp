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

  <h2 class="page-title">📦 Danh sách đơn hàng</h2>

  <!-- Thanh bar filter trạng thái -->
  <div class="status-bar">
    <!-- Tab "Tất cả" -->
    <a href="${pageContext.request.contextPath}/user/order-tracking"
       class="${selectedStatus eq 'ALL' ? 'active' : ''}">Tất cả</a>

    <!-- Lặp qua Enum -->
    <c:forEach var="st" items="${statuses}">
      <c:if test="${st ne 'ALL'}">
        <a href="${pageContext.request.contextPath}/user/order-tracking?status=${st}"
           class="${selectedStatus eq st ? 'active' : ''}">
          <!-- Hiển thị tên trạng thái thân thiện -->
          <c:choose>
            <c:when test="${st eq 'PENDING'}">Đang chờ</c:when>
            <c:when test="${st eq 'PROCESSING'}">Đang xử lý</c:when>
            <c:when test="${st eq 'SHIPPED'}">Đang giao</c:when>
            <c:when test="${st eq 'DELIVERED'}">Đã giao</c:when>
            <c:when test="${st eq 'CANCELED'}">Đã hủy</c:when>
          </c:choose>
        </a>
      </c:if>
    </c:forEach>
  </div>

  <!-- Danh sách đơn hàng -->
  <div class="orders-container">
    <c:choose>
      <c:when test="${not empty orders}">
        <c:forEach var="order" items="${orders}">
          <div class="order-card">

            <div class="order-header">
              <span class="order-id">Mã đơn: #${order.id}</span>

              <!-- Đổi màu trạng thái -->
              <c:choose>
                <c:when test="${order.status eq 'PENDING'}">
                  <c:set var="statusClass" value="pending"/>
                </c:when>
                <c:when test="${order.status eq 'PROCESSING'}">
                  <c:set var="statusClass" value="processing"/>
                </c:when>
                <c:when test="${order.status eq 'SHIPPED'}">
                  <c:set var="statusClass" value="shipping"/>
                </c:when>
                <c:when test="${order.status eq 'DELIVERED'}">
                  <c:set var="statusClass" value="delivered"/>
                </c:when>
                <c:when test="${order.status eq 'CANCELED'}">
                  <c:set var="statusClass" value="cancelled"/>
                </c:when>
              </c:choose>

              <span class="order-status ${statusClass}">
                <c:choose>
                  <c:when test="${order.status eq 'PENDING'}">Đang chờ</c:when>
                  <c:when test="${order.status eq 'PROCESSING'}">Đang xử lý</c:when>
                  <c:when test="${order.status eq 'SHIPPED'}">Đang giao</c:when>
                  <c:when test="${order.status eq 'DELIVERED'}">Đã giao</c:when>
                  <c:when test="${order.status eq 'CANCELED'}">Đã hủy</c:when>
                </c:choose>
              </span>
            </div>

            <p><strong>Ngày đặt:</strong> <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm" /></p>
            <p><strong>Thanh toán:</strong> ${order.paymentMethod}</p>

            <div class="order-items">
              <h4>Sản phẩm:</h4>
              <ul>
                <c:set var="orderTotal" value="0"/>
                <c:forEach var="item" items="${order.items}">
                  <li>
                    <img src="${item.book.thumbnailUrl}" alt="${item.book.title}" class="book-thumbnail"/>
                    <div class="book-info">
                      <p class="book"><strong>${item.book.title}</strong></p>
                      <p class="price">
                        <fmt:formatNumber value="${item.book.price}" type="number"/> VNĐ
                      </p>
                      <p class="qty">x ${item.quantity}</p>
                    </div>
                  </li>
                  <c:set var="orderTotal" value="${orderTotal + (item.book.price * item.quantity)}"/>
                </c:forEach>
              </ul>
              <p class="total"><strong>Thành tiền:</strong>
                <fmt:formatNumber value="${orderTotal}" type="number"/> VNĐ
              </p>
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
