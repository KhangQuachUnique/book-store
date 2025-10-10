<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Áp dụng khuyến mãi</title>
    <link rel="stylesheet" href="<c:url value='/assets/styles/promotion.css'/>">
</head>
<body>
<h2>Áp dụng mã khuyến mãi</h2>

<form action="${pageContext.request.contextPath}/apply-promotion" method="post">
    <div class="promo-wrapper">
        <input id="promoInput" type="text" name="code" placeholder="Nhập hoặc chọn mã khuyến mãi..." required
               autocomplete="off">
        <div id="promoList" class="promo-list">
            <c:forEach var="promo" items="${validPromotions}">
                <div class="promo-item" onmousedown="selectPromo('${promo.code}')">
                    <strong>${promo.code}</strong> – Giảm ${promo.discount}%
                    <span class="expire">(HSD: <fmt:formatDate value="${promo.expireAtDate}"
                                                               pattern="dd/MM/yyyy"/>)</span>
                </div>
            </c:forEach>
        </div>
    </div>
    <button type="submit">Áp dụng</button>
</form>

<c:if test="${not empty promotionResult}">
    <div class="result-box">
        <c:choose>
            <c:when test="${promotionResult.success}">
                <p class="success">${promotionResult.message}</p>
                <p>Mã: <strong>${promotionResult.promotionCode}</strong></p>
                <p>Giảm giá: <strong><fmt:formatNumber value="${promotionResult.discountPercent}" type="number"
                                                       maxFractionDigits="1"/>%</strong></p>
                <p>Tổng tiền:
                    <fmt:formatNumber value="${promotionResult.subtotal}" type="number" groupingUsed="true"/> ₫
                </p>
                <p>Giảm được:
                    <fmt:formatNumber value="${promotionResult.discountAmount}" type="number" groupingUsed="true"/> ₫
                </p>
                <p><strong>Thành tiền:
                    <fmt:formatNumber value="${promotionResult.finalTotal}" type="number" groupingUsed="true"/> ₫
                </strong></p>
            </c:when>
            <c:otherwise>
                <p class="error">${promotionResult.message}</p>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<!-- ✅ JS: hiển thị danh sách khi click và chọn mã -->
<script>
    const promoInput = document.getElementById("promoInput");
    const promoList = document.getElementById("promoList");

    promoInput.addEventListener("focus", () => {
        promoList.style.display = "block";
    });

    promoInput.addEventListener("blur", () => {
        setTimeout(() => promoList.style.display = "none", 150);
    });

    function selectPromo(code) {
        promoInput.value = code;
        promoList.style.display = "none";
    }
</script>

</body>
</html>
