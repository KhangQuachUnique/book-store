<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Management - ${listType}</title>
</head>
<body>
<h1>${listType}</h1>
<a href="/api/user?action=list">All Users</a> |
<a href="/api/user?action=listAdmins">Admins</a> |
<a href="/api/user?action=listUsers">Customers</a> |
<a href="/api/user?action=listBlocked">Blocked Users</a><br><br>

<a href="/api/user?action=newAdmin">Create Admin</a> |
<a href="/api/user?action=newUser">Create User</a><br><br>

<form action="/api/user?action=search" method="get">
    <input type="hidden" name="action" value="search">
    <label>Search by Name, Email, or Phone:</label>
    <input type="text" name="query">
    <input type="submit" value="Search">
</form>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Phone</th>
        <th>Role</th>
        <th>Blocked</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.phone}</td>
            <td>${user.role}</td>
            <td>${user.isBlocked}</td>
            <td>
                <a href="/api/user?action=view&id=${user.id}">View</a>
                <a href="/api/user?action=edit&id=${user.id}">Edit</a>
                <form action="/api/user" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${user.id}">
                    <input type="submit" value="Delete">
                </form>
                <form action="/api/user" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="${user.isBlocked ? 'unblock' : 'block'}">
                    <input type="hidden" name="id" value="${user.id}">
                    <input type="submit" value="${user.isBlocked ? 'Unblock' : 'Block'}">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>