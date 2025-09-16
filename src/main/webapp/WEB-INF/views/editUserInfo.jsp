<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa thông tin người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/userInfo.css">
</head>
<body>
<h2>Chỉnh sửa thông tin người dùng</h2>

<div class="container">
    <form action="${pageContext.request.contextPath}/user/update" method="post">
        <input type="hidden" name="action" value="changeUserInfo"/>

        <label for="name">Họ tên:</label><br/>
        <input type="text" id="name" name="name" value="${user.name}" required/><br/>

        <span id="email-warning" class="warning">Email không thể thay đổi</span><br/>
        <label for="email">Email:</label><br/>
        <input type="email" id="email" name="email" value="${user.email}" readonly/><br/>
        <br/>

        <label for="phone">Số điện thoại:</label><br/>
        <input type="text" id="phone" name="phone" value="${user.phone}" /><br/><br/>

        <input type="submit" value="Xác nhận cập nhật" />
    </form>
</div>

<script>
    const emailInput = document.getElementById("email");
    const warning = document.getElementById("email-warning");

    emailInput.addEventListener("click", function () {
        warning.style.display = "inline"; // hiện dòng báo
    });
</script>

</body>
</html>
