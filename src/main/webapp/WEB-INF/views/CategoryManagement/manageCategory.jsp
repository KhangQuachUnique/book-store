<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Manage Categories</title>
    <link href="${pageContext.request.contextPath}/assets/styles/manageCategory.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container">
    <!-- Hiển thị thông báo nếu có -->
    <c:if test="${not empty message}">
        <div class="alert alert-info">${message}</div>
    </c:if>
    <h1 class="category-title">Manage Categories</h1>

    <!-- Nút thêm mới -->
    <a class="btn btn-primary add-category-btn" href="${pageContext.request.contextPath}/adminn/category?action=add">+ Add Category</a>

    <!-- Thanh tìm kiếm-->
    <div class="row g-2 mb-3">
        <div class="col-12 col-md-3">
            <form class="input-group" action="${pageContext.request.contextPath}/adminn/category" method="get">
                <input type="hidden" name="action" value="search">
                <input type="text" class="form-control rounded-2" name="id" placeholder="Search ID..." value="${param.id}">
            </form>
        </div>
        <div class="col-12 col-md-9">
            <form class="input-group" action="${pageContext.request.contextPath}/adminn/category" method="get">
                <input type="hidden" name="action" value="search">
                <input type="text" class="form-control rounded-2" name="keyword" placeholder="Search category..." value="${param.keyword}">
            </form>
        </div>
    </div>

    <!-- Bảng danh sách -->
    <table class="table table-bordered table-striped">
        <thead>
        <tr class="table-header">
            <th>ID</th>
            <th>Name</th>
            <th>Parent ID</th>
            <th>Created At</th>
            <th>Is Leaf</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="category" items="${currentPageCategories}">
            <tr>
                <td>${category.id}</td>
                <td>${category.name}</td>
                <td>${category.parentId}</td>
                <td>${category.createdAt}</td>
                <td>
                    <c:if test="${category.leaf}">
                        <svg class="icon-check" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.0.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M530.8 134.1C545.1 144.5 548.3 164.5 537.9 178.8L281.9 530.8C276.4 538.4 267.9 543.1 258.5 543.9C249.1 544.7 240 541.2 233.4 534.6L105.4 406.6C92.9 394.1 92.9 373.8 105.4 361.3C117.9 348.8 138.2 348.8 150.7 361.3L252.2 462.8L486.2 141.1C496.6 126.8 516.6 123.6 530.9 134z"/></svg>
                    </c:if>
                    <c:if test="${!category.leaf}">
                        <svg class="icon-cross" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.0.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M183.1 137.4C170.6 124.9 150.3 124.9 137.8 137.4C125.3 149.9 125.3 170.2 137.8 182.7L275.2 320L137.9 457.4C125.4 469.9 125.4 490.2 137.9 502.7C150.4 515.2 170.7 515.2 183.2 502.7L320.5 365.3L457.9 502.6C470.4 515.1 490.7 515.1 503.2 502.6C515.7 490.1 515.7 469.8 503.2 457.3L365.8 320L503.1 182.6C515.6 170.1 515.6 149.8 503.1 137.3C490.6 124.8 470.3 124.8 457.8 137.3L320.5 274.7L183.1 137.4z"/></svg>
                    </c:if>
                </td>
                <td>
                    <a class="btn btn-success btn-sm text-white"
                       href="${pageContext.request.contextPath}/adminn/category?action=edit&id=${category.id}">Edit</a>
                    <form action="${pageContext.request.contextPath}/adminn/category" method="post" class="d-inline" onsubmit="return confirm('Are you sure you want to delete this category?');">
                        <input type="hidden" name="action" value="delete"/>
                        <input type="hidden" name="id" value="${category.id}"/>
                        <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <!-- Phân trang -->
    <div class="pagination-container d-flex justify-content-center mt-4">
        <nav aria-label="Page navigation">
            <ul class="pagination">
                <!-- Nút Previous -->
                <c:if test="${currentPage > 1}">
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/adminn/category?action=list&page=${currentPage-1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                            <span class="sr-only">Previous</span>
                        </a>
                    </li>
                </c:if>
                
                <!-- Trang đầu tiên -->
                <c:if test="${showFirstPage}">
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/adminn/category?action=list&page=1">1</a>
                    </li>
                    <!-- Hiển thị dấu "..." nếu không kề với trang đầu tiên -->
                    <c:if test="${showEllipsisFirst}">
                        <li class="page-item disabled">
                            <span class="page-link">...</span>
                        </li>
                    </c:if>
                </c:if>
                
                <!-- Các trang xung quanh trang hiện tại -->
                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                    <c:choose>
                        <c:when test="${currentPage == i}">
                            <li class="page-item active"><a class="page-link" href="#">${i}</a></li>
                        </c:when>
                        <c:otherwise>
                            <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/adminn/category?action=list&page=${i}">${i}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                
                <!-- Trang cuối cùng -->
                <c:if test="${showLastPage}">
                    <!-- Hiển thị dấu "..." nếu không kề với trang cuối -->
                    <c:if test="${showEllipsisLast}">
                        <li class="page-item disabled">
                            <span class="page-link">...</span>
                        </li>
                    </c:if>
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/adminn/category?action=list&page=${totalPages}">${totalPages}</a>
                    </li>
                </c:if>
                
                <!-- Nút Next -->
                <c:if test="${currentPage < totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/adminn/category?action=list&page=${currentPage+1}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                            <span class="sr-only">Next</span>
                        </a>
                    </li>
                </c:if>
            </ul>
        </nav>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/filterCategory.js"></script>
</body>
</html>
