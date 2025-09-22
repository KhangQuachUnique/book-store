<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>s</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/recommendedBooks.css">
</head>
<body>
    <div class="recommended-section">
        <p>You may also like</p>
        <div class="recommended-container">
            <button class="scroll-btn left">&#10094;</button>

            <div class="recommended-window">
                <div class="recommended-track" id="recommendedTrack">
                    <c:forEach items="${recommendedBooks}" var="book">
                        <div class="book-card small-card">
                            <a href="${pageContext.request.contextPath}/book-detail?id=${book.id}" class="book-link">
                                <div class="book-image">
                                    <img src="${book.thumbnailUrl}" alt="${book.title}" class="book-thumbnail small-thumb">
                                </div>
                                <div class="book-info">
                                    <h4 class="book-title">${book.title}</h4>
                                    <div class="book-price-row column-price">
                                        <c:choose>
                                            <c:when test="${book.discount_rate > 0}">
                                            <span class="book-price-badge">
                                                <fmt:formatNumber value="${book.price * (100 - book.discount_rate) / 100}" type="number"/> VND
                                            </span>
                                            <div class="original-discount">
                                                <span class="original-price">
                                                    <fmt:formatNumber value="${book.price}" type="number"/> VND
                                                </span>
                                                <span class="discount">-${book.discount_rate}%</span>
                                            </div>
                                            </c:when>
                                            <c:otherwise>
                                            <span class="book-price-badge">
                                                <fmt:formatNumber value="${book.price}" type="number"/> VND
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

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const windowEl = document.querySelector(".recommended-window");
            const track = document.getElementById("recommendedTrack");
            const btnLeft = document.querySelector(".scroll-btn.left");
            const btnRight = document.querySelector(".scroll-btn.right");

            function getCardWidth() {
                const card = track.querySelector(".book-card");
                if (!card) return 0;
                const style = getComputedStyle(card);
                return card.offsetWidth + parseFloat(style.marginLeft) + parseFloat(style.marginRight);
            }

            function getVisibleCount() {
                const windowWidth = windowEl.offsetWidth;
                return Math.floor(windowWidth / getCardWidth());
            }

            // ===== Clone phần tử để tạo hiệu ứng vô hạn =====
            const cards = [...track.children];
            const cardWidth = getCardWidth();

            // clone sang cuối
            cards.forEach(card => {
                track.appendChild(card.cloneNode(true));
            });

            // clone sang đầu
            cards.slice().reverse().forEach(card => {
                track.insertBefore(card.cloneNode(true), track.firstChild);
            });

            // đặt scroll ban đầu ở giữa (vùng sách gốc)
            const startPos = cards.length * cardWidth;
            windowEl.scrollLeft = startPos;

            function move(dir) {
                windowEl.scrollBy({
                    left: dir * cardWidth * getVisibleCount(),
                    behavior: "smooth"
                });

                // Sau khi cuộn, nếu vượt quá giới hạn -> reset lại
                setTimeout(() => {
                    if (windowEl.scrollLeft <= 0) {
                        windowEl.scrollLeft = startPos;
                    } else if (windowEl.scrollLeft >= track.scrollWidth - windowEl.offsetWidth) {
                        windowEl.scrollLeft = startPos;
                    }
                }, 400);
            }

            // ===== Auto-play sau 3s =====
            let autoPlayInterval = setInterval(() => move(1), 5000);

            function resetAutoPlay() {
                clearInterval(autoPlayInterval);
                autoPlayInterval = setInterval(() => move(1), 5000);
            }

            btnRight.addEventListener("click", () => {
                move(1);
                resetAutoPlay();
            });

            btnLeft.addEventListener("click", () => {
                move(-1);
                resetAutoPlay();
            });

            // === Xử lý resize (nếu thay đổi kích thước cửa sổ) ===
            window.addEventListener("resize", () => {
                windowEl.scrollLeft = startPos;
                resetAutoPlay();
            });
        });
    </script>



</body>
</html>
