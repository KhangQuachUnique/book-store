<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Người Dùng - Địa Chỉ của ${editUser.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/addressList.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600&display=swap&subset=vietnamese" rel="stylesheet">

</head>
<body class="bg-background text-foreground">
<div class="container">
    <div class="card">
        <div class="card-header">
            <h1 class="card-title">Địa Chỉ của ${editUser.name} (ID: ${editUser.id})</h1>
        </div>
        <div class="card-content">
            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/admin/user?action=newAddress&id=${editUser.id}" class="btn btn-secondary">Thêm Địa Chỉ Mới</a>
                <a href="${pageContext.request.contextPath}/admin/user?action=view&id=${editUser.id}" class="btn btn-outline">Quay Lại Chi Tiết Người Dùng</a>
            </div>
            <c:choose>
                <c:when test="${empty addresses}">
                    <p class="no-data">Không tìm thấy địa chỉ nào cho người dùng này.</p>
                </c:when>
                <c:otherwise>
                    <table class="table">
                        <thead>
                        <tr class="table-header">
                            <th>ID</th>
                            <th>Địa Chỉ</th>
                            <th>Mặc Định</th>
                            <th>Ngày Tạo</th>
                            <th>Thao Tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="address" items="${addresses}">
                            <tr>
                                <td>${address.id}</td>
                                <td>${address.address}</td>
                                <td>${address.defaultAddress ? "Có" : "Không"}</td>
                                <td>${address.createdAt}</td>
                                <td class="action-buttons">
                                    <c:if test="${!address.defaultAddress}">
                                        <form action="${pageContext.request.contextPath}/admin/user" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="setDefault">
                                            <input type="hidden" name="addressId" value="${address.id}">
                                            <input type="hidden" name="userId" value="${editUser.id}">
                                            <input type="submit" value="Đặt Làm Mặc Định" class="btn btn-secondary btn-sm">
                                        </form>
                                        <form action="${pageContext.request.contextPath}/admin/user" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="deleteAddress">
                                            <input type="hidden" name="addressId" value="${address.id}">
                                            <input type="hidden" name="userId" value="${editUser.id}">
                                            <input type="submit" value="Xóa" class="btn btn-destructive btn-sm" onclick="return confirm('Bạn có chắc chắn không?');">
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>
