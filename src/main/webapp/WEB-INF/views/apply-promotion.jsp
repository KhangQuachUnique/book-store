<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Áp dụng khuyến mãi</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #fafafa;
            margin: 50px;
        }

        form {
            margin-bottom: 20px;
        }

        input[type=text] {
            padding: 6px;
            width: 200px;
        }

        button {
            padding: 6px 12px;
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        .success {
            color: green;
            font-weight: bold;
        }

        .error {
            color: red;
            font-weight: bold;
        }

        .result-box {
            background: #fff;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            width: 350px;
        }

        .result-box p {
            margin: 5px 0;
        }
    </style>
</head>
<body>
<h2>Áp dụng mã khuyến mãi</h2>

<form action="${pageContext.request.contextPath}/apply-promotion" method="post">
    <input type="text" name="code" placeholder="Nhập mã khuyến mãi..." required>
    <button type="submit">Áp dụng</button>
</form>

<c:if test="${not empty promotionResult}">
    <div class="result-box">
        <c:choose>
            <c:when test="${promotionResult.success}">
                <p class="success">${promotionResult.message}</p>
                <p>Mã: <strong>${promotionResult.promotionCode}</strong></p>
                <p>Giảm giá: <strong>${promotionResult.discountPercent}%</strong></p>
                <p>Tổng tiền: <fmt:formatNumber value="${promotionResult.subtotal}" type="number" groupingUsed="true"/>
                    ₫</p>
                <p>Giảm được: <fmt:formatNumber value="${promotionResult.discountAmount}" type="number"
                                                groupingUsed="true"/> ₫</p>
                <p><strong>Thành tiền: <fmt:formatNumber value="${promotionResult.finalTotal}" type="number"
                                                         groupingUsed="true"/> ₫</strong></p>
            </c:when>
            <c:otherwise>
                <p class="error">${promotionResult.message}</p>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>
</body>
</html>
