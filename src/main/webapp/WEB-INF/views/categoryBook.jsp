<%-- Created by IntelliJ IDEA. User: kadfw Date: 9/14/2025 Time: 11:36 PM To change this template use File | Settings |
    File Templates. --%>
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
                <h2>Browse Books by Category</h2>
                <form class="search-form" action="${pageContext.request.contextPath}/categories"
                    method="GET">
                    <input type="text" name="search" placeholder="Search by title or author..."
                        class="search-input">
                    <select name="categoryId" class="category-select">
                        <option value="">All Categories</option>
                        <option value="1">Fiction</option>
                        <option value="2">Non-Fiction</option>
                        <option value="3">Science</option>
                        <option value="4">Technology</option>
                        <option value="5">Business</option>
                        <!-- More categories can be dynamically loaded from database -->
                    </select>
                    <button type="submit" class="search-button">Search</button>
                </form>
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

            <!-- Pagination -->
            <div class="pagination">
                <!-- Previous button -->
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a
                            href="?page=${currentPage - 1}${not empty categoryId ? '&category='.concat(categoryId) : ''}">&lt;</a>
                    </c:when>
                    <c:otherwise>
                        <span class="disabled">&lt;</span>
                    </c:otherwise>
                </c:choose>

                <!-- First page -->
                <c:if test="${showFirstEllipsis}">
                    <a
                        href="?page=1${not empty categoryId ? '&category='.concat(categoryId) : ''}">1</a>
                    <span class="ellipsis">...</span>
                </c:if>

                <!-- Visible pages -->
                <c:forEach items="${visiblePages}" var="pageNum">
                    <c:choose>
                        <c:when test="${pageNum == currentPage}">
                            <span class="active">${pageNum}</span>
                        </c:when>
                        <c:otherwise>
                            <a
                                href="?page=${pageNum}${not empty categoryId ? '&category='.concat(categoryId) : ''}">${pageNum}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <!-- Last page -->
                <c:if test="${showLastEllipsis}">
                    <span class="ellipsis">...</span>
                    <a
                        href="?page=${totalPages}${not empty categoryId ? '&category='.concat(categoryId) : ''}">${totalPages}</a>
                </c:if>

                <!-- Next button -->
                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a
                            href="?page=${currentPage + 1}${not empty categoryId ? '&category='.concat(categoryId) : ''}">&gt;</a>
                    </c:when>
                    <c:otherwise>
                        <span class="disabled">&gt;</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </body>

</html>
