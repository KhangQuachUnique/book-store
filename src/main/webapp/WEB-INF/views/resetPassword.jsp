<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password - BookStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/form.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h2>Reset Password</h2>
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
                
                <button type="submit">Reset Password</button>
                
                <div id="message" class="message" style="display: none;"></div>
            </form>
            
            <p><a href="${pageContext.request.contextPath}/login">Back to Login</a></p>
        </div>
    </div>

    <script>
        document.getElementById('resetForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const token = document.querySelector('input[name="token"]').value;
            const messageDiv = document.getElementById('message');
            
            if (password !== confirmPassword) {
                messageDiv.textContent = 'Passwords do not match';
                messageDiv.className = 'message error';
                messageDiv.style.display = 'block';
                return;
            }
            
            const formData = new FormData();
            formData.append('token', token);
            formData.append('password', password);
            formData.append('confirmPassword', confirmPassword);
            
            fetch('${pageContext.request.contextPath}/reset-password', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                messageDiv.textContent = data.message;
                messageDiv.className = data.success ? 'message success' : 'message error';
                messageDiv.style.display = 'block';
                
                if (data.success) {
                    setTimeout(() => {
                        window.location.href = '${pageContext.request.contextPath}/login?reset=success';
                    }, 2000);
                }
            })
            .catch(error => {
                messageDiv.textContent = 'An error occurred. Please try again.';
                messageDiv.className = 'message error';
                messageDiv.style.display = 'block';
            });
        });
    </script>
</body>
</html>