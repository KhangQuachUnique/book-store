import { fetchUtil } from "../fetchUtil.js";

export const wishListApi = {
    getWishList: async () => {
        return await fetchUtil('/wishlist', 'GET');
    },
    addToWishList: async (body) => {
        const response = await fetchUtil('/wishlist', 'POST', { bookId: body.itemId });
        console.log("Add to wishlist response:", response);
        return response;
    },
    removeFromWishList: async (body) => {
        const response = await fetchUtil('/wishlist', 'DELETE', { bookId: body.itemId });
        console.log("Remove from wishlist response:", response);
        return response;
    }
}
