<%--
  Created by IntelliJ IDEA.
  User: USER
  Date: 9/14/2025
  Time: 5:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đổi mật khẩu</title>
    <script>
        function validatePasswords() {
            const newPwd = document.getElementById('newPassword').value;
            const confirm = document.getElementById('confirmPassword').value;
            const submitBtn = document.getElementById('submitBtn');
            const mismatch = document.getElementById('mismatch');

            if (newPwd !== confirm) {
                mismatch.textContent = 'Mật khẩu mới và xác nhận chưa khớp.';
                submitBtn.disabled = true;
            } else {
                mismatch.textContent = '';
                submitBtn.disabled = false;
            }
        }
    </script>
</head>
<body>
<h2>Đổi mật khẩu</h2>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>
<c:if test="${not empty success}">
    <p style="color:green">${success}</p>
</c:if>

<form action="${pageContext.request.contextPath}/user/update" method="post">
    <input type="hidden" name="action" value="changeUserPassword"/>

    <label for="newPassword">Mật khẩu mới:</label><br>
    <input type="password" id="newPassword" name="newPassword" required oninput="validatePasswords()"><br><br>

    <label for="confirmPassword">Xác nhận mật khẩu mới:</label><br>
    <input type="password" id="confirmPassword" name="confirmPassword" required oninput="validatePasswords()"><br>
    <span id="mismatch" style="color:red"></span><br><br>

    <button type="submit" id="submitBtn" disabled>Đổi mật khẩu</button>
</form>
</body>
</html>
