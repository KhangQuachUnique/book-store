import { authApi } from "../services/authService.js";

const injected = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : null;
const contextPath = injected ?? (window.location.pathname.split("/")[1] ? `/${window.location.pathname.split("/")[1]}` : "");
const BASE_URL = contextPath;

function showResult(message, success = true) {
    // Check if showToast is available, otherwise use console and alert as fallback
    if (typeof showToast === 'function') {
        showToast(message, success ? 'success' : 'error', 3000);
    } else {
        console.log(`Toast: ${message} (${success ? 'success' : 'error'})`);
        alert(message);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const spinner = document.getElementById("spinner");

    checkUserStatus().then(res => {
        console.log("User status:", res);
        const userInfo = document.getElementById("userInfo");
        if (userInfo) {
            if (res.ok) {
                userInfo.innerText = `Logged in as: ${res.email || 'User'}`;
            } else {
                userInfo.innerText = "Not logged in";
            }
        }
    });

    function showMessage(el, msg, success = true) {
        el.innerText = msg;
        el.style.color = success ? "green" : "red";
    }

    async function handleForm(formId, resultId, fetchApi, payload, shouldRedirect = false) {
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
                    // Success cases - SC_OK (200)
                    const successMsg = data.message || "Success";
                    showMessage(resultEl, successMsg, true);
                    showResult(successMsg, true);
                    
                    // Only redirect for login, not for registration
                    if (shouldRedirect) {
                        setTimeout(() => window.location.href = BASE_URL + "/home", 700);
                    }
                } else {
                    // Error cases từ servlet
                    let errorMsg = "Failed";
                    
                    switch (res.status) {
                        case 400: // SC_BAD_REQUEST
                            if (data.error && data.error.includes("already exists")) {
                                errorMsg = "Email already exists. Please use a different email";
                            } else {
                                errorMsg = data.error || "Invalid input data";
                            }
                            break;
                        case 401: // SC_UNAUTHORIZED - Login invalid
                            errorMsg = "Invalid email or password";
                            break;
                        case 403: // SC_FORBIDDEN - Unverified/Blocked
                            if (data.error && data.error.includes("not verified")) {
                                errorMsg = "Please check your email to verify your account";
                            } else if (data.error && data.error.includes("blocked")) {
                                if (data.blockedUntil) {
                                    errorMsg = `Account is blocked until ${data.blockedUntil}`;
                                } else {
                                    errorMsg = "Account is blocked";
                                }
                            } else {
                                errorMsg = data.error || "Access denied";
                            }
                            break;
                        case 500: // SC_INTERNAL_SERVER_ERROR
                            if (data.error && data.error.includes("email could not be sent")) {
                                errorMsg = "Account created but verification email failed to send. Please contact support";
                            } else {
                                errorMsg = data.error || "Server error. Please try again later";
                            }
                            break;
                        default:
                            errorMsg = data.error || data.message || "An error occurred";
                    }
                    
                    showMessage(resultEl, errorMsg, false);
                    if (res.status >= 500) {
                        showResult(errorMsg, false);
                    }
                }

            } catch (err) {
                console.error(err);
                const errorMsg = "Connection error. Please try again";
                showMessage(resultEl, errorMsg, false);
                showResult(errorMsg, false);
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



