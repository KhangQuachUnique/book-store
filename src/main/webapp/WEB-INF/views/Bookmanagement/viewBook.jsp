<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>View Book</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/adminBook.css">
</head>
<body>
<div class="container">
    <h1 class="page-title">Book Details</h1>

    <div class="card">
        <div class="card-content">
            <p><strong>ID:</strong> ${book.id}</p>
            <p><strong>Title:</strong> ${book.title}</p>
            <p><strong>Author:</strong> ${book.author}</p>
            <p><strong>Publisher:</strong> ${book.publisher}</p>
            <p><strong>Category:</strong>
                <c:forEach var="category" items="${categories}">
                    <c:if test="${category.id == book.categoryId}">${category.name}</c:if>
                </c:forEach>
            </p>
            <p><strong>Stock:</strong> ${book.stock}</p>
            <p><strong>Original Price:</strong> ${book.originalPrice}</p>
            <p><strong>Discount Rate:</strong> ${book.discount_rate}%</p>
            <p><strong>Price:</strong> ${book.price}</p>
            <p><strong>Thumbnail URL:</strong> ${book.thumbnailUrl}</p>
            <p><strong>Description:</strong> ${book.description}</p>
            <p><strong>Publish Year:</strong> ${book.publishYear}</p>
            <p><strong>Pages:</strong> ${book.pages}</p>
            <p><strong>Rating Average:</strong> ${book.rating}</p>
            <p><strong>Created At:</strong> ${book.createdAt}</p>
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/admin/book?action=edit&id=${book.id}" class="btn btn-primary">Edit</a>
                <a href="${pageContext.request.contextPath}/admin/book?action=list" class="btn btn-secondary">Back to List</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>