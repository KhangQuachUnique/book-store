<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>

    <!-- JS -->
    <script src="<%= request.getContextPath() %>/assets/js/app.js"></script>

    <!-- CSS global -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/footer.css">

    <!-- CSS riêng cho login -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/login.css">
</head>
<body>
    <div class="login-container">
        <h2 class="login-title">Login</h2>
        <form id="loginForm" class="login-form">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" required>
            </div>
            <button type="submit" class="btn-login">Login</button>
        </form>
        <div id="loginResult" class="login-result"></div>
    </div>
</body>
</html>
