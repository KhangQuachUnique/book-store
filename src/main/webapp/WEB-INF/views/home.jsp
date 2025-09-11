<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="home">
    <h1>Welcome to My Web App</h1>

    <p>
        Đây là trang <b>Home</b>. Nội dung của nó sẽ được nhúng
        vào `layout.jsp` qua <code>contentPage</code>.
    </p>

    <hr/>

    <!-- Ví dụ danh sách -->
    <h2>Tin tức mới</h2>
    <ul>
        <c:forEach var="item" items="${newsList}">
            <li>${item.title} - ${item.date}</li>
        </c:forEach>
    </ul>

    <!-- Nếu chưa có dữ liệu -->
    <c:if test="${empty newsList}">
        <p><i>Chưa có tin tức nào.</i></p>
    </c:if>
</section>
