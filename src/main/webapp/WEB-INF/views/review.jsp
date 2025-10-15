<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/review.css">
</head>
<body>
<div class="review-section">
    <div class="review-overall">
        <div>
            <h2 class="review-header">Đánh Giá Của Khách Hàng</h2>
            <div class="total-rating">
                <span>${requestScope.bookReview.averageRating}</span>
                <jsp:include page="ratingStar.jsp">
                    <jsp:param name="fullStars" value="${requestScope.bookReview.fullStars}" />
                    <jsp:param name="partialFraction" value="${requestScope.bookReview.fractionalStars}" />
                    <jsp:param name="emptyStars" value="${requestScope.bookReview.emptyStars}" />
                    <jsp:param name="size" value="26" />
                </jsp:include>
            </div>
            <span class="review-count">(${requestScope.bookReview.totalReviews} đánh giá)</span>
        </div>
        <div class="levels-count">
            <div class="level-count">
                <jsp:include page="ratingStar.jsp">
                    <jsp:param name="fullStars" value="${5}" />
                    <jsp:param name="partialFraction" value="0" />
                    <jsp:param name="emptyStars" value="${0}" />
                    <jsp:param name="size" value="16" />
                </jsp:include>
                <span>${requestScope.bookReview.fiveStarCount}</span>
            </div>
            <div class="level-count">
                <jsp:include page="ratingStar.jsp">
                    <jsp:param name="fullStars" value="${4}" />
                    <jsp:param name="partialFraction" value="0" />
                    <jsp:param name="emptyStars" value="${1}" />
                    <jsp:param name="size" value="16" />
                </jsp:include>
                <span>${requestScope.bookReview.fourStarCount}</span>
            </div>
            <div class="level-count">
                <jsp:include page="ratingStar.jsp">
                    <jsp:param name="fullStars" value="${3}" />
                    <jsp:param name="partialFraction" value="0" />
                    <jsp:param name="emptyStars" value="${2}" />
                    <jsp:param name="size" value="16" />
                </jsp:include>
                <span>${requestScope.bookReview.threeStarCount}</span>
            </div>
            <div class="level-count">
                <jsp:include page="ratingStar.jsp">
                    <jsp:param name="fullStars" value="${2}" />
                    <jsp:param name="partialFraction" value="0" />
                    <jsp:param name="emptyStars" value="${3}" />
                    <jsp:param name="size" value="16" />
                </jsp:include>
                <span>${requestScope.bookReview.twoStarCount}</span>
            </div>
            <div class="level-count">
                <jsp:include page="ratingStar.jsp">
                    <jsp:param name="fullStars" value="${1}" />
                    <jsp:param name="partialFraction" value="0" />
                    <jsp:param name="emptyStars" value="${4}" />
                    <jsp:param name="size" value="16" />
                </jsp:include>
                <span>${requestScope.bookReview.oneStarCount}</span>
            </div>
        </div>
    </div>
    <hr>
    <div class="reviews-display">
        <c:forEach var="review" items="${requestScope.bookReview.reviews}">
            <div class="user-comment">
                <c:choose>
                    <c:when test="${empty review.user.avatarUrl}">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="#858585" viewBox="0 0 640 640" class="avatar"><!--!Font Awesome Free v7.0.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M463 448.2C440.9 409.8 399.4 384 352 384L288 384C240.6 384 199.1 409.8 177 448.2C212.2 487.4 263.2 512 320 512C376.8 512 427.8 487.3 463 448.2zM64 320C64 178.6 178.6 64 320 64C461.4 64 576 178.6 576 320C576 461.4 461.4 576 320 576C178.6 576 64 461.4 64 320zM320 336C359.8 336 392 303.8 392 264C392 224.2 359.8 192 320 192C280.2 192 248 224.2 248 264C248 303.8 280.2 336 320 336z"/></svg>
                    </c:when>
                    <c:otherwise>
                        <img src="${review.user.avatarUrl}" alt="avatar" class="avatar" />
                    </c:otherwise>
                </c:choose>
                <div class="comment-content">
                    <div class="comment-header">
                        <div>
                            <span class="username">${review.user.name}</span>
                            <span class="comment-date">
                                <fmt:formatDate value="${review.createdAt}" pattern="dd/MM/yyyy" />
                            </span>
                        </div>
                        <div class="comment-rating">
                            <jsp:include page="ratingStar.jsp">
                                <jsp:param name="fullStars" value="${review.rating}" />
                                <jsp:param name="partialFraction" value="${0}" />
                                <jsp:param name="emptyStars" value="${5 - review.rating}" />
                                <jsp:param name="size" value="18" />
                            </jsp:include>
                        </div>
                    </div>
                    <p class="comment">${review.comment}</p>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<script type="module" src="${pageContext.request.contextPath}/assets/js/pages/ReviewPage.js"></script>
</body>
</html>
