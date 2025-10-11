<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Management - View Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/viewUser.css">
    <style>
    </style>
    <script>
        function toggleEditAddress(addressId) {
            const form = document.getElementById('edit-address-' + addressId);
            form.classList.toggle('active');
        }
    </script>
</head>
<body class="bg-background text-foreground">
<div class="container">
    <div class="card" style="max-width: 810px;">
        <div class="card-header">
            <h1 class="card-title">User Details</h1>
        </div>
        <div class="card-content" style="width: 810px;">
            <c:if test="${not empty errorMessage}">
                <p class="error">${errorMessage}</p>
            </c:if>
            <c:choose>
                <c:when test="${empty user}">
                    <p class="error">No user data available.</p>
                    <a href="${pageContext.request.contextPath}/admin/user?action=list" class="btn btn-outline">Back to List</a>
                </c:when>
                <c:otherwise>
                    <form action="${pageContext.request.contextPath}/admin/user" method="post" class="form">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="${user.id}">
                        <div class="info-item">
                            <strong>ID:</strong> ${user.id}
                        </div>
                        <div class="info-item">
                            <strong>Name:</strong>
                            <input type="text" name="name" value="${user.name}" class="input" required>
                        </div>
                        <div class="info-item">
                            <strong>Email:</strong>
                            <input type="email" name="email" value="${user.email}" class="input" required>
                        </div>
                        <div class="info-item">
                            <strong>Phone Number:</strong>
                            <input type="text" name="phone" value="${user.phoneNumber}" class="input" required>
                        </div>
                        <div class="info-item">
                            <strong>Role:</strong>
                            <select name="role" class="select">
                                <option value="USER" ${user.role.toString() eq 'USER' ? 'selected' : ''}>Customer</option>
                                <option value="ADMIN" ${user.role.toString() eq 'ADMIN' ? 'selected' : ''}>Administrator</option>
                            </select>
                        </div>
                        <div class="info-item">
                            <strong>Blocked:</strong> ${user.isBlocked ? "Yes" : "No"}
                        </div>
                        <div class="info-item">
                            <strong>Blocked Until:</strong> ${user.blockedUntil != null ? user.blockedUntil : "N/A"}
                        </div>
                        <div class="action-buttons">
                            <input type="submit" value="Save Changes" class="btn btn-primary">
                            <a href="${pageContext.request.contextPath}/admin/user?action=list" class="btn btn-outline">Back to List</a>
                            <a href="${pageContext.request.contextPath}/admin/user?action=newAddress&id=${user.id}" class="btn btn-secondary">Add New Address</a>
                        </div>
                    </form>

                    <h2>Addresses</h2>
                    <c:choose>
                        <c:when test="${empty addresses}">
                            <p class="no-data">No addresses found for this user.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table">
                                <thead>
                                <tr class="table-header">
                                    <th>ID</th>
                                    <th>Address</th>
                                    <th>Default</th>
                                    <th>Created At</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="address" items="${addresses}">
                                    <tr>
                                        <td>${address.id}</td>
                                        <td>${address.address}</td>
                                        <td>${address.defaultAddress ? "Yes" : "No"}</td>
                                        <td>${address.createdAt}</td>
                                        <td class="action-buttons">
                                            <c:if test="${!address.defaultAddress}">
                                                <form action="${pageContext.request.contextPath}/admin/user" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="setDefault">
                                                    <input type="hidden" name="addressId" value="${address.id}">
                                                    <input type="hidden" name="userId" value="${user.id}">
                                                    <input type="submit" value="Set as Default" class="btn btn-secondary btn-sm">
                                                </form>
                                                <form action="${pageContext.request.contextPath}/admin/user" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="deleteAddress">
                                                    <input type="hidden" name="addressId" value="${address.id}">
                                                    <input type="hidden" name="userId" value="${user.id}">
                                                    <input type="submit" value="Delete" class="btn btn-destructive btn-sm" onclick="return confirm('Are you sure you want to delete?');">
                                                </form>
                                            </c:if>
                                            <button onclick="toggleEditAddress(${address.id})" class="btn btn-outline btn-sm">Edit</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="5">
                                            <form id="edit-address-${address.id}" class="edit-address-form" action="${pageContext.request.contextPath}/admin/user" method="post">
                                                <input type="hidden" name="action" value="updateAddress">
                                                <input type="hidden" name="addressId" value="${address.id}">
                                                <input type="hidden" name="userId" value="${user.id}">
                                                <div class="form-group">
                                                    <label for="address-${address.id}">Address:</label>
                                                    <textarea id="address-${address.id}" name="address" rows="4" class="textarea" required>${address.address}</textarea>
                                                </div>
                                                <div class="checkbox-group">
                                                    <input type="checkbox" id="isDefaultAddress-${address.id}" name="isDefaultAddress" value="true" ${address.defaultAddress ? 'checked' : ''}>
                                                    <label for="isDefaultAddress-${address.id}" class="checkbox-label">Set as Default</label>
                                                </div>
                                                <div class="action-buttons">
                                                    <input type="submit" value="Save Address" class="btn btn-primary btn-sm">
                                                    <button type="button" onclick="toggleEditAddress(${address.id})" class="btn btn-outline btn-sm">Cancel</button>
                                                </div>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>