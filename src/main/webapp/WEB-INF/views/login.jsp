<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <html>

        <head>
            <meta charset="UTF-8">
            <title>Login</title>

            <!-- JS -->
            <script src="<%=request.getContextPath()%>/assets/js/app.js" defer></script>

            <link rel="icon" href="${pageContext.request.contextPath}/assets/images/BookieCakeLogo.svg">
            <!-- CSS global & form -->
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/index.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/header.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/footer.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/form.css">
        </head>

        <body>
            <div class="logo">
                <img src="${pageContext.request.contextPath}/assets/images/BookieCakeLogo.svg" alt="Logo">
                <span>Bookie Cake</span>
            </div>
            <div class="form-container">
                <h2 class="form-title">Login</h2>
                <form id="loginForm" class="login-form">
                    <div class="form-group">
                        <label for="email">Email:</label> <input type="email" id="email" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password:</label> <input type="password" id="password" required>
                    </div>
                    <button type="submit" class="btn-submit">Login</button>
                </form>

                <p class="register-link">
                    Not have an account? <a href="<%=request.getContextPath()%>/register">Register</a> |
                    <a href="<%=request.getContextPath()%>/forgot-password">Forgot password?</a>
                </p>


                <div id="spinner" class="spinner" style="display: none;"></div>
                <div id="loginResult" class="form-result"></div>
            </div>
        </body>

    </html>
