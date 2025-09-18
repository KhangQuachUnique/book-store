<%--
  Book Detail Page - Displays comprehensive information about a specific book
  Shows all book attributes including title, author, description, pricing, ratings, etc.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="/WEB-INF/views/layout.jsp">
    <c:param name="content">
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
                                            <span class="star filled">★</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="star empty">☆</span>
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
                            <span class="current-price">$<fmt:formatNumber value="${book.price}" type="number" maxFractionDigits="2" /></span>
                            
                            <c:if test="${hasDiscount}">
                                <span class="original-price">$<fmt:formatNumber value="${book.originalPrice}" type="number" maxFractionDigits="2" /></span>
                                <span class="discount-badge">${book.discountRate}% OFF</span>
                                <div class="savings-text">
                                    You save: $<fmt:formatNumber value="${savings}" type="number" maxFractionDigits="2" />
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

        <!-- Styles for Book Detail Page -->
        <style>
            .book-detail-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            }

            .breadcrumb {
                margin-bottom: 30px;
                font-size: 14px;
                color: #666;
            }

            .breadcrumb a {
                color: #007bff;
                text-decoration: none;
            }

            .breadcrumb a:hover {
                text-decoration: underline;
            }

            .separator {
                margin: 0 10px;
                color: #ccc;
            }

            .current {
                color: #333;
                font-weight: 500;
            }

            .book-detail-content {
                display: grid;
                grid-template-columns: 1fr 2fr;
                gap: 40px;
                margin-bottom: 40px;
            }

            .book-image-section {
                display: flex;
                flex-direction: column;
                gap: 20px;
            }

            .book-image-container {
                background: #f8f9fa;
                border-radius: 8px;
                padding: 20px;
                text-align: center;
            }

            .book-detail-image {
                max-width: 100%;
                max-height: 400px;
                object-fit: cover;
                border-radius: 4px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            }

            .no-image-placeholder {
                height: 300px;
                display: flex;
                align-items: center;
                justify-content: center;
                background: #e9ecef;
                color: #6c757d;
                border-radius: 4px;
            }

            .quick-actions {
                display: flex;
                flex-direction: column;
                gap: 12px;
            }

            .btn {
                padding: 12px 24px;
                border: none;
                border-radius: 6px;
                font-size: 14px;
                font-weight: 500;
                text-decoration: none;
                text-align: center;
                cursor: pointer;
                transition: all 0.2s ease;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                gap: 8px;
            }

            .btn-primary {
                background: #007bff;
                color: white;
            }

            .btn-primary:hover {
                background: #0056b3;
            }

            .btn-secondary {
                background: #6c757d;
                color: white;
            }

            .btn-secondary:hover {
                background: #545b62;
            }

            .btn-outline {
                background: transparent;
                color: #007bff;
                border: 1px solid #007bff;
            }

            .btn-outline:hover {
                background: #007bff;
                color: white;
            }

            .book-info-section {
                display: flex;
                flex-direction: column;
                gap: 25px;
            }

            .book-header h1 {
                font-size: 32px;
                font-weight: 700;
                color: #212529;
                margin: 0 0 8px 0;
                line-height: 1.2;
            }

            .book-author {
                font-size: 18px;
                color: #6c757d;
                margin: 0;
            }

            .author-name {
                color: #007bff;
                font-weight: 500;
            }

            .rating-display {
                display: flex;
                align-items: center;
                gap: 12px;
                flex-wrap: wrap;
            }

            .stars {
                display: flex;
                gap: 2px;
            }

            .star {
                font-size: 20px;
                color: #ffc107;
            }

            .star.empty {
                color: #e9ecef;
            }

            .rating-text {
                font-weight: 500;
                color: #495057;
            }

            .review-count {
                color: #6c757d;
                font-size: 14px;
            }

            .price-container {
                display: flex;
                align-items: center;
                gap: 15px;
                flex-wrap: wrap;
            }

            .current-price {
                font-size: 32px;
                font-weight: 700;
                color: #28a745;
            }

            .original-price {
                font-size: 20px;
                color: #6c757d;
                text-decoration: line-through;
            }

            .discount-badge {
                background: #dc3545;
                color: white;
                padding: 4px 8px;
                border-radius: 4px;
                font-size: 12px;
                font-weight: 600;
            }

            .savings-text {
                color: #28a745;
                font-weight: 500;
                font-size: 14px;
            }

            .stock-status {
                display: flex;
                align-items: center;
                gap: 8px;
                padding: 12px 16px;
                border-radius: 6px;
                font-weight: 500;
            }

            .stock-status.in-stock {
                background: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }

            .stock-status.out-of-stock {
                background: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }

            .book-details-section h3 {
                font-size: 20px;
                font-weight: 600;
                color: #212529;
                margin: 0 0 15px 0;
            }

            .details-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 12px;
            }

            .detail-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 8px 0;
                border-bottom: 1px solid #e9ecef;
            }

            .detail-label {
                font-weight: 500;
                color: #495057;
            }

            .detail-value {
                color: #212529;
            }

            .description-section h3 {
                font-size: 20px;
                font-weight: 600;
                color: #212529;
                margin: 0 0 15px 0;
            }

            .book-description {
                line-height: 1.6;
                color: #495057;
                font-size: 16px;
            }

            .book-description p {
                margin: 0 0 15px 0;
            }

            .related-actions {
                border-top: 1px solid #e9ecef;
                padding-top: 30px;
            }

            .action-buttons {
                display: flex;
                gap: 15px;
                flex-wrap: wrap;
            }

            /* Responsive Design */
            @media (max-width: 768px) {
                .book-detail-content {
                    grid-template-columns: 1fr;
                    gap: 30px;
                }

                .book-header h1 {
                    font-size: 24px;
                }

                .current-price {
                    font-size: 24px;
                }

                .details-grid {
                    grid-template-columns: 1fr;
                }

                .action-buttons {
                    flex-direction: column;
                }
            }
        </style>

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
    </c:param>
</c:import>