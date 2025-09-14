
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Management - Edit User</title>
    <link rel="stylesheet" href="/assets/styles/editUser.css">
</head>
<body class="bg-background text-foreground">
<div class="container">
    <div class="card">
        <div class="card-header">
            <h1 class="card-title">Edit User</h1>
        </div>
        <div class="card-content">
            <form action="/admin/user" method="post" class="form">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${user.id}">
                <div class="form-group">
                    <label for="name" class="label">Name:</label>
                    <input type="text" id="name" name="name" value="${user.name}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="email" class="label">Email:</label>
                    <input type="email" id="email" name="email" value="${user.email}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="phone" class="label">Phone:</label>
                    <input type="text" id="phone" name="phone" value="${user.phone}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="role" class="label">Role:</label>
                    <select id="role" name="role" class="select">
                        <option value="customer" ${user.role == 'customer' ? 'selected' : ''}>Customer</option>
                        <option value="admin" ${user.role == 'admin' ? 'selected' : ''}>Admin</option>
                    </select>
                </div>
                <div class="action-buttons">
                    <input type="submit" value="Save" class="btn btn-primary">
                    <a href="/admin/user?action=list" class="btn btn-outline">Back to List</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
