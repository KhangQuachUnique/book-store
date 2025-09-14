<%--
  Created by IntelliJ IDEA.
  User: kadfw
  Date: 9/14/2025
  Time: 11:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/categoryBook.css">
</head>
<body>
<div class="container">
    <div class="search-section">
        <h2>Browse Books by Category</h2>
        <form class="search-form" action="${pageContext.request.contextPath}/categories" method="GET">
            <input type="text" name="search" placeholder="Search by title or author..." class="search-input">
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
                <c:forEach items="${books}" var="book">
                    <div class="book-card">
                        <div class="book-image">
                            <img src="${book.thumbnailUrl}" alt="${book.title}" class="book-thumbnail">
                        </div>
                        <div class="book-info">
                            <h3 class="book-title">${book.title}</h3>
                            <p class="book-author">Author: ${book.author}</p>
                            <p class="book-publisher">Publisher: ${book.publisher}</p>
                            <div class="book-price-row">
                                <c:if test="${book.discount_rate > 0}">
                                    <span class="original-price">$${book.originalPrice}</span>
                                    <span class="discount">${book.discount_rate}% OFF</span>
                                </c:if>
                                <span class="book-price-badge">$${book.price}</span>
                            </div>
                            <div class="book-rating">
                                <span class="stars">
                                    <c:forEach begin="1" end="${book.rating}" var="i">
                                        <span class="star">&#9733;</span>
                                    </c:forEach>
                                    <c:forEach begin="1" end="${5 - book.rating}" var="i">
                                        <span class="star empty">&#9734;</span>
                                    </c:forEach>
                                </span>
                                <span class="rating-value">${book.rating} / 5</span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
