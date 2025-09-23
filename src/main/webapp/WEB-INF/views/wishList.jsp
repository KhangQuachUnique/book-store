<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>White List Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/whiteList.css">
</head>
<body>

<section class="white-list">
    <h1>Wish List</h1>

    <c:forEach var="book" items="${wishListBooks}">
        <div class="white-list-item-display">
            <div class="white-list-item">
                <img src="${book.thumbnailUrl}" alt="temp">
                <div class="white-list-item-part-2">
                    <span class="title">${book.title}</span>
                    <div class="white-list-item-price-section">
                        <span class="white-list-item-price"><fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></span>
                        <del class="white-list-item-price-old"><fmt:formatNumber value="${book.originalPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></del>
                    </div>
                    <span class="white-list-item-rating">${book.rating}</span>
                    <span class="white-list-item-sold">Đã bán 238</span>
                </div>
                <div class="description">${book.description}</div>
                <div class="actions">
                    <svg class="heart-icon selected" data-book-id="${book.id}" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                        <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41 0.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                    </svg>
                    <form action="${pageContext.request.contextPath}/user/cart" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="add"/>
                        <input type="hidden" name="bookId" value="${book.id}"/>
                        <input type="hidden" name="quantity" value="1"/>
                        <button type="submit" class="add-to-cart-button">Add to cart</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/user/cart" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="buyNow"/>
                        <input type="hidden" name="bookId" value="${book.id}"/>
                        <input type="hidden" name="quantity" value="1"/>
                        <button type="submit" class="buy-button">Buy now</button>
                    </form>
                </div>
            </div>
        </div>
    </c:forEach>
</section>

<script type="module" src="${pageContext.request.contextPath}/assets/js/pages/WishListPage.js"></script>
</body>
</html>
