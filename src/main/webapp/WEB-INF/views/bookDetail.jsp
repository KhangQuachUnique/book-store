<%-- Book Detail Page - Displays comprehensive information about a specific book Shows all book attributes including
    title, author, description, pricing, ratings, etc. --%>
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
                        <img src="${book.thumbnailUrl}" alt="${book.title}"
                             class="book-detail-image">
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
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.0.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M24 48C10.7 48 0 58.7 0 72C0 85.3 10.7 96 24 96L69.3 96C73.2 96 76.5 98.8 77.2 102.6L129.3 388.9C135.5 423.1 165.3 448 200.1 448L456 448C469.3 448 480 437.3 480 424C480 410.7 469.3 400 456 400L200.1 400C188.5 400 178.6 391.7 176.5 380.3L171.4 352L475 352C505.8 352 532.2 330.1 537.9 299.8L568.9 133.9C572.6 114.2 557.5 96 537.4 96L124.7 96L124.3 94C119.5 67.4 96.3 48 69.2 48L24 48zM208 576C234.5 576 256 554.5 256 528C256 501.5 234.5 480 208 480C181.5 480 160 501.5 160 528C160 554.5 181.5 576 208 576zM432 576C458.5 576 480 554.5 480 528C480 501.5 458.5 480 432 480C405.5 480 384 501.5 384 528C384 554.5 405.5 576 432 576z"/></svg>
                        Add to Cart
                    </button>
                </c:if>
                <button class="btn btn-secondary add-to-wishlist" data-book-id="${book.id}">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.0.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M442.9 144C415.6 144 389.9 157.1 373.9 179.2L339.5 226.8C335 233 327.8 236.7 320.1 236.7C312.4 236.7 305.2 233 300.7 226.8L266.3 179.2C250.3 157.1 224.6 144 197.3 144C150.3 144 112.2 182.1 112.2 229.1C112.2 279 144.2 327.5 180.3 371.4C221.4 421.4 271.7 465.4 306.2 491.7C309.4 494.1 314.1 495.9 320.2 495.9C326.3 495.9 331 494.1 334.2 491.7C368.7 465.4 419 421.3 460.1 371.4C496.3 327.5 528.2 279 528.2 229.1C528.2 182.1 490.1 144 443.1 144zM335 151.1C360 116.5 400.2 96 442.9 96C516.4 96 576 155.6 576 229.1C576 297.7 533.1 358 496.9 401.9C452.8 455.5 399.6 502 363.1 529.8C350.8 539.2 335.6 543.9 320 543.9C304.4 543.9 289.2 539.2 276.9 529.8C240.4 502 187.2 455.5 143.1 402C106.9 358.1 64 297.7 64 229.1C64 155.6 123.6 96 197.1 96C239.8 96 280 116.5 305 151.1L320 171.8L335 151.1z"/></svg>
                    Add to Wishlist
                </button>
            </div>
        </div>

        <!-- Right Column: Book Information -->
        <div class="book-info-section">
            <!-- Title and Author -->
            <div class="book-header">
                <h1 class="book-title">${book.title}</h1>
                <p class="book-author">by <span
                        class="author-name">${book.author}</span></p>
            </div>

            <!-- Rating and Reviews -->
            <div class="book-rating-section">
                <div class="rating-display">
                    <div class="stars">
                        <jsp:include page="ratingStar.jsp">
                            <jsp:param name="fullStars" value="${book.fullStars}" />
                            <jsp:param name="partialFraction" value="${book.partialFraction}" />
                            <jsp:param name="emptyStars" value="${book.emptyStars}" />
                            <jsp:param name="size" value="18" />
                        </jsp:include>
                    </div>
                    <span class="rating-text">
                        <fmt:formatNumber value="${book.rating}" type="number" maxFractionDigits="1"/> out of 5
                    </span>
                </div>
            </div>

            <!-- Pricing Information -->
            <div class="pricing-section">
                <div class="price-container">
                    <span class="current-price">
                        <fmt:formatNumber value="${book.price}" type="number"/>
                        VND
                    </span>

                    <c:if test="${hasDiscount}">
                        <span class="original-price">
                            <fmt:formatNumber value="${book.originalPrice}" type="number"/> VND
                        </span>
                        <span class="discount-badge">${book.discount_rate}% OFF</span>
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
                            <span
                                    class="detail-value">${book.publishYear}</span>
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
                                <fmt:formatDate value="${book.createdAt}" pattern="MMM dd, yyyy"/>
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

    <jsp:include page="review.jsp">
        <jsp:param name="bookReview" value="${requestScope.bookReview}"/>
    </jsp:include>

    <!-- Related Actions -->
    <div class="related-actions">
        <div class="action-buttons">
            <button class="btn btn-outline" onclick="window.history.back()">
                <i class="icon-arrow-left"></i> Back to Previous Page
            </button>
            <a href="${pageContext.request.contextPath}/categories"
               class="btn btn-outline">
                <i class="icon-grid"></i> Browse More Books
            </a>
        </div>
    </div>
</div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/pages/bookDetailPage.js"></script>

</body>

</html>
