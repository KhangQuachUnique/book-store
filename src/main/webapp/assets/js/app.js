// Scope variables to avoid global re-declaration across multiple pages
(function () {
    // Prefer server-injected context if available
    const injected = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : null;
    const contextPath = injected ?? (window.location.pathname.split("/")[1] ? `/${window.location.pathname.split("/")[1]}` : "");
    const BASE_URL = contextPath;

    const USER_URL = `${BASE_URL}/user`;

    // API endpoints
    const USER = {
        login: `${BASE_URL}/login`,
        register: `${BASE_URL}/register`,
        refresh: `${USER_URL}/refresh`,
        logout: `${USER_URL}/logout`,
        profile: `${USER_URL}/profile`
    };

    document.addEventListener("DOMContentLoaded", () => {
        const spinner = document.getElementById("spinner");

        function showMessage(el, msg, success = true) {
            el.innerText = msg;
            el.style.color = success ? "green" : "red";
        }

        async function handleForm(formId, apiUrl, resultId, getPayload, options = {}) {
            const form = document.getElementById(formId);
            const resultEl = document.getElementById(resultId);
            if (!form) return;

            const { isLogin = false, isRegister = false } = options;

            form.addEventListener("submit", async (e) => {
                e.preventDefault();
                spinner.style.display = "block";
                showMessage(resultEl, "Processing...", true);

                try {
                    const payload = getPayload();
                    const res = await fetch(apiUrl, {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify(payload),
                        credentials: "include"
                    });

                    const data = await res.json();
                    if (res.ok) {
                        showMessage(resultEl, data.message || "Success", true);
                        if (isLogin) {
                            // redirect về root sau khi login thành công
                            setTimeout(() => window.location.href = BASE_URL + "/home", 700);
                        }
                    } else {
                        showMessage(resultEl, data.error || "Failed", false);
                    }

                } catch (err) {
                    console.error(err);
                    showMessage(resultEl, "Server error, try again", false);
                } finally {
                    spinner.style.display = "none";
                }
            });
        }

        // Register
        handleForm("registerForm", USER.register, "registerResult", () => ({
            name: document.getElementById("regName").value,
            email: document.getElementById("regEmail").value,
            password: document.getElementById("regPassword").value,
            phone: document.getElementById("regPhone").value
        }), { isRegister: true });

        // Login
        handleForm("loginForm", USER.login, "loginResult", () => ({
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        }), { isLogin: true });
    });


    // Logout
    window.logout = async function logout() {
        try {
            const res = await fetch(USER.logout, {
                method: "POST",
                credentials: "include"
            });
            if (!res.ok) throw new Error("Logout failed");

            const data = await res.json();

            // Cập nhật UI trước khi redirect
            const userInfo = document.getElementById("userInfo");
            if (userInfo) userInfo.innerText = "Not logged in";

            // Hiển thị thông báo (nếu muốn)
            console.log(data.message); // hoặc show ở UI thay cho alert

            // Chuyển về home
            window.location.href = BASE_URL + "/home";

        } catch (err) {
            console.error("Logout error:", err);
            alert("Something went wrong while logging out.");
        }
    }


    // Check user status
    window.checkUserStatus = async function checkUserStatus() {
        try {
            console.log("Checking user status...")
            const res = await fetch(USER.profile, {
                method: "GET",
                credentials: "include" // cookie sẽ tự được gửi
            });

            const data = await res.json(); // parse JSON
            console.log(data);

            if (data.status === 401) {
                // Nếu token hết hạn → gọi refresh
                console.log("Access token expired, refreshing...")
                const refreshed = await refreshAccessToken();
                return refreshed ? checkUserStatus() : { loggedIn: false };
            }
            return data
        } catch (err) {
            console.error(err);
            return { loggedIn: false };
        }
    }

    // Refresh token (dùng cookie refresh_token)
    window.refreshAccessToken = async function refreshAccessToken() {
        try {
            const res = await fetch(USER.refresh, {
                method: "POST",
                credentials: "include"
            });

            console.log(res.ok)
            console.log(res)

            return res.ok;
        } catch (err) {
            console.error(err);
            return false;
        }
    }

    // expose BASE_URL if needed elsewhere
    window.BASE_URL = window.BASE_URL || (function () { return contextPath; })();
})();
