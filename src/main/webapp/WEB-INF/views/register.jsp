<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Register</title>
    <meta charset="UTF-8">
    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/register.css">
    <!-- JS -->
    <script src="<%= request.getContextPath() %>/assets/js/app.js" defer></script>
</head>
<body>
<div class="form-container">
    <h2>Register</h2>
    <form id="registerForm">
        <div class="form-group">
            <label for="regName">Name:</label>
            <input type="text" id="regName" name="name" required>
        </div>

        <div class="form-group">
            <label for="regEmail">Email:</label>
            <input type="email" id="regEmail" name="email" required>
        </div>

        <div class="form-group">
            <label for="regPassword">Password:</label>
            <input type="password" id="regPassword" name="password" required>
        </div>

        <div class="form-group">
            <label for="regPhone">Phone (optional):</label>
            <input type="text" id="regPhone" name="phone">
        </div>

        <button type="submit" class="btn-submit">Register</button>
    </form>
    <div id="registerResult" class="form-result"></div>
</div>
</body>
</html>
