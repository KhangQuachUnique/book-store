import { authApi } from "../services/authService.js";

const injected = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : null;
const contextPath = injected ?? (window.location.pathname.split("/")[1] ? `/${window.location.pathname.split("/")[1]}` : "");
const BASE_URL = contextPath;

function showResult(message, success = true) {
    showToast(message, success ? 'success' : 'error', 3000);
}

document.addEventListener("DOMContentLoaded", () => {
    const spinner = document.getElementById("spinner");

    function showMessage(el, msg, success = true) {
        el.innerText = msg;
        el.style.color = success ? "green" : "red";
    }

    async function handleForm(formId, resultId, fetchApi, payload) {
        const form = document.getElementById(formId);
        const resultEl = document.getElementById(resultId);
        if (!form) return;

        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            spinner.style.display = "block";
            showMessage(resultEl, "Processing...", true);

            try {
                const res = await fetchApi(payload());

                const data = await res.json();

                if (res.ok) {
                    showMessage(resultEl, data.message || "Success", true);
                    // redirect về root sau khi login thành công
                    setTimeout(() => window.location.href = BASE_URL + "/home", 700);
                } else {
                    showMessage(resultEl, res.error || "Failed", false);
                }

            } catch (err) {
                console.error(err);
                showMessage(resultEl, "Server error, try again", false);
            } finally {
                spinner.style.display = "none";
            }
        });
    }

    // Login - chỉ bind nếu form tồn tại
    handleForm("loginForm", "loginResult", authApi.login, () => ({
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    }), true);

    // Register - chỉ bind nếu form tồn tại
    handleForm("registerForm", "registerResult", authApi.register, () => ({
        name: document.getElementById("regName").value,
        email: document.getElementById("regEmail").value,
        password: document.getElementById("regPassword").value,
        phone: document.getElementById("regPhone").value
    }));
});


(function () {


// Check user status
    window.checkUserStatus = async function checkUserStatus() {
        try {
            console.log("Checking user status...")
            const res = await authApi.checkUserStatus()

            const data = await res.json();
            console.log(data)
            console.log(res)

            if (res.status === 401) {
                // Nếu token hết hạn → gọi refresh
                console.log("Access token expired, refreshing...")
                const refreshed = await refreshAccessToken();
                return refreshed ? checkUserStatus() : { loggedIn: false };
            }
            return res
        } catch (err) {
            console.error(err);
            return { loggedIn: false };
        }
    }

// Refresh token (dùng cookie refresh_token)
    window.refreshAccessToken = async function refreshAccessToken() {
        try {
            const res = await authApi.refresh();

            console.log(res.ok)
            console.log(res)
            const data = await res.json();

            return res.ok;
        } catch (err) {
            // console.error(err);
            return false;
        }
    }

// expose BASE_URL if needed elsewhere
    window.BASE_URL = window.BASE_URL || (function(){ return contextPath; })();

})();

window.logout = async function logout() {
    try {
        const res = await authApi.logout();
        console.log(res)

        if (!res.ok) showResult("Logout failed", false);

        // Cập nhật UI trước khi redirect
        const userInfo = document.getElementById("userInfo");
        if (userInfo) userInfo.innerText = "Not logged in";

        showResult("Logged out successfully", true);

        // Chuyển về home
        window.location.href = BASE_URL + "/home";

    } catch (err) {
        console.error("Logout error:", err);
        alert("Something went wrong while logging out.");
    }
}



