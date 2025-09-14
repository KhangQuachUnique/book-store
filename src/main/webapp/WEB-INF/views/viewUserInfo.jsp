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
    <title>Thông tin người dùng</title>
</head>
<body>

<h1>Hồ sơ của ${user.name}</h1>
<p>Tên: ${user.name}</p>
<p>Số điện thoại: ${user.phone}</p>
<p>Email: ${user.email}</p>
<p>Mật khẩu: ******</p>


<form action="${pageContext.request.contextPath}/user/edit" method="post">
    <input type="hidden" name="id" value="${user.id}"/>
    <input type="hidden" name="email" value="${user.email}"/>
    <input type="hidden" name="action" value="edit"/>

    <input type="submit" value="Chỉnh sửa hồ sơ" />
</form>

</body>
</html>
