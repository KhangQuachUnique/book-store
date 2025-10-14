<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Wishlist Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/wishList.css">
</head>
<body>

<section class="wish-list">
    <h1>Wish List</h1>
    <c:if test="${empty requestScope.wishList.items}">
        <p>Your wish list is empty.</p>
    </c:if>
    <c:if test="${not empty requestScope.wishList.items}">
        <c:forEach var="wishListItem" items="${requestScope.wishList.items}">
            <div class="wish-list-item-display">
                <div class="wish-list-item">
                    <a href="${pageContext.request.contextPath}/book-detail?id=${wishListItem.book.id}">
                        <img src="${wishListItem.book.thumbnailUrl}" alt="temp">
                        <div class="wish-list-item-part-2">
                            <span class="title">${wishListItem.book.title}</span>
                            <span class="author">Author: ${wishListItem.book.author}</span>
                            <span class="wish-list-item-rating">
                                <jsp:include page="ratingStar.jsp">
                                    <jsp:param name="fullStars" value="${wishListItem.fullStars}" />
                                    <jsp:param name="partialFraction" value="${wishListItem.fractionalStars}" />
                                    <jsp:param name="emptyStars" value="${wishListItem.emptyStars}" />
                                    <jsp:param name="size" value="16" />
                                </jsp:include>
                                    ${wishListItem.book.averageRating}
                            </span>
                            <span class="wish-list-item-sold">Đã bán ${wishListItem.book.sold}</span>
                        </div>
                    </a>
                    <div class="wish-list-item-tail-part">
                        <div class="wish-list-item-price-section">
                            <span class="wish-list-item-price"><fmt:formatNumber value="${wishListItem.book.getPrice()}" type="currency" currencySymbol="VND" maxFractionDigits="0"/></span>
                            <del class="wish-list-item-price-old"><fmt:formatNumber value="${wishListItem.book.originalPrice}" type="currency" currencySymbol="VND" maxFractionDigits="0"/></del>
                        </div>
                        <div class="actions">
                            <svg class="heart-icon selected" data-book-id="${wishListItem.book.id}" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                                <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41 0.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"></path>
                            </svg>
                            <button class="btn btn-primary add-to-cart" data-book-id="${wishListItem.book.id}">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.1.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M24 48C10.7 48 0 58.7 0 72C0 85.3 10.7 96 24 96L69.3 96C73.2 96 76.5 98.8 77.2 102.6L129.3 388.9C135.5 423.1 165.3 448 200.1 448L456 448C469.3 448 480 437.3 480 424C480 410.7 469.3 400 456 400L200.1 400C188.5 400 178.6 391.7 176.5 380.3L171.4 352L475 352C505.8 352 532.2 330.1 537.9 299.8L568.9 133.9C572.6 114.2 557.5 96 537.4 96L124.7 96L124.3 94C119.5 67.4 96.3 48 69.2 48L24 48zM208 576C234.5 576 256 554.5 256 528C256 501.5 234.5 480 208 480C181.5 480 160 501.5 160 528C160 554.5 181.5 576 208 576zM432 576C458.5 576 480 554.5 480 528C480 501.5 458.5 480 432 480C405.5 480 384 501.5 384 528C384 554.5 405.5 576 432 576z"></path></svg>
                            </button>
<%--                            <form action="${pageContext.request.contextPath}/user/cart" method="post" style="display: inline;">--%>
<%--                                <input type="hidden" name="action" value="add"/>--%>
<%--                                <input type="hidden" name="bookId" value="${wishListItem.book.id}"/>--%>
<%--                                <input type="hidden" name="quantity" value="1"/>--%>
<%--                                <button type="submit" class="add-to-cart-button" title="Add to Cart">--%>
<%--                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.1.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M24 48C10.7 48 0 58.7 0 72C0 85.3 10.7 96 24 96L69.3 96C73.2 96 76.5 98.8 77.2 102.6L129.3 388.9C135.5 423.1 165.3 448 200.1 448L456 448C469.3 448 480 437.3 480 424C480 410.7 469.3 400 456 400L200.1 400C188.5 400 178.6 391.7 176.5 380.3L171.4 352L475 352C505.8 352 532.2 330.1 537.9 299.8L568.9 133.9C572.6 114.2 557.5 96 537.4 96L124.7 96L124.3 94C119.5 67.4 96.3 48 69.2 48L24 48zM208 576C234.5 576 256 554.5 256 528C256 501.5 234.5 480 208 480C181.5 480 160 501.5 160 528C160 554.5 181.5 576 208 576zM432 576C458.5 576 480 554.5 480 528C480 501.5 458.5 480 432 480C405.5 480 384 501.5 384 528C384 554.5 405.5 576 432 576z"></path></svg>--%>
<%--                                </button>--%>
<%--                            </form>--%>
                            <form action="${pageContext.request.contextPath}/user/cart" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="buyNow"/>
                                <input type="hidden" name="bookId" value="${wishListItem.book.id}"/>
                                <input type="hidden" name="quantity" value="1"/>
                                <button type="submit" class="buy-button" title="Buy Now">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.1.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M128 128C92.7 128 64 156.7 64 192L64 448C64 483.3 92.7 512 128 512L512 512C547.3 512 576 483.3 576 448L576 192C576 156.7 547.3 128 512 128L128 128zM320 224C373 224 416 267 416 320C416 373 373 416 320 416C267 416 224 373 224 320C224 267 267 224 320 224zM512 248C512 252.4 508.4 256.1 504 255.5C475 251.9 452.1 228.9 448.5 200C448 195.6 451.6 192 456 192L504 192C508.4 192 512 195.6 512 200L512 248zM128 392C128 387.6 131.6 383.9 136 384.5C165 388.1 187.9 411.1 191.5 440C192 444.4 188.4 448 184 448L136 448C131.6 448 128 444.4 128 440L128 392zM136 255.5C131.6 256 128 252.4 128 248L128 200C128 195.6 131.6 192 136 192L184 192C188.4 192 192.1 195.6 191.5 200C187.9 229 164.9 251.9 136 255.5zM504 384.5C508.4 384 512 387.6 512 392L512 440C512 444.4 508.4 448 504 448L456 448C451.6 448 447.9 444.4 448.5 440C452.1 411 475.1 388.1 504 384.5z"></path></svg>
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </c:if>
    <!-- Pagination controls -->
    <c:if test="${requestScope.totalPages > 1}">
        <nav class="pagination">
            <ul>
                <li>
                    <a href="?page=${requestScope.currentPage - 1}&pageSize=${requestScope.pageSize}" class="${requestScope.currentPage == 1 ? 'disabled' : ''}">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640" width="18" height="18"><path d="M169.4 297.4C156.9 309.9 156.9 330.2 169.4 342.7L361.4 534.7C373.9 547.2 394.2 547.2 406.7 534.7C419.2 522.2 419.2 501.9 406.7 489.4L237.3 320L406.6 150.6C419.1 138.1 419.1 117.8 406.6 105.3C394.1 92.8 373.8 92.8 361.3 105.3L169.3 297.3z"></path></svg>
                    </a>
                </li>
                <c:forEach var="i" begin="1" end="${requestScope.totalPages}">
                    <li>
                        <a href="?page=${i}&pageSize=${requestScope.pageSize}" class="${i == requestScope.currentPage ? 'active' : ''}">${i}</a>
                    </li>
                </c:forEach>
                <li>
                    <a href="?page=${requestScope.currentPage + 1}&pageSize=${requestScope.pageSize}" class="${requestScope.currentPage == requestScope.totalPages ? 'disabled' : ''}">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640" width="18" height="18"><path d="M471.1 297.4C483.6 309.9 483.6 330.2 471.1 342.7L279.1 534.7C266.6 547.2 246.3 547.2 233.8 534.7C221.3 522.2 221.3 501.9 233.8 489.4L403.2 320L233.9 150.6C221.4 138.1 221.4 117.8 233.9 105.3C246.4 92.8 266.7 92.8 279.2 105.3L471.2 297.3z"></path></svg>
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>
</section>



<script type="module" src="${pageContext.request.contextPath}/assets/js/pages/WishListPage.js"></script>
</body>
</html>
