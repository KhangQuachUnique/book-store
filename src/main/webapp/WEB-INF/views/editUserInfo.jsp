<%--
  Created by IntelliJ IDEA.
  User: USER
  Date: 9/14/2025
  Time: 3:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Chỉnh sửa thông tin người dùng</title>
</head>
<body>
<h2>Chỉnh sửa thông tin người dùng</h2>

<form action="${pageContext.request.contextPath}/user/update" method="post">
    <input type="hidden" name="action" value="changeUserInfo"/>

    <label for="name">Họ tên:</label><br/>
    <input type="text" id="name" name="name" value="${user.name}" required/><br/><br/>

    <label for="email">Email:</label><br/>
    <input type="email" id="email" name="email" value="${user.email}" readonly/><br/><br/>

    <label for="phone">Số điện thoại:</label><br/>
    <input type="text" id="phone" name="phone" value="${user.phone}" /><br/><br/>

    <input type="submit" value="Xác nhận cập nhật" />
</form>

</body>
</html>
