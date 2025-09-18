<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password - BookStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/form.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/footer.css">
</head>
<body>
    <div class="form-container">
        <h2 class="form-title">Forgot Password</h2>
        <p>Enter your email address and we'll send you a link to reset your password.</p>
        
        <form id="forgotPasswordForm">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            
            <button type="submit" class="btn-submit">Send Reset Link</button>
        </form>
        
        <div id="spinner" class="spinner" style="display: none;"></div>
        <div id="forgotPasswordResult" class="form-result"></div>
        
        <p class="register-link">
            Remember your password? <a href="${pageContext.request.contextPath}/login">Back to Login</a>
        </p>
    </div>

    <script>
        document.getElementById('forgotPasswordForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const email = document.getElementById('email').value;
            const spinner = document.getElementById('spinner');
            const resultDiv = document.getElementById('forgotPasswordResult');
            
            spinner.style.display = 'block';
            resultDiv.textContent = '';
            
            const formData = new FormData();
            formData.append('email', email);
            
            fetch('${pageContext.request.contextPath}/forgot-password', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                spinner.style.display = 'none';
                resultDiv.textContent = data.message;
                resultDiv.className = 'form-result success';
                
                // Clear the form on success
                if (data.message.includes('sent')) {
                    document.getElementById('forgotPasswordForm').reset();
                }
            })
            .catch(error => {
                spinner.style.display = 'none';
                resultDiv.textContent = 'An error occurred. Please try again.';
                resultDiv.className = 'form-result error';
            });
        });
    </script>
</body>
</html>