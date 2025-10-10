<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Management - Create User</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/createUser.css">
</head>
<body class="bg-background text-foreground">
<div class="container">
    <div class="card">
        <div class="card-header">
            <h1 class="card-title">Create New User</h1>
        </div>
        <div class="card-content">

            <form action="${pageContext.request.contextPath}/admin/user" method="post" class="form">
                <input type="hidden" name="action" value="createUser">
                <div class="form-group">
                    <label for="name" class="label">Name:</label>
                    <input type="text" id="name" name="name" class="input" required>
                </div>
                <div class="form-group">
                    <label for="email" class="label">Email:</label>
                    <input type="email" id="email" name="email" class="input" required>
                </div>
                <div class="form-group">
                    <label for="password" class="label">Password:</label>
                    <input type="password" id="password" name="password" class="input" required>
                </div>
                <div class="form-group">
                    <label for="phone" class="label">Phone:</label>
                    <input type="text" id="phone" name="phone" class="input" required>
                </div>
                <div class="action-buttons">
                    <input type="submit" value="Create" class="btn btn-primary">
                    <a href="${pageContext.request.contextPath}/admin/user?action=list" class="btn btn-outline">Back to List</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>