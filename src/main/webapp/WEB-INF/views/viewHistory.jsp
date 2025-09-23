<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/history.css" />

<section class="view-history">
    <h1>View History</h1>

    <c:choose>
        <c:when test="${empty history}">
            <p class="empty">Bạn chưa xem sách nào.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="item" items="${history}">
                <div class="history-item-display">
                    <div class="history-item">
                        <a class="history-link" href="${pageContext.request.contextPath}/book-detail?id=${item.bookId}">
                            <img data-src="${item.thumbnail}" alt="thumbnail">
                            <div class="history-item-part-2">
                                <span class="title">${item.title}</span>
                                <span class="viewed-at">
                                    <fmt:formatDate value="${item.viewedAt}" pattern="dd/MM/yyyy HH:mm"/>
                                </span>
                            </div>
                        </a>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</section>

<script type="module" src="${pageContext.request.contextPath}/assets/js/pages/HistoryPage.js"></script>
