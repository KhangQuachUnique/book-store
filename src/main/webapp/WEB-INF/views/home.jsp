<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>Home</title>
</head>
<body>
<section class="home" style="padding: 60px">
    <h1>Welcome to My Web App</h1>

    <!-- User info -->
    <div id="userInfo">Loading user info...</div>
    <button onclick="logout()">Logout</button>

    <!-- Nút đi tới /admin/book -->
    <button onclick="goToAdminBook()">Quản lý sách</button>


    <hr />

    <!-- Ví dụ danh sách -->
    <h2>Tin tức mới</h2>
    <ul>
        <c:forEach var="item" items="${newsList}">
            <li>${item.title}-${item.date}</li>
        </c:forEach>
    </ul>

    <!-- Nếu chưa có dữ liệu -->
    <c:if test="${empty newsList}">
        <p>
            <i>Chưa có tin tức nào.</i>
        </p>
    </c:if>
</section>

</body>
</html>
