// API base url (tự động dùng contextPath)
const BASE_URL = "/book-store-be";
const API = {
    login: BASE_URL + "/login",
    register: BASE_URL + "/register",
    refresh: BASE_URL + "/refresh",
    logout: BASE_URL + "/logout",
    me: BASE_URL + "/me"
};

// Lưu token
function saveTokens(accessToken, refreshToken) {
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
}

// Xóa token
function clearTokens() {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
}

document.addEventListener("DOMContentLoaded", () => {
    // Register
    const registerForm = document.getElementById("registerForm");
    if (registerForm) {
        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            const name = document.getElementById("regName").value;
            const email = document.getElementById("regEmail").value;
            const password = document.getElementById("regPassword").value;
            const phone = document.getElementById("regPhone").value;

            try {
                const res = await fetch(API.register, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ name, email, password, phone })
                });

                const data = await res.json();
                document.getElementById("registerResult").innerText = res.ok
                    ? data.message
                    : (data.error || "Register failed");
            } catch (err) {
                console.error(err);
            }
        });
    }

    // Login
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;

            try {
                const res = await fetch(API.login, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, password })
                });

                const data = await res.json();
                if (res.ok) {
                    document.getElementById("loginResult").innerText = data.message;
                    saveTokens(data.accessToken, data.refreshToken);
                } else {
                    document.getElementById("loginResult").innerText = data.error || "Login failed";
                }
            } catch (err) {
                console.error(err);
            }
        });
    }
});

// Logout
async function logout() {
    try {
        const res = await fetch(API.logout, { method: "POST" });
        const data = await res.json();

        clearTokens();
        alert(data.message);

        // nếu có UI user info thì reset
        const userInfo = document.getElementById("userInfo");
        if (userInfo) userInfo.innerText = "Not logged in";
    } catch (err) {
        console.error(err);
    }
}

// Refresh token
async function refreshToken() {
    const refreshToken = localStorage.getItem("refreshToken");
    if (!refreshToken) return false;

    try {
        const res = await fetch(API.refresh, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ refreshToken })
        });

        const data = await res.json();
        if (res.ok) {
            localStorage.setItem("accessToken", data.accessToken);
            return true;
        } else {
            clearTokens();
            return false;
        }
    } catch (err) {
        console.error(err);
        return false;
    }
}

// Check user status
async function checkUserStatus() {
    const token = localStorage.getItem("accessToken");
    if (!token) return { loggedIn: false };

    try {
        const res = await fetch(API.me, {
            method: "GET",
            headers: { Authorization: "Bearer " + token }
        });

        if (res.status === 401) {
            const refreshed = await refreshToken();
            return refreshed ? checkUserStatus() : { loggedIn: false };
        }
        return res.json();
    } catch (err) {
        console.error(err);
        return { loggedIn: false };
    }
}
