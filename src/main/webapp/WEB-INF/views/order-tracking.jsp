<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
  <title>Theo dõi đơn hàng</title>
  <link rel="stylesheet" href="<c:url value='/assets/styles/OrderTracking.css'/>">
</head>
<body>
<h2 class="page-title">📦 Danh sách đơn hàng</h2>

<!-- Thanh bar filter trạng thái -->
<div class="status-bar">
  <!-- Tab "Tất cả" cố định (không lấy từ DB) -->
  <a href="${pageContext.request.contextPath}/user/order-tracking?userId=${userId}"
     class="${selectedStatus eq 'all' ? 'active' : ''}">Tất cả</a>

  <!-- Các trạng thái thực tế từ DB, bỏ qua id=0 -->
  <c:forEach var="st" items="${statuses}">
    <c:if test="${st.id != 0}">
      <a href="${pageContext.request.contextPath}/user/order-tracking?userId=${userId}&statusId=${st.id}"
         class="${selectedStatus eq st.id.toString() ? 'active' : ''}">
          ${st.name}
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
            <span class="order-status">${order.statusName}</span>
          </div>

          <p><strong>Ngày đặt:</strong> ${order.createdAt}</p>
          <p><strong>Thanh toán:</strong> ${order.paymentMethod}</p>

          <div class="order-items">
            <h4>Sản phẩm:</h4>
            <ul>
              <c:set var="orderTotal" value="0"/>
              <c:forEach var="item" items="${order.items}">
                <li>
                  <img src="${item.thumbnailUrl}" alt="${item.bookTitle}" class="book-thumbnail"/>
                  <div class="book-info">
                    <p class="book"><strong>${item.bookTitle}</strong></p>
                    <p class="price">
                      <fmt:formatNumber value="${item.price}" type="number"/> VND
                    </p>
                    <p class="qty">x ${item.quantity}</p>
                  </div>
                </li>
                <c:set var="orderTotal" value="${orderTotal + (item.price * item.quantity)}"/>
              </c:forEach>
            </ul>
            <p class="total"><strong>Thành tiền:</strong>
              <fmt:formatNumber value="${orderTotal}" type="number"/> VND
            </p>
          </div>

        </div>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <p class="no-orders">Bạn chưa có đơn hàng nào.</p>
    </c:otherwise>
  </c:choose>
</div>

</body>
</html>
