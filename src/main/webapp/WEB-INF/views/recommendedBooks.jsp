<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/recommendedBooks.css">
</head>

<body>
<!-- ================== TOP SELLING BOOKS ================== -->
<c:if test="${not empty requestScope.topSellingBooks}">
    <div class="recommended-section" id="topSellingSection">
        <p>Sách Bán Chạy</p>
        <div class="recommended-container">
            <button class="scroll-btn left">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M169.4 297.4C156.9 309.9 156.9 330.2 169.4 342.7L361.4 534.7C373.9 547.2 394.2 547.2 406.7 534.7C419.2 522.2 419.2 501.9 406.7 489.4L237.3 320L406.6 150.6C419.1 138.1 419.1 117.8 406.6 105.3C394.1 92.8 373.8 92.8 361.3 105.3L169.3 297.3z"></path>
                </svg>
            </button>

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

            <button class="scroll-btn right">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M471.1 297.4C483.6 309.9 483.6 330.2 471.1 342.7L279.1 534.7C266.6 547.2 246.3 547.2 233.8 534.7C221.3 522.2 221.3 501.9 233.8 489.4L403.2 320L233.9 150.6C221.4 138.1 221.4 117.8 233.9 105.3C246.4 92.8 266.7 92.8 279.2 105.3L471.2 297.3z"></path>
                </svg>
            </button>
        </div>
    </div>
</c:if>

<!-- ================== RECOMMENDED BOOKS ================== -->
<c:if test="${not empty requestScope.recommendedBooks}">
    <div class="recommended-section" id="recommendedSection">
        <p>Có Thể Bạn Cũng Thích</p>
        <div class="recommended-container">
            <button class="scroll-btn left">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M169.4 297.4C156.9 309.9 156.9 330.2 169.4 342.7L361.4 534.7C373.9 547.2 394.2 547.2 406.7 534.7C419.2 522.2 419.2 501.9 406.7 489.4L237.3 320L406.6 150.6C419.1 138.1 419.1 117.8 406.6 105.3C394.1 92.8 373.8 92.8 361.3 105.3L169.3 297.3z"></path>
                </svg>
            </button>

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

            <button class="scroll-btn right">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M471.1 297.4C483.6 309.9 483.6 330.2 471.1 342.7L279.1 534.7C266.6 547.2 246.3 547.2 233.8 534.7C221.3 522.2 221.3 501.9 233.8 489.4L403.2 320L233.9 150.6C221.4 138.1 221.4 117.8 233.9 105.3C246.4 92.8 266.7 92.8 279.2 105.3L471.2 297.3z"></path>
                </svg>
            </button>
        </div>
    </div>
</c:if>

<!-- ================== TOP RATED BOOKS ================== -->
<c:if test="${not empty requestScope.topRatedBooks}">
    <div class="recommended-section" id="topRatedSection">
        <p>Sách Được Đánh Giá Cao Nhất</p>
        <div class="recommended-container">
            <button class="scroll-btn left">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M169.4 297.4C156.9 309.9 156.9 330.2 169.4 342.7L361.4 534.7C373.9 547.2 394.2 547.2 406.7 534.7C419.2 522.2 419.2 501.9 406.7 489.4L237.3 320L406.6 150.6C419.1 138.1 419.1 117.8 406.6 105.3C394.1 92.8 373.8 92.8 361.3 105.3L169.3 297.3z"></path>
                </svg>
            </button>

            <div class="recommended-window">
                <div class="recommended-track">
                    <c:forEach items="${requestScope.topRatedBooks}" var="book">
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

            <button class="scroll-btn right">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640">
                    <path d="M471.1 297.4C483.6 309.9 483.6 330.2 471.1 342.7L279.1 534.7C266.6 547.2 246.3 547.2 233.8 534.7C221.3 522.2 221.3 501.9 233.8 489.4L403.2 320L233.9 150.6C221.4 138.1 221.4 117.8 233.9 105.3C246.4 92.8 266.7 92.8 279.2 105.3L471.2 297.3z"></path>
                </svg>
            </button>
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

        initSlider("topSellingSection");
        initSlider("recommendedSection");
        initSlider("topRatedSection");
    });
</script>

</body>
