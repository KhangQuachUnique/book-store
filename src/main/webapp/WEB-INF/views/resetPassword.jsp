<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Đặt Lại Mật Khẩu - BookStore</title>
            <link rel="icon" href="${pageContext.request.contextPath}/assets/images/BookieCakeLogo.svg">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/form.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/header.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/footer.css">
        </head>

        <body>
            <div class="form-container">
                <h2 class="form-title">Đặt Lại Mật Khẩu</h2>
                <p style="text-align: center; margin-bottom: 25px; color: var(--text-secondary);">Nhập mật khẩu mới của bạn bên dưới.</p>

                <form id="resetForm">
                    <input type="hidden" name="token" value="${token}">

                    <div class="form-group">
                        <label for="password">Mật khẩu mới:</label>
                        <input type="password" id="password" name="password" required minlength="6">
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Xác nhận mật khẩu:</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6">
                    </div>

                    <button type="submit" class="btn-submit">Đặt Lại Mật Khẩu</button>
                </form>

                <div id="spinner" class="spinner" style="display: none;"></div>
                <div id="message" class="form-result"></div>

                <p class="register-link">
                    Nhớ mật khẩu? <a href="${pageContext.request.contextPath}/login">Quay lại Đăng nhập</a>
                </p>
            </div>

            <script>
                document.getElementById('resetForm').addEventListener('submit', function (e) {
                    e.preventDefault();

                    const password = document.getElementById('password').value;
                    const confirmPassword = document.getElementById('confirmPassword').value;
                    const token = document.querySelector('input[name="token"]').value;
                    const messageDiv = document.getElementById('message');
                    const spinner = document.getElementById('spinner');

                    console.log('Reset form submission - Token:', token, 'Password length:', password.length); // Debug log

                    if (password !== confirmPassword) {
                        messageDiv.textContent = 'Mật khẩu không khớp';
                        messageDiv.className = 'form-result error';
                        messageDiv.style.display = 'block';
                        return;
                    }

                    // Show spinner and clear previous messages
                    spinner.style.display = 'block';
                    messageDiv.textContent = '';
                    messageDiv.className = 'form-result';
                    messageDiv.style.display = 'block';

                    // Use URLSearchParams instead of FormData for better servlet compatibility
                    const params = new URLSearchParams();
                    params.append('token', token);
                    params.append('password', password);
                    params.append('confirmPassword', confirmPassword);

                    fetch('${pageContext.request.contextPath}/reset-password', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: params
                    })
                        .then(response => response.json())
                        .then(data => {
                            console.log('Reset response:', data); // Debug log
                            spinner.style.display = 'none';
                            messageDiv.textContent = data.message;
                            messageDiv.className = data.success ? 'form-result success' : 'form-result error';
                            messageDiv.style.display = 'block';

                            if (data.success) {
                                setTimeout(() => {
                                    window.location.href = '${pageContext.request.contextPath}/login?reset=success';
                                }, 2000);
                            }
                        })
                        .catch(error => {
                            console.error('Reset error:', error); // Debug log
                            spinner.style.display = 'none';
                            messageDiv.textContent = 'Đã xảy ra lỗi. Vui lòng thử lại.';
                            messageDiv.className = 'form-result error';
                            messageDiv.style.display = 'block';
                        });
                });
            </script>
        </body>

    </html>
