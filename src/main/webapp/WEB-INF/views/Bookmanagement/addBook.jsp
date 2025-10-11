<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add New Book</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/adminBook.css">
</head>
<body>
<div class="container">
    <h1 class="page-title">Add New Book</h1>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="card">
        <div class="card-content">
            <form action="${pageContext.request.contextPath}/admin/book" method="post">
                <input type="hidden" name="action" value="add">
                <div class="form-group">
                    <label for="title" style="font-weight: bold;">Title</label>
                    <input type="text" id="title" name="title" class="input" required>
                </div>
                <div class="form-group">
                    <label for="author" style="font-weight: bold;">Author</label>
                    <input type="text" id="author" name="author" class="input">
                </div>
                <div class="form-group">
                    <label for="publisher" style="font-weight: bold;">Publisher</label>
                    <input type="text" id="publisher" name="publisher" class="input">
                </div>
                <div class="form-group">
                    <label for="category_id" style="font-weight: bold;">Category</label>
                    <select id="category_id" name="category_id" class="input" required>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="stock" style="font-weight: bold;">Stock</label>
                    <input type="number" id="stock" name="stock" class="input" min="0" required>
                </div>
                <div class="form-group">
                    <label for="original_price" style="font-weight: bold;">Original Price</label>
                    <input type="number" id="original_price" name="original_price" class="input" step="0.01" min="0">
                </div>
                <div class="form-group">
                    <label for="discountRate" style="font-weight: bold;">Discount Rate (%)</label>
                    <input type="number" id="discountRate" name="discountRate" class="input" min="0" max="100">
                </div>
                <div class="form-group">
                    <label for="thumbnail_url" style="font-weight: bold;">Thumbnail URL</label>
                    <input type="text" id="thumbnail_url" name="thumbnail_url" class="input">
                </div>
                <div class="form-group">
                    <label for="description" style="font-weight: bold;">Description</label>
                    <textarea id="description" name="description" class="input"></textarea>
                </div>
                <div class="form-group">
                    <label for="publish_year" style="font-weight: bold;">Publish Year</label>
                    <input type="number" id="publish_year" name="publish_year" class="input">
                </div>
                <div class="form-group">
                    <label for="pages" style="font-weight: bold;">Pages</label>
                    <input type="number" id="pages" name="pages" class="input" min="1">
                </div>
                <div class="form-group">
                    <label for="averageRating" style="font-weight: bold;">Rating Average</label>
                    <input type="number" id="averageRating" name="averageRating" class="input" step="0.1" min="0" max="5">
                </div>
                <div class="form-group">
                    <label for="price" style="font-weight: bold;">Price</label>
                    <input type="number" id="price" name="price" class="input" step="0.01" min="0" required>
                </div>
                <div class="form-group">
                    <label for="sold" style="font-weight: bold;">Books Sold</label>
                    <input type="number" id="sold" name="sold" class="input" min="0" value="0">
                </div>
                <div class="form-group form-actions">
                    <input type="submit" value="Add Book" class="btn btn-primary">
                    <a href="${pageContext.request.contextPath}/admin/book?action=list" class="btn btn-secondary">Return</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>