<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Add Category</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
    <h1 class="mb-4 text-primary">Add New Category</h1>

    <form action="${pageContext.request.contextPath}/admin/category" method="post" class="card p-4 shadow-sm">
        <input type="hidden" name="action" value="create">

        <div class="mb-3">
            <label class="form-label">Category Name</label>
            <input type="text" class="form-control" name="name" required>
            </div>

        <div class="mb-3">
            <label class="form-label">Parent ID (optional)</label>
            <input type="number" class="form-control" name="parent_id">
        </div>

        <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" name="is_leaf" value="true" id="leafCheck">
            <label class="form-check-label" for="leafCheck">Is Leaf</label>
        </div>

        <button type="submit" class="btn btn-primary">Save Category</button>
        <a href="${pageContext.request.contextPath}/admin/category?action=list" class="btn btn-secondary mt-2">Cancel</a>
    </form>
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>

<script src="${pageContext.request.contextPath}/assets/js/filterCategory.js"></script>

</body>
</html>