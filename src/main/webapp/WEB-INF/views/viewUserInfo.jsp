<%--
  Created by IntelliJ IDEA.
  User: USER
  Date: 9/13/2025
  Time: 9:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/userInfo.css">
</head>
<body>
    <section class="profile-section">
        <h2 class="title">Hồ Sơ Của Bạn</h2>
        <div class = "container">
            <div class="user-info">
                <div class="info-row">
                    <span class="label">Họ và tên:</span>
                    <span class="value">${sessionScope.user.name}</span>
                </div>
                <div class="info-row">
                    <span class="label">Số điện thoại:</span>
                    <span class="value">${sessionScope.user.phoneNumber}</span>
                </div>
                <div class="info-row">
                    <span class="label">Email:</span>
                    <span class="value">${sessionScope.user.email}</span>
                </div>
                <div class="info-row">
                    <span class="label">Địa chỉ:</span>
                    <span class="value address-value">${sessionScope.defaultAddress}</span>
                </div>
            </div>

            <form class="form_bnt" action="${pageContext.request.contextPath}/user/edit" method="post">
                <input type="hidden" name="action" value="edit"/>
                <input class="submit" type="submit" value="Chỉnh sửa hồ sơ"/>
            </form>
        </div>
    </section>
</body>
</html>
