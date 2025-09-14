<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/review.css">
</head>
<div class="review-section">
    <h2 class="review-header">Khách hàng đánh giá</h2>
    <div class="total-rating">
        <span>${bookReview.averageRating}</span>
        <jsp:include page="ratingStar.jsp">
            <jsp:param name="fullStars" value="${bookReview.fullStars}" />
            <jsp:param name="partialFraction" value="${bookReview.partialFraction}" />
            <jsp:param name="emptyStars" value="${bookReview.emptyStars}" />
            <jsp:param name="size" value="26" />
        </jsp:include>
    </div>
    <span class="review-count">(${bookReview.totalReviews} đánh giá)</span>
    <hr>
    <div class="reviews-display">
        <c:forEach var="review" items="${bookReview.reviews}">
            <div class="user-comment">
                <img src="${review.avatarUrl}" alt="avatar" class="avatar" />
                <div class="comment-content">
                    <div class="comment-header">
                        <span class="username">${review.username}</span>
                        <span class="comment-date">
                            <fmt:formatDate value="${review.date}" pattern="dd/MM/yyyy" />
                        </span>
                    </div>
                    <div class="comment-rating">
                        <jsp:include page="ratingStar.jsp">
                            <jsp:param name="fullStars" value="${review.fullStars}" />
                            <jsp:param name="partialFraction" value="${review.partialFraction}" />
                            <jsp:param name="emptyStars" value="${review.emptyStars}" />
                            <jsp:param name="size" value="18" />
                        </jsp:include>
                    </div>
                    <div class="comment-text">
                        ${review.comment}
                    </div>
                    <c:if test="${not empty review.avatarUrl}">
                        <div class="comment-image">
                            <img src="${review.avatarUrl}" alt="comment image" />
                        </div>
                    </c:if>
                    <div class="comment-actions">
                        <button class="like-btn" type="button">
                            👍 Like <span class="like-count">${review.likeCount}</span>
                        </button>
                        <button class="reply-btn" type="button">
                            💬 Reply <span class="reply-count">19</span>
                        </button>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</html>
