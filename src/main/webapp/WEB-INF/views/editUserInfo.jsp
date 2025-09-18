<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head class="profile-section">
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/userInfo.css">
</head>
<body>
<h2 class="title">Change Your Profile</h2>

<div class="container">
    <form class="form_input" action="${pageContext.request.contextPath}/user/update" method="post">
        <input type="hidden" name="action" value="changeUserInfo"/>

        <label for="name" class="input-label">Full name:</label><br/>
        <input class="input-value" type="text" id="name" name="name" value="${sessionScope.user.name}" required/><br/>

        <label for="phone" class="input-label">Phone:</label><br/>
        <input class="input-value" type="text" id="phone" name="phone" value="${sessionScope.user.phone}" /><br/>

        <label for="email" class="input-label">Email:</label><br/>
        <input type="email" id="email" name="email" value="${sessionScope.user.email}" readonly/><br/>

        <label for="address" class="input-label">Address:</label><br/>
        <input class="input-value" type="text" id="address" name="address" value="${sessionScope.defaultAddress}" /><br/>

        <input class="submit" type="submit" value="Confirm update" />
    </form>
</div>

</body>
</html>
