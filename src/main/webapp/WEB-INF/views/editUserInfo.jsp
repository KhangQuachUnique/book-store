<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head class="profile-section">
    <title></title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/userInfo.css">
</head>
<body>
<h2 class="title">Thay Đổi Hồ Sơ Của Bạn</h2>

<div class="container">
    <form class="form_input" action="${pageContext.request.contextPath}/user/update" method="post">
        <input type="hidden" name="action" value="changeUserInfo"/>

        <label for="name" class="input-label">Họ và tên:</label><br/>
        <input class="input-value" type="text" id="name" name="name" value="${sessionScope.user.name}" required/><br/>

        <label for="phone" class="input-label">Số điện thoại:</label><br/>
        <input class="input-value" type="text" id="phone" name="phone" value="${sessionScope.user.phoneNumber}" /><br/>

        <label for="email" class="input-label">Email:</label><br/>
        <input class="email" type="email" id="email" name="email" value="${sessionScope.user.email}" readonly/><br/>

        <label for="address" class="input-label">Địa chỉ:</label><br/>
        <div class="address-container">
            <input class="input-address"
                   type="text"
                   id="address"
                   name="address"
                   value="${sessionScope.defaultAddress}" readonly />

            <button type="button" class="edit-button">Chỉnh sửa</button>
        </div>

        <input class="submit" type="submit" value="Xác nhận cập nhật" />
    </form>
</div>

<div class="modal-overlay" id="modalOverlay">
    <div class="modal-content">
        <span class="modal-close" id="modalClose">&times;</span>
        <h3>Chỉnh Sửa Địa Chỉ</h3>

        <div class="add-new-container">
            <label for="newAddressInput"></label><input type="text" class="add-new-input" id="newAddressInput" placeholder="Nhập địa chỉ mới" />
            <button type="button" class="add-button" id="addButton">Thêm</button>
        </div>

        <div class="address-list" id="addressList">
            <c:forEach var="addr" items="${sessionScope.user.addresses}">
                    <div class="address-item" data-id="${addr.id}">
                        <label class="addr-label">
                            <input type="radio" name="addrRadio" class="addr-radio" value="${addr.address}">
                                ${addr.address}
                        </label>
                        <c:if test="${addr.address != sessionScope.defaultAddress}">
                        <label class="delete-checkbox-label">
                            <input type="checkbox" name="deleteAddresses" value="${addr.id}" class="delete-checkbox">
                            Xóa
                        </label>
                        </c:if>
                    </div>
            </c:forEach>
        </div>
    </div>
</div>

<div class="modal-overlay" id="confirmOverlay" style="display:none;">
    <div class="modal-content">
        <span class="modal-close" id="confirmClose">&times;</span>
        <h3 id="confirmTitle">Thông báo</h3>
        <p id="confirmMessage"></p>
        <div id="confirmButtons">
            <button id="confirmBtnYes">Xác nhận</button>
            <button id="confirmBtnNo">Hủy</button>
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
        const form = document.querySelector(".form_input");
        const submitBtn = document.querySelector(".submit");

        const confirmOverlay = document.getElementById("confirmOverlay");
        const confirmTitle = document.getElementById("confirmTitle");
        const confirmMessage = document.getElementById("confirmMessage");
        const confirmButtons = document.getElementById("confirmButtons");
        const confirmBtnYes = document.getElementById("confirmBtnYes");
        const confirmBtnNo = document.getElementById("confirmBtnNo");
        const confirmClose = document.getElementById("confirmClose");

        let confirmCallback = null;

        function showModal(title, message, isConfirm = false, callback = null) {
            confirmTitle.textContent = title;
            confirmMessage.textContent = message;
            confirmCallback = callback;
            if (isConfirm) {
                confirmButtons.style.display = 'flex';
            } else {
                confirmButtons.style.display = 'none';
            }
            confirmOverlay.style.display = "flex";
            setTimeout(() => { confirmOverlay.classList.add("show"); }, 10);
        }

        function closeConfirmModal() {
            confirmOverlay.classList.remove("show");
            setTimeout(() => { confirmOverlay.style.display = "none"; }, 300);
            confirmCallback = null;
        }

        confirmBtnYes.addEventListener("click", () => {
            if (confirmCallback) confirmCallback();
            closeConfirmModal();
        });

        confirmBtnNo.addEventListener("click", closeConfirmModal);
        confirmClose.addEventListener("click", closeConfirmModal);

        document.addEventListener("click", (e) => {
            if (e.target === confirmOverlay) {
                closeConfirmModal();
            }
        });

        editBtn.addEventListener("click", () => {
            modalOverlay.style.display = "flex";
            setTimeout(() => { modalOverlay.classList.add("show"); }, 10);
            newAddressInput.value = "";
        });

        modalClose.addEventListener("click", () => {
            modalOverlay.classList.remove("show");
            setTimeout(() => { modalOverlay.style.display = "none"; }, 300);
        });

        list.addEventListener("change", (e) => {
            if (e.target.classList.contains("addr-radio")) {
                const item = e.target.closest(".address-item");
                const checkbox = item.querySelector(".delete-checkbox");
                if (e.target.checked) {
                    checkbox.disabled = true;
                    checkbox.checked = false;
                } else {
                    checkbox.disabled = false;
                }
                input.value = e.target.value;
                modalOverlay.classList.remove("show");
                setTimeout(() => { modalOverlay.style.display = "none"; }, 300);
            } else if (e.target.classList.contains("delete-checkbox")) {
                const item = e.target.closest(".address-item");
                const radio = item.querySelector(".addr-radio");
                if (e.target.checked) {
                    radio.disabled = true;
                    radio.checked = false;
                } else {
                    radio.disabled = false;
                }
            }
        });

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
                showModal("Thông báo", "Vui lòng nhập địa chỉ mới.");
            }
        });

        submitBtn.addEventListener("click", (e) => {
            const checkboxes = list.querySelectorAll(".delete-checkbox:checked");
            if (checkboxes.length > 0) {
                showModal("Xác nhận", "Bạn có chắc muốn xóa các địa chỉ đã chọn?", true, () => {
                    checkboxes.forEach(cb => {
                        form.appendChild(cb.cloneNode(true));
                    });
                    form.submit();
                });
                e.preventDefault();
            }
        });

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