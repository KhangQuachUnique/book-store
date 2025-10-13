<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
    <title>Book Recommendations</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/recommendedBooks.css">
</head>
<body>

<!-- ================== TOP SELLING BOOKS ================== -->
<div class="recommended-section" id="topSellingSection">
    <p>Best Sellers</p>
    <div class="recommended-container">
        <button class="scroll-btn left">&#10094;</button>

        <div class="recommended-window">
            <div class="recommended-track">
                <c:forEach items="${requestScope.topSellingBooks}" var="book">
                    <div class="book-card small-card">
                        <a href="${pageContext.request.contextPath}/book-detail?id=${book.id}" class="book-link">
                            <div class="book-image">
                                <img src="${book.thumbnailUrl}" alt="${book.title}" class="book-thumbnail small-thumb">
                            </div>
                            <div class="book-info">
                                <h4 class="book-title">${book.title}</h4>
                                <div class="book-price-row column-price">
                                    <c:choose>
                                        <c:when test="${book.discountRate > 0}">
                                            <span class="book-price-badge">
                                                <fmt:formatNumber value="${book.getPrice()}" type="number" /> VND
                                            </span>
                                            <div class="original-discount">
                                                <span class="original-price">
                                                    <fmt:formatNumber value="${book.originalPrice}" type="number" /> VND
                                                </span>
                                                <span class="discount">-${book.discountRate}%</span>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="book-price-badge">
                                                <fmt:formatNumber value="${book.originalPrice}" type="number" /> VND
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>

        <button class="scroll-btn right">&#10095;</button>
    </div>
</div>

<c:if test="${not empty requestScope.recommendedBooks}">
    <!-- ================== RECOMMENDED BOOKS ================== -->
    <div class="recommended-section" id="recommendedSection">
        <p>You may also like</p>
        <div class="recommended-container">
            <button class="scroll-btn left">&#10094;</button>

            <div class="recommended-window">
                <div class="recommended-track">
                    <c:forEach items="${requestScope.recommendedBooks}" var="book">
                        <div class="book-card small-card">
                            <a href="${pageContext.request.contextPath}/book-detail?id=${book.id}" class="book-link">
                                <div class="book-image">
                                    <img src="${book.thumbnailUrl}" alt="${book.title}" class="book-thumbnail small-thumb">
                                </div>
                                <div class="book-info">
                                    <h4 class="book-title">${book.title}</h4>
                                    <div class="book-price-row column-price">
                                        <c:choose>
                                            <c:when test="${book.discountRate > 0}">
                                                <span class="book-price-badge">
                                                    <fmt:formatNumber value="${book.getPrice()}" type="number" /> VND
                                                </span>
                                                <div class="original-discount">
                                                    <span class="original-price">
                                                        <fmt:formatNumber value="${book.originalPrice}" type="number" /> VND
                                                    </span>
                                                    <span class="discount">-${book.discountRate}%</span>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="book-price-badge">
                                                    <fmt:formatNumber value="${book.originalPrice}" type="number" /> VND
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <button class="scroll-btn right">&#10095;</button>
        </div>
    </div>
</c:if>

<script>
    document.addEventListener("DOMContentLoaded", () => {

        function initSlider(sectionId) {
            const section = document.getElementById(sectionId);
            if (!section) return;

            const windowEl = section.querySelector(".recommended-window");
            const track = section.querySelector(".recommended-track");
            const btnLeft = section.querySelector(".scroll-btn.left");
            const btnRight = section.querySelector(".scroll-btn.right");

            function getCardWidth() {
                const card = track.querySelector(".book-card");
                if (!card) return 0;
                const style = getComputedStyle(card);
                return card.offsetWidth + parseFloat(style.marginLeft) + parseFloat(style.marginRight);
            }

            const cards = [...track.children];
            const cardWidth = getCardWidth();

            // Clone for infinite loop
            cards.forEach(card => track.appendChild(card.cloneNode(true)));
            cards.slice().reverse().forEach(card => track.insertBefore(card.cloneNode(true), track.firstChild));

            const startPos = cards.length * cardWidth;
            windowEl.scrollLeft = startPos;

            function getVisibleCount() {
                const windowWidth = windowEl.offsetWidth;
                return Math.floor(windowWidth / getCardWidth());
            }

            function move(dir) {
                windowEl.scrollBy({
                    left: dir * cardWidth * getVisibleCount(),
                    behavior: "smooth"
                });

                setTimeout(() => {
                    if (windowEl.scrollLeft <= 0) {
                        windowEl.scrollLeft = startPos;
                    } else if (windowEl.scrollLeft >= track.scrollWidth - windowEl.offsetWidth) {
                        windowEl.scrollLeft = startPos;
                    }
                }, 400);
            }

            // Auto-scroll
            let autoPlayInterval = setInterval(() => move(1), 5000);
            function resetAutoPlay() {
                clearInterval(autoPlayInterval);
                autoPlayInterval = setInterval(() => move(1), 5000);
            }

            btnRight.addEventListener("click", () => { move(1); resetAutoPlay(); });
            btnLeft.addEventListener("click", () => { move(-1); resetAutoPlay(); });

            window.addEventListener("resize", () => {
                windowEl.scrollLeft = startPos;
                resetAutoPlay();
            });
        }

        // Khởi tạo
        initSlider("topSellingSection");
        initSlider("recommendedSection");
    });
</script>

</body>
