<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Addresses for User ${user.name}</title>
</head>
<body>
<h1>Addresses for ${user.name} (ID: ${user.id})</h1>
<a href="/admin/user?action=newAddress&id=${user.id}">Add New Address</a><br><br>
<a href="/admin/user?action=view&id=${user.id}">Back to User Details</a><br><br>

<c:choose>
    <c:when test="${empty addresses}">
        <p>No addresses found for this user.</p>
    </c:when>
    <c:otherwise>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Address</th>
                <th>Default</th>
                <th>Created At</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="address" items="${addresses}">
                <tr>
                    <td>${address.id}</td>
                    <td>${address.address}</td>
                    <td>${address.defaultAddress}</td>
                    <td>${address.createdAt}</td>

                    <td>
                        <c:if test="${!address.defaultAddress}">
                            <form action="/admin/user" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="setDefault">
                                <input type="hidden" name="addressId" value="${address.id}">
                                <input type="hidden" name="userId" value="${user.id}">
                                <input type="submit" value="Set Default">
                            </form>
                            <form action="/admin/user" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="deleteAddress">
                                <input type="hidden" name="addressId" value="${address.id}">
                                <input type="hidden" name="userId" value="${user.id}">
                                <input type="submit" value="Delete" onclick="return confirm('Are you sure?');">
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>