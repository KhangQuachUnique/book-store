<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
  <title>Theo dõi đơn hàng</title>
  <link rel="stylesheet" href="<c:url value='/assets/styles/OrderTracking.css'/>">
</head>
<body>
<h2 class="page-title">📦 Danh sách đơn hàng của bạn</h2>

<div class="orders-container">
  <c:forEach var="order" items="${orders}">
    <div class="order-card">

      <!-- Header -->
      <div class="order-header">
        <span class="order-id">Mã đơn: #${order.id}</span>

        <!-- Mapping statusId sang class CSS -->
        <c:choose>
          <c:when test="${order.statusId == 1}">
            <span class="order-status pending">${order.statusName}</span>
          </c:when>
          <c:when test="${order.statusId == 2}">
            <span class="order-status confirmed">${order.statusName}</span>
          </c:when>
          <c:when test="${order.statusId == 3}">
            <span class="order-status shipped">${order.statusName}</span>
          </c:when>
          <c:when test="${order.statusId == 4}">
            <span class="order-status completed">${order.statusName}</span>
          </c:when>
          <c:when test="${order.statusId == 5}">
            <span class="order-status cancelled">${order.statusName}</span>
          </c:when>
          <c:otherwise>
            <span class="order-status">${order.statusName}</span>
          </c:otherwise>
        </c:choose>
      </div>

      <p><strong>Ngày đặt:</strong> ${order.createdAt}</p>
      <p><strong>Thanh toán:</strong> ${order.paymentMethod}</p>

      <!-- Danh sách sản phẩm -->
      <div class="order-items">
        <h4>Sản phẩm:</h4>
        <ul>
          <c:set var="orderTotal" value="0"/>
          <c:forEach var="item" items="${order.items}">
            <li>
              <img src="${item.thumbnailUrl}" alt="${item.bookTitle}" class="book-thumbnail"/>
              <div class="book-info">
                <p class="book"><strong>Tựa sách:</strong> ${item.bookTitle}</p>
                <p class="price">
                  <strong>Giá:</strong> <fmt:formatNumber value="${item.price}" type="number"/> VND
                </p>
                <p class="qty"><strong>Số lượng:</strong> ${item.quantity}</p>
              </div>
            </li>
            <!-- Cộng dồn tổng tiền -->
            <c:set var="orderTotal" value="${orderTotal + (item.price * item.quantity)}"/>
          </c:forEach>
        </ul>
        <p class="total"><strong>Thành tiền:</strong> <fmt:formatNumber value="${orderTotal}" type="number"/> VND</p>
      </div>

    </div> <!-- order-card -->
  </c:forEach>
</div>

</body>
</html>
