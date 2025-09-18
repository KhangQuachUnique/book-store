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
                                <h2 class="review-header">Customer Reviews</h2>
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
                            </div>
                            <div class="levels-count">
                                <div class="level-count">
                                    <jsp:include page="ratingStar.jsp">
                                        <jsp:param name="fullStars" value="${5}" />
                                        <jsp:param name="partialFraction" value="0" />
                                        <jsp:param name="emptyStars" value="${0}" />
                                        <jsp:param name="size" value="18" />
                                    </jsp:include>
                                    <span>${requestScope.bookReview.fiveStarCount}</span>
                                </div>
                                <div class="level-count">
                                    <jsp:include page="ratingStar.jsp">
                                        <jsp:param name="fullStars" value="${4}" />
                                        <jsp:param name="partialFraction" value="0" />
                                        <jsp:param name="emptyStars" value="${1}" />
                                        <jsp:param name="size" value="18" />
                                    </jsp:include>
                                    <span>${requestScope.bookReview.fourStarCount}</span>
                                </div>
                                <div class="level-count">
                                    <jsp:include page="ratingStar.jsp">
                                        <jsp:param name="fullStars" value="${3}" />
                                        <jsp:param name="partialFraction" value="0" />
                                        <jsp:param name="emptyStars" value="${2}" />
                                        <jsp:param name="size" value="18" />
                                    </jsp:include>
                                    <span>${requestScope.bookReview.threeStarCount}</span>
                                </div>
                                <div class="level-count">
                                    <jsp:include page="ratingStar.jsp">
                                        <jsp:param name="fullStars" value="${2}" />
                                        <jsp:param name="partialFraction" value="0" />
                                        <jsp:param name="emptyStars" value="${3}" />
                                        <jsp:param name="size" value="18" />
                                    </jsp:include>
                                    <span>${requestScope.bookReview.twoStarCount}</span>
                                </div>
                                <div class="level-count">
                                    <jsp:include page="ratingStar.jsp">
                                        <jsp:param name="fullStars" value="${1}" />
                                        <jsp:param name="partialFraction" value="0" />
                                        <jsp:param name="emptyStars" value="${4}" />
                                        <jsp:param name="size" value="18" />
                                    </jsp:include>
                                    <span>${requestScope.bookReview.oneStarCount}</span>
                                </div>
                            </div>
                        </div>
                        <hr>
                        <div class="reviews-display">
                            <c:forEach var="reviewShow" items="${requestScope.bookReview.reviewShows}">
                                <div class="user-comment">
                                    <c:choose>
                                        <c:when test="${empty reviewShow.avatarUrl}">
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="#858585" viewBox="0 0 640 640"
                                                class="avatar"><!--!Font Awesome Free v7.0.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.-->
                                                <path
                                                    d="M463 448.2C440.9 409.8 399.4 384 352 384L288 384C240.6 384 199.1 409.8 177 448.2C212.2 487.4 263.2 512 320 512C376.8 512 427.8 487.3 463 448.2zM64 320C64 178.6 178.6 64 320 64C461.4 64 576 178.6 576 320C576 461.4 461.4 576 320 576C178.6 576 64 461.4 64 320zM320 336C359.8 336 392 303.8 392 264C392 224.2 359.8 192 320 192C280.2 192 248 224.2 248 264C248 303.8 280.2 336 320 336z" />
                                            </svg>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${reviewShow.avatarUrl}" alt="avatar" class="avatar" />
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="comment-content">
                                        <div class="comment-header">
                                            <span class="username">${reviewShow.username}</span>
                                            <span class="comment-date">
                                                <fmt:formatDate value="${reviewShow.date}" pattern="dd/MM/yyyy" />
                                            </span>
                                        </div>
                                        <div class="comment-rating">
                                            <jsp:include page="ratingStar.jsp">
                                                <jsp:param name="fullStars" value="${reviewShow.fullStars}" />
                                                <jsp:param name="partialFraction"
                                                    value="${reviewShow.partialFraction}" />
                                                <jsp:param name="emptyStars" value="${reviewShow.emptyStars}" />
                                                <jsp:param name="size" value="18" />
                                            </jsp:include>
                                        </div>
                                        <div class="comment-text">
                                            ${reviewShow.comment}
                                        </div>
                                        <div class="comment-actions">
                                            <c:choose>
                                                <c:when test="${reviewShow.likedByCurrentUser == false}">
                                                    <button id="likeBtn" class="like-btn" aria-label="Like button">
                                                        <!-- Icon mặc định chưa like -->
                                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"
                                                            review-id="${reviewShow.id}">
                                                            <path
                                                                d="M80 160c17.7 0 32 14.3 32 32l0 256c0 17.7-14.3 32-32 32l-48 0c-17.7 0-32-14.3-32-32L0 192c0-17.7 14.3-32 32-32l48 0zM270.6 16C297.9 16 320 38.1 320 65.4l0 4.2c0 6.8-1.3 13.6-3.8 19.9L288 160 448 160c26.5 0 48 21.5 48 48 0 19.7-11.9 36.6-28.9 44 17 7.4 28.9 24.3 28.9 44 0 23.4-16.8 42.9-39 47.1 4.4 7.3 7 15.8 7 24.9 0 22.2-15 40.8-35.4 46.3 2.2 5.5 3.4 11.5 3.4 17.7 0 26.5-21.5 48-48 48l-87.9 0c-36.3 0-71.6-12.4-99.9-35.1L184 435.2c-15.2-12.1-24-30.5-24-50l0-186.6c0-14.9 3.5-29.6 10.1-42.9L226.3 43.3C234.7 26.6 251.8 16 270.6 16z">
                                                            </path>
                                                        </svg>
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button id="likeBtn" class="like-btn liked"
                                                        aria-label="Like button">
                                                        <!-- Icon mặc định chưa like -->
                                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"
                                                            review-id="${reviewShow.id}">
                                                            <path
                                                                d="M80 160c17.7 0 32 14.3 32 32l0 256c0 17.7-14.3 32-32 32l-48 0c-17.7 0-32-14.3-32-32L0 192c0-17.7 14.3-32 32-32l48 0zM270.6 16C297.9 16 320 38.1 320 65.4l0 4.2c0 6.8-1.3 13.6-3.8 19.9L288 160 448 160c26.5 0 48 21.5 48 48 0 19.7-11.9 36.6-28.9 44 17 7.4 28.9 24.3 28.9 44 0 23.4-16.8 42.9-39 47.1 4.4 7.3 7 15.8 7 24.9 0 22.2-15 40.8-35.4 46.3 2.2 5.5 3.4 11.5 3.4 17.7 0 26.5-21.5 48-48 48l-87.9 0c-36.3 0-71.6-12.4-99.9-35.1L184 435.2c-15.2-12.1-24-30.5-24-50l0-186.6c0-14.9 3.5-29.6 10.1-42.9L226.3 43.3C234.7 26.6 251.8 16 270.6 16z">
                                                            </path>
                                                        </svg>
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                            <span class="like-count">${reviewShow.likeCount} Like</span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <script type="module"
                        src="${pageContext.request.contextPath}/assets/js/pages/BookReviewPage.js"></script>
                </body>

            </html>
