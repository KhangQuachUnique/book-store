<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit User</title>
</head>
<body>
    <h1>Edit User</h1>
    <form action="/${pageContext.request.contextPath}/admin/user" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${user.id}">
        <label>Name:</label> <input type="text" name="name" value="${user.name}"><br>
        <label>Email:</label> <input type="text" name="email" value="${user.email}"><br>
        <label>Phone:</label> <input type="text" name="phone" value="${user.phone}"><br>
        <label>Role:</label>
        <select name="role">
            <option value="customer" ${user.role == 'customer' ? 'selected' : ''}>Customer</option>
            <option value="admin" ${user.role == 'admin' ? 'selected' : ''}>Admin</option>
        </select><br>
        <input type="submit" value="Save">
    </form>
    <a href="/admin/user?action=list">Back to List</a>
</body>
</html>