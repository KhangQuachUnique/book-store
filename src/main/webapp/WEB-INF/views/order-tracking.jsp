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
        <span class="order-status ${order.status}">
          <c:choose>
            <c:when test="${order.status == 'completed'}">Hoàn thành</c:when>
            <c:when test="${order.status == 'shipped'}">Đang giao</c:when>
            <c:when test="${order.status == 'pending'}">Chờ xử lý</c:when>
            <c:when test="${order.status == 'cancelled'}">Đã hủy</c:when>
            <c:otherwise>Không xác định</c:otherwise>
          </c:choose>
        </span>
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
<%--                <p class="price">--%>
<%--                  <strong>Thành tiền:</strong> <fmt:formatNumber value="${item.price * item.quantity}" type="number"/> VND--%>
<%--                </p>--%>
              </div>
            </li>
            <!-- Cộng dồn tổng tiền đơn hàng -->
            <c:set var="orderTotal" value="${orderTotal + (item.price * item.quantity)}"/>
          </c:forEach>
        </ul>
        <p class="total"><strong>Thành tiền:</strong> <fmt:formatNumber value="${orderTotal}" type="number"/> VND</p>
      </div>

    </div> <!-- đóng order-card -->
  </c:forEach>
</div>

</body>
</html>
