<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Book</title>
    <link rel="stylesheet" href="/assets/styles/users.css">
</head>
<body class="bg-background text-foreground">
<div class="container">
    <h1 class="page-title">Edit Book</h1>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="card">
        <div class="card-content">
            <form action="${pageContext.request.contextPath}/admin/book" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${book.id}">
                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" id="title" name="title" class="input" value="${book.title}" required>
                </div>
                <div class="form-group">
                    <label for="author">Author</label>
                    <input type="text" id="author" name="author" class="input" value="${book.author}">
                </div>
                <div class="form-group">
                    <label for="publisher">Publisher</label>
                    <input type="text" id="publisher" name="publisher" class="input" value="${book.publisher}">
                </div>
                <div class="form-group">
                    <label for="category_id">Category</label>
                    <select id="category_id" name="category_id" class="input" required>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.id}" ${category.id == book.categoryId ? 'selected' : ''}>
                                    ${category.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="stock">Stock</label>
                    <input type="number" id="stock" name="stock" class="input" value="${book.stock}" min="0" required>
                </div>
                <div class="form-group">
                    <label for="original_price">Original Price</label>
                    <input type="number" id="original_price" name="original_price" class="input" value="${book.originalPrice}" step="0.01" min="0">
                </div>
                <div class="form-group">
                    <label for="discount_rate">Discount Rate (%)</label>
                    <input type="number" id="discount_rate" name="discount_rate" class="input" value="${book.discount_rate}" min="0" max="100">
                </div>
                <div class="form-group">
                    <label for="thumbnail_url">Thumbnail URL</label>
                    <input type="text" id="thumbnail_url" name="thumbnail_url" class="input" value="${book.thumbnailUrl}">
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" class="input">${book.description}</textarea>
                </div>
                <div class="form-group">
                    <label for="publish_year">Publish Year</label>
                    <input type="number" id="publish_year" name="publish_year" class="input" value="${book.publishYear}">
                </div>
                <div class="form-group">
                    <label for="pages">Pages</label>
                    <input type="number" id="pages" name="pages" class="input" value="${book.pages}" min="1">
                </div>
                <div class="form-group">
                    <label for="rating_average">Rating Average</label>
                    <input type="number" id="rating_average" name="rating_average" class="input" value="${book.rating}" step="0.1" min="0" max="5">
                </div>
                <div class="form-group">
                    <label for="price">Price</label>
                    <input type="number" id="price" name="price" class="input" value="${book.price}" step="0.01" min="0" required>
                </div>
                <input type="submit" value="Update Book" class="btn btn-primary">
            </form>
        </div>
    </div>
</div>
</body>
</html>