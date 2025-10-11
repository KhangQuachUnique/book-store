import { fetchUtil } from "../fetchUtil.js";

const injected = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : null;
const contextPath = injected ?? (window.location.pathname.split("/")[1] ? `/${window.location.pathname.split("/")[1]}` : "");
const BASE_URL = contextPath;

export const cartApi = {
    /**
     * Add item to cart
     */
    addToCart: async (payload) => {
        const formData = new URLSearchParams();
        formData.append('action', 'add');
        formData.append('bookId', payload.bookId);
        formData.append('quantity', payload.quantity || 1);
        formData.append('ajax', 'true'); // Indicate this is an AJAX request
        
        return fetch(BASE_URL + '/user/cart', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: formData
        });
    },

    /**
     * Update item quantity in cart
     */
    updateQuantity: async (payload) => {
        const formData = new URLSearchParams();
        formData.append('action', 'update');
        formData.append('cartId', payload.cartId);
        formData.append('quantity', payload.quantity);

        return fetch(BASE_URL + '/user/cart', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        });
    },

    /**
     * Remove item from cart
     */
    removeFromCart: async (payload) => {
        const formData = new URLSearchParams();
        formData.append('action', 'remove');
        formData.append('cartId', payload.cartId);

        return fetch(BASE_URL + '/user/cart', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        });
    }
};