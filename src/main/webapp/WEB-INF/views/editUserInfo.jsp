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
        <div class="address-container">
            <input class="input-address"
                   type="text"
                   id="address"
                   name="address"
                   value="${sessionScope.defaultAddress}" readonly />

            <button type="button" class="dropdown-toggle">▼</button>
        </div>

        <div class="address-list" id="addressList" style="display:none;">
            <c:forEach var="addr" items="${sessionScope.otherAddresses}">
                <div class="address-item">
                    <label>
                        <input type="radio" name="addrRadio" class="addr-radio" value="${addr}">
                            ${addr}
                    </label>
                </div>
            </c:forEach>
            <div class="address-item add-address">➕ Add new address</div>
        </div>

        <input class="submit" type="submit" value="Confirm update" />
    </form>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const toggleBtn = document.querySelector(".dropdown-toggle");
        const list = document.getElementById("addressList");
        const input = document.getElementById("address");
        const defaultAddress = "${sessionScope.defaultAddress}";

        // Toggle hiển thị danh sách
        toggleBtn.addEventListener("click", () => {
            list.style.display = list.style.display === "block" ? "none" : "block";
        });

        // Khi chọn radio
        list.addEventListener("change", (e) => {
            if (e.target.classList.contains("addr-radio")) {
                input.value = e.target.value;
            }
        });

        // Add new address
        list.addEventListener("click", (e) => {
            if (e.target.classList.contains("add-address")) {
                alert("TODO: mở popup hoặc redirect để thêm địa chỉ mới");
            }
        });

        // Click ngoài thì đóng list
        document.addEventListener("click", (e) => {
            if (!list.contains(e.target) && !toggleBtn.contains(e.target)) {
                list.style.display = "none";
            }
        });
    });
</script>

</body>
</html>
