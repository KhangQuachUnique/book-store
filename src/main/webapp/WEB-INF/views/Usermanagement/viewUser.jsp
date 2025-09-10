<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View User</title>
</head>
<body>
<h1>User Details</h1>
<p>ID: ${user.id}</p>
<p>Name: ${user.name}</p>
<p>Email: ${user.email}</p>
<p>Phone: ${user.phone}</p>
<p>Role: ${user.role}</p>
<p>Blocked: ${user.isBlocked}</p>
<p>Blocked Until: ${user.blockedUntil}</p>
<a href="/api/user?action=list">Back to List</a>
</body>
</html>