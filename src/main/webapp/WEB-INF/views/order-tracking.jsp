<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
  <title>Theo dõi đơn hàng</title>
  <link rel="stylesheet" href="<c:url value='/assets/styles/OrderTracking.css'/>">
</head>
<div class="order-tracking">

  <h2 class="page-title">📦 Danh sách đơn hàng</h2>

  <!-- Thanh bar filter trạng thái -->
  <div class="status-bar">
    <!-- Tab "Tất cả" cố định (không lấy từ DB) -->
    <a href="${pageContext.request.contextPath}/user/order-tracking"
       class="${selectedStatus eq 'all' ? 'active' : ''}">Tất cả</a>

    <!-- Các trạng thái thực tế từ DB, bỏ qua id=0 -->
    <c:forEach var="st" items="${statuses}">
      <c:if test="${st.id != 0}">
        <a href="${pageContext.request.contextPath}/user/order-tracking?statusId=${st.id}"
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
              <c:set var="statusClass" value=""/>
              <c:choose>
                <c:when test="${order.statusName eq 'Đơn Hàng Đã Đặt'}">
                  <c:set var="statusClass" value="pending"/>
                </c:when>
                <c:when test="${order.statusName eq 'Chờ Xác Nhận'}">
                  <c:set var="statusClass" value="confirmed"/>
                </c:when>
                <c:when test="${order.statusName eq 'Chờ Thanh Toán'}">
                  <c:set var="statusClass" value="waiting-payment"/>
                </c:when>
                <c:when test="${order.statusName eq 'Chờ Giao Hàng' || order.statusName eq 'Đang Giao Hàng'}">
                  <c:set var="statusClass" value="shipping"/>
                </c:when>
                <c:when test="${order.statusName eq 'Đã Hoàn Thành'}">
                  <c:set var="statusClass" value="completed"/>
                </c:when>
                <c:when test="${order.statusName eq 'Đã Hủy'}">
                  <c:set var="statusClass" value="cancelled"/>
                </c:when>
              </c:choose>

              <span class="order-status ${statusClass}">
                  ${order.statusName}
              </span>
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
                        <c:if test="${item.discountRate > 0}">
                          <span class="original-price">
                            <fmt:formatNumber value="${item.originalPrice}" type="number"/> VNĐ
                          </span>
                          <span class="discount-rate">
                            -${item.discountRate}%
                          </span>
                        </c:if>
                        <span class="current-price">
                          <fmt:formatNumber value="${item.price}" type="number"/> VNĐ
                        </span>
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
        <p class="no-orders">Bạn chưa có đơn hàng nào !</p>
      </c:otherwise>
    </c:choose>
  </div>
</div>
</html>

