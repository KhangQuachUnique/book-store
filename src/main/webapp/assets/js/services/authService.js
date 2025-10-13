import { fetchUtil } from "../fetchUtil.js";

export const authApi = {
    login: async (payload) =>
        fetchUtil('/login', 'POST',
            {
                email: payload.email,
                password: payload.password
            }),
    register: async (payload) =>
        fetchUtil('/register', 'POST',
            {
                name: payload.name,
                email: payload.email,
                password: payload.password,
                phone: payload.phone
            }),
    refresh: async () => fetchUtil(BASE_URL + '/user/refresh', 'POST'),
    logout: async () => fetchUtil(BASE_URL + '/user/logout', 'POST'),
    checkUserStatus: async () => fetchUtil(BASE_URL + '/user/profile', 'GET')
}

