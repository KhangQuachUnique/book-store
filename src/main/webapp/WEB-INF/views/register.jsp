<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register</title>

    <!-- JS -->
    <script src="<%=request.getContextPath()%>/assets/js/app.js" defer></script>

    <!-- CSS global & form -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/styles/global.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/styles/index.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/styles/header.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/styles/footer.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/styles/form.css">

</head>
<body>
<div class="form-container">
    <h2 class="form-title">Register</h2>
    <form id="registerForm" class="login-form">
        <div class="form-group">
            <label for="regEmail">Email:</label> <input type="email"
                                                        id="regEmail" name="email" required>
        </div>
        <div class="form-group">
            <label for="regPassword">Password:</label> <input type="password"
                                                              id="regPassword" name="password" required>
        </div>
        <div class="form-group">
            <label for="regName">Full Name:</label> <input type="text"
                                                           id="regName" name="name" required>
        </div>
        <div class="form-group">
            <label for="regPhone">Phone (optional):</label> <input type="text"
                                                                   id="regPhone" name="phone">
        </div>

        <button type="submit" class="btn-submit">Register</button>
    </form>

    <p class="register-link">
        Already have an account? <a
            href="<%=request.getContextPath()%>/user/login">Login</a>
    </p>

    <div id="spinner" class="spinner" style="display: none;"></div>
    <div id="registerResult" class="form-result"></div>
</div>
</body>
</html>
