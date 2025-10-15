<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng Ký</title>

    <!-- JS -->
    <script src="${pageContext.request.contextPath}/assets/js/toast.js"></script>
    <script type="module" src="${pageContext.request.contextPath}/assets/js/pages/authPage.js"></script></script>

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
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/styles/toast.css">

</head>
<body>
<div class="logo">
    <img src="${pageContext.request.contextPath}/assets/images/BookieCakeLogo.svg" alt="Logo" >
    <span>Bookie Cake</span>
</div>
<div class="form-container">
    <h2 class="form-title">Đăng Ký</h2>

    <%-- Xử lý chuẩn hóa số điện thoại --%>
    <%
        String rawPhone = request.getParameter("phone");
        String phone = null;

        if (rawPhone != null && !rawPhone.isEmpty()) {
            // Loại bỏ ký tự không phải số
            phone = rawPhone.replaceAll("[^0-9]", "");
            // Thêm +84 nếu bắt đầu bằng 0
            if (phone.startsWith("0")) {
                phone = "+84" + phone.substring(1);
            }
        }
    %>

    <form id="registerForm" class="login-form" method="post">
        <div class="form-group">
            <label for="regEmail">Email:</label>
            <input type="email" id="regEmail" name="email" required>
        </div>
        <div class="form-group">
            <label for="regPassword">Mật khẩu:</label>
            <input type="password" id="regPassword" name="password" required>
        </div>
        <div class="form-group">
            <label for="regName">Họ và tên:</label>
            <input type="text" id="regName" name="name" required>
        </div>
        <div class="form-group">
            <label for="regPhone">Số điện thoại:</label>
            <input type="text" id="regPhone" name="phone" required
                   placeholder="Ví dụ: 0987654321"
                   pattern="0[0-9]{9,10}"
                   title="Nhập số điện thoại hợp lệ, bắt đầu bằng 0, 10 hoặc 11 chữ số"
                <%= phone != null ? "value='" + phone + "'" : "" %>>
        </div>

        <button type="submit" class="btn-submit">Đăng Ký</button>
    </form>

    <p class="register-link">
        Đã có tài khoản? <a
            href="/login">Đăng nhập</a>
    </p>

    <div id="spinner" class="spinner" style="display: none;"></div>
    <div id="registerResult" class="form-result"></div>
</div>
</body>
</html>
