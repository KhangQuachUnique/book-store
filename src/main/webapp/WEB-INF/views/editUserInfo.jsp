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
        <input class="input-value" type="text" id="phone" name="phone" value="${sessionScope.user.phoneNumber}" /><br/>

        <label for="email" class="input-label">Email:</label><br/>
        <input class="email" type="email" id="email" name="email" value="${sessionScope.user.email}" readonly/><br/>

        <label for="address" class="input-label">Address:</label><br/>
        <div class="address-container">
            <input class="input-address"
                   type="text"
                   id="address"
                   name="address"
                   value="${sessionScope.defaultAddress}" readonly />

            <button type="button" class="edit-button">Edit</button>
        </div>

        <input class="submit" type="submit" value="Confirm update" />
    </form>
</div>

<!-- Modal for address editing -->
<div class="modal-overlay" id="modalOverlay">
    <div class="modal-content">
        <span class="modal-close" id="modalClose">&times;</span>
        <h3>Edit Addresses</h3>
        <!-- Ô nhập địa chỉ mới ở trên cùng -->
        <div class="add-new-container">
            <input type="text" class="add-new-input" id="newAddressInput" placeholder="Enter new address" />
            <button type="button" class="add-button" id="addButton">Add</button>
        </div>
        <!-- Danh sách địa chỉ với scroll -->
        <div class="address-list" id="addressList">
            <c:forEach var="addr" items="${sessionScope.user.addresses}">
                <div class="address-item">
                    <label>
                        <input type="radio" name="addrRadio" class="addr-radio" value="${addr}">
                            ${addr}
                    </label>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const editBtn = document.querySelector(".edit-button");
        const modalOverlay = document.getElementById("modalOverlay");
        const modalClose = document.getElementById("modalClose");
        const list = document.getElementById("addressList");
        const input = document.getElementById("address");
        const newAddressInput = document.getElementById("newAddressInput");
        const addButton = document.getElementById("addButton");
        const defaultAddress = "${sessionScope.defaultAddress}";

        editBtn.addEventListener("click", () => {
            modalOverlay.style.display = "flex";
            setTimeout(() => { modalOverlay.classList.add("show"); }, 10); // Delay để transition
            newAddressInput.value = ""; // Reset input mới
        });

        modalClose.addEventListener("click", () => {
            modalOverlay.classList.remove("show");
            setTimeout(() => { modalOverlay.style.display = "none"; }, 300); // Delay đóng sau transition
        });

        // Chọn địa chỉ từ list
        list.addEventListener("change", (e) => {
            if (e.target.classList.contains("addr-radio")) {
                input.value = e.target.value;
                modalOverlay.classList.remove("show");
                setTimeout(() => { modalOverlay.style.display = "none"; }, 300);
            }
        });

        // Thêm địa chỉ mới
        addButton.addEventListener("click", () => {
            const newAddr = newAddressInput.value.trim();
            if (newAddr) {
                input.removeAttribute("readonly");
                input.setAttribute("required", "true");
                input.value = newAddr;
                input.focus();
                modalOverlay.classList.remove("show");
                setTimeout(() => { modalOverlay.style.display = "none"; }, 300);
            } else {
                alert("Please enter a new address.");
            }
        });

        // Đóng modal khi click ngoài
        document.addEventListener("click", (e) => {
            if (e.target === modalOverlay) {
                modalOverlay.classList.remove("show");
                setTimeout(() => { modalOverlay.style.display = "none"; }, 300);
            }
        });
    });
</script>

</body>
</html>