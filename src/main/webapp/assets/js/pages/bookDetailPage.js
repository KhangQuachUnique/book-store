import {wishListApi} from "../services/WishListService.js";

function showResult(message, success = true) {
    showToast(message, success ? 'success' : 'error', 3000);
}

document.addEventListener('DOMContentLoaded', function () {
    // Add to Cart functionality
    const addToCartBtn = document.querySelector('.add-to-cart');
    if (addToCartBtn) {
        addToCartBtn.addEventListener('click', function () {
            const bookId = this.getAttribute('data-book-id');
            // TODO: Implement add to cart functionality
            alert('Add to cart functionality will be implemented soon!');
        });
    }

    // Add to Wishlist functionality
    const addToWishlistBtn = document.querySelector('.add-to-wishlist');
    if (addToWishlistBtn) {
        addToWishlistBtn.addEventListener('click', async function () {
            const bookId = this.getAttribute('data-book-id');
            const response = await wishListApi.addToWishList({itemId: bookId});
            const body = await response.json();

            if (body.success) {
                showResult(body.message, true);
            } else {
                showResult(body.message, false);
            }
        });
    }
});