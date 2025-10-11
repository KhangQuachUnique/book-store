<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Book</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/adminBook.css">
</head>
<body>
<div class="container">
    <h1 class="page-title">Edit Book</h1>
    <div class="card">
        <div class="card-content">
            <form action="${pageContext.request.contextPath}/admin/book" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${book.id}">
                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" id="title" name="title" value="${book.title}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="author">Author</label>
                    <input type="text" id="author" name="author" value="${book.author}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="publisher">Publisher</label>
                    <input type="text" id="publisher" name="publisher" value="${book.publisher}" class="input">
                </div>
                <div class="form-group">
                    <label for="category_id">Category</label>
                    <select id="category_id" name="category_id" class="input" required>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.id}" ${category.id == book.categoryId ? 'selected' : ''}>${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="stock">Stock</label>
                    <input type="number" id="stock" name="stock" value="${book.stock}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="original_price">Original Price</label>
                    <input type="number" id="original_price" name="original_price" step="0.01" value="${book.originalPrice}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="discount_rate">Discount Rate</label>
                    <input type="number" id="discount_rate" name="discount_rate" value="${book.discountRate}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="thumbnail_url">Thumbnail URL</label>
                    <input type="text" id="thumbnail_url" name="thumbnail_url" value="${book.thumbnailUrl}" class="input">
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" class="input">${book.description}</textarea>
                </div>
                <div class="form-group">
                    <label for="publish_year">Publish Year</label>
                    <input type="number" id="publish_year" name="publish_year" value="${book.publishYear}" class="input">
                </div>
                <div class="form-group">
                    <label for="pages">Pages</label>
                    <input type="number" id="pages" name="pages" value="${book.pages}" class="input">
                </div>
                <div class="form-group">
                    <label for="rating_average">Rating</label>
                    <input type="number" id="rating_average" name="rating_average" step="0.1" value="${book.averageRating}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="price">Price</label>
                    <input type="number" id="price" name="price" step="0.01" value="${book.price}" class="input" required>
                </div>
                <div class="form-group">
                    <input type="submit" value="Update Book" class="btn btn-primary">
                    <a href="${pageContext.request.contextPath}/admin/book?action=list" class="btn btn-secondary">Return</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>