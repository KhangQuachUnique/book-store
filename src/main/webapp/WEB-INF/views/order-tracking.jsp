<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Theo dõi đơn hàng</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    .order { border:1px solid #ccc; margin:10px; padding:10px; border-radius: 6px; }
    .order h4 { margin-top: 10px; }
    .empty { color: #999; }
  </style>
</head>
<body>
<h2>Theo dõi đơn hàng</h2>

<c:if test="${empty orders}">
  <p class="empty">Bạn chưa có đơn hàng nào.</p>
</c:if>

<c:forEach var="order" items="${orders}">
  <div class="order">
    <p><b>Mã đơn:</b> ${order.id}</p>
    <p><b>Ngày đặt:</b> ${order.createdAt}</p>
    <p><b>Tổng tiền:</b> ${order.totalAmount} đ</p>
    <p><b>Trạng thái:</b> ${order.status}</p>
    <p><b>Thanh toán:</b> ${order.paymentMethod}</p>

    <h4>Sản phẩm:</h4>
    <ul>
      <c:forEach var="item" items="${order.items}">
        <li>${item.bookTitle} - SL: ${item.quantity} - Giá: ${item.price} đ</li>
      </c:forEach>
    </ul>
  </div>
</c:forEach>
</body>
</html>
