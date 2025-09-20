<%--
  Created by IntelliJ IDEA.
  User: kadfw
  Date: 9/14/2025
  Time: 11:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/categoryBook.css">
    <!-- Chỉ thêm CSS tối thiểu cho category table -->
    <style>
        .category-table {
            display: none;
            position: fixed;
            top: 20%;
            left: 10%;
            right: 10%;
            background: white;
            border: 1px solid #ccc;
            padding: 15px;
            z-index: 1000;
            max-height: 60vh;
            overflow-y: auto;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .category-table.show { display: block; }
        .category-item {
            padding: 8px;
            cursor: pointer;
            margin: 3px;
            display: inline-block;
            border: 1px solid #ddd;
            background: #f9f9f9;
            border-radius: 3px;
        }
        .category-item.include { border-color: #28a745; background: #d4edda; }
        .category-item.exclude { border-color: #dc3545; background: #f8d7da; }
    </style>
</head>
<body>
<div class="container">
    <div class="search-section">
        <h2>Browse Books by Category</h2>
        <!-- Thanh tìm kiếm - giữ nguyên cấu trúc ban đầu -->
        <form action="${pageContext.request.contextPath}/categories" method="get" class="search-form">
            <input type="text" name="title" placeholder="Search by title" value="${title}">
            <input type="number" name="publish_year" placeholder="Publish year" value="${publishYear}">
            <button type="button" class="btn btn-primary" onclick="toggleCategoryTable()">Select Categories</button>
            <input type="submit" value="Find" class="btn btn-primary">
            <input type="hidden" name="includeCategories" id="includeCategories" value="${includeCategories}">
            <input type="hidden" name="excludeCategories" id="excludeCategories" value="${excludeCategories}">
        </form>

        <!-- Bảng category - chỉ hiển thị khi cần -->
        <div id="categoryTable" class="category-table">
            <div style="margin-bottom: 10px;">
                <strong>Select Categories:</strong>
                <button type="button" class="btn btn-success" onclick="submitFilterForm()">Apply Filter</button>
                <button type="button" class="btn btn-secondary" onclick="toggleCategoryTable()">Close</button>
            </div>
            <c:forEach var="category" items="${categories}">
                <div class="category-item" data-id="${category.id}" onclick="toggleCategory(this, ${category.id})">
                    ${category.name}
                </div>
            </c:forEach>
        </div>
    </div>

            <div class="book-list">
                <c:choose>
                    <c:when test="${empty requestScope.books}">
                        <div class="no-results">
                            <h3>No books found</h3>
                            <p>Try different search criteria or browse all categories</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${requestScope.books}" var="book">
                            <div class="book-card">
                                <a href="${pageContext.request.contextPath}/book-detail?id=${book.id}"
                                    class="book-link">
                                    <div class="book-image">
                                        <img src="${book.thumbnailUrl}" alt="${book.title}"
                                            class="book-thumbnail">
                                    </div>
                                    <div class="book-info">
                                        <h3 class="book-title">${book.title}</h3>
                                        <div class="book-price-row">
                                            <span class="book-price-badge">
                                                <fmt:formatNumber value="${book.price}" type="number" />
                                                VND
                                            </span>
                                            <c:if test="${book.discount_rate > 0}">
                                                <span class="discount">-${book.discount_rate}%</span>
                                            </c:if>
                                        </div>
                                        <p class="book-author">Author: ${book.author}</p>
                                        <p class="book-publisher">Publisher: ${book.publisher}</p>

                                        <div class="book-rating">
                                            <jsp:include page="ratingStar.jsp">
                                                <jsp:param name="fullStars" value="${book.fullStars}" />
                                                <jsp:param name="partialFraction" value="${book.partialFraction}" />
                                                <jsp:param name="emptyStars" value="${book.emptyStars}" />
                                                <jsp:param name="size" value="16" />
                                            </jsp:include>
                                            <span class="rating-value">${book.rating} / 5</span>
                                        </div>
                                    </div>
                                </a>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

    <!-- Giữ nguyên phần pagination nhưng sửa links để bảo toàn filter -->
    <div class="pagination">
        <c:choose>
            <c:when test="${currentPage > 1}">
                <a href="?page=${currentPage - 1}&title=${title}&publish_year=${publishYear}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}${not empty categoryId ? '&category='.concat(categoryId) : ''}">&lt;</a>
            </c:when>
            <c:otherwise>
                <span class="disabled">&lt;</span>
            </c:otherwise>
        </c:choose>

        <c:if test="${showFirstEllipsis}">
            <a href="?page=1&title=${title}&publish_year=${publishYear}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}${not empty categoryId ? '&category='.concat(categoryId) : ''}">1</a>
            <span class="ellipsis">...</span>
        </c:if>

        <c:forEach items="${visiblePages}" var="pageNum">
            <c:choose>
                <c:when test="${pageNum == currentPage}">
                    <span class="active">${pageNum}</span>
                </c:when>
                <c:otherwise>
                    <a href="?page=${pageNum}&title=${title}&publish_year=${publishYear}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}${not empty categoryId ? '&category='.concat(categoryId) : ''}">${pageNum}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${showLastEllipsis}">
            <span class="ellipsis">...</span>
            <a href="?page=${totalPages}&title=${title}&publish_year=${publishYear}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}${not empty categoryId ? '&category='.concat(categoryId) : ''}">${totalPages}</a>
        </c:if>

        <c:choose>
            <c:when test="${currentPage < totalPages}">
                <a href="?page=${currentPage + 1}&title=${title}&publish_year=${publishYear}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}${not empty categoryId ? '&category='.concat(categoryId) : ''}">&gt;</a>
            </c:when>
            <c:otherwise>
                <span class="disabled">&gt;</span>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Chỉ thêm JavaScript tối thiểu cho chức năng Select Categories -->
<script>
let includeCategories = [];
let excludeCategories = [];

// Khởi tạo từ giá trị có sẵn nếu có
if ('${includeCategories}' && '${includeCategories}' !== '') {
    includeCategories = '${includeCategories}'.split(',').map(id => parseInt(id));
}
if ('${excludeCategories}' && '${excludeCategories}' !== '') {
    excludeCategories = '${excludeCategories}'.split(',').map(id => parseInt(id));
}

function toggleCategoryTable() {
    const table = document.getElementById('categoryTable');
    table.classList.toggle('show');
}

function toggleCategory(element, categoryId) {
    if (element.classList.contains('include')) {
        element.classList.remove('include');
        element.classList.add('exclude');
        includeCategories = includeCategories.filter(id => id !== categoryId);
        if (!excludeCategories.includes(categoryId)) {
            excludeCategories.push(categoryId);
        }
    } else if (element.classList.contains('exclude')) {
        element.classList.remove('exclude');
        excludeCategories = excludeCategories.filter(id => id !== categoryId);
    } else {
        element.classList.add('include');
        if (!includeCategories.includes(categoryId)) {
            includeCategories.push(categoryId);
        }
    }
}

function submitFilterForm() {
    document.getElementById('includeCategories').value = includeCategories.join(',');
    document.getElementById('excludeCategories').value = excludeCategories.join(',');
    document.querySelector('.search-form').submit();
}
</script>
</body>
</html>
