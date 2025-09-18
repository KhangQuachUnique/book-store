<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <title>Manage Categories</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            </head>

            <body class="bg-light">
                <div class="container py-4">
                    <!-- Hiển thị thông báo nếu có -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-info">${message}</div>
                    </c:if>
                    <h1 class="mb-4 text-primary">Manage Categories</h1>

                    <!-- Nút thêm mới -->
                    <a class="btn btn-primary mb-3"
                        href="${pageContext.request.contextPath}/admin/category?action=add">+ Add Category</a>

                    <!-- Thanh tìm kiếm-->
                    <div class="row g-2 mb-3">
                        <div class="col-12 col-md-3">
                            <form class="input-group" action="${pageContext.request.contextPath}/admin/category"
                                method="get">
                                <input type="hidden" name="action" value="search">
                                <input type="text" class="form-control" name="id" placeholder="Search ID..."
                                    value="${param.id}">
                            </form>
                        </div>
                        <div class="col-12 col-md-9">
                            <form class="input-group" action="${pageContext.request.contextPath}/admin/category"
                                method="get">
                                <input type="hidden" name="action" value="search">
                                <input type="text" class="form-control" name="keyword" placeholder="Search category..."
                                    value="${param.keyword}">
                            </form>
                        </div>
                    </div>

                    <!-- Bảng danh sách -->
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr style="--bs-table-bg: #800020; --bs-table-color: #fff;">
                                <th>ID</th>
                                <th>Name</th>
                                <th>Parent ID</th>
                                <th>Created At</th>
                                <th>Is Leaf</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="category" items="${categories}">
                                <tr>
                                    <td>${category.id}</td>
                                    <td>${category.name}</td>
                                    <td>${category.parentId}</td>
                                    <td>${category.createdAt}</td>
                                    <td>
                                        <c:if test="${category.isLeaf}">✅</c:if>
                                        <c:if test="${!category.isLeaf}">❌</c:if>
                                    </td>
                                    <td>
                                        <a class="btn btn-success btn-sm text-white"
                                            href="${pageContext.request.contextPath}/admin/category?action=edit&id=${category.id}">Edit</a>
                                        <form action="${pageContext.request.contextPath}/admin/category" method="post"
                                            class="d-inline"
                                            onsubmit="return confirm('Are you sure you want to delete this category?');">
                                            <input type="hidden" name="action" value="delete" />
                                            <input type="hidden" name="id" value="${category.id}" />
                                            <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <script src="${pageContext.request.contextPath}/assets/js/filterCategory.js"></script>
            </body>

        </html>
