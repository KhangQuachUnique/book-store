
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Management - Add New Address</title>
    <link rel="stylesheet" href="/assets/styles/createAddress.css">
</head>
<body class="bg-background text-foreground">
<div class="container">
    <div class="card">
        <div class="card-header">
            <h1 class="card-title">Add New Address for User ID: ${userId}</h1>
        </div>
        <div class="card-content">
            <form action="/admin/user" method="post" class="form">
                <input type="hidden" name="action" value="createAddress">
                <input type="hidden" name="userId" value="${userId}">
                <div class="form-group">
                    <label for="address" class="label">Address:</label>
                    <textarea id="address" name="address" rows="4" class="textarea" required></textarea>
                </div>
                <div class="checkbox-group">
                    <input type="checkbox" id="isDefaultAddress" name="isDefaultAddress" value="true" class="checkbox">
                    <label for="isDefaultAddress" class="label checkbox-label">Set as Default</label>
                </div>
                <div class="action-buttons">
                    <input type="submit" value="Add Address" class="btn btn-primary">
                    <a href="/admin/user?action=viewAddresses&id=${userId}" class="btn btn-outline">Back to Addresses</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
