<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Book</title>
    <style>
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: inline-block;
            width: 150px;
        }
        .form-group input, .form-group select, .form-group textarea {
            width: 300px;
            padding: 5px;
        }
        .btn {
            padding: 5px 10px;
            margin: 5px;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
            border: none;
            cursor: pointer;
        }
    </style>
</head>
<body>
<h1>Edit Book</h1>
<form action="${pageContext.request.contextPath}/admin/book" method="post">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="${book.id}">
    <div class="form-group">
        <label for="title">Title</label>
        <input type="text" id="title" name="title" value="${book.title}" required>
    </div>
    <div class="form-group">
        <label for="author">Author</label>
        <input type="text" id="author" name="author" value="${book.author}" required>
    </div>
    <div class="form-group">
        <label for="publisher">Publisher</label>
        <input type="text" id="publisher" name="publisher" value="${book.publisher}">
    </div>
    <div class="form-group">
        <label for="category_id">Category</label>
        <select id="category_id" name="category_id" required>
            <c:forEach var="category" items="${categories}">
                <option value="${category.id}" ${category.id == book.categoryId ? 'selected' : ''}>${category.name}</option>
            </c:forEach>
        </select>
    </div>
    <div class="form-group">
        <label for="stock">Stock</label>
        <input type="number" id="stock" name="stock" value="${book.stock}" required>
    </div>
    <div class="form-group">
        <label for="original_price">Original Price</label>
        <input type="number" id="original_price" name="original_price" step="0.01" value="${book.originalPrice}" required>
    </div>
    <div class="form-group">
        <label for="discount_rate">Discount Rate</label>
        <input type="number" id="discount_rate" name="discount_rate" value="${book.discount_rate}" required>
    </div>
    <div class="form-group">
        <label for="thumbnail_url">Thumbnail URL</label>
        <input type="text" id="thumbnail_url" name="thumbnail_url" value="${book.thumbnailUrl}">
    </div>
    <div class="form-group">
        <label for="description">Description</label>
        <textarea id="description" name="description">${book.description}</textarea>
    </div>
    <div class="form-group">
        <label for="publish_year">Publish Year</label>
        <input type="number" id="publish_year" name="publish_year" value="${book.publishYear}">
    </div>
    <div class="form-group">
        <label for="pages">Pages</label>
        <input type="number" id="pages" name="pages" value="${book.pages}">
    </div>
    <div class="form-group">
        <label for="rating_average">Rating</label>
        <input type="number" id="rating_average" name="rating_average" step="0.1" value="${book.rating}" required>
    </div>
    <div class="form-group">
        <label for="price">Price</label>
        <input type="number" id="price" name="price" step="0.01" value="${book.price}" required>
    </div>
    <div class="form-group">
        <input type="submit" value="Update Book" class="btn btn-primary">
        <a href="${pageContext.request.contextPath}/admin/book?action=list" class="btn btn-secondary">Return</a>
    </div>
</form>
</body>
</html>