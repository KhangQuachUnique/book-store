<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Management - ${listType}</title>
    <link rel="stylesheet" href="/assets/styles/users.css">
</head>
<body>
<h1>${listType}</h1>
<nav class="user-nav">
    <a href="${pageContext.request.contextPath}/admin/user?action=list">All Users</a>
    <a href="${pageContext.request.contextPath}/admin/user?action=listAdmins">Admins</a>
    <a href="${pageContext.request.contextPath}/admin/user?action=listUsers">Customers</a>
    <a href="${pageContext.request.contextPath}/admin/user?action=listBlocked">Blocked Users</a>
</nav>

<nav class="admin-user-nav">
<a href="${pageContext.request.contextPath}/admin/user?action=newAdmin">Create Admin</a>
<a href="${pageContext.request.contextPath}/admin/user?action=newUser">Create User</a>
</nav>
<form action="${pageContext.request.contextPath}/admin/user?action=search" method="get">
    <input type="hidden" name="action" value="search">
    <label>Search by Name, Email, or Phone:</label>
    <input type="text" name="query">
    <input type="submit" value="Search">
</form>

<table border="1">
    <tr class="table-header"> <%-- table row --%>
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
            <td>${user.id}</td> <%--table data--%>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.phone}</td>
            <td>${user.role}</td>
            <td>${user.isBlocked}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/user?action=view&id=${user.id}" type="">View</a>
                <a href="${pageContext.request.contextPath}/admin/user?action=edit&id=${user.id}">Edit</a>
                <form action="${pageContext.request.contextPath}/admin/user" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${user.id}">
                    <input type="submit" value="Delete">
                </form>
                <form action="${pageContext.request.contextPath}/admin/user" method="post" style="display:inline;">
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