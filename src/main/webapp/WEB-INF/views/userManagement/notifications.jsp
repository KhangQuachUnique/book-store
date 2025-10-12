<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Thông Báo Của Bạn</title>
    <!-- Header/global styles so the header renders properly -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/header.css">
    <fmt:setLocale value="vi_VN"/>
    <!-- Optional: ensure timezone is Vietnam local time -->
    <fmt:setTimeZone value="Asia/Ho_Chi_Minh"/>
    <style>
        /* CSS cho trang thông báo */
        .notifications-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .notifications-container h1 {
            color: var(--primary-color);
            margin-bottom: 1.5rem;
            text-align: center;
        }
        .notification-list {
            list-style-type: none;
            padding: 0;
        }
        .notification-item {
            display: flex;
            align-items: flex-start;
            padding: 1rem;
            border-bottom: 1px solid #eba3a3;
            transition: background-color 0.3s;
        }
        .notification-item:last-child {
            border-bottom: none;
        }
        .notification-item.unread {
            background-color: var(--secondary-color); /* Màu nền cho thông báo chưa đọc */
            font-weight: bold;
        }
        .notification-icon {
            font-size: 1.5rem;
            margin-right: 1rem;
            color: var(--primary-color);
            padding-top: 5px;
        }
        .notification-content {
            flex-grow: 1;
        }
        .notification-message {
            color: var(--text-display);
            margin: 0;
            white-space: pre-line; /* preserve newlines from DB (\n, \r\n) */
            
        }
        .notification-time {
            font-size: 0.85rem;
            color: #888;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <div class="notifications-container">
        <h1><i class="fas fa-bell"></i> Thông Báo</h1>
        <ul class="notification-list">
            <c:choose>
                <c:when test="${not empty notifications}">
                    <c:forEach var="n" items="${notifications}">
                        <%-- Thêm class 'unread' nếu is_read = false --%>
                        <li class="notification-item ${not n.isRead ? 'unread' : ''}">
                            <div class="notification-icon">
                                <i class="fas fa-receipt"></i> </div>
                            <div class="notification-content">
                                <h4>${n.title}</h4><br/>
                                <p class="notification-message">${n.message}</p>
                                <div class="notification-time">
                                    <fmt:formatDate value="${n.createdAt}" pattern="d 'tháng' M 'năm' yyyy 'lúc' HH:mm:ss"/>
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <li class="notification-item" style="justify-content: center;">
                        <p>Bạn không có thông báo nào.</p>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</body>
</html>