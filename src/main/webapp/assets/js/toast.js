// assets/js/toast.js

function showToast(message) {
    const toast = document.getElementById("toast");
    toast.textContent = message;
    toast.classList.add("show");

    setTimeout(() => {
        toast.classList.remove("show");
    }, 3000);
}

// Nếu có biến `TOAST_MESSAGE` thì show luôn khi load trang
window.addEventListener("load", function () {
    if (typeof TOAST_MESSAGE !== "undefined" && TOAST_MESSAGE) {
        showToast(TOAST_MESSAGE);
    }
});
