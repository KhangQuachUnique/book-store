<%--
  Created by IntelliJ IDEA.
  User: kadfw
  Date: 9/15/2025
  Time: 12:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Oops...</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/index.css">
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            font-family: Montserrat, sans-serif;
            height: 100vh;
            text-align: center;
            background-color: #ffffff;
            color: var(--text-secondary);
        }
        h1 {
            font-size: 150px;
            font-weight: 900;
        }
        h2 {
            font-size: 40px;
        }
        p {
            font-size: 20px;
        }
        a {
            margin-top: 30px;
            display: inline-block;
            padding: 10px 20px;
            font-size: 18px;
            font-weight: 700;
            color: #fff;
            background-color: var(--primary-color);
            text-decoration: none;
            border-radius: 5px;
            transition: transform 0.2s, background-color 0.2s;
        }
        a:hover {
            background-color: var(--primary-color-hover);
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <h1>404</h1>
    <h2>Page Not Found</h2>
    <p>The page you are looking for does not exist.</p>
    <a href="${pageContext.request.contextPath}/home">Return to Home</a>
</body>
</html>
