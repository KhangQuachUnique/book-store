<%--
  Created by IntelliJ IDEA.
  User: USER
  Date: 9/13/2025
  Time: 9:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thông tin người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/userInfo.css">
</head>
<body>

<c:if test="${not empty sessionScope.toastMessage}">
    <script>
        // Hiện toast khi load trang
        window.onload = function() {
            showToast("${sessionScope.toastMessage}");
        }
    </script>
    <c:remove var="toastMessage" scope="session"/>
</c:if>

<style>
    .toast {
        position: fixed;
        top: 20px;
        right: 20px;
        background: #4CAF50;
        color: white;
        padding: 15px 20px;
        border-radius: 8px;
        box-shadow: 0px 3px 8px rgba(0,0,0,0.2);
        opacity: 0;
        transform: translateX(100%);
        transition: all 0.5s ease;
        z-index: 9999;
    }
    .toast.show {
        opacity: 1;
        transform: translateX(0);
    }
</style>

<div id="toast" class="toast"></div>

<script>
    function showToast(message) {
        const toast = document.getElementById("toast");
        toast.textContent = message;
        toast.classList.add("show");

        setTimeout(() => {
            toast.classList.remove("show");
        }, 3000);
    }
</script>

<h1>Hồ sơ của bạn</h1>
<div class = "container">
    <div class="user-info">
        <div class="info-row">
            <span class="label">Tên:</span>
            <span class="value">${sessionScope.user.name}</span>
        </div>
        <div class="info-row">
            <span class="label">Số điện thoại:</span>
            <span class="value">${sessionScope.user.phone}</span>
        </div>
        <div class="info-row">
            <span class="label">Email:</span>
            <span class="const">${sessionScope.user.email}</span>
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/user/edit" method="post">
        <input type="hidden" name="action" value="changePassword"/>
        <input type="submit" value="Đổi mật khẩu" />
    </form>

    <form action="${pageContext.request.contextPath}/user/edit" method="post">
        <input type="hidden" name="action" value="editInfo"/>
        <input type="submit" value="Chỉnh sửa hồ sơ" />
    </form>
</div>

</body>
</html>
