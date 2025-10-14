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
    <title>My Web App</title>
    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/admin.css"><!-- new admin theme styles -->
    <script>
        // Provide context path globally for admin JS (category checks, etc.)
        window.APP_CONTEXT = '${pageContext.request.contextPath}';
    </script>
</head>
<body>
<!-- Sidebar -->
<%@include file="fragments/adminSidebar.jsp" %>

<div class="main-content">
    <!-- Header -->
    <%@ include file="fragments/adminHeader.jsp" %>

    <!-- Main -->
    <main>
        <c:import url="${contentPage}" />
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js" defer></script>
<script src="${pageContext.request.contextPath}/assets/js/toggleSidebar.js" defer></script>
</body>
</html>
