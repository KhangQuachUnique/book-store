<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Management - Add New Address</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/createAddress.css">
</head>
<body class="bg-background text-foreground">
<div class="container">
    <div class="card">
        <div class="card-header">
            <!-- CHANGE: Sử dụng selectedUserId để hiển thị đúng ID user được chọn, tránh session -->
            <h1 class="card-title">Add New Address for User ID: ${selectedUserId}</h1>
        </div>
        <div class="card-content">
            <c:if test="${empty selectedUserId}">
                <p class="error">Invalid user ID. <a href="${pageContext.request.contextPath}/admin/user?action=list">Back to List</a></p>
                <!-- CHANGE: Thêm kiểm tra để tránh bug nếu selectedUserId null -->
            </c:if>
            <c:if test="${not empty selectedUserId}">
                <form action="${pageContext.request.contextPath}/admin/user" method="post" class="form">
                    <input type="hidden" name="action" value="createAddress">
                    <!-- CHANGE: Sử dụng selectedUserId từ request để đảm bảo ID đúng, không từ session -->
                    <input type="hidden" name="userId" value="${selectedUserId}">
                    <div class="form-group">
                        <label for="address" class="label">Address:</label>
                        <textarea id="address" name="address" rows="4" class="textarea" required placeholder="Enter full address..."></textarea>
                    </div>
                    <div class="checkbox-group">
                        <input type="checkbox" id="isDefaultAddress" name="isDefaultAddress" value="true" class="checkbox">
                        <label for="isDefaultAddress" class="label checkbox-label">Set as Default</label>
                    </div>
                    <div class="action-buttons">
                        <input type="submit" value="Add Address" class="btn btn-primary">
                        <!-- CHANGE: Back link sử dụng selectedUserId để quay về view của user đúng -->
                        <a href="${pageContext.request.contextPath}/admin/user?action=view&id=${selectedUserId}" class="btn btn-outline">Back to User Details</a>
                    </div>
                </form>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>