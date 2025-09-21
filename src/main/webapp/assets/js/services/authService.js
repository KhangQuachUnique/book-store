import { fetchUtil } from "../fetchUtil.js";

const injected = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : null;
const contextPath = injected ?? (window.location.pathname.split("/")[1] ? `/${window.location.pathname.split("/")[1]}` : "");
const BASE_URL = contextPath;

export const authApi = {
    login: async (payload) =>
        fetchUtil(BASE_URL + '/login', 'POST',
            {
                email: payload.email,
                password: payload.password
            }),
    register: async (payload) =>
        fetchUtil(BASE_URL + '/register', 'POST',
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

