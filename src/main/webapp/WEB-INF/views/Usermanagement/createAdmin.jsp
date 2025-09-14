<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Admin</title>
</head>
<body>
<h1>Create New Admin</h1>
<form action="/admin/user" method="post">
    <input type="hidden" name="action" value="createAdmin">
    <label>Name:</label> <input type="text" name="name"><br>
    <label>Email:</label> <input type="text" name="email"><br>
    <label>Password:</label> <input type="password" name="password"><br>
    <label>Phone:</label> <input type="text" name="phone"><br>
    <input type="submit" value="Create">
</form>
<a href="/admin/user?action=list">Back to List</a>
</body>
</html>