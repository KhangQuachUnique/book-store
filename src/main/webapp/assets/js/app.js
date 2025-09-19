// Tự động lấy context path từ URL hiện tại
const contextPath = window.location.pathname.split("/")[1];
const BASE_URL = contextPath ? `/${contextPath}` : "";

const USER_URL = `${BASE_URL}/user`

// API endpoints
const USER = {
    login: `${USER_URL}/login`,
    register: `${USER_URL}/register`,
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
// async function logout() {
//     try {
//         const res = await fetch(USER.logout, {
//             method: "POST",
//             credentials: "include"
//         });
//         const data = await res.json();
//
//         alert(data.message);
//         window.location.href = BASE_URL + "/home"
//         const userInfo = document.getElementById("userInfo");
//         if (userInfo) userInfo.innerText = "Not logged in";
//     } catch (err) {
//         console.error(err);
//     }
// }
async function logout() {
    try {
        const res = await fetch(USER.logout, {
            method: "POST",
            credentials: "include"
        });

        if (!res.ok) {
            console.error("Logout failed:", res.status);
            alert("Logout failed: " + res.status);
            return;
        }

        // Chỉ parse JSON khi chắc chắn server trả về application/json
        const contentType = res.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
            const data = await res.json();
            alert(data.message);
        } else {
            console.warn("Response is not JSON, skipping parse");
        }

        window.location.href = BASE_URL + "/home";
        const userInfo = document.getElementById("userInfo");
        if (userInfo) userInfo.innerText = "Not logged in";

    } catch (err) {
        console.error(err);
    }
}


// Check user status
// async function checkUserStatus() {
//     try {
//         console.log("Checking user status...")
//         const res = await fetch(USER.profile, {
//             method: "GET",
//             credentials: "include" // cookie sẽ tự được gửi
//         });
//
//         const data = await res.json(); // parse JSON
//         console.log(data);
//
//         if (data.status === 401) {
//             // Nếu token hết hạn → gọi refresh
//             console.log("Access token expired, refreshing...")
//             const refreshed = await refreshAccessToken();
//             return refreshed ? checkUserStatus() : { loggedIn: false };
//         }
//         return data
//     } catch (err) {
//         console.error(err);
//         return { loggedIn: false };
//     }
// }
async function checkUserStatus() {
    try {
        console.log("Checking user status...");
        const res = await fetch(USER.profile, {
            method: "GET",
            credentials: "include"
        });

        if (!res.ok) {
            console.warn("Profile fetch failed:", res.status);
            if (res.status === 401) {
                console.log("Access token expired, refreshing...");
                const refreshed = await refreshAccessToken();
                return refreshed ? checkUserStatus() : { loggedIn: false };
            }
            return { loggedIn: false };
        }

        const data = await res.json();
        console.log(data);
        return data;
    } catch (err) {
        console.error(err);
        return { loggedIn: false };
    }
}


// Refresh token (dùng cookie refresh_token)
async function refreshAccessToken() {
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
