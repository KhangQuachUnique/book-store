<%--
  Book Detail Page - Displays comprehensive information about a specific book
  Shows all book attributes including title, author, description, pricing, ratings, etc.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${book.title} - Book Detail</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/BookieCakeLogo.svg">
    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/bookDetail.css">
</head>
<body>
<!-- Sidebar -->
<%@include file="fragments/sidebar.jspf" %>

<div class="main-content">
    <!-- Header -->
    <%@ include file="fragments/header.jspf" %>

    <!-- Main -->
    <main>
<div class="book-detail-container">
            <!-- Breadcrumb Navigation -->
            <nav class="breadcrumb">
                <a href="${pageContext.request.contextPath}/">Home</a>
                <span class="separator">></span>
                <a href="${pageContext.request.contextPath}/categories">Books</a>
                <span class="separator">></span>
                <span class="current">${book.title}</span>
            </nav>

            <!-- Book Detail Content -->
            <div class="book-detail-content">
                <!-- Left Column: Book Image -->
                <div class="book-image-section">
                    <div class="book-image-container">
                        <c:choose>
                            <c:when test="${not empty book.thumbnailUrl}">
                                <img src="${book.thumbnailUrl}" alt="${book.title}" class="book-detail-image">
                            </c:when>
                            <c:otherwise>
                                <div class="no-image-placeholder">
                                    <span>No Image Available</span>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    
                    <!-- Quick Actions -->
                    <div class="quick-actions">
                        <c:if test="${inStock}">
                            <button class="btn btn-primary add-to-cart" data-book-id="${book.id}">
                                <i class="icon-cart"></i> Add to Cart
                            </button>
                        </c:if>
                        <button class="btn btn-secondary add-to-wishlist" data-book-id="${book.id}">
                            <i class="icon-heart"></i> Add to Wishlist
                        </button>
                    </div>
                </div>

                <!-- Right Column: Book Information -->
                <div class="book-info-section">
                    <!-- Title and Author -->
                    <div class="book-header">
                        <h1 class="book-title">${book.title}</h1>
                        <p class="book-author">by <span class="author-name">${book.author}</span></p>
                    </div>

                    <!-- Rating and Reviews -->
                    <div class="book-rating-section">
                        <div class="rating-display">
                            <div class="stars">
                                <c:forEach begin="1" end="5" var="i">
                                    <c:choose>
                                        <c:when test="${i <= book.ratingAverage}">
                                            <span class="star filled">â˜…</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="star empty">â˜†</span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                            <span class="rating-text">
                                <fmt:formatNumber value="${book.ratingAverage}" type="number" maxFractionDigits="1" /> out of 5
                            </span>
                            <c:if test="${book.reviewCount > 0}">
                                <span class="review-count">(${book.reviewCount} reviews)</span>
                            </c:if>
                        </div>
                    </div>

                    <!-- Pricing Information -->
                    <div class="pricing-section">
                        <div class="price-container">
                            <span class="current-price"><fmt:formatNumber value="${book.price}" type="number"/> VND</span>
                            
                            <c:if test="${hasDiscount}">
                                <span class="original-price"><fmt:formatNumber value="${book.originalPrice}" type="number"/> VND</span>
                                <span class="discount-badge">${book.discountRate}% OFF</span>
                                <div class="savings-text">
                                    You save: <fmt:formatNumber value="${savings}" type="number"/> VND
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <!-- Stock Status -->
                    <div class="stock-section">
                        <div class="stock-status ${inStock ? 'in-stock' : 'out-of-stock'}">
                            <span class="stock-icon"></span>
                            <span class="stock-text">${stockStatus}</span>
                        </div>
                    </div>

                    <!-- Book Details -->
                    <div class="book-details-section">
                        <h3>Book Details</h3>
                        <div class="details-grid">
                            <div class="detail-item">
                                <span class="detail-label">Publisher:</span>
                                <span class="detail-value">${book.publisher}</span>
                            </div>
                            
                            <c:if test="${not empty book.publishYear}">
                                <div class="detail-item">
                                    <span class="detail-label">Publication Year:</span>
                                    <span class="detail-value">${book.publishYear}</span>
                                </div>
                            </c:if>
                            
                            <c:if test="${not empty book.pages && book.pages > 0}">
                                <div class="detail-item">
                                    <span class="detail-label">Pages:</span>
                                    <span class="detail-value">${book.pages}</span>
                                </div>
                            </c:if>
                            
                            <div class="detail-item">
                                <span class="detail-label">Book ID:</span>
                                <span class="detail-value">#${book.id}</span>
                            </div>
                            
                            <c:if test="${not empty book.createdAt}">
                                <div class="detail-item">
                                    <span class="detail-label">Added to catalog:</span>
                                    <span class="detail-value">
                                        <fmt:formatDate value="${book.createdAt}" pattern="MMM dd, yyyy" />
                                    </span>
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <!-- Description -->
                    <c:if test="${not empty book.description}">
                        <div class="description-section">
                            <h3>Description</h3>
                            <div class="book-description">
                                <p>${book.description}</p>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Related Actions -->
            <div class="related-actions">
                <div class="action-buttons">
                    <button class="btn btn-outline" onclick="window.history.back()">
                        <i class="icon-arrow-left"></i> Back to Previous Page
                    </button>
                    <a href="${pageContext.request.contextPath}/categories" class="btn btn-outline">
                        <i class="icon-grid"></i> Browse More Books
                    </a>
                </div>
            </div>
        </div>

        <!-- JavaScript for Book Detail Page -->
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                // Add to Cart functionality
                const addToCartBtn = document.querySelector('.add-to-cart');
                if (addToCartBtn) {
                    addToCartBtn.addEventListener('click', function() {
                        const bookId = this.getAttribute('data-book-id');
                        // TODO: Implement add to cart functionality
                        alert('Add to cart functionality will be implemented soon!');
                    });
                }

                // Add to Wishlist functionality
                const addToWishlistBtn = document.querySelector('.add-to-wishlist');
                if (addToWishlistBtn) {
                    addToWishlistBtn.addEventListener('click', function() {
                        const bookId = this.getAttribute('data-book-id');
                        // TODO: Implement add to wishlist functionality
                        alert('Add to wishlist functionality will be implemented soon!');
                    });
                }
            });
        </script>
    </main>

    <!-- Footer -->
    <%@ include file="fragments/footer.jsp" %>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js" defer></script>
<script src="${pageContext.request.contextPath}/assets/js/toggleSidebar.js" defer></script>
</body>
</html>
