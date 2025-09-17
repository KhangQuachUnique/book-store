<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>Home</title>
    <script src="<%=request.getContextPath()%>/assets/js/app.js"></script>
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

<script>
    function goToAdminBook() {
        const token = localStorage.getItem("accessToken");
        if (!token) {
            alert("Bạn cần đăng nhập trước");
            return;
        }
        // Chuyển trang
        window.location.href = "<%=request.getContextPath()%>/admin/book";
    }

    document.addEventListener("DOMContentLoaded", async () => {
        const status = await checkUserStatus();
        const userInfo = document.getElementById("userInfo");

        if (status.loggedIn) {
            userInfo.innerText = "Logged in as: " + status.email;
        } else {
            userInfo.innerText = "Not logged in";
        }
    });
</script>

</body>
</html>
