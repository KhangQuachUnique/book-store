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
                    <label for="title" style="font-weight: bold;" >Title</label>
                    <input type="text" id="title" name="title" value="${book.title}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="author" style="font-weight: bold;">Author</label>
                    <input type="text" id="author" name="author" value="${book.author}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="publisher" style="font-weight: bold;">Publisher</label>
                    <input type="text" id="publisher" name="publisher" value="${book.publisher}" class="input">
                </div>
                <div class="form-group">
                    <label for="category_id" style="font-weight: bold;">Category</label>
                    <select id="category_id" name="category_id" class="input" required>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.id}" ${category.id == book.category.id ? 'selected' : ''}>${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="stock" style="font-weight: bold;">Stock</label>
                    <input type="number" id="stock" name="stock" value="${book.stock}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="original_price" style="font-weight: bold;">Original Price</label>
                    <input type="number" id="original_price" name="original_price" step="0.01" value="${book.originalPrice}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="discountRate" style="font-weight: bold;">Discount Rate</label>
                    <input type="number" id="discountRate" name="discountRate" value="${book.discountRate}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="thumbnail_url" style="font-weight: bold;">Thumbnail URL</label>
                    <input type="text" id="thumbnail_url" name="thumbnail_url" value="${book.thumbnailUrl}" class="input">
                </div>
                <div class="form-group">
                    <label for="description" style="font-weight: bold;">Description</label>
                    <textarea id="description" name="description" class="input">${book.description}</textarea>
                </div>
                <div class="form-group">
                    <label for="publish_year" style="font-weight: bold;">Publish Year</label>
                    <input type="number" id="publish_year" name="publish_year" value="${book.publishYear}" class="input">
                </div>
                <div class="form-group">
                    <label for="pages" style="font-weight: bold;">Pages</label>
                    <input type="number" id="pages" name="pages" value="${book.pages}" class="input">
                </div>
                <div class="form-group">
                    <label for="averageRating" style="font-weight: bold;">Rating</label>
                    <input type="number" id="averageRating" name="averageRating" step="0.1" value="${book.averageRating}" class="input" required>
                </div>
                <div class="form-group">
                    <label for="price" style="font-weight: bold;">Price</label>
                    <input type="number" id="price" name="price" step="0.01" value="${book.price}" class="input" required>
                </div>
                <!-- Fixed: Added input field for sold -->
                <div class="form-group">
                    <label for="sold" style="font-weight: bold;">Books Sold</label>
                    <input type="number" id="sold" name="sold" value="${book.sold}" class="input" min="0" required>
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