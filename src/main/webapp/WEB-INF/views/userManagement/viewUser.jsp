
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Management - View User</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/viewUser.css">
</head>
<body class="bg-background text-foreground">
<div class="container">
    <div class="card">
        <div class="card-header">
            <h1 class="card-title">User Details</h1>
        </div>
        <div class="card-content">
            <div class="info-item">
                <strong>ID:</strong> ${user.id}
            </div>
            <div class="info-item">
                <strong>Name:</strong> ${user.name}
            </div>
            <div class="info-item">
                <strong>Email:</strong> ${user.email}
            </div>
            <div class="info-item">
                <strong>Phone:</strong> ${user.phone}
            </div>
            <div class="info-item">
                <strong>Role:</strong> ${user.role}
            </div>
            <div class="info-item">
                <strong>Blocked:</strong> ${user.isBlocked ? "Yes" : "No"}
            </div>
            <div class="info-item">
                <strong>Blocked Until:</strong> ${user.blockedUntil != null ? user.blockedUntil : "N/A"}
            </div>
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/admin/user?action=viewAddresses&id=${user.id}" class="btn btn-secondary">View Addresses</a>
                <a href="${pageContext.request.contextPath}/admin/user?action=list" class="btn btn-outline">Back to List</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
