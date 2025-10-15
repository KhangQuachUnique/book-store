<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Quên Mật Khẩu - BookStore</title>
            <link rel="icon" href="${pageContext.request.contextPath}/assets/images/BookieCakeLogo.svg">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/form.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/header.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/footer.css">
        </head>

        <body>
            <div class="form-container">
                <h2 class="form-title">Quên Mật Khẩu</h2>
                <p>Nhập địa chỉ email của bạn và chúng tôi sẽ gửi cho bạn liên kết để đặt lại mật khẩu.</p>

                <form id="forgotPasswordForm">
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" required>
                    </div>

                    <button type="submit" class="btn-submit">Gửi Liên Kết Đặt Lại</button>
                </form>

                <div id="spinner" class="spinner" style="display: none;"></div>
                <div id="forgotPasswordResult" class="form-result"></div>

                <p class="register-link">
                    Nhớ mật khẩu? <a href="${pageContext.request.contextPath}/login">Quay lại Đăng nhập</a>
                </p>
            </div>

            <script>
                document.getElementById('forgotPasswordForm').addEventListener('submit', function (e) {
                    e.preventDefault();

                    const email = document.getElementById('email').value;
                    const spinner = document.getElementById('spinner');
                    const resultDiv = document.getElementById('forgotPasswordResult');

                    console.log('Submitting email:', email); // Debug log

                    spinner.style.display = 'block';
                    resultDiv.textContent = '';
                    resultDiv.className = '';

                    // Use URLSearchParams instead of FormData for better servlet compatibility
                    const params = new URLSearchParams();
                    params.append('email', email);

                    fetch('${pageContext.request.contextPath}/forgot-password', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: params
                    })
                        .then(response => response.json())
                        .then(data => {
                            spinner.style.display = 'none';
                            resultDiv.textContent = data.message;

                            // Set appropriate CSS class based on response
                            if (data.message.includes('sent') || data.message.includes('Reset link') || data.message.includes('gửi') || data.message.includes('Liên kết')) {
                                resultDiv.className = 'form-result success';
                                document.getElementById('forgotPasswordForm').reset();
                            } else {
                                resultDiv.className = 'form-result error';
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error); // Debug log
                            spinner.style.display = 'none';
                            resultDiv.textContent = 'Đã xảy ra lỗi. Vui lòng thử lại.';
                            resultDiv.className = 'form-result error';
                        });
                });
            </script>
        </body>

    </html>
