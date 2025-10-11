<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
            <a class="history-link"
               href="${pageContext.request.contextPath}/book-detail?id=${item.book.id}">

              <img src="${item.book.thumbnailUrl}" alt="${item.book.title}"/>

              <div class="history-item-part-2">
                <span class="title">${item.book.title}</span>

                <c:if test="${not empty item.book.author}">
                  <span class="author">${item.book.author}</span>
                </c:if>

                <div class="history-item-price-section">
                  <span class="price">
                    <fmt:formatNumber value="${item.book.price}" type="number"/> VNĐ
                  </span>
                  <c:if test="${item.book.originalPrice > item.book.price}">
                    <del class="price-old">
                      <fmt:formatNumber value="${item.book.originalPrice}" type="number"/> 
                    </del>
                  </c:if>
                </div>

                <span class="rating">
                    <jsp:include page="ratingStar.jsp">
                                <jsp:param name="fullStars" value="${item.book.fullStars}" />
                                <jsp:param name="partialFraction" value="${item.book.partialFraction}" />
                                <jsp:param name="emptyStars" value="${item.book.emptyStars}" />
                                <jsp:param name="size" value="16" />
                    </jsp:include>
                    ${item.book.averageRating}
                </span>

                <span class="viewed-at">
                  Đã xem lúc: <fmt:formatDate value="${item.viewedAt}" pattern="dd/MM/yyyy HH:mm"/>
                </span>
              </div>
            </a>
          </div>
        </div>
      </c:forEach>
    </c:otherwise>
  </c:choose>
</section>


<!-- <script type="module" src="${pageContext.request.contextPath}/assets/js/pages/HistoryPage.js"></script> -->
