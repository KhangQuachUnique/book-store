import { fetchUtil } from "../fetchUtil.js";

const injected = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : null;
const contextPath = injected ?? (window.location.pathname.split("/")[1] ? `/${window.location.pathname.split("/")[1]}` : "");
const BASE_URL = contextPath;

export const wishListApi = {
    getWishList: async () => {
        return await fetchUtil(BASE_URL + '/user/wishlist', 'GET');
    },
    addToWishList: async (body) => {
        const response = await fetchUtil(BASE_URL + '/user/wishlist', 'POST', { bookId: body.itemId });
        console.log("Add to wishlist response:", response);
        return response;
    },
    removeFromWishList: async (body) => {
        const response = await fetchUtil(BASE_URL + '/user/wishlist', 'DELETE', { bookId: body.itemId });
        console.log("Remove from wishlist response:", response);
        return response;
    }
}
