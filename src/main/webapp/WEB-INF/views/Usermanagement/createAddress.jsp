<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add New Address</title>
</head>
<body>
<h1>Add New Address for User ID: ${userId}</h1>
<form action="/api/user" method="post">
    <input type="hidden" name="action" value="createAddress">
    <input type="hidden" name="userId" value="${userId}">
    <label>Address:</label><br>
    <textarea name="address" rows="4" cols="50"></textarea><br>
    <label><input type="checkbox" name="isDefaultAddress" value="true"> Set as Default</label><br>
    <input type="submit" value="Add Address">
</form>
<a href="/api/user?action=viewAddresses&id=${userId}">Back to Addresses</a>
</body>
</html>