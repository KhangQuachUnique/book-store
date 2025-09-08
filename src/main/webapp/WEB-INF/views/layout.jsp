<%--
  Created by IntelliJ IDEA.
  User: kadfw
  Date: 9/8/2025
  Time: 8:55 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Web App</title>
    <!-- CSS -->
    <link rel="stylesheet" href="../../assets/styles/index.css">
    <link rel="stylesheet" href="../../assets/styles/header.css">
    <link rel="stylesheet" href="../../assets/styles/sidebar.css">
    <link rel="stylesheet" href="../../assets/styles/footer.css">
</head>
<body>
<!-- Sidebar -->
<%@include file="fragments/sidebar.jspf" %>

<div class="main-content">
    <!-- Header -->
    <%@ include file="fragments/header.jspf" %>

    <!-- Main -->
    <main>
        <jsp:include page="/WEB-INF/views/home.jsp"/>
    </main>

    <!-- Footer -->
    <footer>
        <p>&copy; 2025 My Web App</p>
    </footer>
</div>
<!-- JavaScript -->
<script src="../../assets/js/app.js"></script>
</body>
</html>
