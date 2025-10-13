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
</head>
<body>
<div class="container">
    <div class="search-section">
        <div class="search-card">
            <div class="card-header">
                <h2 class="card-title">Browse Books by Category</h2>
            </div>
            <div class="card-content">
                <!-- Thanh tìm kiếm - cấu trúc hiện đại -->
                <form action="${pageContext.request.contextPath}/categories" method="get" class="search-form">
                    <div class="form-group">
                        <input type="text" name="search" placeholder="Search by title or author" value="${search}" class="input">
                    </div>
                    <div class="form-group button-group">
                        <button type="button" class="btn btn-primary" onclick="toggleCategoryTable()">Select Categories</button>
                        <input type="submit" value="Find" class="btn btn-primary" onclick="setSearchAction('title')">
                    </div>
                    <input type="hidden" name="includeCategories" id="includeCategories" value="${includeCategories}">
                    <input type="hidden" name="excludeCategories" id="excludeCategories" value="${excludeCategories}">
                    <input type="hidden" name="action" id="searchAction" value="">
                </form>
            </div>
        </div>

        <!-- Bảng category - chỉ hiển thị khi cần -->
        <div id="categoryTable" class="category-table">
            <div style="margin-bottom: 10px;">
                <strong>Select Categories:</strong>
                <button type="button" class="btn btn-primary" onclick="submitFilterForm()">Apply Filter</button>
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
                                                <fmt:formatNumber value="${book.getPrice()}" type="number" />
                                                VND
                                            </span>
                                            <c:if test="${book.discountRate > 0}">
                                                <span class="discount">-${book.discountRate}%</span>
                                            </c:if>
                                        </div>
                                        <p class="book-author">Author: ${book.author}</p>
                                        <p class="book-publisher">Publisher: ${book.publisher}</p>

<%--                                        <div class="book-rating">--%>
<%--                                            <jsp:include page="ratingStar.jsp">--%>
<%--                                                <jsp:param name="fullStars" value="${book.fullStars}" />--%>
<%--                                                <jsp:param name="partialFraction" value="${book.partialFraction}" />--%>
<%--                                                <jsp:param name="emptyStars" value="${book.emptyStars}" />--%>
<%--                                                <jsp:param name="size" value="16" />--%>
<%--                                            </jsp:include>--%>
<%--                                            <span class="rating-value">${book.rating} / 5</span>--%>
<%--                                        </div>--%>

                                    </div>
                                </a>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

    <!-- Pagination -->
    <div class="pagination">
        <c:choose>
            <c:when test="${currentPage > 1}">
                <a href="?page=${currentPage - 1}&search=${search}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}${not empty categoryId ? '&category='.concat(categoryId) : ''}">&lt;</a>
            </c:when>
            <c:otherwise>
                <span class="disabled">&lt;</span>
            </c:otherwise>
        </c:choose>

        <c:if test="${showFirstEllipsis}">
            <a href="?page=1&search=${search}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}${not empty categoryId ? '&category='.concat(categoryId) : ''}">1</a>
            <span class="ellipsis">...</span>
        </c:if>

        <c:forEach items="${visiblePages}" var="pageNum">
            <c:choose>
                <c:when test="${pageNum == currentPage}">
                    <span class="active">${pageNum}</span>
                </c:when>
                <c:otherwise>
                    <a href="?page=${pageNum}&search=${search}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}${not empty categoryId ? '&category='.concat(categoryId) : ''}">${pageNum}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${showLastEllipsis}">
            <span class="ellipsis">...</span>
            <a href="?page=${totalPages}&search=${search}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}${not empty categoryId ? '&category='.concat(categoryId) : ''}">${totalPages}</a>
        </c:if>

        <c:choose>
            <c:when test="${currentPage < totalPages}">
                <a href="?page=${currentPage + 1}&search=${search}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}${not empty categoryId ? '&category='.concat(categoryId) : ''}">&gt;</a>
            </c:when>
            <c:otherwise>
                <span class="disabled">&gt;</span>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Load JavaScript file and initialize variables -->
<script>
    // Pass JSP variables to JavaScript
    var includeCategories_jsp = '${includeCategories}';
    var excludeCategories_jsp = '${excludeCategories}';
</script>
<script src="${pageContext.request.contextPath}/assets/js/categoryBook.js"></script>
</body>
</html>
