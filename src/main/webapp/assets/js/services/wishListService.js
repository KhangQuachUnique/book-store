import { fetchUtil } from "../fetchUtil.js";

export const wishListApi = {
    getWishList: async () => {
        return await fetchUtil('/wishlist', 'GET');
    },
    addToWishList: async (item) => {
        return await fetchUtil('/wishlist', 'POST', item);
    },
    removeFromWishList: async (itemId) => {
        return await fetchUtil(`/wishlist`, 'DELETE');
    }
}
