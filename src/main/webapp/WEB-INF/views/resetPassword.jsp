<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Reset Password - BookStore</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/form.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/header.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/footer.css">
        </head>

        <body>
            <div class="form-container">
                <h2 class="form-title">Reset Password</h2>
                <p style="text-align: center; margin-bottom: 25px; color: var(--text-secondary);">Enter your new
                    password below.</p>

                <form id="resetForm">
                    <input type="hidden" name="token" value="${token}">

                    <div class="form-group">
                        <label for="password">New Password:</label>
                        <input type="password" id="password" name="password" required minlength="6">
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Confirm Password:</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6">
                    </div>

                    <button type="submit" class="btn-submit">Reset Password</button>
                </form>

                <div id="spinner" class="spinner" style="display: none;"></div>
                <div id="message" class="form-result"></div>

                <p class="register-link">
                    Remember your password? <a href="${pageContext.request.contextPath}/login">Back to Login</a>
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
                        messageDiv.textContent = 'Passwords do not match';
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
                            messageDiv.textContent = 'An error occurred. Please try again.';
                            messageDiv.className = 'form-result error';
                            messageDiv.style.display = 'block';
                        });
                });
            </script>
        </body>

    </html>
