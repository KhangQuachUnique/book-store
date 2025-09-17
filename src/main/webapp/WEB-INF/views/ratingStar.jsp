<%--
  Created by IntelliJ IDEA.
  User: kadfw
  Date: 9/13/2025
  Time: 8:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/starRating.css">
</head>
<body>
<div class="rating-stars" style="--star-size: ${not empty param.size ? param.size : '24'}px">
    <div class="rating-stars">
        <!-- full stars -->
        <c:forEach begin="1" end="${param.fullStars}" var="i">
            <svg class="full-star" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512">
                <path fill="currentColor" d="M259.3 17.8L194 150.2 47.9 171.5c-26.2 3.8-36.7 36-17.7 54.6l105.7 103-25 145.5
                c-4.5 26.1 23 46 46.4 33.7L288 439.6l130.7 68.7c23.4 12.3 50.9-7.6 46.4-33.7l-25-145.5
                105.7-103c19-18.6 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"></path>
            </svg>

        </c:forEach>

        <!-- partial star -->
        <c:if test="${param.partialFraction + 0.0 > 0}">
            <svg class="partial-star" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 512">
                <defs>
                    <linearGradient id="partialGrad" x1="0" y1="0" x2="100%" y2="0">
                        <stop offset="${param.partialFraction * 100}%" stop-color="gold"></stop>
                        <stop offset="${param.partialFraction * 100}%" stop-color="lightgray"></stop>
                    </linearGradient>
                </defs>
                <path fill="url(#partialGrad)" d="M259.3 17.8L194 150.2 47.9 171.5c-26.2 3.8-36.7 36-17.7 54.6l105.7 103-25 145.5
                c-4.5 26.1 23 46 46.4 33.7L288 439.6l130.7 68.7c23.4 12.3 50.9-7.6 46.4-33.7l-25-145.5
                105.7-103c19-18.6 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"></path>
            </svg>
        </c:if>

        <!-- empty stars -->
        <c:forEach begin="1" end="${param.emptyStars}" var="i">
            <svg class="empty-star" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512">
                <path fill="currentColor" d="M259.3 17.8L194 150.2 47.9 171.5c-26.2 3.8-36.7 36-17.7 54.6l105.7 103-25 145.5
                c-4.5 26.1 23 46 46.4 33.7L288 439.6l130.7 68.7c23.4 12.3 50.9-7.6 46.4-33.7l-25-145.5
                105.7-103c19-18.6 8.5-50.8-17.7-54.6L382 150.2 316.7 17.8c-11.7-23.6-45.6-23.9-57.4 0z"></path>
            </svg>

        </c:forEach>
    </div>
</div>

</body>
</html>
