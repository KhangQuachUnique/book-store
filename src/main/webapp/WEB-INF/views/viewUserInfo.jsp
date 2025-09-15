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

<h1>Hồ sơ của <span class="username">${sessionScope.user.name}</span></h1>
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
            <span class="value">${sessionScope.user.email}</span>
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
