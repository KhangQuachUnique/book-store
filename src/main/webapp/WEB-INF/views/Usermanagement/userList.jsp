<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Management - ${listType}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/users.css">
</head>
<body class="bg-background text-foreground">
<div class="container">
    <h1 class="page-title">${listType}</h1>

    <nav class="nav-main">
        <a href="${pageContext.request.contextPath}/admin/user?action=list" class="nav-link">All Users</a>
        <a href="${pageContext.request.contextPath}/admin/user?action=listAdmins" class="nav-link">Admins</a>
        <a href="${pageContext.request.contextPath}/admin/user?action=listUsers" class="nav-link">Customers</a>
        <a href="${pageContext.request.contextPath}/admin/user?action=listBlocked" class="nav-link">Blocked Users</a>
    </nav>

    <nav class="nav-admin">
        <a href="${pageContext.request.contextPath}/admin/user?action=newAdmin" class="btn btn-secondary">Create Admin</a>
        <a href="${pageContext.request.contextPath}/admin/user?action=newUser" class="btn btn-secondary">Create User</a>
    </nav>

    <div class="card search-card">
        <div class="card-header">
            <h2 class="card-title">Search Users</h2>
        </div>
        <div class="card-content">
            <form action="${pageContext.request.contextPath}/admin/user" method="get" class="search-form">
                <input type="hidden" name="action" value="search">
                <input type="text" name="query" class="input" placeholder="Search by Name, Email, or Phone" value="${param.query}">
                <input type="submit" value="Search" class="btn btn-primary">
            </form>
        </div>
    </div>

    <div class="card">
        <div class="card-content">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/admin/user?action=${param.action}&page=${currentPage - 1}&query=${param.query}" class="btn btn-secondary">Previous</a>
                </c:if>
                <span>Page ${currentPage} of ${totalPages}</span>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/admin/user?action=${param.action}&page=${currentPage + 1}&query=${param.query}" class="btn btn-secondary">Next</a>
                </c:if>
            </div>
            <table class="table">
                <thead>
                <tr class="table-header">
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Role</th>
                    <th>Blocked</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>${user.phone}</td>
                        <td>${user.role}</td>
                        <td>${user.isBlocked ? "Yes" : "No"}</td>
                        <td class="action-buttons">
                            <a href="${pageContext.request.contextPath}/admin/user?action=view&id=${user.id}" class="btn btn-outline btn-sm">View</a>
                            <a href="${pageContext.request.contextPath}/admin/user?action=edit&id=${user.id}" class="btn btn-outline btn-sm">Edit</a>
                            <form action="/admin/user" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${user.id}">
                                <input type="submit" value="Delete" class="btn btn-destructive btn-sm btn-lowe" onclick="return confirm('Are you sure you want to delete this user?')">
                            </form>
                            <form action="${pageContext.request.contextPath}/admin/user" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="${user.isBlocked ? 'unblock' : 'block'}">
                                <input type="hidden" name="id" value="${user.id}">
                                <input type="submit" value="${user.isBlocked ? 'Unblock' : 'Block'}" class="btn btn-secondary btn-sm btn-lowe">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/admin/user?action=${param.action}&page=${currentPage - 1}&query=${param.query}" class="btn btn-secondary">Previous</a>
                </c:if>
                <span>Page ${currentPage} of ${totalPages}</span>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/admin/user?action=${param.action}&page=${currentPage + 1}&query=${param.query}" class="btn btn-secondary">Next</a>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>