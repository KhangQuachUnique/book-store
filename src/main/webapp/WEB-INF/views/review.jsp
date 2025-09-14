<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/review.css">
</head>
<div class="review-section">
    <h2 class="review-header">Khách hàng đánh giá</h2>
    <div class="total-rating">
        <span>4.9</span>
        <jsp:include page="ratingStar.jsp">
            <jsp:param name="fullStars" value="4" />
            <jsp:param name="partialFraction" value="0.9" />
            <jsp:param name="emptyStars" value="0" />
            <jsp:param name="size" value="26" />
        </jsp:include>
    </div>
    <span class="review-count">(192 đánh giá)</span>
    <hr>
    <div class="reviews-display">
        <c:forEach var="review" items="${reviews}">
            <div class="user-comment">
                <img src="${review.avatarUrl}" alt="avatar" class="avatar" />
                <div class="comment-content">
                    <div class="comment-header">
                        <span class="username">${review.username}</span>
                        <span class="comment-date">${review.date}</span>
                    </div>
                    <div class="comment-rating">
                        <jsp:include page="ratingStar.jsp">
                            <jsp:param name="fullStars" value="${review.rating}" />
                            <jsp:param name="partialFraction" value="0.0" />
                            <jsp:param name="emptyStars" value="${5 - review.rating}" />
                            <jsp:param name="size" value="20" />
                        </jsp:include>
                    </div>
                    <div class="comment-text">
                        ${review.text}
                    </div>
                    <c:if test="${not empty review.imageUrl}">
                        <div class="comment-image">
                            <img src="${review.imageUrl}" alt="comment image" />
                        </div>
                    </c:if>
                    <div class="comment-actions">
                        <button class="like-btn" type="button">
                            👍 Like <span class="like-count">${review.likeCount}</span>
                        </button>
                        <button class="reply-btn" type="button">
                            💬 Reply <span class="reply-count">${review.replyCount}</span>
                        </button>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</html>
