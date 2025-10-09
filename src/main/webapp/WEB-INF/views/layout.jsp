<%--
  Created by IntelliJ IDEA.
  User: kadfw
  Date: 9/8/2025
  Time: 8:55 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bookie Cake</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/BookieCakeLogo.svg">
    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/toast.css">
    <script>
        // Provide context path globally for admin JS (category checks, etc.)
        window.APP_CONTEXT = '${pageContext.request.contextPath}';
    </script>
</head>
<body>
<!-- Sidebar -->
<%@include file="fragments/sidebar.jspf" %>

<div class="main-content">
    <!-- Header -->
    <%@ include file="fragments/header.jspf" %>

    <!-- Main -->
    <main>
        <c:import url="${requestScope.contentPage}" />
    </main>

    <!-- Footer -->
    <%@ include file="fragments/footer.jsp" %>
</div>
<script src="${pageContext.request.contextPath}/assets/js/toggleSidebar.js" defer></script>
<script src="${pageContext.request.contextPath}/assets/js/toast.js" defer></script>
<script type="module" src="${pageContext.request.contextPath}/assets/js/pages/authPage.js"></script>
<script>
    if (!window.ws || window.ws.readyState === WebSocket.CLOSED) {
        window.ws = new WebSocket("ws://localhost:8080/bookiecake/ws/updates");
        window.ws.onopen = () => console.log("Connected");
        window.ws.onmessage = (e) => console.log("Received:", e.data);
        window.ws.onclose = () => console.log("Disconnected");
    }
</script>
</body>
</html>
