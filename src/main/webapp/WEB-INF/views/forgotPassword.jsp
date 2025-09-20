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
                if (data.message.includes('sent') || data.message.includes('Reset link')) {
                    resultDiv.className = 'form-result success';
                    document.getElementById('forgotPasswordForm').reset();
                } else {
                    resultDiv.className = 'form-result error';
                }
            })
            .catch(error => {
                console.error('Error:', error); // Debug log
                spinner.style.display = 'none';
                resultDiv.textContent = 'An error occurred. Please try again.';
                resultDiv.className = 'form-result error';
            });
        });
    </script>
</body>
</html>