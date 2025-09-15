<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Edit Category</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css" />
</head>
<body class="bg-light">
<div class="container py-4">
    <h1 class="mb-4 text-primary">Edit Category</h1>

    <form action="${pageContext.request.contextPath}/admin/category" method="post" class="card p-4 shadow-sm">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${category.id}">

        <div class="mb-3">
            <label for="edit-category-select" class="form-label fw-bold">Category Name</label>
            <select class="form-select" name="name" id="edit-category-select">
                <%-- Dùng JSTL để lặp qua danh sách categories và tạo các option --%>
                <c:forEach var="cat" items="${allCategories}">
                    <option value="${cat.name}" <c:if test="${cat.name == category.name}">selected</c:if>>
                        <c:out value="${cat.name}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Parent ID (optional)</label>
            <input type="number" class="form-control" name="parent_id" value="${category.parentId}">
        </div>

        <div class="mb-3">
            <label class="form-label">Created At</label>
            <input type="text" class="form-control" value="${category.createdAt}" readonly>
        </div>

        <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" name="is_leaf" value="true"
                   id="leafCheck" <c:if test="${category.isLeaf}">checked</c:if>>
            <label class="form-check-label" for="leafCheck">Is Leaf</label>
        </div>

        <button type="submit" class="btn btn-primary">Update Category</button>
        <a href="${pageContext.request.contextPath}/admin/category?action=list" class="btn btn-secondary mt-2">Cancel</a>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

<script>
    $(document).ready(function() {
        $('#edit-category-select').select2({
            theme: "bootstrap-5", // Giao diện khớp với Bootstrap 5
            placeholder: "Chọn hoặc nhập tên category mới",
            tags: true // Cho phép người dùng nhập một giá trị mới không có trong danh sách
        });
    });
</script>
</body>
</html>